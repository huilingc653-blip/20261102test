package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 账户余额Bean
 * 
 * @author liyh
 *
 */
public class AccBalanceBean implements Serializable {
	/**
	 * 账户
	 */
	private String account;
	
	/**
	 * 账户启动日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date accStartDate;
	
	/**
	 * 币别
	 */
	private String curId;
	
	/**
	 * 科目
	 */
	private String subjectId;
	/**
	 * 余额日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date balanceDate;
	/**
	 * 户名
	 */
	private String accName;
	/** 内转笔数 */
	private java.lang.Integer trCnt = new Integer(0);
	/** 冲正笔数 */
	private java.lang.Integer rushCnt = new Integer(0);
	/** 笔数 */
	private java.lang.Integer cnt = new Integer(0);
	/** 昨日余额 */
	private java.math.BigDecimal yesterBalance = BigDecimal.ZERO;
	/** 余额 */
	private java.math.BigDecimal balance = BigDecimal.ZERO;
	/** 内转借方发生额 */
	private java.math.BigDecimal trDebitMoney = BigDecimal.ZERO;
	/** 内转贷方发生额 */
	private java.math.BigDecimal trCreditMoney = BigDecimal.ZERO;
	/** 冲正借方发生额 */
	private java.math.BigDecimal rushDebitMoney = BigDecimal.ZERO;
	/** 冲正贷方发生额 */
	private java.math.BigDecimal rushCreditMoney = BigDecimal.ZERO;
	/** 借方发生额 */
	private java.math.BigDecimal debitMoney = BigDecimal.ZERO;
	/** 贷方发生额 */
	private java.math.BigDecimal creditMoney = BigDecimal.ZERO;
	/** 借方笔数 */
	private java.lang.Integer debitCnt = new Integer(0);
	/** 贷方笔数 */
	private java.lang.Integer creditCnt = new Integer(0);
	
	/**
	 * 注销日期
	 */
	private Date cancelDate;
	
	/**
	 * 余额保存标识
	 */
	private boolean saveFlag = true;
	
	/**
	 * 记账日标识
	 */
	private String tallyDaySign;
	
	private Integer balanceWay;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Date getAccStartDate() {
		return accStartDate;
	}
	public void setAccStartDate(Date accStartDate) {
		this.accStartDate = accStartDate;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public java.lang.Integer getTrCnt() {
		if (null == this.trCnt) {
			return Integer.valueOf(0);
		}
		return trCnt;
	}
	public void setTrCnt(java.lang.Integer trCnt) {
		this.trCnt = trCnt;
	}
	public java.lang.Integer getRushCnt() {
		if (null == this.rushCnt) {
			return Integer.valueOf(0);
		}
		return rushCnt;
	}
	public void setRushCnt(java.lang.Integer rushCnt) {
		this.rushCnt = rushCnt;
	}
	public java.lang.Integer getCnt() {
		if (null == this.cnt) {
			return Integer.valueOf(0);
		}
		return cnt;
	}
	public void setCnt(java.lang.Integer cnt) {
		this.cnt = cnt;
	}
	public java.math.BigDecimal getYesterBalance() {
		if (null == this.yesterBalance) {
			return BigDecimal.ZERO;
		}
		return yesterBalance;
	}
	public void setYesterBalance(java.math.BigDecimal yesterBalance) {
		this.yesterBalance = yesterBalance;
	}
	public java.math.BigDecimal getBalance() {
		if (null == this.balance) {
			return BigDecimal.ZERO;
		}
		return balance;
	}
	public void setBalance(java.math.BigDecimal balance) {
		this.balance = balance;
	}
	public java.math.BigDecimal getTrDebitMoney() {
		if (null == this.trDebitMoney) {
			return BigDecimal.ZERO;
		}
		return trDebitMoney;
	}
	public void setTrDebitMoney(java.math.BigDecimal trDebitMoney) {
		this.trDebitMoney = trDebitMoney;
	}
	public java.math.BigDecimal getTrCreditMoney() {
		if (null == this.trCreditMoney) {
			return BigDecimal.ZERO;
		}
		return trCreditMoney;
	}
	public void setTrCreditMoney(java.math.BigDecimal trCreditMoney) {
		this.trCreditMoney = trCreditMoney;
	}
	public java.math.BigDecimal getRushDebitMoney() {
		if (null == this.rushDebitMoney) {
			return BigDecimal.ZERO;
		}
		return rushDebitMoney;
	}
	public void setRushDebitMoney(java.math.BigDecimal rushDebitMoney) {
		this.rushDebitMoney = rushDebitMoney;
	}
	public java.math.BigDecimal getRushCreditMoney() {
		if (null == this.rushCreditMoney) {
			return BigDecimal.ZERO;
		}
		return rushCreditMoney;
	}
	public void setRushCreditMoney(java.math.BigDecimal rushCreditMoney) {
		this.rushCreditMoney = rushCreditMoney;
	}
	public java.math.BigDecimal getDebitMoney() {
		if (null == this.debitMoney) {
			return BigDecimal.ZERO;
		}
		return debitMoney;
	}
	public void setDebitMoney(java.math.BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public java.math.BigDecimal getCreditMoney() {
		if (null == this.creditMoney) {
			return BigDecimal.ZERO;
		}
		return creditMoney;
	}
	public void setCreditMoney(java.math.BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public java.lang.Integer getDebitCnt() {
		if (null == this.debitCnt) {
			return Integer.valueOf(0);
		}
		return debitCnt;
	}
	public void setDebitCnt(java.lang.Integer debitCnt) {
		this.debitCnt = debitCnt;
	}
	public java.lang.Integer getCreditCnt() {
		if (null == this.creditCnt) {
			return Integer.valueOf(0);
		}
		return creditCnt;
	}
	public void setCreditCnt(java.lang.Integer creditCnt) {
		this.creditCnt = creditCnt;
	}
	
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	public boolean isSaveFlag() {
		return saveFlag;
	}
	public void setSaveFlag(boolean saveFlag) {
		this.saveFlag = saveFlag;
	}
	public String getTallyDaySign() {
		return tallyDaySign;
	}
	public void setTallyDaySign(String tallyDaySign) {
		this.tallyDaySign = tallyDaySign;
	}
	/**
	 * 内转笔数 +1
	 */
	public void addTrCnt(Integer cnt){
		if (null == this.trCnt) {
			this.trCnt = Integer.valueOf(0);
		}
		this.trCnt = this.trCnt + cnt;
	}
	/**
	 * 冲账笔数 +1
	 */
	public void addTrRush(Integer cnt){
		if (null == this.rushCnt) {
			this.rushCnt = Integer.valueOf(0);
		}
		this.rushCnt = this.rushCnt + cnt;
	}
	/**
	 * 总笔数 +1
	 */
	public void addCnt(Integer cnt){
		if (null == this.cnt) {
			this.cnt = Integer.valueOf(0);
		}
		this.cnt = this.cnt + cnt;
	}
	/**
	 * 内转借方金额合计
	 */
	public void addTrDebitBalance(BigDecimal money){
		if (null == this.trDebitMoney) {
			this.trDebitMoney = BigDecimal.ZERO;
		}
		this.trDebitMoney = this.trDebitMoney.add(money);
	}
	/**
	 * 内转贷方金额合计
	 */
	public void addTrCreditBalance(BigDecimal money){
		if (null == this.trCreditMoney) {
			this.trCreditMoney = BigDecimal.ZERO;
		}
		this.trCreditMoney = this.trCreditMoney.add(money);
	}
	/**
	 * 冲账借方金额合计
	 */
	public void addRushDebitBalance(BigDecimal money){
		if (null == this.rushDebitMoney) {
			this.rushDebitMoney = BigDecimal.ZERO;
		}
		this.rushDebitMoney = this.rushDebitMoney.add(money);
	}
	/**
	 * 冲账贷方金额合计
	 */
	public void addRushCreditBalance(BigDecimal money){
		if (null == this.rushCreditMoney) {
			this.rushCreditMoney = BigDecimal.ZERO;
		}
		this.rushCreditMoney = this.rushCreditMoney.add(money);
	}
	/**
	 * 借方金额合计
	 */
	public void addDebitBalance(BigDecimal money){
		if (null == this.debitMoney) {
			this.debitMoney = BigDecimal.ZERO;
		}
		this.debitMoney = this.debitMoney.add(money);
	}
	/**
	 * 贷方金额合计
	 */
	public void addCreditBalance(BigDecimal money){
		if (null == this.creditMoney) {
			this.creditMoney = BigDecimal.ZERO;
		}
		this.creditMoney = this.creditMoney.add(money);
	}
	/**
	 * 借方笔数 +1
	 */
	public void addDebitCnt(Integer cnt){
		if (null == this.debitCnt) {
			this.debitCnt = Integer.valueOf(0);
		}
		this.debitCnt = this.debitCnt + cnt;
	}
	/**
	 * 贷方笔数 +1
	 */
	public void addCreditCnt(Integer cnt){
		if (null == this.creditCnt) {
			this.creditCnt = Integer.valueOf(0);
		}
		this.creditCnt = this.creditCnt + cnt;
	}
	
	public void addBalance(BigDecimal money){
		if (null == this.balance) {
			this.balance = BigDecimal.ZERO;
		}
		this.balance = this.balance.add(money);
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
}
