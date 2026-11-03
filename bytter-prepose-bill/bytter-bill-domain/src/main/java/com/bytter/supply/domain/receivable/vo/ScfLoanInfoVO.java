package com.bytter.supply.domain.receivable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "ScfLoanInfoVO", description = "融资业务主信息VO")
@Data
public class ScfLoanInfoVO implements Serializable, IBusinessKey {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("融资单号(RZ+年月日+3位流水号)")
    @BtCompositeSearch(fieldName = "t.loan_no")
    private String loanNo;

    @ApiModelProperty("融资企业id")
    @BtCompositeSearch(fieldName = "t.loan_company_id")
    private String loanCompanyId;

    @ApiModelProperty("融资企业名称")
    private String loanCompanyName;

    @ApiModelProperty("债权凭证编号")
    @BtCompositeSearch(fieldName = "t.voucher_code")
    private String voucherCode;

    @ApiModelProperty("关联应收债权凭证信息")
    private ScfReceivableVoucherInfoVO voucherInfo;

    @ApiModelProperty("核心企业")
    private String coreCompany;

    @ApiModelProperty("核心企业id")
    private String coreCompanyId;

    @ApiModelProperty("平台ID")
    @BtCompositeSearch(fieldName = "t.platform_id")
    private String platformId;

    @ApiModelProperty("平台名称")
    private String platformName;

    @ApiModelProperty("资金方")
    @BtCompositeSearch(fieldName = "t.funder_id")
    private String funderId;

    @ApiModelProperty("资金方名称")
    private String funderName;

    @ApiModelProperty("融资产品id")
    private String productId;

    @ApiModelProperty("融资产品名称")
    private String productName;

    @ApiModelProperty("付息方式(1:卖方付息，2:买方付息)")
    private String interestMode;

    @ApiModelProperty("币别ID")
    private String curId;

    @ApiModelProperty("币别名称")
    private String curName;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("融资申请日")
    private Date loanApplyDate;

    @BtCompositeSearch(fieldName = "t.loan_amt")
    @ApiModelProperty("融资金额")
    private BigDecimal loanAmt;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("放款日(默认系统当前日期)")
    @BtCompositeSearch(fieldName = "t.loan_lend_date")
    private Date loanLendDate;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("实际放款日")
    private Date loanActualDate;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty("到期日")
    @BtCompositeSearch(fieldName = "t.loan_due_date")
    private Date loanDueDate;

    @ApiModelProperty("期限(到期日-放款日) 天")
    private String loanDeadline;

    @ApiModelProperty("融资利率")
    private BigDecimal loanRate;

    @ApiModelProperty("利息金额(融资金额*融资利率)")
    private BigDecimal loanInterestAmt;

    @ApiModelProperty("手续费率")
    private BigDecimal loanHandFeeRate;

    @ApiModelProperty("手续费(融资金额*手续费率)")
    private BigDecimal loanHandFeeAmt;

    @ApiModelProperty("服务费率")
    private BigDecimal loanServiceFeeRate;

    @ApiModelProperty("服务费(融资金额*服务费率)")
    private BigDecimal loanServiceFeeAmt;

    @ApiModelProperty("融资成本(利息金额+手续费+服务费)")
    private BigDecimal loanCostAmt;

    @ApiModelProperty("融资到账金额(融资金额-融资成本)")
    @BtCompositeSearch(fieldName = "t.loan_actual_amt")
    private BigDecimal loanActualAmt;

    @ApiModelProperty("收款账号")
    private String loanPaymentAccount;

    @ApiModelProperty("收款开户名")
    private String loanPaymentAccountName;

    @ApiModelProperty("收款开户行")
    private String loanPaymentAccountBank;

    @ApiModelProperty("应收ID")
    private String receivableId;

    @ApiModelProperty("融资审批状态(DRAFT:暂存; APPROVAL_ING:审批中; REJECTED:打回; APPROVAL_PASS:审批通过)")
    @BtCompositeSearch(fieldName = "t.loan_status")
    private String loanStatus;
    @ApiModelProperty("融资审批状态名称")
    private String loanStatusName;
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

    @Override
    public String getBusinessKey() {
        return getReceivableId() != null ? getReceivableId() : getId();
    }
}
