package com.bytter.finance.domain.provide.vo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
public class RateFloatRuleVO {
    /**
     * 基准日期
     */
    private Date baseDate;
    /**
     * 浮动方式
     */
    private String floatWay;
    /**
     * 浮动类型
     */
    private String floatWayNext;
    /**
     * 预提日或结息日
     */
    private int accruedDailyOrSettlementDay;
    /**
     * 浮动周期
     */
    private int floatCycle;
    /**
     * 是否是标准，不是标准就是自然
     */
    private boolean isStanderd;
    /**
     * 规则使用的利率
     */
    private List<InstRateBean> instRateBeanList;
    /**
     * 浮动规则开始日期
     */
    private Date startDate;
    /**
     * 浮动规则结束日期
     */
    private Date endDate;
    /**
     * 固定利率
     */
    private BigDecimal fixedRate;
    private BigDecimal pointProportion;
    /**
     * 放款日
     */
    private Date sendDate;
    public Date getBaseDate() {
        return baseDate;
    }
    public void setBaseDate(Date baseDate) {
        this.baseDate = baseDate;
    }
    public BigDecimal getPointProportion() {
        return pointProportion;
    }
    public void setPointProportion(BigDecimal pointProportion) {
        this.pointProportion = pointProportion;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public String getFloatWay() {
        return floatWay;
    }
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    public int getAccruedDailyOrSettlementDay() {
        return accruedDailyOrSettlementDay;
    }
    public void setAccruedDailyOrSettlementDay(int accruedDailyOrSettlementDay) {
        this.accruedDailyOrSettlementDay = accruedDailyOrSettlementDay;
    }
    public int getFloatCycle() {
        return floatCycle;
    }
    public void setFloatCycle(int floatCycle) {
        this.floatCycle = floatCycle;
    }
    public List<InstRateBean> getInstRateBeanList() {
        return instRateBeanList;
    }
    public void setInstRateBeanList(List<InstRateBean> instRateBeanList) {
        this.instRateBeanList = instRateBeanList;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public BigDecimal getFixedRate() {
        return fixedRate;
    }
    public void setFixedRate(BigDecimal fixedRate) {
        this.fixedRate = fixedRate;
    }
    public boolean isStanderd() {
        return isStanderd;
    }
    public void setStanderd(boolean standerd) {
        isStanderd = standerd;
    }
}
