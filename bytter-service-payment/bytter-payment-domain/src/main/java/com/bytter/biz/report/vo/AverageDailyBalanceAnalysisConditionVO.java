package com.bytter.biz.report.vo;
import java.io.Serializable;
public class AverageDailyBalanceAnalysisConditionVO implements Serializable {
    private String corpIds;
    private String corpName;
    private String depositType;
    private String statistical;
    private String curId;
    private String isConverted;
    private String moneyUnit;
    private String beforeDate;
    private String afterDate;
    private String frequency;
    private String reducedCurId;
    private String corpCurId;
    public String getCorpCurId() {
        return corpCurId;
    }
    public void setCorpCurId(String corpCurId) {
        this.corpCurId = corpCurId;
    }
    public String getReducedCurId() {
        return reducedCurId;
    }
    public void setReducedCurId(String reducedCurId) {
        this.reducedCurId = reducedCurId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getFrequency() {
        return frequency;
    }
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    public String getCorpIds() {
        return corpIds;
    }
    public void setCorpIds(String corpIds) {
        this.corpIds = corpIds;
    }
    public String getDepositType() {
        return depositType;
    }
    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }
    public String getStatistical() {
        return statistical;
    }
    public void setStatistical(String statistical) {
        this.statistical = statistical;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getIsConverted() {
        return isConverted;
    }
    public void setIsConverted(String isConverted) {
        this.isConverted = isConverted;
    }
    public String getMoneyUnit() {
        return moneyUnit;
    }
    public void setMoneyUnit(String moneyUnit) {
        this.moneyUnit = moneyUnit;
    }
    public String getBeforeDate() {
        return beforeDate;
    }
    public void setBeforeDate(String beforeDate) {
        this.beforeDate = beforeDate;
    }
    public String getAfterDate() {
        return afterDate;
    }
    public void setAfterDate(String afterDate) {
        this.afterDate = afterDate;
    }
}
