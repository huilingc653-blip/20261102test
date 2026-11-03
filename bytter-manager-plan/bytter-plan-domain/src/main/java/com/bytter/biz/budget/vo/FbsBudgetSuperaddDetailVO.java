package com.bytter.biz.budget.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**   
 * @Title: Entity
 * @Description: 计划追加明细VO
 * @author onlineGenerator
 * @date 2017-07-14 15:06:43
 * @version 1.0
 *
 */
public class FbsBudgetSuperaddDetailVO implements Serializable {
	private String id;
	/**计划追加ID*/
	private String superaddId;
	/**科目代码*/
	private String itemCode;
	/**科目名称*/
	private String itemName;
	/**追加金额*/
	private BigDecimal bsuperMoney;
	/**备注*/
	private String rmk;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSuperaddId() {
		return superaddId;
	}
	public void setSuperaddId(String superaddId) {
		this.superaddId = superaddId;
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
	public BigDecimal getBsuperMoney() {
		return bsuperMoney;
	}
	public void setBsuperMoney(BigDecimal bsuperMoney) {
		this.bsuperMoney = bsuperMoney;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	
}
