package com.bytter.biz.report.vo;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
public class DepositBalanceAnalysisVO implements Serializable {
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
     * 存储银行超链接的ID集合
     */
    private List<String> bankLinkShow = new ArrayList<>();
    /**
     * 存储银行超链接的ID
     */
    private String bankId;
    /**
     * 存储内部账户超链接的ID集合
     */
    private List<String> innerLinkShow = new ArrayList<>();
    /**
     * 存储内部账户超链接的ID
     */
    private String innerId;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 较上月末-余额
     */
    private BigDecimal monthBalance;
    /**
     * 较上月末-差额
     */
    private BigDecimal monthDifferenceBalance;
    /**
     * 较上月末-比例
     */
    private BigDecimal monthProportion;
    /**
     * 较上季度-余额
     */
    private BigDecimal quarterBalance;
    /**
     * 较上季度-差额
     */
    private BigDecimal quarterDifferenceBalance;
    /**
     * 较上季度-比例
     */
    private BigDecimal quarterProportion;
    /**
     * 较三个月前-余额
     */
    private BigDecimal threeMonthBalance;
    /**
     * 较三个月前-差额
     */
    private BigDecimal threeMonthDifferenceBalance;
    /**
     * 较三个月前-比例
     */
    private BigDecimal threeMonthProportion;
    /**
     * 较上年同期-余额
     */
    private BigDecimal yearOverYearBalance;
    /**
     * 较上年同期-差额
     */
    private BigDecimal yearOverYearDifferenceBalance;
    /**
     * 较上年同期-比例
     */
    private BigDecimal yearOverYearProportion;
    /**
     * 较年初-余额
     */
    private BigDecimal yearBalance;
    /**
     * 较年初-差额
     */
    private BigDecimal yearDifferenceBalance;
    /**
     * 较年初-比例
     */
    private BigDecimal yearProportion;
    /**币种ID*/
    @Currency(inject="curName")
    private String curId;
    private String curName;
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
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
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getMonthBalance() {
        return monthBalance;
    }
    public void setMonthBalance(BigDecimal monthBalance) {
        this.monthBalance = monthBalance;
    }
    public BigDecimal getMonthDifferenceBalance() {
        return monthDifferenceBalance;
    }
    public void setMonthDifferenceBalance(BigDecimal monthDifferenceBalance) {
        this.monthDifferenceBalance = monthDifferenceBalance;
    }
    public BigDecimal getMonthProportion() {
        return monthProportion;
    }
    public void setMonthProportion(BigDecimal monthProportion) {
        this.monthProportion = monthProportion;
    }
    public BigDecimal getQuarterBalance() {
        return quarterBalance;
    }
    public void setQuarterBalance(BigDecimal quarterBalance) {
        this.quarterBalance = quarterBalance;
    }
    public BigDecimal getQuarterDifferenceBalance() {
        return quarterDifferenceBalance;
    }
    public void setQuarterDifferenceBalance(BigDecimal quarterDifferenceBalance) {
        this.quarterDifferenceBalance = quarterDifferenceBalance;
    }
    public BigDecimal getQuarterProportion() {
        return quarterProportion;
    }
    public void setQuarterProportion(BigDecimal quarterProportion) {
        this.quarterProportion = quarterProportion;
    }
    public BigDecimal getThreeMonthBalance() {
        return threeMonthBalance;
    }
    public void setThreeMonthBalance(BigDecimal threeMonthBalance) {
        this.threeMonthBalance = threeMonthBalance;
    }
    public BigDecimal getThreeMonthDifferenceBalance() {
        return threeMonthDifferenceBalance;
    }
    public void setThreeMonthDifferenceBalance(BigDecimal threeMonthDifferenceBalance) {
        this.threeMonthDifferenceBalance = threeMonthDifferenceBalance;
    }
    public BigDecimal getThreeMonthProportion() {
        return threeMonthProportion;
    }
    public void setThreeMonthProportion(BigDecimal threeMonthProportion) {
        this.threeMonthProportion = threeMonthProportion;
    }
    public BigDecimal getYearOverYearBalance() {
        return yearOverYearBalance;
    }
    public void setYearOverYearBalance(BigDecimal yearOverYearBalance) {
        this.yearOverYearBalance = yearOverYearBalance;
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
    public BigDecimal getYearBalance() {
        return yearBalance;
    }
    public void setYearBalance(BigDecimal yearBalance) {
        this.yearBalance = yearBalance;
    }
    public BigDecimal getYearDifferenceBalance() {
        return yearDifferenceBalance;
    }
    public void setYearDifferenceBalance(BigDecimal yearDifferenceBalance) {
        this.yearDifferenceBalance = yearDifferenceBalance;
    }
    public BigDecimal getYearProportion() {
        return yearProportion;
    }
    public void setYearProportion(BigDecimal yearProportion) {
        this.yearProportion = yearProportion;
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
    public List<String> getBankLinkShow() {
        return bankLinkShow;
    }
    public void setBankLinkShow(List<String> bankLinkShow) {
        this.bankLinkShow = bankLinkShow;
    }
    public List<String> getInnerLinkShow() {
        return innerLinkShow;
    }
    public void setInnerLinkShow(List<String> innerLinkShow) {
        this.innerLinkShow = innerLinkShow;
    }
    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    public String getInnerId() {
        return innerId;
    }
    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
