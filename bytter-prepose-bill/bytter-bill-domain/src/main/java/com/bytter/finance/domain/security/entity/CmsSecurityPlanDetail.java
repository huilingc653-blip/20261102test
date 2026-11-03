package com.bytter.finance.domain.security.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 资产证券化还款计划明细表
 * @date 2022-08-30
 */
@Entity
@Table(name = "CMS_SECURITY_PLAN_DETAIL", schema = "")
public class CmsSecurityPlanDetail extends IdEntity implements java.io.Serializable {
    /**
     * 证券发行编号
     */
    private String securityIssueCode;
    /**
     * 计划主表id
     */
    private String securityPlanId;
    /**
     * 还款日期
     */
    private Date repayDate;
    /**
     * 计划还款日
     */
    private Date planRepayDate;
    /**
     * 还款本金
     */
    private BigDecimal repayMoney;
    /**
     * 还款利息
     */
    private BigDecimal repayInterest;
    /**
     * 还款金额
     */
    private BigDecimal repayAmount;
    /**
     * 剩余本金
     */
    private BigDecimal remainMoney;
    /**
     * 是否已还
     */
    private String isRepay;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 是否有效
     */
    private String validSign;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    @Column(name = "SECURITY_ISSUE_CODE")
    public String getSecurityIssueCode() {
        return securityIssueCode;
    }
    public void setSecurityIssueCode(String securityIssueCode) {
        this.securityIssueCode = securityIssueCode;
    }
    @Column(name = "SECURITY_PLAN_ID")
    public String getSecurityPlanId() {
        return securityPlanId;
    }
    public void setSecurityPlanId(String securityPlanId) {
        this.securityPlanId = securityPlanId;
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
    @Column(name = "REPAY_INTEREST")
    public BigDecimal getRepayInterest() {
        return repayInterest;
    }
    public void setRepayInterest(BigDecimal repayInterest) {
        this.repayInterest = repayInterest;
    }
    @Column(name = "REPAY_AMOUNT")
    public BigDecimal getRepayAmount() {
        return repayAmount;
    }
    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }
    @Column(name = "REMAIN_MONEY")
    public BigDecimal getRemainMoney() {
        return remainMoney;
    }
    public void setRemainMoney(BigDecimal remainMoney) {
        this.remainMoney = remainMoney;
    }
    @Column(name = "IS_REPAY")
    public String getIsRepay() {
        return isRepay;
    }
    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
