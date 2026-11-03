package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外汇期权交易处理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_TRAN_OPTION_DEAL", schema = "")
@SuppressWarnings("serial")
public class FexTranOptionDeal extends IdEntity implements java.io.Serializable {
    /**交易ID*/
    private String tranReqId;
    /**操作日期 根据操作类型对应：失效日期/平均汇率确认日期/执行日期/敲出日期/敲入日期*/
    private Date opDate;
    /**操作类型 1-失效，2-已确认均价，3-行权，4-敲出，5-敲入*/
    private String opType;
    /**市场汇率*/
    private BigDecimal marketRate;
    /**平均汇率*/
    private BigDecimal avgRate;
    /**结算方式 0-现金、1-即期交易*/
    private String tranSettleMethod;
    /**产品*/
    private String proId;
    /**单位*/
    private String corpId;
    /**交易对手*/
    private String cotpId;
    /**结算日期*/
    private Date settleDate;
    /**币种*/
    private String curId;
    /**结算金额*/
    private BigDecimal settleMoney;
    /**退款日期*/
    private Date refundDate;
    /**退款金额*/
    private BigDecimal refundMoney;
    /**交易地点*/
    private String tranAddr;
    /**交易地点ID*/
    private String tranAddrId;
    /**备注*/
    private String rmk;
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

    @Column(name ="OP_DATE")
    public Date getOpDate() {
        return opDate;
    }

    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }

    @Column(name ="OP_TYPE",nullable=true)
    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    @Column(name ="MARKET_RATE")
    public BigDecimal getMarketRate() {
        return marketRate;
    }

    public void setMarketRate(BigDecimal marketRate) {
        this.marketRate = marketRate;
    }

    @Column(name ="AVG_RATE")
    public BigDecimal getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(BigDecimal avgRate) {
        this.avgRate = avgRate;
    }

    @Column(name ="TRAN_SETTLE_METHOD")
    public String getTranSettleMethod() {
        return tranSettleMethod;
    }

    public void setTranSettleMethod(String tranSettleMethod) {
        this.tranSettleMethod = tranSettleMethod;
    }

    @Column(name ="PRO_ID")
    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    @Column(name ="CORP_ID",nullable=true)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="COTP_ID",nullable=true)
    public String getCotpId() {
        return cotpId;
    }

    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }

    @Column(name ="SETTLE_DATE")
    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="SETTLE_MONEY")
    public BigDecimal getSettleMoney() {
        return settleMoney;
    }

    public void setSettleMoney(BigDecimal settleMoney) {
        this.settleMoney = settleMoney;
    }

    @Column(name ="REFUND_DATE")
    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    @Column(name ="REFUND_MONEY")
    public BigDecimal getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
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
