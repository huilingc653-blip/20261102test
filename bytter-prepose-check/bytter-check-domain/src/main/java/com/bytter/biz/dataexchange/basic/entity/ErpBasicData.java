package com.bytter.biz.dataexchange.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 基础数据映射
 * @author onlineGenerator
 * @date 2017-12-14 11:24:27
 * @version 1.0
 *
 */
@Entity
@Table(name = "ERP_BASIC_DATA", schema = "")
@SuppressWarnings("serial")
public class ErpBasicData extends IdEntity implements java.io.Serializable {
	/**内部系统代码*/
	private String bytterCode;
	/**外部系统代码*/
	private String erpCode;
	/**外部系统名称*/
	private String erpName;
	/**外部系统备用*/
	private String erpReverse;
	/**对应数据类型*/
	private String dataTypeCode;
	/**对应接口类型*/
	private String typeCode;
	/**createDate*/
	private Date createDate;
	/**createBy*/
	private String createBy;
	/**updateDate*/
	private Date updateDate;
	/**updateBy*/
	private String updateBy;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  内部系统代码
	 */
	@Column(name ="BYTTER_CODE",nullable=true,length=50)
	public String getBytterCode(){
		return this.bytterCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  内部系统代码
	 */
	public void setBytterCode(String bytterCode){
		this.bytterCode = bytterCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外部系统代码
	 */
	@Column(name ="ERP_CODE",nullable=true,length=50)
	public String getErpCode(){
		return this.erpCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外部系统代码
	 */
	public void setErpCode(String erpCode){
		this.erpCode = erpCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外部系统名称
	 */
	@Column(name ="ERP_NAME",nullable=true,length=150)
	public String getErpName(){
		return this.erpName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外部系统名称
	 */
	public void setErpName(String erpName){
		this.erpName = erpName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外部系统备用
	 */
	@Column(name ="ERP_REVERSE",nullable=true,length=100)
	public String getErpReverse(){
		return this.erpReverse;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外部系统备用
	 */
	public void setErpReverse(String erpReverse){
		this.erpReverse = erpReverse;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对应数据类型
	 */
	@Column(name ="DATA_TYPE_CODE",nullable=true,length=50)
	public String getDataTypeCode(){
		return this.dataTypeCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对应数据类型
	 */
	public void setDataTypeCode(String dataTypeCode){
		this.dataTypeCode = dataTypeCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对应接口类型
	 */
	@Column(name ="TYPE_CODE",nullable=true,length=50)
	public String getTypeCode(){
		return this.typeCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对应接口类型
	 */
	public void setTypeCode(String typeCode){
		this.typeCode = typeCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  createDate
	 */
	@Column(name ="CREATE_DATE",nullable=true,scale=6)
	public Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  createDate
	 */
	public void setCreateDate(Date createDate){
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
	public Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  updateDate
	 */
	public void setUpdateDate(Date updateDate){
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
