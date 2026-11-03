package com.bytter.biz.payment.control;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.limit.service.EnterpriseLimitControlService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.TranCaseCallbackDTO;
import com.bytter.biz.wage.entity.NisWage;
import com.bytter.biz.wage.service.NisWageService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 代发工资（wage）落地限额处理单回调处理
 * <p>
 * 委托 {@link NisWageService#completeTranApprove} 执行审批通过/打回收尾
 * （status->95/-1 + 资金计划 + 指令发送等），与资金收付交易/付款办理 handler 同构。
 *
 * @author zhukr
 */
@Service("nisWageTranCaseCallbackHandler")
public class NisWageTranCaseCallbackHandler implements ITranCaseCallbackHandler {

    @Autowired
    private NisWageService nisWageService;

    @Autowired
    private NisMessageService nisMessageService;

    @Override
    public String sourceBizType() {
        return EnterpriseLimitControlService.BIZ_TYPE_NIS_WAGE;
    }

    @Override
    public void handle(TranCaseCallbackDTO dto) {
        NisWage entity = StringUtils.isNotBlank(dto.getBusinessId())
                ? nisWageService.get(dto.getBusinessId(), true)
                : nisWageService.getByWageNo(dto.getBusinessNo());
        if (entity == null) {
            throw new ParameterException("代发工资单据不存在: "
                    + (StringUtils.isNotBlank(dto.getBusinessId()) ? dto.getBusinessId() : dto.getBusinessNo()));
        }
        if (StringUtils.isNotBlank(dto.getCorpId()) && !dto.getCorpId().equals(entity.getCorpId())) {
            throw new ParameterException("代发工资单据不属于该单位: "
                    + (StringUtils.isNotBlank(dto.getBusinessNo()) ? dto.getBusinessNo() : dto.getBusinessId()));
        }
        // 这里审批人 其实就是 更新的人 updateBy
        // 处理结果是最终付款决策：明确返回 A/R 时以其为准；旧消息未带处理结果时才兼容 caseStatus。
        boolean pass = StringUtils.isNotBlank(dto.getHandleResult())
                ? EnterpriseLimitControlService.LANDING_RESULT_AGREE.equals(dto.getHandleResult())
                : dto.getCaseStatus() != null && Integer.valueOf(2).equals(dto.getCaseStatus());
        // 内管端回调可能不传审批人（approveUser 为空），取单据 updateBy（兜底 createBy）
        ICurrentUser approveUser = buildApproveUser(dto.getApproveUser(), entity);

        // 落地预置单（企业端 flowCompleted 命中落地时 markApprovePassForLanding 已把单据提前置 95 + 落地标识=0，
        // 审批内容未执行）回调时：completeTranApprove 内部条件翻转 A/R（互斥）后按单据已 95 直接收尾。
        if (NetbankConstant.STATUE_CHECK.equals(entity.getStatus())
                && EnterpriseLimitControlService.LANDING_RESULT_PENDING.equals(entity.getLandingLimitResult())) {
            nisWageService.completeTranApprove(entity.getId(), pass, approveUser);
            if (!pass) {
                // 落地限额拒绝已完成工资单打回后，释放本次付款尝试的金额和笔数占用。
                nisWageService.releaseEnterpriseLimitForWage(entity.getId(), approveUser);
            }
            return;
        }
        // 幂等：企业端已收尾（95 审批通过，可能重复回调 / 企业端已先收尾），仅记录
        if (NetbankConstant.STATUE_CHECK.equals(entity.getStatus())) {
            Integer messageType = pass
                    ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
            nisMessageService.saveMessage(entity.getId(), messageType,
                    "银行端落地限额处理单回调（幂等）", NisMessageConstant.BUS_TYPE_WAGE, approveUser);
            return;
        }
        // 幂等：单据已是打回（-1，打回结果已生效）重复收到打回结果回调，仅记录（原会抛"不在待审批状态"，
        // 内管端按同一结果重试/重复回调时卡单）
        if (NetbankConstant.STATUE_BACK.equals(entity.getStatus()) && !pass) {
            // 释放接口幂等：重复拒绝回调仍补偿释放，修复“业务已打回但限额仍在途”。
            nisWageService.releaseEnterpriseLimitForWage(entity.getId(), approveUser);
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_CHECK_BACK,
                    "银行端落地限额处理单回调（单据已打回，幂等）", NisMessageConstant.BUS_TYPE_WAGE, approveUser);
            return;
        }
        // 非待审批状态不允许收尾
        if (!NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            throw new ParameterException("代发工资单据不在待审批状态: "
                    + (StringUtils.isNotBlank(dto.getBusinessNo()) ? dto.getBusinessNo() : dto.getBusinessId()));
        }

        // completeTranApprove -> approvePassEnd 内部已记审批通过/打回日志（CHECK_PASS/CHECK_BACK），此处不再重复记
        nisWageService.completeTranApprove(entity.getId(), pass, approveUser);
        if (!pass) {
            // 普通待审批兼容路径打回后同样释放工资单限额占用。
            nisWageService.releaseEnterpriseLimitForWage(entity.getId(), approveUser);
        }
    }

    private ICurrentUser buildApproveUser(String approveUser, NisWage entity) {
        String loginName = StringUtils.isNotBlank(approveUser) ? approveUser
                : (StringUtils.isNotBlank(entity.getUpdateBy()) ? entity.getUpdateBy() : entity.getCreateBy());
        UserDTO user = new UserDTO();
        user.setId(loginName);
        user.setLoginName(loginName);
        user.setUsername(loginName);
        return user;
    }
}
