package com.bytter.bill.api.bill.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 付款选票信息DTO
 */
public class NisBillDTO implements Serializable {

    /**ID*/
    private String id;

    /**付款单ID*/
    private String payId;

    /**票据ID*/
    private String billId;

    /**挂接付款金额*/
    private BigDecimal balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "NisBillVo{" +
                "id='" + id + '\'' +
                ", payId='" + payId + '\'' +
                ", billId='" + billId + '\'' +
                ", balance=" + balance +
                '}';
    }
}
