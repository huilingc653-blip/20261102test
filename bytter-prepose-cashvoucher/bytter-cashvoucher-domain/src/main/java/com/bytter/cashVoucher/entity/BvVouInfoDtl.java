package com.bytter.cashVoucher.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
/**
 * @Title: Entity
 * @Description: 凭证入库明细表
 * @author onlineGenerator
 * @date 2017-05-25 15:52:28
 * @version 1.0
 *
 */
@Entity
@Table(name = "BV_VOU_INFO_DTL", schema = "")
@SuppressWarnings("serial")
public class BvVouInfoDtl extends IdEntity implements java.io.Serializable {
	/**入库信息ID*/
	private String inputId;
	/**所属单位ID*/
	private String corpId;
	private String corpCode;
	private String corpName;
	/**所属库箱ID*/
	private String sboxId;
	/**所属账户*/
	private String bankAcc;
	private String bankName;
	/**所属银行ID*/
	private String bankId;
	private String typeName;
	/**凭证种类*/
	private String vouTid;
	/**凭证编号*/
	private String vouNo;
	/**购入日期*/
	private java.util.Date inputDate;
	/**凭证状态*/
	private String vouStatus;
	/**备注*/
	private String rmk;
	/**退票或作废原因*/
	private String reason;
	/**创建人名称*/
	private String createName;
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
	/**汇率*/
	private BigDecimal curExrate;
	/**币别ID*/
	private String curId;
	private String curName;
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
	@Column(name ="BANK_NAME", length=150)
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Column(name ="TYPE_NAME", length=100)
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Column(name ="CUR_NAME", length=30)
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  入库信息ID
	 */
	@Column(name ="INPUT_ID",nullable=true,length=10)
	public String getInputId(){
		return this.inputId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  入库信息ID
	 */
	public void setInputId(String inputId){
		this.inputId = inputId;
	}
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属库箱ID
	 */
	@Column(name ="SBOX_ID",nullable=true,length=32)
	public String getSboxId(){
		return this.sboxId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属库箱ID
	 */
	public void setSboxId(String sboxId){
		this.sboxId = sboxId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属账户
	 */
	@Column(name ="BANK_ACC",nullable=true,length=50)
	public String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属账户
	 */
	public void setBankAcc(String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属银行ID
	 */
	@Column(name ="BANK_ID",nullable=true,length=32)
	public String getBankId(){
		return this.bankId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属银行ID
	 */
	public void setBankId(String bankId){
		this.bankId = bankId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证种类
	 */
	@Column(name ="VOU_TID",nullable=true,length=32)
	public String getVouTid(){
		return this.vouTid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证种类
	 */
	public void setVouTid(String vouTid){
		this.vouTid = vouTid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证编号
	 */
	@Column(name ="VOU_NO",nullable=true,length=20)
	public String getVouNo(){
		return this.vouNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证编号
	 */
	public void setVouNo(String vouNo){
		this.vouNo = vouNo;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  购入日期
	 */
	@Column(name ="INPUT_DATE",nullable=true)
	public java.util.Date getInputDate(){
		return this.inputDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  购入日期
	 */
	public void setInputDate(java.util.Date inputDate){
		this.inputDate = inputDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证状态
	 */
	@Column(name ="VOU_STATUS",nullable=true,length=10)
	public String getVouStatus(){
		return this.vouStatus;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证状态
	 */
	public void setVouStatus(String vouStatus){
		this.vouStatus = vouStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=100)
	public String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(String rmk){
		this.rmk = rmk;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
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
