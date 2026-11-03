package com.bytter.biz.finacial.interest.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
public class InterestBillExportVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 单位id
	 */
	private String corpId;
	/**
	 * 单位名称
	 */
	private String corpName;
	/**
	 * 币别
	 */
	private String curId;
	/**
	 * 币别名称
	 */
	private String curName;
	/**
	 * 账户种类
	 */
	private String accType;
	/**
	 * 账户种类名称
	 */
	private String accTypeName;
	/**
	 * 户名
	 */
	private String accName;
	/**
	 * 账号
	 */
	private String account;
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
	private String interestName;
	/**
	 * 计息类型
	 */
	private String interestSign;
	private String interestSignName;
	/**
	 * 状态
	 */
	
	private Integer busStatus;
	private String busStatusName;
	
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccTypeName() {
		return accTypeName;
	}
	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
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
	public String getInterestName() {
		return interestName;
	}
	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}
	public String getInterestSign() {
		return interestSign;
	}
	public void setInterestSign(String interestSign) {
		this.interestSign = interestSign;
	}
	public String getInterestSignName() {
		return interestSignName;
	}
	public void setInterestSignName(String interestSignName) {
		this.interestSignName = interestSignName;
	}
	public Integer getBusStatus() {
		return busStatus;
	}
	public void setBusStatus(Integer busStatus) {
		this.busStatus = busStatus;
	}
	public String getBusStatusName() {
		return busStatusName;
	}
	public void setBusStatusName(String busStatusName) {
		this.busStatusName = busStatusName;
	}
	
}
