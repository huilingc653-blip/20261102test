package com.bytter.check.reconcile.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;

public class BisCheckReasonVO implements java.io.Serializable{
	private String id;
	/** 编码**/
    @BtCompositeSearch(fieldName="br.reason_code")
	private String reasonCode;
	/** 名称**/
    @BtCompositeSearch(fieldName="br.reason_name")
	private String reasonName;
	/** 银行类型ID**/
    @BtCompositeSearch(fieldName="br.bank_type_id")
	private String bankTypeId;
	/** 银行类型名称**/
	@BtCompositeSearch(fieldName="br.bank_type_id")
	private String bankTypeName; 
	
	/** 关键字**/
    @Dict(dictGroup = "BIS_CHECK_REASON_SCOPE", inject = "appScopeName", multi = true)
	private String appScope;
	@BtCompositeSearch(fieldName="br.app_scope")
	private String appScopeName;
	/**有效标志 1-有效 0-无效*/
    @Dict(dictGroup = "valid_sign", inject = "validSignName")
	private String validSign;
	@BtCompositeSearch(fieldName="br.valid_sign")
	private String validSignName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReasonName() {
		return reasonName;
	}
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getAppScope() {
		return appScope;
	}
	public void setAppScope(String appScope) {
		this.appScope = appScope;
	}
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getBankTypeName() {
		return bankTypeName;
	}
	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}
	public String getValidSignName() {
		return validSignName;
	}
	public void setValidSignName(String validSignName) {
		this.validSignName = validSignName;
	}
	public String getAppScopeName() {
		return appScopeName;
	}
	public void setAppScopeName(String appScopeName) {
		this.appScopeName = appScopeName;
	}
	
}
