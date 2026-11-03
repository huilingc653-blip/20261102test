package com.bytter.biz.bdlink.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: BtBankAccMessage
 * @author onlineGenerator
 * @date 2017-09-21
 * @version 1.0
 *
 */
//用于传值
public class BtBankAccMessage extends IdEntity implements java.io.Serializable {
	/**录入时间*/
	private java.util.Date enrolDate;
	/**单据号*/
	private String billNo;
	/**操作人员*/
	private String op;
	/**操作人员姓名*/
	private String opName;
	/**messageType*/
	private Integer messageType;
	/**message*/
	private String message;
	/**busType*/
	private String busType;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  录入时间
	 */
	//@Column(name ="ENROL_DATE",nullable=false)
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
	//@Column(name ="BILL_NO",nullable=false,length=20)
	public String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单据号
	 */
	public void setBillNo(String billNo){
		this.billNo = billNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作人员
	 */
	//@Column(name ="OP",nullable=false,length=8)
	public String getOp(){
		return this.op;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作人员
	 */
	public void setOp(String op){
		this.op = op;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作人员姓名
	 */
	//@Column(name ="OP_NAME",nullable=false,length=40)
	public String getOpName(){
		return this.opName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作人员姓名
	 */
	public void setOpName(String opName){
		this.opName = opName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  messageType
	 */
	//@Column(name ="MESSAGE_TYPE",nullable=false,length=10)
	public Integer getMessageType(){
		return this.messageType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  messageType
	 */
	public void setMessageType(Integer messageType){
		this.messageType = messageType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  message
	 */
	//@Column(name ="MESSAGE",nullable=true,length=150)
	public String getMessage(){
		return this.message;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  message
	 */
	public void setMessage(String message){
		this.message = message;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  busType
	 */
	//@Column(name ="BUS_TYPE",nullable=false,length=50)
	public String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  busType
	 */
	public void setBusType(String busType){
		this.busType = busType;
	}
}
