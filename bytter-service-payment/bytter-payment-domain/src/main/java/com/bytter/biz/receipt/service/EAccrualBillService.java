package com.bytter.biz.receipt.service;

import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlDisposeService;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.FcCompareAccDTO;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookBlnedService;
import com.bytter.biz.finacial.api.service.IFcInterestService;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.receipt.dao.EAccrualBillDao;
import com.bytter.biz.receipt.entity.EAccrualBill;
import com.bytter.biz.receipt.util.ReceiptUtils;
import com.bytter.biz.receipt.vo.EAccrualBillVO;
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
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 利息清单电子回单
 * @date 2017-07-04 15:27:13
 */
@Service("eAccrualBillService")
@Transactional
public class EAccrualBillService extends BaseService<EAccrualBill, String> {
    @Autowired
    private EAccrualBillDao eAccrualBillDao;
    @Autowired
    private EBillPrintService ebillPrintService;
    @Autowired
    private EBillMessageService ebillMessageService;
    @DubboReference(check = false)
	private IFcInterestService fcInterestService;
    @DubboReference(check = false)
	private IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IFcAccbookBlnedService bookBlnedService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private ICreditPeriodService creditPeriodService;
    @DubboReference(check = false)
    private IBisBankReceiptDtlDisposeService iBisBankReceiptDtlDisposeService;
    /**
     * 保存
     *
     * @param entity
     */
    public void save(EAccrualBill entity) {
        super.saveEntity(entity);
    }
    @Override
    public BaseDao<EAccrualBill, String> getEntityDao() {
        return eAccrualBillDao;
    }
    /**
     * 查询利息清单电子回单
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<EAccrualBillVO> listForUnion(DataTablesInput input, ICurrentUser user) {
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
            String accAuthSql = SqlUtil.getInSql(accountGrantList, "A.ACC_NO", true);
            commonSql.append(" and ").append(accAuthSql);
        }
        DataTablesOutput<EAccrualBillVO> output = eAccrualBillDao.listForUnion(input, commonSql.toString());
        List<EAccrualBillVO> list=output.getData();
        if(list.size()>0) {
            List<DictDTO> dictList = dictService.list("eVisaType.billType");
            Map<String, String> dictMap = dictList.stream().collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
            for(EAccrualBillVO vo:list){
                vo.setBillTypeName(dictMap.get(vo.getBillType()));//电子回单类型
            }
        }
        return output;
    }
    /**
     * 查询存款利息（定期存款和通知存款）清单电子回单
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<EAccrualBillVO> depositIstlist(DataTablesInput input) {
        StringBuilder commonSql = new StringBuilder();
        if (input.getFormdata().containsKey("printSign")) {
            String printSign = (String) input.getFormdata().get("printSign");
            commonSql.append(ReceiptUtils.getSearchSqlCommon(printSign));
        }
        DataTablesOutput<EAccrualBillVO> dataTablesOutput=eAccrualBillDao.listDepositInterest(input, commonSql.toString());
        List<EAccrualBillVO> data=dataTablesOutput.getData();
        Map<String, String> currencyNameMap= currencyService.getCurrencyNameByIds(data.stream().map(EAccrualBillVO::getCurId).collect(Collectors.toList()));
        Map<String, String> corpNameMap = iCorpService.getNameByIds(data.stream().map(EAccrualBillVO::getCorpId).collect(Collectors.toList()));
        ArrayList<EAccrualBillVO> eAccrualBillVOS=new ArrayList<>();
        for(EAccrualBillVO vo:data){
            vo.setCurName(currencyNameMap.get(vo.getCurId()));
            vo.setCorpName(corpNameMap.get(vo.getCorpId()));
            List<FcCompareAccDTO> voucherNo = bookBlnedService.getVoucherNosByOrigVoucherNo(vo.getOrigVoucherNo());
            if(voucherNo.size()>0){
                vo.setVoucherNo(String.valueOf(voucherNo.get(0).getVoucherNo()));
            }
            String deposit=iBisBankReceiptDtlDisposeService.getById(vo.getOrigVoucherNo());
            if(deposit!=null){
                TbCreditPeriodDTO creditPeriodDTO = creditPeriodService.getById(deposit);
                vo.setDepositName(creditPeriodDTO.getPeriodName());
            }
            eAccrualBillVOS.add(vo);
        }
        return dataTablesOutput;
    }
    /**
     * 根据单据号获取VO
     * 参数至少有一个有值
     *
     * @param billNo 单据号 可空
     * @param id     流水号  可空
     * @return
     */
    public EAccrualBillVO getVOByBillNo(String billNo, String id) {
        return eAccrualBillDao.getVOByBillNo(billNo, id);
    }
    /**
     * 根据查询利息明细
     *
     * @param billNo
     * @return
     */
    public List<FcInterestDtlDTO> getFcInterestDtlDTOByOrderNo(String billNo) {
        return fcInterestService.getFcInterestDtlDTOByOrderNo(null, null, billNo);
    }
    /**
     * 创建利息清单电子回单
     *
     * @param orderNo           流水号
     * @param voucherNo         凭证号
     * @param accNo             账号
     * @param generateMode      利息明细产生方式
     * @param interestStartDate 开始日期
     * @param blnInstDate       结束日期
     * @param operatorCode      操作员
     * @param checkerCode       复核员
     */
    public void createAccrualsBillNew(String orderNo, String voucherNo,
                                      String accNo, String generateMode,
                                      Date interestStartDate, Date blnInstDate,
                                      String operatorCode, String checkerCode,
                                      String origVoucherNo,String origVoucherId,
                                      String summaryContent,ICurrentUser user) {
        if (StringUtils.isEmpty(orderNo)) {
            return;
        }
        // 查询记账凭证明细信息设置电子回单要素
        List<ELoanBillInfoVo> accrualsInfoList = eAccrualBillDao.listInterestInfoByOrderNoAndAccNo(orderNo, accNo);
        // 获取记账明细
        List<ELoanBillInfoVo> accbookBlnedList = eAccrualBillDao.listAccbookBlned(orderNo);
        ELoanBillInfoVo billInfo = new ELoanBillInfoVo();
        //账号集合
        HashMap<String, ELoanBillInfoVo> accInfos = new HashMap<>();
        for (ELoanBillInfoVo accrualsInfo : accrualsInfoList) {
            billInfo.setTallydate(accrualsInfo.getTallydate());
            billInfo.setAccNo(accrualsInfo.getAccNo());
            billInfo.setStartDate(interestStartDate);
            billInfo.setEndDate(blnInstDate);
            billInfo.setInterest(accrualsInfo.getInterest());
            billInfo.setCumu(accrualsInfo.getCumu());
            billInfo.setRate(accrualsInfo.getRate());
            billInfo.setDays(accrualsInfo.getDays());
            billInfo.setRepayMoney(accrualsInfo.getRepayMoney());
            billInfo.setOrderNo(accrualsInfo.getOrderNo());
            billInfo.setMoney(accrualsInfo.getMoney());
            if (!accInfos.containsKey(billInfo.getAccNo())) {
                accInfos.put(billInfo.getAccNo(), eAccrualBillDao.listFcAccByAccNo(billInfo.getAccNo()));
            }
            ELoanBillInfoVo accInfo = accInfos.get(billInfo.getAccNo());
            if (accInfo != null) {
                billInfo.setCorpId(accInfo.getCorpId());
                billInfo.setAccName(accInfo.getAccName());
                billInfo.setAccTypeCode(accInfo.getAccTypeCode());
                billInfo.setPredrawSign(accInfo.getPredrawSign());
                billInfo.setCur(accInfo.getCur());
            }
            // 如果是银行定期、银行活期、银行借款结束本次循环
            if (InnerAccountConstant.FCACC_BANK_DEMAND.equals(accInfo.getAccTypeCode())
                    || InnerAccountConstant.FCACC_BANK_BORROW.equals(accInfo.getAccTypeCode())
                    || InnerAccountConstant.FCACC_BANK_BEGULAR.equals(accInfo.getAccTypeCode())) {
                continue;
            }
            ELoanBillInfoVo homeAccInfo = new ELoanBillInfoVo();
            ELoanBillInfoVo otherAccInfo = new ELoanBillInfoVo();
            for(ELoanBillInfoVo dtl:accbookBlnedList){
                if (!accInfos.containsKey(dtl.getAccNo())) {
                    accInfos.put(dtl.getAccNo(), eAccrualBillDao.listFcAccByAccNo(dtl.getAccNo()));
                }
                if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(accInfo.getAccTypeCode())) {// 如果是企业活期
                    //01-企业活期存款
                    if (dtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0) {
                        //借：利息支出-企业活期（对）
                        otherAccInfo = accInfos.get(dtl.getAccNo());
                    } else {
                        //贷：企业活期存款（主）
                        homeAccInfo = accInfos.get(dtl.getAccNo());
                    }
                } else if(InnerAccountConstant.FCACC_BANK_DEMAND.equals(accInfo.getAccTypeCode())){
                    //02-银行活期存款
                    if (dtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0) {
                        //借：应收利息-银行活期存款(主)
                        homeAccInfo = accInfos.get(dtl.getAccNo());
                    } else {
                        //贷：利息收入-银行活期存款（对）
                        otherAccInfo = accInfos.get(dtl.getAccNo());
                    }
                } else if(InnerAccountConstant.FCACC_LOAN.equals(accInfo.getAccTypeCode())){
                    //03-放款
                    if (!dtl.getAccNo().equals(accInfo.getAccNo())
                            && dtl.getDebitMoney().add(dtl.getCreditMoney()).compareTo(BigDecimal.ZERO) > 0) {
                        if (dtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0) {
                            //借：企业活期（主）
                            homeAccInfo = accInfos.get(dtl.getAccNo());
                        } else {
                            //贷：利息收入（对）
                            otherAccInfo = accInfos.get(dtl.getAccNo());
                        }
                    }
                } else if(InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accInfo.getAccTypeCode())
                || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accInfo.getAccTypeCode())){
                    //05-企业定期 / 08-通知存款
                    if (!dtl.getAccNo().equals(accInfo.getAccNo())
                            && dtl.getDebitMoney().add(dtl.getCreditMoney()).compareTo(BigDecimal.ZERO) > 0) {
                        if (dtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0) {
                            //借：利息支出（对）
                            otherAccInfo = accInfos.get(dtl.getAccNo());
                        } else {
                            //贷：企业活期（主）
                            homeAccInfo = accInfos.get(dtl.getAccNo());
                        }
                    }
                }
            }
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(accInfo.getAccTypeCode())) {// 如果是企业活期
                // 01-存款利息单
                if (InterestConstant.PredrawSign.PREDRAW_SIGN_SEASON_ANTICIPATE.getValue().equals(accInfo.getPredrawSign())
                        && InterestConstant.GENERATEMODE_INTEREST_BATCH.equals(String.valueOf(generateMode).toUpperCase())
                        && !ReceiptUtils.getSeason(accrualsInfo.getEndDate())) {
                    billInfo.setRmk("上列预提利息信息,已照贷记入帐户.");
                    /*billInfo.setHomeSubject("(贷)科目：应付利息-企业活期存款");
                    billInfo.setOtherSubject("利息支出-企业活期存款");*/
                } else {
                    billInfo.setRmk("上列存款信息,已照贷记你单位存款帐户.");
                    /*billInfo.setHomeSubject("(贷)科目：企业活期存款");
                    billInfo.setOtherSubject("利息支出-企业活期存款");*/
                }
                billInfo.setHomeSubject("(贷)科目：" + homeAccInfo.getSysSubjectCodeName());
                billInfo.setOtherSubject(otherAccInfo.getSysSubjectCodeName());
                billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_01);// 存款利息单
            } else if (InnerAccountConstant.FCACC_BANK_DEMAND.equals(accInfo.getAccTypeCode())) {
                if (InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.getValue().equals(accInfo.getPredrawSign())
                        && InterestConstant.GENERATEMODE_INTEREST_BATCH.equals(String.valueOf(generateMode).toUpperCase())
                ) {
                    billInfo.setRmk("上列预提利息信息,已照借记入帐户.");
                    /*billInfo.setHomeSubject("(借)科目：应收利息-银行活期存款");
                    billInfo.setOtherSubject("利息收入-银行活期存款");*/
                    billInfo.setHomeSubject("(借)科目：" + homeAccInfo.getSysSubjectCodeName());
                    billInfo.setOtherSubject(otherAccInfo.getSysSubjectCodeName());
                } else {
                    continue;
                }
            } else if (InnerAccountConstant.FCACC_LOAN.equals(accInfo.getAccTypeCode())) {
			    // 02-贷款利息单
                if (InterestConstant.PredrawSign.PREDRAW_SIGN_SEASON_ANTICIPATE.getValue().equals(accInfo.getPredrawSign())
                        && InterestConstant.GENERATEMODE_INTEREST_BATCH.equals(String.valueOf(generateMode).toUpperCase())
                ) {
                    billInfo.setRmk("上列预提利息信息,已照借记入帐户.");
                    /*billInfo.setHomeSubject("(借)科目：应收利息-放款");
                    billInfo.setOtherSubject("利息收入-放款");*/
                    billInfo.setHomeSubject("(借)科目：" + homeAccInfo.getSysSubjectCodeName());
                    billInfo.setOtherSubject(otherAccInfo.getSysSubjectCodeName());
                } else {
                    billInfo.setRmk("上列放款信息,已照借扣除利息.");
                    /*billInfo.setHomeSubject("(借)科目：企业活期存款");
                    billInfo.setOtherSubject("利息收入-放款");*/
                    billInfo.setHomeSubject("(借)科目：" + homeAccInfo.getSysSubjectCodeName());
                    billInfo.setOtherSubject(otherAccInfo.getSysSubjectCodeName());
                }
                billInfo.setDebitMoney(accrualsInfo.getRepayMoney());
                if (billInfo.getDebitMoney() == null) {
                    billInfo.setDebitMoney(new BigDecimal(0));
                }
                billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_02); // 贷款利息单
            } else if (InnerAccountConstant.FCACC_BANK_BORROW.equals(accInfo.getAccTypeCode())) {
                billInfo.setRmk("上列预提利息信息,已照贷记入帐户.");
                billInfo.setHomeSubject("(贷)科目：应付利息-银行借款");
                billInfo.setOtherSubject("利息支出-银行借款");
            } else if (InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accInfo.getAccTypeCode())) {
                // 04-定期存款利息单
                if (InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.getValue().equals(accInfo.getPredrawSign())
                        && InterestConstant.GENERATEMODE_INTEREST_PART.equals(String.valueOf(generateMode).toUpperCase())
                        //&& InterestConstant.GENERATEMODE_INTEREST_BATCH.equals(String.valueOf(generateMode).toUpperCase())
                ) {
                    billInfo.setRmk("上列预提利息信息,已照贷记入帐户.");
                    /*billInfo.setHomeSubject("(贷)科目：应付利息-企业定期存款");
                    billInfo.setOtherSubject("利息支出-企业定期存款");*/
                } else {
                    billInfo.setRmk("上列利息信息,已照贷记入帐户.");
                    /*billInfo.setHomeSubject("(贷)科目：活期存款");
                    billInfo.setOtherSubject("利息支出-企业定期存款");*/
                }
                billInfo.setHomeSubject("(贷)科目：" + homeAccInfo.getSysSubjectCodeName());
                billInfo.setOtherSubject(otherAccInfo.getSysSubjectCodeName());
                billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_04); // 定期存款利息单
            } else if (InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accInfo.getAccTypeCode())) {
                // 05-通知存款利息单
                if (InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.getValue().equals(accInfo.getPredrawSign())
                        && InterestConstant.GENERATEMODE_INTEREST_PART.equals(String.valueOf(generateMode).toUpperCase())
                        //&& InterestConstant.GENERATEMODE_INTEREST_BATCH.equals(String.valueOf(generateMode).toUpperCase())
                ) {
                    billInfo.setRmk("上列预提利息信息,已照贷记入帐户.");
                   /* billInfo.setHomeSubject("(贷)科目：应付利息-通知存款");
                    billInfo.setOtherSubject("利息支出-通知存款");*/
                } else {
                    billInfo.setRmk("上列预提利息信息,已照贷记入帐户.");
                    /*billInfo.setHomeSubject("(贷)科目：活期存款");
                    billInfo.setOtherSubject("利息支出-通知存款");*/
                }
                billInfo.setHomeSubject("(贷)科目：" + homeAccInfo.getSysSubjectCodeName());
                billInfo.setOtherSubject(otherAccInfo.getSysSubjectCodeName());
                billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_05);// 通知存款利息单
            } else if (InnerAccountConstant.FCACC_LOANBYMANDATE.equals(accInfo.getAccTypeCode())) {
                // 03-委托贷款利息清单
                if (InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.getValue().equals(accInfo.getPredrawSign())
                        && InterestConstant.GENERATEMODE_INTEREST_BATCH.equals(String.valueOf(generateMode).toUpperCase())
                ) {
                    return;
                } else {
                    billInfo.setRmk("上列预提利息信息,已照借记入帐户.");
                    billInfo.setHomeSubject("(借)科目：单位活期存款");
                    billInfo.setOtherSubject("单位活期存款");
                }
                // 合同金额
                ELoanBillInfoVo loanSendInfo = eAccrualBillDao.getloanSendMoneyByAccNo(billInfo.getAccNo());
                if (loanSendInfo != null) {
                    billInfo.setTotolmoney(loanSendInfo.getMoney());
                } else {
                    billInfo.setTotolmoney(new BigDecimal(0));
                }
                // 已还金额
                ELoanBillInfoVo loanRepayInfo = eAccrualBillDao.getloanRepayMoneyByAccNo(billInfo.getAccNo());
                if (loanRepayInfo != null) {
                    billInfo.setRepayMoney(loanRepayInfo.getMoney());
                } else {
                    billInfo.setRepayMoney(new BigDecimal(0));
                }
                //本金
                FcOnlineLedgerDTO fc = fcOnlineLedgerService.getByBlnctrAcc(billInfo.getAccNo());
                billInfo.setMoney(fc != null ? fc.getCheckBalance() : BigDecimal.ZERO);
                billInfo.setDebitMoney(billInfo.getMoney());
                if (billInfo.getDebitMoney() == null) {
                    billInfo.setDebitMoney(new BigDecimal(0));
                }
                billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_03);
            } else if ("13".equals(accInfo.getAccTypeCode())) {
                // 20-定期保证金利息单
                if ("0".equals(accInfo.getInterestSign())) {
                    billInfo.setRmk("上列预提利息信息,已照贷记入帐户.");
                    billInfo.setHomeSubject("(贷)科目：应付利息-活期保证金");
                    billInfo.setOtherSubject("利息支出-活期保证金");
                } else {
                    billInfo.setRmk("上列预提利息信息,已照贷记你单位存款帐户.");
                    billInfo.setHomeSubject("(贷)科目：企业活期存款");
                    billInfo.setOtherSubject("利息支出-活期保证金");
                }
                billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_20);
            } else if ("15".equals(accInfo.getAccTypeCode())) {
                // 03-委托贷款利息清单
                if (InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.getValue().equals(accInfo.getPredrawSign())
                        && InterestConstant.GENERATEMODE_INTEREST_BATCH.equals(String.valueOf(generateMode).toUpperCase())
                ) {
                    billInfo.setRmk("上列预提利息信息,已照贷记入帐户.");
                    billInfo.setHomeSubject("(贷)科目：应付利息-定期保证金");
                    billInfo.setOtherSubject("利息支出-定期保证金");
                } else {
                    billInfo.setRmk("上列利息信息,已照贷记入帐户.");
                    billInfo.setHomeSubject("(贷)科目：活期存款");
                    billInfo.setOtherSubject("利息支出-定期保证金");
                }
                billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_21);
            }else{
                continue;
            }
            BigDecimal money = billInfo.getDebitMoney();
            billInfo.setNumMoney(((money == null ? BigDecimal.ZERO : money).add(accrualsInfo.getInterest())));
            billInfo.setVoucherNo(voucherNo);
            // 如果是作废凭证则判断是否已经生成电子回单修改旧状态和设置日志信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("voucherNo", voucherNo);
            params.put("accNo", billInfo.getAccNo());
            params.put("notStatus", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
            params.put("updateBy", user.getId());
            params.put("updateDate", DateUtils.getDate());
            if (isExists(params, user)) {
                billInfo.setBillNo(SequenceSingleton.getSequence(SequenceEnum.E_ACCRUAL_BILL_NO_I));
                billInfo.setRandom(ReceiptUtils.getBillRandom(billInfo.getBillNo()));
                billInfo.setValidcode(
                        ReceiptUtils.getBillValidcode(
                                billInfo.getAccNo(),
                                null,
                                billInfo.getDebitMoney(),
                                voucherNo,
                                billInfo.getRandom()));
                billInfo.setEncrySource(ReceiptUtils.getBillEncrySource(billInfo.getValidcode()));
                //不存在进行入库
                EAccrualBill accrualsBill = new EAccrualBill();
                accrualsBill.setCorpId(billInfo.getCorpId());
                accrualsBill.setBillNo(billInfo.getBillNo());
                accrualsBill.setTallyDate(billInfo.getTallydate());
                accrualsBill.setBillType(billInfo.getBillType());
                accrualsBill.setAccNo(billInfo.getAccNo());
                accrualsBill.setAccName(billInfo.getAccName());
                accrualsBill.setCurId(billInfo.getCur());
                accrualsBill.setStartDate(billInfo.getStartDate());
                accrualsBill.setEndDate(billInfo.getEndDate());
                accrualsBill.setAccrual(billInfo.getInterest());
                accrualsBill.setCumu(billInfo.getCumu());
                accrualsBill.setInterest(billInfo.getRate());
                accrualsBill.setDays(billInfo.getDays());
                accrualsBill.setOtherSubject(billInfo.getOtherSubject());
                accrualsBill.setHomeSubject(billInfo.getHomeSubject());
                accrualsBill.setRmk(billInfo.getRmk());
                accrualsBill.setRandom(billInfo.getRandom());
                accrualsBill.setEncrySource(billInfo.getEncrySource());
                accrualsBill.setValidateCode(billInfo.getValidcode());
                accrualsBill.setStatus(ReceiptConstant.LOAN_BILL_STATUS_INPUT);
                accrualsBill.setPrints(1);
                accrualsBill.setMoney(billInfo.getMoney());
                accrualsBill.setNummoney(billInfo.getNumMoney());
                accrualsBill.setEncryDate(new Date());
                accrualsBill.setOrigVoucherNo(origVoucherNo);
                accrualsBill.setOrigVoucherId(origVoucherId);
                accrualsBill.setSummaryContent(summaryContent);
                accrualsBill.setSubscriber(user.getUsername());
                accrualsBill.setVoucherNo(voucherNo);
                accrualsBill.setTallierCode(operatorCode);
                accrualsBill.setCheckerCode(checkerCode);
                accrualsBill.setTotolMoney(billInfo.getTotolmoney());
                accrualsBill.setRepayMoney(billInfo.getRepayMoney());
                accrualsBill.setCreateBy(user.getLoginName());
                accrualsBill.setCreateDate(new Date());
                eAccrualBillDao.save(accrualsBill);
                //处理电子回单号
                fcInterestService.processEReceiptNo(billInfo.getAccNo(), billInfo.getOrderNo(), billInfo.getBillNo());
                ebillMessageService.saveEBillMessage(billInfo.getBillNo(),
                        ReceiptConstant.BILL_MSG_OP_TYPE_CREATE,
                        ReceiptConstant.BILL_MSG_INFO_CREATE, user);
                ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
                        billInfo.getCorpId(), 1, user);
                eAccrualBillDao.flush();
            }
        }
    }
    /**
     * 创建利息清单电子回单
     *
     * @param orderNo           流水号
     * @param voucherNo         凭证号
     * @param accNo             账号
     * @param generateMode      利息明细产生方式
     * @param interestStartDate 开始日期
     * @param blnInstDate       结束日期
     * @param operatorCode      操作员
     * @param checkerCode       复核员
     */
    public void createAccrualsBill(String orderNo, String voucherNo,
                                   String accNo, String generateMode,
                                   Date interestStartDate, Date blnInstDate,
                                   String operatorCode, String checkerCode, ICurrentUser user) {
        if (StringUtils.isEmpty(orderNo)) {
            return;
        }
        //查询记账凭证明细信息设置电子回单要素
        List<ELoanBillInfoVo> accrualsInfoList = eAccrualBillDao.listInterestInfoByOrderNoAndAccNo(orderNo, accNo);
        ELoanBillInfoVo billInfo = new ELoanBillInfoVo();
        for (ELoanBillInfoVo accrualsInfo : accrualsInfoList) {
            billInfo.setTallydate(accrualsInfo.getTallydate());
            billInfo.setAccNo(accrualsInfo.getAccNo());
            billInfo.setStartDate(interestStartDate);
            billInfo.setEndDate(blnInstDate);
            billInfo.setInterest(accrualsInfo.getInterest());
            billInfo.setCumu(accrualsInfo.getCumu());
            billInfo.setRate(accrualsInfo.getRate());
            billInfo.setDays(accrualsInfo.getDays());
            billInfo.setRepayMoney(accrualsInfo.getRepayMoney());
            billInfo.setInterestDtlId(accrualsInfo.getInterestDtlId());
            billInfo.setOrderNo(accrualsInfo.getOrderNo());
            billInfo.setMoney(accrualsInfo.getMoney());
            ELoanBillInfoVo accInfo = eAccrualBillDao.listFcAccByAccNo(billInfo.getAccNo());
            if (accInfo != null) {
                billInfo.setCorpId(accInfo.getCorpId());
                billInfo.setAccName(accInfo.getAccName());
                billInfo.setAccTypeCode(accInfo.getAccTypeCode());
                billInfo.setPredrawSign(accInfo.getPredrawSign());
                billInfo.setCur(accInfo.getCur());
            }
            if (InnerAccountConstant.FCACC_LOANBYMANDATE.equals(billInfo.getAccTypeCode())) {
//				--委托贷款利息清单
                if (InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.getValue().equals(billInfo.getPredrawSign())
                        && InterestConstant.GENERATEMODE_INTEREST_BATCH.equals(String.valueOf(generateMode).toUpperCase())) {
                    return;
                } else {
                    billInfo.setRmk("上列预提利息信息,已照借记入帐户.");
                    billInfo.setHomeSubject("(借)科目：单位活期存款");
                    billInfo.setOtherSubject("单位活期存款");
                }
                billInfo.setDebitMoney(billInfo.getRepayMoney());
                if (billInfo.getRepayMoney() == null) {
                    billInfo.setDebitMoney(new BigDecimal(0));
                }
                billInfo.setBillType(ReceiptConstant.RECEIPT_BILL_TYPE_03);
            }
            billInfo.setNumMoney(billInfo.getDebitMoney().add(billInfo.getInterest()));
//			--合同金额
            ELoanBillInfoVo loanSendInfo = eAccrualBillDao.getloanSendMoneyByAccNo(billInfo.getAccNo());
            if (loanSendInfo != null) {
                billInfo.setTotolmoney(loanSendInfo.getMoney());
            } else {
                billInfo.setTotolmoney(new BigDecimal(0));
            }
//			--已还金额
            ELoanBillInfoVo loanRepayInfo = eAccrualBillDao.getloanRepayMoneyByAccNo(billInfo.getAccNo());
            if (loanRepayInfo != null) {
                billInfo.setRepayMoney(loanRepayInfo.getMoney());
            } else {
                billInfo.setRepayMoney(new BigDecimal(0));
            }
            billInfo.setBillNo(SequenceSingleton.getSequence(SequenceEnum.E_ACCRUAL_BILL_NO_I));
            billInfo.setRandom(ReceiptUtils.getBillRandom(billInfo.getBillNo()));
            billInfo.setValidcode(
                    ReceiptUtils.getBillValidcode(
                            billInfo.getAccNo(),
                            null,
                            billInfo.getDebitMoney(),
                            voucherNo,
                            billInfo.getRandom()));
            billInfo.setEncrySource(ReceiptUtils.getBillEncrySource(billInfo.getValidcode()));
            billInfo.setVoucherNo(voucherNo);
            //如果是作废凭证则判断是否已经生成电子回单修改旧状态和设置日志信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("interestDtlId", billInfo.getInterestDtlId());
            params.put("voucherNo", voucherNo);
            params.put("accNo", billInfo.getAccNo());
            params.put("notStatus", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
            params.put("updateBy", user.getLoginName());
            params.put("updateDate", DateUtils.getDate());
            if (isExists(params, user)) {
                //不存在进行入库
                EAccrualBill accrualsBill = new EAccrualBill();
                accrualsBill.setCorpId(billInfo.getCorpId());
                accrualsBill.setBillNo(billInfo.getBillNo());
                accrualsBill.setTallyDate(billInfo.getTallydate());
                accrualsBill.setBillType(billInfo.getBillType());
                accrualsBill.setAccNo(billInfo.getAccNo());
                accrualsBill.setAccName(billInfo.getAccName());
                accrualsBill.setCurId(billInfo.getCur());
                accrualsBill.setStartDate(billInfo.getStartDate());
                accrualsBill.setEndDate(billInfo.getEndDate());
                accrualsBill.setAccrual(billInfo.getInterest());
                accrualsBill.setCumu(billInfo.getCumu());
                accrualsBill.setInterest(billInfo.getRate());
                accrualsBill.setDays(billInfo.getDays());
                accrualsBill.setOtherSubject(billInfo.getOtherSubject());
                accrualsBill.setHomeSubject(billInfo.getHomeSubject());
                accrualsBill.setRmk(billInfo.getRmk());
                accrualsBill.setRandom(billInfo.getRandom());
                accrualsBill.setEncrySource(billInfo.getEncrySource());
                accrualsBill.setValidateCode(billInfo.getValidcode());
                accrualsBill.setStatus(ReceiptConstant.LOAN_BILL_STATUS_INPUT);
                accrualsBill.setPrints(1);
                accrualsBill.setMoney(billInfo.getMoney());
                accrualsBill.setNummoney(billInfo.getNumMoney());
                accrualsBill.setEncryDate(new Date());
                accrualsBill.setSubscriber(user.getUsername());
                accrualsBill.setVoucherNo(voucherNo);
                accrualsBill.setInterestDtlId(billInfo.getInterestDtlId());
                accrualsBill.setTallierCode(operatorCode);
                accrualsBill.setCheckerCode(checkerCode);
                accrualsBill.setTotolMoney(billInfo.getTotolmoney());
                accrualsBill.setRepayMoney(billInfo.getRepayMoney());
                accrualsBill.setCreateBy(user.getLoginName());
                accrualsBill.setCreateDate(new Date());
                eAccrualBillDao.save(accrualsBill);
                //处理电子回单号
                fcInterestService.processEReceiptNo(billInfo.getAccNo(), billInfo.getOrderNo(), billInfo.getBillNo());
                ebillMessageService.saveEBillMessage(billInfo.getBillNo(),
                        ReceiptConstant.BILL_MSG_OP_TYPE_CREATE,
                        ReceiptConstant.BILL_MSG_INFO_CREATE, user);
                ebillPrintService.saveEBillMessage(billInfo.getBillNo(),
                        billInfo.getCorpId(), 1, user);
                eAccrualBillDao.flush();
            }
        }
    }
    private boolean isExists(Map<String, Object> params, ICurrentUser user) {
        boolean flag = false;
        ELoanBillInfoVo printInfo = eAccrualBillDao.getPrintInfoByVoucherNo(params);
        if (printInfo != null) {
            if (printInfo.getPrints() > 1 && printInfo.getPrints2() > 1) {
                params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
                eAccrualBillDao.updateEAccrualBill(params);
                ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
                        ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
                        ReceiptConstant.BILL_MSG_INFO_CANCELL, user);
            } else if (printInfo.getPrints() == 1 && printInfo.getPrints2() > 1) {
                params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
                eAccrualBillDao.updateEAccrualBill(params);
                ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
                        ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
                        ReceiptConstant.BILL_MSG_INFO_CANCELL1, user);
            } else if (printInfo.getPrints() > 1 && printInfo.getPrints2() == 1) {
                params.put("status", ReceiptConstant.LOAN_BILL_STATUS_DELETE);
                eAccrualBillDao.updateEAccrualBill(params);
                ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
                        ReceiptConstant.BILL_MSG_OP_TYPE_CANCELL,
                        ReceiptConstant.BILL_MSG_INFO_CANCELL2, user);
            } else if (printInfo.getPrints() == 1 && printInfo.getPrints2() == 1) {
                eAccrualBillDao.deleteEAccrualBill(params);
                ebillMessageService.saveEBillMessage(printInfo.getOldBillno(),
                        ReceiptConstant.BILL_MSG_OP_TYPE_DELETE,
                        ReceiptConstant.BILL_MSG_INFO_DELETE, user);
                flag = true;
            }
        } else {
            flag = true;
        }
        return flag;
    }
    /**
     * 获取贷款利息单的本金
     * @param account
     * @param startDate
     * @return
     */
    public BigDecimal getMoney(String account, Date startDate){
        return this.eAccrualBillDao.getMoney(account,startDate);
    }
}
