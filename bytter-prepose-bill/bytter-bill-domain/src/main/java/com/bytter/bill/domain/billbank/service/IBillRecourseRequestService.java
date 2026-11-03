package com.bytter.bill.domain.billbank.service;
import java.util.List;
import com.bytter.bill.domain.billbank.vo.*;
/**
 * <br>
 * <b>功能：</b>追索通知服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-06-7 <br>
 */
public interface IBillRecourseRequestService {
	
	/**
	 * <b>功能：</b>追索通知-发起方--查询申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRecourseRequestByBifCode(String bifCode, String reCode);
	/**
	 * <b>功能：</b>追索通知--发起方--指令发送已发送待回应<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param recordId 流水ID
	 * @param voucherId 票据ID
	 * @return
	 */
	void updateVoucherInfo(VoucherInfoVo voucher);
	
	
	/**
	 * <b>功能：</b>追索通知--根据银行接口代码及银行返回代码查询待签收数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-15 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>追索通知-发起方--指令发送已回应成功<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param voucher 票据信息
	 * @return
	 */
	void updateVoucherInfoEnd(VoucherInfoVo voucher);
	
	/**
	 * <b>功能：</b>追索通知--指令发送回应拒收<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param recordId 拒收回复流水ID
	 * @return
	 */
	void updateAccrecordsStatusRejectionByRecordId(VoucherInfoVo voucher);
	
	/**
	 * <b>功能：</b>追索通知-签收方--查询待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-5-3 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
    List<VoucherInfoVo> queryBillRegisterAwaitReplyByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>追索通知-签收方--查询已回应待签收的票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-5-3 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
    List<VoucherInfoVo> queryRegisterAwaitReplyRegisterAwaitByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>追索通知-签收方签收--根据签收回复流水ID修改流水表状态为已签收<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-5-3 <br>
	 * @param recordId 签收回复流水ID
	 * @return
	 */
    void updateAccrecordsStatusByRecordId(VoucherInfoVo voucher);
	/**
	 * <b>功能：</b>追索通知-签收方拒收--根据拒收回复流水ID修改流水表状态为已拒收状态<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-5-3 <br>
	 * @param recordId 拒收回复流水ID
	 * @return
	 */
    void updateAccrecordsRejectionByRecordId(VoucherInfoVo voucher);
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
	 * <b>功能：</b>查询银行账号信息视图<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据信息
	 */
	List<VAccListVo> queryVAccListVo(String bifCode);
	/**
	 * <b>功能：</b>保存追索通知签收流水<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-04-25 <br>
	 * @param  voucher 票据主信息(票据表信息，流水表信息)
	 */
	void saveRecourseAccrecords(VoucherInfoVo voucher);
	
	/**
	 * <b>功能：</b>新增签收表数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param billSign 签收表实体 
	 */
	void saveBillSign(BillSignVo billSign);
}
