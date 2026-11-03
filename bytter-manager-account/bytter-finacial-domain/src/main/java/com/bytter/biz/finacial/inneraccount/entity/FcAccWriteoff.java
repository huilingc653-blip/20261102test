package com.bytter.biz.finacial.inneraccount.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_ACC_WRITEOFF
 * @author onlineGenerator
 * @date 2017-06-12 10:08:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_ACC_WRITEOFF", schema = "")
@SuppressWarnings("serial")
public class FcAccWriteoff extends IdEntity implements java.io.Serializable {
	/**writeoffAcc*/
	private java.lang.String writeoffAcc;
	/**transactor*/
	private java.lang.String transactor;
	/**writeoffTime*/
	private java.util.Date writeoffTime;
	/**checkerCode*/
	private java.lang.String checkerCode;
	/** 记账全局唯一id*/
	private String busGlobalId;
	/** 转入账号 */
	private String transferAcc;
	/**checkTime*/
	private java.util.Date checkTime;
	/**rmk*/
	private java.lang.String rmk;
	private java.util.Date tallyDate;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	@Column(name ="TRANSFER_ACC",nullable=true,length=50)
	public String getTransferAcc() {
		return transferAcc;
	}
	public void setTransferAcc(String transferAcc) {
		this.transferAcc = transferAcc;
	}
	@Column(name ="BUS_GLOBAL_ID",nullable=true,length=50)
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	@Column(name = "TALLY_DATE",nullable=true )
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  writeoffAcc
	 */
	@Column(name ="WRITEOFF_ACC",nullable=false,length=50)
	public java.lang.String getWriteoffAcc(){
		return this.writeoffAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  writeoffAcc
	 */
	public void setWriteoffAcc(java.lang.String writeoffAcc){
		this.writeoffAcc = writeoffAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  transactor
	 */
	@Column(name ="TRANSACTOR",nullable=false,length=20)
	public java.lang.String getTransactor(){
		return this.transactor;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  transactor
	 */
	public void setTransactor(java.lang.String transactor){
		this.transactor = transactor;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  writeoffTime
	 */
	@Column(name ="WRITEOFF_TIME",nullable=false)
	public java.util.Date getWriteoffTime(){
		return this.writeoffTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  writeoffTime
	 */
	public void setWriteoffTime(java.util.Date writeoffTime){
		this.writeoffTime = writeoffTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  checkerCode
	 */
	@Column(name ="CHECKER_CODE",nullable=true,length=20)
	public java.lang.String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  checkerCode
	 */
	public void setCheckerCode(java.lang.String checkerCode){
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  checkTime
	 */
	@Column(name ="CHECK_TIME",nullable=true)
	public java.util.Date getCheckTime(){
		return this.checkTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  checkTime
	 */
	public void setCheckTime(java.util.Date checkTime){
		this.checkTime = checkTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  rmk
	 */
	@Column(name ="RMK",nullable=true,length=80)
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  rmk
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=true)
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
	@Column(name ="CREATE_BY",nullable=true,length=32)
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
	@Column(name ="UPDATE_TIME",nullable=true)
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
	@Column(name ="UPDATE_BY",nullable=true,length=32)
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
