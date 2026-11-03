package com.bytter.biz.finacial.outaccbook.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
public class TallyOutAccbookVO  implements Serializable {
	
	/**网点*/
	@BtCompositeSearch(fieldName="acc.net_id")
	private String netId;
	/**记账日期*/
	@BtCompositeSearch(fieldName="book.tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/**ID*/
	private String id;
	/**流水号*/
	@BtCompositeSearch(fieldName="book.order_no")
	private String orderNo;
	/**凭证号*/
	private String voucherNo;
	/**账号*/
	@BtCompositeSearch(fieldName="bookDTL.account")
	private String acc;
	/**账号名称*/
	private String accName;
	/**收入*/
	private BigDecimal debitMoney;
	/**支出*/
	private BigDecimal creditMoney;
	/**摘要*/
	private String summaryContent;
	/**科目*/
	private String subjectName;
	/**计量单位*/
	private String unit;
	/**状态*/
	@BtCompositeSearch(fieldName="book.status")
	private Integer status;
	
	private String statusName;
	
	private String curId;
	
	private String curName;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getAcc() {
		return acc;
	}
	public void setAcc(String acc) {
		this.acc = acc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
}
