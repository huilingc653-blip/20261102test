package com.bytter.bill.domain.billbank.service.impl;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.dao.BillVoucherInfoDao;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.entity.BillSign;
import com.bytter.bill.domain.billbank.service.BillCommonBaseService;
import com.bytter.bill.domain.billbank.service.IBillRecourseRequestService;
import com.bytter.bill.domain.billbank.vo.BillSignVo;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>追索通知业务操作service<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-6-7 <br>
 */
@Service("billRecourseRequestService")
@Transactional
public class BillRecourseRequestService implements IBillRecourseRequestService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BillBankDao billBankDao;
	@Autowired
	private BillSignDao billSignDao;
	@Autowired
	private BillVoucherInfoDao billVoucherInfoDao;
	@Autowired
	private BillAccrecordsDao billAccrecordsDao;
	
	@Autowired
	private BillCommonBaseService billCommonBaseService;
	@DubboReference(check = false)
	private ICorpService corpServiceImpl;
	/**
	 * <b>功能：</b>追索通知-发起方--查询申请待发送指令票据信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRecourseRequestByBifCode(String bifCode, String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_022,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS16);
		return voucherInfoList;
	}
	/**
	 * <b>功能：</b>追索通知--发起方--指令发送已发送待回应<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param recordId 流水ID
	 * @param voucherId 票据ID
	 * @return
	 */
	public void updateVoucherInfo(VoucherInfoVo voucher){
		//根据流水ID更新票据流水表信息
		billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS18, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
//		updateVoucherInfoEnd(voucher);
		//修改票据表信息 boxStatus：业务处理完成（11） 
		billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
		//根据票据ID流水ID修改票据表的bill_status值，由流水表的rcrs_tp决定是拒付追索还是非拒付追索
		billBankDao.updateVoucherInfoBillStatusByRcrsTp(voucher.getVoucherId(),voucher.getRecordId(),voucher.getNewStmIssuofpprInd(), voucher.getEndDate());
		//修改前一笔流水为可用  追索业务成功以后需要修改前一笔流水为可用（可以继续追索其他人）
	    billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(),AccebillConstant.AB_BILL_VALID_SIGN_1);
	    
	}
	/**
	 * <b>功能：</b>追索通知--根据银行接口代码及银行返回代码查询待签收数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-15 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	public List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode,String reCode) {
		List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_022,bifCode,reCode,AccebillConstant.AB_BILL_OPT_STATUS18);
		
		return voucherInfoList;
	}
	
	/**
	 * <b>功能：</b>追索通知-发起方--指令发送已回应成功<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param voucher 票据信息
	 * @return
	 */
	public void updateVoucherInfoEnd(VoucherInfoVo voucher) {
		//根据票据ID更新票据表信息
		//修改票据表信息 boxStatus：业务处理完成（11） 
		//billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, null);
		billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
		//根据流水ID更新票据流水表信息
		//修改流水表status:签收成功 19
		billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
		//根据票据ID流水ID修改票据表的bill_status值，由流水表的rcrs_tp决定是拒付追索还是非拒付追索
		billBankDao.updateVoucherInfoBillStatusByRcrsTp(voucher.getVoucherId(),voucher.getRecordId(),voucher.getNewStmIssuofpprInd(),voucher.getEndDate());
		//修改前一笔流水为可用  追索业务成功以后需要修改前一笔流水为可用（可以继续追索其他人）
	    billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(),AccebillConstant.AB_BILL_VALID_SIGN_1);
	    
	    //保存一笔清偿待签收数据到签收表（对内）
	    BillAbVoucherinfo vouInfo=billVoucherInfoDao.get(voucher.getVoucherId());
	    BillAbAccrecords accrecord=billAccrecordsDao.get(voucher.getRecordId());
	    BillSign billSign=setBillSign(vouInfo,accrecord);
	    billSign.setMsgtype(AccebillConstant.BRIEF_STATUS_022);
		billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_210001);
		//根据业务接收人的账号写入接收人组织id、接收人组织机构代码、接收人开户行
//		List<Map<String,Object>> objectList = billSignDao.queryVAccListVo(billSign.getRecAcctId());
		List<Map<String,Object>> objectList = corpServiceImpl.queryVAccListVo(billSign.getRecAcctId());
		if(!objectList.isEmpty()){
			this.saveBillSign(billSign);
		}
	    
	}
	
	/**
	 * <b>功能：</b>追索通知--指令发送回应拒收<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-19 <br>
	 * @param recordId 拒收回复流水ID
	 * @return
	 */
	public void updateAccrecordsStatusRejectionByRecordId(VoucherInfoVo voucher) {
		//修改票据表信息 boxStatus：业务处理完成（11） 
		//billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, null);
		billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
		billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4,voucher.getReturnCode(),voucher.getReturnMsg(), voucher.getBillId());
		//修改前一笔流水为可用
	    billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(),AccebillConstant.AB_BILL_VALID_SIGN_1);
	}
	
	/**
	 * <b>功能：</b>根据流水ID更新票据流水表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param recordId 票据流水ID
	 * @param status 操作状态
	 * @param briefId 业务类型
	 * @param returnCode 返回代码
	 * @param returnMsg 返回消息
	 */
	public void updateAccrecordsInfoByRecordId(String recordId, Integer status,
			String briefId, String returnCode, String returnMsg) {
		billBankDao.updateAccrecordsInfoByRecordId(recordId, status==null?null:String.valueOf(status), briefId, returnCode, returnMsg, null);
	}
	/**
	 * <b>功能：</b>根据票据ID更新票据表信息<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-7 <br>
	 * @param voucherId 票据ID
	 * @param boxStatus 处理标记
	 * @param billStatus 票据状态
	 * @param sysSubject 系统科目
	 * @param ebillBlCrclIndc 票据流通标志
	 * @param blBsnTpcd 业务代码
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
	public synchronized void saveBillSign(BillSign billSign) {
		billCommonBaseService.save(billSign);
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
		if(billSign.getReqamt()==null||billSign.getReqamt().doubleValue()==0){
			billSign.setReqamt(billSign.getBillMoney());
		}
		billCommonBaseService.saveBillSign(billSign);
	}
	
	public BillSign setBillSign(BillAbVoucherinfo vouInfo,BillAbAccrecords info){
		BillSign billSign=new BillSign();
		BeanUtils.copyNotNullProperties(vouInfo,billSign,"id");
		billSign.setMsgid(info.getId());
		//billSign.setOrigNo(vouInfo.getOrigNo());
		//billSign.setBillType(vouInfo.getBillType());
		//billSign.setStartDate(vouInfo.getStartDate());
		//billSign.setEndDate(vouInfo.getEndDate());
		//billSign.setBillMoney(vouInfo.getBillMoney());
		//billSign.setPayee(vouInfo.getPayee());
		//billSign.setPayeeacc(vouInfo.getPayeeacc());
		//billSign.setPayeebankno(vouInfo.getPayeebankno());
		//billSign.setPayer(vouInfo.getPayer());
		//billSign.setPayeracc(vouInfo.getPayeracc());
		//billSign.setPayerbankno(vouInfo.getPayerbankno());
		//billSign.setAcceptor(vouInfo.getAcceptor());
		//billSign.setAcceptoracc(vouInfo.getAcceptoracc());
		//billSign.setAcceptorbankno(vouInfo.getAcceptorbankno());
		billSign.setCdtratgs(vouInfo.getPayerCdtRatgs());
		billSign.setCdtratgagcy(vouInfo.getPayerCdtRatgGgcy());
		billSign.setCdtratgduedt(vouInfo.getPayerCdtRatgDuedt());
		billSign.setBussdt(info.getApplyDate());
		//billSign.setAccptncagrmtnb(vouInfo.getAccptncagrmtnb());
		billSign.setTxlctrctnb(vouInfo.getAcontractNo());
		billSign.setInvcnb(vouInfo.getAinvcnb());
		//billSign.setAcceptordate(vouInfo.getACCEPTORDATE);
		//billSign.setSttlmmk(info.getSttlmmk());
		billSign.setBankendrsmtmk(vouInfo.getBanendrsmtmk());
		billSign.setAppldt(info.getApplyDate());
		billSign.setRcrsTp(info.getRcrsTp());
		billSign.setReqamt(info.getRcrsokmoney());
		billSign.setRcrsrsncd(info.getRcrsrsncd());
		billSign.setReqRole(info.getReqRole());
		billSign.setReqNm(info.getReqNm());
		billSign.setReqCmonid(info.getReqCmonid());
		billSign.setReqAcctId(info.getReqAcctId());
		billSign.setReqAcctsvcr(info.getReqAcctsvcr());
		billSign.setReqAcctsvcrAg(info.getReqAcctsvcrAg());
		billSign.setRecRole(info.getRecRole());
		billSign.setRecNm(info.getRecNm());
		billSign.setRecCmonid(info.getRecCmonid());
		billSign.setRecAcctId(info.getRecAcctId());
		billSign.setRecAcctsvcr(info.getRecAcctsvcr());
		billSign.setRecAcctsvcrAg(info.getRecAcctsvcrAg());
		billSign.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
		billSign.setCreateDate(new Date());
		return billSign;
	}
	/**
	 * <b>功能：</b>查询银行账号信息视图<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据信息
	 */
	public List<VAccListVo> queryVAccListVo(String bifCode) {
		List<VAccListVo> vAccListVoList=billBankDao.queryVAccListVo(bifCode);
		return vAccListVoList;
	}
	/**
	 * <b>功能：</b>保存追索通知签收流水<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-04-25 <br>
	 * @param  voucher 票据主信息(票据表信息，流水表信息)
	 */
	public synchronized void saveRecourseAccrecords(VoucherInfoVo voucher) {
		if (logger.isDebugEnabled()) {
			logger.debug("autoSign="+voucher.toString());
		}
		billCommonBaseService.autoSignManage(voucher);
	}
	public List<VoucherInfoVo> queryBillRegisterAwaitReplyByBifCode(String bifCode, String reCode) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<VoucherInfoVo> queryRegisterAwaitReplyRegisterAwaitByBifCode(String bifCode, String reCode) {
		// TODO Auto-generated method stub
		return null;
	}
	public void updateAccrecordsStatusByRecordId(VoucherInfoVo voucher) {
		// TODO Auto-generated method stub
		
	}
	public void updateAccrecordsRejectionByRecordId(VoucherInfoVo voucher) {
		// TODO Auto-generated method stub
		
	}
}
