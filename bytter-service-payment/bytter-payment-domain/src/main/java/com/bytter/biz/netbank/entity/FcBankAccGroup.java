package com.bytter.biz.netbank.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 银行账户组
 *
 */
@Entity
@Table(name = "FC_BANK_ACC_GROUP", schema = "")
@SuppressWarnings("serial")
public class FcBankAccGroup extends IdEntity implements java.io.Serializable {

	/**账户组编码*/
	private String accGroupCode;

	/**账户组名称*/
	private String accGroupName;

//	/**所属营运区ID*/
//	private String operationAreaId;

	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;

	@Column(name ="ACC_GROUP_CODE",length=32)
	public String getAccGroupCode() {
		return accGroupCode;
	}

	public void setAccGroupCode(String accGroupCode) {
		this.accGroupCode = accGroupCode;
	}

	@Column(name ="ACC_GROUP_NAME",length=128)
	public String getAccGroupName() {
		return accGroupName;
	}

	public void setAccGroupName(String accGroupName) {
		this.accGroupName = accGroupName;
	}

//	@Column(name ="OPERATION_AREA_ID",length=32)
//	public String getOperationAreaId() {
//		return operationAreaId;
//	}
//
//	public void setOperationAreaId(String operationAreaId) {
//		this.operationAreaId = operationAreaId;
//	}

	@Column(name ="CREATE_BY",nullable=true,length=32)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
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

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=32)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}

	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
}
