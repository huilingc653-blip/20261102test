package com.bytter.biz.finacial.interest.vo;
import java.io.Serializable;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
public class InterestFactSheetVo implements Serializable{
	/**单位**/
	@BtCompositeSearch(fieldName = "p.corp_id")
	private String corpId;
	private String corpName;
	
	/**账号*/
	@BtCompositeSearch(fieldName = "p.account")
	private java.lang.String account;
	
	/**账户名称*/
	private String accName;
	
	/**开户种类**/
	@BtCompositeSearch(fieldName = "p.acc_type")
	private String accType;
	
	/**预提日期--利息预提情况表查询条件**/
	@BtCompositeSearch(fieldName = "p.interest_time")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestTime;
	
	/**起息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestStartDate;
	
	/**结息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date blnInstDate;
	
	/**天数**/
	private Integer days;
	
	/**利息类型**/
	private String interestType;
	
	/**计息标志**/
	private String interestSign;
	/**利率*/
	private java.math.BigDecimal interestRate;
	
	/**利息*/
	private java.math.BigDecimal interest;
	
	/**内部账号**/
	@BtCompositeSearch(fieldName = "a.blnctr_acc")
	private String blnctrAcc;
	
	/**币别**/
	@BtCompositeSearch(fieldName = "p.cur_id")
	private String curCode;
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
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public Date getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}
	public java.util.Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(java.util.Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public java.util.Date getBlnInstDate() {
		return blnInstDate;
	}
	public void setBlnInstDate(java.util.Date blnInstDate) {
		this.blnInstDate = blnInstDate;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
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
	public java.math.BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(java.math.BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public java.math.BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(java.math.BigDecimal interest) {
		this.interest = interest;
	}
	public String getBlnctrAcc() {
		return blnctrAcc;
	}
	public void setBlnctrAcc(String blnctrAcc) {
		this.blnctrAcc = blnctrAcc;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	
}
