/**
 * 
 */
package com.bytter.biz.fundtransfer.enums;

/**
 * 
 * 银行接口常量
 * 
 * @author 张均锋
 *
 */
public class BankConstants {

	/** 迁移常量 **/
	public static final String SAME_BANK = "0";

	public static final String DIF_BANK = "1";

	public static final String SAME_AREA = "0";

	public static final String DIF_AREA = "1";
	
	// 调拨频率
	public static final int POLICY_BY_DAY = 4;

	public static final int POLICY_BY_WEEK = 8;

	public static final int POLICY_BY_MONTH = 16;
	
}
