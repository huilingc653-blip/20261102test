package com.bytter.system.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: BT_BANK_ACC_CUR
 * @author onlineGenerator
 * @date 2017-06-02 18:03:22
 * @version 1.0
 *
 */
@Entity
@Table(name = "BT_BANK_ACC_CUR", schema = "")
@SuppressWarnings("serial")
public class BtBankAccCur extends IdEntity implements java.io.Serializable {
	/**账号Id*/
	private java.lang.String bankAccId;
	/**币别*/
	private java.lang.String curId;
	
	private BtBankAcc btBankAcc;
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号Id
	 */
	@Column(name ="BANK_ACC_ID",nullable=false,length=32)
	public java.lang.String getBankAccId(){
		return this.bankAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号Id
	 */
	public void setBankAccId(java.lang.String bankAccId){
		this.bankAccId = bankAccId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_ID",nullable=true,length=2)
	public java.lang.String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别
	 */
	public void setCurId(java.lang.String curId){
		this.curId = curId;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BANK_ACC_ID",insertable= false,updatable=false)
	public BtBankAcc getBtBankAcc() {
		return btBankAcc;
	}
	public void setBtBankAcc(BtBankAcc btBankAcc) {
		this.btBankAcc = btBankAcc;
	}
	@Column(name ="COUNTER_ACC",nullable=true,length=32)
	public String getCounterAcc() {
		return counterAcc;
	}
	public void setCounterAcc(String counterAcc) {
		this.counterAcc = counterAcc;
	}
	@Column(name ="SUPER_COUNTER_ACC",nullable=true,length=32)
	public String getSuperCounterAcc() {
		return superCounterAcc;
	}
	public void setSuperCounterAcc(String superCounterAcc) {
		this.superCounterAcc = superCounterAcc;
	}
	@Column(name = "END_DTL_DATE", nullable = true)
	public Date getEndDtlDate() {
		return endDtlDate;
	}
	public void setEndDtlDate(Date endDtlDate) {
		this.endDtlDate = endDtlDate;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  父账号
	 */
	@Column(name = "PRENT_ACC_ID", nullable = true, length = 32)
	public java.lang.String getPrentAccId() {
		return this.prentAccId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  父账号
	 */
	public void setPrentAccId(java.lang.String prentAccId) {
		this.prentAccId = prentAccId;
	}
}
