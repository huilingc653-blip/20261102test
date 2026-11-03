package com.bytter.biz.receipt.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 银行电子回单勾对中心表
 */
@Entity
@Table(name = "BIS_RECEIPT_DTL", schema = "")
@SuppressWarnings("serial")
public class BisReceiptDtl extends IdEntity implements java.io.Serializable {
	/**
	 * 流水id DTL_ID
	 */
	private String dtlId;
	/**
	 * 电子回单id RECEIPT_ID
	 */
	private String receiptId;
	/**
	 * 勾对规则 RULES_NAME
	 */
	private String rulesName;

	/**
	 * 勾对时间 PAYMENTBLENDING_DATE
	 */
	private Date paymentblendingDate;


	@Column(name ="DTL_ID")
	public String getDtlId() {
		return dtlId;
	}

	public void setDtlId(String dtlId) {
		this.dtlId = dtlId;
	}

	@Column(name ="RECEIPT_ID")
	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	@Column(name ="RULES_NAME")
	public String getRulesName() {
		return rulesName;
	}

	public void setRulesName(String rulesName) {
		this.rulesName = rulesName;
	}

	@Column(name ="PAYMENTBLENDING_DATE")
	public Date getPaymentblendingDate() {
		return paymentblendingDate;
	}

	public void setPaymentblendingDate(Date paymentblendingDate) {
		this.paymentblendingDate = paymentblendingDate;
	}

}
