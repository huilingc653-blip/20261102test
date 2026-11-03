package com.bytter.finance.domain.bond.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: CMS_BOND_REPAY_PLAN_DTL
 * @date 2022-06-06 10:07:46
 */
@Entity
@Table(name = "CMS_BOND_REPAY_PLAN_DTL", schema = "")
@SuppressWarnings("serial")
public class CmsBondRepayPlanDtl extends IdEntity implements java.io.Serializable {
    /**
     * 债券还款计划主表id
     */
    private String planId;
    /**
     * 债券还款支付表id
     */
    private String payId;
    /**
     * 还款日期
     */
    private Date repayDate;
    /**
     * 计划还款日
     */
    private Date planRepayDate;
    /**
     * 还款本金值
     */
    private BigDecimal repayMoney;
    /**
     * 剩余本金值
     */
    private BigDecimal remainMoney;
    /**
     * 本次还款金额
     */
    private BigDecimal repayAmount;
    /**
     * 本次偿还占发行总额比例%
     */
    private BigDecimal repayRate;
    /**
     * 是否已还
     */
    private String isRepay;
    /**
     * 有效标志
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
    @Column(name = "PLAN_ID")
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    @Column(name = "PAY_ID")
    public String getPayId() {
        return payId;
    }
    public void setPayId(String payId) {
        this.payId = payId;
    }
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    @Column(name = "PLAN_REPAY_DATE")
    public Date getPlanRepayDate() {
        return planRepayDate;
    }
    public void setPlanRepayDate(Date planRepayDate) {
        this.planRepayDate = planRepayDate;
    }
    @Column(name = "REPAY_MONEY")
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    @Column(name = "REMAIN_MONEY")
    public BigDecimal getRemainMoney() {
        return remainMoney;
    }
    public void setRemainMoney(BigDecimal remainMoney) {
        this.remainMoney = remainMoney;
    }
    @Column(name = "REPAY_AMOUNT")
    public BigDecimal getRepayAmount() {
        return repayAmount;
    }
    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }
    @Column(name = "REPAY_RATE")
    public BigDecimal getRepayRate() {
        return repayRate;
    }
    public void setRepayRate(BigDecimal repayRate) {
        this.repayRate = repayRate;
    }
    @Column(name = "IS_REPAY")
    public String getIsRepay() {
        return isRepay;
    }
    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
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
