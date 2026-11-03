package com.bytter.biz.budget.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**   
 * @Title: Entity
 * @Description: 预算执行组件VO
 * @author onlineGenerator
 * @date 2017-07-18 10:48:05
 * @version 1.0
 *
 */
public class FbsBudgetProcessCommonVO implements Serializable {
	/**科目ID*/
	private String itemId;
	/**科目代码*/
	private String itemCode;
	/**科目名称*/
	private String itemName;
	/**父科目*/
	private String superItemCode;
	/**科目余额*/
	private BigDecimal itemMoney;
	/**上级科目余额*/
	private BigDecimal superItemMoney;
	/**大科目余额*/
	private BigDecimal topItemMoney;
	/**执行金额*/
	private BigDecimal processMoney;
	/**汇率*/
	private BigDecimal exchangeRate;
	/**执行金额（折合本身币别）*/
	private BigDecimal processCurMoney;
	/**计划执行规则*/
	private String processRule;

	private String status;

	private String statusName;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSuperItemCode() {
		return superItemCode;
	}
	public void setSuperItemCode(String superItemCode) {
		this.superItemCode = superItemCode;
	}
	public BigDecimal getItemMoney() {
		return itemMoney;
	}
	public void setItemMoney(BigDecimal itemMoney) {
		this.itemMoney = itemMoney;
	}
	public BigDecimal getSuperItemMoney() {
		return superItemMoney;
	}
	public void setSuperItemMoney(BigDecimal superItemMoney) {
		this.superItemMoney = superItemMoney;
	}
	public BigDecimal getTopItemMoney() {
		return topItemMoney;
	}
	public void setTopItemMoney(BigDecimal topItemMoney) {
		this.topItemMoney = topItemMoney;
	}
	public BigDecimal getProcessMoney() {
		return processMoney;
	}
	public void setProcessMoney(BigDecimal processMoney) {
		this.processMoney = processMoney;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal getProcessCurMoney() {
		return processCurMoney;
	}
	public void setProcessCurMoney(BigDecimal processCurMoney) {
		this.processCurMoney = processCurMoney;
	}
	public String getProcessRule() {
		return processRule;
	}
	public void setProcessRule(String processRule) {
		this.processRule = processRule;
	}
}
