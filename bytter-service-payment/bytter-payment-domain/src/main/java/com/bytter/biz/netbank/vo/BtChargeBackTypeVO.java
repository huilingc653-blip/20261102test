package com.bytter.biz.netbank.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 退单类型VO
 * @author onlineGenerator
 * @date 2017-05-23 15:20:33
 * @version 1.0
 *
 */
@ApiModel("退单类型VO")
public class BtChargeBackTypeVO implements Serializable {
	private static final long serialVersionUID = 3650731181127660208L;
	@ApiModelProperty("ID")
	@Dict(tableName="BT_CHARGE_BACK_TYPE",valueFieldName="id",showFiledName="BUS_TYPE_NAME",inject="busTypeName")
	private String id;

	/**退单类型编码*/
	@BtCompositeSearch(fieldName = "b.bus_type")
	@ApiModelProperty("退单类型编码")
	private java.lang.String busType;

	/**退单类型名称*/
	@BtCompositeSearch(fieldName = "b.bus_type_name")
	@ApiModelProperty("退单类型名称")
	private java.lang.String busTypeName;

	@ApiModelProperty("退单类型显示名称")
	private java.lang.String busTypeNameShow;

	/**是否默认*/
	@ApiModelProperty("是否默认")
	@Dict(dictGroup = "sf_yn",inject = "isDefaultName")
    @BtCompositeSearch(fieldName = "b.is_default")
	private java.lang.String isDefault;
	private java.lang.String isDefaultName;

	/**有效标志*/
	@ApiModelProperty("有效标志")
    @BtCompositeSearch(fieldName = "b.valid_sign")
	@Dict(dictGroup = "valid_sign",inject = "validSignName")
	private java.lang.String validSign;
	private java.lang.String validSignName;
	/**备注*/
	@ApiModelProperty("备注")
    @BtCompositeSearch(fieldName = "b.rmk")
	private java.lang.String rmk;

	/**创建人登录名称*/
	@ApiModelProperty("创建人登录名称")
	private java.lang.String createBy;

	/**创建日期*/
	@ApiModelProperty("创建日期")
	private java.util.Date createDate;

	/**更新人登录名称*/
	@ApiModelProperty("更新人登录名称")
	private java.lang.String updateBy;

	/**更新日期*/
	@ApiModelProperty("更新日期")
	private java.util.Date updateDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getBusTypeName() {
		return busTypeName;
	}
	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}
	public String getBusTypeNameShow() {
		return busTypeNameShow;
	}
	public void setBusTypeNameShow(String busTypeNameShow) {
		this.busTypeNameShow = busTypeNameShow;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
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

	public String getIsDefaultName() {
		return isDefaultName;
	}

	public void setIsDefaultName(String isDefaultName) {
		this.isDefaultName = isDefaultName;
	}

	public String getValidSignName() {
		return validSignName;
	}

	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
}
