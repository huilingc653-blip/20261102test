package com.bytter.supply.domain.receivable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivablePaymentInfoDTO
 * @Description: 债权凭证收款明细DTO
 * @since 2026/7/10
 */
@ApiModel(value = "ScfReceivablePaymentInfoDTO", description = "债权凭证收款明细DTO")
@Data
public class ScfReceivablePaymentInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private String id;

    /** 债权凭证编号 */
    @ApiModelProperty("债权凭证编号")
    private String voucherCode;

    /** 收款单号 */
    @ApiModelProperty("收款单号")
    private String paymentNo;

    /** 租户id */
    @ApiModelProperty("租户id")
    private String tenantId;

    /** 收款审批状态 */
    @ApiModelProperty("收款审批状态")
    private String paymentStatus;

    /** 平台ID */
    @ApiModelProperty("平台id")
    private String platformId;

    /** 平台名称 */
    @ApiModelProperty("平台名称")
    private String platformName;

    /** 融资产品ID */
    @ApiModelProperty("融资产品")
    private String productId;

    /** 融资产品名称 */
    @ApiModelProperty("融资产品名称")
    private String productName;

    /** 收款单位 */
    @ApiModelProperty("收款单位")
    private String receiptCompany;

    /** 收款单位ID */
    @ApiModelProperty("收款单位id")
    private String receiptCompanyId;

    /** 签收日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("签收日期")
    private Date signDate;

    /** 到期日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("到期日")
    private Date expireDate;

    /** 开立/转让单位 */
    @ApiModelProperty("开立/转让单位")
    private String issueTransferCompany;

    /** 开立/转让企业ID */
    @ApiModelProperty("开立/转让企业id")
    private String issueTransferCompanyId;

    /** 币别ID */
    @ApiModelProperty("币别")
    private String curId;

    /** 币别名称 */
    @ApiModelProperty("币别名称")
    private String curName;

    /** 凭证金额 */
    @DecimalMin(value = "0.00", inclusive = false, message = "凭证金额必须大于0")
    @ApiModelProperty("凭证金额")
    private BigDecimal voucherAmt;

    /** 核心企业ID */
    @ApiModelProperty("核心企业id")
    private String coreCompanyId;

    /** 核心企业名称 */
    @ApiModelProperty("核心企业")
    private String coreCompanyName;

    /** 收款账号 */
    @ApiModelProperty("收款账号")
    private String recAccountNo;

    /** 收款户名 */
    @ApiModelProperty("收款户名")
    private String recAccountName;

    /** 收款开户行 */
    @ApiModelProperty("收款开户行")
    private String recBank;

    /** 收款金额 */
    @DecimalMin(value = "0.00", inclusive = false, message = "收款金额必须大于0")
    @ApiModelProperty("收款金额")
    private BigDecimal recAmt;

    /** 收款日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("收款日期")
    private Date receiptDate;

    @ApiModelProperty("是否有效(0：无效；1：有效)")
    private String enable;

    /** 创建人 */
    @ApiModelProperty("创建人")
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty("创建时间")
    private Date createDate;

    /** 修改人 */
    @ApiModelProperty("修改人")
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty("更新时间")
    private Date updateDate;

    @ApiModelProperty("提交状态：提交:submit ; save : 暂存")
    private String sendStatus;
}
