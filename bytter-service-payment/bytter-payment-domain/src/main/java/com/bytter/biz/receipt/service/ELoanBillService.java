package com.bytter.biz.receipt.service;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.dto.VoucherWrapperDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.receipt.dao.ELoanBillDao;
import com.bytter.biz.receipt.entity.ELoanBill;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.biz.receipt.vo.ELoanBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Title: Service
* @Description: 贷款电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:30:01
* @version 1.0
*
*/
@Service("eLoanBillService")
@Transactional
public class ELoanBillService extends BaseService<ELoanBill,String> {
	@Autowired
	private ELoanBillDao eLoanBillDao;
	@Autowired
	private EBillPrintService ebillPrintService;
	
	@Autowired
	private EBillMessageService ebillMessageService;
	@DubboReference(check = false)
	private IFcAccbookService fcAccbookService;
	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	/**
	* 保存
	* @param entity
	*/
 	public void save(ELoanBill entity){
 		super.saveEntity(entity);
 	}
	@Override
	public BaseDao<ELoanBill, String> getEntityDao() {
		return eLoanBillDao;
	}
	/**
	 * 查询贷款电子回单表
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
			String accAuthSql = SqlUtil.getInSql(accountGrantList, "A.DEPOSIT_ACC", true);
			commonSql.append(" and ").append(accAuthSql);
		}
		DataTablesOutput output = eLoanBillDao.listForUnion(input,commonSql.toString());
		List<ELoanBillVO> list=output.getData();
		if(list.size()>0) {
			List<DictDTO> dictList = dictService.list("eVisaType.billType");
			Map<String, String> dictMap = dictList.stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
			Map<String, String> currencyMap = btCurrencyService.getCurrencyNameMap();
			for(ELoanBillVO vo:list){
				vo.setBillTypeName(dictMap.get(vo.getBillType()));//电子回单类型
				vo.setCurName(currencyMap.get(vo.getCurId()));//币别
				String loanTypeName=dictService.getByName("eLoanBill.loanType",vo.getLoanType());
				vo.setLoanTypeName(loanTypeName);
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
	public ELoanBillVO getVOByBillNo(String billNo, String id){
		return eLoanBillDao.getVOByBillNo(billNo,id);
	}
	/**
	 * 创建贷款凭证电子回单
	 * @param orderNo 流水号
	 * @param voucherNo 凭证号
	 * @param tallyDate 记账日期
	 * @param voucherStatus 凭证状态
	 * @param debitAcc 借方账号
	 * @param creditAcc 贷方账号
	 * @param operatorCode 操作员
	 * @param checkerCode 复核员
	 * @param money 金额
	 */
	public void createLoanBill(String orderNo,String voucherNo,
			Date tallyDate,Date accrualDate,String voucherStatus,
			String debitAcc,String creditAcc,
			String operatorCode,String checkerCode,
			BigDecimal money, String origVoucherNo, ICurrentUser user){
		
		String[] blnctrAcc={debitAcc,creditAcc};
		if(StringUtils.isEmpty(blnctrAcc)||blnctrAcc.length!=2){
			return;
		}
		List<ELoanBillInfoVo> fcAccInfoList = eLoanBillDao.listFcAccByAcc(blnctrAcc);
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
				billInfo.setCorpId(debitAccInfo.getCorpId());
				billInfo.setLoanDate(debitAccInfo.getLoanDate());
				billInfo.setLoanRate(debitAccInfo.getLoanRate());
				billInfo.setContractCode(debitAccInfo.getContractCode());
			}else if(fcAccInfo.getAccNo().equals(creditAcc)){
				creditAccInfo=fcAccInfo;
				billInfo.setCreditSubjec(creditAccInfo.getSysSubjectCode());
				billInfo.setCreditSubjecName(creditAccInfo.getSysSubjectCodeName());
			}
		}
		if(debitAccInfo==null||creditAccInfo==null){
			return;
		}
		String loanType=null;
		if(((SubjectConstant.SysSubject.ENTERPRISE_LOAN.getValue().equals(billInfo.getDebitSubjec())
				||SubjectConstant.SysSubject.ENTERPRISE_LOAN_LONG_TERM.getValue().equals(billInfo.getDebitSubjec()))
				&&SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getCreditSubjec()))
			||
			((SubjectConstant.SysSubject.ENTERPRISE_LOAN.getValue().equals(billInfo.getDebitSubjec())
					||SubjectConstant.SysSubject.ENTERPRISE_LOAN_LONG_TERM.getValue().equals(billInfo.getDebitSubjec()))
					&&SubjectConstant.SysSubject.AB_RECEIVE_LOAN.getValue().equals(billInfo.getCreditSubjec()))
				){
			StringBuilder creditAccName=new StringBuilder(billInfo.getCreditSubjecName());
			creditAccName.append("_").append(billInfo.getCreditAcc());
			StringBuilder debitAccName=new StringBuilder(billInfo.getDebitSubjecName());
			debitAccName.append("_").append(billInfo.getDebitAcc());
			billInfo.setCreditAccName(creditAccName.toString());
			billInfo.setDebitAccName(debitAccName.toString());
			
			billInfo.setCur(debitAccInfo.getCur());
			billInfo.setCorpId(debitAccInfo.getCorpId());
			billInfo.setCorpName(debitAccInfo.getCorpName());
			billInfo.setLoanDate(debitAccInfo.getLoanDate());
			billInfo.setLoanRate(debitAccInfo.getLoanRate());
			// 还款日期应该取记账凭证的记账日期
			billInfo.setReturnDate(tallyDate);
			billInfo.setContractCode(debitAccInfo.getContractCode());
			billInfo.setLoanCorp(debitAccInfo.getAccName());
			
			billInfo.setCorpName(billInfo.getLoanCorp());
			loanType=ReceiptConstant.BILL_LOAN_TYPE_1;
		}else if(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())
				&&(SubjectConstant.SysSubject.ENTERPRISE_LOAN.getValue().equals(billInfo.getCreditSubjec())
						||SubjectConstant.SysSubject.ENTERPRISE_LOAN_LONG_TERM.getValue().equals(billInfo.getCreditSubjec()))){
			StringBuilder creditAccName=new StringBuilder(billInfo.getCreditSubjecName());
			creditAccName.append("_").append(billInfo.getCreditAcc());
			StringBuilder debitAccName=new StringBuilder(billInfo.getDebitSubjecName());
			debitAccName.append("_").append(billInfo.getDebitAcc());
			
			billInfo.setCreditAccName(creditAccName.toString());
			billInfo.setDebitAccName(debitAccName.toString());
			
			billInfo.setCur(creditAccInfo.getCur());
			billInfo.setCorpId(creditAccInfo.getCorpId());
			billInfo.setCorpName(creditAccInfo.getCorpName());
			billInfo.setLoanDate(creditAccInfo.getLoanDate());
			billInfo.setLoanRate(creditAccInfo.getLoanRate());
			billInfo.setReturnDate(creditAccInfo.getReturnDate());
			billInfo.setContractCode(creditAccInfo.getContractCode());
			loanType=ReceiptConstant.BILL_LOAN_TYPE_2;
		}
		billInfo.setDebitMoney(money);
		
		//单位为空不生成电子回单
		if(StringUtils.isEmpty(billInfo.getCorpId())){
			return;
		}
		billInfo.setVoucherNo(voucherNo);
		billInfo.setOrigVoucherNo(origVoucherNo);
	
		//如果是作废凭证则判断是否已经生成电子回单修改旧状态和设置日志信息
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("voucherNo", voucherNo);
		params.put("notStatus", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", DateUtils.getDate());
		ELoanBillInfoVo printInfo=eLoanBillDao.getPrintInfoByVoucherNo(params);
		if(printInfo!=null){
			if(FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)){
			if(printInfo.getPrints()>1&&printInfo.getPrints2()>1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				eLoanBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
						ReceiptConstant.BILL_MSG_INFO_CANCELL,user);
			}else if(printInfo.getPrints()==1&&printInfo.getPrints2()>1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				eLoanBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
						ReceiptConstant.BILL_MSG_INFO_CANCELL1,user);
			}else if(printInfo.getPrints()>1&&printInfo.getPrints2()==1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				eLoanBillDao.updateELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
						ReceiptConstant.BILL_MSG_INFO_CANCELL2,user);
			}else if(printInfo.getPrints()==1&&printInfo.getPrints2()==1){
				eLoanBillDao.deleteELoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_DELETE,
						ReceiptConstant.BILL_MSG_INFO_DELETE,user);
			}
			}
		}else if(!FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)){
			billInfo.setBillNo(SequenceSingleton.getSequence(SequenceEnum.E_LOAN_BILL_NO_L));
			billInfo.setRandom(ReceiptUtils.getBillRandom(billInfo.getBillNo()));
			billInfo.setValidcode(
					ReceiptUtils.getBillValidcode(
							billInfo.getDebitAcc(), 
							null,
							billInfo.getDebitMoney(),
							null,
							billInfo.getRandom()));
			billInfo.setEncrySource(ReceiptUtils.getBillEncrySource(billInfo.getValidcode()));
			//非作废凭证进行入库
			ELoanBill loanBill=new ELoanBill();
			loanBill.setCorpId(billInfo.getCorpId());
			loanBill.setBillNo(billInfo.getBillNo());
			loanBill.setLoanCorp(billInfo.getCorpName());
			loanBill.setLoanRate(billInfo.getLoanRate());
			loanBill.setLoanDate(billInfo.getLoanDate());
			loanBill.setCurId(billInfo.getCur());
			if (loanType.equals(ReceiptConstant.BILL_LOAN_TYPE_1)) {//贷款取贷方账号
				loanBill.setDepositAcc(billInfo.getDebitAcc());
			} else {//还款取借方账号
				loanBill.setDepositAcc(billInfo.getCreditAcc());
			}
			loanBill.setContractCode(billInfo.getContractCode());
			loanBill.setReturnDate(tallyDate);
			loanBill.setLoanMoney(billInfo.getDebitMoney());
			loanBill.setDebitAcc(billInfo.getDebitAccName());
			loanBill.setVoucherNo(voucherNo);
			loanBill.setOldVoucher(origVoucherNo);
			loanBill.setCreditAcc(billInfo.getCreditAccName());
			loanBill.setStatus(ReceiptConstant.LOAN_BILL_STATUS_INPUT);
			loanBill.setPrints(1);
			loanBill.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_07);
			loanBill.setValidateCode(billInfo.getValidcode());
			loanBill.setEncrySource(billInfo.getEncrySource());
			loanBill.setSubscriber(user.getUsername());
			loanBill.setEncryDate(new Date());
			loanBill.setRandom(billInfo.getRandom());
			loanBill.setTallyDate(tallyDate);
			loanBill.setAccrualDate(accrualDate);
			loanBill.setTallyerCode(operatorCode);
			loanBill.setCheckerCode(checkerCode);
			loanBill.setLoanType(loanType);
			loanBill.setCreateBy(user.getLoginName());
			loanBill.setCreateDate(new Date());
			eLoanBillDao.save(loanBill);
			ebillMessageService.saveEBillMessage(billInfo.getBillNo(), 
					ReceiptConstant.BILL_MSG_OP_TYPE_CREATE,
					ReceiptConstant.BILL_MSG_INFO_CREATE,user);
			ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
					billInfo.getCorpId(), 1,user);
			eLoanBillDao.flush();
		}
	}
	/**
	 * 设置还款金额
	 * @param eLoanBillVO
	 */
	public void setReturnMoney(ELoanBillVO eLoanBillVO){
		//获取凭证号
		String voucherNo = eLoanBillVO.getVoucherNo();
		//默认为贷款金额
		BigDecimal returnMoney = eLoanBillVO.getLoanMoney();
		if(EmptyUtils.isNotEmpty(voucherNo)){
			VoucherWrapperDTO voucherWrapperDTO = fcAccbookService.getVoucherByVoucherNo(voucherNo);
			if(!EmptyUtils.isEmpty(voucherWrapperDTO)){
				List<FcAccbookVoucherDTO>  list = voucherWrapperDTO.getTarget();
				if(EmptyUtils.isNotEmpty(list)){
					for(FcAccbookVoucherDTO dto : list){
						List<FcAccbookDtlDTO> dtlList = dto.getBookDtls();
						if(EmptyUtils.isNotEmpty(dtlList)){
							for(FcAccbookDtlDTO dtlDTO : dtlList){
								FcAccDTO fcAccDTO = innerAccountService.getByBlnctrAcc(dtlDTO.getAccount());
								if(!EmptyUtils.isEmpty(fcAccDTO) && fcAccDTO.getSysSubjectCode().equals(SubjectConstant.SysSubject.ENTERPRISE_LOAN.getValue())){
									returnMoney = dtlDTO.getDebitMoney().add(dtlDTO.getCreditMoney());
								}
							}
						}
					}
				}
			}
		}
		eLoanBillVO.setReturnMoney(returnMoney);
	}
}
