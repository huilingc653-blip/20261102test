package com.bytter.accountReceivable.writeOff.constant;

public class WriteoffConstant {

    /*** 核销规则：1-应收余额  */
    public final static String WRITE_OFF_RULE_1 = "1";
    /*** 核销规则：2-付款账号  */
    public final static String WRITE_OFF_RULE_2 = "2";
    /*** 核销规则：3-收款账号  */
    public final static String WRITE_OFF_RULE_3 = "3";
    /*** 核销规则：4-到期日期  */
    public final static String WRITE_OFF_RULE_4 = "4";
    /*** 核销规则：5-应收币种  */
    public final static String WRITE_OFF_RULE_5 = "5";


    /*** 核销状态：0-	未核销  */
    public final static String WRITE_OFF_STATUS_0 = "0";
    /*** 核销状态：1- 部分核销  */
    public final static String WRITE_OFF_STATUS_1 = "1";
    /*** 核销状态：2-	已核销  */
    public final static String WRITE_OFF_STATUS_2 = "2";


    /*** 核销类型：1-	自动核销  */
    public final static String WRITE_OFF_TYPE_1 = "1";
    /*** 核销状态：2- 手工核销  */
    public final static String WRITE_OFF_TYPE_2 = "2";
    /*** 核销状态：3-	强制核销  */
    public final static String WRITE_OFF_TYPE_3 = "3";
    /*** 核销状态：4-	取消核销  */
    public final static String WRITE_OFF_TYPE_4 = "4";


    /*** 核销所属表名 - 流水  */
    public final static String WRITE_OFF_PAYMENT_MEANS_1 = "1";
    /*** 核销所属表名 - 票据  */
    public final static String WRITE_OFF_PAYMENT_MEANS_2 = "2";
    /*** 核销所属表名 - 信用证  */
    public final static String WRITE_OFF_PAYMENT_MEANS_3 = "3";

    /*** 核销数 - 一对一  */
    public final static String WRITE_OFF_NUM_11 = "11";
    /*** 核销数 - 一对多  */
    public final static String WRITE_OFF_NUM_12 = "12";
    /*** 核销数 - 多对一  */
    public final static String WRITE_OFF_NUM_21 = "21";
    /*** 核销数 - 一对零/强制核销  */
    public final static String WRITE_OFF_NUM_10 = "10";

}
