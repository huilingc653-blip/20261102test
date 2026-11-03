package com.bytter.bill.domain.billbank.service;
import com.bytter.bill.domain.billbank.vo.BillSignVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>贴现申请服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-06-07 <br>
 */
public interface IBillDiscountRequestService {
	
	/**
	 * <b>功能：</b>贴现申请-发起方--查询申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillDiscountByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>票据池贴现申请-发起方--查询申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillDiscountPoolByBifCode(String bifCode, String reCode);
	/**
	 * <b>功能：</b>贴现申请--发起方--指令发送已回应待签收<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @return
	 */
	void updateVoucherInfo(VoucherInfoVo voucher);
	
	/**
	 * <b>功能：</b>删除批次票据信息完成<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2018-09-29 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return 票据信息
	 */
    void dltBatchNoByBifCodeEnd(String recordId);
	
	/**
	 * <b>功能：</b>查询查询待删除批次票据信息<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2018-09-29 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return 票据信息
	 */
	List<VoucherInfoVo> queryDltBatchNoByBifCode(String bifCode);
	
	/**
	 * <b>功能：</b>贴现申请---根据银行接口代码及银行返回代码查询待签收数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-15 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode);
	
	
	/**
	 * <b>功能：</b>票据池贴现申请---根据银行接口代码及银行返回代码查询待签收数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-15 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	List<VoucherInfoVo> queryBillRegisterAwaitRespondPoolByBifCode(String bifCode, String reCode);
	
	/**
	 * <b>功能：</b>贴现申请-发起方--签收同意完成后修改票据表与流水表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-6 <br>
	 * @param voucher 票据信息
	 * @return
	 */
	void updateVoucherInfoEnd(VoucherInfoVo voucher);
	
	/**
	 * <b>功能：</b>贴现申请-发起方--签收方拒签后修改票据表与流水表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-6 <br>
	 * @param voucher 票据信息
	 * @return
	 */
	void updateVoucherInfoRejection(VoucherInfoVo voucher);
	
	
	
	/**
	 * <b>功能：</b>背书提示保证-根据银行接口传过来的票据信息及状态修改票据及流水信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-5 <br>
	 * @param VoucherInfoVo 票据信息
	 * @param status 状态   
	 * 1：发起方--指令发送已回应待签收--票据ID,流水ID
	 * 2：发起方--签收同意完成后修改票据表与流水表信息--票据ID,流水ID
	 * 3：发起方--签收方拒签后修改票据表与流水表信息--票据ID,流水ID
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

	void updateAccrecordsInfoByRecordIdForBaffle(String recordId, Integer status,
												 String briefId, String returnCode, String returnMsg);

	/**  *
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
	 * <b>功能：</b>新增签收表数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param billSign 签收表实体 
	 */
	void saveBillSign(BillSignVo billSign);
}
