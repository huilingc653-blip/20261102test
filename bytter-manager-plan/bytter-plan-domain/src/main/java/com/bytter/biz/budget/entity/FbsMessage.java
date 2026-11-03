package com.bytter.biz.budget.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
/**   
 * @Title: Entity
 * @Description: FBS_MESSAGE
 * @author onlineGenerator
 * @date 2017-07-19 13:39:53
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_MESSAGE", schema = "")
@SuppressWarnings("serial")
public class FbsMessage extends IdEntity implements java.io.Serializable {
	/**录入时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date enterDate;
	/**单据号*/
	private java.lang.String billId;
	/**操作人员*/
	private java.lang.String op;
	/**操作人员姓名*/
	private java.lang.String opName;
	/**操作类型*/
	private java.lang.Integer messageType;
	/**消息*/
	private java.lang.String message;
	/**业务名称 如计划上报 计划追加 计划执行*/
	private java.lang.String busName;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  录入时间
	 */
	@Column(name ="ENTER_DATE",nullable=false)
	public java.util.Date getEnterDate(){
		return this.enterDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  录入时间
	 */
	public void setEnterDate(java.util.Date enterDate){
		this.enterDate = enterDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单据号
	 */
	@Column(name ="BILL_ID",nullable=false,length=32)
	public java.lang.String getBillId(){
		return this.billId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单据号
	 */
	public void setBillId(java.lang.String billId){
		this.billId = billId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作人员
	 */
	@Column(name ="OP",nullable=false,length=8)
	public java.lang.String getOp(){
		return this.op;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作人员
	 */
	public void setOp(java.lang.String op){
		this.op = op;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作人员姓名
	 */
	@Column(name ="OP_NAME",nullable=false,length=40)
	public java.lang.String getOpName(){
		return this.opName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作人员姓名
	 */
	public void setOpName(java.lang.String opName){
		this.opName = opName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  操作类型
	 */
	@Column(name ="MESSAGE_TYPE",nullable=false,length=10)
	public java.lang.Integer getMessageType(){
		return this.messageType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  操作类型
	 */
	public void setMessageType(java.lang.Integer messageType){
		this.messageType = messageType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  消息
	 */
	@Column(name ="MESSAGE",nullable=true,length=150)
	public java.lang.String getMessage(){
		return this.message;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  消息
	 */
	public void setMessage(java.lang.String message){
		this.message = message;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务名称 如计划上报 计划追加 计划执行
	 */
	@Column(name ="BUS_NAME",nullable=false,length=50)
	public java.lang.String getBusName(){
		return this.busName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务名称 如计划上报 计划追加 计划执行
	 */
	public void setBusName(java.lang.String busName){
		this.busName = busName;
	}
}
