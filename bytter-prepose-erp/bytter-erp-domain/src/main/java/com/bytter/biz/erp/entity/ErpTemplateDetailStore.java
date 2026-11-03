package com.bytter.biz.erp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ERP_TEMPLATE_DTL_STORE")
public class ErpTemplateDetailStore{
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "STORE_ID")
	private String store_id;// ƾ֤ģ���ҵ�����ݹ�����ID
	@Column(name = "LOAN_DIRECTION")
	private String loanDirection;// ������� 0,�� 1,��
	@Column(name = "SUBJECT_CODE")
	private String subjectCode;// ��Ŀ����
	@Column(name = "SUBJECT_NAME")
	private String subjectName;// ��Ŀ����
	@Column(name = "SUBJECT_CLASS")
	private String subjectClass; // ��Ŀ����
	@Column(name = "SUBJECT_TYPE")
	private String subjectType;// ��Ŀ����

	@Column(name = "COST_CENTER_CODE")
	private String costCenterCode;// �ɱ�����
	@Column(name = "CASH_FLOW_CODE")
	private String cashFlowCode; // �ֽ�������
	@Column(name = "ACCOUNT_CODE")
	private String accountCode;// ���˴���
	@Column(name = "ACCOUNT_TYPE")
	private String accountType;// �˻�����
	@Column(name = "PAYMENT_REASON_CODE")
	private String paymentReasonCode;// ����ԭ�����
	@Column(name = "LEDGER_SIGN")
	private String ledgenSign;// ���˱�ʶ
	@Column(name = "TRANS_TYPE")
	private String transType;// ��������

	@Column(name = "DTL_ABS")
	private String dtlAbs;// ժҪ

	@Column(name = "ASSIGN")
	private String assign;// ����
	@Column(name = "TRADE_PARTNER")
	private String tradePartner;// ó�׻��
	@Column(name = "TAX_CODE")
	private String taxCode;// ˰��
	@Column(name = "DTL_ITEM_CODE")
	private String dtlItemCode;// Ԥ�����
	@Column(name = "DTL_REVERSE1")
	private String dtlReverse1;// �Զ���һ
	@Column(name = "DTL_REVERSE2")
	private String dtlReverse2;// �Զ����

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getLoanDirection() {
		return loanDirection;
	}

	public void setLoanDirection(String loanDirection) {
		this.loanDirection = loanDirection;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectClass() {
		return subjectClass;
	}

	public void setSubjectClass(String subjectClass) {
		this.subjectClass = subjectClass;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getCostCenterCode() {
		return costCenterCode;
	}

	public void setCostCenterCode(String costCenterCode) {
		this.costCenterCode = costCenterCode;
	}

	public String getCashFlowCode() {
		return cashFlowCode;
	}

	public void setCashFlowCode(String cashFlowCode) {
		this.cashFlowCode = cashFlowCode;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getPaymentReasonCode() {
		return paymentReasonCode;
	}

	public void setPaymentReasonCode(String paymentReasonCode) {
		this.paymentReasonCode = paymentReasonCode;
	}

	public String getLedgenSign() {
		return ledgenSign;
	}

	public void setLedgenSign(String ledgenSign) {
		this.ledgenSign = ledgenSign;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getAssign() {
		return assign;
	}

	public void setAssign(String assign) {
		this.assign = assign;
	}

	public String getTradePartner() {
		return tradePartner;
	}

	public void setTradePartner(String tradePartner) {
		this.tradePartner = tradePartner;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getDtlItemCode() {
		return dtlItemCode;
	}

	public void setDtlItemCode(String dtlItemCode) {
		this.dtlItemCode = dtlItemCode;
	}

	public String getDtlReverse1() {
		return dtlReverse1;
	}

	public void setDtlReverse1(String dtlReverse1) {
		this.dtlReverse1 = dtlReverse1;
	}

	public String getDtlReverse2() {
		return dtlReverse2;
	}

	public void setDtlReverse2(String dtlReverse2) {
		this.dtlReverse2 = dtlReverse2;
	}

	public String getDtlAbs() {
		return dtlAbs;
	}

	public void setDtlAbs(String dtlAbs) {
		this.dtlAbs = dtlAbs;
	}

}
