package com.bytter.biz.expense.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 费用报销表
 * @author onlineGenerator
 * @date 2017-05-31 16:04:01
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_EXPENSE_DETAIL", schema = "")
@SuppressWarnings("serial")
public class NisExpenseDetail extends IdEntity implements java.io.Serializable {
	/**报销编号*/
	private java.lang.String expenseId;
	/**单位*/
	private java.lang.String corpId;
	/**报账日期*/
	private java.util.Date expenseDate;
	/**职工编号*/
	private java.lang.String workId;
	/**职工姓名*/
	private java.lang.String userName;
	/**部门名称*/
	private java.lang.String deptName;
	/**操作类型*/
	private java.lang.String opType;
	/**业务类型*/
	private java.lang.String busType;
	/**接口代码*/
	private java.lang.String bifCode;
	/**跨行标识*/
	private java.lang.String difBank;
	/**地域标识*/
	private java.lang.String areaSign;
	/**报账金额*/
	private java.math.BigDecimal expenseMoney;
	/**开户行行号*/
	private java.lang.String bankCode;
	/**开户银行名称*/
	private java.lang.String bankName;
	/**银行卡类型*/
	private java.lang.String cardType;
	/**银行账号*/
	private java.lang.String bankAcc;
	/**账户名*/
	private java.lang.String accName;
	/**所在省*/
	private java.lang.String prov;
	/**所在市*/
	private java.lang.String city;
	/**用途*/
	private java.lang.String purpose;
	/**说明*/
	private java.lang.String rmk;
	/**付款单号*/
	private java.lang.String billId;
	/**交易状态*/
	private java.lang.String voucherStat;
	/**返回信息（交易前）*/
	private java.lang.String firstReturnMsg;
	/**返回信息（交易后）*/
	private java.lang.String nextReturnMsg;
	/**返回时间*/
	private java.util.Date returnTime;
	/**单据来源*/
	private java.lang.String billFrom;
	/**ERP编号*/
	private java.lang.String erpId;
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
	/**附件ID*/
	private java.lang.String attachmentId;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  报销编号
	 */
	@Column(name ="EXPENSE_ID",nullable=false,length=12)
	public java.lang.String getExpenseId(){
		return this.expenseId;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  报销编号
	 */
	public void setExpenseId(java.lang.String expenseId){
		this.expenseId = expenseId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位
	 */
	@Column(name ="CORP_ID",nullable=true,length=32)
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  报账日期
	 */
	@Column(name ="EXPENSE_DATE",nullable=true)
	public java.util.Date getExpenseDate(){
		return this.expenseDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  报账日期
	 */
	public void setExpenseDate(java.util.Date expenseDate){
		this.expenseDate = expenseDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  职工编号
	 */
	@Column(name ="WORK_ID",nullable=true,length=32)
	public java.lang.String getWorkId(){
		return this.workId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  职工编号
	 */
	public void setWorkId(java.lang.String workId){
		this.workId = workId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  职工姓名
	 */
	@Column(name ="USER_NAME",nullable=true,length=40)
	public java.lang.String getUserName(){
		return this.userName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  职工姓名
	 */
	public void setUserName(java.lang.String userName){
		this.userName = userName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  部门名称
	 */
	@Column(name ="DEPT_NAME",nullable=true,length=30)
	public java.lang.String getDeptName(){
		return this.deptName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  部门名称
	 */
	public void setDeptName(java.lang.String deptName){
		this.deptName = deptName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作类型
	 */
	@Column(name ="OP_TYPE",nullable=true,length=25)
	public java.lang.String getOpType(){
		return this.opType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作类型
	 */
	public void setOpType(java.lang.String opType){
		this.opType = opType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型
	 */
	@Column(name ="BUS_TYPE",nullable=true,length=25)
	public java.lang.String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型
	 */
	public void setBusType(java.lang.String busType){
		this.busType = busType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口代码
	 */
	@Column(name ="BIF_CODE",nullable=true,length=4)
	public java.lang.String getBifCode(){
		return this.bifCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口代码
	 */
	public void setBifCode(java.lang.String bifCode){
		this.bifCode = bifCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  跨行标识
	 */
	@Column(name ="DIF_BANK",nullable=true,length=1)
	public java.lang.String getDifBank(){
		return this.difBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  跨行标识
	 */
	public void setDifBank(java.lang.String difBank){
		this.difBank = difBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  地域标识
	 */
	@Column(name ="AREA_SIGN",nullable=true,length=1)
	public java.lang.String getAreaSign(){
		return this.areaSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  地域标识
	 */
	public void setAreaSign(java.lang.String areaSign){
		this.areaSign = areaSign;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  报账金额
	 */
	@Column(name ="EXPENSE_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getExpenseMoney(){
		return this.expenseMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  报账金额
	 */
	public void setExpenseMoney(java.math.BigDecimal expenseMoney){
		this.expenseMoney = expenseMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开户行行号
	 */
	@Column(name ="BANK_CODE",nullable=true,length=20)
	public java.lang.String getBankCode(){
		return this.bankCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开户行行号
	 */
	public void setBankCode(java.lang.String bankCode){
		this.bankCode = bankCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开户银行名称
	 */
	@Column(name ="BANK_NAME",nullable=true,length=150)
	public java.lang.String getBankName(){
		return this.bankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开户银行名称
	 */
	public void setBankName(java.lang.String bankName){
		this.bankName = bankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行卡类型
	 */
	@Column(name ="CARD_TYPE",nullable=true,length=1)
	public java.lang.String getCardType(){
		return this.cardType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行卡类型
	 */
	public void setCardType(java.lang.String cardType){
		this.cardType = cardType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行账号
	 */
	@Column(name ="BANK_ACC",nullable=true,length=50)
	public java.lang.String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行账号
	 */
	public void setBankAcc(java.lang.String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账户名
	 */
	@Column(name ="ACC_NAME",nullable=true,length=150)
	public java.lang.String getAccName(){
		return this.accName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户名
	 */
	public void setAccName(java.lang.String accName){
		this.accName = accName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所在省
	 */
	@Column(name ="PROV",nullable=true,length=32)
	public java.lang.String getProv(){
		return this.prov;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所在省
	 */
	public void setProv(java.lang.String prov){
		this.prov = prov;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所在市
	 */
	@Column(name ="CITY",nullable=true,length=32)
	public java.lang.String getCity(){
		return this.city;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所在市
	 */
	public void setCity(java.lang.String city){
		this.city = city;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用途
	 */
	@Column(name ="PURPOSE",nullable=true,length=120)
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
	 *@return: java.lang.String  说明
	 */
	@Column(name ="RMK",nullable=true,length=100)
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  说明
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款单号
	 */
	@Column(name ="BILL_ID",nullable=true,length=32)
	public java.lang.String getBillId(){
		return this.billId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单号
	 */
	public void setBillId(java.lang.String billId){
		this.billId = billId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  交易状态
	 */
	@Column(name ="VOUCHER_STAT",nullable=true,length=1)
	public java.lang.String getVoucherStat(){
		return this.voucherStat;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  交易状态
	 */
	public void setVoucherStat(java.lang.String voucherStat){
		this.voucherStat = voucherStat;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  返回信息（交易前）
	 */
	@Column(name ="FIRST_RETURN_MSG",nullable=true,length=2000)
	public java.lang.String getFirstReturnMsg(){
		return this.firstReturnMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  返回信息（交易前）
	 */
	public void setFirstReturnMsg(java.lang.String firstReturnMsg){
		this.firstReturnMsg = firstReturnMsg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  返回信息（交易后）
	 */
	@Column(name ="NEXT_RETURN_MSG",nullable=true,length=2000)
	public java.lang.String getNextReturnMsg(){
		return this.nextReturnMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  返回信息（交易后）
	 */
	public void setNextReturnMsg(java.lang.String nextReturnMsg){
		this.nextReturnMsg = nextReturnMsg;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  返回时间
	 */
	@Column(name ="RETURN_TIME",nullable=true)
	public java.util.Date getReturnTime(){
		return this.returnTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  返回时间
	 */
	public void setReturnTime(java.util.Date returnTime){
		this.returnTime = returnTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单据来源
	 */
	@Column(name ="BILL_FROM",nullable=true,length=10)
	public java.lang.String getBillFrom(){
		return this.billFrom;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单据来源
	 */
	public void setBillFrom(java.lang.String billFrom){
		this.billFrom = billFrom;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  ERP编号
	 */
	@Column(name ="ERP_ID",nullable=true,length=32)
	public java.lang.String getErpId(){
		return this.erpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ERP编号
	 */
	public void setErpId(java.lang.String erpId){
		this.erpId = erpId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
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
	 *@return: java.lang.String  附件ID
	 */
	@Column(name ="ATTACHMENT_ID",nullable=true,length=32)
	public java.lang.String getAttachmentId() {
		return attachmentId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附件ID
	 */
	public void setAttachmentId(java.lang.String attachmentId) {
		this.attachmentId = attachmentId;
	}
}
