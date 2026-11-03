package com.bytter.bill.domain.bill.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

/**
 * @author lijinquan
 * @description:
 * @date 2024-09-03 11:28
 */
public class DSLAbEmitBillPayBean {

    /**票据号*/
    @JSONField(name = "RECEIPT_NUM")
    private String receiptNum;

    /**票面金额*/
    @JSONField(name = "RECEIPT_COST")
    private BigDecimal receiptCost;

    /**开票日期*/
    @JSONField(name = "CREATE_RECEIPT_DATE")
    private String createReceiptDate;

    /**票据到期日期*/
    @JSONField(name = "RECEIPT_DUE_DATE")
    private String receiptDueDate;

    /**保证金比例*/
    @JSONField(name = "CASH_DEPOSIT_SCALE")
    private String cashDepositScale;

    /**保证金金额*/
    @JSONField(name = "CASH_DEPOSIT_COST")
    private BigDecimal cashDepositCost;

    /**兑付日期*/
    @JSONField(name = "PAY_DATE")
    private String payDate;

    /**orgId*/
    @JSONField(name = "ORG_ID")
    private String orgId;

    /**单位代码*/
    @JSONField(name = "COMPANY_CODE")
    private String companyCode;


    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(String receiptNum) {
        this.receiptNum = receiptNum;
    }

    public BigDecimal getReceiptCost() {
        return receiptCost;
    }

    public void setReceiptCost(BigDecimal receiptCost) {
        this.receiptCost = receiptCost;
    }

    public String getCreateReceiptDate() {
        return createReceiptDate;
    }

    public void setCreateReceiptDate(String createReceiptDate) {
        this.createReceiptDate = createReceiptDate;
    }

    public String getReceiptDueDate() {
        return receiptDueDate;
    }

    public void setReceiptDueDate(String receiptDueDate) {
        this.receiptDueDate = receiptDueDate;
    }

    public String getCashDepositScale() {
        return cashDepositScale;
    }

    public void setCashDepositScale(String cashDepositScale) {
        this.cashDepositScale = cashDepositScale;
    }

    public BigDecimal getCashDepositCost() {
        return cashDepositCost;
    }

    public void setCashDepositCost(BigDecimal cashDepositCost) {
        this.cashDepositCost = cashDepositCost;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
