package com.bytter.report.domain.netbank.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 银行存款余额趋势VO
 *
 * @author zuoy 2018-12-05
 */
public class ReportAccountBalVO implements Serializable {
    /**
     * 月份
     */
    private Integer month;
    /**
     * 余额
     */
    private BigDecimal bal;
    /**
     * 同比
     */
    private BigDecimal yearOnYear;
    /**
     * 环比
     */
    private BigDecimal monthOnMonth;
    /**
     * 类型 0-银行账户， 1-内部账户
     */
    private String type;
    public Integer getMonth() {
        return month;
    }
    public void setMonth(Integer month) {
        this.month = month;
    }
    public BigDecimal getBal() {
        return bal;
    }
    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }
    public BigDecimal getYearOnYear() {
        return yearOnYear;
    }
    public void setYearOnYear(BigDecimal yearOnYear) {
        this.yearOnYear = yearOnYear;
    }
    public BigDecimal getMonthOnMonth() {
        return monthOnMonth;
    }
    public void setMonthOnMonth(BigDecimal monthOnMonth) {
        this.monthOnMonth = monthOnMonth;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
