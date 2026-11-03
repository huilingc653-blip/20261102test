package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 银行账号明细录入表
 * @author onlineGenerator
 * @date 2017-06-29 13:51:01
 * @version 1.0
 *
 */
@Entity
@Table(name = "BT_BANK_ACC_DTL", schema = "")
@SuppressWarnings("serial")
public class BtBankAccDtl extends IdEntity implements java.io.Serializable {
	/**业务单号*/
	private java.lang.String accNo;
	/**单位ID*/
	private java.lang.String corpId;
	/**本方账号*/
	private java.lang.String bankAcc;
	/**本方账户名*/
	private java.lang.String accName;
	/**本方账户开户行*/
	private java.lang.String bankName;
	/**接口代码*/
	private java.lang.String bifCode;
	/**对方账号*/
	private java.lang.String oppAccNo;
	/**对方账户名*/
	private java.lang.String oppAccName;
	/**对方账户开户行*/
	private java.lang.String oppAccBank;
	/**借贷标志 0-收 1-支*/
	private java.lang.String cdSign;
	/**红蓝标志 1-红 2-蓝*/
	private java.lang.String rbSign;
	/**交易金额*/
	private java.math.BigDecimal amt;
	/**余额*/
	private java.math.BigDecimal bal;
	/**币别ID*/
	private java.lang.String curId;
	/**用途*/
	private java.lang.String uses;
	/**附言*/
	private java.lang.String postscript;
	/**摘要*/
	private java.lang.String remark;
	/**交易时间*/
	private java.util.Date transTime;
	/**顺序号*/
	private java.lang.Integer orderNo;
	/**月份*/
	private java.lang.Integer months;
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
	/**项目信息*/
	private String projectRemark;
	/**
	 * 银行流水号
	 */
	private java.lang.String bankSerialNo;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务单号
	 */
	@Column(name ="ACC_NO",nullable=true,length=40)
	public java.lang.String getAccNo(){
		return this.accNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务单号
	 */
	public void setAccNo(java.lang.String accNo){
		this.accNo = accNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=true,length=32)
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
	 *@return: java.lang.String  本方账号
	 */
	@Column(name ="BANK_ACC",nullable=true,length=60)
	public java.lang.String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  本方账号
	 */
	public void setBankAcc(java.lang.String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  本方账户名
	 */
	@Column(name ="ACC_NAME",nullable=true,length=200)
	public java.lang.String getAccName(){
		return this.accName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  本方账户名
	 */
	public void setAccName(java.lang.String accName){
		this.accName = accName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  本方账户开户行
	 */
	@Column(name ="BANK_NAME",nullable=true,length=200)
	public java.lang.String getBankName(){
		return this.bankName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  本方账户开户行
	 */
	public void setBankName(java.lang.String bankName){
		this.bankName = bankName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口代码
	 */
	@Column(name ="BIF_CODE",nullable=true,length=32)
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
	 *@return: java.lang.String  对方账号
	 */
	@Column(name ="OPP_ACC_NO",nullable=true,length=60)
	public java.lang.String getOppAccNo(){
		return this.oppAccNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对方账号
	 */
	public void setOppAccNo(java.lang.String oppAccNo){
		this.oppAccNo = oppAccNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对方账户名
	 */
	@Column(name ="OPP_ACC_NAME",nullable=true,length=200)
	public java.lang.String getOppAccName(){
		return this.oppAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对方账户名
	 */
	public void setOppAccName(java.lang.String oppAccName){
		this.oppAccName = oppAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对方账户开户行
	 */
	@Column(name ="OPP_ACC_BANK",nullable=true,length=200)
	public java.lang.String getOppAccBank(){
		return this.oppAccBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对方账户开户行
	 */
	public void setOppAccBank(java.lang.String oppAccBank){
		this.oppAccBank = oppAccBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借贷标志 0-收 1-支
	 */
	@Column(name ="CD_SIGN",nullable=true,length=1)
	public java.lang.String getCdSign(){
		return this.cdSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借贷标志 0-收 1-支
	 */
	public void setCdSign(java.lang.String cdSign){
		this.cdSign = cdSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  红蓝标志 1-红 2-蓝
	 */
	@Column(name ="RB_SIGN",nullable=true,length=1)
	public java.lang.String getRbSign(){
		return this.rbSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  红蓝标志 1-红 2-蓝
	 */
	public void setRbSign(java.lang.String rbSign){
		this.rbSign = rbSign;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  交易金额
	 */
	@Column(name ="AMT",nullable=true,scale=2,length=19)
	public java.math.BigDecimal getAmt(){
		return this.amt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  交易金额
	 */
	public void setAmt(java.math.BigDecimal amt){
		this.amt = amt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  余额
	 */
	@Column(name ="BAL",nullable=true,scale=2,length=19)
	public java.math.BigDecimal getBal(){
		return this.bal;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  余额
	 */
	public void setBal(java.math.BigDecimal bal){
		this.bal = bal;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别ID
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
	public java.lang.String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别ID
	 */
	public void setCurId(java.lang.String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用途
	 */
	@Column(name ="USES",nullable=true,length=150)
	public java.lang.String getUses(){
		return this.uses;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用途
	 */
	public void setUses(java.lang.String uses){
		this.uses = uses;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附言
	 */
	@Column(name ="POSTSCRIPT",nullable=true,length=150)
	public java.lang.String getPostscript(){
		return this.postscript;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附言
	 */
	public void setPostscript(java.lang.String postscript){
		this.postscript = postscript;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="REMARK",nullable=true,length=80)
	public java.lang.String getRemark(){
		return this.remark;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要
	 */
	public void setRemark(java.lang.String remark){
		this.remark = remark;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  交易时间
	 */
	@Column(name ="TRANS_TIME",nullable=true)
	public java.util.Date getTransTime(){
		return this.transTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  交易时间
	 */
	public void setTransTime(java.util.Date transTime){
		this.transTime = transTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  月份
	 */
	@Column(name ="ORDER_NO",length=10)
	public java.lang.Integer getOrderNo() {
		return orderNo;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@param: java.lang.Integer  顺序号
	 */
	public void setOrderNo(java.lang.Integer orderNo) {
		this.orderNo = orderNo;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  顺序号
	 */
	@Column(name ="MONTHS",nullable=true,length=10)
	public java.lang.Integer getMonths(){
		return this.months;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  月份
	 */
	public void setMonths(java.lang.Integer months){
		this.months = months;
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
	@Column(name ="PROJECT_REMARK",nullable=true)
	public String getProjectRemark() {
		return projectRemark;
	}
	public void setProjectRemark(String projectRemark) {
		this.projectRemark = projectRemark;
	}
	@Column(name = "BANK_SERIAL_NO", nullable = true, length = 60)
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
}
