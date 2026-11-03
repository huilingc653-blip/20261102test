package com.bytter.bill.domain.billPool.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.io.Serializable;
import java.util.List;
public class AbPoolBusTypeVO implements Serializable{
	 private String id;
	
	 /**
     * 业务类型代码
     */
	private String bustypeCode;
	
	 /**
     * 业务类型名称
     */
	@BtCompositeSearch(fieldName="ap.bustype_name")
	@JsonAlias({"busTypeName"})
	private String bustypeName;
	
	
	/**
     * 上一级别业务名称
     */
	private String parentBusName;
	
	 /**
     * 上级业务类型
     */
	private String parentId;
	
	  /**
     * 状态：1启用、0禁用 默认禁用
     */
	@BtCompositeSearch(fieldName="ct.valid_Sign")
	@Dict(dictGroup = "AB_POLL_VALID_SIGN", inject = "validSignName")
	private String validSign;
	private String validSignName;
	
	 /**
     * 层级
     */
	private Integer bustypeLevel;
	
	/**
     * 创建人id
     */
    private String createId;
    /**
     * 创建日期
     */
    private java.util.Date createTime;
	 /**
     * 更新人id
     */
    private String updateId;
    
    private List<AbPoolBusTypeVO> minRowList; 
    
    private List<AbPoolBusTypeVO> children;
    
    
    /**
     * 更新日期
     */
    private java.util.Date updateTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBustypeCode() {
		return bustypeCode;
	}
	public void setBustypeCode(String bustypeCode) {
		this.bustypeCode = bustypeCode;
	}
	public String getBustypeName() {
		return bustypeName;
	}
	public void setBustypeName(String bustypeName) {
		this.bustypeName = bustypeName;
	}
	public void setBusTypeName(String busTypeName) {
		this.bustypeName = busTypeName;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getValidSignName() {
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	public String getParentBusName() {
		return parentBusName;
	}
	public void setParentBusName(String parentBusName) {
		this.parentBusName = parentBusName;
	}
	public Integer getBustypeLevel() {
		return bustypeLevel;
	}
	public void setBustypeLevel(Integer bustypeLevel) {
		this.bustypeLevel = bustypeLevel;
	}
	public List<AbPoolBusTypeVO> getMinRowList() {
		return minRowList;
	}
	public void setMinRowList(List<AbPoolBusTypeVO> minRowList) {
		this.minRowList = minRowList;
	}
	public List<AbPoolBusTypeVO> getChildren() {
		return children;
	}
	public void setChildren(List<AbPoolBusTypeVO> children) {
		this.children = children;
	}
	
}
