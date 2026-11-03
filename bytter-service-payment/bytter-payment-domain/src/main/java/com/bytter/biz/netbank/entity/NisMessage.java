package com.bytter.biz.netbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
/**   
 * @Title: Entity
 * @Description: NIS_MESSAGE
 * @author onlineGenerator
 * @date 2017-06-12 14:53:20
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_MESSAGE", schema = "")
@SuppressWarnings("serial")
public class NisMessage extends IdEntity implements java.io.Serializable {
	/**录入时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date enrolDate;
	/**单据号*/
	private java.lang.String billNo;
	/**操作人员*/
	private java.lang.String op;
	/**操作人员姓名*/
	private java.lang.String opName;
	/**messageType*/
	private java.lang.Integer messageType;
	/**message*/
	private java.lang.String message;
	/**busType*/
	private java.lang.String busType;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  录入时间
	 */
	@Column(name ="ENROL_DATE",nullable=false)
	public java.util.Date getEnrolDate(){
		return this.enrolDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  录入时间
	 */
	public void setEnrolDate(java.util.Date enrolDate){
		this.enrolDate = enrolDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单据号
	 */
	@Column(name ="BILL_NO",nullable=false,length=32)
	public java.lang.String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单据号
	 */
	public void setBillNo(java.lang.String billNo){
		this.billNo = billNo;
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
	 *@return: java.lang.Integer  messageType
	 */
	@Column(name ="MESSAGE_TYPE",nullable=false,length=10)
	public java.lang.Integer getMessageType(){
		return this.messageType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  messageType
	 */
	public void setMessageType(java.lang.Integer messageType){
		this.messageType = messageType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  message
	 */
	@Column(name ="MESSAGE",nullable=true,length=150)
	public java.lang.String getMessage(){
		return this.message;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  message
	 */
	public void setMessage(java.lang.String message){
		this.message = message;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  busType
	 */
	@Column(name ="BUS_TYPE",nullable=false,length=50)
	public java.lang.String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  busType
	 */
	public void setBusType(java.lang.String busType){
		this.busType = busType;
	}
}
