package com.bytter.biz.payment.control;

import com.bytter.biz.limit.service.EnterpriseLimitControlService;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.netbank.vo.NisBillheadVO;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.TranCaseCallbackDTO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 付款办理（pay）落地限额处理单回调处理
 * <p>
 * 委托 {@link NisBillheadService#completeTranApprove} 执行审批通过/打回收尾
 * （status->95/-1 + 资金计划 + 指令发送等），与资金收付交易 handler 同构。
 *
 * @author zhukr
 */
@Service("nisBillheadTranCaseCallbackHandler")
public class NisBillheadTranCaseCallbackHandler implements ITranCaseCallbackHandler {

    @Autowired
    private NisBillheadService nisBillheadService;

    @Autowired
    private NisMessageService nisMessageService;

    @Override
    public String sourceBizType() {
        return EnterpriseLimitControlService.BIZ_TYPE_NIS_BILLHEAD;
    }

    @Override
    public void handle(TranCaseCallbackDTO dto) {
        NisBillhead entity = nisBillheadService.get(dto.getBusinessId(), true);
        if (entity == null) {
            throw new ParameterException("付款办理单据不存在: "
                    + (StringUtils.isNotBlank(dto.getBusinessId()) ? dto.getBusinessId() : dto.getBusinessNo()));
        }
        if (StringUtils.isNotBlank(dto.getCorpId()) && !dto.getCorpId().equals(entity.getCorpId())) {
            throw new ParameterException("付款办理单据不属于该单位: "
                    + (StringUtils.isNotBlank(dto.getBusinessNo()) ? dto.getBusinessNo() : dto.getBusinessId()));
        }
        // 这里审批人 其实就是  更新的人 updateBy
        // 处理结果是最终付款决策：明确返回 A/R 时以其为准；旧消息未带处理结果时才兼容 caseStatus。
        boolean pass = StringUtils.isNotBlank(dto.getHandleResult())
                ? EnterpriseLimitControlService.LANDING_RESULT_AGREE.equals(dto.getHandleResult())
                : dto.getCaseStatus() != null && Integer.valueOf(2).equals(dto.getCaseStatus());
        // 内管端回调可能不传审批人（approveUser 为空），取单据 updateBy（兜底 createBy）
        ICurrentUser approveUser = buildApproveUser(dto.getApproveUser(), entity);

        // 落地预置单回调时按单据特征分流收尾（占用与落地处理单均同 NIS_BILLHEAD 维度、共用本回调）：
        // 自动支付策略预置单（autoPayBill 命中落地，isAutoPay=YES）→ completeTranApproveAutoPay——
        // 通过=直接生成自动支付指令（单据保持 95），打回=95->-1；非费用报销付款单同步释放占用；
        // 人工审批落地预置单（flowCompleted 命中落地，markApprovePassForLanding 提前置 95 + 落地标识=0）→
        // completeTranApprove——条件翻转 A/R（互斥）后按单据已 95 直接走人工收尾内容。
        if (NetbankConstant.STATUE_CHECK.equals(entity.getStatus())
                && EnterpriseLimitControlService.LANDING_RESULT_PENDING.equals(entity.getLandingLimitResult())) {
            if (CommonConstant.YES.equals(entity.getIsAutoPay())) {
                nisBillheadService.completeTranApproveAutoPay(entity.getId(), pass, approveUser);
            } else {
                nisBillheadService.completeTranApprove(entity.getId(), pass, approveUser);
            }
            if (!pass) {
                releaseEnterpriseLimitAfterRejectIfNeeded(entity, approveUser);
            }
            return;
        }
        // 幂等：企业端已收尾（95审批通过/100记账通过，可能重复回调 / 企业端已先收尾），仅记录
        if (NetbankConstant.STATUE_CHECK.equals(entity.getStatus())
                || NetbankConstant.STATUE_TALLY_CHECKPASS.equals(entity.getStatus())) {
            Integer messageType = pass
                    ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
            nisMessageService.saveMessage(entity.getId(), messageType,
                    "银行端落地限额处理单回调（幂等）", NisMessageConstant.BUS_TYPE_PAY, approveUser);
            return;
        }
        // 幂等：单据已是打回（-1，打回结果已生效）重复收到打回结果回调，仅记录（原会抛"不在待审批状态"，
        // 内管端按同一结果重试/重复回调时卡单）
        if (NetbankConstant.STATUE_BACK.equals(entity.getStatus()) && !pass) {
            // 非费用报销单重复拒绝时补偿释放；费用报销保留占用等待报销处理。
            releaseEnterpriseLimitAfterRejectIfNeeded(entity, approveUser);
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_CHECK_BACK,
                    "银行端落地限额处理单回调（单据已打回，幂等）", NisMessageConstant.BUS_TYPE_PAY, approveUser);
            return;
        }
        // 非待审批状态不允许收尾
        if (!NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            throw new ParameterException("付款办理单据不在待审批状态: "
                    + (StringUtils.isNotBlank(dto.getBusinessNo()) ? dto.getBusinessNo() : dto.getBusinessId()));
        }

        // completeTranApprove 收尾后已统一补记审批通过/打回日志（CHECK_PASS/CHECK_BACK，见 NisBillheadService.completeTranApprove），
        // 此处不再重复记
        nisBillheadService.completeTranApprove(entity.getId(), pass, approveUser);
        if (!pass) {
            releaseEnterpriseLimitAfterRejectIfNeeded(entity, approveUser);
        }
    }

    /**
     * 落地审批拒绝后按业务回退落点处理限额。
     * 费用报销汇总付款单保留占用，由费用报销处理的删除或打回动作释放。
     */
    private void releaseEnterpriseLimitAfterRejectIfNeeded(NisBillhead entity, ICurrentUser user) {
        if (NetbankConstant.BILLFROM_2.equals(entity.getBillFrom())) {
            return;
        }
        // 普通付款单打回事务完成后，幂等释放当前付款尝试的限额占用。
        nisBillheadService.releaseEnterpriseLimitForBillhead(entity.getId(), user);
    }

    private ICurrentUser buildApproveUser(String approveUser, NisBillhead entity) {
        String loginName = StringUtils.isNotBlank(approveUser) ? approveUser
                : (StringUtils.isNotBlank(entity.getUpdateBy()) ? entity.getUpdateBy() : entity.getCreateBy());
        UserDTO user = new UserDTO();
        user.setId(loginName);
        user.setLoginName(loginName);
        user.setUsername(loginName);
        return user;
    }
}
