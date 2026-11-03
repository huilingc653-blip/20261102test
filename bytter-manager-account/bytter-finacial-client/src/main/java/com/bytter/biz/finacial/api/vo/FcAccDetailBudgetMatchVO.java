package com.bytter.biz.finacial.api.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Created by wangzx on 2018/3/23
 */
@Data
public class FcAccDetailBudgetMatchVO implements Serializable {
	@ApiModelProperty(value = "主键ID")
	private String id;
	/**单位*/
	@ApiModelProperty(value = "单位")
	private String corpId;
	/**交易日期*/
	@ApiModelProperty(value = "交易日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "f.tallyDate")
	private Date tallyDate;
	/**本方账号*/
	@ApiModelProperty(value = "本方账号")
	@BtCompositeSearch(fieldName = "f.blnctrAcc")
	private String blnctrAcc;
	@ApiModelProperty(value = "账号")
	private String account;
	/**本方账户名称*/
	@ApiModelProperty(value = "本方账户名称")
	@BtCompositeSearch(fieldName = "f.accName")
	private String accName;
	/**对方账号*/
	@ApiModelProperty(value = "对方账号")
	@BtCompositeSearch(fieldName = "f.oppAccNo")
	private String oppAccNo;
	/**对方账户名称*/
	@ApiModelProperty(value = "对方账户名称")
	@BtCompositeSearch(fieldName = "f.oppAccName")
	private String oppAccName;
	/**借方发生额*/
	@ApiModelProperty(value = "借方发生额")
	@BtCompositeSearch(fieldName = "f.debitMoney")
	private BigDecimal debitMoney;
	/**贷方发生额*/
	@ApiModelProperty(value = "贷方发生额")
	@BtCompositeSearch(fieldName = "f.creditMoney")
	private BigDecimal creditMoney;
	/**收支标志 0 收 1 支*/
	@ApiModelProperty(value = "收支标志 0 收 1 支")
	@BtCompositeSearch(fieldName = "f.cdSign")
	@Dict(dictGroup = "btBankAccDtl.cdSign",inject = "cdSignName")
	private String cdSign;
	@ApiModelProperty(value = "收支标志名称")
	@BtCompositeSearch(fieldName = "t.cd_sign")
	private String cdSignName;
	/**凭证号*/
	@ApiModelProperty(value = "凭证号")
	@BtCompositeSearch(fieldName = "f.voucherNo")
	private String voucherNo;
	/**流水号*/
	@ApiModelProperty(value = "流水号")
	@BtCompositeSearch(fieldName = "f.orderNo")
	private String orderNo;
	/** 资金计划挂接状态 0 - 未确认 1 - 已确认 -2 - 已删除*/
	@ApiModelProperty(value = "资金计划挂接状态 0 - 未确认 1 - 已确认 -2 - 已删除")
	@Dict(dictGroup = "bisAccDtlBudgetRule.fbsStatus", inject = "fbsStatusName")
	private String fbsStatus;
	@ApiModelProperty(value = "资金计划挂接状态名称")
	private String fbsStatusName;
	/**预算挂接科目*/
	@ApiModelProperty(value = "预算挂接科目")
	private String budget;
	/**预算科目信息*/
	@ApiModelProperty(value = "预算科目信息")
	private String itemInfo;
	/**执行日期*/
	@ApiModelProperty(value = "执行日期")
	private String processDate;
	/**币别*/
	@ApiModelProperty(value = "币别")
	private String curId;
	/**日结标志*/
	@ApiModelProperty(value = "日结标志")
	private String dayCloseSign;
	/**预算科目信息*/
	@ApiModelProperty(value = "预算科目信息")
	private String itemInfoText;
	/**是否隐藏 1：隐藏*/
	@ApiModelProperty(value = "是否隐藏 1：隐藏")
	private String checkboxHide;
	/** 币别汇率 */
	@ApiModelProperty(value = "币别汇率")
	private BigDecimal curExrate;
	@ApiModelProperty(value = "业务来源类型")
	private String busOriType;
}
