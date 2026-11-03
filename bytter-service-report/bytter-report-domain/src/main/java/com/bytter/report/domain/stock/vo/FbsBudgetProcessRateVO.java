package com.bytter.report.domain.stock.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Corps;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class FbsBudgetProcessRateVO implements Serializable {
    private String id;
    /**单位ID*/
    @Corps({
            @Corp(inject = "corpName"),
            @Corp(inject = "corpCode",field = "code")
    })
    private String corpId;
    private String corpCode;
    private String corpName;
    /**编制时间*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date enrolDate;
    /**计划开始时间*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date beginDate;
    /**计划结束时间*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    /**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
    private String timeType;
    private String timeTypeShow;
    /**计划上报金额*/
    private BigDecimal bMoney;
    /**计划执行金额*/
    private BigDecimal processMoney;
    /**执行率*/
    private BigDecimal processRatio;
    /***科目Id*/
    @Dict(tableName="FBS_ITEM",valueFieldName="id",showFiledName="item_name",inject="itemName")
    private String itemId;
    /***科目代码*/
    private String itemCode;
    /***科目名称*/
    private String itemName;
    /**币别*/
    @Currency(inject="curName")
    private String curId;
    /**币别名称*/
    private String curName;
    /**汇率*/
    private BigDecimal exchangeRate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public Date getEnrolDate() {
        return enrolDate;
    }
    public void setEnrolDate(Date enrolDate) {
        this.enrolDate = enrolDate;
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
    public String getTimeTypeShow() {
        return timeTypeShow;
    }
    public void setTimeTypeShow(String timeTypeShow) {
        this.timeTypeShow = timeTypeShow;
    }
    public BigDecimal getbMoney() {
        return bMoney;
    }
    public void setbMoney(BigDecimal bMoney) {
        this.bMoney = bMoney;
    }
    public BigDecimal getProcessMoney() {
        return processMoney;
    }
    public void setProcessMoney(BigDecimal processMoney) {
        this.processMoney = processMoney;
    }
    public BigDecimal getProcessRatio() {
        return processRatio;
    }
    public void setProcessRatio(BigDecimal processRatio) {
        this.processRatio = processRatio;
    }
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
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
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
