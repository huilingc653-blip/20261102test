package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 对账余额调节表
 * @author onlineGenerator
 * @date 2017-06-02 15:29:08
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_BANK_BALANCE_ADJUST", schema = "")
@SuppressWarnings("serial")
public class FcBankBalanceAdjust extends IdEntity implements java.io.Serializable {
	/**银行账面余额*/
	private java.math.BigDecimal bankInitBalance;
	/**企业账面余额*/
	private java.math.BigDecimal blnInitBalance;
	/**银行调节余额*/
	private java.math.BigDecimal bankAdjBal;
	/**企业调节余额*/
	private java.math.BigDecimal blnAdjBalance;
	/**结转日期*/
	private java.util.Date adjustDate;
	/**开始日期*/
	private java.util.Date beginDate;
	/**结束日期*/
	private java.util.Date endDate;
	/**结转用户*/
	private String adjCode;
	/**状态*/
	private Integer status;
	/**反结转时间*/
	private java.util.Date readjDate;
	/**反结转用户*/
	private String readjUser;
	/**最后更新用户代码*/
	private String updateBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**创建用户代码*/
	private String createBy;
	/**创建时间*/
	private java.util.Date createTime;
	/**企业账号*/
	private String account;
	/**币别代码*/
	private String curCode;
	/**银行账号*/
	private String bankAcc;
	/**对账类型*/
	private String checkType;
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  银行账面余额
	 */
	@Column(name ="BANK_INIT_BALANCE")
	public java.math.BigDecimal getBankInitBalance(){
		return this.bankInitBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  银行账面余额
	 */
	public void setBankInitBalance(java.math.BigDecimal bankInitBalance){
		this.bankInitBalance = bankInitBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  企业账面余额
	 */
	@Column(name ="BLN_INIT_BALANCE")
	public java.math.BigDecimal getBlnInitBalance(){
		return this.blnInitBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  企业账面余额
	 */
	public void setBlnInitBalance(java.math.BigDecimal blnInitBalance){
		this.blnInitBalance = blnInitBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  银行调节余额
	 */
	@Column(name ="BANK_ADJ_BAL")
	public java.math.BigDecimal getBankAdjBal(){
		return this.bankAdjBal;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  银行调节余额
	 */
	public void setBankAdjBal(java.math.BigDecimal bankAdjBal){
		this.bankAdjBal = bankAdjBal;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  企业调节余额
	 */
	@Column(name ="BLN_ADJ_BALANCE")
	public java.math.BigDecimal getBlnAdjBalance(){
		return this.blnAdjBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  企业调节余额
	 */
	public void setBlnAdjBalance(java.math.BigDecimal blnAdjBalance){
		this.blnAdjBalance = blnAdjBalance;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结转日期
	 */
	@Column(name ="ADJUST_DATE")
	public java.util.Date getAdjustDate(){
		return this.adjustDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结转日期
	 */
	public void setAdjustDate(java.util.Date adjustDate){
		this.adjustDate = adjustDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  开始日期
	 */
	@Column(name ="BEGIN_DATE")
	public java.util.Date getBeginDate(){
		return this.beginDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  开始日期
	 */
	public void setBeginDate(java.util.Date beginDate){
		this.beginDate = beginDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结束日期
	 */
	@Column(name ="END_DATE")
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结转用户
	 */
	@Column(name ="ADJ_CODE")
	public String getAdjCode(){
		return this.adjCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结转用户
	 */
	public void setAdjCode(String adjCode){
		this.adjCode = adjCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS")
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  反结转时间
	 */
	@Column(name ="READJ_DATE")
	public java.util.Date getReadjDate(){
		return this.readjDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  反结转时间
	 */
	public void setReadjDate(java.util.Date readjDate){
		this.readjDate = readjDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  反结转用户
	 */
	@Column(name ="READJ_USER")
	public String getReadjUser(){
		return this.readjUser;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  反结转用户
	 */
	public void setReadjUser(String readjUser){
		this.readjUser = readjUser;
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  企业账号
	 */
	@Column(name ="ACCOUNT")
	public String getAccount(){
		return this.account;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  企业账号
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
}
