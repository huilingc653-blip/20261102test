package com.bytter.core.constant;
import java.io.Serializable;
/**
 * 信贷模块常量类
 */
public class CmsConstant implements Serializable {
    /**
     * 信贷单据状态
     */
    /**
     * 已删除
     */
    public final static int DELETED = -2;
    /**
     * 已打回
     */
    public final static int BACK = -1;
    /**
     * 作废
     */
    public final static int ABOLISH = -3;
    /**
     * 作废代办
     */
    public final static int ABOLISHDISPOSE = -4;
    /**
     * 暂存
     */
    public final static int TEMP = 0;
    /**
     * 提交审批
     */
    public final static int COMMITAPPROVE = 11;
    /**
     * 流程等待状态 当前流程需要等待其他流程的执行结果时，使用的中间等待状态
     */
    public final static int FLOWWAIT = 12;
    /**
     * 补录审批
     */
    public final static int REPAIRAPPRAVE = 21;
    /**
     * 审核通过
     */
    public final static int APPROVED = 95;
    /**
     * 待生成合同
     */
    public final static int CONTRACT = 94;
    /**
     * 待发放
     */
    public final static int PREPARESEND = 96;
    /**
     * 借款待还 状态值重复删除 public final static int STAYSTILL = 97;
     */
    /*
     * 融资租赁
     */
    /**
     * 租赁申请补录状态
     */
    /**
     * 租赁申请暂存
     */
    public final static int LEASE_TEMP_0 = 0;
    /**
     * 租赁申请审判中
     */
    public final static int LEASE_COMMITAPPROVE_11 = 11;
    /**
     * 租赁申请审批通过
     */
    public final static int LEASE_APPROVED_95 = 95;
    /**
     * 租赁申请打回
     */
    public final static int LEASE_BACK_1 = -1;
    /**
     * 删除
     */
    public final static int LEASE_DELETED_2 = -2;
    /**
     * 租赁补录暂存
     */
    public final static int LEASE_REPAIR_TEMP = 20;
    /**
     * 租赁补录审批中
     */
    public final static int LEASE_REPAIR_COMMITAPPROVE = 21;
    /**
     * 租赁补录审批通过
     */
    public final static int LEASE_REPAIR_APPROVED = 100;
    /**
     * 租赁补录打回
     */
    public final static int LEASE_REPAIR_BACK = -3;
    /*
     * 租赁业务状态
     */
    /**
     * 暂存
     */
    public final static String LEASE_BIZ_STATUS_0 = "0";
    /**
     * 审批中
     */
    public final static String LEASE_BIZ_STATUS_11 = "11";
    /**
     * 审批打回
     */
    public final static String LEASE_BIZ_STATUS__1 = "-1";
    /**
     * 审批完成
     */
    public final static String LEASE_BIZ_STATUS_95 = "95";
    /**
     * 删除
     */
    public final static String LEASE_BIZ_STATUS__2 = "-2";
    /**
     * 补录暂存
     */
    public final static String LEASE_BIZ_STATUS_20 = "20";
    /**
     * 补录审批中
     */
    public final static String LEASE_BIZ_STATUS_21 = "21";
    /**
     * 补录审批打回
     */
    public final static String LEASE_BIZ_STATUS__3 = "-3";
    /**
     * 补录完成
     */
    public final static String LEASE_BIZ_STATUS_96 = "96";
    /**
     * 标的物登记暂存
     */
    public final static String LEASE_BIZ_STATUS_30 = "30";
    /**
     * 标的物登记审批中
     */
    public final static String LEASE_BIZ_STATUS_31 = "31";
    /**
     * 标的物登记审批打回
     */
    public final static String LEASE_BIZ_STATUS__4 = "-4";
    /**
     * 标的物登记完成
     */
    public final static String LEASE_BIZ_STATUS_97 = "97";
    /**
     * 标的物修改暂存
     */
    public final static String LEASE_BIZ_STATUS_40 = "40";
    /**
     * 标的物修改审批中
     */
    public final static String LEASE_BIZ_STATUS_41 = "41";
    /**
     * 标的物修改审批打回
     */
    public final static String LEASE_BIZ_STATUS__5 = "-5";
    /**
     * 标的物修改完成
     */
    public final static String LEASE_BIZ_STATUS_98 = "98";
    /**
     * 租金计划登记暂存
     */
    public final static String LEASE_BIZ_STATUS_50 = "50";
    /**
     * 租金计划登记审批中
     */
    public final static String LEASE_BIZ_STATUS_51 = "51";
    /**
     * 租金计划登记审批打回
     */
    public final static String LEASE_BIZ_STATUS__6 = "-6";
    /**
     * 租金计划登记完成
     */
    public final static String LEASE_BIZ_STATUS_99 = "99";
    /**
     * 租金计划修改暂存
     */
    public final static String LEASE_BIZ_STATUS_60 = "60";
    /**
     * 租金计划修改审批中
     */
    public final static String LEASE_BIZ_STATUS_61 = "61";
    /**
     * 租金计划修改审批打回
     */
    public final static String LEASE_BIZ_STATUS__7 = "-7";
    /**
     * 租金计划修改完成
     */
    public final static String LEASE_BIZ_STATUS_100 = "100";
    /**
     * 支付申请暂存
     */
    public final static String LEASE_BIZ_STATUS_70 = "70";
    /**
     * 支付申请审批中
     */
    public final static String LEASE_BIZ_STATUS_71 = "71";
    /**
     * 支付申请审批打回
     */
    public final static String LEASE_BIZ_STATUS__8 = "-8";
    /**
     * 支付申请完成
     */
    public final static String LEASE_BIZ_STATUS_101 = "101";
    /**
     * 退租暂存
     */
    public final static String LEASE_BIZ_STATUS_25 = "25";
    /**
     * 退租审批中
     */
    public final static String LEASE_BIZ_STATUS_26 = "26";
    /**
     * 退租审批打回
     */
    public final static String LEASE_BIZ_STATUS__9 = "-9";
    /**
     * 退租完成
     */
    public final static String LEASE_BIZ_STATUS_102 = "102";
    /**
     * 续租暂存
     */
    public final static String LEASE_BIZ_STATUS_35 = "35";
    /**
     * 续租审批中
     */
    public final static String LEASE_BIZ_STATUS_36 = "36";
    /**
     * 续租审批打回
     */
    public final static String LEASE_BIZ_STATUS__10 = "-10";
    /**
     * 续租完成
     */
    public final static String LEASE_BIZ_STATUS_103 = "103";
    /**
     * 回购暂存
     */
    public final static String LEASE_BIZ_STATUS_45 = "45";
    /**
     * 回购审批中
     */
    public final static String LEASE_BIZ_STATUS_46 = "46";
    /**
     * 回购审批打回
     */
    public final static String LEASE_BIZ_STATUS__11 = "-11";
    /**
     * 回购完成
     */
    public final static String LEASE_BIZ_STATUS_104 = "104";
    /**
     * 展期暂存
     */
    public final static String LEASE_BIZ_STATUS_55 = "55";
    /**
     * 展期审批中
     */
    public final static String LEASE_BIZ_STATUS_56 = "56";
    /**
     * 展期审批打回
     */
    public final static String LEASE_BIZ_STATUS__12 = "-12";
    /**
     * 展期完成
     */
    public final static String LEASE_BIZ_STATUS_105 = "105";
    /**
     * 变更暂存
     */
    public final static String LEASE_BIZ_STATUS_65 = "65";
    /**
     * 变更审批中
     */
    public final static String LEASE_BIZ_STATUS_66 = "66";
    /**
     * 变更审批打回
     */
    public final static String LEASE_BIZ_STATUS__13 = "-13";
    /**
     * 变更完成
     */
    public final static String LEASE_BIZ_STATUS_106 = "106";
    /**
     * 租赁物修改状态
     */
    /**
     * 审批通过
     */
    public final static int LEASE_APPROVED = 94;
    /**
     * 审批打回
     */
    public final static int LEASE_BACK = -3;
    /**
     * 暂存
     */
    public final static int LEASE_TEMP = 1;
    /**
     * 提交审批
     */
    public final static int LEASE_COMMITAPPROVE = 12;
    /**
     * 发放状态
     */
    /**
     * 待记账
     */
    public final static int WAITTOTAL = 97;
    /**
     * 记账提交待复核
     */
    public final static int NBC_STATUS_SUBMIT = 98;
    /**
     * 改账完成
     */
    public final static int NBC_STATUS_MODIFY_COMPLETE = 99;
    /**
     * 复核打回
     */
    public final static int NBC_STATUS_CHECK_BACK = 100;
    /**
     * 改账打回
     */
    public final static int NBC_STATUS_MODIFY_BACK = 101;
    /**
     * 记账完成
     */
    public final static int COMPELETOTAL = 102;
    /**
     * 展期申请中
     */
    public final static int EXTENDDATING = 103;
    /**
     * 展期申请成功 或者 展期处理完成
     */
    public final static int COMPELTEXTENDATE = 104;
    /**
     * 记账中 （网点间记账未复核）
     */
    public final static int NETWAITTOTAL = 105;
    /**
     * 还款补录成功
     */
    public final static int REPAYADDITIONAL_OK = 106;
    /**
     * 记账暂存
     */
    public final static int NBC_STATUS_TEMP = 120;
    /**
     * 业务结束
     */
    public final static int FINISH = 110;
    /**
     * 审批通过！
     */
    public static final String CMS_APPROVE_SUCC = "cms.approve.succ";
    /**
     * 审批打回！
     */
    public static final String CMS_APPROVE_BACK = "cms.approve.back";
    /**
     * 资产证券化发行业务状态
     */
    /** 发行暂存 */
    public final static String SECURITY_BIZ_STATUS_0 = "0";
    /** 发行审批中 */
    public final static String SECURITY_BIZ_STATUS_11 = "11";
    /** 发行审批打回 */
    public final static String SECURITY_BIZ_STATUS__1 = "-1";
    /** 发行完成 */
    public final static String SECURITY_BIZ_STATUS_95 = "95";
    /** 到账确认审批中 */
    public final static String SECURITY_BIZ_STATUS_21 = "21";
    /** 到账确认中 */
    public final static String SECURITY_BIZ_STATUS_25 = "25";
    /** 到账确认完成 */
    public final static String SECURITY_BIZ_STATUS_96 = "96";
    /** 还款计划登记审批中 */
    public final static String SECURITY_BIZ_STATUS_31 = "31";
    /** 还款计划登记完成 */
    public final static String SECURITY_BIZ_STATUS_97 = "97";
    /** 还款计划登记打回 */
    public final static String SECURITY_BIZ_STATUS__3 = "-3";
    /** 还款计划修改审批中 */
    public final static String SECURITY_BIZ_STATUS_41 = "41";
    /** 还款计划修改完成 */
    public final static String SECURITY_BIZ_STATUS_98 = "98";
    /** 还款计划修改打回 */
    public final static String SECURITY_BIZ_STATUS__4 = "-4";
    /** 还款确认审批中 */
    public final static String SECURITY_BIZ_STATUS_51 = "51";
    /** 还款确认中 */
    public final static String SECURITY_BIZ_STATUS_55 = "55";
    /** 还款完成 */
    public final static String SECURITY_BIZ_STATUS_100 = "100";
    /**
     * 资产证券化还款确认状态
     */
    /** 待确认 */
    public final static Integer SECURITY_REPAY_SURE_0 = 0;
    /** 已选择 */
    public final static Integer SECURITY_REPAY_SURE_1 = 1;
    /** 打回 */
    public final static Integer SECURITY_REPAY_SURE__1 = -1;
    /** 审批中 */
    public final static Integer SECURITY_REPAY_SURE_3 = 3;
    /** 已确认 */
    public final static Integer SECURITY_REPAY_SURE_5 = 5;
    /**
     * 内部贷款
     */
    public final static String LOAN_SIGN_01 = "01";
    /**
     * 银行借款
     */
    public final static String LOAN_SIGN_02 = "02";
    /**
     * 委托贷款
     */
    public final static String LOAN_SIGN_03 = "03";
    /**
     * 还款方式-还本付息
     */
    public final static String REPAY_TYPE_01 = "01";
    /**
     * 还款方式-分期等额本金
     */
    public final static String REPAY_TYPE_02 = "02";
    /**
     * 还款方式-其他
     */
    public final static String REPAY_TYPE_03 = "03";
    /**
     * 还款方式-分期等额本息
     */
    public final static String REPAY_TYPE_04 = "04";
    /**
     * 还款方式-分期等额本金-不还息
     */
    public final static String REPAY_TYPE_05 = "05";
    /**
     * 还款方式-其他-不还息
     */
    public final static String REPAY_TYPE_06 = "06";
    /**
     * 还款方式-自定义
     */
    public final static String REPAY_TYPE_07 = "07";
    /**
     * 贷款期限-月(贷款)
     */
    public static final String CMS_TERN_UNIT_MEONTH = "1";
    /**
     * 贷款期限-天(贷款)
     */
    public static final String CMS_TERN_UNIT_DAY = "2";
    /**
     * 贷款期限-月
     */
    public static final String CMS_LOAN_TERN_UNIT_MEONTH = "M";
    /**
     * 贷款期限-天
     */
    public static final String CMS_LOAN_TERN_UNIT_DAY = "D";
    /**
     * 贷款发放方式-一次性发放
     */
    public static final String LOAN_SEND_OUT_TYPE_ONE = "1";
    /**
     * 贷款发放方式-分次发放
     */
    public static final String LOAN_SEND_OUT_TYPE_TWO = "2";
    /**
     * 贷款还款支付方式-现汇支付
     */
    public static final String LOAN_REPAY_PAY_MONEY = "1";
    /**
     * 贷款还款支付方式-票据支付
     */
    public static final String LOAN_REPAY_PAY_RECEIPT = "2";
    /**
     * 期限范围-短期
     */
    public final static String TERM_RANGE_01 = "01";
    /**
     * 期限范围-中期
     */
    public final static String TERM_RANGE_02 = "02";
    /**
     * 期限范围-长期
     */
    public final static String TERM_RANGE_03 = "03";
    /**
     * 浮动方式-固定利率
     */
    public final static String FLOAT_WAY_01 = "01";
    /**
     * 浮动方式-即时浮动
     */
    public final static String FLOAT_WAY_02 = "02";
    /**
     * 浮动方式-按月浮动
     */
    public final static String FLOAT_WAY_03 = "03";
    /**
     * 浮动方式-按季浮动
     */
    public final static String FLOAT_WAY_04 = "04";
    /**
     * 浮动方式-按半年浮动
     */
    public final static String FLOAT_WAY_05 = "05";
    /**
     * 浮动方式-按年浮动
     */
    public final static String FLOAT_WAY_06 = "06";
    /**
     * 浮动方式开始时间-次（月、季、半年、年）第一天
     */
    public final static String FLOAT_WAY_START_1 = "1";
    /**
     * 浮动方式开始时间-次（月、季、半年、年）贷款日
     */
    public final static String FLOAT_WAY_START_2 = "2";
    /**
     * 浮动方式开始时间-次（月、季、半年、年）计息日次日
     */
    public final static String FLOAT_WAY_START_3 = "3";
    /**
     * 担保方式-信用、保证、抵押、质押
     */
    /**
     * 信用
     */
    public final static String PL_TYPE_1 = "1";
    /**
     * 保证
     */
    public final static String PL_TYPE_2 = "2";
    /**
     * 抵押
     */
    public final static String PL_TYPE_3 = "3";
    /**
     * 质押
     */
    public final static String PL_TYPE_4 = "4";
    /**
     * 是否银行办理 0-否 1-是
     */
    public final static String IS_BANK_0 = "0";
    public final static String IS_BANK_1 = "1";
    /**
     * 贷款合同模板编号初始化
     */
    public final static String LOAN_CONTRACTNO_INIT = "00000000000000";
    /**
     * 是否 0-否 1-是
     */
    public final static String IS_FALSE = "0";
    public final static String IS_TRUE = "1";
    /**
     * 线上还款标记
     */
    public final static String ONLINE_TRUE = "1";
    /**
     * 线下还款标记
     */
    public final static String ONLINE_FALSE = "0";
    /**
     * 还款类型repayType
     */
    /**
     * 归还本金
     */
    public final static String REPAY_TYPE_1 = "1";
    /**
     * 归还本金及归还金额利息
     */
    public final static String REPAY_TYPE_2 = "2";
    /**
     * 归还本金及所有利息
     */
    public final static String REPAY_TYPE_3 = "3";
    /**
     * 归还利息
     */
    public final static String REPAY_TYPE_4 = "4";
    /**
     * 信贷业务状态
     */
    /**
     * 暂存
     */
    public final static String BUSINESSSTATE_10 = "10";
    /**
     * 补录打回
     */
    public final static String BUSINESSSTATE_12 = "12";
    /**
     * 待审批
     */
    public final static String BUSINESSSTATE_15 = "15";
    /**
     * 审批打回
     */
    public final static String BUSINESSSTATE_17 = "17";
    /* 预计还款日不能为空！ */
    public static final String CMS_REPAYMENTSCHEDULE_PREDICEREPAYDATE_ISNULL = "cms.repaymentschedule.prediceRepayDate.isnull";
    /* 还款本金不能为空！ */
    public static final String CMS_REPAYMENTSCHEDULE_REPAYMENTMONEY_ISNULL = "cms.repaymentschedule.repaymentMoney.isnull";
    /* 还款利息不能为空！ */
    public static final String CMS_REPAYMENTSCHEDULE_REPAYMENTIST_ISNULL = "cms.repaymentschedule.repaymentIst.isnull";
    /* 是否已还标识不能为空 */
    public static final String CMS_REPAYMENTSCHEDULE_ISREPAY_ISNULL = "cms.repaymentschedule.isRepay.isnull";
    /* 是否已还标识输入值错误 */
    public static final String CMS_REPAYMENTSCHEDULE_ISREPAY_ERROR = "cms.repaymentschedule.isRepay.error";
    /* 自动还款 */
    public static final String TABLE_CMSPROVIDELOANINFO_AUTOPAY = "table.cmsProvideLoanInfo.AutoPay";
    /**
     * 信贷类型
     */
    /**
     * 信贷
     */
    public final static String CREDITTYPE_0 = "0";
    /**
     * 贷款
     */
    public final static String CREDITTYPE_1 = "1";
    /**
     * 委托贷款
     */
    public final static String CREDITTYPE_2 = "2";
    /**
     * 有效标志
     */
    /**
     * 有效
     */
    public final static String VALIDSIGN = "1";
    /**
     * 无效
     */
    public final static String INVALIDFLAG = "-2";
    /**
     * 删除
     */
    public final static String BUSINESSSTATE_2_ = "-2";
    /**
     * 补录暂存
     */
    public final static String BUSINESSSTATE_20 = "20";
    /**
     * 待补录
     */
    public final static String BUSINESSSTATE_21 = "21";
    /**
     * 补录待审批
     */
    public final static String BUSINESSSTATE_25 = "25";
    /**
     * 补录审批打回
     */
    public final static String BUSINESSSTATE_26 = "26";
    /**
     * 发放打回
     */
    public final static String BUSINESSSTATE_27 = "27";
    /**
     * 发放暂存
     */
    public final static String BUSINESSSTATE_30 = "30";
    /**
     * 待发放
     */
    public final static String BUSINESSSTATE_31 = "31";
    /**
     * 发放中
     */
    public final static String BUSINESSSTATE_32 = "32";
    /**
     * 还款中
     */
    public final static String BUSINESSSTATE_33 = "33";
    /**
     * 已还款
     */
    public final static String BUSINESSSTATE_34 = "34";
    /**
     * 发放待审批
     */
    public final static String BUSINESSSTATE_35 = "35";
    /**
     * 发放审批打回
     */
    public final static String BUSINESSSTATE_37 = "37";
    /**
     * 待记账
     */
    public final static String BUSINESSSTATE_38 = "38";
    /**
     * 还款暂存
     */
    public final static String BUSINESSSTATE_40 = "40";
    /**
     * 还款待审批
     */
    public final static String BUSINESSSTATE_41 = "41";
    /**
     * 待还款
     */
    public final static String BUSINESSSTATE_42 = "42";
    /**
     * 审批通过待确认
     */
    public final static String BUSINESSSTATE_43 = "43";
    /**
     * 利息确认打回
     */
    public final static String BUSINESSSTATE_46 = "46";
    /**
     * 还款打回
     */
    public final static String BUSINESSSTATE_47 = "47";
    /**
     * 还款完成
     */
    public final static String BUSINESSSTATE_48 = "48";
    /**
     * 还款审批打回
     */
    public final static String BUSINESSSTATE_49 = "49";
    /**
     * 还款补录暂存
     */
    public final static String BUSINESSSTATE_50 = "50";
    /**
     * 还款补录待审批
     */
    public final static String BUSINESSSTATE_51 = "51";
    /**
     * 还款待补录
     */
    public final static String BUSINESSSTATE_52 = "52";
    /**
     * 还款补录审批打回
     */
    public final static String BUSINESSSTATE_56 = "56";
    /**
     * 还款补录打回
     */
    public final static String BUSINESSSTATE_57 = "57";
    /**
     * 扣款中
     */
    public final static String BUSINESSSTATE_58 = "58";
    /**
     * 扣款打回
     */
    public final static String BUSINESSSTATE_53 = "53";
    /**
     * 还款补录成功
     */
    public final static String BUSINESSSTATE_59 = "59";
    /**
     * 展期暂存
     */
    public final static String BUSINESSSTATE_60 = "60";
    /**
     * 展期待审批
     */
    public final static String BUSINESSSTATE_61 = "61";
    /**
     * 展期完成
     */
    public final static String BUSINESSSTATE_65 = "65";
    /**
     * 展期补录打回
     */
    public final static String BUSINESSSTATE_66 = "66";
    /**
     * 展期申请审批打回
     */
    public final static String BUSINESSSTATE_67 = "67";
    /**
     * 展期申请中
     */
    public final static String BUSINESSSTATE_68 = "68";
    /**
     * 展期处理打回
     */
    public final static String BUSINESSSTATE_69 = "69";
    /**
     * 展期补录暂存
     */
    public final static String BUSINESSSTATE_70 = "70";
    /**
     * 展期补录待审批
     */
    public final static String BUSINESSSTATE_71 = "71";
    /**
     * 展期待补录
     */
    public final static String BUSINESSSTATE_72 = "72";
    /**
     * 展期处理打回
     */
    public final static String BUSINESSSTATE_76 = "76";
    /**
     * 展期补录打回
     */
    public final static String BUSINESSSTATE_77 = "77";
    /**
     * 展期待处理
     */
    public final static String BUSINESSSTATE_78 = "78";
    /**
     * 展期申请成功
     */
    public final static String BUSINESSSTATE_79 = "79";
    /**
     * 展期补录审批打回
     */
    public final static String BUSINESSSTATE_80 = "80";
    /**
     * 作废待审批
     */
    public final static String BUSINESSSTATE_81 = "81";
    /**
     * 已作废
     */
    public final static String BUSINESSSTATE_85 = "85";
    /**
     * 作废待办
     */
    public final static String BUSINESSSTATE_87 = "87";
    /**
     * 作废审批打回
     */
    public final static String BUSINESSSTATE_88 = "88";
    /**
     * 作废撤销
     */
    public final static String BUSINESSSTATE_86 = "86";
    /**
     * 合同待生成
     */
    public final static String BUSINESSSTATE_90 = "90";
    /**
     * 待记账
     */
    public final static String BUSINESSSTATE_91 = "91";
    /**
     * 记账暂存
     */
    public final static String BUSINESSSTATE_92 = "92";
    /**
     * 记账待复核
     */
    public final static String BUSINESSSTATE_93 = "93";
    /**
     * 记账复核打回
     */
    public final static String BUSINESSSTATE_94 = "94";
    /**
     * 改账打回
     */
    public final static String BUSINESSSTATE_95 = "95";
    /**
     * 记账打回
     */
    public final static String BUSINESSSTATE_96 = "96";
    /**
     * 业务结束
     */
    public final static String BUSINESSSTATE_110 = "110";
    /**
     * 内部贷款 --申请业务前缀
     */
    public final static String LOAN_INFO_BILL_CODE_PREFIX = "A1";
    /**
     * 内部贷款 --发放业务前缀
     */
    public final static String LOAN_SEND_BILL_CODE_PREFIX = "A2";
    /**
     * 内部贷款 --还款业务前缀
     */
    public final static String LOAN_REPAY_BILL_CODE_PREFIX = "A3";
    /**
     * 内部贷款 --展期业务前缀
     */
    public final static String LOAN_DEFERRED_BILL_CODE_PREFIX = "A4";
    /**
     * 内部拆借申请
     */
    public final static String INL_BILL_CODE_PREFIX = "N1";
    /**
     * 内部拆借发放
     */
    public final static String INL_SEND_BILL_CODE_PREFIX = "N2";
    /**
     * 还款计划是否已还标记
     */
    /**
     * 已还
     */
    public final static String REPAYMENT_SCHEDULE_ISREPAYYES = "1";
    /**
     * 未还
     */
    public final static String REPAYMENT_SCHEDULE_ISREPAYNO = "0";
    /**
     * 是否对接合同管理：是
     */
    public final static String RELATED_CONTRACT_YES = "1";
    /**
     * 是否对接合同管理：否
     */
    public final static String RELATED_CONTRACT_NO = "0";
    /**
     * 对接合同管理模式：业务生成合同
     */
    public final static String BUSINESS_CREATE_CON = "0";
    /**
     * 对接合同管理模式：业务调用合同
     */
    public final static String BUSINESS_SELECT_CON = "1";
    /**
     * 履约环节：放款
     */
    public final static String SEGMENT_LOAN = "0";
    /**
     * 履约环节：还款
     */
    public final static String SEGMENT_REPAY = "1";
    /**
     * 内部贷款合同
     */
    public final static String INNER_LOAN_CON = "内部贷款合同";
    /**
     * 内部贷款放款
     */
    public final static String INNER_LOAN_SEND = "内部贷款放款";
    /**
     * 内部贷款还款
     */
    public final static String INNER_LOAN_REPAY = "内部贷款还款";
    /**
     * 委托贷款合同
     */
    public final static String PRECATIVE_LOAN_CON = "委托贷款合同";
    /**
     * 委托贷款放款
     */
    public final static String PRECATIVE_LOAN_SEND = "委托贷款放款";
    /**
     * 委托贷款还款
     */
    public final static String PRECATIVE_LOAN_REPAY = "委托贷款还款";
    /**
     * 银行借款合同
     */
    public final static String PROVIDE_LOAN_CON = "银行借款合同";
    /**
     * 银行借款放款
     */
    public final static String PROVIDE_LOAN_SEND = "银行借款放款";
    /**
     * 银行借款还款
     */
    public final static String PROVIDE_LOAN_REPAY = "银行借款还款";
    /**
     * 银行借款全部还清
     */
    public final static String PROVIDE_LOAN_REPAYOVER = "银行借款全部还清";
    /**
     * 合同作废
     */
    public final static String PROVIDE_LOAN_ABOLISH = "合同作废";
    /**
     * 内部拆借合同
     */
    public final static String INL_CON = "内部拆借合同";
    /**
     * 内部拆借放款
     */
    public final static String INL_CON_SEND = "内部拆借放款";
    /**
     * 内部拆借还款
     */
    public final static String INL_CON_REPAY = "内部拆借还款";
    /**
     * 内部拆借全部还清
     */
    public final static String INL_CON_REPAYOVER = "内部拆借全部还清";
    /**
     * 信贷对接的合同终止状态
     */
    public static final Integer CON_INFO_STATUS_STOP = 90;
    /**
     * 较上月末
     */
    public static final String CYCLE_M = "M";
    /**
     * 较上季末
     */
    public static final String CYCLE_S = "S";
    /**
     * 去年同期
     */
    public static final String CYCLE_L = "L";
    /**
     * 年初
     */
    public static final String CYCLE_Y = "Y";
    /**
     * 金额单位：元
     */
    public static final String MONEY_TYPE_1 = "1";
    /**
     * 金额单位：千元
     */
    public static final String MONEY_TYPE_1000 = "2";
    /**
     * 金额单位：万元
     */
    public static final String MONEY_TYPE_10000 = "3";
    /**
     * 金额单位：亿元
     */
    public static final String MONEY_TYPE_100000000 = "4";
    /**
     * 统计维度：1---融资银行
     */
    public static final String SORT_TYPE_1 = "1";
    /**
     * 统计维度：0---融资单位
     */
    public static final String SORT_TYPE_0 = "0";
    /**
     * 贷款余额趋势分析查询权限
     */
    public static final String LOAN_BALANCE_TREND_QUERY = "loan:balance:trend:query";
    /**
     * 查询频率 0-按天查询 1-按月查询
     */
    public final static String SEARCH_BY_DAY = "0";
    public final static String SEARCH_BY_MONTH = "1";
    /**
     * 频率周期类型 accrual.dateType
     */
    /** 不计提 */
    public final static String ACCRUAL_DATETYPE_0 = "0";
    /** 天 */
    public final static String ACCRUAL_DATETYPE_1 = "1";
    /** 周 */
    public final static String ACCRUAL_DATETYPE_2 = "2";
    /** 月 */
    public final static String ACCRUAL_DATETYPE_3 = "3";
    /** 季 */
    public final static String ACCRUAL_DATETYPE_4 = "4";
    /** 年 */
    public final static String ACCRUAL_DATETYPE_5 = "5";
    /**
     * 有效标志
     */
    /** 有效 */
    public final static String VALIDSIGN_1 = "1";
    /** 无效 */
    public final static String VALIDSIGN_0 = "0";
    /**
     * 登记修改标志
     */
    /** 登记 */
    public final static String ADD_OR_UPDATE_0 = "0";
    /** 修改 */
    public final static String ADD_OR_UPDATE_1 = "1";
    /**
     * 股权明细业务状态
     */
    /** 审批通过 */
    public final static String EQUITY_DETAIL_BIZSTATUS_0 = "0";
    /** 计划登记审批打回 */
    public final static String EQUITY_DETAIL_BIZSTATUS_1 = "1";
    /** 计划登记审批中 */
    public final static String EQUITY_DETAIL_BIZSTATUS_5 = "5";
    /** 计划登记审批通过 */
    public final static String EQUITY_DETAIL_BIZSTATUS_10 = "10";
    /** 计划修改审批打回 */
    public final static String EQUITY_DETAIL_BIZSTATUS_11 = "11";
    /** 计划修改审批中 */
    public final static String EQUITY_DETAIL_BIZSTATUS_15 = "15";
    /** 计划修改审批完成 */
    public final static String EQUITY_DETAIL_BIZSTATUS_20 = "20";
    /** 缴付/过户暂存 */
    public final static String EQUITY_DETAIL_BIZSTATUS_22 = "22";
    /** 缴付/过户审批中 */
    public final static String EQUITY_DETAIL_BIZSTATUS_25 = "25";
    /** 缴付/过户审批打回 */
    public final static String EQUITY_DETAIL_BIZSTATUS_21 = "21";
    /** 缴付/过户中 */
    public final static String EQUITY_DETAIL_BIZSTATUS_26 = "26";
    /** 缴付/过户完成 */
    public final static String EQUITY_DETAIL_BIZSTATUS_30 = "30";
    /** 回购计划登记审批打回 */
    public final static String EQUITY_DETAIL_BIZSTATUS_31 = "31";
    /** 回购计划登记审批中 */
    public final static String EQUITY_DETAIL_BIZSTATUS_35 = "35";
    /** 回购计划登记审批通过 */
    public final static String EQUITY_DETAIL_BIZSTATUS_40 = "40";
    /** 回购计划修改审批打回 */
    public final static String EQUITY_DETAIL_BIZSTATUS_41 = "41";
    /** 回购计划修改审批中 */
    public final static String EQUITY_DETAIL_BIZSTATUS_45 = "45";
    /** 回购计划修改审批完成 */
    public final static String EQUITY_DETAIL_BIZSTATUS_50 = "50";
    /** 回购审批打回 */
    public final static String EQUITY_DETAIL_BIZSTATUS_51 = "51";
    /** 回购暂存 */
    public final static String EQUITY_DETAIL_BIZSTATUS_52 = "52";
    /** 回购审批中 */
    public final static String EQUITY_DETAIL_BIZSTATUS_55 = "55";
    /** 回购中 */
    public final static String EQUITY_DETAIL_BIZSTATUS_56 = "56";
    /** 回购完成 */
    public final static String EQUITY_DETAIL_BIZSTATUS_60 = "60";
    /** 请输入正确的融资方式 */
    public final static String CMS_EQUITY_TRUE_FINANCINGWAY = "cms.equity.true.financingWay";
    /** 融资方式不能为空 */
    public final static String CMS_EQUITY_FINANCINGWAY_ISNULL = "cms.equity.financingWay.isNull";
    /** 出资人不能为空 */
    public final static String CMS_EQUITY_INVESTOR_ISNULL = "cms.equity.investor.isNull";
    /** 实物资产资产名称不能为空 */
    public final static String CMS_EQUITY_ASSETNAME_ISNULL = "cms.equity.assetName.isNull";
    /** 请输入正确的出资方式 */
    public final static String CMS_EQUITY_TRUE_INVWAY = "cms.equity.true.invWay";
    /** 出资方式不能为空 */
    public final static String CMS_EQUITY_INVWAY_ISNULL = "cms.equity.invWay.isNull";
    /** 出资金额不能为空 */
    public final static String CMS_EQUITY_INVMONEY_ISNULL = "cms.equity.invMoney.isNull";
    /** 缴付/过户日期不能为空！ */
    public final static String CMS_EQUITY_PAYDATE_ISNULL = "cms.equity.payDate.isNull";
    /** 本次缴付金额不能为空！ */
    public final static String CMS_EQUITY_PAYMONEY_ISNULL = "cms.equity.payMoney.isNull";
    /** 回购日期不能为空！ */
    public final static String CMS_EQUITY_REPODATE_ISNULL = "cms.equity.repoDate.isNull";
    /** 本次回购金额不能为空！ */
    public final static String CMS_EQUITY_REPOMONEY_ISNULL = "cms.equity.repoMoney.isNull";
    /**
     * 债券发行业务状态
     */
    /** 发行暂存 */
    public final static String BOND_BIZ_STATUS_0 = "0";
    /** 发行审批中 */
    public final static String BOND_BIZ_STATUS_11 = "11";
    /** 发行审批打回 */
    public final static String BOND_BIZ_STATUS__1 = "-1";
    /** 发行审批完成 */
    public final static String BOND_BIZ_STATUS_95 = "95";
    /** 发行删除 */
    public final static String BOND_BIZ_STATUS__2 = "-2";
    /** 债券还款计划登记暂存 */
    public final static String BOND_BIZ_STATUS_50 = "50";
    /** 债券还款计划登记审批中 */
    public final static String BOND_BIZ_STATUS_51 = "51";
    /** 债券还款计划登记审批打回 */
    public final static String BOND_BIZ_STATUS__5 = "-5";
    /** 债券还款计划登记完成 */
    public final static String BOND_BIZ_STATUS_99 = "99";
    /** 债券还款计划修改暂存 */
    public final static String BOND_BIZ_STATUS_60 = "60";
    /** 债券还款计划修改审批中 */
    public final static String BOND_BIZ_STATUS_61 = "61";
    /** 债券还款计划修改审批打回 */
    public final static String BOND_BIZ_STATUS__6 = "-6";
    /** 债券还款计划修改完成 */
    public final static String BOND_BIZ_STATUS_100 = "100";
    /** 债券还款支付申请暂存 */
    public final static String BOND_BIZ_STATUS_70 = "70";
    /** 债券还款支付申请审批中 */
    public final static String BOND_BIZ_STATUS_71 = "71";
    /** 债券还款支付申请审批打回 */
    public final static String BOND_BIZ_STATUS__7 = "-7";
    /** 债券还款支付申请完成 */
    public final static String BOND_BIZ_STATUS_101 = "101";
    /** 债券还息支付申请暂存 */
    public final static String BOND_BIZ_STATUS_80 = "80";
    /** 债券还息支付申请审批中 */
    public final static String BOND_BIZ_STATUS_81 = "81";
    /** 债券还息支付申请审批打回 */
    public final static String BOND_BIZ_STATUS__8 = "-8";
    /** 债券还息支付申请完成 */
    public final static String BOND_BIZ_STATUS_102 = "102";
    /** 债券展期暂存 */
    public final static String BOND_BIZ_STATUS_90 = "90";
    /** 债券展期审批中 */
    public final static String BOND_BIZ_STATUS_91 = "91";
    /** 债券展期审批打回 */
    public final static String BOND_BIZ_STATUS__9 = "-9";
    /** 债券展期完成 */
    public final static String BOND_BIZ_STATUS_103 = "103";
    /** 债劵注册业务前缀 */
    public final static String BOND_REGISTER_BILL_CODE_PREFIX = "Z1";
    /** 债劵发行业务前缀 */
    public final static String BOND_ISSUE_BILL_CODE_PREFIX = "Z2";
    /** 债劵可转债业务前缀 */
    public final static String BOND_TRANSFER_BILL_CODE_PREFIX = "T1";
    /** 债劵可交债业务前缀 */
    public final static String BOND_SWAP_BILL_CODE_PREFIX = "S1";
    /** 债劵展期业务前缀 */
    public final static String BOND_EXTEND_BILL_CODE_PREFIX = "E1";
    /**
     * 债券代码值错误!
     */
    public final static String BOND_EXCEL_ERROR_BONDCODE_ISERROR = "bond.excel.error.bondCode.isError";
    /**
     * 贷款类型值
     */
    public static final String CMS_TYPE_INNER = "1";
}
