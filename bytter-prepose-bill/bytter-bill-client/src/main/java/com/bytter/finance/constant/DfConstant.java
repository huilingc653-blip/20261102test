package com.bytter.finance.constant;
import java.io.Serializable;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-05 19:18
 * @Version 1.0
 */
public class DfConstant implements Serializable {
    /** 已删除 */
    public final static int DELETED = -2;
    /** 已打回 */
    public final static int BACK = -1;
    /** 作废 */
    public final static int ABOLISH = -3;
    /** 暂存 */
    public final static int TEMP = 0;
    /** 提交审批 */
    public final static int COMMITAPPROVE = 11;
    /** 审核通过--待还款 */
    public final static int APPROVED = 95;
    /**
     * 还款完成
     */
    public final static int repaid = 100;
    /**
     * 待还款
     */
    public final static int REPAY_BIZ_STATUS_1 = 1;
    /**
     * 还款中
     */
    public final static int REPAY_BIZ_STATUS_2 = 2;
    /**
     * 已还款
     */
    public final static int REPAY_BIZ_STATUS_3 = 3;
    /** 直融申请业务流水号前缀 */
    public final static String DF_DIRECT_FINANCE_INFO_CODE = "F1";
    /** 直融还款业务流水号前缀 */
    public final static String DF_DIRECT_FINANCE_REPAY_CODE = "F2";
    /**
     * 是否 0-否 1-是
     */
    public final static String IS_FALSE = "0";
    public final static String IS_TRUE = "1";
}
