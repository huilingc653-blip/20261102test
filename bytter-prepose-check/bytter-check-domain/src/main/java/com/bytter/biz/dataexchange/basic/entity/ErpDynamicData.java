package com.bytter.biz.dataexchange.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 自定义数据映射
 * @author zhengg
 * @date 2018-04-19 11:24:27
 * @version 1.0
 *
 */
@Entity
@Table(name = "ERP_DYNAMIC_DATA")
@SuppressWarnings("serial")
public class ErpDynamicData extends IdEntity implements java.io.Serializable {
	/** 接口类型 */
	private String typeCode;
	/** 数据类型代码 */
	private String dataTypeId;
	/** 内部系统代码 */
	private String innerCode;
	/** 内部系统名称*/
	private String innerName;
	/** 内部系统备用 */
	private String innerReserve;
	/** 外部系统代码 */
	private String externalCode;
	/** 外部系统名称 */
	private String externalName;
	/** 外部系统备用 */
	private String externalReserve;
	
	/** 创建时间 */
	private Date createDate;
	/** 创建人 */
	private String createBy;
	/** 修改时间 */
	private Date updateDate;
	/** 修改人 */
	private String updateBy;
	
	@Column(name ="TYPE_CODE")
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	@Column(name ="DATA_TYPE_ID")
	public String getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	@Column(name ="INNER_CODE")
	public String getInnerCode() {
		return innerCode;
	}
	public void setInnerCode(String innerCode) {
		this.innerCode = innerCode;
	}
	@Column(name ="INNER_NAME")
	public String getInnerName() {
		return innerName;
	}
	public void setInnerName(String innerName) {
		this.innerName = innerName;
	}
	@Column(name ="INNER_RESERVE")
	public String getInnerReserve() {
		return innerReserve;
	}
	public void setInnerReserve(String innerReserve) {
		this.innerReserve = innerReserve;
	}
	@Column(name ="EXTERNAL_CODE")
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	@Column(name ="EXTERNAL_NAME")
	public String getExternalName() {
		return externalName;
	}
	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}
	@Column(name ="EXTERNAL_RESERVE")
	public String getExternalReserve() {
		return externalReserve;
	}
	public void setExternalReserve(String externalReserve) {
		this.externalReserve = externalReserve;
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
	@Override
	public String toString() {
		return "ErpDynamicData [typeCode=" + typeCode + ", dataTypeId=" + dataTypeId  + ", innerCode=" + innerCode + ", innerName=" + innerName + ", innerReserve="
				+ innerReserve + ", externalCode=" + externalCode + ", externalName=" + externalName
				+ ", externalReserve=" + externalReserve + ", createDate=" + createDate + ", createBy=" + createBy
				+ ", updateDate=" + updateDate + ", updateBy=" + updateBy + ", getTypeCode()=" + getTypeCode()
				+ ", getDataTypeId()=" + getDataTypeId() 
				+ ", getInnerCode()=" + getInnerCode() + ", getInnerName()=" + getInnerName() + ", getInnerReserve()="
				+ getInnerReserve() + ", getExternalCode()=" + getExternalCode() + ", getExternalName()="
				+ getExternalName() + ", getExternalReserve()=" + getExternalReserve() + ", getCreateDate()="
				+ getCreateDate() + ", getCreateBy()=" + getCreateBy() + ", getUpdateDate()=" + getUpdateDate()
				+ ", getUpdateBy()=" + getUpdateBy() + "]";
	}
}
