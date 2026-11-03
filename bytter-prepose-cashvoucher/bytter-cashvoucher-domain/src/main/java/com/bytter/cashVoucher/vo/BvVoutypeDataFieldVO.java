package com.bytter.cashVoucher.vo;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.enums.CommonDictEnum;
/**
 * @Title:VO
 * @Description:凭证类型取值字段明细VO
 * @Author:kongq
 * @Date:2017-06-12 9:29:30
 * @Version:1.0
 */
@SuppressWarnings("serial")
public class BvVoutypeDataFieldVO implements java.io.Serializable {
	private String id;
	/** 凭证种类表ID */
	@BtCompositeSearch(fieldName = "vou_Tid")
	private String vouTid;
	/** 取数字段代码 */
	@BtCompositeSearch(fieldName = "field_code")
	private String fieldCode;
	/** 取数字段名称 */
	@BtCompositeSearch(fieldName = "field_name")
	private String fieldName;
	/**数据类型：0无，1金额，2	银行账户，3银行大类 */
	@Dict(tableName="sys_dict_group",valueFieldName="type_group_code",showFiledName="id",inject="dictGroupId")
	private String dictGroup;
	/**数据类型：0无，1金额，2	银行账户，3银行大类 */
	@Dict(tableName="sys_dict_group",valueFieldName="id",showFiledName="type_group_name",inject="dictGroupName")
	private String dictGroupId;
	@BtCompositeSearch(fieldName ="dict_group")
	private String dictGroupName;
	/**数据类型：0无，1金额，2	银行账户，3银行大类 */
	@Dict(dictGroup = "bv_voutype_field_datatype", inject = "dataTypeName")
	private String dataType;
	@BtCompositeSearch(fieldName ="data_type")
	private String dataTypeName;
	/** 打印列表是否显示：0否，1是 */
	@Dict(dictGroup = "sf_yn", inject = "isShowName")
	private String isShow;
	@BtCompositeSearch(fieldName = "is_show")
	private String isShowName;
	/** 有效标志 */
	@Dict(dictGroup = "sf_yn", inject = "validSignName")
	private String validSign;
	@BtCompositeSearch(fieldName = "valid_Sign")
	private String validSignName;
	/** 排序字段 */
	private Integer ordernum;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVouTid() {
		return vouTid;
	}
	public void setVouTid(String vouTid) {
		this.vouTid = vouTid;
	}
	public String getFieldCode() {
		return fieldCode;
	}
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataTypeName() {
		return dataTypeName;
	}
	public void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getIsShowName() {
		if (ObjectUtil.isNotEmpty(validSign)) {
			return CommonDictEnum.getDictValue("sf_yn", isShow);
		}
		return isShowName;
	}
	public void setIsShowName(String isShowName) {
		this.isShowName = isShowName;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getValidSignName() {
		if (ObjectUtil.isNotEmpty(validSign)) {
			return CommonDictEnum.getDictValue("sf_yn", validSign);
		}
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	public Integer getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}
	public String getDictGroup() {
		return dictGroup;
	}
	public void setDictGroup(String dictGroup) {
		this.dictGroup = dictGroup;
	}
	public String getDictGroupName() {
		return dictGroupName;
	}
	public void setDictGroupName(String dictGroupName) {
		this.dictGroupName = dictGroupName;
	}
	public String getDictGroupId() {
		return dictGroupId;
	}
	public void setDictGroupId(String dictGroupId) {
		this.dictGroupId = dictGroupId;
	}
	@Override
	public String toString() {
		return "BvVoutypeDataFieldVO [id=" + id + ", vouTid=" + vouTid + ", fieldCode=" + fieldCode + ", fieldName="
				+ fieldName + ", dictGroup=" + dictGroup + ", dictGroupName=" + dictGroupName + ", dataType=" + dataType
				+ ", dataTypeName=" + dataTypeName + ", isShow=" + isShow + ", isShowName=" + isShowName
				+ ", validSign=" + validSign + ", validSignName=" + validSignName + ", ordernum=" + ordernum + "]";
	}
}
