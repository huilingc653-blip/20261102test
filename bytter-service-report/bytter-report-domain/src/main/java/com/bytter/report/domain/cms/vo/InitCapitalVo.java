package com.bytter.report.domain.cms.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class InitCapitalVo implements Serializable {

    private List<String> xData;

    private List<BigDecimal> yData;

    private List<BigDecimal> y1Data;

    private BigDecimal sumReMoney;

    private BigDecimal sumReInterest;

    public BigDecimal getSumReInterest() {
        return sumReInterest;
    }

    public void setSumReInterest(BigDecimal sumReInterest) {
        this.sumReInterest = sumReInterest;
    }

    public BigDecimal getSumReMoney() {
        return sumReMoney;
    }

    public void setSumReMoney(BigDecimal sumReMoney) {
        this.sumReMoney = sumReMoney;
    }

    public List<String> getxData() {
        return xData;
    }

    public void setxData(List<String> xData) {
        this.xData = xData;
    }

    public List<BigDecimal> getyData() {
        return yData;
    }

    public void setyData(List<BigDecimal> yData) {
        this.yData = yData;
    }

    public List<BigDecimal> getY1Data() {
        return y1Data;
    }

    public void setY1Data(List<BigDecimal> y1Data) {
        this.y1Data = y1Data;
    }
}
