package com.bytter.biz.payment.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 银行端限额服务 -> 企业端 落地限额处理单（BT_TR_LIMIT_CASE）结果回调
 * <p>
 * 企业端审批通过后命中落地限额（LANDING_REQUIRED），单据暂停（status=11）等待银行处理；
 * 银行端对处理单给出结果后，通过 MQ 通知企业端，消费端按 {@link #sourceBizType}
 * 分发到对应来源业务的处理器，
 * 执行审批通过/打回收尾（status->95/-1 + 资金计划 + 现金流）。
 * <p>
 * 交互使用专用 DTO（含 来源业务类型/业务对应ID/审批状态/租户ID 等基础信息），不再使用 IWorkFlowResult。
 *
 * @author zhukr
 */
public class TranCaseCallbackDTO implements Serializable {

    /**签约客户ID（= 企业端租户ID，用于回调线程还原租户上下文）*/
    private String tenantId;
    /** 来源业务类型（BT_TR_LIMIT_CASE.SOURCE_BIZ_TYPE）：0付款办理、1资金收付、2代发工资、3费用报销。 */
    private String sourceBizType;
    /**业务对应 ID（来源业务 sourceBizId = 资金收付交易单据 ID）*/
    private String businessId;
    /**业务单号（资金收付交易 billNo）*/
    private String businessNo;
    /**单位ID*/
    private String corpId;
    /**落地处理单ID（BT_TR_LIMIT_CASE.ID）*/
    private String caseId;
    /**落地处理单号（BT_TR_LIMIT_CASE.CASE_NO）*/
    private String caseNo;
    /**审批状态（BT_TR_LIMIT_CASE.CASE_STATUS：2审批通过 / 3打回）*/
    private Integer caseStatus;
    /**处理结果（BT_TR_LIMIT_CASE.HANDLE_RESULT：A同意 / R拒绝）*/
    private String handleResult;
    /**审批人登录名*/
    private String approveUser;
    /**审批意见*/
    private String approveComment;
    /**审批时间*/
    private Date approveDate;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getSourceBizType() {
        return sourceBizType;
    }

    public void setSourceBizType(String sourceBizType) {
        this.sourceBizType = sourceBizType;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public Integer getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Integer caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public String getApproveComment() {
        return approveComment;
    }

    public void setApproveComment(String approveComment) {
        this.approveComment = approveComment;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }
}
