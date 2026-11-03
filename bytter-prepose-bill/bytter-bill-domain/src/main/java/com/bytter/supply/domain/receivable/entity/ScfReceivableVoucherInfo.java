package com.bytter.supply.domain.receivable.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableVoucherInfo
 * @Description: 应收债权凭证信息
 * @since 2026/6/30
 */
@Entity
@Table(name = "SCF_RECEIVABLE_VOUCHER_INFO")
public class ScfReceivableVoucherInfo extends IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 凭证编号 */
    private String voucherNo;

    /** 凭证来源 */
    private String voucherSource;

    /** 凭证编码 */
    private String voucherCode;

    /** 凭证金额 */
    private BigDecimal voucherAmt;

    /** 平台ID */
    private String platformId;

    /** 平台名称 */
    private String platformName;

    /** 产品ID */
    private String productId;

    /** 产品名称 */
    private String productName;

    /** 单位代码 */
    private String corpId;

    /** 单位名称 */
    private String corpName;

    /** 签约日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date signDate;

    /** 签收操作('签收',拒签,'作废') */
    private String signOperation;

    /** 凭证类型 */
    private String voucherType;

    /** 到期日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expireDate;

    /** 凭证业务状态 */
    private String voucherStatus;

    /** 审批状态 */
    private String approvalStatus;

    /** 作废时间 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelDate;

    /** 作废原因 */
    private String cancelReason;

    /** 核心企业ID */
    private String coreCompanyId;

    /** 核心企业 */
    private String coreCompanyName;

    /** 币别ID */
    private String curId;

    /** 币别名称 */
    private String curName;

    /** 开立/转让 企业 */
    private String issueTransferCompany;

    /** 开立/转让 企业id */
    private String issueTransferCompanyId;

    /** 原始凭证编号 */
    private String sourceVoucherCode;

    /** 前置凭证编号 */
    private String forehandVoucherCode;

    /** 线上签署标识 */
    private String onlineSign;

    /** 合同编号 */
    private String contractCode;

    /** 合同名称 */
    private String contractName;

    /** 合同金额 */
    private BigDecimal contractAmt;

    /** 合同日期 */
    private Date contractDate;

    /** 合同附件ID */
    private String contractFileId;

    /** 发票附件ID */
    private String invoiceFileId;

    /** 融资付息方式(1:卖方付息,2:买方付息) */
    private String interestMode;

    /** 承诺付款日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date promisePayDate;

    /** 流转单位 */
    private String transferCompany;

    /** 流转单位id */
    private String transferCompanyId;

    /** 是否允许拆分(0:否, 1:是) */
    private String allowSplit;

    /** 创建时间 */
    private Date createDate;


    @Column(name = "VOUCHER_NO")
    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    @Column(name = "VOUCHER_SOURCE")
    public String getVoucherSource() {
        return voucherSource;
    }

    public void setVoucherSource(String voucherSource) {
        this.voucherSource = voucherSource;
    }

    @Column(name = "VOUCHER_CODE")
    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    @Column(name = "VOUCHER_AMT")
    public BigDecimal getVoucherAmt() {
        return voucherAmt;
    }

    public void setVoucherAmt(BigDecimal voucherAmt) {
        this.voucherAmt = voucherAmt;
    }

    @Column(name = "PLATFORM_ID")
    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    @Column(name = "PLATFORM_NAME")
    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Column(name = "PRODUCT_ID")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    @Column(name = "SIGN_DATE")
    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    @Column(name = "SIGN_OPERATION")
    public String getSignOperation() {
        return signOperation;
    }

    public void setSignOperation(String signOperation) {
        this.signOperation = signOperation;
    }

    @Column(name = "VOUCHER_TYPE")
    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    @Column(name = "EXPIRE_DATE")
    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    @Column(name = "VOUCHER_STATUS")
    public String getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(String voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    @Column(name = "APPROVAL_STATUS")
    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    @Column(name = "CANCEL_DATE")
    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    @Column(name = "CANCEL_REASON")
    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    @Column(name = "CORE_COMPANY_ID")
    public String getCoreCompanyId() {
        return coreCompanyId;
    }

    public void setCoreCompanyId(String coreCompanyId) {
        this.coreCompanyId = coreCompanyId;
    }

    @Column(name = "CORE_COMPANY_NAME")
    public String getCoreCompanyName() {
        return coreCompanyName;
    }

    public void setCoreCompanyName(String coreCompanyName) {
        this.coreCompanyName = coreCompanyName;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    @Column(name = "ISSUE_TRANSFER_COMPANY")
    public String getIssueTransferCompany() {
        return issueTransferCompany;
    }

    public void setIssueTransferCompany(String issueTransferCompany) {
        this.issueTransferCompany = issueTransferCompany;
    }

    @Column(name = "ISSUE_TRANSFER_COMPANY_ID")
    public String getIssueTransferCompanyId() {
        return issueTransferCompanyId;
    }

    public void setIssueTransferCompanyId(String issueTransferCompanyId) {
        this.issueTransferCompanyId = issueTransferCompanyId;
    }

    @Column(name = "SOURCE_VOUCHER_CODE")
    public String getSourceVoucherCode() {
        return sourceVoucherCode;
    }

    public void setSourceVoucherCode(String sourceVoucherCode) {
        this.sourceVoucherCode = sourceVoucherCode;
    }

    @Column(name = "FOREHAND_VOUCHER_CODE")
    public String getForehandVoucherCode() {
        return forehandVoucherCode;
    }

    public void setForehandVoucherCode(String forehandVoucherNo) {
        this.forehandVoucherCode = forehandVoucherNo;
    }

    @Column(name = "ONLINE_SIGN")
    public String getOnlineSign() {
        return onlineSign;
    }

    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }

    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Column(name = "CONTRACT_NAME")
    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    @Column(name = "CONTRACT_AMT")
    public BigDecimal getContractAmt() {
        return contractAmt;
    }

    public void setContractAmt(BigDecimal contractAmt) {
        this.contractAmt = contractAmt;
    }

    @Column(name = "CONTRACT_DATE")
    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    @Column(name = "CONTRACT_FILE_ID")
    public String getContractFileId() {
        return contractFileId;
    }

    public void setContractFileId(String contractFileId) {
        this.contractFileId = contractFileId;
    }

    @Column(name = "INVOICE_FILE_ID")
    public String getInvoiceFileId() {
        return invoiceFileId;
    }

    public void setInvoiceFileId(String invoiceFileId) {
        this.invoiceFileId = invoiceFileId;
    }

    @Column(name = "INTEREST_MODE")
    public String getInterestMode() {
        return interestMode;
    }

    public void setInterestMode(String interestMode) {
        this.interestMode = interestMode;
    }

    @Column(name = "PROMISE_PAY_DATE")
    public Date getPromisePayDate() {
        return promisePayDate;
    }

    public void setPromisePayDate(Date promisePayDate) {
        this.promisePayDate = promisePayDate;
    }

    @Column(name = "TRANSFER_COMPANY")
    public String getTransferCompany() {
        return transferCompany;
    }

    public void setTransferCompany(String transferCompany) {
        this.transferCompany = transferCompany;
    }

    @Column(name = "TRANSFER_COMPANY_ID")
    public String getTransferCompanyId() {
        return transferCompanyId;
    }

    public void setTransferCompanyId(String transferCompanyId) {
        this.transferCompanyId = transferCompanyId;
    }

    @Column(name = "ALLOW_SPLIT")
    public String getAllowSplit() {
        return allowSplit;
    }

    public void setAllowSplit(String allowSplit) {
        this.allowSplit = allowSplit;
    }

    @Column(name = "CREATE_DATE", insertable = false, updatable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
