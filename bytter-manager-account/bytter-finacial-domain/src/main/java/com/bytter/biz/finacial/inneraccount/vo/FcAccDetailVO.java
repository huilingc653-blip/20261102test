package com.bytter.biz.finacial.inneraccount.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * Created by wangzx on 2017/6/23.
 */
public class FcAccDetailVO implements Serializable {
	private String id;
	/**账号*/
	@BtCompositeSearch(fieldName = "account")
	private java.lang.String account;
	/**凭证号*/
	@BtCompositeSearch(fieldName = "voucherNo")
	private java.lang.String voucherNo;
	/**户名*/
	@BtCompositeSearch(fieldName = "accName")
	private java.lang.String accName;
	/**单位*/
	@BtCompositeSearch(fieldName = "corpId")
	private java.lang.String corpId;
	/**币别*/
	@BtCompositeSearch(fieldName = "curId")
    private String curId;
    private String curName;    
	/**记账日期*/
	@BtCompositeSearch(fieldName = "tallyDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	
	/**起息日期*/
	@BtCompositeSearch(fieldName = "interestStartDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestStartDate;
	/**摘要*/
	@BtCompositeSearch(fieldName = "summaryContent")
	private java.lang.String summaryContent;
	/**借方发生额(支出)*/
	@BtCompositeSearch(fieldName = "debitMoney")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal debitMoney;
	/**贷方发生额(收入)*/
	@BtCompositeSearch(fieldName = "creditMoney")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal creditMoney;
	/**凭证种类代码(付款方式)*/
	@BtCompositeSearch(fieldName = "voucherType")
	private java.lang.String voucherType;
	private String voucherName;
	/**余额*/
	@BtCompositeSearch(fieldName = "balance")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal balance;
	/**外单位名称*/
	@BtCompositeSearch(fieldName = "externalName")
	private java.lang.String externalName;
	/**外单位账号*/
	private java.lang.String externalAcc;
	/**原凭证号(付款单号)*/
	@BtCompositeSearch(fieldName = "origVoucherNo")
	private java.lang.String origVoucherNo;
	private java.lang.String sysVoucherCode;
	/**账户类别*/
	@BtCompositeSearch(fieldName = "accType")
	private java.lang.String accType;
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public java.lang.String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(java.lang.String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public java.lang.String getAccName() {
		return accName;
	}
	public void setAccName(java.lang.String accName) {
		this.accName = accName;
	}
	public java.lang.String getCorpId() {
		return corpId;
	}
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
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
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public java.util.Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(java.util.Date interestStartDate) {
		this.interestStartDate = interestStartDate;
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
	public java.lang.String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(java.lang.String voucherType) {
		this.voucherType = voucherType;
	}
	public java.math.BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(java.math.BigDecimal balance) {
		this.balance = balance;
	}
	public java.lang.String getExternalName() {
		return externalName;
	}
	public void setExternalName(java.lang.String externalName) {
		this.externalName = externalName;
	}
	public java.lang.String getExternalAcc() {
		return externalAcc;
	}
	public void setExternalAcc(java.lang.String externalAcc) {
		this.externalAcc = externalAcc;
	}
	public java.lang.String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(java.lang.String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
	}
	public String getSysVoucherCode() {
		return sysVoucherCode;
	}
	public void setSysVoucherCode(String sysVoucherCode) {
		this.sysVoucherCode = sysVoucherCode;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
}
