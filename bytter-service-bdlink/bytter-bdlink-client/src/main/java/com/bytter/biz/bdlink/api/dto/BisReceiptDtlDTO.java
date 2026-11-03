package com.bytter.biz.bdlink.api.dto;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 银行电子回单勾对中心表
 */
@SuppressWarnings("serial")
public class BisReceiptDtlDTO extends IdEntity implements java.io.Serializable {

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

	public Date getPaymentblendingDate() {
		return paymentblendingDate;
	}

	public void setPaymentblendingDate(Date paymentblendingDate) {
		this.paymentblendingDate = paymentblendingDate;
	}
}
