package com.bytter.core.constant;
/**
 * 付款日志常量类
 * @author huangx
 * @since 2017/6/12.
 */
public class NisMessageConstant {
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
    /*** 消息类型：6-合并  */
    public final static Integer MESSAGE_TYPE_MERGE = 6;
    /*** 消息类型：7-撤销合并  */
    public final static Integer MESSAGE_TYPE_MERGE_CANCEL = 7;
    /*** 消息类型：8-拆分  */
    public final static Integer MESSAGE_TYPE_SPLIT = 8;
    /*** 消息类型：9-撤销拆分  */
    public final static Integer MESSAGE_TYPE_SPLIT_CANCEL = 9;
    /*** 消息类型：10-汇总  */
    public final static Integer MESSAGE_TYPE_COLLECT = 10;
    /*** 消息类型：11-汇总打回  */
    public final static Integer MESSAGE_TYPE_COLLECT_BACK = 11;
    /*** 消息类型：12-付款执行打回  */
    public final static Integer MESSAGE_TYPE_EXECUTE_BACK = 12;
    /*** 消息类型：13-付款执行发送  */
    public final static Integer MESSAGE_TYPE_EXECUTE_SEND = 13;
    /*** 消息类型：16-退回（外部系统，如票据退票）  */
    public final static Integer MESSAGE_TYPE_BACK = 16;
    /*** 消息类型：17-付款成功  */
    public final static Integer MESSAGE_TYPE_SUCCEED = 17;
    /*** 消息类型：18-结果异常打回（代发工资）  */
    public final static Integer MESSAGE_TYPE_DEAL_BACK = 18;
    /*** 消息类型：19-手工记账打回  */
    public final static Integer MESSAGE_TYPE_CMSTALLY_BACK = 19;
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
    /*** 消息类型：27-支票打印  */
    public final static Integer MESSAGE_TYPE_BILL_PRINT = 27;
    /*** 消息类型：28-批量设置付款账号  */
    public final static Integer MESSAGE_TYPE_SET_PAYERACC = 28;
    /*** 消息类型：29-审批修改  */
    public final static Integer MESSAGE_TYPE_APPROVE_UPDATE = 29;
    /*** 消息类型：30-记账暂存  */
    public final static Integer MESSAGE_TYPE_TALLY_TEMP = 30;
    /*** 消息类型：31-记账提交  */
    public final static Integer MESSAGE_TYPE_TALLY_SUBMIT = 31;
    /*** 消息类型：32-记账打回  */
    public final static Integer MESSAGE_TYPE_TALLY_BACK = 32;
    /*** 消息类型：33-改账打回  */
    public final static Integer MESSAGE_TYPE_TALLY_CHANGE_BACK = 33;
    /*** 消息类型：34-复核打回  */
    public final static Integer MESSAGE_TYPE_TALLY_CHECK_BACK = 34;
    /*** 消息类型：35-复核通过  */
    public final static Integer MESSAGE_TYPE_TALLY_CHECK_PASS = 35;
    /*** 消息类型：36-收款成功  */
    public final static Integer MESSAGE_TYPE_RECEIVABLE_SUCCEED = 36;
    /*** 消息类型：37-自动支付  */
    public final static Integer MESSAGE_TYPE_AUTO_PAY = 37;
    /*** 消息类型：38-指令结果查询  */
    public final static Integer MESSAGE_TYPE_EXC_QUERY_RESULT = 38;
    /*** 消息类型：39-退款勾兑打回  */
    public final static Integer MESSAGE_TYPE_REFUNDBLENDING_BACK = 39;
    /*** 消息类型：40-设为支付成功  */
    public final static Integer MESSAGE_TYPE_PAY_SUCCESS = 40;
    /*** 消息类型：41-设为支付失败  */
    public final static Integer MESSAGE_TYPE_PAY_FAILURE = 41;
    /*** 业务类型：pay-支付结算  */
    public final static String BUS_TYPE_PAY = "pay";
    /*** 业务类型：expense-费用报销  */
    public final static String BUS_TYPE_EXPENSE = "expense";
    /*** 业务类型：allocation-资金调拨  */
    public final static String BUS_TYPE_ALLOCATION = "allocation";
    /*** 业务类型：cgs-委托收款  */
    public final static String BUS_TYPE_CGS = "cgs";
    /*** 业务类型：bdlinkPay-银企直联付款指令  */
    public final static String BUS_TYPE_BDLINKPAY = "bdlinkPay";
    /*** 业务类型：bdlinkGather-银企直联收款款指令  */
    public final static String BUS_TYPE_BDLINKGATHER = "bdlinkGather";
    /*** 业务类型：bdlinkFinance-银企直联理财款指令  */
    public final static String BUS_TYPE_BDLINKFINANCE = "bdlinkFinance";
    /*** 业务类型：wage-代发工资  */
    public final static String BUS_TYPE_WAGE = "wage";
    /*** 业务类型：receivable-收款管理  */
    public final static String BUS_TYPE_RECEIVABLE ="receivable";
    /*** 业务类型：payPolicy-自动支付策略  */
    public final static String BUS_TYPE_PAYPOLICY = "payPolicy";
    /** * 录入保存！ */
    public static final String NIS_LOG_ADD_SAVE_SUCC = "nis.log.add.save.succ";
    /** * 编辑保存！ */
    public static final String NIS_LOG_UPDATE_SAVE_SUCC = "nis.log.update.save.succ";
    /**	 * 提交！	 */
    public static final String NIS_LOG_SUBMIT_SUCC = "nis.log.submit.succ";
    /** * 录入暂存！ */
    public static final String NIS_LOG_ADD_TEMPSAVE_SUCC = "nis.log.add.tempsave.succ";
    /** * 编辑暂存！ */
    public static final String NIS_LOG_UPDATE_TEMPSAVE_SUCC = "nis.log.update.tempsave.succ";
    /**	 * 录入提交！	 */
    public static final String NIS_LOG_ADD_SUBMIT_SUCC = "nis.log.add.submit.succ";
    /**	 * 编辑提交！	 */
    public static final String NIS_LOG_UPDATE_SUBMIT_SUCC = "nis.log.update.submit.succ";
    /**	 * 批量提交！	 */
    public static final String NIS_LOG_BATCH_SUBMIT_SUCC = "nis.log.batch.submit.succ";
    /** * 删除！ */
    public static final String NIS_LOG_DELETE_SUCC = "nis.log.delete.succ";
    /**	 * 批量删除！	 */
    public static final String NIS_LOG_BATCH_DELETE_SUCC = "nis.log.batch.delete.succ";
    /** * 撤回！ */
    public static final String NIS_LOG_WITHDRAW_SUCC = "nis.log.withdraw.succ";
    /**	 * 审批打回！	 */
    public static final String NIS_LOG_APP_BACK = "nis.log.app.back";
    /**	 * 审批通过！	 */
    public static final String NIS_LOG_APP_SUCC = "nis.log.app.succ";
    /** * 导入！ */
    public static final String NIS_LOG_IMPORT_SUCC = "nis.log.import.succ";
    /**	 * 自动支付	 */
    public static final String NIS_LOG_AUTO_PAY = "nis.log.auto.pay";
    /** 可疑指令发送 */
    public static final String NIS_LOG_EXC_CONFIRM_SEND = "nis.log.exc.confirm.send";
    /** 可疑指令作废 */
    public static final String NIS_LOG_EXC_CONFIRM_DEFEAT = "nis.log.exc.confirm.defeat";
}
