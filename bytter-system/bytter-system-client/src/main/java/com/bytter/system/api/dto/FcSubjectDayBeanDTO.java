package com.bytter.system.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 科目日结单Bean
 * @author liyh
 *
 */
public class FcSubjectDayBeanDTO implements Serializable {
	/** 结算日期 */
	private java.util.Date optDate;
	/** 所属网点 */
	private String netId;
	/** 区域代码 */
	private String areaCode;
	/** 行业代码 */
	private String vocationCode;
	/** 科目Id */
	private String subjectId;
	/** 币别代码 */
	private String curId;
	/** 转账借方金额 */
	private BigDecimal trDebitMoney = BigDecimal.ZERO;
	/** 转账借方笔数 */
	private Integer trDebitCnt = Integer.valueOf(0);
	/** 转账贷方金额 */
	private BigDecimal trCreditMoney = BigDecimal.ZERO;
	/** 转账贷方笔数 */
	private Integer trCreditCnt = Integer.valueOf(0);
	/** 现金借方金额 */
	private BigDecimal cashDebitMoney = BigDecimal.ZERO;
	/** 现金借方笔数 */
	private Integer cashDebitCnt = Integer.valueOf(0);
	/** 现金贷方金额 */
	private BigDecimal cashCreditMoney = BigDecimal.ZERO;
	/** 现金贷方笔数 */
	private Integer cashCreditCnt = Integer.valueOf(0);
	/** 昨日余额 */
	private BigDecimal yestBalance = BigDecimal.ZERO;
	/** 今日余额 */
	private BigDecimal todayBalance = BigDecimal.ZERO;
	
	public FcSubjectDayBeanDTO() {
		
	}
	public FcSubjectDayBeanDTO(String netId, String subjectId, String curId) {
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
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getVocationCode() {
		return vocationCode;
	}
	public void setVocationCode(String vocationCode) {
		this.vocationCode = vocationCode;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public BigDecimal getTrDebitMoney() {
		return trDebitMoney;
	}
	public void setTrDebitMoney(BigDecimal trDebitMoney) {
		this.trDebitMoney = trDebitMoney;
	}
	public Integer getTrDebitCnt() {
		return trDebitCnt;
	}
	public void setTrDebitCnt(Integer trDebitCnt) {
		this.trDebitCnt = trDebitCnt;
	}
	public BigDecimal getTrCreditMoney() {
		return trCreditMoney;
	}
	public void setTrCreditMoney(BigDecimal trCreditMoney) {
		this.trCreditMoney = trCreditMoney;
	}
	public Integer getTrCreditCnt() {
		return trCreditCnt;
	}
	public void setTrCreditCnt(Integer trCreditCnt) {
		this.trCreditCnt = trCreditCnt;
	}
	public BigDecimal getCashDebitMoney() {
		return cashDebitMoney;
	}
	public void setCashDebitMoney(BigDecimal cashDebitMoney) {
		this.cashDebitMoney = cashDebitMoney;
	}
	public Integer getCashDebitCnt() {
		return cashDebitCnt;
	}
	public void setCashDebitCnt(Integer cashDebitCnt) {
		this.cashDebitCnt = cashDebitCnt;
	}
	public BigDecimal getCashCreditMoney() {
		return cashCreditMoney;
	}
	public void setCashCreditMoney(BigDecimal cashCreditMoney) {
		this.cashCreditMoney = cashCreditMoney;
	}
	public Integer getCashCreditCnt() {
		return cashCreditCnt;
	}
	public void setCashCreditCnt(Integer cashCreditCnt) {
		this.cashCreditCnt = cashCreditCnt;
	}
	public BigDecimal getYestBalance() {
		return yestBalance;
	}
	public void setYestBalance(BigDecimal yestBalance) {
		this.yestBalance = yestBalance;
	}
	public BigDecimal getTodayBalance() {
		return todayBalance;
	}
	public void setTodayBalance(BigDecimal todayBalance) {
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
