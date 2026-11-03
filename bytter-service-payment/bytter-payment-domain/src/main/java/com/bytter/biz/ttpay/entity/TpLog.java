package com.bytter.biz.ttpay.entity;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * @Title: Entity
 * @Description: 第三方支付日志
 * @author zhengg
 * @date 2019-09-24 15:43:54
 * @version 1.0
 *
 */
@Entity
@Table(name = "TP_LOG", schema = "")
@SuppressWarnings("serial")
public class TpLog extends IdEntity implements java.io.Serializable {
	/**外部引用ID*/
	private java.lang.String quoteId;
	/**日志类型*/
	@Dict(dictGroup = "TTPAY_LOG_TYPE", inject = "logTypeName")
	private java.lang.String logType;
	/**日志消息*/
	private java.lang.String msg;
	/**创建人名称*/
	private java.lang.String createName;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	private java.lang.String logTypeName;
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
	 *@return: java.lang.String  日志类型
	 */
	@Column(name ="LOG_TYPE",nullable=true,length=10)
	public java.lang.String getLogType(){
		return this.logType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日志类型
	 */
	public void setLogType(java.lang.String logType){
		this.logType = logType;
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
	@Transient
	public java.lang.String getLogTypeName() {
		return logTypeName;
	}
	public void setLogTypeName(java.lang.String logTypeName) {
		this.logTypeName = logTypeName;
	}
	@Override
	public String toString() {
		return "TpLog [quoteId=" + quoteId + ", logType=" + logType + ", msg=" + msg + ", createName=" + createName
				+ ", createBy=" + createBy + ", createDate=" + createDate + ", updateName=" + ", logTypeName=" + logTypeName + "]";
	}
}
