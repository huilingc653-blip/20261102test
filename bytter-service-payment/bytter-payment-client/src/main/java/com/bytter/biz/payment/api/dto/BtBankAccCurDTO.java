package com.bytter.biz.payment.api.dto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import java.util.Date;
/**
 * Title: 账号币别对应表
 */
public class BtBankAccCurDTO implements java.io.Serializable {
	private java.lang.String id;
	/**
	 * 账号Id
	 */
	private java.lang.String bankAccId;
	/**
	 * 币别
	 */
	private java.lang.String curId;
	/**
	 * 关联银行账号Id
	 */
	private java.lang.String btBankAccId;
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
	private java.lang.String prentAccId;
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getBankAccId() {
		return bankAccId;
	}
	public void setBankAccId(java.lang.String bankAccId) {
		this.bankAccId = bankAccId;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public java.lang.String getBtBankAccId() {
		return btBankAccId;
	}
	public void setBtBankAccId(java.lang.String btBankAccId) {
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
