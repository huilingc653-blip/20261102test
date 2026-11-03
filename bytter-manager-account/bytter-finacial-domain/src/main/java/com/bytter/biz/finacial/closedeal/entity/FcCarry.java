package com.bytter.biz.finacial.closedeal.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 结转记录表
 * @author onlineGenerator
 * @date 2017-06-27 09:55:51
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_CARRY", schema = "")
@SuppressWarnings("serial")
public class FcCarry extends IdEntity implements java.io.Serializable {
	/**结转类型*/
	private java.lang.String carryType;
	/**所属网点*/
	private java.lang.String netId;
	/**结转日期*/
	private java.util.Date carryTime;
	/**结转人代码*/
	private java.lang.String operatorCode;
	/**凭证起始日期*/
	private java.util.Date voucherStartDate;
	/**凭证终止日期*/
	private java.util.Date voucherEndDate;
	/**结转帐号*/
	private java.lang.String carryAcc;
	/**反结转日期*/
	private java.util.Date restoreCarryTime;
	/**反结转人代码*/
	private java.lang.String restoreCarryOptor;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/** 用户名 */
	private java.lang.String username;
	@Column(name = "USER_NAME")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结转类型
	 */
	@Column(name ="CARRY_TYPE")
	public java.lang.String getCarryType(){
		return this.carryType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结转类型
	 */
	public void setCarryType(java.lang.String carryType){
		this.carryType = carryType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属网点
	 */
	@Column(name ="NET_ID")
	public java.lang.String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属网点
	 */
	public void setNetId(java.lang.String netId){
		this.netId = netId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结转日期
	 */
	@Column(name ="CARRY_TIME")
	public java.util.Date getCarryTime(){
		return this.carryTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结转日期
	 */
	public void setCarryTime(java.util.Date carryTime){
		this.carryTime = carryTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结转人代码
	 */
	@Column(name ="OPERATOR_CODE")
	public java.lang.String getOperatorCode(){
		return this.operatorCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结转人代码
	 */
	public void setOperatorCode(java.lang.String operatorCode){
		this.operatorCode = operatorCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  凭证起始日期
	 */
	@Column(name ="VOUCHER_START_DATE")
	public java.util.Date getVoucherStartDate(){
		return this.voucherStartDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  凭证起始日期
	 */
	public void setVoucherStartDate(java.util.Date voucherStartDate){
		this.voucherStartDate = voucherStartDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  凭证终止日期
	 */
	@Column(name ="VOUCHER_END_DATE")
	public java.util.Date getVoucherEndDate(){
		return this.voucherEndDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  凭证终止日期
	 */
	public void setVoucherEndDate(java.util.Date voucherEndDate){
		this.voucherEndDate = voucherEndDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结转帐号
	 */
	@Column(name ="CARRY_ACC")
	public java.lang.String getCarryAcc(){
		return this.carryAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结转帐号
	 */
	public void setCarryAcc(java.lang.String carryAcc){
		this.carryAcc = carryAcc;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  反结转日期
	 */
	@Column(name ="RESTORE_CARRY_TIME")
	public java.util.Date getRestoreCarryTime(){
		return this.restoreCarryTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  反结转日期
	 */
	public void setRestoreCarryTime(java.util.Date restoreCarryTime){
		this.restoreCarryTime = restoreCarryTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  反结转人代码
	 */
	@Column(name ="RESTORE_CARRY_OPTOR")
	public java.lang.String getRestoreCarryOptor(){
		return this.restoreCarryOptor;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  反结转人代码
	 */
	public void setRestoreCarryOptor(java.lang.String restoreCarryOptor){
		this.restoreCarryOptor = restoreCarryOptor;
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
