package com.bytter.finance.domain.provide.service;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.inl.entity.CmsInlSend;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.vo.InstRateBean;
import com.bytter.finance.domain.provide.vo.RateFloatPointVO;
import com.bytter.finance.domain.provide.vo.RateFloatRuleVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class RateFloatRuleService {
    private final RateFloatRuleHandlerFactory rateFloatRuleHandlerFactory = new RateFloatRuleHandlerFactory();
    // 自然周期的浮动方式，如半年、年的贷款日、结息日
    List<String> notStanderdFloatWay = Lists.newArrayList(
            CmsConstant.FLOAT_WAY_05 + CommonConstant.COLON_SPLIT + CmsConstant.FLOAT_WAY_START_2,
            CmsConstant.FLOAT_WAY_05 + CommonConstant.COLON_SPLIT + CmsConstant.FLOAT_WAY_START_3,
            CmsConstant.FLOAT_WAY_06 + CommonConstant.COLON_SPLIT + CmsConstant.FLOAT_WAY_START_2,
            CmsConstant.FLOAT_WAY_06 + CommonConstant.COLON_SPLIT + CmsConstant.FLOAT_WAY_START_3,
            CmsConstant.FLOAT_WAY_04 + CommonConstant.COLON_SPLIT + CmsConstant.FLOAT_WAY_START_2,
            CmsConstant.FLOAT_WAY_04 + CommonConstant.COLON_SPLIT + CmsConstant.FLOAT_WAY_START_3);
    boolean isStanderd(String floatWay, String floatWayNext) {
        return !notStanderdFloatWay.contains(floatWay + CommonConstant.COLON_SPLIT + floatWayNext);
    }
    public RateFloatRuleVO buildRateFloatRuleVO(ProvideLoanSend send, Date baseDate, String floatWay,
            String floatWayNext, String interestSign) {
        RateFloatRuleVO floatRule = new RateFloatRuleVO();
        floatRule.setSendDate(send.getSendDate());
        floatRule.setBaseDate(baseDate);
        floatRule.setFloatWay(floatWay);
        floatRule.setFloatWayNext(floatWayNext);
        Integer accruedDailyOrSettlementDay = 0;
        if (InterestConstant.INTEREST_SIGN_YES.equals(interestSign)) {
            accruedDailyOrSettlementDay = send.getExpiryDate();// 结息日
        } else {
            accruedDailyOrSettlementDay = send.getAccruedDailyDate();// 预提日
        }
        if (EmptyUtils.isEmpty(accruedDailyOrSettlementDay) || accruedDailyOrSettlementDay.intValue() == 0) {
            accruedDailyOrSettlementDay = 21;// 默认21号
        }
        floatRule.setAccruedDailyOrSettlementDay(accruedDailyOrSettlementDay);
        int floatCycle = 1;
        if ("03".equals(floatWay)) {// 按月浮动
            floatCycle = 1;
        } else if ("04".equals(floatWay)) {// 按季浮动
            floatCycle = 3;
        } else if ("05".equals(floatWay)) {// 按半年浮动
            floatCycle = 6;
        } else if ("06".equals(floatWay)) {// 按年浮动
            floatCycle = 12;
        }
        floatRule.setFloatCycle(floatCycle);
        floatRule.setStanderd(isStanderd(floatWay, floatWayNext));
        return floatRule;
    }
    //内部拆解获取
    public RateFloatRuleVO inlbuildRateFloatRuleVO(CmsInlSend send, Date baseDate, String floatWay, String floatWayNext, String interestSign){
        RateFloatRuleVO floatRule= new RateFloatRuleVO();
        floatRule.setSendDate(send.getSendDate());
        floatRule.setBaseDate(baseDate);
        floatRule.setFloatWay(floatWay);
        floatRule.setFloatWayNext(floatWayNext);
        Integer accruedDailyOrSettlementDay = 0;
        if(InterestConstant.INTEREST_SIGN_YES.equals(interestSign)){
            accruedDailyOrSettlementDay = send.getExpiryDate();//结息日
        }else{
            accruedDailyOrSettlementDay = send.getAccruedDailyDate();//预提日
        }
        if(EmptyUtils.isEmpty(accruedDailyOrSettlementDay) || accruedDailyOrSettlementDay.intValue() == 0){
            accruedDailyOrSettlementDay = 21;//默认21号
        }
        floatRule.setAccruedDailyOrSettlementDay(accruedDailyOrSettlementDay);
        int floatCycle=1;
        if("03".equals(floatWay)){//按月浮动
            floatCycle=1;
        }else if("04".equals(floatWay)){//按季浮动
            floatCycle=3;
        }else if("05".equals(floatWay)){//按半年浮动
            floatCycle=6;
        }else if("06".equals(floatWay)){//按年浮动
            floatCycle=12;
        }
        floatRule.setFloatCycle(floatCycle);
        floatRule.setStanderd(isStanderd(floatWay,floatWayNext));
        return floatRule;
    }
    public List<RateFloatPointVO> getRateFloatPoints(RateFloatRuleVO rateFloatRule) {
        List<RateFloatPointVO> floatPoints = rateFloatRuleHandlerFactory.getFloatRuleHandler(rateFloatRule)
                .apply(rateFloatRule);
        if (!CmsConstant.FLOAT_WAY_01.equals(rateFloatRule.getFloatWay())) {// 固定利率IRateFloatRuleHandler
                                                                            // 会处理利率和pb点数的，只有浮动利率才需要取分割点的利率，pb点数
            // 使用放款单指定的报价利率，更新浮动点的利率
            updateFloatPointRate(floatPoints, rateFloatRule.getInstRateBeanList());
            // 使用放款单指定的pb点数，更新浮动点的pb点数
            // TODO 待确认生效日之前是没有点数或比例的
            updateFloatPointPointProportion(floatPoints, rateFloatRule.getPointProportion());
        } else {
            // TODO 固定利率 的利率和BP点数的需求待确定？ 固定利率有2个地方，他们在以前是不同的，1。借款申请录入2.浮动方式修改——使用的是用户输入一个固定的利率，这个固定利率就是PLRate
            for (RateFloatPointVO floatPoint : floatPoints) {
                floatPoint.setRate(rateFloatRule.getFixedRate());
                floatPoint.setPointProportion(BigDecimal.ZERO);
            }
        }
        return floatPoints;
    }
    private void updateFloatPointRate(List<RateFloatPointVO> floatPoints, List<InstRateBean> rates) {
        if (EmptyUtils.isEmpty(floatPoints) || EmptyUtils.isEmpty(rates)) {
            return;
        }
        int rateStartIndex = 0;
        boolean existsGreaterFloatPointDate = false;// 存在大于浮动点日期的利率报价
        for (int i = 0; i < floatPoints.size(); i++) {
            existsGreaterFloatPointDate = false;
            for (int j = rateStartIndex; j < rates.size(); j++) {
                if (DateUtils.compareDate(floatPoints.get(i).getFloatPointDate(), rates.get(j).getEffectDate()) < 0) {
                    existsGreaterFloatPointDate = true;
                    floatPoints.get(i).setRate(j > 0 ? rates.get(j - 1).getNewRate() : null);// null 表示没有小于浮动点日期的利率报价
                    // 下一个浮动点的日期 一定也大于 rates.get(j).getEffectDate() 但是不一定大于 rates.get(j+1).getEffectDate()
                    // 所以下一次从j开始而不是从j+1开始
                    rateStartIndex = j;
                    break;
                }
            }
            // 不存在大于浮动点日期的利率报价，则取最后一个报价的利率
            if (!existsGreaterFloatPointDate) {
                floatPoints.get(i).setRate(rates.get(rates.size() - 1).getNewRate());
            }
        }
    }
    private void updateFloatPointPointProportion(List<RateFloatPointVO> floatPoints, BigDecimal pointProportion) {
        if (EmptyUtils.isEmpty(floatPoints)) {
            return;
        }
        for (RateFloatPointVO floatPoint : floatPoints) {
            floatPoint.setPointProportion(pointProportion);
        }
    }
    private static class RateFloatRuleHandlerFactory {
        private Map<String, IRateFloatRuleHandler> floatType2RateFloatRuleHandler = Maps.newHashMap();
        private static final RateFloatRuleHandlerFactory factory = new RateFloatRuleHandlerFactory();
        private RateFloatRuleHandlerFactory() {
            // FLOAT_WAY + : + FLOAT_WAY_START 及时浮动 FLOAT_WAY_START 取空字符串，非及时浮动 FLOAT_WAY 取空字符串
            floatType2RateFloatRuleHandler.put(CmsConstant.FLOAT_WAY_01 + CommonConstant.COLON_SPLIT,
                    new FixedRateRuleHandler());
            floatType2RateFloatRuleHandler.put(CmsConstant.FLOAT_WAY_02 + CommonConstant.COLON_SPLIT,
                    new TimelyFloatRuleHandler());
            floatType2RateFloatRuleHandler.put(CommonConstant.COLON_SPLIT + CmsConstant.FLOAT_WAY_START_1,
                    new FirstDayFloatRuleHandler());
            floatType2RateFloatRuleHandler.put(CommonConstant.COLON_SPLIT + CmsConstant.FLOAT_WAY_START_2,
                    new LoanDayFloatRuleHandler());
            floatType2RateFloatRuleHandler.put(CommonConstant.COLON_SPLIT + CmsConstant.FLOAT_WAY_START_3,
                    new SettlementDayFloatRuleHandler());
        }
        public static RateFloatRuleHandlerFactory getRateFloatRuleHandlerFactory() {
            return factory;
        }
        public IRateFloatRuleHandler getFloatRuleHandler(RateFloatRuleVO rateFloatRule) {
            if (CmsConstant.FLOAT_WAY_01.equals(rateFloatRule.getFloatWay())
                    || CmsConstant.FLOAT_WAY_02.equals(rateFloatRule.getFloatWay())) {
                return floatType2RateFloatRuleHandler.get(rateFloatRule.getFloatWay() + CommonConstant.COLON_SPLIT);
            } else {
                return floatType2RateFloatRuleHandler.get(CommonConstant.COLON_SPLIT + rateFloatRule.getFloatWayNext());
            }
        }
    }
    private interface IRateFloatRuleHandler {
        /**
         * 使用这个浮动规则，获取该规则中[startDate,endDate)的所有分割点的日期
         * 
         * @param floatRule
         * @return
         */
        List<RateFloatPointVO> apply(RateFloatRuleVO floatRule);
    }
    private static class FixedRateRuleHandler implements IRateFloatRuleHandler {
        @Override
        public List<RateFloatPointVO> apply(RateFloatRuleVO floatRuleVO) {
            List<RateFloatPointVO> floatPoints = Lists.newArrayList();
            Date startDate = floatRuleVO.getStartDate();
            RateFloatPointVO startFloatPoint = buildRateFloatPointVO(startDate);
            floatPoints.add(startFloatPoint);
            return floatPoints;
        }
    }
    private static class TimelyFloatRuleHandler implements IRateFloatRuleHandler {
        @Override
        public List<RateFloatPointVO> apply(RateFloatRuleVO floatRuleVO) {
            List<RateFloatPointVO> floatPoints = Lists.newArrayList();
            List<InstRateBean> rateBeans = floatRuleVO.getInstRateBeanList();
            if (EmptyUtils.isEmpty(rateBeans)) {
                return floatPoints;
            }
            Date startDate = floatRuleVO.getStartDate();
            Date endDate = floatRuleVO.getEndDate();
            for (InstRateBean rateBean : rateBeans) {
                if (DateUtils.compareDate(rateBean.getEffectDate(), startDate) >= 0
                        && DateUtils.compareDate(rateBean.getEffectDate(), endDate) < 0) {
                    floatPoints.add(buildRateFloatPointVO(rateBean.getEffectDate()));
                }
            }
            return floatPoints;
        }
    }
    private static RateFloatPointVO buildRateFloatPointVO(Date floatPointDate) {
        RateFloatPointVO rateFloatPointVO = new RateFloatPointVO();
        rateFloatPointVO.setFloatPointDate(floatPointDate);
        return rateFloatPointVO;
    }
    private static abstract class AbstractCycleFloatRuleHandler implements IRateFloatRuleHandler {
        public List<RateFloatPointVO> apply(RateFloatRuleVO floatRule) {
            List<RateFloatPointVO> floatPoints = Lists.newArrayList();
            int floatCycle = floatRule.getFloatCycle();
            // 根据基准日期、浮动方式来确定第一次分割点
            // Date firstFloatDate =
            // getFirstFloatDate(floatRule.getSendDate(),floatRule.getSendDay(),floatRule.getAccruedDailyOrSettlementDay(),floatCycle,floatRule.isStanderd());
            Date firstFloatDate = getFirstFloatDate(floatRule);
            // 使用startDate来修正 第一次分割点
            Date startDate = floatRule.getStartDate();
            Date endDate = floatRule.getEndDate();
            if (DateUtils.compareDate(firstFloatDate, startDate) <= 0) {
                floatPoints.add(buildRateFloatPointVO(startDate));
            } else if (DateUtils.compareDate(firstFloatDate, endDate) < 0) {
                floatPoints.add(buildRateFloatPointVO(firstFloatDate));
            } else {
                return floatPoints;
            }
            // 使用浮动周期来进行第N次的分割 （N>=2）
            Date lastFloatDate = firstFloatDate;
            // lastFloatDate+floatCycle <= endDate
            while (DateUtils.compareDate((lastFloatDate = DateUtils.addMonth(lastFloatDate, floatCycle)),
                    endDate) <= 0) {
                if (DateUtils.compareDate(lastFloatDate, startDate) > 0) {
                    floatPoints.add(buildRateFloatPointVO(lastFloatDate));
                }
            }
            return floatPoints;
        }
        // abstract Date getFirstFloatDate(Date sendDate,Integer sendDay,Integer accruedDailyOrSettlementDay,Integer
        // cycle,boolean isStanderd);
        abstract Date getFirstFloatDate(RateFloatRuleVO floatRule);
    }
    private static class FirstDayFloatRuleHandler extends AbstractCycleFloatRuleHandler {
        @Override
        Date getFirstFloatDate(RateFloatRuleVO floatRule) {
            Calendar calendar = DateUtils.getFirstDayCalender(floatRule.getBaseDate());
            calendar.set(Calendar.DAY_OF_MONTH, 1);// 月初第一天
            int cycle = floatRule.getFloatCycle();
            if (floatRule.isStanderd()) {
                if (cycle == 1) {
                    calendar.add(Calendar.MONTH, 1);// 月份+1
                } else {
                    int month = calendar.get(Calendar.MONTH);
                    do {
                        calendar.add(Calendar.MONTH, 1);// 月份+1
                        month = calendar.get(Calendar.MONTH) + 1;// 还原java月份差额
                    } while (month % cycle != 1);
                }
            } else {
                calendar.add(Calendar.MONTH, cycle);
            }
            int day = 1;
            setInstDateDay(calendar, day);// 浮动生效的准确年月日
            return calendar.getTime();
        }
    }
    private static class LoanDayFloatRuleHandler extends AbstractCycleFloatRuleHandler {
        @Override
        Date getFirstFloatDate(RateFloatRuleVO floatRule) {
            Calendar calendar = DateUtils.getFirstDayCalender(floatRule.getBaseDate());
            calendar.set(Calendar.DAY_OF_MONTH, 1);// 月初第一天
            int cycle = floatRule.getFloatCycle();
            if (floatRule.isStanderd()) {
                if (cycle == 1 || cycle == 12) { // 次年贷款日 是贷款日的下一个年度的对月对日。次月贷款日 月只用加1
                    calendar.add(Calendar.MONTH, cycle);// 月份+12
                } else { // 其它周期，都是 下一个周期的首月
                    int month = calendar.get(Calendar.MONTH);
                    do {
                        calendar.add(Calendar.MONTH, 1);// 月份+1
                        month = calendar.get(Calendar.MONTH) + 1;// 还原java月份差额
                    } while (month % cycle != 1);
                }
            } else {
                calendar.add(Calendar.MONTH, cycle);
            }
            Calendar sendCalendar = Calendar.getInstance();
            sendCalendar.setTime(floatRule.getSendDate());
            int sendDay = sendCalendar.get(Calendar.DAY_OF_MONTH);// 获取贷款日
            setInstDateDay(calendar, sendDay);// 浮动生效的准确年月日
            return calendar.getTime();
        }
    }
    private static class SettlementDayFloatRuleHandler extends AbstractCycleFloatRuleHandler {
        @Override
        Date getFirstFloatDate(RateFloatRuleVO floatRule) {
            Calendar calendar = DateUtils.getFirstDayCalender(floatRule.getBaseDate());
            calendar.set(Calendar.DAY_OF_MONTH, 1);// 月初第一天
            int cycle = floatRule.getFloatCycle();
            if (floatRule.isStanderd()) {
                calendar.add(Calendar.MONTH, cycle);// 增加 count确保日期进入下个季度或者半年
                int month = calendar.get(Calendar.MONTH) + 1;
                while (month % cycle != 0) { // 所属周期的周期点
                    calendar.add(Calendar.MONTH, 1);// 月份+1
                    month = calendar.get(Calendar.MONTH) + 1;// 还原java月份差额
                }
            } else {
                calendar.add(Calendar.MONTH, cycle);
            }
            int day = floatRule.getAccruedDailyOrSettlementDay();
            // 计息日次日
            day += 1;
            setInstDateDay(calendar, day);// 浮动生效的准确年月日
            return calendar.getTime();
        }
    }
    /**
     * 获取日期sendDate的浮动生效日期
     * 
     * @param sendDate
     *            发放日期
     * @param day
     *            结息日
     * @param floatWayNext
     *            下次浮动明细
     * @param isStanderd
     *            自然季(false)或标准季(true)
     * @param count
     *            不同浮动方式对应的数字
     * @return
     */
    private Date getFloatDateV2(Date sendDate, Integer day, String floatWayNext, boolean isStanderd, Integer count) {
        Calendar calendar = DateUtils.getFirstDayCalender(sendDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 月初第一天
        if (count == 1) {// 每月计息按照自然季来计算
            isStanderd = false;
        }
        if (isStanderd) {// 标准
            if ("1".equals(floatWayNext)) {// 次《什么》第一天
                day = 1;
                int month = calendar.get(Calendar.MONTH);
                do {
                    calendar.add(Calendar.MONTH, 1);// 月份+1
                    month = calendar.get(Calendar.MONTH) + 1;// 还原java月份差额
                } while (month % count != 1);
            } else if ("2".equals(floatWayNext)) {// 次《什么》第贷款日
                Calendar sendCalendar = Calendar.getInstance();
                sendCalendar.setTime(sendDate);
                day = sendCalendar.get(Calendar.DAY_OF_MONTH);// 获取贷款日
                if (count == 12) { // 次年贷款日 是贷款日的下一个年度的对月对日
                    calendar.add(Calendar.MONTH, count);// 月份+12
                } else { // 其它周期，都是 下一个周期的首月
                    int month = calendar.get(Calendar.MONTH);
                    do {
                        calendar.add(Calendar.MONTH, 1);// 月份+1
                        month = calendar.get(Calendar.MONTH) + 1;// 还原java月份差额
                    } while (month % count != 1);
                }
            } else if ("3".equals(floatWayNext)) {// 次《什么》结息日，下一个周期的末月
                int month = calendar.get(Calendar.MONTH);
                do {
                    calendar.add(Calendar.MONTH, 1);// 月份+1
                    month = calendar.get(Calendar.MONTH) + 1;// 还原java月份差额
                } while (month % count != (count - 1));
                // 计息日次日
                day += 1;
                System.out.println("floatWayNext:" + floatWayNext + "day:" + day + "calendar:"
                        + DateUtils.format(calendar.getTime(), "yyyy-MM-dd"));
            }
        } else {// 自然
            if ("1".equals(floatWayNext)) {// 次《什么》第一天
                day = 1;
            } else if ("2".equals(floatWayNext)) {// 次《什么》第贷款日
                Calendar sendCalendar = Calendar.getInstance();
                sendCalendar.setTime(sendDate);
                day = sendCalendar.get(Calendar.DAY_OF_MONTH);// 获取贷款日
                System.out.println("floatWayNext:" + floatWayNext + "day:" + day + "calendar:"
                        + DateUtils.format(calendar.getTime(), "yyyy-MM-dd"));
            } else {
                day += 1;
            }
            calendar.add(Calendar.MONTH, count);// 月份+1
        }
        this.setInstDateDay(calendar, day);// 浮动生效的准确年月日
        return calendar.getTime();
    }
    /** 设置某月中的第day天 */
    private static void setInstDateDay(Calendar calendar, Integer day) {
        String d = DateUtils.format(calendar.getTime(), "yyyy-MM-dd");
        Integer lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 月份最后一天
        if (day < lastDay && day != 0) {// 结息日小于月末最后一天
            calendar.set(Calendar.DAY_OF_MONTH, day);// 结息日
        } else {
            // 结息日大于月末最后一天,设置最后一天为结息日
            calendar.set(Calendar.DAY_OF_MONTH, lastDay);// 月末最后一天作为结息日
        }
    }
}
