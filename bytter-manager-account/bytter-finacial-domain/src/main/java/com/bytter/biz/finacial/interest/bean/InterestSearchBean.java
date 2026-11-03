package com.bytter.biz.finacial.interest.bean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class InterestSearchBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String netId;
    private String accType;
    private Date tallyDate;
    private Date blnInstDate;
    private String account = "";
    private BigDecimal strikeInterest;
    private String userId;
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
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public Date getBlnInstDate() {
		return blnInstDate;
	}
	public void setBlnInstDate(Date blnInstDate) {
		this.blnInstDate = blnInstDate;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public BigDecimal getStrikeInterest() {
		return strikeInterest;
	}
	public void setStrikeInterest(BigDecimal strikeInterest) {
		this.strikeInterest = strikeInterest;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    
    
}
