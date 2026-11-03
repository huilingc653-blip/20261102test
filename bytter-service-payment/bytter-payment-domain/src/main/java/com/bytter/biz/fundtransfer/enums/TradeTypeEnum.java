/**
 * 
 */
package com.bytter.biz.fundtransfer.enums;

/**
 * 交易类型（外付网银小类）<br>
 * _VALUE值只能是一位
 * 
 * @author zhangjf
 * 
 */
public enum TradeTypeEnum {

	/** 普通付款 **/
	COMMON_PAYMENT('1', "普通付款"),
	/** 资金归集 （手动）**/
	FUND_GATHER_MANUAL('2', "资金归集（手动）"),
	/** 资金下拨（手动） **/
	FUND_ALLOWCATED_MANUAL('3', "资金下拨（手动）"),
	/** 资金归集 **/
	FUND_GATHER('4', "资金归集"),
	/** 资金下拨 **/
	FUND_ALLOWCATED('5', "资金下拨");

	public final char value;
	public final String description;

	TradeTypeEnum(char value, String description) {
		this.value = value;
		this.description = description;
	}

	public static TradeTypeEnum toEnum(char value) {
		for (TradeTypeEnum type : TradeTypeEnum.values()) {
			if (value == type.value)
				return type;
		}
		return null;
	}
}
