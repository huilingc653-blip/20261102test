package com.bytter.biz.finacial.interest.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcInterestDtlTempVo implements Serializable{
	
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 户名
	 */
	private String accName;
	/**
	 * 起息日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestStartDate;
	/**
	 * 结束日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date blnInstDate;
	/**
	 * 天数
	 */
	private Integer days;
	/**
	 * 积数
	 */
	private BigDecimal cumulation;
	/**
	 * 利率
	 */
	private BigDecimal interestRate;
	/**
	 * 利息
	 */
	private BigDecimal interest;
	/**
	 * 利率类型
	 */
	private String interestType;
	/**
	 * 计息类型
	 */
	private String interestSign;
	/**
	 * 状态
	 */
	private Integer busStatus;
	/**经办人*/
	private String operatorCode;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public Date getBlnInstDate() {
		return blnInstDate;
	}
	public void setBlnInstDate(Date blnInstDate) {
		this.blnInstDate = blnInstDate;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public BigDecimal getCumulation() {
		return cumulation;
	}
	public void setCumulation(BigDecimal cumulation) {
		this.cumulation = cumulation;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public String getInterestType() {
		return interestType;
	}
	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}
	public String getInterestSign() {
		return interestSign;
	}
	public void setInterestSign(String interestSign) {
		this.interestSign = interestSign;
	}
	public Integer getBusStatus() {
		return busStatus;
	}
	public void setBusStatus(Integer busStatus) {
		this.busStatus = busStatus;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
}
