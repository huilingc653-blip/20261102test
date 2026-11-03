package com.bytter.biz.netbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 业务类别表
 * @author onlineGenerator
 * @date 2017-05-17 19:23:52
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_BUS_TYPE", schema = "")
@SuppressWarnings("serial")
public class NisBusType extends IdEntity implements java.io.Serializable {
	/**业务类别编码*/
	private java.lang.String busType;
	/**业务类别名称*/
	private java.lang.String busTypeName;
	/**收付款标志*/
	private java.lang.String cdSign;
	/**计划科目*/
	private java.lang.String itemCode;
	/**有效标志*/
	private java.lang.String validSign;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**updateDate*/
	private java.util.Date updateDate;
	/** 对应系统业务类别*/
	private String sysBusType;
	/** 结算类型*/
	private String settleType;
	/** 租户id*/
	private String tenantId;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类别编码
	 */
	@Column(name ="BUS_TYPE",nullable=false,length=10)
	public java.lang.String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类别编码
	 */
	public void setBusType(java.lang.String busType){
		this.busType = busType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类别名称
	 */
	@Column(name ="BUS_TYPE_NAME",nullable=false,length=60)
	public java.lang.String getBusTypeName(){
		return this.busTypeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类别名称
	 */
	public void setBusTypeName(java.lang.String busTypeName){
		this.busTypeName = busTypeName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收付款标志
	 */
	@Column(name ="CD_SIGN",nullable=true,length=1)
	public java.lang.String getCdSign(){
		return this.cdSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收付款标志
	 */
	public void setCdSign(java.lang.String cdSign){
		this.cdSign = cdSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计划科目
	 */
	@Column(name ="ITEM_CODE",nullable=true,length=40)
	public java.lang.String getItemCode(){
		return this.itemCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计划科目
	 */
	public void setItemCode(java.lang.String itemCode){
		this.itemCode = itemCode;
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
	 *@return: java.util.Date  updateDate
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
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
	@Column(name ="SYS_BUS_TYPE")
	public String getSysBusType() {
		return sysBusType;
	}
	public void setSysBusType(String sysBusType) {
		this.sysBusType = sysBusType;
	}
	@Column(name ="SETTLE_TYPE")
	public String getSettleType() {
		return settleType;
	}
	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}

	@Column(name ="TENANT_ID")
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
