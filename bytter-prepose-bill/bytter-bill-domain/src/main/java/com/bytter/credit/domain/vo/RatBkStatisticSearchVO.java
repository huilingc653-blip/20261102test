package com.bytter.credit.domain.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * 授信报表查询VO
 * 
 * @author yangl
 *
 */
public class RatBkStatisticSearchVO implements Serializable {
    /** 单位代码 */
    private String corpId;
    /** 单位代码 */
    private String corpName;
    /** 银行编号 */
    private String bankCode;
    /** 银行编号 */
    private String bankName;
    /** 估计授信时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ratDate;
    /** 授信币种 */
    private String curCode;
    /** 授信币种 */
    private String curName;
    /** 币别单位 */
    private String moneyType;
    private String optType;
    private String bankTypeId;
    /** 授信种类 */
    private String ratType;
    /** 授信品种 */
    private String ratCode;
    /** 授信模式 */
    private String ratMode;
    /** 授信开始时间 */
    private Date beginDate;
    /** 授信结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 授信开始时间 */
    private Date beginDate2;
    /** 授信结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate2;
    // 统计标识
    private String flagSign;
    /** 授信开始时间 */
    private String beginDateStr;
    /** 授信结束时间 */
    private String endDateStr;
    /**
     * 过期标志
     */
    private String isExpire;
    /**
     * 统计维度
     */
    private String typesOfStatistics;
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
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public Date getRatDate() {
        return ratDate;
    }
    public void setRatDate(Date ratDate) {
        this.ratDate = ratDate;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getMoneyType() {
        return moneyType;
    }
    public void setMoneyType(String moneyType) {
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
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
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
    public String getFlagSign() {
        return flagSign;
    }
    public void setFlagSign(String flagSign) {
        this.flagSign = flagSign;
    }
    public String getBeginDateStr() {
        return beginDateStr;
    }
    public void setBeginDateStr(String beginDateStr) {
        this.beginDateStr = beginDateStr;
    }
    public String getEndDateStr() {
        return endDateStr;
    }
    public void setEndDateStr(String endDateStr) {
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
}
