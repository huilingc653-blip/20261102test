package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.check.vo.IBaseDataInject;

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
public class FexProfitLossManageVO  implements Serializable, IBaseDataInject {
    private String id;
    /**交易表id*/
    private String reqId;
    /**交易号码*/
    @BtCompositeSearch(fieldName = "t.bill_no")
    private String billNo;
    /**交易类型*/
    @BtCompositeSearch(fieldName = "f.tran_type")
    @Dict(dictGroup = "fex.tranApp.tranType", inject = "tranTypeName")
    private String tranType;
    private String tranTypeName;
    /**
     * 交易货币对id
     */
    @BtCompositeSearch(fieldName = "te.TRAN_CUR_PAIR_ID")
    private String tranCurPairId;
    private String tranCurPairName;
    /**买卖方向*/
    @BtCompositeSearch(fieldName = "t.BUS_DIRECTION")
    private String busDirection;
    /**涨跌方向*/
    @BtCompositeSearch(fieldName = "t.UPDOWN_DIRECTION")
    private String updownDirection;
    /**
     * 持仓金额
     */
    @BtCompositeSearch(fieldName = "te.position_money")
    private BigDecimal positionMoney;
    /**损益币种id*/
    @BtCompositeSearch(fieldName = "f.cur_id")
    private String curId;
    private String curEnName;
    /**单位id*/
    @BtCompositeSearch(fieldName = "f.corp_id")
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**生命周期id*/
    private String lifeCycle;
    /**报价日期*/
    @BtCompositeSearch(fieldName = "f.QUOTATION_DATE")
    private Date quotationDate;
    /**结算日期*/
    @BtCompositeSearch(fieldName = "f.SETTLE_DATE")
    private Date settleDate;
    /**合同汇率*/
    private BigDecimal contractRate;
    /**估值利率*/
    private BigDecimal valuationRate;
    /**浮动损益*/
    private BigDecimal floatMoney;
    /**折本位币浮动损益*/
    private BigDecimal convertFloatMoney;
    /**已实现损益*/
    private BigDecimal realMoneyTotal;
    /**折本位币已实现损益*/
    private BigDecimal convertRealMoneyTotal;
    /**状态*/
    private Integer status;
    /**创建人*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**修改人*/
    private String updateBy;
    /**创建日期*/
    private Date updateDate;
    /**参考利率*/
    private BigDecimal referenceInterestRate;
    /**折本位币汇率*/
    private BigDecimal convertRate;
    /**折本位币报价时间*/
    private Date convertDate;
    /**折本位币报价方式*/
    private String convertCurEnCode;
    /** 报价方式*/
    private String quotationMethod;
    /**
     * 报价方式标准币种
     */
    private String quotationCurId;
    /**
     * 报价方式标准币种
     */
    private String quotationReferCurId;
    /**
     * 折本位币标准币种
     */
    private String convertCurId;
    /**
     * 折本位币报价币种
     */
    private String convertCurReferId;

    /**浮动净损益*/
    private BigDecimal netFloatMoney;
    /**折本位币浮动净损益**/
    private BigDecimal netConvertFloatMoney;
    /**已实现净损益**/
    private BigDecimal netRealMoneyTotal;
    /**折本位币已实现净损益**/
    private BigDecimal netConvertRealMoneyTotal;

    /**浮动净损益**/
    private BigDecimal premiumMoney;

    /**折本位币浮动净损益**/
    private BigDecimal convertMoney;

    public BigDecimal getPremiumMoney() {
        return premiumMoney;
    }

    public void setPremiumMoney(BigDecimal premiumMoney) {
        this.premiumMoney = premiumMoney;
    }

    public BigDecimal getConvertMoney() {
        return convertMoney;
    }

    public void setConvertMoney(BigDecimal convertMoney) {
        this.convertMoney = convertMoney;
    }

    public BigDecimal getNetFloatMoney() {
        return netFloatMoney;
    }

    public void setNetFloatMoney(BigDecimal netFloatMoney) {
        this.netFloatMoney = netFloatMoney;
    }

    public BigDecimal getNetConvertFloatMoney() {
        return netConvertFloatMoney;
    }

    public void setNetConvertFloatMoney(BigDecimal netConvertFloatMoney) {
        this.netConvertFloatMoney = netConvertFloatMoney;
    }

    public BigDecimal getNetRealMoneyTotal() {
        return netRealMoneyTotal;
    }

    public void setNetRealMoneyTotal(BigDecimal netRealMoneyTotal) {
        this.netRealMoneyTotal = netRealMoneyTotal;
    }

    public BigDecimal getNetConvertRealMoneyTotal() {
        return netConvertRealMoneyTotal;
    }

    public void setNetConvertRealMoneyTotal(BigDecimal netConvertRealMoneyTotal) {
        this.netConvertRealMoneyTotal = netConvertRealMoneyTotal;
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

    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    public Date getConvertDate() {
        return convertDate;
    }

    public void setConvertDate(Date convertDate) {
        this.convertDate = convertDate;
    }

    public String getConvertCurEnCode() {
        return convertCurEnCode;
    }

    public void setConvertCurEnCode(String convertCurEnCode) {
        this.convertCurEnCode = convertCurEnCode;
    }

    public String getQuotationMethod() {
        return quotationMethod;
    }

    public void setQuotationMethod(String quotationMethod) {
        this.quotationMethod = quotationMethod;
    }

    public BigDecimal getReferenceInterestRate() {
        return referenceInterestRate;
    }

    public void setReferenceInterestRate(BigDecimal referenceInterestRate) {
        this.referenceInterestRate = referenceInterestRate;
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

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public Date getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(Date quotationDate) {
        this.quotationDate = quotationDate;
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

    public BigDecimal getValuationRate() {
        return valuationRate;
    }

    public void setValuationRate(BigDecimal valuationRate) {
        this.valuationRate = valuationRate;
    }

    public BigDecimal getFloatMoney() {
        return floatMoney;
    }

    public void setFloatMoney(BigDecimal floatMoney) {
        this.floatMoney = floatMoney;
    }

    public BigDecimal getConvertFloatMoney() {
        return convertFloatMoney;
    }

    public void setConvertFloatMoney(BigDecimal convertFloatMoney) {
        this.convertFloatMoney = convertFloatMoney;
    }

    public BigDecimal getRealMoneyTotal() {
        return realMoneyTotal;
    }

    public void setRealMoneyTotal(BigDecimal realMoneyTotal) {
        this.realMoneyTotal = realMoneyTotal;
    }

    public BigDecimal getConvertRealMoneyTotal() {
        return convertRealMoneyTotal;
    }

    public void setConvertRealMoneyTotal(BigDecimal convertRealMoneyTotal) {
        this.convertRealMoneyTotal = convertRealMoneyTotal;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getTranTypeName() {
        return tranTypeName;
    }

    public void setTranTypeName(String tranTypeName) {
        this.tranTypeName = tranTypeName;
    }

    public String getTranCurPairId() {
        return tranCurPairId;
    }

    public void setTranCurPairId(String tranCurPairId) {
        this.tranCurPairId = tranCurPairId;
    }

    public String getTranCurPairName() {
        return tranCurPairName;
    }

    public void setTranCurPairName(String tranCurPairName) {
        this.tranCurPairName = tranCurPairName;
    }

    public String getBusDirection() {
        return busDirection;
    }

    public void setBusDirection(String busDirection) {
        this.busDirection = busDirection;
    }

    public String getUpdownDirection() {
        return updownDirection;
    }

    public void setUpdownDirection(String updownDirection) {
        this.updownDirection = updownDirection;
    }

    public BigDecimal getPositionMoney() {
        return positionMoney;
    }

    public void setPositionMoney(BigDecimal positionMoney) {
        this.positionMoney = positionMoney;
    }

    public String getCurEnName() {
        return curEnName;
    }

    public void setCurEnName(String curEnName) {
        this.curEnName = curEnName;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
}
