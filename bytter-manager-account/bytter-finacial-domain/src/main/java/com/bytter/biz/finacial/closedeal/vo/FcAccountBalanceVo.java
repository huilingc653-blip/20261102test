package com.bytter.biz.finacial.closedeal.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcAccountBalanceVo implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	
	/**所属网点*/
	@BtCompositeSearch(fieldName="b.net_Id")
	private java.lang.String netId;
	/**科目代码*/
	@BtCompositeSearch(fieldName="b.subject_id")
	private java.lang.String subjectId;
	private java.lang.String subjectName;
	/**开户单位ID*/
	@BtCompositeSearch(fieldName="b.corp_Id")
	private java.lang.String corpId;
	/**余额方向*/
	@BtCompositeSearch(fieldName="b.balance_way")
	private Integer balanceWay;
	private java.lang.String balanceName;
	/**账户类别*/
	@BtCompositeSearch(fieldName="b.acc_type")
	private java.lang.String accType;
	/**余额日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName="b.balance_date")
	private java.util.Date balanceDate;
	private java.util.Date gedBalanceDate;
	private java.util.Date ledBalanceDate;
	/**账号*/
	@BtCompositeSearch(fieldName="b.account")
	private java.lang.String account;
	/**币别代码*/
	@BtCompositeSearch(fieldName="b.cur_Id")
	private java.lang.String curId;
	private java.lang.String curName;
	/**账户名称*/
	@BtCompositeSearch(fieldName="b.acc_Name")
	private java.lang.String accName;
	/**内转笔数*/
	private java.lang.Integer trCnt;
	/**冲正笔数*/
	private java.lang.Integer rushCnt;
	/**笔数*/
	private java.lang.Integer cnt;
	/**昨日余额*/
	@BtCompositeSearch(fieldName="b.yester_balance")
	private java.math.BigDecimal yesterBalance;
	/**余额*/
	@BtCompositeSearch(fieldName="b.balance")
	private java.math.BigDecimal balance;
	/**内转借方发生额*/
	private java.math.BigDecimal trDebitMoney;
	/**内转贷方发生额*/
	private java.math.BigDecimal trCreditMoney;
	/**冲正借方发生额*/
	private java.math.BigDecimal rushDebitMoney;
	/**冲正贷方发生额*/
	private java.math.BigDecimal rushCreditMoney;
	/**借方发生额*/
	@BtCompositeSearch(fieldName="b.debit_Money")
	private java.math.BigDecimal debitMoney;
	/**贷方发生额*/
	@BtCompositeSearch(fieldName="b.credit_Money")
	private java.math.BigDecimal creditMoney;
	/**是否记账日*/
	private java.lang.String tallyDaySign;
	/**借方笔数*/
	private java.lang.Integer debitCnt;
	/**贷方笔数*/
	private java.lang.Integer creditCnt;
	/**冻结金额*/
	private java.math.BigDecimal freezeMoney;

	//用来分组统计使用
	private java.math.BigDecimal calBalance;

	public java.math.BigDecimal getCalBalance() {
		return calBalance;
	}

	public void setCalBalance(java.math.BigDecimal calBalance) {
		this.calBalance = calBalance;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(java.lang.String subjectId) {
		this.subjectId = subjectId;
	}
	public java.lang.String getCorpId() {
		return corpId;
	}
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}
	
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public java.lang.String getAccType() {
		return accType;
	}
	public void setAccType(java.lang.String accType) {
		this.accType = accType;
	}
	public java.util.Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(java.util.Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public java.lang.String getAccName() {
		return accName;
	}
	public void setAccName(java.lang.String accName) {
		this.accName = accName;
	}
	public java.lang.Integer getTrCnt() {
		return trCnt;
	}
	public void setTrCnt(java.lang.Integer trCnt) {
		this.trCnt = trCnt;
	}
	public java.lang.Integer getRushCnt() {
		return rushCnt;
	}
	public void setRushCnt(java.lang.Integer rushCnt) {
		this.rushCnt = rushCnt;
	}
	public java.lang.Integer getCnt() {
		return cnt;
	}
	public void setCnt(java.lang.Integer cnt) {
		this.cnt = cnt;
	}
	public java.math.BigDecimal getYesterBalance() {
		return yesterBalance;
	}
	public void setYesterBalance(java.math.BigDecimal yesterBalance) {
		this.yesterBalance = yesterBalance;
	}
	public java.math.BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(java.math.BigDecimal balance) {
		this.balance = balance;
	}
	public java.math.BigDecimal getTrDebitMoney() {
		return trDebitMoney;
	}
	public void setTrDebitMoney(java.math.BigDecimal trDebitMoney) {
		this.trDebitMoney = trDebitMoney;
	}
	public java.math.BigDecimal getTrCreditMoney() {
		return trCreditMoney;
	}
	public void setTrCreditMoney(java.math.BigDecimal trCreditMoney) {
		this.trCreditMoney = trCreditMoney;
	}
	public java.math.BigDecimal getRushDebitMoney() {
		return rushDebitMoney;
	}
	public void setRushDebitMoney(java.math.BigDecimal rushDebitMoney) {
		this.rushDebitMoney = rushDebitMoney;
	}
	public java.math.BigDecimal getRushCreditMoney() {
		return rushCreditMoney;
	}
	public void setRushCreditMoney(java.math.BigDecimal rushCreditMoney) {
		this.rushCreditMoney = rushCreditMoney;
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
	public java.lang.String getTallyDaySign() {
		return tallyDaySign;
	}
	public void setTallyDaySign(java.lang.String tallyDaySign) {
		this.tallyDaySign = tallyDaySign;
	}
	public java.lang.Integer getDebitCnt() {
		return debitCnt;
	}
	public void setDebitCnt(java.lang.Integer debitCnt) {
		this.debitCnt = debitCnt;
	}
	public java.lang.Integer getCreditCnt() {
		return creditCnt;
	}
	public void setCreditCnt(java.lang.Integer creditCnt) {
		this.creditCnt = creditCnt;
	}
	public java.math.BigDecimal getFreezeMoney() {
		return freezeMoney;
	}
	public void setFreezeMoney(java.math.BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public java.lang.String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(java.lang.String subjectName) {
		this.subjectName = subjectName;
	}
	public java.lang.String getBalanceName() {
		return balanceName;
	}
	public void setBalanceName(java.lang.String balanceName) {
		this.balanceName = balanceName;
	}
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	public java.util.Date getGedBalanceDate() {
		return gedBalanceDate;
	}
	public void setGedBalanceDate(java.util.Date gedBalanceDate) {
		this.gedBalanceDate = gedBalanceDate;
	}
	public java.util.Date getLedBalanceDate() {
		return ledBalanceDate;
	}
	public void setLedBalanceDate(java.util.Date ledBalanceDate) {
		this.ledBalanceDate = ledBalanceDate;
	}
	
	
	
}
