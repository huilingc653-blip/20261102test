package com.bytter.biz.expense.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 业务类型表
 * @author onlineGenerator
 * @date 2017-06-01 14:42:30
 * @version 1.0
 *
 */
@Entity
@Table(name = "NIS_BUSINESS_TYPE", schema = "")
@SuppressWarnings("serial")
public class NisBusinessType extends IdEntity implements java.io.Serializable {
	/**业务类型编码*/
	private java.lang.String busType;
	/**业务类型名称*/
	private java.lang.String busTypeName;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型编码
	 */
	@Column(name ="BUS_TYPE",nullable=false,length=10)
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型名称
	 */
	@Column(name ="BUS_TYPE_NAME",nullable=false,length=30)
	public java.lang.String getBusTypeName(){
		return this.busTypeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型名称
	 */
	public void setBusTypeName(java.lang.String busTypeName){
		this.busTypeName = busTypeName;
	}
}
