package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能介绍 : 损益信息<br>
 * <b>日期：</b>2025/9/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Entity
@Table(name = "FEX_PROFIT_LOSS_MANAGE_DTL")
public class FexProfitLossManageDtl extends IdEntity implements Serializable {
    /**损益id*/
    private String fexId;
    /**交易表id*/
    private String reqId;
    /**生命周期id*/
    private String lifeCycleId;
    /**生命周期id*/
    private String lifeCycleBillNo;
    /**操作类型*/
    private String opType;
    /**序号*/
    private Integer orderNo;
    /**交割日期、操作日*/
    private Date deliverlyDate;
    /**参考汇率*/
    private BigDecimal referRate;
    /**合同汇率*/
    private BigDecimal contractRate;
    /**浮动损益（已实现损益）*/
    private BigDecimal money;
    /**折本位币浮动损益（已实现损益）*/
    private BigDecimal convertMoney;
    /**创建人*/
    private String createBy;
    /**浮动损益*/
    private Date createDate;
    /**折本位币浮动损益*/
    private String updateBy;
    /**已实现损益*/
    private Date updateDate;

    @Column(name ="LIFE_CYCLE_BILL_NO")
    public String getLifeCycleBillNo() {
        return lifeCycleBillNo;
    }

    public void setLifeCycleBillNo(String lifeCycleBillNo) {
        this.lifeCycleBillNo = lifeCycleBillNo;
    }

    @Column(name ="REQ_ID")
    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    /**折本位币已实现损益*/


    @Column(name ="FEX_ID")
    public String getFexId() {
        return fexId;
    }

    public void setFexId(String fexId) {
        this.fexId = fexId;
    }

    @Column(name ="LIFE_CYCLE_ID")
    public String getLifeCycleId() {
        return lifeCycleId;
    }

    public void setLifeCycleId(String lifeCycleId) {
        this.lifeCycleId = lifeCycleId;
    }

    @Column(name ="OP_TYPE")
    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    @Column(name ="ORDER_NO")
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name ="DELIVERLY_DATE")
    public Date getDeliverlyDate() {
        return deliverlyDate;
    }

    public void setDeliverlyDate(Date deliverlyDate) {
        this.deliverlyDate = deliverlyDate;
    }

    @Column(name ="REFER_RATE")
    public BigDecimal getReferRate() {
        return referRate;
    }

    public void setReferRate(BigDecimal referRate) {
        this.referRate = referRate;
    }

    @Column(name ="CONTRACT_RATE")
    public BigDecimal getContractRate() {
        return contractRate;
    }

    public void setContractRate(BigDecimal contractRate) {
        this.contractRate = contractRate;
    }

    @Column(name ="MONEY")
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Column(name ="CONVERT_MONEY")
    public BigDecimal getConvertMoney() {
        return convertMoney;
    }

    public void setConvertMoney(BigDecimal convertMoney) {
        this.convertMoney = convertMoney;
    }

    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
