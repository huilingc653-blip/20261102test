package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 下拨金额分析表（报表）
 *
 * @author wangzx
 * @date 2017-1-23
 */
public class DownAllocationAnalysisSearchVO implements Serializable {
	/**
	 * 网点ID
	 */
	@BtCompositeSearch(fieldName = "info.netId")
	private String netId;
	/**
	 * 单位ID
	 */
	@BtCompositeSearch(fieldName = "info.corpId")
	private String corpId;
	/**
	 * 申请日期
	 */
	@BtCompositeSearch(fieldName = "info.createDate")
	private Date applyDate;
	private Date gedApplyDate;
	private Date ledApplyDate;
	/**
	 * 付款方式
	 */
	@BtCompositeSearch(fieldName = "info.voucherType")
	private String voucherType;
	/**
	 * 金额
	 */
	@BtCompositeSearch(fieldName = "info.payMoney")
	private BigDecimal payMoney;
	private BigDecimal gegPayMoney;
	private BigDecimal legPayMoney;
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public Date getGedApplyDate() {
		return gedApplyDate;
	}
	public void setGedApplyDate(Date gedApplyDate) {
		this.gedApplyDate = gedApplyDate;
	}
	public Date getLedApplyDate() {
		return ledApplyDate;
	}
	public void setLedApplyDate(Date ledApplyDate) {
		this.ledApplyDate = ledApplyDate;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public BigDecimal getGegPayMoney() {
		return gegPayMoney;
	}
	public void setGegPayMoney(BigDecimal gegPayMoney) {
		this.gegPayMoney = gegPayMoney;
	}
	public BigDecimal getLegPayMoney() {
		return legPayMoney;
	}
	public void setLegPayMoney(BigDecimal legPayMoney) {
		this.legPayMoney = legPayMoney;
	}
}
