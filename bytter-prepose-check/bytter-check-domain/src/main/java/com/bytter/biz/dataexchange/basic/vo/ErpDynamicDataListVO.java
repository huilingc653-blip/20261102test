package com.bytter.biz.dataexchange.basic.vo;
import com.bytter.core.base.entity.IdEntity;
import java.util.List;
/**
 * @Title: VO
 * @Description: 自定义数据映射集合VO
 * @author zhengg
 * @date 2018-04-19 11:24:27
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class ErpDynamicDataListVO extends IdEntity implements java.io.Serializable {
	/** 接口类型 */
	private String typeCode;
	
	/** 数据类型ID */
	private String dataTypeId;
	
	/** 数据类型代码 */
	private String dataTypeCode;
	/** 数据类型名称 */
	private String dataTypeName;
	
	private List<ErpDynamicDataVO> dataList;
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
	public List<ErpDynamicDataVO> getDataList() {
		return dataList;
	}
	public void setDataList(List<ErpDynamicDataVO> dataList) {
		this.dataList = dataList;
	}
	@Override
	public String toString() {
		return "ErpDynamicDataListVO [typeCode=" + typeCode + ", dataTypeId=" + dataTypeId + ", dataTypeCode="
				+ dataTypeCode + ", dataTypeName=" + dataTypeName + ", dataList=" + dataList + ", getTypeCode()="
				+ getTypeCode() + ", getDataTypeId()=" + getDataTypeId() + ", getDataTypeCode()=" + getDataTypeCode()
				+ ", getDataTypeName()=" + getDataTypeName() + ", getDataList()=" + getDataList() + "]";
	}
	
}
