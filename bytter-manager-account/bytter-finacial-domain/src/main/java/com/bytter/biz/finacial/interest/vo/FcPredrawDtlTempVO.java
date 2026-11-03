package com.bytter.biz.finacial.interest.vo;
import java.io.Serializable;
import java.util.List;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcPredrawDtlTempVO implements Serializable {
	private java.lang.String id;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestTime;
	/**流水号*/
	private java.lang.String orderNo;
	/**账号*/
	private java.lang.String account;
	/**币别代码*/
	private java.lang.String curCode;
	/**利息*/
	private java.math.BigDecimal interest;
	/**余额*/
	private java.math.BigDecimal balance;
	/**冲账标志*/
	private java.lang.String strikeSign;
	/**冲账日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date strikeDate;
	/**冲账利息*/
	private java.math.BigDecimal strikeInterest;
	/**结息标志*/
	private java.lang.String sign;
	private List<FcInterestDtlTemp> interestDtls;
	
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.util.Date getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(java.util.Date interestTime) {
		this.interestTime = interestTime;
	}
	public java.lang.String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	public java.math.BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(java.math.BigDecimal interest) {
		this.interest = interest;
	}
	public java.math.BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(java.math.BigDecimal balance) {
		this.balance = balance;
	}
	public java.lang.String getStrikeSign() {
		return strikeSign;
	}
	public void setStrikeSign(java.lang.String strikeSign) {
		this.strikeSign = strikeSign;
	}
	public java.util.Date getStrikeDate() {
		return strikeDate;
	}
	public void setStrikeDate(java.util.Date strikeDate) {
		this.strikeDate = strikeDate;
	}
	public java.math.BigDecimal getStrikeInterest() {
		return strikeInterest;
	}
	public void setStrikeInterest(java.math.BigDecimal strikeInterest) {
		this.strikeInterest = strikeInterest;
	}
	public java.lang.String getSign() {
		return sign;
	}
	public void setSign(java.lang.String sign) {
		this.sign = sign;
	}
	public List<FcInterestDtlTemp> getInterestDtls() {
		return interestDtls;
	}
	public void setInterestDtls(List<FcInterestDtlTemp> interestDtls) {
		this.interestDtls = interestDtls;
	}
	
	
}
