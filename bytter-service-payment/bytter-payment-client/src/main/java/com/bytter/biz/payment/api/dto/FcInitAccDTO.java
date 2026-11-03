package com.bytter.biz.payment.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 账户初始化数据
 * @author liyh
 *
 */
public class FcInitAccDTO implements Serializable {
	/** 账号 */
	private String account;
	/** 网点代码 */
	private java.lang.String netId;
	/** 科目代码 */
	private java.lang.String subjectId;
	/** 币别代码 */
	private java.lang.String curId;
	
	/** 账名 */
	private String accName;
	/** 余额 */
	private BigDecimal balance;
	/** 余额 */
	private Integer balanceWay;
	/** 借方余额 */
	private BigDecimal debitMoney;
	/** 借方余额 */
	private BigDecimal creditMoney;
	/** 积数 */
	private BigDecimal cumulation;
	/** 年余额 */
	private BigDecimal yearBalance;
	/** 年合计 */
	private BigDecimal yearTotal;
	/** 状态 */
	private Integer status;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
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
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
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
	public BigDecimal getCumulation() {
		return cumulation;
	}
	public void setCumulation(BigDecimal cumulation) {
		this.cumulation = cumulation;
	}
	public BigDecimal getYearBalance() {
		return yearBalance;
	}
	public void setYearBalance(BigDecimal yearBalance) {
		this.yearBalance = yearBalance;
	}
	public BigDecimal getYearTotal() {
		return yearTotal;
	}
	public void setYearTotal(BigDecimal yearTotal) {
		this.yearTotal = yearTotal;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
