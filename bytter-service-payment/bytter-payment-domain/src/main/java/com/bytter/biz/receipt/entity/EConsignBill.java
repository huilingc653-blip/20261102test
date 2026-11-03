package com.bytter.biz.receipt.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 委托收款电子回单表
 * @author onlineGenerator
 * @date 2017-07-04 15:29:50
 * @version 1.0
 *
 */
@Entity
@Table(name = "E_CONSIGN_BILL", schema = "")
@SuppressWarnings("serial")
public class EConsignBill extends IdEntity implements java.io.Serializable {
	/**billNo*/
	private java.lang.String billNo;
	/**单位ID*/
	private java.lang.String corpId;
	/**电子回单类型06-委托收款通知单*/
	private java.lang.String billType;
	/**付款单位名称*/
	private java.lang.String payerName;
	/**付款单位账号*/
	private java.lang.String payerAcc;
	/**付款单位开户行*/
	private java.lang.String payerBank;
	/**收款单位名称*/
	private java.lang.String payeeName;
	/**收款单位开户行*/
	private java.lang.String payeeBank;
	/**收款单位账号*/
	private java.lang.String payeeAcc;
	/**币别*/
	private java.lang.String curId;
	/**金额*/
	private java.math.BigDecimal payMoney;
	/**用途*/
	private java.lang.String purpose;
	/**结算方式*/
	private java.lang.String payType;
	/**凭证号*/
	private java.lang.String voucherNo;
	/**委托日期*/
	private java.util.Date consignDate;
	/**委托收款单号*/
	private java.lang.String consignCode;
	/**贷方*/
	private java.lang.String creditAcc;
	/**借方*/
	private java.lang.String debitAcc;
	/**合同号*/
	private java.lang.String contractCode;
	/**记账员*/
	private java.lang.String tallierCode;
	/**复核员*/
	private String checkerCode;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**产生编码日期*/
	private java.util.Date encryDate;
	/**加密随机数*/
	private java.lang.String random;
	/**编码源*/
	private java.lang.String encrySource;
	/**subscriber*/
	private java.lang.String subscriber;
	/**验证码*/
	private java.lang.String validateCode;
	/**验证标志10-已验证11-验证失败12-未验证默认为12*/
	private java.lang.Integer isValidate;
	/**验证人*/
	private java.lang.String validBy;
	/**验证时间*/
	private java.util.Date validDate;
	/**打印次数*/
	private java.lang.Integer prints;
	/**状态1，新输入-1，删除2，冲帐*/
	private java.lang.Integer status;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  billNo
	 */
	@Column(name ="BILL_NO",nullable=false,length=13)
	public java.lang.String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  billNo
	 */
	public void setBillNo(java.lang.String billNo){
		this.billNo = billNo;
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子回单类型06-委托收款通知单
	 */
	@Column(name ="BILL_TYPE",nullable=false,length=2)
	public java.lang.String getBillType(){
		return this.billType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单类型06-委托收款通知单
	 */
	public void setBillType(java.lang.String billType){
		this.billType = billType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款单位名称
	 */
	@Column(name ="PAYER_NAME",nullable=true,length=200)
	public java.lang.String getPayerName(){
		return this.payerName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单位名称
	 */
	public void setPayerName(java.lang.String payerName){
		this.payerName = payerName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款单位账号
	 */
	@Column(name ="PAYER_ACC",nullable=false,length=100)
	public java.lang.String getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单位账号
	 */
	public void setPayerAcc(java.lang.String payerAcc){
		this.payerAcc = payerAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款单位开户行
	 */
	@Column(name ="PAYER_BANK",nullable=true,length=200)
	public java.lang.String getPayerBank(){
		return this.payerBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单位开户行
	 */
	public void setPayerBank(java.lang.String payerBank){
		this.payerBank = payerBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款单位名称
	 */
	@Column(name ="PAYEE_NAME",nullable=true,length=200)
	public java.lang.String getPayeeName(){
		return this.payeeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款单位名称
	 */
	public void setPayeeName(java.lang.String payeeName){
		this.payeeName = payeeName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款单位开户行
	 */
	@Column(name ="PAYEE_BANK",nullable=true,length=200)
	public java.lang.String getPayeeBank(){
		return this.payeeBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款单位开户行
	 */
	public void setPayeeBank(java.lang.String payeeBank){
		this.payeeBank = payeeBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款单位账号
	 */
	@Column(name ="PAYEE_ACC",nullable=true,length=100)
	public java.lang.String getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款单位账号
	 */
	public void setPayeeAcc(java.lang.String payeeAcc){
		this.payeeAcc = payeeAcc;
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="PAY_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getPayMoney(){
		return this.payMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setPayMoney(java.math.BigDecimal payMoney){
		this.payMoney = payMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用途
	 */
	@Column(name ="PURPOSE",nullable=true,length=60)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结算方式
	 */
	@Column(name ="PAY_TYPE",nullable=false,length=10)
	public java.lang.String getPayType(){
		return this.payType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结算方式
	 */
	public void setPayType(java.lang.String payType){
		this.payType = payType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证号
	 */
	@Column(name ="VOUCHER_NO",nullable=true,length=40)
	public java.lang.String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证号
	 */
	public void setVoucherNo(java.lang.String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  委托日期
	 */
	@Column(name ="CONSIGN_DATE",nullable=true)
	public java.util.Date getConsignDate(){
		return this.consignDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  委托日期
	 */
	public void setConsignDate(java.util.Date consignDate){
		this.consignDate = consignDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托收款单号
	 */
	@Column(name ="CONSIGN_CODE",nullable=true,length=20)
	public java.lang.String getConsignCode(){
		return this.consignCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托收款单号
	 */
	public void setConsignCode(java.lang.String consignCode){
		this.consignCode = consignCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷方
	 */
	@Column(name ="CREDIT_ACC",nullable=true,length=500)
	public java.lang.String getCreditAcc(){
		return this.creditAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷方
	 */
	public void setCreditAcc(java.lang.String creditAcc){
		this.creditAcc = creditAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借方
	 */
	@Column(name ="DEBIT_ACC",nullable=true,length=500)
	public java.lang.String getDebitAcc(){
		return this.debitAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借方
	 */
	public void setDebitAcc(java.lang.String debitAcc){
		this.debitAcc = debitAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合同号
	 */
	@Column(name ="CONTRACT_CODE",nullable=true,length=40)
	public java.lang.String getContractCode(){
		return this.contractCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合同号
	 */
	public void setContractCode(java.lang.String contractCode){
		this.contractCode = contractCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账员
	 */
	@Column(name ="TALLIER_CODE",nullable=true,length=32)
	public java.lang.String getTallierCode(){
		return this.tallierCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账员
	 */
	public void setTallierCode(java.lang.String tallierCode){
		this.tallierCode = tallierCode;
	}
	@Column(name ="CHECKER_CODE",nullable=true,length=32)
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记账日期
	 */
	@Column(name ="TALLY_DATE",nullable=true)
	public java.util.Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  记账日期
	 */
	public void setTallyDate(java.util.Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  产生编码日期
	 */
	@Column(name ="ENCRY_DATE",nullable=true)
	public java.util.Date getEncryDate(){
		return this.encryDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  产生编码日期
	 */
	public void setEncryDate(java.util.Date encryDate){
		this.encryDate = encryDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  加密随机数
	 */
	@Column(name ="RANDOM",nullable=true,length=20)
	public java.lang.String getRandom(){
		return this.random;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加密随机数
	 */
	public void setRandom(java.lang.String random){
		this.random = random;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  编码源
	 */
	@Column(name ="ENCRY_SOURCE",nullable=true,length=200)
	public java.lang.String getEncrySource(){
		return this.encrySource;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  编码源
	 */
	public void setEncrySource(java.lang.String encrySource){
		this.encrySource = encrySource;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  subscriber
	 */
	@Column(name ="SUBSCRIBER",nullable=true,length=50)
	public java.lang.String getSubscriber(){
		return this.subscriber;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  subscriber
	 */
	public void setSubscriber(java.lang.String subscriber){
		this.subscriber = subscriber;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  验证码
	 */
	@Column(name ="VALIDATE_CODE",nullable=true,length=100)
	public java.lang.String getValidateCode(){
		return this.validateCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  验证码
	 */
	public void setValidateCode(java.lang.String validateCode){
		this.validateCode = validateCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  验证标志10-已验证11-验证失败12-未验证默认为12
	 */
	@Column(name ="IS_VALIDATE",nullable=true,length=2)
	public java.lang.Integer getIsValidate(){
		return this.isValidate;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  验证标志10-已验证11-验证失败12-未验证默认为12
	 */
	public void setIsValidate(java.lang.Integer isValidate){
		this.isValidate = isValidate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  验证人
	 */
	@Column(name ="VALID_BY",nullable=true,length=50)
	public java.lang.String getValidBy(){
		return this.validBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  验证人
	 */
	public void setValidBy(java.lang.String validBy){
		this.validBy = validBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  验证时间
	 */
	@Column(name ="VALID_DATE",nullable=true)
	public java.util.Date getValidDate(){
		return this.validDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  验证时间
	 */
	public void setValidDate(java.util.Date validDate){
		this.validDate = validDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  打印次数
	 */
	@Column(name ="PRINTS",nullable=true,length=10)
	public java.lang.Integer getPrints(){
		return this.prints;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  打印次数
	 */
	public void setPrints(java.lang.Integer prints){
		this.prints = prints;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态1，新输入-1，删除2，冲帐
	 */
	@Column(name ="STATUS",nullable=false,length=10)
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态1，新输入-1，删除2，冲帐
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
}
