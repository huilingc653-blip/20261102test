package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
/**   
 * @Title: Entity
 * @Description: 月计划分解表VO
 * @author onlineGenerator
 * @date 2017-08-04 10:20:46
 * @version 1.0
 *
 */
public class FbsBudgetResolveMonthVO implements Serializable {
	/**ID*/
	private String id;
	/**计划明细ID*/
	private String detailId;
	/**计划ID*/
	private String budgetId;
	/**追加明细id*/
	private String superaddDtlId;
	/***科目Id
	 * 本领域表，直接关联查询
	 * */
	//@Dict(tableName="FBS_ITEM",valueFieldName="id",showFiledName="item_name",inject="itemName")
	private String itemId;
	/**科目代码*/
	private String itemCode;
	/**科目名称*/
	private String itemName;
	/**上报金额*/
	private BigDecimal bMoney;
	/**1月上报金额*/
	private BigDecimal bMoney1;
	/**2月上报金额*/
	private BigDecimal bMoney2;
	/**3月上报金额*/
	private BigDecimal bMoney3;
	/**4月上报金额*/
	private BigDecimal bMoney4;
	/**5月上报金额*/
	private BigDecimal bMoney5;
	/**6月上报金额*/
	private BigDecimal bMoney6;
	/**追加金额*/
	private BigDecimal bsuperMoney;
	/**第1周追加金额*/
	private BigDecimal bsuperMoney1;
	/**第2周追加金额*/
	private BigDecimal bsuperMoney2;
	/**第3周追加金额*/
	private BigDecimal bsuperMoney3;
	/**第4周追加金额*/
	private BigDecimal bsuperMoney4;
	/**第5周追加金额*/
	private BigDecimal bsuperMoney5;
	/**第6周追加金额*/
	private BigDecimal bsuperMoney6;
	/**本次追加金额*/
	private BigDecimal btempMoney;
	/**第1周本次追加金额*/
	private BigDecimal btempMoney1;
	/**第2周本次追加金额*/
	private BigDecimal btempMoney2;
	/**第3周本次追加金额*/
	private BigDecimal btempMoney3;
	/**第4周本次追加金额*/
	private BigDecimal btempMoney4;
	/**第5周本次追加金额*/
	private BigDecimal btempMoney5;
	/**第6周本次追加金额*/
	private BigDecimal btempMoney6;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**计划科目*/
	private FbsItemVO item = new FbsItemVO();
	HashMap<Integer, BigDecimal> bMoneyMap = new HashMap<Integer, BigDecimal>();
	HashMap<Integer, BigDecimal> bsuperMoneyMap = new HashMap<Integer, BigDecimal>();
	HashMap<Integer, BigDecimal> btempMoneyMap = new HashMap<Integer, BigDecimal>();
	HashMap<Integer, BigDecimal> remainBMoneyMap = new HashMap<Integer, BigDecimal>();
	/**可（上报/追减）金额*/
	private BigDecimal canBMoney;
	/**备注*/
	private String rmk;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDetailId() {
		return detailId;
	}
	public void setDetailId(String detailId) {
		this.detailId = detailId;
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
	public BigDecimal getbMoney() {
		return bMoney;
	}
	public void setbMoney(BigDecimal bMoney) {
		this.bMoney = bMoney;
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
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
	public BigDecimal getCanBMoney() {
		return canBMoney;
	}
	public void setCanBMoney(BigDecimal canBMoney) {
		this.canBMoney = canBMoney;
	}
	public BigDecimal getBsuperMoney() {
		return bsuperMoney;
	}
	public void setBsuperMoney(BigDecimal bsuperMoney) {
		this.bsuperMoney = bsuperMoney;
	}
	public BigDecimal getBsuperMoney1() {
		return bsuperMoney1;
	}
	public void setBsuperMoney1(BigDecimal bsuperMoney1) {
		this.bsuperMoney1 = bsuperMoney1;
	}
	public BigDecimal getBsuperMoney2() {
		return bsuperMoney2;
	}
	public void setBsuperMoney2(BigDecimal bsuperMoney2) {
		this.bsuperMoney2 = bsuperMoney2;
	}
	public BigDecimal getBsuperMoney3() {
		return bsuperMoney3;
	}
	public void setBsuperMoney3(BigDecimal bsuperMoney3) {
		this.bsuperMoney3 = bsuperMoney3;
	}
	public BigDecimal getBsuperMoney4() {
		return bsuperMoney4;
	}
	public void setBsuperMoney4(BigDecimal bsuperMoney4) {
		this.bsuperMoney4 = bsuperMoney4;
	}
	public BigDecimal getBsuperMoney5() {
		return bsuperMoney5;
	}
	public void setBsuperMoney5(BigDecimal bsuperMoney5) {
		this.bsuperMoney5 = bsuperMoney5;
	}
	public BigDecimal getBsuperMoney6() {
		return bsuperMoney6;
	}
	public void setBsuperMoney6(BigDecimal bsuperMoney6) {
		this.bsuperMoney6 = bsuperMoney6;
	}
	public BigDecimal getBtempMoney() {
		return btempMoney;
	}
	public void setBtempMoney(BigDecimal btempMoney) {
		this.btempMoney = btempMoney;
	}
	public BigDecimal getBtempMoney1() {
		return btempMoney1;
	}
	public void setBtempMoney1(BigDecimal btempMoney1) {
		this.btempMoney1 = btempMoney1;
	}
	public BigDecimal getBtempMoney2() {
		return btempMoney2;
	}
	public void setBtempMoney2(BigDecimal btempMoney2) {
		this.btempMoney2 = btempMoney2;
	}
	public BigDecimal getBtempMoney3() {
		return btempMoney3;
	}
	public void setBtempMoney3(BigDecimal btempMoney3) {
		this.btempMoney3 = btempMoney3;
	}
	public BigDecimal getBtempMoney4() {
		return btempMoney4;
	}
	public void setBtempMoney4(BigDecimal btempMoney4) {
		this.btempMoney4 = btempMoney4;
	}
	public BigDecimal getBtempMoney5() {
		return btempMoney5;
	}
	public void setBtempMoney5(BigDecimal btempMoney5) {
		this.btempMoney5 = btempMoney5;
	}
	public BigDecimal getBtempMoney6() {
		return btempMoney6;
	}
	public void setBtempMoney6(BigDecimal btempMoney6) {
		this.btempMoney6 = btempMoney6;
	}
	public HashMap<Integer, BigDecimal> getBsuperMoneyMap() {
		return bsuperMoneyMap;
	}
	public void setBsuperMoneyMap(HashMap<Integer, BigDecimal> bsuperMoneyMap) {
		this.bsuperMoneyMap = bsuperMoneyMap;
	}
	public HashMap<Integer, BigDecimal> getBtempMoneyMap() {
		return btempMoneyMap;
	}
	public void setBtempMoneyMap(HashMap<Integer, BigDecimal> btempMoneyMap) {
		this.btempMoneyMap = btempMoneyMap;
	}
	public HashMap<Integer, BigDecimal> getRemainBMoneyMap() {
		return remainBMoneyMap;
	}
	public void setRemainBMoneyMap(HashMap<Integer, BigDecimal> remainBMoneyMap) {
		this.remainBMoneyMap = remainBMoneyMap;
	}
	public BigDecimal getBMoney() {
		return bMoney;
	}
	public void setBMoney(BigDecimal bMoney) {
		this.bMoney = bMoney;
	}
	public BigDecimal getBMoney1() {
		return bMoney1;
	}
	public void setBMoney1(BigDecimal bMoney1) {
		this.bMoney1 = bMoney1;
	}
	public BigDecimal getBMoney2() {
		return bMoney2;
	}
	public void setBMoney2(BigDecimal bMoney2) {
		this.bMoney2 = bMoney2;
	}
	public BigDecimal getBMoney3() {
		return bMoney3;
	}
	public void setBMoney3(BigDecimal bMoney3) {
		this.bMoney3 = bMoney3;
	}
	public BigDecimal getBMoney4() {
		return bMoney4;
	}
	public void setBMoney4(BigDecimal bMoney4) {
		this.bMoney4 = bMoney4;
	}
	public BigDecimal getBMoney5() {
		return bMoney5;
	}
	public void setBMoney5(BigDecimal bMoney5) {
		this.bMoney5 = bMoney5;
	}
	public BigDecimal getBMoney6() {
		return bMoney6;
	}
	public void setBMoney6(BigDecimal bMoney6) {
		this.bMoney6 = bMoney6;
	}
	public String getSuperaddDtlId() {
		return superaddDtlId;
	}
	public void setSuperaddDtlId(String superaddDtlId) {
		this.superaddDtlId = superaddDtlId;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
}
