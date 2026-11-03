package com.bytter.credit.dto;

import java.io.Serializable;

/**
 * 授信录入主表DTO
 *
 * @author liyh
 *
 */
public class RatBkrationDTO implements Serializable {

	/** 网点编号 */
	private String netCode;
	/** 单位代码 */
	private String corpId;
	/** 授信种类 */
	private String ratType;
	/** 银行编号 */
	private String bankCode;

	/**
	 * 银行类别
	 */
	private String bankType;
	/** 敞口授信 */
	private String openSign;
	/** 授信模式 */
	private String ratMode;
	/** 授信币种 */
	private String curCode;
	/** 授信开始时间 */
	private java.util.Date begindate;
	/** 授信结束时间 */
	private java.util.Date enddate;
	/** 授信金额 */
	private java.math.BigDecimal money;
	/** 额度类型 */
	private String type;
	/** 通过时间 */
	private java.util.Date passDate;
	/** 备注 */
	private String remark;
	/** 状态 */
	private Integer status;
	/** 授信合同号 */
	private String contractNo;
	/** 额度是否包含保证金 */
	private String includeBailmoney;

	public String getNetCode() {
		return netCode;
	}

	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getRatType() {
		return ratType;
	}

	public void setRatType(String ratType) {
		this.ratType = ratType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getOpenSign() {
		return openSign;
	}

	public void setOpenSign(String openSign) {
		this.openSign = openSign;
	}

	public String getRatMode() {
		return ratMode;
	}

	public void setRatMode(String ratMode) {
		this.ratMode = ratMode;
	}

	public String getCurCode() {
		return curCode;
	}

	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}

	public java.util.Date getBegindate() {
		return begindate;
	}

	public void setBegindate(java.util.Date begindate) {
		this.begindate = begindate;
	}

	public java.util.Date getEnddate() {
		return enddate;
	}

	public void setEnddate(java.util.Date enddate) {
		this.enddate = enddate;
	}

	public java.math.BigDecimal getMoney() {
		return money;
	}

	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public java.util.Date getPassDate() {
		return passDate;
	}

	public void setPassDate(java.util.Date passDate) {
		this.passDate = passDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getIncludeBailmoney() {
		return includeBailmoney;
	}

	public void setIncludeBailmoney(String includeBailmoney) {
		this.includeBailmoney = includeBailmoney;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

}
