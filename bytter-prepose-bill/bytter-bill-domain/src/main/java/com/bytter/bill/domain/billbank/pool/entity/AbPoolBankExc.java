package com.bytter.bill.domain.billbank.pool.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 票据池银行接口状态中间表
 * @author zhengg
 * @date 2020-02-24 14:40:39
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_BANK_EXC", schema = "")
@SuppressWarnings("serial")
public class AbPoolBankExc implements java.io.Serializable {
	private String id;
	
	/**bankId*/
	private String bankId;
	/**applyId*/
	private String applyId;
	/**reCode*/
	private String reCode;
	/**reMsg*/
	private String reMsg;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  bankId
	 */
	@Column(name ="BANK_ID",nullable=true,length=32)
	public String getBankId(){
		return this.bankId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  bankId
	 */
	public void setBankId(String bankId){
		this.bankId = bankId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  applyId
	 */
	@Column(name ="APPLY_ID",nullable=true,length=32)
	public String getApplyId(){
		return this.applyId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  applyId
	 */
	public void setApplyId(String applyId){
		this.applyId = applyId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  reCode
	 */
	@Column(name ="RE_CODE",nullable=true,length=10)
	public String getReCode(){
		return this.reCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  reCode
	 */
	public void setReCode(String reCode){
		this.reCode = reCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  reMsg
	 */
	@Column(name ="RE_MSG",nullable=true,length=500)
	public String getReMsg(){
		return this.reMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  reMsg
	 */
	public void setReMsg(String reMsg){
		this.reMsg = reMsg;
	}
	@Id
    @Column(name = "ID", unique = true, length = 32)
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
	@Override
	public String toString() {
		return "AbPoolBankExc ["+
		"id=" + 
		getId() + ", "+
		"bankId=" + 
		bankId + ", "+
		"applyId=" + 
		applyId + ", "+
		"reCode=" + 
		reCode + ", "+
		"reMsg=" + 
		reMsg + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
