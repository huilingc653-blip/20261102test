package com.bytter.biz.foreign.entity;


import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 生命周期表
 * @author wuxc
 * @date 2025-09-06
 * @version 1.0
 */
@Entity
@Table(name = "FEX_TRAN_LIFE_CYCLE", schema = "")
public class FexTranLifeCycle extends IdEntity implements java.io.Serializable {

    /**
     * 交易信息ID
     */
    private String reqId;

    /**
     * 单位
     */
    private String corpId;

    /**
     * 编号
     */
    private String billNo;

    /**
     * 操作类型
     */
    private String opType;

    /**
     * 操作日期
     */
    private Date opDate;

    /**
     * 到期日期
     */
    private Date expiresDate;

    /**
     * 结算日期
     */
    private Date settleDate;

    /**
     * 合同汇率
     */
    private BigDecimal contractRate;

    /**
     * 即期汇率
     */
    private BigDecimal spotRate;

    /**
     * 执行汇率
     */
    private BigDecimal processRate;

    /**
     * 结算方式（更名为交割方式：差额、全额）
     */
    private String tranSettleMethod;

    /**
     * 金额是否自动计算
     */
    private String isPaymentAmount;

    /**
     * 买入币种
     */
    private String buyCurId;

    /**
     * 卖出币种
     */
    private String sellCurId;

    /**
     * 操作金额
     */
    private BigDecimal opMoney;

    /**
     * 买入金额
     */
    private BigDecimal buyMoney;
    private BigDecimal realBuyMoney;

    /**
     * 卖出金额
     */
    private BigDecimal sellMoney;
    private BigDecimal realSellMoney;

    /**
     * 买入余额
     */
    private BigDecimal buyBal;

    /**
     * 卖出余额
     */
    private BigDecimal sellBal;

    /**
     * 损益、结算金额
     */
    private BigDecimal money;

    /**
     * 损益币种
     */
    private String profitLossCur;

    /**
     * 净损益
     */
    private BigDecimal profitLossMoney;

    /**
     * 操作日记账汇率
     */
    private BigDecimal recordRate;

    /**
     * 结算账号
     */
    private String bankAcc;

    /**
     * 结算币种
     */
    private String curId;

    /**
     * 提前汇率
     */
    private BigDecimal earlyRate;

    /**
     * 提前方式
     */
    private String earlyType;
    /**
     * 平均汇率
     */
    private BigDecimal avgRate;
    /**
     * 展期汇率
     */
    private BigDecimal deferredRate;

    /**
     * 展期方式
     */
    private String deferredType;

    /**
     * 平仓结算日期
     */
    private Date closeSettleDate;

    /**
     * 平仓汇率
     */
    private BigDecimal closeRate;

    /**
     * 平仓远期汇率
     */
    private BigDecimal closeForwardRate;

    /**
     * 折本位币报价方式
     */
    private String convertCurType;

    /**
     * 折本位币汇率
     */
    private BigDecimal convertRate;

    /**
     * 折本位币净损益
     */
    private BigDecimal convertLossMoney;

    /**
     * 收款账号
     */
    private String payeeAcc;

    /**
     * 付款账号
     */
    private String payerAcc;

    /**
     * 提前交割方式
     */
    private String earlySettlementType;

    /**
     * 提前交割汇率
     */
    private BigDecimal earlySettlementRate;

    /**
     * 平仓损益
     */
    private BigDecimal closeLossMoney;

    /**
     * 交割损益
     */
    private BigDecimal deliveryLossMoney;

    /**
     * 总损益
     */
    private BigDecimal allLossMoney;

    /**
     * 交易地点ID
     */
    private String tranAddrId;

    /**
     * 交易地点
     */
    private String tranAddr;

    /**
     * 备注
     */
    private String rmk;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 状态
     */
    private String lifeStatus;


    /**
     * 持仓金额
     */
    private BigDecimal positionMoney;


    @Column(name = "REQ_ID", nullable = true, length = 32)
    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    @Column(name = "CORP_ID", nullable = true, length = 32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "BILL_NO", nullable = true, length = 20)
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    @Column(name = "OP_TYPE", nullable = true, length = 10)
    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    @Column(name = "OP_DATE", nullable = true)
    public Date getOpDate() {
        return opDate;
    }

    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }

    @Column(name = "EXPIRES_DATE", nullable = true)
    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    @Column(name = "SETTLE_DATE", nullable = true)
    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    @Column(name = "CONTRACT_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getContractRate() {
        return contractRate;
    }

    public void setContractRate(BigDecimal contractRate) {
        this.contractRate = contractRate;
    }

    @Column(name = "SPOT_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getSpotRate() {
        return spotRate;
    }

    public void setSpotRate(BigDecimal spotRate) {
        this.spotRate = spotRate;
    }

    @Column(name = "PROCESS_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getProcessRate() {
        return processRate;
    }

    public void setProcessRate(BigDecimal processRate) {
        this.processRate = processRate;
    }

    @Column(name = "TRAN_SETTLE_METHOD", nullable = true, length = 1)
    public String getTranSettleMethod() {
        return tranSettleMethod;
    }

    public void setTranSettleMethod(String tranSettleMethod) {
        this.tranSettleMethod = tranSettleMethod;
    }

    @Column(name = "IS_PAYMENT_AMOUNT", nullable = true, length = 10)
    public String getIsPaymentAmount() {
        return isPaymentAmount;
    }

    public void setIsPaymentAmount(String isPaymentAmount) {
        this.isPaymentAmount = isPaymentAmount;
    }

    @Column(name = "BUY_CUR_ID", nullable = true, length = 32)
    public String getBuyCurId() {
        return buyCurId;
    }

    public void setBuyCurId(String buyCurId) {
        this.buyCurId = buyCurId;
    }

    @Column(name = "SELL_CUR_ID", nullable = true, length = 32)
    public String getSellCurId() {
        return sellCurId;
    }

    public void setSellCurId(String sellCurId) {
        this.sellCurId = sellCurId;
    }

    @Column(name = "OP_MONEY", nullable = true, precision = 18, scale = 6)
    public BigDecimal getOpMoney() {
        return opMoney;
    }

    public void setOpMoney(BigDecimal opMoney) {
        this.opMoney = opMoney;
    }

    @Column(name = "BUY_MONEY", nullable = true, precision = 18, scale = 6)
    public BigDecimal getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }

    @Column(name = "SELL_MONEY", nullable = true, precision = 18, scale = 6)
    public BigDecimal getSellMoney() {
        return sellMoney;
    }

    public void setSellMoney(BigDecimal sellMoney) {
        this.sellMoney = sellMoney;
    }

    @Column(name = "BUY_BAL", nullable = true, precision = 20, scale = 6)
    public BigDecimal getBuyBal() {
        return buyBal;
    }

    public void setBuyBal(BigDecimal buyBal) {
        this.buyBal = buyBal;
    }

    @Column(name = "SELL_BAL", nullable = true, precision = 20, scale = 6)
    public BigDecimal getSellBal() {
        return sellBal;
    }

    public void setSellBal(BigDecimal sellBal) {
        this.sellBal = sellBal;
    }

    @Column(name = "MONEY", nullable = true, precision = 20, scale = 6)
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Column(name = "PROFIT_LOSS_CUR", nullable = true, length = 32)
    public String getProfitLossCur() {
        return profitLossCur;
    }

    public void setProfitLossCur(String profitLossCur) {
        this.profitLossCur = profitLossCur;
    }

    @Column(name = "PROFIT_LOSS_MONEY", nullable = true, precision = 20, scale = 6)
    public BigDecimal getProfitLossMoney() {
        return profitLossMoney;
    }

    public void setProfitLossMoney(BigDecimal profitLossMoney) {
        this.profitLossMoney = profitLossMoney;
    }

    @Column(name = "RECORD_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getRecordRate() {
        return recordRate;
    }

    public void setRecordRate(BigDecimal recordRate) {
        this.recordRate = recordRate;
    }

    @Column(name = "BANK_ACC", nullable = true, length = 100)
    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    @Column(name = "CUR_ID", nullable = true, length = 32)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "EARLY_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getEarlyRate() {
        return earlyRate;
    }

    public void setEarlyRate(BigDecimal earlyRate) {
        this.earlyRate = earlyRate;
    }

    @Column(name = "EARLY_TYPE", nullable = true, length = 10)
    public String getEarlyType() {
        return earlyType;
    }

    public void setEarlyType(String earlyType) {
        this.earlyType = earlyType;
    }
    @Column(name = "AVG_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(BigDecimal avgRate) {
        this.avgRate = avgRate;
    }

    @Column(name = "DEFERRED_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }

    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }

    @Column(name = "DEFERRED_TYPE", nullable = true, length = 10)
    public String getDeferredType() {
        return deferredType;
    }

    public void setDeferredType(String deferredType) {
        this.deferredType = deferredType;
    }

    @Column(name = "CLOSE_SETTLE_DATE", nullable = true)
    public Date getCloseSettleDate() {
        return closeSettleDate;
    }

    public void setCloseSettleDate(Date closeSettleDate) {
        this.closeSettleDate = closeSettleDate;
    }

    @Column(name = "CLOSE_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getCloseRate() {
        return closeRate;
    }

    public void setCloseRate(BigDecimal closeRate) {
        this.closeRate = closeRate;
    }

    @Column(name = "CLOSE_FORWARD_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getCloseForwardRate() {
        return closeForwardRate;
    }

    public void setCloseForwardRate(BigDecimal closeForwardRate) {
        this.closeForwardRate = closeForwardRate;
    }

    @Column(name = "CONVERT_CUR_TYPE", nullable = true, length = 10)
    public String getConvertCurType() {
        return convertCurType;
    }

    public void setConvertCurType(String convertCurType) {
        this.convertCurType = convertCurType;
    }

    @Column(name = "CONVERT_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    @Column(name = "CONVERT_LOSS_MONEY", nullable = true, precision = 20, scale = 6)
    public BigDecimal getConvertLossMoney() {
        return convertLossMoney;
    }

    public void setConvertLossMoney(BigDecimal convertLossMoney) {
        this.convertLossMoney = convertLossMoney;
    }

    @Column(name = "PAYEE_ACC", nullable = true, length = 100)
    public String getPayeeAcc() {
        return payeeAcc;
    }

    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }

    @Column(name = "PAYER_ACC", nullable = true, length = 100)
    public String getPayerAcc() {
        return payerAcc;
    }

    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }

    @Column(name = "EARLY_SETTLEMENT_TYPE", nullable = true, length = 10)
    public String getEarlySettlementType() {
        return earlySettlementType;
    }

    public void setEarlySettlementType(String earlySettlementType) {
        this.earlySettlementType = earlySettlementType;
    }

    @Column(name = "EARLY_SETTLEMENT_RATE", nullable = true, precision = 20, scale = 8)
    public BigDecimal getEarlySettlementRate() {
        return earlySettlementRate;
    }

    public void setEarlySettlementRate(BigDecimal earlySettlementRate) {
        this.earlySettlementRate = earlySettlementRate;
    }

    @Column(name = "CLOSE_LOSS_MONEY", nullable = true, precision = 20, scale = 6)
    public BigDecimal getCloseLossMoney() {
        return closeLossMoney;
    }

    public void setCloseLossMoney(BigDecimal closeLossMoney) {
        this.closeLossMoney = closeLossMoney;
    }

    @Column(name = "DELIVERY_LOSS_MONEY", nullable = true, precision = 20, scale = 6)
    public BigDecimal getDeliveryLossMoney() {
        return deliveryLossMoney;
    }

    public void setDeliveryLossMoney(BigDecimal deliveryLossMoney) {
        this.deliveryLossMoney = deliveryLossMoney;
    }

    @Column(name = "ALL_LOSS_MONEY", nullable = true, precision = 20, scale = 6)
    public BigDecimal getAllLossMoney() {
        return allLossMoney;
    }

    public void setAllLossMoney(BigDecimal allLossMoney) {
        this.allLossMoney = allLossMoney;
    }

    @Column(name = "TRAN_ADDR_ID", nullable = true, length = 32)
    public String getTranAddrId() {
        return tranAddrId;
    }

    public void setTranAddrId(String tranAddrId) {
        this.tranAddrId = tranAddrId;
    }

    @Column(name = "TRAN_ADDR", nullable = true, length = 100)
    public String getTranAddr() {
        return tranAddr;
    }

    public void setTranAddr(String tranAddr) {
        this.tranAddr = tranAddr;
    }

    @Column(name = "RMK", nullable = true, length = 1000)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name = "CREATE_BY", nullable = true, length = 100)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_BY", nullable = true, length = 100)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "LIFE_STATUS", nullable = true, length = 50)
    public String getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(String lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    @Column(name = "REAL_BUY_MONEY", nullable = true, precision = 18, scale = 6)
    public BigDecimal getRealBuyMoney() {
        return realBuyMoney;
    }

    public void setRealBuyMoney(BigDecimal realBuyMoney) {
        this.realBuyMoney = realBuyMoney;
    }

    @Column(name = "REAL_SELL_MONEY", nullable = true, precision = 18, scale = 6)
    public BigDecimal getRealSellMoney() {
        return realSellMoney;
    }

    public void setRealSellMoney(BigDecimal realSellMoney) {
        this.realSellMoney = realSellMoney;
    }


    @Column(name ="POSITION_MONEY",nullable=true)
    public BigDecimal getPositionMoney() {
        return positionMoney;
    }

    public void setPositionMoney(BigDecimal positionMoney) {
        this.positionMoney = positionMoney;
    }
}

