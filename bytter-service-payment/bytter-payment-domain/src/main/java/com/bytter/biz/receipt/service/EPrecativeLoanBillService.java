package com.bytter.biz.receipt.service;

import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.service.IReceiptQueryService;
import com.bytter.biz.finacial.api.vo.ELoanBillInfoVo;
import com.bytter.biz.receipt.dao.EPrecativeLoanBillDao;
import com.bytter.biz.receipt.entity.EPrecativeLoanBill;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.EPrecativeLoanBillVO;
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
import com.bytter.finance.api.ICmsPrecativeLoanSendService;
import com.bytter.finance.dto.CmsPrecativeLoanRepayDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.vo.FcAccVO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: 委托贷款电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:30:40
* @version 1.0
*
*/
@Service("ePrecativeLoanBillService")
@Transactional
public class EPrecativeLoanBillService extends BaseService<EPrecativeLoanBill,String> {
	@Autowired
	private EPrecativeLoanBillDao ePrecativeLoanBillDao;
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
	private ICmsPrecativeLoanSendService iCmsPrecativeLoanSendService;
	@DubboReference(check = false)
	private IReceiptQueryService iReceiptQueryService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
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
 	public void save(EPrecativeLoanBill entity){
 		super.saveEntity(entity);
 	}
	@Override
	public BaseDao<EPrecativeLoanBill, String> getEntityDao() {
		return ePrecativeLoanBillDao;
	}
	/**
	 * 查询利息清单电子回单
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
			String accAuthSql = SqlUtil.getInSql(accountGrantList, "A.PRECATIVE_ACC", true);
			commonSql.append(" and ").append(accAuthSql);
		}
		DataTablesOutput output = ePrecativeLoanBillDao.listForUnion(input,commonSql.toString());
		
		List<EPrecativeLoanBillVO> voList=output.getData();
		List<EPrecativeLoanBillVO> billVoList=new ArrayList<EPrecativeLoanBillVO>();
		List<DictDTO> list = dictService.list("eVisaType.billType");
		Map<String, String> dictMap = list.stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
		List<String> corps = voList.stream().map(EPrecativeLoanBillVO::getCorpId).collect(Collectors.toList());
		List<String> corps2 = corps.stream().collect(Collectors.toList());//受托人
		List<String> precativeCorps = voList.stream().map(EPrecativeLoanBillVO::getPrecativeCorp).collect(Collectors.toList());
		List<String> precativeCorps2 = precativeCorps.stream().collect(Collectors.toList());//委托人 
		List<String> loanCorps = voList.stream().map(EPrecativeLoanBillVO::getLoanCorp).collect(Collectors.toList());
		List<String> loanCorps2 = loanCorps.stream().collect(Collectors.toList());//借款人
		Map<String, String> corpMap = iCorpService.getNameByIds(corps2);
		Map<String, String> precativeCorpMap = iCorpService.getNameByIds(precativeCorps2);
		Map<String, String> loanCorpsMap = iCorpService.getNameByIds(loanCorps2);
		Map<String, String> currencyMap = btCurrencyService.getCurrencyNameMap();
		
		List<String> billCodes = new ArrayList<String>();
		for(EPrecativeLoanBillVO vo:voList) {
			billCodes.add(vo.getOldVoucher());
		}
		List<CmsPrecativeLoanRepayDTO> listRepay = iCmsPrecativeLoanSendService.queryRepayListByBillCodes(billCodes);
		Map<String, CmsPrecativeLoanRepayDTO> eepayMap = listRepay.stream().collect(Collectors.toMap(CmsPrecativeLoanRepayDTO::getBillCode, Function.identity(),(key1,key2) -> key2));
		for(EPrecativeLoanBillVO vo:voList) {
			  vo.setBillTypeName(dictMap.get(vo.getBillType()));//电子回单类型
			  vo.setCurName(currencyMap.get(vo.getCurId()));//币别 
			  vo.setCorpName(corpMap.get(vo.getCorpId()));//受托人
			  //委托人          precativeCorp  字段数据为ID
			  //借款人		    loanCorp    字段数据为ID
			  //还款日期:       repaymentDate   字段数据为空
//			  vo.setPrecativeCorp(precativeCorpMap.get(vo.getPrecativeCorp()));
//			  vo.setLoanCorp(loanCorpsMap.get(vo.getLoanCorp()));
			if(ObjectUtils.isEmpty(vo.getRepaymentDate())) {
				if(eepayMap.containsKey(vo.getOldVoucher())) {
					vo.setRepaymentDate(eepayMap.get(vo.getOldVoucher()).getRepayDate());//还款日期
				}
			}
			String precativeAcc = vo.getPrecativeAcc();
			String loanAcc = vo.getLoanAcc();
			List<String> query = Lists.newArrayList();
			query.add(precativeAcc);
			query.add(loanAcc);
			List<FcAccDTO> accSet = iFcAccService.getAccSet(query);
			if (null != accSet && !accSet.isEmpty()) {
				accSet.stream().forEach(it -> {
					if (it.getBlnctrAcc().equals(vo.getPrecativeAcc())) {
						vo.setPrecativeCorp(it.getAccName());
					}
					if (it.getBlnctrAcc().equals(vo.getLoanAcc())) {
						vo.setLoanCorp(it.getAccName());
					}
				});
			}
			billVoList.add(vo);
		}
		output.setData(billVoList);
		return output;
	}
	/**
	 * 根据单据号获取VO
	 * 参数至少有一个有值
	 * @param billNo 单据号 可空
	 * @param id 流水号  可空
	 * @return
	 */
	public EPrecativeLoanBillVO getVOByBillNo(String billNo, String id){
		EPrecativeLoanBillVO billVO = ePrecativeLoanBillDao.getVOByBillNo(billNo, id);
		if (null != billVO){
			String precativeAcc = billVO.getPrecativeAcc();
			String loanAcc = billVO.getLoanAcc();
			List<String> query = Lists.newArrayList();
			query.add(precativeAcc);
			query.add(loanAcc);
			List<FcAccDTO> accSet = iFcAccService.getAccSet(query);
			if (StringUtils.isNotBlank(billVO.getCorpId()) ){
				CorpDTO corp =iCorpService.get(billVO.getCorpId());
				billVO.setCorpName(corp.getName());
				billVO.setNetId(corp.getNetId());
			}

			if (null != accSet && !accSet.isEmpty()  ){
				accSet.stream().forEach( it ->{
					if (it.getBlnctrAcc().equals(billVO.getPrecativeAcc())){
						billVO.setPrecativeCorp(it.getAccName());
					}
					if (it.getBlnctrAcc().equals(billVO.getLoanAcc())){
						billVO.setLoanCorp(it.getAccName());
					}
				});
			}

		}

		return billVO;
	}
	/**
	 * 创建委托贷款或者委托贷款还款电子回单
	 * @param voucherNo 凭证号
	 * @param tallyDate 记账日期
	 * @param voucherStatus 凭证状态
	 * @param operatorCode 操作员
	 * @param checkerCode 复核员
	 * @param busGlobalId 全局唯一ID
	 * @param busOriType 贷款类型 46 47
	 */
	public void createPrecativeBill(String voucherNo,
			Date tallyDate,String voucherStatus,
			String operatorCode,String checkerCode,
			String busGlobalId,Integer busOriType, String origVoucherNo, ICurrentUser user){
		
		if(!checkPrecativeLoan(busOriType, voucherNo, busGlobalId)){
			return;
		}
		//查询记账凭证明细信息设置电子回单要素
		List<ELoanBillInfoVo> accbookBlnedList = iReceiptQueryService.listFcAccbookBlnedSubjectBybusGlobalId(busGlobalId);
		ELoanBillInfoVo loanBillInfo=new ELoanBillInfoVo();
		for(ELoanBillInfoVo accbookBlned:accbookBlnedList){
			FcAccVO fcacc = iFcAccService.getFcAccVOByBlnctrAcc(accbookBlned.getAccount());
			accbookBlned.setSysSubjectCode(fcacc.getSysSubjectCode());
			accbookBlned.setSysSubjectCodeName(fcacc.getSubjectName());
			accbookBlned.setNetId(fcacc.getNetId());
			accbookBlned.setNetName(fcacc.getNetName());
			accbookBlned.setCorpId(fcacc.getCorpId());
			accbookBlned.setCorpName(fcacc.getCorpName());
			accbookBlned.setAccName(fcacc.getAccName());
			accbookBlned.setLoanDate(fcacc.getAccStartDate());
			accbookBlned.setLoanRate(fcacc.getInterestRate());
			accbookBlned.setDeadlineDate(fcacc.getDeadlineDate());
			accbookBlned.setContractCode(fcacc.getContractNo());
			accbookBlned.setCur(fcacc.getCurId());
			accbookBlned.setCurName(fcacc.getCurName());
			/**
			 * 委托贷款分录
			 * 1.借：企业活期存款 （委托方）
			 * 2.贷：委托存款 （新开户）
			 * 3.借：委托贷款 （新开户）
			 * 4.贷：企业活期存款（贷款方）
			 * 委托贷款还款分录（无利息四笔分录，有利息是六笔分录）
			 * 1.借：企业活期存款 （贷款方）
			 * 2.贷：委托贷款 （新开户）
			 * 3.借：委托存款 （新开户）
			 * 4.贷：企业活期存款（委托方）
			 * 只还利息（两笔分录）
			 * 借：企业活期存款（贷款方）
			 * 贷：企业活期存款（委托方）
			 */
			if(FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.getValue().equals(busOriType)){
				//46
				if(accbookBlned.getDebitMoney().doubleValue()!=0
						&& SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())){
					loanBillInfo.setDebitAcc(accbookBlned.getAccount());
					loanBillInfo.setDebitSubjec(accbookBlned.getSysSubjectCode());
					loanBillInfo.setDebitSubjecName(accbookBlned.getSysSubjectCodeName());
				}else if(accbookBlned.getCreditMoney().doubleValue()!=0
						&& SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())){
					loanBillInfo.setCreditAcc(accbookBlned.getAccount());
					loanBillInfo.setCreditSubjec(accbookBlned.getSysSubjectCode());
					loanBillInfo.setCreditSubjecName(accbookBlned.getSysSubjectCodeName());
				}
			}else if(FinaBookConstant.BusOriType.NB_PRECATIVE_REPAYMENT.getValue().equals(busOriType)){
				//47
				if(accbookBlnedList.size() == 2){
					if(accbookBlned.getDebitMoney().doubleValue()!=0
							&& SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())){
						loanBillInfo.setCreditAcc(accbookBlned.getAccount());
						loanBillInfo.setCreditInterestSubject(accbookBlned.getSysSubjectCode());
						loanBillInfo.setCreditInterestSubjectName(accbookBlned.getSysSubjectCodeName());
					}else if(accbookBlned.getCreditMoney().doubleValue()!=0
							&& SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())){
						loanBillInfo.setDebitAcc(accbookBlned.getAccount());
						loanBillInfo.setDebitInterestSubject(accbookBlned.getSysSubjectCode());
						loanBillInfo.setDebitInterestSubjectName(accbookBlned.getSysSubjectCodeName());
					}
				}else if(accbookBlnedList.size() >= 4){
					if(accbookBlned.getDebitMoney().doubleValue()!=0
							&& SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())){
						loanBillInfo.setCreditAcc(accbookBlned.getAccount());
						loanBillInfo.setCreditSubjec(accbookBlned.getSysSubjectCode());
						loanBillInfo.setCreditSubjecName(accbookBlned.getSysSubjectCodeName());
					}else if(accbookBlned.getCreditMoney().doubleValue()!=0
							&& SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())){
						loanBillInfo.setDebitAcc(accbookBlned.getAccount());
						loanBillInfo.setDebitSubjec(accbookBlned.getSysSubjectCode());
						loanBillInfo.setDebitSubjecName(accbookBlned.getSysSubjectCodeName());
					}
					if(accbookBlnedList.size() == 6){
						if(accbookBlned.getDebitMoney().doubleValue()!=0
								&& SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())){
							loanBillInfo.setCreditInterestSubject(accbookBlned.getSysSubjectCode());
							loanBillInfo.setCreditInterestSubjectName(accbookBlned.getSysSubjectCodeName());
						}else if(accbookBlned.getCreditMoney().doubleValue()!=0
								&& SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())){
							loanBillInfo.setDebitInterestSubject(accbookBlned.getSysSubjectCode());
							loanBillInfo.setDebitInterestSubjectName(accbookBlned.getSysSubjectCodeName());
						}
					}
				}
			}
			if(SubjectConstant.SysSubject.INTEREST_ENTURST_LOANS.getValue().equals(accbookBlned.getSysSubjectCode())){//委托贷款
				loanBillInfo.setLoanSubject(accbookBlned.getSysSubjectCode());//委托贷款科目
				loanBillInfo.setLoanSubjectName(accbookBlned.getSysSubjectCodeName());//委托贷款科目名称
				loanBillInfo.setLoanAcc(accbookBlned.getAccount());//委托贷款账号
				loanBillInfo.setLoanCorp(accbookBlned.getCorpName());//委托贷款单位
				if(FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.getValue().equals(busOriType)){//46
					loanBillInfo.setLoanMoney((accbookBlned.getDebitMoney().add(accbookBlned.getCreditMoney())));
				} else if(FinaBookConstant.BusOriType.NB_PRECATIVE_REPAYMENT.getValue().equals(busOriType)){//47
					//去委托贷款合同的还款本金，根据voucherNo, busGlobalId获取委托贷款还款单据的本金
					loanBillInfo.setLoanMoney((accbookBlned.getDebitMoney().add(accbookBlned.getCreditMoney())));
				}
				loanBillInfo.setCur(accbookBlned.getCur());
				loanBillInfo.setCurName(accbookBlned.getCurName());
				loanBillInfo.setCorpId(accbookBlned.getNetId());//此处单位id是贷款受托人，因此取网点名称
				loanBillInfo.setCorpName(accbookBlned.getNetName());
				loanBillInfo.setLoanDate(accbookBlned.getLoanDate());
				loanBillInfo.setLoanRate(accbookBlned.getLoanRate());
				loanBillInfo.setDeadlineDate(accbookBlned.getDeadlineDate());
				loanBillInfo.setContractCode(accbookBlned.getContractCode());
			}
			if(SubjectConstant.SysSubject.INTEREST_ENTUST_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())){//委托存款
				loanBillInfo.setPrecativeSubject(accbookBlned.getSysSubjectCode());//委托存款科目
				loanBillInfo.setPrecativeSubjectName(accbookBlned.getSysSubjectCodeName());//委托存款科目名称
				loanBillInfo.setPrecativeAcc(accbookBlned.getAccount());//委托存款账号
				loanBillInfo.setPrecativeCorp(accbookBlned.getCorpName());//委托存款单位
				/*loanBillInfo.setLoanMoney((accbookBlned.getDebitMoney().add(accbookBlned.getCreditMoney())));
				loanBillInfo.setCur(accbookBlned.getCur());
				loanBillInfo.setCurName(accbookBlned.getCurName());
				loanBillInfo.setCorpId(accbookBlned.getCorpId());
				loanBillInfo.setCorpName(accbookBlned.getCorpName());
				loanBillInfo.setLoanDate(accbookBlned.getLoanDate());
				loanBillInfo.setLoanRate(accbookBlned.getLoanRate());
				loanBillInfo.setDeadlineDate(accbookBlned.getDeadlineDate());
				loanBillInfo.setContractCode(accbookBlned.getContractCode());*/
			}
		}
		//单位为空不生成电子回单
		if(StringUtils.isEmpty(loanBillInfo.getCorpId())){
			return;
		}
		
		if(FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.getValue().equals(busOriType)){
			StringBuilder debitAccName=new StringBuilder(loanBillInfo.getDebitSubjecName());
			debitAccName.append("_").append(loanBillInfo.getDebitAcc());
			loanBillInfo.setDebitAccName(debitAccName.toString());
			
			StringBuilder creditAccName=new StringBuilder(loanBillInfo.getCreditSubjecName());
			creditAccName.append("_").append(loanBillInfo.getCreditAcc());
			loanBillInfo.setCreditAccName(creditAccName.toString());
			//委托存款-分录
			StringBuilder precativeAccName = new StringBuilder(loanBillInfo.getPrecativeSubjectName());
			precativeAccName.append("_").append(loanBillInfo.getPrecativeAcc());
			loanBillInfo.setPrecativeAccName(precativeAccName.toString());
			//委托贷款-分录
			StringBuilder loanAccName = new StringBuilder(loanBillInfo.getLoanSubjectName());
			loanAccName.append("_").append(loanBillInfo.getLoanAcc());
			loanBillInfo.setLoanAccName(loanAccName.toString());
			loanBillInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_21);
			loanBillInfo.setRepaymentDate(null);
		}else if(FinaBookConstant.BusOriType.NB_PRECATIVE_REPAYMENT.getValue().equals(busOriType)){
			StringBuilder debitAccName=new StringBuilder(loanBillInfo.getDebitSubjecName());
			debitAccName.append("_").append(loanBillInfo.getDebitAcc());
			loanBillInfo.setCreditAccName(debitAccName.toString());
			
			StringBuilder creditAccName=new StringBuilder(loanBillInfo.getCreditSubjecName());
			creditAccName.append("_").append(loanBillInfo.getCreditAcc());
			loanBillInfo.setDebitAccName(creditAccName.toString());
			//委托存款-分录
			StringBuilder precativeAccName = new StringBuilder(loanBillInfo.getPrecativeSubjectName());
			precativeAccName.append("_").append(loanBillInfo.getPrecativeAcc());
			loanBillInfo.setPrecativeAccName(precativeAccName.toString());
			//委托贷款-分录
			StringBuilder loanAccName = new StringBuilder(loanBillInfo.getLoanSubjectName());
			loanAccName.append("_").append(loanBillInfo.getLoanAcc());
			loanBillInfo.setLoanAccName(loanAccName.toString());
			if(accbookBlnedList.size() == 6){
				//利息分录-借
				StringBuilder debitInterestAccName = new StringBuilder(loanBillInfo.getDebitSubjecName());
				debitInterestAccName.append("_").append(loanBillInfo.getCreditAcc());
				loanBillInfo.setDebitInterestAccName(debitInterestAccName.toString());
				//利息分录-贷
				StringBuilder creditInterestAccName = new StringBuilder(loanBillInfo.getCreditSubjecName());
				creditInterestAccName.append("_").append(loanBillInfo.getDebitAcc());
				loanBillInfo.setCreditInterestAccName(creditInterestAccName.toString());
			}
			loanBillInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_22);
			loanBillInfo.setRepaymentDate(tallyDate);
		}
		
		loanBillInfo.setVoucherNo(voucherNo);
		//原始凭证号
		loanBillInfo.setOrigVoucherNo(origVoucherNo);
		//如果是作废凭证则判断是否已经生成电子回单修改旧状态和设置日志信息
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("voucherNo", voucherNo);
		params.put("notStatus", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", DateUtils.getDate());
		com.bytter.biz.receipt.vo.ELoanBillInfoVo printInfo=ePrecativeLoanBillDao.getPrintInfoByVoucherNo(params);
		if(printInfo!=null){
			if(FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)){
			if(printInfo.getPrints()>1&&printInfo.getPrints2()>1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				ePrecativeLoanBillDao.updateEPrecativeLoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL, 
						ReceiptConstant.BILL_MSG_INFO_CANCELL,user);
			}else if(printInfo.getPrints()==1&&printInfo.getPrints2()>1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				ePrecativeLoanBillDao.updateEPrecativeLoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL, 
						ReceiptConstant.BILL_MSG_INFO_CANCELL1,user);
			}else if(printInfo.getPrints()>1&&printInfo.getPrints2()==1){
				params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
				ePrecativeLoanBillDao.updateEPrecativeLoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL, 
						ReceiptConstant.BILL_MSG_INFO_CANCELL2,user);
			}else if(printInfo.getPrints()==1&&printInfo.getPrints2()==1){
				ePrecativeLoanBillDao.deleteEPrecativeLoanBill(params);
				ebillMessageService.saveEBillMessage(printInfo.getOldBillno(), 
						ReceiptConstant.BILL_MSG_OP_TYPE_DELETE, 
						ReceiptConstant.BILL_MSG_INFO_DELETE,user);
			}
			}
		}else if(!FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)){
			loanBillInfo.setBillNo(SequenceSingleton.getSequence(SequenceEnum.E_PRECATIVE_LOAN_BILL_NO_R));
			loanBillInfo.setRandom(ReceiptUtils.getBillRandom(loanBillInfo.getBillNo()));
			loanBillInfo.setValidcode(
					ReceiptUtils.getBillValidcode(
							loanBillInfo.getDebitAcc(), 
							null,
							loanBillInfo.getLoanMoney(),
							null,
							loanBillInfo.getRandom()));
			loanBillInfo.setEncrySource(ReceiptUtils.getBillEncrySource(loanBillInfo.getValidcode()));
			//非作废凭证进行入库
			EPrecativeLoanBill loanBill=new EPrecativeLoanBill();
			loanBill.setBillNo(loanBillInfo.getBillNo());
			loanBill.setCorpId(loanBillInfo.getCorpId());
			loanBill.setPrecativeCorp(loanBillInfo.getPrecativeCorp());
			loanBill.setPrecativeAcc(loanBillInfo.getPrecativeAcc());//委托存款
			loanBill.setLoanCorp(loanBillInfo.getLoanCorp());
			loanBill.setLoanAcc(loanBillInfo.getLoanAcc());//委托贷款
			loanBill.setLoanDate(loanBillInfo.getLoanDate());
			loanBill.setDeadlineDate(loanBillInfo.getDeadlineDate());
			loanBill.setRepaymentDate(loanBillInfo.getRepaymentDate());
			loanBill.setLoanMoney(loanBillInfo.getLoanMoney());
			loanBill.setLoanRate(loanBillInfo.getLoanRate());
			if(ReceiptConstant.RECEIPT_BILL_TYPE_21.equals(loanBillInfo.getBillType())){//委托贷款
				loanBill.setDebitAcc(loanBillInfo.getDebitAccName() + ";" + loanBillInfo.getLoanAccName());
				loanBill.setCreditAcc(loanBillInfo.getPrecativeAccName() + ";" + loanBillInfo.getCreditAccName());
			}else{
				if(StringUtils.isNotEmpty(loanBillInfo.getDebitInterestAccName()) && StringUtils.isNotEmpty(loanBillInfo.getCreditInterestAccName())){
					loanBill.setDebitAcc(loanBillInfo.getDebitAccName() + ";" + loanBillInfo.getPrecativeAccName() + ";" + loanBillInfo.getDebitInterestAccName());
					loanBill.setCreditAcc(loanBillInfo.getLoanAccName() + ";" + loanBillInfo.getCreditAccName() + ";" + loanBillInfo.getCreditInterestAccName());
				}else{
					loanBill.setDebitAcc(loanBillInfo.getDebitAccName() + ";" + loanBillInfo.getPrecativeAccName());
					loanBill.setCreditAcc(loanBillInfo.getLoanAccName() + ";" + loanBillInfo.getCreditAccName());
				}
			}
			loanBill.setCurId(loanBillInfo.getCur());
			loanBill.setContractCode(loanBillInfo.getContractCode());
			loanBill.setPrecativeLoanAcc(loanBillInfo.getLoanAcc());//委托贷款账号
			loanBill.setVoucherNo(voucherNo);
			loanBill.setOldVoucher(origVoucherNo);
			loanBill.setStatus(ReceiptConstant.LOAN_BILL_STATUS_INPUT);
			loanBill.setPrints(1);
			loanBill.setBillType(loanBillInfo.getBillType());
			loanBill.setValidateCode(loanBillInfo.getValidcode());
			loanBill.setEncrySource(loanBillInfo.getEncrySource());
			loanBill.setSubscriber(user.getUsername());
			loanBill.setEncryDate(new Date());
			loanBill.setRandom(loanBillInfo.getRandom());
			loanBill.setTallyDate(tallyDate);
			loanBill.setTallyerCode(operatorCode);
			loanBill.setCheckerCode(checkerCode);
			loanBill.setBusGlobalId(busGlobalId);
			loanBill.setCreateBy(user.getLoginName());
			loanBill.setCreateDate(new Date());
			ePrecativeLoanBillDao.save(loanBill);
			ebillMessageService.saveEBillMessage(loanBillInfo.getBillNo(), 
					ReceiptConstant.BILL_MSG_OP_TYPE_CREATE, 
					ReceiptConstant.BILL_MSG_INFO_CREATE,user);
			ebillPrintService.saveEBillMessage(loanBillInfo.getBillNo(),
					loanBillInfo.getCorpId(), 1,user);
			ePrecativeLoanBillDao.flush();
		}
	}
	/**
	 * 判断是否需要生成电子回单
	 * @param busOriType 唯一ID
	 * @param busGlobalId
	 * @return
	 */
	public boolean checkPrecativeLoan(Integer busOriType,String voucherNo,String busGlobalId){
		if(FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.getValue().equals(busOriType)){
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("busGlobalId", busGlobalId);
			params.put("notVoucherNo", voucherNo);
			params.put("notStatus", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
			int num=ePrecativeLoanBillDao.getPrecativeLoanNum(params);
			if(num>0){
				return false;
			}
		}else if(!(FinaBookConstant.BusOriType.NB_PRECATIVE_REPAYMENT.getValue().equals(busOriType)
				||FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.getValue().equals(busOriType))){
			//非委托贷款-贷款和委托贷款-还款的业务不处理
			return false;
		}
		return true;
	}
}
