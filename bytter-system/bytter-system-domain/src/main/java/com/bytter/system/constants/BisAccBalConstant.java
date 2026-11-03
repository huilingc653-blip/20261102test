package com.bytter.system.constants;
/**
 * 银行账户流水、余额常量类
 * @author zuoy
 * @since 2017-10-31
 */
public class BisAccBalConstant {
	/**
	 * 单据状态-暂存
	 */
	public final static Integer STATUE_TEMP = 0;
	/**
	 * 单据状态-打回
	 */
	public final static Integer STATUE_BACK = -1;
	/**
	 * 单据状态-删除
	 */
	public final static Integer STATUE_DEL = -2;
	/**
	 * 单据状态-待审批
	 */
	public final static Integer STATUE_WAIT_CHECK = 11;
	/**
	 * 单据状态-审批通过
	 */
	public final static Integer STATUE_CHECK = 95;
	/**
	 * 单据状态-办理完成
	 */
	public final static Integer STATUE_CANCELLATION = 100;
	/**
	 * 截止交易日期
	 */
	public final static String IS_COMPLETE_YES = "1";
	/**
	 * 截止交易日期
	 */
	public final static String IS_COMPLETE_NO = "0";
	/**
	 * 借贷标志 0-收入
	 */
	public final static String CDSIGN_IN = "0";
	/**
	 * 借贷标志 1-支出
	 */
	public final static String CDSIGN_OUT = "1";
	/**
	 * 红蓝标志 1-红
	 */
	public final static String RBSIGN_RED = "1";
	/**
	 * 红蓝标志 2-蓝
	 */
	public final static String RBSIGN_BLUE = "2";
	/**
	 * 挂接计划标志 0-未确认
	 */
	public final static String FBS_STATUS_N = "0";
	/**
	 * 挂接计划标志 1-已确认
	 */
	public final static String FBS_STATUS_Y = "1";
	/**
	 * 挂接计划标志 2-已删除
	 */
	public final static String FBS_STATUS_D = "2";
	/**
	 * 挂接计划标志 3-已匹配
	 */
	public final static String FBS_STATUS_M = "3";
	/**
	 * 操作 - 确认
	 */
	public final static String TYPE_Y= "Y";
	/**
	 * 操作 - 删除
	 */
	public final static String TYPE_D = "D";
	/**
	 * 操作 - 恢复
	 */
	public final static String TYPE_H = "H";
	/**
	 * 操作 - 还原
	 */
	public final static String TYPE_R= "R";
}
