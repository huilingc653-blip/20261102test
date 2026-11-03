package com.bytter.biz.receipt.service;

import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.receipt.dao.EExchangeBillDao;
import com.bytter.biz.receipt.entity.EExchangeBill;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部结售汇电子回单
 * @date 2017-07-04 15:30:33
 */
@Service("eExchangeBillService")
@Transactional
public class EExchangeBillService extends BaseService<EExchangeBill, String> {
	@Autowired
	private EExchangeBillDao eExchangeBillDao;
	@Autowired
	private EBillPrintService ebillPrintService;
	@Autowired
	private EBillMessageService ebillMessageService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(EExchangeBill entity) {
		super.saveEntity(entity);
	}
	@Override
	public BaseDao<EExchangeBill, String> getEntityDao() {
		return eExchangeBillDao;
	}
	/**
	 * 创建内部结售汇电子回单
	 *
	 * @param origVoucherNo 原凭证号
	 * @param origVoucherNo  操作员
	 */
	public void createExchangeBill(String origVoucherNo, ICurrentUser user) {
		BtCurrencyDTO curDto = btCurrencyService.getNatualCurrency();
		if (curDto == null) {
			return;
		}
		String[] voucherTypes = {NetbankConstant.PAY_TYPE_46, NetbankConstant.PAY_TYPE_47};
		List<ELoanBillInfoVo> accbookBlnedDtlList = eExchangeBillDao.listAccbookBlnedByOrigVoucherNo(origVoucherNo, voucherTypes);
		if (accbookBlnedDtlList == null || accbookBlnedDtlList.size() < 1) {
			return;
		}
		ELoanBillInfoVo billInfo = new ELoanBillInfoVo();
		String voucherNo = "";
		for (ELoanBillInfoVo accbookInfo : accbookBlnedDtlList) {
			if (!voucherNo.equals(accbookInfo.getVoucherNo())) {
				voucherNo = accbookInfo.getVoucherNo();
				billInfo.setVoucherNo(billInfo.getVoucherNo() + "," + accbookInfo.getVoucherNo());
				billInfo.setOrigVoucherNo(accbookInfo.getOrigVoucherNo());
				billInfo.setVoucherType(accbookInfo.getVoucherType());
				billInfo.setCheckerCode(accbookInfo.getCheckerCode());
				billInfo.setOperatorCode(accbookInfo.getOperatorCode());
				billInfo.setVoucherState(accbookInfo.getVoucherState());
				billInfo.setAffixAmount(accbookInfo.getAffixAmount());
				billInfo.setTallydate(accbookInfo.getTallydate());
				billInfo.setStartDate(accbookInfo.getStartDate());
			}
			if (SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getSysSubjectCode())) {
				if (NetbankConstant.PAY_TYPE_46.equals(accbookInfo.getVoucherCode())) {
					if (accbookInfo.getDebitMoney() != null && accbookInfo.getDebitMoney().doubleValue() != 0) {
						billInfo.setOrderNo(accbookInfo.getOrderNo());
						billInfo.setDebitSubjec(accbookInfo.getSysSubjectCode());
						billInfo.setDebitMoney(accbookInfo.getDebitMoney());
						billInfo.setDebitAcc(accbookInfo.getAccNo());
						billInfo.setMoney(billInfo.getDebitMoney());
						billInfo.setSummaryContent(accbookInfo.getSummaryContent());
						billInfo.setAccNo(billInfo.getDebitAcc());
						billInfo.setTradeRate(billInfo.getTradeRate());
						if (!curDto.getId().equals(accbookInfo.getCur())) {
							billInfo.setCurOut(accbookInfo.getCur());
						}
					} else {
						billInfo.setCreditAcc(accbookInfo.getAccNo());
						billInfo.setCreditMoney(accbookInfo.getCreditMoney());
						billInfo.setMoneyRmb(billInfo.getCreditMoney());
						if (!curDto.getId().equals(accbookInfo.getCur())) {
							billInfo.setCurIn(accbookInfo.getCur());
						}
					}
				} else if (NetbankConstant.PAY_TYPE_47.equals(accbookInfo.getVoucherCode())) {
					billInfo.setOrderNo(accbookInfo.getOrderNo());
					if (accbookInfo.getDebitMoney() != null && accbookInfo.getDebitMoney().doubleValue() != 0) {
						billInfo.setDebitSubjec(accbookInfo.getSysSubjectCode());
						billInfo.setDebitAcc(accbookInfo.getAccNo());
						billInfo.setDebitMoney(accbookInfo.getDebitMoney());
						billInfo.setMoneyRmb(billInfo.getDebitMoney());
						if (!curDto.getId().equals(accbookInfo.getCur())) {
							billInfo.setCurOut(accbookInfo.getCur());
						}
					} else {
						billInfo.setCreditAcc(accbookInfo.getAccNo());
						billInfo.setCreditMoney(accbookInfo.getCreditMoney());
						billInfo.setMoney(billInfo.getCreditMoney());
						billInfo.setAccNo(billInfo.getCreditAcc());
						billInfo.setTradeRate(billInfo.getTradeRate());
						if (!curDto.getId().equals(accbookInfo.getCur())) {
							billInfo.setCurIn(accbookInfo.getCur());
						}
					}
				}
			}
		}
		// 未来通过参数获取，目前默认为1
		String format = "1";
		// TODO 电子回单注意问题：format  WHERE sys_code = 'fcs' AND code = 'subpena_format';
		String[] blnctrAcc = {billInfo.getDebitAcc(), billInfo.getCreditAcc()};
		List<ELoanBillInfoVo> fcAccInfoList = eExchangeBillDao.listFcAccByAcc(blnctrAcc);
		ELoanBillInfoVo debitAccInfo = null;
		ELoanBillInfoVo creditAccInfo = null;
		for (ELoanBillInfoVo fcAccInfo : fcAccInfoList) {
			if (billInfo.getDebitAcc().equals(fcAccInfo.getAccNo())) {
				debitAccInfo = fcAccInfo;
				if ("1".equals(format)) {
					billInfo.setDebitAccName(debitAccInfo.getAccName());
					billInfo.setDebitCorpName(debitAccInfo.getCorpName());
					billInfo.setPayerBank(debitAccInfo.getNetName());
				} else if ("2".equals(format)) {
				}
			} else if (billInfo.getCreditAcc().equals(fcAccInfo.getAccNo())) {
				creditAccInfo = fcAccInfo;
				if ("1".equals(format)) {
					billInfo.setCreditAccName(creditAccInfo.getAccName());
					billInfo.setCreditCorpName(creditAccInfo.getCorpName());
					billInfo.setPayeeBank(creditAccInfo.getNetName());
				} else if ("2".equals(format)) {
				}
			}
		}
		if (debitAccInfo != null) {
			billInfo.setDebitCorpId(debitAccInfo.getCorpId());
		}
		if (creditAccInfo != null) {
			billInfo.setCreditCorpId(creditAccInfo.getCorpId());
		}
		billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_18);
		billInfo.setDebitSubjecName("企业活期_" + billInfo.getDebitAcc());
		billInfo.setCreditSubjecName("企业活期_" + billInfo.getCreditAcc());
		if (billInfo.getCreditCorpId().equals(billInfo.getDebitCorpId())) {
			billInfo.setCorpId(billInfo.getCreditCorpId());
		} else {
			billInfo.setCorpId(billInfo.getCreditCorpId() + ";" + billInfo.getDebitCorpId());
		}
		//如果是作废凭证则判断是否已经生成电子回单修改旧状态和设置日志信息
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("voucherNo", billInfo.getVoucherNo());
		params.put("notStatus", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", DateUtils.getDate());
		ELoanBillInfoVo printInfo = eExchangeBillDao.getPrintInfoByVoucherNo(params);
		if (printInfo != null) {
			if (FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(billInfo.getVoucherState())) {
				if (printInfo.getPrints() > 1 && printInfo.getPrints2() > 1) {
					params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
					eExchangeBillDao.updateELoanBill(params);
					ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
							ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
							ReceiptConstant.BILL_MSG_INFO_CANCELL,user);
				} else if (printInfo.getPrints() == 1 && printInfo.getPrints2() > 1) {
					params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
					eExchangeBillDao.updateELoanBill(params);
					ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
							ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
							ReceiptConstant.BILL_MSG_INFO_CANCELL1,user);
				} else if (printInfo.getPrints() > 1 && printInfo.getPrints2() == 1) {
					params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
					eExchangeBillDao.updateELoanBill(params);
					ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
							ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
							ReceiptConstant.BILL_MSG_INFO_CANCELL2,user);
				} else if (printInfo.getPrints() == 1 && printInfo.getPrints2() == 1) {
					eExchangeBillDao.deleteELoanBill(params);
					ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
							ReceiptConstant.BILL_MSG_OP_TYPE_DELETE,
							ReceiptConstant.BILL_MSG_INFO_DELETE,user);
				}
			}
		} else if (!FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(billInfo.getVoucherState())) {
			billInfo.setBillNo(SequenceSingleton.getSequence(SequenceEnum.E_EXCHANGE_BILL_NO_E));
			billInfo.setRandom(ReceiptUtils.getBillRandom(billInfo.getBillNo()));
			billInfo.setValidcode(
					ReceiptUtils.getBillValidcode(
							billInfo.getDebitAcc(),
							billInfo.getCreditAcc(),
							billInfo.getMoney(),
							billInfo.getVoucherNo(),
							billInfo.getRandom()));
			billInfo.setEncrySource(ReceiptUtils.getBillEncrySource(billInfo.getValidcode()));
			//非作废凭证进行入库
			EExchangeBill loanBill = new EExchangeBill();
			loanBill.setCorpId(billInfo.getCorpId());
			loanBill.setBillNo(billInfo.getBillNo());
			loanBill.setPayerName(billInfo.getDebitAccName());
			loanBill.setPayerAcc(billInfo.getDebitAcc());
			loanBill.setPayerBank(billInfo.getPayerBank());
			loanBill.setPayeeName(billInfo.getCreditAccName());
			loanBill.setPayeeAcc(billInfo.getCreditAcc());
			loanBill.setPayeeBank(billInfo.getPayeeBank());
			loanBill.setCurId(billInfo.getCur());
			loanBill.setPurpose(billInfo.getSummaryContent());
			loanBill.setPayType(billInfo.getVoucherType());
			loanBill.setCheckerCode(billInfo.getCheckerCode());
			loanBill.setAccrualDate(billInfo.getStartDate());
			loanBill.setTallyDate(billInfo.getTallydate());
			loanBill.setVoucherNo(billInfo.getVoucherNo());
			loanBill.setEncryDate(new Date());
			loanBill.setRandom(billInfo.getRandom());
			loanBill.setEncrySource(billInfo.getEncrySource());
			loanBill.setValidateCode(billInfo.getValidcode());
			loanBill.setStatus(ReceiptConstant.LOAN_BILL_STATUS_INPUT);
			loanBill.setOldVoucher(billInfo.getOrigVoucherNo());
			loanBill.setOldVoucherMoney(null);
			loanBill.setBillType(billInfo.getBillType());
			loanBill.setTallierCode(billInfo.getOperatorCode());
			loanBill.setRemark(null);
			loanBill.setDebit(billInfo.getDebitSubjecName());
			loanBill.setCredit(billInfo.getCreditSubjecName());
			loanBill.setPrints(1);
			loanBill.setOldVoucherName(null);
			loanBill.setAccessory(billInfo.getAffixAmount());
			loanBill.setSubscriber(user.getUsername());
			loanBill.setTradeRate(billInfo.getTradeRate());
			loanBill.setPayMoney(billInfo.getMoney());
			loanBill.setPayMoneyRmb(billInfo.getMoneyRmb());
			loanBill.setCurIn(billInfo.getCurIn());
			loanBill.setCurOut(billInfo.getCurOut());
			loanBill.setCreateBy(user.getLoginName());
			loanBill.setCreateDate(new Date());
			eExchangeBillDao.save(loanBill);
			ebillMessageService.saveEBillMessage(billInfo.getBillNo(),
					ReceiptConstant.BILL_MSG_OP_TYPE_CREATE,
					ReceiptConstant.BILL_MSG_INFO_CREATE,user);
			if (StringUtils.isNotEmpty(billInfo.getCorpId())
					&& billInfo.getCorpId().indexOf(";") > -1) {
				String[] corpIds = billInfo.getCorpId().split(";");
				ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
						corpIds[0], 1,user);
				ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
						corpIds[1], 1,user);
			} else {
				ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
						billInfo.getCorpId(), 1,user);
			}
			eExchangeBillDao.flush();
		}
	}
}
