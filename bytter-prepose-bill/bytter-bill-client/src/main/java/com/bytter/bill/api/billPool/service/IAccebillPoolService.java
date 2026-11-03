package com.bytter.bill.api.billPool.service;
import com.bytter.bill.api.billPool.dto.AbPoolDtlDTO;
import com.bytter.bill.api.billPool.dto.AbPoolEmitFinaApplyDTO;
import com.bytter.core.base.entity.ICurrentUser;
import java.util.Date;
/**
 * 票据池公共调用接口
 * @author zhenghj
 * @
 */
public interface IAccebillPoolService {
	
	/**
	 * 根据参数获取票据池明细信息
	 * @param id 票据池明细id
	 */
	AbPoolDtlDTO getAbPoolDtlDtoByParams(String id);
	/**
	 * 票据池背书转让-付款接口调用
	 *
	 * @param poolDtlIds 票据池明细流水id
	 * @param recName 被背书人名称
	 * @param recAcc 被背书人账号
	 * @param recBankCode 被背书人行号
	 * @param businessDate 办理日期
	 * @param userVo          操作用户
	 * @return 保存的明细流水id
	 */
	String saveAbPoolEndorseInfoByNisPay(String poolDtlIds, String recName, String recAcc, String recBankCode, Date businessDate, ICurrentUser userVo);
	/**
	 * 票据池出票融资申请-付款接口调用
	 *
	 * @param emitFinaApplyDTO 出票融资信息
	 * @param userVo          操作用户
	 */
	void saveEmitFinaApplyByNisPay(AbPoolEmitFinaApplyDTO emitFinaApplyDTO, ICurrentUser userVo);
}
