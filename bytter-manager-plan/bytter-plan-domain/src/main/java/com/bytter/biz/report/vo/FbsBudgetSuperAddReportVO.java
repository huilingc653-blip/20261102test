package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
public class FbsBudgetSuperAddReportVO implements Serializable {
    /**计划id*/
    private String budgetId;
    /**单位id*/
    @BtCompositeSearch(fieldName = "f.corpId")
    private String corpId;
    /**单位名称*/
    @BtCompositeSearch(fieldName = "f.corpName")
    private String corpName;
    /**计划开始时间*/
    @BtCompositeSearch(fieldName = "f.beginDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**计划结束时间*/
    @BtCompositeSearch(fieldName = "f.endDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 状态
     * sys_dict_group.type_group_code = fbsBudget.status
     * sys_dict.type_code = status字段的值
     *
     * TODO 数据字典国际化问题，待解决
     * */
    @Dict(dictGroup = "fbsBudget.status", inject = "statusStr")
    @BtCompositeSearch(fieldName = "f.status")
    private Integer status;

    @BtCompositeSearch(fieldName = "f.status")
    private String statusStr;
    /**
     * 周期类型
     * TODO TODO 数据字典国际化问题，待解决
     * */

    @Dict(dictGroup = "fbs.timeType", inject = "timeTypeName")
    @BtCompositeSearch(fieldName = "f.timeType")
    private String timeType;

    @BtCompositeSearch(fieldName = "f.timeType")
    private String timeTypeName;
    /**币别id*/
    private String curId;


    @BtCompositeSearch(fieldName = "f.curName")
    private String curName;

    @BtCompositeSearch(fieldName = "f.addCount")
    private Integer addCount;

    public String getBudgetId() {
        return budgetId;
    }
    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getTimeType() {
        return timeType;
    }
    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public Integer getAddCount() {
        return addCount;
    }
    public void setAddCount(Integer addCount) {
        this.addCount = addCount;
    }
    public String getStatusStr() {
        return statusStr;
    }
    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
    public String getTimeTypeName() {
        return timeTypeName;
    }
    public void setTimeTypeName(String timeTypeName) {
        this.timeTypeName = timeTypeName;
    }
}
