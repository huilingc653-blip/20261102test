package com.bytter.biz.receipt.entity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 承兑汇票电子回单表
 * @author onlineGenerator
 * @date 2017-07-04 15:30:24
 * @version 1.0
 *
 */
@Entity
@Table(name = "E_ACCEPT_BILL", schema = "")
@SuppressWarnings("serial")
public class EAcceptBill extends IdEntity implements java.io.Serializable {
	/**起息日期*/
	private java.util.Date accrualDate;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**记账员*/
	private java.lang.String tallierCode;
	/**复核员*/
	private String checkerCode;
	/**凭证号*/
	private java.lang.String voucherNo;
	/**原始凭证号*/
	private java.lang.String oldVoucher;
	/**原始凭证号ID*/
	private java.lang.String oldVoucherId;
	/**原始凭证名称*/
	private java.lang.String oldVoucherName;
	/**原凭证金额*/
	private java.math.BigDecimal oldVoucherMoney;
	/**借方科目*/
	private java.lang.String debit;
	/**贷方科目*/
	private java.lang.String credit;
	/**借方科目1*/
	private java.lang.String debit1;
	/**贷方科目1*/
	private java.lang.String credit1;
	/**贴现利息收入*/
	private BigDecimal accessory;
	/**附言*/
	private java.lang.String remark;
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
	/**电子回单号码*/
	private java.lang.String billNo;
	/**单位ID*/
	private java.lang.String corpId;
	/**电子回单类型16-承兑汇票电子回单*/
	private java.lang.String billType;
	/**付款人名称*/
	private java.lang.String payerName;
	/**付款人账号*/
	private java.lang.String payerAcc;
	/**付款人开户行*/
	private java.lang.String payerBank;
	/**收款人名称*/
	private java.lang.String payeeName;
	/**收款人账号*/
	private java.lang.String payeeAcc;
	/**收款人开户行*/
	private java.lang.String payeeBank;
	/**币别*/
	private java.lang.String curId;
	/**金额*/
	private java.math.BigDecimal payMoney;
	/**用途*/
	private java.lang.String purpose;
	/**结算方式*/
	private java.lang.String payType;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  起息日期
	 */
	@Column(name ="ACCRUAL_DATE",nullable=true)
	public java.util.Date getAccrualDate(){
		return this.accrualDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  起息日期
	 */
	public void setAccrualDate(java.util.Date accrualDate){
		this.accrualDate = accrualDate;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始凭证号
	 */
	@Column(name ="OLD_VOUCHER",nullable=true,length=50)
	public java.lang.String getOldVoucher(){
		return this.oldVoucher;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始凭证号
	 */
	public void setOldVoucher(java.lang.String oldVoucher){
		this.oldVoucher = oldVoucher;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始凭证号ID
	 */
	@Column(name ="OLD_VOUCHER_ID",nullable=true,length=50)
	public java.lang.String getOldVoucherId(){
		return this.oldVoucherId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始凭证号ID
	 */
	public void setOldVoucherId(java.lang.String oldVoucherId){
		this.oldVoucherId = oldVoucherId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始凭证名称
	 */
	@Column(name ="OLD_VOUCHER_NAME",nullable=true,length=50)
	public java.lang.String getOldVoucherName(){
		return this.oldVoucherName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始凭证名称
	 */
	public void setOldVoucherName(java.lang.String oldVoucherName){
		this.oldVoucherName = oldVoucherName;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  原凭证金额
	 */
	@Column(name ="OLD_VOUCHER_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getOldVoucherMoney(){
		return this.oldVoucherMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  原凭证金额
	 */
	public void setOldVoucherMoney(java.math.BigDecimal oldVoucherMoney){
		this.oldVoucherMoney = oldVoucherMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借方科目
	 */
	@Column(name ="DEBIT",nullable=true,length=500)
	public java.lang.String getDebit(){
		return this.debit;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借方科目
	 */
	public void setDebit(java.lang.String debit){
		this.debit = debit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷方科目
	 */
	@Column(name ="CREDIT",nullable=true,length=500)
	public java.lang.String getCredit(){
		return this.credit;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷方科目
	 */
	public void setCredit(java.lang.String credit){
		this.credit = credit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借方科目1
	 */
	@Column(name ="DEBIT1",nullable=true,length=500)
	public java.lang.String getDebit1(){
		return this.debit1;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借方科目1
	 */
	public void setDebit1(java.lang.String debit1){
		this.debit1 = debit1;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷方科目1
	 */
	@Column(name ="CREDIT1",nullable=true,length=500)
	public java.lang.String getCredit1(){
		return this.credit1;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷方科目1
	 */
	public void setCredit1(java.lang.String credit1){
		this.credit1 = credit1;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  贴现利息收入
	 */
	@Column(name ="ACCESSORY",nullable=true,length=10)
	public BigDecimal getAccessory(){
		return this.accessory;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  贴现利息收入
	 */
	public void setAccessory(BigDecimal accessory){
		this.accessory = accessory;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附言
	 */
	@Column(name ="REMARK",nullable=true,length=200)
	public java.lang.String getRemark(){
		return this.remark;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附言
	 */
	public void setRemark(java.lang.String remark){
		this.remark = remark;
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子回单号码
	 */
	@Column(name ="BILL_NO",nullable=false,length=13)
	public java.lang.String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单号码
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
	 *@return: java.lang.String  电子回单类型16-承兑汇票电子回单
	 */
	@Column(name ="BILL_TYPE",nullable=false,length=2)
	public java.lang.String getBillType(){
		return this.billType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单类型16-承兑汇票电子回单
	 */
	public void setBillType(java.lang.String billType){
		this.billType = billType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款人名称
	 */
	@Column(name ="PAYER_NAME",nullable=true,length=200)
	public java.lang.String getPayerName(){
		return this.payerName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款人名称
	 */
	public void setPayerName(java.lang.String payerName){
		this.payerName = payerName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款人账号
	 */
	@Column(name ="PAYER_ACC",nullable=false,length=100)
	public java.lang.String getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款人账号
	 */
	public void setPayerAcc(java.lang.String payerAcc){
		this.payerAcc = payerAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款人开户行
	 */
	@Column(name ="PAYER_BANK",nullable=true,length=100)
	public java.lang.String getPayerBank(){
		return this.payerBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款人开户行
	 */
	public void setPayerBank(java.lang.String payerBank){
		this.payerBank = payerBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款人名称
	 */
	@Column(name ="PAYEE_NAME",nullable=true,length=200)
	public java.lang.String getPayeeName(){
		return this.payeeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款人名称
	 */
	public void setPayeeName(java.lang.String payeeName){
		this.payeeName = payeeName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款人账号
	 */
	@Column(name ="PAYEE_ACC",nullable=true,length=100)
	public java.lang.String getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款人账号
	 */
	public void setPayeeAcc(java.lang.String payeeAcc){
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款人开户行
	 */
	@Column(name ="PAYEE_BANK",nullable=true,length=100)
	public java.lang.String getPayeeBank(){
		return this.payeeBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款人开户行
	 */
	public void setPayeeBank(java.lang.String payeeBank){
		this.payeeBank = payeeBank;
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
	@Column(name ="PURPOSE",nullable=true,length=160)
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
	@Column(name ="PAY_TYPE",nullable=false,length=50)
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
}
