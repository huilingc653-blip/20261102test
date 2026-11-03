package com.bytter.supply.domain.receivable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ztd
 * @description 债权凭证收款明细VO
 * @since 2026/7/10
 */
@ApiModel(value = "ScfReceivablePaymentInfoVO", description = "债权凭证收款明细VO")
@Data
public class ScfReceivablePaymentInfoVO implements Serializable, IBusinessKey {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("债权凭证编号")
    @BtCompositeSearch(fieldName = "t.voucher_code")
    private String voucherCode;

    @ApiModelProperty("关联应收债权凭证信息")
    private ScfReceivableVoucherInfoVO voucherInfo;

    @ApiModelProperty("收款单号")
    private String paymentNo;

    /** 收款审批状态 */
    @ApiModelProperty("收款审批状态")
    @BtCompositeSearch(fieldName = "t.payment_status")
    private String paymentStatus;
    @ApiModelProperty("收款审批状态名称")
    private String paymentStatusName;

    @JsonIgnore
    private String businessKey;

    @ApiModelProperty("平台id")
    @BtCompositeSearch(fieldName = "t.platform_id")
    private String platformId;

    @ApiModelProperty("平台名称")
    private String platformName;

    @ApiModelProperty("融资产品")
    @BtCompositeSearch(fieldName = "t.product_id")
    private String productId;

    @ApiModelProperty("融资产品名称")
    private String productName;

    @ApiModelProperty("收款单位")
    private String receiptCompany;

    @ApiModelProperty("收款单位id")
    @BtCompositeSearch(fieldName = "t.receipt_company_id")
    private String receiptCompanyId;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("签收日期")
    private Date signDate;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("到期日")
    private Date expireDate;

    @ApiModelProperty("开立/转让单位")
    private String issueTransferCompany;

    @ApiModelProperty("开立/转让企业id")
    private String issueTransferCompanyId;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("币别名称")
    private String curName;

    @ApiModelProperty("凭证金额")
    private BigDecimal voucherAmt;

    @ApiModelProperty("核心企业id")
    private String coreCompanyId;

    @ApiModelProperty("核心企业")
    private String coreCompanyName;

    @ApiModelProperty("收款账号")
    private String recAccountNo;

    @ApiModelProperty("收款户名")
    private String recAccountName;

    @ApiModelProperty("收款开户行")
    private String recBank;

    @ApiModelProperty("收款金额")
    @BtCompositeSearch(fieldName = "t.rec_amt")
    private BigDecimal recAmt;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("收款日期")
    @BtCompositeSearch(fieldName = "t.receipt_date")
    private Date receiptDate;

    @ApiModelProperty("是否有效(0：无效；1：有效)")
    private String enable;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private Date updateDate;

    @Override
    public String getBusinessKey() {
        return businessKey != null ? businessKey : getId();
    }
}
