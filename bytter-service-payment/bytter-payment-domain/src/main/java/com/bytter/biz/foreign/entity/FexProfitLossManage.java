package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@Entity
@Table(name = "FEX_PROFIT_LOSS_MANAGE")
public class FexProfitLossManage extends IdEntity implements Serializable {
    /**交易表id*/
    private String reqId;
    /**损益币种id*/
    private String curId;
    /**单位id*/
    private String corpId;
    /**生命周期id*/
    private String lifeCycle;
    /**报价日期*/
    private Date quotationDate;
    /**结算日期*/
    private Date settleDate;
    /**合同汇率*/
    private BigDecimal contractRate;
    /**估值利率*/
    private BigDecimal valuationRate;
    /**
     * 折本位币汇率
     */
    private BigDecimal convertRate;
    /**浮动损益*/
    private BigDecimal floatMoney;
    /**折本位币浮动损益*/
    private BigDecimal convertFloatMoney;
    /**浮动净损益*/
    private BigDecimal floatMoneyTotal;
    /**折本位币浮动净损益*/
    private BigDecimal convertFloatMoneyTotal;
    /**已实现损益*/
    private BigDecimal realMoneyTotal;
    /**折本位币已实现损益*/
    private BigDecimal convertRealMoneyTotal;
    /**已实现净损益合计*/
    private BigDecimal netRealMoneyTotal;
    /**折本位币已实现净损益合计*/
    private BigDecimal netConvertRealMoneyTotal;
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
    /**折本位币报价时间*/
    private Date convertDate;
    /**折本位币报价方式*/
    private String convertCurEnCode;
    /**
     * 报价方式
     */
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

    /**
     * 交易类型
     */
    private String tranType;

    @Column(name ="TRAN_TYPE")
    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    @Column(name ="NET_REAL_MONEY_TOTAL")
    public BigDecimal getNetRealMoneyTotal() {
        return netRealMoneyTotal;
    }

    public void setNetRealMoneyTotal(BigDecimal netRealMoneyTotal) {
        this.netRealMoneyTotal = netRealMoneyTotal;
    }

    @Column(name ="NET_CONVERT_REAL_MONEY_TOTAL")
    public BigDecimal getNetConvertRealMoneyTotal() {
        return netConvertRealMoneyTotal;
    }

    public void setNetConvertRealMoneyTotal(BigDecimal netConvertRealMoneyTotal) {
        this.netConvertRealMoneyTotal = netConvertRealMoneyTotal;
    }

    @Column(name ="FLOAT_MONEY_TOTAL")
    public BigDecimal getFloatMoneyTotal() {
        return floatMoneyTotal;
    }

    public void setFloatMoneyTotal(BigDecimal floatMoneyTotal) {
        this.floatMoneyTotal = floatMoneyTotal;
    }

    @Column(name ="CONVERT_FLOAT_MONEY_TOTAL")
    public BigDecimal getConvertFloatMoneyTotal() {
        return convertFloatMoneyTotal;
    }

    public void setConvertFloatMoneyTotal(BigDecimal convertFloatMoneyTotal) {
        this.convertFloatMoneyTotal = convertFloatMoneyTotal;
    }

    @Column(name ="QUOTATION_CUR_ID")
    public String getQuotationCurId() {
        return quotationCurId;
    }

    public void setQuotationCurId(String quotationCurId) {
        this.quotationCurId = quotationCurId;
    }

    @Column(name ="QUOTATION_REFER_CUR_ID")
    public String getQuotationReferCurId() {
        return quotationReferCurId;
    }

    public void setQuotationReferCurId(String quotationReferCurId) {
        this.quotationReferCurId = quotationReferCurId;
    }

    @Column(name ="CONVERT_CUR_ID")
    public String getConvertCurId() {
        return convertCurId;
    }

    public void setConvertCurId(String convertCurId) {
        this.convertCurId = convertCurId;
    }

    @Column(name ="CONVERT_CUR_REFER_ID")
    public String getConvertCurReferId() {
        return convertCurReferId;
    }

    public void setConvertCurReferId(String convertCurReferId) {
        this.convertCurReferId = convertCurReferId;
    }

    @Column(name ="QUOTATION_METHOD")
    public String getQuotationMethod() {
        return quotationMethod;
    }

    public void setQuotationMethod(String quotationMethod) {
        this.quotationMethod = quotationMethod;
    }

    @Column(name ="CONVERT_CUR_EN_CODE")
    public String getConvertCurEnCode() {
        return convertCurEnCode;
    }

    public void setConvertCurEnCode(String convertCurEnCode) {
        this.convertCurEnCode = convertCurEnCode;
    }

    @Column(name ="CONVERT_DATE")
    public Date getConvertDate() {
        return convertDate;
    }

    public void setConvertDate(Date convertDate) {
        this.convertDate = convertDate;
    }

    @Column(name ="REFERENCE_INTEREST_RATE")
    public BigDecimal getReferenceInterestRate() {
        return referenceInterestRate;
    }

    public void setReferenceInterestRate(BigDecimal referenceInterestRate) {
        this.referenceInterestRate = referenceInterestRate;
    }

    @Column(name ="REQ_ID")
    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="LIFE_CYCLE")
    public String getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    @Column(name ="QUOTATION_DATE")
    public Date getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(Date quotationDate) {
        this.quotationDate = quotationDate;
    }

    @Column(name ="SETTLE_DATE")
    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    @Column(name ="CONTRACT_RATE")
    public BigDecimal getContractRate() {
        return contractRate;
    }

    public void setContractRate(BigDecimal contractRate) {
        this.contractRate = contractRate;
    }

    @Column(name ="VALUATION_RATE")
    public BigDecimal getValuationRate() {
        return valuationRate;
    }

    public void setValuationRate(BigDecimal valuationRate) {
        this.valuationRate = valuationRate;
    }

    @Column(name ="CONVERT_RATE", precision = 20, scale = 8)
    public BigDecimal getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
    }

    @Column(name ="FLOAT_MONEY")
    public BigDecimal getFloatMoney() {
        return floatMoney;
    }

    public void setFloatMoney(BigDecimal floatMoney) {
        this.floatMoney = floatMoney;
    }

    @Column(name ="CONVERT_FLOAT_MONEY")
    public BigDecimal getConvertFloatMoney() {
        return convertFloatMoney;
    }

    public void setConvertFloatMoney(BigDecimal convertFloatMoney) {
        this.convertFloatMoney = convertFloatMoney;
    }

    @Column(name ="REAL_MONEY_TOTAL")
    public BigDecimal getRealMoneyTotal() {
        return realMoneyTotal;
    }

    public void setRealMoneyTotal(BigDecimal realMoneyTotal) {
        this.realMoneyTotal = realMoneyTotal;
    }

    @Column(name ="CONVERT_REAL_MONEY_TOTAL")
    public BigDecimal getConvertRealMoneyTotal() {
        return convertRealMoneyTotal;
    }

    public void setConvertRealMoneyTotal(BigDecimal convertRealMoneyTotal) {
        this.convertRealMoneyTotal = convertRealMoneyTotal;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
