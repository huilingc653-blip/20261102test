package com.bytter.biz.netbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Title: Entity
 * @Description: 退单类型表
 * @author onlineGenerator
 * @date 2017-05-17 14:26:13
 * @version 1.0
 *
 */
@ApiModel("退单类型表")
@Entity
@Table(name = "BT_CHARGE_BACK_TYPE", schema = "")
@SuppressWarnings("serial")
public class BtChargeBackType extends IdEntity implements java.io.Serializable {
	/**退单类型编码*/
	@ApiModelProperty("退单类型编码")
	private java.lang.String busType;

	/**退单类型名称*/
	@ApiModelProperty("退单类型名称")
	private java.lang.String busTypeName;

	/**是否默认*/
	@ApiModelProperty("是否默认")
	private java.lang.String isDefault;

	/**有效标志*/
	@ApiModelProperty("有效标志")
	private java.lang.String validSign;

	/**备注*/
	@ApiModelProperty("备注")
	private java.lang.String rmk;

	/**创建人登录名称*/
	@ApiModelProperty("创建人登录名称")
	private java.lang.String createBy;

	/**创建日期*/
	@ApiModelProperty("创建日期")
	private java.util.Date createDate;

	/**更新人登录名称*/
	@ApiModelProperty("更新人登录名称")
	private java.lang.String updateBy;

	/**更新日期*/
	@ApiModelProperty("更新日期")
	private java.util.Date updateDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  退单类型编码
	 */
	@Column(name ="BUS_TYPE",nullable=false,length=10)
	public java.lang.String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  退单类型编码
	 */
	public void setBusType(java.lang.String busType){
		this.busType = busType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  退单类型名称
	 */
	@Column(name ="BUS_TYPE_NAME",nullable=false,length=30)
	public java.lang.String getBusTypeName(){
		return this.busTypeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  退单类型名称
	 */
	public void setBusTypeName(java.lang.String busTypeName){
		this.busTypeName = busTypeName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否默认
	 */
	@Column(name ="IS_DEFAULT",nullable=true,length=1)
	public java.lang.String getIsDefault(){
		return this.isDefault;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否默认
	 */
	public void setIsDefault(java.lang.String isDefault){
		this.isDefault = isDefault;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标志
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public java.lang.String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标志
	 */
	public void setValidSign(java.lang.String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=100)
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
