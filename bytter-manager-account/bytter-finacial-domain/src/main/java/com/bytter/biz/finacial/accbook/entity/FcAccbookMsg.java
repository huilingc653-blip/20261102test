package com.bytter.biz.finacial.accbook.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_ACCBOOK_MSG
 * @author onlineGenerator
 * @date 2017-06-16 16:15:09
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_ACCBOOK_MSG", schema = "")
@SuppressWarnings("serial")
public class FcAccbookMsg extends IdEntity implements java.io.Serializable {

	/**租户ID*/
	private String tenantId;

	/**流水表ID*/
	private java.lang.String bookId;
	/**日志类型*/
	private java.lang.String msgType;
	/**日志状态*/
	private java.lang.String msgStatus;
	/**日志内容*/
	private java.lang.String msg;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;

	/**
	 *方法: 获取java.lang.String
	 *@return: java.lang.String  租户ID
	 */
	@Column(name ="TENANT_ID")
	public String getTenantId() {
		return this.tenantId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  租户ID
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水表ID
	 */
	@Column(name ="BOOK_ID")
	public java.lang.String getBookId(){
		return this.bookId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水表ID
	 */
	public void setBookId(java.lang.String bookId){
		this.bookId = bookId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日志类型
	 */
	@Column(name ="MSG_TYPE")
	public java.lang.String getMsgType(){
		return this.msgType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日志类型
	 */
	public void setMsgType(java.lang.String msgType){
		this.msgType = msgType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日志状态
	 */
	@Column(name ="MSG_STATUS")
	public java.lang.String getMsgStatus(){
		return this.msgStatus;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日志状态
	 */
	public void setMsgStatus(java.lang.String msgStatus){
		this.msgStatus = msgStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日志内容
	 */
	@Column(name ="MSG")
	public java.lang.String getMsg(){
		return this.msg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日志内容
	 */
	public void setMsg(java.lang.String msg){
		this.msg = msg;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="CREATE_TIME")
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户代码
	 */
	@Column(name ="CREATE_BY")
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后更新时间
	 */
	@Column(name ="UPDATE_TIME")
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后更新用户代码
	 */
	@Column(name ="UPDATE_BY")
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
}
