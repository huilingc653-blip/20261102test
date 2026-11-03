package com.bytter.biz.bdlink.vo;
import java.util.Date;
public class BisBankReceiptDtlAutoVO  {
    private String receiptId;
    private String corpId;
    private String amt;
    private String payAccNo;
    private String payAccName;
    private String payBranchName;
    private String receiptNo;
    private String transSerialNo;
    private String payeeAccNo;
    private String payeeAccName;
    private String cdSign;
    private Date charageDate;
    private String curId;
    private String usefor;
    private String rules;
    private String rulesName;
    private String isExistId;
    private String dtlOnlyId;
    private String receiptOnlyId;
    private String bisEbillNo;
    /**交易日期-相差天数*/
    private Integer transDays;

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getPayAccNo() {
        return payAccNo;
    }

    public void setPayAccNo(String payAccNo) {
        this.payAccNo = payAccNo;
    }

    public String getPayAccName() {
        return payAccName;
    }

    public void setPayAccName(String payAccName) {
        this.payAccName = payAccName;
    }

    public String getPayBranchName() {
        return payBranchName;
    }

    public void setPayBranchName(String payBranchName) {
        this.payBranchName = payBranchName;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getTransSerialNo() {
        return transSerialNo;
    }

    public void setTransSerialNo(String transSerialNo) {
        this.transSerialNo = transSerialNo;
    }

    public String getPayeeAccNo() {
        return payeeAccNo;
    }

    public void setPayeeAccNo(String payeeAccNo) {
        this.payeeAccNo = payeeAccNo;
    }

    public String getPayeeAccName() {
        return payeeAccName;
    }

    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public Date getCharageDate() {
        return charageDate;
    }

    public void setCharageDate(Date charageDate) {
        this.charageDate = charageDate;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getUsefor() {
        return usefor;
    }

    public void setUsefor(String usefor) {
        this.usefor = usefor;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getRulesName() {
        return rulesName;
    }

    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
    }

    public String getIsExistId() {
        return isExistId;
    }

    public void setIsExistId(String isExistId) {
        this.isExistId = isExistId;
    }

    public String getDtlOnlyId() {
        return dtlOnlyId;
    }

    public void setDtlOnlyId(String dtlOnlyId) {
        this.dtlOnlyId = dtlOnlyId;
    }

    public String getReceiptOnlyId() {
        return receiptOnlyId;
    }

    public void setReceiptOnlyId(String receiptOnlyId) {
        this.receiptOnlyId = receiptOnlyId;
    }

    public String getBisEbillNo() {
        return bisEbillNo;
    }

    public void setBisEbillNo(String bisEbillNo) {
        this.bisEbillNo = bisEbillNo;
    }

    public Integer getTransDays() {
        return transDays;
    }

    public void setTransDays(Integer transDays) {
        this.transDays = transDays;
    }
}
