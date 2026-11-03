package com.bytter.bill.domain.billbank.service.impl;
import java.util.List;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.service.IBillDestructionRequestService;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <br>
 * <b>功能：</b>开票撤票申请业务操作service<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-6-1 <br>
 */
@Service("billDestructionRequestService")
@Transactional
public class BillDestructionRequestService implements IBillDestructionRequestService {
	@Autowired
	private BillBankDao billBankDao;
	/**
	 * <b>功能：</b>撤票申请--查询申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-1 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillDestructionByBifCode(String bifCode, String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_004,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS16);
		return voucherInfoList;
	}
	/**
	 * <b>功能：</b>撤票申请--指令发送待回应<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-1 <br>
	 * @param recordId 流水ID
	 * @param voucherId 票据ID
	 * @return
	 */
	public void updateVoucherInfo(VoucherInfoVo voucher){
		//修改流水表status:已回应待签收 18
		billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS18, null, voucher.getReturnCode(), voucher.getReturnMsg(),voucher.getBillId());
	}
	
	/**
	 * <b>功能：</b>撤票申请--根据银行接口代码及银行返回代码查询待回应数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-14 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode,String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_004,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS18);
		
		return voucherInfoList;
	}
	
	/**
	 * <b>功能：</b>撤票申请-发起方--指令发送回应成功--修改流水表状态为业务已完成及票据表状态为结束已作废<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param voucher 票据信息
	 * 
	 * @return
	 */
	public void updateVoucherInfoEnd(VoucherInfoVo voucher) {
		BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
		if(oldInfo == null){
			throw new RuntimeException("票据信息不存在！");
		}
		BeanUtils.copyNotNullProperties(oldInfo,voucher);
		String billStatus = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEWBILL_STATUS_CS05 : AccebillConstant.AB_BILL_STATUS_000002;
		String ebillBlCrclIndc = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.CIRCULATE_TF0501 : null;
		String blBsnTpcd = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC19 : null;
		//根据票据ID更新票据表信息
		//修改票据表信息bill_status:结束已作废 000002  boxStatus:业务处理完成 11
		billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus, ebillBlCrclIndc, blBsnTpcd);
		billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
		//根据流水ID更新票据流水表信息
		//修改流水表status:业务完成  19
		billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(),voucher.getBillId());
		//修改从表为无效
		billBankDao.updateVoucherinfoViceValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
		//修改流水为已结束
		billBankDao.updateAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
	}
	
	
	/**
	 * <b>功能：</b>撤票申请--指令发送回应拒收<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-19 <br>
	 * @param recordId 拒收回复流水ID
	 * @return
	 */
	public void updateAccrecordsStatusRejectionByRecordId(VoucherInfoVo voucher) {
		billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4,voucher.getReturnCode(),voucher.getReturnMsg(), voucher.getBillId());
		//修改前一笔流水为可用
	    billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(),AccebillConstant.AB_BILL_VALID_SIGN_1);
	}
	
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
	public void updateAccrecordsInfoByRecordId(String recordId, Integer status,
			String briefId, String returnCode, String returnMsg) {
		billBankDao.updateAccrecordsInfoByRecordId(recordId, status==null?null:String.valueOf(status), briefId, returnCode, returnMsg,null);
	}
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
	public void updateVoucherInfoByVoucherId(String voucherId, Integer boxStatus,
			String billStatus, Long sysSubject, String ebillBlCrclIndc, String blBsnTpcd) {
		billBankDao.updateVoucherInfoByVoucherId(voucherId, boxStatus, billStatus, ebillBlCrclIndc, blBsnTpcd);
	}
	
	/**
	 * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-6 <br>
	 * @param recordId 流水ID 
	 * @param reCode 银行返回代码
	 * @param reMsg 银行返回消息
	 */
	public void updateAccrecordsReCodeAndReMsgByRecordId(String recordId,
			String reCode, String reMsg) {
		billBankDao.updateAccrecordsReCodeAndReMsgByRecordId(recordId, reCode, reMsg);
	}
	/**
	 * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-6 <br>
	 * @param recordId 流水ID 
	 * @param reCode 银行返回代码
	 * @param reMsg 银行返回消息
	 * @param billId 银行返回消息
	 */
	public void updateAccrecordsReCodeAndReMsgByRecordId(String recordId,
			String reCode, String reMsg,String billId) {
		billBankDao.updateAccrecordsReCodeAndReMsgByRecordId(recordId, reCode, reMsg,billId);
	}
}
