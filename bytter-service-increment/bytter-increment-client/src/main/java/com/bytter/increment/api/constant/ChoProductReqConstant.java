package com.bytter.increment.api.constant;
/**
 * 理财产品常量类
 * @author sench
 * @since 2017/09/23.
 */
public class ChoProductReqConstant {
    /*** 理财登记参数配置：choProduct-理财登记 */
    public static final String CHO_PRODUCT_CONFIG_TYPE = "choProduct";
    /*** 理财登记参数配置：endDay-理财到期提醒 */
    public static final String CHO_PRODUCT_CONFIG_ENDDAY = "endDay";
    /*** 产品理财状态：0-暂存  */
    public final static Integer STATUE_TEMP = 0;
    /*** 产品理财状态：1-打回  */
    public final static Integer STATUE_BACK = -1;
    /*** 产品理财状态：-2-删除  */
    public final static Integer STATUE_DEL = -2;
    /*** 产品理财状态：11-待审批  */
    public final static Integer STATUE_WAIT_CHECK = 11;
    /*** 产品理财状态：95-审批通过  */
    public final static Integer STATUE_CHECK = 95;
    /*** 产品理财状态：95-业务结束(产品全部赎回完毕)*/
    public final static Integer STATUE_FINISH = 110;
    /*** 金额单位-元*/
    public static final String COMMON_MONEYUNIT_1 = "1";
    /*** 金额单位-万元*/
    public static final String COMMON_MONEYUNIT_10000 = "10000";
    /**理财类业务查询:理财暂存*/
    public final static String QUERY_STATUS_TEMP = "0";
    /**理财类业务查询:理财审批中*/
    public final static String QUERY_STATUS_WAIT_CHECK = "11";
    /**理财类业务查询:理财审批通过*/
    public final static String QUERY_STATUS_CHECK = "95";
    /**理财类业务查询:理财打回*/
    public final static String QUERY_STATUS_BACK = "-1";
    /**理财类业务查询:赎回暂存*/
    public final static String QUERY_STATUS_REDEEMTEMP = "20";
    /**理财类业务查询:赎回审批中*/
    public final static String QUERY_STATUS_REDEEMWAITCHECK = "21";
    /**理财类业务查询:赎回审批通过*/
    public final static String QUERY_STATUS_REDEEMCHECK = "25";
    /**理财类业务查询:赎回审批打回*/
    public final static String QUERY_STATUS_REDEEMBACK = "27";
    /**理财类业务查询:业务结束*/
    public final static String QUERY_STATUS_FINISH = "110";
    /**
     * 保本收益型
     */
    public static final String FINANCIAL_TYPE0 = "0";
    /**
     * 保本浮动收益型
     */
    public static final String FINANCIAL_TYPE1 = "1";
    /**
     * 非保本浮动收益型
     */
    public static final String FINANCIAL_TYPE2 = "2";
    /**
     * 理财登记暂存
     */
    public static final Integer BIZ_STATUS1 = 1;
    /**
     * 理财登记审批中
     */
    public static final Integer BIZ_STATUS2 = 2;
    /**
     * 理财登记打回
     */
    public static final Integer BIZ_STATUS3 = 3;
    /**
     * 理财登记审批通过
     */
    public static final Integer BIZ_STATUS4 = 4;
    /**
     * 理财赎回暂存
     */
    public static final Integer BIZ_STATUS5 = 5;
    /**
     * 理财赎回审批中
     */
    public static final Integer BIZ_STATUS6 = 6;
    /**
     * 理财赎回打回
     */
    public static final Integer BIZ_STATUS7 = 7;
    /**
     * 理财赎回审批通过
     */
    public static final Integer BIZ_STATUS8 = 8;
    /**
     * 理财续期暂存
     */
    public static final Integer BIZ_STATUS9 = 9;
    /**
     * 理财续期审批中
     */
    public static final Integer BIZ_STATUS10 = 10;
    /**
     * 理财续期打回
     */
    public static final Integer BIZ_STATUS11 = 11;
    /**
     * 理财续期审批通过
     */
    public static final Integer BIZ_STATUS12 = 12;
    /**
     * 理财购买申请暂存
     */
    public static final Integer BIZ_STATUS13 = 13;
    /**
     * 理财购买申请审批中
     */
    public static final Integer BIZ_STATUS14 = 14;
    /**
     * 理财购买申请打回
     */
    public static final Integer BIZ_STATUS15 = 15;
    /**
     * 理财购买申请审批通过
     */
    public static final Integer BIZ_STATUS16 = 16;
    /**
     * 业务结束
     */
    public static final Integer BIZ_STATUS20 = 20;
}
