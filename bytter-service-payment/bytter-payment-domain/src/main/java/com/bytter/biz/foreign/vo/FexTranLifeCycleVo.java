package com.bytter.biz.foreign.vo;


import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FexTranLifeCycleVo implements Serializable, IBaseDataInject, IBusinessKey {
    private String id;

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
    @BtCompositeSearch(fieldName = "ftlc.bill_no")
    private String billNo;
    /**
     * 交易编号
     */
    @BtCompositeSearch(fieldName = "r.bill_no")
    private String relBillNo;

    /**
     * 操作类型
     */
    @BtCompositeSearch(fieldName = "ftlc.op_type")
    private String opType;

    /**
     * 操作日期
     */
    @BtCompositeSearch(fieldName = "ftlc.op_date")
    private Date opDate;

    /**
     * 到期日期
     */
    private Date expiresDate;

    /**
     * 结算日期
     */
    @BtCompositeSearch(fieldName = "ftlc.settle_date")
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

    private String buyCurCode;

    /**
     * 卖出币种
     */
    private String sellCurId;

    private String sellCurCode;

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
    @BtCompositeSearch(fieldName = "ftlc.money")
    private BigDecimal money;

    /**
     * 损益币种
     */
    @BtCompositeSearch(fieldName = "ftlc.profit_loss_cur")
    private String profitLossCur;

    /**
     * 损益币种英文简码（展示用）
     */
    private String profitLossCurCode;

    /**
     * 净损益
     */
    @BtCompositeSearch(fieldName = "ftlc.profit_loss_money")
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
    @BtCompositeSearch(fieldName = "ftlc.convert_loss_money")
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
     * 平均汇率
     */
    private BigDecimal avgRate;
    /**
     * 持仓金额
     */
    private BigDecimal positionMoney;

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
    @BtCompositeSearch(fieldName = "ftlc.life_status")
    private String lifeStatus;
    /**
     * 是否退款
     */
    private String isRefund;


    private String corpCur;
    private String corpCurEn;

    public BigDecimal getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(BigDecimal avgRate) {
        this.avgRate = avgRate;
    }

    @BtCompositeSearch(fieldName = "t.tran_cur_pair_id")
    private String tranCurPairId;

    /**
     * 交易货币对名称
     */
    private String tranCurPairIdName;
    private String tranCurPairBaseId;
    private String tranCurPairReferId;

    /**
     * 报价方式
     */
    private String quotationMethod;
    private String quotationCurId;
    private String quotationReferCurId;


    /**
     * 操作金额
     */
    @BtCompositeSearch(fieldName = "ftlc.op_money")
    private BigDecimal opMoney;

    /**
     * 流水勾对状态
     */
    private String blendSign;

    /**
     * 买卖方向 1-买入，2-卖出
     */
    private String busDirection;

    /**交易日期*/
    private Date tranDate;


    /**交割类型 0-本金交割、1-无本金交割*/
    private String deliveryType;

    private String tranType;

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

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public Date getOpDate() {
        return opDate;
    }

    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }

    public String getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(String isRefund) {
        this.isRefund = isRefund;
    }

    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
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

    public BigDecimal getProcessRate() {
        return processRate;
    }

    public void setProcessRate(BigDecimal processRate) {
        this.processRate = processRate;
    }

    public String getTranSettleMethod() {
        return tranSettleMethod;
    }

    public void setTranSettleMethod(String tranSettleMethod) {
        this.tranSettleMethod = tranSettleMethod;
    }

    public String getIsPaymentAmount() {
        return isPaymentAmount;
    }

    public void setIsPaymentAmount(String isPaymentAmount) {
        this.isPaymentAmount = isPaymentAmount;
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

    public BigDecimal getBuyBal() {
        return buyBal;
    }

    public void setBuyBal(BigDecimal buyBal) {
        this.buyBal = buyBal;
    }

    public BigDecimal getSellBal() {
        return sellBal;
    }

    public void setSellBal(BigDecimal sellBal) {
        this.sellBal = sellBal;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getProfitLossCur() {
        return profitLossCur;
    }

    public void setProfitLossCur(String profitLossCur) {
        this.profitLossCur = profitLossCur;
    }

    public String getProfitLossCurCode() {
        return profitLossCurCode;
    }

    public void setProfitLossCurCode(String profitLossCurCode) {
        this.profitLossCurCode = profitLossCurCode;
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

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public BigDecimal getEarlyRate() {
        return earlyRate;
    }

    public void setEarlyRate(BigDecimal earlyRate) {
        this.earlyRate = earlyRate;
    }

    public String getEarlyType() {
        return earlyType;
    }

    public void setEarlyType(String earlyType) {
        this.earlyType = earlyType;
    }

    public BigDecimal getDeferredRate() {
        return deferredRate;
    }

    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }

    public String getDeferredType() {
        return deferredType;
    }

    public void setDeferredType(String deferredType) {
        this.deferredType = deferredType;
    }

    public Date getCloseSettleDate() {
        return closeSettleDate;
    }

    public void setCloseSettleDate(Date closeSettleDate) {
        this.closeSettleDate = closeSettleDate;
    }

    public BigDecimal getCloseRate() {
        return closeRate;
    }

    public void setCloseRate(BigDecimal closeRate) {
        this.closeRate = closeRate;
    }

    public BigDecimal getCloseForwardRate() {
        return closeForwardRate;
    }

    public void setCloseForwardRate(BigDecimal closeForwardRate) {
        this.closeForwardRate = closeForwardRate;
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

    public BigDecimal getConvertLossMoney() {
        return convertLossMoney;
    }

    public void setConvertLossMoney(BigDecimal convertLossMoney) {
        this.convertLossMoney = convertLossMoney;
    }

    public String getPayeeAcc() {
        return payeeAcc;
    }

    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }

    public String getPayerAcc() {
        return payerAcc;
    }

    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }

    public String getEarlySettlementType() {
        return earlySettlementType;
    }

    public void setEarlySettlementType(String earlySettlementType) {
        this.earlySettlementType = earlySettlementType;
    }

    public BigDecimal getEarlySettlementRate() {
        return earlySettlementRate;
    }

    public void setEarlySettlementRate(BigDecimal earlySettlementRate) {
        this.earlySettlementRate = earlySettlementRate;
    }

    public BigDecimal getCloseLossMoney() {
        return closeLossMoney;
    }

    public void setCloseLossMoney(BigDecimal closeLossMoney) {
        this.closeLossMoney = closeLossMoney;
    }

    public BigDecimal getDeliveryLossMoney() {
        return deliveryLossMoney;
    }

    public void setDeliveryLossMoney(BigDecimal deliveryLossMoney) {
        this.deliveryLossMoney = deliveryLossMoney;
    }

    public BigDecimal getAllLossMoney() {
        return allLossMoney;
    }

    public void setAllLossMoney(BigDecimal allLossMoney) {
        this.allLossMoney = allLossMoney;
    }

    public String getTranAddrId() {
        return tranAddrId;
    }

    public void setTranAddrId(String tranAddrId) {
        this.tranAddrId = tranAddrId;
    }

    public String getTranAddr() {
        return tranAddr;
    }

    public void setTranAddr(String tranAddr) {
        this.tranAddr = tranAddr;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
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

    public String getLifeStatus() {
        return lifeStatus;
    }

    public void setLifeStatus(String lifeStatus) {
        this.lifeStatus = lifeStatus;
    }

    public String getCorpCur() {
        return corpCur;
    }

    public void setCorpCur(String corpCur) {
        this.corpCur = corpCur;
    }

    public String getCorpCurEn() {
        return corpCurEn;
    }

    public void setCorpCurEn(String corpCurEn) {
        this.corpCurEn = corpCurEn;
    }

    public String getBuyCurCode() {
        return buyCurCode;
    }

    public void setBuyCurCode(String buyCurCode) {
        this.buyCurCode = buyCurCode;
    }

    public String getSellCurCode() {
        return sellCurCode;
    }

    public void setSellCurCode(String sellCurCode) {
        this.sellCurCode = sellCurCode;
    }

    public BigDecimal getRealBuyMoney() {
        return realBuyMoney;
    }

    public void setRealBuyMoney(BigDecimal realBuyMoney) {
        this.realBuyMoney = realBuyMoney;
    }

    public BigDecimal getRealSellMoney() {
        return realSellMoney;
    }

    public void setRealSellMoney(BigDecimal realSellMoney) {
        this.realSellMoney = realSellMoney;
    }

    public String getRelBillNo() {
        return relBillNo;
    }

    public void setRelBillNo(String relBillNo) {
        this.relBillNo = relBillNo;
    }

    public String getTranCurPairIdName() {
        return tranCurPairIdName;
    }

    public void setTranCurPairIdName(String tranCurPairIdName) {
        this.tranCurPairIdName = tranCurPairIdName;
    }

    public String getQuotationMethod() {
        return quotationMethod;
    }

    public void setQuotationMethod(String quotationMethod) {
        this.quotationMethod = quotationMethod;
    }

    public BigDecimal getOpMoney() {
        return opMoney;
    }

    public void setOpMoney(BigDecimal opMoney) {
        this.opMoney = opMoney;
    }

    public String getBlendSign() {
        return blendSign;
    }

    public void setBlendSign(String blendSign) {
        this.blendSign = blendSign;
    }

    public String getTranCurPairId() {
        return tranCurPairId;
    }

    public void setTranCurPairId(String tranCurPairId) {
        this.tranCurPairId = tranCurPairId;
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

    public String getBusDirection() {
        return busDirection;
    }

    public void setBusDirection(String busDirection) {
        this.busDirection = busDirection;
    }

    public BigDecimal getPositionMoney() {
        return positionMoney;
    }

    public void setPositionMoney(BigDecimal positionMoney) {
        this.positionMoney = positionMoney;
    }

    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }
}
