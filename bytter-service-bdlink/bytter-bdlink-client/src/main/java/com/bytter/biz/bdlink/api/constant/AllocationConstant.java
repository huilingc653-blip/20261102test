package com.bytter.biz.bdlink.api.constant;
/**
 * 资金调拨常量类
 * @author huangx
 * @since 2017/6/20.
 */
public class AllocationConstant {
	/*** 资金调拨参数配置：allot-资金调拨 */
	public static final String ALLOT_CONFIG_TYPE = "allot";
	/*** 资金调拨参数配置：budgetSel-是否挂接资金计划  */
	public static final String ALLOT_CONFIG_BUDGETSEL = "budgetSel";
	/*** 资金调拨参数配置：payItem-下拨默认走的计划科目  */
	public static final String ALLOT_CONFIG_DOWNITEM = "downItem";
	/*** 资金调拨参数配置：incItem-请款默认走的计划科目  */
	public static final String ALLOT_CONFIG_ASKITEM = "askItem";
	/*** 资金调拨参数配置：accountOver-内部账户余额不足  */
	public static final String ALLOT_CONFIG_ACCOUNTOVER = "accountOver";
	/*** 资金调拨参数配置：bankaccOver-银行账户余额不足  */
	public static final String ALLOT_CONFIG_BANKACCOVER = "bankaccOver";
	/*** 支付结算参数配置：bankaccOverApprove-审批时银行账户余额不足  */
	public static final String ALLOT_CONFIG_BANKACCOVER_APPROVE = "bankaccOverApprove";
	/*** 资金调拨参数配置：isAccount-是否记账  */
	public static final String ALLOT_CONFIG_ISACCOUNT = "isAccount";
	/*** 资金调拨参数配置：isAccountPos-头寸调拨是否记账  */
	public static final String ALLOT_CONFIG_ISACCOUNTPOS = "isAccountPos";
	/*** 资金调拨参数配置：isOutoAccount-是否自动记账  */
	public static final String ALLOT_CONFIG_ISAUTOACCOUNT = "isAutoAccount";
	/*** 资金调拨参数配置：payAccount-是否先付款后记账  */
	public static final String ALLOT_CONFIG_PAYACCOUNT = "payAccount";
	/*** 资金调拨参数配置：bisExcAutoAccount-指令发送模式 */
	public static final String ALLOT_CONFIG_BISEXC_AUTOACCOUNT = "bisExcAutoAccount";
	/*** 资金调拨参数配置：askAllowEditMoney-请款审批时是否允许修改金额 */
	public static final String ALLOT_CONFIG_ASKALLOWEDITMONEY = "askAllowEditMoney";
	/*** 资金调拨参数配置：askExcSendGrant-请款指令发送权限：1-付款方/0-收款方 */
	public static final String  ALLOT_CONFIG_ASKEXCSENDGRANT= "askExcSendGrant";
	/*** 手动调拨单是否要与付款明细勾对：1:是 0：否*/
	public static final String  ALLOT_CONFIG_ALLOCATIONALLOWBLENDING= "allocationAllowBlending";
	/**
	 * 单据状态：暂存
	 */
	public final static Integer STATUE_TEMP = 0;
	/**
	 * 单据状态：打回
	 */
	public final static Integer STATUE_BACK = -1;//
	/**
	 * 单据状态：删除
	 */
	public final static Integer STATUE_DEL = -2;
	/**
	 * 单据状态：待审批
	 */
	public final static Integer STATUE_WAIT_CHECK = 11;
	/**
	 * 单据状态：审批通过
	 */
	public final static Integer STATUE_CHECK = 95;
	/**
	 * 单据状态：记账暂存
	 */
	public final static Integer STATUE_TALLY_TEMP = 96;
	/**
	 * 单据状态：记账待复核
	 */
	public final static Integer STATUE_TALLY_WAIT_CHECK = 97;
	/**
	 * 单据状态：记账复核打回
	 */
	public final static Integer STATUE_TALLY_BACK = 98;
	/**
	 * 单据状态：记账复核通过
	 */
	public final static Integer STATUE_TALLY_CHECKPASS = 100;
	/**
	 * 调拨类型：定时归集
	 */
	public final static String GATHERTYPE_0 = "0";
	/**
	 * 调拨类型：实时归集
	 */
	public final static String GATHERTYPE_1 = "1";
	/**
	 * 调拨类型：定时下拨
	 */
	public final static String GATHERTYPE_2 = "2";
	/**
	 * 归集方式
	 * 8-全额归集，6-取整归集，1-留底资金归集，9-固定金额归集，b-比例上划(交易额)
	 */
	/**
	 * 全额归集
	 */
	public final static String GATHERMODE_8 = "8";
	/**
	 * 取整归集
	 */
	public final static String GATHERMODE_6 = "6";
	/**
	 * 留底资金归集
	 */
	public final static String GATHERMODE_1 = "1";
	/**
	 * 固定金额归集
	 */
	public final static String GATHERMODE_9 = "9";
	/**
	 * 比例上划(交易额)
	 */
	public final static String GATHERMODE_B = "b";
	/**
	 * 下拨方式
	 * 4-固定余额，3-固定额度，0-根据前一日归集金额下拨，a-余额不足时固定额度下拨，2-比例下拨(交易额)
	 */
	/**
	 * 固定余额
	 */
	public final static String GATHERMODE_4 = "4";
	/**
	 * 固定额度
	 */
	public final static String GATHERMODE_3 = "3";
	/**
	 * 根据前一日归集金额下拨
	 */
	public final static String GATHERMODE_0 = "0";
	/**
	 * 余额不足时固定额度下拨
	 */
	public final static String GATHERMODE_A = "a";
	/**
	 * 比例下拨(交易额)
	 */
	public final static String GATHERMODE_2 = "2";
	/**
	 * 调拨周期
	 * 4-按日，8-按周，16-按月，32-按季，64-按年
	 */
	/**
	 * 按日
	 */
	public final static String FRQMODE_1 = "4";
	/**
	 * 按周
	 */
	public final static String FRQMODE_2 = "8";
	/**
	 * 按月
	 */
	public final static String FRQMODE_3 = "16";
	/**
	 * 按季
	 */
	public final static String FRQMODE_4 = "32";
	/**
	 * 按年
	 */
	public final static String FRQMODE_5 = "64";
	/**
	 * 资金调拨单类型
	 * 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴
	 */
	/**
	 * 上划
	 */
	public final static Integer ALLOCATION_TYPE_UP = 10;
	/**
	 * 下拨
	 */
	public final static Integer ALLOCATION_TYPE_DOWN = 11;
	/**
	 * 请款
	 */
	public final static Integer ALLOCATION_TYPE_ASK = 12;
	/**
	 * 头寸调拨
	 */
	public final static Integer ALLOCATION_TYPE_POS = 13;
	/**
	 * 上缴
	 */
	public final static Integer ALLOCATION_TYPE_TURN = 14;
	/**
	 * 单据来源 1-资金调拨
	 */
	public final static String BILLFROM_1 = "1";
	/**
	 * 单据来源 2-信贷管理-银行借款
	 */
	public final static String BILL_FROM_2 = "2";
	/**
	 * 单据来源 3-结算管理-银行保证金-存入保证金
	 */
	public final static String BILL_FROM_3 = "3";
	/**
	 * 单据来源 3-结算管理-银行保证金-追加保证金
	 */
	public final static String BILL_FROM_4 = "4";
	/**
	 * 资金池标识 0-非资金池
	 */
	public final static String POOL_SIGN_0 = "0";
	/**
	 * 资金池标识 1-资金池
	 */
	public final static String POOL_SIGN_1 = "1";
}
