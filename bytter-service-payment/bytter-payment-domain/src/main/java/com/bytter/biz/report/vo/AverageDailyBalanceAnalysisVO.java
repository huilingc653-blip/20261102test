package com.bytter.biz.report.vo;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class AverageDailyBalanceAnalysisVO implements Serializable {
    private String id;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 网点ID
     */
    private String netId;
    /**
     * 网点名称
     */
    private String netName;
    /**
     * 币种ID
     */
    @Currency(inject = "curName")
    private String curId;
    /**
     * 币种名称
     */
    private String curName;
    /**
     * 余额日期
     */
    private Date balDate;
    /**
     * 期末余额
     */
    private BigDecimal balance;
    /**
     * 总余额
     */
    private BigDecimal totalBalance = new BigDecimal(0);
    /**
     * 本期-日均
     */
    private BigDecimal averageBalance;
    /**
     * 较上期-日均
     */
    private BigDecimal previousAverageBalance;
    /**
     * 较上期-差额
     */
    private BigDecimal previousDifferenceBalance;
    /**
     * 较上期-比例
     */
    private BigDecimal previousProportion;
    /**
     * 较上年同期-日均
     */
    private BigDecimal yearOverYearAverageBalance;
    /**
     * 较上年同期-差额
     */
    private BigDecimal yearOverYearDifferenceBalance;
    /**
     * 较上年同期-比例
     */
    private BigDecimal yearOverYearProportion;
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
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
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
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getAverageBalance() {
        return averageBalance;
    }
    public void setAverageBalance(BigDecimal averageBalance) {
        this.averageBalance = averageBalance;
    }
    public BigDecimal getPreviousAverageBalance() {
        return previousAverageBalance;
    }
    public void setPreviousAverageBalance(BigDecimal previousAverageBalance) {
        this.previousAverageBalance = previousAverageBalance;
    }
    public BigDecimal getPreviousDifferenceBalance() {
        return previousDifferenceBalance;
    }
    public void setPreviousDifferenceBalance(BigDecimal previousDifferenceBalance) {
        this.previousDifferenceBalance = previousDifferenceBalance;
    }
    public BigDecimal getPreviousProportion() {
        return previousProportion;
    }
    public void setPreviousProportion(BigDecimal previousProportion) {
        this.previousProportion = previousProportion;
    }
    public BigDecimal getYearOverYearAverageBalance() {
        return yearOverYearAverageBalance;
    }
    public void setYearOverYearAverageBalance(BigDecimal yearOverYearAverageBalance) {
        this.yearOverYearAverageBalance = yearOverYearAverageBalance;
    }
    public BigDecimal getYearOverYearDifferenceBalance() {
        return yearOverYearDifferenceBalance;
    }
    public void setYearOverYearDifferenceBalance(BigDecimal yearOverYearDifferenceBalance) {
        this.yearOverYearDifferenceBalance = yearOverYearDifferenceBalance;
    }
    public BigDecimal getYearOverYearProportion() {
        return yearOverYearProportion;
    }
    public void setYearOverYearProportion(BigDecimal yearOverYearProportion) {
        this.yearOverYearProportion = yearOverYearProportion;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }
    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }
    public Date getBalDate() {
        return balDate;
    }
    public void setBalDate(Date balDate) {
        this.balDate = balDate;
    }
}
