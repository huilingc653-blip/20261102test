package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 科目日结单Bean
 * @author liyh
 *
 */
public class FcSubjectDayBean implements Serializable {
	/** 结算日期 */
	private java.util.Date optDate;
	/** 所属网点 */
	private java.lang.String netId;
	/** 区域代码 */
	private java.lang.String areaCode;
	/** 行业代码 */
	private java.lang.String vocationCode;
	/** 科目Id */
	private java.lang.String subjectId;
	/** 币别代码 */
	private java.lang.String curId;
	/** 转账借方金额 */
	private java.math.BigDecimal trDebitMoney = BigDecimal.ZERO;
	/** 转账借方笔数 */
	private java.lang.Integer trDebitCnt = Integer.valueOf(0);
	/** 转账贷方金额 */
	private java.math.BigDecimal trCreditMoney = BigDecimal.ZERO;
	/** 转账贷方笔数 */
	private java.lang.Integer trCreditCnt = Integer.valueOf(0);
	/** 现金借方金额 */
	private java.math.BigDecimal cashDebitMoney = BigDecimal.ZERO;
	/** 现金借方笔数 */
	private java.lang.Integer cashDebitCnt = Integer.valueOf(0);
	/** 现金贷方金额 */
	private java.math.BigDecimal cashCreditMoney = BigDecimal.ZERO;
	/** 现金贷方笔数 */
	private java.lang.Integer cashCreditCnt = Integer.valueOf(0);
	/** 昨日余额 */
	private java.math.BigDecimal yestBalance = BigDecimal.ZERO;
	/** 今日余额 */
	private java.math.BigDecimal todayBalance = BigDecimal.ZERO;
	
	public FcSubjectDayBean() {
		
	}
	public FcSubjectDayBean(String netId, String subjectId, String curId) {
		this.netId = netId;
		this.subjectId = subjectId;
		this.curId = curId;
	}
	public java.util.Date getOptDate() {
		return optDate;
	}
	public void setOptDate(java.util.Date optDate) {
		this.optDate = optDate;
	}
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(java.lang.String areaCode) {
		this.areaCode = areaCode;
	}
	public java.lang.String getVocationCode() {
		return vocationCode;
	}
	public void setVocationCode(java.lang.String vocationCode) {
		this.vocationCode = vocationCode;
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
	public java.math.BigDecimal getTrDebitMoney() {
		return trDebitMoney;
	}
	public void setTrDebitMoney(java.math.BigDecimal trDebitMoney) {
		this.trDebitMoney = trDebitMoney;
	}
	public java.lang.Integer getTrDebitCnt() {
		return trDebitCnt;
	}
	public void setTrDebitCnt(java.lang.Integer trDebitCnt) {
		this.trDebitCnt = trDebitCnt;
	}
	public java.math.BigDecimal getTrCreditMoney() {
		return trCreditMoney;
	}
	public void setTrCreditMoney(java.math.BigDecimal trCreditMoney) {
		this.trCreditMoney = trCreditMoney;
	}
	public java.lang.Integer getTrCreditCnt() {
		return trCreditCnt;
	}
	public void setTrCreditCnt(java.lang.Integer trCreditCnt) {
		this.trCreditCnt = trCreditCnt;
	}
	public java.math.BigDecimal getCashDebitMoney() {
		return cashDebitMoney;
	}
	public void setCashDebitMoney(java.math.BigDecimal cashDebitMoney) {
		this.cashDebitMoney = cashDebitMoney;
	}
	public java.lang.Integer getCashDebitCnt() {
		return cashDebitCnt;
	}
	public void setCashDebitCnt(java.lang.Integer cashDebitCnt) {
		this.cashDebitCnt = cashDebitCnt;
	}
	public java.math.BigDecimal getCashCreditMoney() {
		return cashCreditMoney;
	}
	public void setCashCreditMoney(java.math.BigDecimal cashCreditMoney) {
		this.cashCreditMoney = cashCreditMoney;
	}
	public java.lang.Integer getCashCreditCnt() {
		return cashCreditCnt;
	}
	public void setCashCreditCnt(java.lang.Integer cashCreditCnt) {
		this.cashCreditCnt = cashCreditCnt;
	}
	public java.math.BigDecimal getYestBalance() {
		return yestBalance;
	}
	public void setYestBalance(java.math.BigDecimal yestBalance) {
		this.yestBalance = yestBalance;
	}
	public java.math.BigDecimal getTodayBalance() {
		return todayBalance;
	}
	public void setTodayBalance(java.math.BigDecimal todayBalance) {
		this.todayBalance = todayBalance;
	}
	public void addTrDebitMoney(BigDecimal money) {
		this.trDebitMoney = this.trDebitMoney.add(money);
	}
	
	public void addTrCreditMoney(BigDecimal money) {
		this.trCreditMoney = this.trCreditMoney.add(money);
	}
	
	public void addCashDebitMoney(BigDecimal money) {
		this.cashDebitMoney = this.cashDebitMoney.add(money);
	}
	
	public void addCashCreditMoney(BigDecimal money) {
		this.cashCreditMoney = this.cashCreditMoney.add(money);
	}
	public void addTrDebitCnt(Integer cnt) {
		this.trDebitCnt = this.trDebitCnt + cnt;
	}
	
	public void addTrCreditCnt(Integer cnt) {
		this.trCreditCnt = this.trCreditCnt + cnt;
	}
	
	public void addCashDebitCnt(Integer cnt) {
		this.cashDebitCnt = this.cashDebitCnt + cnt;
	}
	
	public void addCashCreditCnt(Integer cnt) {
		this.cashDebitCnt = this.cashDebitCnt + cnt;
	}
	
	public void addYestBalance(BigDecimal money) {
		this.yestBalance = this.yestBalance.add(money);
	}
	
	public void addTodayBalance(BigDecimal money) {
		this.todayBalance = this.todayBalance.add(money);
	}
}
