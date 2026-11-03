package com.bytter.finance.constant;
import java.io.Serializable;
/**
 * 融资租赁常量类
 *
 */
public class LeaseConstant implements Serializable {
    /**
     * 融资租赁申请lease_no前缀
     */
    public final static String FINANCING_LEASE_LEASE_NO = "L1";
    /**
     * 融资租赁资金计划lease_no前缀
     */
    public final static String LEASECAPITALPLAN_LEASE_LEASE_NO = "L4";
    /**
     * 融资租赁租金支付lease_no前缀
     */
    public final static String LEASEPAY_LEASE_LEASE_NO = "L5";
    /**
     * 融资挂担保sysCode常量
     */
    public final static String LEASE_GUA_SYSCODE = "lease";
    /**
     * 租赁租金计划导入错误提示
     */
    /* 计划付租日不能为空！ */
    public static final String CMS_LEASECAPITALPLANDEL_PAYPLANDATE_ISNULL = "cms.leasecapitalplandel.payplandate.isnull";
    /* 租赁本金不能为空！ */
    public static final String CMS_LEASECAPITALPLANDEL_LEASEMONRY_ISNULL = "cms.leasecapitalplandel.leasemonry.isnull";
    /* 租赁利息不能为空！ */
    public static final String CMS_LEASECAPITALPLANDEL_LEASEINTEREST_ISNULL = "cms.leasecapitalplandel.leaseinterest.isnull";
    /* 本期费用不能为空！ */
    public static final String CMS_LEASECAPITALPLANDEL_THISCOST_ISNULL = "cms.leasecapitalplandel.thiscost.isnull";
    /* 保证金不能为空！ */
    public static final String CMS_LEASECAPITALPLANDEL_BAILMONEY_ISNULL = "cms.leasecapitalplandel.bailmoney.isnull";
    /* 是否已还标识不能为空！ */
    public static final String CMS_LEASECAPITALPLANDEL_ISREPAY_ISNULL = "cms.leasecapitalplandel.isRepay.isnull";
    /* 是否已还标识输入值错误！ */
    public static final String CMS_LEASECAPITALPLANDEL_ISREPAY_ERROR = "cms.leasecapitalplandel.isRepay.error";
}
