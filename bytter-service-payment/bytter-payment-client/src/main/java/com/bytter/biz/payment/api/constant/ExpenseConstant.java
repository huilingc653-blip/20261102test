package com.bytter.biz.payment.api.constant;
/**
 * 费用报销常量类
 * @author huangx
 * @since 2017/6/2.
 */
public class ExpenseConstant {
	
	/*** 费用报销参数配置：exp-费用报销 */
	public static final String EXP_CONFIG_TYPE = "exp";
	/*** 费用报销参数配置：budgetSel-是否挂接资金计划  */
	public static final String EXP_CONFIG_BUDGETSEL = "budgetSel";
	/*** 费用报销参数配置：payItem-付款方式默认走的计划科目  */
	public static final String EXP_CONFIG_PAYITEM = "payItem";
	/*** 费用报销参数配置：expFbsin-报销计划挂接环节  */
	public static final String EXP_CONFIG_EXPFBSIN = "expFbsin";
	/*** 费用报销参数配置：accountOver-内部账户余额不足  */
	public static final String EXP_CONFIG_ACCOUNTOVER = "accountOver";
	/*** 费用报销参数配置：bankaccOver-银行账户余额不足  */
	public static final String EXP_CONFIG_BANKACCOVER = "bankaccOver";
	/*** 费用报销参数配置：payAccount-是否先付款后记账  */
	public static final String EXP_CONFIG_PAYACCOUNT = "payAccount";
	/*** 费用报销参数配置：isAutoAccount-是否自动记账  */
	public static final String EXP_CONFIG_ISAUTOACCOUNT = "isAutoAccount";
	/*** 费用报销参数配置：autoAccounter-自动记账人  */
	public static final String EXP_CONFIG_AUTOACCOUNTER = "autoAccounter";
	
	/*** 付款业务处理状态：0-暂存  */
	public final static Integer STATUE_TEMP = 0;
	/*** 付款业务处理状态：1-打回  */
    public final static Integer STATUE_BACK = -1;
    /*** 付款业务处理状态：-2-删除  */
    public final static Integer STATUE_DEL = -2;
    /*** 付款业务处理状态：11-待审批  */
    public final static Integer STATUE_WAIT_CHECK = 11;
    /*** 付款业务处理状态：95-审批通过  */
    public final static Integer STATUE_CHECK = 95;
    /*** 付款业务处理状态：100-已汇总  */
    public final static Integer STATUE_COLLECT = 100;
    
    /*** 单据来源：0-费用报销  */
    public final static String BILLFROM_0 = "0";
    /*** 单据来源：1-支付结算  */
    public final static String BILLFROM_1 = "1";
    /*** 单据来源：2-ERP系统  */
    public final static String BILLFROM_2 = "2";
    
    /*** 业务类型：01-借款申请  */
    public final static String BUSTYPE_01 = "01";
    /*** 业务类型：02-报销申请  */
    public final static String BUSTYPE_02 = "02";
    /*** 业务类型：03-退款申请  */
    public final static String BUSTYPE_03 = "03";
    
    /*** 操作类型：01-个人借款  */
    public final static String OPTYPE_01_01 = "01";
    /*** 操作类型：02-部门借款  */
    public final static String OPTYPE_01_02 = "02";
    /*** 操作类型：01-个人报销  */
    public final static String OPTYPE_02_01 = "01";
    /*** 操作类型：02-部门报销  */
    public final static String OPTYPE_02_02 = "02";
    /*** 操作类型：03-市场费用报销  */
    public final static String OPTYPE_02_03 = "03";
    /*** 操作类型：01-现金  */
    public final static String OPTYPE_03_01 = "01";
    /*** 操作类型：02-转账  */
    public final static String OPTYPE_03_02 = "02";
    
    /*** 银行卡类型：0-同城借记卡(中行)  */
    public final static String CARDTYPE_0 = "0";
    /*** 银行卡类型：1-异地借记卡(中行)  */
    public final static String CARDTYPE_1 = "1";
    /*** 银行卡类型：2-同城信用卡(中行)  */
    public final static String CARDTYPE_2 = "2";
    /*** 银行卡类型：3-异地信用卡(中行)  */
    public final static String CARDTYPE_3 = "3";
    /*** 银行卡类型：4-银行卡  */
    public final static String CARDTYPE_4 = "4";
    /*** 银行卡类型：5-存折  */
    public final static String CARDTYPE_5 = "5";
    /*** 报销计划挂接环节：0-报销录入  */
    public final static String EXPFBSIN_ENTER = "0";
    /*** 报销计划挂接环节：1-报销汇总  */
    public final static String EXPFBSIN_POOL = "1";
}
