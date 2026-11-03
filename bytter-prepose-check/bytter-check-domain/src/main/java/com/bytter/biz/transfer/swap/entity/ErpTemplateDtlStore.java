package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
import java.io.Serializable;
/**
 * Created by zhouj on 2018/5/18.
 */
@Entity
@Table(name = "ERP_TEMPLATE_DTL_STORE", catalog = "")
public class ErpTemplateDtlStore extends IdEntity implements Serializable{
    private String storeId;
    private Boolean loanDirection;
    private String subjectCode;
    private String subjectName;
    private String subjectClass;
    private String subjectType;
    private String costCenterCode;
    private String cashFlowCode;
    private String accountCode;
    private String accountType;
    private String dtlAbs;
    private String paymentReasonCode;
    private String ledgerSign;
    private String transType;
    private String assign;
    private String tradePartner;
    private String taxCode;
    private String dtlItemCode;
    private String dtlReverse1;
    private String dtlReverse2;
    @Basic
    @Column(name = "STORE_ID", nullable = true, length = 32)
    public String getStoreId() {
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    @Basic
    @Column(name = "LOAN_DIRECTION", nullable = true, precision = 0)
    public Boolean getLoanDirection() {
        return loanDirection;
    }
    public void setLoanDirection(Boolean loanDirection) {
        this.loanDirection = loanDirection;
    }
    @Basic
    @Column(name = "SUBJECT_CODE", nullable = true, length = 100)
    public String getSubjectCode() {
        return subjectCode;
    }
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
    @Basic
    @Column(name = "SUBJECT_NAME", nullable = true, length = 100)
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    @Basic
    @Column(name = "SUBJECT_CLASS", nullable = true, length = 100)
    public String getSubjectClass() {
        return subjectClass;
    }
    public void setSubjectClass(String subjectClass) {
        this.subjectClass = subjectClass;
    }
    @Basic
    @Column(name = "SUBJECT_TYPE", nullable = true, length = 100)
    public String getSubjectType() {
        return subjectType;
    }
    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
    @Basic
    @Column(name = "COST_CENTER_CODE", nullable = true, length = 100)
    public String getCostCenterCode() {
        return costCenterCode;
    }
    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }
    @Basic
    @Column(name = "CASH_FLOW_CODE", nullable = true, length = 100)
    public String getCashFlowCode() {
        return cashFlowCode;
    }
    public void setCashFlowCode(String cashFlowCode) {
        this.cashFlowCode = cashFlowCode;
    }
    @Basic
    @Column(name = "ACCOUNT_CODE", nullable = true, length = 100)
    public String getAccountCode() {
        return accountCode;
    }
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }
    @Basic
    @Column(name = "ACCOUNT_TYPE", nullable = true, length = 100)
    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    @Basic
    @Column(name = "DTL_ABS", nullable = true, length = 100)
    public String getDtlAbs() {
        return dtlAbs;
    }
    public void setDtlAbs(String dtlAbs) {
        this.dtlAbs = dtlAbs;
    }
    @Basic
    @Column(name = "PAYMENT_REASON_CODE", nullable = true, length = 100)
    public String getPaymentReasonCode() {
        return paymentReasonCode;
    }
    public void setPaymentReasonCode(String paymentReasonCode) {
        this.paymentReasonCode = paymentReasonCode;
    }
    @Basic
    @Column(name = "LEDGER_SIGN", nullable = true, length = 100)
    public String getLedgerSign() {
        return ledgerSign;
    }
    public void setLedgerSign(String ledgerSign) {
        this.ledgerSign = ledgerSign;
    }
    @Basic
    @Column(name = "TRANS_TYPE", nullable = true, length = 100)
    public String getTransType() {
        return transType;
    }
    public void setTransType(String transType) {
        this.transType = transType;
    }
    @Basic
    @Column(name = "ASSIGN", nullable = true, length = 100)
    public String getAssign() {
        return assign;
    }
    public void setAssign(String assign) {
        this.assign = assign;
    }
    @Basic
    @Column(name = "TRADE_PARTNER", nullable = true, length = 100)
    public String getTradePartner() {
        return tradePartner;
    }
    public void setTradePartner(String tradePartner) {
        this.tradePartner = tradePartner;
    }
    @Basic
    @Column(name = "TAX_CODE", nullable = true, length = 100)
    public String getTaxCode() {
        return taxCode;
    }
    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }
    @Basic
    @Column(name = "DTL_ITEM_CODE", nullable = true, length = 100)
    public String getDtlItemCode() {
        return dtlItemCode;
    }
    public void setDtlItemCode(String dtlItemCode) {
        this.dtlItemCode = dtlItemCode;
    }
    @Basic
    @Column(name = "DTL_REVERSE1", nullable = true, length = 100)
    public String getDtlReverse1() {
        return dtlReverse1;
    }
    public void setDtlReverse1(String dtlReverse1) {
        this.dtlReverse1 = dtlReverse1;
    }
    @Basic
    @Column(name = "DTL_REVERSE2", nullable = true, length = 100)
    public String getDtlReverse2() {
        return dtlReverse2;
    }
    public void setDtlReverse2(String dtlReverse2) {
        this.dtlReverse2 = dtlReverse2;
    }
}
