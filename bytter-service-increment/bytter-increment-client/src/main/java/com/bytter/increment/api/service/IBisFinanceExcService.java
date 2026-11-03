package com.bytter.increment.api.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.increment.api.dto.BisFinanceExcDTO;

/**
 * 理财指令接口类
 * @author huangx
 * @since 2017-08-23 11:38:15
 */
public interface IBisFinanceExcService {
	/**
	 * 新增理财指令
	 * @param bisFinanceExc 理财指令
	 * @param user 用户
	 */
	void addBisFinanceExc(BisFinanceExcDTO bisFinanceExc, ICurrentUser user);

	BisFinanceExcDTO getByVoucherNo(String voucherNo);

	void updateBisFinanceExc(BisFinanceExcDTO bisFinanceExc);

}
