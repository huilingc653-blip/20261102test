package com.bytter.bill.api.bill.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Title: Entity
 * @Description: 票据银行对于自动业务配置
 * @author zhenghj
 * @date 2017-09-28
 * @version 1.0
 *
 */

@Data
public class AbBillBankAutoDtlDTO implements java.io.Serializable {
	/**银行接口类型ID*/
	private String bifCode;
	private String bifId;
	private String bifName;
	private String bankTypeId;
	private String bankType;
	private String typeName;
	/**提示承兑（0否； 1是 ）*/
	private String autoAccept;
	/**提示收票（0否； 1是 ）*/
	private String autoInvoice;
	/**提示付款（0否； 1是 ）*/
	private String autoPay;
	/**票据池背书（0否； 1是 ）*/
	private String poolEndorse;
	/**票据池开票（0否； 1是 ）*/
	private String poolEmitBill;
	/**是否有效（0否； 1是 ）*/
	private String validSign;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**
	 * 是否支持新一代票据，1是，0否
	 */
	private String newBillFlg;
	private String newBifId;
	private String newBifCode;
}
