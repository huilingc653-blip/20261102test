package com.bytter.report.domain.cms.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 融资风险余额汇总表
 *
 * @author zhenghj 2020-12-11
 * @since 3.4
 */
public class FinancingRiskBalanceVO implements Serializable {
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 单位代码
     */
    private String corpName;
    /**
     * 流贷余额（数值型）
     * 银行借款模块中的借款余额
     */
    private BigDecimal flowLoanRiskBal;
    /**
     * 票据票面余额（数值型）
     * 开出应付票据的余额+收入应收票据
     */
    private BigDecimal billRiskBal;
    /**
     * 债券票面余额（数值型）
     * 直接融资的余额
     */
    private BigDecimal bondRiskBal;
    /**
     * 融资合计（数值型）
     * 流贷余额+票据票面余额+债券票面余额
     */
    private BigDecimal financingSum;
    /**
     * 利息净支出合计（数值型）
     * 上面三项业务的利息支出合计
     */
    private BigDecimal netInterestSum;
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
     * 上个月同期 融资合计数
     */
    private BigDecimal lastMonthSameDateFinancingMoneySum;
    /**
     * 去年年底 融资合计数
     */
    private BigDecimal lastYearEndFinancingMoneySum;
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
    public BigDecimal getFlowLoanRiskBal() {
        return flowLoanRiskBal;
    }
    public void setFlowLoanRiskBal(BigDecimal flowLoanRiskBal) {
        this.flowLoanRiskBal = flowLoanRiskBal;
    }
    public BigDecimal getBillRiskBal() {
        return billRiskBal;
    }
    public void setBillRiskBal(BigDecimal billRiskBal) {
        this.billRiskBal = billRiskBal;
    }
    public BigDecimal getBondRiskBal() {
        return bondRiskBal;
    }
    public void setBondRiskBal(BigDecimal bondRiskBal) {
        this.bondRiskBal = bondRiskBal;
    }
    public BigDecimal getFinancingSum() {
        return financingSum;
    }
    public void setFinancingSum(BigDecimal financingSum) {
        this.financingSum = financingSum;
    }
    public BigDecimal getNetInterestSum() {
        return netInterestSum;
    }
    public void setNetInterestSum(BigDecimal netInterestSum) {
        this.netInterestSum = netInterestSum;
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
    public BigDecimal getLastMonthSameDateFinancingMoneySum() {
        return lastMonthSameDateFinancingMoneySum;
    }
    public void setLastMonthSameDateFinancingMoneySum(BigDecimal lastMonthSameDateFinancingMoneySum) {
        this.lastMonthSameDateFinancingMoneySum = lastMonthSameDateFinancingMoneySum;
    }
    public BigDecimal getLastYearEndFinancingMoneySum() {
        return lastYearEndFinancingMoneySum;
    }
    public void setLastYearEndFinancingMoneySum(BigDecimal lastYearEndFinancingMoneySum) {
        this.lastYearEndFinancingMoneySum = lastYearEndFinancingMoneySum;
    }
    @Override
    public String toString() {
        return "FinancingRiskBalanceVO{" +
                "corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", flowLoanRiskBal=" + flowLoanRiskBal +
                ", billRiskBal=" + billRiskBal +
                ", bondRiskBal=" + bondRiskBal +
                ", financingSum=" + financingSum +
                ", netInterestSum=" + netInterestSum +
                ", chainIncrease=" + chainIncrease +
                ", increaseComparedLastYearEnd=" + increaseComparedLastYearEnd +
                ", chainIncreaseRatioComparedLastMonth=" + chainIncreaseRatioComparedLastMonth +
                ", increaseRatioComparedLastYearEnd=" + increaseRatioComparedLastYearEnd +
                ", thisYearFinancingMoneySum=" + thisYearFinancingMoneySum +
                ", lastMonthSameDateFinancingMoneySum=" + lastMonthSameDateFinancingMoneySum +
                ", lastYearEndFinancingMoneySum=" + lastYearEndFinancingMoneySum +
                '}';
    }
}
