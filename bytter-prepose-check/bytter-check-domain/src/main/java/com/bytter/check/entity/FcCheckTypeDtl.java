package com.bytter.check.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Title: Entity
 * @Description: FC_CHECK_TYPE_DTL
 * @author onlineGenerator
 * @date 2018-01-03 14:15:14
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_CHECK_TYPE_DTL", schema = "")
@SuppressWarnings("serial")
public class FcCheckTypeDtl extends IdEntity implements java.io.Serializable {
	/**核对标准ID*/
	private String checkTypeId;
	/**对账类型*/
	private String checkType;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  核对标准ID
	 */
	@Column(name ="CHECK_TYPE_ID")
	public String getCheckTypeId(){
		return this.checkTypeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  核对标准ID
	 */
	public void setCheckTypeId(String checkTypeId){
		this.checkTypeId = checkTypeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对账类型
	 */
	@Column(name ="CHECK_TYPE")
	public String getCheckType(){
		return this.checkType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对账类型
	 */
	public void setCheckType(String checkType){
		this.checkType = checkType;
	}
	
}
