package com.bytter.biz.netbank.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: Entity
 * @author onlineGenerator
 * @version 1.0
 *
 */
public class FcBankAccGroupVO extends ExcelModel implements Serializable {
	/**
	 * id
	 */
	private String id;

	/**
	 * 账户组编码
	 */
	@Excel(name = "*账户组编码")
	@BtCompositeSearch(fieldName = "g.accGroupCode")
	private String accGroupCode;

	/**
	 * 账户组名称
	 */
	@Excel(name = "*账户组名称")
	@BtCompositeSearch(fieldName = "g.accGroupName")
	private String accGroupName;

	@BtCompositeSearch(fieldName = "g.areaNames")
	private String areaNames;

	@Excel(name = "所属运营区编码")
	@BtCompositeSearch(fieldName = "g.areaCodes")
	private String areaCodes;

	/**
	 * 所属营运区ID
	 */
	@BtCompositeSearch(fieldName = "g.operationAreaIds")
	private String operationAreaIds;

	private java.lang.String createBy;
	/**
	 * 创建日期
	 */
	private java.util.Date createDate;
	/**
	 * 更新人登录名称
	 */
	private java.lang.String updateBy;
	/**
	 * 更新日期
	 */
	private java.util.Date updateDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccGroupCode() {
		return accGroupCode;
	}

	public void setAccGroupCode(String accGroupCode) {
		this.accGroupCode = accGroupCode;
	}

	public String getAccGroupName() {
		return accGroupName;
	}

	public void setAccGroupName(String accGroupName) {
		this.accGroupName = accGroupName;
	}

	public String getOperationAreaIds() {
		return operationAreaIds;
	}

	public void setOperationAreaIds(String operationAreaIds) {
		this.operationAreaIds = operationAreaIds;
	}

	public String getAreaNames() {
		return areaNames;
	}

	public void setAreaNames(String areaNames) {
		this.areaNames = areaNames;
	}

	public String getAreaCodes() {
		return areaCodes;
	}

	public void setAreaCodes(String areaCodes) {
		this.areaCodes = areaCodes;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getBeanName() {
		return "fcBankAccGroupService";
	}
}

