package com.bytter.biz.foreign.vo;

import java.math.BigDecimal;

public class FexTranReqExtVo {

    private String id;

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

    private String profitLossCurName;
    private String profitLossCurEnglishCode;

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

    private String tranCurPairReferEnglishCode;

    private String tranCurPairBaseEnglishCode;

    private String quotationCurEnglishCode;

    private String quotationReferCurEnglishCode;

    private String convertCurEnglishCode;

    private String convertCurReferEnglishCode;

    public String getConvertCurEnglishCode() {
        return convertCurEnglishCode;
    }

    public void setConvertCurEnglishCode(String convertCurEnglishCode) {
        this.convertCurEnglishCode = convertCurEnglishCode;
    }

    public String getConvertCurReferEnglishCode() {
        return convertCurReferEnglishCode;
    }

    public void setConvertCurReferEnglishCode(String convertCurReferEnglishCode) {
        this.convertCurReferEnglishCode = convertCurReferEnglishCode;
    }

    public String getTranCurPairReferEnglishCode() {
        return tranCurPairReferEnglishCode;
    }

    public void setTranCurPairReferEnglishCode(String tranCurPairReferEnglishCode) {
        this.tranCurPairReferEnglishCode = tranCurPairReferEnglishCode;
    }

    public String getTranCurPairBaseEnglishCode() {
        return tranCurPairBaseEnglishCode;
    }

    public void setTranCurPairBaseEnglishCode(String tranCurPairBaseEnglishCode) {
        this.tranCurPairBaseEnglishCode = tranCurPairBaseEnglishCode;
    }

    public String getQuotationCurEnglishCode() {
        return quotationCurEnglishCode;
    }

    public void setQuotationCurEnglishCode(String quotationCurEnglishCode) {
        this.quotationCurEnglishCode = quotationCurEnglishCode;
    }

    public String getQuotationReferCurEnglishCode() {
        return quotationReferCurEnglishCode;
    }

    public void setQuotationReferCurEnglishCode(String quotationReferCurEnglishCode) {
        this.quotationReferCurEnglishCode = quotationReferCurEnglishCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getTranCurPairId() {
        return tranCurPairId;
    }

    public void setTranCurPairId(String tranCurPairId) {
        this.tranCurPairId = tranCurPairId;
    }

    public String getIsPaymentAmount() {
        return isPaymentAmount;
    }

    public void setIsPaymentAmount(String isPaymentAmount) {
        this.isPaymentAmount = isPaymentAmount;
    }

    public String getTranRateType() {
        return tranRateType;
    }

    public void setTranRateType(String tranRateType) {
        this.tranRateType = tranRateType;
    }

    public String getQuotationMethod() {
        return quotationMethod;
    }

    public void setQuotationMethod(String quotationMethod) {
        this.quotationMethod = quotationMethod;
    }

    public BigDecimal getPositionMoney() {
        return positionMoney;
    }

    public void setPositionMoney(BigDecimal positionMoney) {
        this.positionMoney = positionMoney;
    }

    public String getProfitLossCur() {
        return profitLossCur;
    }

    public void setProfitLossCur(String profitLossCur) {
        this.profitLossCur = profitLossCur;
    }

    public BigDecimal getProfitLossMoney() {
        return profitLossMoney;
    }

    public void setProfitLossMoney(BigDecimal profitLossMoney) {
        this.profitLossMoney = profitLossMoney;
    }

    public String getConvertCurType() {
        return convertCurType;
    }

    public void setConvertCurType(String convertCurType) {
        this.convertCurType = convertCurType;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public BigDecimal getConvertMoney() {
        return convertMoney;
    }

    public void setConvertMoney(BigDecimal convertMoney) {
        this.convertMoney = convertMoney;
    }

    public String getQuotationCurId() {
        return quotationCurId;
    }

    public void setQuotationCurId(String quotationCurId) {
        this.quotationCurId = quotationCurId;
    }

    public String getQuotationReferCurId() {
        return quotationReferCurId;
    }

    public void setQuotationReferCurId(String quotationReferCurId) {
        this.quotationReferCurId = quotationReferCurId;
    }

    public String getTranCurPairBaseId() {
        return tranCurPairBaseId;
    }

    public void setTranCurPairBaseId(String tranCurPairBaseId) {
        this.tranCurPairBaseId = tranCurPairBaseId;
    }

    public String getTranCurPairReferId() {
        return tranCurPairReferId;
    }

    public void setTranCurPairReferId(String tranCurPairReferId) {
        this.tranCurPairReferId = tranCurPairReferId;
    }

    public String getLifeCycleState() {
        return lifeCycleState;
    }

    public void setLifeCycleState(String lifeCycleState) {
        this.lifeCycleState = lifeCycleState;
    }

    public String getConvertCurId() {
        return convertCurId;
    }

    public void setConvertCurId(String convertCurId) {
        this.convertCurId = convertCurId;
    }

    public String getConvertCurReferId() {
        return convertCurReferId;
    }

    public void setConvertCurReferId(String convertCurReferId) {
        this.convertCurReferId = convertCurReferId;
    }

    public String getProfitLossCurName() {
        return profitLossCurName;
    }

    public void setProfitLossCurName(String profitLossCurName) {
        this.profitLossCurName = profitLossCurName;
    }

    public String getProfitLossCurEnglishCode() {
        return profitLossCurEnglishCode;
    }

    public void setProfitLossCurEnglishCode(String profitLossCurEnglishCode) {
        this.profitLossCurEnglishCode = profitLossCurEnglishCode;
    }
}
