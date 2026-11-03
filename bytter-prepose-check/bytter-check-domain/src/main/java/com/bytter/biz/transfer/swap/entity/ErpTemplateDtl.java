package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
/**
 * Created by zhouj on 2018/5/18.
 */
@Entity
@Table(name = "ERP_TEMPLATE_DTL", schema = "")
public class ErpTemplateDtl extends IdEntity implements java.io.Serializable{
    private String templateId;
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
    public ErpTemplateDtl() {
        super();
    }
    public ErpTemplateDtl(String templateId) {
        super();
        this.templateId = templateId;
    }
    public ErpTemplateDtl(String id,String templateId) {
        super();
        this.setId(id);
        this.templateId = templateId;
    }
    @Column(name = "TEMPLATE_ID", nullable = true, length = 32)
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    @Column(name = "LOAN_DIRECTION", nullable = true, precision = 0)
    public Boolean getLoanDirection() {
        return loanDirection;
    }
    public void setLoanDirection(Boolean loanDirection) {
        this.loanDirection = loanDirection;
    }
    @Column(name = "SUBJECT_CODE", nullable = true, length = 100)
    public String getSubjectCode() {
        return subjectCode;
    }
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
    @Column(name = "SUBJECT_NAME", nullable = true, length = 100)
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    @Column(name = "SUBJECT_CLASS", nullable = true, length = 100)
    public String getSubjectClass() {
        return subjectClass;
    }
    public void setSubjectClass(String subjectClass) {
        this.subjectClass = subjectClass;
    }
    @Column(name = "SUBJECT_TYPE", nullable = true, length = 100)
    public String getSubjectType() {
        return subjectType;
    }
    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
    @Column(name = "COST_CENTER_CODE", nullable = true, length = 100)
    public String getCostCenterCode() {
        return costCenterCode;
    }
    public void setCostCenterCode(String costCenterCode) {
        this.costCenterCode = costCenterCode;
    }
    @Column(name = "CASH_FLOW_CODE", nullable = true, length = 100)
    public String getCashFlowCode() {
        return cashFlowCode;
    }
    public void setCashFlowCode(String cashFlowCode) {
        this.cashFlowCode = cashFlowCode;
    }
    @Column(name = "ACCOUNT_CODE", nullable = true, length = 100)
    public String getAccountCode() {
        return accountCode;
    }
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }
    @Column(name = "ACCOUNT_TYPE", nullable = true, length = 100)
    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    @Column(name = "DTL_ABS", nullable = true, length = 100)
    public String getDtlAbs() {
        return dtlAbs;
    }
    public void setDtlAbs(String dtlAbs) {
        this.dtlAbs = dtlAbs;
    }
    @Column(name = "PAYMENT_REASON_CODE", nullable = true, length = 100)
    public String getPaymentReasonCode() {
        return paymentReasonCode;
    }
    public void setPaymentReasonCode(String paymentReasonCode) {
        this.paymentReasonCode = paymentReasonCode;
    }
    @Column(name = "LEDGER_SIGN", nullable = true, length = 100)
    public String getLedgerSign() {
        return ledgerSign;
    }
    public void setLedgerSign(String ledgerSign) {
        this.ledgerSign = ledgerSign;
    }
    @Column(name = "TRANS_TYPE", nullable = true, length = 100)
    public String getTransType() {
        return transType;
    }
    public void setTransType(String transType) {
        this.transType = transType;
    }
    @Column(name = "ASSIGN", nullable = true, length = 100)
    public String getAssign() {
        return assign;
    }
    public void setAssign(String assign) {
        this.assign = assign;
    }
    @Column(name = "TRADE_PARTNER", nullable = true, length = 100)
    public String getTradePartner() {
        return tradePartner;
    }
    public void setTradePartner(String tradePartner) {
        this.tradePartner = tradePartner;
    }
    @Column(name = "TAX_CODE", nullable = true, length = 100)
    public String getTaxCode() {
        return taxCode;
    }
    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }
    @Column(name = "DTL_ITEM_CODE", nullable = true, length = 100)
    public String getDtlItemCode() {
        return dtlItemCode;
    }
    public void setDtlItemCode(String dtlItemCode) {
        this.dtlItemCode = dtlItemCode;
    }
    @Column(name = "DTL_REVERSE1", nullable = true, length = 100)
    public String getDtlReverse1() {
        return dtlReverse1;
    }
    public void setDtlReverse1(String dtlReverse1) {
        this.dtlReverse1 = dtlReverse1;
    }
    @Column(name = "DTL_REVERSE2", nullable = true, length = 100)
    public String getDtlReverse2() {
        return dtlReverse2;
    }
    public void setDtlReverse2(String dtlReverse2) {
        this.dtlReverse2 = dtlReverse2;
    }
}
