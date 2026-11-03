package com.bytter.report.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * Title: DTO
 * Description: APP首页报表：存款取数通用DTO
 *
 * @author huangx
 * @version 1.0
 * @since 2020-06-04 15:35:12
 */
public class DepositReportCommonDTO implements Serializable {
    /**
     * 业务类别
     */
    private String busType;
    /**
     * 业务类别名称
     */
    private String busTypeName;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 占比
     */
    private BigDecimal proportion;
    /**
     * 月环比
     */
    private BigDecimal monthOnMonth;
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    public String getBusTypeName() {
        return busTypeName;
    }
    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getProportion() {
        return proportion;
    }
    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }
    public BigDecimal getMonthOnMonth() {
        return monthOnMonth;
    }
    public void setMonthOnMonth(BigDecimal monthOnMonth) {
        this.monthOnMonth = monthOnMonth;
    }
    @Override
    public int hashCode() {
        return busType.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        DepositReportCommonDTO deposit = (DepositReportCommonDTO) obj;
        return busType.equals(deposit.getBusType());
    }
}
