package com.bytter.biz.receipt.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 对账单电子回单表
 * @author onlineGenerator
 * @date 2017-07-04 15:30:28
 * @version 1.0
 *
 */
@Entity
@Table(name = "E_COMPARE_BILL", schema = "")
@SuppressWarnings("serial")
public class ECompareBill extends IdEntity implements java.io.Serializable {
	/**billNo*/
	private String billNo;
	/**单位ID*/
	private String corpId;
	/**电子回单类型17-对账单电子回单*/
	private String billType;
	/**内部账号*/
	private String accNo;
	/**账户名称*/
	private String accName;
	/**summaryComment*/
	private String summaryComment;
	/**记账员*/
	private String tallyerCode;
	/**tallyDate*/
	private java.util.Date tallyDate;
	/**币别*/
	private String curId;
	/**对账单票据号码*/
	private String billCode;
	/**interestStartDate*/
	private java.util.Date interestStartDate;
	/**借方金额*/
	private java.math.BigDecimal debitMoney;
	/**贷方金额*/
	private java.math.BigDecimal creditMoney;
	/**余额*/
	private java.math.BigDecimal balance;
	/**凭证号*/
	private String voucherNo;
	/**红蓝标志（R -红，B 蓝）*/
	private String redblueSign;
	/**产生编码日期*/
	private java.util.Date encryDate;
	/**加密随机数*/
	private String random;
	/**编码源*/
	private String encrySource;
	/**subscriber*/
	private String subscriber;
	/**验证码*/
	private String validateCode;
	/**验证标志10-已验证11-验证失败12-未验证默认为12*/
	private Integer isValidate;
	/**验证人*/
	private String validBy;
	/**验证时间*/
	private java.util.Date validDate;
	/**打印次数*/
	private Integer prints;
	/**状态1，新输入-1，删除2，冲帐*/
	private Integer status;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**行号*/
	private Integer lineNo;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  billNo
	 */
	@Column(name ="BILL_NO",nullable=false,length=13)
	public String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  billNo
	 */
	public void setBillNo(String billNo){
		this.billNo = billNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位ID
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子回单类型17-对账单电子回单
	 */
	@Column(name ="BILL_TYPE",nullable=false,length=2)
	public String getBillType(){
		return this.billType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单类型17-对账单电子回单
	 */
	public void setBillType(String billType){
		this.billType = billType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  内部账号
	 */
	@Column(name ="ACC_NO",nullable=true,length=50)
	public String getAccNo(){
		return this.accNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  内部账号
	 */
	public void setAccNo(String accNo){
		this.accNo = accNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户名称
	 */
	@Column(name ="ACC_NAME",nullable=true,length=90)
	public String getAccName(){
		return this.accName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户名称
	 */
	public void setAccName(String accName){
		this.accName = accName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  summaryComment
	 */
	@Column(name ="SUMMARY_COMMENT",nullable=true,length=300)
	public String getSummaryComment(){
		return this.summaryComment;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  summaryComment
	 */
	public void setSummaryComment(String summaryComment){
		this.summaryComment = summaryComment;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账员
	 */
	@Column(name ="TALLYER_CODE",nullable=true,length=20)
	public String getTallyerCode(){
		return this.tallyerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账员
	 */
	public void setTallyerCode(String tallyerCode){
		this.tallyerCode = tallyerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  tallyDate
	 */
	@Column(name ="TALLY_DATE",nullable=true)
	public java.util.Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  tallyDate
	 */
	public void setTallyDate(java.util.Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
	public String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别
	 */
	public void setCurId(String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对账单票据号码
	 */
	@Column(name ="BILL_CODE",nullable=true,length=30)
	public String getBillCode(){
		return this.billCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对账单票据号码
	 */
	public void setBillCode(String billCode){
		this.billCode = billCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  interestStartDate
	 */
	@Column(name ="INTEREST_START_DATE",nullable=true)
	public java.util.Date getInterestStartDate(){
		return this.interestStartDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  interestStartDate
	 */
	public void setInterestStartDate(java.util.Date interestStartDate){
		this.interestStartDate = interestStartDate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  借方金额
	 */
	@Column(name ="DEBIT_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getDebitMoney(){
		return this.debitMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  借方金额
	 */
	public void setDebitMoney(java.math.BigDecimal debitMoney){
		this.debitMoney = debitMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贷方金额
	 */
	@Column(name ="CREDIT_MONEY",nullable=true,scale=2,length=38)
	public java.math.BigDecimal getCreditMoney(){
		return this.creditMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贷方金额
	 */
	public void setCreditMoney(java.math.BigDecimal creditMoney){
		this.creditMoney = creditMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  余额
	 */
	@Column(name ="BALANCE",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBalance(){
		return this.balance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  余额
	 */
	public void setBalance(java.math.BigDecimal balance){
		this.balance = balance;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证号
	 */
	@Column(name ="VOUCHER_NO",nullable=true,length=40)
	public String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证号
	 */
	public void setVoucherNo(String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  红蓝标志（R -红，B 蓝）
	 */
	@Column(name ="REDBLUE_SIGN",nullable=true,length=1)
	public String getRedblueSign(){
		return this.redblueSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  红蓝标志（R -红，B 蓝）
	 */
	public void setRedblueSign(String redblueSign){
		this.redblueSign = redblueSign;
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
	public String getRandom(){
		return this.random;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加密随机数
	 */
	public void setRandom(String random){
		this.random = random;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  编码源
	 */
	@Column(name ="ENCRY_SOURCE",nullable=true,length=200)
	public String getEncrySource(){
		return this.encrySource;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  编码源
	 */
	public void setEncrySource(String encrySource){
		this.encrySource = encrySource;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  subscriber
	 */
	@Column(name ="SUBSCRIBER",nullable=true,length=50)
	public String getSubscriber(){
		return this.subscriber;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  subscriber
	 */
	public void setSubscriber(String subscriber){
		this.subscriber = subscriber;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  验证码
	 */
	@Column(name ="VALIDATE_CODE",nullable=true,length=100)
	public String getValidateCode(){
		return this.validateCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  验证码
	 */
	public void setValidateCode(String validateCode){
		this.validateCode = validateCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  验证标志10-已验证11-验证失败12-未验证默认为12
	 */
	@Column(name ="IS_VALIDATE",nullable=true,length=2)
	public Integer getIsValidate(){
		return this.isValidate;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  验证标志10-已验证11-验证失败12-未验证默认为12
	 */
	public void setIsValidate(Integer isValidate){
		this.isValidate = isValidate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  验证人
	 */
	@Column(name ="VALID_BY",nullable=true,length=50)
	public String getValidBy(){
		return this.validBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  验证人
	 */
	public void setValidBy(String validBy){
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
	public Integer getPrints(){
		return this.prints;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  打印次数
	 */
	public void setPrints(Integer prints){
		this.prints = prints;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态1，新输入-1，删除2，冲帐
	 */
	@Column(name ="STATUS",nullable=false,length=10)
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态1，新输入-1，删除2，冲帐
	 */
	public void setStatus(Integer status){
		this.status = status;
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
	@Column(name ="LINE_NO")
	public Integer getLineNo() {
		return lineNo;
	}
	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}
}
