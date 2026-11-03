package com.bytter.check.vo;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcCompareCheckTypeVO implements Serializable {
	
	private java.lang.String id;
	/**核对标准名称*/
	private java.lang.String checkTypeName;
	/**排序*/
	private java.lang.Integer sort;
	/**启用标志*/
	private java.lang.String validSign;
	/**状态*/
	private java.lang.Integer status;
	/**updateBy*/
	private java.lang.String updateBy;
	/**updateTime*/
	private java.util.Date updateTime;
	/**createBy*/
	private java.lang.String createBy;
	/**createTime*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date createTime;
	
	private java.lang.String checkTypes;
	private java.lang.String checkTypesName;
	
	
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getCheckTypeName() {
		return checkTypeName;
	}
	public void setCheckTypeName(java.lang.String checkTypeName) {
		this.checkTypeName = checkTypeName;
	}
	public java.lang.Integer getSort() {
		return sort;
	}
	public void setSort(java.lang.Integer sort) {
		this.sort = sort;
	}
	public java.lang.String getValidSign() {
		return validSign;
	}
	public void setValidSign(java.lang.String validSign) {
		this.validSign = validSign;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.lang.String getCheckTypes() {
		return checkTypes;
	}
	public void setCheckTypes(java.lang.String checkTypes) {
		this.checkTypes = checkTypes;
	}
	public java.lang.String getCheckTypesName() {
		return checkTypesName;
	}
	public void setCheckTypesName(java.lang.String checkTypesName) {
		this.checkTypesName = checkTypesName;
	}
}
