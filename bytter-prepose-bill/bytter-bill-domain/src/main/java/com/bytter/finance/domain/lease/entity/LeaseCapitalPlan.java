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
 * @Description: 租赁计划主表
 * @date 2022-04-02 14:22:49
 */
@Entity
@Table(name = "CMS_LEASE_CAPITAL_PLAN", schema = "")
@SuppressWarnings("serial")
public class LeaseCapitalPlan extends IdEntity implements java.io.Serializable {
    /** 承租人 */
    private String corpId;
    /** 租赁表id */
    private String leaseId;
    /** 融资租赁合同编号 */
    private String leaseCode;
    /** 还款开始日 */
    private Date repayBeginDate;
    /** 还款周期 */
    private String repayCycel;
    /** 付款方式 */
    private String repayType;
    /** 起租日 */
    private Date beginDate;
    /** 到期日 */
    private Date endDate;
    /** 利率 */
    private BigDecimal rate;
    /** 利率期限 */
    private String repayCycle;
    /** 支付方式 */
    private String payWay;
    /** 支付金额 */
    private BigDecimal payMoney;
    /** 状态 */
    private Integer status;
    /** 是否保存操作 */
    private String isSaveSign;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "LEASE_ID")
    public String getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }
    @Column(name = "LEASE_CODE")
    public String getLeaseCode() {
        return leaseCode;
    }
    public void setLeaseCode(String leaseCode) {
        this.leaseCode = leaseCode;
    }
    @Column(name = "REPAY_BEGIN_DATE")
    public Date getRepayBeginDate() {
        return repayBeginDate;
    }
    public void setRepayBeginDate(Date repayBeginDate) {
        this.repayBeginDate = repayBeginDate;
    }
    @Column(name = "REPAY_CYCEL")
    public String getRepayCycel() {
        return repayCycel;
    }
    public void setRepayCycel(String repayCycel) {
        this.repayCycel = repayCycel;
    }
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    @Column(name = "BEGIN_DATE")
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "RATE")
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    @Column(name = "REPAY_CYCLE")
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    @Column(name = "PAY_WAY")
    public String getPayWay() {
        return payWay;
    }
    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
    @Column(name = "PAY_MONEY")
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "IS_SAVESIGN")
    public String getIsSaveSign() {
        return isSaveSign;
    }
    public void setIsSaveSign(String isSaveSign) {
        this.isSaveSign = isSaveSign;
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
