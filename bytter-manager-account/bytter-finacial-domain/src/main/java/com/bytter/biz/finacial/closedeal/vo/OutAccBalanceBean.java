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
public class OutAccBalanceBean implements Serializable {
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
	private Date opDate;
	/**
	 * 户名
	 */
	private String accName;
	/** 收入数量 */
	private java.lang.Integer debitNum;
	/** 付出数量 */
	private java.lang.Integer creditNum;
	/** 收入金额 */
	private java.math.BigDecimal debitMoney;
	/** 付出金额 */
	private java.math.BigDecimal creditMoney;
	/** 昨日余额 */
	private java.math.BigDecimal yBalance;
	/** 今天余额 */
	private java.math.BigDecimal tBalance;
	/** 昨日笔数 */
	private java.lang.Integer yCount;
	/** 今日笔数 */
	private java.lang.Integer tCount;
	/** 收方笔数 */
	private java.lang.Integer debitCount;
	/** 付方笔数 */
	private java.lang.Integer creditCount;
	/** 昨日数量 */
	private java.lang.Integer yNum;
	/** 今日数量 */
	private java.lang.Integer tNum;
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
	
	public OutAccBalanceBean() {
		this.debitNum = Integer.valueOf(0);
		this.creditNum = Integer.valueOf(0);
		this.debitMoney = BigDecimal.ZERO;
		this.creditMoney = BigDecimal.ZERO;
		this.yBalance = BigDecimal.ZERO;
		this.tBalance = BigDecimal.ZERO;
		this.yCount = Integer.valueOf(0);
		this.tCount = Integer.valueOf(0);
		this.debitCount = Integer.valueOf(0);
		this.creditCount = Integer.valueOf(0);
		this.yNum = Integer.valueOf(0);
		this.tNum = Integer.valueOf(0);
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
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
	public Date getOpDate() {
		return opDate;
	}
	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
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
	public java.lang.Integer getDebitNum() {
		return debitNum;
	}
	public void setDebitNum(java.lang.Integer debitNum) {
		this.debitNum = debitNum;
	}
	public java.lang.Integer getCreditNum() {
		return creditNum;
	}
	public void setCreditNum(java.lang.Integer creditNum) {
		this.creditNum = creditNum;
	}
	public java.math.BigDecimal getyBalance() {
		if (null == this.yBalance) {
			return BigDecimal.ZERO;
		}
		return yBalance;
	}
	public void setyBalance(java.math.BigDecimal yBalance) {
		if (null == yBalance) {
			yBalance = BigDecimal.ZERO;
		} 
		this.yBalance = yBalance;
	}
	public java.math.BigDecimal gettBalance() {
		if (null == this.tBalance) {
			return BigDecimal.ZERO;
		}
		return tBalance;
	}
	public void settBalance(java.math.BigDecimal tBalance) {
		this.tBalance = tBalance;
	}
	public java.lang.Integer getyCount() {
		if (null == this.yCount) {
			return Integer.valueOf(0);
		}
		return yCount;
	}
	public void setyCount(java.lang.Integer yCount) {
		if (null == yCount) {
			yCount = Integer.valueOf(0);
		}
		this.yCount = yCount;
	}
	public java.lang.Integer gettCount() {
		if (null == this.tCount) {
			return Integer.valueOf(0);
		}
		return tCount;
	}
	public void settCount(java.lang.Integer tCount) {
		this.tCount = tCount;
	}
	public java.lang.Integer getDebitCount() {
		return debitCount;
	}
	public void setDebitCount(java.lang.Integer debitCount) {
		this.debitCount = debitCount;
	}
	public java.lang.Integer getCreditCount() {
		return creditCount;
	}
	public void setCreditCount(java.lang.Integer creditCount) {
		this.creditCount = creditCount;
	}
	public java.lang.Integer getyNum() {
		if (null == this.yNum) {
			return Integer.valueOf(0);
		}
		return yNum;
	}
	public void setyNum(java.lang.Integer yNum) {
		if (null == yNum) {
			yNum = Integer.valueOf(0);
		} 
		this.yNum = yNum;
	}
	public java.lang.Integer gettNum() {
		if (null == this.tNum) {
			return Integer.valueOf(0);
		}
		return tNum;
	}
	public void settNum(java.lang.Integer tNum) {
		this.tNum = tNum;
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
	 * 借方金额合计
	 */
	public void addDebitBalance(BigDecimal money) {
		if (null == this.debitMoney) {
			this.debitMoney = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.debitMoney = this.debitMoney.add(money);
	}
	/**
	 * 贷方金额合计
	 */
	public void addCreditBalance(BigDecimal money) {
		if (null == this.creditMoney) {
			this.creditMoney = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.creditMoney = this.creditMoney.add(money);
	}
	public void addYBalance(BigDecimal money) {
		if (null == this.yBalance) {
			this.yBalance = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.yBalance = this.yBalance.add(money);
	}
	public void addTBalance(BigDecimal money) {
		if (null == this.tBalance) {
			this.tBalance = BigDecimal.ZERO;
		}
		if (null == money) {
			money = BigDecimal.ZERO;
		}
		this.tBalance = this.tBalance.add(money);
	}
	public void addDebitNum(Integer cnt) {
		if (null == this.debitNum) {
			this.debitNum = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.debitNum = this.debitNum + cnt;
	}
	public void addCreditNum(Integer cnt) {
		if (null == this.creditNum) {
			this.creditNum = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.creditNum = this.creditNum + cnt;
	}
	public void addTNum(Integer cnt) {
		if (null == this.tNum) {
			this.tNum = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.tNum = this.tNum + cnt;
	}
	public void addYNum(Integer cnt) {
		if (null == this.yNum) {
			this.yNum = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.yNum = this.yNum + cnt;
	}
	public void addTCount(Integer cnt) {
		if (null == this.tCount) {
			this.tCount = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.tCount = this.tCount + cnt;
	}
	public void addYCount(Integer cnt) {
		if (null == this.yCount) {
			this.yCount = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.yCount = this.yCount + cnt;
	}
	public void addDebitCount(Integer cnt) {
		if (null == this.debitCount) {
			this.debitCount = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.debitCount = this.debitCount + cnt;
	}
	public void addCreditCount(Integer cnt) {
		if (null == this.creditCount) {
			this.creditCount = Integer.valueOf(0);
		}
		if (null == cnt) {
			cnt = Integer.valueOf(0);
		}
		this.creditCount = this.creditCount + cnt;
	}
}
