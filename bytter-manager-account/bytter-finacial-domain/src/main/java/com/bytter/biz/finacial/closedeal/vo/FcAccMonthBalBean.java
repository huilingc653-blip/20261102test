package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 月总量表Bean
 * 
 * @author liyh
 *
 */
public class FcAccMonthBalBean implements Serializable {
	/** 年月 */
	private java.lang.String yearMonth2;
	/** 账号 */
	private java.lang.String account;
	/** 账户名称 */
	private java.lang.String accName;
	/** 内转笔数 */
	private java.lang.Integer trCnt;
	/** 冲正笔数 */
	private java.lang.Integer rushCnt;
	/** 笔数 */
	private java.lang.Integer cnt;
	/** 内转借方发生额 */
	private java.math.BigDecimal trDebitMoney;
	/** 内转贷方发生额 */
	private java.math.BigDecimal trCreditMoney;
	/** 冲正借方发生额 */
	private java.math.BigDecimal rushDebitMoney;
	/** 冲正贷方发生额 */
	private java.math.BigDecimal rushCreditMoney;
	/** 借方发生额 */
	private java.math.BigDecimal debitMoney;
	/** 贷方发生额 */
	private java.math.BigDecimal creditMoney;
	/** 上月余额 */
	private java.math.BigDecimal preBalance;
	/** 本月余额 */
	private java.math.BigDecimal balance;
	public java.lang.String getYearMonth2() {
		return yearMonth2;
	}
	public void setYearMonth2(java.lang.String yearMonth2) {
		this.yearMonth2 = yearMonth2;
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
	public java.math.BigDecimal getPreBalance() {
		if (null == this.preBalance) {
			return BigDecimal.ZERO;
		}
		return preBalance;
	}
	public void setPreBalance(java.math.BigDecimal preBalance) {
		this.preBalance = preBalance;
	}
	public java.math.BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(java.math.BigDecimal balance) {
		this.balance = balance;
	}
	public void addTrCnt(Integer cnt) {
		if (null == this.trCnt) {
			this.trCnt = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.trCnt = this.trCnt + cnt;
	}
	public void addRushCnt(Integer cnt) {
		if (null == this.rushCnt) {
			this.rushCnt = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.rushCnt = this.rushCnt + cnt;
	}
	public void addCnt(Integer cnt) {
		if (null == this.cnt) {
			this.cnt = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.cnt = this.cnt + cnt;
	}
	public void addTrDebitMoney(BigDecimal money) {
		if (null == this.trDebitMoney) {
			this.trDebitMoney = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.trDebitMoney = this.trDebitMoney.add(money);
	}
	public void addTrCreditMoney(BigDecimal money) {
		if (null == this.trCreditMoney) {
			this.trCreditMoney = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.trCreditMoney = this.trCreditMoney.add(money);
	}
	public void addRushDebitMoney(BigDecimal money) {
		if (null == this.rushDebitMoney) {
			this.rushDebitMoney = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.rushDebitMoney = this.rushDebitMoney.add(money);
	}
	public void addRushCreditMoney(BigDecimal money) {
		if (null == this.rushCreditMoney) {
			this.rushCreditMoney = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.rushCreditMoney = this.rushCreditMoney.add(money);
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
	public void addPreBalance(BigDecimal money) {
		if (null == this.preBalance) {
			this.preBalance = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.preBalance = this.preBalance.add(money);
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
}
