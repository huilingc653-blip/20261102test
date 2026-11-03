package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: FC_COMPARE_ACC
 * @author onlineGenerator
 * @date 2017-06-02 17:21:47
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_COMPARE_ACC", schema = "")
@SuppressWarnings("serial")
public class FcCompareAcc extends IdEntity implements java.io.Serializable {
	/**对账类型*/
	private String checkType;
	/**流水号*/
	private String orderNo;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**企业账号*/
	private String compAcc;
	/**币别代码*/
	private String curId;
	/**借方发生额*/
	private java.math.BigDecimal debitMoney;
	/**贷方发生额*/
	private java.math.BigDecimal creditMoney;
	/**余额*/
	private java.math.BigDecimal balance;
	/**凭证号*/
	private String voucherNo;
	/**红蓝标志*/
	private String redblueSign;
	/**对账标志*/
	private String compareSign;
	/**摘要*/
	private String summaryContent;
	/**行号*/
	private java.math.BigDecimal lineNo;
	/**序号*/
	private String bankId;
	/**票据号*/
	private String billNo;
	/**起息日期*/
	private java.util.Date interestStartDate;
	/**调节日期*/
	private java.util.Date adjustDate;
	/**输入日期*/
	private java.util.Date importDate;
	/**输入类型*/
	private String importType;
	/**数据源*/
	private String dataSource;
	/**收款账号*/
	private String payeeAcc;
	/**导入用户*/
	private String importUser;
	/**对账时间*/
	private java.util.Date checkDate;
	/**对账勾对用户*/
	private String checkUser;
	/**最后更新用户代码*/
	private String updateBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**创建用户代码*/
	private String createBy;
	/**创建时间*/
	private java.util.Date createTime;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对账类型
	 */
	@Column(name ="CHECK_TYPE")
	public String getCheckType(){
		return this.checkType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对账类型
	 */
	public void setCheckType(String checkType){
		this.checkType = checkType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水号
	 */
	@Column(name ="ORDER_NO")
	public String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水号
	 */
	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记账日期
	 */
	@Column(name ="TALLY_DATE")
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
	 *@return: java.lang.String  企业账号
	 */
	@Column(name ="COMP_ACC")
	public String getCompAcc(){
		return this.compAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  企业账号
	 */
	public void setCompAcc(String compAcc){
		this.compAcc = compAcc;
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  行号
	 */
	@Column(name ="LINE_NO")
	public java.math.BigDecimal getLineNo(){
		return this.lineNo;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  行号
	 */
	public void setLineNo(java.math.BigDecimal  lineNo){
		this.lineNo = lineNo;
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
	 *@return: java.util.Date  起息日期
	 */
	@Column(name ="INTEREST_START_DATE")
	public java.util.Date getInterestStartDate(){
		return this.interestStartDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  起息日期
	 */
	public void setInterestStartDate(java.util.Date interestStartDate){
		this.interestStartDate = interestStartDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  调节日期
	 */
	@Column(name ="ADJUST_DATE")
	public java.util.Date getAdjustDate(){
		return this.adjustDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  调节日期
	 */
	public void setAdjustDate(java.util.Date adjustDate){
		this.adjustDate = adjustDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  输入日期
	 */
	@Column(name ="IMPORT_DATE")
	public java.util.Date getImportDate(){
		return this.importDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  输入日期
	 */
	public void setImportDate(java.util.Date importDate){
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数据源
	 */
	@Column(name ="DATA_SOURCE")
	public String getDataSource(){
		return this.dataSource;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数据源
	 */
	public void setDataSource(String dataSource){
		this.dataSource = dataSource;
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
	public java.util.Date getCheckDate(){
		return this.checkDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  对账时间
	 */
	public void setCheckDate(java.util.Date checkDate){
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
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
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
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
}
