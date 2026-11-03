package com.bytter.report.domain.cms.vo;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 授信报表查询VO
 *
 * @author yangl
 */
public class RatBkStatisticSearchVO implements Serializable {

    /**
     * 单位代码
     */
    private java.lang.String corpId;
    /**
     * 单位代码
     */
    private java.lang.String corpName;
    /**
     * 银行编号
     */
    private java.lang.String bankCode;
    /**
     * 银行编号
     */
    private java.lang.String bankName;
    /**
     * 估计授信时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date ratDate;
    /**
     * 授信币种
     */
    private java.lang.String curCode;
    /**
     * 授信币种
     */
    private java.lang.String curName;
    /**
     * 币别单位
     */
    private java.lang.String moneyType;

    /**
     * 状态
     */
    private String status;
    private String optType;
    private String bankTypeId;
    /**
     * 授信种类
     */
    private java.lang.String ratType;
    /**
     * 授信品种
     */
    private java.lang.String ratCode;
    /**
     * 授信模式
     */
    private java.lang.String ratMode;
    /**
     * 授信开始时间
     */
    private java.util.Date beginDate;
    /**
     * 授信结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endDate;
    /**
     * 授信开始时间
     */
    private java.util.Date beginDate2;
    /**
     * 授信结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endDate2;
    //统计标识
    private String flagSign;
    /**
     * 授信开始时间
     */
    private java.lang.String beginDateStr;
    /**
     * 授信结束时间
     */
    private java.lang.String endDateStr;

    /**
     * 过期标志
     */
    private String isExpire;

    /**
     * 统计维度
     */
    private String typesOfStatistics;

    /**
     * 报表日期
     */
    private String reportDate;

    /**
     * 制表人
     */
    private String userName;

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public java.lang.String getCorpId() {
        return corpId;
    }

    public void setCorpId(java.lang.String corpId) {
        this.corpId = corpId;
    }

    public java.lang.String getCorpName() {
        return corpName;
    }

    public void setCorpName(java.lang.String corpName) {
        this.corpName = corpName;
    }

    public java.lang.String getBankCode() {
        return bankCode;
    }

    public void setBankCode(java.lang.String bankCode) {
        this.bankCode = bankCode;
    }

    public java.lang.String getBankName() {
        return bankName;
    }

    public void setBankName(java.lang.String bankName) {
        this.bankName = bankName;
    }

    public java.util.Date getRatDate() {
        return ratDate;
    }

    public void setRatDate(java.util.Date ratDate) {
        this.ratDate = ratDate;
    }

    public java.lang.String getCurCode() {
        return curCode;
    }

    public void setCurCode(java.lang.String curCode) {
        this.curCode = curCode;
    }

    public java.lang.String getCurName() {
        return curName;
    }

    public void setCurName(java.lang.String curName) {
        this.curName = curName;
    }

    public java.lang.String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(java.lang.String moneyType) {
        this.moneyType = moneyType;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public java.lang.String getRatCode() {
        return ratCode;
    }

    public void setRatCode(java.lang.String ratCode) {
        this.ratCode = ratCode;
    }

    public java.util.Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(java.util.Date beginDate) {
        this.beginDate = beginDate;
    }

    public java.util.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    public String getFlagSign() {
        return flagSign;
    }

    public void setFlagSign(String flagSign) {
        this.flagSign = flagSign;
    }

    public java.lang.String getBeginDateStr() {
        return beginDateStr;
    }

    public void setBeginDateStr(java.lang.String beginDateStr) {
        this.beginDateStr = beginDateStr;
    }

    public java.lang.String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(java.lang.String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getRatMode() {
        return ratMode;
    }

    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }

    public String getRatType() {
        return ratType;
    }

    public void setRatType(String ratType) {
        this.ratType = ratType;
    }

    public String getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(String isExpire) {
        this.isExpire = isExpire;
    }

    public String getTypesOfStatistics() {
        return typesOfStatistics;
    }

    public void setTypesOfStatistics(String typesOfStatistics) {
        this.typesOfStatistics = typesOfStatistics;
    }

    public Date getBeginDate2() {
        return beginDate2;
    }

    public void setBeginDate2(Date beginDate2) {
        this.beginDate2 = beginDate2;
    }

    public Date getEndDate2() {
        return endDate2;
    }

    public void setEndDate2(Date endDate2) {
        this.endDate2 = endDate2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
