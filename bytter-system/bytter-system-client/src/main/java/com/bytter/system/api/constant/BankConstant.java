package com.bytter.system.api.constant;
/**
 * 银行信息常量类
 * Created by huangx 2018.11.22
 */
public class BankConstant {
    //数据来源
    /*** 数据来源：0-初始化  */
    public static final String IBPS_CODE_INIT = "0";
    /*** 数据来源：1-录入  */
    public static final String IBPS_CODE_INPUT = "1";
    /*** 数据来源：2-导入  */
    public static final String IBPS_CODE_IMPORT = "2";


    //自定义配置类型
    /*** 自定义配置类型：bankPayType-支付类型  */
    public static final String CUSTOM_TYPE_BANKPAYTYPE = "bankPayType";
    /*** 自定义配置类型：payeeType-收款人类型  */
    public static final String CUSTOM_TYPE_PAYEETYPE = "payeeType";
    /*** 自定义配置类型：bnfRelate-受益人关联关系  */
    public static final String CUSTOM_TYPE_BNFRELATE = "bnfRelate";
    /*** 自定义配置类型：otherCustom-其他信息模块  */
    public static final String CUSTOM_TYPE_OTHERCUSTOM = "otherCustom";

    //适用页面 1-外汇买卖交易 2-资金调拨交易 3-资金收付交易 4-外币汇款申请 5-付款办理
    /*** 适用页面：1-外汇买卖交易  */
    public static final String APPLAY_SCOPE_1 = "1";
    /*** 适用页面：2-资金调拨交易  */
    public static final String APPLAY_SCOPE_2 = "2";
    /*** 适用页面：3-资金收付交易  */
    public static final String APPLAY_SCOPE_3 = "3";
    /*** 适用页面：4-外币汇款申请  */
    public static final String APPLAY_SCOPE_4 = "4";
    /*** 适用页面：5-付款办理  */
    public static final String APPLAY_SCOPE_5 = "5";
}
