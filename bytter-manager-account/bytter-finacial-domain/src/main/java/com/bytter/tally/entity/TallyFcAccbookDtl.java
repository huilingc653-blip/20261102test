package com.bytter.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.system.api.dto.FcAccDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**   
 * @Title: Entity
 * @Description: FC_ACCBOOK_DTL
 * @author onlineGenerator
 * @date 2017-05-31 15:02:54
 * @version 1.0
 * TODO 非本域实体，临时解决
 */
@Entity
@Table(name = "FC_ACCBOOK_DTL", schema = "")
@SuppressWarnings("serial")
public class TallyFcAccbookDtl extends IdEntity implements java.io.Serializable {
	/**流水ID*/
	private String bookId;
	/**行号*/
	private Integer lineNo;
	/**摘要*/
	private String summaryContent;
	/**账号*/
	private String account;
	/**币别代码*/
	private String curCode;
	/**账户名称*/
	private String accName;
	/**单价*/
	private java.math.BigDecimal price;
	/**数量*/
	private Integer amount;
	/**借方发生额*/
	private java.math.BigDecimal debitMoney;
	/**贷方发生额*/
	private java.math.BigDecimal creditMoney;
	/**余额*/
	private java.math.BigDecimal balance;
	/**凭证种类代码*/
	private String voucherType;
	/**原始凭证号*/
	private String origVoucherNo;
	/**原始业务ID*/
	private String origVoucherId;
	/**现金流量分类*/
	private String cashFlux;
	/**资金计划挂接状态*/
	private String fbsStatus;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水ID
	 */
	@Column(name ="BOOK_ID")
	public String getBookId(){
		return this.bookId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水ID
	 */
	public void setBookId(String bookId){
		this.bookId = bookId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  行号
	 */
	@Column(name ="LINE_NO")
	public Integer getLineNo(){
		return this.lineNo;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  行号
	 */
	public void setLineNo(Integer lineNo){
		this.lineNo = lineNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="SUMMARY_CONTENT")
	public String getSummaryContent(){
		return this.summaryContent;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要
	 */
	public void setSummaryContent(String summaryContent){
		this.summaryContent = summaryContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="ACCOUNT")
	public String getAccount(){
		return this.account;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号
	 */
	public void setAccount(String account){
		this.account = account;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_CODE")
	public String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurCode(String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户名称
	 */
	@Column(name ="ACC_NAME")
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
	public Integer getAmount(){
		return this.amount;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  数量
	 */
	public void setAmount(Integer amount){
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
	public String getVoucherType(){
		return this.voucherType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证种类代码
	 */
	public void setVoucherType(String voucherType){
		this.voucherType = voucherType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始凭证号
	 */
	@Column(name ="ORIG_VOUCHER_NO")
	public String getOrigVoucherNo(){
		return this.origVoucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始凭证号
	 */
	public void setOrigVoucherNo(String origVoucherNo){
		this.origVoucherNo = origVoucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始凭证ID
	 */
	@Column(name ="ORIG_VOUCHER_ID")
	public String getOrigVoucherId(){
		return this.origVoucherId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始凭证ID
	 */
	public void setOrigVoucherId(String origVoucherId){
		this.origVoucherId = origVoucherId;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  现金流量分类
	 */
	@Column(name ="CASH_FLUX")
	public String getCashFlux(){
		return this.cashFlux;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  现金流量分类
	 */
	public void setCashFlux(String cashFlux){
		this.cashFlux = cashFlux;
	}
	
	/**
	 * 账户信息
	 */
	private FcAccDTO accDTO;
	
	@Transient
	public FcAccDTO getAccDTO() {
		return accDTO;
	}
	public void setAccDTO(FcAccDTO accDTO) {
		this.accDTO = accDTO;
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
}
