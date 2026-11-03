package com.bytter.system.constants;
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
//    Valid tags
    public final static String STATUS_VALID_ONE = "1";
    public final static String STATUS_VALID_ZERO = "0";

    /*** 发送状态 0-发送成功 1-发送失败 */
    public static final String EXCEPTION_WARN_STATUS_0 = "0";
    public static final String EXCEPTION_WARN_STATUS_1 = "1";

}
