package com.bytter.biz.ttpay.vo;
import com.bytter.core.utils.StringUtils;
/**
 * @Title: VO
 * @Description: 各支付方式对账单字段和表信息VO
 * @author 郑刚
 * @date 2019-09-30 15:43:05
 * @version 1.0
 */
public class TpPayBillViewDtlVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String dataKey;
	
	/**第三方支付方式**/
	private String payType;
	/**支付方式对应表**/
	private String tableCode;
	/**支付方式显示名称**/
	private String payShowName;
	/**字段代码**/
	private String fieldCode;
	/**字段显示名称**/
	private String fieldName;
	/**数据类型:string字符串number数字money金额rate利率date日期time日期加时间**/
	private String dataType;
	
	/**统一显示时对应VO实体属性**/
	private String voAttr;
	/**字段用途：1列表2查看明细3合计多个用,隔开**/
	private String fieldUse;
	
	private String dictType;
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	public String getPayShowName() {
		return payShowName;
	}
	public void setPayShowName(String payShowName) {
		this.payShowName = payShowName;
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
	public String getVoAttr() {
		return voAttr;
	}
	public void setVoAttr(String voAttr) {
		this.voAttr = voAttr;
	}
	public String getFieldUse() {
		return fieldUse;
	}
	public void setFieldUse(String fieldUse) {
		this.fieldUse = fieldUse;
	}
	public String getDataKey() {
		if(StringUtils.isNotEmpty(getFieldCode())
				&&StringUtils.isEmpty(dataKey)){
			setDataKey(getFieldCode().toLowerCase());
		}
		return dataKey;
	}
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
	public String getDictType() {
		return dictType;
	}
	public void setDictType(String dictType) {
		this.dictType = dictType;
	}
	@Override
	public String toString() {
		return "TpPayBillViewDtl [dataKey=" + dataKey + ", payType=" + payType + ", tableCode=" + tableCode
				+ ", payShowName=" + payShowName + ", fieldCode=" + fieldCode + ", fieldName=" + fieldName
				+ ", dataType=" + dataType + ", voAttr=" + voAttr + ", fieldUse=" + fieldUse + ", dictType=" + dictType
				+ ", getPayType()=" + getPayType() + ", getTableCode()=" + getTableCode() + ", getPayShowName()="
				+ getPayShowName() + ", getFieldCode()=" + getFieldCode() + ", getFieldName()=" + getFieldName()
				+ ", getDataType()=" + getDataType() + ", getVoAttr()=" + getVoAttr() + ", getFieldUse()="
				+ getFieldUse() + ", getDataKey()=" + getDataKey() + ", getDictType()=" + getDictType() + "]";
	}
	
}
