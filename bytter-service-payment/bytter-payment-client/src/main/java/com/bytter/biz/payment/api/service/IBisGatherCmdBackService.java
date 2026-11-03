package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.core.base.entity.ICurrentUser;
/**
 * 收款指令退回接口类
 * @author huangx
 * @since 2017-08-24 15:14:11
 */
public interface IBisGatherCmdBackService {
	/**
	 * 收款指令退回
	 * @param bisGatherCmd 收款指令
	 * @param user 用户
	 */
	void backBisGatherCmd(BisGatherCmdDTO bisGatherCmd, ICurrentUser user);
	/**
	 * 收款指令作废
	 * @param bisGatherCmd 收款指令
	 * @param user 用户
	 */
	void defeatBisGatherCmd(BisGatherCmdDTO bisGatherCmd, ICurrentUser user);
}
