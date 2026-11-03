package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 下拨申请单
 * @author onlineGenerator
 * @date 2017-07-17 14:43:45
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BILL_TRANSFER", schema = "")
@SuppressWarnings("serial")
public class AbBillTransfer extends IdEntity implements java.io.Serializable {
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**票据流水ID*/
	//private java.lang.String billAid;
	/**单号*/
	private String allocatedCode;
	/**金额*/
	private java.math.BigDecimal allocationMoney;
	/**原因*/
	private String allocationReason;
	/**录入单位ID*/
	private String recordCorpId;
	/**录入单位名称*/
	private String recordCorpName;
	/**
	 * 录入单位code
	 */
	private String recordCorpCode;
	/**录入日期*/
	private java.util.Date recordDate;
	/**下拨票据号多个票据号用分号隔开*/
	//private java.lang.String billNo;
	/**收票单位ID*/
	private String recevieCorpId;
	/**收票单位名称*/
	private String recevieCorpName;

	/**
	 * 收票单位code(冗余)
	 */
	private String recevieCorpCode;
	/**收票单位账号*/
	private String recevieBankacc;
	/**下拨中心账号*/
	private String recordBankacc;
	/**票据币别*/
	private String billCurrency;

	/**
	 * 票据币别名称（冗余）
	 */
	private String billCurName;
	/**附件*/
	private String enclosure;
	/**创建人名称*/
	private String createName;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=60)
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
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
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_NAME",nullable=true,length=60)
	public String getUpdateName(){
		return this.updateName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=60)
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
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
	 *@return: java.lang.String  单号
	 */
	@Column(name ="ALLOCATED_CODE",nullable=true,length=32)
	public String getAllocatedCode(){
		return this.allocatedCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单号
	 */
	public void setAllocatedCode(String allocatedCode){
		this.allocatedCode = allocatedCode;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="ALLOCATION_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getAllocationMoney(){
		return this.allocationMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setAllocationMoney(java.math.BigDecimal allocationMoney){
		this.allocationMoney = allocationMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原因
	 */
	@Column(name ="ALLOCATION_REASON",nullable=true,length=240)
	public String getAllocationReason(){
		return this.allocationReason;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原因
	 */
	public void setAllocationReason(String allocationReason){
		this.allocationReason = allocationReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  录入单位ID
	 */
	@Column(name ="RECORD_CORP_ID",nullable=true,length=32)
	public String getRecordCorpId(){
		return this.recordCorpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  录入单位ID
	 */
	public void setRecordCorpId(String recordCorpId){
		this.recordCorpId = recordCorpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  录入单位名称
	 */
	@Column(name ="RECORD_CORP_NAME",nullable=true,length=60)
	public String getRecordCorpName(){
		return this.recordCorpName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  录入单位名称
	 */
	public void setRecordCorpName(String recordCorpName){
		this.recordCorpName = recordCorpName;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  录入日期
	 */
	@Column(name ="RECORD_DATE",nullable=true)
	public java.util.Date getRecordDate(){
		return this.recordDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  录入日期
	 */
	public void setRecordDate(java.util.Date recordDate){
		this.recordDate = recordDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收票单位ID
	 */
	@Column(name ="RECEVIE_CORP_ID",nullable=true,length=32)
	public String getRecevieCorpId(){
		return this.recevieCorpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收票单位ID
	 */
	public void setRecevieCorpId(String recevieCorpId){
		this.recevieCorpId = recevieCorpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收票单位名称
	 */
	@Column(name ="RECEVIE_CORP_NAME",nullable=true,length=60)
	public String getRecevieCorpName(){
		return this.recevieCorpName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收票单位名称
	 */
	public void setRecevieCorpName(String recevieCorpName){
		this.recevieCorpName = recevieCorpName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据币别
	 */
	@Column(name ="BILL_CURRENCY",nullable=true,length=30)
	public String getBillCurrency(){
		return this.billCurrency;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据币别
	 */
	public void setBillCurrency(String billCurrency){
		this.billCurrency = billCurrency;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附件
	 */
	@Column(name ="ENCLOSURE",nullable=true,length=240)
	public String getEnclosure(){
		return this.enclosure;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附件
	 */
	public void setEnclosure(String enclosure){
		this.enclosure = enclosure;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=60)
	public String getCreateName(){
		return this.createName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(String createName){
		this.createName = createName;
	}
	@Column(name ="RECEVIE_BANKACC",nullable=true,length=60)
	public String getRecevieBankacc() {
		return recevieBankacc;
	}
	public void setRecevieBankacc(String recevieBankacc) {
		this.recevieBankacc = recevieBankacc;
	}
	@Column(name ="RECORD_BANKACC",nullable=true,length=60)
	public String getRecordBankacc() {
		return recordBankacc;
	}
	public void setRecordBankacc(String recordBankacc) {
		this.recordBankacc = recordBankacc;
	}
	@Column(name ="RECORD_CORP_CODE",nullable=true,length=200)
	public String getRecordCorpCode() {
		return recordCorpCode;
	}

	public void setRecordCorpCode(String recordCorpCode) {
		this.recordCorpCode = recordCorpCode;
	}
	@Column(name ="RECEVIE_CORP_CODE",nullable=true,length=200)
	public String getRecevieCorpCode() {
		return recevieCorpCode;
	}

	public void setRecevieCorpCode(String recevieCorpCode) {
		this.recevieCorpCode = recevieCorpCode;
	}

	@Column(name ="BILL_CUR_NAME",nullable=true,length=200)
	public String getBillCurName() {
		return billCurName;
	}

	public void setBillCurName(String billCurName) {
		this.billCurName = billCurName;
	}
}
