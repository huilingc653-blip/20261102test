package com.bytter.report.domain.stock.vo;
import java.io.Serializable;
import java.util.Date;
public class FbsBudgetProcessRateQueryVO implements Serializable {
    private String corpId;
    /**计划开始时间*/
    private Date beginDate;
    /**计划结束时间*/
    private Date endDate;
    /**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
    private String timeType;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getTimeType() {
        return timeType;
    }
    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }
}
