package com.bytter.finance.constant;
import java.io.Serializable;
/**
 * @author luxy
 * @since 2019/5/22 17:24
 */
public class PricingConstant implements Serializable {
    /**
     * 定价管理模块公用是否标识-是
     */
    public static final String PRICING_COMMON_Y = "1";
    /**
     * 定价管理模块公用是否标识-否
     */
    public static final String PRICING_COMMON_N = "0";
    /**
     * 存款利率维护状态标识-暂存
     */
    public static final Integer STATUS_TEMP = 0;
    /**
     * 存款利率维护状态标识-待复核
     */
    public static final Integer STATUS_RECHECK = 1;
    /**
     * 存款利率维护状态标识-复核打回
     */
    public static final Integer STATUS_BACK = -1;
    /**
     * 存款利率维护状态标识-复核通过
     */
    public static final Integer STATUS_PASS = 2;
    /**
     * 保存标识-暂存
     */
    public static final String SAVE_TEMP = "0";
    /**
     * 保存标识-提交
     */
    public static final String SAVE_SUBMIT = "1";
    /**
     * 存款类型标识-活期
     */
    public static final String DEPOSIT_CLASS_HQ = "04";
    /**
     * 存款类型标识-定期
     */
    public static final String DEPOSIT_CLASS_DQ = "05";
    /**
     * 存款类型标识-通知
     */
    public static final String DEPOSIT_CLASS_TZ = "06";
    /**
     * 存款账户标识 1-内部账户存款
     */
    public static final String DEPOSIT_ACC_SIGN_INTERNAL = "1";
    /**
     * 存款账户标识 2-资金池账户存款
     */
    public static final String DEPOSIT_ACC_SIGN_POOL = "2";
    /**
     * 存款账户标识 3-银行账户存款
     */
    public static final String DEPOSIT_ACC_SIGN_BANK = "3";
}
