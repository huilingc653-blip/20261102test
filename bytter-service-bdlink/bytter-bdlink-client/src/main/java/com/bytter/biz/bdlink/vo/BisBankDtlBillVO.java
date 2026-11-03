package com.bytter.biz.bdlink.vo;

import java.io.Serializable;

public class BisBankDtlBillVO implements Serializable {
    private String dtlId;
    private String receiptId;
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
