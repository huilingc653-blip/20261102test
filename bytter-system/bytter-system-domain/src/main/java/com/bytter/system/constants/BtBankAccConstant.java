package com.bytter.system.constants;
import java.util.Calendar;
import java.util.Date;
/**
 * 银行账户常量类
 * @author zuoy
 * @since 2017-10-31
 */
public class BtBankAccConstant {
	
	/*** 账户管理参数配置：bankAccEndDateAlertDays-账户到期提醒提前天数 */
	public static final String BANK_ACC_ENDDATE_ALERT_DAYS = "bankAccEndDateAlertDays";
	/*** 账户年检到期提醒提前天数 */
	public static final String FCS_CHECK_EXPIRE_DATE_ALERT_DAYS = "checkExpireDateAlertDays";
	/*** 网银信息到期提醒提前天数 */
	public static final String NET_UKEY_DATE_ALERT_DAYS = "netUKeyDateAlertDays";
	/*** 招行接口代码初始化ID */
	public static final String BIS_BIF_CODE_CMB = "402880385c20008b015c2025c382001d";
	/**
	 * 暂存
	 */
	public final static Integer STATUE_TEMP = 0;
	/**
	 * 打回
	 */
	public final static Integer STATUE_BACK = -1;
	/**
	 * 删除
	 */
	public final static Integer STATUE_DEL = -2;
	/**
	 * 待审批
	 */
	public final static Integer STATUE_WAIT_CHECK = 11;
	/**
	 * 审批通过
	 */
	public final static Integer STATUE_CHECK = 95;
	/**
	 * 办理完成
	 */
	public final static Integer STATUE_CANCELLATION = 100;
	/**
	 * 是否有效
	 */
	/**
	 * 无效
	 */
	public final static String VALIDSIGN_0 = "0";
	/**
	 * 有效
	 */
	public final static String VALIDSIGN_1 = "1";
	
	/**
	 * 是否在线
	 */
	/**
	 * 离线
	 */
	public final static String ISONLINE_0 = "0";
	/**
	 * 在线
	 */
	public final static String ISONLINE_1 = "1";
	/**
	 * 线上线下标记
	 */
	/**
	 * 线下
	 */
	public final static String CIRCULATIONMODE_0 = "0";
	/**
	 * 线上
	 */
	public final static String CIRCULATIONMODE_1 = "1";
	/**
	 * 是否资金池账号
	 */
	/** 离线*/
	public final static String ISCAPITALPOOL_0 = "0";
	/** 在线*/
	public final static String ISCAPITALPOOL_1 = "1";
	/**
	 * 业务类型
	 */
	/**
	 * 开户申请
	 */
	public final static String BUSTYPE_1 = "1";
	/**
	 * 变更申请
	 */
	public final static String BUSTYPE_2 = "2";
	/**
	 * 注销申请
	 */
	public final static String BUSTYPE_3 = "3";
	/**
	 * 父子标志
	 */
	/**
	 * 子账号
	 */
	public final static Integer PARENTSIGN_0 = 0;
	/**
	 * 父账号
	 */
	public final static Integer PARENTSIGN_1 = 1;
	/**
	 *
	 */
	public static String BANKACCLIST_EXCEPTION = "参数设置错误";
	/** 冻结类型 */
	/**
	 * 冻结
	 */
	public final static Integer UFFLAG_0 = 0;
	/**
	 * 解冻
	 */
	public final static Integer UFFLAG_1 = 1;
	/**
	 * 是否票据专户  1-是
	 */
	public final static String ELECTRICBILL_YES = "1";
	/**
	 * 是否票据专户 0-否
	 */
	public final static String ELECTRICBILL_NO = "0";
	/**
	 * 是
	 */
	public final static String YES = "1";
	/**
	 * 否
	 */
	public final static String NO = "0";
	/**
	 * 是
	 */
	public final static String YES_ZN = "是";
	/**
	 * 否
	 */
	public final static String NO_ZN = "否";
	/**
	 * 计息周期 月
	 */
	public final static String INTERESTCYCLE_MONTH_ZH = "月";
	/**
	 * 计息周期 季
	 */
	public final static String INTERESTCYCLE_SEASON_ZH = "季";
	/**
	 * 计息周期 无
	 */
	public final static String INTERESTCYCLE_NONE_ZH = "无";
	/**
	 * 计息周期 月
	 */
	public final static Integer INTERESTCYCLE_MONTH = 1;
	/**
	 * 计息周期 季
	 */
	public final static Integer INTERESTCYCLE_SEASON = 0;
	/**
	 * 计息周期 无
	 */
	public final static Integer INTERESTCYCLE_NONE = -1;
	/*** 账户类别：01-活期  */
	public final static String ACCTYPE_01 = "01";
	/*** 账户类别：02-定期  */
	public final static String ACCTYPE_02 = "02";
	/*** 账户类别：03-贷款户  */
	public final static String ACCTYPE_03 = "03";
	/*** 账户类别：04-保证金   */
	public final static String ACCTYPE_04 = "04";
	/*** 账户类别：05-专用户  */
	public final static String ACCTYPE_05 = "05";
	/*** 账户类别：06-通知  */
	public final static String ACCTYPE_06 = "06";
	/*** 账户类别：07-协定  */
	public final static String ACCTYPE_07 = "07";
	/*** 账户类别：09-虚拟户  */
	public final static String ACCTYPE_09 = "09";
	/*** 账户类别：10-票据专户  */
	public final static String ACCTYPE_10 = "10";
	/**
	 * 是否相关联
	 */
	public final static String RELATION_YES = "1";
	/**
	 * 是否相关联
	 */
	public final static String RELATION_NO = "0";
	/**
	 * 是否相关联
	 */
	public final static String FORM_APP = "app";
	/**
	 * 是否相关联
	 */
	public final static String FORM_ACC = "acc";
	/*** 勾兑规则：1-金额  */
	public final static String PAYMENTBLENDING_1 = "1";
	/*** 勾兑规则：2-付款账号  */
	public final static String PAYMENTBLENDING_2 = "2";
	/*** 勾兑规则：3-收款账号  */
	public final static String PAYMENTBLENDING_3 = "3";
	/*** 勾兑规则：4-交易日期  */
	public final static String PAYMENTBLENDING_4 = "4";
	/*** 勾兑规则：5-币别  */
	public final static String PAYMENTBLENDING_5 = "5";
	/*** 勾兑标志：0-未勾兑  */
	public final static String BLENDSIGN_0 = "0";
	/*** 勾兑标志：1-已勾兑  */
	public final static String BLENDSIGN_1 = "1";
	/*** 勾兑标志：2-已作废  */
	public final static String BLENDSIGN_2 = "2";
	/*** 勾兑标志：3-勾兑待确认  */
	public final static String BLENDSIGN_3 = "3";
    /*** 原单处理方式：1-不处理原单  */
	public final static String BLENDTYPE_1 = "1";
    /*** 原单处理方式：2-生成新单  */
	public final static String BLENDTYPE_2 = "2";
    /*** 原单处理方式：3-打回原单  */
	public final static String BLENDTYPE_3 = "3";
	/*** 账户状态：1-正常 */
	public final static String ACCSTATUS_1 = "1";
	/*** 账户状态：-2已销户 */
	public final static String ACCSTATUS_2 = "-2";
	/**
	 * 斜杠
	 */
	public static final String SLASH = "/";
	/**
	 * 横杠Horizontal
	 */
	public static final String HORIZONTAL = "-";
	/**
	 * 分隔符
	 */
	public static final String DELIMITER_D_COLON="::";
	/**
	 * 账户类别 字典DEY btBankAcc.accType
	 */
	public static final String BANKACC_ACCTYPE_DICT = "btBankAcc.accType";
	/**
	 * 境内/境外账号 字典DEY btBankAcc.isDomesticBank
	 */
	public static final String BANKACC_ISDOMESTICBANK_DICT = "btBankAcc.isDomesticBank";

	/**
	 * 销户原因
	 */
	public final static String CANCEL_REASON = "系统自动销户";

	/**
	 * 是否存在附件 0-不存在 1-存在
	 */
	public static final String ATTACHMENT_SIGN_0 = "0";
	public static final String ATTACHMENT_SIGN_1 = "1";

	/**
	 * 银行类别 - 第三方账号id
	 */
	public static final String BANK_TYPE_THIRD_PARTY = "6fa60c628a7e4b99a47ddfad9fa41577";
}
