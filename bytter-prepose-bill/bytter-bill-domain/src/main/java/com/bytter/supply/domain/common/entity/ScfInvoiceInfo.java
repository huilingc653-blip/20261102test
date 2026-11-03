package com.bytter.supply.domain.common.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "SCF_INVOICE_INFO")
public class ScfInvoiceInfo extends IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 发票类型 */
    private String invoiceType;

    /** 发票号码 */
    private String invoiceNumber;

    /** 发票代码 */
    private String invoiceCode;

    /** 开票日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date billDate;

    /** 币种名称 */
    private String curName;

    /** 币种 */
    private String curId;

    /** 价税合计 */
    private BigDecimal totalAmt;

    /** 税率 */
    private BigDecimal taxRate;

    /** 税额 */
    private BigDecimal taxAmt;

    /** 金额 */
    private BigDecimal billAmt;

    /** 销方名称 */
    private String sellerName;

    /** 购方名称 */
    private String buyerName;

    /** 应付凭证id */
    private String payId;

    /** 应收凭证id */
    private String receivableId;

    /** 应付应收标识(0为应付，1为应收) */
    private String sign;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createDate;

    /** 修改人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateDate;

    @Column(name = "INVOICE_TYPE")
    public String getInvoiceType() { return invoiceType; }
    public void setInvoiceType(String invoiceType) { this.invoiceType = invoiceType; }

    @Column(name = "INVOICE_NUMBER")
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    @Column(name = "INVOICE_CODE")
    public String getInvoiceCode() { return invoiceCode; }
    public void setInvoiceCode(String invoiceCode) { this.invoiceCode = invoiceCode; }

    @Column(name = "BILL_DATE")
    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }

    @Column(name = "CUR_NAME")
    public String getCurName() { return curName; }
    public void setCurName(String curName) { this.curName = curName; }

    @Column(name = "CUR_ID")
    public String getCurId() { return curId; }
    public void setCurId(String curId) { this.curId = curId; }

    @Column(name = "TOTAL_AMT")
    public BigDecimal getTotalAmt() { return totalAmt; }
    public void setTotalAmt(BigDecimal totalAmt) { this.totalAmt = totalAmt; }

    @Column(name = "TAX_RATE")
    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }

    @Column(name = "TAX_AMT")
    public BigDecimal getTaxAmt() { return taxAmt; }
    public void setTaxAmt(BigDecimal taxAmt) { this.taxAmt = taxAmt; }

    @Column(name = "BILL_AMT")
    public BigDecimal getBillAmt() { return billAmt; }
    public void setBillAmt(BigDecimal billAmt) { this.billAmt = billAmt; }

    @Column(name = "SELLER_NAME")
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    @Column(name = "BUYER_NAME")
    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    @Column(name = "PAY_ID")
    public String getPayId() { return payId; }
    public void setPayId(String payId) { this.payId = payId; }

    @Column(name = "RECEIVABLE_ID")
    public String getReceivableId() { return receivableId; }
    public void setReceivableId(String receivableId) { this.receivableId = receivableId; }

    @Column(name = "SIGN")
    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }

    @Column(name = "CREATE_BY")
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
