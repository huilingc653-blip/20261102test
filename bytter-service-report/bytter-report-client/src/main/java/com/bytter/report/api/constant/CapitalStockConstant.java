package com.bytter.report.api.constant;
import java.util.HashMap;
/**
 * 资金存量常量类
 *
 * @author zuoy 2017-10-25
 */
public class CapitalStockConstant {
	/**
	 * 日期类型 - 日
	 */
	public static final String DATE_TYPE_DAY = "day";
	/**
	 * 日期类型 - 周
	 */
	public static final String DATE_TYPE_WEEK = "week";
	/**
	 * 日期类型 - 月
	 */
	public static final String DATE_TYPE_MONTH = "month";
	/**
	 * 日期类型 - 季
	 */
	public static final String DATE_TYPE_SEASON = "season";
	/**
	 * 日期类型 - 年
	 */
	public static final String DATE_TYPE_YEAR = "year";
	/**
	 * 单位  元
	 */
	public static final String MONTY_UNIT_YUAN = "yuan";
	/**
	 * 单位  万元
	 */
	public static final String MONTY_UNIT_MILLION = "million";
	/**
	 * 存量种类 - 银行存款
	 */
	public static final String STOCK_TYPE_BAL = "1";
	/**
	 * 存量种类 - 应收票据
	 */
	public static final String STOCK_TYPE_BILL = "2";
	/**
	 * 报表类型  - 网点
	 */
	public static final String REPORT_TYPE_NET = "net";
	/**
	 * 报表类型  - 单位
	 */
	public static final String REPORT_TYPE_CORP = "corp";
	/**
	 * 报表类型  - 账户
	 */
	public static final String REPORT_TYPE_BANK_ACC = "bankType";
	/**
	 * 报表类型  - 账户流水
	 */
	public static final String REPORT_TYPE_DTL = "dtl";
	/**
	 * 报表类型  - 应收票据明细
	 */
	public static final String REPORT_TYPE_ABBILLDTL = "abBilldtl";
	/**
	 * 报表类型  - 账户冻结
	 */
	public static final String REPORT_TYPE_FREEZE = "freeze";
	/**
	 * 报表类型  - 应收票据占用明细
	 */
	public static final String REPORT_TYPE_ABBILLFREEZE = "abBillFreeze";
	/**
	 * 币别种类  - 人民币
	 */
	public static final String CUR_TYPE_RMB = "人民币";
	public static final HashMap<String, String> CUR_NAME_MAP = new HashMap<String, String>() {
		{
			put("人民币", "#990033");
			put("港币", "#993366");
			put("美元", "#996699");
			put("欧元", "#996633");
			put("英镑", "#999999");
			put("日元", "#99CC33");
			put("澳元", "#9999CC");
			put("韩元", "#99CCFF");
			put("加元", "#99CCCC");
			put("瑞郎", "#99FFCC");
		}
	};
	public static final String CUR_TYPE_JPY = "日元";
	public static final String CUR_TYPE_US = "美元";
}
