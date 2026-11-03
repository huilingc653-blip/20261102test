package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外汇牌价表
 * @author huangx
 * @date 2020-07-16
 *
 */
@Entity
@Table(name = "FEX_RATE_INFO")
public class FexRateInfo extends IdEntity implements Serializable {
    /**外汇牌价查询指令*/
    private String rateExcId;
    /**外汇牌价类型*/
    private String rateTypeId;
    /**标准币种*/
    private String cur;
    /**报价币种*/
    private String referCur;
    /**汇买价*/
    private BigDecimal purchaseRate;
    /**汇卖价*/
    private BigDecimal sellRate;
    /**中间价*/
    private BigDecimal middleRate;
    /**汇率日期*/
    private Date exchangeRateDate;
    /**期限*/
    private String term;
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
    /**发布时间*/
    private Date effectDate;
    /**报价金额*/
    private String curUnit;

    @Column(name ="RATE_EXC_ID")
    public String getRateExcId() {
        return rateExcId;
    }

    public void setRateExcId(String rateExcId) {
        this.rateExcId = rateExcId;
    }

    @Column(name ="RATE_TYPE_ID")
    public String getRateTypeId() {
        return rateTypeId;
    }

    public void setRateTypeId(String rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    @Column(name ="CUR")
    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    @Column(name ="REFER_CUR")
    public String getReferCur() {
        return referCur;
    }

    public void setReferCur(String referCur) {
        this.referCur = referCur;
    }

    @Column(name ="PURCHASE_RATE")
    public BigDecimal getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(BigDecimal purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    @Column(name ="SELL_RATE")
    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    @Column(name ="MIDDLE_RATE")
    public BigDecimal getMiddleRate() {
        return middleRate;
    }

    public void setMiddleRate(BigDecimal middleRate) {
        this.middleRate = middleRate;
    }

    @Column(name ="EXCHANGE_RATE_DATE")
    public Date getExchangeRateDate() {
        return exchangeRateDate;
    }

    public void setExchangeRateDate(Date exchangeRateDate) {
        this.exchangeRateDate = exchangeRateDate;
    }

    @Column(name ="TERM")
    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Column(name ="RMK")
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
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

    @Column(name ="EFFECT_DATE")
    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    @Column(name ="CUR_UNIT")
    public String getCurUnit() {
        return curUnit;
    }

    public void setCurUnit(String curUnit) {
        this.curUnit = curUnit;
    }
}
