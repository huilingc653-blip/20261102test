package com.bytter.biz.finacial.interest.service.proccessor;
import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.enums.interest.InterestProcessType;
import com.bytter.biz.finacial.interest.bean.*;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookVO;
import com.bytter.biz.finacial.interest.vo.FcPredrawDtlTempVO;
import com.bytter.biz.finacial.interest.vo.InterestInfoContainer;
import com.bytter.increment.api.dto.FcAgreedDepositDTO;
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
/**
 * 企业协定存款计息适配器
 */
@Transactional
@Service("interest_AgreedInterestProvider")
public class AgreedInterestProvider extends BaseInterestProvider implements Serializable {
    private Map<String, Date> preMap = new HashMap<String, Date>();
    /**
     * 计算利息
     */
    public AccInterestInfo calcInterest(AccInterestInfo accInterestInfo) {
        Map<String, InstrateAdjustBean> instrateAdjustInfos = accInterestInfo.getContainer().getInstrateAdjustInfos();
        sysRateType = accInterestInfo.getContainer().getSysRateType();
        // 根据不同计息处理类型进行计息
        List<FcInterestDtlTemp> interestDtls = null;
        for (Entry<InterestProcessType, Map<String, List<FcAccuBalancef>>> entry : accInterestInfo
                .getInterestAccuBalances().entrySet()) {
            if (entry.getKey() == InterestProcessType.PREDRAW) {
            }
            interestDtls = processInterestDtl(entry.getKey(), entry.getValue(), instrateAdjustInfos,
                    accInterestInfo.getContainer());
            if (entry.getKey() == InterestProcessType.INTEREST) {//结息计息处理
                accInterestInfo.setInterestDtls(interestDtls);
            } else {//预提计息处理
                accInterestInfo.setPredrawDtls(processPredrawDtls(interestDtls, accInterestInfo.getContainer()));
            }
        }
        return accInterestInfo;
    }
    /**
     * <b>功能介绍：</b>根据计息积数产生利息明细<br>
     * <b>日期：</b>2017年7月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private List<FcInterestDtlTemp> processInterestDtl(InterestProcessType interestProcessType,
                                                       Map<String, List<FcAccuBalancef>> accAccuBalances, Map<String, InstrateAdjustBean> instrateAdjustInfos,
                                                       InterestInfoContainer interestContainer) {
        List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
        Map<String, String> agreePreDateMap = new HashMap<String, String>(); //协定日期积数段
        String account = null, instrtType = null;
        Map<String, AccInstrateBean> currentAccRateMap = null;
        Map<String, List<InstrateAccuBean>> accInstrateTypeAccuMap = null;
        Map<String, FcInterestDtlTemp> agreedInterestDtls = null;
        List<InstrateAccuBean> accInstrateTypeAccus = null;
        Date endInstDate = interestContainer.getEndInstDate();//止息日
        for (Entry<String, List<FcAccuBalancef>> entry : accAccuBalances.entrySet()) {
            account = entry.getKey();
            currentAccRateMap = new HashMap<String, AccInstrateBean>();
            accInstrateTypeAccuMap = new HashMap<String, List<InstrateAccuBean>>();
            agreedInterestDtls = new HashMap<String, FcInterestDtlTemp>();
            for (FcAccuBalancef accuBalance : entry.getValue()) {
                Date balanceDate = accuBalance.getBalanceDate();//余额日期
                boolean flag = true;//上次预提时间判断
                if (InterestProcessType.PREDRAW == interestProcessType) {
                    String preSign = accuBalance.getPreSign();
                    if (StringUtils.isNotEmpty(preSign) && FinacialConstant.BOOLEAN_TRUE.equals(preSign)) {
                        flag = false;//预提过，则不再预提
                    }
                } else {
                    //结息,避免月末预提月初冲销参数下把最后一天积数计息了
                    if (DateUtils.date2Str(balanceDate, DateUtils.date_sdf()).equals(DateUtils.date2Str(endInstDate, DateUtils.date_sdf()))) {
                        continue;
                    }
                }
                if (!flag) {
                    continue;
                }
                if (EmptyUtils.isNotEmpty(accuBalance.getAgreedDepositSign()) &&
                        CloseConstant.AGREE_SIGN_YES.equals(accuBalance.getAgreedDepositSign())) {//协定利息处理
                    //TODO 协定利息暂不处理
                    String agreedKey = accuBalance.getAgreedDepositId() + "_" + accuBalance.getAgreedType();
                    FcInterestDtlTemp agreedInterestDtl = agreedInterestDtls.get(agreedKey);
                    if (agreedInterestDtl == null) {
                        FcAgreedDepositDTO agreeDtl = agreedDepositService.getChoAgreeInfoByAgrAcc(accuBalance.getAccount(),balanceDate);
                        agreedInterestDtl = createInterestDtl(interestContainer);
                        agreedInterestDtl.setAccount(accuBalance.getAccount());
                        agreedInterestDtl.setCurCode(accuBalance.getCurCode());//币别
                        agreedInterestDtl.setInterestType(InterestConstant.INSTRT_TYPE_AGREEMENT);//协定
                        agreedInterestDtl.setInterestRate(agreeDtl.getAgrInterest());//协定利率
                        agreedInterestDtl.setInterestStartDate(accuBalance.getBalanceDate());
                        agreedInterestDtl.setAccount(accuBalance.getAccount());
                        agreedInterestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_IST);//计息标识
                        interestDtls.add(agreedInterestDtl);
                        agreedInterestDtls.put(agreedKey, agreedInterestDtl);
                    }
                    /**
                     * 协定积数根据日期+协定段    标识一天只能有一个协定积数
                     */
                    String balanceDateStr = DateUtils.formatDate(balanceDate);
                    String preKey = balanceDateStr + agreedKey;
                    String preValue = agreePreDateMap.get(preKey);//防止一天的协定积数(反日结调整账务)分多天计算
                    if (EmptyUtils.isEmpty(preValue)) {
                        agreedInterestDtl.setDays(agreedInterestDtl.getDays() + 1);
                        agreePreDateMap.put(preKey, preKey);
                    }
                    agreedInterestDtl.setCumulation(agreedInterestDtl.getCumulation().add(getCumuByAccuBalance(accuBalance)));
                }
            }
            interestDtls.addAll(packInterestDtls(accInstrateTypeAccuMap, interestContainer));
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
     * <b>功能介绍：</b>根据利息明细产生预提明细<br>
     * <b>日期：</b>2017年7月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private List<FcPredrawDtlTempVO> processPredrawDtls(List<FcInterestDtlTemp> interestDtls,
                                                        InterestInfoContainer interestContainer) {
        Map<String, FcPredrawDtlTempVO> accPredrawMap = new HashMap<String, FcPredrawDtlTempVO>();
        FcPredrawDtlTempVO predrawDtl = null;
        for (FcInterestDtlTemp interestDtl : interestDtls) {
            interestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_PRE);
            predrawDtl = accPredrawMap.get(interestDtl.getAccount());
            if (predrawDtl == null) {
                predrawDtl = createPredrawDtl(interestContainer);
                predrawDtl.setAccount(interestDtl.getAccount());
                predrawDtl.setInterestTime(interestDtl.getInterestTime());
                predrawDtl.setCurCode(interestDtl.getCurCode());
                accPredrawMap.put(predrawDtl.getAccount(), predrawDtl);
            }
            predrawDtl.getInterestDtls().add(interestDtl);
            predrawDtl.setInterest(predrawDtl.getInterest().add(interestDtl.getInterest()));
        }
        return new ArrayList<FcPredrawDtlTempVO>(accPredrawMap.values());
    }
    /**
     * <b>功能介绍：</b>获取账户利率信息<br>
     * <b>日期：</b>2017年7月9日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private AccInstrateBean getAccInstrateInfo(FcAccuBalancef accuBalance,
                                               Map<String, InstrateAdjustBean> instrateAdjustInfos, Map<String, AccInstrateBean> currentAccRateMap, Map<String, FcAccDTO> accList) {
        FcAccDTO accDTO = accList.get(accuBalance.getAccount());
        InstrateAdjustBean instrateAdjustBean = instrateAdjustInfos.get(accuBalance.getAccount());
        BigDecimal totalCumu = queryAccuBalanceByDate(accuBalance.getAccount(), accuBalance.getBalanceDate(), accDTO.getInterestStartDate());
        
        BigDecimal acumu = getCumuByAccuBalance(accuBalance);
        AccInstrateBean rateInfo = null;
        BigDecimal interestRate = new BigDecimal(0);
        BigDecimal overdraftRate = new BigDecimal(0);
        if (accList.get(accuBalance.getAccount()).getInterestRate() != null) {
            interestRate = accDTO.getInterestRate();
        }
        if (accList.get(accuBalance.getAccount()).getOverdraftRate() != null) {
            overdraftRate = accDTO.getOverdraftRate();
        }
        if (totalCumu.doubleValue() < 0) {//透支利率  之前是acumu
            rateInfo = packRateInfo(instrateAdjustBean, currentAccRateMap, overdraftRate,
                    InterestConstant.INSTRT_TYPE_OVERDRAFT, accuBalance);
        } else {//正常利率
            rateInfo = packRateInfo(instrateAdjustBean, currentAccRateMap, interestRate,
                    InterestConstant.INSTRT_TYPE_NORMAL, accuBalance);
        }
        return rateInfo;
    }
    /**
     * <b>功能介绍：</b>组装账户利率信息<br>
     * <b>日期：</b>2017年7月9日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Override
    protected AccInstrateBean packRateInfo(InstrateAdjustBean instrateAdjustBean,
                                           Map<String, AccInstrateBean> currentAccRateMap, BigDecimal instrtTypeRate, String instrtType,
                                           FcAccuBalancef accuBalance) {
        AccInstrateBean currRate = currentAccRateMap.get(instrtType);
        if (currRate != null) {
            Date curRateDate = currRate.getStartDate();
            if (accuBalance.getBalanceDate().compareTo(curRateDate) < 0) {//假如计算的日期比使用的利率时间小的话，则使用当前的利率
                return currRate;
            }
        }
        boolean isAdjust = false;
        List<AccInstrateBean> adjustRates = null;
        if (instrateAdjustBean != null) {//判断是否存在调整利率
            adjustRates = instrateAdjustBean.getAccInstratesByAdjustType(instrtType);
            if (adjustRates != null && !adjustRates.isEmpty()) {
                isAdjust = true;
            }
        }
        AccInstrateBean rateInfo = null;
        if (isAdjust) {//存在调整利率的情况
            rateInfo = getRateByAdjust(accuBalance.getBalanceDate(), adjustRates, currRate);
            rateInfo.setInstrtType(instrtType);
            rateInfo.setAccount(accuBalance.getAccount());
        } else {
            Date startDate;
            Date instrateTime;
            if (currRate == null) {
                startDate = accuBalance.getBalanceDate();
                instrateTime = accuBalance.getBalanceDate();
                rateInfo = new AccInstrateBean(accuBalance.getAccount(), instrtType, instrateTime, startDate, instrtTypeRate,
                        null, "1");
            } else {
                startDate = currRate.getStartDate();
                instrateTime = currRate.getInstrateTime();
                //如果上次积数的日期和本次的积数日期相差 > 1,则用新的开始日期计算
              /*  if (dayDiffs(accuBalance.getBalanceDate(), instrateTime) != 1) {
                    startDate = accuBalance.getBalanceDate();
                    instrateTime = accuBalance.getBalanceDate();
                    //startDate = instrateTime;
                } else {*/
                    instrateTime = DateUtils.addDay(instrateTime, 1);
                /*}*/
                rateInfo = new AccInstrateBean(accuBalance.getAccount(), instrtType, instrateTime, startDate, instrtTypeRate,
                        null, currRate.getAdjustId(), currRate.getChangeType(), currRate.getZoneBeans());
            }
        }
        currentAccRateMap.put(instrtType, rateInfo);
        return rateInfo;
    }
    /**
     * <b>功能介绍：</b>有利率调整时获取账户的调整利率<br>
     * <b>日期：</b>2017年7月9日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Override
    protected AccInstrateBean getRateByAdjust(Date balanceDate, List<AccInstrateBean> adjustRates,
                                              AccInstrateBean currRate) {
        BigDecimal rate = null;
        Date rateDate = null;
        int i = 0;
        int adjustSize = adjustRates.size();
        String changeType = "1";
        List<AccInstrateZoneBean> zoneBeans = new ArrayList<AccInstrateZoneBean>();
        for (Iterator<AccInstrateBean> it = adjustRates.iterator(); it.hasNext(); ) {
            AccInstrateBean adjustRateInfo = it.next();
            BigDecimal newInstrate = adjustRateInfo.getNewInstrate();
            BigDecimal oldInstrate = adjustRateInfo.getOldInstrate();
            Date adjustTime = adjustRateInfo.getInstrateTime();
            int maxIndex = adjustRates.size() - 1;
            if (balanceDate.compareTo(adjustTime) < 0) {
                rate = oldInstrate;
                if (currRate != null) {
                    rateDate = currRate.getStartDate();
                    if (rateDate.compareTo(balanceDate) >= 0) {
                        rateDate = adjustTime;
                    }
                } else {
                    rateDate = adjustTime;
                }
                if (i != maxIndex) {
                    //it.remove();
                }
                break;
            } else if (balanceDate.compareTo(adjustTime) == 0) {
                rate = newInstrate;
                if (currRate != null) {
                    rateDate = currRate.getStartDate();
                    if (rateDate.compareTo(balanceDate) >= 0) {
                        rateDate = adjustTime;
                    }
                } else {
                    rateDate = adjustTime;
                }
                changeType = adjustRateInfo.getChangeType();
                if ("3".equals(changeType)) {
                    zoneBeans = adjustRateInfo.getZoneBeans();
                }
                break;
            } else if (i == adjustSize - 1) {
                if (null != currRate) {
                    currRate.setInstrateTime(balanceDate);
                    currRate.setStartDate(balanceDate);
                    return currRate;
                }
                rate = newInstrate;
                rateDate = balanceDate;//最后一次利率调整以积数日期为准，不用参考利率的调整日期
                changeType = adjustRateInfo.getChangeType();
                if ("3".equals(changeType)) {
                    zoneBeans = adjustRateInfo.getZoneBeans();
                }
            } else {
                it.remove();
            }
            i++;
        }
        return new AccInstrateBean(null, null, balanceDate, rateDate, rate, null, changeType, zoneBeans);
    }
    /**
     * <b>功能介绍：</b>获取调整利率(包括余额区间)<br>
     * <b>日期：</b>2017年7月9日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private BigDecimal getInstrateRate(AccInstrateBean accuInfo, BigDecimal balVal) {
        String changeType = accuInfo.getChangeType();
        if (!"3".equals(changeType)) {//余额区间利率调整
            return accuInfo.getNewInstrate();
        }
        BigDecimal balance = balVal;
        String[] operate = new String[]{">=", "<=", "=", ">", "<"};
        List<AccInstrateZoneBean> zoneBeans = accuInfo.getZoneBeans();
        for (AccInstrateZoneBean zoneBean : zoneBeans) {
            BigDecimal downLimit = zoneBean.getDownLimit();
            BigDecimal upLimit = zoneBean.getUpLimit();
            String downOp = zoneBean.getDownOp();//下限操作符
            String upOp = zoneBean.getUpOp();//上限操作符
            if (operate[0].equals(downOp)) {//>=
                if (operate[1].equals(upOp)) {//<=
                    if (balance.compareTo(downLimit) >= 0 && balance.compareTo(upLimit) <= 0) {
                        return zoneBean.getNewInstrate();//区间
                    }
                } else if (operate[2].equals(upOp)) {//=
                    if (balance.compareTo(downLimit) >= 0 && balance.compareTo(upLimit) == 0) {
                        return zoneBean.getNewInstrate();//区间
                    }
                } else if (operate[4].equals(upOp)) {//<
                    if (balance.compareTo(downLimit) >= 0 && balance.compareTo(upLimit) < 0) {
                        return zoneBean.getNewInstrate();//区间
                    }
                } else if (StringUtils.isBlank(upOp)) {
                    if (balance.compareTo(downLimit) >= 0) {
                        return zoneBean.getNewInstrate();//最上限
                    }
                }
            } else if (operate[2].equals(downOp)) {//=
                if (operate[1].equals(upOp)) {//<=
                    if (balance.compareTo(downLimit) == 0 && balance.compareTo(upLimit) <= 0) {
                        return zoneBean.getNewInstrate();//区间
                    }
                } else if (operate[2].equals(upOp)) {//=
                    if (balance.compareTo(downLimit) == 0 && balance.compareTo(upLimit) == 0) {
                        return zoneBean.getNewInstrate();//区间
                    }
                } else if (operate[4].equals(upOp)) {//<
                    if (balance.compareTo(downLimit) == 0 && balance.compareTo(upLimit) < 0) {
                        return zoneBean.getNewInstrate();//区间
                    }
                } else if (StringUtils.isBlank(upOp)) {
                    if (balance.compareTo(downLimit) == 0) {
                        return zoneBean.getNewInstrate();//最上限
                    }
                }
            } else if (operate[3].equals(downOp)) {//>
                if (operate[1].equals(upOp)) {//<=
                    if (balance.compareTo(downLimit) > 0 && balance.compareTo(upLimit) <= 0) {
                        return zoneBean.getNewInstrate();//区间
                    }
                } else if (operate[2].equals(upOp)) {//=
                    if (balance.compareTo(downLimit) > 0 && balance.compareTo(upLimit) == 0) {
                        return zoneBean.getNewInstrate();//区间
                    }
                } else if (operate[4].equals(upOp)) {//<
                    if (balance.compareTo(downLimit) > 0 && balance.compareTo(upLimit) < 0) {
                        return zoneBean.getNewInstrate();//区间
                    }
                } else if (StringUtils.isBlank(upOp)) {
                    if (balance.compareTo(downLimit) > 0) {
                        return zoneBean.getNewInstrate();//最上限
                    }
                }
            }
        }
        return accuInfo.getOldInstrate();//不满足余额区间采用旧利率
    }
    /**
     * <b>功能介绍：</b>获取账户的积数，如果有调整积数<br>
     * <b>日期：</b>2017年7月9日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Override
    public BigDecimal getCumuByAccuBalance(FcAccuBalancef accuBalance) {
        double acumu = accuBalance.getCumu().doubleValue();
        if (accuBalance.getAdjustCumu() != null) {
            acumu += accuBalance.getAdjustCumu().doubleValue();
        }
        return new BigDecimal(acumu);
    }
    public void createInterestVoucher(AccInterestInfo accInterestInfo) {//创建计息凭证
        processInterestVoucher(accInterestInfo);
    }
    public void processInterestVoucher(AccInterestInfo accInterestInfo) {//创建计息凭证
        final List<FcInterestDtlTemp> interestDtls = accInterestInfo.getInterestDtls();
        if (interestDtls == null || interestDtls.size() == 0) {
            return;
        }
        InterestParamBean paramBean = new InterestParamBean(false, accInterestInfo.getContainer().isAccInCenter(),
                accInterestInfo.getContainer().isMoreVoucherDtl());
        paramBean.setPoolSign(accInterestInfo.isPoolSign());//资金池标识
        
        Map<String, FcFinacialAccbookVO> accbookMap = createCommonVoucher(interestDtls, accInterestInfo.getContainer()
                .getTallyDate(), accInterestInfo.getContainer().getUserBean().getId(), paramBean);
        if (accbookMap == null) {
            return;
        }
        //构建结息凭证
        accInterestInfo.setInterestAccbook(new ArrayList<FcFinacialAccbookVO>(accbookMap.values()));
        //按照账号排序
        sortInterestVoucher(accInterestInfo);
        if (!accInterestInfo.isNeedSave()) { //保存利息凭证
            return;
        }
        paramBean.setBisExc(accInterestInfo.getContainer().isInstBisExc());
        saveInterestVoucher(accbookMap, interestDtls, paramBean);
    }
    /**
     * 获取贷方活期账号
     */
    protected FcAccDTO getCurrentAcc(FcInterestDtlTemp interestDtl, String corpCode, String curId, FcAccDTO interestAcc) {
        return iFcAccService.getAccDTOByAccount(interestAcc.getRelationAcc());
//        String netId = interestAcc.getNetId();
//        CorpDTO corpDTO = iCorpService.getNetByCorpId(corpCode);
//        BtCurrencyDTO currencyDTO = iBtCurrencyService.getObjectById(curId);
//        Set<String> corps = new HashSet<>();
//
//        int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
//        List<FcAccDTO> fcAccDTOS = null;
//        if (0 == entrySign) {//网点
//            corps.add(corpDTO.getId());
//            fcAccDTOS = iFcAccService.getFcAccBySysSubjectId(SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.getValue(), null, corps, curId);
//        }else {
//            corpDTO = iCorpService.get(corpCode);
//            corps.add(corpCode);
//            fcAccDTOS = iFcAccService.getFcAccBySysSubjectId(SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.getValue(), null, corps, curId);
//        }
//        if(fcAccDTOS.size() < 1) {
//            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_CORPCURNOTACC, corpDTO.getName(), currencyDTO.getCurName(), SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED.getName());
//        }
//        return fcAccDTOS.get(0);
    }
    /**企业活期存款*/
    protected FcAccDTO findInterestDebitSysAcc(String netCode, String corpCode, String curCode,String account) {
        FcAccDTO fcAcc = iFcAccService.getAccDTOByAccount(account);
        FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(fcAcc.getRelationAcc());
        return fcAccDTO;
    }
    /**
     * 借方系统科目/借 利息支出-企业协定存款
     */
    @Override
    public ConstantBean<String> getDebitSysAcc() {
        return SubjectConstant.SysSubject.DISCOUNT_INTEREST_COMP_AGREED;
    }
    /**
     * 贷方系统科目/ 贷  应付利息-企业协定存款
     */
    @Override
    public ConstantBean<String> getCreditSysAcc() {
        return SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT;
    }
}
