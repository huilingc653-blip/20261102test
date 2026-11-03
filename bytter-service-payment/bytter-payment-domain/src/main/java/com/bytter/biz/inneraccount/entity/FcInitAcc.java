package com.bytter.biz.inneraccount.entity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
//import com.bytter.system.api.dto.FcAcc;
/**   
 * @Title: Entity
 * @Description: 内部账号初始化
 * @author onlineGenerator
 * @date 2017-06-12 18:12:02
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_INIT_ACC", schema = "")
@SuppressWarnings("serial")
public class FcInitAcc extends IdEntity implements java.io.Serializable {
	/**账号*/
	private java.lang.String account;
	/**余额*/
	private java.math.BigDecimal balance;
	/**积数*/
	private java.math.BigDecimal cumulation;
	/**初始化日期*/
	private java.util.Date initTime;
	/**复核员代码*/
	private java.lang.String checkerCode;
	/**复核日期*/
	private java.util.Date checkTime;
	/**年余额*/
	private java.math.BigDecimal yearBalance;
	/**年合计*/
	private java.math.BigDecimal yearTotal;
	/**状态*/
	private java.lang.Integer status;
	/**备注*/
	private java.lang.String rmk;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
//TODO 这里和FcAcc_init 关联，待解决
//	private FcAcc fcAcc;
//
//	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name = "ACCOUNT",referencedColumnName = "blnctr_Acc", insertable = false,updatable = false)
//	public FcAcc getFcAcc() {
//		return fcAcc;
//	}
//
//	public void setFcAcc(FcAcc fcAcc) {
//		this.fcAcc = fcAcc;
//	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="ACCOUNT",nullable=false,length=50)
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  余额
	 */
	@Column(name ="BALANCE",nullable=true,scale=2,length=15)
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  积数
	 */
	@Column(name ="CUMULATION",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getCumulation(){
		return this.cumulation;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  积数
	 */
	public void setCumulation(java.math.BigDecimal cumulation){
		this.cumulation = cumulation;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  初始化日期
	 */
	@Column(name ="INIT_TIME",nullable=false)
	public java.util.Date getInitTime(){
		return this.initTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  初始化日期
	 */
	public void setInitTime(java.util.Date initTime){
		this.initTime = initTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复核员代码
	 */
	@Column(name ="CHECKER_CODE",nullable=true,length=20)
	public java.lang.String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核员代码
	 */
	public void setCheckerCode(java.lang.String checkerCode){
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复核日期
	 */
	@Column(name ="CHECK_TIME",nullable=true)
	public java.util.Date getCheckTime(){
		return this.checkTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  复核日期
	 */
	public void setCheckTime(java.util.Date checkTime){
		this.checkTime = checkTime;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  年余额
	 */
	@Column(name ="YEAR_BALANCE",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getYearBalance(){
		return this.yearBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  年余额
	 */
	public void setYearBalance(java.math.BigDecimal yearBalance){
		this.yearBalance = yearBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  年合计
	 */
	@Column(name ="YEAR_TOTAL",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getYearTotal(){
		return this.yearTotal;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  年合计
	 */
	public void setYearTotal(java.math.BigDecimal yearTotal){
		this.yearTotal = yearTotal;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=2)
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
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=40)
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=32)
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
	@Column(name ="UPDATE_BY",nullable=true,length=32)
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
}
