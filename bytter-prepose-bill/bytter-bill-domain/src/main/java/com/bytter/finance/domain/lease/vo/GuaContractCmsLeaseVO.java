package com.bytter.finance.domain.lease.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GuaContractCmsLeaseVO implements Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 融资租赁单号
     */
    private String leaseNo;
    /**
     * 融资租赁合同编号
     */
    private String leaseCode;
    /**
     * 原币币种
     */
    @Currency(inject = "oriCurName")
    @BtCompositeSearch(fieldName = "linfo.ori_curid")
    private String oriCurid;

    private String oriCurName;

    /**
     * 原币金额
     */
    @BtCompositeSearch(fieldName = "linfo.ori_money")
    private BigDecimal oriMoney;
    /**
     * 人名币金额
     */

    private BigDecimal rmbMoney;
    /**
     * 出租人
     */
    private String lessor;

    /**
     * 起租日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**
     * 到期日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "gco.use_date")
    private Date useDate;

    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "gco.status")
    //@Dict(dictGroup = "Contract.UseStatus", inject = "statusName")
    private Integer status;
    private String statusName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeaseNo() {
        return leaseNo;
    }

    public void setLeaseNo(String leaseNo) {
        this.leaseNo = leaseNo;
    }

    public String getLeaseCode() {
        return leaseCode;
    }

    public void setLeaseCode(String leaseCode) {
        this.leaseCode = leaseCode;
    }

    public String getOriCurid() {
        return oriCurid;
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

    public String getLessor() {
        return lessor;
    }

    public void setLessor(String lessor) {
        this.lessor = lessor;
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

    public Date getUseDate() {
        return useDate;
    }

    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
