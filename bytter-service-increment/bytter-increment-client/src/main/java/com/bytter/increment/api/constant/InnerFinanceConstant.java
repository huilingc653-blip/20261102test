package com.bytter.increment.api.constant;
/**
 * 内部理财常量类
 * @author huangx
 * @since 2019/11/1.
 */
public class InnerFinanceConstant {
    /** 内部存款参数配置：choInner-内部存款 */
    public static final String CHO_INNER_CONFIG_TYPE = "choInner";
    /** 内部存款参数配置：regularOnset-定期起存金额 */
    public static final String CHO_INNER_CONFIG_REGULARONSET = "regularOnset";
    /** 内部存款参数配置：regularKeep-定期留存金额 */
    public static final String CHO_INNER_CONFIG_REGULARKEEP = "regularKeep";
    /** 内部存款参数配置：noticeOnset-通知起存金额 */
    public static final String CHO_INNER_CONFIG_NOTICEONSET = "noticeOnset";
    /** 内部存款参数配置：noticeKeep-通知留存金额 */
    public static final String CHO_INNER_CONFIG_NOTICEKEEP = "noticeKeep";
    /** 内部存款参数配置：agreedDepositAlertDays-协定存款到期提醒天数 */
    public static final String CHO_INNER_CONFIG_AGREEDDEPOSITALERTDAYS = "agreedDepositAlertDays";
    /** 内部存款参数配置：regularAlertDays-定期存款到期提醒天数 */
    public static final String CHO_INNER_CONFIG_REGULARALERTDAYS = "regularAlertDays";
    /** 内部存款参数配置：noticeAlertDays-通知存款到期提醒天数 */
    public static final String CHO_INNER_CONFIG_NOTICEALERTDAYS = "noticeAlertDays";
    /** 内部存款参数配置：isAutoAccount-是否自动记账 */
    public static final String CHO_INNER_CONFIG_ISAUTOACCOUNT = "isAutoAccount";
    /** 内部理财处理状态：0-暂存  */
  	public final static Integer STATUE_TEMP = 0;
  	/** 内部理财处理状态：1-打回  */
  	public final static Integer STATUE_BACK = -1;
  	/** 内部理财处理状态：-2-删除  */
  	public final static Integer STATUE_DEL = -2;
  	/** 内部理财处理状态：11-待审批  */
  	public final static Integer STATUE_WAIT_CHECK = 11;
  	/** 内部理财处理状态：95-审批通过  */
  	public final static Integer STATUE_CHECK = 95;
    /** 内部理财处理状态：96-记账暂存  */
    public final static Integer STATUE_TALLY_TEMP = 96;
    /** 内部理财处理状态：97-记账待复核  */
    public final static Integer STATUE_TALLY_WAIT_CHECK = 97;
    /** 内部理财处理状态：98-记账复核打回  */
    public final static Integer STATUE_TALLY_BACK = 98;
    /** 内部理财处理状态：99-改账打回  */
    public final static Integer STATUE_TALLY_CHANG_BACK = 99;
    /** 内部理财处理状态：100-记账复核通过  */
    public final static Integer STATUE_TALLY_CHECKPASS = 100;
    /** 内部理财处理状态：-3-取消预约  */
    public final static Integer STATUE_CANCEL = -3;
    /** 内部理财处理状态：101-已取款  */
    public final static Integer STATUE_COMPLETE = 101;
  	/** 业务类型：10-活期转定期  */
    public final static Integer BUS_TYPE_C2T = 10;
    /** 业务类型：11-定期转活期  */
    public final static Integer BUS_TYPE_T2C = 11;
    /** 业务类型：12-活期转通知  */
    public final static Integer BUS_TYPE_C2N = 12;
    /** 业务类型：13-通知转活期  */
    public final static Integer BUS_TYPE_N2C = 13;
    /** 业务类型：14-预约通知  */
    public final static Integer BUS_TYPE_N2B = 14;
    /** 业务类型：15-取消预约通知  */
    public final static Integer BUS_TYPE_B2N = 15;
    /** 期限：3M-3个月  */
    public final static String LIMIT_TIME_3M = "3M";
    /** 期限：6M-6个月  */
    public final static String LIMIT_TIME_6M = "6M";
    /** 期限：1Y-1年  */
    public final static String LIMIT_TIME_1Y = "1Y";
    /** 期限：2Y-2年  */
    public final static String LIMIT_TIME_2Y = "2Y";
    /** 期限：3Y-3年  */
    public final static String LIMIT_TIME_3Y = "3Y";
    /** 期限：5Y-5年  */
    public final static String LIMIT_TIME_5Y = "5Y";
    /** 到期处理方式：0-自动终止  */
    public final static String CERTIFICATE_WAY_0 = "0";
    /** 到期处理方式：1-本金续存  */
    public final static String CERTIFICATE_WAY_1 = "1";
    /** 到期处理方式：2-本息续存  */
    public final static String CERTIFICATE_WAY_2 = "2";
    /**协定存款相关字段*/
    /*** 协定单据状态：10-登记  */
    public final static Integer BILL_STATUS_10 = 10;
    /*** 协定单据状态：11-到期处理/终止  */
    public final static Integer BILL_STATUS_11 = 11;
    /*** 单据类型：0-登记  */
    public final static String BUS_TYPE_0 = "0";
    /*** 单据类型：1-到期处理  */
    public final static String BUS_TYPE_1 = "1";
    /*** 单据类型：2-终止  */
    public final static String BUS_TYPE_2 = "2";
    /*** 资金池标识 0-非资金池*/
    public final static String POOL_SIGN_0 = "0";
    /*** 资金池标识 1-资金池*/
    public final static String POOL_SIGN_1 = "1";
    /*** 到期处理方式 0-自动延期*/
    public final static String AGR_CERTIFICATE_WAY_0 = "0";
    /*** 到期处理方式 0-自动到期*/
    public final static String AGR_CERTIFICATE_WAY_1 = "1";
    /*** 年计算天数*/
    public final static Integer YEAR_DAYS = 360;
}
