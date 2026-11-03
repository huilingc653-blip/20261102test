package com.bytter.finance.domain.provide.service.process;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import java.math.BigDecimal;
import java.util.*;
/**
 * <br>
 * <b>功能：</b>人民币基准利率调整<br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2015-8-19 <br>
 * <b>版权所有：<b>版权所有(C) 2010，拜特科技<br>
 */
public class CmsProvideInterestHandler {
    protected ProvideLoanSend send;
    protected CmsProvideLoanInfo info;
    protected InterestVO istBean;
    protected List<CmsProvideInterest> istList;// 存储所有利息数据
    protected List<RepaymentSchedule> schedules;// 获取所有的还款计划
    protected List<RateAdjustBean> rateAdjusts;// 借款利率调整
    protected List<InterestWayBean> istWayBeans;// 计息方式调整
    protected List<IstFloatBean> floatBeans;// 浮动方式调整
    protected List<InstRateBean> rateBeans;// 基准利率调整
    protected Map<String, InstRateBean> forCurMap;// 外币基准利率调整
    private boolean isForCur;// 是否外币调整
    public CmsProvideInterestHandler(ProvideLoanSend send, CmsProvideLoanInfo info, InterestVO istBean) {
        super();
        this.send = send;
        this.info = info;
        this.istBean = istBean;
    }
    public CmsProvideInterestHandler(ProvideLoanSend send, CmsProvideLoanInfo info, List<CmsProvideInterest> istList,
            List<RepaymentSchedule> schedules, List<InterestWayBean> istWayBeans, List<IstFloatBean> floatBeans,
            List<InstRateBean> rateBeans, List<RateAdjustBean> rateAdjusts, boolean isForCur) {
        super();
        this.send = send;
        this.info = info;
        if (null == istList || istList.isEmpty()) {
            istList = new ArrayList<CmsProvideInterest>();
        }
        this.istList = istList;
        if (null == schedules || schedules.isEmpty()) {
            schedules = new ArrayList<RepaymentSchedule>();
        }
        this.schedules = schedules;
        if (null == istWayBeans || istWayBeans.isEmpty()) {
            istWayBeans = new ArrayList<InterestWayBean>();
        }
        this.istWayBeans = istWayBeans;
        if (null == floatBeans || floatBeans.isEmpty()) {
            floatBeans = new ArrayList<IstFloatBean>();
        }
        this.floatBeans = floatBeans;
        if (null == rateBeans || rateBeans.isEmpty()) {
            rateBeans = new ArrayList<InstRateBean>();
        }
        this.rateBeans = rateBeans;
        if (null == rateAdjusts || rateAdjusts.isEmpty()) {
            rateAdjusts = new ArrayList<RateAdjustBean>();
        }
        this.rateAdjusts = rateAdjusts;
        this.isForCur = isForCur;
        if (isForCur) {
            setForCurInstRateMap();
        }
    }
    public Map<String, InstRateBean> getForCurMap() {
        return forCurMap;
    }
    public void setForCurMap(Map<String, InstRateBean> forCurMap) {
        this.forCurMap = forCurMap;
    }
    public boolean isForCur() {
        return isForCur;
    }
    public void setForCur(boolean isForCur) {
        this.isForCur = isForCur;
    }
    public InterestVO getIstBean() {
        return istBean;
    }
    public void setIstBean(InterestVO istBean) {
        this.istBean = istBean;
    }
    public ProvideLoanSend getSend() {
        return send;
    }
    public void setSend(ProvideLoanSend send) {
        this.send = send;
    }
    public List<RepaymentSchedule> getSchedules() {
        return schedules;
    }
    public void setSchedules(List<RepaymentSchedule> schedules) {
        this.schedules = schedules;
    }
    public List<InterestWayBean> getIstWayBeans() {
        return istWayBeans;
    }
    public void setIstWayBeans(List<InterestWayBean> istWayBeans) {
        this.istWayBeans = istWayBeans;
    }
    public List<IstFloatBean> getFloatBeans() {
        return floatBeans;
    }
    public void setFloatBeans(List<IstFloatBean> floatBeans) {
        this.floatBeans = floatBeans;
    }
    public List<InstRateBean> getRateBeans() {
        return rateBeans;
    }
    public void setRateBeans(List<InstRateBean> rateBeans) {
        this.rateBeans = rateBeans;
    }
    public Date getMaxBlnInstDate() {
        return maxBlnInstDate;
    }
    public void setMaxBlnInstDate(Date maxBlnInstDate) {
        this.maxBlnInstDate = maxBlnInstDate;
    }
    public Date getMaxPredrawDate() {
        return maxPredrawDate;
    }
    public void setMaxPredrawDate(Date maxPredrawDate) {
        this.maxPredrawDate = maxPredrawDate;
    }
    public BigDecimal getUnPayInterest() {
        return unPayInterest;
    }
    public void setUnPayInterest(BigDecimal unPayInterest) {
        this.unPayInterest = unPayInterest;
    }
    public void setIstList(List<CmsProvideInterest> istList) {
        this.istList = istList;
    }
    public void setAdjustDatas(List<RepaymentSchedule> schedules, List<InterestWayBean> istWayBeans,
            List<IstFloatBean> floatBeans, List<InstRateBean> rateBeans) {
        this.schedules = schedules;
        this.istWayBeans = istWayBeans;
        this.floatBeans = floatBeans;
        this.rateBeans = rateBeans;
    }
    private void setForCurInstRateMap() {
        forCurMap = new HashMap<String, InstRateBean>();
        for (Iterator<InstRateBean> iterator = rateBeans.iterator(); iterator.hasNext();) {
            InstRateBean rateBean = iterator.next();
            Date effectDate = rateBean.getEffectDate();// 生效日
            String effectDateStr = DateUtils.formatDate(effectDate, "yyyy-MM-dd");
            InstRateBean eBean = forCurMap.get(effectDateStr);
            if (null == eBean) {
                forCurMap.put(effectDateStr, rateBean);
            } else {
                Date oriEffectDate = eBean.getEffectDate();
                if (oriEffectDate.getTime() >= effectDate.getTime()) {
                    forCurMap.put(effectDateStr, eBean);
                }
            }
        }
    }
    /**
     * <br>
     * <b>功能：</b>获取日期所在的天数<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param date
     * @return
     */
    protected int getDayInMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    protected void createInterestWayBeans() {
        Date blnInstDate = istBean.getBlnInstDate();
        InterestWayBean oriBean = new InterestWayBean();
        oriBean.setEffectDate(istBean.getEffectDate());
        oriBean.setInterestWay(istBean.getInterestWay());
        oriBean.setPredrawDay(istBean.getInterestDay());
        oriBean.setInterestDay(istBean.getInterestDay());
        oriBean.setIsStanderd(istBean.getIsStanderd());
        oriBean.setStartDate(istBean.getInterestStartDate());
        oriBean.setEndDate(blnInstDate);
        List<InterestWayBean> istWayBeans = getIstZoneByIstWay(oriBean);
        for (Iterator<InterestWayBean> iterator = istWayBeans.iterator(); iterator.hasNext();) {
            InterestWayBean wayBean = iterator.next();
            InterestVO interestBean = new InterestVO();
            try {
                BeanUtils.copyProperties(interestBean, istBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            interestBean.setLastIstWay(false);
            interestBean.setInterestStartDate(wayBean.getStartDate());
            interestBean.setBlnInstDate(wayBean.getEndDate());
            if (InterestConstant.INTEREST_SIGN_NO.equals(istBean.getInterestSign())) {
                interestBean.setInterestDay(wayBean.getPredrawDay());
            } else {
                interestBean.setInterestDay(wayBean.getInterestDay());
            }
            interestBean.setInterestWay(wayBean.getInterestWay());
            interestBean.setIsStanderd(wayBean.getIsStanderd());
            interestBean.setLastReapyDate(wayBean.getEffectDate());
            boolean flag = false;
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(blnInstDate);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(interestBean.getBlnInstDate());
            if (!iterator.hasNext()) {
                if (DateUtils.dayDiffs(blnInstDate, interestBean.getBlnInstDate()) == 0) {
                    interestBean.setLastIstWay(true);// 最后一次计息方式调整
                }
            }
            if (DateUtils.dayDiffs(blnInstDate, interestBean.getBlnInstDate()) == 0) {
                flag = true;
            }
            istBean = interestBean;
            /**
             * 删除下次计息方式调整启用之后的利息数据 FIXME 待完成
             */
            istBean.setInterestStartDate(removeEffectInterest(wayBean.getStartDate()));
            processInterestDtl(flag);
        }
    }
    /**
     * <br>
     * <b>功能：</b>删除生效日之后的利息数据,<br>
     * </b>并取最后一次计息的止息日作为下次计息的起息日<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-20 <br>
     * 
     * @param effectDate
     * @return
     */
    protected Date removeEffectInterest(Date effectDate) {
        List<CmsProvideInterest> tempList = new ArrayList<CmsProvideInterest>();
        Date blnInstDate = effectDate;
        String interestSign = istBean.getInterestSign();
        for (CmsProvideInterest interest : istList) {
            if (null == interest) {
                continue;
            }
            Integer status = interest.getStatus();
            String instSign = interest.getInterestSign();
            if (!instSign.equals(interestSign)) {
                tempList.add(interest);
                continue;
            }
            if ("1".equals(status)) {
                tempList.add(interest);
                blnInstDate = DateUtils.addDay(interest.getBlnInstDate(), 1);// 止息日次日作为下次的起息日
            }
            Date repayDate = interest.getRepayDate();
            if (effectDate.getTime() >= repayDate.getTime()) {
                tempList.add(interest);
                blnInstDate = DateUtils.addDay(interest.getBlnInstDate(), 1);// 止息日次日作为下次的起息日
            } else {
                break;
            }
        }
        istList = tempList;
        return blnInstDate;
    }
    /**
     * <br>
     * <b>功能：</b>处理利息明细<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param flag
     *            是否是还款日
     * @return
     */
    protected void processInterestDtl(boolean flag) {
        Date sendDate = istBean.getInterestStartDate();// 发放日期
        Integer interestDay = istBean.getInterestDay();// 计息日
        Integer sendDay = getDayInMonth(sendDate);// 发放天
        Calendar calendar = getFirstDayCalender(sendDate);
        Integer peroid = 0;
        String interestWay = istBean.getInterestWay();
        if (InterestConstant.INTEREST_SIGN_NO.equals(istBean.getInterestSign())) {
            peroid = getPredrawPeroid(interestWay);// 预提
        } else {
            peroid = getInstPeroid(interestWay);// 结息
        }
        boolean isStanderd = "02".equals(istBean.getIsStanderd()) || "03".equals(istBean.getIsStanderd())
                || "05".equals(istBean.getIsStanderd()) || (peroid == 1);// 是否标准季半年年
        if (100 != peroid && 200 != peroid && 2 != peroid) {
            if (isStanderd) {
                setInstDateDay(calendar, interestDay);
                istBean.setRepayCalendar(calendar);
            }
        }
        String interestSign = istBean.getInterestSign();
        if (InterestConstant.INTEREST_SIGN_YES.equals(interestSign)) {// 计息方式不计息,只生成还款计划当天的数据
            if (peroid == 0) {
                // 生成一条还款日期的结息数据
                createInterestDtl(true);
                return;
            }
        }
        if (InterestConstant.INTEREST_SIGN_NO.equals(interestSign)) {// 预提方式不计息,只生成还款计划当天的数据
            if (peroid == 0) {
                // 生成一条还款日期的结息数据
                if (flag) {
                    createInterestDtl(true);
                }
                return;
            }
        }
        if (peroid == 100 || peroid == 200 || peroid == 2) {
            compareInstDate();
        } else if (!isStanderd) {
            if (peroid == 3 || peroid == 6 || peroid == 12) {
                if (sendDate.getTime() < istBean.getBlnInstDate().getTime()) {
                    compareInstDate();
                }
            }
        }
        if (100 != peroid && 200 != peroid && 2 != peroid && sendDay < interestDay) {// 发放日小于计息日
            /**
             * 发放日当月能否满足结息要求
             */
            if (peroid != 100 && peroid != 200 && peroid != 2) {
                if (isStanderd) {
                    if (InterestConstant.INTEREST_SIGN_YES.equals(interestSign)) {
                        if (isInstDateMonth(calendar, peroid)) {// 计息月计息
                            compareInstDate();
                        }
                    } else {
                        if (!isInstDateMonth(calendar, peroid)) {// 非计息则预提
                            compareInstDate();
                        }
                    }
                } else {
                    if (InterestConstant.INTEREST_SIGN_NO.equals(interestSign)) {
                        compareInstDate();
                    }
                }
                /*
                 * if (isInstDateMonth(calendar, peroid)) { compareInstDate(); }
                 */
            }
        }
        // 下个计息周期,递归调用,比较计息日和还款日
        createInstDateIstDtl(flag);
    }
    /**
     * <br>
     * <b>功能：</b>获取计息区间的计息方式<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-19 <br>
     * 
     * @param oriBean
     * @return
     */
    protected List<InterestWayBean> getIstZoneByIstWay(InterestWayBean oriBean) {
        List<InterestWayBean> dates = new ArrayList<InterestWayBean>();
        Date startDate = istBean.getInterestStartDate();// 起息日期
        Date endDate = istBean.getBlnInstDate();// 止息日期
        Date tempDate = startDate;
        for (Iterator<InterestWayBean> iterator = istWayBeans.iterator(); iterator.hasNext();) {
            InterestWayBean wayBean = iterator.next();
            Date effectDate = wayBean.getEffectDate();// 生效日期
            if (effectDate.getTime() <= tempDate.getTime()) {// 生效日期<起息日
                if (iterator.hasNext()) {
                    continue;
                } else {
                    InterestWayBean bean = new InterestWayBean();
                    bean.setEffectDate(tempDate);
                    bean.setStartDate(tempDate);
                    bean.setEndDate(endDate);
                    bean.setInterestWay(wayBean.getInterestWay());
                    if (InterestConstant.INTEREST_SIGN_YES.equals(istBean.getInterestSign())) {
                        bean.setInterestDay(wayBean.getInterestDay());
                        bean.setPredrawDay(wayBean.getPredrawDay());
                    } else {
                        // 预提修改 20150923 预提的时候启用预提日期
                        bean.setInterestDay(wayBean.getPredrawDay());
                        bean.setPredrawDay(wayBean.getPredrawDay());
                    }
                    bean.setIsStanderd(wayBean.getIsStanderd());
                    dates.add(bean);
                    break;
                }
            } else if (effectDate.getTime() <= endDate.getTime()) {// 起息日<生效日期<止息日
                InterestWayBean bean = new InterestWayBean();
                bean.setEffectDate(tempDate);
                bean.setStartDate(tempDate);
                bean.setEndDate(effectDate);
                bean.setInterestWay(oriBean.getInterestWay());
                // bean.setInterestDay(oriBean.getInterestDay());
                if (InterestConstant.INTEREST_SIGN_YES.equals(istBean.getInterestSign())) {
                    bean.setInterestDay(oriBean.getInterestDay());
                    bean.setPredrawDay(oriBean.getPredrawDay());
                } else {
                    // 预提修改 20150923 预提的时候启用预提日期
                    bean.setInterestDay(oriBean.getPredrawDay());
                    bean.setPredrawDay(oriBean.getPredrawDay());
                }
                bean.setIsStanderd(oriBean.getIsStanderd());
                dates.add(bean);
                tempDate = effectDate;
                oriBean.setInterestWay(wayBean.getInterestWay());
                if (InterestConstant.INTEREST_SIGN_YES.equals(istBean.getInterestSign())) {
                    oriBean.setInterestDay(wayBean.getInterestDay());
                    oriBean.setPredrawDay(wayBean.getPredrawDay());
                } else {
                    // 预提修改 20150923 预提的时候启用预提日期
                    oriBean.setInterestDay(wayBean.getPredrawDay());
                    oriBean.setPredrawDay(wayBean.getPredrawDay());
                }
                oriBean.setIsStanderd(wayBean.getIsStanderd());
                oriBean.setStartDate(tempDate);
                if (!iterator.hasNext()) {
                    oriBean.setEffectDate(effectDate);
                    oriBean.setEndDate(endDate);
                    dates.add(oriBean);
                }
            } else {// 生效日期>止息日
                break;
            }
        }
        if (dates.isEmpty()) {
            dates.add(oriBean);
        }
        return dates;
    }
    /**
     * <br>
     * <b>功能：</b>根据计息方式获取获取计息周期<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param interestWay
     *            计息方式
     * @return
     */
    protected int getInstPeroid(String interestWay) {
        if (InterestConstant.PREDRAW_SIGN_MONTH_INTEREST.equals(interestWay)) {
            return 1;
        } else if (InterestConstant.PREDRAW_SIGN_SEASON_NO_ANTICIPATE.equals(interestWay)
                || InterestConstant.PREDRAW_SIGN_SEASON_ANTICIPATE.equals(interestWay)) {
            return 3;
        } /*
           * else if (InterestWay.INTEREST_WAY_11.get().equals(interestWay) ||
           * InterestWay.INTEREST_WAY_12.get().equals(interestWay)) { return 6; } else if
           * (InterestWay.INTEREST_WAY_15.get().equals(interestWay) ||
           * InterestWay.INTEREST_WAY_16.get().equals(interestWay)) { return 12; } else if
           * (InterestWay.INTEREST_WAY_21.get().equals(interestWay)) { return 2; } else if
           * (InterestWay.INTEREST_WAY_19.get().equals(interestWay)) { return 100;//一周 } else if
           * (InterestWay.INTEREST_WAY_20.get().equals(interestWay)) { return 200;//两周 }
           */
        return 0;
    }
    /**
     * <br>
     * <b>功能：</b>根据计息方式获取获取预提周期<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param interestWay
     * @return
     */
    // FIXME 预提周期待确定
    protected Integer getPredrawPeroid(String interestWay) {
        if (InterestConstant.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.equals(interestWay)
                || InterestConstant.PREDRAW_SIGN_SEASON_ANTICIPATE.equals(interestWay)) {
            return 3;
        } /*
           * else if (InterestWay.INTEREST_WAY_11.get().equals(interestWay) ||
           * InterestWay.INTEREST_WAY_13.get().equals(interestWay)) { return 6; } else if
           * (InterestWay.INTEREST_WAY_15.get().equals(interestWay) ||
           * InterestWay.INTEREST_WAY_17.get().equals(interestWay)) { return 12; }
           */
        return 0;
    }
    /**
     * <br>
     * <b>功能：</b>当前月份是否满足计息月<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param calendar
     * @param count
     * @return
     */
    protected boolean isInstDateMonth(Calendar calendar, Integer count) {
        if (count == 0) {
            return false;
        }
        Integer month = calendar.get(Calendar.MONTH) + 1;
        if (12 == count) {
            return month % count == 0;// 是否12月份
        } else if (6 == count) {
            return month % count == 0;
        } else {
            return month % count == 0;
        }
    }
    /**
     * <br>
     * <b>功能：</b>获取给定日期的第一天<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-17 <br>
     * 
     * @param date
     * @return
     */
    protected Calendar getFirstDayCalender(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
    /**
     * <br>
     * <b>功能：</b>设置计息日<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param calendar
     * @param interestDay
     */
    protected void setInstDateDay(Calendar calendar, Integer interestDay) {
        Integer lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 月份最后一天
        if (interestDay < lastDay && interestDay != 0) {// 结息日小于月末最后一天
            calendar.set(Calendar.DAY_OF_MONTH, interestDay);// 结息日
        } else {
            // 结息日大于月末最后一天,设置最后一天为结息日
            calendar.set(Calendar.DAY_OF_MONTH, lastDay);// 月末最后一天作为结息日
        }
    }
    /**
     * <br>
     * <b>功能：</b>止息日之前递归结息日生成计息数据<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param istBean
     * @param send
     * @return
     */
    protected void createInstDateIstDtl(boolean flag) {
        while (istBean.getRepayCalendar().getTime().getTime() < istBean.getBlnInstDate().getTime()) {
            generateInterestDtl();
        }
        Date repayDate = istBean.getRepayCalendar().getTime();
        if (flag && istBean.getRepayCalendar().getTime().getTime() > istBean.getBlnInstDate().getTime()) {
            generateInterestDtl();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(repayDate);
            istBean.setRepayCalendar(calendar);
        }
    }
    /**
     * <br>
     * <b>功能：</b>生成利息明细<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param istBean
     * @param send
     * @return
     */
    protected void generateInterestDtl() {
        int interestDay = istBean.getInterestDay();
        String interestWay = istBean.getInterestWay();
        int count = 0;
        if (InterestConstant.INTEREST_SIGN_NO.equals(istBean.getInterestSign())) {
            count = getPredrawPeroid(interestWay);// 预提
        } else {
            count = getInstPeroid(interestWay);// 结息
        }
        boolean isStanderd = "02".equals(istBean.getIsStanderd()) || "03".equals(istBean.getIsStanderd())
                || "05".equals(istBean.getIsStanderd()) || (count == 1);// 是否标准季半年年
        String interestSign = istBean.getInterestSign();
        Calendar calendar = null;
        if (InterestConstant.INTEREST_SIGN_YES.equals(interestSign)) {
            calendar = calcInstPeriod(istBean.getInterestWay(), istBean);
        } else {
            // 如果满足自然月则不做预提处理
            calendar = addNextMonth(istBean.getInterestWay(), istBean);
            if (InterestConstant.PREDRAW_SIGN_SEASON_ANTICIPATE.equals(interestWay)
            /*
             * || InterestWay.INTEREST_WAY_11.get().equals(interestWay) ||
             * InterestWay.INTEREST_WAY_15.get().equals(interestWay)
             */) {
                if (isStanderd) {
                    if (isInstDateMonth(calendar, count)) {
                        return;
                    }
                } else {
                    if (calcPeroid(istBean.getLastReapyDate(), calendar, count)) {
                        return;
                    }
                }
            }
        }
        if (100 != count && 200 != count && 2 != count) {
            if (isStanderd) {
                setInstDateDay(calendar, interestDay);
            }
        }
        istBean.setRepayCalendar(calendar);
        compareInstDate();
    }
    private Calendar addNextMonth(String interestWay, InterestVO istBean) {
        Calendar calendar = istBean.getRepayCalendar();
        int count = getPredrawPeroid(interestWay);
        boolean isStanderd = "02".equals(istBean.getIsStanderd()) || "03".equals(istBean.getIsStanderd())
                || "05".equals(istBean.getIsStanderd());// 是否标准季半年年
        if (100 != count && 200 != count && 2 != count) {
            if (isStanderd) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return getNextInstPeroid(istBean.getInterestSign(), calendar, interestWay, 1, isStanderd);
    }
    private boolean calcPeroid(Date sendDate, Calendar calendar, Integer count) {
        Calendar staCalendar = Calendar.getInstance();
        staCalendar.setTime(sendDate);
        int months = (calendar.get(Calendar.YEAR) - staCalendar.get(Calendar.YEAR)) * 12 + calendar.get(Calendar.MONTH)
                - staCalendar.get(Calendar.MONTH);
        return months % count == 0;
    }
    /**
     * <br>
     * <b>功能：</b>计算下个计息周期<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param interestWay
     * @param istBean
     * @return
     */
    protected Calendar calcInstPeriod(String interestWay, InterestVO istBean) {
        Calendar calendar = istBean.getRepayCalendar();
        int count = 0;
        if (InterestConstant.INTEREST_SIGN_NO.equals(istBean.getInterestSign())) {
            count = getPredrawPeroid(interestWay);// 预提
        } else {
            count = getInstPeroid(interestWay);// 结息
        }
        boolean isStanderd = "02".equals(istBean.getIsStanderd()) || "03".equals(istBean.getIsStanderd())
                || "05".equals(istBean.getIsStanderd()) || (count == 1);// 是否标准季半年年
        if (100 != count && 200 != count && 2 != count) {
            if (isStanderd) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return getNextInstPeroid(istBean.getInterestSign(), calendar, interestWay, count, isStanderd);
    }
    /**
     * <br>
     * <b>功能：</b>获取下一计息周期<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-22 <br>
     * 
     * @param interestSign
     * @param calendar
     * @param interestWay
     * @param count
     * @return
     */
    protected Calendar getNextInstPeroid(String interestSign, Calendar calendar, String interestWay, Integer count,
            boolean isStanderd) {
        if (InterestConstant.INTEREST_SIGN_NO.equals(interestSign)) {
            return getInstDate(calendar, isStanderd, count);
            /*
             * if (InterestWay.INTEREST_WAY_03.get().equals(interestWay) ||
             * InterestWay.INTEREST_WAY_11.get().equals(interestWay) ||
             * InterestWay.INTEREST_WAY_15.get().equals(interestWay)) { return getInstDate(calendar, true, count); }
             * else { return getInstDate(calendar, false, count); }
             */
        } else {
            if (/* InterestWay.INTEREST_WAY_21.get().equals(interestWay) || */ InterestConstant.PREDRAW_SIGN_MONTH_INTEREST
                    .equals(interestWay)) {
                return getInstDate(calendar, false, count);
            } /*
               * else if (InterestWay.INTEREST_WAY_19.get().equals(interestWay)) { calendar.add(Calendar.DAY_OF_MONTH,
               * 7);//一周 return calendar; } else if (InterestWay.INTEREST_WAY_20.get().equals(interestWay)) {
               * calendar.add(Calendar.DAY_OF_MONTH, 14);//一周 return calendar; }
               */
            return getInstDate(calendar, isStanderd, count);
            // return getInstDate(calendar, true, count);
        }
    }
    /**
     * <br>
     * <b>功能：</b>获取计息方式的计息周期月份<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param calendar
     * @param isStanderd
     * @param count
     * @return
     */
    protected Calendar getInstDate(Calendar calendar, boolean isStanderd, Integer count) {
        if (isStanderd) {// 标准
            if (12 == count) {
                Integer month = calendar.get(Calendar.MONTH) + 1;
                do {
                    calendar.add(Calendar.MONTH, 1);// 月份+1
                    month = calendar.get(Calendar.MONTH) + 1;
                } while (month % count != 0);
            } else if (6 == count) {
                Integer month = calendar.get(Calendar.MONTH) + 1;
                do {
                    calendar.add(Calendar.MONTH, 1);// 月份+1
                    month = calendar.get(Calendar.MONTH) + 1;
                } while (month % count != 0);
            } else {
                Integer month = calendar.get(Calendar.MONTH) + 1;
                do {
                    calendar.add(Calendar.MONTH, 1);// 月份+1
                    month = calendar.get(Calendar.MONTH) + 1;
                } while (month % count != 0);
            }
        } else {// 自然
            calendar.add(Calendar.MONTH, count);// 月份+1
        }
        return calendar;
    }
    /**
     * <br>
     * <b>功能：</b>比较结息日和还款日,生成对应的利息数据<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param istBean
     * @param send
     * @return
     */
    protected void compareInstDate() {
        Date repayDate = istBean.getBlnInstDate();
        Date instDate = istBean.getRepayCalendar().getTime();
        // System.out.println("repayDate:" + repayDate + "/ instDate:" + instDate);
        boolean isStanderd = "02".equals(istBean.getIsStanderd()) || "03".equals(istBean.getIsStanderd())
                || "05".equals(istBean.getIsStanderd());// 是否标准季半年年
        String interestSign = istBean.getInterestSign();
        String interestWay = istBean.getInterestWay();
        if (InterestConstant.INTEREST_SIGN_NO.equals(interestSign)) {
            if (InterestConstant.PREDRAW_SIGN_SEASON_ANTICIPATE.equals(interestWay)
            /*
             * || InterestWay.INTEREST_WAY_11.get().equals(interestWay) ||
             * InterestWay.INTEREST_WAY_15.get().equals(interestWay)
             */) {
                if (!isStanderd) {
                    if (calcPeroid(istBean.getLastReapyDate(), istBean.getRepayCalendar(),
                            getPredrawPeroid(istBean.getInterestWay()))) {
                        return;
                    }
                }
            }
        }
        if (instDate.getTime() >= repayDate.getTime()) {// 还款日小于计息日
            if (istBean.isLastIstWay()) {
                // 生成一条还款日期的结息数据
                createInterestDtl(true);
            } else {
                createInterestDtl(false);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(repayDate);
                istBean.setRepayCalendar(calendar);
            }
        } else if (instDate.getTime() < repayDate.getTime()) {// 还款日>计息日
            Date interestStartDate = istBean.getInterestStartDate();
            if (interestStartDate.getTime() < instDate.getTime()) {
                // 生成一条计息日的结息数据
                istBean.setBlnInstDate(instDate);// 设置止息日为计息日
                createInterestDtl(false);
            }
            istBean.setBlnInstDate(repayDate);// 设置下次止息日为预计还款日
        }
    }
    /**
     * <br>
     * <b>功能：</b>根据浮动方式产生利息数据<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param dateSign
     * @param istBean
     * @param send
     * @return
     */
    public void createInterestDtl(boolean dateSign) {
        InstRateBean oriBean = new InstRateBean();
        oriBean.setEffectDate(istBean.getEffectDate());
        oriBean.setStartDate(istBean.getInterestStartDate());
        oriBean.setEndDate(istBean.getBlnInstDate());
        oriBean.setOriRate(istBean.getInterestRate());
        oriBean.setNewRate(istBean.getInterestRate());
        boolean sign = false;
        istBean.setSignRepayDate(istBean.getBlnInstDate());// 设置还款日期标识 给分段标识还款日期
        List<InstRateBean> istRateBeans = getIstZoneByRate(oriBean);
        /**
         * 获取基准利率区间
         */
        for (Iterator<InstRateBean> iterator = istRateBeans.iterator(); iterator.hasNext();) {
            InstRateBean rateBean = iterator.next();
            InterestVO interestBean = new InterestVO();
            try {
                BeanUtils.copyProperties(interestBean, istBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // interestBean.setInterestStartDate(rateBean.getStartDate());
            interestBean.setBlnInstDate(rateBean.getEndDate());
            interestBean.setInterestRate(rateBean.getNewRate());
            interestBean.setSendDate(rateBean.getEffectDate());// 生效日作为发放日
            if (!iterator.hasNext()) {
                interestBean.setLastFloat(true);// 最后一次浮动
                sign = dateSign;// 最后一次
            } else {
                if (floatBeans.size() > 1) {
                    interestBean.setFloatTimes(true);// 多次浮动
                }
            }
            istBean = interestBean;
            /**
             * 删除下次利率调整生效日期之后的利息数据
             */
            // istBean.setInterestStartDate(removeEffectInterest(rateBean.getEffectDate()));
            istBean.setInterestStartDate(removeEffectInterest(istBean.getInterestStartDate()));
            getRecentFloatWay();// 设置最近的浮动方式
            createFloatInterestDtl(sign);
        }
    }
    /**
     * <br>
     * <b>功能：</b>按照基准利率调整划分计息区间<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-20 <br>
     * 
     * @param oriBean
     * @return
     */
    protected List<InstRateBean> getIstZoneByRate(InstRateBean oriBean) {
        List<InstRateBean> dates = new ArrayList<InstRateBean>();
        Date startDate = istBean.getInterestStartDate();// 起息日期
        Date endDate = istBean.getBlnInstDate();// 止息日期
        Date tempDate = istBean.getEffectDate();
        Date sendEffectDate = send.getRateEntriesEffectDate();// 浮动生效日期
        // BigDecimal newRate = oriBean.getNewRate();//新利率
        BigDecimal oriRate = oriBean.getOriRate();// 旧利率
        String floatWay = istBean.getFloatWay();
        if (!"01".equals(floatWay)) {// 非固定利率
            for (Iterator<InstRateBean> iterator = rateBeans.iterator(); iterator.hasNext();) {
                InstRateBean rateBean = iterator.next();
                Date effectDate = rateBean.getEffectDate();// 生效日期
                // if(effectDate.getTime() >= sendEffectDate.getTime()){//生效日期 >= 浮动生效日期
                if (effectDate.getTime() <= startDate.getTime()) {// 生效日期<起息日
                    if (iterator.hasNext()) {
                        continue;
                    } else {
                        InstRateBean bean = new InstRateBean();
                        bean.setEffectDate(tempDate);
                        bean.setStartDate(tempDate);
                        bean.setEndDate(endDate);
                        bean.setNewRate(rateBean.getNewRate());
                        bean.setOriRate(rateBean.getOriRate());
                        dates.add(bean);
                        break;
                    }
                } else if (effectDate.getTime() <= endDate.getTime()) {// 起息日<生效日期<止息日
                    if (sendEffectDate.getTime() >= effectDate.getTime()) {// 生效日期 在 浮动日期生效之前 不使用其他的利率调整
                        InstRateBean bean = new InstRateBean();
                        bean.setEffectDate(tempDate);
                        bean.setStartDate(tempDate);
                        bean.setEndDate(endDate);
                        bean.setNewRate(rateBean.getNewRate());
                        bean.setOriRate(oriRate);
                        addDates(dates, bean);
                        tempDate = effectDate;
                        oriRate = rateBean.getNewRate();
                    } else {
                        InstRateBean bean = new InstRateBean();
                        bean.setEffectDate(tempDate);
                        bean.setStartDate(tempDate);
                        bean.setEndDate(endDate);
                        bean.setNewRate(oriRate);
                        bean.setOriRate(oriRate);
                        addDates(dates, bean);
                        tempDate = effectDate;
                        InstRateBean bean2 = new InstRateBean();
                        bean2.setStartDate(tempDate);
                        bean2.setEffectDate(tempDate);
                        bean2.setEndDate(endDate);
                        bean2.setNewRate(rateBean.getNewRate());
                        bean2.setOriRate(oriRate);
                        addDates(dates, bean2);
                        oriRate = rateBean.getNewRate();
                    }
                } else {// 生效日期>止息日
                    break;
                }
                // }
            }
        }
        if (dates.isEmpty()) {
            dates.add(oriBean);
        }
        return dates;
    }
    private void sendEffectDate() {
    }
    private void addDates(List<InstRateBean> dates, InstRateBean bean) {
        if (!EmptyUtils.isEmptyList(dates) && dates.size() >= 1) {// 存在一个元素
            InstRateBean irb = dates.get(dates.size() - 1);// 获取最后一个元素
            irb.setEndDate(bean.getEffectDate());
            dates.add(bean);
        } else {
            dates.add(bean);
        }
    }
    protected void getRecentFloatWay() {
        String floatWay = istBean.getFloatWay();
        String floatWayNext = istBean.getFloatWayNext();
        String floatWayStanderd = istBean.getFloatWayStanderd();
        BigDecimal floatFixRate = istBean.getFloatFixRate();
        Date sendDate = istBean.getSendDate();
        for (IstFloatBean floatBean : floatBeans) {
            Date effectDate = floatBean.getEffectDate();
            if (effectDate.getTime() < sendDate.getTime()) {
                floatWay = floatBean.getFloatWay();
                floatWayNext = floatBean.getFloatWayNext();
                floatWayStanderd = floatBean.getFloatWayStanderd();
                floatFixRate = floatBean.getFixRate();// 浮动方式为固定时的浮动利率
            } else {
                break;
            }
        }
        istBean.setFloatWay(floatWay);
        istBean.setFloatWayNext(floatWayNext);
        istBean.setFloatWayStanderd(floatWayStanderd);
        istBean.setFloatFixRate(floatFixRate);
    }
    protected void createFloatInterestDtl(boolean dateSign) {
        Date repayDate = istBean.getBlnInstDate();
        Date interestStartDate = istBean.getInterestStartDate();
        String floatWay = istBean.getFloatWay();// 浮动方式
        BigDecimal plRate = istBean.getInterestRate();// 基准利率
        if ("01".equals(floatWay)) {// 固定利率
            // plRate = send.getPlRate();
            plRate = istBean.getFloatFixRate();// 浮动方式为固定利率时的利率
            if (EmptyUtils.isEmptyList(rateAdjusts)) {
                BigDecimal rate = this.deferendRate(plRate);// 添加展期分段
                istList.add(createProvideInterest(dateSign, rate));
            } else {
                for (Iterator<RateAdjustBean> iterator = rateAdjusts.iterator(); iterator.hasNext();) {// 利率调整
                    RateAdjustBean justBean = iterator.next();
                    Date rateAdjustStartDate = justBean.getEnterDate();// 利率调整开始日期
                    if (istBean.getInterestStartDate().getTime() > rateAdjustStartDate.getTime()) {// 起息日期 > 利率调整开始日期
                        plRate = justBean.getPlRate();// 利率调整作为新利率
                        // if (istBean.isLastRepayment()) {//浮动调整最后一次
                        BigDecimal rate = this.deferendRate(plRate);// 添加展期分段
                        istList.add(createProvideInterest(dateSign, rate));
                    } else if (istBean.getBlnInstDate().getTime() >= rateAdjustStartDate.getTime()) {// 起息日期 < 利率调整开始日期<
                                                                                                     // 止息日期 利率调整分段
                        Date blnInstDate = istBean.getBlnInstDate();
                        istBean.setBlnInstDate(rateAdjustStartDate);// 利率调整开始日期为止息日
                        istList.add(createProvideInterest(false, plRate, "1"));// 生效前 使用固定利率
                        istBean.setBlnInstDate(blnInstDate);// 止息日下移
                        plRate = justBean.getPlRate();// 利率调整作为新利率
                        // if (istBean.isLastRepayment()) {//浮动调整最后一次
                        BigDecimal rate = this.deferendRate(plRate);// 添加展期分段
                        istList.add(createProvideInterest(dateSign, rate));
                    } else {// 止息日期 <= 利率调整开始日期
                            // if (istBean.isLastRepayment()) {//浮动调整最后一次
                        BigDecimal rate = this.deferendRate(plRate);// 添加展期分段
                        istList.add(createProvideInterest(dateSign, rate));
                    }
                }
            }
        } else {
            BigDecimal floatPoint = send.getPointsProportionText();// 比例、点数
            String floatType = send.getPointsProportionSelect();// 比例、点数浮动
            /*
             * boolean userNewRate = istBean.isUserNewRate(); if (userNewRate) {//已经使用了新的浮动利率 Double effectRate =
             * calcFloatRate(plRate, floatType, floatPoint); istList.add(createProvideInterest(dateSign, effectRate));
             * return; }
             */
            Date effectDate = null;
            if (InterestConstant.INTEREST_SIGN_NO.equals(istBean.getInterestSign())) {
                effectDate = getInstEffectDate(istBean.getSendDate(), istBean.getPredrawDay());// 预提的时候用PredrawDay存储计息日
            } else {
                effectDate = getInstEffectDate(istBean.getSendDate(), istBean.getInterestDay());// 调整利率生效日
            }
            /**
             * 判断生效日是否在计息区间内
             */
            BigDecimal effectRate = calcFloatRate(plRate, floatType, floatPoint);
            if (effectDate.getTime() < interestStartDate.getTime() && istBean.isLastFloat()) {// 起息日>生效日,新利率
                BigDecimal rate = this.deferendRate(effectRate);// 添加展期分段
                istList.add(createProvideInterest(dateSign, rate));
                istBean.setUserNewRate(true);// 已经使用了浮动利率,后续利率都需要浮动
            } else if (effectDate.getTime() <= repayDate.getTime()) {// 起息日<生效日<止息日,分段
                Date blnInstDate = istBean.getBlnInstDate();
                istBean.setBlnInstDate(effectDate);// 生效日为止息日
                BigDecimal rate = null;
                if (istBean.isUserNewRate()) {
                    rate = this.deferendRate(effectRate);// 添加展期分段
                } else {
                    rate = this.deferendRate(istBean.getFloatFixRate());// 添加展期分段
                }
                istList.add(createProvideInterest(false, rate, "1"));// 生效前 使用固定利率
                istBean.setUserNewRate(true);// 已经使用了浮动利率,后续利率都需要浮动
                istBean.setBlnInstDate(blnInstDate);// 从生效日到止息日
                rate = this.deferendRate(effectRate);// 添加展期分段
                istList.add(createProvideInterest(dateSign, rate));// 生效后
            } else {// 生效日>止息日,浮动前利率
                BigDecimal rate = null;
                if (istBean.isUserNewRate()) {
                    rate = this.deferendRate(effectRate);// 添加展期分段
                } else {
                    rate = this.deferendRate(istBean.getFloatFixRate());// 添加展期分段
                }
                istList.add(createProvideInterest(dateSign, rate));
            }
        }
    }
    /**
     * 设置展期之后 使用展期利率 TODO 利率调整 是否启用展期利率
     */
    private BigDecimal deferendRate(BigDecimal rate) {
        if (!EmptyUtils.isEmpty(send.getDeferredDate())) {// 展期
            Date deferendStartDate = send.getRepayDate();// 展期开始日期
            if (istBean.getInterestStartDate().getTime() >= deferendStartDate.getTime()) {// 起息日期 >= 展期日期
                return send.getDeferredRate();// 展期利率作为新利率
            } else if (istBean.getBlnInstDate().getTime() > deferendStartDate.getTime()) {// 起息日期 < 展期日期 < 止息日期 展期分段
                Date blnInstDate = istBean.getBlnInstDate();
                istBean.setBlnInstDate(deferendStartDate);// 展期开始日期为止息日
                istList.add(createProvideInterest(false, rate, "1"));// 生效前 使用固定利率
                istBean.setBlnInstDate(blnInstDate);// 止息日下移
                return send.getDeferredRate();// 展期利率作为新利率
            } else {// 止息日期 <= 展期日期
                return rate;
            }
        }
        return rate;
    }
    /**
     * <br>
     * <b>功能：</b>产生借款利息明细数据<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-18 <br>
     * 
     * @param dateSign
     * @param interestRate
     * @return
     */
    protected CmsProvideInterest createProvideInterest(boolean dateSign, BigDecimal interestRate, String... flag) {
        CmsProvideInterest ist = new CmsProvideInterest();
        Date repayDate = istBean.getBlnInstDate();
        Date interestStartDate = istBean.getInterestStartDate();
        BigDecimal tempMoney = istBean.getLoanMoney();
        BigDecimal unPayInterest = istBean.getUnPayInterest();
        ist.setRepaymentScheduleId(istBean.getRepaymentScheduleId());// 设置还款计划ID
        ist.setSignRepayDate(istBean.getSignRepayDate());
        ist.setContractCode(send.getContractCode());
        ist.setBillCode(send.getBillCode());
        ist.setCorpId(send.getCorpId());
        ist.setCurId(send.getCurCode());
        ist.setRepayDate(repayDate);
        ist.setInterestType("1");// 正常
        ist.setInterestStartDate(interestStartDate);
        int interestDay = istBean.getInterestDay();// 预提日
        if (InterestConstant.INTEREST_SIGN_YES.equals(istBean.getInterestSign())) {
            ist.setBlnInstDate(DateUtils.addDay(repayDate, -1));// 结束日期为还款日前一天
        } else {
            if (31 == interestDay) {// 月末预提
                if (dateSign || istBean.isFloatTimes()) {// 最后一次还款
                    ist.setBlnInstDate(DateUtils.addDay(repayDate, -1));// 结束日期为还款日前一天
                } else {// 如果为预提日期,则等于预提日期
                    ist.setBlnInstDate(repayDate);// 结束日期为还款日
                }
            } else {
                ist.setBlnInstDate(DateUtils.addDay(repayDate, -1));// 结束日期为还款日前一天
            }
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(repayDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(interestStartDate);
        if (InterestConstant.INTEREST_SIGN_NO.equals(istBean.getInterestSign()) && (31 == interestDay) && !dateSign
                && !istBean.isFloatTimes()) {
            ist.setDays(DateUtils.dateDiff('d', calendar1, calendar2) + 1);
        } else {
            ist.setDays(DateUtils.dateDiff('d', calendar1, calendar2));
        }
        if (ist.getDays() <= 0) {
            return null;
        }
        ist.setRate(istBean.getInterestRate());// 原利率
        ist.setFloatPoint(send.getPointsProportionText());// 浮动点数
        ist.setInterestRate(interestRate);// 计息利率
        ist.setRateFax(send.getInterestRate());
        if (null == ist.getRateFax()) {
            ist.setRateFax(BigDecimal.ZERO);
        }
        ist.setLoanMoney(tempMoney);
        if (dateSign) {// 还款日还款
            ist.setRepayMoney(istBean.getRepayMoney());
        } else {
            ist.setRepayMoney(BigDecimal.ZERO);
        }
        BigDecimal leftMoney = ist.getLoanMoney().subtract(ist.getRepayMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);
        ist.setLeftMoney(leftMoney);
        String rateBaseStr = info.getRateBase();
        BigDecimal rateBase = new BigDecimal(360);
        try {
            rateBase = new BigDecimal(rateBaseStr);
        } catch (Exception e) {
            rateBase = new BigDecimal(360);
        }
        BigDecimal interest = ist.getRepayMoney().multiply(new BigDecimal(ist.getDays()))
                .multiply(ist.getInterestRate()).divide(rateBase, 10, BigDecimal.ROUND_HALF_UP)
                // .divide(new BigDecimal(100),10,BigDecimal.ROUND_HALF_UP)
                .setScale(2, BigDecimal.ROUND_HALF_UP);// 避免精度丢失
        ist.setInterest(interest);// 归还本金利息
        BigDecimal totalInterest = ist.getLoanMoney().multiply(new BigDecimal(ist.getDays()))
                .multiply(ist.getInterestRate()).divide(rateBase, 10, BigDecimal.ROUND_HALF_UP)
                // .divide(new BigDecimal(100),10,BigDecimal.ROUND_HALF_UP)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        ist.setTotalInterest(totalInterest);// 全部利息
        BigDecimal payInterest = BigDecimal.ZERO;
        if (dateSign) {
            // 利息不下移
            // if (istBean.isLastRepayment()) {//最后一次还款
            payInterest = ist.getTotalInterest().add(unPayInterest).setScale(2, BigDecimal.ROUND_HALF_UP);
            ist.setPayInterest(payInterest);
            ist.setUnPayInterest(BigDecimal.ZERO);
            istBean.setUnPayInterest(BigDecimal.ZERO);
            /*
             * } else { payInterest = ist.getInterest().setScale(2,BigDecimal.ROUND_HALF_UP);
             * ist.setPayInterest(payInterest); BigDecimal unPayInteres =
             * ist.getTotalInterest().subtract(ist.getPayInterest()).setScale(2,BigDecimal.ROUND_HALF_UP);
             * ist.setUnPayInterest(unPayInteres);
             * istBean.setUnPayInterest(ist.getUnPayInterest().add(unPayInterest).setScale(2,BigDecimal.ROUND_HALF_UP));
             * }
             */
        } else {
            if (flag.length != 0 && "1".equals(flag[0])) {// 利率调整或者浮动生效，非计息日
                /** 将利率调整的截断的归还利息算到下段上 */
                /*
                 * payInterest = ist.getInterest().setScale(2,BigDecimal.ROUND_HALF_UP);
                 * ist.setPayInterest(BigDecimal.ZERO); BigDecimal unPayInteres =
                 * ist.getTotalInterest().subtract(ist.getPayInterest()).setScale(2,BigDecimal.ROUND_HALF_UP);
                 * ist.setUnPayInterest(unPayInteres);
                 * istBean.setUnPayInterest(ist.getUnPayInterest().add(unPayInterest).setScale(2,BigDecimal.
                 * ROUND_HALF_UP));
                 */
                /** 正常显示本次截断利息 */
                payInterest = ist.getTotalInterest().add(unPayInterest).setScale(2, BigDecimal.ROUND_HALF_UP);
                ist.setPayInterest(payInterest);
                ist.setUnPayInterest(BigDecimal.ZERO);
                istBean.setUnPayInterest(BigDecimal.ZERO);
            } else {
                payInterest = ist.getTotalInterest().add(unPayInterest).setScale(2, BigDecimal.ROUND_HALF_UP);
                ist.setPayInterest(payInterest);
                ist.setUnPayInterest(BigDecimal.ZERO);
                istBean.setUnPayInterest(BigDecimal.ZERO);
            }
        }
        BigDecimal interestFax = ist.getPayInterest()
                .divide(new BigDecimal(100).subtract(ist.getRateFax()), 10, BigDecimal.ROUND_HALF_UP)
                .multiply(ist.getRateFax()).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (InterestConstant.INTEREST_SIGN_NO.equals(istBean.getInterestSign())) {
            interestFax = ist.getTotalInterest()
                    .divide(new BigDecimal(100).subtract(ist.getRateFax()), 10, BigDecimal.ROUND_HALF_UP)
                    .multiply(ist.getRateFax()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        ist.setInterestFax(interestFax);
        BigDecimal realInterest = ist.getPayInterest().add(ist.getInterestFax()).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (InterestConstant.INTEREST_SIGN_NO.equals(istBean.getInterestSign())) {
            realInterest = ist.getTotalInterest().add(ist.getInterestFax()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        ist.setRealInterest(realInterest);
        ist.setStatus(0);// 未还
        ist.setInterestSign(istBean.getInterestSign());
        istBean.setLoanMoney(ist.getLeftMoney());
        istBean.setInterestStartDate(DateUtils.addDay(ist.getBlnInstDate(), 1));// 下次起息日
        return ist;
    }
    /**
     * <br>
     * <b>功能：</b>获取利息调整生效日,确认是否分段<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-17 <br>
     * 
     * @param send
     * @param interestDay
     * @return
     */
    // FIXME 待确定是否为发放日
    protected Date getInstEffectDate(Date sendDate, Integer interestDay) {
        String floatWay = istBean.getFloatWay();// 浮动方式
        String floatWayNext = istBean.getFloatWayNext();// 具体浮动方式
        boolean isStanderd = false;
        String floatStadnderd = istBean.getFloatWayStanderd();
        if ("02".equals(floatStadnderd) || "03".equals(floatStadnderd) || "05".equals(floatStadnderd)) {
            isStanderd = true;
        }
        return getFloatEffectDate(sendDate, interestDay, floatWay, floatWayNext, isStanderd);
    }
    protected Date getFloatEffectDate(Date sendDate, int interestDay, String floatWay, String floatWayNext,
            boolean isStanderd) {
        if ("01".equals(floatWay)) {// 固定利率
        } else if ("02".equals(floatWay)) {// 即时浮动
            return sendDate;
        } /*
           * else if ("17".equals(floatWay)) {//按周浮动 return DateUtil.addDay(sendDate, 7); } else if
           * ("18".equals(floatWay)) {//按两周浮动 return DateUtil.addDay(sendDate, 14); }
           */ else if ("03".equals(floatWay)) {// 按月浮动
            return getInstFloatDate(sendDate, floatWayNext, interestDay, false, 1);
        } /*
           * else if ("19".equals(floatWay)) {//按两月浮动 return getInstFloatDate(sendDate, floatWayNext, interestDay,
           * false, 2); }
           */ else if ("04".equals(floatWay)) {// 按标准季浮动
            return getInstFloatDate(sendDate, floatWayNext, interestDay, isStanderd, 3);
        } /*
           * else if ("14".equals(floatWay)) {//按自然季浮动 return getInstFloatDate(sendDate, floatWayNext, interestDay,
           * false, 3); }
           */ else if ("05".equals(floatWay)) {// 按标准半年浮动
            return getInstFloatDate(sendDate, floatWayNext, interestDay, isStanderd, 6);
        } /*
           * else if ("15".equals(floatWay)) {//按自然半年浮动 return getInstFloatDate(sendDate, floatWayNext, interestDay,
           * false, 6); }
           */ else if ("06".equals(floatWay)) {// 按标准年浮动
            return getInstFloatDate(sendDate, floatWayNext, interestDay, isStanderd, 12);
        } /*
           * else if ("16".equals(floatWay)) {//按自然年浮动 return getInstFloatDate(sendDate, floatWayNext, interestDay,
           * false, 12); }
           */
        return sendDate;
    }
    /**
     * <br>
     * <b>功能：</b>根据浮动规则获取利率浮动生效日期<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-17 <br>
     * 
     * @param sendDate
     *            发放日
     * @param floatWayNext
     *            浮动日规则
     * @param isStanderd
     *            是否按标准季年浮动
     * @return
     */
    protected Date getInstFloatDate(Date sendDate, String floatWayNext, Integer interestDay, boolean isStanderd,
            Integer count) {
        int sendDay = getDayInMonth(send.getSendDate());// 发放日的天
        Calendar calendar = getFirstDayCalender(sendDate);
        if (isStanderd) {// 标准
            if (12 == count) {
                int month = calendar.get(Calendar.MONTH) + 1;
                do {
                    calendar.add(Calendar.MONTH, 1);// 月份+1
                    month = calendar.get(Calendar.MONTH) + 1;
                } while (month % count != 1);
            } else if (6 == count) {
                int month = calendar.get(Calendar.MONTH) + 1;
                do {
                    calendar.add(Calendar.MONTH, 1);// 月份+1
                    month = calendar.get(Calendar.MONTH) + 1;
                } while (month % count != 1);
            } else {
                int month = calendar.get(Calendar.MONTH) + 1;
                do {
                    calendar.add(Calendar.MONTH, 1);// 月份+1
                    month = calendar.get(Calendar.MONTH) + 1;
                } while (month % count != 1);
            }
        } else {// 自然
            calendar.add(Calendar.MONTH, count);// 月份+1
        }
        if ("1".equals(floatWayNext)) {// 第一天
        } else if ("2".equals(floatWayNext)) {// 贷款日
            setInstDateDay(calendar, sendDay);
        } else if ("3".equals(floatWayNext)) {// 计息日次日
            setInstDateDay(calendar, interestDay + 1);
        } else if ("4".equals(floatWayNext)) {// 计息日
            setInstDateDay(calendar, interestDay);
        }
        if ((sendDate.getTime() <= send.getRateEntriesEffectDate().getTime()
                && send.getRateEntriesEffectDate().getTime() < istBean.getBlnInstDate().getTime())
                || (null == interestDay || 0 == interestDay)) {// 开始日期 <=利率生效日期<=计息日期
            return send.getRateEntriesEffectDate();
        }
        return calendar.getTime();
    }
    /**
     * <br>
     * <b>功能：</b>计算浮动利率<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-17 <br>
     * 
     * @param plrate
     * @param floatType
     * @param floatPoint
     * @return
     */
    protected BigDecimal calcFloatRate(BigDecimal plrate, String floatType, BigDecimal floatPoint) {
        if (floatPoint == null) {
            floatPoint = BigDecimal.ZERO;
        }
        if ("02".equals(floatType)) {// 比例 利率plrate * ( 1 + ( 比例floatPoint / 100 ))
            return plrate
                    .multiply(
                            new BigDecimal(1).add(floatPoint.divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP)))
                    .setScale(6, BigDecimal.ROUND_HALF_UP);
        } else {// 点数浮动 利率plrate + (点数floatPoint / 100)
            return plrate.add(floatPoint.divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP)).setScale(6,
                    BigDecimal.ROUND_HALF_UP);
        }
    }
    protected Date maxBlnInstDate;// 最大的止息日期
    protected Date maxPredrawDate;// 最大的预提日期
    protected BigDecimal unPayInterest;// 未结利息
    public void adjustProvideInterest() {
        /**
         * 调整还款计划、计息方式、基准利率、浮动方式后重新生成生效日期之后的利息数据
         */
        Integer interestDay = send.getExpiryDate();// 结息日
        Integer predrawDay = send.getAccruedDailyDate();// 预提日
        /**
         * 计息数据：还款计划日期+结息日期 循环还款计划,生成结息利息数据,没有还款计划则只能递增结息日期
         */
        Date sendDate = send.getSendDate();// 发放日期
        BigDecimal loanMoney = send.getMoney();// 本金
        // 计息的起息日期以结息的结束日期次日开始计算
        istBean = new InterestVO(DateUtils.addDay(maxBlnInstDate, 1), loanMoney, BigDecimal.ZERO);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sendDate);
        istBean.setRepayCalendar(calendar);
        istBean.setIsStanderd("02");// 默认标准//标准,自然
        istBean.setFloatWayStanderd("02");// 浮动标准,自然//send.getFloatTypeFiscalQuarter()
        istBean.setFloatWay(send.getFloatWay());
        istBean.setFloatWayNext(send.getFloatWayNext());
        istBean.setSendDate(sendDate);
        istBean.setEffectDate(sendDate);
        istBean.setLastReapyDate(sendDate);
        istBean.setInterestWay(send.getInterestWay());
        istBean.setInterestRate(send.getBenchmarkRate());// 基准利率
        istBean.setFloatFixRate(send.getPlRate());
        istBean.setInterestDay(interestDay);// 设置结息日
        istBean.setInterestSign(InterestConstant.INTEREST_SIGN_YES);// 测算标识
        istBean.setUnPayInterest(getUnPayInterest());
        createCmsProvideInterests();// 利息测算数据
        istBean = new InterestVO(DateUtils.addDay(maxPredrawDate, 1), loanMoney, BigDecimal.ZERO);
        calendar.setTime(sendDate);
        istBean.setRepayCalendar(calendar);
        istBean.setIsStanderd("02");// 默认标准//标准,自然
        istBean.setFloatWayStanderd("02");// 浮动标准,自然 send.getFloatTypeFiscalQuarter()
        istBean.setFloatWay(send.getFloatWay());
        istBean.setFloatWayNext(send.getFloatWayNext());
        istBean.setSendDate(sendDate);
        istBean.setEffectDate(sendDate);
        istBean.setLastReapyDate(sendDate);
        istBean.setInterestWay(send.getInterestWay());
        istBean.setInterestRate(send.getBenchmarkRate());// 基准利率
        istBean.setFloatFixRate(send.getPlRate());
        istBean.setInterestDay(predrawDay);// 设置预提日
        istBean.setPredrawDay(interestDay);// 预提时候的计息日
        istBean.setInterestSign(InterestConstant.INTEREST_SIGN_NO);// 预提标识
        createCmsProvideInterests();// 利息预提数据
        Date interestTime = DateUtils.getDate();
        List<CmsProvideInterest> list = new ArrayList<CmsProvideInterest>();
        for (CmsProvideInterest interest : istList) {
            if (null != interest) {
                list.add(interest);
                interest.setInterestTime(interestTime);
            }
        }
        istList = list;
    }
    /**
     * <br>
     * <b>功能：</b>生成测算或者预提利息数据<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-22 <br>
     */
    protected void createCmsProvideInterests() {
        if (null != schedules && !schedules.isEmpty()) {
            for (Iterator<RepaymentSchedule> iterator = schedules.iterator(); iterator.hasNext();) {
                RepaymentSchedule schedule = iterator.next();
                String isReapy = schedule.getIsRepay();// 是否已还
                if (EmptyUtils.isNotEmpty(isReapy) && "1".equals(isReapy)) {
                    BigDecimal leftMoney = istBean.getLoanMoney().subtract(schedule.getRepaymentMoney()).setScale(2,
                            BigDecimal.ROUND_HALF_UP);
                    istBean.setLoanMoney(leftMoney);
                    istBean.setInterestStartDate(schedule.getPrediceRepayDate());
                    this.setMaxBlnInstDate(schedule.getPrediceRepayDate());// 预计还款日作为 最大的止息日期
                    this.setMaxPredrawDate(schedule.getPrediceRepayDate());// 预计还款日作为 最大的预提日期
                    continue;
                }
                /*
                 * if (InterestSign.INTEREST_SIGN_YES.get().equals(istBean.getInterestSign())) { }
                 */
                istBean.setRepaymentScheduleId(schedule.getId());// 还款计划ID
                istBean.setLastRepayment(!iterator.hasNext());// 是否最后一次还款
                istBean.setRepayMoney(schedule.getRepaymentMoney());// 还款金额
                istBean.setBlnInstDate(schedule.getPrediceRepayDate());// 预计还款日作为本次的计息止息日
                createInterestWayBeans();
            }
        } else {
            istBean.setBlnInstDate(send.getRepayDate());// 止息日
            istBean.setRepayMoney(send.getMoney());
            istBean.setLastRepayment(true);
            createInterestWayBeans();// FIXME
        }
    }
    /**
     * <br>
     * <b>功能：</b>返回计息明细数据<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-20 <br>
     * 
     * @return
     */
    public List<CmsProvideInterest> getIstList() {
        return istList;
    }
    public List<RateAdjustBean> getRateAdjusts() {
        return rateAdjusts;
    }
    public void setRateAdjusts(List<RateAdjustBean> rateAdjusts) {
        this.rateAdjusts = rateAdjusts;
    }
}
