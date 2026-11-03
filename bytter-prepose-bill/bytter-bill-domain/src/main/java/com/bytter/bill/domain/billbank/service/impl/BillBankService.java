package com.bytter.bill.domain.billbank.service.impl;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillVoucherInfoDao;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.pool.service.BillPoolManageImpService;
import com.bytter.bill.domain.billbank.service.BillCommonBaseService;
import com.bytter.bill.domain.billbank.service.IBillBankService;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>开票申请业务操作service<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-05-31 <br>
 */
@Service("billBankService")
@Transactional
public class BillBankService implements IBillBankService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BillBankDao billBankDao;
	@Autowired
	private BillPoolManageImpService billPoolManageImpService;
	
	@Autowired
	private BillCommonBaseService billCommonBaseService;
	
	@Autowired
	private BillAccrecordsDao billAccrecordsDao;
	
	@Autowired
	private BillVoucherInfoDao billVoucherInfoDao;
	
	/**
	 * <b>功能：</b>开票申请-根据银行接口代码查询开票申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-5-31 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRegisterByBifCode(String bifCode, String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_001,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS16,AccebillConstant.poolSign_2,false);
		return voucherInfoList;
	}
	
	/**
	 * <b>功能：</b>批次开票申请-根据银行接口代码查询开票申请待发送指令票据信息<br>
	 * <b>作者：</b>zhengg<br>
	 * <b>日期：</b> 2020-11-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRegisterBatchByBifCode(String bifCode,String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_001,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS16,AccebillConstant.poolSign_2, true);
		return voucherInfoList;
	}
	/**
	 * <b>功能：</b>开票申请-根据银行接口代码查询开票申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-5-31 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRegisterPoolByBifCode(String bifCode,String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_001,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS16,AccebillConstant.poolSign_1);
		return voucherInfoList;
	}
	/**
	 * <b>功能：</b>开票申请-根据流水ID修改开票申请流水表状态为待回应<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-1 <br>
	 * @param recordId 流水ID
	 * @return
	 */
	public void updateAccrecordsStatusByRecordId(String recordId) {
		billBankDao.updateAccrecordsStatusByRecordId(recordId, AccebillConstant.AB_BILL_OPT_STATUS18);
	}
	
	/**
	 * <b>功能：</b>开票申请-根据银行接口代码查询开票申请待回应数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-14 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRegisterAwaitRespondPoolByBifCode(String bifCode,String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_001,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS18,AccebillConstant.poolSign_1);
		return voucherInfoList;
	}
	
	/**
	 * <b>功能：</b>开票申请-根据银行接口代码查询开票申请待回应数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-14 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode,String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_001,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS18,AccebillConstant.poolSign_2, false);
		
		return voucherInfoList;
	}
	
	/**
	 * <b>功能：</b>批量开票申请-根据银行接口代码查询开票申请待回应数据<br>
	 * <b>作者：</b>zhengg<br>
	 * <b>日期：</b> 2020-11-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRegisterAwaitRespondBatchByBifCode(String bifCode,String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_001,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS18,AccebillConstant.poolSign_2, true);
		return voucherInfoList;
	}
	/**
	 * <b>功能：</b>开票申请-回应完成后修改票据表与流水表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-1 <br>
	 * @param voucher 票据信息
	 * @return
	 */
	public void updateVoucherInfo(VoucherInfoVo voucher) {
		BillAbVoucherinfo billAbVoucherinfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
		//根据票据ID更新票据表信息
		//修改票据表信息bill_status:出票已登记(010004),
		String billStatus = "1".equals(billAbVoucherinfo.getNewStmIssuofpprInd()) ? AccebillConstant.NEWBILL_STATUS_CS01 : AccebillConstant.AB_BILL_STATUS_010004;
		String ebillBlCrclIndc = "1".equals(billAbVoucherinfo.getNewStmIssuofpprInd()) ? AccebillConstant.CIRCULATE_TF0101 : null;
		String blBsnTpcd = "1".equals(billAbVoucherinfo.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC01 : null;
		billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus, ebillBlCrclIndc, blBsnTpcd);
		//根据流水ID更新票据流水表信息
		//修改流水表status:待提示承兑
		billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(),voucher.getBillId());
		//根据票据ID更新票据表票据号
		billBankDao.updateVoucherOrigNoByVoucherId(voucher.getVoucherId(), voucher.getOrigNo(), billAbVoucherinfo.getSbbllRngStrtSn(), billAbVoucherinfo.getSbbllRngEndSn());
		//根据流水ID更新票据流水表的上个票据状态
		billBankDao.updateAccrecordsLastInfoByRecordId(voucher.getRecordId(),billStatus);
		
		//针对出票申请业务状态修改进行票据池处理
		billPoolManageImpService.accebillBusPoolOp(voucher.getRecordId());
	}
	
	/**
	 * <b>功能：</b>自动生成生成提示承兑待发送（需经办）业务流水<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2022-6-21 <br>
	 * @param voucher 票据信息（主表id:VoucherId，流水id:RecordId）
	 * @return
	 */
	public void autoBillAcceptAccrecords(VoucherInfoVo voucher){
		BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
		if(oldInfo == null){
			throw new RuntimeException("票据信息不存在！");
		}
		BeanUtils.copyNotNullProperties(oldInfo,voucher);
		String billStatus = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEWBILL_STATUS_CS01 : AccebillConstant.AB_BILL_STATUS_020001;
		String ebillBlCrclIndc = "1".equals(voucher.getNewStmIssuofpprInd()) ? null : AccebillConstant.CIRCULATE_TF0302;
		String blBsnTpcd = "1".equals(voucher.getNewStmIssuofpprInd()) ? null : AccebillConstant.NEW_DRAFT_TYPE_BC02;
		//修改票据表信息bill_status:提示承兑待签收(020001)
		billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus, ebillBlCrclIndc, blBsnTpcd);
		//生成提示承兑待发送（需经办）业务流水
		this.saveAcceptanceAccrecords(voucher);
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
		if(AccebillConstant.AB_BILL_OPT_STATUS_1.equals(String.valueOf(status))||AccebillConstant.AB_BILL_OPT_STATUS18.equals(String.valueOf(status))){
			//针对出票申请业务失败进行票据池处理
			billPoolManageImpService.accebillBusPoolOp(recordId);
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
	 *
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
	 * <b>功能：</b>根据流水ID更新银行返回代码、消息、流水状态为18、billId赋值<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2020-09-22 <br>
	 * @param recordId 流水ID
	 * @param reCode 银行返回代码
	 * @param reMsg 银行返回消息
	 * @param billId 银行返回id
	 */
	public void updateAccReCodeAndReMsgAndBillByRecordId(String recordId,
			String reCode, String reMsg, String billId) {
		//修改流水表status:已回应待签收 18
		billBankDao.updateAccrecordsInfoByRecordId(recordId, AccebillConstant.AB_BILL_OPT_STATUS18, null, reCode, reMsg, billId);
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
	 * <b>功能：</b>自动签收后处理来自外部的票据--开票申请业务完成<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2019-06-11 <br>
	 * @param  voucher 票据主信息
	 */
	public synchronized void autoSignManage(VoucherInfoVo voucher) {
		voucher.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);
		voucher.setBriefId("001");
		if (logger.isDebugEnabled()) {
			logger.debug("autoSign="+ voucher);
		}
		billCommonBaseService.autoSignManage(voucher);
	}
	
	public void saveAcceptanceAccrecords(VoucherInfoVo voucher){
		BillAbVoucherinfo voucherInfo=billVoucherInfoDao.get(voucher.getVoucherId());
		String recordId = billAccrecordsDao.getAccrecordsInfoByVoucherIdAndBriefId(voucher.getVoucherId(), AccebillConstant.BRIEF_STATUS_002);
		if(StringUtil.isEmpty(recordId)){
			BillAbAccrecords accrecords=billAccrecordsDao.get(voucher.getRecordId());
			// 生成新的流水信息(提示承兑申请流水)
			// 新的流水数据
			BillAbAccrecords entity = new BillAbAccrecords();
			BeanUtils.copyNotNullProperties(accrecords,entity);
			entity.setVid(voucherInfo.getId());// 票据主表ID
			entity.setVvid(accrecords.getVvid());// 票据从表ID
			entity.setLastid(accrecords.getId());// 保存上一笔流水ID
			entity.setLastbillcode(accrecords.getBriefId());// 保存上一笔业务类型
			entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);// 有效标志  0未完成；1可用；2已结束
			entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);// 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
			//待发送
			entity.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS16);//操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签
			entity.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);//是否发送指令标志：0否；1是 ；
			entity.setReturnCode(AccebillConstant.BANK_RE_CODE3);//返回代码
			// entity.setReturnMsg();//处理结果
			entity.setBriefId(AccebillConstant.BRIEF_STATUS_002);// 流水业务类型002 提示承兑
			//entity.setInsideSing(accrecords.getInsideSing());// 内部业务标志
			//entity.setEnterStockNew(abVice.getEnterStock());// 库存
			//entity.setEnterStockOld(abAccrecords.getEnterStockNew());// 把上一条所属库存保存到旧的所属库存
			//entity.setBailType(accrecords.getBailType());//保证类型:01开票保证，02承兑保证；03背书保证
			//entity.setBankendrsmtmk(accrecords.getBankendrsmtmk());// 不得转让标记：EM00可再转让；EM01 不得转让
			entity.setApplyDate(new Date());// 申请日期
			entity.setBusinessDate(new Date());// 业务办理日期
			//entity.setBifCode(accrecords.getBifCode());
			//entity.setCnAccrecords(accrecords.getCnAccrecords());
			//entity.setSignUpFlag(voucher.getSignUpFlag());
			entity.setBillFlag(voucherInfo.getBillFlag());
			//写入接口需要发送的消息号 每次修改重新生成新的消息号
			entity.setMsgId(StringUtil.getMsgId(4));
			// .......业务申请人...，业务接收人....
			//entity.setReqRole(accrecords.getReqRole());//业务申请人类别
			//entity.setReqNm(accrecords.getReqNm());//业务申请人
			//entity.setReqCmonid(accrecords.getReqCmonid());//业务申请人组织机构代码
			//entity.setReqAcctId(accrecords.getReqAcctId());//业务申请人账号
			//entity.setReqAcctsvcr(accrecords.getReqAcctsvcr());//业务申请人开户行号
			//entity.setReqAcctsvcrAg(accrecords.getReqAcctsvcrAg());//业务申请人承接行号
			//entity.setRecRole(accrecords.getRecRole());//业务接收人类别
			entity.setRecNm(voucherInfo.getAcceptor());//业务接收人名称
			//entity.setRecCmonid(voucherInfo.);//业务接收人组织机构代码
			entity.setRecAcctId(voucherInfo.getAcceptoracc());//业务接收人账号
			entity.setRecAcctsvcr(voucherInfo.getAcceptorbankno());//业务接收人开户行号
			//entity.setRecAcctsvcrAg(accrecords.getRecAcctsvcrAg());//业务接收人承接行号
			//entity.setBillId(accrecords.getBillId());
			entity.setIsHandling(AccebillConstant.IS_HANDLING1);//是否经办 1是
			entity.setCreateDate(new Date());
			billAccrecordsDao.save(entity);
		}
	}
}
