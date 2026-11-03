package com.bytter.bill.domain.billbank.pool.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 额度操作信息VO
 * 
 */
public class PoolLimitVO extends PoolBaseVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 数据类型：1申请额度，2分配额度
	 */
	private String type;
	
	/**
	 * 申请客户机构号
	 */
	private String applyBankOrgCode;
	/**
	 * 申请单位名称
	 */
	private String applyCorpName;
	/**
	 * 对手客户机构号
	 */
	private String oppBankOrgCode;
	/**
	 * 对手单位名称
	 */
	private String oppCorpName;
	
	/**
	 * 额度业务代码
	 */
	private String bustypeCode;
	
	/**
	 * 额度业务代码明细
	 */
	private String bustypeDtlCode;
	
	/**
	 * 额度种类:1：低风险， 2：非低风险
	 */
	private String limitType;
	
	/**
	 * 交易申请日期
	 */
	private Date appDate;
	/**
	 * 额度结束日期
	 */
	private Date endDate;
	
	/**
	 * 操作额度
	 */
	private BigDecimal money;
	/**
	 * 核准类型：1通过0不通过
	 */
	private String checkType;
	
	/**
	 * 核准信息
	 */
	private String checkMsg;
	
	
	/**
	 * 保证金账号
	 */
	private String bailBankAcc;
	
	/**
	 * 备注
	 */
	private String rmk;
	/**
	 * 调剂额度
	 */
	private BigDecimal applyMoney;
	
	/**
	 * 调剂前额度
	 */
	private BigDecimal oldMoney;
	
	/**
	 * 调剂后额度
	 */
	private BigDecimal finalMoney;
	
	
	
	public String getApplyBankOrgCode() {
		return applyBankOrgCode;
	}
	public void setApplyBankOrgCode(String applyBankOrgCode) {
		this.applyBankOrgCode = applyBankOrgCode;
	}
	public String getOppBankOrgCode() {
		return oppBankOrgCode;
	}
	public void setOppBankOrgCode(String oppBankOrgCode) {
		this.oppBankOrgCode = oppBankOrgCode;
	}
	public String getBustypeCode() {
		return bustypeCode;
	}
	public void setBustypeCode(String bustypeCode) {
		this.bustypeCode = bustypeCode;
	}
	public String getLimitType() {
		return limitType;
	}
	public void setLimitType(String limitType) {
		this.limitType = limitType;
	}
	public Date getAppDate() {
		return appDate;
	}
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCheckMsg() {
		return checkMsg;
	}
	public void setCheckMsg(String checkMsg) {
		this.checkMsg = checkMsg;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getApplyCorpName() {
		return applyCorpName;
	}
	public void setApplyCorpName(String applyCorpName) {
		this.applyCorpName = applyCorpName;
	}
	public String getOppCorpName() {
		return oppCorpName;
	}
	public void setOppCorpName(String oppCorpName) {
		this.oppCorpName = oppCorpName;
	}
	public String getBustypeDtlCode() {
		return bustypeDtlCode;
	}
	public void setBustypeDtlCode(String bustypeDtlCode) {
		this.bustypeDtlCode = bustypeDtlCode;
	}
	public String getBailBankAcc() {
		return bailBankAcc;
	}
	public void setBailBankAcc(String bailBankAcc) {
		this.bailBankAcc = bailBankAcc;
	}
	public BigDecimal getApplyMoney() {
		return applyMoney;
	}
	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}
	public BigDecimal getOldMoney() {
		return oldMoney;
	}
	public void setOldMoney(BigDecimal oldMoney) {
		this.oldMoney = oldMoney;
	}
	public BigDecimal getFinalMoney() {
		return finalMoney;
	}
	public void setFinalMoney(BigDecimal finalMoney) {
		this.finalMoney = finalMoney;
	}
	@Override
	public String toString() {
		return "PoolLimitVO [type=" + type + ", applyBankOrgCode="
				+ applyBankOrgCode + ", applyCorpName=" + applyCorpName
				+ ", oppBankOrgCode=" + oppBankOrgCode + ", oppCorpName="
				+ oppCorpName + ", bustypeCode=" + bustypeCode
				+ ", bustypeDtlCode=" + bustypeDtlCode + ", limitType="
				+ limitType + ", appDate=" + appDate + ", endDate=" + endDate
				+ ", money=" + money + ", checkType=" + checkType
				+ ", checkMsg=" + checkMsg + ", bailBankAcc=" + bailBankAcc
				+ ", rmk=" + rmk + ", applyMoney=" + applyMoney + ", oldMoney="
				+ oldMoney + ", finalMoney=" + finalMoney+"],["+super.toString() + "]";
	}
	
}
