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
 * @Title: ScfLoanInfoDTO
 * @Description: 融资业务主信息DTO
 * @since 2026/7/3
 */
@ApiModel(value = "ScfLoanInfoDTO", description = "融资业务主信息DTO")
@Data
public class ScfLoanInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ApiModelProperty("主键ID")
    private String id;
    /** 融资单号(RZ+年月日+3位流水号) */
    @ApiModelProperty("融资单号(RZ+年月日+3位流水号)")
    private String loanNo;
    /** 融资企业id */
    @ApiModelProperty("融资企业id")
    private String loanCompanyId;
    /** 融资企业名称 */
    @ApiModelProperty("融资企业名称")
    private String loanCompanyName;
    /** 债权凭证编号 */
    @ApiModelProperty("债权凭证编号")
    private String voucherCode;
    /** 核心企业 */
    @ApiModelProperty("核心企业")
    private String coreCompany;
    /** 核心企业id */
    @ApiModelProperty("核心企业id")
    private String coreCompanyId;
    /** 平台ID */
    @ApiModelProperty("平台ID")
    private String platformId;
    @ApiModelProperty("平台名称")
    private String platformName;
    /** 资金方 */
    @ApiModelProperty("资金方")
    private String funderId;
    @ApiModelProperty("资金方名称")
    private String funderName;
    /** 融资产品id */
    @ApiModelProperty("融资产品id")
    private String productId;
    /** 融资产品名称 */
    @ApiModelProperty("融资产品名称")
    private String productName;
    /** 付息方式(1:卖方付息，2:买方付息，默认卖方付息) */
    @ApiModelProperty("付息方式(1:卖方付息，2:买方付息)")
    private String interestMode;
    /** 币别ID */
    @ApiModelProperty("币别ID")
    private String curId;
    /** 币别名称 */
    @ApiModelProperty("币别名称")
    private String curName;
    /** 融资申请日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("融资申请日")
    private Date loanApplyDate;
    /** 融资金额 */
    @DecimalMin(value = "0.00", inclusive = false, message = "融资金额必须大于0")
    @ApiModelProperty("融资金额")
    private BigDecimal loanAmt;
    /** 放款日(默认系统当前日期) */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("放款日(默认系统当前日期)")
    private Date loanLendDate;
    /** 实际放款日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("实际放款日")
    private Date loanActualDate;
    /** 到期日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("到期日")
    private Date loanDueDate;
    /** 期限(到期日-放款日) 天 */
    @ApiModelProperty("期限(到期日-放款日) 天")
    private String loanDeadline;
    /** 融资利率 */
    @ApiModelProperty("融资利率")
    private BigDecimal loanRate;
    /** 利息金额(融资金额*融资利率) */
    @ApiModelProperty("利息金额(融资金额*融资利率)")
    private BigDecimal loanInterestAmt;
    /** 手续费率 */
    @ApiModelProperty("手续费率")
    private BigDecimal loanHandFeeRate;
    /** 手续费(融资金额*手续费率) */
    @ApiModelProperty("手续费(融资金额*手续费率)")
    private BigDecimal loanHandFeeAmt;
    /** 服务费率 */
    @ApiModelProperty("服务费率")
    private BigDecimal loanServiceFeeRate;
    /** 服务费(融资金额*服务费率) */
    @ApiModelProperty("服务费(融资金额*服务费率)")
    private BigDecimal loanServiceFeeAmt;
    /** 融资成本(利息金额+手续费+服务费) */
    @ApiModelProperty("融资成本(利息金额+手续费+服务费)")
    private BigDecimal loanCostAmt;
    /** 融资到账金额(融资金额-融资成本) */
    @ApiModelProperty("融资到账金额(融资金额-融资成本)")
    private BigDecimal loanActualAmt;
    /** 收款账号 */
    @ApiModelProperty("收款账号")
    private String loanPaymentAccount;
    /** 收款开户名 */
    @ApiModelProperty("收款开户名")
    private String loanPaymentAccountName;
    /** 收款开户行 */
    @ApiModelProperty("收款开户行")
    private String loanPaymentAccountBank;
    /** 应收ID */
    @ApiModelProperty("应收ID")
    private String receivableId;
    /** 融资审批状态(DRAFT:暂存; APPROVAL_ING:审批中; REJECTED:打回; APPROVAL_PASS:审批通过) */
    @ApiModelProperty("融资审批状态(DRAFT:暂存; APPROVAL_ING:审批中; REJECTED:打回; APPROVAL_PASS:审批通过)")
    private String loanStatus;
    @ApiModelProperty("线上线下标识(0:线下；1:线上)")
    private String onlineSign;

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
    /** 修改时间 */
    @ApiModelProperty("修改时间")
    private Date updateDate;

    @ApiModelProperty("提交状态：提交:submit ; save : 暂存")
    private String sendStatus;
}
