package com.bytter.check.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: FC_COMPARE_CHECK_TYPE
 * @author onlineGenerator
 * @date 2018-01-03 14:14:11
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_COMPARE_CHECK_TYPE", schema = "")
@SuppressWarnings("serial")
public class FcCompareCheckType extends IdEntity implements java.io.Serializable {
	/**核对标准名称*/
	private String checkTypeName;
	/**排序*/
	private Integer sort;
	/**启用标志*/
	private String validSign;
	/**状态*/
	private Integer status;
	/**updateBy*/
	private String updateBy;
	/**updateTime*/
	private Date updateTime;
	/**createBy*/
	private String createBy;
	/**createTime*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date createTime;
	
	private String checkTypes;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  核对标准名称
	 */
	@Column(name ="CHECK_TYPE_NAME")
	public String getCheckTypeName(){
		return this.checkTypeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  核对标准名称
	 */
	public void setCheckTypeName(String checkTypeName){
		this.checkTypeName = checkTypeName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  排序
	 */
	@Column(name ="SORT")
	public Integer getSort(){
		return this.sort;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  排序
	 */
	public void setSort(Integer sort){
		this.sort = sort;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  启用标志
	 */
	@Column(name ="VALID_SIGN")
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  启用标志
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS")
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  updateBy
	 */
	@Column(name ="UPDATE_BY")
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  updateBy
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  updateTime
	 */
	@Column(name ="UPDATE_TIME")
	public Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  updateTime
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  createBy
	 */
	@Column(name ="CREATE_BY")
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  createBy
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  createTime
	 */
	@Column(name ="CREATE_TIME")
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  createTime
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	@Transient
	public String getCheckTypes() {
		return checkTypes;
	}
	public void setCheckTypes(String checkTypes) {
		this.checkTypes = checkTypes;
	}
	
	
}
