/**
 * 
 */
package com.bytter.biz.fundtransfer.enums;

/**
 * 指令状态
 * 
 * @author zhangjf
 * 
 */
public enum VoucherStatEnum {
	/** 银行已处理成功 **/
	TRADED_SUCCEED("0", "交易成功"),

	/** 已提取数据，尚未发送银行 **/
	WAIT_RESULT("1", "等待结果"),

	/** 银行已收到，但处理失败 **/
	TRADED_FAILED("2", "银行处理失败"),

	/** 准备发送 **/
	READY_SENDING("3", "准备发送"),

	/** 已发送银行，等待结果 **/
	BANK_ACCEPTED("4", "银行已接受"),

	/** 查询转账结果，记录不存在 **/
	LACKOF_DATA("5", "收付款数据不全"),

	/** 必要数据缺少 **/
	WAIT_FILL_DATA("6", "等待补充数据"),

	/** 指令无效 **/
	INVALID_CMD("7", "指令无效"),

	/** 余额不足 **/
	INSUFFICIENT_BALANCE("8", "余额不足"),

	/** 发送银行前处理失败 **/
	BI_HANDLE_FAILED("9", "接口处理失败"),

	/** 指令已提取等待发送队列 **/
	FETCHED_CMD("F", "指令已提取"),

	/** 发送队列成功 **/
	SENDED_QUEUE_SUCCEED("S", "指令发送成功"),

	/** 发送队列失败 **/
	SENDED_QUEUE_FAILED("E", "指令发送失败"),

	/** 队列已消费 **/
	FETCHED_QUEUE("W", "指令已消费"),
	
	/** 数据被篡改 **/
	DATAALTERED_QUEUE("-1", "数据被篡改"),
	
	/** 交易总金额与明细金额不一致 **/
	/* DATA_INCONSISTENT("I","交易总金额与明细金额不一致"), */

	/** 可疑交易 **/
	DUBIOUS_QUEUE("T", "可疑交易");

	public final String value;
	public final String description;

	VoucherStatEnum(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public static VoucherStatEnum toEnum(String value) {
		if (value == null) {
			return null;
		}
		for (VoucherStatEnum se : VoucherStatEnum.values()) {
			if (value.equals(se.value)) {
				return se;
			}
		}
		return null;
	}
}
