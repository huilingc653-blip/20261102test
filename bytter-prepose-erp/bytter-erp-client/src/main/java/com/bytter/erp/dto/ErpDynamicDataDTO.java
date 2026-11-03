package com.bytter.erp.dto;
/**
 * 自定义数据映射DTO
 *
 * @author zuoy 2018-05-17
 */
@SuppressWarnings("serial")
public class ErpDynamicDataDTO implements java.io.Serializable {
	private String id;
	/**
	 * 接口类型
	 */
	private String typeCode;
	/**
	 * 接口名称
	 */
	private String typeName;
	/**
	 * 数据类型ID
	 */
	private String dataTypeId;
	/**
	 * 数据类型代码
	 */
	private String dataTypeCode;
	/**
	 * 数据类型名称
	 */
	private String dataTypeName;
	/**
	 * 内部系统代码
	 */
	private String innerCode;
	/**
	 * 内部系统名称
	 */
	private String innerName;
	/**
	 * 内部系统备用
	 */
	private String innerReserve;
	/**
	 * 外部系统代码
	 */
	private String externalCode;
	/**
	 * 外部系统名称
	 */
	private String externalName;
	/**
	 * 外部系统备用
	 */
	private String externalReserve;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	public String getDataTypeCode() {
		return dataTypeCode;
	}
	public void setDataTypeCode(String dataTypeCode) {
		this.dataTypeCode = dataTypeCode;
	}
	public String getDataTypeName() {
		return dataTypeName;
	}
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}
	public String getInnerCode() {
		return innerCode;
	}
	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}
	public String getInnerName() {
		return innerName;
	}
	public void setInnerName(String innerName) {
		this.innerName = innerName;
	}
	public String getInnerReserve() {
		return innerReserve;
	}
	public void setInnerReserve(String innerReserve) {
		this.innerReserve = innerReserve;
	}
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	public String getExternalName() {
		return externalName;
	}
	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}
	public String getExternalReserve() {
		return externalReserve;
	}
	public void setExternalReserve(String externalReserve) {
		this.externalReserve = externalReserve;
	}
}
