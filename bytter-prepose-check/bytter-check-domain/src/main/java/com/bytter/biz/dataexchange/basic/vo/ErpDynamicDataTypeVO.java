package com.bytter.biz.dataexchange.basic.vo;
import com.bytter.core.base.entity.IdEntity;
import org.apache.commons.lang.StringUtils;
/**
 * @Title: VO
 * @Description: 自定义数据类型VO
 * @author zhengg
 * @date 2018-04-19 11:24:27
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class ErpDynamicDataTypeVO extends IdEntity implements java.io.Serializable {
	public ErpDynamicDataTypeVO(){}
	public ErpDynamicDataTypeVO(String dataTypeCode,String dataTypeName){
		this.dataTypeCode=dataTypeCode;
		this.dataTypeName=dataTypeName;
	}
	public ErpDynamicDataTypeVO(String id,String dataTypeCode,String dataTypeName){
		this.id=id;
		this.dataTypeCode=dataTypeCode;
		this.dataTypeName=dataTypeName;
	}
	
	private String id;
	/** 数据类型代码 */
	private String dataTypeCode;
	/** 数据类型名称 */
	private String dataTypeName;
	/** 显示名称 **/
	private String showName;
	public String getDataTypeCode() {
		return dataTypeCode;
	}
	public void setDataTypeCode(String dataTypeCode) {
		this.dataTypeCode = dataTypeCode;
		setShowName(dataTypeCode, null);
	}
	public String getDataTypeName() {
		return dataTypeName;
	}
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
		setShowName(null, dataTypeName);
	}
	
	public String getShowName() {
		return showName;
	}
	
	private void setShowName(String typeCode,String typeName) {
		String showName=getShowName();
		if(StringUtils.isEmpty(showName)){
			showName="";
		}
		if(StringUtils.isNotEmpty(typeCode)){
			setShowName("("+typeCode+")"+showName);
		}
		if(StringUtils.isNotEmpty(typeName)){
			setShowName(showName+typeName);
		}
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "ErpDynamicDataTypeVO [id=" + id + ", dataTypeCode=" + dataTypeCode + ", dataTypeName=" + dataTypeName
				+ ", showName=" + showName + ", getDataTypeCode()=" + getDataTypeCode() + ", getDataTypeName()="
				+ getDataTypeName() + ", getShowName()=" + getShowName() + ", getId()=" + getId() + "]";
	}
}
