package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.bpm.IBusinessKey;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title
 * @Description
 * @Author wanggl
 * @Date 2022-04-24
 * @Version 1.0
 */
public class CmsLeaseReportVO implements Serializable, IBusinessKey {
    private String id;

    private String leaseId;
    /**
     * 天数
     */
    private Long dates;

    /**
     * 期次
     */
    private Integer times;
    /**
     * 融资租赁类型
     */
    @BtCompositeSearch(fieldName = "i.LEASE_TYPE")
    private String leaseType;
    /**
     * 利率
     */
    private String rate;
    /**
     * 融资租赁合同编号
     */
    private String leaseCode;
    /**
     * 承租人
     */
    @BtCompositeSearch(fieldName = "i.corp_id")
    @Corp(inject = "corpName")
    private String corpId;

    private String corpName;
    /**
     * 出租人
     */
    @BtCompositeSearch(fieldName = "i.LESSOR")
    private String lessor;
    /**
     * 供货商
     */
    private String supplier;
    /**
     * 起租日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "i.BEGIN_DATE")
    private Date beginDate;
    /**
     * 到期日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 原币币种
     */
    @Currency(inject = "oriCurName")
    private String oriCurid;

    private String oriCurName;
    /**
     * 原币金额
     */
    private BigDecimal oriMoney;
    /**
     * 人名币金额
     */
    private BigDecimal rmbMoney;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 付租日期
     */
    private Date payDate;

    /**
     * 计划付租日
     */
    private Date planDate;

    /**
     * 本期租金
     */
    private BigDecimal thisMoney;

    /**
     * 租金本金
     */
    private BigDecimal leaseMoney;

    /**
     * 租赁利息
     */
    private BigDecimal leaseInterest;

    /**
     * 本期费用
     */
    private BigDecimal thisCost;

    /**
     * 剩余本金
     */
    private BigDecimal surplusMoney;

    /**
     * 保证金
     */
    private BigDecimal bailMoney;

    private Double pointProText;

    /**
     * 已还租金
     */
    private BigDecimal allThisMoney;
    /**
     * 已还本金
     */
    private BigDecimal allMoney;
    /**
     * 已还利息
     */
    private BigDecimal allInter;
    /**
     * 已还费用
     */
    private BigDecimal allCost;
    /**
     * 未还租金
     */
    private BigDecimal notThisMoney;
    /**
     * 未还本金
     */
    private BigDecimal notallMoney;
    /**
     * 未还利息
     */
    private BigDecimal notallInter;
    /**
     * 未还费用
     */
    private BigDecimal notallCost;

    /**
     * 是否已还
     */
    private String isRepay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }

    public String getLeaseType() {
        return leaseType;
    }

    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }

    public String getLeaseCode() {
        return leaseCode;
    }

    public void setLeaseCode(String leaseCode) {
        this.leaseCode = leaseCode;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getLessor() {
        return lessor;
    }

    public void setLessor(String lessor) {
        this.lessor = lessor;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOriCurid() {
        return oriCurid;
    }

    public String getIsRepay() {
        return isRepay;
    }

    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
    }

    public void setOriCurid(String oriCurid) {
        this.oriCurid = oriCurid;
    }

    public String getOriCurName() {
        return oriCurName;
    }

    public void setOriCurName(String oriCurName) {
        this.oriCurName = oriCurName;
    }

    public BigDecimal getOriMoney() {
        return oriMoney;
    }

    public void setOriMoney(BigDecimal oriMoney) {
        this.oriMoney = oriMoney;
    }

    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }

    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public BigDecimal getThisMoney() {
        return thisMoney;
    }

    public void setThisMoney(BigDecimal thisMoney) {
        this.thisMoney = thisMoney;
    }

    public BigDecimal getLeaseMoney() {
        return leaseMoney;
    }

    public void setLeaseMoney(BigDecimal leaseMoney) {
        this.leaseMoney = leaseMoney;
    }

    public BigDecimal getLeaseInterest() {
        return leaseInterest;
    }

    public void setLeaseInterest(BigDecimal leaseInterest) {
        this.leaseInterest = leaseInterest;
    }

    public BigDecimal getThisCost() {
        return thisCost;
    }

    public void setThisCost(BigDecimal thisCost) {
        this.thisCost = thisCost;
    }

    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }

    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }

    public Double getPointProText() {
        return pointProText;
    }

    public void setPointProText(Double pointProText) {
        this.pointProText = pointProText;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public BigDecimal getBailMoney() {
        return bailMoney;
    }

    public void setBailMoney(BigDecimal bailMoney) {
        this.bailMoney = bailMoney;
    }

    public BigDecimal getAllThisMoney() {
        return allThisMoney;
    }

    public void setAllThisMoney(BigDecimal allThisMoney) {
        this.allThisMoney = allThisMoney;
    }

    public BigDecimal getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(BigDecimal allMoney) {
        this.allMoney = allMoney;
    }

    public BigDecimal getAllInter() {
        return allInter;
    }

    public void setAllInter(BigDecimal allInter) {
        this.allInter = allInter;
    }

    public BigDecimal getAllCost() {
        return allCost;
    }

    public void setAllCost(BigDecimal allCost) {
        this.allCost = allCost;
    }

    public BigDecimal getNotThisMoney() {
        return notThisMoney;
    }

    public void setNotThisMoney(BigDecimal notThisMoney) {
        this.notThisMoney = notThisMoney;
    }

    public BigDecimal getNotallMoney() {
        return notallMoney;
    }

    public void setNotallMoney(BigDecimal notallMoney) {
        this.notallMoney = notallMoney;
    }

    public BigDecimal getNotallInter() {
        return notallInter;
    }

    public void setNotallInter(BigDecimal notallInter) {
        this.notallInter = notallInter;
    }

    public BigDecimal getNotallCost() {
        return notallCost;
    }

    public void setNotallCost(BigDecimal notallCost) {
        this.notallCost = notallCost;
    }

    public Long getDates() {
        return dates;
    }

    public void setDates(Long dates) {
        this.dates = dates;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
