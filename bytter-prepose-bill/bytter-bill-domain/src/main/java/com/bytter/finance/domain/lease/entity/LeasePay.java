package com.bytter.finance.domain.lease.entity;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author mof
 * @version 1.0
 * @Title: Entity
 * @Description: 租赁租金支付表
 * @date 2022-04-15 14:22:49
 */
@Entity
@Table(name = "CMS_LEASE_PAY", schema = "")
@SuppressWarnings("serial")
public class LeasePay extends IdEntity implements java.io.Serializable {
    /**
     * 租赁表id
     */
    private String leaseId;
    /**
     * 承租人
     */
    private String corpId;
    /**
     * 租金计划主表id
     */
    private String leasePlanId;
    /**
     * 租金计划子表id
     */
    private String leasePlanDelId;
    /**
     * 出租人
     */
    private String lessor;
    /**
     * 收款账号
     */
    private String onlinePaymentAcc;
    /**
     * 收款账户名称
     */
    private String onlineAccName;
    /**
     * 收款账户开户行行号
     */
    private String onlineBankCode;
    /**
     * 收款账户开户行名称
     */
    private String onlineBankName;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 支付方式
     */
    private String onlineSign;
    /**
     * 用途
     */
    private String purpose;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 租金支付日期
     */
    private Date payDate;
    /**
     * 本金
     */
    private BigDecimal money;
    /**
     * 利息
     */
    private BigDecimal interest;
    /**
     * 租金
     */
    private BigDecimal leaseMoney;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    @Column(name = "LEASE_ID")
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "LEASE_PLAN_ID")
    public String getLeasePlanId() {
        return leasePlanId;
    }
    public void setLeasePlanId(String leasePlanId) {
        this.leasePlanId = leasePlanId;
    }
    @Column(name = "LEASE_PLANDEL_ID")
    public String getLeasePlanDelId() {
        return leasePlanDelId;
    }
    public void setLeasePlanDelId(String leasePlanDelId) {
        this.leasePlanDelId = leasePlanDelId;
    }
    @Column(name = "LESSOR")
    public String getLessor() {
        return lessor;
    }
    public void setLessor(String lessor) {
        this.lessor = lessor;
    }
    @Column(name = "ONLINE_PAYMENT_ACC")
    public String getOnlinePaymentAcc() {
        return onlinePaymentAcc;
    }
    public void setOnlinePaymentAcc(String onlinePaymentAcc) {
        this.onlinePaymentAcc = onlinePaymentAcc;
    }
    @Column(name = "ONLINE_ACC_NAME")
    public String getOnlineAccName() {
        return onlineAccName;
    }
    public void setOnlineAccName(String onlineAccName) {
        this.onlineAccName = onlineAccName;
    }
    @Column(name = "ONLINE_BANK_CODE")
    public String getOnlineBankCode() {
        return onlineBankCode;
    }
    public void setOnlineBankCode(String onlineBankCode) {
        this.onlineBankCode = onlineBankCode;
    }
    @Column(name = "ONLINE_BANK_NAME")
    public String getOnlineBankName() {
        return onlineBankName;
    }
    public void setOnlineBankName(String onlineBankName) {
        this.onlineBankName = onlineBankName;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "ONLINE_SIGN")
    public String getOnlineSign() {
        return onlineSign;
    }
    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }
    @Column(name = "PURPOSE")
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "PAY_DATE")
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    @Column(name = "INTEREST")
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    @Column(name = "LEASE_MONEY")
    public BigDecimal getLeaseMoney() {
        return leaseMoney;
    }
    public void setLeaseMoney(BigDecimal leaseMoney) {
        this.leaseMoney = leaseMoney;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
