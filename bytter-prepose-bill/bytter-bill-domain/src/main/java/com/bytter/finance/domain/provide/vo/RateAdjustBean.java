package com.bytter.finance.domain.provide.vo;
import java.io.Serializable;
public class RateAdjustBean implements Serializable {
    /** 借款单号 */
    private String billCode;
    /** 借款利率 */
    private java.math.BigDecimal plRate;
    /** 展期利率 */
    private java.math.BigDecimal deferredRate;
    /** 预期利率 */
    private java.math.BigDecimal overdueRate;
    /** 利率启用日期 */
    private java.util.Date enterDate;
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public java.math.BigDecimal getPlRate() {
        return plRate;
    }
    public void setPlRate(java.math.BigDecimal plRate) {
        this.plRate = plRate;
    }
    public java.math.BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(java.math.BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    public java.math.BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(java.math.BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    public java.util.Date getEnterDate() {
        return enterDate;
    }
    public void setEnterDate(java.util.Date enterDate) {
        this.enterDate = enterDate;
    }
}
