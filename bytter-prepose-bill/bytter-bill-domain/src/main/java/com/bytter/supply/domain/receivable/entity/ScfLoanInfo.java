package com.bytter.supply.domain.receivable.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfLoanInfo
 * @Description: 融资业务主信息
 * @since 2026/7/3
 */
@Entity
@Table(name = "SCF_LOAN_INFO")
public class ScfLoanInfo extends IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 融资单号(RZ+年月日+3位流水号) */
    private String loanNo;

    /** 融资企业id */
    private String loanCompanyId;

    /** 融资企业名称 */
    private String loanCompanyName;

    /** 债权凭证编号 */
    private String voucherCode;

    /** 核心企业 */
    private String coreCompany;

    /** 核心企业id */
    private String coreCompanyId;

    /** 平台ID */
    private String platformId;
    /** 平台名称 */
    private String platformName;

    /** 资金方 */
    private String funderId;

    /** 资金方名称 */
    private String funderName;

    /** 融资产品id */
    private String productId;

    /** 融资产品名称 */
    private String productName;

    /** 付息方式(1:卖方付息，2:买方付息，默认卖方付息) */
    private String interestMode;

    /** 币别ID */
    private String curId;

    /** 币别名称 */
    private String curName;

    /** 融资申请日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date loanApplyDate;

    /** 融资金额 */
    private BigDecimal loanAmt;

    /** 放款日(默认系统当前日期) */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date loanLendDate;

    /** 实际放款日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date loanActualDate;

    /** 到期日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date loanDueDate;

    /** 期限(到期日-放款日) 天 */
    private String loanDeadline;

    /** 融资利率 */
    private BigDecimal loanRate;

    /** 利息金额(融资金额*融资利率) */
    private BigDecimal loanInterestAmt;

    /** 手续费率 */
    private BigDecimal loanHandFeeRate;

    /** 手续费(融资金额*手续费率) */
    private BigDecimal loanHandFeeAmt;

    /** 服务费率 */
    private BigDecimal loanServiceFeeRate;

    /** 服务费(融资金额*服务费率) */
    private BigDecimal loanServiceFeeAmt;

    /** 融资成本(利息金额+手续费+服务费) */
    private BigDecimal loanCostAmt;

    /** 融资到账金额(融资金额-融资成本) */
    private BigDecimal loanActualAmt;

    /** 收款账号 */
    private String loanPaymentAccount;

    /** 收款开户名 */
    private String loanPaymentAccountName;

    /** 收款开户行 */
    private String loanPaymentAccountBank;

    /** 应收ID */
    private String receivableId;

    /** 融资审批状态(DRAFT:暂存; APPROVAL_ING:审批中; REJECTED:打回; APPROVAL_PASS:审批通过) */
    private String loanStatus;

    /** 线上线下标识(0:线下；1:线上) */
    private String onlineSign;

    /**是否有效(0：无效；1：有效)*/
    private String enable;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createDate;

    /** 修改人 */
    private String updateBy;

    /** 修改时间 */
    private Date updateDate;

    @Column(name = "LOAN_NO")
    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    @Column(name = "LOAN_COMPANY_ID")
    public String getLoanCompanyId() {
        return loanCompanyId;
    }

    public void setLoanCompanyId(String loanCompanyId) {
        this.loanCompanyId = loanCompanyId;
    }

    @Column(name = "LOAN_COMPANY_NAME")
    public String getLoanCompanyName() {
        return loanCompanyName;
    }

    public void setLoanCompanyName(String loanCompanyName) {
        this.loanCompanyName = loanCompanyName;
    }

    @Column(name = "VOUCHER_CODE")
    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    @Column(name = "CORE_COMPANY")
    public String getCoreCompany() {
        return coreCompany;
    }

    public void setCoreCompany(String coreCompany) {
        this.coreCompany = coreCompany;
    }

    @Column(name = "CORE_COMPANY_ID")
    public String getCoreCompanyId() {
        return coreCompanyId;
    }

    public void setCoreCompanyId(String coreCompanyId) {
        this.coreCompanyId = coreCompanyId;
    }

    @Column(name = "PLATFORM_ID")
    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    @Column(name = "FUNDER_NAME")
    public String getFunderName() {
        return funderName;
    }

    public void setFunderName(String funderName) {
        this.funderName = funderName;
    }

    @Column(name = "FUNDER_ID")
    public String getFunderId() {
        return funderId;
    }

    public void setFunderId(String funderId) {
        this.funderId = funderId;
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

    @Column(name = "INTEREST_MODE")
    public String getInterestMode() {
        return interestMode;
    }

    public void setInterestMode(String interestMode) {
        this.interestMode = interestMode;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "LOAN_APPLY_DATE")
    public Date getLoanApplyDate() {
        return loanApplyDate;
    }

    public void setLoanApplyDate(Date loanApplyDate) {
        this.loanApplyDate = loanApplyDate;
    }

    @Column(name = "LOAN_AMT")
    public BigDecimal getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(BigDecimal loanAmt) {
        this.loanAmt = loanAmt;
    }

    @Column(name = "LOAN_LEND_DATE")
    public Date getLoanLendDate() {
        return loanLendDate;
    }

    public void setLoanLendDate(Date loanLendDate) {
        this.loanLendDate = loanLendDate;
    }

    @Column(name = "LOAN_ACTUAL_DATE")
    public Date getLoanActualDate() {
        return loanActualDate;
    }

    public void setLoanActualDate(Date loanActualDate) {
        this.loanActualDate = loanActualDate;
    }

    @Column(name = "LOAN_DUE_DATE")
    public Date getLoanDueDate() {
        return loanDueDate;
    }

    public void setLoanDueDate(Date loanDueDate) {
        this.loanDueDate = loanDueDate;
    }

    @Column(name = "LOAN_DEADLINE")
    public String getLoanDeadline() {
        return loanDeadline;
    }

    public void setLoanDeadline(String loanDeadline) {
        this.loanDeadline = loanDeadline;
    }

    @Column(name = "LOAN_RATE")
    public BigDecimal getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(BigDecimal loanRate) {
        this.loanRate = loanRate;
    }

    @Column(name = "LOAN_INTEREST_AMT")
    public BigDecimal getLoanInterestAmt() {
        return loanInterestAmt;
    }

    public void setLoanInterestAmt(BigDecimal loanInterestAmt) {
        this.loanInterestAmt = loanInterestAmt;
    }

    @Column(name = "LOAN_HAND_FEE_RATE")
    public BigDecimal getLoanHandFeeRate() {
        return loanHandFeeRate;
    }

    public void setLoanHandFeeRate(BigDecimal loanHandFeeRate) {
        this.loanHandFeeRate = loanHandFeeRate;
    }

    @Column(name = "LOAN_HAND_FEE_AMT")
    public BigDecimal getLoanHandFeeAmt() {
        return loanHandFeeAmt;
    }

    public void setLoanHandFeeAmt(BigDecimal loanHandFeeAmt) {
        this.loanHandFeeAmt = loanHandFeeAmt;
    }

    @Column(name = "LOAN_SERVICE_FEE_RATE")
    public BigDecimal getLoanServiceFeeRate() {
        return loanServiceFeeRate;
    }

    public void setLoanServiceFeeRate(BigDecimal loanServiceFeeRate) {
        this.loanServiceFeeRate = loanServiceFeeRate;
    }

    @Column(name = "LOAN_SERVICE_FEE_AMT")
    public BigDecimal getLoanServiceFeeAmt() {
        return loanServiceFeeAmt;
    }

    public void setLoanServiceFeeAmt(BigDecimal loanServiceFeeAmt) {
        this.loanServiceFeeAmt = loanServiceFeeAmt;
    }

    @Column(name = "LOAN_COST_AMT")
    public BigDecimal getLoanCostAmt() {
        return loanCostAmt;
    }

    public void setLoanCostAmt(BigDecimal loanCostAmt) {
        this.loanCostAmt = loanCostAmt;
    }

    @Column(name = "LOAN_ACTUAL_AMT")
    public BigDecimal getLoanActualAmt() {
        return loanActualAmt;
    }

    public void setLoanActualAmt(BigDecimal loanActualAmt) {
        this.loanActualAmt = loanActualAmt;
    }

    @Column(name = "LOAN_PAYMENT_ACCOUNT")
    public String getLoanPaymentAccount() {
        return loanPaymentAccount;
    }

    public void setLoanPaymentAccount(String loanPaymentAccount) {
        this.loanPaymentAccount = loanPaymentAccount;
    }

    @Column(name = "LOAN_PAYMENT_ACCOUNT_NAME")
    public String getLoanPaymentAccountName() {
        return loanPaymentAccountName;
    }

    public void setLoanPaymentAccountName(String loanPaymentAccountName) {
        this.loanPaymentAccountName = loanPaymentAccountName;
    }

    @Column(name = "LOAN_PAYMENT_ACCOUNT_BANK")
    public String getLoanPaymentAccountBank() {
        return loanPaymentAccountBank;
    }

    public void setLoanPaymentAccountBank(String loanPaymentAccountBank) {
        this.loanPaymentAccountBank = loanPaymentAccountBank;
    }

    @Column(name = "RECEIVABLE_ID")
    public String getReceivableId() {
        return receivableId;
    }

    public void setReceivableId(String receivableId) {
        this.receivableId = receivableId;
    }

    @Column(name = "LOAN_STATUS")
    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    @Column(name = "ONLINE_SIGN")
    public String getOnlineSign() {
        return onlineSign;
    }

    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }

    @Column(name = "ENABLE")
    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
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
