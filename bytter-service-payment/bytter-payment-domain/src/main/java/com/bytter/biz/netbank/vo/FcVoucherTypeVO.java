package com.bytter.biz.netbank.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Title: Entity
 * @Description: 自定义付款方式VO
 * @author onlineGenerator
 * @date 2017-05-12 17:18:41
 * @version V1.0
 *
 */
@ApiModel("自定义付款方式VO")
@Data
public class FcVoucherTypeVO implements Serializable {
	@Dict(tableName="FC_VOUCHER_TYPE",valueFieldName="id",showFiledName="VOUCHER_NAME",inject="voucherName")
	@ApiModelProperty("ID")
	private String id;

	/**付款方式编码*/
	@BtCompositeSearch(fieldName = "f.voucher_Type")
	@ApiModelProperty("付款方式编码")
	private String voucherType;

	/**付款方式名称*/
	@BtCompositeSearch(fieldName = "f.voucher_Name")
	@ApiModelProperty("付款方式名称")
	private String voucherName;

    @BtCompositeSearch(fieldName = "f.voucher_Name")
	@ApiModelProperty("付款方式显示名称")
	private String voucherNameShow;

	@Dict(tableName="BT_PAY_TYPE",valueFieldName="id",showFiledName="SYS_VOUCHER_NAME",inject="sysVoucherName")
	@ApiModelProperty("系统结算方式ID")
	private String sysVoucherId;

	/**系统结算方式*/
	@BtCompositeSearch(fieldName = "f.sys_voucher_code")
	@ApiModelProperty("系统结算方式")
	private String sysVoucherCode;

	/**系统结算方式名称*/
	@BtCompositeSearch(fieldName = "b.sys_voucher_name")
	@ApiModelProperty("系统结算方式名称")
	private String sysVoucherName;

	/**现金转账标志*/
	@Dict(dictGroup = "cashTransSign",inject = "cashTransSignName")
	@BtCompositeSearch(fieldName = "f.cash_trans_sign")
	@ApiModelProperty("现金转账标志")
	private String cashTransSign;
	private String cashTransSignName;

	/**结算类型*/
	@Dict(dictGroup = "settlType",inject = "settlTypeName")
	@BtCompositeSearch(fieldName = "f.settl_type")
	@ApiModelProperty("结算类型")
	private String settlType;
	private String settlTypeName;

	/**是否走银企接口*/
	@BtCompositeSearch(fieldName = "f.is_electron_pay")
	@ApiModelProperty("是否走银企接口")
	@Dict(dictGroup = "sf_yn",inject = "isElectronPayName")
	private String isElectronPay;
	private String isElectronPayName;

	/**是否记账*/
	@Dict(dictGroup = "sf_yn",inject = "isAccountName")
	@BtCompositeSearch(fieldName = "f.is_account")
	@ApiModelProperty("是否记账")
	private String isAccount;
	private String isAccountName;

	/**是否挂资金计划*/
	@Dict(dictGroup = "sf_yn",inject = "isBudgetName")
	@BtCompositeSearch(fieldName = "f.is_budget")
	@ApiModelProperty("是否挂资金计划")
	private String isBudget;
	private String isBudgetName;

	/**指令发送模式*/
	@ApiModelProperty("指令发送模式")
    @BtCompositeSearch(fieldName = "f.send_Mode")
	@Dict(dictGroup = "bisExc.sendMode",inject = "sendModeName")
	private java.lang.String sendMode;
	private java.lang.String sendModeName;

	/**备注*/
	@BtCompositeSearch(fieldName = "f.rmk")
	@ApiModelProperty("备注")
	private String rmk;

	/**是否系统付款方式*/
	@Dict(dictGroup = "sf_yn",inject = "isSystemName")
	@ApiModelProperty("是否系统付款方式")
	private String isSystem;
	private String isSystemName;

	/**有效标志*/
	@Dict(dictGroup = "valid_sign",inject = "validSignName")
	@BtCompositeSearch(fieldName = "f.valid_sign")
	@ApiModelProperty("有效标志")
	private String validSign;
	private String validSignName;

	/**开票区间控制标识 1-三个月 2-六个月*/
	@ApiModelProperty("开票区间控制标识 1-三个月 2-六个月")
	@Dict(dictGroup = "BILL_SECTION_CONTROL",inject = "billSectionControlName")
	private java.lang.String billSectionControl;
	private java.lang.String billSectionControlName;


}
