package com.bytter.biz.finacial.accbook.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: FC_ACCBOOK_BLNED_DTL
 * @author onlineGenerator
 * @date 2017-06-19 11:28:25
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_ACCBOOK_BLNED_DTL", schema = "")
@SuppressWarnings("serial")
public class FcAccbookBlnedDtl extends IdEntity implements java.io.Serializable {
	/**流水ID*/
	private java.lang.String bookId;
	/**行号*/
	private java.lang.Integer lineNo;
	/**摘要*/
	private java.lang.String summaryContent;
	/**账号*/
	private java.lang.String account;
	/**币别代码
*/
	private java.lang.String curCode;
	/**账户名称*/
	private java.lang.String accName;
	/**单价*/
	private java.math.BigDecimal price;
	/**数量*/
	private java.lang.Integer amount;
	/**借方发生额*/
	private java.math.BigDecimal debitMoney;
	/**贷方发生额*/
	private java.math.BigDecimal creditMoney;
	/**余额*/
	private java.math.BigDecimal balance;
	/**凭证种类代码*/
	private java.lang.String voucherType;
	/**原始凭证号*/
	private java.lang.String origVoucherNo;
	/**原始凭证Id*/
	private java.lang.String origVoucherId;
	/**现金流量分类*/
	private java.lang.String cashFlux;
	/**资金计划挂接状态*/
	private String fbsStatus;
	private String subjectId;
	private String subjectName;
	/**
	 * 网点id
	 */
	private String netId;
	/**
	 * 银行类别code
	 */
	private String bankCode;
	/**
	 * 科目代码code
	 */
	private String subjectCode;
	/**
	 * 余额方向
	 */
	private Integer balanceWay;
	//增加 subject_id、subject_name

	@Column(name ="BALANCE_WAY")
	public Integer getBalanceWay() {
		return balanceWay;
	}

	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}

	@Column(name ="SUBJECT_ID")
	public String getSubjectId(){
		return this.subjectId;
	}
	public void setSubjectId(java.lang.String subjectId){
		this.subjectId = subjectId;
	}
	@Column(name ="SUBJECT_NAME")
	public String getSubjectName(){
		return this.subjectName;
	}
	public void setSubjectName(java.lang.String subjectName){
		this.subjectName = subjectName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水ID
	 */
	@Column(name ="BOOK_ID")
	public java.lang.String getBookId(){
		return this.bookId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水ID
	 */
	public void setBookId(java.lang.String bookId){
		this.bookId = bookId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  行号
	 */
	@Column(name ="LINE_NO")
	public java.lang.Integer getLineNo(){
		return this.lineNo;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  行号
	 */
	public void setLineNo(java.lang.Integer lineNo){
		this.lineNo = lineNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="SUMMARY_CONTENT")
	public java.lang.String getSummaryContent(){
		return this.summaryContent;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要
	 */
	public void setSummaryContent(java.lang.String summaryContent){
		this.summaryContent = summaryContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="ACCOUNT")
	public java.lang.String getAccount(){
		return this.account;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号
	 */
	public void setAccount(java.lang.String account){
		this.account = account;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_CODE")
	public java.lang.String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurCode(java.lang.String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户名称
	 */
	@Column(name ="ACC_NAME")
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  单价
	 */
	@Column(name ="PRICE")
	public java.math.BigDecimal getPrice(){
		return this.price;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  单价
	 */
	public void setPrice(java.math.BigDecimal price){
		this.price = price;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  数量
	 */
	@Column(name ="AMOUNT")
	public java.lang.Integer getAmount(){
		return this.amount;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  数量
	 */
	public void setAmount(java.lang.Integer amount){
		this.amount = amount;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  借方发生额
	 */
	@Column(name ="DEBIT_MONEY")
	public java.math.BigDecimal getDebitMoney(){
		return this.debitMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  借方发生额
	 */
	public void setDebitMoney(java.math.BigDecimal debitMoney){
		this.debitMoney = debitMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贷方发生额
	 */
	@Column(name ="CREDIT_MONEY")
	public java.math.BigDecimal getCreditMoney(){
		return this.creditMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贷方发生额
	 */
	public void setCreditMoney(java.math.BigDecimal creditMoney){
		this.creditMoney = creditMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  余额
	 */
	@Column(name ="BALANCE")
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
	 *@return: java.lang.String  凭证种类代码
	 */
	@Column(name ="VOUCHER_TYPE")
	public java.lang.String getVoucherType(){
		return this.voucherType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证种类代码
	 */
	public void setVoucherType(java.lang.String voucherType){
		this.voucherType = voucherType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始凭证号
	 */
	@Column(name ="ORIG_VOUCHER_NO")
	public java.lang.String getOrigVoucherNo(){
		return this.origVoucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始凭证号
	 */
	public void setOrigVoucherNo(java.lang.String origVoucherNo){
		this.origVoucherNo = origVoucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始凭证ID
	 */
	@Column(name ="ORIG_VOUCHER_ID")
	public java.lang.String getOrigVoucherId(){
		return this.origVoucherId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始凭证ID
	 */
	public void setOrigVoucherId(java.lang.String origVoucherId){
		this.origVoucherId = origVoucherId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  现金流量分类
	 */
	@Column(name ="CASH_FLUX")
	public java.lang.String getCashFlux(){
		return this.cashFlux;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  现金流量分类
	 */
	public void setCashFlux(java.lang.String cashFlux){
		this.cashFlux = cashFlux;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  资金计划挂接状态
	 */
	@Column(name ="FBS_STATUS")
	public String getFbsStatus() {
		return fbsStatus;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  资金计划挂接状态
	 */
	public void setFbsStatus(String fbsStatus) {
		this.fbsStatus = fbsStatus;
	}
	@Column(name ="NET_ID")
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	@Column(name ="BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	@Column(name ="SYS_SUBJECT_CODE")
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
}
