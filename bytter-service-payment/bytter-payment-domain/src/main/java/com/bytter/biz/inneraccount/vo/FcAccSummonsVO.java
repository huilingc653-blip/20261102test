package com.bytter.biz.inneraccount.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class FcAccSummonsVO implements Serializable{

	private String id;
	/**流水号*/
	private String orderNo;		
	/**凭证号*/
	@BtCompositeSearch(fieldName = "cc.voucher_no")
	private String voucherNo;
	/**金额*/
	@BtCompositeSearch(fieldName = "cc.sum_money")
	private java.math.BigDecimal sumMoney;
	/**种类*/
	@BtCompositeSearch(fieldName = "cc.voucher_type")
	@Dict(tableName="FC_VOUCHER_TYPE",valueFieldName="id",showFiledName="VOUCHER_NAME",inject="voucherName")
	private String voucherType;

	private String voucherTypeName;
	/**付款方式*/
	@BtCompositeSearch(fieldName = "pay_type")
	private String voucherId;
	@BtCompositeSearch(fieldName = "voucher_name")
	private String voucherName;
	/**记账日期*/
	@BtCompositeSearch(fieldName = "tally_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	/**摘要*/
	@BtCompositeSearch(fieldName = "summary_content")
	private String summaryContent;
	/**单位*/
	@BtCompositeSearch(fieldName = "corp_Id")
	private String corpId;
	/**是否隐藏 1：隐藏*/
	private Integer checkboxHide;

	public String getVoucherTypeName() {
		return voucherTypeName;
	}

	public void setVoucherTypeName(String voucherTypeName) {
		this.voucherTypeName = voucherTypeName;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public java.math.BigDecimal getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(java.math.BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}

	public Integer getCheckboxHide() {
		return checkboxHide;
	}

	public void setCheckboxHide(Integer checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
}
