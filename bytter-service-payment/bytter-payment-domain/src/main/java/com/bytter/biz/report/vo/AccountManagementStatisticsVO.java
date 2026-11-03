package com.bytter.biz.report.vo;

import com.bytter.core.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description:账户管理统计表
 * @author jiangw
 * @date 2024.09.11
 * @version 1.0
 *
 */
public class AccountManagementStatisticsVO implements java.io.Serializable {

	private static final long serialVersionUID = 6490814688726935044L;

	/**
	 * 区域
	 */
	private String areaName;
	/**
	 * 省区
	 */
	private String sysProvinceName;
	/**
	 * 单位ID
	 */
	private String corpId;
	private String corpName;
	/**
	 *股权属性
	 */
	private String equityMaintenanceId;
	private String equityMaintenanceName;
	/**
	 * 账户组
	 */
	private String fcAccGroupCode;
	private String fcAccGroupName;
	/**
	 * 户名
	 */
	private String accName;
	/**
	 * 银行类别
	 */
	private String bankTypeId;
	private String bankTypeName;
	/**
	 * 开户行名称
	 */
	private String bankName;
	/**
	 * 银行账号
	 */
	private String bankAcc;
	/**
	 * 所在省
	 */
	private String prov;
	private String provName;
	/**
	 * 所在市
	 */
	private String city;
	private String cityName;
	/**
	 * 是否上线
	 */
	private String isOnline;
	private String isOnlineStr;
	/**
	 * 账户用途
	 */
	private String accountPurpose;
	/**
	 * 状态
	 */
	private Integer status;
	private String statusStr;
	/**
	 * 绑定账号
	 **/
	private String bindingBankAcc;
	/**
	 * 绑定账号银行类型名称
	 **/
	private String bindingBankTypeName;
	/**
	 * 实时余额
	 */
	private BigDecimal realTimeBal;

	/**
	 * 绑定人名称
	 **/
	private String bindingByName;
	/**
	 * 绑定人手机
	 **/
	private String bindingByMobile;
	/**
	 * 登录密码
	 **/
	private String loginPassword;
	/**
	 * 支付密码
	 **/
	private String payPassword;
	/**
	 * 绑定账户性质
	 **/
	private String bindingNatureId;
	private String bindingNatureName;
	/**
	 * 绑定账户用户组
	 */
	private String bindingAccGroupCode;

	private String corpParentIds;
	// 是否绑定第三方账号
	private String isBindThreeAcc;
	// 账户用途
	private String accUseCode;
	private String accUseName;

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getEquityMaintenanceId() {
		return equityMaintenanceId;
	}

	public void setEquityMaintenanceId(String equityMaintenanceId) {
		this.equityMaintenanceId = equityMaintenanceId;
	}

	public String getEquityMaintenanceName() {
		return equityMaintenanceName;
	}

	public void setEquityMaintenanceName(String equityMaintenanceName) {
		this.equityMaintenanceName = equityMaintenanceName;
	}

	public String getFcAccGroupCode() {
		return fcAccGroupCode;
	}

	public void setFcAccGroupCode(String fcAccGroupCode) {
		this.fcAccGroupCode = fcAccGroupCode;
	}

	public String getFcAccGroupName() {
		return fcAccGroupName;
	}

	public void setFcAccGroupName(String fcAccGroupName) {
		this.fcAccGroupName = fcAccGroupName;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getBankTypeId() {
		return bankTypeId;
	}

	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}

	public String getBankTypeName() {
		return bankTypeName;
	}

	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAcc() {
		return bankAcc;
	}

	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getProvName() {
		return provName;
	}

	public void setProvName(String provName) {
		this.provName = provName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public String getIsOnlineStr() {
		return isOnlineStr;
	}

	public void setIsOnlineStr(String isOnlineStr) {
		this.isOnlineStr = isOnlineStr;
	}

	public String getAccountPurpose() {
		return accountPurpose;
	}

	public void setAccountPurpose(String accountPurpose) {
		this.accountPurpose = accountPurpose;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getBindingBankAcc() {
		return bindingBankAcc;
	}

	public void setBindingBankAcc(String bindingBankAcc) {
		this.bindingBankAcc = bindingBankAcc;
	}

	public BigDecimal getRealTimeBal() {
		return realTimeBal;
	}

	public void setRealTimeBal(BigDecimal realTimeBal) {
		this.realTimeBal = realTimeBal;
	}

	public String getBindingBankTypeName() {
		return bindingBankTypeName;
	}

	public void setBindingBankTypeName(String bindingBankTypeName) {
		this.bindingBankTypeName = bindingBankTypeName;
	}

	public String getBindingByName() {
		return bindingByName;
	}

	public void setBindingByName(String bindingByName) {
		this.bindingByName = bindingByName;
	}

	public String getBindingByMobile() {
		return bindingByMobile;
	}

	public void setBindingByMobile(String bindingByMobile) {
		this.bindingByMobile = bindingByMobile;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getBindingNatureId() {
		return bindingNatureId;
	}

	public void setBindingNatureId(String bindingNatureId) {
		this.bindingNatureId = bindingNatureId;
	}

	public String getBindingNatureName() {
		return bindingNatureName;
	}

	public void setBindingNatureName(String bindingNatureName) {
		this.bindingNatureName = bindingNatureName;
	}

	public String getBindingAccGroupCode() {
		return bindingAccGroupCode;
	}

	public void setBindingAccGroupCode(String bindingAccGroupCode) {
		this.bindingAccGroupCode = bindingAccGroupCode;
	}

	public String getCorpParentIds() {
		return corpParentIds;
	}

	public void setCorpParentIds(String corpParentIds) {
		this.corpParentIds = corpParentIds;
	}

	public String getSysProvinceName() {
		return sysProvinceName;
	}

	public void setSysProvinceName(String sysProvinceName) {
		this.sysProvinceName = sysProvinceName;
	}

	public String getIsBindThreeAcc() {
		return isBindThreeAcc;
	}

	public void setIsBindThreeAcc(String isBindThreeAcc) {
		this.isBindThreeAcc = isBindThreeAcc;
	}

	public String getAccUseCode() {
		return accUseCode;
	}

	public void setAccUseCode(String accUseCode) {
		this.accUseCode = accUseCode;
	}

	public String getAccUseName() {
		return accUseName;
	}

	public void setAccUseName(String accUseName) {
		this.accUseName = accUseName;
	}
}
