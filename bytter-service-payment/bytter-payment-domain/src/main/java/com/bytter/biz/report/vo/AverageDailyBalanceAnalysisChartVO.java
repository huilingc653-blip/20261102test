package com.bytter.biz.report.vo;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class AverageDailyBalanceAnalysisChartVO implements Serializable {
    /**单位名称*/
    private String corpName;
    @Currency(inject = "curName")
    private String curId;
    /**币别*/
    private String curName;
    /**存款类型*/
    @Dict(dictGroup = "btBankAcc.accType", inject = "depositType")
    private String bankAccType;
    @Dict(dictGroup = "InnerAccount.AccType", inject = "depositType")
    private String innerAccType;
    private String depositType;
    /**银行或内部存款，统计维度*/
    private String statistical;
    /**
     * 余额日期
     */
    private String date;
    /**
     * 本期-日均
     */
    private BigDecimal averageBalance;
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
    public String getBankAccType() {
        return bankAccType;
    }
    public void setBankAccType(String bankAccType) {
        this.bankAccType = bankAccType;
    }
    public String getInnerAccType() {
        return innerAccType;
    }
    public void setInnerAccType(String innerAccType) {
        this.innerAccType = innerAccType;
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
    public BigDecimal getAverageBalance() {
        return averageBalance;
    }
    public void setAverageBalance(BigDecimal averageBalance) {
        this.averageBalance = averageBalance;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
