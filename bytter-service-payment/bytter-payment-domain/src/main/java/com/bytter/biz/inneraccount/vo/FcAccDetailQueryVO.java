package com.bytter.biz.inneraccount.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Created by wangzx on 2017/6/23.
 */
public class FcAccDetailQueryVO implements Serializable {
	/**账号*/
	@BtCompositeSearch(fieldName = "t.account")
	private String account;
	/**户名*/
	@BtCompositeSearch(fieldName = "t.accName")
	private String accName;
	/**币别*/
	@BtCompositeSearch(fieldName = "t.curId")
	@Currency(inject="curName")
	private String curId;
	private String curName;
	/**起息日期*/
	@BtCompositeSearch(fieldName = "t.interestStartDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestStartDate;
	/**记账日期*/
	@BtCompositeSearch(fieldName = "t.tallyDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/**凭证号*/
	@BtCompositeSearch(fieldName = "t.voucherNo")
	private String voucherNo;
	/**摘要*/
	@BtCompositeSearch(fieldName = "t.summaryContent")
	private String summaryContent;
	/**借方发生额(支出)*/
	@BtCompositeSearch(fieldName = "t.debitMoney")
	private java.math.BigDecimal debitMoney;
	/**贷方发生额(收入)*/
	@BtCompositeSearch(fieldName = "t.creditMoney")
	private java.math.BigDecimal creditMoney;
	/**凭证种类代码(付款方式)*/
	@BtCompositeSearch(fieldName = "t.voucherType")
	private String voucherType;
	private String voucherName;
	/**余额*/
	@BtCompositeSearch(fieldName = "t.balance")
	private java.math.BigDecimal balance;
	/**外单位名称*/
	@BtCompositeSearch(fieldName = "t.externalName")
	private String externalName;
	/**外单位账号*/
	@BtCompositeSearch(fieldName = "t.externalAcc")
	private String externalAcc;
	/**原凭证号(付款单号)*/
	@BtCompositeSearch(fieldName = "t.origVoucherNo")
	private String origVoucherNo;
	/**单位*/
	@BtCompositeSearch(fieldName = "t.corpId")
	private java.lang.String corpId;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
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
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
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
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getExternalName() {
		return externalName;
	}
	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}
	public String getExternalAcc() {
		return externalAcc;
	}
	public void setExternalAcc(String externalAcc) {
		this.externalAcc = externalAcc;
	}
	public String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
}
