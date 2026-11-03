package com.bytter.bill.domain.bill.entity;
import com.bytter.core.annotation.Dict;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**   
 * @Title: Entity
 * @Description: 票据日志
 * @author onlineGenerator
 * @date 2017-06-21 15:43:05
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_LOG", schema = "")
@SuppressWarnings("serial")
public class AbLog extends IdEntity implements java.io.Serializable {
	/**外部引用ID(从表ID)*/
	private String quotezId;
	/**外部引用ID(流水表ID)*/
	private String quoteId;
	/**业务类型*/
	@Dict(dictGroup = "AB_BRIEF_STATUS", inject = "briefIdName")
	private String briefId;
	/**日志类型：-2删除-1打回01新增02修改04启用05停用06入库0210打印11重新打印20使用21出售22丢失23作废24退票25移库*/
	@Dict(dictGroup = "AB_LOG_LOGTYPE", inject = "logTypeName")
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
	private String briefIdName;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外部引用ID
	 */
	@Column(name ="QUOTEZ_ID",nullable=true,length=32)
	public String getQuotezId(){
		return this.quotezId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外部引用ID
	 */
	public void setQuotezId(String quotezId){
		this.quotezId = quotezId;
	}
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
	 *@return: java.lang.String  业务类型
	 */
	@Column(name ="BRIEF_ID",nullable=true,length=10)
	public String getBriefId(){
		return this.briefId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型
	 */
	public void setBriefId(String briefId){
		this.briefId = briefId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日志类型：-2删除-1打回01新增02修改04启用05停用06入库0210打印11重新打印20使用21出售22丢失23作废24退票25移库
	 */
	@Column(name ="LOG_TYPE",nullable=true,length=10)
	public String getLogType(){
		return this.logType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日志类型：-2删除-1打回01新增02修改04启用05停用06入库0210打印11重新打印20使用21出售22丢失23作废24退票25移库
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
	@Transient
	public String getBriefIdName() {
		return briefIdName;
	}
	public void setBriefIdName(String briefIdName) {
		this.briefIdName = briefIdName;
	}
	@Override
	public String toString() {
		return "AbLog [quotezId=" + quotezId + ", quoteId=" + quoteId + ", briefId=" + briefId + ", logType=" + logType
				+ ", msg=" + msg + ", createName=" + createName + ", createBy=" + createBy + ", createDate="
				+ createDate + ", logTypeName=" + logTypeName + ", briefIdName=" + briefIdName + "]";
	}
	
	
}
