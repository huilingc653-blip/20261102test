package com.bytter.bill.domain.billbank.service.impl;
import java.util.Date;
import java.util.List;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.pool.service.BillPoolManageImpService;
import com.bytter.bill.domain.billbank.service.BillCommonBaseService;
import com.bytter.bill.domain.billbank.service.IBillDiscountRequestService;
import com.bytter.bill.domain.billbank.service.IEDraftExcBillService;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.bill.domain.billbank.vo.BillSignVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <br>
 * <b>功能：</b>贴现申请业务操作service<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-6-7 <br>
 */
@Service("billDiscountRequestService")
@Transactional
public class BillDiscountRequestService implements IBillDiscountRequestService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BillBankDao billBankDao;
	@Autowired
	private BillSignDao billSignDao;
	@Autowired
	private IEDraftExcBillService eDraftExcBillService;
	@Autowired
	private BillPoolManageImpService billPoolManageImpService;
	@Autowired
	private BillCommonBaseService billCommonBaseService;

	@Autowired
	private BillAccrecordsDao billAccrecordsDao;
	
	/**
	 * <b>功能：</b>贴现申请-发起方--查询申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillDiscountByBifCode(String bifCode, String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_011,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS16,AccebillConstant.poolSign_2);
		//新票参数处理
		voucherInfoList = NewDraftDealMethod.dealNewDraftArg(voucherInfoList,billBankDao);
		if (logger.isDebugEnabled()) {
			logger.debug("voucherInfoList="+voucherInfoList.toString());
		}
		return voucherInfoList;
	}
	/**
	 * <b>功能：</b>票据池贴现申请-发起方--查询申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillDiscountPoolByBifCode(String bifCode,String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_011,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS16,AccebillConstant.poolSign_1);
		//新票参数处理
		voucherInfoList = NewDraftDealMethod.dealNewDraftArg(voucherInfoList,billBankDao);
		return voucherInfoList;
	}
	/**
	 * <b>功能：</b>贴现申请--发起方--指令发送已回应待签收--流水表状态为已回应待签收<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param voucher
	 * @return
	 */
	public void updateVoucherInfo(VoucherInfoVo voucher){
		//根据流水ID更新票据流水表信息
		//修改流水表status:已回应待签收 17
		billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS17, null, voucher.getReturnCode(), voucher.getReturnMsg(),voucher.getBillId());
		//修改票据表bill_status为 买断式贴现待签收 110101  或者 回购式贴现待签收 110201
		billBankDao.updateVoucherInfoBillStatus(voucher.getVoucherId(), voucher.getRecordId(), 1, voucher.getNewStmIssuofpprInd(),voucher.getEbillBlCrclIndc());
		//针对贴现业务进行票据池处理
		billPoolManageImpService.accebillBusPoolOp(voucher.getRecordId());

//        发送申请不这种已拆票处理需要查询结果设置
		//新票据拆分请求确认处理
//		NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId());
	}
	
	
	/**
	 * <b>功能：</b>贴现申请--根据银行接口代码及银行返回代码查询待签收数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-15 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode,String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_011,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS18,AccebillConstant.poolSign_2);
		//新票参数处理
		voucherInfoList = NewDraftDealMethod.dealNewDraftArg(voucherInfoList,billBankDao);
		
		return voucherInfoList;
	}
	
	/**
	 * <b>功能：</b>票据池贴现申请--根据银行接口代码及银行返回代码查询待签收数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-15 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRegisterAwaitRespondPoolByBifCode(String bifCode,String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_011,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS18,AccebillConstant.poolSign_1);
		//新票参数处理
		voucherInfoList = NewDraftDealMethod.dealNewDraftArg(voucherInfoList,billBankDao);
		return voucherInfoList;
	}
	/**
	 * <b>功能：</b>贴现申请-发起方--签收同意完成后修改票据表与流水表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param voucher 票据信息
	 * @return
	 */
	public void updateVoucherInfoEnd(VoucherInfoVo voucher) {
		//根据票据ID更新票据表信息
		//修改票据表信息boxStatus：业务处理完成（11）
		billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, voucher.getBillStatus(),voucher.getEbillBlCrclIndc(),voucher.getBlBsnTpcd());
		billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
		//根据流水ID更新票据流水表信息
		//修改流水表status:签收成功 19
		billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(),voucher.getBillId());
		if(voucher.getBillStatus()==null){
			///修改票据表bill_status为 买断式贴现已签收 110106 回购式贴现已签收 110206
			billBankDao.updateVoucherInfoBillStatus(voucher.getVoucherId(), voucher.getRecordId(), 2,voucher.getNewStmIssuofpprInd(),voucher.getEbillBlCrclIndc());
		}
		//修改从表为无效
		//billBankDao.updateVoucherinfoViceValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
		eDraftExcBillService.saveEDraftExcBill(voucher.getRecordId(), EDraftConstant.MSG_TYPE_BACKINFO);
		
		//针对贴现申请业务完成进行票据池处理
		billPoolManageImpService.discountPoolOp(voucher.getRecordId());
		//新票据拆分请求确认处理
		NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId());
	}
	
	/**
	 * <b>功能：</b>贴现申请-发起方--签收方拒签后修改票据表与流水表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param voucher 票据信息
	 * @return
	 */
	public void updateVoucherInfoRejection(VoucherInfoVo voucher) {
		//根据票据ID更新票据表信息
		//修改票据表信息boxStatus：业务处理完成（11）
		//billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, null);
		billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
		//根据流水ID更新票据流水表信息  修改流水表status:拒收 -4,判断returnCode是否为撤回，是则为撤回
		String optStatus = AccebillConstant.AB_BILL_OPT_STATUS_4;
		if(AccebillConstant.AB_BILL_OPT_STATUS_3.equals(voucher.getReturnCode())){
			optStatus = AccebillConstant.AB_BILL_OPT_STATUS_3;
		}
		billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), optStatus, null, voucher.getReturnCode(), voucher.getReturnMsg(),voucher.getBillId());
		//修改前一笔流水为可用
	    billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(),AccebillConstant.AB_BILL_VALID_SIGN_1);
		//把本条业务流水的LASTBILLSTATUS的值赋值到票据表的bill_status
		billBankDao.updateVoucherInfoBillStatus(voucher.getRecordId(), voucher.getVoucherId());
		//针对贴现申请业务完成进行票据池处理
		billPoolManageImpService.discountPoolOp(voucher.getRecordId());
		//新票据拆分请求确认处理
		NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId());
	}
	/**
	 * <b>功能：</b>贴现申请-根据银行接口传过来的票据信息及状态修改票据及流水信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param voucher 票据信息
	 * @param status 状态   
	 * 1：发起方--指令发送已回应待签收--票据ID,流水ID
	 * 2：发起方--签收同意完成后修改票据表与流水表信息--票据ID,流水ID
	 * 3：发起方--签收方拒签后修改票据表与流水表信息--票据ID,流水ID
	 */
	public void upateVoucherAndAccrecordsByVoucherInfoAndStatus(
			VoucherInfoVo voucher, int status) {
		switch (status) {
			//发起方--指令发送已回应待签收
			case 1:
				updateVoucherInfo(voucher);
			//发起方--签收同意完成后修改票据表与流水表信息
			case 2:
				updateVoucherInfoEnd(voucher);
			//发起方--签收方拒签后修改票据表与流水表信息
			case 3:
				updateVoucherInfoRejection(voucher);
			default:
				break;
		}
		
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
		updateAccrecordsInfoByRecordIdFail(recordId,status,briefId,returnCode,returnMsg);

		//若是线上可拆分新票，发送查询正面信息指令，并设置流水表指令id字段exc_id，用于查询银行是否存在此区间票据，若存在，说明拆分成功，若不存在说明没拆分成功
		//NewDraftDealMethod.newDraftSendFailConfirmExc(billBankDao,recordId,eDraftExcBillService);
	}

	/**  *
	 * <b>功能：</b>根据流水ID更新票据流水表信息<br>
	 * @param recordId 票据流水ID
	 * @param status 操作状态
	 * @param briefId 业务类型
	 * @param returnCode 返回代码
	 * @param returnMsg 返回消息
	 */
	public void updateAccrecordsInfoByRecordIdForBaffle(String recordId, Integer status,
														String briefId, String returnCode, String returnMsg) {
		updateAccrecordsInfoByRecordIdFail(recordId,status,briefId,returnCode,returnMsg);
		BillAbAccrecords accrecords = billAccrecordsDao.get(recordId,true);
		accrecords.setUpdateDate(new Date());
		accrecords.setExcId(null);
		billAccrecordsDao.saveOrUpdate(accrecords);
	}

	private void updateAccrecordsInfoByRecordIdFail(String recordId, Integer status,
													String briefId, String returnCode, String returnMsg) {
		billBankDao.updateAccrecordsInfoByRecordId(recordId, status==null?null:String.valueOf(status), briefId, returnCode, returnMsg,null);
		if(AccebillConstant.AB_BILL_OPT_STATUS_1.equals(String.valueOf(status))){
			//针对贴现申请业务失败进行票据池处理
			billPoolManageImpService.discountPoolOp(recordId);
		}

		if(!(AccebillConstant.AB_BILL_OPT_STATUS15.equals(String.valueOf(status))
				|| AccebillConstant.AB_BILL_OPT_STATUS16.equals(String.valueOf(status))
				|| AccebillConstant.AB_BILL_OPT_STATUS17.equals(String.valueOf(status))
				|| AccebillConstant.AB_BILL_OPT_STATUS_1.equals(String.valueOf(status))
				|| AccebillConstant.AB_BILL_OPT_STATUS_2.equals(String.valueOf(status)))){
			//新票据拆分请求确认处理
			NewDraftDealMethod.newDraftSplitAck(billBankDao,recordId);
		}
	}
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
	public void updateVoucherInfoByVoucherId(String voucherId, Integer boxStatus,
			String billStatus, Long sysSubject, String ebillBlCrclIndc, String blBsnTpcd) {
		billBankDao.updateVoucherInfoByVoucherId(voucherId, boxStatus, billStatus,ebillBlCrclIndc,blBsnTpcd);
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
	
	/**
	 * <b>功能：</b>新增签收表数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param billSign 签收表实体 
	 */
	public synchronized void saveBillSign(BillSignVo billSign) {
		if (logger.isDebugEnabled()) {
			logger.debug("BillSign="+billSign.toString());
		}
		billCommonBaseService.saveBillSign(billSign);
	}
	public void dltBatchNoByBifCodeEnd(String recordId) {
		// TODO Auto-generated method stub
		
	}
	public List<VoucherInfoVo> queryDltBatchNoByBifCode(String bifCode) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
