package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.EnterpriseLimitApprovalCallbackDTO;
import com.bytter.biz.payment.api.dto.EnterpriseLimitCallbackResultDTO;

/**
 * 银行端调用的企业端落地限额审批结果回调接口。
 */
public interface IEnterpriseLimitCallbackService {

    EnterpriseLimitCallbackResultDTO approvalResult(EnterpriseLimitApprovalCallbackDTO callback);
}
