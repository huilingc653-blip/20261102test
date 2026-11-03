package com.bytter.cashVoucher.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**   
 * @Title: Entity
 * @Description: 空白凭证日志
 * @author onlineGenerator
 * @date 2017-05-31 15:43:54
 * @version 1.0
 *
 */
@Entity
@Table(name = "BV_LOG", schema = "")
@SuppressWarnings("serial")
public class BvLog extends IdEntity implements java.io.Serializable {
	/**外部引用ID*/
	private String quoteId;
	/**日志类型*/
	// @Dict(dictGroup = "BVLOG_LOGTYPE", inject = "logTypeName")
	private String logType;
	/**日志消息*/
	private String msg;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	private String logTypeName;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外部引用ID
	 */
	@Column(name ="QUOTE_ID",nullable=true,length=32)
	public String getQuoteId(){
		return this.quoteId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外部引用ID
	 */
	public void setQuoteId(String quoteId){
		this.quoteId = quoteId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日志类型
	 */
	@Column(name ="LOG_TYPE",nullable=true,length=10)
	public String getLogType(){
		return this.logType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日志类型
	 */
	public void setLogType(String logType){
		this.logType = logType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日志消息
	 */
	@Column(name ="MSG",nullable=true,length=1000)
	public String getMsg(){
		return this.msg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日志消息
	 */
	public void setMsg(String msg){
		this.msg = msg;
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
	@Transient
	public String getLogTypeName() {
		return logTypeName;
	}
	public void setLogTypeName(String logTypeName) {
		this.logTypeName = logTypeName;
	}
	@Override
	public String toString() {
		return "BvLog [quoteId=" + quoteId + ", logType=" + logType + ", msg=" + msg + ", createName=" + createName
				+ ", createBy=" + createBy + ", createDate=" + createDate + ", updateName=" + ", logTypeName=" + logTypeName + "]";
	}
	
	
	
}
