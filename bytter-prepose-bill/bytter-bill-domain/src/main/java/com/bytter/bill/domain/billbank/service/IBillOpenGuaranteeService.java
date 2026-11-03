package com.bytter.bill.domain.billbank.service;
import java.util.List;
import com.bytter.bill.domain.billbank.vo.*;
/**
 * <br>
 * <b>功能：</b>开票提示保证服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-06-2 <br>
 */
public interface IBillOpenGuaranteeService {
	
	/**
	 * <b>功能：</b>提示保证--查询申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-2 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillOpenGuaranteeByBifCode(String bifCode, String reCode);
	/**
	 * <b>功能：</b>提示保证--发起方--指令发送已回应待签收--根据流水ID修改提示保证流水表状态为已回应待签收<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-2 <br>
	 * @param recordId 流水ID	
	 * @return
	 */
	void updateAccrecordsStatusByRecordId(VoucherInfoVo voucher);
	
	
	/**
	 * <b>功能：</b>提示保证-根据银行接口代码及银行返回代码查询待签收数据r>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-14 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>提示保证--发起方--同意签收完成后--根据流水ID修改提示保证流水表状态为业务已完成<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-2 <br>
	 * @param recordId 流水ID	
	 * @return
	 */
	void updateAccrecordsStatusEndByRecordId(VoucherInfoVo voucher);
	
	/**
	 * <b>功能：</b>提示保证-发起方--签收方拒签后修改流水表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-2 <br>
	 * @param recordId 流水ID	
	 * @return
	 */
	void  updateAccrecordsStatusRejection(VoucherInfoVo voucher);
	
	/**
	 * <b>功能：</b>提示保证-签收方--查询待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-2 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillOpenGuaranteeReplyByBifCode(String bifCode, String reCode);
	
	
	/**
	 * <b>功能：</b>提示保证-签收方签收--根据签收回复流水ID修改流水表状态为已签收<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-2 <br>
	 * @param recordId 签收回复流水ID
	 * @return
	 */
	void updateAccrecordsStatusReplyByRecordId(VoucherInfoVo voucher);
	
	
	/**
	 * <b>功能：</b>提示保证-签收方拒收--根据拒收回复流水ID修改流水表状态为已拒收状态<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-2 <br>
	 * @param recordId 拒收回复流水ID
	 * @return
	 */
	void updateAccrecordsStatusRejectionByRecordId(VoucherInfoVo voucher);
	
	
	/**
	 * <b>功能：</b>提示保证-根据银行接口传过来的票据信息及状态修改票据及流水信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-5 <br>
	 * @param VoucherInfoVo 票据信息
	 * @param status 状态   
	 * 1：发起方--指令发送已回应待签收--流水ID
	 * 2：发起方--签收同意完成后修改票据表与流水表信息--流水ID
	 * 3：发起方--签收方拒签后修改票据表与流水表信息--流水ID
	 * 4：签收方--签收方签收--签收回复流水ID
	 * 5：签收方--签收方拒收--拒收回复流水ID
	 */
	void upateVoucherAndAccrecordsByVoucherInfoAndStatus(VoucherInfoVo voucher, int status);
	
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
	 *
	 * @param voucherId  票据ID
	 * @param boxStatus  处理标记
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
	
	/**
	 * <b>功能：</b>新增签收表数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param billSign 签收表实体 
	 */
	void saveBillSign(BillSignVo billSign);
	
	/**
	 * <b>功能：</b>查询银行账号信息视图<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据信息
	 */
	List<VAccListVo> queryVAccListVo(String bifCode);
}
