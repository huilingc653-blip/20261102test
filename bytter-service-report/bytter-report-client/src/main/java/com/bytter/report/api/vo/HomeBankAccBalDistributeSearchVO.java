package com.bytter.report.api.vo;
import java.io.Serializable;
import java.util.Date;
public class HomeBankAccBalDistributeSearchVO implements Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 查询日期
     */
    private Date searchDate;
    /**
     * 金额单位
     */
    private Integer moneyType;
    /**
     * 分组标志
     */
    private String groupBy;
    /**
     * 查询周期模式：Y-按年 M-按月
     */
    private String timeType;
    /**
     * 年份
     */
    private String year;
    /**
     * 月份
     */
    private String month;
    /**
     * 查询日期-开始日期
     */
    private Date searchBeginDate;
    /**
     * 查询日期-结束日期
     */
    private Date searchEndDate;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public Date getSearchDate() {
        return searchDate;
    }
    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }
    public Integer getMoneyType() {
        return moneyType;
    }
    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }
    public String getGroupBy() {
        return groupBy;
    }
    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
    public String getTimeType() {
        return timeType;
    }
    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public Date getSearchBeginDate() {
        return searchBeginDate;
    }
    public void setSearchBeginDate(Date searchBeginDate) {
        this.searchBeginDate = searchBeginDate;
    }
    public Date getSearchEndDate() {
        return searchEndDate;
    }
    public void setSearchEndDate(Date searchEndDate) {
        this.searchEndDate = searchEndDate;
    }
}
