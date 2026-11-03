package com.bytter.ras.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.IdEntity;

/**   
 * @Title: Entity
 * @Description: 营收稽核日志
 * @author onlineGenerator
 * @date 2017-06-21 15:43:05
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAS_LOG", schema = "")
@SuppressWarnings("serial")
public class RasLog extends IdEntity implements java.io.Serializable {
	/**外部引用ID(流水表ID)*/
	private java.lang.String quoteId;
	/**日志类型：01新增02修改03删除04启用05停用06认领07导入10自动稽核11手动稽核12差异稽核13反稽核20借出21收回22确认-1打回*/
	@Dict(dictGroup = "RAS_LOG_TYPE", inject = "logTypeName")
	private java.lang.String logType;
	private java.lang.String logTypeName;
	/**日志消息*/
	private java.lang.String msg;
	/**创建人名称*/
	private java.lang.String createName;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外部引用ID
	 */
	@Column(name ="QUOTE_ID",nullable=true,length=32)
	public java.lang.String getQuoteId(){
		return this.quoteId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外部引用ID
	 */
	public void setQuoteId(java.lang.String quoteId){
		this.quoteId = quoteId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日志类型：01新增02修改03删除04启用05停用06认领07导入10自动稽核11手动稽核12差异稽核13反稽核20借出21收回22确认
	 */
	@Column(name ="LOG_TYPE",nullable=true,length=10)
	public java.lang.String getLogType(){
		return this.logType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日志类型：01新增02修改03删除04启用05停用06认领07导入10自动稽核11手动稽核12差异稽核13反稽核20借出21收回22确认
	 */
	public void setLogType(java.lang.String logType){
		this.logType = logType;
	}

	@Transient
	public java.lang.String getLogTypeName() {
		return logTypeName;
	}

	public void setLogTypeName(java.lang.String logTypeName) {
		this.logTypeName = logTypeName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日志消息
	 */
	@Column(name ="MSG",nullable=true,length=1000)
	public java.lang.String getMsg(){
		return this.msg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日志消息
	 */
	public void setMsg(java.lang.String msg){
		this.msg = msg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public java.lang.String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(java.lang.String createName){
		this.createName = createName;
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

	@Override
	public String toString() {
		return "RasLog [quoteId=" + quoteId + ", logType=" + logType + ", logTypeName=" + logTypeName + ", msg=" + msg + ", createName=" + createName
				+ ", createBy=" + createBy + ", createDate=" + createDate + "]";
	}
}
