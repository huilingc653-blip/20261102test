package com.bytter.biz.finacial.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 手工账回写列值对应表
 * @author lvwl
 *
 */
@Entity
@Table(name = "FC_MANUAL_FINACIAL_COLUMN", schema = "")
@SuppressWarnings("serial")
public class FcManualFinacialColumn extends IdEntity implements java.io.Serializable {
	/**手工账类型表ID*/
	private String typeId;
	/**回写值类型 0常量 1程序提供选择*/
	private String writeBackValueType;
	/**回写数据表的表明*/
	private String writeBackTable;
	/**回写的字段名*/
	private String writeBackColumn;
	/**回写的数据对应的取值字段名*/
	private String writeBackValueColumn;
	/**与业务关联字段*/
	private String businessId;
	@Column(name ="TYPE_ID")
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	@Column(name ="WRITE_BACK_VALUE_TYPE")
	public String getWriteBackValueType() {
		return writeBackValueType;
	}
	public void setWriteBackValueType(String writeBackValueType) {
		this.writeBackValueType = writeBackValueType;
	}
	@Column(name ="WRITE_BACK_TABLE")
	public String getWriteBackTable() {
		return writeBackTable;
	}
	public void setWriteBackTable(String writeBackTable) {
		this.writeBackTable = writeBackTable;
	}
	@Column(name ="WRITE_BACK_COLUMN")
	public String getWriteBackColumn() {
		return writeBackColumn;
	}
	public void setWriteBackColumn(String writeBackColumn) {
		this.writeBackColumn = writeBackColumn;
	}
	@Column(name ="WRITE_BACK_VALUE_COLUMN")
	public String getWriteBackValueColumn() {
		return writeBackValueColumn;
	}
	public void setWriteBackValueColumn(String writeBackValueColumn) {
		this.writeBackValueColumn = writeBackValueColumn;
	}
	@Column(name ="BUSINESS_ID")
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
}
