package com.bytter.biz.netbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 付款方式分配表
 * @author onlineGenerator
 * @date 2017-05-17 10:08:00
 * @version 1.0
 *
 */
@Entity
@Table(name = "BT_ASSIGN_PAY_WAY", schema = "")
@SuppressWarnings("serial")
public class BtAssignPayWay extends IdEntity implements java.io.Serializable {
	/**单位ID*/
	private java.lang.String corpId;
	/**付款方式*/
	private java.lang.String voucherCode;
	/**默认付款方式*/
	private java.lang.String defVoucherCode;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
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
	 *@return: java.lang.String  付款方式
	 */
	@Column(name ="VOUCHER_CODE",nullable=false,length=32)
	public java.lang.String getVoucherCode(){
		return this.voucherCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款方式
	 */
	public void setVoucherCode(java.lang.String voucherCode){
		this.voucherCode = voucherCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  默认付款方式
	 */
	@Column(name ="DEF_VOUCHER_CODE",nullable=true,length=100)
	public java.lang.String getDefVoucherCode(){
		return this.defVoucherCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  默认付款方式
	 */
	public void setDefVoucherCode(java.lang.String defVoucherCode){
		this.defVoucherCode = defVoucherCode;
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
}
