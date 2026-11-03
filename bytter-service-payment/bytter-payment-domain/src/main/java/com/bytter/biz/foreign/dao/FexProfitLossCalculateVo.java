package com.bytter.biz.foreign.dao;

import java.math.BigDecimal;

public class FexProfitLossCalculateVo {

    /**
     * 交易货币对标准币种
     */
    private String tranCurPairBaseId;
    /**
     * 交易货币对报价币种
     */
    private String tranCurPairReferId;

    /**
     * 标准币种
     */
    private String quotationCurId;
    /**
     * 报价币种
     */
    private String quotationReferCurId;

    /**
     * 合同汇率
     */
    private BigDecimal contractRate;

    /**
     * 即期汇率
     */
    private BigDecimal spotRate;

    /**
     * 提前交割汇率
     */
    private BigDecimal earlySettlementRate;

    /**
     * 损益币种
     */
    private String profitLossCur;

    /**
     * 损益币种
     */
    private String settleCur;

    /**
     * 买入币种
     */
    private String buyCurId;

    /**
     * 卖出币种
     */
    private String sellCurId;

    /**
     * 买入金额
     */
    private BigDecimal buyMoney;

    /**
     * 卖出金额
     */
    private BigDecimal sellMoney;

    /**买卖方向*/
    private String busDirection;

    /**
     * 结算金额
     */
    private BigDecimal settleMoney;

    /**
     * 折本位币汇率
     */
    private BigDecimal convertRate;

    /**
     * 操作日记账汇率
     */
    private BigDecimal recordRate;

    /**
     * 损益金额
     */
    private BigDecimal profitLossMoney;

    /**
     * 默认外汇牌价类型
     */
    private String rateTypeId;

    private String corpId;
    // 是否计算折本位币
    private String isShowConvert;

    /**
     * 折本位币报价方式
     */
    private String convertCurType;

    /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    private String tranType;
    /**
     *  涨跌方向 0-看涨 1-看跌  ForeignConstant.FEX_TRAN_DIRECTION_UP
     */
    private String updownDirection;

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

    public BigDecimal getContractRate() {
        return contractRate;
    }

    public void setContractRate(BigDecimal contractRate) {
        this.contractRate = contractRate;
    }

    public BigDecimal getSpotRate() {
        return spotRate;
    }

    public void setSpotRate(BigDecimal spotRate) {
        this.spotRate = spotRate;
    }

    public String getProfitLossCur() {
        return profitLossCur;
    }

    public void setProfitLossCur(String profitLossCur) {
        this.profitLossCur = profitLossCur;
    }

    public String getSettleCur() {
        return settleCur;
    }

    public void setSettleCur(String settleCur) {
        this.settleCur = settleCur;
    }

    public String getBuyCurId() {
        return buyCurId;
    }

    public void setBuyCurId(String buyCurId) {
        this.buyCurId = buyCurId;
    }

    public String getSellCurId() {
        return sellCurId;
    }

    public void setSellCurId(String sellCurId) {
        this.sellCurId = sellCurId;
    }

    public BigDecimal getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }

    public BigDecimal getSellMoney() {
        return sellMoney;
    }

    public void setSellMoney(BigDecimal sellMoney) {
        this.sellMoney = sellMoney;
    }

    public String getBusDirection() {
        return busDirection;
    }

    public void setBusDirection(String busDirection) {
        this.busDirection = busDirection;
    }

    public BigDecimal getSettleMoney() {
        return settleMoney;
    }

    public void setSettleMoney(BigDecimal settleMoney) {
        this.settleMoney = settleMoney;
    }

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public BigDecimal getProfitLossMoney() {
        return profitLossMoney;
    }

    public void setProfitLossMoney(BigDecimal profitLossMoney) {
        this.profitLossMoney = profitLossMoney;
    }

    public BigDecimal getRecordRate() {
        return recordRate;
    }

    public void setRecordRate(BigDecimal recordRate) {
        this.recordRate = recordRate;
    }

    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getIsShowConvert() {
        return isShowConvert;
    }

    public void setIsShowConvert(String isShowConvert) {
        this.isShowConvert = isShowConvert;
    }

    public BigDecimal getEarlySettlementRate() {
        return earlySettlementRate;
    }

    public void setEarlySettlementRate(BigDecimal earlySettlementRate) {
        this.earlySettlementRate = earlySettlementRate;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getUpdownDirection() {
        return updownDirection;
    }

    public void setUpdownDirection(String updownDirection) {
        this.updownDirection = updownDirection;
    }


    public String getConvertCurType() {
        return convertCurType;
    }

    public void setConvertCurType(String convertCurType) {
        this.convertCurType = convertCurType;
    }
}
