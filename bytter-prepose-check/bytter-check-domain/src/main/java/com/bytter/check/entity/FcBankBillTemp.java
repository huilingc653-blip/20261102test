package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 银行未达账初始化
 * @author onlineGenerator
 * @date 2017-06-08 09:16:58
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_BANK_BILL_TEMP", schema = "")
@SuppressWarnings("serial")
public class FcBankBillTemp extends IdEntity implements java.io.Serializable {
	/**凭证号*/
	private String voucherNo;
	/**凭证流水号*/
	private String voucherOrderNo;
	/**记账日期*/
	private Date tallyDate;
	/**账号*/
	private String account;
	/**币别代码*/
	private String curId;
	/**银行账号ID*/
	private String bankAccId;
	/**银行账号*/
	private String bankAcc;
	/**借方发生额*/
	private BigDecimal debitMoney;
	/**贷方发生额*/
	private BigDecimal creditMoney;
	/**余额*/
	private BigDecimal balance;
	/**红蓝标志*/
	private String redblueSign;
	/**对账标志*/
	private String compareSign;
	/**序号*/
	private String bankId;
	/**票据号*/
	private String billNo;
	/**调节日期*/
	private Date adjustDate;
	/**输入日期*/
	private Date importDate;
	/**输入类型*/
	private String importType;
	/**行号*/
	private Integer lineNo;
	/**摘要*/
	private String summaryContent;
	/**ERP凭证号*/
	private String erpVoucherNo;
	/**收款账号*/
	private String payeeAcc;
	/**导入用户*/
	private String importUser;
	/**对账时间*/
	private Date checkDate;
	/**对账勾对用户*/
	private String checkUser;
	/**最后更新用户代码*/
	private String updateBy;
	/**最后更新时间*/
	private Date updateTime;
	/**创建用户代码*/
	private String createBy;
	/**创建时间*/
	private Date createTime;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证号
	 */
	@Column(name ="VOUCHER_NO")
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
	 *@return: java.lang.String  凭证流水号
	 */
	@Column(name ="VOUCHER_ORDER_NO")
	public String getVoucherOrderNo(){
		return this.voucherOrderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证流水号
	 */
	public void setVoucherOrderNo(String voucherOrderNo){
		this.voucherOrderNo = voucherOrderNo;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记账日期
	 */
	@Column(name ="TALLY_DATE")
	public Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  记账日期
	 */
	public void setTallyDate(Date tallyDate){
		this.tallyDate = tallyDate;
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
	@Column(name ="CUR_ID")
	public String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurId(String curId){
		this.curId = curId;
	}
	@Column(name ="BANK_ACC_ID")
	public String getBankAccId() {
		return bankAccId;
	}
	@Column(name ="BANK_ACC_ID")
	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行账号
	 */
	@Column(name ="BANK_ACC")
	public String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行账号
	 */
	public void setBankAcc(String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  借方发生额
	 */
	@Column(name ="DEBIT_MONEY")
	public BigDecimal getDebitMoney(){
		return this.debitMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  借方发生额
	 */
	public void setDebitMoney(BigDecimal debitMoney){
		this.debitMoney = debitMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贷方发生额
	 */
	@Column(name ="CREDIT_MONEY")
	public BigDecimal getCreditMoney(){
		return this.creditMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贷方发生额
	 */
	public void setCreditMoney(BigDecimal creditMoney){
		this.creditMoney = creditMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  余额
	 */
	@Column(name ="BALANCE")
	public BigDecimal getBalance(){
		return this.balance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  余额
	 */
	public void setBalance(BigDecimal balance){
		this.balance = balance;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  红蓝标志
	 */
	@Column(name ="REDBLUE_SIGN")
	public String getRedblueSign(){
		return this.redblueSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  红蓝标志
	 */
	public void setRedblueSign(String redblueSign){
		this.redblueSign = redblueSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对账标志
	 */
	@Column(name ="COMPARE_SIGN")
	public String getCompareSign(){
		return this.compareSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对账标志
	 */
	public void setCompareSign(String compareSign){
		this.compareSign = compareSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  序号
	 */
	@Column(name ="BANK_ID")
	public String getBankId(){
		return this.bankId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  序号
	 */
	public void setBankId(String bankId){
		this.bankId = bankId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据号
	 */
	@Column(name ="BILL_NO")
	public String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据号
	 */
	public void setBillNo(String billNo){
		this.billNo = billNo;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  调节日期
	 */
	@Column(name ="ADJUST_DATE")
	public Date getAdjustDate(){
		return this.adjustDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  调节日期
	 */
	public void setAdjustDate(Date adjustDate){
		this.adjustDate = adjustDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  输入日期
	 */
	@Column(name ="IMPORT_DATE")
	public Date getImportDate(){
		return this.importDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  输入日期
	 */
	public void setImportDate(Date importDate){
		this.importDate = importDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  输入类型
	 */
	@Column(name ="IMPORT_TYPE")
	public String getImportType(){
		return this.importType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  输入类型
	 */
	public void setImportType(String importType){
		this.importType = importType;
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
	 *@return: java.lang.String  ERP凭证号
	 */
	@Column(name ="ERP_VOUCHER_NO")
	public String getErpVoucherNo(){
		return this.erpVoucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ERP凭证号
	 */
	public void setErpVoucherNo(String erpVoucherNo){
		this.erpVoucherNo = erpVoucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款账号
	 */
	@Column(name ="PAYEE_ACC")
	public String getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号
	 */
	public void setPayeeAcc(String payeeAcc){
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  导入用户
	 */
	@Column(name ="IMPORT_USER")
	public String getImportUser(){
		return this.importUser;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  导入用户
	 */
	public void setImportUser(String importUser){
		this.importUser = importUser;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  对账时间
	 */
	@Column(name ="CHECK_DATE")
	public Date getCheckDate(){
		return this.checkDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  对账时间
	 */
	public void setCheckDate(Date checkDate){
		this.checkDate = checkDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对账勾对用户
	 */
	@Column(name ="CHECK_USER")
	public String getCheckUser(){
		return this.checkUser;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对账勾对用户
	 */
	public void setCheckUser(String checkUser){
		this.checkUser = checkUser;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后更新用户代码
	 */
	@Column(name ="UPDATE_BY")
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后更新时间
	 */
	@Column(name ="UPDATE_TIME")
	public Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户代码
	 */
	@Column(name ="CREATE_BY")
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME")
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
}
