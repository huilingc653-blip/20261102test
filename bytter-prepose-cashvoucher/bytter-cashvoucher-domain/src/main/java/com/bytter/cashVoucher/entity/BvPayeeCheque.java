package com.bytter.cashVoucher.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: BvPayeeChequeDao
 * @Description: 支票收款登记数据操作类
 * @author Jinyong.Cao
 * @date 2018-06-13 13:19:08
 * @version 1.0
 *
 */
@Entity
@Table(name = "BV_PAYEE_CHEQUE", schema = "")
@SuppressWarnings("serial")
public class BvPayeeCheque extends IdEntity implements java.io.Serializable{
	/**所属单位ID*/
	private String corpId;
	/**单位编号 */
	private String corpCode;
	/** 单位名称 */
	private String corpName;
	/**票据号码*/
	private String origNo;
	/**票据金额*/
	private BigDecimal billMoney;
	/**购入日期*/
	private Date inputDate;
	/**付款人*/
	private String payer;
	/**付款人账户*/
	private String payerAcc;
	/**付款人银行账户*/
	private String payerBankNo;
	/**付款人银行名称*/
	private String payerBankName;
	/**收款人*/
	private String payee;
	/**收款人账户*/
	private String payeeAcc;
	/**收款人银行账户*/
	private String payeeBankNo;
	/**收款人银行名称*/
	private String payeeBankName;
	/**状态：-2已删除,-1打回,0已登记,1复核中,2已入库,3退票,4作废 */
	private Integer status;
	/**用途*/
	private String use;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**开票日期*/
	private Date startDate;
	/**到期日期*/
	private Date endDate;
	/**汇率*/
	private BigDecimal curExrate;
	/**币别ID*/
	private String curId;
	/**币种名称*/
	private String curName;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属单位ID
	 */
	@Column(name ="CORP_ID",nullable=true,length=32)
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属单位ID
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}
	@Column(name ="CORP_CODE", length=32)
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	@Column(name ="CORP_NAME", length=200)
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	@Column(name ="CUR_NAME", length=30)
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据号码
	 */
	@Column(name ="ORIG_NO",nullable=true,length=32)
	public String getOrigNo() {
		return origNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据号码
	 */
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  票据金额
	 */
	@Column(name ="BILL_MONEY",nullable=true)
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  票据金额
	 */
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  购入时间
	 */
	@Column(name ="INPUT_DATE",nullable=true)
	public Date getInputDate() {
		return inputDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  购入时间
	 */
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款人
	 */
	@Column(name ="PAYER",nullable=true,length=240)
	public String getPayer() {
		return payer;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款人
	 */
	public void setPayer(String payer) {
		this.payer = payer;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款人账户
	 */
	@Column(name ="PAYER_ACC",nullable=true,length=30)
	public String getPayerAcc() {
		return payerAcc;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款人账户
	 */
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款人银行行号
	 */
	@Column(name ="PAYER_BANK_NO",nullable=true,length=30)
	public String getPayerBankNo() {
		return payerBankNo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款人银行行号
	 */
	public void setPayerBankNo(String payerBankNo) {
		this.payerBankNo = payerBankNo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款人银行名称
	 */
	@Column(name ="PAYER_BANK_NAME",nullable=true,length=240)
	public String getPayerBankName() {
		return payerBankName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款人银行名称
	 */
	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款人
	 */
	@Column(name ="PAYEE",nullable=true,length=240)
	public String getPayee() {
		return payee;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款人
	 */
	public void setPayee(String payee) {
		this.payee = payee;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款人账户
	 */
	@Column(name ="PAYEE_ACC",nullable=true,length=30)
	public String getPayeeAcc() {
		return payeeAcc;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款人账户
	 */
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款人银行行号
	 */
	@Column(name ="PAYEE_BANK_NO",nullable=true,length=30)
	public String getPayeeBankNo() {
		return payeeBankNo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款人银行行号
	 */
	public void setPayeeBankNo(String payeeBankNo) {
		this.payeeBankNo = payeeBankNo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款人银行名称
	 */
	@Column(name ="PAYEE_BANK_NAME",nullable=true,length=240)
	public String getPayeeBankName() {
		return payeeBankName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款人银行名称
	 */
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用途
	 */
	@Column(name ="\"USE\"",nullable=true,length=100)
	public String getUse() {
		return use;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用途
	 */
	public void setUse(String use) {
		this.use = use;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=50)
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
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
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
	@Column(name ="UPDATE_DATE",nullable=true)
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
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  开票日期
	 */
	@Column(name ="START_DATE",nullable=true)
	public Date getStartDate() {
		return startDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  开票日期
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  到期日期
	 */
	@Column(name ="END_DATE",nullable=true)
	public Date getEndDate() {
		return endDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  到期日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  汇率
	 */
	@Column(name ="CUR_EXRATE",nullable=true)
	public BigDecimal getCurExrate() {
		return curExrate;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  汇率
	 */
	public void setCurExrate(BigDecimal curExrate) {
		this.curExrate = curExrate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别ID
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
	public String getCurId() {
		return curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别ID
	 */
	public void setCurId(String curId) {
		this.curId = curId;
	}
}
