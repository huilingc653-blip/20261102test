package com.bytter.report.domain.cms.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 融资风险敞口余额汇总表
 *
 * @author chenlw 2020-12-07
 * @since 3.4
 */
public class FinancingRiskExposureBalanceVO implements Serializable {
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 单位代码
     */
    private String corpName;
    /**
     * 流贷风险敞口余额（数值型）
     * 银行借款模块中的借款余额减保证金金额
     */
    private BigDecimal flowLoanRiskExpBal;
    /**
     * 票据风险敞口余额（数值型）
     * 开出应付票据的余额（是否包括应收票据贴现呢?）减保证金金额
     */
    private BigDecimal billRiskExpBal;
    /**
     * 债券风险敞口余额（数值型）
     * 直接融资的余额
     */
    private BigDecimal bondRiskExpBal;
    /**
     * 风险敞口合计（数值型）
     * 流贷风险敞口余额+票据票面风险敞口余额+债券票面余额
     */
    private BigDecimal riskExpSum;
    /**
     * 利息净支出合计（数值型）
     * 上面三项业务的利息支出合计-对应保证金利息收入
     */
    private BigDecimal netInterestExpenseSum;
    /**
     * 环比增幅（数值型）
     * 融资合计数与去年同期相比的增幅
     */
    private BigDecimal chainIncrease;
    /**
     * 较去年底增幅（数值型）
     * 融资合计数与去年底相比的增幅
     */
    private BigDecimal increaseComparedLastYearEnd;
    /**
     * 环比增长比例（百分比）
     */
    private BigDecimal chainIncreaseRatioComparedLastMonth;
    /**
     * 较去年底增长比例（百分比）
     */
    private BigDecimal increaseRatioComparedLastYearEnd;
    /**
     * 今年融资合计数 （指定日期）
     */
    private BigDecimal thisYearFinancingMoneySum;
    /**
     * 今年融资合计数 （敞口）（指定日期）
     */
    private BigDecimal thisYearFinancingOpenMoneySum;
    /**
     * 上个月（敞口） 融资合计数
     */
    private BigDecimal lastMonthDateFinancingOpenMoneySum;
    /**
     * 去年年底（敞口） 融资合计数
     */
    private BigDecimal lastYearEndFinancingOpenMoneySum;
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
    public BigDecimal getFlowLoanRiskExpBal() {
        return flowLoanRiskExpBal;
    }
    public void setFlowLoanRiskExpBal(BigDecimal flowLoanRiskExpBal) {
        this.flowLoanRiskExpBal = flowLoanRiskExpBal;
    }
    public BigDecimal getBillRiskExpBal() {
        return billRiskExpBal;
    }
    public void setBillRiskExpBal(BigDecimal billRiskExpBal) {
        this.billRiskExpBal = billRiskExpBal;
    }
    public BigDecimal getBondRiskExpBal() {
        return bondRiskExpBal;
    }
    public void setBondRiskExpBal(BigDecimal bondRiskExpBal) {
        this.bondRiskExpBal = bondRiskExpBal;
    }
    public BigDecimal getRiskExpSum() {
        return riskExpSum;
    }
    public void setRiskExpSum(BigDecimal riskExpSum) {
        this.riskExpSum = riskExpSum;
    }
    public BigDecimal getNetInterestExpenseSum() {
        return netInterestExpenseSum;
    }
    public void setNetInterestExpenseSum(BigDecimal netInterestExpenseSum) {
        this.netInterestExpenseSum = netInterestExpenseSum;
    }
    public BigDecimal getChainIncrease() {
        return chainIncrease;
    }
    public void setChainIncrease(BigDecimal chainIncrease) {
        this.chainIncrease = chainIncrease;
    }
    public BigDecimal getIncreaseComparedLastYearEnd() {
        return increaseComparedLastYearEnd;
    }
    public void setIncreaseComparedLastYearEnd(BigDecimal increaseComparedLastYearEnd) {
        this.increaseComparedLastYearEnd = increaseComparedLastYearEnd;
    }
    public BigDecimal getChainIncreaseRatioComparedLastMonth() {
        return chainIncreaseRatioComparedLastMonth;
    }
    public void setChainIncreaseRatioComparedLastMonth(BigDecimal chainIncreaseRatioComparedLastMonth) {
        this.chainIncreaseRatioComparedLastMonth = chainIncreaseRatioComparedLastMonth;
    }
    public BigDecimal getIncreaseRatioComparedLastYearEnd() {
        return increaseRatioComparedLastYearEnd;
    }
    public void setIncreaseRatioComparedLastYearEnd(BigDecimal increaseRatioComparedLastYearEnd) {
        this.increaseRatioComparedLastYearEnd = increaseRatioComparedLastYearEnd;
    }
    public BigDecimal getThisYearFinancingMoneySum() {
        return thisYearFinancingMoneySum;
    }
    public void setThisYearFinancingMoneySum(BigDecimal thisYearFinancingMoneySum) {
        this.thisYearFinancingMoneySum = thisYearFinancingMoneySum;
    }
    public BigDecimal getThisYearFinancingOpenMoneySum() {
        return thisYearFinancingOpenMoneySum;
    }
    public void setThisYearFinancingOpenMoneySum(BigDecimal thisYearFinancingOpenMoneySum) {
        this.thisYearFinancingOpenMoneySum = thisYearFinancingOpenMoneySum;
    }
    public BigDecimal getLastMonthDateFinancingOpenMoneySum() {
        return lastMonthDateFinancingOpenMoneySum;
    }
    public void setLastMonthDateFinancingOpenMoneySum(BigDecimal lastMonthDateFinancingOpenMoneySum) {
        this.lastMonthDateFinancingOpenMoneySum = lastMonthDateFinancingOpenMoneySum;
    }
    public BigDecimal getLastYearEndFinancingOpenMoneySum() {
        return lastYearEndFinancingOpenMoneySum;
    }
    public void setLastYearEndFinancingOpenMoneySum(BigDecimal lastYearEndFinancingOpenMoneySum) {
        this.lastYearEndFinancingOpenMoneySum = lastYearEndFinancingOpenMoneySum;
    }
}
