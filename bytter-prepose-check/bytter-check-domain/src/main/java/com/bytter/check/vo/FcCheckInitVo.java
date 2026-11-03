package com.bytter.check.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
public class FcCheckInitVo  implements Serializable {
	
	private String id;
	/**对账类型*/
	@BtCompositeSearch(fieldName="it.check_type")
	private String checkType;
	/**企业账号*/
	@BtCompositeSearch(fieldName="it.account")
	private String account;
	/**银行账号*/
	@BtCompositeSearch(fieldName="it.bank_Acc")
	private String bankAcc;
	/**对账初始日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="it.check_date")
	private java.util.Date checkDate;
	/**企业余额*/
	@BtCompositeSearch(fieldName="it.bln_balance")
	private java.math.BigDecimal blnBalance;
	/**银行余额*/
	@BtCompositeSearch(fieldName="it.bank_balance")
	private java.math.BigDecimal bankBalance;
	/**备注*/
	@BtCompositeSearch(fieldName="it.rmk")
	private String rmk;
	/**网点*/
	@BtCompositeSearch(fieldName="it.net_id")
	private String netId;
	/**单位*/
	@BtCompositeSearch(fieldName="it.corp_id")
	private String corpId;
	/**中心账户名称*/
	@BtCompositeSearch(fieldName="it.account_acc_name")
	private String accName;
	/**银行账户账户名称 */
	@BtCompositeSearch(fieldName="it.bank_acc_name")
	private String bankAccName;
	/**开户行名称*/
	@BtCompositeSearch(fieldName="it.bank_name")
	private String bankName;
	
	/**币别ID*/
	@Currency(inject="curName")
	private String curId;
	/**币别ID*/
	private String curName;
	
	private String compareSign;
	
	/**ERP账号**/
	private String erpCode;
	/**ERP名称**/
	private String erpName;
	/**外部系统备用**/
	private String erpReverse;
	/**erp对应银行账号**/
	@BtCompositeSearch(fieldName="app.bank_acc")
	private String bankAccount;
	/**erp对应银行账号**/
	@BtCompositeSearch(fieldName="app.bank_name")
	private String bankAccountName;
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public String getErpCode() {
		return erpCode;
	}
	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}
	public String getErpName() {
		return erpName;
	}
	public void setErpName(String erpName) {
		this.erpName = erpName;
	}
	public String getErpReverse() {
		return erpReverse;
	}
	public void setErpReverse(String erpReverse) {
		this.erpReverse = erpReverse;
	}
	public String getCompareSign() {
		return compareSign;
	}
	public void setCompareSign(String compareSign) {
		this.compareSign = compareSign;
	}
	
	
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	
	
	public String getBankAccName() {
		return bankAccName;
	}
	public void setBankAccName(String bankAccName) {
		this.bankAccName = bankAccName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public java.util.Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(java.util.Date checkDate) {
		this.checkDate = checkDate;
	}
	public java.math.BigDecimal getBlnBalance() {
		return blnBalance;
	}
	public void setBlnBalance(java.math.BigDecimal blnBalance) {
		this.blnBalance = blnBalance;
	}
	public java.math.BigDecimal getBankBalance() {
		return bankBalance;
	}
	public void setBankBalance(java.math.BigDecimal bankBalance) {
		this.bankBalance = bankBalance;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	
	
}
