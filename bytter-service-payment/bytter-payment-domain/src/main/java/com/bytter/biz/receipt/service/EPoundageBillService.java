package com.bytter.biz.receipt.service;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.receipt.dao.EPoundageBillDao;
import com.bytter.biz.receipt.entity.EPoundageBill;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.EAccrualBillVO;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.biz.receipt.vo.EPoundageBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Title: Service
* @Description: 手续费电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:30:13
* @version 1.0
*
*/
@Service("ePoundageBillService")
@Transactional
public class EPoundageBillService extends BaseService<EPoundageBill,String> {
	@Autowired
	private EPoundageBillDao ePoundageBillDao;
	@Autowired
	private EBillPrintService ebillPrintService;

	@Autowired
	private EBillMessageService ebillMessageService;
	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	/**
	* 保存
	* @param entity
	*/
 	public void save(EPoundageBill entity){
 		super.saveEntity(entity);
 	}
 	@Override
	public BaseDao<EPoundageBill, String> getEntityDao() {
		return ePoundageBillDao;
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
			String accAuthSql = SqlUtil.getInSql(accountGrantList, "A.PAYER_ACC", true);
			commonSql.append(" and ").append(accAuthSql);
		}
		DataTablesOutput output = ePoundageBillDao.listForUnion(input,commonSql.toString());
		List<EPoundageBillVO> list=output.getData();
		if(list.size()>0) {
			List<DictDTO> dictList = dictService.list("eVisaType.billType");
			Map<String, String> dictMap = dictList.stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
			Map<String, String> currencyMap = btCurrencyService.getCurrencyNameMap();
			for(EPoundageBillVO vo:list){
				vo.setBillTypeName(dictMap.get(vo.getBillType()));//电子回单类型
				vo.setCurName(currencyMap.get(vo.getCurId()));//币别
				String corpName=iCorpService.getCorpName(vo.getCorpId());
				vo.setCorpName(corpName);
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
	public EPoundageBillVO getVOByBillNo(String billNo, String id){
		return ePoundageBillDao.getVOByBillNo(billNo,id);
	}
	/**
	 * 创建手续费电子回单
	 * @param orderNo 流水号
	 * @param voucherNo 凭证号
	 * @param origVoucherNo 原凭证号
	 * @param affixAmount 附件张数
	 * @param tallyDate 记账日期
	 * @param interStart 起息日期
	 * @param voucherStatus 凭证状态
	 * @param debitAcc 借方账号
	 * @param creditAcc 贷方账号
	 * @param operatorCode 操作员
	 * @param checkerCode 复核员
	 */
	public void createPoundageBill(String orderNo,String voucherNo,
			String origVoucherNo,Integer affixAmount,
			Date tallyDate,Date interStart,String voucherStatus,
			String debitAcc,String creditAcc,
			String operatorCode,String checkerCode, ICurrentUser user){

		String[] blnctrAcc={debitAcc,creditAcc};
		if(StringUtils.isEmpty(blnctrAcc)||blnctrAcc.length!=2){
			return;
		}
		List<ELoanBillInfoVo> fcAccInfoList = ePoundageBillDao.listFcAccByAcc(blnctrAcc);
		ELoanBillInfoVo debitAccInfo=null;
		ELoanBillInfoVo creditAccInfo=null;
		ELoanBillInfoVo billInfo=new ELoanBillInfoVo();
		billInfo.setDebitAcc(debitAcc);
		billInfo.setCreditAcc(creditAcc);
		for(ELoanBillInfoVo fcAccInfo:fcAccInfoList){
			if(debitAcc.equals(fcAccInfo.getAccNo())){
				debitAccInfo=fcAccInfo;
				billInfo.setDebitSubjec(debitAccInfo.getSysSubjectCode());
				billInfo.setDebitSubjecName(debitAccInfo.getSysSubjectCodeName());
				billInfo.setCur(debitAccInfo.getCur());
				billInfo.setCorpId(debitAccInfo.getCorpId());
				billInfo.setCorpName(debitAccInfo.getCorpName());
				billInfo.setDebitNetName(debitAccInfo.getNetName());
				billInfo.setLoanDate(debitAccInfo.getLoanDate());
				billInfo.setLoanRate(debitAccInfo.getLoanRate());
				billInfo.setContractCode(debitAccInfo.getContractCode());
				billInfo.setDebitAccName(debitAccInfo.getAccName());
			}else if(creditAcc.equals(fcAccInfo.getAccNo())){
				creditAccInfo=fcAccInfo;
				billInfo.setCreditSubjec(creditAccInfo.getSysSubjectCode());
				billInfo.setCreditSubjecName(creditAccInfo.getSysSubjectCodeName());
				billInfo.setCreditCorpName(creditAccInfo.getCorpName());
				billInfo.setCreditNetName(creditAccInfo.getNetName());
				billInfo.setCreditAccName(creditAccInfo.getAccName());
			}
		}
		if(debitAccInfo==null||creditAccInfo==null){
			return;
		}
		//手续费没有开户行，这取账号所在的单位名称
		billInfo.setPayerBank(billInfo.getCorpName());
		billInfo.setPayeeBank(billInfo.getCreditCorpName());

		List<ELoanBillInfoVo> accbookBlnedList = ePoundageBillDao.listAccbookBlnedAcc(orderNo,blnctrAcc);
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
			if(StringUtils.isEmpty(billInfo.getVoucherName())){
				billInfo.setVoucherName(accbookInfo.getVoucherName());
			}
		}
		if(debitBookInfo==null||creditBookInfo==null){
			return;
		}


		if(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())
				&&(SubjectConstant.SysSubject.INTEREST_BANK_CURRENT_DEPOSIT.getValue().equals(billInfo.getCreditSubjec())
				||SubjectConstant.SysSubject.WAIT_AMORTIZE_COST.getValue().equals(billInfo.getCreditSubjec())
				||SubjectConstant.SysSubject.PROCEDURE_FEE_INCOME.getValue().equals(billInfo.getCreditSubjec())
				||SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getCreditSubjec()))
			){
//			--付款通知单
//			StringBuilder debitSubjectName=new StringBuilder("企业活期_");
			StringBuilder debitSubjectName=new StringBuilder(billInfo.getDebitSubjecName());
			debitSubjectName.append(billInfo.getDebitAcc());
			billInfo.setDebitSubjecName(debitSubjectName.toString());
//			StringBuilder creditSubjectName=new StringBuilder("银行活期_");
			StringBuilder creditSubjectName=new StringBuilder(billInfo.getCreditSubjecName());
			creditSubjectName.append(billInfo.getCreditAcc());
			billInfo.setCreditSubjecName(creditSubjectName.toString());

			billInfo.setMoney(billInfo.getCreditMoney());
		}else{
			return;
		}

		//单位为空不生成电子回单
		if(StringUtils.isEmpty(billInfo.getCorpId())){
			return;
		}
		billInfo.setVoucherNo(voucherNo);

		//如果是作废凭证则判断是否已经生成电子回单修改旧状态和设置日志信息
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("voucherNo", voucherNo);
		params.put("notStatus", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", DateUtils.getDate());
		ELoanBillInfoVo printInfo=ePoundageBillDao.getPrintInfoByVoucherNo(params);
		if(printInfo!=null){
			if(FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)){
			if(printInfo.getPrints()>1&&printInfo.getPrints2()>1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				ePoundageBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
						ReceiptConstant.BILL_MSG_INFO_CANCELL,user);
			}else if(printInfo.getPrints()==1&&printInfo.getPrints2()>1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				ePoundageBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
						ReceiptConstant.BILL_MSG_INFO_CANCELL1,user);
			}else if(printInfo.getPrints()>1&&printInfo.getPrints2()==1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				ePoundageBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
						ReceiptConstant.BILL_MSG_INFO_CANCELL2,user);
			}else if(printInfo.getPrints()==1&&printInfo.getPrints2()==1){
				ePoundageBillDao.deleteELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
						ReceiptConstant.BILL_MSG_OP_TYPE_DELETE,
						ReceiptConstant.BILL_MSG_INFO_DELETE,user);
			}
			}
		}else if(!FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)){
			billInfo.setBillNo(SequenceSingleton.getSequence(SequenceEnum.E_POUNDAGE_BILL_NO_P));
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
			EPoundageBill loanBill=new EPoundageBill();
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
			loanBill.setPayType(billInfo.getVoucherName());
			loanBill.setCheckerCode(checkerCode);
			loanBill.setAccrualDate(interStart);
			loanBill.setTallyDate(tallyDate);
			loanBill.setVoucherNo(voucherNo);
			loanBill.setEncryDate(new Date());
			loanBill.setRandom(billInfo.getRandom());
			loanBill.setEncrySource(billInfo.getEncrySource());
			loanBill.setValidateCode(billInfo.getValidcode());
			loanBill.setStatus(ReceiptConstant.LOAN_BILL_STATUS_INPUT);
			loanBill.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_11);
			loanBill.setOldVoucher(origVoucherNo);
			loanBill.setTallierCode(operatorCode);
			loanBill.setDebit(billInfo.getDebitSubjecName());
			loanBill.setCredit(billInfo.getCreditSubjecName());
			loanBill.setPrints(1);
			loanBill.setAccessory(affixAmount);
			loanBill.setSubscriber(user.getUsername());
			loanBill.setCreateBy(user.getLoginName());
			loanBill.setCreateDate(new Date());
			ePoundageBillDao.save(loanBill);
			ebillMessageService.saveEBillMessage(billInfo.getBillNo(),
					ReceiptConstant.BILL_MSG_OP_TYPE_CREATE,
					ReceiptConstant.BILL_MSG_INFO_CREATE,user);
			ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
					billInfo.getCorpId(), 1,user);
			ePoundageBillDao.flush();
		}
	}
}
