package com.bytter.cashVoucher.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 凭证打印格式
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Entity
@Table(name = "BV_PRINT_FORMAT", schema = "")
@SuppressWarnings("serial")
public class BvPrintFormat extends IdEntity implements java.io.Serializable {
	/**凭证类型ID*/
	private String vouTid;
	/**打印場景ID*/
	private String sceneId;
	/**左边距*/
	private Double leftBorder;
	/**打印格式编号*/
	private String fmtNo;
	/**打印格式说明*/
	private String fmtName;
	/**纸张大小*/
	private String paperSize;
	/**下边距*/
	private Double upBorder;
	/** 有效标志*/
	private String validSign;
	/** 背景图片ID*/
	private String backgroundImage;
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
	 *@return: java.lang.String  背景图片ID
	 */
	@Column(name ="BACKGROUND_IMAGE",nullable=true,length=32)
	public String getBackgroundImage() {
		return backgroundImage;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  背景图片ID
	 */
	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证类型ID
	 */
	@Column(name ="VOU_TID",nullable=true,length=32)
	public String getVouTid(){
		return this.vouTid;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证类型ID
	 */
	public void setVouTid(String vouTid){
		this.vouTid = vouTid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打印場景ID
	 */
	@Column(name ="SCENE_ID")
	public String getSceneId(){
		return this.sceneId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打印場景ID
	 */
	public void setSceneId(String sceneId){
		this.sceneId = sceneId;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  左边距
	 */
	@Column(name ="LEFT_BORDER",nullable=true,length=10)
	public Double getLeftBorder(){
		return this.leftBorder;
	}
	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  左边距
	 */
	public void setLeftBorder(Double leftBorder){
		this.leftBorder = leftBorder;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打印格式编号
	 */
	@Column(name ="FMT_NO",nullable=true,length=10)
	public String getFmtNo(){
		return this.fmtNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打印格式编号
	 */
	public void setFmtNo(String fmtNo){
		this.fmtNo = fmtNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打印格式说明
	 */
	@Column(name ="FMT_NAME",nullable=true,length=50)
	public String getFmtName(){
		return this.fmtName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打印格式说明
	 */
	public void setFmtName(String fmtName){
		this.fmtName = fmtName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  纸张大小
	 */
	@Column(name ="PAPER_SIZE",nullable=true,length=20)
	public String getPaperSize(){
		return this.paperSize;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  纸张大小
	 */
	public void setPaperSize(String paperSize){
		this.paperSize = paperSize;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  下边距
	 */
	@Column(name ="UP_BORDER",nullable=true,length=10)
	public Double getUpBorder(){
		return this.upBorder;
	}
	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  下边距
	 */
	public void setUpBorder(Double upBorder){
		this.upBorder = upBorder;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String   有效标志
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String   有效标志
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
	@Override
	public String toString() {
		return "BvPrintFormat [vouTid=" + vouTid + ", leftBorder=" + leftBorder + ", fmtNo=" + fmtNo + ", fmtName="
				+ fmtName + ", paperSize=" + paperSize + ", upBorder=" + upBorder + ", validSign=" + validSign
				+ ", rmk=" + rmk + ", createName=" + createName + ", createBy=" + createBy + ", createDate="
				+ createDate + ", updateName=" + updateName + ", updateBy=" + updateBy + ", updateDate=" + updateDate
				+ "]";
	}
	
}
