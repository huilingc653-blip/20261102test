package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**   
 * @Title: Entity
 * @Description: 外汇管理日志表
 * @author huangx
 * @date 2019-07-15 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_MESSAGE", schema = "")
@SuppressWarnings("serial")
public class FexMessage extends IdEntity implements java.io.Serializable {
	/**录入时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date enrolDate;
	/**单据号*/
	private String billNo;
	/**操作人员*/
	private String op;
	/**操作人员姓名*/
	private String opName;
	/**消息类型*/
	private Integer messageType;
	/**消息*/
	private String message;
	/**业务类型 foreign-外币汇款 exc-外币汇款指令*/
	private String busType;

	@Column(name ="ENROL_DATE",nullable=false)
	public java.util.Date getEnrolDate(){
		return this.enrolDate;
	}

	public void setEnrolDate(java.util.Date enrolDate){
		this.enrolDate = enrolDate;
	}

	@Column(name ="BILL_NO",nullable=false,length=32)
	public String getBillNo(){
		return this.billNo;
	}

	public void setBillNo(String billNo){
		this.billNo = billNo;
	}

	@Column(name ="OP",nullable=false,length=8)
	public String getOp(){
		return this.op;
	}

	public void setOp(String op){
		this.op = op;
	}

	@Column(name ="OP_NAME",nullable=false,length=40)
	public String getOpName(){
		return this.opName;
	}

	public void setOpName(String opName){
		this.opName = opName;
	}

	@Column(name ="MESSAGE_TYPE",nullable=false,length=10)
	public Integer getMessageType(){
		return this.messageType;
	}

	public void setMessageType(Integer messageType){
		this.messageType = messageType;
	}

	@Column(name ="MESSAGE",nullable=true,length=150)
	public String getMessage(){
		return this.message;
	}

	public void setMessage(String message){
		this.message = message;
	}

	@Column(name ="BUS_TYPE",nullable=false,length=50)
	public String getBusType(){
		return this.busType;
	}

	public void setBusType(String busType){
		this.busType = busType;
	}
}
