package com.bytter.finance.domain.provide.vo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
/**
 * 利率浮动点
 */
public class RateFloatPointVO {
    /**
     * 浮动点的日期
     */
    private Date floatPointDate;
    /**
     * 利率
     */
    private BigDecimal rate;
    /**
     * 点数或比例
     */
    private BigDecimal pointProportion;
    public Date getFloatPointDate() {
        return floatPointDate;
    }
    public void setFloatPointDate(Date floatPointDate) {
        this.floatPointDate = floatPointDate;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public BigDecimal getPointProportion() {
        return pointProportion;
    }
    public void setPointProportion(BigDecimal pointProportion) {
        this.pointProportion = pointProportion;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RateFloatPointVO that = (RateFloatPointVO) o;
        return Objects.equals(floatPointDate, that.floatPointDate) && Objects.equals(rate, that.rate)
                && Objects.equals(pointProportion, that.pointProportion);
    }
    @Override
    public int hashCode() {
        return Objects.hash(floatPointDate, rate, pointProportion);
    }
}
