package com.bytter.biz.finacial.report.vo;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
public class FcLedgerListVO implements Serializable{
	
	/**单位名称*/
	private java.lang.String corpName;
	/**币别*/
	private java.lang.String curName;
	/**帐号*/
	private java.lang.String account;
	/**户名*/
	private java.lang.String accName;
	/**期初余额*/
	private java.math.BigDecimal beginningBalance;
	/** 记账日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/** 业务种类 */
	private String raceId;
	private java.lang.String busRaceName;
	/** 凭证种类代码 */
	private java.lang.String voucherTypeName;
	/** 凭证号 */
	private java.lang.String voucherNo;
	/** 摘要 */
	private java.lang.String summaryContent;
	/** 借方发生额 */
	private java.math.BigDecimal debitMoney;
	/** 贷方发生额 */
	private java.math.BigDecimal creditMoney;
	/** 余额 */
	private java.math.BigDecimal balance;
	/** 借贷方向 */
	private java.math.BigDecimal balanceWay;
	/** 账户总数 */
	private java.lang.Long accNum;
	public java.lang.String getCorpName() {
		return corpName;
	}
	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
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
	public java.math.BigDecimal getBeginningBalance() {
		return beginningBalance;
	}
	public void setBeginningBalance(java.math.BigDecimal beginningBalance) {
		this.beginningBalance = beginningBalance;
	}
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public java.lang.String getBusRaceName() {
		return busRaceName;
	}
	public void setBusRaceName(java.lang.String busRaceName) {
		this.busRaceName = busRaceName;
	}
	public java.lang.String getVoucherTypeName() {
		return voucherTypeName;
	}
	public void setVoucherTypeName(java.lang.String voucherTypeName) {
		this.voucherTypeName = voucherTypeName;
	}
	public java.lang.String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(java.lang.String voucherNo) {
		this.voucherNo = voucherNo;
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
	public java.math.BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(java.math.BigDecimal balance) {
		this.balance = balance;
	}
	public java.math.BigDecimal getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(java.math.BigDecimal balanceWay) {
		this.balanceWay = balanceWay;
	}
	public java.lang.Long getAccNum() {
		return accNum;
	}
	public void setAccNum(java.lang.Long accNum) {
		this.accNum = accNum;
	}
	public String getRaceId() {
		return raceId;
	}
	public void setRaceId(String raceId) {
		this.raceId = raceId;
	}
}
