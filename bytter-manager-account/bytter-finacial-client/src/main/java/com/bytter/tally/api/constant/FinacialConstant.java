package com.bytter.tally.api.constant;
import com.bytter.core.constant.ConstantBean;
/**
 * 核算模块常量类
 * @author liyh
 * @since 2011-8-21
 */
public class FinacialConstant {
	/**
	 * 余额方向-借方
	 */
	public static final String BALANCE_WAY_DEBIT = "1";
	/**
	 * 余额方向-贷方
	 */
	public static final String BALANCE_WAY_CREDIT = "-1";
	/**
	 * 有效标识-有效
	 */
	public static final String VALID_TRUE = "1";
	/**
	 * 有效标识-无效
	 */
	public static final String VALID_FALSE = "0";
	/**
	 * 是否标识-是
	 */
	public static final String BOOLEAN_TRUE = "1";
	/**
	 * 是否标识-否
	 */
	public static final String BOOLEAN_FALSE = "0";
	/**
	 * 对账类型-银行对账
	 */
	public static final String CHECK_TYPE_BANK = "3";
	/**
	 * 对账类型-ERP对账
	 */
	public static final String CHECK_TYPE_ERP = "2";
	/**
	 * 对账类型-中心对账
	 */
	public static final String CHECK_TYPE_CEN = "1";
	/**
	 * 红蓝标识 - 红
	 */
	public static final String REDBLUE_SIGN_RED = "R";
	/**
	 * 红蓝标识 - 蓝
	 */
	public static final String REDBLUE_SIGN_BLUE = "B";
	/**
	 * 输入类型 - 手工
	 */
	public static final String IMPORT_TYPE_HAND = "1";
	/**
	 * 输入类型 - 文本导入
	 */
	public static final String IMPORT_TYPE_FILE = "2";
	/**
	 * 输入类型 - 接口导入
	 */
	public static final String IMPORT_TYPE_INTERFACE = "3";
	/**
	 * 输入类型 - 未达账初始化
	 */
	public static final String IMPORT_TYPE_FILE_UNSTAY = "4";
	/**输入类型 - 手工*/
	public static final String CTR_IMPORT_TYPE_HAND = "2";
	/**输入类型 - 柜台*/
	public static final String CTR_IMPORT_TYPE_DEFAULT = "1";
	/**
	 * 对账标志 - 未对账
	 */
	public static final String COMPARE_SIGN_NO = "0";
	/**
	 * 对账标志 - 已对账
	 */
	public static final String COMPARE_SIGN_YES = "1";
	/**
	 * 对账标志 - 手工勾兑
	 */
	public static final String COMPARE_SIGN_HAND = "2";
	/**
	 * 对账标志 - 已结转
	 */
	public static final String COMPARE_SIGN_OVER = "3";
	/**
	 * 对账标志 - 本期未达
	 */
	public static final String COMPARE_SIGN_STAY = "4";
	/**
	 * 对账标志 - 未达勾兑
	 */
	public static final String COMPARE_STAY_HAND = "5";
	/**
	 * 对账标志 - 上期未达
	 */
	public static final String COMPARE_SIGN_STAY_UP = "6";
	/**
	 * 冲账标识 - 未冲账
	 */
	public static final String RUSH_SIGN_NO = "0";
	/**
	 * 冲账标识 - 已冲账
	 */
	public static final String RUSH_SIGN_YES = "1";
	/**
	 * 红蓝标识 - 蓝(正常)
	 */
	public static final String RB_SIGN_B = "B";
	/**
	 * 红蓝标识 - 蓝(冲销)
	 */
	public static final String RB_SIGN_R = "R";
	/**
	 * 手续费计提标志 - 未计提
	 */
	public static final String CHARGED_SIGN_NO = "0";
	/**
	 * 手续费计提标志 - 已计提
	 */
	public static final String CHARGED_SIGN_YES = "1";
	/**
	 * 冲账标识 - 未冲
	 */
	public static final String RUSH_SIGN_N = "0";
	/**
	 * 手续费计提标志 - 已冲
	 */
	public static final String RUSH_SIGN_Y = "1";
	/**
	 * 记账标识-已记账
	 */
    public final static String TALLY_SIN_Y = "1";
    /**
	 * 记账标识-未记账
	 */
    public final static String TALLY_SIN_N = "0";
    /**
     * 费用类型计算方法： 取固定值
     */
    public static final String CALC_TYPE_FIXED = "10";
    /**
     * 费用类型计算方法：按比例
     */
    public static final String CALC_TYPE_SCALE = "20";
    /**
     * 加急标识：是
     */
    public static final String EMERGENCYSIGN_F = "F";
    /**
     * 加急标识：否
     */
    public static final String EMERGENCYSIGN_G = "G";
    /**
     * 费用明细所属类型-10 费用计提维护
     */
    public static final Integer BELONG_TYPE_CHARGE = 10;
    /**
     * 费用明细所属类型- 20 业务规则定义
     */
    public static final Integer BELONG_TYPE_BUSINESS = 20;
    /**
     * 报表格式定义左右-左
     */
    public static final String REPORT_DIRECTION_L = "1";
    /**
     * 报表格式定义左右-右
     */
    public static final String REPORT_DIRECTION_R = "2";
    /**
     * 报表格式定义-资产负债表
     */
    public static final String REPORT_TYPE_1 = "1";
    /**
     * 报表格式定义-损益表
     */
    public static final String REPORT_TYPE_2 = "2";
    /**
     * 报表格式定义-现金流量表
     */
    public static final String REPORT_TYPE_3 = "3";
    /**其他*/
    public static final Integer ENTRY_TYPE_10 = 10;
    /**预提*/
    public static final Integer ENTRY_TYPE_20 = 20;
    /**冲预提*/
    public static final Integer ENTRY_TYPE_30 = 30;
    /**结息*/
    public static final Integer ENTRY_TYPE_40 = 40;
    /**
     * 利率调整类型
     */
    public static final String INSTRATE_CHANGT_YPE = "1";
    /**
     * 账务操作日志类型 - 正常
     */
    public static final String FINACIAL_MSGTYPE_N = "N";
    /**
     * 账务操作日志类型 - 作废
     */
    public static final String FINACIAL_MSGTYPE_B = "B";
    /**
     * 账务操作日志类型 - 冲账
     */
    public static final String FINACIAL_MSGTYPE_R = "R";
    /**计提记录状态 -0：删除*/
    public static final String CHARGE_STATUS_0 = "0";
    /**计提记录状态 -1：正常*/
    public static final String CHARGE_STATUS_1 = "1";
    /**
     * 账务操作日志-状态
     * 暂存
     */
    public static final String FINACIAL_MSGSTATUS_0 = "00";
    /**修改*/
    public static final String FINACIAL_MSGSTATUS_1 = "01";
    /**提交*/
    public static final String FINACIAL_MSGSTATUS_2 = "02";
    /**抹账*/
    public static final String FINACIAL_MSGSTATUS_3 = "03";
    /**复核打回*/
    public static final String FINACIAL_MSGSTATUS_4 = "04";
    /**复核通过*/
    public static final String FINACIAL_MSGSTATUS_5 = "05";
    /**
     * 账务处理标识 0-已作废或已冲账 1：作废中 2：冲账中 ,3可作废或可冲账
     */
    public static final String DEAL_FLAG_0 = "0";
    public static final String DEAL_FLAG_1 = "1";
    public static final String DEAL_FLAG_2 = "2";
    public static final String DEAL_FLAG_3 = "3";
    /**
     * 未日结标识
     */
    public static final String OVER_SIGN_NO = "0";
    /**
	 * 已日结标识
	 */
	public static final String OVER_SIGN_YES = "1";
	/**日结类型*/
	public static final String DATE_KNOT="日";
	/**日结类型*/
	public static final String MONTH_KNOT="月";
	/**
	 * 录入页面
	 */
	public static final String PAGE_INIT = "add";
	/**
	 * 修改页面
	 */
	public static final String PAGE_EDIT = "edit";
	/**
	 * 复核页面
	 */
	public static final String PAGE_CHECK = "check";
	/**
	 * 查询页面
	 */
	public static final String PAGE_QUERY = "query";
	/**
	 * 方法：暂存
	 */
	public static final String METHOD_SAVE = "save";
	/**
	 * 方法：提交
	 */
	public static final String METHOD_SUBMIT = "submit";
	/**
	 * 方法：复核
	 */
	public static final String METHOD_CHECK = "check";
	/**
	 * 方法：打回
	 */
	public static final String METHOD_BACK = "back";
	/**
	 * 方法：删除
	 */
	public static final String METHOD_DELETE = "delete";
	/**
	 * 核对标准
	 * 1：按发生额核对
	 * 2：按发生额和票据号核对
	 * 3：按时间与发生额核对
	 * 4：按时间发生额票据号核对
	 */
	public static class CheckType {
		public final static ConstantBean<String> TYPE_MONEY = new ConstantBean<String>("按发生额核对", "1");
		public final static ConstantBean<String> TYPE_MONEY_BILLNO = new ConstantBean<String>("按发生额和票据号核对", "2");
		public final static ConstantBean<String> TYPE_TIME_MONEY = new ConstantBean<String>("按时间与发生额核对", "3");
		public final static ConstantBean<String> TYPE_TIME_MONEY_BILLNO = new ConstantBean<String>("按时间发生额票据号核对", "4");
		public final static ConstantBean<String> TYPE_VOUCHERNO = new ConstantBean<String>("按凭证号核对", "5");
		public final static ConstantBean<String> TYPE_MONEY_SAP_VOUCHERNO = new ConstantBean<String>("按发生额和SAP凭证号核对", "6");
		public final static ConstantBean<String> TYPE_TIME_ABS_MONEY = new ConstantBean<String>("按时间与摘要与发生额核对", "7");
		public final static ConstantBean<String> TYPE_TIME_ACC_ABS_MONEY = new ConstantBean<String>("按时间+付款方账号+收款方账号+摘要+发生额核对", "8");
	}
    /**
     * 电子账务记账自动复核
     */
    public static final String TALLY_CHECK = "tally_check";
    /**
     * 自动记账人
     */
    public static final String AUTO_ACCOUNTER = "autoAccounter";
    /**
     * 自动复核人
     */
    public static final String AUTO_CHECKER = "autoChecker";
    /**
     * 记账凭证号生成规则
     */
    public static final String VOUCHER_NO_RULE = "voucherNoRule";
    /**
     * 记账凭证起息日期是否为记账日期
     */
    public static final String INTEREST_START_DATE_RULE = "interestStartDateRule";
	/** 已删除*/
	public static final int DELETED = -2;
	/**定时任务执行serviceBean**/
	public final static String FC_AUTOCHECK_TASKCOMMAND="com.bytter.check.service.FcAutoCheckConfigService";
}
