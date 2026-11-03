package com.bytter.biz.payment.control;

import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.foreign.service.FexMessageService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.limit.service.EnterpriseLimitControlService;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.TranCaseCallbackDTO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资金收付交易现金流（tranType=4 付款）落地限额处理单回调处理
 * <p>
 * 委托 {@link FexCashFlowService#completeTranApprove} 执行审批通过/打回收尾
 * （status->95/-1 + 现金流 + 银企指令发送等），与付款办理/代发工资 handler 同构。
 *
 * @author zhukr
 */
@Service("fexCashFlowTranCaseCallbackHandler")
public class FexCashFlowTranCaseCallbackHandler implements ITranCaseCallbackHandler {

    @Autowired
    private FexCashFlowService fexCashFlowService;

    @Autowired
    private FexMessageService fexMessageService;

    @Autowired
    private FexTranReqService fexTranReqService;

    @Override
    public String sourceBizType() {
        return EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW;
    }

    @Override
    public void handle(TranCaseCallbackDTO dto) {
        FexCashFlowVO vo = StringUtils.isNotBlank(dto.getBusinessId())
                ? fexCashFlowService.getByIdUnion(dto.getBusinessId())
                : fexCashFlowService.getByBillNoUnion(dto.getBusinessNo());
        FexCashFlow entity = vo == null ? null : fexCashFlowService.get(vo.getId(), true);
        if (entity == null && StringUtils.isNotBlank(dto.getBusinessId())) {
            // 资金收付从主交易提交起统一以 FexTranReq.id 作为来源 ID，落地回调据此查找付款现金流。
            entity = fexCashFlowService.getFirstCashFlowByTranReqId(dto.getBusinessId(), CommonConstant.YES);
        }
        if (entity == null && StringUtils.isNotBlank(dto.getBusinessNo())) {
            // businessNo统一传交易单bill_no；兼容回调仅携带业务编号时先定位交易单，再查付款现金流。
            FexTranReqVO tranReq = fexTranReqService.getByBillNoUnion(dto.getBusinessNo());
            if (tranReq != null) {
                entity = fexCashFlowService.getFirstCashFlowByTranReqId(tranReq.getId(), CommonConstant.YES);
            }
        }
        if (entity == null) {
            throw new ParameterException("资金收付交易现金流单据不存在: "
                    + (StringUtils.isNotBlank(dto.getBusinessId()) ? dto.getBusinessId() : dto.getBusinessNo()));
        }
        if (StringUtils.isNotBlank(dto.getCorpId()) && !dto.getCorpId().equals(entity.getCorpId())) {
            throw new ParameterException("资金收付交易现金流单据不属于该单位: "
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
        if (CommonConstant.STATUE_CHECK.equals(entity.getStatus())
                && EnterpriseLimitControlService.LANDING_RESULT_PENDING.equals(entity.getLandingLimitResult())) {
            fexCashFlowService.completeTranApprove(entity.getId(), pass, approveUser);
            if (!pass) {
                // 资金收付打回事务完成后，按主交易ID幂等释放限额占用。
                fexCashFlowService.rejectAfterEnterpriseLimitApproval(entity.getId(), approveUser);
            }
            return;
        }
        // 幂等：企业端已收尾（95 审批通过，可能重复回调 / 企业端已先收尾），仅记录
        if (CommonConstant.STATUE_CHECK.equals(entity.getStatus())) {
            Integer messageType = pass
                    ? FexMessageConstant.MESSAGE_TYPE_CHECK_PASS : FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
            fexMessageService.saveMessage(entity.getId(), messageType,
                    "银行端落地限额处理单回调（幂等）", FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW, approveUser);
            return;
        }
        // 幂等：单据已是打回（-1，打回结果已生效）重复收到打回结果回调，仅记录（原会抛"不在待审批状态"，
        // 内管端按同一结果重试/重复回调时卡单）
        if (CommonConstant.STATUE_BACK.equals(entity.getStatus()) && !pass) {
            // 重复拒绝回调仍补偿释放，修复业务已打回但限额仍在途的半完成状态。
            fexCashFlowService.rejectAfterEnterpriseLimitApproval(entity.getId(), approveUser);
            fexMessageService.saveMessage(entity.getId(), FexMessageConstant.MESSAGE_TYPE_CHECK_BACK,
                    "银行端落地限额处理单回调（单据已打回，幂等）", FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW, approveUser);
            return;
        }
        // 非待审批状态不允许收尾
        if (!NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            throw new ParameterException("资金收付交易现金流单据不在待审批状态: "
                    + (StringUtils.isNotBlank(dto.getBusinessNo()) ? dto.getBusinessNo() : dto.getBusinessId()));
        }

        fexCashFlowService.completeTranApprove(entity.getId(), pass, approveUser);
        if (!pass) {
            // 普通待审批兼容路径打回后同样释放主交易限额占用。
            fexCashFlowService.rejectAfterEnterpriseLimitApproval(entity.getId(), approveUser);
        }
    }

    private ICurrentUser buildApproveUser(String approveUser, FexCashFlow entity) {
        String loginName = StringUtils.isNotBlank(approveUser) ? approveUser
                : (StringUtils.isNotBlank(entity.getUpdateBy()) ? entity.getUpdateBy() : entity.getCreateBy());
        UserDTO user = new UserDTO();
        user.setId(loginName);
        user.setLoginName(loginName);
        user.setUsername(loginName);
        return user;
    }
}
