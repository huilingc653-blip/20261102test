package com.bytter.report.domain.financial.vo;
import com.bytter.core.annotation.Corp;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 总账查询明细VO对象
 *
 * @author zuoy 2020-11-19
 */
public class GeneralLedgerDetailVO implements Serializable {
    /**
     * 单位ID
     */
    @Corp(inject = "netName")
    private String netId;
    /**
     * 单位名称
     */
    private String netName;
    /**
     * 记账日期(yyyy-MM)
     */
    private String ledgerDate;
    /**
     * 年份
     */
    private String year;
    /**
     * 月份
     */
    private String month;
    /**
     * 借方发生额
     */
    private BigDecimal debitMoney;
    /**
     * 贷方发生额
     */
    private BigDecimal creditMoney;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 行数据类型 0-月份合计，1-本年累计，2-上年转入
     */
    private int rowDataType;
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
    public String getLedgerDate() {
        return ledgerDate;
    }
    public void setLedgerDate(String ledgerDate) {
        this.ledgerDate = ledgerDate;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getMonth() {
        return month;
    }
    public void setMonth(String month) {
        this.month = month;
    }
    public BigDecimal getDebitMoney() {
        return debitMoney;
    }
    public void setDebitMoney(BigDecimal debitMoney) {
        this.debitMoney = debitMoney;
    }
    public BigDecimal getCreditMoney() {
        return creditMoney;
    }
    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public int getRowDataType() {
        return rowDataType;
    }
    public void setRowDataType(int rowDataType) {
        this.rowDataType = rowDataType;
    }
}
