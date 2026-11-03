package com.bytter.core.constant;
/**
 * 外汇管理日志常量类
 * @author huangx
 * @since 2019/7/17
 */
public class FexMessageConstant {
	
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
    /*** 业务类型：foreign-外币汇款  */
    public final static String BUS_TYPE_FOREIGN = "foreign";
    /*** 业务类型：exc-外币汇款指令  */
    public final static String BUS_TYPE_EXC = "exc";
	/*** 业务类型：fex_cotp-交易对手  */
	public final static String BUS_TYPE_FEX_COTP = "fex_cotp";
	/*** 业务类型：fex_cotp_bankacc-交易对手银行账号  */
	public final static String BUS_TYPE_FEX_COTP_BANKACC = "fex_cotp_bankacc";
	/*** 业务类型：fex_corp_bankacc_0-主体单位默认银行账户  */
	public final static String BUS_TYPE_FEX_CORP_BANKACC_0 = "fex_corp_bankacc_0";
	/*** 业务类型：fex_corp_bankacc_1-交易对手默认银行账户  */
	public final static String BUS_TYPE_FEX_CORP_BANKACC_1 = "fex_corp_bankacc_1";
	/*** 业务类型：fex_cotp-交易需求  */
	public final static String BUS_TYPE_FEX_TRAN_APP = "fex_cotp_tran_app";
	/*** 业务类型：fex_tran_req-交易管理  */
	public final static String BUS_TYPE_FEX_TRAN_REQ = "fex_tran_req";
	/*** 业务类型：fex_cash_flow-现金流  */
	public final static String BUS_TYPE_FEX_CASH_FLOW = "fex_cash_flow";
	/*** 业务类型：fex_tran_deal-交易处理  */
	public final static String BUS_TYPE_FEX_TRAN_DEAL = "fex_tran_deal";
	/*** 业务类型：fex_tran_option_deal-外汇期权交易处理  */
	public final static String BUS_TYPE_FEX_TRAN_OPTION_DEAL = "fex_tran_option_deal";
	/*** 业务类型：fex_exposure-外汇敞口  */
	public final static String BUS_TYPE_EXPOSURE = "fex_exposure";
	/** * 录入保存！ */
	public static final String FEX_LOG_ADD_SAVE_SUCC = "nis.log.add.save.succ";
	/** * 编辑保存！ */
	public static final String FEX_LOG_UPDATE_SAVE_SUCC = "nis.log.update.save.succ";
	/**	 * 提交！	 */
	public static final String FEX_LOG_SUBMIT_SUCC = "nis.log.submit.succ";
	/** * 录入暂存！ */
	public static final String FEX_LOG_ADD_TEMPSAVE_SUCC = "nis.log.add.tempsave.succ";
	/** * 编辑暂存！ */
	public static final String FEX_LOG_UPDATE_TEMPSAVE_SUCC = "nis.log.update.tempsave.succ";
	/**	 * 录入提交！	 */
	public static final String FEX_LOG_ADD_SUBMIT_SUCC = "nis.log.add.submit.succ";
	/**	 * 编辑提交！	 */
	public static final String FEX_LOG_UPDATE_SUBMIT_SUCC = "nis.log.update.submit.succ";
	/**	 * 批量提交！	 */
	public static final String FEX_LOG_BATCH_SUBMIT_SUCC = "nis.log.batch.submit.succ";
	/** * 删除！ */
	public static final String FEX_LOG_DELETE_SUCC = "nis.log.delete.succ";
	/**	 * 批量删除！	 */
	public static final String FEX_LOG_BATCH_DELETE_SUCC = "nis.log.batch.delete.succ";
	/** * 撤回！ */
	public static final String FEX_LOG_WITHDRAW_SUCC = "nis.log.withdraw.succ";
	/**	 * 审批打回！	 */
	public static final String FEX_LOG_APP_BACK = "nis.log.app.back";
	/**	 * 审批通过！	 */
	public static final String FEX_LOG_APP_SUCC = "nis.log.app.succ";
	/** * 导入！ */
	public static final String FEX_LOG_IMPORT_SUCC = "nis.log.import.succ";
}
