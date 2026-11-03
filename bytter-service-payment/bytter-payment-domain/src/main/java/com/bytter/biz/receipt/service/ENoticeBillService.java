package com.bytter.biz.receipt.service;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.service.IReceiptQueryService;
import com.bytter.biz.finacial.api.vo.ELoanBillInfoVo;
import com.bytter.biz.receipt.dao.ENoticeBillDao;
import com.bytter.biz.receipt.entity.ENoticeBill;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.ENoticeBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
* @Title: Service
* @Description: 通知单电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:30:08
* @version 1.0
*
*/
@Service("eNoticeBillService")
@Transactional
public class ENoticeBillService extends BaseService<ENoticeBill,String> {
	@Autowired
	private ENoticeBillDao eNoticeBillDao;
	@Autowired
	private EBillPrintService ebillPrintService;
	
	@Autowired
	private EBillMessageService ebillMessageService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@DubboReference(check = false)
	private IReceiptQueryService iReceiptQueryService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private ICorpService corpService;
	/**
	* 保存
	* @param entity
	*/
 	public void save(ENoticeBill entity){
 		super.saveEntity(entity);
 	}
	@Override
	public BaseDao<ENoticeBill, String> getEntityDao() {
		return eNoticeBillDao;
	}
	/**
	 *  查询通知单电子回单
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listForUnion(DataTablesInput input, ICurrentUser user){
		StringBuilder commonSql=new StringBuilder();
		if(input.getFormdata().containsKey("printSign")) {
			String printSign = (String)input.getFormdata().get("printSign");
			commonSql.append(ReceiptUtils.getSearchSqlCommon(printSign));
		}
		//权限控制类型  1-单位级 2-账号级
		String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
		if (!CommonConstant.YES.equals(controlType)) {
			List<String> accountGrantList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_RECEIPT_ELEBILL_QUERY, null,
					AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
			String payerAccAuthSql = SqlUtil.getInSql(accountGrantList, "A.PAYER_ACC", true);
			String payeeAccAuthSql = SqlUtil.getInSql(accountGrantList, "A.PAYEE_ACC", true);
			commonSql.append(" and (").append(payerAccAuthSql).append(" or ").append(payeeAccAuthSql).append(")");
		}
		DataTablesOutput output = eNoticeBillDao.listForUnion(input,commonSql.toString());
		List<ENoticeBillVO> list=output.getData();
		if(list.size()>0) {
			List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.NETBANK_RECEIPT_ELEBILL_QUERY, user.getId());
			Map<String, String> corpMap = corpService.getNameByIds(corpIds);
			List<DictDTO> dictList = dictService.list("eVisaType.billType");
			Map<String, String> dictMap = dictList.stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
//			Map<String, String> currencyMap = btCurrencyService.getCurrencyNameMap();

			for(ENoticeBillVO vo:list){
				vo.setCorpName(corpMap.get(vo.getCorpId()));//单位
				vo.setBillTypeName(dictMap.get(vo.getBillType()));//电子回单类型
//				vo.setCurName(currencyMap.get(vo.getCurId()));//币别
			}
		}
		return output;

	}
	/**
	 * 根据单据号获取VO
	 * 参数至少有一个有值
	 * @param billNo 单据号 可空
	 * @param id 流水号  可空
	 * @return
	 */
	public ENoticeBillVO getVOByBillNo(String billNo, String id){
		ENoticeBillVO eNoticeBillVO=eNoticeBillDao.getVOByBillNo(billNo,id);
		List<DictDTO> dictList = dictService.list("eVisaType.billType");
		Map<String, String> dictMap = dictList.stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
		eNoticeBillVO.setBillTypeName(dictMap.get(eNoticeBillVO.getBillType()));//电子回单类型
		return eNoticeBillVO;
	}
	
	/**
	 * 创建贷款凭证电子回单
	 * @param type 几借几贷
	 * @param orderNo 流水号
	 * @param voucherNo 凭证号
	 * @param origVoucherNo 原凭证号
	 * @param affixAmount 附件张数
	 * @param tallyDate 记账日期
	 * @param interStart 起息日期
	 * @param voucherStatus 凭证状态
	 * @param debitAcc 借方账号
	 * @param creditAcc 贷方账号
	 * @param voucherType 凭证类型
	 * @param externalCorp 外单位名称
	 * @param externalAcc 外单位帐号
	 * @param bank 开户行
	 * @param rmk 备注
	 * @param operatorCode 操作员
	 * @param checkerCode 复核员
	 */
	public void createNoticeBill(String type,
			String orderNo,String voucherNo,
			String origVoucherNo,Integer affixAmount,
			Date tallyDate,Date interStart,
			String voucherStatus,
			String debitAcc,String creditAcc,String interestAcc,
			String voucherType,
			String externalCorp,String externalAcc,
			String bank,String rmk,
			String operatorCode,String checkerCode, ICurrentUser user){
		if(StringUtils.isEmpty(debitAcc)){
			return;
		}
		String[] blnctrAcc={debitAcc,creditAcc};
//		List<ELoanBillInfoVo> fcAccInfoList = eNoticeBillDao.listFcAccByAcc(blnctrAcc);
		List<FcAccDTO> fcAccInfoList = iFcAccService.getAccSet(Arrays.asList(blnctrAcc));
		FcAccDTO debitAccInfo=null;
		FcAccDTO creditAccInfo=null;
		ELoanBillInfoVo billInfo=new ELoanBillInfoVo();
		billInfo.setDebitAcc(debitAcc);
		billInfo.setCreditAcc(creditAcc);
		for(FcAccDTO fcAccInfo:fcAccInfoList){
			if(debitAcc.equals(fcAccInfo.getBlnctrAcc())){
				debitAccInfo=fcAccInfo;
				billInfo.setDebitSubjec(debitAccInfo.getSysSubjectCode());
				billInfo.setDebitSubjecName(debitAccInfo.getSubjectName());
				billInfo.setCur(debitAccInfo.getCurId());
				billInfo.setCorpId(debitAccInfo.getCorpId());
				billInfo.setDebitNetName(debitAccInfo.getNetName());
			}else if(creditAcc.equals(fcAccInfo.getBlnctrAcc())){
				creditAccInfo=fcAccInfo;
				billInfo.setCreditSubjec(creditAccInfo.getSysSubjectCode());
				billInfo.setCreditSubjecName(creditAccInfo.getSubjectName());
				billInfo.setCreditCorpId(creditAccInfo.getCorpId());
				billInfo.setCreditCorpName(creditAccInfo.getCorpName());
				billInfo.setCreditNetName(creditAccInfo.getNetName());
			}
		}
		if(debitAccInfo==null){
			return;
		}
		billInfo.setPayerBank(billInfo.getDebitNetName());
		billInfo.setPayeeBank(billInfo.getCreditNetName());
		ELoanBillInfoVo vo = null;
		FcAccDTO interestAccount = null;
		if(StringUtils.isNotBlank(interestAcc)){
			String[] interestAccAr={interestAcc};
			List<ELoanBillInfoVo> accbookBlnedList = iReceiptQueryService.listAccbookBlnedAcc(orderNo,interestAccAr);
			if(EmptyUtils.isNotEmpty(accbookBlnedList)) {
				vo = accbookBlnedList.get(0);
				if(ReceiptConstant.BILL_TYPE_D.equals(type)){
					billInfo.setMoney(vo.getDebitMoney());
				}else if(!ReceiptConstant.BILL_TYPE_D.equals(type)){
					billInfo.setMoney(vo.getCreditMoney());
				}
			}

//			List<ELoanBillInfoVo> interestAccList = eNoticeBillDao.listFcAccByAcc(interestAccAr);
			List<FcAccDTO> interestAccList = iFcAccService.getAccSet(Arrays.asList(interestAccAr));
			if(EmptyUtils.isNotEmpty(interestAccList)) {
				interestAccount = interestAccList.get(0);
			}
		} else {
			List<ELoanBillInfoVo> accbookBlnedList = iReceiptQueryService.listAccbookBlnedAcc(orderNo,blnctrAcc);
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
			if(ReceiptConstant.BILL_TYPE_D.equals(type)){
				billInfo.setMoney(billInfo.getDebitMoney());
			}else if(!ReceiptConstant.BILL_TYPE_D.equals(type)){
				billInfo.setMoney(billInfo.getCreditMoney());
			}
		}
		//未来通过参数获取，目前默认为1
		String format="1";
		// TODO 电子回单注意问题：format  WHERE sys_code = 'fcs' AND code = 'subpena_format';
		if((SubjectConstant.SysSubject.INTEREST_BANK_CURRENT_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())
				||SubjectConstant.SysSubject.SUBJECT_CASH.getValue().equals(billInfo.getDebitSubjec())
				||SubjectConstant.SysSubject.TEMPORARY_RECEIVE_MONEY.getValue().equals(billInfo.getDebitSubjec())
				||(StringUtils.isNotEmpty(billInfo.getDebitSubjec()) &&
				(SubjectConstant.SYSSUBJECT_INTEREST_EXPENSES.equals(billInfo.getDebitSubjec().substring(0, 3))
						|| SubjectConstant.SYSSUBJECT_INTEREST_INCOME.equals(billInfo.getDebitSubjec().substring(0, 3)))))
				&&SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getCreditSubjec())
			){
//			--收款通知单
			/*billInfo.setCreditSubjecName(new StringBuilder("企业活期_").append(billInfo.getCreditAcc()).toString());
			if(SubjectConstant.SysSubject.INTEREST_BANK_CURRENT_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())){
				billInfo.setDebitSubjecName(new StringBuilder("银行活期_").append(billInfo.getDebitAcc()).toString());
			}else if(SubjectConstant.SysSubject.SUBJECT_CASH.getValue().equals(billInfo.getDebitSubjec())){
				billInfo.setDebitSubjecName(new StringBuilder("现金_").append(billInfo.getDebitAcc()).toString());
			}else if(SubjectConstant.SysSubject.TEMPORARY_RECEIVE_MONEY.getValue().equals(billInfo.getDebitSubjec())){
				billInfo.setDebitSubjecName(new StringBuilder("暂收款_").append(billInfo.getDebitAcc()).toString());
			}else if(StringUtils.isNotEmpty(billInfo.getDebitSubjec())){
				if (SubjectConstant.SYSSUBJECT_INTEREST_EXPENSES.equals(billInfo.getDebitSubjec().substring(0, 3))) {
					billInfo.setDebitSubjecName(new StringBuilder("利息支出_").append(billInfo.getDebitAcc()).toString());
				} else if(SubjectConstant.SYSSUBJECT_INTEREST_INCOME.equals(billInfo.getDebitSubjec().substring(0, 3))){
					billInfo.setDebitSubjecName(new StringBuilder("利息收入_").append(billInfo.getDebitAcc()).toString());
				}
			}*/
			billInfo.setCreditSubjecName(new StringBuilder(billInfo.getCreditSubjecName()+"_").append(billInfo.getCreditAcc()).toString());
			billInfo.setDebitSubjecName(new StringBuilder(billInfo.getDebitSubjecName()+"_").append(billInfo.getDebitAcc()).toString());
			billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_09);
			billInfo.setCorpId(billInfo.getCreditCorpId());
			if("1".equals(format) &&creditAccInfo!=null){
				billInfo.setCreditAccName(creditAccInfo.getAccName());
				billInfo.setPayeeBank(creditAccInfo.getNetName());
			}else if("2".equals(format)){
			}
			if(StringUtils.isNotEmpty(externalCorp)){
				billInfo.setDebitAccName(externalCorp);
			}else{
				billInfo.setDebitAccName(" ");
			}
			if(StringUtils.isNotEmpty(externalAcc)){
				billInfo.setDebitAcc(externalAcc);
			}else{
				billInfo.setDebitAcc(" ");
			}
			
			if(StringUtils.isNotEmpty(bank)){
				billInfo.setPayerBank(bank);
			}else{
				billInfo.setPayerBank(" ");
			}
		}else if((SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())
				&&(SubjectConstant.SysSubject.INTEREST_BANK_CURRENT_DEPOSIT.getValue().equals(billInfo.getCreditSubjec())
						||SubjectConstant.SysSubject.SUBJECT_CASH.getValue().equals(billInfo.getCreditSubjec())
						||SubjectConstant.SysSubject.PROCEDURE_FEE_INCOME.getValue().equals(billInfo.getCreditSubjec())
						||(StringUtils.isNotEmpty(billInfo.getCreditSubjec()) && SubjectConstant.SYSSUBJECT_INTEREST_INCOME.equals(billInfo.getCreditSubjec().substring(0, 3)))))
				||(StringUtils.isNotEmpty(billInfo.getCreditSubjec()) &&
				(SubjectConstant.SYSSUBJECT_INTEREST_EXPENSES.equals(billInfo.getCreditSubjec().substring(0, 3))
						|| SubjectConstant.SYSSUBJECT_INTEREST_INCOME.equals(billInfo.getCreditSubjec().substring(0, 3))))
				&&SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())
				){
//			 --付款通知单
			/*billInfo.setDebitSubjecName(new StringBuilder("企业活期_").append(billInfo.getDebitAcc()).toString());
			if(SubjectConstant.SysSubject.PROCEDURE_FEE_INCOME.getValue().equals(billInfo.getCreditSubjec())) {
				billInfo.setCreditSubjecName(new StringBuilder("手续费收入_").append(billInfo.getCreditAcc()).toString());
			} else if(StringUtils.isNotEmpty(billInfo.getCreditSubjec()) && SubjectConstant.SYSSUBJECT_INTEREST_INCOME.equals(billInfo.getCreditSubjec())){
				billInfo.setCreditSubjecName(new StringBuilder("利息收入_").append(billInfo.getCreditSubjec().substring(0, 3)).toString());
			} else {
				billInfo.setCreditSubjecName(new StringBuilder("银行活期_").append(billInfo.getCreditAcc()).toString());
			}*/
			billInfo.setDebitSubjecName(new StringBuilder(billInfo.getDebitSubjecName()+"_").append(billInfo.getDebitAcc()).toString());
			billInfo.setCreditSubjecName(new StringBuilder(billInfo.getCreditSubjecName()+"_").append(billInfo.getCreditAcc()).toString());
			billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_08);
			if(StringUtils.isNotEmpty(externalCorp)){
				billInfo.setCreditAccName(externalCorp);
			}else{
				billInfo.setCreditAccName(" ");
			}
			if(StringUtils.isNotEmpty(externalAcc)){
				billInfo.setCreditAcc(externalAcc);
			}else{
				billInfo.setCreditAcc(" ");
			}
			
			if(StringUtils.isNotEmpty(bank)){
				billInfo.setPayeeBank(bank);
			}else{
				billInfo.setPayeeBank(" ");
			}
			
			if("1".equals(format) &&debitAccInfo!=null){
				billInfo.setDebitAccName(debitAccInfo.getAccName());
				billInfo.setPayerBank(debitAccInfo.getNetName());
			}else if("2".equals(format)){
				
			}
		}else if(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())
				&&SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getCreditSubjec())){
//			--内部转账通知单
			billInfo.setDebitSubjecName(new StringBuilder(billInfo.getDebitSubjecName()+"_").append(billInfo.getDebitAcc()).toString());
			billInfo.setCreditSubjecName(new StringBuilder(billInfo.getCreditSubjecName()+"_").append(billInfo.getCreditAcc()).toString());
			billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_10);
			if(String.valueOf(billInfo.getCorpId()).equals(billInfo.getCreditCorpId())){
				billInfo.setCorpId(billInfo.getCorpId());
			}else{
				billInfo.setCorpId(new StringBuilder(billInfo.getCorpId()).append(";").append(billInfo.getCreditCorpId()).toString());
			}
			if("1".equals(format) &&creditAccInfo!=null){
				billInfo.setCreditAccName(creditAccInfo.getAccName());
				billInfo.setPayeeBank(creditAccInfo.getNetName());
			}else if("2".equals(format)){
				
			}
			if("1".equals(format) &&debitAccInfo!=null){
				billInfo.setDebitAccName(debitAccInfo.getAccName());
				billInfo.setPayerBank(debitAccInfo.getNetName());
			}else if("2".equals(format)){
				
			}
		}else{
			return;
		}
		if(interestAccount != null) {
			if (ReceiptConstant.BILL_TYPE_D.equals(type)) {
				if(SubjectConstant.SysSubject.ENTERPRISE_LOAN.getValue().equals(interestAccount.getSysSubjectCode())) {
					billInfo.setDebitSubjecName(new StringBuilder(interestAccount.getSubjectName()+"_").append(interestAcc).toString());
				}
			} else if (!ReceiptConstant.BILL_TYPE_D.equals(type)) {
				if(SubjectConstant.SysSubject.ENTERPRISE_LOAN.getValue().equals(interestAccount.getSysSubjectCode())) {
					billInfo.setCreditSubjecName(new StringBuilder(interestAccount.getSubjectName()+"_").append(interestAcc).toString());
				}
			}
		}
		
		if(billInfo.getMoney()==null){
			return;
		}
		billInfo.setVoucherNo(voucherNo);
	
		//如果是作废凭证则判断是否已经生成电子回单修改旧状态和设置日志信息
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("voucherNo", voucherNo);
		params.put("notStatus", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
		params.put("oldVoucher",origVoucherNo);
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", DateUtils.getDate());
		ELoanBillInfoVo printInfo=eNoticeBillDao.getPrintInfoByVoucherNo(params);
		if(printInfo!=null){
			if(FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)){
			if(printInfo.getPrints()>1&&printInfo.getPrints2()>1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				eNoticeBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL, 
						ReceiptConstant.BILL_MSG_INFO_CANCELL,user);
			}else if(printInfo.getPrints()==1&&printInfo.getPrints2()>1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				eNoticeBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL, 
						ReceiptConstant.BILL_MSG_INFO_CANCELL1,user);
			}else if(printInfo.getPrints()>1&&printInfo.getPrints2()==1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				eNoticeBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL, 
						ReceiptConstant.BILL_MSG_INFO_CANCELL2,user);
			}else if(printInfo.getPrints()==1&&printInfo.getPrints2()==1){
				eNoticeBillDao.deleteELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_DELETE, 
						ReceiptConstant.BILL_MSG_INFO_DELETE,user);
			}
			}
		}else if(!FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)){
			billInfo.setBillNo(SequenceSingleton.getSequence(SequenceEnum.E_NOTICE_BILL_NO_N));
			billInfo.setRandom(ReceiptUtils.getBillRandom(billInfo.getBillNo()));
			billInfo.setValidcode(
					ReceiptUtils.getBillValidcode(
							billInfo.getDebitAcc(), 
							billInfo.getCreditAcc(),
							billInfo.getMoney(),
							voucherNo,
							billInfo.getRandom()));
			billInfo.setEncrySource(ReceiptUtils.getBillEncrySource(billInfo.getValidcode()));
			//非作废凭证进行入库
			ENoticeBill loanBill=new ENoticeBill();
			loanBill.setCorpId(billInfo.getCorpId());
			loanBill.setBillNo(billInfo.getBillNo());
			loanBill.setPayerName(billInfo.getDebitAccName());
			loanBill.setPayerAcc(billInfo.getDebitAcc());
			loanBill.setPayerBank(billInfo.getPayerBank());
			loanBill.setPayeeName(billInfo.getCreditAccName());
			loanBill.setPayeeAcc(billInfo.getCreditAcc());
			loanBill.setPayeeBank(billInfo.getPayeeBank());
			loanBill.setCurId(billInfo.getCur());
			loanBill.setPayMoney(billInfo.getMoney());
			loanBill.setPurpose(billInfo.getSummaryContent());
			loanBill.setPayType(voucherType);
			loanBill.setCheckerCode(checkerCode);
			loanBill.setAccrualDate(interStart);
			loanBill.setTallyDate(tallyDate);
			loanBill.setVoucherNo(voucherNo);
			loanBill.setEncryDate(new Date());
			loanBill.setRandom(billInfo.getRandom());
			loanBill.setEncrySource(billInfo.getEncrySource());
			loanBill.setValidateCode(billInfo.getValidcode());
			loanBill.setStatus(ReceiptConstant.LOAN_BILL_STATUS_INPUT);
			loanBill.setOldVoucher(origVoucherNo);
			loanBill.setOldVoucherMoney(null);
			loanBill.setBillType(billInfo.getBillType());
			loanBill.setTallierCode(operatorCode);
//			remark
			loanBill.setDebit(billInfo.getDebitSubjecName());
			loanBill.setCredit(billInfo.getCreditSubjecName());
			loanBill.setPrints(1);
			loanBill.setOldVoucherName(null);
			loanBill.setAccessory(affixAmount);
			loanBill.setSubscriber(user.getUsername());
			loanBill.setCreateBy(user.getLoginName());
			loanBill.setCreateDate(new Date());
			ebillMessageService.saveEBillMessage(billInfo.getBillNo(), 
					ReceiptConstant.BILL_MSG_OP_TYPE_CREATE, 
					ReceiptConstant.BILL_MSG_INFO_CREATE,user);
			if(billInfo.getCorpId().indexOf(";")>-1){
				String[] corpIds=billInfo.getCorpId().split(";");
				ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
						corpIds[0], 1,user);
				ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
						corpIds[1], 1,user);
				loanBill.setCorpId(corpIds[0]);
				ENoticeBill loanBillCorp2=new ENoticeBill();
				BeanUtils.copyProperties(loanBill, loanBillCorp2);
				loanBillCorp2.setCorpId(corpIds[1]);
				eNoticeBillDao.save(loanBill);
				eNoticeBillDao.save(loanBillCorp2);
			}else{
				ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
						billInfo.getCorpId(), 1,user);
				eNoticeBillDao.save(loanBill);
			}
			eNoticeBillDao.flush();
		}
	}
}
