package com.bytter.cashVoucher.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 库箱维护
 * @author onlineGenerator
 * @date 2017-05-25 18:17:27
 * @version 1.0
 *
 */
@Entity
@Table(name = "BV_STORAGE_BOX", schema = "")
@SuppressWarnings("serial")
public class BvStorageBox extends IdEntity implements java.io.Serializable {
	/**更新日期*/
	private java.util.Date updateDate;
	/**库箱代码*/
	private String code;
	/**库存名称*/
	private String name;
	/**库箱所属单位*/
	private String corpId;
	/**冗余·库箱所属单位名*/
	private String corpName;
	/**库箱保管员*/
	private String keeperUid;
	/**冗余·库箱保管员用户名*/
	private String keeperName;
	/**库箱地址*/
	private String address;
	/**备注*/
	private String rmk;
	/**有效标志：0，停用  1，启用  2，已删除*/
	private String validSign;
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
	 *@return: java.lang.String  库箱代码
	 */
	@Column(name ="CODE",nullable=true,length=10)
	public String getCode(){
		return this.code;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  库箱代码
	 */
	public void setCode(String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  库存名称
	 */
	@Column(name ="NAME",nullable=true,length=50)
	public String getName(){
		return this.name;
	}
	@Override
	public String toString() {
		return "BvStorageBox [updateDate=" + updateDate + ", code=" + code + ", name=" + name + ", corpId=" + corpId
				+ ", keeperUid=" + keeperUid + ", address=" + address + ", rmk=" + rmk + ", validSign=" + validSign
				+ ", createName=" + createName + ", createBy=" + createBy + ", createDate=" + createDate
				+ ", updateName=" + updateName + ", updateBy=" + updateBy + "]";
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  库存名称
	 */
	public void setName(String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  库箱所属单位
	 */
	@Column(name ="CORP_ID",nullable=true,length=32)
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  库箱所属单位
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  库箱保管员
	 */
	@Column(name ="KEEPER_UID",nullable=true,length=32)
	public String getKeeperUid(){
		return this.keeperUid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  库箱保管员
	 */
	public void setKeeperUid(String keeperUid){
		this.keeperUid = keeperUid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  库箱地址
	 */
	@Column(name ="ADDRESS",nullable=true,length=100)
	public String getAddress(){
		return this.address;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  库箱地址
	 */
	public void setAddress(String address){
		this.address = address;
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
	 *@return: java.lang.String  有效标志：0，否 1，是 2，已删除
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标志：0，否 1，是 2，已删除
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
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
	@Column(name ="CORP_NAME", length=200)
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	@Column(name ="KEEPER_NAME", length=30)
	public String getKeeperName() {
		return keeperName;
	}
	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}
}
