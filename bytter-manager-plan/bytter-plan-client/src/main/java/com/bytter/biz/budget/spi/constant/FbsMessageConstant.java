package com.bytter.biz.budget.spi.constant;
/**
 * 资金计划日志常量类
 * @author huangx
 * @since 2017/6/12.
 */
public class FbsMessageConstant {
	/*** 消息类型：0-录入  */
    public final static Integer MESSAGE_TYPE_INIT = 0;
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
    /*** 消息类型：6-汇总打回  */
    public final static Integer MESSAGE_TYPE_COLLECT_BACK = 6;
    /*** 消息类型：7-汇总  */
    public final static Integer MESSAGE_TYPE_COLLECT = 7;
    /*** 消息类型：8-汇总编辑  */
    public final static Integer MESSAGE_TYPE_COLLECT_EDIT = 8;
    /*** 消息类型：9-审批编辑  */
    public final static Integer MESSAGE_TYPE_CHECK_EDIT = 9;
    /*** 消息类型：10-自动生成  */
    public final static Integer MESSAGE_TYPE_AUTO_INIT = 10;
	/*** 业务类型：input-计划上报  */
    public final static String BUS_NAME_INPUT = "input";
    /*** 业务类型：superadd-计划追加 */
    public final static String BUS_NAME_SUPERADD = "superadd";
    /*** 业务类型：process-计划执行  */
    public final static String BUS_NAME_PROCESS = "process";
	/** * 录入保存！ */
	public static final String FBS_LOG_ADD_SAVE_SUCC = "fbs.log.add.save.succ";
	/** * 编辑保存！ */
	public static final String FBS_LOG_UPDATE_SAVE_SUCC = "fbs.log.update.save.succ";
	/**	 * 提交！	 */
	public static final String FBS_LOG_SUBMIT_SUCC = "fbs.log.submit.succ";
	/** * 录入暂存！ */
	public static final String FBS_LOG_ADD_TEMPSAVE_SUCC = "fbs.log.add.tempsave.succ";
	/** * 编辑暂存！ */
	public static final String FBS_LOG_UPDATE_TEMPSAVE_SUCC = "fbs.log.update.tempsave.succ";
	/**	 * 录入提交！	 */
	public static final String FBS_LOG_ADD_SUBMIT_SUCC = "fbs.log.add.submit.succ";
	/**	 * 编辑提交！	 */
	public static final String FBS_LOG_UPDATE_SUBMIT_SUCC = "fbs.log.update.submit.succ";
	/**	 * 批量提交！	 */
	public static final String FBS_LOG_BATCH_SUBMIT_SUCC = "fbs.log.batch.submit.succ";
	/** * 删除！ */
	public static final String FBS_LOG_DELETE_SUCC = "fbs.log.delete.succ";
	/**	 * 批量删除！	 */
	public static final String FBS_LOG_BATCH_DELETE_SUCC = "fbs.log.batch.delete.succ";
	/** * 撤回！ */
	public static final String FBS_LOG_WITHDRAW_SUCC = "fbs.log.withdraw.succ";
	/**	 * 审批打回！	 */
	public static final String FBS_LOG_APP_BACK = "fbs.log.app.back";
	/**	 * 审批通过！	 */
	public static final String FBS_LOG_APP_SUCC = "fbs.log.app.succ";
	/** * 导入！ */
	public static final String FBS_LOG_IMPORT_SUCC = "fbs.log.import.succ";
	/** * 审批保存！ */
	public static final String FBS_LOG_APP_SAVE_SUCC = "fbs.log.app.save.succ";
	/** * 上级网点汇总！ */
	public static final String FBS_LOG_COLLECT_SUCC = "fbs.log.collect.succ";
	/** * 上级网点汇总打回！ */
	public static final String FBS_LOG_COLLECT_BACK = "fbs.log.collect.back";
	/** * 上级网点汇总删除！ */
	public static final String FBS_LOG_COLLECT_DELETE = "fbs.log.collect.delete";
	/** * 上级网点汇总编辑！ */
	public static final String FBS_LOG_COLLECT_EDIT = "fbs.log.collect.edit";
	/** * 上级网点汇总审批完成！ */
	public static final String FBS_LOG_COLLECT_APP_SUCC = "fbs.log.collect.app.succ";
	/** * 汇总保存！ */
	public static final String FBS_LOG_COLLECT_SAVE_SUCC = "fbs.log.collect.save.succ";
	/** * 初始化计划！ */
	public static final String FBS_LOG_INIT_SUCC = "fbs.log.init.succ";
	/** * 上级网点汇总审批完成后初始化！ */
	public static final String FBS_LOG_COLLECT_APP_INIT_SUCC = "fbs.log.collect.init.succ";
}
