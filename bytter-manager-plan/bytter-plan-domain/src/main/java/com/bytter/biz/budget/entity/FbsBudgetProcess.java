package com.bytter.biz.budget.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 预算执行表
 * @author onlineGenerator
 * @date 2017-07-18 10:48:05
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_BUDGET_PROCESS", schema = "")
@SuppressWarnings("serial")
public class FbsBudgetProcess extends IdEntity implements java.io.Serializable {
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
	/**单位ID*/
	private java.lang.String corpId;
	/**单位名称*/
	private java.lang.String corpName;
	/**科目代码*/
	private java.lang.String itemCode;
	/**科目名称*/
	private java.lang.String itemName;
	/**执行金额*/
	private java.math.BigDecimal processMoney;
	/**执行金额-折合人民币*/
	private java.math.BigDecimal processNatualMoney;
	/**执行日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date processDate;
	/**业务单据号*/
	private java.lang.String billNo;
	/**业务单ID*/
	private java.lang.String billId;
	/**币别*/
	private java.lang.String curId;
	/**币别名称*/
	private java.lang.String curName;
	/**结算类型  0-现款 1-票据*/
	private java.lang.String settlType;
	/**部门*/
	private java.lang.String deptName;
	/**用途*/
	private java.lang.String purpose;
	/**期望支付日期*/
	private java.util.Date wishPayday;
	/**付款账号*/
	private java.lang.String payerAcc;
	/**收款账号名称*/
	private java.lang.String payeeAccName;
	/**收款账号*/
	private java.lang.String payeeAcc;
	/**收款开户行名称*/
	private java.lang.String payeeBank;
	/**合同号*/
	private java.lang.String contract;
	/**归属日期*/
	private java.util.Date vestingDate;
	/**备注*/
	private java.lang.String rmk;
	/**执行业务名称*/
	private java.lang.String businessName;
	/**执行系统名称*/
	private java.lang.String processSysname;
	/**内转标识 1-内转 0- 非内转*/
	private java.lang.String innerSign;
	/**汇率*/
	private java.math.BigDecimal exchangeRate;
	/**执行金额-折合本身币别*/
	private java.math.BigDecimal processCurMoney;
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
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
	@Column(name ="CORP_NAME",nullable=true)
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目代码
	 */
	@Column(name ="ITEM_CODE",nullable=false,length=40)
	public java.lang.String getItemCode(){
		return this.itemCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目代码
	 */
	public void setItemCode(java.lang.String itemCode){
		this.itemCode = itemCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目名称
	 */
	@Column(name ="ITEM_NAME",nullable=false,length=100)
	public java.lang.String getItemName(){
		return this.itemName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目名称
	 */
	public void setItemName(java.lang.String itemName){
		this.itemName = itemName;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  执行金额
	 */
	@Column(name ="PROCESS_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getProcessMoney(){
		return this.processMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  执行金额
	 */
	public void setProcessMoney(java.math.BigDecimal processMoney){
		this.processMoney = processMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  执行金额（折合人民币）
	 */
	@Column(name ="PROCESS_NATUAL_MONEY",nullable=false,scale=2,length=15)
	public BigDecimal getProcessNatualMoney() {
		return processNatualMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  执行金额（折合人民币）
	 */
	public void setProcessNatualMoney(BigDecimal processNatualMoney) {
		this.processNatualMoney = processNatualMoney;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  执行日期
	 */
	@Column(name ="PROCESS_DATE",nullable=true)
	public java.util.Date getProcessDate(){
		return this.processDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  执行日期
	 */
	public void setProcessDate(java.util.Date processDate){
		this.processDate = processDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务单据号
	 */
	@Column(name ="BILL_NO",nullable=true,length=32)
	public java.lang.String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务单据号
	 */
	public void setBillNo(java.lang.String billNo){
		this.billNo = billNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务单ID
	 */
	@Column(name ="BILL_ID",nullable=true,length=32)
	public java.lang.String getBillId(){
		return this.billId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务单据号
	 */
	public void setBillId(java.lang.String billId){
		this.billId = billId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
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
	@Column(name ="CUR_NAME",nullable=true)
	public String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结算类型  0-现款 1-票据
	 */
	@Column(name ="SETTL_TYPE",nullable=true,length=1)
	public java.lang.String getSettlType(){
		return this.settlType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结算类型  0-现款 1-票据
	 */
	public void setSettlType(java.lang.String settlType){
		this.settlType = settlType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  部门
	 */
	@Column(name ="DEPT_NAME",nullable=true,length=100)
	public java.lang.String getDeptName(){
		return this.deptName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  部门
	 */
	public void setDeptName(java.lang.String deptName){
		this.deptName = deptName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用途
	 */
	@Column(name ="PURPOSE",nullable=true,length=100)
	public java.lang.String getPurpose(){
		return this.purpose;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用途
	 */
	public void setPurpose(java.lang.String purpose){
		this.purpose = purpose;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  期望支付日期
	 */
	@Column(name ="WISH_PAYDAY",nullable=true)
	public java.util.Date getWishPayday(){
		return this.wishPayday;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  期望支付日期
	 */
	public void setWishPayday(java.util.Date wishPayday){
		this.wishPayday = wishPayday;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款账号
	 */
	@Column(name ="PAYER_ACC",nullable=true,length=30)
	public java.lang.String getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款账号
	 */
	public void setPayerAcc(java.lang.String payerAcc){
		this.payerAcc = payerAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款账号名称
	 */
	@Column(name ="PAYEE_ACC_NAME",nullable=true,length=150)
	public java.lang.String getPayeeAccName(){
		return this.payeeAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号名称
	 */
	public void setPayeeAccName(java.lang.String payeeAccName){
		this.payeeAccName = payeeAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款账号
	 */
	@Column(name ="PAYEE_ACC",nullable=true,length=40)
	public java.lang.String getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号
	 */
	public void setPayeeAcc(java.lang.String payeeAcc){
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款开户行名称
	 */
	@Column(name ="PAYEE_BANK",nullable=true,length=150)
	public java.lang.String getPayeeBank(){
		return this.payeeBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款开户行名称
	 */
	public void setPayeeBank(java.lang.String payeeBank){
		this.payeeBank = payeeBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合同号
	 */
	@Column(name ="CONTRACT",nullable=true,length=30)
	public java.lang.String getContract(){
		return this.contract;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合同号
	 */
	public void setContract(java.lang.String contract){
		this.contract = contract;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  归属日期
	 */
	@Column(name ="VESTING_DATE",nullable=true)
	public java.util.Date getVestingDate(){
		return this.vestingDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  归属日期
	 */
	public void setVestingDate(java.util.Date vestingDate){
		this.vestingDate = vestingDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=100)
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  执行业务名称
	 */
	@Column(name ="BUSINESS_NAME",nullable=true,length=30)
	public java.lang.String getBusinessName(){
		return this.businessName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  执行业务名称
	 */
	public void setBusinessName(java.lang.String businessName){
		this.businessName = businessName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  执行系统名称
	 */
	@Column(name ="PROCESS_SYSNAME",nullable=true,length=30)
	public java.lang.String getProcessSysname(){
		return this.processSysname;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  执行系统名称
	 */
	public void setProcessSysname(java.lang.String processSysname){
		this.processSysname = processSysname;
	}
	@Column(name ="INNER_SIGN",nullable=true,length=1)
	public String getInnerSign() {
		return innerSign;
	}
	public void setInnerSign(String innerSign) {
		this.innerSign = innerSign;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  计划汇率
	 */
	@Column(name ="EXCHANGE_RATE",nullable=false,scale=4,length=12)
	public java.math.BigDecimal getExchangeRate(){
		return this.exchangeRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  计划汇率
	 */
	public void setExchangeRate(java.math.BigDecimal exchangeRate){
		this.exchangeRate = exchangeRate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  执行金额（折合本身）
	 */
	@Column(name ="PROCESS_CUR_MONEY",nullable=false,scale=2,length=15)
	public BigDecimal getProcessCurMoney() {
		return processCurMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  执行金额（折合本身）
	 */
	public void setProcessCurMoney(BigDecimal processCurMoney) {
		this.processCurMoney = processCurMoney;
	}
}
