package com.bytter.biz.finacial.report.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcBookDtlListVO implements Serializable{
	
	private String id;
	/**网点 */
	private java.lang.String netId;
	/**网点名称*/
	private java.lang.String netName;
	/**帐号*/
	private java.lang.String account;
	/**户名*/
	private java.lang.String accName;
	/**币别*/
	private java.lang.String curName;
	/**科目*/
	private java.lang.String subjectName;
	private java.lang.String subjectCode;
	/**期初余额*/
	private java.math.BigDecimal beginningBalance;
	/** 记账日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/** 凭证号 */
	private java.lang.String voucherNo;
	/**凭证种类代码*/
	private String voucherType;
	private String voucherName;
	/** 摘要 */
	private java.lang.String summaryContent;
	/**借方发生额*/
	private java.math.BigDecimal debitMoney;
	/** 贷方发生额 */
	private java.math.BigDecimal creditMoney;
	/** 借贷方向 */
	private java.lang.String balanceWay;
	/** 余额 */
	private java.math.BigDecimal balance;
	/** 流水号 */
	private String orderNo;
	
	/** 账户总数 */
	private Integer accCount;
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public java.lang.String getAccName() {
		return accName;
	}
	public void setAccName(java.lang.String accName) {
		this.accName = accName;
	}
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	public java.lang.String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(java.lang.String subjectName) {
		this.subjectName = subjectName;
	}
	public java.math.BigDecimal getBeginningBalance() {
		return beginningBalance;
	}
	public void setBeginningBalance(java.math.BigDecimal beginningBalance) {
		this.beginningBalance = beginningBalance;
	}
	public java.lang.String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(java.lang.String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public java.lang.String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(java.lang.String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public java.lang.String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(java.lang.String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public java.math.BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(java.math.BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public java.math.BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(java.math.BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public java.lang.String getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(java.lang.String balanceWay) {
		this.balanceWay = balanceWay;
	}
	public java.math.BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(java.math.BigDecimal balance) {
		this.balance = balance;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getAccCount() {
		return accCount;
	}
	public void setAccCount(Integer accCount) {
		this.accCount = accCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
}
