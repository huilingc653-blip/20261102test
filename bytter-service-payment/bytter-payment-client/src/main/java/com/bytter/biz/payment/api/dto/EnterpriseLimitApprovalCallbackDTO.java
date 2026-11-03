package com.bytter.biz.payment.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 银行端落地限额审批结果回调。
 */
@Data
public class EnterpriseLimitApprovalCallbackDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 银行端事件唯一号，重试时保持不变。 */
    private String eventId;
    /** 企业租户ID，同时作为签约客户ID。 */
    private String tenantId;
    /** 银行机构ID，对应SYS_TENANT_MANAGER.CORP_ID。 */
    private String bankOrgId;
    /** 业务单据类型：NIS_BILLHEAD/FEX_CASH_FLOW。 */
    private String sourceBizType;
    /** 业务单据ID。 */
    private String sourceBizId;
    /** 付款尝试版本，防止旧审批结果覆盖新尝试。 */
    private Integer attemptVersion;
    /** 审批结果：A同意/R拒绝。 */
    private String approvalResult;
    /** 银行端落地处理单号。 */
    private String caseNo;
    /** 审批意见。 */
    private String approvalDesc;
    /** 银行端审批人ID。 */
    private String approvedBy;
    /** 银行端审批时间。 */
    private Date approvedTime;
}
