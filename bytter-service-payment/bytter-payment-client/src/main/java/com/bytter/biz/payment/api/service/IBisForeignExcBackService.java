package com.bytter.biz.payment.api.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
/**
 * 汇款指令退回接口类
 * @author huangx
 * @since 2017-08-24 15:14:11
 */
public interface IBisForeignExcBackService {
	/**
	 * 汇款指令退回
	 * @param bisForeignExc 外汇管理DTO
	 * @param user 操作用户
	 */
	void backBisForeignExc(BisForeignExcDTO bisForeignExc, ICurrentUser user);
	
	/**
	 * 汇款指令作废
	 * @param bisForeignExc 外汇管理DTO
	 * @param user 操作用户
	 */
	void defeatBisForeignExc(BisForeignExcDTO bisForeignExc, ICurrentUser user);
}
