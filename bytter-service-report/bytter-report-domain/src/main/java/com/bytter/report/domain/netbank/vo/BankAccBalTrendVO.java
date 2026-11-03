package com.bytter.report.domain.netbank.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 银行存款余额趋势VO
 *
 * @author zuoy 2018-12-05
 */
public class BankAccBalTrendVO implements Serializable {
	/**
	 * 余额时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date balDate;
	private String month;
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
	 * 币别ID
	 */
	@Currency(inject = "curName")
	private String curId;
	/**
	 * 币别名称
	 */
	private String curName;
	/**
	 * 余额
	 */
	private BigDecimal bal;
	/**
	 * 预算额度
	 */
	private BigDecimal budgetMoney;
	/**
	 * 上期余额
	 */
	private BigDecimal lastBal;
	public Date getBalDate() {
		return balDate;
	}
	public void setBalDate(Date balDate) {
		this.balDate = balDate;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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
	public BigDecimal getBal() {
		return bal;
	}
	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}
	public BigDecimal getBudgetMoney() {
		return budgetMoney;
	}
	public void setBudgetMoney(BigDecimal budgetMoney) {
		this.budgetMoney = budgetMoney;
	}
	public BigDecimal getLastBal() {
		return lastBal;
	}
	public void setLastBal(BigDecimal lastBal) {
		this.lastBal = lastBal;
	}
}
