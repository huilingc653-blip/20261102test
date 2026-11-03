package com.bytter.core.constant;
/**
 * 基础数据常量类
 */
public class BaseDataConstant {
    /** 贷款利率维护 - 是否启用 */
    /** 已启用 */
    public final static String IS_VALID_TRUE = "1";
    /** 已禁用 */
    public final static String IS_VALID_FALSE = "0";
    /** 贷款利率维护 流程状态 */
    /** 提交 */
    public final static Integer STATUS_SUBMIT = 1;
    /** 通过 */
    public final static Integer STATUS_PASS = 2;
    /** 打回 */
    public final static Integer STATUS_BACK = 3;
    /**
     * 是否央行基准利率 1：是 0：否
     */
    public final static String IS_BANKBASERATE = "1";
    public final static String NOT_BANKBASERATE = "0";

    /** 贷款利率日期类型 */
    public final static String RATE_MSG_TYPE = "R";
    /**
     * 贷款类型-贷款
     */
    public final static String TYPE_CREDIT = "1";
    /**
     * 贷款类型-借款
     */
    public final static String TYPE_BORROW = "2";
    /**
     * 状态-可编辑
     */
    public final static Integer STATUS_REDACT = 0;
    /***
     * 状态-删除
     */
    public final static Integer STATUS_DELETE = -2;
    /**
     * 是否默认 1：不可删除及更改
     */
    public final static String IS_DEFAULT = "1";



    /** 信贷系统参数类型 */
    public final static String CMS_CONFIG_TYPE = "cms";
    /** 委托贷款参数 */
    /** 是否挂预算控制 */
    public final static String IS_ENTRUST_BUDGET = "is_entrust_budget";
    /** 委托贷款业务选择的预算科目 */
    public final static String WTLOAN_BUDGET_SEL = "wtloan_budget_sel";
    /** 委托贷款录入业务选择的预算科目 */
    public final static String WTLOANENTRY_BUDGET_SEL = "wtloanentry_budget_sel";
    /** 委托贷款补录业务选择的预算科目 */
    public final static String WTLOANMAKEUP_BUDGET_SEL = "wtloanmakeup_budget_sel";
    /** 委托贷款归还业务选择的预算科目 */
    public final static String WTLOANRETURN_BUDGET_SEL = "wtloanreturn_budget_sel";
    /** 委托贷款还款补录业务选择的预算科目 */
    public final static String WTLOANRETURNMAKEUP_BUDGET_SEL = "wtloanreturnmakeup_budget_sel";
    /** 委托贷款是否挂担保 */
    public final static String IS_WTLOAN_GUA = "Is_wtloan_gua";
    /** 委托贷款展期次数 */
    public final static String LOANDFNUM = "LoanDfNum";
    /** 委托贷款是否记账 */
    public final static String IS_ENTRUST_TALLY = "is_entrust_tally";
    /** 委托贷款管理是否自动记账 */
    public final static String IS_ENTRUST_AUTOMATIC_TALLY = "is_entrust_automatic_tally";
    public final static String PRECATIVE_CONFIG_NOTIFYDAYS = "precative_Expiration_Warn";
    /** 委托贷款是否对接合同管理 */
    public final static String PRECATIVE_RELATE_CON = "pre_relate_con";
    /** 对接合同管理模式 */
    public final static String PRECATIVE_CON_TYPE = "pre_relate_con_type";
    /** 委托贷款履约环节 */
    public final static String PRECATIVE_SEGMENT = "pre_relate_con_segment";
    /** 银行借款参数设置 */
    /** 是否挂预算控制 */
    public final static String IS_BORROW_BUDGET = "is_borrow_budget";
    /** 是否挂授信额度控制 */
    public final static String RELATE_RAT = "relate_rat";
    /** 借款还款选择的预算科目 */
    public final static String PLOAN_REPAY_HANG_BUDGET = "ploan_repay_hang_budget";
    /** 借款补录/发放选择的预算科目 */
    public final static String PLOAN_SEND_HANG_BUDGET = "ploan_send_hang_budget";
    /** 银行借款是否挂担保 */
    public final static String IS_BANKLOAN_GUA = "isHangGua";
    /** 银行借款是否挂保证金 */
    public final static String IS_HOOK_BOND = "isBond";
    /** 银行借款是否敞口授信 */
    public final static String IS_OPEN_RAT = "isRatOpen";
    /** 银行借款是否挂抵质押物 */
    public final static String IS_RELATE_PLEDGE = "isRelatePledge";
    /** 借款管理是否记账 */
    public final static String IS_BORROW_TALLY = "is_borrow_tally";
    /** 借款按借款申请记录还款 */
    public final static String IS_APPLICATION = "is_application";
    /** 借款展期次数 */
    public final static String BORROWDFNUM = "BorrowDfNum";
    /** 借款管理是否自动记账 */
    public final static String IS_BORROW_AUTOMATIC_TALLY = "is_borrow_automatic_tally";
    /** 发放到期提醒 */
    public final static String PROVIDE_CONFIG_NOTIFYDAYS = "provide_config_notifydays";
    /** 其他应付款账户挂账选择 */
    public final static String IS_OTHER_ACCOUNTS_SELECT = "is_other_accounts_select";
    /** 是否对接合同管理 */
    public final static String BANK_RELEATE_CON = "bank_relate_con";
    /** 对接合同管理模式 */
    public final static String BANK_RELATE_CON_TYPE = "bank_relate_con_type";
    /** 履约环节 */
    public final static String BANK_RELATE_CON_SEGMENT = "bank_relate_con_segment";
    /** 贷款管理参数设置 */
    public final static String IS_LOAN_BUDGET = "is_loan_budget";
    /** 贷款还款选择的预算科目 */
    public final static String LOAN_REPAY_HANG_BUDGET = "loan_repay_hang_budget";
    /** 贷款发放选择的预算科目 */
    public final static String LOAN_SEND_HANG_BUDGET = "loan_send_hang_budget";
    /** 贷款管理是否记账 */
    public final static String IS_ACCOUNTING = "is_accounting";
    /** 贷款关注类天数定义 */
    public final static String ISGZ = "isGZ";
    /** 贷款次级类天数定义 */
    public final static String ISCJ = "isCJ";
    /** 贷款可疑类天数定义 */
    public final static String ISKY = "isKY";
    /** 贷款损失类天数定义 */
    public final static String ISSS = "isSS";
    /** 内部贷款是否挂担保 */
    public final static String IS_INNLOAN_GUA = "Is_innloan_gua";
    /** 内部贷款管理是否自动记账 */
    public final static String IS_INTERIOR_AUTOMATIC_TALLY = "is_interior_automatic_tally";
    /** 内部贷款展期次数 */
    public final static String INNERLOANDFNUM = "innerLoanDfNum";
    /** 贷款到期提醒天数 */
    public final static String LOANEXPIRATIONWARN = "loanExpirationWarn";
    /**
     * 计息提醒天数
     */
    public final static String INSWARNDAYS = "insWarnDays";
    /**
     * 每月计息日
     */
    public final static String BATCHINSDAYS = "batchInstDay";
    /** 流水业务状态 1000 银行借款 */
    public static final String BRIEF_STATUS_1000 = "1000";
    /** 流水业务状态 2000 内部贷款 */
    public static final String BRIEF_STATUS_2000 = "2000";
    /** 流水业务状态 3000 委托贷款 */
    public static final String BRIEF_STATUS_3000 = "3000";
    /** 授信到期提醒 */
    public final static String RAT_CONFIG_NOTIFYDAYS = "rat_Expiration_Warn";
    /** 银企接口收款待记账提醒 */
    public final static String BTNETCHALK_TALLY = "btNetChalk_tally";
    /** 内部贷款发放待记账提醒 */
    public final static String LOANSEND_TALLY = "loanSend_tally";
    /** 内部贷款还款待记账提醒 */
    public final static String LOANREPAY_TALLY = "loanRepay_tally";
    /** 直融业务参数设置 */
    /* 直融业务是否挂资金计划 */
    public final static String IS_DF_BUDGET = "is_df_budget";
    /** 直融业务登记挂接资金计划科目 */
    public final static String DIRECT_FINANCE_INFO_BUDGET = "df_info_budget";
    /** 直融业务登记挂接资金计划科目 */
    public final static String DIRECT_FINANCE_REPAY_BUDGET = "df_repay_budget";
    /**
     * 直融还款到期提醒
     */
    public final static String DIRECT_FINANCE_REPAY_NOTICE_DAYS = "df_repay_notice_days";
    /** 保证金到期提醒 */
    public final static String EXPIRE_REMIND_DAY = "expire_remind_day";
    /** 抵押是否允许多次挂接 */
    public final static String MANY_TIMES = "manyTimes";
    /** 内部拆借挂被担保合同 */
    public final static String IS_HANG_GUA = "is_hang_gua";
    /** 内部拆借挂合同参数 */
    public final static String IS_CON = "is_con";
    /** 内部拆借挂合同类型 */
    public final static String CON_TYPE = "con_type";
    /** 内部拆借挂合同履约环节 */
    public final static String CON_SEGMENT = "con_segment";
    /** 内部拆借挂授信参数 */
    public final static String IS_HANG_CREDIT = "is_hang_credit";
    /** 内部拆借展期多次参数 */
    public final static String DEFERRED_NUM = "deferred_num";
    /**
     * 内部拆解到期提醒天数设置
     */
    public final static String NOTIFY_DAYS = "notify_days";
    /**
     * 1000=银团借款类型
     */
    public final static String SYNDICATED = "1000";
    /**
     * 本息关系 1-按剩余本金
     */
    public static final String CAPITAL_AND_INTEREST_1 = "1";
    /**
     * 本息关系 2-分期等额本息
     */
    public static final String CAPITAL_AND_INTEREST_2 = "2";
    /**
     * 本息关系 3-利息与本金无关（分别自定义）
     */
    public static final String CAPITAL_AND_INTEREST_3 = "3";
    /**
     * 计提/计息类型-表示到期支付
     */
    public static final String NUM = "num";
    /**
     * 计提/计息类型-第三输入框输入数值
     */
    public static final String OTHER = "other";
    /**
     * 频率类型 D-按时间周期
     */
    public static final String FREQUENCY_TYPE_D = "D";
    /**
     * 频率类型 C-按次数
     */
    public static final String FREQUENCY_TYPE_C = "C";
    /**
     * 频率类型 P-不规律（有计划）
     */
    public static final String FREQUENCY_TYPE_P = "P";
    /**
     * 频率类型 N-随借随还（无计划）
     */
    public static final String FREQUENCY_TYPE_N = "N";
    /**
     * 频率(第二列) 1-天 / 按比例
     */
    public static final String FREQUENCY_NEXT_1 = "1";
    /**
     * 频率(第二列) 2-周 / 无比例
     */
    public static final String FREQUENCY_NEXT_2 = "2";
    /**
     * 频率(第二列) 1-月
     */
    public static final String FREQUENCY_NEXT_3 = "3";
    /**
     * 频率(第二列) 1-季
     */
    public static final String FREQUENCY_NEXT_4 = "4";
    /**
     * 频率(第二列) 1-年
     */
    public static final String FREQUENCY_NEXT_5 = "5";
    /**
     * 季末
     */
    public static final String FREQUENCY_1 = "1";
    /**
     * 半年
     */
    public static final String FREQUENCY_2 = "2";
}
