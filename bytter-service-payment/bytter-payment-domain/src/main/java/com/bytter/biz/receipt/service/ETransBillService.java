package com.bytter.biz.receipt.service;

import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlDisposeService;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.biz.finacial.api.dto.FcCompareAccDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookBlnedService;
import com.bytter.biz.finacial.api.service.IReceiptQueryService;
import com.bytter.biz.receipt.dao.ETransBillDao;
import com.bytter.biz.receipt.entity.ETransBill;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.ELoanBillInfoVo;
import com.bytter.biz.receipt.vo.ETransBillVO;
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
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 账户转换电子回单表
 * @date 2017-07-04 15:30:19
 */
@Service("eTransBillService")
@Transactional
public class ETransBillService extends BaseService<ETransBill, String> {
    @Autowired
    private ETransBillDao eTransBillDao;
    @Autowired
    private EBillPrintService ebillPrintService;
    @Autowired
    private EBillMessageService ebillMessageService;
    @DubboReference(check = false)
	private IFcAccService iFcAccService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IReceiptQueryService iReceiptQueryService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private ICreditPeriodService creditPeriodService;
    @DubboReference(check = false)
    private IBisBankReceiptDtlDisposeService iBisBankReceiptDtlDisposeService;
    @DubboReference(check = false)
    private IFcAccbookBlnedService bookBlnedService;



    /**
     * 保存
     *
     * @param entity
     */
    public void save(ETransBill entity) {
        super.saveEntity(entity);
    }
    @Override
    public BaseDao<ETransBill, String> getEntityDao() {
        return eTransBillDao;
    }
    /**
     * 创建账户转换电子回单
     *
     * @param orderNo       流水号
     * @param voucherNo     凭证号
     * @param tallyDate     记账日期
     * @param voucherStatus 凭证状态
     * @param operatorCode  操作员
     * @param checkerCode   复核员
     */
    public void createTransBill(String orderNo, String voucherNo,
                                Date tallyDate, String voucherStatus,
                                String operatorCode, String checkerCode, ICurrentUser user) {
        //查询记账凭证明细信息设置电子回单要素
        List<com.bytter.biz.finacial.api.vo.ELoanBillInfoVo> accbookBlnedList = iReceiptQueryService.listFcAccbookBlnedSubjectByOrderNo(orderNo);
        BigDecimal debitMoney = BigDecimal.ZERO;
        ELoanBillInfoVo billInfo = new ELoanBillInfoVo();
        for (com.bytter.biz.finacial.api.vo.ELoanBillInfoVo accbookBlned : accbookBlnedList) {
            FcAccVO fcacc = iFcAccService.getFcAccVOByBlnctrAcc(accbookBlned.getAccount());
            accbookBlned.setAccName(fcacc.getAccName());
            accbookBlned.setSysSubjectCode(fcacc.getSysSubjectCode());

            billInfo.setDebitMoney(accbookBlned.getDebitMoney());
            billInfo.setCreditMoney(accbookBlned.getCreditMoney());
            billInfo.setSummaryContent(accbookBlned.getSummaryContent());// 摘要
            billInfo.setOrigVoucherNo(accbookBlned.getOrigVoucherNo());// 原始凭证号
            billInfo.setOrigVoucherId(accbookBlned.getOrigVoucherId());// 原始凭证id
            // 判断借贷方
            if (SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())
                    || SubjectConstant.SysSubject.FIXED_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())
                    || SubjectConstant.SysSubject.NOTICE_DEPOSIT.getValue().equals(accbookBlned.getSysSubjectCode())) {
                if (billInfo.getDebitMoney().doubleValue() != 0) {
                    billInfo.setDebitSubjec(accbookBlned.getSysSubjectCode());
                    billInfo.setDebitAcc(accbookBlned.getAccount());
                    billInfo.setDebitAccName(accbookBlned.getAccName());
                    billInfo.setCur(accbookBlned.getCur());
                    billInfo.setMoney(accbookBlned.getDebitMoney());
                    debitMoney = accbookBlned.getDebitMoney();
                } else if (billInfo.getCreditMoney().doubleValue() != 0) {
                    billInfo.setCreditSubjec(accbookBlned.getSysSubjectCode());
                    billInfo.setCreditAcc(accbookBlned.getAccount());
                    billInfo.setCreditAccName(accbookBlned.getAccName());
                    billInfo.setMoney(accbookBlned.getCreditMoney());
                }
            }
        }
        // 如果借方账号和贷方账号都不存在直接返回
        String[] blnctrAcc = {billInfo.getDebitAcc(), billInfo.getCreditAcc()};
        if (StringUtils.isEmpty(blnctrAcc) || blnctrAcc.length != 2) {
            return;
        }
        // 得到内部账号信息
        String countSql=" and t.blnctr_acc in ('"+billInfo.getDebitAcc()+"','"+billInfo.getCreditAcc()+"') ";
        List<FcAccDTO> fcaccList = iFcAccService.queryBySomeCondPgd(countSql);
        if (fcaccList == null || fcaccList.size() != 2) {
            return;
        }
        // 账号对应内部账号信息
        Map<String, ELoanBillInfoVo> fcaccMap = new HashMap<String, ELoanBillInfoVo>();
        for (FcAccDTO acc : fcaccList) {
            ELoanBillInfoVo fcacc=new ELoanBillInfoVo();
            fcacc.setAccount(acc.getBlnctrAcc());
            fcacc.setStartDate(acc.getInterestStartDate());
            fcacc.setEndDate(acc.getDeadlineDate());
            fcacc.setNotifyType(acc.getNotifyType());
            fcacc.setRate(acc.getInterestRate());
            fcacc.setCorpId(acc.getCorpId());
            fcaccMap.put(fcacc.getAccount(), fcacc);
        }
        if (SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())
                && SubjectConstant.SysSubject.NOTICE_DEPOSIT.getValue().equals(billInfo.getCreditSubjec())) {
            // 活期转通知
            billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_12);
            ELoanBillInfoVo creditAcc = fcaccMap.get(billInfo.getCreditAcc());
            billInfo.setStartDate(creditAcc.getStartDate());
            billInfo.setEndDate(creditAcc.getEndDate());
            billInfo.setNotifyType(creditAcc.getNotifyType());
            billInfo.setRate(creditAcc.getRate());
            billInfo.setCorpId(creditAcc.getCorpId());
            //billInfo.setMoney();
            ELoanBillInfoVo debitAcc = fcaccMap.get(billInfo.getDebitAcc());
            billInfo.setDepositDate(debitAcc.getStartDate());
            billInfo.setDepositRate(debitAcc.getRate());
        } else if (SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())
                && SubjectConstant.SysSubject.FIXED_DEPOSIT.getValue().equals(billInfo.getCreditSubjec())) {
            // 活期转定期
            billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_13);
            ELoanBillInfoVo creditAcc = fcaccMap.get(billInfo.getCreditAcc());
            billInfo.setStartDate(creditAcc.getStartDate());
            billInfo.setEndDate(creditAcc.getEndDate());
            billInfo.setNotifyType(creditAcc.getNotifyType());
            billInfo.setRate(creditAcc.getRate());
            billInfo.setCorpId(creditAcc.getCorpId());
            ELoanBillInfoVo debitAcc = fcaccMap.get(billInfo.getDebitAcc());
            billInfo.setDepositDate(debitAcc.getStartDate());
            billInfo.setDepositRate(debitAcc.getRate());
            billInfo.setLimit(DateUtils.dayDiffs(creditAcc.getEndDate(), creditAcc.getStartDate()));
        } else if (SubjectConstant.SysSubject.FIXED_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())
                && SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getCreditSubjec())) {
            // 定期转活期
            billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_14);
            ELoanBillInfoVo debitAcc = fcaccMap.get(billInfo.getDebitAcc());
            billInfo.setStartDate(debitAcc.getStartDate());
            billInfo.setEndDate(debitAcc.getEndDate());
            billInfo.setNotifyType(debitAcc.getNotifyType());
            billInfo.setRate(debitAcc.getRate());
            billInfo.setCorpId(debitAcc.getCorpId());
            ELoanBillInfoVo creditAcc = fcaccMap.get(billInfo.getCreditAcc());
            billInfo.setDepositDate(creditAcc.getStartDate());
            billInfo.setDepositRate(creditAcc.getRate());
            billInfo.setLimit(DateUtils.dayDiffs(debitAcc.getEndDate(), debitAcc.getStartDate()));
        } else if (SubjectConstant.SysSubject.NOTICE_DEPOSIT.getValue().equals(billInfo.getDebitSubjec())
                && SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.getValue().equals(billInfo.getCreditSubjec())) {
            // 通知转活期
            billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_15);
            ELoanBillInfoVo debitAcc = fcaccMap.get(billInfo.getDebitAcc());
            billInfo.setStartDate(debitAcc.getStartDate());
            billInfo.setEndDate(debitAcc.getEndDate());
            billInfo.setNotifyType(debitAcc.getNotifyType());
            billInfo.setRate(debitAcc.getRate());
            billInfo.setCorpId(debitAcc.getCorpId());
            billInfo.setMoney(debitMoney);
            ELoanBillInfoVo creditAcc = fcaccMap.get(billInfo.getCreditAcc());
            billInfo.setDepositDate(creditAcc.getStartDate());
            billInfo.setDepositRate(creditAcc.getRate());
        }
        // 单位为空不生成电子回单
        if (StringUtils.isEmpty(billInfo.getCorpId())) {
            return;
        }
        billInfo.setVoucherNo(voucherNo);
        // 对原始数据和电子回单进行校验
        // 对于已经作废的凭证如果已经生成电子回单修改原状态和设置日志信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("voucherNo", voucherNo);
        params.put("notStatus", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
        params.put("updateBy", user.getLoginName());
        params.put("updateDate", DateUtils.getDate());
        ELoanBillInfoVo printInfo = eTransBillDao.getPrintInfoByVoucherNo(params);
        if (printInfo != null) {
            if (FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)) { //如果是作废复核
                if (printInfo.getPrints() > 1 && printInfo.getPrints2() > 1) { // 成员单位已打印，中心已打印
                    params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
                    eTransBillDao.updateETransBill(params);  // 更新状态为删除
                    ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
                            ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
                            ReceiptConstant.BILL_MSG_INFO_CANCELL, user);// 插入日志信息
                } else if (printInfo.getPrints() == 1 && printInfo.getPrints2() > 1) { // 成员单位打印，中心未打印
                    params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
                    eTransBillDao.updateETransBill(params);
                    ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
                            ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
                            ReceiptConstant.BILL_MSG_INFO_CANCELL1, user);
                } else if (printInfo.getPrints() > 1 && printInfo.getPrints2() == 1) { // 中心打印，成员单位未打印
                    params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
                    eTransBillDao.updateETransBill(params);
                    ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
                            ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
                            ReceiptConstant.BILL_MSG_INFO_CANCELL2, user);
                } else if (printInfo.getPrints() == 1 && printInfo.getPrints2() == 1) { // 成员单位和中心都未打印
                    eTransBillDao.deleteETransBill(params);
                    ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
                            ReceiptConstant.BILL_MSG_OP_TYPE_DELETE,
                            ReceiptConstant.BILL_MSG_INFO_DELETE, user);
                }
            }
        } else if (!FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.getValue().equals(voucherStatus)) { // 非作废凭证进行入库
            billInfo.setBillNo(SequenceSingleton.getSequence(SequenceEnum.E_TRANS_BILL_NO_T));
            billInfo.setRandom(ReceiptUtils.getBillRandom(billInfo.getBillNo()));
            billInfo.setValidcode(
                    ReceiptUtils.getBillValidcode(
                            billInfo.getDebitAcc(),
                            billInfo.getCreditAcc(),
                            billInfo.getMoney(),
                            null,
                            billInfo.getRandom()));
            billInfo.setEncrySource(ReceiptUtils.getBillEncrySource(billInfo.getValidcode()));
            ETransBill transBill = new ETransBill();
            transBill.setCorpId(billInfo.getCorpId());
            transBill.setSummaryContent(billInfo.getSummaryContent());
            transBill.setOrigVoucherNo(billInfo.getOrigVoucherNo());
            transBill.setOrigVoucherId(billInfo.getOrigVoucherId());
            transBill.setBillNo(billInfo.getBillNo());
            //transBill.setAccNo(billInfo.getAccount());
            //transBill.setAccName(billInfo.getAccName());
            transBill.setAccNo(billInfo.getCreditAcc());
            transBill.setAccName(billInfo.getCreditAccName());
            transBill.setStartDate(billInfo.getStartDate());
            transBill.setEndDate(billInfo.getEndDate());
            transBill.setRate(billInfo.getRate());
            transBill.setDayLimit(billInfo.getLimit());
            transBill.setNotifyType(billInfo.getNotifyType());
            transBill.setMoney(billInfo.getMoney());
            //transBill.setCurrentAcc(billInfo.getCreditAcc());
            //transBill.setCurrentAccName(billInfo.getCreditAccName());
            transBill.setCurId(billInfo.getCur());
            transBill.setCurrentAcc(billInfo.getDebitAcc());
            transBill.setCurrentAccName(billInfo.getDebitAccName());
            transBill.setDepositDate(billInfo.getDepositDate());
            transBill.setDepositRate(billInfo.getDepositRate());
            transBill.setVoucherNo(voucherNo);
            transBill.setStatus(ReceiptConstant.LOAN_BILL_STATUS_INPUT);
            transBill.setPrints(1);
            transBill.setValidateCode(billInfo.getValidcode());
            transBill.setEncrySource(billInfo.getEncrySource());
            transBill.setSubscriber(user.getUsername());
            transBill.setEncryDate(new Date());
            transBill.setRandom(billInfo.getRandom());
            transBill.setTallierCode(operatorCode);
            transBill.setTallyDate(tallyDate);
            transBill.setCheckerCode(checkerCode);
            transBill.setBillType(billInfo.getBillType());
            transBill.setCreateBy(user.getLoginName());
            transBill.setCreateDate(new Date());
            eTransBillDao.save(transBill);
            ebillMessageService.saveEBillMessage(billInfo.getBillNo(),
                    ReceiptConstant.BILL_MSG_OP_TYPE_CREATE,
                    ReceiptConstant.BILL_MSG_INFO_CREATE, user);
            ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
                    billInfo.getCorpId(), 1, user);
            eTransBillDao.flush();
        }
    }
	/**
	 * 获取定期存单数据
	 * @param input
	 * @return
	 */
	public DataTablesOutput datatables(DataTablesInput input, ICurrentUser user){
        StringBuilder commonSql = new StringBuilder();
        if (input.getFormdata().containsKey("printSign")) {
            String printSign = (String) input.getFormdata().get("printSign");
            commonSql.append(ReceiptUtils.getSearchSqlCommon(printSign));
        }
        //权限控制类型  1-单位级 2-账号级
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            List<String> accountGrantList = accountGrantService.listByMultiValAccs(user.getId(), ResourceConstant.NETBANK_RECEIPT_ELEBILL_QUERY, null,
                    AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
            String accAuthSql = SqlUtil.getInSql(accountGrantList, "A.current_acc", true);
            commonSql.append(" and ").append(accAuthSql);
        }
        DataTablesOutput dataTablesOutput=eTransBillDao.datatables(input,commonSql.toString());
        List data=dataTablesOutput.getData();
        ArrayList<ETransBillVO> eTransBillVOS=new ArrayList<>();
        for(Object datum:data){
            ETransBillVO vo= (ETransBillVO) datum;
            BtCurrencyDTO currencyDTO = currencyService.getObjectById(vo.getCurId());
            vo.setCurName(currencyDTO.getCurName());
            String corpName=iCorpService.getCorpName(vo.getCorpId());
            vo.setCorpName(corpName);
            List<FcCompareAccDTO> voucherNo = bookBlnedService.getVoucherNosByOrigVoucherNo(vo.getOrigVoucherNo());
            if(voucherNo.size()>0){
                vo.setVoucherNo(String.valueOf(voucherNo.get(0).getVoucherNo()));
            }
            String deposit=iBisBankReceiptDtlDisposeService.getById(vo.getOrigVoucherNo());
            if(deposit!=null){
                TbCreditPeriodDTO creditPeriodDTO = creditPeriodService.getById(deposit);
                vo.setPeriod(creditPeriodDTO.getPeriod());
                vo.setPeriodCycle(creditPeriodDTO.getPeriodCycle());
                vo.setDepositRateName(creditPeriodDTO.getPeriodName());
            }
            eTransBillVOS.add(vo);
        }
        dataTablesOutput.setData(eTransBillVOS);
        return dataTablesOutput;
    }
}
