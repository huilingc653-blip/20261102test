package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 保证金存取流水
 * @author zhengg
 * @date 2020-02-25 15:28:25
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_BAIL_dtl", schema = "")
@SuppressWarnings("serial")
public class AbPoolBailDtl extends IdEntity implements java.io.Serializable {
	/**保证金主信息ID*/
	private String bailId;
	/**存取类型:1：存入， 2：提取*/
	private String type;
	/**保证金额度*/
	private java.math.BigDecimal bailMoney;
	/**可用额度*/
	private java.math.BigDecimal money;
	/**存取账户*/
	private String oppAcc;
	/**存取账户名称*/
	private String oppAccName;
	/**存取账户行号*/
	private String oppBankNo;
	/**存取账户行名*/
	private String oppBankName;
	/**申请日期*/
	private java.util.Date appDate;
	/**指令状态：待发送16，发送失败-1，银行处理中18，银行交易成功19，银行交易失败-3*/
	private String optStatus;
	/**银行返回信息*/
	private String returnMsg;
	/**审批状态：0暂存，-1打回，-2删除，11审批中，95审批通过*/
	private Integer status;
	/**创建人名称*/
	private String createId;
	/**更新日期*/
	private java.util.Date createTime;
	/**更新人名称*/
	private String updateId;
	/**更新日期*/
	private java.util.Date updateTime;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="BAIL_ID",nullable=true,length=32)
	public String getBailId(){
		return this.bailId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位ID
	 */
	public void setBailId(String bailId){
		this.bailId = bailId;
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  保证金额度
	 */
	@Column(name ="BAIL_MONEY",nullable=true,precision=15,scale=2)
	public java.math.BigDecimal getBailMoney(){
		return this.bailMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  保证金额度
	 */
	public void setBailMoney(java.math.BigDecimal bailMoney){
		this.bailMoney = bailMoney;
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
	 *@return: java.util.Date  申请日期
	 */
	@Column(name ="APP_DATE",nullable=true)
	public java.util.Date getAppDate(){
		return this.appDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  申请日期
	 */
	public void setAppDate(java.util.Date appDate){
		this.appDate = appDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  指令状态：待发送16，发送失败-1，银行处理中18，银行交易成功19，银行交易失败-3
	 */
	@Column(name ="OPT_STATUS",nullable=true,length=2)
	public String getOptStatus(){
		return this.optStatus;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指令状态：待发送16，发送失败-1，银行处理中18，银行交易成功19，银行交易失败-3
	 */
	public void setOptStatus(String optStatus){
		this.optStatus = optStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行返回信息
	 */
	@Column(name ="RETURN_MSG",nullable=true,length=500)
	public String getReturnMsg(){
		return this.returnMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行返回信息
	 */
	public void setReturnMsg(String returnMsg){
		this.returnMsg = returnMsg;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审批状态：0暂存，-1打回，-2删除，11审批中，95审批通过
	 */
	@Column(name ="STATUS",nullable=true,precision=6,scale=0)
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审批状态：0暂存，-1打回，-2删除，11审批中，95审批通过
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_ID",nullable=true,length=32)
	public String getCreateId(){
		return this.createId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateId(String createId){
		this.createId = createId;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_ID",nullable=true,length=32)
	public String getUpdateId(){
		return this.updateId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateId(String updateId){
		this.updateId = updateId;
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
	
	@Override
	public String toString() {
		return "AbPoolBailDtl ["+
		"id=" + 
		getId() + ", "+
		"bailId=" + 
		bailId + ", "+
		"type=" + 
		type + ", "+
		"bailMoney=" + 
		bailMoney + ", "+
		"money=" + 
		money + ", "+
		"oppAcc=" + 
		oppAcc + ", "+
		"oppAccName=" + 
		oppAccName + ", "+
		"oppBankNo=" + 
		oppBankNo + ", "+
		"oppBankName=" + 
		oppBankName + ", "+
		"appDate=" + 
		appDate + ", "+
		"optStatus=" + 
		optStatus + ", "+
		"returnMsg=" + 
		returnMsg + ", "+
		"status=" + 
		status + ", "+
		"createId=" + 
		createId + ", "+
		"createTime=" + 
		createTime + ", "+
		"updateId=" + 
		updateId + ", "+
		"updateTime=" + 
		updateTime + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
