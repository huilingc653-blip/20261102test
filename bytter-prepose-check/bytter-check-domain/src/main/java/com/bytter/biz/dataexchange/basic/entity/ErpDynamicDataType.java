package com.bytter.biz.dataexchange.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import org.apache.commons.lang.StringUtils;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 自定义数据类型
 * @author zhengg
 * @date 2018-04-19 11:24:27
 * @version 1.0
 *
 */
@Entity
@Table(name = "ERP_DYNAMIC_DATA_TYPE")
@SuppressWarnings("serial")
public class ErpDynamicDataType extends IdEntity implements java.io.Serializable {
	
	/** 数据类型代码 */
	private String dataTypeCode;
	/** 数据类型名称 */
	private String dataTypeName;
	
	/** 创建时间 */
	private Date createDate;
	/** 创建人 */
	private String createBy;
	/** 修改时间 */
	private Date updateDate;
	/** 修改人 */
	private String updateBy;
	
	/** 显示名称 **/
	private String showName;
	/** 初始化序号 **/
	private Integer sort;
	@Column(name ="sort")
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	@Column(name ="DATA_TYPE_CODE")
	public String getDataTypeCode() {
		return dataTypeCode;
	}
	public void setDataTypeCode(String dataTypeCode) {
		this.dataTypeCode = dataTypeCode;
		setShowName(dataTypeCode, null);
	}
	@Column(name ="DATA_TYPE_NAME")
	public String getDataTypeName() {
		return dataTypeName;
	}
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
		setShowName(null, dataTypeName);
	}
	@Column(name ="CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name ="CREATE_BY")
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Column(name ="UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(name ="UPDATE_BY")
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	
	@Transient
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
	@Override
	public String toString() {
		return "ErpDynamicDataType [dataTypeCode=" + dataTypeCode + ", dataTypeName=" + dataTypeName + ", createDate="
				+ createDate + ", createBy=" + createBy + ", updateDate=" + updateDate + ", updateBy=" + updateBy
				+ ", showName=" + showName + ", getDataTypeCode()=" + getDataTypeCode() + ", getDataTypeName()="
				+ getDataTypeName() + ", getCreateDate()=" + getCreateDate() + ", getCreateBy()=" + getCreateBy()
				+ ", getUpdateDate()=" + getUpdateDate() + ", getUpdateBy()=" + getUpdateBy() + ", getShowName()="
				+ getShowName() + "]";
	}
	
}
