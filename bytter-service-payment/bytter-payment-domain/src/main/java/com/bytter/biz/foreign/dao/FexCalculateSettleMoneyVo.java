package com.bytter.biz.foreign.dao;



import java.math.BigDecimal;

public class FexCalculateSettleMoneyVo {

    private String tranCurPairIdName;

    /**
     * 交易货币对标准币种
     */
    private String tranCurPairBaseId;


    /**
     * 报价方式
     */
    private String quotationMethod;


    /**买入金额*/
    private BigDecimal buyMoney;
    /**卖出金额*/
    private BigDecimal sellMoney;

    private String buyCurId;

    private String sellCurId;

    /**
     * 结算币种
     */
    private String settleCurId;


    /**合同汇率*/
    private BigDecimal contractRate;
    /**计算汇率*/
    private BigDecimal rate;


    /**买卖方向*/
    private String busDirection;



    /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    private String tranType;
    /**
     *  涨跌方向 0-看涨 1-看跌  ForeignConstant.FEX_TRAN_DIRECTION_UP
     */
    private String updownDirection;

    public FexCalculateSettleMoneyVo() {}

    public FexCalculateSettleMoneyVo(String tranCurPairIdName, String tranCurPairBaseId, String quotationMethod,
                                     BigDecimal buyMoney, BigDecimal sellMoney, String buyCurId, String sellCurId,
                                     BigDecimal contractRate, BigDecimal rate, String busDirection, String settleCurId) {
        this.tranCurPairIdName = tranCurPairIdName;
        this.tranCurPairBaseId = tranCurPairBaseId;
        this.quotationMethod = quotationMethod;
        this.buyMoney = buyMoney;
        this.sellMoney = sellMoney;
        this.buyCurId = buyCurId;
        this.sellCurId = sellCurId;
        this.contractRate = contractRate;
        this.rate = rate;
        this.busDirection = busDirection;
        this.settleCurId = settleCurId;
    }

    public String getTranCurPairIdName() {
        return tranCurPairIdName;
    }

    public void setTranCurPairIdName(String tranCurPairIdName) {
        this.tranCurPairIdName = tranCurPairIdName;
    }

    public String getTranCurPairBaseId() {
        return tranCurPairBaseId;
    }

    public void setTranCurPairBaseId(String tranCurPairBaseId) {
        this.tranCurPairBaseId = tranCurPairBaseId;
    }

    public String getQuotationMethod() {
        return quotationMethod;
    }

    public void setQuotationMethod(String quotationMethod) {
        this.quotationMethod = quotationMethod;
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

    public BigDecimal getContractRate() {
        return contractRate;
    }

    public void setContractRate(BigDecimal contractRate) {
        this.contractRate = contractRate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getBusDirection() {
        return busDirection;
    }

    public void setBusDirection(String busDirection) {
        this.busDirection = busDirection;
    }

    public String getSettleCurId() {
        return settleCurId;
    }

    public void setSettleCurId(String settleCurId) {
        this.settleCurId = settleCurId;
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
}
