package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author luxy
 * @Title: LoanDailyAnalysisVO
 * @ProjectName T3(3.2)
 * @Description: 日均贷款分析VO
 * @date 2020/2/24 14:02
 */
public class LoanDailyAnalysisVO  {
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
    private java.lang.String bankType;
    /**
     * 银行类型
     */
    private java.lang.String bankTypeName;

    /***网点id*/
    private String netId;
    /*** 融资方式*/
    @BtCompositeSearch()
    @Dict(dictGroup = "Loan.Sign", inject = "loanSignName")
    private String loanSign;
    private String loanSignName;
    /**是否包含在途金额*/
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
    private Date startDate;
    /*** 结束日期*/
    @BtCompositeSearch()
    private Date endDate;
    /*** 余额日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date balanceDate;
    /*** 每日余额*/
    private BigDecimal dailyBalance;

    /*** 期末余额*/
    private BigDecimal curLoanMoney;
    /*** 本期日均贷款金额*/
    private BigDecimal curLoanDailyMoney;


    /*** 上期贷款金额*/
    private BigDecimal preLoanMoney;
    /*** 上期日均贷款金额*/
    private BigDecimal preLoanDailyMoney;
    /*** 较上期差额*/
    private BigDecimal preDifference;
    /*** 上期比例*/
    private BigDecimal preRate;

    /*** 上年同期贷款金额*/
    private BigDecimal lastLoanMoney;
    /*** 上年同期日均贷款金额*/
    private BigDecimal lastLoanDailyMoney;
    /*** 较上年同期差额*/
    private BigDecimal lastDifference;
    /*** 上年同期比例*/
    private BigDecimal lastRate;
    /*** 发放日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;
    /*** 还款日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /*** 还款在途日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayAwayDate;

    /*** 发放额度*/
    private BigDecimal sendMoney;
    /*** 还款额度*/
    private BigDecimal repayMoney;
    /*** 还款在途额度*/
    private BigDecimal repayAwayMoney;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getCurLoanMoney() {
        return curLoanMoney;
    }

    public void setCurLoanMoney(BigDecimal curLoanMoney) {
        this.curLoanMoney = curLoanMoney;
    }

    public BigDecimal getCurLoanDailyMoney() {
        return curLoanDailyMoney;
    }

    public void setCurLoanDailyMoney(BigDecimal curLoanDailyMoney) {
        this.curLoanDailyMoney = curLoanDailyMoney;
    }

    public BigDecimal getPreLoanMoney() {
        return preLoanMoney;
    }

    public void setPreLoanMoney(BigDecimal preLoanMoney) {
        this.preLoanMoney = preLoanMoney;
    }

    public BigDecimal getPreLoanDailyMoney() {
        return preLoanDailyMoney;
    }

    public void setPreLoanDailyMoney(BigDecimal preLoanDailyMoney) {
        this.preLoanDailyMoney = preLoanDailyMoney;
    }

    public BigDecimal getPreDifference() {
        return preDifference;
    }

    public void setPreDifference(BigDecimal preDifference) {
        this.preDifference = preDifference;
    }

    public BigDecimal getPreRate() {
        return preRate;
    }

    public void setPreRate(BigDecimal preRate) {
        this.preRate = preRate;
    }

    public BigDecimal getLastLoanMoney() {
        return lastLoanMoney;
    }

    public void setLastLoanMoney(BigDecimal lastLoanMoney) {
        this.lastLoanMoney = lastLoanMoney;
    }

    public BigDecimal getLastLoanDailyMoney() {
        return lastLoanDailyMoney;
    }

    public void setLastLoanDailyMoney(BigDecimal lastLoanDailyMoney) {
        this.lastLoanDailyMoney = lastLoanDailyMoney;
    }

    public BigDecimal getLastDifference() {
        return lastDifference;
    }

    public void setLastDifference(BigDecimal lastDifference) {
        this.lastDifference = lastDifference;
    }

    public BigDecimal getLastRate() {
        return lastRate;
    }

    public void setLastRate(BigDecimal lastRate) {
        this.lastRate = lastRate;
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

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public BigDecimal getDailyBalance() {
        return dailyBalance;
    }

    public void setDailyBalance(BigDecimal dailyBalance) {
        this.dailyBalance = dailyBalance;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Date getRepayAwayDate() {
        return repayAwayDate;
    }

    public void setRepayAwayDate(Date repayAwayDate) {
        this.repayAwayDate = repayAwayDate;
    }

    public BigDecimal getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }

    public BigDecimal getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }

    public BigDecimal getRepayAwayMoney() {
        return repayAwayMoney;
    }

    public void setRepayAwayMoney(BigDecimal repayAwayMoney) {
        this.repayAwayMoney = repayAwayMoney;
    }
}
