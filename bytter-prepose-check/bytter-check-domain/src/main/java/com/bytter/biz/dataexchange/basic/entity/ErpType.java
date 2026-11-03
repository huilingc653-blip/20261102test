package com.bytter.biz.dataexchange.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 接口类型
 * @author onlineGenerator
 * @date 2017-12-13 14:53:07
 * @version 1.0
 *
 */
@Entity
@Table(name = "ERP_TYPE", schema = "")
@SuppressWarnings("serial")
public class ErpType extends IdEntity implements java.io.Serializable {
	/**系统代码*/
	private String typeCode;
	/**系统名称*/
	private String typeName;
	/**备注*/
	private String rmk;
	/**createDate*/
	private java.util.Date createDate;
	/**createBy*/
	private String createBy;
	/**updateDate*/
	private java.util.Date updateDate;
	/**updateBy*/
	private String updateBy;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  系统代码
	 */
	@Column(name ="TYPE_CODE",nullable=true,length=50)
	public String getTypeCode(){
		return this.typeCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  系统代码
	 */
	public void setTypeCode(String typeCode){
		this.typeCode = typeCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  系统名称
	 */
	@Column(name ="TYPE_NAME",nullable=true,length=50)
	public String getTypeName(){
		return this.typeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  系统名称
	 */
	public void setTypeName(String typeName){
		this.typeName = typeName;
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  createDate
	 */
	@Column(name ="CREATE_DATE",nullable=true,scale=6)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  createDate
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  createBy
	 */
	@Column(name ="CREATE_BY",nullable=true,length=32)
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  createBy
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  updateDate
	 */
	@Column(name ="UPDATE_DATE",nullable=true,scale=6)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  updateDate
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  updateBy
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=32)
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  updateBy
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
}
