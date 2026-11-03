package com.bytter.biz.limit.exception;

import com.bytter.biz.limit.enums.EnterpriseLimitMessageEnum;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.BusinessExceptionDetail;
import com.bytter.saas.management.exchange.api.dto.LimitHitDetailDto;
import com.bytter.workflow.enums.WorkFlowMessageEnum;

import java.util.ArrayList;
import java.util.List;

/** 批量提交限额拦截异常，携带每笔超限对应的规则快照。 */
public class PaymentLimitBatchExceededException extends BusinessException implements BusinessExceptionDetail {
    private static final long serialVersionUID = 1L;
    private final List<LimitHitDetailDto> details;

    public PaymentLimitBatchExceededException(String[] params, List<LimitHitDetailDto> details) {
        super(WorkFlowMessageEnum.WORK_FLOW_BATCH_SUBMIT_NOTICE, params);
        this.details = details == null
                ? new ArrayList<LimitHitDetailDto>() : new ArrayList<LimitHitDetailDto>(details);
    }

    public PaymentLimitBatchExceededException(List<LimitHitDetailDto> details) {
        super(EnterpriseLimitMessageEnum.LIMIT_CONTROL_AMOUNT_EXCEEDS_RULE, resolveRuleName(details));
        this.details = details == null
                ? new ArrayList<LimitHitDetailDto>() : new ArrayList<LimitHitDetailDto>(details);
    }

    private static String resolveRuleName(List<LimitHitDetailDto> details) {
        if (details == null || details.isEmpty() || details.get(0) == null) {
            return "";
        }
        LimitHitDetailDto detail = details.get(0);
        if (detail.getRuleName() != null) {
            return detail.getRuleName();
        }
        return detail.getRuleCode() == null ? "" : detail.getRuleCode();
    }

    @Override
    public List<LimitHitDetailDto> getDetail() {
        return details;
    }
}
