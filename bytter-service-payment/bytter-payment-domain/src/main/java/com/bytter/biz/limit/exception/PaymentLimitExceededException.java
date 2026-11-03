package com.bytter.biz.limit.exception;

import com.bytter.biz.limit.enums.EnterpriseLimitMessageEnum;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.BusinessExceptionDetail;
import com.bytter.saas.management.exchange.api.dto.LimitHitDetailDto;

/** 支付限额拦截异常，携带内管返回的当前规则快照。 */
public class PaymentLimitExceededException extends BusinessException implements BusinessExceptionDetail {
    private static final long serialVersionUID = 1L;
    private final LimitHitDetailDto detail;

    public PaymentLimitExceededException(String[] params, LimitHitDetailDto detail) {
        super(EnterpriseLimitMessageEnum.LIMIT_CONTROL_AMOUNT_EXCEEDS_RULE, params);
        this.detail = detail;
    }

    @Override
    public LimitHitDetailDto getDetail() {
        return detail;
    }
}
