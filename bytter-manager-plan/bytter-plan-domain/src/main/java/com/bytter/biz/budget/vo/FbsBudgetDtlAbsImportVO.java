package com.bytter.biz.budget.vo;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 计划上报明细VO
 * @date 2017-07-14 15:06:43
 */
@ExcelTarget("FbsBudgetDtlAbsImportVO")
public class FbsBudgetDtlAbsImportVO extends ExcelModel implements Serializable {
	private String id;
	/**
	 * 计划ID
	 */
	private String budgetId;
	/**
	 * 科目代码
	 */
	@Excel(name = "科目编号")
	private String itemCode;
	/**
	 * 科目名称
	 */
	@Excel(name = "科目名称")
	private String itemName;
	/**
	 * 上报金额
	 */
	@Excel(name = "预算金额")
	private BigDecimal bMoney;
	/**
	 * 追加金额
	 */
	private BigDecimal bsuperMoney;
	/**
	 * 本次追加金额
	 */
	private BigDecimal btempMoney;
	/**
	 * 执行金额
	 */
	private BigDecimal processMoney;
	/**
	 * 可（上报/追减）金额
	 */
	private BigDecimal canBMoney;
	/**
	 * 留存金额
	 */
	private BigDecimal remainBMoney;
	/**
	 * 追加明细ID
	 */
	private String superaddDtlId;
	/**
	 * 上报金额合计
	 */
	private BigDecimal bMoneySum;
	private String isHasDetail = "0";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBudgetId() {
		return budgetId;
	}
	public void setBudgetId(String budgetId) {
		this.budgetId = budgetId;
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
	public BigDecimal getbMoney() {
		return bMoney;
	}
	public void setbMoney(BigDecimal bMoney) {
		this.bMoney = bMoney;
	}
	public BigDecimal getBsuperMoney() {
		return bsuperMoney;
	}
	public void setBsuperMoney(BigDecimal bsuperMoney) {
		this.bsuperMoney = bsuperMoney;
	}
	public BigDecimal getBtempMoney() {
		return btempMoney;
	}
	public void setBtempMoney(BigDecimal btempMoney) {
		this.btempMoney = btempMoney;
	}
	public BigDecimal getProcessMoney() {
		return processMoney;
	}
	public void setProcessMoney(BigDecimal processMoney) {
		this.processMoney = processMoney;
	}
	public BigDecimal getCanBMoney() {
		return canBMoney;
	}
	public void setCanBMoney(BigDecimal canBMoney) {
		this.canBMoney = canBMoney;
	}
	public BigDecimal getRemainBMoney() {
		return remainBMoney;
	}
	public void setRemainBMoney(BigDecimal remainBMoney) {
		this.remainBMoney = remainBMoney;
	}
	public String getSuperaddDtlId() {
		return superaddDtlId;
	}
	public void setSuperaddDtlId(String superaddDtlId) {
		this.superaddDtlId = superaddDtlId;
	}
	public BigDecimal getbMoneySum() {
		return bMoneySum;
	}
	public void setbMoneySum(BigDecimal bMoneySum) {
		this.bMoneySum = bMoneySum;
	}
	public String getIsHasDetail() {
		return isHasDetail;
	}
	public void setIsHasDetail(String isHasDetail) {
		this.isHasDetail = isHasDetail;
	}
	@Override
	public String getBeanName() {
		return "fbsBudgetDownloadAndImportService";
	}
}
