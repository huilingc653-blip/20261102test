package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 接口代码业务规则表
 * @author onlineGenerator
 * @date 2017-05-17 10:16:20
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_BIF_RULE", schema = "")
@SuppressWarnings("serial")
public class BisBifRule extends IdEntity implements java.io.Serializable {
	/**接口代码*/
	private String bifCode;
	/**接口代码名称*/
	private String bifName;
	/**付款人*/
	private Integer payerName;
	/**付款账号*/
	private Integer payerAcc;
	/**付款开户行*/
	private Integer payerBank;
	/**收款人*/
	private Integer payeeName;
	/**收款账号*/
	private Integer payeeAcc;
	/**收款开户行*/
	private Integer payeeBank;
	/**用途*/
	private Integer purpose;
	/**附言*/
	private Integer addWord;
	/**用途是否支持特殊字符*/
	private String isPurpose;
	/**附言是否额外包含业务单号*/
	private String isAddBillNo;
	/**银行代发是否在线*/
	private String isBankOnline;
	/**银行代发发送数*/
	private Integer sendNum;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**同行是否必填联行号*/
	private String isMustBankCode;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口代码
	 */
	@Column(name ="BIF_CODE",nullable=false,length=4)
	public String getBifCode(){
		return this.bifCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口代码
	 */
	public void setBifCode(String bifCode){
		this.bifCode = bifCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口代码名称
	 */
	@Column(name ="BIF_NAME",nullable=false,length=30)
	public String getBifName(){
		return this.bifName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口代码名称
	 */
	public void setBifName(String bifName){
		this.bifName = bifName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款人
	 */
	@Column(name ="PAYER_NAME",nullable=true,length=10)
	public Integer getPayerName(){
		return this.payerName;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  付款人
	 */
	public void setPayerName(Integer payerName){
		this.payerName = payerName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款账号
	 */
	@Column(name ="PAYER_ACC",nullable=true,length=10)
	public Integer getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  付款账号
	 */
	public void setPayerAcc(Integer payerAcc){
		this.payerAcc = payerAcc;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付款开户行
	 */
	@Column(name ="PAYER_BANK",nullable=true,length=10)
	public Integer getPayerBank(){
		return this.payerBank;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  付款开户行
	 */
	public void setPayerBank(Integer payerBank){
		this.payerBank = payerBank;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款人
	 */
	@Column(name ="PAYEE_NAME",nullable=true,length=10)
	public Integer getPayeeName(){
		return this.payeeName;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  收款人
	 */
	public void setPayeeName(Integer payeeName){
		this.payeeName = payeeName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款账号
	 */
	@Column(name ="PAYEE_ACC",nullable=true,length=10)
	public Integer getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  收款账号
	 */
	public void setPayeeAcc(Integer payeeAcc){
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收款开户行
	 */
	@Column(name ="PAYEE_BANK",nullable=true,length=10)
	public Integer getPayeeBank(){
		return this.payeeBank;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  收款开户行
	 */
	public void setPayeeBank(Integer payeeBank){
		this.payeeBank = payeeBank;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  用途
	 */
	@Column(name ="PURPOSE",nullable=true,length=10)
	public Integer getPurpose(){
		return this.purpose;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  用途
	 */
	public void setPurpose(Integer purpose){
		this.purpose = purpose;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  附言
	 */
	@Column(name ="ADD_WORD",nullable=true,length=10)
	public Integer getAddWord(){
		return this.addWord;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  附言
	 */
	public void setAddWord(Integer addWord){
		this.addWord = addWord;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用途是否支持特殊字符
	 */
	@Column(name ="IS_PURPOSE",nullable=true,length=1)
	public String getIsPurpose(){
		return this.isPurpose;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用途是否支持特殊字符
	 */
	public void setIsPurpose(String isPurpose){
		this.isPurpose = isPurpose;
	}
	@Column(name ="IS_ADD_BILLNO",nullable=true,length=1)
	public String getIsAddBillNo() {
		return isAddBillNo;
	}
	public void setIsAddBillNo(String isAddBillNo) {
		this.isAddBillNo = isAddBillNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行代发是否在线
	 */
	@Column(name ="IS_BANK_ONLINE",nullable=true,length=1)
	public String getIsBankOnline(){
		return this.isBankOnline;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行代发是否在线
	 */
	public void setIsBankOnline(String isBankOnline){
		this.isBankOnline = isBankOnline;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  银行代发发送数
	 */
	@Column(name ="SEND_NUM",nullable=true,length=10)
	public Integer getSendNum(){
		return this.sendNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  银行代发发送数
	 */
	public void setSendNum(Integer sendNum){
		this.sendNum = sendNum;
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
	@Column(name ="IS_MUST_BANK_CODE",nullable=true)
	public String getIsMustBankCode() {
		return isMustBankCode;
	}
	public void setIsMustBankCode(String isMustBankCode) {
		this.isMustBankCode = isMustBankCode;
	}
}
