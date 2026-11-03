package com.bytter.finance.domain.provide.vo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
public class IstFloatBean implements java.io.Serializable {
    /**
     * 发放单号
     */
    private String billCode;
    /**
     * 新浮动方式
     */
    private String floatWay;
    /**
     * 新浮动日规则
     */
    private String floatWayNext;
    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectDate;
    /**
     * 原浮动方式
     */
    private String oriFloatWay;
    /**
     * 原浮动日规则
     */
    private Integer oriFloatWayNext;
    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 浮动实际启用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date floatEffectDate;
    /**
     * 浮动是否标准季半年年
     */
    String floatWayStanderd;// 浮动是否标准季/半年/年
    /**
     * 浮动点数或百分比
     */
    private BigDecimal pointsText;

    /**
     * 基准利率
     */
    private BigDecimal rate;

    private BigDecimal fixRate;// 浮动方式为固定利率时的利率
    /**
     * 新的基准利率类型
     */
    private String referenceRateType;
    /**
     * 新的贷款周期
     */
    private String repayCycle;
    /**
     * 最后一次修改的浮动方式对应的利率报价
     */
    List<InstRateBean> instRateBeansForLastTimesFloat;
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
    public Date getEffectDate() {
        return effectDate;
    }
    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }
    public String getOriFloatWay() {
        return oriFloatWay;
    }
    public void setOriFloatWay(String oriFloatWay) {
        this.oriFloatWay = oriFloatWay;
    }
    public Integer getOriFloatWayNext() {
        if (oriFloatWayNext == null) {
            oriFloatWayNext = new Integer(0);
        }
        return oriFloatWayNext;
    }
    public void setOriFloatWayNext(Integer oriFloatWayNext) {
        this.oriFloatWayNext = oriFloatWayNext;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
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
    public Date getFloatEffectDate() {
        return floatEffectDate;
    }
    public void setFloatEffectDate(Date floatEffectDate) {
        this.floatEffectDate = floatEffectDate;
    }
    public String getFloatWayStanderd() {
        return floatWayStanderd;
    }
    public void setFloatWayStanderd(String floatWayStanderd) {
        this.floatWayStanderd = floatWayStanderd;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }


    public BigDecimal getFixRate() {
        return fixRate;
    }
    public void setFixRate(BigDecimal fixRate) {
        this.fixRate = fixRate;
    }
    public BigDecimal getPointsText() {
        return pointsText;
    }
    public void setPointsText(BigDecimal pointsText) {
        this.pointsText = pointsText;
    }
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    public List<InstRateBean> getInstRateBeansForLastTimesFloat() {
        return instRateBeansForLastTimesFloat;
    }
    public void setInstRateBeansForLastTimesFloat(List<InstRateBean> instRateBeansForLastTimesFloat) {
        this.instRateBeansForLastTimesFloat = instRateBeansForLastTimesFloat;
    }
}
