package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 科目日结单Bean
 * 
 * @author liyh
 *
 */
public class FcOutSubjectDayBean implements Serializable {
	/** 科目代码 */
	private java.lang.String subjectCode;
	/** 所属网点 */
	private java.lang.String netId;
	/** 币别代码 */
	private java.lang.String curCode;
	/** 日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date optDate;
	/** 收方笔数 */
	private java.lang.Integer debitCount;
	/** 付方笔数 */
	private java.lang.Integer creditCount;
	/** 收方发生额 */
	private java.math.BigDecimal debitMoney;
	/** 付方发生额 */
	private java.math.BigDecimal creditMoney;
	/** 昨日余额 */
	private java.math.BigDecimal yBalance;
	/** 收方数量 */
	private java.lang.Integer debitNum;
	/** 付方数量 */
	private java.lang.Integer creditNum;
	/** 今日余额 */
	private java.math.BigDecimal tBalance;
	/** 昨日笔数 */
	private java.lang.Integer yCount;
	/** 今日笔数 */
	private java.lang.Integer tCount;
	/** 收方张数 */
	private java.lang.Integer debitPageNum;
	/** 付方张数 */
	private java.lang.Integer creditPageNum;
	/** 昨日张数 */
	private java.lang.Integer yPageNum;
	/** 今日张数 */
	private java.lang.Integer tPageNum;
	/** 昨日数量 */
	private java.lang.Integer yNum;
	/** 今日数量 */
	private java.lang.Integer tNum;
	public FcOutSubjectDayBean() {
		
	}
	public FcOutSubjectDayBean(String netId, String subjectId, String curId) {
		this.netId = netId;
		this.subjectCode = subjectId;
		this.curCode = curId;
	}
	
	public java.lang.String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(java.lang.String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	public java.util.Date getOptDate() {
		return optDate;
	}
	public void setOptDate(java.util.Date optDate) {
		this.optDate = optDate;
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
	public java.math.BigDecimal getyBalance() {
		return yBalance;
	}
	public void setyBalance(java.math.BigDecimal yBalance) {
		this.yBalance = yBalance;
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
	public java.math.BigDecimal gettBalance() {
		return tBalance;
	}
	public void settBalance(java.math.BigDecimal tBalance) {
		this.tBalance = tBalance;
	}
	public java.lang.Integer getyCount() {
		return yCount;
	}
	public void setyCount(java.lang.Integer yCount) {
		this.yCount = yCount;
	}
	public java.lang.Integer gettCount() {
		return tCount;
	}
	public void settCount(java.lang.Integer tCount) {
		this.tCount = tCount;
	}
	public java.lang.Integer getDebitPageNum() {
		return debitPageNum;
	}
	public void setDebitPageNum(java.lang.Integer debitPageNum) {
		this.debitPageNum = debitPageNum;
	}
	public java.lang.Integer getCreditPageNum() {
		return creditPageNum;
	}
	public void setCreditPageNum(java.lang.Integer creditPageNum) {
		this.creditPageNum = creditPageNum;
	}
	public java.lang.Integer getyPageNum() {
		return yPageNum;
	}
	public void setyPageNum(java.lang.Integer yPageNum) {
		this.yPageNum = yPageNum;
	}
	public java.lang.Integer gettPageNum() {
		return tPageNum;
	}
	public void settPageNum(java.lang.Integer tPageNum) {
		this.tPageNum = tPageNum;
	}
	public java.lang.Integer getyNum() {
		return yNum;
	}
	public void setyNum(java.lang.Integer yNum) {
		this.yNum = yNum;
	}
	public java.lang.Integer gettNum() {
		return tNum;
	}
	public void settNum(java.lang.Integer tNum) {
		this.tNum = tNum;
	}
	
	/**
	 * 借方金额合计
	 */
	public void addDebitMoney(BigDecimal money) {
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
	public void addCreditMoney(BigDecimal money) {
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
