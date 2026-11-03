package com.bytter.cashVoucher.vo;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.enums.CommonDictEnum;
import com.bytter.cashVoucher.entity.BvVoucherType;
import com.bytter.cashVoucher.entity.BvVoutypeDataField;
import com.bytter.cashVoucher.entity.BvVoutypeScene;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
/**
 * @Title:VO
 * @Description:凭证类型VO
 * @author kongq
 * @date 2017-06-03 15:43:01
 * @version 1.0
 */
@SuppressWarnings("serial")
public class BvVoucherTypeVO implements java.io.Serializable {
	/** 凭证类型ID */
	// @Dict(tableName="BV_VOUCHER_TYPE",valueFieldName="id",showFiledName="name",inject="name")
	private String id;
	/** 凭证种类代码 */
	@BtCompositeSearch(fieldName = "code")
	private String code;
	/** 凭证种类名称 */
	@BtCompositeSearch(fieldName = "name")
	private String name;
	/** 是否入库标志 */
	// @Dict(dictGroup = "sf_yn", inject = "isInboxName")
	@BtCompositeSearch(fieldName = "is_Inbox")
	private String isInbox;
	@BtCompositeSearch(fieldName = "is_Inbox")
	private String isInboxName;
	/** 数据来源 */
	@BtCompositeSearch(fieldName = "data_Source")
	private String dataSource;
	/** 备注 */
	@BtCompositeSearch(fieldName = "rmk")
	private String rmk;
	/** 有效标志 */
	// @Dict(dictGroup = "sf_yn", inject = "validSignName")
	private String validSign;
	@BtCompositeSearch(fieldName = "valid_Sign")
	private String validSignName;
	/** 凭证类型取值字段表集合 */
	private List<BvVoutypeDataField> bvVoutypeDataFieldList;
	/** 凭证类型打印场景集合 */
	private List<BvVoutypeScene> bvVoutypeSceneList;
	/** 凭证类型 */
	 private BvVoucherType bvVoucherType;
	 private String locale;
	 public BvVoucherTypeVO(){
		 locale=LocaleContextHolder.getLocale().toString();
	 }
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIsInbox() {
		return isInbox;
	}
	public void setIsInbox(String isInbox) {
		this.isInbox = isInbox;
	}
	public String getIsInboxName() {
        if (ObjectUtil.isNotEmpty(isInbox)) {
            return CommonDictEnum.getDictValue("sf_yn", isInbox);
        }
		return isInboxName;
	}
	public void setIsInboxName(String isInboxName) {
		this.isInboxName = isInboxName;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
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
	public BvVoucherType getBvVoucherType() {
		return bvVoucherType;
	}
	public void setBvVoucherType(BvVoucherType bvVoucherType) {
		this.bvVoucherType = bvVoucherType;
	}
	public List<BvVoutypeDataField> getBvVoutypeDataFieldList() {
		return bvVoutypeDataFieldList;
	}
	public void setBvVoutypeDataFieldList(List<BvVoutypeDataField> bvVoutypeDataFieldList) {
		this.bvVoutypeDataFieldList = bvVoutypeDataFieldList;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public List<BvVoutypeScene> getBvVoutypeSceneList() {
		return bvVoutypeSceneList;
	}
	public void setBvVoutypeSceneList(List<BvVoutypeScene> bvVoutypeSceneList) {
		this.bvVoutypeSceneList = bvVoutypeSceneList;
	}
	@Override
	public String toString() {
		return "BvVoucherTypeVO [id=" + id + ", code=" + code + ", name=" + name + ", isInbox=" + isInbox
				+ ", isInboxName=" + isInboxName + ", dataSource=" + dataSource + ", rmk=" + rmk + ", validSign="
				+ validSign + ", validSignName=" + validSignName + ", bvVoutypeDataFieldList=" + bvVoutypeDataFieldList
				+ ", bvVoutypeSceneList=" + bvVoutypeSceneList + ", bvVoucherType=" + bvVoucherType + ", locale="
				+ locale + ", getId()=" + getId() + ", getCode()=" + getCode() + ", getName()=" + getName()
				+ ", getIsInbox()=" + getIsInbox() + ", getIsInboxName()=" + getIsInboxName() + ", getDataSource()="
				+ getDataSource() + ", getRmk()=" + getRmk() + ", getValidSign()=" + getValidSign()
				+ ", getValidSignName()=" + getValidSignName() + ", getBvVoucherType()=" + getBvVoucherType()
				+ ", getBvVoutypeDataFieldList()=" + getBvVoutypeDataFieldList() + ", getLocale()=" + getLocale()
				+ ", getBvVoutypeSceneList()=" + getBvVoutypeSceneList() + "]";
	}
}
