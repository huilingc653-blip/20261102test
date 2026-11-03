package com.bytter.biz.finacial.interest.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * <br>
 * <b>类功能：</b>不足款清单<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月11日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public class FcLessPayBillVO implements Serializable {
	
	private String id;
	/**
	 * 放款账号
	 */
	private String account;
	/**
	 * 应收利息账号
	 */
	private String needinterestAcc;
	/**
	 * 户名
	 */
	private String accName;
	/**
	 * 应收利息账号户名
	 */
	private String needinterestAccName;
	/**
	 * 币别
	 */
	private String curCode;
	/**
	 * 币别名称
	 */
	private String curName;
	/**
	 * 利息
	 */
	private BigDecimal interest;
	/**
	 * 扣款账号
	 */
	private String accForDeduct;
	/**
	 * 扣款账号户名
	 */
	private String accDeductName;
	/**
	 * 扣款当前可用余额
	 */
	private BigDecimal balance;
	/**
	 * 录入时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date enterDate;
	/**
	 * 是否已还
	 */
	private java.lang.String isRepay;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getNeedinterestAcc() {
		return needinterestAcc;
	}
	public void setNeedinterestAcc(String needinterestAcc) {
		this.needinterestAcc = needinterestAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getNeedinterestAccName() {
		return needinterestAccName;
	}
	public void setNeedinterestAccName(String needinterestAccName) {
		this.needinterestAccName = needinterestAccName;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public String getAccForDeduct() {
		return accForDeduct;
	}
	public void setAccForDeduct(String accForDeduct) {
		this.accForDeduct = accForDeduct;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getAccDeductName() {
		return accDeductName;
	}
	public void setAccDeductName(String accDeductName) {
		this.accDeductName = accDeductName;
	}
	public Date getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public java.lang.String getIsRepay() {
		return isRepay;
	}
	public void setIsRepay(java.lang.String isRepay) {
		this.isRepay = isRepay;
	}
	
}
