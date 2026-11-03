package com.bytter.bill.api.bill.dto;
import com.bytter.core.base.entity.IdEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 付款办理挂接已付款票据中间实体
 *
 * @author ouyj
 */
public class AbBillPayMiddleDTO extends IdEntity implements Serializable {
    /**
     * 票据付款中间表id
     */
    private String id;
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
     * 状态 0暂存 1审批通过
     */
    private Integer status;

    /** 出票标志 */
    private String newStmIssuofpprInd;

    /** 是否允许分包 */
    private String blgrdbagcrclPrmtInd;
    @Override
    public String getId() {
        return id;
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getNewStmIssuofpprInd() {
        return newStmIssuofpprInd;
    }

    public void setNewStmIssuofpprInd(String newStmIssuofpprInd) {
        this.newStmIssuofpprInd = newStmIssuofpprInd;
    }

    public String getBlgrdbagcrclPrmtInd() {
        return blgrdbagcrclPrmtInd;
    }

    public void setBlgrdbagcrclPrmtInd(String blgrdbagcrclPrmtInd) {
        this.blgrdbagcrclPrmtInd = blgrdbagcrclPrmtInd;
    }
    @Override
    public String toString() {
        return "AbBillPayMiddleDTO{" +
                "payId='" + payId + '\'' +
                ", billId='" + billId + '\'' +
                ", balance='" + balance + '\'' +
                ", payMoney='" + payMoney + '\'' +
                '}';
    }
}
