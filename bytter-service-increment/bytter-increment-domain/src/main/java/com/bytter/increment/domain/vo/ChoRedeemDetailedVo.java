package com.bytter.increment.domain.vo;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: VO
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
public class ChoRedeemDetailedVo implements java.io.Serializable {
    /**赎回金额*/
    private BigDecimal redeemedMoney;
    /**赎回日期*/
    private Date  redeemedDate;
    /**赎回的理财天数*/
    private Integer redeemedDay;
    /**赎回理财权数*/
    private BigDecimal redeemedWeight;
    /**应收利息*/
    private BigDecimal redeemedReferIncomeMoney;
    /**赎回实收利息(手工)*/
    private BigDecimal redeemedIncomeMoney;
    /**赎回加权平均投资金额*/
    private BigDecimal redeemedWeightAverageInvest;
    /**赎回年化收益率(%)*/
    private BigDecimal redeemedAnnualizedReturn;
    /**当期已赎回理财天数*/
    private Integer currentDay;
    /**当期已赎回理财权数*/
    private BigDecimal currentWeight;
    /**当期已赎回利息*/
    private BigDecimal currentIncomeMoney;
    /**当期已赎回加权平均投资金额*/
    private BigDecimal currentRedeemedWeightAverageInvest;
    public BigDecimal getRedeemedMoney() {
        return redeemedMoney;
    }
    public void setRedeemedMoney(BigDecimal redeemedMoney) {
        this.redeemedMoney = redeemedMoney;
    }
    public Date getRedeemedDate() {
        return redeemedDate;
    }
    public void setRedeemedDate(Date redeemedDate) {
        this.redeemedDate = redeemedDate;
    }
    public Integer getRedeemedDay() {
        return redeemedDay;
    }
    public void setRedeemedDay(Integer redeemedDay) {
        this.redeemedDay = redeemedDay;
    }
    public BigDecimal getRedeemedWeight() {
        return redeemedWeight;
    }
    public void setRedeemedWeight(BigDecimal redeemedWeight) {
        this.redeemedWeight = redeemedWeight;
    }
    public BigDecimal getRedeemedReferIncomeMoney() {
        return redeemedReferIncomeMoney;
    }
    public void setRedeemedReferIncomeMoney(BigDecimal redeemedReferIncomeMoney) {
        this.redeemedReferIncomeMoney = redeemedReferIncomeMoney;
    }
    public BigDecimal getRedeemedIncomeMoney() {
        return redeemedIncomeMoney;
    }
    public void setRedeemedIncomeMoney(BigDecimal redeemedIncomeMoney) {
        this.redeemedIncomeMoney = redeemedIncomeMoney;
    }
    public BigDecimal getRedeemedWeightAverageInvest() {
        return redeemedWeightAverageInvest;
    }
    public void setRedeemedWeightAverageInvest(BigDecimal redeemedWeightAverageInvest) {
        this.redeemedWeightAverageInvest = redeemedWeightAverageInvest;
    }
    public BigDecimal getRedeemedAnnualizedReturn() {
        return redeemedAnnualizedReturn;
    }
    public void setRedeemedAnnualizedReturn(BigDecimal redeemedAnnualizedReturn) {
        this.redeemedAnnualizedReturn = redeemedAnnualizedReturn;
    }
    public Integer getCurrentDay() {
        return currentDay;
    }
    public void setCurrentDay(Integer currentDay) {
        this.currentDay = currentDay;
    }
    public BigDecimal getCurrentWeight() {
        return currentWeight;
    }
    public void setCurrentWeight(BigDecimal currentWeight) {
        this.currentWeight = currentWeight;
    }
    public BigDecimal getCurrentIncomeMoney() {
        return currentIncomeMoney;
    }
    public void setCurrentIncomeMoney(BigDecimal currentIncomeMoney) {
        this.currentIncomeMoney = currentIncomeMoney;
    }
    public BigDecimal getCurrentRedeemedWeightAverageInvest() {
        return currentRedeemedWeightAverageInvest;
    }
    public void setCurrentRedeemedWeightAverageInvest(BigDecimal currentRedeemedWeightAverageInvest) {
        this.currentRedeemedWeightAverageInvest = currentRedeemedWeightAverageInvest;
    }
}
