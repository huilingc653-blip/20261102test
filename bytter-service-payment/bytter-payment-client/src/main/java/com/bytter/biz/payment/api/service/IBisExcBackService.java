package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.core.base.entity.ICurrentUser;
/**
 * 付款指令退回接口类
 * @author huangx
 * @since 2017-08-24 15:14:11
 */
public interface IBisExcBackService {
	/**
	 * 付款指令退回
	 * @param bisExc 付款指令DTO
	 * @param user 用户
	 */
	void backBisExc(BisExcDTO bisExc, ICurrentUser user);
	/**
	 * 付款指令作废
	 * @param bisExc 付款指令DTO
	 * @param user 用户
	 */
	void defeatBisExc(BisExcDTO bisExc, ICurrentUser user);
	/**
	 * 付款指令发送 后记账
	 * @param bisExc 付款指令DTO
	 * @param user 用户
	 */
	void sendBisExcAccounting(BisExcDTO bisExc, ICurrentUser user);
	
	/**
	 * 前置校验付款指令发送 后记账
	 * @param bisExc 付款指令DTO
	 * @param user 用户
	 */
	void checkSendBisExcAccounting(BisExcDTO bisExc, ICurrentUser user);
	/**
	 * 付款指令重发
	 * @param bisExc 付款指令DTO
	 * @param user 用户
	 */
	void resendBisExc(BisExcDTO bisExc, ICurrentUser user);
	/**
	 * 付款指令支付状态更新
	 * @param bisExc 付款指令DTO
	 * @param user 用户
	 */
	void setPayResultBisExc(BisExcDTO bisExc, ICurrentUser user);
	/**
	 * 付款指令查询结果
	 * @param bisExc 付款指令DTO
	 * @param user 用户
	 */
	void queryResultBisExc(BisExcDTO bisExc, ICurrentUser user);
}
