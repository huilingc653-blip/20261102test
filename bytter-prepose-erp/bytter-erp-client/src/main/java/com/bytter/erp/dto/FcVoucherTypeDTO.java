package com.bytter.erp.dto;
import java.io.Serializable;
public class FcVoucherTypeDTO implements Serializable {
	private String id;
	/**付款方式编码*/
	private String voucherType;
	/**付款方式名称*/
	private String voucherName;
	/**系统结算方式*/
	private String sysVoucherCode;
	/**现金转账标志*/
	private String cashTransSign;
	/**结算类型*/
	private String settlType;
	/**是否走银企接口*/
	private String isElectronPay;
	/**是否记账*/
	private String isAccount;
	/**是否挂资金计划*/
	private String isBudget;
	/**指令发送模式*/
	private String sendMode;
	/**备注*/
	private String rmk;
	/**是否系统付款方式*/
	private String isSystem;
	/**有效标志*/
	private String validSign;
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
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getSysVoucherCode() {
		return sysVoucherCode;
	}
	public void setSysVoucherCode(String sysVoucherCode) {
		this.sysVoucherCode = sysVoucherCode;
	}
	public String getCashTransSign() {
		return cashTransSign;
	}
	public void setCashTransSign(String cashTransSign) {
		this.cashTransSign = cashTransSign;
	}
	public String getSettlType() {
		return settlType;
	}
	public void setSettlType(String settlType) {
		this.settlType = settlType;
	}
	public String getIsElectronPay() {
		return isElectronPay;
	}
	public void setIsElectronPay(String isElectronPay) {
		this.isElectronPay = isElectronPay;
	}
	public String getIsAccount() {
		return isAccount;
	}
	public void setIsAccount(String isAccount) {
		this.isAccount = isAccount;
	}
	public String getIsBudget() {
		return isBudget;
	}
	public void setIsBudget(String isBudget) {
		this.isBudget = isBudget;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
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
	
	
}
