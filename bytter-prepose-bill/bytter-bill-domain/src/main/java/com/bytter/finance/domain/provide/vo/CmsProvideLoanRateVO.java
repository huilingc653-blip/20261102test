package com.bytter.finance.domain.provide.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
@SuppressWarnings({ "serial" })
public class CmsProvideLoanRateVO implements Serializable {
    /** 借款id */
    private String id;
    /** 借款单号 */
    @BtCompositeSearch(fieldName = "bill_Code")
    private String billCode;
    /** 借款利率 */
    private java.math.BigDecimal plRate;
    /** 展期利率 */
    private java.math.BigDecimal deferredRate;
    /** 预期利率 */
    private java.math.BigDecimal overdueRate;
    /** 执行日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date enterDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public java.util.Date getEnterDate() {
        return enterDate;
    }
    public void setEnterDate(java.util.Date enterDate) {
        this.enterDate = enterDate;
    }
    /** 当前日期 */
    private String toDayDate;
    public String getToDayDate() {
        return toDayDate;
    }
    public void setToDayDate(String toDayDate) {
        this.toDayDate = toDayDate;
    }
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
    @Override
    public String toString() {
        return "CmsProvideLoanRateVO [id=" + id + ", billCode=" + billCode + ", plRate=" + plRate + ", deferredRate="
                + deferredRate + ", overdueRate=" + overdueRate + ", enterDate=" + enterDate + ", toDayDate="
                + toDayDate + "]";
    }
}
