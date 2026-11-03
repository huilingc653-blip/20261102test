package com.bytter.guarantee.constant;
/**
 * 保证金常量类
 * 
 * @author fusb
 * @since 2018-9-11
 */
public class FcBondConstant {
    /**
     * 审批对应的key:存出保证金
     */
    public final static String FC_FCBONDDETAIL_FLOW = "netbankFcBondSave";
    /**
     * 审批对应的key:追加保证金
     */
    public final static String FC_FCBONDADD_FLOW = "netbankFcBondAdd";
    /**
     * 审批对应的key:保证金维护
     */
    public final static String FC_FCBONDPRESERVE_FLOW = "netbankFcBondPreserve";
    /*** 消息类型：0-暂存 */
    public final static Integer MESSAGE_TYPE_TEMP = 0;
    /*** 消息类型：1-提交 */
    public final static Integer MESSAGE_TYPE_SUBMIT = 1;
    /*** 消息类型：2-录入 */
    public final static Integer MESSAGE_TYPE_ADD = 2;
    /*** 消息类型：3-删除 */
    public final static Integer MESSAGE_TYPE_DELETE = 3;
    /*** 消息类型：4-审批打回 */
    public final static Integer MESSAGE_TYPE_CHECK_BACK = 4;
    /*** 消息类型：5-审批通过 */
    public final static Integer MESSAGE_TYPE_CHECK_PASS = 5;
    /*** 消息类型：6-编辑 */
    public final static Integer MESSAGE_TYPE_UPDATE = 6;
    /** 消息类型：7-释放 */
    public final static Integer MESSAGE_TYPE_RELEASE = 7;
    /** 消息类型：8-业务删除打回 */
    public final static Integer MESSAGE_TYPE_BACK = 8;
    /*** 保证金处理状态：0-暂存 */
    public final static Integer STATUE_TEMP = 0;
    /*** 保证金处理状态：-1-打回 */
    public final static Integer STATUE_BACK = -1;
    /*** 保证金处理状态：-2-删除 */
    public final static Integer STATUE_DEL = -2;
    /*** 保证金处理状态：11-待审批 */
    public final static Integer STATUE_WAIT_CHECK = 11;
    /*** 保证金处理状态：95-审批通过 */
    public final static Integer STATUE_CHECK = 95;
    /*** 保证金处理状态：100-已汇总 */
    public final static Integer STATUE_COLLECT = 100;
    /*** 保证金处理状态：95-占用 */
    public final static Integer STATUE_APPLY = 95;
    /*** 保证金处理状态：2-释放 */
    public final static Integer STATUE_RELEASE = 2;
    /** 占用类型："0"-业务占用 */
    public final static String APPLY_TYPE_BUSINESS = "0";
    /** 占用类型："1"-手工占用 */
    public final static String APPLY_TYPE_HANDWORK = "1";
    /** 项目类型："0"-银行授信 */
    public final static String ITEM_TYPE_CREDIT = "0";
    /** 项目类型："1"-银行借款 */
    public final static String ITEM_TYPE_LOAN = "1";
    /** 项目类型："2"-承兑汇票 */
    public final static String ITEM_TYPE_BILL = "2";
    /** 项目类型："3"-手工录入 */
    public final static String ITEM_TYPE_OTHER = "3";
    /** 项目类型："4"-授信追加 */
    public final static String ITEM_TYPE_ADD = "4";
    /** 项目类型："5"- 保函 */
    public final static String ITEM_TYPE_GUA = "5";
    /** 项目类型："6"- 国际结算 */
    public final static String ITEM_TYPE_INTERNATIONAL = "6";
    /** 项目类型："7"- 融资租赁 */
    public final static String ITEM_TYPE_LEASE = "7";
    /** 项目类型："8"- 债券 */
    public final static String ITEM_TYPE_BOND = "8";
    /** 项目类型："9"- 资产证券化 */
    public final static String ITEM_TYPE_SECURITY = "9";
    /** 挂接保证金 */
    public final static String IS_HOOK_Y = "1";
    /** 不挂接保证金 */
    public final static String IS_HOOK_N = "0";
    /** 生成付款单:1-生成 */
    public final static String PAYED_IS = "1";
    /** 生成付款单:1-不生成 */
    public final static String PAYED_NOT = "0";
    /** 保证金银行控制选择 0银行行号相同 1银行类别相同 */
    public final static String BAIL_BANK_CONTROL_TYPE_0 = "0";
    public final static String BAIL_BANK_CONTROL_TYPE_1 = "1";
    public static final String OP_MSG_INPUT_SUBMIT = "录入提交";
    public static final String OP_MSG_INPUT_TEMP = "录入暂存";
    public static final String OP_MSG_UPDATE_SUBMIT = "修改提交";
    public static final String OP_MSG_UPDATE_TEMP = "修改暂存";
    public static final String OP_MSG_CHECK_AUTO_SUCCESS = "系统自动审批通过";
    public static final String OP_MSG_CHECK_SUCCESS = "审批通过";
    public static final String OP_MSG_CHECK_BACK = "审批打回";
    public static final String OP_MSG_DELETE = "删除";
    public static final String OP_MSG_RELEASE = "释放保证金";
    /** 以下是银行保证金管理的相关权限 */
    /** 存出保证金: 查看权限 */
    public final static String NETBANK_FCBOND_DEPOSITOUT_QUERY = "netbank:fcBond:depositOut:query";
    /** 存出保证金：增删改权限 */
    public final static String NETBANK_FCBOND_DEPOSITOUT_APPLY = "netbank:fcBond:depositOut:apply";
    /** 追加保证金: 增删改查权限 */
    public final static String NETBANK_FCBONDADD_DEPOSIT_APPLY = "netbank:fcBondAdd:deposit:apply";
    /** 追加保证金: 查詢权限 */
    public final static String NETBANK_FCBONDADD_DEPOSIT_QUERY = "netbank:fcBondAdd:deposit:query";
    /** 维护保证金: 增删改查权限 */
    public final static String NETBANK_FCBONDORESERVE_DEPOSIT_APPLY = "netbank:fcBondPreserve:deposit:apply";
    /** 维护保证金: 查询权限 */
    public final static String NETBANK_FCBONDORESERVE_DEPOSIT_QUERY = "netbank:fcBondPreserve:deposit:query";
    /** 保证金转回: 转回权限 */
    public final static String NETBANK_FCBOND_DEPOSITBACK_APPLY = "netbank:fcBond:depositBack:apply";
    /** 保证金转回: 查看权限 */
    public final static String NETBANK_FCBOND_DEPOSITBACK_QUERY = "netbank:fcBond:depositBack:query";
    /** 保证金明细表: 查询权限 */
    public final static String NETBANK_FCBOND_QUERY = "netbank:fcBond:query";
}
