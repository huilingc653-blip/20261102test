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
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FBS_BUDGET_DETAIL
 * @author onlineGenerator
 * @date 2017-07-19 14:42:18
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_BUDGET_DETAIL", schema = "")
@SuppressWarnings("serial")
public class FbsBudgetDetail extends IdEntity implements java.io.Serializable {
	/**明细ID*/
	private java.lang.String detailId;
	/**追加ID*/
	private java.lang.String superaddId;
	/**科目代码*/
	private java.lang.String itemCode;
	/**科目名称*/
	private java.lang.String itemName;
	/**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
	private java.lang.String timeType;
	/**上报金额*/
	private java.math.BigDecimal bMoney;
	/**追加金额*/
	private java.math.BigDecimal bsuperMoney;
	/**本次追加金额*/
	private java.math.BigDecimal btempMoney;
	/**执行金额*/
	private java.math.BigDecimal processMoney;
	/**币别*/
	private java.lang.String curId;
	/**结算类型  0-现款 1-票据*/
	private java.lang.String settlType;
	/**部门*/
	private java.lang.String corpName;
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
	/**汇总时下级单位的明细id*/
	private java.lang.String childCorpDtlId;
	/**折合人民币*/
	private java.math.BigDecimal natualMoney;
	/**已追加金额折合人民币*/
	private java.math.BigDecimal bsuperNatualMoney;
	/**本次追加金额折合人民币*/
	private java.math.BigDecimal btempNatualMoney;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  明细ID
	 */
	@Column(name ="DETAIL_ID",nullable=true,length=32)
	public java.lang.String getDetailId(){
		return this.detailId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  明细ID
	 */
	public void setDetailId(java.lang.String detailId){
		this.detailId = detailId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  追加ID
	 */
	@Column(name ="SUPERADD_ID",nullable=true,length=32)
	public java.lang.String getSuperaddId(){
		return this.superaddId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  追加ID
	 */
	public void setSuperaddId(java.lang.String superaddId){
		this.superaddId = superaddId;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计划周期类型 1-年 2-季 3-月 4-周 5-天
	 */
	@Column(name ="TIME_TYPE",nullable=false,length=1)
	public java.lang.String getTimeType(){
		return this.timeType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计划周期类型 1-年 2-季 3-月 4-周 5-天
	 */
	public void setTimeType(java.lang.String timeType){
		this.timeType = timeType;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  上报金额
	 */
	@Column(name ="B_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBMoney(){
		return this.bMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  上报金额
	 */
	public void setBMoney(java.math.BigDecimal bMoney){
		this.bMoney = bMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  追加金额
	 */
	@Column(name ="BSUPER_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBsuperMoney(){
		return this.bsuperMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  追加金额
	 */
	public void setBsuperMoney(java.math.BigDecimal bsuperMoney){
		this.bsuperMoney = bsuperMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  本次追加金额
	 */
	@Column(name ="BTEMP_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBtempMoney(){
		return this.btempMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  本次追加金额
	 */
	public void setBtempMoney(java.math.BigDecimal btempMoney){
		this.btempMoney = btempMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  执行金额
	 */
	@Column(name ="PROCESS_MONEY",nullable=true,scale=2,length=15)
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
	@Column(name ="CORP_NAME",nullable=true,length=100)
	public java.lang.String getCorpName(){
		return this.corpName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  部门
	 */
	public void setCorpName(java.lang.String corpName){
		this.corpName = corpName;
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
	@Column(name ="CHILD_CORP_DTL_ID",nullable=true,length=32)
	public String getChildCorpDtlId() {
		return childCorpDtlId;
	}
	public void setChildCorpDtlId(String childCorpDtlId) {
		this.childCorpDtlId = childCorpDtlId;
	}
	@Column(name ="NATUAL_MONEY",nullable=true,length=32)
	public BigDecimal getNatualMoney() {
		return natualMoney;
	}
	public void setNatualMoney(BigDecimal natualMoney) {
		this.natualMoney = natualMoney;
	}
	@Column(name ="BSUPER_NATUAL_MONEY",nullable=true,length=32)
	public BigDecimal getBsuperNatualMoney() {
		return bsuperNatualMoney;
	}
	public void setBsuperNatualMoney(BigDecimal bsuperNatualMoney) {
		this.bsuperNatualMoney = bsuperNatualMoney;
	}
	@Column(name ="BTEMP_NATUAL_MONEY",nullable=true,length=32)
	public BigDecimal getBtempNatualMoney() {
		return btempNatualMoney;
	}
	public void setBtempNatualMoney(BigDecimal btempNatualMoney) {
		this.btempNatualMoney = btempNatualMoney;
	}
}
