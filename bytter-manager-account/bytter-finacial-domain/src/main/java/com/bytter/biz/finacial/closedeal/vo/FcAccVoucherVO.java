package com.bytter.biz.finacial.closedeal.vo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 账户账务发生额VO
 * 
 * @author liyh
 *
 */
@Data
public class FcAccVoucherVO implements Serializable {
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 户名
	 */
	private String accName;
	/**
	 * 币别
	 */
	private String curId;
	/**
	 * 币别名称
	 */
	private String curName;
	/**
	 * 余额方向
	 */
	private Integer balanceWay;
	/**
	 * 余额
	 */
	private BigDecimal balance;
	/**
	* 科目
	*/
	private String subjectCode;

	private String subjectName;
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
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
    public String getSubjectCode() {
        return subjectCode;
    }
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
