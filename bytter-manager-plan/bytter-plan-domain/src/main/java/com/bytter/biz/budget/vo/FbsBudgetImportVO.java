package com.bytter.biz.budget.vo;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 计划上报明细VO
 * @date 2017-07-14 15:06:43
 */
@ExcelTarget("FbsBudgetImportVO")
public class FbsBudgetImportVO extends ExcelModel implements Serializable {
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
	@Excel(name = "上报金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private BigDecimal bMoney;
	/**
	 * 上报金额 用于前端显示的
	 */
	private List<BigDecimal> showBMoney;
	/**
	 * 1月上报金额
	 */
	@Excel(name = "1月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney1;
	/**
	 * 2月上报金额
	 */
	@Excel(name = "2月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney2;
	/**
	 * 3月上报金额
	 */
	@Excel(name = "3月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney3;
	/**
	 * 4月上报金额
	 */
	@Excel(name = "4月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney4;
	/**
	 * 5月上报金额
	 */
	@Excel(name = "5月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney5;
	/**
	 * 6月上报金额
	 */
	@Excel(name = "6月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney6;
	/**
	 * 7月上报金额
	 */
	@Excel(name = "7月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney7;
	/**
	 * 8月上报金额
	 */
	@Excel(name = "8月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney8;
	/**
	 * 9月上报金额
	 */
	@Excel(name = "9月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney9;
	/**
	 * 10月上报金额
	 */
	@Excel(name = "10月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney10;
	/**
	 * 11月上报金额
	 */
	@Excel(name = "11月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney11;
	/**
	 * 12月上报金额
	 */
	@Excel(name = "12月", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney12;
	/**
	 * 第一周上报金额
	 */
	@Excel(name = "第1周", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bWeekMoney1;
	/**
	 * 第2周上报金额
	 */
	@Excel(name = "第2周", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bWeekMoney2;
	/**
	 * 第3周上报金额
	 */
	@Excel(name = "第3周", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bWeekMoney3;
	/**
	 * 第4周上报金额
	 */
	@Excel(name = "第4周", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bWeekMoney4;
	/**
	 * 第5周上报金额
	 */
	@Excel(name = "第5周", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bWeekMoney5;
	/**
	 * 第6周上报金额
	 */
	@Excel(name = "第6周", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bWeekMoney6;
	/**
	 * 上报金额
	 */
	@Excel(name = "新追加金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private BigDecimal bsuperMoney;
	/**
	 * 可上报金额
	 */
	private BigDecimal canBMoney;
	private FbsItemVO item = new FbsItemVO();
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
	public BigDecimal getCanBMoney() {
		return canBMoney;
	}
	public void setCanBMoney(BigDecimal canBMoney) {
		this.canBMoney = canBMoney;
	}
	public BigDecimal getbMoney1() {
		return bMoney1;
	}
	public void setbMoney1(BigDecimal bMoney1) {
		this.bMoney1 = bMoney1;
	}
	public BigDecimal getbMoney2() {
		return bMoney2;
	}
	public void setbMoney2(BigDecimal bMoney2) {
		this.bMoney2 = bMoney2;
	}
	public BigDecimal getbMoney3() {
		return bMoney3;
	}
	public void setbMoney3(BigDecimal bMoney3) {
		this.bMoney3 = bMoney3;
	}
	public BigDecimal getbMoney4() {
		return bMoney4;
	}
	public void setbMoney4(BigDecimal bMoney4) {
		this.bMoney4 = bMoney4;
	}
	public BigDecimal getbMoney5() {
		return bMoney5;
	}
	public void setbMoney5(BigDecimal bMoney5) {
		this.bMoney5 = bMoney5;
	}
	public BigDecimal getbMoney6() {
		return bMoney6;
	}
	public void setbMoney6(BigDecimal bMoney6) {
		this.bMoney6 = bMoney6;
	}
	public BigDecimal getbMoney7() {
		return bMoney7;
	}
	public void setbMoney7(BigDecimal bMoney7) {
		this.bMoney7 = bMoney7;
	}
	public BigDecimal getbMoney8() {
		return bMoney8;
	}
	public void setbMoney8(BigDecimal bMoney8) {
		this.bMoney8 = bMoney8;
	}
	public BigDecimal getbMoney9() {
		return bMoney9;
	}
	public void setbMoney9(BigDecimal bMoney9) {
		this.bMoney9 = bMoney9;
	}
	public BigDecimal getbMoney10() {
		return bMoney10;
	}
	public void setbMoney10(BigDecimal bMoney10) {
		this.bMoney10 = bMoney10;
	}
	public BigDecimal getbMoney11() {
		return bMoney11;
	}
	public void setbMoney11(BigDecimal bMoney11) {
		this.bMoney11 = bMoney11;
	}
	public BigDecimal getbMoney12() {
		return bMoney12;
	}
	public void setbMoney12(BigDecimal bMoney12) {
		this.bMoney12 = bMoney12;
	}
	public BigDecimal getbWeekMoney1() {
		return bWeekMoney1;
	}
	public void setbWeekMoney1(BigDecimal bWeekMoney1) {
		this.bWeekMoney1 = bWeekMoney1;
	}
	public BigDecimal getbWeekMoney2() {
		return bWeekMoney2;
	}
	public void setbWeekMoney2(BigDecimal bWeekMoney2) {
		this.bWeekMoney2 = bWeekMoney2;
	}
	public BigDecimal getbWeekMoney3() {
		return bWeekMoney3;
	}
	public void setbWeekMoney3(BigDecimal bWeekMoney3) {
		this.bWeekMoney3 = bWeekMoney3;
	}
	public BigDecimal getbWeekMoney4() {
		return bWeekMoney4;
	}
	public void setbWeekMoney4(BigDecimal bWeekMoney4) {
		this.bWeekMoney4 = bWeekMoney4;
	}
	public BigDecimal getbWeekMoney5() {
		return bWeekMoney5;
	}
	public void setbWeekMoney5(BigDecimal bWeekMoney5) {
		this.bWeekMoney5 = bWeekMoney5;
	}
	public BigDecimal getbWeekMoney6() {
		return bWeekMoney6;
	}
	public void setbWeekMoney6(BigDecimal bWeekMoney6) {
		this.bWeekMoney6 = bWeekMoney6;
	}
	public BigDecimal getBsuperMoney() {
		return bsuperMoney;
	}
	public void setBsuperMoney(BigDecimal bsuperMoney) {
		this.bsuperMoney = bsuperMoney;
	}
	public FbsItemVO getItem() {
		return item;
	}
	public void setItem(FbsItemVO item) {
		this.item = item;
	}
	public BigDecimal getbMoney() {
		return bMoney;
	}
	public void setbMoney(BigDecimal bMoney) {
		this.bMoney = bMoney;
	}
	public List<BigDecimal> getShowBMoney() {
		return showBMoney;
	}
	public void setShowBMoney(List<BigDecimal> showBMoney) {
		this.showBMoney = showBMoney;
	}
	@Override
	public String getBeanName() {
		return "fbsBudgetDownloadAndImportService";
	}
}
