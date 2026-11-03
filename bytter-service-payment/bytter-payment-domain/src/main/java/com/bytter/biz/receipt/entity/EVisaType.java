package com.bytter.biz.receipt.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 签章类型维护
 * @author onlineGenerator
 * @date 2017-06-05 10:52:28
 * @version 1.0
 *
 */
@Entity
@Table(name = "E_VISA_TYPE", schema = "")
@SuppressWarnings("serial")
public class EVisaType extends IdEntity implements java.io.Serializable {
	/**网点*/
	private java.lang.String netId;
	/**回单类型*/
	private java.lang.String billType;
	/**回单类型名称*/
	private java.lang.String billName;
	/**签章图片名称*/
	private java.lang.String fileName;
	/**路径*/
	private java.lang.String dir;
	/**宽度*/
	private java.lang.String picWidth;
	/**高度*/
	private java.lang.String picHeight;
	/**有效标志 0，否 1，是*/
	private java.lang.String validSign;
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
	 *@return: java.lang.String  网点
	 */
	@Column(name ="NET_ID",nullable=false,length=32)
	public java.lang.String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  网点
	 */
	public void setNetId(java.lang.String netId){
		this.netId = netId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  回单类型
	 */
	@Column(name ="BILL_TYPE",nullable=false,length=2)
	public java.lang.String getBillType(){
		return this.billType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  回单类型
	 */
	public void setBillType(java.lang.String billType){
		this.billType = billType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  回单类型名称
	 */
	@Column(name ="BILL_NAME",nullable=true,length=40)
	public java.lang.String getBillName(){
		return this.billName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  回单类型名称
	 */
	public void setBillName(java.lang.String billName){
		this.billName = billName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  签章图片名称
	 */
	@Column(name ="FILE_NAME",nullable=true,length=50)
	public java.lang.String getFileName(){
		return this.fileName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  签章图片名称
	 */
	public void setFileName(java.lang.String fileName){
		this.fileName = fileName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  路径
	 */
	@Column(name ="DIR",nullable=true,length=100)
	public java.lang.String getDir(){
		return this.dir;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  路径
	 */
	public void setDir(java.lang.String dir){
		this.dir = dir;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  宽度
	 */
	@Column(name ="PIC_WIDTH",nullable=true,length=10)
	public java.lang.String getPicWidth(){
		return this.picWidth;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  宽度
	 */
	public void setPicWidth(java.lang.String picWidth){
		this.picWidth = picWidth;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  高度
	 */
	@Column(name ="PIC_HEIGHT",nullable=true,length=10)
	public java.lang.String getPicHeight(){
		return this.picHeight;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  高度
	 */
	public void setPicHeight(java.lang.String picHeight){
		this.picHeight = picHeight;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标志 0，否 1，是
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public java.lang.String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标志 0，否 1，是
	 */
	public void setValidSign(java.lang.String validSign){
		this.validSign = validSign;
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
