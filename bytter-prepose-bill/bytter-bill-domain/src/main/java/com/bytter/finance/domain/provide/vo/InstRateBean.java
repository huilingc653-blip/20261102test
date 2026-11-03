package com.bytter.finance.domain.provide.vo;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
public class InstRateBean implements java.io.Serializable {
    /**
     * 发放单号
     */
    private String billCode;
    /**
     * 贷款周期
     */
    private String periodId;
    /**
     * 币别
     */
    private String curCode;
    /**
     * 基准利率类别
     */
    private String baseRateId;
    /**
     * 新利率
     */
    private BigDecimal newRate;
    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectDate;
    /**
     * 原利率
     */
    private BigDecimal oriRate;
    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public BigDecimal getNewRate() {
        return newRate;
    }
    public void setNewRate(BigDecimal newRate) {
        this.newRate = newRate;
    }
    public Date getEffectDate() {
        return effectDate;
    }
    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }
    public BigDecimal getOriRate() {
        return oriRate;
    }
    public void setOriRate(BigDecimal oriRate) {
        this.oriRate = oriRate;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getPeriodId() {
        if (periodId == null) {
            periodId = "";
        }
        return periodId;
    }
    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getBaseRateId() {
        return baseRateId;
    }
    public void setBaseRateId(String baseRateId) {
        this.baseRateId = baseRateId;
    }
}
