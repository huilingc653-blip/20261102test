package com.bytter.biz.payment.api.constant;
/**
 * 付款日志常量类
 * @author huangx
 * @since 2017/6/12.
 */
public class BtBankAccMessageConstant {
    /*** 消息类型：0-余额初始化  */
    public final static Integer MESSAGE_TYPE_INIT = 0;
    /*** 消息类型：10-由明细生成余额  */
    public final static Integer MESSAGE_TYPE_DTLINIT = 10;
	/*** 消息类型：1-新增  */
    public final static Integer MESSAGE_TYPE_ADD = 1;
    /*** 消息类型：2-编辑  */
    public final static Integer MESSAGE_TYPE_UPDATE = 2;
    /*** 消息类型：3-删除  */
    public final static Integer MESSAGE_TYPE_DELETE = 3;
    /*** 消息类型：4-审批打回  */
    public final static Integer MESSAGE_TYPE_CHECK_BACK = 4;
    /*** 消息类型：5-审批通过  */
    public final static Integer MESSAGE_TYPE_CHECK_PASS = 5;
    /*** 消息类型：6-导入  */
    public final static Integer MESSAGE_TYPE_IMPORT = 6;
    /*** 消息类型：7-办理打回  */
    public final static Integer MESSAGE_TYPE_BACK = 7;
	/*** 消息类型：8-提交  */
	public final static Integer MESSAGE_TYPE_SUBMIT = 8;
    /*** 消息类型：11-变更暂存  */
    public final static Integer MESSAGE_TYPE_CHANGE_UPDATE = 11;
    /*** 消息类型：12-变更提交  */
    public final static Integer MESSAGE_TYPE_CHANGE_SUBMIT = 12;
    /*** 消息类型：13-作废  */
    public final static Integer MESSAGE_TYPE_DEFEAT = 13;
	/*** 消息类型：26-审批撤回  */
	public final static Integer MESSAGE_TYPE_CHECK_REVOKE = 26;
    /*** 业务类型：app-开户  */
    public final static String BUS_TYPE_APP = "app";
    /*** 业务类型：apphandle-开户办理  */
    public final static String BUS_TYPE_APPHANDLE = "apphandle";
    /*** 业务类型：rev-变更  */
    public final static String BUS_TYPE_REV = "rev";
    /*** 业务类型：revhandle 变更办理 */
    public final static String BUS_TYPE_REVHANDLE = "revhandle";
    /*** 业务类型：del-注销  */
    public final static String BUS_TYPE_DEL = "del";
    /*** 业务类型：delhandle-注销办理  */
    public final static String BUS_TYPE_DELHANDLE = "delhandle";
    /*** 业务类型：acchandle-银行账号维护  */
    public final static String BUS_TYPE_ACCHANDLE = "acchandle";
	/*** 业务类型：accbal-银行账户余额管理  */
	public final static String BUS_TYPE_ACCBAL = "accbal";
	/*** 业务类型：accdtl-银行账号流水管理  */
	public final static String BUS_TYPE_ACCDTL= "accdtl";
}
