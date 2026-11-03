package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 账户属性表
 * @author onlineGenerator
 * @date 2017-05-12 17:18:17
 * @version V1.0
 *
 */
@SuppressWarnings("serial")
@ApiModel("BisAccAttribute")
@Entity
@Table(name = "BIS_ACC_ATTRIBUTE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BisAccAttribute extends IdEntity implements java.io.Serializable {
	/**收支属性编码*/
	@ApiModelProperty("收支属性编码")
	private String attributeCode;

	/**收支属性名称*/
	@ApiModelProperty("收支属性名称")
	private String attributeName;

	/**对外属性*/
	@ApiModelProperty("对外属性")
	private String foreignDes;

	/**对同级属性*/
	@ApiModelProperty("对同级属性")
	private String sameDes;

	/**收支属性描述*/
	@ApiModelProperty("收支属性描述")
	private String attributeDes;

	/**对上级属性 */
	@ApiModelProperty("对上级属性")
	private String superDes;

	/**备注*/
	@ApiModelProperty("备注")
	private String rmk;

	/**有效标志*/
	@ApiModelProperty("有效标志")
	private String validSign;

	/**创建人登录名称*/
	@ApiModelProperty("创建人登录名称")
	private String createBy;

	/**创建日期*/
	@ApiModelProperty("创建日期")
	private java.util.Date createDate;

	/**更新人登录名称*/
	@ApiModelProperty("更新人登录名称")
	private String updateBy;

	/**更新日期*/
	@ApiModelProperty("更新日期")
	private java.util.Date updateDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收支属性编码
	 */
	@Column(name ="ATTRIBUTE_CODE",nullable=false,length=10)
	public String getAttributeCode(){
		return this.attributeCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收支属性编码
	 */
	public void setAttributeCode(String attributeCode){
		this.attributeCode = attributeCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收支属性名称
	 */
	@Column(name ="ATTRIBUTE_NAME",nullable=false,length=30)
	public String getAttributeName(){
		return this.attributeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收支属性名称
	 */
	public void setAttributeName(String attributeName){
		this.attributeName = attributeName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对外属性
	 */
	@Column(name ="FOREIGN_DES",nullable=true,length=1)
	public String getForeignDes(){
		return this.foreignDes;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对外属性
	 */
	public void setForeignDes(String foreignDes){
		this.foreignDes = foreignDes;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对同级属性
	 */
	@Column(name ="SAME_DES",nullable=true,length=1)
	public String getSameDes(){
		return this.sameDes;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对同级属性
	 */
	public void setSameDes(String sameDes){
		this.sameDes = sameDes;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收支属性描述
	 */
	@Column(name ="ATTRIBUTE_DES",nullable=false,length=3)
	public String getAttributeDes(){
		return this.attributeDes;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收支属性描述
	 */
	public void setAttributeDes(String attributeDes){
		this.attributeDes = attributeDes;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对上级属性
	 */
	@Column(name ="SUPER_DES",nullable=true,length=1)
	public String getSuperDes(){
		return this.superDes;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对上级属性
	 */
	public void setSuperDes(String superDes){
		this.superDes = superDes;
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
	 *@return: java.lang.String  有效标志
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标志
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
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
