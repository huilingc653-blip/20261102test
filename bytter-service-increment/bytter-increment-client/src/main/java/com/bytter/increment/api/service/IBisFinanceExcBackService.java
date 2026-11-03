package com.bytter.increment.api.service;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.increment.api.dto.BisFinanceExcDTO;

/**
 * 理财指令退回接口类
 * @author huangx
 * @since 2017-08-24 15:14:11
 */
public interface IBisFinanceExcBackService {
	/**
	 * 理财指令退回
	 * @param bisFinanceExc 理财指令
	 * @param user 用户
	 */
	void backBisFinanceExc(BisFinanceExcDTO bisFinanceExc, ICurrentUser user);
	/**
	 * 理财指令作废
	 * @param bisFinanceExc 理财指令
	 * @param user 用户
	 */
	void defeatBisFinanceExc(BisFinanceExcDTO bisFinanceExc, ICurrentUser user);
}
