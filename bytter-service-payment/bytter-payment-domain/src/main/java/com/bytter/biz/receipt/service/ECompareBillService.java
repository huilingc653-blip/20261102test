package com.bytter.biz.receipt.service;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IReceiptQueryService;
import com.bytter.biz.finacial.api.vo.ELoanBillInfoVo;
import com.bytter.biz.receipt.dao.ECompareBillDao;
import com.bytter.biz.receipt.entity.ECompareBill;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.ECompareBillVO;
import com.bytter.check.api.IFcCompareAccService;
import com.bytter.check.api.IFcCompareErpService;
import com.bytter.check.dto.FcCompareAccDTO;
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

/**
* @Title: Service
* @Description: 对账单电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:30:28
* @version 1.0
*
*/
@Service("eCompareBillService")
@Transactional
public class ECompareBillService extends BaseService<ECompareBill,String> {
	@Autowired
	private ECompareBillDao eCompareBillDao;
	@DubboReference(check = false)
	private IFcCompareErpService iFcCompareErpService;
	@Autowired
	private EBillPrintService ebillPrintService;
	@Autowired
	private EBillMessageService ebillMessageService;
	@DubboReference(check = false)
	private IFcAccbookService fcAccbookService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;
	@DubboReference(check = false)
	private IFcCompareAccService fcCompareAccService;
	@DubboReference(check = false)
	private IReceiptQueryService iReceiptQueryService;
	@Autowired
	private IDictService dictServiceI;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	/**
	* 保存
	* @param entity
	*/
 	public void save(ECompareBill entity){
 		super.saveEntity(entity);
 	}
	@Override
	public BaseDao<ECompareBill, String> getEntityDao() {
		return eCompareBillDao;
	}
	/**
	 * 查询对账单电子回单表
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listForUnion(DataTablesInput input, ICurrentUser user){
		StringBuilder commonSql=new StringBuilder();
		if(input.getFormdata().containsKey("printSign")) {
			String printSign = (String)input.getFormdata().get("printSign");
			StringBuilder sql = new StringBuilder();
			if (ReceiptConstant.PRINT_STATUS_YES.equals(printSign)) {
				sql.append(" and b.prints > 1");
			} else if (ReceiptConstant.PRINT_STATUS_NO.equals(printSign)) {
				sql.append(" and b.prints = 1");
			}
			commonSql.append(sql.toString());
		}
		//权限控制类型  1-单位级 2-账号级
		String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
		if (!CommonConstant.YES.equals(controlType)) {
			List<String> accountGrantList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_RECEIPT_ELEBILL_QUERY, null,
					AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
			String accAuthSql = SqlUtil.getInSql(accountGrantList, "A.acc_no", true);
			commonSql.append(" and ").append(accAuthSql);
		}
		DataTablesOutput data = eCompareBillDao.listForUnion(input,commonSql.toString());
		List<ECompareBillVO> list = data.getData();
		if(list.size()>0) {
			List<String> corpIdS = corpService.getUserCorpIds(com.bytter.core.constant.ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_MANAGERQUERY, user.getId());
			//获取单位信息
			Map<String, String> corpMap = corpService.getNameByIds(corpIdS);
			Map<String, String> currencyMap = btCurrencyService.getCurrencyNameMap();
			for (ECompareBillVO vo : list) {
				vo.setBillTypeName(dictServiceI.get("eVisaType.billType", vo.getBillType()));
				vo.setCorpName(corpMap.get(vo.getCorpId()));
				vo.setCurName(currencyMap.get(vo.getCurId()));//币别
			}
			//设置期初期末余额
			for (int i = 0; i < list.size(); i++) {
				List<ECompareBillVO> list1 = this.getVOByBillNo(list.get(i).getBillNo());
				BigDecimal creditMoney = BigDecimal.ZERO;
				BigDecimal debitMoney = BigDecimal.ZERO;
				for (ECompareBillVO vo : list1) {
					creditMoney = creditMoney.add(vo.getCreditMoney());
					debitMoney = debitMoney.add(vo.getDebitMoney());
				}
				list.get(i).setSumCreditMoney(creditMoney);
				list.get(i).setSumDebitMoney(debitMoney);
				list.get(i).setBeginBalanceWay(list1.get(0).getBeginBalanceWay());
				list.get(i).setEndBalance(list1.get(list1.size() - 1).getBalance());
			}
		}
		return data;
	}
	/**
	 * 根据单据号获取VO
	 * 参数至少有一个有值
	 * @param billNo 单据号 可空
	 * @return
	 */
	public List<ECompareBillVO> getVOByBillNo(String billNo){
		List<ECompareBillVO> list  = eCompareBillDao.getVOByBillNo(billNo);
		Map<String, String> currencyMap = btCurrencyService.getCurrencyNameMap();
		for (ECompareBillVO vo : list) {
			vo.setCurName(currencyMap.get(vo.getCurId()));//币别
		}
		return list;
	}
	public ECompareBillVO getSumCompareBills(String billNo) {
		return eCompareBillDao.getSumCompareBills(billNo);
	}
	/**
	 * 生成对账电子回单listForUnion
	 * @param tallyStartDate
	 * @param tallyEndDate
	 * @param user
	 */
	public void createCompareBill(Date tallyStartDate, Date tallyEndDate, ICurrentUser user){
		// 增加校验，判断对账单余额不正确的数据是否存在
		Map<String,Object> params=new HashMap<>();
		params.put("tallyStartDate", tallyStartDate);
		params.put("tallyEndDate", tallyEndDate);
		String compareAccSql=" SELECT id,VOUCHER_NO AS voucherNo,Comp_Acc AS compAcc,LINE_NO AS lineNo ,BALANCE FROM FC_COMPARE_ACC WHERE TALLY_DATE >=:tallyStartDate AND TALLY_DATE <:tallyEndDate";
		List<FcCompareAccDTO> compareAccList= fcCompareAccService.getFcCompareAccDTOBySql(compareAccSql,params);
		if(compareAccList.size()>0) {
			String[] voucherNos = new String[compareAccList.size()];
			BigDecimal[] balanceS = new BigDecimal[compareAccList.size()];
			String[] compAccs = new String[compareAccList.size()];
			BigDecimal[] lineNos = new BigDecimal[compareAccList.size()];
			for (int i = 0; i < compareAccList.size(); i++) {
				FcCompareAccDTO compareAccDTO = compareAccList.get(i);
				if (StringUtils.isNotEmpty(compareAccDTO.getVoucherNo())) {
					voucherNos[i] = compareAccDTO.getVoucherNo();
				} else {
					voucherNos[i] = "";
				}
				balanceS[i] = compareAccDTO.getBalance();
				compAccs[i] = compareAccDTO.getCompAcc();
				lineNos[i] = compareAccDTO.getLineNo();
			}
			params.put("voucherNo", voucherNos);
			params.put("balance", balanceS);
			params.put("compAcc", compAccs);
			params.put("lineNo", lineNos);
			BigDecimal countCompareBal = iReceiptQueryService.getCountCompareBalanceInvalid(params);
			if (countCompareBal.intValue() > 0) {
				List<ELoanBillInfoVo> voList = iReceiptQueryService.getCompareAccBalance(params);
				for (ELoanBillInfoVo vo : voList) {
					Map<String, Object> param = new HashMap<>();
					param.put("bal", vo.getDebitMoney());
					param.put("voucherNo", vo.getVoucherNo());
					param.put("balance", vo.getDebitMoney());
					param.put("compAcc", vo.getAccount());
					param.put("lineNo", vo.getBillNo());
					param.put("tallyStartDate", tallyStartDate);
					param.put("tallyEndDate", tallyEndDate);
					//根据记账日期区间更新对账单余额，确保下一步生成对账电子回单余额正确
					fcCompareAccService.updateCompareAccBalance(param);
					//根据记账日期区间更新对账电子回单余额，确保下一步生成对账电子回单余额正确
					eCompareBillDao.updateCompareBillBalance(param);
				}
			}
		}

		//获取对账单信息
		List<FcCompareAccDTO>  fcCompareAccDTOList = iFcCompareErpService.listForEBill(tallyStartDate,tallyEndDate);
		String random = "";
		String encrySource = "";
		String voucherState = "";
		String billNo = SequenceSingleton.getSequence(SequenceEnum.E_COMPARE_BILL_NO_D);
		String account = "";
		for (FcCompareAccDTO fcCompareAccDTO : fcCompareAccDTOList ){
			if(!account.equals(fcCompareAccDTO.getCompAcc())){
				//重新获取billNo数据
				billNo = SequenceSingleton.getSequence(SequenceEnum.E_COMPARE_BILL_NO_D);
			}
			account = fcCompareAccDTO.getCompAcc();
			BigDecimal count = eCompareBillDao.getCountCompareBill(fcCompareAccDTO.getVoucherNo(),fcCompareAccDTO.getCompAcc(),fcCompareAccDTO.getLineNo());
			if(StringUtils.isNotEmpty(fcCompareAccDTO.getVoucherNo())) {
				FcAccbookDTO fcAccbookDTO = fcAccbookService.getBookVOByVoucherNo(fcCompareAccDTO.getVoucherNo());
				voucherState = fcAccbookDTO.getVoucherState()==null?"":fcAccbookDTO.getVoucherState();
				if (count.intValue() > 0) {
					if (voucherState.equals("B")) {
						ECompareBillVO eCompareBillVO = eCompareBillDao.getVOByVoucherNo(fcCompareAccDTO.getVoucherNo(), fcCompareAccDTO.getCompAcc());
						if (!EmptyUtils.isEmpty(eCompareBillVO)) {
							if (eCompareBillVO.getPrints() > 1 && eCompareBillVO.getPrints2() > 1) {
								eCompareBillDao.updateECompareBill(fcCompareAccDTO.getVoucherNo(), fcCompareAccDTO.getCompAcc(), fcCompareAccDTO.getLineNo().intValue(), user);
								ebillMessageService.saveEBillMessage(eCompareBillVO.getBillNo(),
										ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
										ReceiptConstant.BILL_MSG_INFO_CANCELL, user);
							} else if (eCompareBillVO.getPrints() == 1 && eCompareBillVO.getPrints2() > 1) {
								eCompareBillDao.updateECompareBill(fcCompareAccDTO.getVoucherNo(), fcCompareAccDTO.getCompAcc(), fcCompareAccDTO.getLineNo().intValue(), user);
								ebillMessageService.saveEBillMessage(eCompareBillVO.getBillNo(),
										ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
										ReceiptConstant.BILL_MSG_INFO_CANCELL1, user);
							} else if (eCompareBillVO.getPrints() > 1 && eCompareBillVO.getPrints2() == 1) {
								eCompareBillDao.updateECompareBill(fcCompareAccDTO.getVoucherNo(), fcCompareAccDTO.getCompAcc(), fcCompareAccDTO.getLineNo().intValue(), user);
								ebillMessageService.saveEBillMessage(eCompareBillVO.getBillNo(),
										ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
										ReceiptConstant.BILL_MSG_INFO_CANCELL2, user);
							} else if (eCompareBillVO.getPrints() == 1 && eCompareBillVO.getPrints2() == 1) {
								eCompareBillDao.deleteECompareBill(fcCompareAccDTO.getVoucherNo(), fcCompareAccDTO.getCompAcc(), fcCompareAccDTO.getLineNo().intValue());
								ebillMessageService.saveEBillMessage(eCompareBillVO.getBillNo(),
										ReceiptConstant.BILL_MSG_OP_TYPE_DELETE,
										ReceiptConstant.BILL_MSG_INFO_DELETE, user);
							}
						}
					}
				} else {
					if (!voucherState.equals("B")) {
						ECompareBill eCompareBill = new ECompareBill();
						FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(fcCompareAccDTO.getCompAcc());
						eCompareBill.setBillNo(billNo);
						random = ReceiptUtils.getBillRandom(eCompareBill.getBillNo());
						encrySource = fcAccDTO.getBlnctrAcc() + DateUtils.formatDate("yyyyMMdd") + random;
						eCompareBill.setCorpId(fcAccDTO.getCorpId());
						eCompareBill.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_17);
						eCompareBill.setAccNo(fcAccDTO.getBlnctrAcc());
						eCompareBill.setAccName(fcAccDTO.getAccName());
						eCompareBill.setSummaryComment(fcCompareAccDTO.getSummaryContent());
						eCompareBill.setCurId(fcAccDTO.getCurId());
						eCompareBill.setBillCode("");
						eCompareBill.setInterestStartDate(fcCompareAccDTO.getInterestStartDate());
						eCompareBill.setDebitMoney(fcCompareAccDTO.getDebitMoney());
						eCompareBill.setCreditMoney(fcCompareAccDTO.getCreditMoney());
						eCompareBill.setBalance(fcCompareAccDTO.getBalance());
						eCompareBill.setVoucherNo(fcCompareAccDTO.getVoucherNo());
						eCompareBill.setStatus(ReceiptConstant.LOAN_BILL_STATUS_INPUT);
						eCompareBill.setPrints(1);
						eCompareBill.setValidateCode(encrySource);
						eCompareBill.setEncrySource(encrySource);
						eCompareBill.setSubscriber(user.getLoginName());
						eCompareBill.setEncryDate(DateUtils.getDate());
						eCompareBill.setRandom(random);
						eCompareBill.setTallyDate(fcCompareAccDTO.getTallyDate());
						eCompareBill.setTallyerCode("");
						eCompareBill.setLineNo(fcCompareAccDTO.getLineNo().intValue());
						eCompareBill.setRedblueSign(fcCompareAccDTO.getRedblueSign());
						eCompareBill.setCreateBy(user.getLoginName());
						eCompareBill.setCreateDate(DateUtils.getDate());
						eCompareBillDao.save(eCompareBill);
						ebillMessageService.saveEBillMessage(eCompareBill.getBillNo(),
								ReceiptConstant.BILL_MSG_OP_TYPE_CREATE,
								ReceiptConstant.BILL_MSG_INFO_CREATE, user);
						ebillPrintService.saveEBillMessage(eCompareBill.getBillNo(),
								eCompareBill.getCorpId(), 1, user);
						eCompareBillDao.flush();
					}
				}
			}
		}
	}
}
