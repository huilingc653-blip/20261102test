package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 月总账表Bean
 * 
 * @author liyh
 *
 */
public class FcMonthLedgerBean implements Serializable {
	/** 年月 */
	private java.lang.String yearMonth2;
	/** 网点代码 */
	private java.lang.String netCode;
	/** 区域代码 */
	private java.lang.String areaCode;
	/** 行业代码 */
	private java.lang.String vocationCode;
	/** 科目代码 */
	private java.lang.String subjectCode;
	/** 币别代码 */
	private java.lang.String currencyCode;
	/** 月初余额 */
	private java.math.BigDecimal initBalance;
	/** 借方发生额 */
	private java.math.BigDecimal debitMoney;
	/** 贷方发生额 */
	private java.math.BigDecimal creditMoney;
	/** 借方笔数 */
	private java.lang.Integer debitCnt;
	/** 贷方笔数 */
	private java.lang.Integer creditCnt;
	/** 月末余额 */
	private java.math.BigDecimal balance;
	/** 累计借方发生额 */
	private java.math.BigDecimal totalDebitMoney;
	/** 累计贷方发生额 */
	private java.math.BigDecimal totalCreditMoney;
	
	public FcMonthLedgerBean() {
		super();
	}
	
	public FcMonthLedgerBean(FcSubjectKeyBean bean) {
		super();
		this.netCode = bean.getNetCode();
		this.subjectCode = bean.getSubjectCode();
		this.currencyCode = bean.getCurrencyCode();
	}
	public FcMonthLedgerBean(String netCode, String subjectCode, String currencyCode) {
		super();
		this.netCode = netCode;
		this.subjectCode = subjectCode;
		this.currencyCode = currencyCode;
	}
	public java.lang.String getYearMonth2() {
		return yearMonth2;
	}
	public void setYearMonth2(java.lang.String yearMonth2) {
		this.yearMonth2 = yearMonth2;
	}
	public java.lang.String getNetCode() {
		return netCode;
	}
	public void setNetCode(java.lang.String netCode) {
		this.netCode = netCode;
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
	public java.lang.String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(java.lang.String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public java.lang.String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(java.lang.String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public java.math.BigDecimal getInitBalance() {
		return initBalance;
	}
	public void setInitBalance(java.math.BigDecimal initBalance) {
		this.initBalance = initBalance;
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
	public java.math.BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(java.math.BigDecimal balance) {
		this.balance = balance;
	}
	public java.math.BigDecimal getTotalDebitMoney() {
		return totalDebitMoney;
	}
	public void setTotalDebitMoney(java.math.BigDecimal totalDebitMoney) {
		this.totalDebitMoney = totalDebitMoney;
	}
	public java.math.BigDecimal getTotalCreditMoney() {
		return totalCreditMoney;
	}
	public void setTotalCreditMoney(java.math.BigDecimal totalCreditMoney) {
		this.totalCreditMoney = totalCreditMoney;
	}
	public void addDebitMoney(BigDecimal money) {
		if (null == this.debitMoney) {
			this.debitMoney = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.debitMoney = this.debitMoney.add(money);
	}
	public void addCreditMoney(BigDecimal money) {
		if (null == this.creditMoney) {
			this.creditMoney = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.creditMoney = this.creditMoney.add(money);
	}
	
	public void addDebitCnt(Integer cnt) {
		if (null == this.debitCnt) {
			this.debitCnt = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.debitCnt = this.debitCnt + cnt;
	}
	
	public void addCreditCnt(Integer cnt) {
		if (null == this.creditCnt) {
			this.creditCnt = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.creditCnt = this.creditCnt + cnt;
	}
	
	public void addBalance(BigDecimal money) {
		if (null == this.balance) {
			this.balance = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.balance = this.balance.add(money);
	}
	public void addTotalDebitMoney(BigDecimal money) {
		if (null == this.totalDebitMoney) {
			this.totalDebitMoney = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.totalDebitMoney = this.totalDebitMoney.add(money);
	}
	public void addTotalCreditMoney(BigDecimal money) {
		if (null == this.totalCreditMoney) {
			this.totalCreditMoney = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.totalCreditMoney = this.totalCreditMoney.add(money);
	}
	
	public void addInitBalance(BigDecimal money) {
		if (null == this.initBalance) {
			this.initBalance = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.initBalance = this.initBalance.add(money);
	}
	
	/**
	 * 余额日期
	 */
	private Date balanceDate;
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	
}
