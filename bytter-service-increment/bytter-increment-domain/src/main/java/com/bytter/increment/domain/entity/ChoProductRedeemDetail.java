package com.bytter.increment.domain.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 赎回明细表
 */
@Entity
@Table(name = "CHO_PRODUCT_REDEEM_DETAIL", schema = "")
public class ChoProductRedeemDetail extends IdEntity implements java.io.Serializable {
    /**产品登记单号*/
    private String reqId;
    /**赎回Id*/
    private String cprId;
    /**赎回金额*/
    private BigDecimal redeemMoney;
    /**赎回日期*/
    private Date redeemDate;
    /**到账日期*/
    private Date transferDate;
    /**收益金额*/
    private BigDecimal incomeMoney;
    /**实际收益率*/
    private BigDecimal incomeRate;
    /**备注*/
    private String rmk;
    /**申购费*/
    private BigDecimal subFeeMoney;
    /**赎回费*/
    private BigDecimal redeemChargeMoney;
    /**其他手续费*/
    private BigDecimal chargeMoney;
    /**银行流水认领组件挂接确认*/
    private String collectionConfirm;

    /**
     * 赎回日净值
     */
    private BigDecimal redeemDailyNetVal;

    /**
     * 赎回份额
     */
    private BigDecimal redeemPortion;


    @Column(name = "REDEEM_DAILY_NET_VAL")
    public BigDecimal getRedeemDailyNetVal() {
        return redeemDailyNetVal;
    }

    public void setRedeemDailyNetVal(BigDecimal redeemDailyNetVal) {
        this.redeemDailyNetVal = redeemDailyNetVal;
    }

    @Column(name = "REDEEM_PORTION")
    public BigDecimal getRedeemPortion() {
        return redeemPortion;
    }

    public void setRedeemPortion(BigDecimal redeemPortion) {
        this.redeemPortion = redeemPortion;
    }

    @Column(name ="COLLECTION_CONFIRM",nullable=false,length=1)
    public String getCollectionConfirm() {
        return collectionConfirm;
    }

    public void setCollectionConfirm(String collectionConfirm) {
        this.collectionConfirm = collectionConfirm;
    }

    @Column(name ="REQ_ID",length=32)
    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    @Column(name ="REDEEM_MONEY",scale = 2,length=15)
    public BigDecimal getRedeemMoney() {
        return redeemMoney;
    }

    public void setRedeemMoney(BigDecimal redeemMoney) {
        this.redeemMoney = redeemMoney;
    }

    @Column(name ="REDEEM_DATE")
    public Date getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(Date redeemDate) {
        this.redeemDate = redeemDate;
    }

    @Column(name ="TRANSFER_DATE")
    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    @Column(name ="INCOME_MONEY",scale = 2,length=15)
    public BigDecimal getIncomeMoney() {
        return incomeMoney;
    }

    public void setIncomeMoney(BigDecimal incomeMoney) {
        this.incomeMoney = incomeMoney;
    }

    @Column(name ="INCOME_RATE",scale = 6,length=8)
    public BigDecimal getIncomeRate() {
        return incomeRate;
    }

    public void setIncomeRate(BigDecimal incomeRate) {
        this.incomeRate = incomeRate;
    }

    @Column(name ="RMK",length=200)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="SUB_FEE_MONEY",scale = 2,length=15)
    public BigDecimal getSubFeeMoney() {
        return subFeeMoney;
    }

    public void setSubFeeMoney(BigDecimal subFeeMoney) {
        this.subFeeMoney = subFeeMoney;
    }

    @Column(name ="REDEEM_CHARGE_MONEY",scale = 2,length=15)
    public BigDecimal getRedeemChargeMoney() {
        return redeemChargeMoney;
    }

    public void setRedeemChargeMoney(BigDecimal redeemChargeMoney) {
        this.redeemChargeMoney = redeemChargeMoney;
    }

    @Column(name ="CHARGE_MONEY",scale = 2,length=15)
    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    @Column(name ="CPR_ID")
    public String getCprId() {
        return cprId;
    }

    public void setCprId(String cprId) {
        this.cprId = cprId;
    }
}
