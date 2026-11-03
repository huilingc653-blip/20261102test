package com.bytter.report.domain.cms.vo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行额度统计VO
 *
 * @author yangl
 *
 */
public class RatBkStatisticVO implements Serializable {

	/** 单位代码 */
	private java.lang.String corpId;
	/** 单位代码 */
	@Excel(name="授信单位")
	private java.lang.String corpName;
	/**
	 * 单位代码
	 */
	private java.lang.String corpNo;
	/**
	 * 状态
	 */
	private java.lang.String status;
	/**
	 * 状态中文
	 */
	private java.lang.String statusStr;
	/** 银行类型 */
	private java.lang.String bankType;
	/** 银行编号 */
	private java.lang.String bankCode;
	/** 银行编号 */
	@Excel(name="授信银行")
	private java.lang.String bankName;
	/** 授信币种 */
//	@Currency(inject = "curName")
	private java.lang.String curCode;
	/** 授信币种 */
	@Excel(name="币别")
	private java.lang.String curName;
	/** 授信开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date beginDate;
	/** 授信开始时间 */
	private java.lang.String beginDateStr;
	/** 授信结束时间 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endDate;
	/** 授信结束时间 */
	private java.lang.String endDateStr;
	/** 授信总额度 **/
	@Excel(name="授信额度")
	private BigDecimal money;
	private String moneyStr;
	/** 占用额度 */
	@Excel(name="占用额度")
	private java.math.BigDecimal useMoney;
	/** 在审占用额度 **/
	@Excel(name="在审占用额度")
	private BigDecimal approveMoney;
	/** 可用额度 **/
	@Excel(name="可用额度")
	private BigDecimal overPlusMoney;
	/** 剩余额度 **/
	@Excel(name="剩余额度")
	private BigDecimal leftMoney;
	/** 分配方式 */
//	@Dict(dictGroup = "RatBkrationDis.DistributeWay", inject = "disWayName")
	private java.lang.String disWay;
	@Excel(name="分配方式")
	private java.lang.String disWayName;

	/** 授信有效日期 */
	@Excel(name="授信有效日期")
	private java.lang.String rationDateStr;
	//冻结额度
	private BigDecimal freezeMoney;
	//追加在途额度
	private BigDecimal addMoney;
	//虚拟占用额度
	private BigDecimal virtualUration;
	//虚拟调剂额度
	private BigDecimal virtualRelief;

	//虚拟占用额度
	private BigDecimal reliefAndUrationMoney;

	private String ratCode;
	//统计标识
	private String flagSign;

	//授信模式
//	@Dict(dictGroup = "RatBkration.RatMode", inject = "ratModeName")
	private String ratMode;
	@Excel(name="授信模式")
	private String ratModeName;

	/**保证金比例**/
	private BigDecimal assurepercent;
	/**费率**/
	private BigDecimal procedureFeeRate;

	public java.lang.String getCorpId() {
		return corpId;
	}

	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}

	public java.lang.String getCorpNo() {
		return corpNo;
	}

	public void setCorpNo(java.lang.String corpNo) {
		this.corpNo = corpNo;
	}

	public java.lang.String getCorpName() {
		return corpName;
	}

	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}

	public java.lang.String getBankType() {
		return bankType;
	}

	public void setBankType(java.lang.String bankType) {
		this.bankType = bankType;
	}

	public java.lang.String getBankCode() {
		return bankCode;
	}

	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}

	public java.lang.String getBankName() {
		return bankName;
	}

	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}

	public java.lang.String getCurCode() {
		return curCode;
	}

	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}

	public java.lang.String getCurName() {
		return curName;
	}

	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}

	public java.util.Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(java.util.Date beginDate) {
		this.beginDate = beginDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public java.math.BigDecimal getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(java.math.BigDecimal useMoney) {
		this.useMoney = useMoney;
	}

	public java.lang.String getDisWay() {
		return disWay;
	}

	public void setDisWay(java.lang.String disWay) {
		this.disWay = disWay;
	}

	public java.lang.String getRationDateStr() {
		return rationDateStr;
	}

	public void setRationDateStr(java.lang.String rationDateStr) {
		this.rationDateStr = rationDateStr;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getApproveMoney() {
		return approveMoney;
	}

	public void setApproveMoney(BigDecimal approveMoney) {
		this.approveMoney = approveMoney;
	}

	public BigDecimal getOverPlusMoney() {
        if (null == overPlusMoney) {
            overPlusMoney = BigDecimal.ZERO;
        }
		return overPlusMoney;
	}

	public void setOverPlusMoney(BigDecimal overPlusMoney) {
		this.overPlusMoney = overPlusMoney;
	}

	public BigDecimal getLeftMoney() {
		return leftMoney;
	}

	public void setLeftMoney(BigDecimal leftMoney) {
		this.leftMoney = leftMoney;
	}

	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}

	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}

	public BigDecimal getAddMoney() {
		return addMoney;
	}

	public void setAddMoney(BigDecimal addMoney) {
		this.addMoney = addMoney;
	}

	public BigDecimal getVirtualUration() {
        if (null == virtualUration) {
            virtualUration = BigDecimal.ZERO;
        }
		return virtualUration;
	}

	public void setVirtualUration(BigDecimal virtualUration) {
		this.virtualUration = virtualUration;
	}

	public BigDecimal getVirtualRelief() {
		if (null == virtualRelief) {
            virtualRelief = BigDecimal.ZERO;
        }
		return virtualRelief;
	}

	public void setVirtualRelief(BigDecimal virtualRelief) {
		this.virtualRelief = virtualRelief;
	}

	public BigDecimal getReliefAndUrationMoney() {
        if (null == reliefAndUrationMoney) {
            reliefAndUrationMoney = BigDecimal.ZERO;
        }
		return reliefAndUrationMoney;
	}

	public void setReliefAndUrationMoney(BigDecimal reliefAndUrationMoney) {
		this.reliefAndUrationMoney = reliefAndUrationMoney;
	}

	public String getMoneyStr() {
		return moneyStr;
	}

	public void setMoneyStr(String moneyStr) {
		this.moneyStr = moneyStr;
	}

	public String getRatCode() {
		return ratCode;
	}

	public void setRatCode(String ratCode) {
		this.ratCode = ratCode;
	}

	public String getFlagSign() {
		return flagSign;
	}

	public void setFlagSign(String flagSign) {
		this.flagSign = flagSign;
	}

	public java.lang.String getBeginDateStr() {
		if (ObjectUtil.isNotEmpty(beginDate)) {
			return DateUtil.format(beginDate, "yyyy-MM-dd");
		}
		return beginDateStr;
	}

	public void setBeginDateStr(java.lang.String beginDateStr) {
		this.beginDateStr = beginDateStr;
	}

	public java.lang.String getEndDateStr() {
		if (ObjectUtil.isNotEmpty(endDate)) {
			return DateUtil.format(endDate, "yyyy-MM-dd");
		}
		return endDateStr;
	}

	public void setEndDateStr(java.lang.String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getRatMode() {
		return ratMode;
	}

	public void setRatMode(String ratMode) {
		this.ratMode = ratMode;
	}

	public String getRatModeName() {
		return ratModeName;
	}

	public void setRatModeName(String ratModeName) {
		this.ratModeName = ratModeName;
	}

	public String getDisWayName() {
		return disWayName;
	}

	public void setDisWayName(String disWayName) {
		this.disWayName = disWayName;
	}

	public BigDecimal getAssurepercent() {
		return assurepercent;
	}

	public void setAssurepercent(BigDecimal assurepercent) {
		this.assurepercent = assurepercent;
	}

	public BigDecimal getProcedureFeeRate() {
		return procedureFeeRate;
	}

	public void setProcedureFeeRate(BigDecimal procedureFeeRate) {
		this.procedureFeeRate = procedureFeeRate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
}
