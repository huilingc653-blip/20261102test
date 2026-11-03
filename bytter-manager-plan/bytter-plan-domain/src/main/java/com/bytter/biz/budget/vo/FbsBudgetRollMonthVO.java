package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.Dict;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**   
 * @Title: Entity
 * @Description: 年计划分解表VO
 * @author onlineGenerator
 * @date 2017-07-26 13:54:53
 * @version 1.0
 *
 */
public class FbsBudgetRollMonthVO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3845504584915241408L;
	/**ID 滚动 上报id*/
	private List<String> id;
	/**计划ID*/
	private String budgetId;
	/***科目Id*/
	//@Dict(tableName="FBS_ITEM",valueFieldName="id",showFiledName="item_name",inject="itemName")
	private String itemId;
	/**科目代码*/
	private String itemCode;
	/**科目名称*/
	private String itemName;
	/**上报金额 滚动月上报*/
	private List<BigDecimal> bMoney;
	/**可（上报/追减）金额*/
	private BigDecimal canBMoney;
	/**计划科目*/
	private FbsItemVO item = new FbsItemVO();
	HashMap<Integer, BigDecimal> bMoneyMap = new HashMap<Integer, BigDecimal>();
	/**明细录入金额 滚动明细金额*/
	private List<BigDecimal> detailMoney;
	public List<String> getId() {
		return id;
	}
	public void setId(List<String> id) {
		this.id = id;
	}
	public String getBudgetId() {
		return budgetId;
	}
	public void setBudgetId(String budgetId) {
		this.budgetId = budgetId;
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
	public List<BigDecimal> getbMoney() {
		return bMoney;
	}
	public void setbMoney(List<BigDecimal> bMoney) {
		this.bMoney = bMoney;
	}
	public BigDecimal getCanBMoney() {
		return canBMoney;
	}
	public void setCanBMoney(BigDecimal canBMoney) {
		this.canBMoney = canBMoney;
	}
	public FbsItemVO getItem() {
		return item;
	}
	public void setItem(FbsItemVO item) {
		this.item = item;
	}
	public HashMap<Integer, BigDecimal> getbMoneyMap() {
		return bMoneyMap;
	}
	public void setbMoneyMap(HashMap<Integer, BigDecimal> bMoneyMap) {
		this.bMoneyMap = bMoneyMap;
	}
	public List<BigDecimal> getDetailMoney() {
		return detailMoney;
	}
	public void setDetailMoney(List<BigDecimal> detailMoney) {
		this.detailMoney = detailMoney;
	}
}
