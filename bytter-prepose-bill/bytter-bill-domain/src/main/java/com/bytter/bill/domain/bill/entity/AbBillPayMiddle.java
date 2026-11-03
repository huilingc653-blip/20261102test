package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 付款办理挂接已付款票据中间实体
 *
 * @author ouyj
 */
@Entity
@Table(name = "AB_BILL_PAY_MIDDLE")
@SuppressWarnings("serial")
public class AbBillPayMiddle extends IdEntity implements Serializable {
    /**
     * 付款id
     */
    private String payId;
    /**
     * 票据从表id
     */
    private String billId;
    /**
     * 挂接付款余额
     */
    private BigDecimal balance;
    /**
     * 本次付款金额
     */
    private BigDecimal payMoney;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 状态
     */
    private Integer status;
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "pay_id")
    public String getPayId() {
        return payId;
    }
    public void setPayId(String payId) {
        this.payId = payId;
    }
    @Column(name = "BILL_ID")
    public String getBillId() {
        return billId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }
    @Column(name = "balance")
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    @Column(name = "pay_money")
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
}
