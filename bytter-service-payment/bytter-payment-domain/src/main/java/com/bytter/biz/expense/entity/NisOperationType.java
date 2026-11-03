package com.bytter.biz.expense.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 操作类型表
 * @author onlineGenerator
 * @date 2017-06-01 14:40:32
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_OPERATION_TYPE", schema = "")
@SuppressWarnings("serial")
public class NisOperationType extends IdEntity implements java.io.Serializable {
	/**操作类型编码*/
	private java.lang.String opType;
	/**操作类型名称*/
	private java.lang.String opTypeName;
	/**业务类型编码*/
	private java.lang.String busType;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作类型编码
	 */
	@Column(name ="OP_TYPE",nullable=false,length=10)
	public java.lang.String getOpType(){
		return this.opType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作类型编码
	 */
	public void setOpType(java.lang.String opType){
		this.opType = opType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作类型名称
	 */
	@Column(name ="OP_TYPE_NAME",nullable=false,length=30)
	public java.lang.String getOpTypeName(){
		return this.opTypeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作类型名称
	 */
	public void setOpTypeName(java.lang.String opTypeName){
		this.opTypeName = opTypeName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型编码
	 */
	@Column(name ="BUS_TYPE",nullable=false,length=32)
	public java.lang.String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型编码
	 */
	public void setBusType(java.lang.String busType){
		this.busType = busType;
	}
}
