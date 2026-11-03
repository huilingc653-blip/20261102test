package com.bytter.cashVoucher.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 凭证打印操作
 * @author onlineGenerator
 * @date 2017-06-13 11:18:30
 * @version 1.0
 *
 */
@Entity
@Table(name = "BV_PRINT_VOU_OPINFO", schema = "")
@SuppressWarnings("serial")
public class BvPrintVouOpinfo extends IdEntity implements java.io.Serializable {
	/**凭证种类*/
	private String vouTid;
	/**占用凭证号ID*/
	private String voudtlId;
	/**占用凭证号*/
	private String vouNo;
	/**dataId*/
	private String dataId;
	/**凭证金额*/
	private java.math.BigDecimal vouMoney;
	/**printSize*/
	private Integer printSize;
	/**备注*/
	private String rmk;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证种类
	 */
	@Column(name ="VOU_TID",nullable=true,length=32)
	public String getVouTid(){
		return this.vouTid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证种类
	 */
	public void setVouTid(String vouTid){
		this.vouTid = vouTid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  占用凭证号ID
	 */
	@Column(name ="VOUDTL_ID",nullable=true,length=32)
	public String getVoudtlId(){
		return this.voudtlId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  占用凭证号ID
	 */
	public void setVoudtlId(String voudtlId){
		this.voudtlId = voudtlId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  占用凭证号
	 */
	@Column(name ="VOU_NO",nullable=true,length=32)
	public String getVouNo(){
		return this.vouNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  占用凭证号
	 */
	public void setVouNo(String vouNo){
		this.vouNo = vouNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  dataId
	 */
	@Column(name ="DATA_ID",nullable=true,length=32)
	public String getDataId(){
		return this.dataId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  dataId
	 */
	public void setDataId(String dataId){
		this.dataId = dataId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  凭证金额
	 */
	@Column(name ="VOU_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getVouMoney(){
		return this.vouMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  凭证金额
	 */
	public void setVouMoney(java.math.BigDecimal vouMoney){
		this.vouMoney = vouMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  printSize
	 */
	@Column(name ="PRINT_SIZE",nullable=true,length=10)
	public Integer getPrintSize(){
		return this.printSize;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  printSize
	 */
	public void setPrintSize(Integer printSize){
		this.printSize = printSize;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=100)
	public String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public String getCreateName(){
		return this.createName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
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
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public String getUpdateName(){
		return this.updateName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
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
