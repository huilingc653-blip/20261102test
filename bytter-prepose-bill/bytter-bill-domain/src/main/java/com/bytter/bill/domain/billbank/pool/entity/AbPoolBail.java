package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 保证金信息表
 * @author zhengg
 * @date 2020-02-24 14:40:38
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_BAIL", schema = "")
@SuppressWarnings("serial")
public class AbPoolBail extends IdEntity implements java.io.Serializable {
	/**单位ID*/
	private String corpId;
	/**票据池ID*/
	private String bpId;
	/**保证金账户*/
	private String bankAcc;
	/**保证金余额*/
	private java.math.BigDecimal bailMoney;
	/**更新日期*/
	private java.util.Date bailDate;
	/**状态：1有效、0无效 */
	private String validSign;
	/**更新日期*/
	private java.util.Date createTime;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=true,length=32)
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  池接口代码
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据池ID
	 */
	@Column(name ="BP_ID",nullable=true,length=32)
	public String getBpId(){
		return this.bpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据池ID
	 */
	public void setBpId(String bpId){
		this.bpId = bpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据池ID
	 */
	@Column(name ="BANK_ACC",nullable=true,length=50)
	public String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据池ID
	 */
	public void setBankAcc(String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  备注
	 */
	@Column(name ="BAIL_MONEY",nullable=true,precision=15,scale=2)
	public java.math.BigDecimal getBailMoney(){
		return this.bailMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  备注
	 */
	public void setBailMoney(java.math.BigDecimal bailMoney){
		this.bailMoney = bailMoney;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="BAIL_DATE",nullable=true)
	public java.util.Date getBailDate(){
		return this.bailDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setBailDate(java.util.Date bailDate){
		this.bailDate = bailDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态：1有效、0无效
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=2)
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态：1有效、0无效
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="CREATE_TIME",nullable=true,scale=6)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return "AbPoolBail ["+
		"id=" + 
		getId() + ", "+
		"corpId=" + 
		corpId + ", "+
		"bpId=" + 
		bpId + ", "+
		"bankAcc=" + 
		bankAcc + ", "+
		"bailMoney=" + 
		bailMoney + ", "+
		"bailDate=" + 
		bailDate + ", "+
		"validSign=" + 
		validSign + ", "+
		"createTime=" + 
		createTime + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
