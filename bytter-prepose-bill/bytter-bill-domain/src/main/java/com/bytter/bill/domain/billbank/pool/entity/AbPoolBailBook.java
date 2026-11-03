package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 票据池明细登记薄
 * @author zhengg
 * @date 2020-02-24 14:40:39
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_BAIL_BOOK", schema = "")
@SuppressWarnings("serial")
public class AbPoolBailBook extends IdEntity implements java.io.Serializable {
	/**交易日期*/
	private java.util.Date transDate;
	
	/**存取账户*/
	private String oppAcc;
	/**存取账户名称*/
	private String oppAccName;
	/**存取账户行号*/
	private String oppBankNo;
	/**存取账户行名*/
	private String oppBankName;
	/**更新日期*/
	private java.util.Date createTime;
	/**更新日期*/
	private java.util.Date updateTime;
	/**主键ID*/
	private String extId;
	/**单位ID*/
	private String corpId;
	/**票据池ID*/
	private String bpId;
	/**存取类型:1：存入， 2：提取*/
	private String type;
	/**保证金账户*/
	private String bailAcc;
	/**可用额度*/
	private java.math.BigDecimal money;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  交易日期
	 */
	@Column(name ="TRANS_DATE",nullable=true,scale=6)
	public java.util.Date getTransDate(){
		return this.transDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  交易日期
	 */
	public void setTransDate(java.util.Date transDate){
		this.transDate = transDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  存取账户
	 */
	@Column(name ="OPP_ACC",nullable=true,length=50)
	public String getOppAcc(){
		return this.oppAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  存取账户
	 */
	public void setOppAcc(String oppAcc){
		this.oppAcc = oppAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  存取账户名称
	 */
	@Column(name ="OPP_ACC_NAME",nullable=true,length=50)
	public String getOppAccName(){
		return this.oppAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  存取账户名称
	 */
	public void setOppAccName(String oppAccName){
		this.oppAccName = oppAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  存取账户行号
	 */
	@Column(name ="OPP_BANK_NO",nullable=true,length=50)
	public String getOppBankNo(){
		return this.oppBankNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  存取账户行号
	 */
	public void setOppBankNo(String oppBankNo){
		this.oppBankNo = oppBankNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  存取账户行名
	 */
	@Column(name ="OPP_BANK_NAME",nullable=true,length=50)
	public String getOppBankName(){
		return this.oppBankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  存取账户行名
	 */
	public void setOppBankName(String oppBankName){
		this.oppBankName = oppBankName;
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
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_TIME",nullable=true,scale=6)
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键ID
	 */
	@Column(name ="EXT_ID",nullable=false,length=50)
	public String getExtId(){
		return this.extId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键ID
	 */
	public void setExtId(String extId){
		this.extId = extId;
	}
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
	 *@param: java.lang.String  单位ID
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
	 *@return: java.lang.String  存取类型:1：存入， 2：提取
	 */
	@Column(name ="TYPE",nullable=true,length=2)
	public String getType(){
		return this.type;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  存取类型:1：存入， 2：提取
	 */
	public void setType(String type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  保证金账户
	 */
	@Column(name ="BAIL_ACC",nullable=true,length=50)
	public String getBailAcc(){
		return this.bailAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  保证金账户
	 */
	public void setBailAcc(String bailAcc){
		this.bailAcc = bailAcc;
	}
	
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  可用额度
	 */
	@Column(name ="MONEY",nullable=true,precision=15,scale=2)
	public java.math.BigDecimal getMoney(){
		return this.money;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  可用额度
	 */
	public void setMoney(java.math.BigDecimal money){
		this.money = money;
	}
	
	@Override
	public String toString() {
		return "AbPoolBailBook ["+
		"transDate=" + 
		transDate + ", "+
		"oppAcc=" + 
		oppAcc + ", "+
		"oppAccName=" + 
		oppAccName + ", "+
		"oppBankNo=" + 
		oppBankNo + ", "+
		"oppBankName=" + 
		oppBankName + ", "+
		"createTime=" + 
		createTime + ", "+
		"updateTime=" + 
		updateTime + ", "+
		"id=" + 
		getId() + ", "+
		"extId=" + 
		extId + ", "+
		"corpId=" + 
		corpId + ", "+
		"bpId=" + 
		bpId + ", "+
		"type=" + 
		type + ", "+
		"bailAcc=" + 
		bailAcc + ", "+
		"money=" + 
		money + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
