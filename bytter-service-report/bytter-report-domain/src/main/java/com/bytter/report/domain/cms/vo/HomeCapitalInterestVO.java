package com.bytter.report.domain.cms.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2023/10/27<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class HomeCapitalInterestVO implements Serializable {
    /**融资品种*/
    private String finType;
    /**单位*/
    private String corpId;
    /**币种*/
    private String curId;
    /**开始日期*/
    private String startDate;
    /**结束日期*/
    private String endDate;
    /**金额单位*/
    private BigDecimal moneyType;
    /**月份*/
    private String month;
    /**应付本金*/
    private BigDecimal reMoney;
    /**应付利息*/
    private BigDecimal reInterest;

    public String getFinType() {
        return finType;
    }

    public void setFinType(String finType) {
        this.finType = finType;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(BigDecimal moneyType) {
        this.moneyType = moneyType;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getReMoney() {
        return reMoney;
    }

    public void setReMoney(BigDecimal reMoney) {
        this.reMoney = reMoney;
    }

    public BigDecimal getReInterest() {
        return reInterest;
    }

    public void setReInterest(BigDecimal reInterest) {
        this.reInterest = reInterest;
    }
}
