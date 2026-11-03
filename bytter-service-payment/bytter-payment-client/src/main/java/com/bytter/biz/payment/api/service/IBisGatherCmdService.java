package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.core.base.entity.ICurrentUser;
import java.util.List;
import java.util.Map;
/**
 * 收款指令接口类
 * @author huangx
 * @since 2017-6-30 09:49:44
 */
public interface IBisGatherCmdService {
	/**
	 * 新增收款指令
	 *
	 * @param bisGatherCmd 收款指令
	 * @param user 用户
	 */
	void addBisGatherCmd(BisGatherCmdDTO bisGatherCmd, ICurrentUser user);
	/**
	 * 收款指令发送
	 *
	 * @param bisGatherCmd 收款指令
	 * @param user 用户
	 */
	void autoSendBisGatherCmd(BisGatherCmdDTO bisGatherCmd, ICurrentUser user);
	/**
	 * 根据业务单号ID集合和指令来源，获取指令Map
	 * @param billIds
	 * @param voucherFrom
	 * @return
	 */
	Map<String, BisGatherCmdDTO> getBisGatherCmdMapByCbsBillIds(List<String> billIds, String voucherFrom);
	/**
	 * 根据业务单号集合和指令来源，获取指令Map
	 * @param billNos
	 * @param voucherFrom
	 * @return
	 */
	Map<String, BisGatherCmdDTO> getBisGatherCmdMapByCbsBillNos(List<String> billNos, String voucherFrom);

	BisGatherCmdDTO getByVoucherNo(String voucherNo);

}
