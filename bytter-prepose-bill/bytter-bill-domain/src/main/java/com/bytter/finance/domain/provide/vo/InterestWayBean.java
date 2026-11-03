package com.bytter.finance.domain.provide.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
public class InterestWayBean implements java.io.Serializable {
    /**
     * 发放单号
     */
    private String billCode;
    /**
     * 新计息方式
     */
    private String interestWay;
    /**
     * 新计息日
     */
    private Integer interestDay;
    /**
     * 预提日
     */
    private Integer predrawDay;
    private String isStanderd;// 是否标准季/半年/年
    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectDate;
    /**
     * 原计息方式
     */
    private String oriInterestWay;
    /**
     * 原计息日
     */
    private Integer oriInterestDay;
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
     * 利息设置第一列
     */
    private String interestFrequencyType;
    /**
     * 利息设置第二列
     */
    private String interestFrequencyNext;
    /**
     * 利息设置第三列
     */
    private String interestFrequency;
    /**
     * 利息设置第四列
     */
    private String interestFrequencyNum;
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public Integer getInterestDay() {
        if (interestDay == null) {
            interestDay = new Integer(0);
        }
        return interestDay;
    }
    public void setInterestDay(Integer interestDay) {
        this.interestDay = interestDay;
    }
    public Date getEffectDate() {
        return effectDate;
    }
    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }
    public String getOriInterestWay() {
        return oriInterestWay;
    }
    public void setOriInterestWay(String oriInterestWay) {
        this.oriInterestWay = oriInterestWay;
    }
    public Integer getOriInterestDay() {
        if (oriInterestDay == null) {
            oriInterestDay = new Integer(0);
        }
        return oriInterestDay;
    }
    public void setOriInterestDay(Integer oriInterestDay) {
        this.oriInterestDay = oriInterestDay;
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
    public String getIsStanderd() {
        return isStanderd;
    }
    public void setIsStanderd(String isStanderd) {
        this.isStanderd = isStanderd;
    }
    public Integer getPredrawDay() {
        if (predrawDay == null) {
            predrawDay = new Integer(0);
        }
        return predrawDay;
    }
    public void setPredrawDay(Integer predrawDay) {
        this.predrawDay = predrawDay;
    }
    public String getInterestFrequencyType() {
        return interestFrequencyType;
    }
    public void setInterestFrequencyType(String interestFrequencyType) {
        this.interestFrequencyType = interestFrequencyType;
    }
    public String getInterestFrequencyNext() {
        return interestFrequencyNext;
    }
    public void setInterestFrequencyNext(String interestFrequencyNext) {
        this.interestFrequencyNext = interestFrequencyNext;
    }
    public String getInterestFrequency() {
        return interestFrequency;
    }
    public void setInterestFrequency(String interestFrequency) {
        this.interestFrequency = interestFrequency;
    }
    public String getInterestFrequencyNum() {
        return interestFrequencyNum;
    }
    public void setInterestFrequencyNum(String interestFrequencyNum) {
        this.interestFrequencyNum = interestFrequencyNum;
    }
}
