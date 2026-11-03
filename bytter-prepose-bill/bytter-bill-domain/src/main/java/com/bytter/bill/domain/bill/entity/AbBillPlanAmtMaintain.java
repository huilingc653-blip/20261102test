package com.bytter.bill.domain.bill.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行计划开票金额维护表
 *
 * @author fangcf
 * @date 2025-06-30
 */
@Entity
@Table(name = "AB_BILL_PLAN_AMT_MAINTAIN", schema = "")
public class AbBillPlanAmtMaintain extends IdEntity implements java.io.Serializable {

    /**
     * 单位id
     */
    private String corpId;
    /**
     * 银行类型
     */
    private String bankTypeId;
    /**
     * 授信类型：敞口和低风险
     */
    private String creditType;
    /**
     * 周开始日
     */
    private Date weekStartDay;
    /**
     * 周结束日
     */
    private Date weekEndDay;
    /**
     * 本周计划开票金额
     */
    private BigDecimal currWeekBillMoney;
    /**
     * 下周计划开票金额
     */
    private BigDecimal nextWeekBillMoney;
    /**
     * 无
     */
    private String isDeleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name = "CREDIT_TYPE")
    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    @Column(name = "WEEK_START_DAY")
    public Date getWeekStartDay() {
        return weekStartDay;
    }

    public void setWeekStartDay(Date weekStartDay) {
        this.weekStartDay = weekStartDay;
    }

    @Column(name = "WEEK_END_DAY")
    public Date getWeekEndDay() {
        return weekEndDay;
    }

    public void setWeekEndDay(Date weekEndDay) {
        this.weekEndDay = weekEndDay;
    }

    @Column(name = "CURR_WEEK_BILL_MONEY")
    public BigDecimal getCurrWeekBillMoney() {
        return currWeekBillMoney;
    }

    public void setCurrWeekBillMoney(BigDecimal currWeekBillMoney) {
        this.currWeekBillMoney = currWeekBillMoney;
    }

    @Column(name = "NEXT_WEEK_BILL_MONEY")
    public BigDecimal getNextWeekBillMoney() {
        return nextWeekBillMoney;
    }

    public void setNextWeekBillMoney(BigDecimal nextWeekBillMoney) {
        this.nextWeekBillMoney = nextWeekBillMoney;
    }

    @Column(name = "IS_DELETED")
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
