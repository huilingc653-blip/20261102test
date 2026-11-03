package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.*;


import java.util.List;

/**
 * @author luxy
 * @Title: LoanDailyAnalysisVO
 * @ProjectName T3(3.2)
 * @Description: 日均贷款分析VO
 * @date 2020/2/24 14:02
 */
public class LoanDailyParamVO  {
    /*** 单位id*/
    @BtCompositeSearch()
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;

    /**
     * 银行类型编码
     */
    @BtCompositeSearch()
    @BankType(inject = "bankTypeName")
    private String bankType;
    /**
     * 银行类型
     */
    private String bankTypeName;

    /***网点id*/
    private String netId;
    /*** 融资方式*/
    @BtCompositeSearch()
    @Dict(dictGroup = "Loan.Sign", inject = "loanSignName")
    private String loanSign;
    private String loanSignName;
    /**
     * 是否包含在途金额
     */
    @BtCompositeSearch()
    private String isContain;
    /*** 币别id*/
    @BtCompositeSearch()
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /*** 是否折合本位币*/
    @BtCompositeSearch()
    private String isConvertStandCur;

    private String convertCurId;

    /*** 统计维度*/
    @BtCompositeSearch()
    private String typesOfStatistics;
    /*** 金额单位*/
    @BtCompositeSearch()
    private String moneyUnit;
    /*** 查询频率*/
    @BtCompositeSearch()
    private String searchFrequency;
    /*** 开始日期*/
    @BtCompositeSearch()
    private String startDate;
    /*** 结束日期*/
    @BtCompositeSearch()
    private String endDate;

    private List<String> corps;

    private String searchCorps;

    /*** 查询结束日期*/
    private String searchEndDate;


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

    public String getLoanSign() {
        return loanSign;
    }

    public void setLoanSign(String loanSign) {
        this.loanSign = loanSign;
    }

    public String getLoanSignName() {
        return loanSignName;
    }

    public void setLoanSignName(String loanSignName) {
        this.loanSignName = loanSignName;
    }

    public String getIsContain() {
        return isContain;
    }

    public void setIsContain(String isContain) {
        this.isContain = isContain;
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

    public String getIsConvertStandCur() {
        return isConvertStandCur;
    }

    public void setIsConvertStandCur(String isConvertStandCur) {
        this.isConvertStandCur = isConvertStandCur;
    }

    public String getTypesOfStatistics() {
        return typesOfStatistics;
    }

    public void setTypesOfStatistics(String typesOfStatistics) {
        this.typesOfStatistics = typesOfStatistics;
    }

    public String getMoneyUnit() {
        return moneyUnit;
    }

    public void setMoneyUnit(String moneyUnit) {
        this.moneyUnit = moneyUnit;
    }

    public String getSearchFrequency() {
        return searchFrequency;
    }

    public void setSearchFrequency(String searchFrequency) {
        this.searchFrequency = searchFrequency;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getCorps() {
        return corps;
    }

    public void setCorps(List<String> corps) {
        this.corps = corps;
    }

    public String getSearchCorps() {
        return searchCorps;
    }

    public void setSearchCorps(String searchCorps) {
        this.searchCorps = searchCorps;
    }

    public String getConvertCurId() {
        return convertCurId;
    }

    public void setConvertCurId(String convertCurId) {
        this.convertCurId = convertCurId;
    }

    public String getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(String searchEndDate) {
        this.searchEndDate = searchEndDate;
    }
}
