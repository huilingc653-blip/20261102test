package com.bytter.finance.domain.lease.entity;
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
 * @Description: 租赁计划明细表
 * @date 2022-04-02 14:22:49
 */
@Entity
@Table(name = "CMS_LEASE_CAPITAL_PLANDEL", schema = "")
@SuppressWarnings("serial")
public class LeaseCapitalPlanDel extends IdEntity implements java.io.Serializable {
    /**
     * 租赁计划主表id
     */
    private String leasePlanId;
    /**
     * 付租日期
     */
    private Date payDate;
    /**
     * 计划付租日
     */
    private Date payPlanDate;
    /**
     * 租赁本金
     */
    private BigDecimal leaseMonry;
    /**
     * 租赁利息
     */
    private BigDecimal leaseInterest;
    /**
     * 本期租金
     */
    private BigDecimal thisMoney;
    /**
     * 本期费用
     */
    private BigDecimal thisCost;
    /**
     * 保证金金额
     */
    private BigDecimal bailMoney;
    /**
     * 剩余本金
     */
    private BigDecimal surplusMoney;
    /**
     * 是否已还
     */
    private String isRepay;
    /**
     * 支付订单号
     */
    private String payNo;
    /**
     * 是否有效状态 -2:已删除
     */
    private String validSign;
    /**
     * 备注
     */
    private String rmk;
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
    @Column(name = "LEASE_PLAN_ID")
    public String getLeasePlanId() {
        return leasePlanId;
    }
    public void setLeasePlanId(String leasePlanId) {
        this.leasePlanId = leasePlanId;
    }
    @Column(name = "PAY_DATE")
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    @Column(name = "PAY_PLAN_DATE")
    public Date getPayPlanDate() {
        return payPlanDate;
    }
    public void setPayPlanDate(Date payPlanDate) {
        this.payPlanDate = payPlanDate;
    }
    @Column(name = "LEASE_MONRY")
    public BigDecimal getLeaseMonry() {
        return leaseMonry;
    }
    public void setLeaseMonry(BigDecimal leaseMonry) {
        this.leaseMonry = leaseMonry;
    }
    @Column(name = "LEASE_INTEREST")
    public BigDecimal getLeaseInterest() {
        return leaseInterest;
    }
    public void setLeaseInterest(BigDecimal leaseInterest) {
        this.leaseInterest = leaseInterest;
    }
    @Column(name = "THIS_MONEY")
    public BigDecimal getThisMoney() {
        return thisMoney;
    }
    public void setThisMoney(BigDecimal thisMoney) {
        this.thisMoney = thisMoney;
    }
    @Column(name = "THIS_COST")
    public BigDecimal getThisCost() {
        return thisCost;
    }
    public void setThisCost(BigDecimal thisCost) {
        this.thisCost = thisCost;
    }
    @Column(name = "BAIL_MONEY")
    public BigDecimal getBailMoney() {
        return bailMoney;
    }
    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }
    @Column(name = "SURPLUS_MONEY")
    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }
    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }
    @Column(name = "IS_REPAY")
    public String getIsRepay() {
        return isRepay;
    }
    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
    }
    @Column(name = "PAY_NO")
    public String getPayNo() {
        return payNo;
    }
    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
