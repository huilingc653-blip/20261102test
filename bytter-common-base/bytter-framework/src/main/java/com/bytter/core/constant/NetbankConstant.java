package com.bytter.core.constant;
/**
 * 付款常量类
 * @author huangx
 * @since 2017/6/2.
 */
public class NetbankConstant {
	/*** 支付结算参数配置：nis-支付结算 */
	public static final String NIS_CONFIG_TYPE = "nis";
	/*** 支付结算参数配置：budgetSel-是否挂接资金计划  */
	public static final String NIS_CONFIG_BUDGETSEL = "budgetSel";
	/*** 支付结算参数配置：payItem-付款方式默认走的计划科目  */
	public static final String NIS_CONFIG_PAYITEM = "payItem";
	/*** 支付结算参数配置：incItem-收款方式默认走的计划科目  */
	public static final String NIS_CONFIG_INCITEM = "incItem";
	/*** 支付结算参数配置：accountOver-内部账户余额不足  */
	public static final String NIS_CONFIG_ACCOUNTOVER = "accountOver";
	/*** 支付结算参数配置：bankaccOver-银行账户余额不足  */
	public static final String NIS_CONFIG_BANKACCOVER = "bankaccOver";
    /*** 支付结算参数配置：bankaccOverApprove-付款时银行账户余额不足  */
    public static final String NIS_CONFIG_BANKACCOVER_APPROVE = "bankaccOverApprove";
    /*** 支付结算参数配置：balControlType-付款余额控制方式  */
    public static final String NIS_CONFIG_BALCONTROLTYPE = "balControlType";
	/*** 支付结算参数配置：allowEditDownacc-是否允许中心修改下拨账号  */
	public static final String NIS_CONFIG_ALLOWEDITDOWNACC = "allowEditDownacc";
	/*** 支付结算参数配置：repayAlertDays-重复付款提醒周期  */
	public static final String NIS_CONFIG_REPAYALERTDAYS = "repayAlertDays";
	/*** 支付结算参数配置：payAccount-是否先付款后记账  */
	public static final String NIS_CONFIG_PAYACCOUNT = "payAccount";
	/*** 支付结算参数配置：isAutoAccount-是否自动记账  */
	public static final String NIS_CONFIG_ISAUTOACCOUNT = "isAutoAccount";
	/*** 支付结算参数配置：autoAccounter-自动记账人  */
	public static final String NIS_CONFIG_AUTOACCOUNTER = "autoAccounter";
	/*** 支付结算参数配置：chargeRate-手续费率  */
	public static final String NIS_CONFIG_CHARGERATE = "chargeRate";
	/*** 支付结算参数配置：replyDays-最多等待天数  */
	public static final String NIS_CONFIG_REPLYDAYS = "replyDays";
	/*** 支付结算参数配置：noReplyAct-无答复时  */
	public static final String NIS_CONFIG_NOREPLYACT = "noReplyAct";
    /*** 支付结算参数配置：showIsPayed-显示已付款是标识  */
    public static final String NIS_CONFIG_SHOWISPAYED = "showIsPayed";
    /*** 支付结算参数配置：isMathcFailReason-是否匹配交易失败原因 */
    public static final String NIS_CONFIG_ISMATHCFAILREASON = "isMathcFailReason";
    /** 支付结算参数配置：requiredBusiness-业务类别是否必输  */
    public static final String NIS_CONFIG_REQUIREDBUSINESS = "requiredBusiness";
    /** 支付结算参数配置：isEnableUserAssign-是否启用用户分配付款方式  */
    public static final String NIS_CONFIG_ISENABLEUSERASSIGN = "isEnableUserAssign";
    /** 支付结算参数配置：autoPayerAccNum-自动支付账号数量  */
    public static final String NIS_CONFIG_AUTOPAYERACCNUM = "autoPayerAccNum";
    /** 支付结算参数配置：isEnableSplit 是否启用拆分  */
    public static final String NIS_CONFIG_ISENABLESPLIT = "isEnableSplit";
    /** 支付结算参数配置：isEnableMerge 是否启用合并  */
    public static final String NIS_CONFIG_ISENABLEMERGE = "isEnableMerge";
    /** 收款认领管理参数配置：receivable 收款认领  */
    public static final String RECEIVABLE_CONFIG_TYPE = "receivable";
    /** 收款认领管理参数配置：isClaimBusType 是否认领到业务类别  */
    public static final String RECEIVABLE_CONFIG_ISCLAIMBUSTYPE = "isClaimBusType";
    /*** 收款认领管理参数配置：budgetSel-是否挂接资金计划  */
    public static final String RECEIVABLE_CONFIG_BUDGETSEL = "budgetSel";
    /*** 收款认领管理参数配置：dayCount-收款流水认领天数 */
    public static final String RECEIVABLE_CONFIG_DAYCOUNT = "dayCount";
    /*** 收款认领管理参数配置：collectionAutoCount-后台任务每批次执行笔数 */
    public static final String RECEIVABLE_CONFIG_COLLECTIONAUTOCOUNT = "collectionAutoCount";
    /*** 流水退款勾对参数配置：autoTrue-自动勾对是否直接确认  */
    public static final String RECEIVABLE_CONFIG_AUTOTRUE = "autoTrue";
    /** 支付结算参数配置：autoPayerAccNum-付款支付超时预警天数  */
    public static final String NIS_CONFIG_PAYOVERTIME = "payOverTime";
    /** 支付结算参数配置：waitResultOverTime-支付等待结果超时预警天数  */
    public static final String NIS_CONFIG_WAITRESULTOVERTIME = "waitResultOverTime";
    /** 支付结算参数配置：allowDifferentBillMoney-允许付款金额与票据金额不一致  */
    public static final String NIS_CONFIG_ALLOWDIFFERENTBILLMONEY = "allowDifferentBillMoney";
    /** 支付结算参数配置：delayTime-跨行转付在下拨指令成功后延迟支付  */
    public static final String NIS_CONFIG_DELAYTIME = "delayTime";
    /**
     * 支付结算参数配置：enablePendingPaymentPool-是否启用待付款池
     */
    public static final String NIS_CONFIG_ENABLEPENDINGPAYMENTPOOL = "enablePendingPaymentPool";
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
    /*** 付款业务处理状态：96-记账暂存  */
    public final static Integer STATUE_TALLY_TEMP = 96;
    /*** 付款业务处理状态：97-记账待复核  */
    public final static Integer STATUE_TALLY_WAIT_CHECK = 97;
    /*** 付款业务处理状态：98-记账复核打回  */
    public final static Integer STATUE_TALLY_BACK = 98;
    /*** 付款业务处理状态：99-票据完成  */
    public final static Integer STATUE_BILL_COMPLETE = 99;
    /*** 付款业务处理状态：100-记账复核通过  */
    public final static Integer STATUE_TALLY_CHECKPASS = 100;
    /*** 付款业务处理状态：101-记账作废暂存  */
    public final static Integer STATUE_TALLY_INVALID_TEMP = 101;
    /*** 付款业务处理状态：102-记账作废待复核  */
    public final static Integer STATUE_TALLY_INVALID_WAIT_CHECK = 102;
    /*** 付款业务处理状态：103-记账作废打回  */
    public final static Integer STATUE_TALLY_INVALID_BACK = 103;
    /*** 付款业务处理状态：104-记账作废复核通过  */
    public final static Integer STATUE_TALLY_INVALID_CHECKPASS = 104;
    /*** 单据来源：0-退款勾兑打回  */
    public final static String BILLFROM_0 = "0";
    /*** 单据来源：1-支付结算  */
    public final static String BILLFROM_1 = "1";
    /*** 单据来源：2-费用报销  */
    public final static String BILLFROM_2 = "2";
    /*** 单据来源：3-票据管理  */
    public final static String BILLFROM_3 = "3";
    /*** 单据来源：4-信贷管理 --内部拆借放款  */
    public final static String BILLFROM_4 = "4";
    /*** 单据来源：5-国际结算  */
    public final static String BILLFROM_5 = "5";
    /*** 单据来源：6-ERP系统  */
    public final static String BILLFROM_6 = "6";
    /*** 单据来源：7-退款勾兑  */
    public final static String BILLFROM_7 = "7";
    /*** 单据来源：8-信贷管理 --内部拆借还款  */
    public final static String BILLFROM_8 = "8";
    /*** 单据来源：9-信贷管理 --融资租赁租金支付  */
    public final static String BILLFROM_9 = "9";
    /*** 单据来源：9-信贷管理 --股权融资回购  */
    public final static String BILLFROM_10 = "10";
    /*** 单据来源：10-信贷管理 --债券还款支付  */
    public final static String BILLFROM_11 = "11";
    /*** 对私标志：0-否  */
    public final static String FORINDIVIDUAL_0 = "0";
    /*** 对私标志：1-是  */
    public final static String FORINDIVIDUAL_1 = "1";
    /*** 加急标志：0-否  */
    public final static String ISQUICK_0 = "0";
    /*** 加急标志：1-是  */
    public final static String ISQUICK_1 = "1";
    /*** 已付款标志：0-否  */
    public final static String ISPAYED_0 = "0";
    /*** 已付款标志：1-是  */
    public final static String ISPAYED_1 = "1";
    /*** 是否垫支：0-否  */
    public final static String ISADVANCE_0 = "0";
    /*** 是否垫支：1-是  */
    public final static String ISADVANCE_1 = "1";
    /*** 是否已开票：0-否  */
    public final static String ISOPENBILL_0 = "0";
    /*** 是否已开票：1-是  */
    public final static String ISOPENBILL_1 = "1";
    /*** 记账标志：y-不记账  */
    public final static String DIRECTPAYFLAG_Y = "y";
    /*** 记账标志：n-记账  */
    public final static String DIRECTPAYFLAG_N = "n";
    /*** 跨行标志：0-同行  */
    public final static String DIFBANK_0 = "0";
    /*** 跨行标志：1-他行  */
    public final static String DIFBANK_1 = "1";
    /*** 跨行标志：0-同城  */
    public final static String AREASIGN_0 = "0";
    /*** 跨行标志：1-异地  */
    public final static String AREASIGN_1 = "1";
    /*** 系统结算方式：01-转收  */
    public final static String PAY_TYPE_01 = "01";
    /*** 系统结算方式：02-转付  */
    public final static String PAY_TYPE_02 = "02";
    /*** 系统结算方式：04-现付  */
    public final static String PAY_TYPE_04 = "04";
    /*** 系统结算方式：05-内转  */
    public final static String PAY_TYPE_05 = "05";
    /*** 系统结算方式：06-电汇  */
    public final static String PAY_TYPE_06 = "06";
    /*** 系统结算方式：09-手续费  */
    public final static String PAY_TYPE_09 = "09";
    /*** 系统结算方式：10-拨付  */
    public final static String PAY_TYPE_10 = "10";
    /*** 系统结算方式：11-其他  */
    public final static String PAY_TYPE_11 = "11";
    /*** 系统结算方式：33-代付  */
    public final static String PAY_TYPE_33 = "33";
    /*** 系统结算方式：34-外付网银  */
    public final static String PAY_TYPE_34 = "34";
    /*** 系统结算方式：35-代理支付  */
    public final static String PAY_TYPE_35 = "35";
    /*** 系统结算方式：37-头寸调拨  */
    public final static String PAY_TYPE_37 = "37";
    /*** 系统结算方式：48-代发工资  */
    public final static String PAY_TYPE_48 = "48";
    /*** 系统结算方式：49-应付票据  */
    public final static String PAY_TYPE_49 = "49";
    /*** 系统结算方式：50-应收票据  */
    public final static String PAY_TYPE_50 = "50";
    /*** 系统结算方式：46-代客结汇  */
    public final static String PAY_TYPE_46 = "46";
    /*** 系统结算方式：47-代客购汇  */
    public final static String PAY_TYPE_47 = "47";
    /*** 系统结算方式：56-自营结汇  */
    public final static String PAY_TYPE_56 = "56";
    /*** 系统结算方式：57-自营购汇  */
    public final static String PAY_TYPE_57 = "57";
    /*** 系统结算方式：62-现金支票  */
    public final static String PAY_TYPE_62 = "62";
    /*** 系统结算方式：63-转账支票  */
    public final static String PAY_TYPE_63 = "63";
    /*** 系统结算方式：30-活期转定期  */
    public final static String PAY_TYPE_30 = "30";
    /*** 系统结算方式：31-活期转通知  */
    public final static String PAY_TYPE_31 = "31";
    /*** 系统结算方式：58-定期转活期  */
    public final static String PAY_TYPE_58 = "58";
    /*** 系统结算方式：59-定期转通知  */
    public final static String PAY_TYPE_59 = "59";
    /*** 系统结算方式：T-转账  */
    public final static String SIGN_TRAN = "T";
    /*** 系统结算方式：C-现金  */
    public final static String SIGN_CASH = "C";
    /*** 是否要记账：0-否  */
    public final static String ISACCOUNT_0 = "0";
    /*** 是否要记账：1-是  */
    public final static String ISACCOUNT_1 = "1";
    /*** 记账模式：0-手动记账  */
    public final static String ACCOUNTMODE_MANUAL = "0";
    /*** 记账模式：1-自动记账  */
    public final static String ACCOUNTMODE_AUTO = "1";
    /*** 记账顺序：0-先记账后付款  */
    public final static String ACCOUNTORDER_ACCOUNTPAY = "0";
    /*** 记账顺序：1-先付款后记账  */
    public final static String ACCOUNTORDER_PAYACCOUNT = "1";
	/** * 拆分数据！ */
	public static final String NETBANK_LOG_SPLIT_SUCC = "netbank.log.split.succ";
	/** * 撤销拆分数据！ */
	public static final String NETBANK_LOG_SPLIT_CANCEL_SUCC = "netbank.log.split.cancel.succ";
	/** * 合并数据！ */
	public static final String NETBANK_LOG_MERGE_SUCC = "netbank.log.merge.succ";
	/** * 撤销合并数据！ */
	public static final String NETBANK_LOG_MERGE_CANCEL_SUCC = "netbank.log.merge.cancel.succ";
	/** * 费用报销汇总打回失败明细！ */
	public static final String NETBANK_LOG_BACKDETAIL_SUCC = "netbank.log.backdetail.succ";
	/** * 付款执行批量打回！ */
	public static final String NETBANK_LOG_BATCH_EXECUTE_BACK_SUCC = "netbank.log.batch.execute.back.succ";
	/** * 付款执行打回！ */
	public static final String NETBANK_LOG_EXECUTE_BACK_SUCC = "netbank.log.execute.back.succ";
	/** * 付款执行发送！ */
	public static final String NETBANK_LOG_EXECUTE_SEND_SUCC = "netbank.log.execute.send.succ";
	/** * 手工记账打回！ */
	public static final String NETBANK_LOG_CMSTALLY_BACK_SUCC = "netbank.log.cmstally.back.succ";
    /** * 退款勾兑打回！ */
    public static final String NETBANK_LOG_REFUNDBLENDING_BACK_SUCC = "netbank.log.refundblending.back.succ";
	/*** 打回类型：1-支付结算打回  */
	public final static String BACK_TYPE_1 = "1";
	/*** 打回类型：2-付款执行打回  */
	public final static String BACK_TYPE_2 = "2";
	/*** 打回类型：3-付款执行批量打回  */
	public final static String BACK_TYPE_3 = "3";
	/*** 打回类型：4-手工记账打回  */
	public final static String BACK_TYPE_4 = "4";
    /*** 打回类型：5-退款勾兑打回  */
    public final static String BACK_TYPE_5 = "5";
    /*** 单据来源：1-收款管理  */
    public final static String RECEIVABLE_BILLFROM_1 = "1";
    /*** 单据来源：2-流水认领  */
    public final static String RECEIVABLE_BILLFROM_2 = "2";
    /*** 单据来源：3-ERP系统  */
    public final static String RECEIVABLE_BILLFROM_3 = "3";
    /*** 单据来源：4-应收核销  */
    public final static String RECEIVABLE_BILLFROM_4 = "4";

    /*** 已收款*/
    public final static Integer RECEIVABLE_STATUE_ISREC = 100;
    /*** 优先策略：1-同行优先  */
    public final static String POLICY_TYPE_1 = "1";
    /*** 打回类型：2-小额优先  */
    public final static String POLICY_TYPE_2 = "2";
    /*** 打回类型：3-早制单优先  */
    public final static String POLICY_TYPE_3 = "3";
    /*** 打回类型：4-手工记账打回  */
    public final static String POLICY_TYPE_4 = "4";
    /*** 打回类型：5-退款勾兑打回  */
    public final static String POLICY_TYPE_5 = "5";
    /**
     * 是否启用用户分配付款方式 0 否，1是
     */
    public final static String ISENABLEUSERASSIGN_0 = "0";
    public final static String ISENABLEUSERASSIGN_1 = "1";
}
