package com.bytter.biz.finacial.interest.service.proccessor;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.enums.interest.InterestProcessType;
import com.bytter.biz.finacial.interest.bean.AccInstrateBean;
import com.bytter.biz.finacial.interest.bean.AccInterestInfo;
import com.bytter.biz.finacial.interest.bean.InstrateAdjustBean;
import com.bytter.biz.finacial.interest.dao.FcInterestDao;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.utils.InterestHelper;
import com.bytter.biz.finacial.interest.vo.FcPredrawDtlTempVO;
import com.bytter.biz.finacial.interest.vo.InterestInfoContainer;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
/**
 * 05-企业定期 企业定期存款计息适配器<br>
 */
@Transactional
@Service("interest_FixedInterestProvider")
public class FixedInterestProvider  extends BaseInterestProvider implements Serializable {
    @Autowired
    private FcInterestDao fcInstDao;
    @DubboReference(check = false)
	private IFcAccService iFcAccService;
    /**
     * 账户上次预提时间
     */
    protected Map<String, Date> preMap = new HashMap<String, Date>();
    
    /**查询定期账户 */
    @Override
    public List<FcAccuBalancef> queryAccuBalanceInfos(String netCode, String accTypeCode, Date endInstDate) {
        return fcInterestDao.queryAccuBalanceForNone(netCode, new String[] { accTypeCode }, endInstDate);
    }
    /**查询定期账户 */
    @Override
    public List<FcAccuBalancef> queryAccAccuBalance(String account, Date endInstDate) {
        return fcInterestDao.queryAccuoutBalanceForNone(account, endInstDate);
    }
    @Override
    public AccInterestInfo calcInterest(AccInterestInfo accInterestInfo) {
        //利息bean
        InterestInfoContainer interestContainer = accInterestInfo.getContainer();
        sysRateType =  interestContainer.getSysRateType();//系统利率类型
        
        //账户上次预提时间(账号对应 预提日期和结息日期)
        getAccLastInterestDate( accInterestInfo.getContainer().getNetCode(), accInterestInfo.getAccTypeKeyBean().getaccType());
        
        for (Map.Entry<InterestProcessType, Map<String, List<FcAccuBalancef>>> entry : accInterestInfo.getInterestAccuBalances().entrySet()) {
            if (entry.getKey() == InterestProcessType.INTEREST) {//计息
                accInterestInfo.getInterestDtls().addAll(processInterestDtl(InterestProcessType.INTEREST, entry.getValue(), interestContainer));
            } else {//处理预提
                List<FcInterestDtlTemp> interestDtls = processInterestDtl(InterestProcessType.PREDRAW,entry.getValue(), interestContainer);
                accInterestInfo.getPredrawDtls().addAll(processPredrawDtls(interestDtls, interestContainer));
            }
        }
        return accInterestInfo;
    }
    
    /**
     * 获取账户上次预提时间
     */
    protected void getAccLastInterestDate(String netCode, String accTypeCode) {
        //获取账户的上次预提、结息时间
        List<Map<String, Object>> preList = fcInstDao.getInterestLastTimeForNone(netCode, accTypeCode);
        preMap = new HashMap<String, Date>();
        if (!preList.isEmpty()) {
            for (Map<String, Object> map : preList) {
                String account = (String) map.get("ACCOUNT");
                Date startDate = (Date) map.get("INTERESTSTARTDATE");
                Date preDateStr;
                if(null == map.get("PREDATE") ){
                    preDateStr = startDate;
                } else {
                    preDateStr = (Date) map.get("PREDATE");
                    preDateStr = DateUtils.addDay(preDateStr, 1);//预提次日
                }
                Date preDate = DateUtils.parseDate(preDateStr);
                preMap.put(account, preDate);
            }
        }
        
    }
    /**
     *处理利息明细
     * @param interestProcessType 计息类型
     * @param accAccuBalances :账户积数信息
     * @return 利息明细信息
     */
    protected List<FcInterestDtlTemp> processInterestDtl(InterestProcessType interestProcessType,
                                                         Map<String, List<FcAccuBalancef>> accAccuBalances, InterestInfoContainer interestContainer) {
        List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
        Date endInstDate = interestContainer.getEndInstDate();//日结日期
        if(!EmptyUtils.isEmpty(endInstDate)) {
            Map<String, InstrateAdjustBean> instrateAdjustInfos = interestContainer.getInstrateAdjustInfos();
            Calendar endInstCalendar = Calendar.getInstance();//止息日期
            endInstCalendar.setTime(endInstDate);
            endInstCalendar.set(Calendar.HOUR, 0);
            endInstCalendar.set(Calendar.MINUTE, 0);
            endInstCalendar.set(Calendar.SECOND, 0);
            if (null != accAccuBalances) {
                for (Map.Entry<String, List<FcAccuBalancef>> entry : accAccuBalances.entrySet()) {
                    FcAccuBalancef accuBalance = entry.getValue().get(0);
                    FcAccDTO fcAcc = iFcAccService.getAccDTOByAccount(accuBalance.getAccount());
                    Calendar deadlineDate = Calendar.getInstance();
                    deadlineDate.set(Calendar.HOUR, 0);
                    deadlineDate.set(Calendar.MINUTE, 0);
                    deadlineDate.set(Calendar.SECOND, 0);
                    deadlineDate.setTime(fcAcc.getDeadlineDate());//到期日期
                    //到期日期应不算最后一天，得减一天
                    long overDays = DateUtils.getOffsetDays(deadlineDate.getTime(), endInstDate);//相差的天数
                    Calendar lastInterestDate = Calendar.getInstance();
                    if (InterestProcessType.INTEREST.equals(interestProcessType)) {//计息
                        lastInterestDate.setTime(fcAcc.getInterestStartDate());//本次计息的起息日为账号起息日
                    } else {
                        Date preDate = preMap.get(accuBalance.getAccount());
                        if (null == preDate) {
                            preDate = fcAcc.getAccStartDate();
                        }
                        if (preDate.compareTo(fcAcc.getInterestStartDate()) < 0) {
                            lastInterestDate.setTime(fcAcc.getInterestStartDate());//账户到期处理后以新的起息日计息
                        } else {
                            lastInterestDate.setTime(preDate);//获取账户本次预提的实际起始日
                        }
                    }
                    lastInterestDate.set(Calendar.HOUR, 0);
                    lastInterestDate.set(Calendar.MINUTE, 0);
                    lastInterestDate.set(Calendar.SECOND, 0);
                    if (DateUtils.getOffsetDays(lastInterestDate.getTime(), endInstDate) < 1) {//上次预提或结息时间>=止息日
                        continue;
                    }
                    if (overDays > 0) {//超期
                        if (lastInterestDate.compareTo(deadlineDate) < 0) {//超期前的预提利息
                            getDaysByYearAndMonth(interestProcessType, interestDtls, accuBalance, fcAcc,
                                    getInitialRate(fcAcc, instrateAdjustInfos), InterestConstant.INSTRT_TYPE_NORMAL,
                                    interestContainer, lastInterestDate.getTime(), deadlineDate.getTime(), true);
                        } else {
                            deadlineDate.setTime(lastInterestDate.getTime()); //上次预提或结息的时间在超期之后，则本次预提起始日为上次预提(或结息)日
                        }
                        processOverDayInterestRates(interestProcessType, fcAcc, interestDtls, accuBalance, deadlineDate,
                                endInstCalendar, InterestConstant.INSTRT_TYPE_EXCEED, fcAcc.getOverdueRate(),
                                interestContainer);
                    } else if (overDays == 0) {
                        getDaysByYearAndMonth(interestProcessType, interestDtls, accuBalance, fcAcc, fcAcc.getInterestRate(), InterestConstant.INSTRT_TYPE_NORMAL,
                                interestContainer, lastInterestDate.getTime(), endInstCalendar.getTime(), true);
                    } else {//非超期预提(提前预提、到期预提)，按照正常利率计算利息
                        if (InterestProcessType.INTEREST.equals(interestProcessType)) {//计息
                            processInterestBySegment(interestProcessType, fcAcc, interestDtls, accuBalance, lastInterestDate,
                                    endInstCalendar, InterestConstant.INSTRT_TYPE_AHEAD, fcAcc.getOverdraftRate(),
                                    interestContainer, true);
                        } else { //预提
                            getDaysByYearAndMonth(interestProcessType, interestDtls, accuBalance, fcAcc,
                                    fcAcc.getInterestRate(), InterestConstant.INSTRT_TYPE_NORMAL,
                                    interestContainer, lastInterestDate.getTime(), endInstCalendar.getTime(), true);
                        }
                    }
                }
            }
        }
        return transDtlTemps(interestDtls);
    }
    /**
     * 得到账户初始默认利率
     */
    private BigDecimal getInitialRate(FcAccDTO fcAcc, Map<String, InstrateAdjustBean> instrateAdjustInfos) {
        InstrateAdjustBean instrateAdjustBean = instrateAdjustInfos.get(fcAcc.getBlnctrAcc());
        if (null != instrateAdjustBean) {
            List<AccInstrateBean> adjustRates = instrateAdjustBean.getAccInstratesByAdjustType(InterestConstant.INSTRT_TYPE_NORMAL);
            if (null != adjustRates && adjustRates.size() > 0) {
                return adjustRates.get(0).getOldInstrate();
            }
        }
        return fcAcc.getInterestRate();
    }
    /**
     * 计算定期计息日到到期日之间的天数(对年对月)
     */
    public void getDaysByYearAndMonth(InterestProcessType interestProcessType,List<FcInterestDtlTemp> interestDtls,
                                       FcAccuBalancef accuBalance, FcAccDTO fcAcc,BigDecimal interestRate,
                                       String instrtType, InterestInfoContainer interestContainer, Date startDate, Date endDate, boolean flag) {
        String sysFixInterestWay = interestContainer.getSysFixInterestWay();
        boolean fixedActrueDays = interestContainer.isFixedActrueDays();
        Long betDays = Long.valueOf(0);
        //初始化
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.setTime(endDate);
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.HOUR, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        //获取与开始日期最近的同日的月份
        int day1 = startCalendar.get(Calendar.DAY_OF_MONTH);//获取开始日期所在的天
        int day2 = endCalendar.get(Calendar.DAY_OF_MONTH);//获取结束日期所在的天
        long monthDays = 30l;//计算利息月份天数
        if (day2 <= day1) {
            if (day2 == day1 || InterestHelper.isLastDayOfMonth(endDate)) {//整月或者 是本月最后一天
                tempCalendar.setTime(endDate);
                //算整月
                betDays = getNormalDays(sysFixInterestWay, fixedActrueDays, startCalendar, tempCalendar);
                if (InterestProcessType.INTEREST.equals(interestProcessType) && flag) {
                    processInterestBySegment(interestProcessType, fcAcc,interestDtls, accuBalance, startCalendar,
                            tempCalendar, instrtType, interestRate, interestContainer, true);
                } else {
                    processNormalInterest(interestProcessType, interestDtls, accuBalance, interestRate, betDays,
                            startCalendar, tempCalendar, instrtType, interestContainer);
                }
            } else {
                /**
                 * 说明不足一月,则取上个月的该天,注意可能2月没有29,30,31或者其他月份没有31号
                 */
                tempCalendar.setTime(endDate);
                tempCalendar.set(Calendar.DAY_OF_MONTH, 1);//第一天
                tempCalendar.add(Calendar.DAY_OF_MONTH, -1);//上月的最后一天
                int tempDay = tempCalendar.get(Calendar.DAY_OF_MONTH);//获取上月最后一天
                if (tempDay <= day1) {
                    //之前算整月
                    betDays = getNormalDays(sysFixInterestWay, fixedActrueDays, startCalendar, tempCalendar);
                    if (InterestProcessType.INTEREST.equals(interestProcessType)  && flag) {
                        processInterestBySegment(interestProcessType, fcAcc,interestDtls, accuBalance, startCalendar,
                                tempCalendar, instrtType, interestRate, interestContainer, true);
                    } else {
                        processNormalInterest(interestProcessType, interestDtls, accuBalance, interestRate, betDays,
                                startCalendar, tempCalendar, instrtType, interestContainer);
                    }
                    //后面的从本月的第一天算
                    tempCalendar.add(Calendar.DAY_OF_MONTH, 1);//本月的第一天
                    long overDays = DateUtils.dayDiffs(endDate, tempCalendar.getTime());
                    if (interestContainer.isFixedActrueDays()) {
                        monthDays = DateUtils.getDayOfMonth(tempCalendar.getTime());
                    }
                    if (InterestProcessType.INTEREST.equals(interestProcessType)  && flag) {
                        processInterestBySegment(interestProcessType,fcAcc, interestDtls, accuBalance, tempCalendar,
                                endCalendar, instrtType, interestRate, interestContainer, true);
                    } else {
                        processCommonDayInterest(interestProcessType, interestDtls, accuBalance, interestRate,
                                monthDays, overDays, tempCalendar, endCalendar, instrtType, interestContainer);
                    }
                } else {
                    tempCalendar.set(Calendar.DAY_OF_MONTH, day1);//上月天数与起息日保持一致
                    betDays = getNormalDays(sysFixInterestWay, fixedActrueDays, startCalendar, tempCalendar);
                    if (InterestProcessType.INTEREST.equals(interestProcessType)  && flag) {
                        processInterestBySegment(interestProcessType, fcAcc,interestDtls, accuBalance, startCalendar,
                                tempCalendar, instrtType, interestRate, interestContainer, true);
                    } else {
                        processNormalInterest(interestProcessType, interestDtls, accuBalance, interestRate, betDays,
                                startCalendar, tempCalendar, instrtType, interestContainer);
                    }
                    if (fixedActrueDays) {
                        monthDays = DateUtils.getDayOfMonth(tempCalendar.getTime());
                    }
                    //后面的从上月的起息日算
                    long overDays = DateUtils.dayDiffs(endDate, tempCalendar.getTime());
                    if (InterestProcessType.INTEREST.equals(interestProcessType)  && flag) {
                        processInterestBySegment(interestProcessType,fcAcc, interestDtls, accuBalance, tempCalendar,
                                endCalendar, instrtType, interestRate, interestContainer, true);
                    } else {
                        processCommonDayInterest(interestProcessType, interestDtls, accuBalance, interestRate,
                                monthDays, overDays, tempCalendar, endCalendar, instrtType, interestContainer);
                    }
                }
            }
        } else {
            tempCalendar.setTime(endDate);
            tempCalendar.set(Calendar.DAY_OF_MONTH, day1);//本月天数与起息日保持一致
            betDays = getNormalDays(sysFixInterestWay, fixedActrueDays, startCalendar, tempCalendar);
            if (InterestProcessType.INTEREST.equals(interestProcessType)  && flag) {
                processInterestBySegment(interestProcessType, fcAcc,interestDtls, accuBalance, startCalendar, tempCalendar,
                        instrtType, interestRate, interestContainer, true);
            } else {
                processNormalInterest(interestProcessType, interestDtls, accuBalance, interestRate, betDays,
                        startCalendar, tempCalendar, instrtType, interestContainer);
            }
            if (fixedActrueDays) {
                monthDays = DateUtils.getDayOfMonth(tempCalendar.getTime());
            }
            //后面的从本月的第一天算
            long overDays = DateUtils.dayDiffs(endDate, tempCalendar.getTime());
            if (InterestProcessType.INTEREST.equals(interestProcessType)  && flag) {
                processInterestBySegment(interestProcessType,fcAcc, interestDtls, accuBalance, tempCalendar, endCalendar,
                        instrtType, interestRate, interestContainer, true);
            } else {
                processCommonDayInterest(interestProcessType, interestDtls, accuBalance, interestRate, monthDays,
                        overDays, tempCalendar, endCalendar, instrtType, interestContainer);
            }
        }
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
        //调整前,按照实际天数计算利率，调整后超期天数：(年-年)*360+(月-月)*30
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
                                if (InterestConstant.INSTRT_TYPE_NORMAL.equals(instrtType)) {
                                    getDaysByYearAndMonth(interestProcessType, interestDtls, accuBalance, fcAcc,accInstrateBean.getOldInstrate(),
                                            instrtType, interestContainer, tempCalendar.getTime(),
                                            adjustCalendar.getTime(), false);
                                } else {
                                   // initOverDay = InterestHelper.getDiffDays(tempCalendar.getTime(), adjustCalendar.getTime());
                                    initOverDay = DateUtils.dayDiffs(adjustCalendar.getTime(), tempCalendar.getTime());
                                    processCommonInterest(interestProcessType,interestDtls, accuBalance,
                                            accInstrateBean.getOldInstrate(), initOverDay, tempCalendar,
                                            adjustCalendar, instrtType, interestContainer);//逾期利率即活期利率
                                }
                                tempCalendar.setTime(adjustCalendar.getTime());//下次调整起始间隔日期为本次调整日期
                            } else {//其他调整利率
                                if (InterestConstant.INSTRT_TYPE_NORMAL.equals(instrtType)) {
                                    getDaysByYearAndMonth(interestProcessType, interestDtls, accuBalance, fcAcc,interestRate,
                                            instrtType, interestContainer, tempCalendar.getTime(),
                                            adjustCalendar.getTime(), false);
                                } else {
                                   // initOverDay = InterestHelper.getDiffDays(tempCalendar.getTime(),adjustCalendar.getTime());
                                    initOverDay = DateUtils.dayDiffs(adjustCalendar.getTime(), tempCalendar.getTime());
                                    processCommonInterest(interestProcessType, interestDtls, accuBalance, interestRate,
                                            initOverDay, tempCalendar, adjustCalendar, instrtType, interestContainer);
                                }
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
                if (InterestConstant.INSTRT_TYPE_NORMAL.equals(instrtType)) {
                    getDaysByYearAndMonth(interestProcessType, interestDtls, accuBalance, fcAcc,interestRate,
                            instrtType, interestContainer, tempCalendar.getTime(), endInterestCalendar.getTime(), false);
                } else {
                    //initOverDay = InterestHelper.getDiffDays(tempCalendar.getTime(), endInterestCalendar.getTime());
                    initOverDay = DateUtils.dayDiffs(endInterestCalendar.getTime(), tempCalendar.getTime());
                    processCommonInterest(interestProcessType, interestDtls, accuBalance, interestRate, initOverDay,
                            tempCalendar, endInterestCalendar, instrtType, interestContainer);
                }
            } else { //没有利率调整,按照天数计算
                if (InterestConstant.INSTRT_TYPE_NORMAL.equals(instrtType)) {
                    getDaysByYearAndMonth(interestProcessType, interestDtls, accuBalance, fcAcc,tempRate,
                            instrtType, interestContainer, deadlineDate.getTime(), endInterestCalendar.getTime(), false);
                } else {
                    //initOverDay = InterestHelper.getDiffDays(deadlineDate.getTime(), endInterestCalendar.getTime());
                    initOverDay = DateUtils.dayDiffs(endInterestCalendar.getTime(), deadlineDate.getTime());
                    processCommonInterest(interestProcessType, interestDtls, accuBalance, tempRate,
                            initOverDay, deadlineDate, endInterestCalendar, instrtType, interestContainer);
                }
            }
        } else { //没有利率调整,按照天数计算
            if (InterestConstant.INSTRT_TYPE_NORMAL.equals(instrtType)) {
                getDaysByYearAndMonth(interestProcessType, interestDtls, accuBalance,fcAcc, tempRate,
                        instrtType, interestContainer, deadlineDate.getTime(), endInterestCalendar.getTime(), false);
            } else {
                //initOverDay = InterestHelper.getDiffDays(deadlineDate.getTime(), endInterestCalendar.getTime());
                initOverDay = DateUtils.dayDiffs(endInterestCalendar.getTime(), deadlineDate.getTime());
                processCommonInterest(interestProcessType, interestDtls, accuBalance, tempRate,
                        initOverDay, deadlineDate, endInterestCalendar, instrtType, interestContainer);
            }
        }
    }
    /**
     * 定期对实际整月份的利息
     * @param interestProcessType ：计息类型
     * @param interestDtls ：利息明细集合
     * @param accuBalance ：调整积数信息
     * @param interestRate ：本次计算利息的利率
     * @param currentDays ：本次计算利息的实际天数
     * @param interestStartCalendar ：本次计算利息的起始日
     * @param blnInstCalendar ：本次计算利息的结束日
     * @param instrtType ：利率类型
     * @param interestContainer
     * @param 
     */
    protected void processNormalInterest(InterestProcessType interestProcessType,
                                         List<FcInterestDtlTemp> interestDtls, FcAccuBalancef accuBalance, BigDecimal interestRate,
                                         Long currentDays, Calendar interestStartCalendar, Calendar blnInstCalendar, String instrtType,
                                         InterestInfoContainer interestContainer) {
        if (currentDays <= 0 || interestStartCalendar.compareTo(blnInstCalendar) > 0) {
            return;
        }
        BigDecimal cumulations = accuBalance.getCumu().multiply(BigDecimal.valueOf(currentDays));
        FcInterestDtlTemp interestDtl = createInterestDtl(interestContainer);
        interestDtl.setAccount(accuBalance.getAccount());
        interestDtl.setInterestStartDate(interestStartCalendar.getTime());//起息日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(blnInstCalendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        interestDtl.setBlnInstDate(calendar.getTime());
        interestDtl.setInterestType(instrtType);
        interestDtl.setDays(currentDays.intValue());
        interestDtl.setCumulation(cumulations);
        if (InterestProcessType.INTEREST.equals(interestProcessType)) {
            interestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_IST);//计息
            interestDtl.setInterestRate(interestRate);
        } else {
            interestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_PRE);//预提
            BigDecimal rate = getLastInstrateRate(accuBalance.getAccount(), interestRate, interestContainer, blnInstCalendar);
            interestDtl.setInterestRate(rate);
        }
        BigDecimal interest = interestDtl.getCumulation().multiply(interestDtl.getInterestRate())
                .divide(sysRateType, 2, BigDecimal.ROUND_HALF_UP);
        interestDtl.setInterest(interest);//利息=积数*利率/360/100
        //interestDtl.setFcAcc(accuBalance.getFcAcc());
        interestDtl.setCurCode(accuBalance.getCurCode());
        interestDtls.add(interestDtl);
    }
    /**
     * 功能：</b>获取当前日期内的调整的正常利率
     */
    protected BigDecimal getLastInstrateRate(String account, BigDecimal oriRate, InterestInfoContainer container,
                                             Calendar endInterestCalendar) {
        BigDecimal interestRate = BigDecimal.ZERO;
        String instrtType = InterestConstant.INSTRT_TYPE_NORMAL;
        boolean flag = true;
        InstrateAdjustBean instrateAdjustBean = container.getInstrateAdjustInfos().get(account);
        if (null != instrateAdjustBean) {
            List<AccInstrateBean> adjustRates = instrateAdjustBean.getAccInstratesByAdjustType(instrtType);
            if (null != adjustRates && adjustRates.size() > 0) {
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
        } else {
            interestRate = oriRate;
        }
        return interestRate;
    }
    /**
     * 定期正常存期内不足实际天数的利息 
     * @param interestProcessType ：计息类型
     * @param interestDtls ：利息明细集合
     * @param accuBalance ：调整积数信息
     * @param interestRate ：本次计算利息的利率
     * @param monthDays ：本次计算利息当月天数
     * @param currentDays ：本次计算利息的实际天数
     * @param interestStartCalendar ：本次计算利息的起始日
     * @param blnInstCalendar ：本次计算利息的结束日
     * @param instrtType ：利率类型
     * @param interestContainer
     * @param 
     */
    protected void processCommonDayInterest(InterestProcessType interestProcessType,
                                            List<FcInterestDtlTemp> interestDtls, FcAccuBalancef accuBalance, BigDecimal interestRate,
                                            Long monthDays, Long currentDays, Calendar interestStartCalendar, Calendar blnInstCalendar,
                                            String instrtType, InterestInfoContainer interestContainer) {
        if (currentDays <= 0 || interestStartCalendar.compareTo(blnInstCalendar) > 0) {
            return;
        }
        BigDecimal cumulations = accuBalance.getCumu().multiply(BigDecimal.valueOf(currentDays));
        FcInterestDtlTemp interestDtl = createInterestDtl(interestContainer);
        interestDtl.setAccount(accuBalance.getAccount());
        interestDtl.setInterestStartDate(interestStartCalendar.getTime());//起息日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(interestDtl.getInterestStartDate());
        calendar.add(Calendar.DAY_OF_MONTH, currentDays.intValue() - 1);
        interestDtl.setBlnInstDate(calendar.getTime());
        interestDtl.setInterestType(instrtType);
        interestDtl.setDays(currentDays.intValue());
        interestDtl.setCumulation(cumulations);
        interestDtl.setCurCode(accuBalance.getCurCode());
        if (InterestProcessType.INTEREST.equals(interestProcessType)) {
            interestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_IST);//计息
            interestDtl.setInterestRate(interestRate);
        } else {
            interestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_PRE);//预提
            BigDecimal rate = getLastInstrateRate(accuBalance.getAccount(), interestRate, interestContainer, blnInstCalendar);
            interestDtl.setInterestRate(rate);
        }
        BigDecimal dayRateType = BigDecimal.ZERO;
        if (new BigDecimal(InterestConstant.RATE_MONTH_DIV).equals(sysRateType)) {
            dayRateType = new BigDecimal(monthDays * 10);
        } else {
            dayRateType = new BigDecimal(monthDays * 12);
        }
        BigDecimal interest = interestDtl.getCumulation().multiply(interestDtl.getInterestRate())
                .divide(dayRateType, 2, BigDecimal.ROUND_HALF_UP);
        interestDtl.setInterest(interest);//利息=积数*利率/360/100
        //interestDtl.setFcAcc(accuBalance.getFcAcc());
        interestDtl.setAccount(accuBalance.getAccount());
        interestDtls.add(interestDtl);
    }
    /**
     * 定期计算某时间内利息通用方法
     * @param interestProcessType ：计息类型
     * @param interestDtls ：利息明细集合
     * @param accuBalance ：调整积数信息
     * @param interestRate ：本次计算利息的利率
     * @param currentDays ：本次计算利息的实际天数
     * @param interestStartCalendar ：本次计算利息的起始日
     * @param blnInstCalendar ：本次计算利息的结束日
     * @param instrtType ：利率类型
     * @param interestContainer
     */
    protected void processCommonInterest(InterestProcessType interestProcessType,
                                         List<FcInterestDtlTemp> interestDtls, FcAccuBalancef accuBalance, BigDecimal interestRate,
                                         int currentDays, Calendar interestStartCalendar, Calendar blnInstCalendar, String instrtType,
                                         InterestInfoContainer interestContainer) {
        if (currentDays <= 0 || interestStartCalendar.compareTo(blnInstCalendar) > 0) {
            return;
        }
        BigDecimal cumulations = accuBalance.getCumu().multiply(BigDecimal.valueOf(currentDays)); 
        FcInterestDtlTemp interestDtl = createInterestDtl(interestContainer);
        interestDtl.setAccount(accuBalance.getAccount());
        interestDtl.setInterestStartDate(interestStartCalendar.getTime());//起息日
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(interestDtl.getInterestStartDate());
//        calendar.add(Calendar.DAY_OF_MONTH, currentDays.intValue()-1);
        Date blnInstDate = DateUtils.addDay(interestDtl.getInterestStartDate(),currentDays-1);
        interestDtl.setBlnInstDate(blnInstDate);
        interestDtl.setInterestType(instrtType);
        interestDtl.setDays(currentDays);
        interestDtl.setCumulation(cumulations);
        interestDtl.setInterestRate(interestRate);
        interestDtl.setCurCode(accuBalance.getCurCode());
        BigDecimal interest = interestDtl.getCumulation().multiply(interestDtl.getInterestRate())
                .divide(sysRateType, 2, BigDecimal.ROUND_HALF_UP);
        interestDtl.setInterest(interest);//利息=积数*利率/360/100
        //interestDtl.setFcAcc(accuBalance.getFcAcc());
        interestDtl.setAccount(accuBalance.getAccount());
        if (InterestProcessType.INTEREST.equals(interestProcessType)) {
            interestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_IST);//计息
        } else {
            interestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_PRE);//预提
        }
        interestDtls.add(interestDtl);
    }
    /**
     * 得到超期利息
     */
    private void processOverDayInterestRates(InterestProcessType interestProcessType,FcAccDTO fcAcc,
                                             List<FcInterestDtlTemp> interestDtls,FcAccuBalancef accuBalance, Calendar deadlineDate,
                                             Calendar endInterestCalendar, String instrtType, BigDecimal tempRate, InterestInfoContainer interestContainer) {
        processInterestBySegment(interestProcessType,fcAcc, interestDtls, accuBalance,deadlineDate, endInterestCalendar,
                instrtType, tempRate, interestContainer, true);
    }
    
    /**
     * 定期天数计算
     */
    private long getNormalDays(String sysFixInterestWay, boolean fixedActrueDays, Calendar startCalendar, Calendar endCalendar){
        if (InterestConstant.INST_FIX_YEAR_MONTH.equals(sysFixInterestWay)) {//定期计息方式：对年对月,或者参数设置为按每月30天计算
            int months = (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 12
                    + (endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH));
            return (long) (months * 30) ;
        } else {//定期计息方式：实际天数,不分段，且按实际天数计算
            return DateUtils.getOffsetDays(startCalendar.getTime(), endCalendar.getTime());
        }
    }
    /**
     * 预提信息
     */
    protected List<FcPredrawDtlTempVO> processPredrawDtls(List<FcInterestDtlTemp> interestDtls,
                                                        InterestInfoContainer interestContainer) {
        Map<String, FcPredrawDtlTempVO> predrawDtls = new HashMap<String, FcPredrawDtlTempVO>();
        for (FcInterestDtlTemp interestDtl : interestDtls) {
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
    
    /**
     *贷方系统科目/查找单位应付利息－企业定期存款账户
     */
    @Override
    public ConstantBean<String> getCreditSysAcc(){
        return  SubjectConstant.SysSubject.INTEREST_PAYABLE_FIXED_DEPOSIT;
    }
    /**
     * 功能：借方系统科目/查找单位利息支出－企业定期存款账户
     */
    @Override
    public ConstantBean<String> getDebitSysAcc(){
        return  SubjectConstant.SysSubject.INTEREST_EXPENSES_FIXED_DEPOSIT;
    }
    @Override
    public void processInterestVoucher(AccInterestInfo accInterestInfo) {
    }
    @Override
    public void createInterestVoucher(AccInterestInfo accInterestInfo) {
        
    }
}
