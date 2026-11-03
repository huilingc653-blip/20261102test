package com.bytter.supply.domain.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfInvoiceInfoVO
 * @Description: 应付/应收债券发票信息VO
 * @since 2026/7/3
 */
@ApiModel(value = "ScfInvoiceInfoVO", description = "应付/应收债券发票信息VO")
public class ScfInvoiceInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private String id;

    /** 发票类型 */
    @ApiModelProperty("发票类型")
    private String invoiceType;

    /** 发票号码 */
    @ApiModelProperty("发票号码")
    private String invoiceNumber;

    /** 发票代码 */
    @ApiModelProperty("发票代码")
    private String invoiceCode;

    /** 开票日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("开票日期")
    private Date billDate;

    /** 币种名称 */
    @ApiModelProperty("币种名称")
    private String curName;

    /** 币种 */
    @ApiModelProperty("币种")
    private String curId;

    /** 价税合计 */
    @ApiModelProperty("价税合计")
    private BigDecimal totalAmt;

    /** 税率 */
    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    /** 税额 */
    @ApiModelProperty("税额")
    private BigDecimal taxAmt;

    /** 金额 */
    @ApiModelProperty("金额")
    private BigDecimal billAmt;

    /** 销方名称 */
    @ApiModelProperty("销方名称")
    private String sellerName;

    /** 购方名称 */
    @ApiModelProperty("购方名称")
    private String buyerName;

    /** 应付凭证id */
    @ApiModelProperty("应付凭证id")
    private String payId;

    /** 应收凭证id */
    @ApiModelProperty("应收凭证id")
    private String receivableId;

    /** 应付应收标识(0为应付，1为应收) */
    @ApiModelProperty("应付应收标识（0=应付，1=应收）")
    private String sign;

    /** 创建人 */
    @ApiModelProperty("创建人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createDate;

    /** 修改人 */
    @ApiModelProperty("修改人")
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private Date updateDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getInvoiceType() { return invoiceType; }
    public void setInvoiceType(String invoiceType) { this.invoiceType = invoiceType; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getInvoiceCode() { return invoiceCode; }
    public void setInvoiceCode(String invoiceCode) { this.invoiceCode = invoiceCode; }

    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }

    public String getCurName() { return curName; }
    public void setCurName(String curName) { this.curName = curName; }

    public String getCurId() { return curId; }
    public void setCurId(String curId) { this.curId = curId; }

    public BigDecimal getTotalAmt() { return totalAmt; }
    public void setTotalAmt(BigDecimal totalAmt) { this.totalAmt = totalAmt; }

    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }

    public BigDecimal getTaxAmt() { return taxAmt; }
    public void setTaxAmt(BigDecimal taxAmt) { this.taxAmt = taxAmt; }

    public BigDecimal getBillAmt() { return billAmt; }
    public void setBillAmt(BigDecimal billAmt) { this.billAmt = billAmt; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }

    public String getPayId() { return payId; }
    public void setPayId(String payId) { this.payId = payId; }

    public String getReceivableId() { return receivableId; }
    public void setReceivableId(String receivableId) { this.receivableId = receivableId; }

    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}
