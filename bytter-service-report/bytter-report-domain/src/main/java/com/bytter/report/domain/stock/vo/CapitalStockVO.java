package com.bytter.report.domain.stock.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 资金存量报表VO
 *
 * @author zuoy 2017-10-25
 */
public class CapitalStockVO implements Serializable {
	
	private String id;
	/**
	 * 单位ID
	 */
	@Corp(inject = "corpName")
	private String corpId;
	/**
	 * 单位名称
	 */
	private String corpName;
	/**
	 * 网点ID
	 */
	@Corp(inject = "netName")
	private String netId;
	/**
	 * 网点名称
	 */
	private String netName;
	/**
	 * 存量类型 1-现金 2-票据
	 */
	@Dict(dictGroup = "capitalStock.stockType", inject = "stockTypeName")
	private String stockType;
	/**
	 * 存量类型名称
	 */
	private String stockTypeName;
	/**
	 * 期初资金余额
	 */
	private BigDecimal beginBal;
	/**
	 * 本期资金增加额
	 */
	private BigDecimal inAmt;
	/**
	 * 本期资金减少额
	 */
	private BigDecimal outAmt;
	/**
	 * 期末资金余额 （期初资金余额+本期资金增加额-本期资金减少额）
	 */
	private BigDecimal endBal;
	/**
	 * 本期不可动用资金
	 */
	private BigDecimal freezeMoney;
	/**
	 * 可用资金余额 (期末资金余额-本期不可动用资金)
	 */
	private BigDecimal availBal;
	/**
	 * 银行类型ID
	 */
	private String bankTypeId;
	/**
	 * 银行名称
	 */
	private String bankTypeName;
	/**
	 * 报表类型
	 */
	private String reportType;
	/**
	 * 日期类型
	 */
	private String dateType;
	/**
	 * 金额单位
	 */
	private String moneyUnit;
	/**
	 * 币别
	 */
	private String curId;
	/**
	 * 折合币种
	 */
	private String reducedCurId;
	/**
	 * 单位
	 */
	private String unit;
	/**
	 * 指标筛选App专用
	 */
	private String indexType;
	/**
	 * 图表显示类型APP专用
	 */
	private String barType;
	public String getReducedCurId() {
		return reducedCurId;
	}
	public void setReducedCurId(String reducedCurId) {
		this.reducedCurId = reducedCurId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getStockType() {
		return stockType;
	}
	public void setStockType(String stockType) {
		this.stockType = stockType;
	}
	public String getStockTypeName() {
		return stockTypeName;
	}
	public void setStockTypeName(String stockTypeName) {
		this.stockTypeName = stockTypeName;
	}
	public BigDecimal getBeginBal() {
		return beginBal;
	}
	public void setBeginBal(BigDecimal beginBal) {
		this.beginBal = beginBal;
	}
	public BigDecimal getOutAmt() {
		return outAmt;
	}
	public void setOutAmt(BigDecimal outAmt) {
		this.outAmt = outAmt;
	}
	public BigDecimal getInAmt() {
		return inAmt;
	}
	public void setInAmt(BigDecimal inAmt) {
		this.inAmt = inAmt;
	}
	public BigDecimal getEndBal() {
		return endBal;
	}
	public void setEndBal(BigDecimal endBal) {
		this.endBal = endBal;
	}
	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}
	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public BigDecimal getAvailBal() {
		return availBal;
	}
	public void setAvailBal(BigDecimal availBal) {
		this.availBal = availBal;
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
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public String getMoneyUnit() {
		return moneyUnit;
	}
	public void setMoneyUnit(String moneyUnit) {
		this.moneyUnit = moneyUnit;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getIndexType() {
		return indexType;
	}
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}
	public String getBarType() {
		return barType;
	}
	public void setBarType(String barType) {
		this.barType = barType;
	}
	@Override
	public String toString() {
		return "CapitalStockVO{" +
				"id='" + id + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", netId='" + netId + '\'' +
				", netName='" + netName + '\'' +
				", stockType='" + stockType + '\'' +
				", stockTypeName='" + stockTypeName + '\'' +
				", beginBal=" + beginBal +
				", inAmt=" + inAmt +
				", outAmt=" + outAmt +
				", endBal=" + endBal +
				", freezeMoney=" + freezeMoney +
				", availBal=" + availBal +
				", bankTypeId='" + bankTypeId + '\'' +
				", bankTypeName='" + bankTypeName + '\'' +
				", reportType='" + reportType + '\'' +
				", dateType='" + dateType + '\'' +
				", moneyUnit='" + moneyUnit + '\'' +
				", curId='" + curId + '\'' +
				", barType='" + barType + '\'' +
				", indexType='" + indexType + '\'' +
				'}';
	}
}
