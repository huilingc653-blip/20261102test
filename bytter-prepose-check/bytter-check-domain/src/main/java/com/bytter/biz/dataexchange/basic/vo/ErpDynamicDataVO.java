package com.bytter.biz.dataexchange.basic.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import javax.validation.constraints.Size;
/**
 * @Title: VO
 * @Description: 自定义数据映射VO
 * @author zhengg
 * @date 2018-04-19 11:24:27
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class ErpDynamicDataVO extends ExcelModel implements java.io.Serializable {
	
	@Override
	public String getBeanName() {
		return "erpDynamicDataService";
	}
	private String id;
	
	/** 接口类型 */
	@Excel(name = "*接口类型代码",isImportField="true")
	@BtCompositeSearch(fieldName="edd.type_code")
	private String typeCode;
	
	/** 接口名称 */
	@Excel(name = "接口类型名称")
	private String typeName;
	
	/** 数据类型ID */
	@BtCompositeSearch(fieldName="edd.data_type_id")
	private String dataTypeId;
	
	/** 数据类型代码 */
	@Excel(name = "*数据类型代码",isImportField="true")
	private String dataTypeCode;
	/** 数据类型名称 */
	@Excel(name = "数据类型名称")
	private String dataTypeName;
	/** 内部系统代码 */
	@Excel(name = "*内部系统代码",isImportField="true")
	@BtCompositeSearch(fieldName="edd.inner_code")
	private String innerCode;
	
	/** 内部系统名称 */
	@Excel(name = "内部系统名称")
	@BtCompositeSearch(fieldName="edd.inner_name")
	private String innerName;
	/** 内部系统备用 */
	@Excel(name = "内部系统备注")
	private String innerReserve;
	/** 外部系统代码 */
	@Excel(name = "外部系统代码")
	@BtCompositeSearch(fieldName="edd.external_code")
	private String externalCode;
	/** 外部系统名称 */
	@Excel(name = "外部系统名称")
	@BtCompositeSearch(fieldName="edd.external_name")
	private String externalName;
	/** 外部系统备用 */
	@Excel(name = "外部系统备注")
	private String externalReserve;
	
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Override
	public String toString() {
		return "ErpDynamicDataVO [id=" + id + ", typeCode=" + typeCode + ", typeName=" + typeName + ", dataTypeId="
				+ dataTypeId + ", dataTypeCode=" + dataTypeCode + ", dataTypeName=" + dataTypeName + ", innerCode="
				+ innerCode + ", innerName=" + innerName + ", innerReserve=" + innerReserve + ", externalCode="
				+ externalCode + ", externalName=" + externalName + ", externalReserve=" + externalReserve
				+ ", getTypeCode()=" + getTypeCode() + ", getDataTypeId()=" + getDataTypeId() + ", getInnerCode()="
				+ getInnerCode() + ", getInnerName()=" + getInnerName() + ", getInnerReserve()=" + getInnerReserve()
				+ ", getExternalCode()=" + getExternalCode() + ", getExternalName()=" + getExternalName()
				+ ", getExternalReserve()=" + getExternalReserve() + ", getDataTypeCode()=" + getDataTypeCode()
				+ ", getDataTypeName()=" + getDataTypeName() + ", getId()=" + getId() + ", getTypeName()="
				+ getTypeName() + "]";
	}
}
