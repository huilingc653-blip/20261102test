package com.bytter.credential.api.constant;
import java.io.Serializable;
/**
 * <br>
 * <b>类功能：</b>信贷操作信息类型<br>
 */
public class LcMsgConstant implements Serializable {
	/**入库，审批提交*/
	public final static int MSGA_SUBMIT = 20;
	/**登记提交*/
	public final static int MSGA_INPUT = 21;
	/**修改暂存*/
	public final static int MSGA_MODIFY  = 22;
	/**删除*/
	public final static int MSGA_DELETE = 23;
	/**审批通过*/
	public final static int MSGA_CHECKOK = 24;
	/**审批不通过,打回*/
	public final static int MSGA_ROLLBACK=25;
	/**数据补录*/
	public final static int MSGA_REPAIR=26;
	/**数据补录打回*/
	public final static int MSGA_REPAIRBACK = 31;
	/**修改提交*/
	public final static int MSGA_REPAIR_MODIFY=32;
}
