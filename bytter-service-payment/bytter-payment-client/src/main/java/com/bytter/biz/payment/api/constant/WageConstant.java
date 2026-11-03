package com.bytter.biz.payment.api.constant;
/**
 * 代发工资常量类
 * @author huangx
 * @since 2018/1/2.
 */
public class WageConstant {
	/*** 代发工资参数配置：wage-代发工资 */
	public static final String WAGE_CONFIG_TYPE = "wage";
	/*** 代发工资参数配置：budgetSel-是否挂接资金计划  */
	public static final String WAGE_CONFIG_BUDGETSEL = "budgetSel";
	/*** 代发工资参数配置：payItem-默认走的计划科目  */
	public static final String WAGE_CONFIG_PAYITEM = "payItem";
	/*** 代发工资参数配置：bankaccOver-银行账户余额不足  */
	public static final String WAGE_CONFIG_BANKACCOVER = "bankaccOver";
	/*** 代发工资参数配置：bankaccOverApprove-审批时银行账户余额不足  */
	public static final String WAGE_CONFIG_BANKACCOVER_APPROVE = "bankaccOverApprove";
	/*** 代发工资参数配置：wagesVerify-是否显示明细金额  */
	public static final String WAGE_CONFIG_WAGESVERIFY = "wagesVerify";
	/*** 代发工资参数配置：creator-制单人显示  */
	public static final String WAGE_CONFIG_CREATOR = "creator";
}
