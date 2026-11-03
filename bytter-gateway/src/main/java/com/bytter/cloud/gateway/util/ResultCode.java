package com.bytter.cloud.gateway.util;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回码实现
 *
 * @author pangu
 */

@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {

	/**
	 * 操作成功
	 */
	SUCCESS(200, "操作成功"),
	/**
	 * 业务异常
	 */
	FAILURE(400, "业务异常");
	

	/**
	 * 状态码
	 */
	final int code;
	/**
	 * 消息内容
	 */
	final String msg;
}
