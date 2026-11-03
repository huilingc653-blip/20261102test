package com.bytter.finance.domain.basedata.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 还款计划主表
 * @author songkk
 * @date 2017-08-01 09:58:44
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_REPAYMENT_SCHEDULE", schema = "")
@SuppressWarnings("serial")
public class CmsRepaymentSchedule extends IdEntity implements java.io.Serializable {
    /** 贷款类型 */
    private String cmsType;
    /** 业务单据号 */
    private String billNo;
    /** 状态 */
    private String status;
    /** 还款开始日 */
    private java.util.Date repayBeginDate;
    /** 还款周期 */
    private String repayCycel;
    /** 利率 */
    private java.math.BigDecimal loanRate;
    /** 开始日期 */
    private java.util.Date loanDate;
    /** 还款日期 */
    private java.util.Date repayDate;
    /** 还款方式 */
    private String repayType;
    /** 发放金额 */
    private java.math.BigDecimal loanMoney;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    @Column(name = "CMS_TYPE")
    public String getCmsType() {
        return cmsType;
    }
    public void setCmsType(String cmsType) {
        this.cmsType = cmsType;
    }
    @Column(name = "BILL_NO")
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "REPAY_BEGIN_DATE")
    public java.util.Date getRepayBeginDate() {
        return repayBeginDate;
    }
    public void setRepayBeginDate(java.util.Date repayBeginDate) {
        this.repayBeginDate = repayBeginDate;
    }
    @Column(name = "REPAY_CYCEL")
    public String getRepayCycel() {
        return repayCycel;
    }
    public void setRepayCycel(String repayCycel) {
        this.repayCycel = repayCycel;
    }
    @Column(name = "LOAN_RATE")
    public java.math.BigDecimal getLoanRate() {
        return loanRate;
    }
    public void setLoanRate(java.math.BigDecimal loanRate) {
        this.loanRate = loanRate;
    }
    @Column(name = "LOAN_DATE")
    public java.util.Date getLoanDate() {
        return loanDate;
    }
    public void setLoanDate(java.util.Date loanDate) {
        this.loanDate = loanDate;
    }
    @Column(name = "REPAY_DATE")
    public java.util.Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(java.util.Date repayDate) {
        this.repayDate = repayDate;
    }
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    @Column(name = "LOAN_MONEY")
    public java.math.BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(java.math.BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人名称
     */
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return this.createName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人名称
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    @Override
    public String toString() {
        return "CmsRepaymentSchedule [cmsType=" + cmsType + ", billNo=" + billNo + ", status=" + status
                + ", repayBeginDate=" + repayBeginDate + ", repayCycel=" + repayCycel + ", loanRate=" + loanRate
                + ", loanDate=" + loanDate + ", repayDate=" + repayDate + ", repayType=" + repayType + ", loanMoney="
                + loanMoney + ", createName=" + createName + ", createBy=" + createBy + ", createDate=" + createDate
                + "]";
    }
}
