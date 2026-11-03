package com.bytter.biz.finacial.interest.service.proccessor;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant.SysSubject;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.InterestProcessType;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.interest.bean.AccInstrateBean;
import com.bytter.biz.finacial.interest.bean.AccInterestInfo;
import com.bytter.biz.finacial.interest.bean.InstrateAccuBean;
import com.bytter.biz.finacial.interest.bean.InstrateAdjustBean;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.entity.FcLessPayBill;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookDtlVO;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookVO;
import com.bytter.biz.finacial.interest.vo.FcPredrawDtlTempVO;
import com.bytter.biz.finacial.interest.vo.InterestInfoContainer;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.FcAccDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
/**
 * <br>
 * <b>类功能：</b>放款计息适配器<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年8月29日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
@Transactional
@Service("interest_LoanInterestProvider")
public class LoanInterestProvider extends BaseInterestProvider implements Serializable {
    /**
     * 账户上次预提时间
     */
    private Map<String, Date> preMap = new HashMap<String, Date>();
    @Override
    public AccInterestInfo calcInterest(AccInterestInfo accInterestInfo) {
        InterestInfoContainer interestContainer = accInterestInfo.getContainer();
        sysRateType = interestContainer.getSysRateType();
        for (Entry<InterestProcessType, Map<String, List<FcAccuBalancef>>> entry : accInterestInfo
                .getInterestAccuBalances().entrySet()) {
            if (InterestProcessType.INTEREST.equals(entry.getKey())) {//计息
                accInterestInfo.getInterestDtls().addAll(
                        processInterestDtl(InterestProcessType.INTEREST,
                                accInterestInfo.getInterestAccuBalances().get(InterestProcessType.INTEREST),
                                interestContainer));
                if (OperateType.PART_INTEREST == accInterestInfo.getContainer().getOpType()
                        || OperateType.SUM == accInterestInfo.getContainer().getOpType()) {
                    continue;
                }
                createInterestVoucher("", accInterestInfo);
            } else {
                List<FcInterestDtlTemp> interestDtls = processInterestDtl(InterestProcessType.PREDRAW,
                        accInterestInfo.getInterestAccuBalances().get(InterestProcessType.PREDRAW), interestContainer);
                accInterestInfo.getPredrawDtls().addAll(processPredrawDtls(interestDtls, interestContainer));
            }
        }
        return accInterestInfo;
    }
    /**
     * <b>功能介绍：</b>处理利息明细<br>
     * <b>日期：</b>2017年8月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param interestProcessType
     * @param accAccuBalances
     * @param interestContainer
     * @return
     * @version 1.0
     */
    private List<FcInterestDtlTemp> processInterestDtl(InterestProcessType interestProcessType,
                                                       Map<String, List<FcAccuBalancef>> accAccuBalances, InterestInfoContainer interestContainer) {
        List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
        Calendar endInterestCalendar = Calendar.getInstance();//止息日期
        Date endInstDate = interestContainer.getEndInstDate();//止息日
        endInterestCalendar.setTime(endInstDate);
        endInterestCalendar.set(Calendar.HOUR, 0);
        endInterestCalendar.set(Calendar.MINUTE, 0);
        endInterestCalendar.set(Calendar.SECOND, 0);
        Map<String, Date> accDateMap = new HashMap<>();//账户计息日期集合
        if (null != accAccuBalances) {
            for (Entry<String, List<FcAccuBalancef>> entry : accAccuBalances.entrySet()) {
                String account = entry.getKey();
                Date interestStartDate = accDateMap.get(account);
                if (EmptyUtils.isEmpty(interestStartDate)) {
                    FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(account);//查询账户
                    interestStartDate = fcAccDTO.getInterestStartDate();//起息日期
                    accDateMap.put(account, interestStartDate);
                }
                Map<String, List<InstrateAccuBean>> accInstrateTypeAccuMap = new HashMap<String, List<InstrateAccuBean>>();
                AccInstrateBean rateInfo = null;
                Map<String, AccInstrateBean> accCurRates = new HashMap<String, AccInstrateBean>();
                for (FcAccuBalancef accuBalance : entry.getValue()) {
                    Date balanceDate = accuBalance.getBalanceDate();//余额日期
                    if(DateUtils.dayDiffs(DateUtils.parseDate(balanceDate), DateUtils.parseDate(interestStartDate)) < 0){
                        continue;
                    }
                    boolean flag = true;//上次预提时间判断
                    if (InterestProcessType.PREDRAW == interestProcessType) {
                        String preSign = accuBalance.getPreSign();
                        if (StringUtils.isNotEmpty(preSign) && FinacialConstant.BOOLEAN_TRUE.equals(preSign)) {
                            flag = false;//预提过，则不再预提
                        }
                    } else {
                        //结息,避免月末预提月初冲销参数下把最后一天积数计息了
                        if (DateUtils.date2Str(balanceDate, DateUtils.date_sdf).equals(DateUtils.date2Str(endInstDate, DateUtils.date_sdf))) {
                            continue;
                        }
                    }
                    if (flag) {
                        rateInfo = getAccInstrateInfo(accuBalance, interestContainer, accCurRates,
                                endInterestCalendar);
                        String instrtType = rateInfo.getInstrtType();
                        List<InstrateAccuBean> accInstrateTypeAccus = accInstrateTypeAccuMap.get(instrtType);
                        if (accInstrateTypeAccus == null) {
                            accInstrateTypeAccus = new ArrayList<InstrateAccuBean>();
                            accInstrateTypeAccuMap.put(instrtType, accInstrateTypeAccus);
                        }
                        accInstrateTypeAccus.add(new InstrateAccuBean(account, interestContainer.getInterestAcc().get(accuBalance.getAccount()), rateInfo
                                .getStartDate(), rateInfo.getInstrateTime(), rateInfo.getNewInstrate(),
                                getCumuByAccuBalance(accuBalance), accuBalance.getBalanceDate()));
                    }
                }
                interestDtls.addAll(packInterestDtls(accInstrateTypeAccuMap, interestContainer));
            }
        }
        Calendar calendar = Calendar.getInstance();
        for (FcInterestDtlTemp interestDtl : interestDtls) {
            // 利息 = 积数 X 日利率(利率/利率类型)
            interestDtl.setInterest(interestDtl.getCumulation().multiply(interestDtl.getInterestRate())
                    .divide(sysRateType, 2, BigDecimal.ROUND_HALF_UP));
            calendar.setTime(interestDtl.getInterestStartDate());
            calendar.add(Calendar.DAY_OF_MONTH, interestDtl.getDays() - 1);
            interestDtl.setBlnInstDate(calendar.getTime());
        }
        return transDtlTemps(interestDtls);
    }
    /**
     * <b>功能介绍：</b>获取账户利率信息<br>
     * <b>日期：</b>2017年8月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param accuBalance
     * @param interestContainer
     * @param accCurRates
     * @param endInterestCalendar
     * @return
     * @version 1.0
     */
    private AccInstrateBean getAccInstrateInfo(FcAccuBalancef accuBalance,
                                               InterestInfoContainer interestContainer, Map<String, AccInstrateBean> accCurRates,
                                               Calendar endInterestCalendar) {
        Map<String, InstrateAdjustBean> instrateAdjustInfos = interestContainer.getInstrateAdjustInfos();
        String account = accuBalance.getAccount();
        FcAccDTO fcAcc = interestContainer.getInterestAcc().get(account);
        AccInstrateBean rateInfo = null;
        Calendar deadlineDate = Calendar.getInstance();
        deadlineDate.setTime(new Date(fcAcc.getDeadlineDate().getTime()));//到期日期
        deadlineDate.set(Calendar.HOUR, 0);
        deadlineDate.set(Calendar.MINUTE, 0);
        deadlineDate.set(Calendar.SECOND, 0);
        Calendar balanceCalendar = Calendar.getInstance();
        balanceCalendar.setTime(accuBalance.getBalanceDate());//到期日期
        balanceCalendar.set(Calendar.HOUR, 0);
        balanceCalendar.set(Calendar.MINUTE, 0);
        balanceCalendar.set(Calendar.SECOND, 0);
        String instrtType = "";
        BigDecimal interestRate = new BigDecimal(0);
        if (balanceCalendar.compareTo(deadlineDate) >= 0) {//已到期
            //是否展期
            if (!StringUtils.isEmpty(String.valueOf(fcAcc.getDelayRate())) && fcAcc.getDelayDeadlineDate() != null) {//申请展期
                Calendar delayDeadlineCalendar = Calendar.getInstance();
                delayDeadlineCalendar.setTime(DateUtils.addDay(fcAcc.getDelayDeadlineDate(), -1));
                delayDeadlineCalendar.set(Calendar.HOUR, 0);
                delayDeadlineCalendar.set(Calendar.MINUTE, 0);
                delayDeadlineCalendar.set(Calendar.SECOND, 0);
                if (balanceCalendar.compareTo(delayDeadlineCalendar) > 0) {//展期已超期，逾期利率
                    instrtType = InterestConstant.INSTRT_TYPE_OVERDUE;
                    interestRate = fcAcc.getOverdueRate();
                } else { //展期利率
                    instrtType = InterestConstant.INSTRT_TYPE_DELAY;
                    interestRate = fcAcc.getDelayRate();
                }
            } else { //未申请展期（正常、逾期）
                instrtType = InterestConstant.INSTRT_TYPE_OVERDUE;
                interestRate = fcAcc.getOverdueRate();
            }
        } else {//未到期，按贷款利率计算利息
            instrtType = InterestConstant.INSTRT_TYPE_NORMAL;
            interestRate = fcAcc.getInterestRate();
        }
        InstrateAdjustBean instrateAdjustBean = instrateAdjustInfos.get(account);
        rateInfo = packRateInfo(instrateAdjustBean, accCurRates, interestRate, instrtType, accuBalance);
        return rateInfo;
    }
    /**
     * <b>功能介绍：</b>构建批量计息凭证<br>
     * <b>日期：</b>2017年8月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param commonSummary
     * @param accInterestInfo
     * @return
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> createInterestVoucher(String commonSummary, AccInterestInfo accInterestInfo) {
        boolean isAccInCenter = accInterestInfo.getContainer().isAccInCenter();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, List<FcInterestDtlTemp>> interestMap = new HashMap<String, List<FcInterestDtlTemp>>();
        List<FcInterestDtlTemp> interestDtls = accInterestInfo.getInterestDtls();
        if (EmptyUtils.isEmptyList(interestDtls) || interestDtls.size() == 0) {
            return list;
        }
        for (FcInterestDtlTemp interestDtl : interestDtls) {
            List<FcInterestDtlTemp> interestDtlsTemp = interestMap.get(interestDtl.getAccount());
            if (null == interestDtlsTemp) {
                interestDtlsTemp = new ArrayList<FcInterestDtlTemp>();
                interestMap.put(interestDtl.getAccount(), interestDtlsTemp);
            }
            interestDtlsTemp.add(interestDtl);
        }
        Map<String, BigDecimal> balMap = new ConcurrentHashMap<String, BigDecimal>();//扣息账户余额
        for (Entry<String, List<FcInterestDtlTemp>> entry : interestMap.entrySet()) {
            List<FcInterestDtlTemp> dtls = entry.getValue();
            if (dtls == null || dtls.size() == 0) {
                continue;
            }
            FcAccDTO creditAcc = null;
            FcAccDTO dtlAcc = iFcAccService.getAccDTOByAccount(dtls.get(0).getAccount());
            //int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
            //优先获取计息账号设置的利息相关户
            String creditIstAcc = getCreditAccout(dtlAcc, true);//获取计息账户的应收应付利息户
            if (EmptyUtils.isNotEmpty(creditIstAcc)) {
                creditAcc = iFcAccService.getAccDTOByAccount(creditIstAcc);//获取计息账户信息
            } else {
                if (isAccInCenter) {
                    String netCode = dtlAcc.getNetId();
                    creditAcc = findInterestCreditSysAcc(netCode, netCode, dtlAcc.getCurId());
                } else {
                    creditAcc = findInterestCreditSysAcc(null, dtlAcc.getCorpId(), dtlAcc.getCurId());
                }
            }
            Map<String, Object> map = createCommonVoucher(dtls, creditAcc, commonSummary, accInterestInfo, balMap);
            if (map != null) {
                list.add(map);
            }
        }
        boolean isMoreVoucherDtl = accInterestInfo.getContainer().isMoreVoucherDtl();
        String opUserId = interestDtls.get(0).getOperatorCode();
        if (isMoreVoucherDtl) {
            List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
            Date tallyDate = accInterestInfo.getContainer().getTallyDate();
            FcFinacialAccbookVO accbook = initAccbook(tallyDate, false,accInterestInfo.getContainer().getNetCode(),
                    /**accInterestInfo.getContainer().getUserBean().getId()*/"", opUserId, accInterestInfo.isPoolSign(), accInterestInfo.getAccBean());
            List<FcFinacialAccbookDtlVO> accbookDtls = new ArrayList<FcFinacialAccbookDtlVO>();
            List<FcInterestDtlTemp> interestDtlTemps = new ArrayList<FcInterestDtlTemp>();
            BigDecimal creditMoney = BigDecimal.ZERO;
            for (int i = 0, size = list.size(); i < size; i++) {
                accbook.setFcAccbookDtls(new ArrayList<FcFinacialAccbookDtlVO>());
                Map<String, Object> map = list.get(i);
                String opType = (String) map.get("opType");
                interestDtlTemps.addAll((List<FcInterestDtlTemp>) map.get("interestDtls"));
                if (!"1".equals(opType)) {//表内
                    FcFinacialAccbookVO accbookTemp = (FcFinacialAccbookVO) map.get("accbook");
                    List<FcFinacialAccbookDtlVO> bookDtls = accbookTemp.getFcAccbookDtls();
                    if (accbookDtls.isEmpty() || accbookDtls.size() == 0) {
                        accbookDtls.addAll(bookDtls);
                        creditMoney = creditMoney.add(bookDtls.get(0).getCreditMoney()).add(bookDtls.get(0).getDebitMoney());
                    } else {
                        //累加企业活期
                        for (FcFinacialAccbookDtlVO fcFinacialAccbookDtl : bookDtls) {
                            if (fcFinacialAccbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) != 0) {
                                creditMoney = creditMoney.add(fcFinacialAccbookDtl.getDebitMoney());
                                accbookDtls.add(fcFinacialAccbookDtl);
                            }
                        }
                    }
                } else {//表外
                    resultList.add(map);
                }
            }
            for (FcFinacialAccbookDtlVO bookAccbookDtl : accbookDtls) {
                if (bookAccbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                    bookAccbookDtl.setCreditMoney(creditMoney);
                }
            }

            accbook.setFcAccbookDtls(accbookDtls);//多张凭证
            Map<String, Object> bookMap = new HashMap<String, Object>();
            bookMap.put("opType", "0");//表内凭证
            bookMap.put("accbook", accbook);//表内凭证
            bookMap.put("interestDtls", interestDtlTemps);//表内凭证
            resultList.add(bookMap);
            return resultList;
        }
        return list;
    }
    /**
     * <b>功能介绍：</b>构建凭证明细<br>
     * <b>日期：</b>2017年8月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param interestDtls
     * @param creditAcc
     * @param commonSummary
     * @param accInterestInfo
     * @param balMap
     * @return
     * @version 1.0
     */
    private Map<String, Object> createCommonVoucher(List<FcInterestDtlTemp> interestDtls, FcAccDTO creditAcc,
                                                    String commonSummary, AccInterestInfo accInterestInfo, Map<String, BigDecimal> balMap) {
        Date tallyDate = accInterestInfo.getContainer().getTallyDate();
        Calendar tallyDateCan = Calendar.getInstance();
        tallyDateCan.setTime(tallyDate);
        String sendSummary = getIMultiLangService().getLang("cms.loan.send");//放款
        commonSummary = (tallyDateCan.get(Calendar.MONTH) + 1) + getIMultiLangService().getLang(InterestConstant.FINACIAL_INTEREST_MONTH) + sendSummary;
        String interestSummary = getIMultiLangService().getLang(InterestConstant.FINACIAL_INTEREST_SUMMARY);//结息
        String accSummary = getIMultiLangService().getLang(InterestConstant.FINACIAL_INTEREST_ACCOUNT);//账号
//        ShiroUser userBean = accInterestInfo.getContainer().getUserBean();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("interestDtls", interestDtls);
        FcAccDTO fcAcc = null;
        BigDecimal interest = BigDecimal.ZERO;
        Date endInstDate = null;
        String opUserId = "";
        if (!interestDtls.isEmpty()) {
            endInstDate = interestDtls.get(0).getBlnInstDate();
            opUserId = interestDtls.get(0).getOperatorCode();//计息提交人
            for (FcInterestDtlTemp interestDtl : interestDtls) {
                interest = interest.add(interestDtl.getInterest());
                fcAcc = iFcAccService.getAccDTOByAccount(interestDtl.getAccount());
                if (endInstDate.compareTo(interestDtl.getBlnInstDate()) < 0) {
                    endInstDate = interestDtl.getBlnInstDate();
                }
            }
        }
        if (interest.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        if (SubjectConstant.SysSubject.INTEREST_NOT_ACCURED_LOAN.get().equals(iFcSubjectService.findSubjectById(fcAcc.getSubjectId()).getSubjectCode())) {//转非应计贷款计息，记表外待收利息
            //FIXME 表外记账....
        } else { // 放款账户计息
            String opType = "0";
            String accForDeduct = fcAcc.getAccForDeduct();
            FcAccDTO debitAcc = null;
            //TODO 扣息账号没有设置,根据系统参数规则查找扣息活期账号
            if (StringUtils.isEmpty(accForDeduct)) {
                debitAcc = getCurrentAcc(null, fcAcc.getCorpId(), fcAcc.getCurId(), fcAcc);
            } else {
                debitAcc = iFcAccService.getAccDTOByAccount(accForDeduct);//扣息账号
            }
            if (debitAcc == null) {//单位{0}下的放款账户{1},没有对应的扣息活期账号！
                logger.debug("单位[" + fcAcc.getCorpName() + "]下的放款账户[" + fcAcc.getBlnctrAcc() + "],没有对应的扣息活期账号！");
                throw new ParameterException(InterestMessageEnum.FIN_INTEREST_CORPLOANNOTACCFORDEDUCT, fcAcc.getCorpName(), fcAcc.getBlnctrAcc());
            }
            String overDraftSign = debitAcc.getOverdraftSign();//透支标志
            BigDecimal accBalance = balMap.get(debitAcc.getBlnctrAcc());
            if (null == accBalance) {
                List<String> accs = new ArrayList<String>();
                accs.add(debitAcc.getBlnctrAcc());
                FcOnlineLedgerDTO onledger = iFcOnlineLedgerService.getLedgerDTOsByAccSet(accs).get(0);
                accBalance = onledger.getCheckBalance().subtract(onledger.getFreezeMoney());//扣息活期账号余额
            }
            boolean balanceFlag = false;
            if (InnerAccountConstant.OVERDRAFT_SIGN_N.equals(overDraftSign)) {//不允许
                balanceFlag = accBalance.compareTo(interest) >= 0;
            } else if (InnerAccountConstant.OVERDRAFT_SIGN_R.equals(overDraftSign)) {//受限透支
                BigDecimal overDraftMoney = debitAcc.getOverdraftMoney();
                balanceFlag = accBalance.add(overDraftMoney).compareTo(interest) >= 0;
            } else if (InnerAccountConstant.OVERDRAFT_SIGN_D.equals(overDraftSign)) {//不受限透支
                balanceFlag = true;
            }
            List<FcFinacialAccbookDtlVO> accbookDtls = new ArrayList<FcFinacialAccbookDtlVO>();
            FcVoucherTypeDTO voucherType = getVoucherBySysVoucherCode();
            String debitSummary = commonSummary + interestSummary + "，" + accSummary + "：" + fcAcc.getBlnctrAcc()
                    + " " + fcAcc.getAccName();
            if (balanceFlag) { //企业活期存款(扣息账号)
                accbookDtls.add(createAccbookDtl(debitAcc, debitSummary, interest, BigDecimal.ZERO, voucherType, /*userBean.getId()*/""));
                for (FcInterestDtlTemp interestDtl : interestDtls) { //扣息则表示已全部归还
                    interestDtl.setRepayMoney(interestDtl.getInterest());
                }
                opType = "2";//正常
                balMap.put(debitAcc.getBlnctrAcc(), accBalance.subtract(interest));//扣减利息后的余额
            } else { //不够扣息，记应收利息科目
                opType = "0";//不够扣息
                FcAccDTO needInterestAcc = this.findNeedInterestAcc(fcAcc.getBlnctrAcc());
                //修改放款摘要信息
                accbookDtls
                        .add(createAccbookDtl(needInterestAcc, debitSummary, interest, BigDecimal.ZERO, voucherType, /*userBean.getId()*/""));
                FcLessPayBill lessPayBill = new FcLessPayBill();
//                lessPayBill.setId(InterestHelper.createGlobalId());
                lessPayBill.setAccount(fcAcc.getBlnctrAcc());
                lessPayBill.setNeedinterestAcc(needInterestAcc.getBlnctrAcc());//应收利息账号
                lessPayBill.setInterest(interest);
                lessPayBill.setBalance(accBalance);
                lessPayBill.setCurCode(fcAcc.getCurId());
                lessPayBill.setOperatorCode(/*userBean.getId()*/"");
                lessPayBill.setCreateBy(/*userBean.getUsername()*/"");
                lessPayBill.setEnterDate(DateUtils.getDate());
                lessPayBill.setStatus(CommonConstant.STATUE_WAIT_CHECK);
                lessPayBill.setEndInstDate(endInstDate);
                accInterestInfo.getLessPayBills().add(lessPayBill);
                balMap.put(debitAcc.getBlnctrAcc(), accBalance);//扣应收，扣息账户余额不变
            }
            accbookDtls.add(createAccbookDtl(creditAcc, commonSummary + interestSummary, BigDecimal.ZERO, interest, voucherType,/*userBean.getId()*/""));
            FcFinacialAccbookVO accbook = initAccbook(tallyDate, false,fcAcc.getNetId(),/*userBean.getId()*/"", opUserId,
                    accInterestInfo.isPoolSign(), accInterestInfo.getAccBean());
            accbook.setFcAccbookDtls(accbookDtls);
            accbook.setOperatorCode(/**accInterestInfo.getContainer().getUserBean().getId()*/"");
            map.put("opType", opType);
            map.put("accbook", accbook);
        }
        return map;
    }
    /**
     * <b>功能介绍：</b>查找应收利息账户<br>
     * <b>日期：</b>2017年8月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param loanAcc
     * @return
     * @version 1.0
     */
    private FcAccDTO findNeedInterestAcc(String loanAcc) {
        FcAccDTO loanFcAcc = iFcAccService.getAccDTOByAccount(loanAcc);
        Set<String> corpCodes = new HashSet<String>();
        corpCodes.add(loanFcAcc.getCorpId());
//		List<FcSubjectDTO> subs = iFcSubjectService.findSubjectBySysCode(SubjectConstant.SysSubject.INTEREST_PAYABLE_OWE_INNERACC.get(),CommonConstant.VALID_SIGN_YES);
		/*FcAccDTO acc = fcInterestDao.findNeedInterestAcc(loanFcAcc.getCorpId(), InnerAccountConstant.FCACC_INNER_ACCOUNT,
				subs.get(0).getId());*/
        List<FcAccDTO> accs = iFcAccService.getFcAccBySysSubjectId(SubjectConstant.SysSubject.INTEREST_PAYABLE_OWE_INNERACC.get(), null, corpCodes,
                loanFcAcc.getCurId());
        if (EmptyUtils.isEmptyList(accs)) {
            logger.debug("单位[" + loanFcAcc.getCorpName() + "]没有对应的[" + SubjectConstant.SysSubject.INTEREST_PAYABLE_OWE_INNERACC.getName() + "]账号！");
            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_CORPLOANNOTACC, loanFcAcc.getCorpName(), loanAcc, SubjectConstant.SysSubject.INTEREST_PAYABLE_OWE_INNERACC.getName());
        }
        return accs.get(0);
    }
    /**
     * <b>功能介绍：</b>表外待收利息账户<br>
     * <b>日期：</b>2017年8月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param loanAcc
     * @return
     * @version 1.0
     */
    @SuppressWarnings("unused")
    private FcAccDTO findWaitInterestAcc(String loanAcc) {
        FcAccDTO acc = fcInterestDao.findNeedInterestAcc(loanAcc, InnerAccountConstant.FCACC_OFFBALANCE,
                iFcSubjectService.findSubjectByCode(SubjectConstant.SysSubject.INTEREST_WAIT_LOAN.get()).getId());
        if (acc == null) {
            logger.debug("放款账户[" + loanAcc + "]没有对应的表外待收利息账户");
            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_LOANNOTOUTACC, loanAcc);
        }
        return acc;
    }
    /**
     * <b>功能介绍：</b>预提信息<br>
     * <b>日期：</b>2017年8月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param interestDtls
     * @param interestContainer
     * @return
     * @version 1.0
     */
    protected List<FcPredrawDtlTempVO> processPredrawDtls(List<FcInterestDtlTemp> interestDtls,
                                                          InterestInfoContainer interestContainer) {
        Map<String, FcPredrawDtlTempVO> predrawDtls = new HashMap<String, FcPredrawDtlTempVO>();
        for (FcInterestDtlTemp interestDtl : interestDtls) {
            interestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_PRE);
            FcPredrawDtlTempVO predrawDtl = predrawDtls.get(interestDtl.getAccount());
            if (predrawDtl == null) {
                predrawDtl = createPredrawDtl(interestContainer);
                predrawDtl.setAccount(interestDtl.getAccount());
                predrawDtl.setCurCode(interestDtl.getCurCode());
            }
            predrawDtl.getInterestDtls().add(interestDtl);
            predrawDtl.setInterest(predrawDtl.getInterest().add(interestDtl.getInterest()));
            predrawDtls.put(interestDtl.getAccount(), predrawDtl);
        }
        return new ArrayList<FcPredrawDtlTempVO>(predrawDtls.values());
    }
    public void processInterestVoucher(AccInterestInfo accInterestInfo) {
        final List<FcInterestDtlTemp> interestDtls = accInterestInfo.getInterestDtls();
        if (null == interestDtls || interestDtls.size() == 0) {
            return;
        }
        Calendar tallyDate = Calendar.getInstance();
        tallyDate.setTime(accInterestInfo.getContainer().getTallyDate());
        String commonSummary = (tallyDate.get(Calendar.MONTH) + 1) + "月利息";
        List<Map<String, Object>> list = this.createInterestVoucher(commonSummary, accInterestInfo);
        try {
            saveInterestVoucher(accInterestInfo, list);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof ParameterException) {
                ParameterException new_name = (ParameterException) e;
                throw new_name;
            } else {
                throw new ParameterException(InterestMessageEnum.FIN_INTEREST_PROCESSVOUCHERFAILURE);
            }
        }
    }
    /**
     * <b>功能介绍：</b>保存凭证<br>
     * <b>日期：</b>2017年8月30日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param accInterestInfo
     * @param list
     * @throws Exception
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    private void saveInterestVoucher(AccInterestInfo accInterestInfo, List<Map<String, Object>> list) throws Exception {
        List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
        for (int i = 0, size = list.size(); i < size; i++) {
            Map<String, Object> map = list.get(i);
            interestDtls = (List<FcInterestDtlTemp>) map.get("interestDtls");
            if (interestDtls.isEmpty()) {
                return;
            }
            String opType = (String) map.get("opType");
            if (!"1".equals(opType)) {
                List<FcFinacialAccbookVO> accbooks = new ArrayList<FcFinacialAccbookVO>();
                accbooks.add((FcFinacialAccbookVO) map.get("accbook"));
                saveVoucher(accbooks, interestDtls);
            } else {
                //FIXME 保存表外记账
            }
        }
    }
    @SuppressWarnings("unchecked")
    public void createInterestVoucher(AccInterestInfo accInterestInfo) {
        final List<FcInterestDtlTemp> interestDtls = accInterestInfo.getInterestDtls();
        if (null == interestDtls || interestDtls.size() == 0) {
            return;
        }
        Calendar tallyDate = Calendar.getInstance();
        tallyDate.setTime(interestDtls.get(0).getTallyDate());
        String commonSummary = (tallyDate.get(Calendar.MONTH) + 1) + "月利息";
//        ShiroUser userBean = new ShiroUser();
//        userBean.setUsername("");
//        accInterestInfo.getContainer().setUserBean(userBean);
        List<Map<String, Object>> list = this.createInterestVoucher(commonSummary, accInterestInfo);
        String opUserId = interestDtls.get(0).getOperatorCode();//计息提交人
        List<FcFinacialAccbookVO> accbooks = new ArrayList<FcFinacialAccbookVO>();
        for (int i = 0, size = list.size(); i < size; i++) {
        	FcFinacialAccbookVO accbook = initAccbook(tallyDate.getTime(), false,accInterestInfo.getContainer().getNetCode(),
                        /**accInterestInfo.getContainer().getUserBean().getId()*/"", opUserId, accInterestInfo.isPoolSign(), accInterestInfo.getAccBean());
            accbook.setFcAccbookDtls(new ArrayList<FcFinacialAccbookDtlVO>());
            Map<String, Object> map = list.get(i);
            String opType = (String) map.get("opType");
            List<FcInterestDtlTemp> interestDtlTemps = (List<FcInterestDtlTemp>) map.get("interestDtls");
            if (interestDtlTemps.isEmpty()) {
                return;
            }
            if (!"1".equals(opType)) {
                FcFinacialAccbookVO accbookTemp = (FcFinacialAccbookVO) map.get("accbook");
                accbooks.add(accbookTemp);
            } else {//表外
            }
        }
        accInterestInfo.setInterestAccbook(accbooks);
        //按照账号排序
        sortInterestVoucher(accInterestInfo);
    }
    /**
     * <b>功能介绍：</b>利息借方账号<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param corpCode
     * @param curCode
     * @return
     * @version 1.0
     */
    protected FcAccDTO findInterestDebitSysAcc(String corpCode, String curCode) {
        Set<String> corpCodes = new HashSet<String>();
        corpCodes.add(corpCode);
        FcAccDTO creditSysAcc = findAccBySysSubjectCode(SysSubject.INTEREST_PAYABLE_LOAN.get(), null, corpCodes,
                curCode, SysSubject.INTEREST_PAYABLE_LOAN.getName(), null, corpCode, null).get(0);
        if (creditSysAcc == null) {
            String corpName = iCorpService.get(corpCode).getName();//获取单位名称
            String currentName = "";
            if (StringUtils.isNotEmpty(curCode)) {
                currentName = iBtCurrencyService.getObjectById(curCode).getCurName();
            }
            logger.debug("单位[" + corpName + "],币别[" + currentName + "],没有开设[" + SysSubject.INTEREST_PAYABLE_LOAN.getName() + "]账户");
            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_CORPCURNOTACC, corpName, currentName,
                    SysSubject.INTEREST_PAYABLE_LOAN.getName());
        }
        return creditSysAcc;
    }
    /**
     * 获取利息收入企业放款账户
     */
    protected FcAccDTO findInterestCreditSysAcc(String netCode, String corpCode, String curCode) {
        Set<String> corpCodes = new HashSet<String>();
        corpCodes.add(corpCode);
        FcAccDTO creditSysAcc = findAccBySysSubjectCode(SysSubject.INTEREST_EXPENSES_LOAN.get(), netCode, corpCodes,
                curCode, SysSubject.INTEREST_EXPENSES_LOAN.getName(), null, corpCode, null).get(0);
        if (creditSysAcc == null) {//单位{0},币别{1},没有开设{2}账户。
            String corpName = iCorpService.get(corpCode).getName();//获取单位名称
            String currentName = "";
            if (StringUtils.isNotEmpty(curCode)) {
                currentName = iBtCurrencyService.getObjectById(curCode).getCurName();
            }
            logger.debug("单位[" + corpName + "],币别[" + currentName + "],没有开设[" + SysSubject.INTEREST_EXPENSES_LOAN.getName() + "]账户。");
            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_CORPCURNOTACC, corpName, currentName,
                    SysSubject.INTEREST_EXPENSES_LOAN.getName());
        }
        return creditSysAcc;
    }
    /**
     * <br>
     * <b>功能：</b>预提借方系统科目/查找单位应收利息-企业放款<br>
     * <b>作者：</b>李又虎<br>
     * <b>日期：</b> 2013-2-25 <br>
     *
     * @return
     */
    @Override
    public ConstantBean<String> getCreditSysAcc() {
        return SysSubject.INTEREST_EXPENSES_LOAN;
    }
    /**
     * <br>
     * <b>功能：</b>贷方系统科目/查找单位利息收入-企业放款账户<br>
     * <b>作者：</b>李又虎<br>
     * <b>日期：</b> 2013-2-25 <br>
     *
     * @return
     */
    @Override
    public ConstantBean<String> getDebitSysAcc() {
        return SysSubject.INTEREST_PAYABLE_LOAN;
    }
}
