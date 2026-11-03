package com.bytter.biz.budget.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author huangx
 * @Description 销售回款分析表
 * @date 2019-3-26
 */
@Entity
@Table(name = "FBS_SALESRETURN_ANALYSE", schema = "")
public class FbsSalesReturnAnalyse extends IdEntity implements java.io.Serializable {
    /**单位ID*/
    private String corpId;
    /**计划开始时间*/
    private Date beginDate;
    /**客商名称*/
    private String oppAccName;
    /**计划现金回款*/
    private BigDecimal planCashMoney;
    /**计划票据回款*/
    private BigDecimal planBillMoney;
    /**实际现金回款*/
    private BigDecimal factCashMoney;
    /**实际票据回款*/
    private BigDecimal factBillMoney;
    /**差异原因*/
    private String diffReason;
    /**状态*/
    private Integer analyseStatus;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    @Column(name ="CORP_ID",nullable=false,length=32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="BEGIN_DATE",nullable=false)
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    @Column(name ="OPP_ACC_NAME",nullable=true,length=100)
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    @Column(name ="PLAN_CASH_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getPlanCashMoney() {
        return planCashMoney;
    }
    public void setPlanCashMoney(BigDecimal planCashMoney) {
        this.planCashMoney = planCashMoney;
    }
    @Column(name ="PLAN_BILL_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getPlanBillMoney() {
        return planBillMoney;
    }
    public void setPlanBillMoney(BigDecimal planBillMoney) {
        this.planBillMoney = planBillMoney;
    }
    @Column(name ="FACT_CASH_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getFactCashMoney() {
        return factCashMoney;
    }
    public void setFactCashMoney(BigDecimal factCashMoney) {
        this.factCashMoney = factCashMoney;
    }
    @Column(name ="FACT_BILL_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getFactBillMoney() {
        return factBillMoney;
    }
    public void setFactBillMoney(BigDecimal factBillMoney) {
        this.factBillMoney = factBillMoney;
    }
    @Column(name ="DIFF_REASON",nullable=true,length=100)
    public String getDiffReason() {
        return diffReason;
    }
    public void setDiffReason(String diffReason) {
        this.diffReason = diffReason;
    }
    @Column(name ="ANALYSE_STATUS",nullable=true,length=10)
    public Integer getAnalyseStatus() {
        return analyseStatus;
    }
    public void setAnalyseStatus(Integer analyseStatus) {
        this.analyseStatus = analyseStatus;
    }
    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
