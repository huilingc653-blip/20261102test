package com.bytter.supply.domain.payable.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "SCF_PAYABLE_VOUCHER", schema = "")
@SuppressWarnings("serial")
public class ScfPayableVoucher extends IdEntity implements Serializable {
    /**
     * 债权凭证模版id
     */
    private String templateId;
    /**
     * 单据编号
     */
    private String voucherNo;
    /**
     * 债权凭证编码
     */
    private String voucherCode;
    /**
     * 预留 可后续添加模板版本id
     */
    private String versionId;
    /**
     * 平台
     */
    private String platformId;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * 企业ID
     */
    private String corpId;
    /**
     * 开立日期
     */
    private Date openDate;
    /**
     * 到期日期
     */
    private Date expireDate;
    /**
     * 核心企业
     */
    private String coreCorp;
    /**
     * 核心企业名称
     */
    private String coreCorpName;
    /**
     * 币种ID
     */
    private String curId;
    /**
     * 凭证金额
     */
    private BigDecimal voucherMoney;
    /**
     * 供应商ID
     */
    private String supplyId;
    /**
     * 供应商名称
     */
    private String supplyName;
    /**
     * 付款账户
     */
    private String receiveAccount;
    /**
     * 付款银行
     */
    private String receiveBank;
    /**
     * 线上签章
     */
    private String onlineSign;
    /**
     * 额度编码
     */
    private String quotaCode;
    /**
     * 额度名称
     */
    private String quotaName;
    /**
     * 额度余额
     */
    private BigDecimal quotaBalance;
    /**
     * 合同编码
     */
    private String contractCode;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同金额
     */
    private BigDecimal contractMoney;
    /**
     * 合同日期
     */
    private Date contractDate;
    /**
     * 合同文件
     */
    private String contractFile;
    /**
     * 发票文件
     */
    private String invoiceFile;
    /**
     * 单据来源
     */
    private String voucherSource;
    /**
     * 融资付息方式(1:卖方付息,2:买方付息)
     */
    private String interestMode;
    /**
     * 承诺付款日
     */
    private Date promisePayDate;
    /**
     * 供应商统一社会信用代码
     */
    private String supplyCreditCode;

    /** 凭证业务状态 */
    private String voucherStatus;

    /** 审批状态 */
    private String approvalStatus;

    /** 关联付款单ID（付款单审批通过后写入，用于选票排除已关联凭证） */
    private String payId;

    /** 关联付款单号（付款单审批通过后写入，用于选票排除已关联凭证） */
    private String payNo;

    /** 作废原因（作废操作时录入） */
    private String cancelReason;

    /** 作废时间（作废操作时写入） */
    private Date cancelDate;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;

    @Column(name = "VOUCHER_CODE")
    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    @Column(name = "SUPPLY_NAME")
    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    @Column(name = "VOUCHER_NO")
    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    @Column(name = "TEMPLATE_ID")
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Column(name = "VERSION_ID")
    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @Column(name = "PLATFORM_ID")
    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    @Column(name = "PRODUCT_ID")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "OPEN_DATE")
    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    @Column(name = "EXPIRE_DATE")
    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    @Column(name = "CORE_CORP")
    public String getCoreCorp() {
        return coreCorp;
    }

    public void setCoreCorp(String coreCorp) {
        this.coreCorp = coreCorp;
    }

    @Column(name = "CORE_CORP_NAME")
    public String getCoreCorpName() {
        return coreCorpName;
    }

    public void setCoreCorpName(String coreCorpName) {
        this.coreCorpName = coreCorpName;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "VOUCHER_SOURCE")
    public String getVoucherSource() {
        return voucherSource;
    }

    public void setVoucherSource(String voucherSource) {
        this.voucherSource = voucherSource;
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

    @Column(name = "SUPPLY_CREDIT_CODE")
    public String getSupplyCreditCode() {
        return supplyCreditCode;
    }

    public void setSupplyCreditCode(String supplyCreditCode) {
        this.supplyCreditCode = supplyCreditCode;
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

    @Column(name = "PAY_ID")
    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    @Column(name = "PAY_NO")
    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    @Column(name = "CANCEL_REASON")
    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    @Column(name = "CANCEL_DATE")
    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    @Column(name = "VOUCHER_MONEY")
    public BigDecimal getVoucherMoney() {
        return voucherMoney;
    }

    public void setVoucherMoney(BigDecimal voucherMoney) {
        this.voucherMoney = voucherMoney;
    }

    @Column(name = "SUPPLY_ID")
    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }
    @Column(name = "RECEIVE_ACCOUNT")
    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    @Column(name = "RECEIVE_BANK")
    public String getReceiveBank() {
        return receiveBank;
    }

    public void setReceiveBank(String receiveBank) {
        this.receiveBank = receiveBank;
    }

    @Column(name = "ONLINE_SIGN")
    public String getOnlineSign() {
        return onlineSign;
    }

    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }

    @Column(name = "QUOTA_CODE")
    public String getQuotaCode() {
        return quotaCode;
    }

    public void setQuotaCode(String quotaCode) {
        this.quotaCode = quotaCode;
    }

    @Column(name = "QUOTA_NAME")
    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName;
    }

    @Column(name = "QUOTA_BALANCE")
    public BigDecimal getQuotaBalance() {
        return quotaBalance;
    }

    public void setQuotaBalance(BigDecimal quotaBalance) {
        this.quotaBalance = quotaBalance;
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

    @Column(name = "CONTRACT_MONEY")
    public BigDecimal getContractMoney() {
        return contractMoney;
    }

    public void setContractMoney(BigDecimal contractMoney) {
        this.contractMoney = contractMoney;
    }

    @Column(name = "CONTRACT_DATE")
    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    @Column(name = "CONTRACT_FILE")
    public String getContractFile() {
        return contractFile;
    }

    public void setContractFile(String contractFile) {
        this.contractFile = contractFile;
    }

    @Column(name = "INVOICE_FILE")
    public String getInvoiceFile() {
        return invoiceFile;
    }

    public void setInvoiceFile(String invoiceFile) {
        this.invoiceFile = invoiceFile;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
