package com.bytter.bill.domain.billbank.service;
import java.util.List;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
/**
 * <br>
 * <b>功能：</b>开票撤票申请服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-06-1 <br>
 */
public interface IBillDestructionRequestService {
	
	/**
	 * <b>功能：</b>撤票申请--查询申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-1 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillDestructionByBifCode(String bifCode, String reCode);
	/**
	 * <b>功能：</b>撤票申请--指令发送待回应--<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-1 <br>
	 * @param voucher 票据信息
	 * @return
	 */
	void updateVoucherInfo(VoucherInfoVo voucher);
	
	
	/**
	 * <b>功能：</b>撤票申请---根据银行接口代码及银行返回代码查询待回应数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-14 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode);
	
	
	/**
	 * <b>功能：</b>撤票申请--指令发送回应成功--修改流水表状态为业务已完成及票据表状态为结束已作废<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-1 <br>
	 * @param voucher 票据信息
	 * @return
	 */
	void updateVoucherInfoEnd(VoucherInfoVo voucher);
	
	
	/**
	 * <b>功能：</b>撤票申请--指令发送回应拒收<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-19 <br>
	 * @param recordId 拒收回复流水ID
	 * @return
	 */
	void updateAccrecordsStatusRejectionByRecordId(VoucherInfoVo voucher);
	
	/**
	 * <b>功能：</b>根据流水ID更新票据流水表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-5 <br>
	 * @param recordId 票据流水ID
	 * @param status 操作状态
	 * @param briefId 业务类型
	 * @param returnCode 返回代码
	 * @param returnMsg 返回消息
	 */
	void updateAccrecordsInfoByRecordId(String recordId, Integer status, String briefId, String returnCode, String returnMsg);
	/**
	 * <b>功能：</b>根据票据ID更新票据表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-5 <br>
	 * @param voucherId 票据ID
	 * @param boxStatus 处理标记
	 * @param billStatus 票据状态
	 * @param sysSubject 系统科目
	 * @param ebillBlCrclIndc 流通标志
	 * @param blBsnTpcd 业务代码
	 */
	void updateVoucherInfoByVoucherId(String voucherId, Integer boxStatus, String billStatus, Long sysSubject, String ebillBlCrclIndc, String blBsnTpcd);
	
	/**
	 * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-6 <br>
	 * @param recordId 流水ID 
	 * @param reCode 银行返回代码
	 * @param reMsg 银行返回消息
	 */
	void updateAccrecordsReCodeAndReMsgByRecordId(String recordId, String reCode, String reMsg);
	
	/**
	 * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-6 <br>
	 * @param recordId 流水ID 
	 * @param reCode 银行返回代码
	 * @param reMsg 银行返回消息
	 * @param billId 银行返回id
	 */
	void updateAccrecordsReCodeAndReMsgByRecordId(String recordId, String reCode, String reMsg, String billId);
}
