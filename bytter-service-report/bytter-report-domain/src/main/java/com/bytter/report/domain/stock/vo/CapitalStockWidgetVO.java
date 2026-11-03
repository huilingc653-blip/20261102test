package com.bytter.report.domain.stock.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 资金存量首页报表VO
 *
 * @author zuoy 2017-11-09
 */
public class CapitalStockWidgetVO implements Serializable {
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
	 * 银行存款可用资金
	 */
	private BigDecimal accAvailBal;
	/**
	 * 应收票据可用资金
	 */
	private BigDecimal billAvailBal;
	/**
	 * 币别
	 */
	@Currency(inject = "curName")
	private String curId;
	private String curName;
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
	public BigDecimal getAccAvailBal() {
		return accAvailBal;
	}
	public void setAccAvailBal(BigDecimal accAvailBal) {
		this.accAvailBal = accAvailBal;
	}
	public BigDecimal getBillAvailBal() {
		return billAvailBal;
	}
	public void setBillAvailBal(BigDecimal billAvailBal) {
		this.billAvailBal = billAvailBal;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
}
