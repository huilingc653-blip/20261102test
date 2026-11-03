package com.bytter.check.reconcile.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "BIS_CHECK_DTL", schema = "")
@SuppressWarnings("serial")
public class BisCheckDtl extends IdEntity implements java.io.Serializable{ 
	
	private String billNo;     
	private String bankAcc;    
	private String corpId;    
	private String accName;   
	private String accNature;   
	private String curId;   
	private Date tobillDate; 
	private BigDecimal bal;         
	private String voucherStat;
	private Date returnTime; 
	private String checkResult;
	private Date checkDate;
	private String reasonId;   
	private String mobileNo;   
	private String status;   
	private String checkStatus;   
	private String rmk;         
	private String checkStat;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**拓展字段（用于存储银行返回要素）*/
	private String expandFields;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
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
	public Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
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
	@Column(name ="UPDATE_DATE")
	public Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	@Column(name ="BILL_NO",nullable=false)
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	@Column(name ="BANK_ACC",nullable=true)
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	@Column(name ="ACC_NAME",nullable=true)
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Column(name ="CUR_ID",nullable=true)
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Column(name ="TOBILL_DATE",nullable=false)
	public Date getTobillDate() {
		return tobillDate;
	}
	public void setTobillDate(Date tobillDate) {
		this.tobillDate = tobillDate;
	}
	@Column(name ="BAL",nullable=true)
	public BigDecimal getBal() {
		return bal;
	}
	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}
	@Column(name ="VOUCHER_STAT",nullable=true)
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	@Column(name ="RETURN_TIME",nullable=true)
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	@Column(name ="CHECK_RESULT",nullable=true)
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	
	@Column(name ="CHECK_DATE",nullable=false)
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	@Column(name ="REASON_ID",nullable=false)
	public String getReasonId() {
		return reasonId;
	}
	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}
	
	@Column(name ="MOBILE_NO",nullable=false)
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	@Column(name ="RMK",nullable=true)
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@Column(name ="CHECK_STAT",nullable=true)
	public String getCheckStat() {
		return checkStat;
	}
	public void setCheckStat(String checkStat) {
		this.checkStat = checkStat;
	}
	@Column(name ="corp_Id",nullable=true)
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name ="ACC_NATURE",nullable=true)
	public String getAccNature() {
		return accNature;
	}
	public void setAccNature(String accNature) {
		this.accNature = accNature;
	}
	@Column(name ="STATUS",nullable=true)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    @Column(name ="check_Status")
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	@Column(name ="EXPAND_FIELDS")
	public String getExpandFields() {
		return expandFields;
	}
	public void setExpandFields(String expandFields) {
		this.expandFields = expandFields;
	}
}
