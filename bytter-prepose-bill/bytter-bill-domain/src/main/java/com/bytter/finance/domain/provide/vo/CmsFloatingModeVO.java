package com.bytter.finance.domain.provide.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
@SuppressWarnings({ "serial" })
public class CmsFloatingModeVO implements Serializable {
    /** 发放id */
    private String id;
    /** 发放单号 */
    private String billCode;
    /** 启动日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enterDate;
    /** 浮动方式 */
    private String floatingMode;
    /** 浮动方式显示值 */
    private String floatingModeName;
    /** 浮动方式二级菜单 */
    private String floatModeNext;
    /** 当前日期 */
    private String toDayDate;
    /** 固定利率 */
    private String fixedRate;
    /** 基础利率 */
    private BigDecimal rate;
    /**
     * 浮动点数或百分比
     */
    private BigDecimal pointsText;
    /** 基准利率类型 */
    private String referenceRateType;
    /** 贷款周期 */
    private String repayCycle;
    /**
     * 利率报价日期
     */
    private Date rateQuotedPriceDate;
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public Date getEnterDate() {
        return enterDate;
    }
    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }
    public String getFloatingMode() {
        return floatingMode;
    }
    public void setFloatingMode(String floatingMode) {
        this.floatingMode = floatingMode;
    }
    public String getFloatModeNext() {
        return floatModeNext;
    }
    public void setFloatModeNext(String floatModeNext) {
        this.floatModeNext = floatModeNext;
    }
    public String getToDayDate() {
        return toDayDate;
    }
    public void setToDayDate(String toDayDate) {
        this.toDayDate = toDayDate;
    }
    public String getFixedRate() {
        return fixedRate;
    }
    public void setFixedRate(String fixedRate) {
        this.fixedRate = fixedRate;
    }
    public String getFloatingModeName() {
        return floatingModeName;
    }
    public void setFloatingModeName(String floatingModeName) {
        this.floatingModeName = floatingModeName;
    }
    @Override
    public String toString() {
        return "CmsFloatingModeVO [billCode=" + billCode + ", enterDate=" + enterDate + ", floatingMode=" + floatingMode
                + ", floatModeNext=" + floatModeNext + ", toDayDate=" + toDayDate + ", fixedRate=" + fixedRate + "]";
    }
    public BigDecimal getPointsText() {
        return pointsText;
    }
    public void setPointsText(BigDecimal pointsText) {
        this.pointsText = pointsText;
    }
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    public Date getRateQuotedPriceDate() {
        return rateQuotedPriceDate;
    }
    public void setRateQuotedPriceDate(Date rateQuotedPriceDate) {
        this.rateQuotedPriceDate = rateQuotedPriceDate;
    }
}
