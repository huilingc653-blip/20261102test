package com.bytter.finance.domain.provide.vo;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
@SuppressWarnings({ "serial" })
public class CmsInterestWayVO implements Serializable {
    /** 发放id */
    private String id;
    /** 发放单号 */
    private String sendCode;
    @Dict(dictGroup = "Admin.PredrawSign", inject = "interestWayName")
    /** 计息方式 */
    private String interestWay;
    /** 计息方式名称 */
    private String interestWayName;
    /** 计息日 */
    private Integer expiryDay;
    /** 预提日 */
    private Integer withholdingDay;
    /** 执行日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date activeDate;
    /** 当前日期 */
    private String toDayDate;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getToDayDate() {
        return toDayDate;
    }
    public void setToDayDate(String toDayDate) {
        this.toDayDate = toDayDate;
    }
    public java.util.Date getLastUpdateTime() {
        return lastUpdateTime;
    }
    public void setLastUpdateTime(java.util.Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    public String getInterestWayName() {
        return interestWayName;
    }
    public void setInterestWayName(String interestWayName) {
        this.interestWayName = interestWayName;
    }
    /** 上次更新日期 */
    private java.util.Date lastUpdateTime;
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public java.util.Date getActiveDate() {
        return activeDate;
    }
    public void setActiveDate(java.util.Date activeDate) {
        this.activeDate = activeDate;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public Integer getExpiryDay() {
        return expiryDay;
    }
    public void setExpiryDay(Integer expiryDay) {
        this.expiryDay = expiryDay;
    }
    public Integer getWithholdingDay() {
        return withholdingDay;
    }
    public void setWithholdingDay(Integer withholdingDay) {
        this.withholdingDay = withholdingDay;
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
