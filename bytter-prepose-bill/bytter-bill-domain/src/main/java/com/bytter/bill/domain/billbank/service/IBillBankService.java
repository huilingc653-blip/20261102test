package com.bytter.bill.domain.billbank.service;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>开票申请服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-05-31 <br>
 */
public interface IBillBankService {
	
	/**
	 * <b>功能：</b>开票申请-根据银行接口代码查询开票申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-5-31 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterByBifCode(String bifCode, String reCode);
	/**
	 * <b>功能：</b>批次开票申请-根据银行接口代码查询开票申请待发送指令票据信息<br>
	 * <b>作者：</b>zhengg<br>
	 * <b>日期：</b> 2020-11-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterBatchByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>票据池开票申请-根据银行接口代码查询开票申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-5-31 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterPoolByBifCode(String bifCode, String reCode);
	/**
	 * <b>功能：</b>开票申请-根据流水ID修改开票申请流水表状态为已回应待签收<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-1 <br>
	 * @param recordId 流水ID
	 * @return
	 */
	void updateAccrecordsStatusByRecordId(String recordId);
	
	/**
	 * <b>功能：</b>开票申请-根据银行接口代码查询开票申请待回应数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-14 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode);
	/**
	 * <b>功能：</b>批量开票申请-根据银行接口代码查询开票申请待回应数据<br>
	 * <b>作者：</b>zhengg<br>
	 * <b>日期：</b> 2020-11-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterAwaitRespondBatchByBifCode(String bifCode, String reCode);
	
	
	/**
	 * <b>功能：</b>票据池开票申请-根据银行接口代码查询开票申请待回应数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-14 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterAwaitRespondPoolByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>开票申请-回应完成后修改票据表与流水表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-1 <br>
	 * @param voucher 票据信息
	 * @return
	 */
	void updateVoucherInfo(VoucherInfoVo voucher);
	
	
	
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
	 * @param ebillBlCrclIndc 票据流通标志
	 * @param blBsnTpcd 票据业务类型代码
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
	 * <b>功能：</b>根据流水ID更新银行返回代码、消息、流水状态为18、billId赋值<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2020-09-22 <br>
	 * @param recordId 流水ID
	 * @param reCode 银行返回代码
	 * @param reMsg 银行返回消息
	 * @param billId 银行返回id
	 */
	void updateAccReCodeAndReMsgAndBillByRecordId(String recordId, String reCode, String reMsg, String billId);
	/**
	 * <b>功能：</b>自动签收后处理来自外部的票据--开票申请业务完成<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2019-06-11 <br>
	 * @param  voucher 票据主信息
	 * @param  briefId 业务类型
	 */
	void autoSignManage(VoucherInfoVo voucher);
	
	/**
	 * <b>功能：</b>自动生成生成提示承兑待发送（需经办）业务流水<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2022-6-21 <br>
	 * @param voucher 票据信息（主表id:VoucherId，流水id:RecordId）
	 * @return
	 */
	void autoBillAcceptAccrecords(VoucherInfoVo voucher);
}
