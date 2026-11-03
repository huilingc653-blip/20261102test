package com.bytter.biz.receipt.service;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.receipt.dao.EConsignBillDao;
import com.bytter.biz.receipt.entity.EConsignBill;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Service
* @Description: 委托收款电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:29:50
* @version 1.0
*
*/
@Service("eConsignBillService")
@Transactional
public class EConsignBillService extends BaseService<EConsignBill,String> {
	@Autowired
	private EConsignBillDao eConsignBillDao;
	@Autowired
	private EBillPrintService ebillPrintService;
	
	@Autowired
	private EBillMessageService ebillMessageService;
	/**
	* 保存
	* @param entity
	*/
 	public void save(EConsignBill entity){
 		super.saveEntity(entity);
 	}
 	
	@Override
	public BaseDao<EConsignBill, String> getEntityDao() {
		return eConsignBillDao;
	}
	
	/**
	 * 创建委托收款电子回单
	 * @param orderNo 流水号
	 * @param voucherNo 凭证号
	 * @param tallyDate 记账日期
	 * @param voucherStatus 凭证状态
	 * @param debitAcc 借方账号
	 * @param creditAcc 贷方账号
	 * @param voucherType 凭证类型
	 * @param origVoucherNo 原凭证号
	 * @param netId --网点ID
	 * @param operatorCode 操作员
	 * @param checkerCode 复核员
	 */
	public void createConsignBill(String orderNo,String voucherNo,
			Date tallyDate,String voucherStatus,
			String debitAcc,String creditAcc,
			String voucherType,String origVoucherNo,String netId,
			String operatorCode,String checkerCode, ICurrentUser user){
		
		String[] blnctrAcc={debitAcc,creditAcc};
		if(StringUtils.isEmpty(blnctrAcc)||blnctrAcc.length!=2){
			return;
		}
		List<ELoanBillInfoVo> fcAccInfoList = eConsignBillDao.listFcAccByAcc(blnctrAcc);
		ELoanBillInfoVo debitAccInfo=null;
		ELoanBillInfoVo creditAccInfo=null;
		ELoanBillInfoVo billInfo=new ELoanBillInfoVo();
		billInfo.setDebitAcc(debitAcc);
		billInfo.setCreditAcc(creditAcc);
		for(ELoanBillInfoVo fcAccInfo:fcAccInfoList){
			if(fcAccInfo.getAccNo().equals(debitAcc)){
				debitAccInfo=fcAccInfo;
				billInfo.setDebitSubjec(debitAccInfo.getSysSubjectCode());
				billInfo.setDebitSubjecName(debitAccInfo.getSysSubjectCodeName());
				billInfo.setCur(debitAccInfo.getCur());
				billInfo.setDebitCorpId(debitAccInfo.getCorpId());
				billInfo.setDebitCorpName(debitAccInfo.getCorpName());
				billInfo.setDebitNetId(debitAccInfo.getNetId());
				billInfo.setDebitNetName(debitAccInfo.getNetName());
				billInfo.setContractCode(debitAccInfo.getContractCode());
				billInfo.setDebitAccName(debitAccInfo.getAccName());
			}else if(fcAccInfo.getAccNo().equals(creditAcc)){
				creditAccInfo=fcAccInfo;
				billInfo.setCreditSubjec(creditAccInfo.getSysSubjectCode());
				billInfo.setCreditSubjecName(creditAccInfo.getSysSubjectCodeName());
				billInfo.setCreditCorpId(creditAccInfo.getCorpId());
				billInfo.setCreditCorpName(creditAccInfo.getCorpName());
				billInfo.setCreditNetId(creditAccInfo.getNetId());
				billInfo.setCreditNetName(creditAccInfo.getNetName());
				billInfo.setCreditAccName(creditAccInfo.getAccName());
			}
		}
		billInfo.setPayerBank(billInfo.getDebitNetName());
		billInfo.setPayeeBank(billInfo.getCreditNetName());
		if(debitAccInfo==null||creditAccInfo==null){
			return;
		}
		
		billInfo.setPayerBank(billInfo.getDebitNetName());
		billInfo.setPayeeBank(billInfo.getCreditNetName());
		
		List<ELoanBillInfoVo> accbookBlnedList = eConsignBillDao.listAccbookBlnedAcc(orderNo,blnctrAcc);
		ELoanBillInfoVo debitBookInfo=null;
		ELoanBillInfoVo creditBookInfo=null;
		for(ELoanBillInfoVo accbookInfo:accbookBlnedList){
			if(debitAcc.equals(accbookInfo.getAccNo())){
				debitBookInfo=accbookInfo;
				billInfo.setDebitMoney(debitBookInfo.getDebitMoney());
				billInfo.setSummaryContent(debitBookInfo.getSummaryContent());
			}else if(creditAcc.equals(accbookInfo.getAccNo())){
				creditBookInfo=accbookInfo;
				billInfo.setCreditMoney(creditBookInfo.getCreditMoney());
			}
		}
		billInfo.setMoney(billInfo.getDebitMoney());
		if(billInfo.getMoney()==null){
			return;
		}
		if(billInfo.getDebitNetId().equals(billInfo.getCreditNetId())){
			billInfo.setCorpId(new StringBuilder(billInfo.getDebitCorpId()).append(";").append(billInfo.getCreditCorpId()).toString());
		}else if(!billInfo.getDebitNetId().equals(billInfo.getCreditNetId())
				&&billInfo.getDebitNetId().equals(netId)){
			billInfo.setCorpId(billInfo.getDebitCorpId());
		}else if(!billInfo.getDebitNetId().equals(billInfo.getCreditNetId())
				&&!billInfo.getDebitNetId().equals(netId)){
			billInfo.setCorpId(billInfo.getDebitCorpId());
		}else{
		    return;
		}
		
		ELoanBillInfoVo cgsBillInfo=eConsignBillDao.getCgsBillByConsignCode(origVoucherNo);
		if(cgsBillInfo!=null){
			billInfo.setConsignDate(cgsBillInfo.getConsignDate());
			billInfo.setContractCode(cgsBillInfo.getContractCode());
			billInfo.setSummaryContent(cgsBillInfo.getSummaryContent());
		}
		billInfo.setVoucherNo(voucherNo);
		billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_06);
	
		//如果是作废凭证则判断是否已经生成电子回单修改旧状态和设置日志信息
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("voucherNo", voucherNo);
		params.put("notStatus", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", DateUtils.getDate());
		ELoanBillInfoVo printInfo=eConsignBillDao.getPrintInfoByVoucherNo(params);
		if(printInfo!=null){
			if(FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)){
			if(printInfo.getPrints()>1&&printInfo.getPrints2()>1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				eConsignBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL, 
						ReceiptConstant.BILL_MSG_INFO_CANCELL,user);
			}else if(printInfo.getPrints()==1&&printInfo.getPrints2()>1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				eConsignBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL, 
						ReceiptConstant.BILL_MSG_INFO_CANCELL1,user);
			}else if(printInfo.getPrints()>1&&printInfo.getPrints2()==1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				eConsignBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL, 
						ReceiptConstant.BILL_MSG_INFO_CANCELL2,user);
			}else if(printInfo.getPrints()==1&&printInfo.getPrints2()==1){
				eConsignBillDao.deleteELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_DELETE, 
						ReceiptConstant.BILL_MSG_INFO_DELETE,user);
			}
			}
		}else if(!FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)){
			billInfo.setBillNo(SequenceSingleton.getSequence(SequenceEnum.E_CONSIGN_BILL_NO_C));
			billInfo.setRandom(ReceiptUtils.getBillRandom(billInfo.getBillNo()));
			billInfo.setValidcode(
					ReceiptUtils.getBillValidcode(
							billInfo.getDebitAcc(), 
							billInfo.getCreditAcc(),
							billInfo.getDebitMoney(),
							voucherNo,
							billInfo.getRandom()));
			billInfo.setEncrySource(ReceiptUtils.getBillEncrySource(billInfo.getValidcode()));
			//非作废凭证进行入库
			EConsignBill loanBill=new EConsignBill();
			loanBill.setCorpId(billInfo.getCorpId());
			loanBill.setBillNo(billInfo.getBillNo());
			loanBill.setPayerName(billInfo.getDebitAccName());
			loanBill.setPayerBank(billInfo.getPayerBank());
			loanBill.setPayerAcc(billInfo.getDebitAcc());
			loanBill.setPayeeName(billInfo.getCreditAccName());
			loanBill.setPayeeBank(billInfo.getPayeeBank());
			loanBill.setPayeeAcc(billInfo.getCreditAcc());
			loanBill.setCurId(billInfo.getCur());
			loanBill.setPayMoney(billInfo.getMoney());
			loanBill.setPurpose(billInfo.getSummaryContent());
			loanBill.setVoucherNo(origVoucherNo);
			loanBill.setConsignDate(billInfo.getConsignDate());
			loanBill.setConsignCode(origVoucherNo);
			loanBill.setCreditAcc(billInfo.getCreditSubjecName()+billInfo.getCreditAcc());
			loanBill.setDebitAcc(billInfo.getDebitSubjecName()+billInfo.getDebitAcc());
			loanBill.setContractCode(billInfo.getContractCode());
			loanBill.setEncryDate(new Date());
			loanBill.setRandom(billInfo.getRandom());
			loanBill.setEncrySource(billInfo.getEncrySource());
			loanBill.setBillType(billInfo.getBillType());
			loanBill.setPayType(voucherType);
			loanBill.setValidateCode(billInfo.getValidcode());
			loanBill.setStatus(ReceiptConstant.LOAN_BILL_STATUS_INPUT);
			loanBill.setTallierCode(operatorCode);
			loanBill.setTallyDate(tallyDate);
			loanBill.setPrints(1);
			loanBill.setSubscriber(user.getUsername());
			loanBill.setCheckerCode(checkerCode);
			loanBill.setCreateBy(user.getLoginName());
			loanBill.setCreateDate(new Date());
			
			
			eConsignBillDao.save(loanBill);
			ebillMessageService.saveEBillMessage(billInfo.getBillNo(), 
					ReceiptConstant.BILL_MSG_OP_TYPE_CREATE, 
					ReceiptConstant.BILL_MSG_INFO_CREATE,user);
			if(billInfo.getCorpId().indexOf(CommonConstant.SEMI_SPLIT)>-1){
				String[] corpIds=billInfo.getCorpId().split(";");
				ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
						corpIds[0], 1,user);
				ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
						corpIds[1], 1,user);
			}else{
				ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
						billInfo.getCorpId(), 1,user);
			}
			eConsignBillDao.flush();
		}
	}
}
