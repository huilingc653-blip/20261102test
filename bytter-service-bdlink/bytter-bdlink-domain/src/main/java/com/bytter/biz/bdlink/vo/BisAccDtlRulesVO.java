package com.bytter.biz.bdlink.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
public class BisAccDtlRulesVO implements  Serializable {
    @BtCompositeSearch(fieldName = "")
    private String dtlId;
    @BtCompositeSearch(fieldName = "")
    private String receiptId;
    @BtCompositeSearch(fieldName = "")
    private String rulesName;
    public String getDtlId() {
        return dtlId;
    }
    public void setDtlId(String dtlId) {
        this.dtlId = dtlId;
    }
    public String getReceiptId() {
        return receiptId;
    }
    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }
    public String getRulesName() {
        return rulesName;
    }
    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
    }
}
