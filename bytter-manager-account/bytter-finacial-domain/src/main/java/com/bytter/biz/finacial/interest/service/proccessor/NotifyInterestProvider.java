package com.bytter.biz.finacial.interest.service.proccessor;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.enums.interest.InterestProcessType;
import com.bytter.biz.finacial.interest.bean.*;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.utils.InterestHelper;
import com.bytter.biz.finacial.interest.vo.InterestInfoContainer;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import com.bytter.increment.api.service.IChoInnerFinanceService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * 通知存款 通知存款计息适配器<br>
 */
@Transactional
@Service("interest_NotifyInterestProvider")
public class NotifyInterestProvider extends FixedInterestProvider implements  java.io.Serializable{
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IChoInnerFinanceService iChoInnerFinanceService;
    
    
    private boolean isInstPunish;
    
    @Override
    public AccInterestInfo calcInterest(AccInterestInfo accInterestInfo) {
        InterestInfoContainer interestContainer = accInterestInfo.getContainer();
        sysRateType = interestContainer.getSysRateType();
        this.isInstPunish = interestContainer.isInstPunish();
        getAccLastInterestDate(interestContainer.getNetCode(), InnerAccountConstant.FCACC_BANK_DEPOSIT);
        for (Map.Entry<InterestProcessType, Map<String, List<FcAccuBalancef>>> entry : accInterestInfo.getInterestAccuBalances().entrySet()) {
            if (InterestProcessType.INTEREST.equals(entry.getKey())) {//计息
                accInterestInfo.getInterestDtls().addAll(processInterestDtls(InterestProcessType.INTEREST, entry.getValue(), interestContainer));
            } else {//处理预提
                List<FcInterestDtlTemp> interestDtls = processInterestDtl(InterestProcessType.PREDRAW,entry.getValue(), interestContainer);
                accInterestInfo.getPredrawDtls().addAll(processPredrawDtls(interestDtls, interestContainer));
            }
        }
        return accInterestInfo;
    }
    /**
     * 处理计息明细
     */
    private List<FcInterestDtlTemp> processInterestDtls(InterestProcessType interestProcessType, Map<String, List<FcAccuBalancef>> accAccuBalances, 
                                                        InterestInfoContainer interestContainer) {
        List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
        boolean isNotifyDraw = interestContainer.isNotifyDraw();
        if (!isNotifyDraw) {//不需要通知预约
            return processNoNotifyInterestDtls(interestProcessType, accAccuBalances, interestContainer);
        }
        //存款管理的通知信息---预约登记
        List<ChoInnerFinanceBean> fcDrawNoticeList = fcInterestDao.getSingleNotifyTellerInfo(new Integer[] {0}, interestContainer.getAccount());
        //存款管理的取消通知信息 --取消预约登记
        //List<ChoInnerFinanceBean> fcDrawNoticeCancleList = fcInterestDao.getNotifyTellerInfo(new Integer[] {95}, 11,false);
        //获取理财管理通知取款信息--通知预约
//        List<ChoInnerFinanceBean> chowDrawBeans = fcInterestDao.getNotifyTellerInfo(new Integer[] {95,99,107},  14 ,true);
        List<ChoInnerFinanceDTO> choInnerFinanceDTOS01 = iChoInnerFinanceService.getChoInnerFinance(new Integer[]{95, 99, 107}, 14);
        List<String> accounts01 = choInnerFinanceDTOS01.stream().map(ChoInnerFinanceDTO::getPayerAcc).collect(Collectors.toList());
        List<String> accountList01 = fcInterestDao.getNotifyTellerInfo(accounts01, true);
        List<ChoInnerFinanceDTO> collect = choInnerFinanceDTOS01.stream().filter(a -> accountList01.contains(a.getPayerAcc())).collect(Collectors.toList());
        List<ChoInnerFinanceBean> chowDrawBeans = BeanUtils.copyListProperties(collect, ChoInnerFinanceBean.class);
        //获取理财管理取消通知取款信息 --取消通知
//        List<ChoInnerFinanceBean> chowCancelBeans = fcInterestDao.getNotifyTellerInfo(new Integer[] {95,101},  15 ,true);
        List<ChoInnerFinanceDTO> choInnerFinanceDTOS02 = iChoInnerFinanceService.getChoInnerFinance(new Integer[] {95,101},  15);
        List<String> accounts02 = choInnerFinanceDTOS02.stream().map(ChoInnerFinanceDTO::getPayerAcc).collect(Collectors.toList());
        List<String> accountList02 = fcInterestDao.getNotifyTellerInfo(accounts02, true);
        List<ChoInnerFinanceDTO> collect2 = choInnerFinanceDTOS02.stream().filter(a -> accountList02.contains(a.getPayerAcc())).collect(Collectors.toList());
        List<ChoInnerFinanceBean> chowCancelBeans = BeanUtils.copyListProperties(collect2, ChoInnerFinanceBean.class);
        Calendar endInterestCalendar = Calendar.getInstance();//止息日期
        if(interestContainer.getEndInstDate()!=null) {
            endInterestCalendar.setTime(interestContainer.getEndInstDate());
        }
        endInterestCalendar.set(Calendar.HOUR, 0);
        endInterestCalendar.set(Calendar.MINUTE, 0);
        endInterestCalendar.set(Calendar.SECOND, 0);
        
        if (null != accAccuBalances) {
            for (Map.Entry<String, List<FcAccuBalancef>> entry : accAccuBalances.entrySet()) {
                FcAccuBalancef accuBalance = entry.getValue().get(0);
                FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(accuBalance.getAccount());
                Date interestStartDate = fcAccDTO.getInterestStartDate();//起息日期
                if(interestStartDate.compareTo(endInterestCalendar.getTime()) > 0) {
                    continue;
                }
                //预约登记，取款预约
                NotifyDrawInfo fcDrawNotice = getLastNotifyTellerInfo(fcDrawNoticeList, accuBalance.getAccount(), chowDrawBeans);
                
                /* 1、通知存款，没有通知取款，利率按照通知公式 N * 1(7) + T计算；
					* 2、通知存款，有通知但是通知取款日小于通知的通知取款日期，利率按照提前利率计算；
					* 3、通知存款，有通知且通知取款日等于通知的通知取款日期，但是取款金额小于通知金额，利率按照通知利率计算；
					* 4、通知存款，有通知且通知取款日等于通知的通知取款日期，但是取款金额大于通知金额，通知金额部分按照通知利率计算，超过通知金额部分按照提前利率计算；
					* 5、通知存款，有通知且通知取款日等于通知的通知取款日期，但是取款金额等于通知金额，利率按照通知利率计算；
					* 6、通知利率有调整，取小于等于当前日期的最新利率计算
					* 7、提前利率有调整，取小于等于当前日期的最新利率计算；
                    * 
                    */
                Calendar deadlineDate = Calendar.getInstance();
                deadlineDate.set(Calendar.HOUR, 0);
                deadlineDate.set(Calendar.MINUTE, 0);
                deadlineDate.set(Calendar.SECOND, 0);
                Calendar lastInterestDate = Calendar.getInstance();
                lastInterestDate.setTime(fcAccDTO.getInterestStartDate());//本次计息的起息日为账号起息日
                lastInterestDate.set(Calendar.HOUR, 0);
                lastInterestDate.set(Calendar.MINUTE, 0);
                lastInterestDate.set(Calendar.SECOND, 0);
                
                boolean isNotiFied = fcDrawNotice.getNotified();
                if (!isNotiFied) {//没有通知取款信息
                    return processNoNotifyInterestDtls(interestProcessType, accAccuBalances, interestContainer);
                    /*deadlineDate.setTime(fcAccDTO.getInterestStartDate());
                    processInterestBySegment(interestProcessType,fcAccDTO,interestDtls, accuBalance, deadlineDate,endInterestCalendar, 
                                            InterestConstant.INSTRT_TYPE_AHEAD, fcAccDTO.getOverdraftRate(), interestContainer, true);*/
                } else {//有通知取款信息
                    if (isInstPunish) { //罚息
                        processPunishInterestDtls(null, fcDrawNotice, fcAccDTO,interestDtls, accuBalance,
                                interestContainer, chowCancelBeans);
                    }
                    deadlineDate.setTime(fcDrawNotice.getDrawDate());//到期取款日期
                    long overDays = InterestHelper.getDiffDays(deadlineDate.getTime(), endInterestCalendar.getTime());
                    if (overDays > 0) {//逾期,预约日期之后进行提取的，就相当于没有预约
                        return processNoNotifyInterestDtls(interestProcessType, accAccuBalances, interestContainer);
                        /*Calendar endInterestCalendar1 = Calendar.getInstance();//止息日期
                        endInterestCalendar1.setTime(fcDrawNotice.getDrawDate());
                        endInterestCalendar1.set(Calendar.HOUR, 0);
                        endInterestCalendar1.set(Calendar.MINUTE, 0);
                        endInterestCalendar1.set(Calendar.SECOND, 0);
                        processInterestNormalDate(fcDrawNotice,fcAccDTO, interestProcessType, interestDtls, accuBalance,
                                lastInterestDate, endInterestCalendar1, interestContainer);
                        Calendar lastInterestDate1 = Calendar.getInstance();
                        lastInterestDate1.setTime(fcDrawNotice.getDrawDate());//本次计息的起息日为取款日期
                        lastInterestDate1.set(Calendar.HOUR, 0);
                        lastInterestDate1.set(Calendar.MINUTE, 0);
                        lastInterestDate1.set(Calendar.SECOND, 0);
                        processInterestBySegment(interestProcessType,fcAccDTO, interestDtls, accuBalance, lastInterestDate1,
                                endInterestCalendar, InterestConstant.INSTRT_TYPE_EXCEED, fcAccDTO.getOverdraftRate(), interestContainer, true);*/
                    } else if (overDays == 0) { //到期支取
                        processInterestNormalDate(fcDrawNotice,fcAccDTO, interestProcessType, interestDtls, accuBalance,
                                lastInterestDate, endInterestCalendar, interestContainer);
                    } else {//提前支取 ,按照活期利率计算利息信息
                        processInterestBySegment(interestProcessType, fcAccDTO,interestDtls, accuBalance, lastInterestDate,
                                endInterestCalendar, InterestConstant.INSTRT_TYPE_AHEAD, fcAccDTO.getOverdraftRate(), interestContainer, true);
                    }
                }
            }
        }
        return interestDtls;
    }
    /**
     * 功能：分段计算超期利率
     * @param tempRate 默认利率
     * @param isDays 按实际天数计算标识，true：按实际天数，false：对年对月
     * @return
     */
    public void processInterestBySegment(InterestProcessType interestProcessType,FcAccDTO fcAcc,
                                         List<FcInterestDtlTemp> interestDtls, FcAccuBalancef accuBalance, Calendar deadlineDate,
                                         Calendar endInterestCalendar, String instrtType, BigDecimal tempRate, InterestInfoContainer interestContainer,
                                         boolean isDays) {
        String account = fcAcc.getBlnctrAcc();
        //按照实际天数计算利率，
        InstrateAdjustBean instrateAdjustBean = interestContainer.getInstrateAdjustInfos().get(account);
        Calendar adjustCalendar = Calendar.getInstance();
        BigDecimal interestRate = BigDecimal.ZERO;
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(deadlineDate.getTime());
        int initOverDay = 0;
        boolean flag = false;
        if (null != instrateAdjustBean) {
            //if (null != instrateAdjustBean && !InstrtType.INSTRT_TYPE_NORMAL.get().equals(instrtType)) {
            List<AccInstrateBean> adjustRates = instrateAdjustBean.getAccInstratesByAdjustType(instrtType);
            if (null != adjustRates && adjustRates.size() > 0) {
                for (AccInstrateBean accInstrateBean : adjustRates) {
                    adjustCalendar.setTime(accInstrateBean.getInstrateTime());
                    if (tempCalendar.compareTo(adjustCalendar) <= 0){//调整日期在超期日之后
                        if(endInterestCalendar.compareTo(adjustCalendar) >= 0) {//止息日在调整日期在之后
                            if (!flag) {//获取调整之前的利率
                                initOverDay = DateUtils.dayDiffs(adjustCalendar.getTime(), tempCalendar.getTime());
                                processCommonInterest(interestProcessType,interestDtls, accuBalance,
                                        accInstrateBean.getOldInstrate(), initOverDay, tempCalendar,
                                        adjustCalendar, instrtType, interestContainer);//逾期利率即活期利率
                                tempCalendar.setTime(adjustCalendar.getTime());//下次调整起始间隔日期为本次调整日期
                            } else {//其他调整利率
                                initOverDay = DateUtils.dayDiffs(adjustCalendar.getTime(), tempCalendar.getTime());
                                processCommonInterest(interestProcessType, interestDtls, accuBalance, interestRate,
                                        initOverDay, tempCalendar, adjustCalendar, instrtType, interestContainer);
                                tempCalendar.setTime(adjustCalendar.getTime());//下次调整起始间隔日期为本次调整日期
                            }
                            flag = true;
                            interestRate = accInstrateBean.getNewInstrate();
                        } else {
                            flag = true;
                            interestRate = accInstrateBean.getOldInstrate();
                        }
                    } else {
                        flag = true;
                        interestRate = accInstrateBean.getNewInstrate();
                    }
                }
            }
            //止息日到最后一次调整日
            if (flag) {
                initOverDay = DateUtils.dayDiffs(endInterestCalendar.getTime(), tempCalendar.getTime());
                processCommonInterest(interestProcessType, interestDtls, accuBalance, interestRate, initOverDay,
                        tempCalendar, endInterestCalendar, instrtType, interestContainer);
            } else { //没有利率调整,按照天数计算
                initOverDay = DateUtils.dayDiffs(endInterestCalendar.getTime(), deadlineDate.getTime());
                processCommonInterest(interestProcessType, interestDtls, accuBalance, tempRate,
                        initOverDay, deadlineDate, endInterestCalendar, instrtType, interestContainer);
            }
        } else { //没有利率调整,按照天数计算
            initOverDay = DateUtils.dayDiffs(endInterestCalendar.getTime(), deadlineDate.getTime());
            processCommonInterest(interestProcessType, interestDtls, accuBalance, tempRate,
                    initOverDay, deadlineDate, endInterestCalendar, instrtType, interestContainer);
        }
    }
    /**
     * 功能：不需要通知预约计息
     */
    private List<FcInterestDtlTemp> processNoNotifyInterestDtls(InterestProcessType interestProcessType,
                                                                   Map<String, List<FcAccuBalancef>> accAccuBalances, InterestInfoContainer interestContainer){
        List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
        Calendar endInterestCalendar = Calendar.getInstance();//止息日期
        if(interestContainer.getEndInstDate() != null) {
            endInterestCalendar.setTime(interestContainer.getEndInstDate());
        }
        endInterestCalendar.set(Calendar.HOUR, 0);
        endInterestCalendar.set(Calendar.MINUTE, 0);
        endInterestCalendar.set(Calendar.SECOND, 0);
        if (null != accAccuBalances) {
            for (Map.Entry<String, List<FcAccuBalancef>> entry : accAccuBalances.entrySet()) {
                FcAccuBalancef accuBalance = entry.getValue().get(0);
                String account = accuBalance.getAccount();
                FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(account);
                BigDecimal rate = fcAccDTO.getInterestRate();
                Calendar deadlineDate = Calendar.getInstance();
                deadlineDate.set(Calendar.HOUR, 0);
                deadlineDate.set(Calendar.MINUTE, 0);
                deadlineDate.set(Calendar.SECOND, 0);
                Calendar lastInterestDate = Calendar.getInstance();
//                lastInterestDate.setTime(instMap.get(account));//获取账户本次计息的实际起始日
                lastInterestDate.setTime(fcAccDTO.getInterestStartDate());//本次计息的起息日为账号起息日
                lastInterestDate.set(Calendar.HOUR, 0);
                lastInterestDate.set(Calendar.MINUTE, 0);
                lastInterestDate.set(Calendar.SECOND, 0);
                String notifyType = fcAccDTO.getNotifyType().toString();
                processInterestNotifyBySegment(interestProcessType, interestDtls, accuBalance, lastInterestDate,
                        endInterestCalendar, notifyType, rate.doubleValue(), interestContainer,fcAccDTO.getOverdraftRate());
            }
        }
        return interestDtls;
    }
    /**
     * 分段计算超期利率(通知存款)
     */
    public void processInterestNotifyBySegment(InterestProcessType interestProcessType,List<FcInterestDtlTemp> interestDtls,
                                               FcAccuBalancef accuBalance, Calendar lastInterestDate,Calendar endInterestCalendar, String notifyType, Double tempRate,
                                               InterestInfoContainer interestContainer,BigDecimal OverdraftRate) {
        String account = accuBalance.getAccount();
        // 计息时间
        Calendar tempCal = Calendar.getInstance();
        Calendar adjustCalendar = Calendar.getInstance();
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(lastInterestDate.getTime());
        int initOverDay = 0;
        InstrateAdjustBean instrateAdjustBean = interestContainer.getInstrateAdjustInfos().get(account);
        //正常利率 ： NormalRates 提前利率:AheadRates
        List<AccInstrateBean> NormalRates = null;
        List<AccInstrateBean> AheadRates = null;
        if(instrateAdjustBean!=null){
            NormalRates = instrateAdjustBean.getAccInstratesByAdjustType(InterestConstant.INSTRT_TYPE_NORMAL);
            AheadRates = instrateAdjustBean.getAccInstratesByAdjustType(InterestConstant.INSTRT_TYPE_AHEAD);
        }
        this.processCommonInterestNotify(notifyType, NormalRates, AheadRates, tempCalendar, adjustCalendar, endInterestCalendar,
                tempCal, lastInterestDate, interestProcessType, interestDtls, accuBalance, interestContainer,
                initOverDay,  tempRate,OverdraftRate);
    }
    /**
     * 分段计算超期利率(通知存款)
     */
    private void processCommonInterestNotify(String notifyType ,List<AccInstrateBean> NormalRates,List<AccInstrateBean> AheadRates,
                                             Calendar tempCalendar,Calendar adjustCalendar,Calendar endInterestCalendar,Calendar tempCal,Calendar lastInterestDate,
                                             InterestProcessType interestProcessType,List<FcInterestDtlTemp> interestDtls,FcAccuBalancef accuBalance,
                                             InterestInfoContainer interestContainer,int initOverDay,Double tempRate,BigDecimal overdraftRate){
        // 间隔天数
//        long betDays = InterestHelper.getDiffDays(lastInterestDate.getTime(),
//                endInterestCalendar.getTime());
        int betDays = DateUtils.dayDiffs(endInterestCalendar.getTime(), lastInterestDate.getTime());
        if ("1".equals(notifyType)) {
            if(null==NormalRates){
                processCommonInterest(interestProcessType, interestDtls,accuBalance, BigDecimal.valueOf(tempRate), betDays,
                        lastInterestDate, endInterestCalendar, InterestConstant.INSTRT_TYPE_NORMAL,interestContainer);
            }else{
                for (AccInstrateBean accInstrateBean : NormalRates) {
                    // 利率开始时间
                    adjustCalendar.setTime(accInstrateBean.getInstrateTime());
                    if (tempCalendar.compareTo(adjustCalendar) <= 0&& endInterestCalendar.compareTo(adjustCalendar) >= 0) {
                        //initOverDay = InterestHelper.getDiffDays(tempCalendar.getTime(),adjustCalendar.getTime());
                        initOverDay = DateUtils.dayDiffs(adjustCalendar.getTime(), tempCalendar.getTime());
                        processCommonInterest(interestProcessType,interestDtls, accuBalance,accInstrateBean.getOldInstrate(),
                                initOverDay,tempCalendar, adjustCalendar,InterestConstant.INSTRT_TYPE_NORMAL,interestContainer);
                        tempCalendar.setTime(adjustCalendar.getTime());
                    }
                }
            }
        } else {
            int overDays = (int) (betDays % 7);// 不足7天的部分
            int currentDays = betDays - overDays;// 7天的倍数
            tempCal.setTime(DateUtils.addDay(lastInterestDate.getTime(),(int) currentDays));
            if (currentDays > 0) {
                if(null==NormalRates){
                    processCommonInterest(interestProcessType, interestDtls,accuBalance, BigDecimal.valueOf(tempRate),
                            currentDays, lastInterestDate, tempCal,InterestConstant.INSTRT_TYPE_NORMAL,interestContainer);
                }else{
                    BigDecimal finalrate = BigDecimal.ZERO;
                    for (AccInstrateBean accInstrateBean : NormalRates) {
                        adjustCalendar.setTime(accInstrateBean.getInstrateTime());
                        finalrate=accInstrateBean.getNewInstrate();
                        if (tempCalendar.compareTo(adjustCalendar) <= 0&& tempCal.compareTo(adjustCalendar) >= 0) {
                            //initOverDay = InterestHelper.getDiffDays(tempCalendar.getTime(),adjustCalendar.getTime());
                            initOverDay = DateUtils.dayDiffs(adjustCalendar.getTime(), tempCalendar.getTime());
                            processCommonInterest(interestProcessType,interestDtls, accuBalance,accInstrateBean.getOldInstrate(),
                                    initOverDay, tempCalendar,adjustCalendar,InterestConstant.INSTRT_TYPE_NORMAL,interestContainer);
                            tempCalendar.setTime(adjustCalendar.getTime());// 下次调整起始间隔日期为本次调整日期
                        }
                    }
                    if (tempCal.compareTo(adjustCalendar) >= 0) {
                        //initOverDay = InterestHelper.getDiffDays(tempCalendar.getTime(),tempCal.getTime());
                        initOverDay = DateUtils.dayDiffs(tempCal.getTime(), tempCalendar.getTime());
                        processCommonInterest(interestProcessType,interestDtls, accuBalance,finalrate,
                                initOverDay, tempCalendar,adjustCalendar,InterestConstant.INSTRT_TYPE_NORMAL,interestContainer);
                    }
                }
            }
            if (overDays > 0) {
                if(null==AheadRates){
                    processCommonInterest(interestProcessType, interestDtls,accuBalance,overdraftRate,overDays,tempCal, 
                                                    endInterestCalendar,InterestConstant.INSTRT_TYPE_AHEAD,interestContainer);
                }else{
                    BigDecimal finalrate= BigDecimal.ZERO;
                    for (AccInstrateBean accInstrateBean : AheadRates) {
                        // 提前利率开始时间
                        finalrate=accInstrateBean.getNewInstrate();
                        adjustCalendar.setTime(accInstrateBean.getInstrateTime());
                        if (tempCal.compareTo(adjustCalendar) <= 0&& endInterestCalendar.compareTo(adjustCalendar) >= 0) {
                            //initOverDay = InterestHelper.getDiffDays(tempCal.getTime(),adjustCalendar.getTime());
                            initOverDay = DateUtils.dayDiffs(adjustCalendar.getTime(), tempCal.getTime());
                            processCommonInterest(interestProcessType,interestDtls, accuBalance,accInstrateBean.getOldInstrate(), initOverDay,
                                    tempCal, adjustCalendar,InterestConstant.INSTRT_TYPE_AHEAD,interestContainer);
                            tempCal.setTime(adjustCalendar.getTime());
                        }
                    }
                    if (endInterestCalendar.compareTo(adjustCalendar) >= 0) {
                        //initOverDay = InterestHelper.getDiffDays(tempCal.getTime(),endInterestCalendar.getTime());
                        initOverDay = DateUtils.dayDiffs(endInterestCalendar.getTime(), tempCal.getTime());
                        processCommonInterest(interestProcessType,interestDtls, accuBalance,finalrate,
                                initOverDay, tempCalendar,adjustCalendar,InterestConstant.INSTRT_TYPE_AHEAD,interestContainer);
                    }
                }
            }
        }
    }
    
    /**
     * 获取最后一次通知取款的信息
     */
    private NotifyDrawInfo getLastNotifyTellerInfo(List<ChoInnerFinanceBean> FcDrawNoticeList, String account,
                                                   List<ChoInnerFinanceBean> chowDrawBeans) {
        NotifyDrawInfo info = new NotifyDrawInfo(account);
        boolean flag = false;
        //预约登记
        for (ChoInnerFinanceBean fcDrawNotice : FcDrawNoticeList) {
            if (account.trim().equals(fcDrawNotice.getPayerAcc().trim())) {
                info.setNotified(true);
                info.setDrawDate(fcDrawNotice.getPayDate());//所有申请日期都是payDate
                info.setNoticeMoney(fcDrawNotice.getPayMoney());
                flag = true;
            }
        }
        //取款预约
        for (ChoInnerFinanceBean bean : chowDrawBeans) {
            if (account.trim().equals(bean.getPayerAcc().trim())) {
                info.setNotified(true);
                info.setChowmatisticBean(bean);
                info.setDrawDate(bean.getPayDate());//取消日期
                info.setNoticeMoney(bean.getPayMoney());
                flag = true;
            }
        }
        return info;
    }
    /**
     * 处理罚息信息
     */
    private void processPunishInterestDtls(List<ChoInnerFinanceBean> fcDrawNoticeCancleList, NotifyDrawInfo fcDrawNotice,FcAccDTO fcAccDTO,
                                           List<FcInterestDtlTemp> interestDtls, FcAccuBalancef accuBalance,
                                           InterestInfoContainer interestContainer, List<ChoInnerFinanceBean> chowBeans) {
        //判断是否存在取消通知
        NotifyDrawInfo cancleBean = getLastNotifyTellerInfo(fcDrawNoticeCancleList, accuBalance.getAccount(), chowBeans);
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        boolean isInterestPunish = false;
        if (null != cancleBean) { //存在取消通知信息
            Date cancleDate = cancleBean.getEnterDate();//取消时间
            Date drawDate = cancleBean.getDrawDate();//取款时间
            startCalendar.setTime(cancleBean.getNotifyTime());
            if (cancleDate.compareTo(drawDate) > 0) { //取消日大于取款日
                endCalendar.setTime(drawDate);
            } else {
                endCalendar.setTime(cancleDate);
            }
            isInterestPunish = true;
        } else {
            isInterestPunish = false;
        }
        if (isInterestPunish) {
            processPunishInterest(isInterestPunish, fcDrawNotice,fcAccDTO, InterestProcessType.PREDRAW, interestDtls,
                    accuBalance, startCalendar, endCalendar, interestContainer);
        }
    }
    
    /**
     * 获取罚息明细
     */
    private void processPunishInterest(boolean isInterestPunish, NotifyDrawInfo fcDrawNotice,FcAccDTO fcAccDTO,
                                       InterestProcessType interestProcessType, List<FcInterestDtlTemp> interestDtls,
                                       FcAccuBalancef accuBalance, Calendar lastInterestDate, Calendar endInterestCalendar,
                                       InterestInfoContainer interestContainer) {
        if (isInterestPunish) {
            BigDecimal notifyMoney = fcDrawNotice.getNoticeMoney();//通知取款金额
            BigDecimal tellerMoney = accuBalance.getCumu();//取款金额
            if (tellerMoney.compareTo(notifyMoney) == 1) {//大于通知金额
                BigDecimal aCumu = accuBalance.getCumu();
                accuBalance.setCumu(notifyMoney);
                processInterestByLastInterestRate(interestProcessType,fcAccDTO, interestDtls, accuBalance, lastInterestDate,
                        endInterestCalendar, InterestConstant.INSTRT_TYPE_NORMAL, fcAccDTO.getInterestRate(), interestContainer);
                accuBalance.setCumu(tellerMoney.subtract(tellerMoney));
                processInterestBySegment(interestProcessType, fcAccDTO,interestDtls, accuBalance, lastInterestDate,
                        endInterestCalendar, InterestConstant.INSTRT_TYPE_AHEAD, fcAccDTO.getOverdueRate(), interestContainer, true);
                accuBalance.setCumu(aCumu);
            } else {//小于等于
                processInterestByLastInterestRate(interestProcessType,fcAccDTO, interestDtls, accuBalance, lastInterestDate,
                        endInterestCalendar, InterestConstant.INSTRT_TYPE_NORMAL, fcAccDTO.getInterestRate(), interestContainer);
            }
        }
    }
    /**
     * 定期计息方式：不控制(取最近的利率)
     */
    protected void processInterestByLastInterestRate(InterestProcessType interestProcessType,FcAccDTO fcAccDTO,
                                                     List<FcInterestDtlTemp> interestDtls, FcAccuBalancef accuBalance, Calendar deadlineDate,
                                                     Calendar endInterestCalendar, String instrtType, BigDecimal tempRate, InterestInfoContainer interestContainer) {
        String account = accuBalance.getAccount();
        BigDecimal interestRate = BigDecimal.ZERO;
        boolean flag = true;
        InstrateAdjustBean instrateAdjustBean = interestContainer.getInstrateAdjustInfos().get(account);
        if (null != instrateAdjustBean) {
            List<AccInstrateBean> adjustRates = instrateAdjustBean.getAccInstratesByAdjustType(instrtType);
            if (null != adjustRates) {
                for (AccInstrateBean accInstrateBean : adjustRates) {
                    if (endInterestCalendar.getTime().compareTo(accInstrateBean.getInstrateTime()) < 0) {
                        interestRate = accInstrateBean.getOldInstrate();
                        flag = false;
                        break;
                    }
                }
                if (flag) {//最后一次调整的利率
                    interestRate = adjustRates.get(adjustRates.size() - 1).getNewInstrate();
                    flag = false;
                }
            }
            if (flag) {
                interestRate = tempRate;
            }
           // Long currentDays = InterestHelper.getDiffDays(deadlineDate.getTime(), endInterestCalendar.getTime());// 超期前实际累计天数
            int currentDays = DateUtils.dayDiffs(endInterestCalendar.getTime(), deadlineDate.getTime());
            processCommonInterest(interestProcessType, interestDtls, accuBalance, interestRate, currentDays,
                    deadlineDate, endInterestCalendar, instrtType, interestContainer);
        } else {
            /*processInterestBySegment(interestProcessType,fcAccDTO, interestDtls, accuBalance, deadlineDate, endInterestCalendar,
                    instrtType, tempRate, interestContainer, true);*/
            int currentDays = DateUtils.dayDiffs(endInterestCalendar.getTime(), deadlineDate.getTime());
            processCommonInterest(interestProcessType, interestDtls, accuBalance, tempRate, currentDays,
                    deadlineDate, endInterestCalendar, instrtType, interestContainer);
        }
    }
    /**
     * 处理到期支取利息信息
     */
    private void processInterestNormalDate(NotifyDrawInfo fcDrawNotice, FcAccDTO fcAccDTO,InterestProcessType interestProcessType,
                                           List<FcInterestDtlTemp> interestDtls, FcAccuBalancef accuBalance, Calendar lastInterestDate,
                                           Calendar endInterestCalendar, InterestInfoContainer interestContainer) {
        BigDecimal notifyMoney = fcDrawNotice.getNoticeMoney();//通知取款金额
        BigDecimal tellerMoney = accuBalance.getCumu();//取款金额
        if (tellerMoney.compareTo(notifyMoney) == 1) {//大于通知金额
            BigDecimal aCumu = accuBalance.getCumu();
            accuBalance.setCumu(notifyMoney);
            processInterestByLastInterestRate(interestProcessType,fcAccDTO ,interestDtls, accuBalance, lastInterestDate,
                    endInterestCalendar, InterestConstant.INSTRT_TYPE_NORMAL, fcAccDTO.getInterestRate(),
                    interestContainer);
            accuBalance.setCumu(tellerMoney.subtract(notifyMoney));
            processInterestBySegment(interestProcessType,fcAccDTO, interestDtls, accuBalance, lastInterestDate,
                    endInterestCalendar, InterestConstant.INSTRT_TYPE_AHEAD, fcAccDTO.getOverdraftRate(),
                    interestContainer, true);
            accuBalance.setCumu(aCumu);
        } else if (tellerMoney.compareTo(notifyMoney) == 0) {//等于
            processInterestByLastInterestRate(interestProcessType, fcAccDTO,interestDtls, accuBalance, lastInterestDate,
                    endInterestCalendar, InterestConstant.INSTRT_TYPE_NORMAL, fcAccDTO.getInterestRate(),
                    interestContainer);
        } else { // 小于通知金额 支取金额<=预约金额，全部按照通知利率计算利息
            processInterestBySegment(interestProcessType,fcAccDTO, interestDtls, accuBalance, lastInterestDate,
                    endInterestCalendar, InterestConstant.INSTRT_TYPE_NORMAL, fcAccDTO.getInterestRate(),
                    interestContainer, true);
        }
    }
    /**
     * 处理预提利息明细
     */
    @Override
    protected List<FcInterestDtlTemp> processInterestDtl(InterestProcessType interestProcessType,
                                                            Map<String, List<FcAccuBalancef>> accAccuBalances, InterestInfoContainer interestContainer) {
        List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
        //获取通知信息 "复核", 95 "取消中", 96 "取款中", 97  "预约登记", 10
//        List<ChoInnerFinanceBean> fcDrawNoticeList = fcInterestDao.getNotifyTellerInfo(new Integer[] {95,96,97},10,false);
        List<ChoInnerFinanceDTO> choInnerFinanceDTOS01 = iChoInnerFinanceService.getChoInnerFinance(new Integer[]{95,96,97}, 10);
        List<String> accounts01 = choInnerFinanceDTOS01.stream().map(ChoInnerFinanceDTO::getPayerAcc).collect(Collectors.toList());
        List<String> accountList01 = fcInterestDao.getNotifyTellerInfo(accounts01, false);
        List<ChoInnerFinanceDTO> collect = choInnerFinanceDTOS01.stream().filter(a -> accountList01.contains(a.getPayerAcc())).collect(Collectors.toList());
        List<ChoInnerFinanceBean> fcDrawNoticeList = BeanUtils.copyListProperties(collect, ChoInnerFinanceBean.class);
        //获取理财管理通知取款信息 "审批通过", 95 "取消中",99 "取款中", 107   "通知预约", 14
//        List<ChoInnerFinanceBean> chowDrawBeans = fcInterestDao.getNotifyTellerInfo(new Integer[] {95,99,107},14,true);
        List<ChoInnerFinanceDTO> choInnerFinanceDTOS02 = iChoInnerFinanceService.getChoInnerFinance(new Integer[]{95,99,107}, 14);
        List<String> accounts02 = choInnerFinanceDTOS02.stream().map(ChoInnerFinanceDTO::getPayerAcc).collect(Collectors.toList());
        List<String> accountList02 = fcInterestDao.getNotifyTellerInfo(accounts02, true);
        List<ChoInnerFinanceDTO> collect2 = choInnerFinanceDTOS02.stream().filter(a -> accountList02.contains(a.getPayerAcc())).collect(Collectors.toList());
        List<ChoInnerFinanceBean> chowDrawBeans = BeanUtils.copyListProperties(collect2, ChoInnerFinanceBean.class);
        Calendar endInterestCalendar = Calendar.getInstance();//止息日期
        endInterestCalendar.setTime(interestContainer.getEndInstDate());
        endInterestCalendar.set(Calendar.HOUR, 0);
        endInterestCalendar.set(Calendar.MINUTE, 0);
        endInterestCalendar.set(Calendar.SECOND, 0);
        if (null != accAccuBalances) {
            for (Map.Entry<String, List<FcAccuBalancef>> entry : accAccuBalances.entrySet()) {
                FcAccuBalancef accuBalance = entry.getValue().get(0);
                String account = accuBalance.getAccount();
                FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(account);
                NotifyDrawInfo fcDrawNotice = getLastNotifyTellerInfo(fcDrawNoticeList, account, chowDrawBeans);
                Calendar deadlineDate = Calendar.getInstance();
                deadlineDate.set(Calendar.HOUR, 0);
                deadlineDate.set(Calendar.MINUTE, 0);
                deadlineDate.set(Calendar.SECOND, 0);
                Calendar lastInterestDate = Calendar.getInstance();
                if (preMap.get(account) != null ){
                lastInterestDate.setTime(preMap.get(account));//获取账户本次预提的实际起始日
                }
                lastInterestDate.set(Calendar.HOUR, 0);
                lastInterestDate.set(Calendar.MINUTE, 0);
                lastInterestDate.set(Calendar.SECOND, 0);
                if (InterestHelper.getDiffDays(lastInterestDate.getTime(), endInterestCalendar.getTime()) < 1) {//上次预提或结息时间>=止息日
                    continue;
                }
                boolean isNotiFied = fcDrawNotice.getNotified();
                if (!isNotiFied) {//没有通知取款信息
                    if(preMap.get(account) != null ){
                        deadlineDate.setTime(preMap.get(account));//起始日
                    }
                    processInterestByLastInterestRate(interestProcessType, fcAccDTO,interestDtls, accuBalance, lastInterestDate,
                            endInterestCalendar, InterestConstant.INSTRT_TYPE_NORMAL, fcAccDTO.getInterestRate(), interestContainer);
                } else {//是否超期
                    deadlineDate.setTime(fcDrawNotice.getDrawDate());//到期取款日期
                    long overDays = InterestHelper.getDiffDays(deadlineDate.getTime(), endInterestCalendar.getTime());
                    if (overDays > 0) {//超期,按照活期利率计算利息信息
                        //如果预提 按照活期利率计算利息
                        processInterestBySegment(interestProcessType, fcAccDTO,interestDtls, accuBalance, lastInterestDate,
                                endInterestCalendar, InterestConstant.INSTRT_TYPE_NORMAL, fcAccDTO.getInterestRate(), interestContainer, true);
                    } else { //未超期，按照约定利率计算利息
                        processInterestByLastInterestRate(interestProcessType, fcAccDTO,interestDtls, accuBalance,
                                lastInterestDate, endInterestCalendar, InterestConstant.INSTRT_TYPE_NORMAL, fcAccDTO.getInterestRate(),
                                    interestContainer);
                    }
                }
            }
        }
        return transDtlTemps(interestDtls);
    }
    /**
     *贷方系统科目/查找单位应付利息-通知存款账户
     */
    @Override
    public ConstantBean<String> getCreditSysAcc(){
        return SubjectConstant.SysSubject.INTEREST_PAYABLE_NOTICE_DEPOSIT;
    }
    /**
     *借方系统科目/查找单位利息支出-通知存款账户
     */
    @Override
    public ConstantBean<String> getDebitSysAcc(){
        return SubjectConstant.SysSubject.INTEREST_EXPENSES_NOTICE_DEPOSIT;
    }
}
