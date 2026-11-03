package com.bytter.biz.dataexchange.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 数据类型
 * @author onlineGenerator
 * @date 2017-12-13 15:29:54
 * @version 1.0
 *
 */
@Entity
@Table(name = "ERP_DATA_TYPE", schema = "")
@SuppressWarnings("serial")
public class ErpDataType extends IdEntity implements java.io.Serializable {
	/**类型代码*/
	private String code;
	/**类型名称*/
	private String name;
	/**表名*/
	private String tableName;
	/**CODE字段*/
	private String keyField;
	/**NAME字段*/
	private String valueField;
	/**条件*/
	private String queryCondition;
	/**是否有效*/
	private String validSign;
	/**备注*/
	private String rmk;
	/**是否系统初始化*/
	private String isSystem;
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
	 *@return: java.lang.String  类型代码
	 */
	@Column(name ="CODE",nullable=true,length=50)
	public String getCode(){
		return this.code;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  类型代码
	 */
	public void setCode(String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  类型名称
	 */
	@Column(name ="NAME",nullable=true,length=50)
	public String getName(){
		return this.name;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  类型名称
	 */
	public void setName(String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  表名
	 */
	@Column(name ="TABLE_NAME",nullable=true,length=50)
	public String getTableName(){
		return this.tableName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  表名
	 */
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  CODE字段
	 */
	@Column(name ="KEY_FIELD",nullable=true,length=50)
	public String getKeyField(){
		return this.keyField;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  CODE字段
	 */
	public void setKeyField(String keyField){
		this.keyField = keyField;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  NAME字段
	 */
	@Column(name ="VALUE_FIELD",nullable=true,length=50)
	public String getValueField(){
		return this.valueField;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  NAME字段
	 */
	public void setValueField(String valueField){
		this.valueField = valueField;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  条件
	 */
	@Column(name ="QUERY_CONDITION",nullable=true,length=100)
	public String getQueryCondition(){
		return this.queryCondition;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  条件
	 */
	public void setQueryCondition(String queryCondition){
		this.queryCondition = queryCondition;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否有效
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否有效
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
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
	 *@return: java.lang.String  是否系统初始化
	 */
	@Column(name ="IS_SYSTEM",nullable=true,length=1)
	public String getIsSystem(){
		return this.isSystem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否系统初始化
	 */
	public void setIsSystem(String isSystem){
		this.isSystem = isSystem;
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
