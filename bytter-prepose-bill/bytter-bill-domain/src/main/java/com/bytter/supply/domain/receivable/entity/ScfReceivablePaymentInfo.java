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

@Entity
@Table(name = "SCF_RECEIVABLE_PAYMENT_INFO")
public class ScfReceivablePaymentInfo extends IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 债权凭证编号 */
    private String voucherCode;

    /** 收款单号 */
    private String paymentNo;

    /** 收款审批状态 */
    private String paymentStatus;

    /** 平台ID */
    private String platformId;

    /** 平台名称 */
    private String platformName;

    /** 融资产品ID */
    private String productId;

    /** 融资产品名称 */
    private String productName;

    /** 收款单位 */
    private String receiptCompany;

    /** 收款单位ID */
    private String receiptCompanyId;

    /** 签收日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date signDate;

    /** 到期日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expireDate;

    /** 开立/转让单位 */
    private String issueTransferCompany;

    /** 开立/转让企业ID */
    private String issueTransferCompanyId;

    /** 币别ID */
    private String curId;

    /** 币别名称 */
    private String curName;

    /** 凭证金额 */
    private BigDecimal voucherAmt;

    /** 核心企业ID */
    private String coreCompanyId;

    /** 核心企业名称 */
    private String coreCompanyName;

    /** 收款账号 */
    private String recAccountNo;

    /** 收款户名 */
    private String recAccountName;

    /** 收款开户行 */
    private String recBank;

    /** 收款金额 */
    private BigDecimal recAmt;

    /** 收款日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date receiptDate;

    /** 是否有效(0：无效；1：有效) */
    private String enable;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createDate;

    /** 修改人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateDate;

    @Column(name = "VOUCHER_CODE")
    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }

    @Column(name = "PAYMENT_NO")
    public String getPaymentNo() { return paymentNo; }
    public void setPaymentNo(String paymentCode) { this.paymentNo = paymentCode; }

    @Column(name = "PAYMENT_STATUS")
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    @Column(name = "PLATFORM_ID")
    public String getPlatformId() { return platformId; }
    public void setPlatformId(String platformId) { this.platformId = platformId; }

    @Column(name = "PLATFORM_NAME")
    public String getPlatformName() { return platformName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }

    @Column(name = "PRODUCT_ID")
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    @Column(name = "PRODUCT_NAME")
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    @Column(name = "RECEIPT_COMPANY")
    public String getReceiptCompany() { return receiptCompany; }
    public void setReceiptCompany(String receiptCompany) { this.receiptCompany = receiptCompany; }

    @Column(name = "RECEIPT_COMPANY_ID")
    public String getReceiptCompanyId() { return receiptCompanyId; }
    public void setReceiptCompanyId(String receiptCompanyId) { this.receiptCompanyId = receiptCompanyId; }

    @Column(name = "SIGN_DATE")
    public Date getSignDate() { return signDate; }
    public void setSignDate(Date signDate) { this.signDate = signDate; }

    @Column(name = "EXPIRE_DATE")
    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }

    @Column(name = "ISSUE_TRANSFER_COMPANY")
    public String getIssueTransferCompany() { return issueTransferCompany; }
    public void setIssueTransferCompany(String issueTransferCompany) { this.issueTransferCompany = issueTransferCompany; }

    @Column(name = "ISSUE_TRANSFER_COMPANY_ID")
    public String getIssueTransferCompanyId() { return issueTransferCompanyId; }
    public void setIssueTransferCompanyId(String issueTransferCompanyId) { this.issueTransferCompanyId = issueTransferCompanyId; }

    @Column(name = "CUR_ID")
    public String getCurId() { return curId; }
    public void setCurId(String curId) { this.curId = curId; }

    @Column(name = "CUR_NAME")
    public String getCurName() { return curName; }
    public void setCurName(String curName) { this.curName = curName; }

    @Column(name = "VOUCHER_AMT")
    public BigDecimal getVoucherAmt() { return voucherAmt; }
    public void setVoucherAmt(BigDecimal voucherAmt) { this.voucherAmt = voucherAmt; }

    @Column(name = "CORE_COMPANY_ID")
    public String getCoreCompanyId() { return coreCompanyId; }
    public void setCoreCompanyId(String coreCompanyId) { this.coreCompanyId = coreCompanyId; }

    @Column(name = "CORE_COMPANY_NAME")
    public String getCoreCompanyName() { return coreCompanyName; }
    public void setCoreCompanyName(String coreCompanyName) { this.coreCompanyName = coreCompanyName; }

    @Column(name = "REC_ACCOUNT_NO")
    public String getRecAccountNo() { return recAccountNo; }
    public void setRecAccountNo(String recAccountNo) { this.recAccountNo = recAccountNo; }

    @Column(name = "REC_ACCOUNT_NAME")
    public String getRecAccountName() { return recAccountName; }
    public void setRecAccountName(String recAccountName) { this.recAccountName = recAccountName; }

    @Column(name = "REC_BANK")
    public String getRecBank() { return recBank; }
    public void setRecBank(String recBank) { this.recBank = recBank; }

    @Column(name = "REC_AMT")
    public BigDecimal getRecAmt() { return recAmt; }
    public void setRecAmt(BigDecimal recAmt) { this.recAmt = recAmt; }

    @Column(name = "RECEIPT_DATE")
    public Date getReceiptDate() { return receiptDate; }
    public void setReceiptDate(Date receiptDate) { this.receiptDate = receiptDate; }

    @Column(name = "ENABLE")
    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
