package com.bytter.biz.finacial.interest.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 不足款清单
 * @author onlineGenerator
 * @date 2017-07-10 11:51:48
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_LESS_PAY_BILL", schema = "")
@SuppressWarnings("serial")
public class FcLessPayBill extends IdEntity implements java.io.Serializable {
	/**账号*/
	private java.lang.String account;
	/**应收利息账号*/
	private java.lang.String needinterestAcc;
	/**币别代码*/
	private java.lang.String curCode;
	/**利息*/
	private java.math.BigDecimal interest;
	/**余额*/
	private java.math.BigDecimal balance;
	/**状态*/
	private java.lang.Integer status;
	/**是否已还*/
	private java.lang.String isRepay;
	/**录入日期*/
	private java.util.Date enterDate;
	/**操作人*/
	private java.lang.String operatorCode;
	/**复核人*/
	private java.lang.String checkerCode;
	/**复核日期*/
	private java.util.Date checkDate;
	/**截止日期*/
	private java.util.Date endInstDate;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**
	 * 新增冗余字段 ACC_NAME   ACC_FOR_DEDUCT
	 */
	private String accName;
	private String accForDeduct;
	@Column(name = "ACC_NAME")
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Column(name = "ACC_FOR_DEDUCT")
	public String getAccForDeduct() {
		return accForDeduct;
	}
	public void setAccForDeduct(String accForDeduct) {
		this.accForDeduct = accForDeduct;
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
	 *@return: java.lang.String  应收利息账号
	 */
	@Column(name ="NEEDINTEREST_ACC")
	public java.lang.String getNeedinterestAcc() {
		return needinterestAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  应收利息账号
	 */
	public void setNeedinterestAcc(java.lang.String needinterestAcc) {
		this.needinterestAcc = needinterestAcc;
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利息
	 */
	@Column(name ="INTEREST")
	public java.math.BigDecimal getInterest(){
		return this.interest;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利息
	 */
	public void setInterest(java.math.BigDecimal interest){
		this.interest = interest;
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS")
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
	
	@Column(name ="IS_REPAY")
	public java.lang.String getIsRepay() {
		return isRepay;
	}
	
	public void setIsRepay(java.lang.String isRepay) {
		this.isRepay = isRepay;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  录入日期
	 */
	@Column(name ="ENTER_DATE")
	public java.util.Date getEnterDate(){
		return this.enterDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  录入日期
	 */
	public void setEnterDate(java.util.Date enterDate){
		this.enterDate = enterDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作人
	 */
	@Column(name ="OPERATOR_CODE")
	public java.lang.String getOperatorCode(){
		return this.operatorCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作人
	 */
	public void setOperatorCode(java.lang.String operatorCode){
		this.operatorCode = operatorCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复核人
	 */
	@Column(name ="CHECKER_CODE")
	public java.lang.String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核人
	 */
	public void setCheckerCode(java.lang.String checkerCode){
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复核日期
	 */
	@Column(name ="CHECK_DATE")
	public java.util.Date getCheckDate(){
		return this.checkDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  复核日期
	 */
	public void setCheckDate(java.util.Date checkDate){
		this.checkDate = checkDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  截止日期
	 */
	@Column(name ="END_INST_DATE")
	public java.util.Date getEndInstDate(){
		return this.endInstDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  截止日期
	 */
	public void setEndInstDate(java.util.Date endInstDate){
		this.endInstDate = endInstDate;
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
	 *@return: java.lang.String  创建用户代码
	 */
	@Column(name ="CREATE_BY")
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
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
	 *@return: java.lang.String  最后更新用户代码
	 */
	@Column(name ="UPDATE_BY")
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
}
