package com.bytter.erp.dto;
import java.util.Date;
/**
 * Title: 账号币别对应表
 */
public class BtBankAccCurDTO implements java.io.Serializable {
	private String id;
	/**
	 * 账号Id
	 */
	private String bankAccId;
	/**
	 * 币别
	 */
	private String curId;
	/**
	 * 关联银行账号Id
	 */
	private String btBankAccId;
	/**
	 * 关联内部账号
	 */
	private String counterAcc;
	/**
	 * 关联上级内部账号
	 */
	private String superCounterAcc;
	/**
	 * 银行账户流水截止时间
	 **/
	private Date endDtlDate;
	/**
	 * 父账号ID
	 */
	private String prentAccId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBankAccId() {
		return bankAccId;
	}
	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getBtBankAccId() {
		return btBankAccId;
	}
	public void setBtBankAccId(String btBankAccId) {
		this.btBankAccId = btBankAccId;
	}
	public String getCounterAcc() {
		return counterAcc;
	}
	public void setCounterAcc(String counterAcc) {
		this.counterAcc = counterAcc;
	}
	public String getSuperCounterAcc() {
		return superCounterAcc;
	}
	public void setSuperCounterAcc(String superCounterAcc) {
		this.superCounterAcc = superCounterAcc;
	}
	public Date getEndDtlDate() {
		return endDtlDate;
	}
	public void setEndDtlDate(Date endDtlDate) {
		this.endDtlDate = endDtlDate;
	}
	public String getPrentAccId() {
		return prentAccId;
	}
	public void setPrentAccId(String prentAccId) {
		this.prentAccId = prentAccId;
	}
}
