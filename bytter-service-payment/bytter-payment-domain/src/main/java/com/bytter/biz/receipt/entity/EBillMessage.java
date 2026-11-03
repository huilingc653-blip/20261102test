package com.bytter.biz.receipt.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: E_BILL_MESSAGE
 * @author onlineGenerator
 * @date 2017-08-22 17:34:11
 * @version 1.0
 *
 */
@Entity
@Table(name = "E_BILL_MESSAGE", schema = "")
@SuppressWarnings("serial")
public class EBillMessage extends IdEntity implements java.io.Serializable {
	/**录入时间*/
	private java.util.Date enrolDate;
	/**电子回单号*/
	private java.lang.String billNo;
	/**操作人员*/
	private java.lang.String op;
	/**操作人员姓名*/
	private java.lang.String opName;
	/**操作类型OP_TYPE =1 表示生成数据OP_TYPE =2 表示作废数据OP_TYPE =3 表示删除数据OP_TYPE =4 表示打印数据*/
	private java.lang.Integer opType;
	/**消息*/
	private java.lang.String message;
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
	 *@return: java.lang.String  电子回单号
	 */
	@Column(name ="BILL_NO",nullable=false,length=20)
	public java.lang.String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单号
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
	 *@return: java.lang.Integer  操作类型OP_TYPE =1 表示生成数据OP_TYPE =2 表示作废数据OP_TYPE =3 表示删除数据OP_TYPE =4 表示打印数据
	 */
	@Column(name ="OP_TYPE",nullable=false,length=10)
	public java.lang.Integer getOpType(){
		return this.opType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  操作类型OP_TYPE =1 表示生成数据OP_TYPE =2 表示作废数据OP_TYPE =3 表示删除数据OP_TYPE =4 表示打印数据
	 */
	public void setOpType(java.lang.Integer opType){
		this.opType = opType;
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
}
