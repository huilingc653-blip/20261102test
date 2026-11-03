package com.bytter.check.vo;

import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

/**   
 * @Title: Entity
 * @Description: FC_BANK_BILL_TEMP
 * @author onlineGenerator
 * @date 2017-06-07 18:00:04
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@ExcelTarget("FcBankBillTempVo")
public class FcBankBillTempVo extends ExcelModel implements java.io.Serializable,IBaseDataInject {
	private String id;
	
	
	
	/**银行账号*/
	@Excel(name = "*银行账号")
	private String bankAcc;
	/**记账日期*发生日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@Excel(name = "*发生日期",importFormat = "yyyy/MM/dd")
	private java.util.Date tallyDate;
	/**借方发生额*/
	@Excel(name = "*借方金额")
	private java.math.BigDecimal debitMoney;
	/**贷方发生额*/
	@Excel(name = "*贷方金额")
	private java.math.BigDecimal creditMoney;
	/**红蓝标志*/
	@Excel(name = "红蓝标志")
	private String redblueSign;
	private String redblueSignValue;
	
	/**摘要*/
	@Excel(name = "摘要")
	private String summaryContent;
	


	 /**凭证号*/
	private String voucherNo;
	/**凭证流水号*/
	private String voucherOrderNo;
	/**账号*/
	@Excel(name = "*内部账号/erp企业科目")
	private String account;
	/**币别代码*/
	private String curCode;
	/**摘要*/
	@Excel(name = "币别")
	private String curName;
	/**余额*/
	private java.math.BigDecimal balance;
	/**对账标志*/
	private String compareSign;
	/**序号*/
	private String bankId;
	/**票据号*/
	private String billNo;
	/**调节日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date adjustDate;
	/**输入日期*/
	private java.util.Date importDate;
	/**输入类型*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String importType;
	/**行号*/
	private Integer lineNo;
	/**ERP凭证号*/
	private String erpVoucherNo;
	/**收款账号*/
	private String payeeAcc;
	/**导入用户*/
	private String importUser;
	/**对账时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
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
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTime;

	@Override
	public String getBeanName() {
		return "fcBankBillTempService";
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证号
	 */
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRedblueSignValue() {
		return redblueSignValue;
	}

	public void setRedblueSignValue(String redblueSignValue) {
		this.redblueSignValue = redblueSignValue;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证流水号
	 */
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
	 *@return: java.lang.String  账号
	 */
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
	 *@return: java.lang.String  银行账号
	 */
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
	 *@return: java.lang.String  红蓝标志
	 */
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

	public String getCurName() {
		return curName;
	}

	public void setCurName(String curName) {
		this.curName = curName;
	}
	
	
}
