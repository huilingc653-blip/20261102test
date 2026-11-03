package com.bytter.biz.payment.api.dto;


import java.io.Serializable;

public class FcVoucherTypeDTO implements Serializable {

	private String id;

	/**付款方式编码*/
	private java.lang.String voucherType;

	/**付款方式名称*/
	private java.lang.String voucherName;

	/**系统结算方式*/
	private java.lang.String sysVoucherCode;

	/**现金转账标志*/
	private java.lang.String cashTransSign;

	/**结算类型*/
	private java.lang.String settlType;

	/**是否走银企接口*/
	private java.lang.String isElectronPay;

	/**是否记账*/
	private java.lang.String isAccount;

	/**是否挂资金计划*/
	private java.lang.String isBudget;

	/**指令发送模式*/
	private java.lang.String sendMode;

	/**备注*/
	private java.lang.String rmk;

	/**是否系统付款方式*/
	private java.lang.String isSystem;

	/**有效标志*/
	private java.lang.String validSign;

	/**创建人登录名称*/
	private java.lang.String createBy;

	/**创建日期*/
	private java.util.Date createDate;

	/**更新人登录名称*/
	private java.lang.String updateBy;

	/**更新日期*/
	private java.util.Date updateDate;

	/**开票区间控制标识 1-三个月 2-六个月*/
	private java.lang.String billSectionControl;

	public String getBillSectionControl() {
		return billSectionControl;
	}

	public void setBillSectionControl(String billSectionControl) {
		this.billSectionControl = billSectionControl;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(java.lang.String voucherType) {
		this.voucherType = voucherType;
	}
	public java.lang.String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(java.lang.String voucherName) {
		this.voucherName = voucherName;
	}
	public java.lang.String getSysVoucherCode() {
		return sysVoucherCode;
	}
	public void setSysVoucherCode(java.lang.String sysVoucherCode) {
		this.sysVoucherCode = sysVoucherCode;
	}
	public java.lang.String getCashTransSign() {
		return cashTransSign;
	}
	public void setCashTransSign(java.lang.String cashTransSign) {
		this.cashTransSign = cashTransSign;
	}
	public java.lang.String getSettlType() {
		return settlType;
	}
	public void setSettlType(java.lang.String settlType) {
		this.settlType = settlType;
	}
	public java.lang.String getIsElectronPay() {
		return isElectronPay;
	}
	public void setIsElectronPay(java.lang.String isElectronPay) {
		this.isElectronPay = isElectronPay;
	}
	public java.lang.String getIsAccount() {
		return isAccount;
	}
	public void setIsAccount(java.lang.String isAccount) {
		this.isAccount = isAccount;
	}
	public java.lang.String getIsBudget() {
		return isBudget;
	}
	public void setIsBudget(java.lang.String isBudget) {
		this.isBudget = isBudget;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public java.lang.String getRmk() {
		return rmk;
	}
	public void setRmk(java.lang.String rmk) {
		this.rmk = rmk;
	}
	public java.lang.String getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(java.lang.String isSystem) {
		this.isSystem = isSystem;
	}
	public java.lang.String getValidSign() {
		return validSign;
	}
	public void setValidSign(java.lang.String validSign) {
		this.validSign = validSign;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
}
