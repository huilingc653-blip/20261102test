package com.bytter.system.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: BT_BANK_ACC_FREEZE
 * @author onlineGenerator
 * @date 2017-06-14 14:26:46
 * @version 1.0
 *
 */
@Entity
@Table(name = "BT_BANK_ACC_FREEZE", schema = "")
@SuppressWarnings("serial")
public class BtBankAccFreeze extends IdEntity implements java.io.Serializable {
	/**bankAccId*/
	private java.lang.String bankAccId;
	/**银行账号*/
	private java.lang.String bankAcc;
	/**单位ID*/
	private java.lang.String corpId;
	/**余额*/
	private java.math.BigDecimal bal;
	/**冻结类型*/
	private java.lang.String ufFlag;
	/**冻结，解冻金额*/
	private java.math.BigDecimal freezeMoney;
	/**可冻结金额*/
	private java.math.BigDecimal avalBal;
	/**冻结，解冻日期*/
	private java.util.Date freezeDate;
	/**原因*/
	private java.lang.String freezeRession;
	/**被冻结信息*/
	private java.lang.String fUuid;
	/**状态*/
	private java.lang.Integer status;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**币别*/
	private java.lang.String curId;
	/**编号*/
	private java.lang.String freezeNo;
	/**是否是计划冻结的数据*/
	private java.lang.String planFlag;


	@Column(name ="PLAN_FLAG",nullable=true,length=1)
	public String getPlanFlag() {
		return planFlag;
	}

	public void setPlanFlag(String planFlag) {
		this.planFlag = planFlag;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  bankAccId
	 */
	@Column(name ="BANK_ACC_ID",nullable=true,length=32)
	public java.lang.String getBankAccId(){
		return this.bankAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  bankAccId
	 */
	public void setBankAccId(java.lang.String bankAccId){
		this.bankAccId = bankAccId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行账号
	 */
	@Column(name ="BANK_ACC",nullable=true,length=30)
	public java.lang.String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行账号
	 */
	public void setBankAcc(java.lang.String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=true,length=32)
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位ID
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  余额
	 */
	@Column(name ="BAL",nullable=true,scale=2,length=20)
	public java.math.BigDecimal getBal(){
		return this.bal;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  余额
	 */
	public void setBal(java.math.BigDecimal bal){
		this.bal = bal;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  冻结类型
	 */
	@Column(name ="UF_FLAG",nullable=true,length=1)
	public java.lang.String getUfFlag(){
		return this.ufFlag;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  冻结类型
	 */
	public void setUfFlag(java.lang.String ufFlag){
		this.ufFlag = ufFlag;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  冻结，解冻金额
	 */
	@Column(name ="FREEZE_MONEY",nullable=true,scale=2,length=20)
	public java.math.BigDecimal getFreezeMoney(){
		return this.freezeMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  冻结，解冻金额
	 */
	public void setFreezeMoney(java.math.BigDecimal freezeMoney){
		this.freezeMoney = freezeMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  可冻结金额
	 */
	@Column(name ="AVAL_BAL",nullable=true,scale=2,length=20)
	public java.math.BigDecimal getAvalBal(){
		return this.avalBal;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  可冻结金额
	 */
	public void setAvalBal(java.math.BigDecimal avalBal){
		this.avalBal = avalBal;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  冻结，解冻日期
	 */
	@Column(name ="FREEZE_DATE",nullable=true)
	public java.util.Date getFreezeDate(){
		return this.freezeDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  冻结，解冻日期
	 */
	public void setFreezeDate(java.util.Date freezeDate){
		this.freezeDate = freezeDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原因
	 */
	@Column(name ="FREEZE_RESSION",nullable=true,length=500)
	public java.lang.String getFreezeRession(){
		return this.freezeRession;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原因
	 */
	public void setFreezeRession(java.lang.String freezeRession){
		this.freezeRession = freezeRession;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	@Column(name ="CUR_ID",nullable=true,length=32)
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	@Column(name ="FREEZE_NO",nullable=true,length=32)
	public java.lang.String getFreezeNo() {
		return freezeNo;
	}
	public void setFreezeNo(java.lang.String freezeNo) {
		this.freezeNo = freezeNo;
	}
	@Column(name ="F_UUID",nullable=true,length=32)
	public java.lang.String getfUuid() {
		return fUuid;
	}
	public void setfUuid(java.lang.String fUuid) {
		this.fUuid = fUuid;
	}
	
	
	
}
