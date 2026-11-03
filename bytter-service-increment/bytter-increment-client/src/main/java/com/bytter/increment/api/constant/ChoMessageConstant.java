package com.bytter.increment.api.constant;
/**
 * 理财日志常量类
 * @author huangx
 * @since 2017/09/23.
 */
public class ChoMessageConstant {
	/*** 消息类型：1-暂存  */
    public final static Integer MESSAGE_TYPE_TEMP = 1;
    /*** 消息类型：2-提交  */
    public final static Integer MESSAGE_TYPE_SUBMIT = 2;
    /*** 消息类型：3-删除  */
    public final static Integer MESSAGE_TYPE_DELETE = 3;
    /*** 消息类型：4-审批打回  */
    public final static Integer MESSAGE_TYPE_CHECK_BACK = 4;
    /*** 消息类型：5-审批通过  */
    public final static Integer MESSAGE_TYPE_CHECK_PASS = 5;
    /*** 消息类型：6-补录 */
    public final static Integer MESSAGE_TYPE_BL = 6;
    /*** 消息类型：7-记账打回 */
    public final static Integer MESSAGE_TYPE_TALLY_BACK = 7;
    
    /*** 消息类型：20-自动生成  */
    public final static Integer MESSAGE_TYPE_AUTO_ADD = 20;
    /*** 消息类型：21-指令发送  */
    public final static Integer MESSAGE_TYPE_EXC_SEND = 21;
    /*** 消息类型：22-指令退回  */
    public final static Integer MESSAGE_TYPE_EXC_BACK = 22;
    /*** 消息类型：23-指令作废  */
    public final static Integer MESSAGE_TYPE_EXC_REPEAL = 23;
    /*** 消息类型：24-指令重发  */
    public final static Integer MESSAGE_TYPE_EXC_RESEND = 24;
    /*** 消息类型：25-指令恢复 */
    public final static Integer MESSAGE_TYPE_EXC_RECOVER = 25;
	/*** 消息类型：26-审批撤回  */
	public final static Integer MESSAGE_TYPE_CHECK_REVOKE = 26;
    
    /*** 业务类型：bank-银行理财  */
    public final static String BUS_TYPE_BANK = "bank";
    /*** 业务类型：inner-内部理财  */
    public final static String BUS_TYPE_INNER= "inner";
    /*** 业务类型：bdlink-理财指令  */
    public final static String BUS_TYPE_BDLINK = "bdlink";
    /*** 业务类型：product-理财产品登记  */
    public final static String BUS_TYPE_PRODUCT_REQ = "product_req";
    /*** 业务类型：product-理财产品赎回  */
    public final static String BUS_TYPE_PRODUCT_REDEEM = "product_redeem";
    /*** 业务类型：product-理财产品续期  */
    public final static String BUS_TYPE_PRODUCT_RENEWAL = "product_renewal";
    /*** 业务类型：agree-协定存款  */
    public final static String BUS_TYPE_AGREE = "agree";
}
