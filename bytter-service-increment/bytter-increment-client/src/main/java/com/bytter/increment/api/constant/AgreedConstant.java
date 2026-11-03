package com.bytter.increment.api.constant;
/**
 * 协定存款常量类
 * @author wangzx
 * @since 2019/5/14.
 */
public class AgreedConstant {
	/*** 协定登记状态：0-暂存  */
	public final static Integer STATUE_TEMP = 0;
	/*** 协定登记状态：1-复核打回  */
    public final static Integer STATUE_BACK = -1;
    /*** 协定登记状态：-2-删除  */
    public final static Integer STATUE_DEL = -2;
    /*** 协定登记状态：11-待复核  */
    public final static Integer STATUE_WAIT_CHECK = 11;
    /*** 协定登记状态：95-复核通过  */
    public final static Integer STATUE_CHECK = 95;
    /*** 协定单据状态：10-登记  */
    public final static Integer BILL_STATUS_10 = 10;
    /*** 协定单据状态：11-延期/终止  */
    public final static Integer BILL_STATUS_11 = 11;
    /*** 单据类型：0-登记  */
    public final static String BUS_TYPE_0 = "0";
    /*** 单据类型：1-延期  */
    public final static String BUS_TYPE_1 = "1";
    /*** 单据类型：2-终止  */
    public final static String BUS_TYPE_2 = "2";
    /*** 资金池标识 0-非资金池*/
    public final static String POOL_SIGN_0 = "0";
    /*** 资金池标识 1-资金池*/
    public final static String POOL_SIGN_1 = "1";
}
