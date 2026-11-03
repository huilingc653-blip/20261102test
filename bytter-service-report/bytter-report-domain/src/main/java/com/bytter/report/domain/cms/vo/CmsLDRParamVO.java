package com.bytter.report.domain.cms.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author luxy
 * @Title: CmsLDRParamVO
 * @ProjectName T3(3.2)
 * @Description: 融资存贷比传参VO
 * @date 2020/2/7 11:26
 */
public class CmsLDRParamVO implements Serializable {
    private String cycle;//对比周期
    private String isContain;//是否包含在途
    private String typesOfStatistics;//统计维度
    private String netFlag;//当统计维度为银行时取结算中心作为银行的标记
    private String[] cycleList;//对比周期
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;//截止查询时间
    private String curId;//币别
    private String isConvertStandCur;//是否折合本位币
    private String bankType;//银行类型id
    private String netId;//网点id
    private String moneyUnit;//币别单位

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getIsContain() {
        return isContain;
    }

    public void setIsContain(String isContain) {
        this.isContain = isContain;
    }

    public String getSortType() {
        return typesOfStatistics;
    }

    public void setSortType(String typesOfStatistics) {
        this.typesOfStatistics = typesOfStatistics;
    }

    public String getNetFlag() {
        return netFlag;
    }

    public void setNetFlag(String netFlag) {
        this.netFlag = netFlag;
    }

    public String[] getCycleList() {
        return cycleList;
    }

    public void setCycleList(String[] cycleList) {
        this.cycleList = cycleList;
    }

    public String getTypesOfStatistics() {
        return typesOfStatistics;
    }

    public void setTypesOfStatistics(String typesOfStatistics) {
        this.typesOfStatistics = typesOfStatistics;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getIsConvertStandCur() {
        return isConvertStandCur;
    }

    public void setIsConvertStandCur(String isConvertStandCur) {
        this.isConvertStandCur = isConvertStandCur;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getMoneyUnit() {
        return moneyUnit;
    }

    public void setMoneyUnit(String moneyUnit) {
        this.moneyUnit = moneyUnit;
    }
}
