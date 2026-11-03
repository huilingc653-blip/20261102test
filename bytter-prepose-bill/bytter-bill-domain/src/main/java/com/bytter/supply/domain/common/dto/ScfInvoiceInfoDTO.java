package com.bytter.supply.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfInvoiceInfoDTO
 * @Description: 应付/应收债券发票信息DTO
 * @since 2026/7/3
 */
@ApiModel(value = "ScfInvoiceInfoDTO", description = "应付/应收债券发票信息DTO")
public class ScfInvoiceInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty("主键ID")
    private String id;
    /** 发票类型 */
    @NotBlank(message = "发票类型不能为空")
    @ApiModelProperty("发票类型")
    private String invoiceType;
    /** 发票号码 */
    @NotBlank(message = "发票号码不能为空")
    @ApiModelProperty("发票号码")
    private String invoiceNumber;
    /** 发票代码 */
    @NotBlank(message = "发票代码不能为空")
    @ApiModelProperty("发票代码")
    private String invoiceCode;
    /** 开票日期 */
    @NotNull(message = "开票日期不能为空")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("开票日期")
    private Date billDate;
    /** 币种名称 */
    @ApiModelProperty("币种名称")
    private String curName;
    /** 币种 */
    @NotBlank(message = "币种不能为空")
    @ApiModelProperty("币种")
    private String curId;
    /** 价税合计 */
    @NotNull(message = "价税合计不能为空")
    @ApiModelProperty("价税合计")
    private BigDecimal totalAmt;
    /** 税率 */
    @NotNull(message = "税率不能为空")
    @ApiModelProperty("税率")
    private BigDecimal taxRate;
    /** 税额 */
    @NotNull(message = "税额不能为空")
    @ApiModelProperty("税额")
    private BigDecimal taxAmt;
    /** 金额 */
    @NotNull(message = "金额不能为空")
    @ApiModelProperty("金额")
    private BigDecimal billAmt;
    /** 销方名称 */
    @NotBlank(message = "销方名称不能为空")
    @ApiModelProperty("销方名称")
    private String sellerName;
    /** 购方名称 */
    @NotBlank(message = "购方名称不能为空")
    @ApiModelProperty("购方名称")
    private String buyerName;
    /** 应付凭证id */
    @ApiModelProperty("应付凭证id")
    private String payId;
    /** 应收凭证id */
    @ApiModelProperty("应收凭证id")
    private String receivableId;
    /** 应付应收标识(0为应付，1为应收) */
    @NotBlank(message = "应付应收标识不能为空")
    @ApiModelProperty("应付应收标识（0=应付，1=应收）")
    private String sign;

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
}
