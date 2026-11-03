package com.bytter.finance.domain.provide.service.process;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>外币利率调整<br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2015-8-19 <br>
 * <b>版权所有：<b>版权所有(C) 2010，拜特科技<br>
 */
public class CmsProvideForCurInterestHandler extends CmsProvideInterestHandler {
    public CmsProvideForCurInterestHandler(ProvideLoanSend send, CmsProvideLoanInfo info, InterestVO istBean) {
        super(send, info, istBean);
    }
    public CmsProvideForCurInterestHandler(ProvideLoanSend send, CmsProvideLoanInfo info,
            List<CmsProvideInterest> istList, List<RepaymentSchedule> schedules, List<InterestWayBean> istWayBeans,
            List<IstFloatBean> floatBeans, List<InstRateBean> rateBeans, List<RateAdjustBean> rateAdjusts,
            boolean isForCur) {
        super(send, info, istList, schedules, istWayBeans, floatBeans, rateBeans, rateAdjusts, isForCur);
    }
    public void createInterestDtl(boolean dateSign) {
        IstFloatBean oriBean = new IstFloatBean();
        oriBean.setStartDate(istBean.getInterestStartDate());
        oriBean.setEndDate(istBean.getBlnInstDate());
        oriBean.setFloatWay(istBean.getFloatWay());
        oriBean.setFloatWayNext(istBean.getFloatWayNext());
        oriBean.setFloatWayStanderd(istBean.getFloatWayStanderd());
        oriBean.setFixRate(istBean.getFloatFixRate());
        boolean isStanderd = false;
        String floatStadnderd = oriBean.getFloatWayStanderd();
        if ("02".equals(floatStadnderd) || "03".equals(floatStadnderd) || "05".equals(floatStadnderd)) {
            isStanderd = true;
        }
        Date oriEffectDate = null;
        if (InterestConstant.INTEREST_SIGN_NO.equals(istBean.getInterestSign())) {
            oriEffectDate = getFloatEffectDate(istBean.getEffectDate(), istBean.getPredrawDay(), oriBean.getFloatWay(),
                    oriBean.getFloatWayNext(), isStanderd);// 预提的时候用PredrawDay存储计息日
        } else {
            oriEffectDate = getFloatEffectDate(istBean.getEffectDate(), istBean.getInterestDay(), oriBean.getFloatWay(),
                    oriBean.getFloatWayNext(), isStanderd);// 浮动启用日期
        }
        oriBean.setEffectDate(oriEffectDate);
        List<IstFloatBean> floatBeans = getIstZoneByFloat(oriBean);
        /**
         * 获取浮动区间
         */
        boolean interestSign = dateSign;
        for (Iterator<IstFloatBean> iterator = floatBeans.iterator(); iterator.hasNext();) {
            IstFloatBean floatBean = iterator.next();
            Date effectDate = floatBean.getEffectDate();
            InterestVO interestBean = new InterestVO();
            try {
                BeanUtils.copyProperties(interestBean, istBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            interestBean.setInterestStartDate(floatBean.getStartDate());
            interestBean.setFloatWay(floatBean.getFloatWay());
            interestBean.setFloatWayNext(floatBean.getFloatWayNext());
            interestBean.setFloatWayStanderd(floatBean.getFloatWayStanderd());
            interestBean.setFloatFixRate(floatBean.getFixRate());
            interestBean.setSendDate(effectDate);// 生效日作为发放日
            if (!iterator.hasNext()) {
                interestBean.setLastFloat(true);// 最后一次浮动
                if (interestSign) {
                    dateSign = interestSign;
                }
            } else {
                if (floatBeans.size() > 1) {
                    interestBean.setFloatTimes(true);// 多次浮动
                    if (interestSign) {
                        dateSign = false;
                    }
                }
            }
            istBean = interestBean;
            Date blnInstDate = floatBean.getEndDate();
            if (istBean.getInterestStartDate().getTime() < effectDate.getTime()
                    && effectDate.getTime() < blnInstDate.getTime()) {
                // 浮动前分段,生效前原利率,生效后用生效日的利率
                istBean.setBlnInstDate(effectDate);// 生效日为止息日
                // istBean.setInterestRate(setRecentInstRate(effectDate));
                createFloatInterestDtl(false);
            }
            // istBean.setEffectDate(effectDate);
            istBean.setBlnInstDate(floatBean.getEndDate());
            /**
             * 删除下次浮动生效日期之后的利息数据 FIXME 待处理
             */
            istBean.setInterestRate(setRecentInstRate(floatBean.getEffectDate()));
            createFloatInterestDtl(dateSign);
        }
    }
    /**
     * <br>
     * <b>功能：</b>设置调整生效日期当天的Libor值,没有则取借款日当天的Libor值<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-9-9 <br>
     */
    private BigDecimal setRecentInstRate(Date effectDate) {
        /**
         * 取生效日期的libor值,判断生效日是否在计息区间内
         */
        InstRateBean rateBean = forCurMap.get(DateUtils.formatDate(effectDate, "yyyy-MM-dd"));
        if (null != rateBean) {
            return rateBean.getNewRate();
        }
        return send.getBenchmarkRate();
    }
    /**
     * <br>
     * <b>功能：</b>按照浮动方式划分计息区间<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2015-8-20 <br>
     * 
     * @param oriBean
     * @return
     */
    protected List<IstFloatBean> getIstZoneByFloat(IstFloatBean oriBean) {
        List<IstFloatBean> dates = new ArrayList<IstFloatBean>();
        Date startDate = istBean.getInterestStartDate();// 起息日期
        Date endDate = istBean.getBlnInstDate();// 止息日期
        Date tempDate = startDate;
        for (Iterator<IstFloatBean> iterator = floatBeans.iterator(); iterator.hasNext();) {
            IstFloatBean floatBean = iterator.next();
            Date floatEffectDate = floatBean.getEffectDate();// 生效日期
            boolean isStanderd = false;
            String floatStadnderd = floatBean.getFloatWayStanderd();
            if ("02".equals(floatStadnderd) || "03".equals(floatStadnderd) || "05".equals(floatStadnderd)) {
                isStanderd = true;
            }
            Date effectDate = null;
            if (InterestConstant.INTEREST_SIGN_NO.equals(istBean.getInterestSign())) {
                effectDate = getFloatEffectDate(floatEffectDate, istBean.getPredrawDay(), floatBean.getFloatWay(),
                        floatBean.getFloatWayNext(), isStanderd);// 预提的时候用PredrawDay存储计息日
            } else {
                effectDate = getFloatEffectDate(floatEffectDate, istBean.getInterestDay(), floatBean.getFloatWay(),
                        floatBean.getFloatWayNext(), isStanderd);// 浮动启用日期
            }
            if (effectDate.getTime() <= tempDate.getTime()) {// 生效日期<起息日
                if (iterator.hasNext()) {
                    continue;
                } else {
                    IstFloatBean bean = new IstFloatBean();
                    bean.setEffectDate(effectDate);
                    bean.setStartDate(tempDate);
                    bean.setEndDate(endDate);
                    bean.setFloatWay(floatBean.getFloatWay());
                    bean.setFloatWayNext(floatBean.getFloatWayNext());
                    bean.setFloatWayStanderd(floatStadnderd);
                    bean.setFixRate(floatBean.getFixRate());
                    dates.add(bean);
                    break;
                }
            } else if (effectDate.getTime() <= endDate.getTime()) {// 起息日<生效日期<止息日
                IstFloatBean bean = new IstFloatBean();
                bean.setEffectDate(oriBean.getEffectDate());// 生效日期用原先的启用日期
                bean.setStartDate(tempDate);
                bean.setEndDate(effectDate);
                bean.setFloatWay(oriBean.getFloatWay());
                bean.setFloatWayNext(oriBean.getFloatWayNext());
                bean.setFloatWayStanderd(oriBean.getFloatWayStanderd());
                bean.setFixRate(oriBean.getFixRate());
                dates.add(bean);
                tempDate = effectDate;
                oriBean.setFloatWay(floatBean.getFloatWay());
                oriBean.setFloatWayNext(floatBean.getFloatWayNext());
                oriBean.setFloatWayStanderd(floatStadnderd);
                oriBean.setFixRate(floatBean.getFixRate());
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
    protected void createFloatInterestDtl(boolean dateSign) {
        Date repayDate = istBean.getBlnInstDate();
        Date interestStartDate = istBean.getInterestStartDate();
        String floatWay = istBean.getFloatWay();// 浮动方式
        BigDecimal plRate = istBean.getInterestRate();// 原利率
        if ("01".equals(floatWay)) {// 固定利率
            plRate = istBean.getFloatFixRate();// 浮动方式为固定利率时的利率
            istBean.setInterestRate(plRate);
            if (istBean.isLastRepayment()) {// 浮动调整最后一次
                istList.add(createProvideInterest(dateSign, plRate));
            } else {
                istList.add(createProvideInterest(dateSign, plRate));
            }
        } else {
            BigDecimal floatPoint = send.getPointsProportionText();// 比例、点数
            String floatType = send.getPointsProportionSelect();// 比例、点数浮动
            BigDecimal effectRate = calcFloatRate(plRate, floatType, floatPoint);
            istList.add(createProvideInterest(dateSign, effectRate));
        }
    }
}
