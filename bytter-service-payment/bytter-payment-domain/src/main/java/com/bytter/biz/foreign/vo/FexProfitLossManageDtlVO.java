package com.bytter.biz.foreign.vo;

import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能介绍 : 损益信息明细<br>
 * <b>日期：</b>2025/9/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class FexProfitLossManageDtlVO implements Serializable, IBaseDataInject {
    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFexId() {
        return fexId;
    }

    public void setFexId(String fexId) {
        this.fexId = fexId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getLifeCycleId() {
        return lifeCycleId;
    }

    public void setLifeCycleId(String lifeCycleId) {
        this.lifeCycleId = lifeCycleId;
    }

    public String getLifeCycleBillNo() {
        return lifeCycleBillNo;
    }

    public void setLifeCycleBillNo(String lifeCycleBillNo) {
        this.lifeCycleBillNo = lifeCycleBillNo;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Date getDeliverlyDate() {
        return deliverlyDate;
    }

    public void setDeliverlyDate(Date deliverlyDate) {
        this.deliverlyDate = deliverlyDate;
    }

    public BigDecimal getReferRate() {
        return referRate;
    }

    public void setReferRate(BigDecimal referRate) {
        this.referRate = referRate;
    }

    public BigDecimal getContractRate() {
        return contractRate;
    }

    public void setContractRate(BigDecimal contractRate) {
        this.contractRate = contractRate;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getConvertMoney() {
        return convertMoney;
    }

    public void setConvertMoney(BigDecimal convertMoney) {
        this.convertMoney = convertMoney;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
