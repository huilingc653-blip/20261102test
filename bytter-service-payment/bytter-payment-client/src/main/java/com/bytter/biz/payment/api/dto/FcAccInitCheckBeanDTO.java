package com.bytter.biz.payment.api.dto;
import java.io.Serializable;
/**
 * @author YonJar
 * @date 2023/4/27.
 * @description
 */
public class FcAccInitCheckBeanDTO implements Serializable {
    private static final long serialVersionUID = 7885616781898432380L;
    /** 账号 */
    private java.lang.String account;
    /** 户名 */
    private java.lang.String accName;
    /** 余额 */
    private java.math.BigDecimal balance;
    /** 积数 */
    private java.math.BigDecimal cumulation;
    /** 初始化日期 */
    private java.util.Date initTime;
    /** 年余额 */
    private java.math.BigDecimal yearBalance;
    /** 年合计 */
    private java.math.BigDecimal yearTotal;
    /** 状态 */
    private java.lang.Integer status;
    /**
     * 经办员
     */
    private String operator;
    public java.lang.String getAccount() {
        return account;
    }
    public void setAccount(java.lang.String account) {
        this.account = account;
    }
    public java.lang.String getAccName() {
        return accName;
    }
    public void setAccName(java.lang.String accName) {
        this.accName = accName;
    }
    public java.math.BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(java.math.BigDecimal balance) {
        this.balance = balance;
    }
    public java.math.BigDecimal getCumulation() {
        return cumulation;
    }
    public void setCumulation(java.math.BigDecimal cumulation) {
        this.cumulation = cumulation;
    }
    public java.util.Date getInitTime() {
        return initTime;
    }
    public void setInitTime(java.util.Date initTime) {
        this.initTime = initTime;
    }
    public java.math.BigDecimal getYearBalance() {
        return yearBalance;
    }
    public void setYearBalance(java.math.BigDecimal yearBalance) {
        this.yearBalance = yearBalance;
    }
    public java.math.BigDecimal getYearTotal() {
        return yearTotal;
    }
    public void setYearTotal(java.math.BigDecimal yearTotal) {
        this.yearTotal = yearTotal;
    }
    public java.lang.Integer getStatus() {
        return status;
    }
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
}
