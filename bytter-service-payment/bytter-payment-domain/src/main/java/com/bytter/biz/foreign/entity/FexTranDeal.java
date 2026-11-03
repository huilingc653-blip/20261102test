package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 交易管理处理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_TRAN_DEAL", schema = "")
@SuppressWarnings("serial")
public class FexTranDeal extends IdEntity implements java.io.Serializable {
    /**交易ID*/
    private String tranReqId;
    /**单位*/
    private String corpId;
    /**交割方向  1-买入，2-卖出*/
    private String deliverySign;
    /**原结算日期*/
    private Date oldSettleDate;
    /**新结算日期*/
    private Date newSettleDate;
    /**结算金额*/
    private BigDecimal settleMoney;
    /**结算汇率*/
    private BigDecimal settleRate;
    /**即期汇率*/
    private BigDecimal wishRate;
    /**余额*/
    private BigDecimal bal;
    /**违约日期*/
    private Date defaultDate;
    /**币种*/
    private String curId;
    /**违约金*/
    private BigDecimal defaultMoney;
    /**交割汇率*/
    private BigDecimal deliveryRate;
    /**交割日期*/
    private Date deliveryDate;
    /**状态*/
    private Integer status;
    /**交易地点*/
    private String tranAddr;
    /**交易地点ID*/
    private String tranAddrId;
    /**备注*/
    private String rmk;
    /**操作类型 1-提前/展期交割，2-违约，3-NDF交割*/
    private String opType;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    @Column(name ="TRAN_REQ_ID",nullable=true)
    public String getTranReqId() {
        return tranReqId;
    }

    public void setTranReqId(String tranReqId) {
        this.tranReqId = tranReqId;
    }

    @Column(name ="CORP_ID",nullable=true)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="DELIVERY_SIGN",nullable=true)
    public String getDeliverySign() {
        return deliverySign;
    }

    public void setDeliverySign(String deliverySign) {
        this.deliverySign = deliverySign;
    }

    @Column(name ="OLD_SETTLE_DATE",nullable=true)
    public Date getOldSettleDate() {
        return oldSettleDate;
    }

    public void setOldSettleDate(Date oldSettleDate) {
        this.oldSettleDate = oldSettleDate;
    }

    @Column(name ="NEW_SETTLE_DATE",nullable=true)
    public Date getNewSettleDate() {
        return newSettleDate;
    }

    public void setNewSettleDate(Date newSettleDate) {
        this.newSettleDate = newSettleDate;
    }

    @Column(name ="SETTLE_MONEY",nullable=true)
    public BigDecimal getSettleMoney() {
        return settleMoney;
    }

    public void setSettleMoney(BigDecimal settleMoney) {
        this.settleMoney = settleMoney;
    }

    @Column(name ="SETTLE_RATE",nullable=true)
    public BigDecimal getSettleRate() {
        return settleRate;
    }

    public void setSettleRate(BigDecimal settleRate) {
        this.settleRate = settleRate;
    }

    @Column(name ="WISH_RATE",nullable=true)
    public BigDecimal getWishRate() {
        return wishRate;
    }

    public void setWishRate(BigDecimal wishRate) {
        this.wishRate = wishRate;
    }

    @Column(name ="BAL",nullable=true)
    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    @Column(name ="DEFAULT_DATE",nullable=true)
    public Date getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
    }

    @Column(name ="CUR_ID",nullable=true)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="DEFAULT_MONEY",nullable=true)
    public BigDecimal getDefaultMoney() {
        return defaultMoney;
    }

    public void setDefaultMoney(BigDecimal defaultMoney) {
        this.defaultMoney = defaultMoney;
    }

    @Column(name ="DELIVERY_RATE",nullable=true)
    public BigDecimal getDeliveryRate() {
        return deliveryRate;
    }

    public void setDeliveryRate(BigDecimal deliveryRate) {
        this.deliveryRate = deliveryRate;
    }

    @Column(name ="DELIVERY_DATE",nullable=true)
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Column(name ="STATUS",nullable=true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="TRAN_ADDR",nullable=true)
    public String getTranAddr() {
        return tranAddr;
    }

    public void setTranAddr(String tranAddr) {
        this.tranAddr = tranAddr;
    }

    @Column(name = "TRAN_ADDR_ID")
    public String getTranAddrId() {
        return tranAddrId;
    }

    public void setTranAddrId(String tranAddrId) {
        this.tranAddrId = tranAddrId;
    }

    @Column(name ="RMK",nullable=true)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="OP_TYPE",nullable=true)
    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    @Column(name ="CREATE_BY",nullable=true)
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

    @Column(name ="UPDATE_BY",nullable=true)
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
