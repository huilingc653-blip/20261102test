package com.bytter.letter.domain.constant;
import java.io.Serializable;
@SuppressWarnings("serial")
/**
 * 保函模块常量类
 */
public class LetterConstant implements Serializable {
    /**
     * 保函更改编号前缀
     */
    public static final String GUA_GUARANTEE_CHANGE_CODE = "LGC";
    /**
     * 申请状态 -2 删除 ,-1 打回,0 暂存,-3 领取失败, 11 待审批 ,95审批通过,101 完成更改,102 拒绝更改,103 更改中,100 注销,104 终止中,105 已终止,106 终止打回
     */
    public static final Integer STATUS_DELETE = -2;
    public static final Integer STATUS_BACK = -1;
    public static final Integer STATUS_FAIL = -3;
    public static final Integer STATUS_TEMP = 0;
    public static final Integer STATUS_WAIT_CHECK = 11;
    public static final Integer STATUS_CHECK = 95;
    public static final Integer STATUS_CHANGE_CHECK = 101;
    public static final Integer STATUS_CHANGE_REFUSE = 102;
    public static final Integer STATUS_CHANGEING = 103;
    public static final Integer STATUS_LOGOUT = 100;
    public static final Integer STATUS_ENDING = 104;
    public static final Integer STATUS_END = 105;
    public static final Integer STATUS_END_BACK = 106;
    /**
     * 担保方式 0 抵押，1 质押，2 保证
     */
    public static final String GUA_TYPE_DY = "0";
    public static final String GUA_TYPE_ZY = "1";
    public static final String GUA_TYPE_BZ = "2";
    /**
     * 类型 1-释放 2-占用
     */
    public static final String GUA_TRACK_SF = "1";
    public static final String GUA_TRACK_ZY = "2";
    /**
     * 是否已变更 0-未变更 1-已变更
     */
    public static final String GUA_NO_CHANGE = "0";
    public static final String GUA_YES_CHANGE = "1";
    /** 以下是担保管理的相关权限 */
    /**
     * 保函申请: 查询权限
     */
    public final static String GUA_GUARANTEE_INFO_QUERY = "gua:guarantee:info:query";
    /**
     * 保函申请: 操作权限
     */
    public final static String GUA_GUARANTEE_INFO_APPLY = "gua:guarantee:info:apply";
    /**
     * 保函领取:领取权限
     */
    public final static String GUA_GUARANTEE_INFO_RECEIVE = "gua:guarantee:info:receive";
    /**
     * 保函履行登记:登记权限
     */
    public final static String GUA_GUARANTEE_INFO_REGISTER = "gua:guarantee:info:register";
    /**
     * 保函更改申请：查询权限
     */
    public final static String GUA_GUARANTEE_CHANGE_QUERY = "gua:guarantee:change:query";
    /**
     * 保函更改申请：应用权限
     */
    public final static String GUA_GUARANTEE_CHANGE_APPLY = "gua:guarantee:change:apply";
    /**
     * 保函更改办理:查询权限
     */
    public final static String GUA_GUARANTEE_CHANGEHANDLE_QUERY = "gua:guarantee:changehandle:query";
    /**
     * 保函更改办理:应用权限
     */
    public final static String GUA_GUARANTEE_CHANGEHANDLE_APPLY = "gua:guarantee:changehandle:apply";
    /**
     * 保函: 注销权限
     */
    public final static String GUA_GUARANTEE_INFO_LOGOUT = "gua:guarantee:info:logout";
    /**
     * 对外担保合同还款计划: 列表查看权限
     */
    public final static String GUA_CONTRACT_INFOPLAN_QUERY = "gua:contract:infoplan:query";
    /**
     * 消息类型
     */
    /**
     * 消息类型：0-暂存
     */
    public final static String MESSAGE_TYPE_TEMP = "0";
    /**
     * 消息类型：1-提交
     */
    public final static String MESSAGE_TYPE_SUBMIT = "1";
    /**
     * 消息类型：2-录入
     */
    public final static String MESSAGE_TYPE_ADD = "2";
    /**
     * 消息类型：3-删除
     */
    public final static String MESSAGE_TYPE_DELETE = "3";
    /**
     * 消息类型：4-审批打回
     */
    public final static String MESSAGE_TYPE_CHECK_BACK = "4";
    /**
     * 消息类型：5-审批通过
     */
    public final static String MESSAGE_TYPE_CHECK_PASS = "5";
    /**
     * 消息类型：6-编辑
     */
    public final static String MESSAGE_TYPE_UPDATE = "6";
    /**
     * 消息类型：7-变更
     */
    public final static String MESSAGE_TYPE_CHANGE = "7";
    /**
     * 消息类型：9-注销
     */
    public final static String MESSAGE_TYPE_LOGOUT = "9";
    /**
     * 操作信息
     */
    public static final String OP_MSG_INPUT_SUBMIT = "录入提交";
    public static final String OP_MSG_INPUT_TEMP = "录入暂存";
    public static final String OP_MSG_UPDATE_SUBMIT = "修改提交";
    public static final String OP_MSG_UPDATE_TEMP = "修改暂存";
    public static final String OP_MSG_CHECK_SUCCESS = "审批通过";
    public static final String OP_MSG_CHECK_BACK = "审批打回";
    public static final String OP_MSG_DELETE = "删除";
    public static final String OP_MSG_CHANGE = "变更";
    public static final String OP_MSG_LOGOUT = "注销";
    /**
     * 保函相关常量
     */
    /**
     * 保函领取完成
     */
    public static final Integer GUARANTEE_RECEIVED = 96;
    /**
     * 履行中
     */
    public static final Integer GUARANTEE_REGISTERED = 97;
    /**
     * 履行完成
     */
    public static final Integer GUARANTEE_REGISFINISH = 98;
    /**
     * 担保合同状态
     */
    /**
     * 暂存
     */
    public static final Integer GUA_CONTRACT_TEMP = 0;
    /**
     * 在审使用
     */
    public static final Integer GUA_CONTRACT_APPROVED = 1;
    /**
     * 使用中
     */
    public static final Integer GUA_CONTRACT_USED = 2;
    /**
     * 释放
     */
    public static final Integer GUA_CONTRACT_RELEASE = 3;
    /**
     * 担保模块公用是否标识-是
     */
    public static final String GUA_COMMON_Y = "1";
    /**
     * 担保模块公用是否标识-否
     */
    public static final String GUA_COMMON_N = "0";
    /**
     * 对接合同管理：合同名称
     */
    public final static String CON_NAME = "担保合同";

    /**
     * 提交
     */
    public final static int MSGA_SUBMIT = 20;
    /**
     * 录入暂存
     */
    public final static int MSGA_INPUT = 21;
    /**
     * 修改暂存
     */
    public final static int MSGA_MODIFY = 22;
    /**
     * 录入提交
     */
    public final static String MSGA_INPUT_SUBMIT = "49";
    /**
     * 补录提交
     */
    public final static String MSGA_REPAIR_SUBMIT = "50";
    /**
     * 修改提交
     */
    public final static int MSGA_REPAIR_MODIFY = 32;
    /**
     * 抵押是否允许多次挂接
     */
    public final static String MANY_TIMES = "manyTimes";
    /**
     * 审批通过
     */
    public final static int MSGA_CHECKOK = 24;
    /**
     * 审批打回
     */
    public final static String MSGA_CHECKBACK = "72";
    /**
     * 注销
     */
    public final static String MSGA_LOGOUT = "73";
    /**
     * 变更
     */
    public final static String MSGA_CHANGE = "74";
    /**
     * 保函到期提醒天数
     */
    public final static String GUA_GUARANTEE_NOTIFYDAYS = "notify_Days";
    /**
     * 保函是否挂接手续
     */
    public final static String GUA_GUARANTEE_RAT_CONTAINS = "rat_Contains";

}
