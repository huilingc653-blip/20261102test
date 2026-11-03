package com.bytter.biz.finacial.interest.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
public class AccuBillsVO implements Serializable {
	/**日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date balanceDate;
	/**账号*/
	private String account;
	/**户名*/
	private String accName;
	/**积数*/
	private BigDecimal cumu;
	/**调整积数*/
	private BigDecimal adjustCumu;
	/**开户种类*/
	private String accType;
	
	public Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}
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
	public BigDecimal getCumu() {
		return cumu;
	}
	public void setCumu(BigDecimal cumu) {
		this.cumu = cumu;
	}
	public BigDecimal getAdjustCumu() {
		return adjustCumu;
	}
	public void setAdjustCumu(BigDecimal adjustCumu) {
		this.adjustCumu = adjustCumu;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
}
