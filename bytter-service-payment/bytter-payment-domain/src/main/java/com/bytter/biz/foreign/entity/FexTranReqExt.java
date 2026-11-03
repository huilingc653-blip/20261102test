package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * @Title: Entity
 * @Description: 交易管理扩展表
 * @author wuxc
 * @date 2025-09-2
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_TRAN_REQ_EXT", schema = "")
public class FexTranReqExt extends IdEntity implements java.io.Serializable {

    /**
     * 交易id
     */
    private String reqId;
    /**
     * 交易货币对id
     */
    private String tranCurPairId;
    /**
     * 交易货币对标准币种
     */
    private String tranCurPairBaseId;
    /**
     * 交易货币对报价币种
     */
    private String tranCurPairReferId;

    /**
     * 金额是否自动计算
     */
    private String isPaymentAmount;
    /**
     * 交易汇率类型
     */
    private String tranRateType;
    /**
     * 报价方式
     */
    private String quotationMethod;
    /**
     * 标准币种
     */
    private String quotationCurId;
    /**
     * 报价币种
     */
    private String quotationReferCurId;
    /**
     * 持仓金额
     */
    private BigDecimal positionMoney;
    /**
     * 损益币种
     */
    private String profitLossCur;
    /**
     * 损益
     */
    private BigDecimal profitLossMoney;
    /**
     * 折本位币报价方式
     */
    private String convertCurType;
    /**
     * 折本位币标准币种
     */
    private String convertCurId;
    /**
     * 折本位币报价币种
     */
    private String convertCurReferId;
    /**
     * 折本位币汇率
     */
    private BigDecimal convertRate;
    /**
     * 折本位币损益、期权费金额
     */
    private BigDecimal convertMoney;

    /**
     * 生命周期状态
     */
    private String lifeCycleState;


    @Column(name ="REQ_ID",nullable=true,length=32)
    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    @Column(name ="TRAN_CUR_PAIR_ID",nullable=true,length=32)
    public String getTranCurPairId() {
        return tranCurPairId;
    }

    public void setTranCurPairId(String tranCurPairId) {
        this.tranCurPairId = tranCurPairId;
    }

    @Column(name ="CONVERT_CUR_ID",nullable=true,length=32)
    public String getConvertCurId() {
        return convertCurId;
    }

    public void setConvertCurId(String convertCurId) {
        this.convertCurId = convertCurId;
    }
    @Column(name ="CONVERT_CUR_REFER_ID",nullable=true,length=32)
    public String getConvertCurReferId() {
        return convertCurReferId;
    }

    public void setConvertCurReferId(String convertCurReferId) {
        this.convertCurReferId = convertCurReferId;
    }

    @Column(name ="IS_PAYMENT_AMOUNT",nullable=true,length=10)
    public String getIsPaymentAmount() {
        return isPaymentAmount;
    }

    public void setIsPaymentAmount(String isPaymentAmount) {
        this.isPaymentAmount = isPaymentAmount;
    }

    @Column(name ="TRAN_RATE_TYPE",nullable=true,length=10)
    public String getTranRateType() {
        return tranRateType;
    }

    public void setTranRateType(String tranRateType) {
        this.tranRateType = tranRateType;
    }

    @Column(name ="QUOTATION_METHOD",nullable=true,length=100)
    public String getQuotationMethod() {
        return quotationMethod;
    }

    public void setQuotationMethod(String quotationMethod) {
        this.quotationMethod = quotationMethod;
    }

    @Column(name ="QUOTATION_CUR_ID",nullable=true,length=32)
    public String getQuotationCurId() {
        return quotationCurId;
    }

    public void setQuotationCurId(String quotationCurId) {
        this.quotationCurId = quotationCurId;
    }

    @Column(name ="QUOTATION_REFER_CUR_ID",nullable=true,length=32)
    public String getQuotationReferCurId() {
        return quotationReferCurId;
    }

    public void setQuotationReferCurId(String quotationReferCurId) {
        this.quotationReferCurId = quotationReferCurId;
    }

    @Column(name ="POSITION_MONEY",nullable=true)
    public BigDecimal getPositionMoney() {
        return positionMoney;
    }

    public void setPositionMoney(BigDecimal positionMoney) {
        this.positionMoney = positionMoney;
    }

    @Column(name ="PROFIT_LOSS_CUR",nullable=true,length=32)
    public String getProfitLossCur() {
        return profitLossCur;
    }

    public void setProfitLossCur(String profitLossCur) {
        this.profitLossCur = profitLossCur;
    }

    @Column(name ="PROFIT_LOSS_MONEY",nullable=true)
    public BigDecimal getProfitLossMoney() {
        return profitLossMoney;
    }

    public void setProfitLossMoney(BigDecimal profitLossMoney) {
        this.profitLossMoney = profitLossMoney;
    }

    @Column(name ="CONVERT_CUR_TYPE",nullable=true,length=10)
    public String getConvertCurType() {
        return convertCurType;
    }

    public void setConvertCurType(String convertCurType) {
        this.convertCurType = convertCurType;
    }

    @Column(name ="CONVERT_RATE",nullable=true, precision = 20, scale = 8)
    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    @Column(name ="CONVERT_MONEY",nullable=true)
    public BigDecimal getConvertMoney() {
        return convertMoney;
    }

    public void setConvertMoney(BigDecimal convertMoney) {
        this.convertMoney = convertMoney;
    }

    @Column(name ="TRAN_CUR_PAIR_BASE_ID",nullable=true,length=32)
    public String getTranCurPairBaseId() {
        return tranCurPairBaseId;
    }

    public void setTranCurPairBaseId(String tranCurPairBaseId) {
        this.tranCurPairBaseId = tranCurPairBaseId;
    }

    @Column(name ="TRAN_CUR_PAIR_REFER_ID",nullable=true,length=32)
    public String getTranCurPairReferId() {
        return tranCurPairReferId;
    }

    public void setTranCurPairReferId(String tranCurPairReferId) {
        this.tranCurPairReferId = tranCurPairReferId;
    }

    @Column(name ="LIFE_CYCLE_STATE",nullable=true,length=20)
    public String getLifeCycleState() {
        return lifeCycleState;
    }

    public void setLifeCycleState(String lifeCycleState) {
        this.lifeCycleState = lifeCycleState;
    }
}
