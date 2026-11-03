package com.bytter.finance.constant;
import java.io.Serializable;
public class InlWorkFlowConstant implements Serializable {
    /**
     * 融资租赁标的物登记
     */
    public final static String LEASE_OBJECT_INFO = "LeaseObjectInfo";
    /**
     * 融资租赁退租
     */
    public final static String LEASE_AFTER_REFUND = "afterLeaseRefund";
    /**
     * 融资租赁续租
     */
    public final static String LEASE_AFTER_CONTINUE = "afterLeaseContinue";
    /**
     * 融资租赁回购
     */
    public final static String LEASE_AFTER_BUYBACK = "afterLeaseBuyBack";
    /**
     * 融资租赁标的物修改
     */
    public final static String LEASE_UPDATE_OBJECT_INFO = "LeaseUpdateObjectInfo";
    /**
     * 融资租赁标的物登记
     */
    public final static String LEASE_EXTEND = "LeaseExtend";
    /**
     * 内部拆借登记
     */
    public final static String CMS_INL_INFO = "cmsInlInfo";
    /**
     * 内部拆借登记（拆出方）
     */
    public final static String CMS_INL_OUT_INFO = "cmsInlOutInfo";
    /**
     * 内部拆借发放
     */
    public final static String CMS_INL_SEND = "cmsInlSend";
    /**
     * 内部拆借还款
     */
    public final static String CMS_INL_REPAY = "cmsInlRepay";
    /**
     * 内部拆借展期
     */
    public final static String CMS_INL_DEFERRED = "cmsInlDeferred";
    /**
     * 内部拆借作废
     */
    public final static String CMS_INL_ABOLISH = "cmsInlAbolish";
    /**
     * 内部拆借利率调整
     */
    public final static String CMS_INL_ADJUSTRATE = "cmsInlAdjustrate";
    /**
     * 内部拆借还款计划修改
     */
    public final static String CMS_INL_REPLAY_CHANGE = "cmsInlRepayChange";
}
