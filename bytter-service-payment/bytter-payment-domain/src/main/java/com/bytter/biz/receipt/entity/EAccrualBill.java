package com.bytter.biz.receipt.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 利息清单电子回单
 * @author onlineGenerator
 * @date 2017-07-04 15:27:13
 * @version 1.0
 *
 */
@Entity
@Table(name = "E_ACCRUAL_BILL", schema = "")
@SuppressWarnings("serial")
public class EAccrualBill extends IdEntity implements java.io.Serializable {
	/**打印次数*/
	private java.lang.Integer prints;
	/**状态1，新输入-1，删除2， 冲帐*/
	private java.lang.Integer status;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**电子回单号A+年年月月日日+6位序列号(从000001开始)*/
	private java.lang.String billNo;
	/**单位ID*/
	private java.lang.String corpId;
	/**电子回单类型01-存款利息单02-贷款利息单03-委托贷款利息清单04-定期存款利息单05-通知存款利息单19-活期保证金利息单20-定期保证金利息单*/
	private java.lang.String billType;
	/**利息产生日期（记账日期）*/
	private java.util.Date tallyDate;
	/**账号*/
	private java.lang.String accNo;
	/**户名*/
	private java.lang.String accName;
	/**币别*/
	private java.lang.String curId;
	/**开始日期*/
	private java.util.Date startDate;
	/**结束日期*/
	private java.util.Date endDate;
	/**利息*/
	private java.math.BigDecimal accrual;
	/**积数*/
	private java.math.BigDecimal cumu;
	/**利率*/
	private java.math.BigDecimal interest;
	/**天数*/
	private java.lang.Integer days;
	/**对方科目*/
	private java.lang.String otherSubject;
	/**本方科目*/
	private java.lang.String homeSubject;
	/**本金*/
	private java.math.BigDecimal money;
	/**本息合计*/
	private java.math.BigDecimal nummoney;
	/**凭证号*/
	private java.lang.String voucherNo;
	/**备注*/
	private java.lang.String rmk;
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
	/**利息清单明细ID*/
	private String interestDtlId;
	/**复核员*/
	private java.lang.String checkerCode;
	/**记账员*/
	private java.lang.String tallierCode;
	/**合同金额*/
	private java.math.BigDecimal totolMoney;
	/**已还款金额*/
	private java.math.BigDecimal repayMoney;
	/**摘要*/
	private java.lang.String summaryContent;
	/**原始凭证号*/
	private java.lang.String origVoucherNo;
	/**原始凭证id*/
	private java.lang.String origVoucherId;
	@Column(name ="TOTOL_MONEY",nullable=true,scale=2,length=18)
	public java.math.BigDecimal getTotolMoney() {
		return totolMoney;
	}
	public void setTotolMoney(java.math.BigDecimal totolMoney) {
		this.totolMoney = totolMoney;
	}
	@Column(name ="REPAY_MONEY",nullable=true,scale=2,length=18)
	public java.math.BigDecimal getRepayMoney() {
		return repayMoney;
	}
	public void setRepayMoney(java.math.BigDecimal repayMoney) {
		this.repayMoney = repayMoney;
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
	 *@return: java.lang.String  电子回单号A+年年月月日日+6位序列号(从000001开始)
	 */
	@Column(name ="BILL_NO",nullable=false,length=13)
	public java.lang.String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单号A+年年月月日日+6位序列号(从000001开始)
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
	 *@return: java.lang.String  电子回单类型01-存款利息单02-贷款利息单03-委托贷款利息清单04-定期利息单05-通知利息单19-活期保证金利息单20-定期保证金利息单
	 */
	@Column(name ="BILL_TYPE",nullable=true,length=2)
	public java.lang.String getBillType(){
		return this.billType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单类型01-存款利息单02-贷款利息单03-委托贷款利息清单04-定期利息单05-通知利息单19-活期保证金利息单20-定期保证金利息单
	 */
	public void setBillType(java.lang.String billType){
		this.billType = billType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  利息产生日期（记账日期）
	 */
	@Column(name ="TALLY_DATE",nullable=true)
	public java.util.Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  利息产生日期（记账日期）
	 */
	public void setTallyDate(java.util.Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="ACC_NO",nullable=false,length=100)
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
	 *@return: java.lang.String  户名
	 */
	@Column(name ="ACC_NAME",nullable=true,length=100)
	public java.lang.String getAccName(){
		return this.accName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  户名
	 */
	public void setAccName(java.lang.String accName){
		this.accName = accName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_ID",nullable=false,length=32)
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  开始日期
	 */
	@Column(name ="START_DATE",nullable=false)
	public java.util.Date getStartDate(){
		return this.startDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  开始日期
	 */
	public void setStartDate(java.util.Date startDate){
		this.startDate = startDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结束日期
	 */
	@Column(name ="END_DATE",nullable=false)
	public java.util.Date getEndDate(){
		return this.endDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结束日期
	 */
	public void setEndDate(java.util.Date endDate){
		this.endDate = endDate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利息
	 */
	@Column(name ="ACCRUAL",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getAccrual(){
		return this.accrual;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利息
	 */
	public void setAccrual(java.math.BigDecimal accrual){
		this.accrual = accrual;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  积数
	 */
	@Column(name ="CUMU",nullable=true,scale=2,length=18)
	public java.math.BigDecimal getCumu(){
		return this.cumu;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  积数
	 */
	public void setCumu(java.math.BigDecimal cumu){
		this.cumu = cumu;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利率
	 */
	@Column(name ="INTEREST",nullable=true,scale=6,length=14)
	public java.math.BigDecimal getInterest(){
		return this.interest;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利率
	 */
	public void setInterest(java.math.BigDecimal interest){
		this.interest = interest;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  天数
	 */
	@Column(name ="DAYS",nullable=true,length=10)
	public java.lang.Integer getDays(){
		return this.days;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  天数
	 */
	public void setDays(java.lang.Integer days){
		this.days = days;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对方科目
	 */
	@Column(name ="OTHER_SUBJECT",nullable=false,length=500)
	public java.lang.String getOtherSubject(){
		return this.otherSubject;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对方科目
	 */
	public void setOtherSubject(java.lang.String otherSubject){
		this.otherSubject = otherSubject;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  本方科目
	 */
	@Column(name ="HOME_SUBJECT",nullable=false,length=500)
	public java.lang.String getHomeSubject(){
		return this.homeSubject;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  本方科目
	 */
	public void setHomeSubject(java.lang.String homeSubject){
		this.homeSubject = homeSubject;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  本金
	 */
	@Column(name ="MONEY",nullable=true,scale=2,length=18)
	public java.math.BigDecimal getMoney(){
		return this.money;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  本金
	 */
	public void setMoney(java.math.BigDecimal money){
		this.money = money;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  本息合计
	 */
	@Column(name ="NUMMONEY",nullable=true,scale=2,length=18)
	public java.math.BigDecimal getNummoney(){
		return this.nummoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  本息合计
	 */
	public void setNummoney(java.math.BigDecimal nummoney){
		this.nummoney = nummoney;
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
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=false,length=50)
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
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
	@Column(name ="INTEREST_DTL_ID",nullable=true,length=32)
	public String getInterestDtlId() {
		return interestDtlId;
	}
	public void setInterestDtlId(String interestDtlId) {
		this.interestDtlId = interestDtlId;
	}
	@Column(name ="CHECKER_CODE",nullable=true,length=32)
	public java.lang.String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(java.lang.String checkerCode) {
		this.checkerCode = checkerCode;
	}
	@Column(name ="TALLIER_CODE",nullable=true,length=32)
	public java.lang.String getTallierCode() {
		return tallierCode;
	}
	public void setTallierCode(java.lang.String tallierCode) {
		this.tallierCode = tallierCode;
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
