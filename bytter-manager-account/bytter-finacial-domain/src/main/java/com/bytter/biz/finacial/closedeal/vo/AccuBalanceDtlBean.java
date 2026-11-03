package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 批量余额表余额Bean
 * @author liyh
 *
 */
public class AccuBalanceDtlBean implements Serializable {
	
	/**
	 * 账户启用日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date accStartDate;
	
	/**
	 * 余额日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date balanceDate;
	
	/**
	 * 账号
	 */
	private String account;
	
	/**
	 * 余额
	 */
	private BigDecimal balance;
	
	/**
	 * 开户种类
	 */
	private String accType;
	
	/**
	 * 币别
	 */
	private String curId;
	/**
	 * 计息标志
	 */
	private String interestSign;
	/**
	 * 预提标注
	 */
	private String preSign;
	public Date getAccStartDate() {
		return accStartDate;
	}
	public void setAccStartDate(Date accStartDate) {
		this.accStartDate = accStartDate;
	}
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
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getInterestSign() {
		return interestSign;
	}
	public void setInterestSign(String interestSign) {
		this.interestSign = interestSign;
	}
	public String getPreSign() {
		return preSign;
	}
	public void setPreSign(String preSign) {
		this.preSign = preSign;
	}
}
