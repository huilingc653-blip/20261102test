package com.bytter.supply.constant;

/**
 * @author ztd
 * @description TODO
 * @since 2026/7/7
 */
public class ScfBusinessConstant {

    /**
     * 付息方式：1 卖方付息
     */
    public static final String INTEREST_MODE_SELLER = "1";

    /**
     * 付息方式：2 买方付息
     */
    public static final String INTEREST_MODE_BUYER = "2";

    // ====================== 转让标识 ======================

    /**
     * 转让标识：1 一对多，凭证拆分转让多家企业
     */
    public static final String TRANSFER_FLAG_ONE_TO_MANY = "1";

    /**
     * 转让标识：2 多对一，多张凭证转让一家企业
     */
    public static final String TRANSFER_FLAG_MANY_TO_ONE = "2";

    // ====================== 提交状态 ======================

    /**
     * 提交状态：submit 提交
     */
    public static final String SEND_STATUS_SUBMIT = "submit";

    /**
     * 提交状态：save 暂存
     */
    public static final String SEND_STATUS_SAVE = "save";

    // ====================== 是否允许拆分 ======================

    /**
     * 是否允许拆分：0 否
     */
    public static final String ALLOW_SPLIT_NO = "0";

    /**
     * 是否允许拆分：1 是
     */
    public static final String ALLOW_SPLIT_YES = "1";

    // ====================== 凭证签收状态 ======================
    /**
     * 凭证签收状态：1 未开立
     */
    public static final String PAYABLE_VOUCHER_SIGN_STATUS_UNOPENED = "1";
    /**
     * 凭证签收状态：2 开立中
     */
    public static final String PAYABLE_VOUCHER_SIGN_STATUS_OPENING = "2";
    /**
     * 凭证签收状态：3 已开立
     */
    public static final String PAYABLE_VOUCHER_SIGN_STATUS_OPENED = "3";
    /**
     * 凭证签收状态：4 已撤销
     */
    public static final String PAYABLE_VOUCHER_SIGN_STATUS_CANCELED = "4";
    /**
     * 凭证签收状态：5 已拒签
     */
    public static final String PAYABLE_VOUCHER_SIGN_STATUS_REJECTED = "5";
    /**
     * 凭证签收状态：6 已签收
     */
    public static final String PAYABLE_VOUCHER_SIGN_STATUS_SIGNED = "6";

    /**
     * 是否有效(0：无效；1：有效)
     */
    public static final String ENABLE_NO = "0";
    public static final String ENABLE_YES = "1";
}
