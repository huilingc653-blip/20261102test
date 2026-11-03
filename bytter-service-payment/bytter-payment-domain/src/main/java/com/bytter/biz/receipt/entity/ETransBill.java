package com.bytter.biz.receipt.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 账户转换电子回单表
 * @author onlineGenerator
 * @date 2017-07-04 15:30:19
 * @version 1.0
 *
 */
@Entity
@Table(name = "E_TRANS_BILL", schema = "")
@SuppressWarnings("serial")
public class ETransBill extends IdEntity implements java.io.Serializable {
	/**电子回单号*/
	private java.lang.String billNo;
	/**单位ID*/
	private java.lang.String corpId;
	/**电子回单类型*/
	private java.lang.String billType;
	/**账号*/
	private java.lang.String accNo;
	/**账户名称*/
	private java.lang.String accName;
	/**起存日期*/
	private java.util.Date startDate;
	/**到期日期*/
	private java.util.Date endDate;
	/**币别*/
	private java.lang.String curId;
	/**利率*/
	private java.math.BigDecimal rate;
	/**期限*/
	private java.lang.Integer dayLimit;
	/**通知类型*/
	private java.lang.Integer notifyType;
	/**活期账号*/
	private java.lang.String currentAcc;
	/**活期账户名称*/
	private java.lang.String currentAccName;
	/**存款日期*/
	private java.util.Date depositDate;
	/**凭证号*/
	private java.lang.String voucherNo;
	/**存款利率*/
	private java.math.BigDecimal depositRate;
	/**money*/
	private java.math.BigDecimal money;
	/**记账员*/
	private java.lang.String tallierCode;
	/**复核员*/
	private java.lang.String checkerCode;
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
	/**验证标志*/
	private java.lang.Integer isValidate;
	/**验证人*/
	private java.lang.String validBy;
	/**验证时间*/
	private java.util.Date validDate;
	/**打印次数*/
	private java.lang.Integer prints;
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
	/**摘要*/
	private java.lang.String summaryContent;
	/**原始凭证号*/
	private java.lang.String origVoucherNo;
	/**原始凭证id*/
	private java.lang.String origVoucherId;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子回单号
	 */
	@Column(name ="BILL_NO",nullable=false,length=13)
	public java.lang.String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单号
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
	 *@return: java.lang.String  电子回单类型
	 */
	@Column(name ="BILL_TYPE",nullable=false,length=2)
	public java.lang.String getBillType(){
		return this.billType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单类型
	 */
	public void setBillType(java.lang.String billType){
		this.billType = billType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="ACC_NO",nullable=true,length=60)
	public java.lang.String getAccNo(){
		return this.accNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号
	 */
	public void setAccNo(java.lang.String accNo){
		this.accNo = accNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户名称
	 */
	@Column(name ="ACC_NAME",nullable=true,length=60)
	public java.lang.String getAccName(){
		return this.accName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户名称
	 */
	public void setAccName(java.lang.String accName){
		this.accName = accName;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  起存日期
	 */
	@Column(name ="START_DATE",nullable=true)
	public java.util.Date getStartDate(){
		return this.startDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  起存日期
	 */
	public void setStartDate(java.util.Date startDate){
		this.startDate = startDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  到期日期
	 */
	@Column(name ="END_DATE",nullable=true)
	public java.util.Date getEndDate(){
		return this.endDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  到期日期
	 */
	public void setEndDate(java.util.Date endDate){
		this.endDate = endDate;
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
	 *@return: java.math.BigDecimal  利率
	 */
	@Column(name ="RATE",nullable=true,scale=2,length=9)
	public java.math.BigDecimal getRate(){
		return this.rate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利率
	 */
	public void setRate(java.math.BigDecimal rate){
		this.rate = rate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  期限
	 */
	@Column(name ="DAY_LIMIT",nullable=true,length=10)
	public java.lang.Integer getDayLimit(){
		return this.dayLimit;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  期限
	 */
	public void setDayLimit(java.lang.Integer dayLimit){
		this.dayLimit = dayLimit;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  通知类型
	 */
	@Column(name ="NOTIFY_TYPE",nullable=true,length=10)
	public java.lang.Integer getNotifyType(){
		return this.notifyType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  通知类型
	 */
	public void setNotifyType(java.lang.Integer notifyType){
		this.notifyType = notifyType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  活期账号
	 */
	@Column(name ="CURRENT_ACC",nullable=true,length=60)
	public java.lang.String getCurrentAcc(){
		return this.currentAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  活期账号
	 */
	public void setCurrentAcc(java.lang.String currentAcc){
		this.currentAcc = currentAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  活期账户名称
	 */
	@Column(name ="CURRENT_ACC_NAME",nullable=true,length=60)
	public java.lang.String getCurrentAccName(){
		return this.currentAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  活期账户名称
	 */
	public void setCurrentAccName(java.lang.String currentAccName){
		this.currentAccName = currentAccName;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  存款日期
	 */
	@Column(name ="DEPOSIT_DATE",nullable=true)
	public java.util.Date getDepositDate(){
		return this.depositDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  存款日期
	 */
	public void setDepositDate(java.util.Date depositDate){
		this.depositDate = depositDate;
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  存款利率
	 */
	@Column(name ="DEPOSIT_RATE",nullable=true,scale=2,length=9)
	public java.math.BigDecimal getDepositRate(){
		return this.depositRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  存款利率
	 */
	public void setDepositRate(java.math.BigDecimal depositRate){
		this.depositRate = depositRate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  money
	 */
	@Column(name ="MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getMoney(){
		return this.money;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  money
	 */
	public void setMoney(java.math.BigDecimal money){
		this.money = money;
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复核员
	 */
	@Column(name ="CHECKER_CODE",nullable=true,length=32)
	public java.lang.String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核员
	 */
	public void setCheckerCode(java.lang.String checkerCode){
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
	 *@return: java.lang.Integer  验证标志
	 */
	@Column(name ="IS_VALIDATE",nullable=true,length=2)
	public java.lang.Integer getIsValidate(){
		return this.isValidate;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  验证标志
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
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=false,length=10)
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param:  java.lang.Integer  状态
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
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="SUMMARY_CONTENT")
	public String getSummaryContent() {
		return summaryContent;
	}
	/**
	 *方法: 设置java.lang.String
	 *@return: java.lang.String  摘要
	 */
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始凭证号
	 */
	@Column(name ="ORIG_VOUCHER_NO",nullable=true,length=40)
	public String getOrigVoucherNo() {
		return origVoucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始凭证号
	 */
	public void setOrigVoucherNo(String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始凭ID
	 */
	@Column(name ="ORIG_VOUCHER_ID",nullable=true,length=32)
	public String getOrigVoucherId() {
		return origVoucherId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始凭号ID
	 */
	public void setOrigVoucherId(String origVoucherId) {
		this.origVoucherId = origVoucherId;
	}
}
