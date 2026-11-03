package com.bytter.biz.finacial.api.constant;
import com.bytter.core.constant.ConstantBean;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 核算模块资源常量类
 * @author liyh
 * @since 2011-8-21
 */
public class InterestConstant implements Serializable{
	/**
	 * 利息状态-暂存
	 */
	public static final Integer INTEREST_STATUE_TEMP = 0;
	/**
	 * 利息状态-待审批
	 */
	public static final Integer INTEREST_WAIT_CHECK = 11;
	/**
	 * 利息状态-打回
	 */
	public static final Integer INTEREST_BACK = -1;
	/**
	 * 利息状态-删除
	 */
	public static final Integer INTEREST_STATUE_DELETE = -2;
	/**
	 * 利息状态-审批通过
	 */
	public static final Integer INTEREST_STATUE_CHECK = 95;
    /**
     * 计息摘要-月
     */
    public static final String FINACIAL_INTEREST_MONTH = "finacial.interest.month";
    /**
     * 计息摘要-余额
     */
    public static final String FINACIAL_INTEREST_SUMMARY = "finacial.interest.summary";
    /**
     * 计息摘要-余额
     */
    public static final String FINACIAL_INTEREST_ACCOUNT = "finacial.interest.account";
    /**
     * 资金池计息-账户类型凭证对应
     */
    public static Map<String, String> POOL_INTEREST_ACCTYPE_VOUCHER_MAPPING = new HashMap<String, String>();
    static {
        POOL_INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT, "finacial.interest.poolNet.current");
    }
    /**
     * 计息-账户类型凭证对应
     */
	public static Map<String, String[]> INTEREST_ACCTYPE_VOUCHER_MAPPING = new HashMap<String, String[]>();
    static {
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT, new String[] { "借 利息支出-企业活期存款", "贷 企业活期存款" });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BANK_DEMAND, new String[] { "借 应收利息-银行活期存款", "贷 利息收入-银行活期存款" });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_LOAN, new String[] { "借 企业活期存款", "贷 利息收入-企业放款" });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BANK_BORROW, new String[] { "借 利息支出-企业活期存款", "贷 企业活期存款" });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BUSINESS_BEGULAR, new String[] { "借 利息支出-企业定期存款", "贷 企业定期存款" });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BANK_BEGULAR, new String[] { "借 应收利息-银行定期存款", "贷 利息收入-银行定期存款" });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_INNER_ACCOUNT, new String[] { "收 待收利息复利", "XXXX" });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BANK_DEPOSIT, new String[] { "借 利息支出-通知存款", "贷 应付利息-企业通知存款" });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_LOANBYMANDATE, new String[] { "借 企业活期存款—借款方", "贷 企业活期存款—委托方" });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_AGREEMENT, new String[] { "借 利息支出-企业协定存款", "贷 企业活期存款" });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_ENTRUSTED_DEPOSIT, new String[] { "收 待收利息复利", " " });
        INTEREST_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_OFFBALANCE, new String[] { "借 XXXXX", "贷 XXXXX" });
    }
    /**
     * 预提-账户类型凭证对应
     */
    public static Map<String, String[]> PREDRAW_ACCTYPE_VOUCHER_MAPPING = new HashMap<String, String[]>();
    static {
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT, new String[] { "借 利息支出-企业活期存款", "贷 应付利息-企业活期存款" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BANK_DEMAND, new String[] { "借 应收利息-银行活期存款", "贷 利息收入-银行活期存款" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_LOAN, new String[] { "借 应收利息-企业放款", "贷 利息收入-企业放款" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BANK_BORROW, new String[] { "借 利息支出-银行借款", "贷 应付利息-银行借款" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BUSINESS_BEGULAR, new String[] { "借 利息支出-企业定期存款", "贷 应付利息-企业定期存款" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BANK_BEGULAR, new String[] { "借 应收利息-银行定期存款", "贷 利息收入-银行定期存款" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_INNER_ACCOUNT, new String[] { "借 XXXXX", "贷 XXXXX" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_BANK_DEPOSIT, new String[] { "借 利息支出-通知存款", "贷 应付利息-企业通知存款" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_LOANBYMANDATE, new String[] { "借 XXXXX", "贷 XXXXX" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_AGREEMENT, new String[] { "借 利息支出-企业协定存款", "贷 应付利息-企业协定存款" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_ENTRUSTED_DEPOSIT, new String[] { "借 XXXXX", "贷 XXXXX" });
        PREDRAW_ACCTYPE_VOUCHER_MAPPING.put(InnerAccountConstant.FCACC_OFFBALANCE, new String[] { "借 XXXXX", "贷 XXXXX" });
    }
    /**
     * 结息方式
     */
    public static class PredrawSign {
        public static final ConstantBean<String> PREDRAW_SIGN_SEASON_ANTICIPATE = new ConstantBean<String>("季末结息预提",
                "1");
        public static final ConstantBean<String> PREDRAW_SIGN_SEASON_NO_ANTICIPATE = new ConstantBean<String>(
                "季末结息不预提", "2");
        public static final ConstantBean<String> PREDRAW_SIGN_MONTH_INTEREST = new ConstantBean<String>("每月结息", "3");
        public static final ConstantBean<String> PREDRAW_SIGN_NO_SEASON_ANTICIPATE = new ConstantBean<String>(
                "季末不结息预提", "4");
        public static final ConstantBean<String> PREDRAW_SIGN_NO_SEASON_NO_ANTICIPATE = new ConstantBean<String>(
                "季末不结息不预提", "5");
    }
    /**
     * 利率类型-正常利率
     */
    public static final String INSTRT_TYPE_NORMAL = "1";
    /**
     * 利率类型-逾期利率
     */
    public static final String INSTRT_TYPE_OVERDUE = "2";
    /**
     * 利率类型-超期利率
     */
    public static final String INSTRT_TYPE_EXCEED = "3";
    /**
     * 利率类型-透支利率
     */
    public static final String INSTRT_TYPE_OVERDRAFT = "4";
    /**
     * 利率类型-协定/大额
     */
    public static final String INSTRT_TYPE_AGREEMENT = "5";
    /**
     * 利率类型-提前利率
     */
    public static final String INSTRT_TYPE_AHEAD = "6";
    /**
     * 利率类型-展期利率
     */
    public static final String INSTRT_TYPE_DELAY = "7";
    /**
     * 季末结息预提
     */
    public static final String PREDRAW_SIGN_SEASON_ANTICIPATE = "1";
    /**
     * 季末结息不预提
     */
    public static final String PREDRAW_SIGN_SEASON_NO_ANTICIPATE = "2";
    /**
     * 每月结息
     */
    public static final String PREDRAW_SIGN_MONTH_INTEREST = "3";
    /**
     * 季末不结息预提
     */
    public static final String PREDRAW_SIGN_NO_SEASON_ANTICIPATE = "4";
    /**
     * 季末不结息不预提
     */
    public static final String PREDRAW_SIGN_NO_SEASON_NO_ANTICIPATE = "5";
    /**
     * 计息的年天数和月天数
     */
    public static final int DAY_OF_YEAR = 360;
    public static final int DAY_OF_MONTH = 30;
    /**
     * 利率转换的除数
     */
    /**年利率转日利率的除数*/
    public static final long RATE_YEAR_DIV = DAY_OF_YEAR;
    /**月利率转日利率的除数*/
    public static final long RATE_MONTH_DIV = DAY_OF_MONTH;
	/**
     * 入息标志--结息
     */
    public static final String INTEREST_SIGN_IST = "1";
    /**
     * 入息标志--预提
     */
    public static final String INTEREST_SIGN_PRE = "0";
    /**
     * 预提冲预提方式-不控制
     */
    public static final String PREDRAW_RUSH_WAY_NONE = "0";
    /**
     * 预提冲预提方式-计息日预提冲销
     */
    public static final String PREDRAW_RUSH_WAY_INTEREST_DAY = "1";
    /**
     * 预提冲预提方式-月末预提月初冲销
     */
    public static final String PREDRAW_RUSH_WAY_MONTH_END = "2";
    /**
     * 批量结息方式-不控制
     */
    public static final String BATCH_INST_WAY_NONE = "0";
    /**
     * 批量结息方式-计息日结息
     */
    public static final String BATCH_INST_WAY_INTEREST_DAY = "1";
    /**
     * 定期计息方式-不分段
     */
    public static final String INST_FIX_NONE = "0";
    /**
     * 定期计息方式-对年对月
     */
    public static final String INST_FIX_YEAR_MONTH = "1";
    /**
     * 定期计息方式-实际天数
     */
    public static final String INST_FIX_DAYS = "2";
    /**
     * 预提标志-预提的部分
     */
    public static final String PREDRAW_SIGN_PDW = "1";
    /**
     * 预提标志-结息不足的部分
     */
    public static final String PREDRAW_SIGN_NOT = "2";
    /**
     * 预提标志-结息不足已经还
     */
    public static final String PREDRAW_SIGN_BACK = "3";
    /**
     * 冲账标志--完全冲完
     */
    public static final String STRIKE_SIGN_ALL = "1";
    /**
     * 冲账标志--部分
     */
    public static final String STRIKE_SIGN_PART = "0";
    /**
     * 日结参数
     */
    public static final String PARAM_DAY_OVER = "day_over";
    /**
     * 利息产生方式-P-批量计息
     */
    public static final String GENERATEMODE_INTEREST_BATCH = "P";
    /**
     * 利息产生方式-I-个别计息
     */
    public static final String GENERATEMODE_INTEREST_PART = "I";
    /**
     * 计息
     */
    public static final String INTEREST_SIGN_YES = "1";
    /**
     * 未计息
     */
    public static final String INTEREST_SIGN_NO = "0";
    /**
     * 利息已还
     */
    public static final String INTEREST_REPAY_YES = "1";
    /**
     * 利息未还
     */
    public static final String INTEREST_REPAY_NO = "0";
    /**信贷计息业务code*/
    public static final String INTEREST_BUSINESS_CMS_CODE = "cms";
    /**
     * 批量计息-经办
     */
    public static final String INTEREST_TYPE_CALC = "calc_hand";
    /**
     * 批量计息-复核
     */
    public static final String INTEREST_TYPE_CHECK = "calc_check";
    /**
     * 利息冲预提提交
     */
    public static final String INTEREST_TYPE_RUSH_HAND = "runsh_hand";
    /**
     * 利息冲预提复核
     */
    public static final String INTEREST_TYPE_RUSH_CHECK = "runsh_check";
    /**
     * 批量计息-经办
     */
    public static final String INTEREST_TYPE_POOL_CALC = "pool_hand";
    /**
     * 批量计息-复核
     */
    public static final String INTEREST_TYPE_POOL_CHECK = "pool_check";
    /**
     * 通知存款账号通知预约登记状态-正常
     */
    public static final Integer NODBOOKING_STATUE_TEMP = 0;
    /**
     * 通知存款账号通知预约登记状态-已删除
     */
    public static final Integer NODBOOKING_STATUE_DELETE = -2;
    /**
     * 通知存款账号通知预约登记状态-已取消
     */
    public static final Integer NODBOOKING_STATUE_UNBOOKING = 1;
    /**
     * 通知存款账号通知预约登记状态-已使用
     */
    public static final Integer NODBOOKING_STATUE_USED = 3;
    /**
     * 计提频率周期类型：不计提
     */
    public static final String ACCRUAL_DATETYP_0 = "0";
    /**
     * 计提频率周期类型：天
     */
    public static final String ACCRUAL_DATETYP_1 = "1";
    /**
     * 计提频率周期类型：周
     */
    public static final String ACCRUAL_DATETYP_2 = "2";
    /**
     * 计提频率周期类型：月
     */
    public static final String ACCRUAL_DATETYP_3 = "3";
    /**
     * 计提频率周期类型：季
     */
    public static final String ACCRUAL_DATETYP_4 = "4";
    /**
     * 计提频率周期类型：年
     */
    public static final String ACCRUAL_DATETYP_5 = "5";
}
