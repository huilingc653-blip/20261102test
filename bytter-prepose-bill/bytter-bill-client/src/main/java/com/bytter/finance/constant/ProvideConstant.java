package com.bytter.finance.constant;
import java.io.Serializable;
/**
 * 借款常量类
 */
public class ProvideConstant implements Serializable {
    /** 借款申请billCode前缀 */
    public final static String INFO_BILL_CODE_PREFIX = "B1";
    /** 借款发放billCode前缀 */
    public final static String SEND_BILL_CODE_PREFIX = "B2";
    /** 借款还款billCode前缀 */
    public final static String REPAY_BILL_CODE_PREFIX = "B3";
    /** 借款展期billCode前缀 */
    public final static String DEFERRED_BILL_CODE_PREFIX = "B4";
    /** 借款挂担保sysCode常量 */
    public final static String PROVIDE_GUA_SYSCODE = "provide";
    /**
     * 银团明细导入错误提示
     */
    /* 银行名称不能为空！ */
    public static final String CMS_PROVIDE_BANKNAME_ISNULL = "cms.provide.bankname.isnull";
    /* 银行开户行号不能为空！ */
    public static final String CMS_PROVIDE_BANKCODE_ISNULL = "cms.provide.bankcode.isnull";
    /**
     * 联行号与银行名称不匹配
     */
    public static final String CMS_PROVIDE_BANKCODE_NOTMATCH = "cms.provide.bankcode.notmatch";
    /**
     * 联行号在系统中不存在
     */
    public static final String CMS_PROVIDE_BANKCODE_NOTEXIST = "cms.provide.bankcode.notexist";
    /* 本次放款不能为空！ */
    public static final String CMS_PROVIDE_SENDMONEY_ISNULL = "cms.provide.sendmoney.isnull";
    /* 承贷金额不能为空！ */
    public static final String CMS_PROVIDE_COMMITMENTMONEY_ISNULL = "cms.provide.commitmentmoney.isnull";
    /* 本次还款金额不能为空！ */
    public static final String CMS_PROVIDE_REPAYMENTMONEY_ISNULL = "cms.provide.repaymentmoney.isnull";
    /* 还款利息不能为空！ */
    public static final String CMS_PROVIDE_REPAYMENTIST_ISNULL = "cms.provide.repaymentist.isnull";
}
