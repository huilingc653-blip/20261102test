package com.bytter.biz.finacial.interest.vo;
import java.io.Serializable;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcPredrawDtlVo implements Serializable{
	
	/**账号*/
	@BtCompositeSearch(fieldName = "p.account")
	private java.lang.String account;
	/**账户名称*/
	private String accName;
	/**流水号*/
	@BtCompositeSearch(fieldName = "p.order_no")
	private java.lang.String orderNo;
	/**预提日期--利息预提情况表查询条件**/
	@BtCompositeSearch(fieldName = "p.interest_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date interestTime;
	
	private Date ge_interestTime;
	private Date le_interestTime;
	
	/**起息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestStartDate;
	
	/**结息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date blnInstDate;
	/**利率*/
	private java.math.BigDecimal interestRate;
	/**利息*/
	private java.math.BigDecimal interest;
	/**冲预提日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date strikeDate;
	/**冲预提金额**/
	private java.math.BigDecimal strikeInterest;
	/**网点**/
	@BtCompositeSearch(fieldName = "p.net_id")
	private String netId;
	/**单位**/
	@BtCompositeSearch(fieldName = "a.corp_id")
	private String corpId;
	private String corpName;
	/**币别**/
	@BtCompositeSearch(fieldName = "p.cur_code")
	private String curCode;
	/**页码**/
	private  Integer pageNum;
	/**开户种类**/
//	@BtCompositeSearch(fieldName = "a.acc_type")
	private String accType;
	/**天数**/
	private Integer days;
	/**利息类型**/
	private String interestType;
    private String id;
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
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
	public java.lang.String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
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
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Date getGe_interestTime() {
		return ge_interestTime;
	}
	public void setGe_interestTime(Date ge_interestTime) {
		this.ge_interestTime = ge_interestTime;
	}
	public Date getLe_interestTime() {
		return le_interestTime;
	}
	public void setLe_interestTime(Date le_interestTime) {
		this.le_interestTime = le_interestTime;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
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
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public Date getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
