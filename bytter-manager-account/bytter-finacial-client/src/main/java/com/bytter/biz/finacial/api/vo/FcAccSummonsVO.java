package com.bytter.biz.finacial.api.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
@ApiModel("账户传票信息")
public class FcAccSummonsVO implements Serializable{
	@ApiModelProperty(value = "主键ID")
	private String id;
	/**流水号*/
	@ApiModelProperty(value = "流水号")
	private String orderNo;
	/**凭证号*/
	@BtCompositeSearch(fieldName = "cc.voucher_no")
	@ApiModelProperty(value = "凭证号")
	private String voucherNo;
	/**金额*/
	@BtCompositeSearch(fieldName = "cc.sum_money")
	@ApiModelProperty(value = "金额")
	private BigDecimal sumMoney;
	/**种类*/
	@BtCompositeSearch(fieldName = "a.voucher_type")
	@Dict(dictGroup = "summons.voucherType", inject = "voucherTypeName")
	@ApiModelProperty(value = "种类")
	private String voucherType;
	@ApiModelProperty(value = "种类名称")
	private String voucherTypeName;
	/**付款方式*/
	@BtCompositeSearch(fieldName = "pay_type")
	@ApiModelProperty(value = "付款方式")
	private String voucherId;
	@BtCompositeSearch(fieldName = "d.voucher_name")
	@ApiModelProperty(value = "凭证名称")
	private String voucherName;
	/**记账日期*/
	@BtCompositeSearch(fieldName = "tally_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "记账日期")
	private Date tallyDate;
	/**摘要*/
	@BtCompositeSearch(fieldName = "summary_content")
	@ApiModelProperty(value = "摘要")
	private String summaryContent;
	/**单位*/
	@BtCompositeSearch(fieldName = "corp_Id")
	@ApiModelProperty(value = "单位")
	private String corpId;
	/**是否隐藏 1：隐藏*/
	@ApiModelProperty(value = "是否隐藏 1：隐藏")
	private Integer checkboxHide;
	/** 内部账号*/
	@ApiModelProperty(value = "内部账号")
	private String  account;
	@ApiModelProperty(value = "账户名称")
	private String  accName;
	@ApiModelProperty(value = "借方金额")
	private BigDecimal debitMoney;
    @ApiModelProperty(value = "贷方金额")
	private BigDecimal creditMoney;

	private BigDecimal debit;

	private BigDecimal credit;
}
