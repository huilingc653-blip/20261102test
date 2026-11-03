package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 票据余额对比分析（报表）
 * @author zhenghj
 * @date 2020-1-15
 */
public class AbBillBalCompareVO implements Serializable {
	/**
	 *单位
	 */
	@Corp(inject = "corpName")
	private String corpId;
	private String corpName;
	/**流通介质：10 纸票、11 电票*/
	@Dict(dictGroup = "AB_BILL_FLAG", inject ="billFlagName")
	private Integer billFlag;
	private String billFlagName;
	/**票据类型：10 银票、11 商票*/
	@Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	/**票据类型名称：10 银票、11 商票*/
	private String billTypeName;
	/**
	 * 金额单位
	 */
	private Integer moneyUnit;
	/**
	 *  对比周期
	 *  1-较上月末、2-较上季度、3-较三个月前、4-较上年同期、5-较年初，默认全部
	 */
	@BtCompositeSearch()
	private String comparedCycle;
	/**
	 * 统计月份
	 */
	private Date statisticsMonth;
	/**
	 * 余额
	 */
	private BigDecimal bal;
	/**
	 *	上月末余额
	 */
	private BigDecimal befMonthBal;
	/**
	 *	上月末差额
	 */
	private BigDecimal befMonthDifBal;
	/**
	 *	上月末比例
	 */
	private BigDecimal befMonthProportion;
	/**
	 *	三个月前余额
	 */
	private BigDecimal befThreeMonthBal;
	/**
	 *	三个月前差额
	 */
	private BigDecimal befThreeMDifBal;
	/**
	 *	三个月前比例
	 */
	private BigDecimal befThreeMProportion;
	/**
	 *	上季度末余额
	 */
	private BigDecimal befQuarterBal;
	/**
	 *	上季度末差额
	 */
	private BigDecimal befQuarterDifBal;
	/**
	 *	上季度末比例
	 */
	private BigDecimal befQuarterProportion;
	/**
	 *	上年同期余额
	 */
	private BigDecimal befYearBal;
	/**
	 *	上年同期差额
	 */
	private BigDecimal befYearDifBal;
	/**
	 *	上年同期比例
	 */
	private BigDecimal befYearProportion;
	/**
	 *	年初余额
	 */
	private BigDecimal yearEarlyBal;
	/**
	 *	年初差额
	 */
	private BigDecimal yearEarlyDifBal;
	/**
	 *	年初比例
	 */
	private BigDecimal yearEarlyProportion;
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
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	public String getBillFlagName() {
		return billFlagName;
	}
	public void setBillFlagName(String billFlagName) {
		this.billFlagName = billFlagName;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public Integer getMoneyUnit() {
		return moneyUnit;
	}
	public void setMoneyUnit(Integer moneyUnit) {
		this.moneyUnit = moneyUnit;
	}
	public String getComparedCycle() {
		return comparedCycle;
	}
	public void setComparedCycle(String comparedCycle) {
		this.comparedCycle = comparedCycle;
	}
	public Date getStatisticsMonth() {
		return statisticsMonth;
	}
	public void setStatisticsMonth(Date statisticsMonth) {
		this.statisticsMonth = statisticsMonth;
	}
	public BigDecimal getBal() {
		return bal;
	}
	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}
	public BigDecimal getBefMonthBal() {
		return befMonthBal;
	}
	public void setBefMonthBal(BigDecimal befMonthBal) {
		this.befMonthBal = befMonthBal;
	}
	public BigDecimal getBefMonthDifBal() {
		return befMonthDifBal;
	}
	public void setBefMonthDifBal(BigDecimal befMonthDifBal) {
		this.befMonthDifBal = befMonthDifBal;
	}
	public BigDecimal getBefMonthProportion() {
		return befMonthProportion;
	}
	public void setBefMonthProportion(BigDecimal befMonthProportion) {
		this.befMonthProportion = befMonthProportion;
	}
	public BigDecimal getBefThreeMonthBal() {
		return befThreeMonthBal;
	}
	public void setBefThreeMonthBal(BigDecimal befThreeMonthBal) {
		this.befThreeMonthBal = befThreeMonthBal;
	}
	public BigDecimal getBefThreeMDifBal() {
		return befThreeMDifBal;
	}
	public void setBefThreeMDifBal(BigDecimal befThreeMDifBal) {
		this.befThreeMDifBal = befThreeMDifBal;
	}
	public BigDecimal getBefThreeMProportion() {
		return befThreeMProportion;
	}
	public void setBefThreeMProportion(BigDecimal befThreeMProportion) {
		this.befThreeMProportion = befThreeMProportion;
	}
	public BigDecimal getBefQuarterBal() {
		return befQuarterBal;
	}
	public void setBefQuarterBal(BigDecimal befQuarterBal) {
		this.befQuarterBal = befQuarterBal;
	}
	public BigDecimal getBefQuarterDifBal() {
		return befQuarterDifBal;
	}
	public void setBefQuarterDifBal(BigDecimal befQuarterDifBal) {
		this.befQuarterDifBal = befQuarterDifBal;
	}
	public BigDecimal getBefQuarterProportion() {
		return befQuarterProportion;
	}
	public void setBefQuarterProportion(BigDecimal befQuarterProportion) {
		this.befQuarterProportion = befQuarterProportion;
	}
	public BigDecimal getBefYearBal() {
		return befYearBal;
	}
	public void setBefYearBal(BigDecimal befYearBal) {
		this.befYearBal = befYearBal;
	}
	public BigDecimal getBefYearDifBal() {
		return befYearDifBal;
	}
	public void setBefYearDifBal(BigDecimal befYearDifBal) {
		this.befYearDifBal = befYearDifBal;
	}
	public BigDecimal getBefYearProportion() {
		return befYearProportion;
	}
	public void setBefYearProportion(BigDecimal befYearProportion) {
		this.befYearProportion = befYearProportion;
	}
	public BigDecimal getYearEarlyBal() {
		return yearEarlyBal;
	}
	public void setYearEarlyBal(BigDecimal yearEarlyBal) {
		this.yearEarlyBal = yearEarlyBal;
	}
	public BigDecimal getYearEarlyDifBal() {
		return yearEarlyDifBal;
	}
	public void setYearEarlyDifBal(BigDecimal yearEarlyDifBal) {
		this.yearEarlyDifBal = yearEarlyDifBal;
	}
	public BigDecimal getYearEarlyProportion() {
		return yearEarlyProportion;
	}
	public void setYearEarlyProportion(BigDecimal yearEarlyProportion) {
		this.yearEarlyProportion = yearEarlyProportion;
	}
	@Override
	public String toString() {
		return "AbBillBalCompareVO{" +
				"corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", billFlag=" + billFlag +
				", billFlagName='" + billFlagName + '\'' +
				", billType=" + billType +
				", billTypeName='" + billTypeName + '\'' +
				", moneyUnit=" + moneyUnit +
				", comparedCycle='" + comparedCycle + '\'' +
				", statisticsMonth=" + statisticsMonth +
				", bal=" + bal +
				", befMonthBal=" + befMonthBal +
				", befMonthDifBal=" + befMonthDifBal +
				", befMonthProportion=" + befMonthProportion +
				", befThreeMonthBal=" + befThreeMonthBal +
				", befThreeMDifBal=" + befThreeMDifBal +
				", befThreeMProportion=" + befThreeMProportion +
				", befQuarterBal=" + befQuarterBal +
				", befQuarterDifBal=" + befQuarterDifBal +
				", befQuarterProportion=" + befQuarterProportion +
				", befYearBal=" + befYearBal +
				", befYearDifBal=" + befYearDifBal +
				", befYearProportion=" + befYearProportion +
				", yearEarlyBal=" + yearEarlyBal +
				", yearEarlyDifBal=" + yearEarlyDifBal +
				", yearEarlyProportion=" + yearEarlyProportion +
				'}';
	}
}
