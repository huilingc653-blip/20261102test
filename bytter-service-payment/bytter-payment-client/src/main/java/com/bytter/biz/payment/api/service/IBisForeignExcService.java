package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.core.base.entity.ICurrentUser;

import java.util.List;

/**
 * 汇款指令接口类
 * @author huangx
 * @since 2017-6-30 09:49:44
 */
public interface IBisForeignExcService {
	/**
	 * 新增汇款指令
	 * @param bisForeignExc 外汇管理DTO
	 * @param user 操作用户
	 */
	void addBisForeignExc(BisForeignExcDTO bisForeignExc, ICurrentUser user);

	/**
	 * 根据指令单号获取指令信息
	 * @param voucherNo 指令单号
	 * @return 外汇管理DTO
	 */
	BisForeignExcDTO getBisForeignExcByVoucherNo(String voucherNo);

	/**
	 * 汇款指令发送
	 * @param bisForeignExc 外汇管理DTO
	 * @param user 操作用户
	 */
	void autoSendBisForeignExc(BisForeignExcDTO bisForeignExc, ICurrentUser user);


	/**
	 * 根据业务ID获取指令信息
	 * @param cbsBillId 业务ID
	 * @return 外汇管理DTO
	 */
	BisForeignExcDTO getByCbsBillId(String cbsBillId);

	/**
	 * 生成加密信息
	 * @param bisForeignExc
	 * @return
	 */
	String setVerifyCode(BisForeignExcDTO bisForeignExc);

	/**
	 * 生成签名
	 * @param bisForeignExc
	 * @return
	 */
	String getSignatureCode(BisForeignExcDTO bisForeignExc);

	List<String> listFexTranReqIdsByStatus(String voucherStat, List<String> tranBillIdList);

	BisForeignExcDTO getByVoucherNo(String voucherNo);

	/**
	 * 指令退回。
	 * @param id
	 * @param user
	 */
	void backBisForeignExcForCashFlow(String id, ICurrentUser user);

}
