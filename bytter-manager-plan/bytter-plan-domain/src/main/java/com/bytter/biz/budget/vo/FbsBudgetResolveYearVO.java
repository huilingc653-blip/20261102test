package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
/**   
 * @Title: Entity
 * @Description: 年计划分解表VO
 * @author onlineGenerator
 * @date 2017-07-26 13:54:53
 * @version 1.0
 *
 */
public class FbsBudgetResolveYearVO implements Serializable {
	/**ID*/
	private String id;
	/**计划明细ID*/
	private String detailId;
	/**计划ID*/
	private String budgetId;
	/**追加明细id*/
	private String superaddDtlId;
	/***科目Id*/
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
	/**7月上报金额*/
	private BigDecimal bMoney7;
	/**8月上报金额*/
	private BigDecimal bMoney8;
	/**9月上报金额*/
	private BigDecimal bMoney9;
	/**10月上报金额*/
	private BigDecimal bMoney10;
	/**11月上报金额*/
	private BigDecimal bMoney11;
	/**12月上报金额*/
	private BigDecimal bMoney12;
	/**追加金额*/
	private BigDecimal bsuperMoney;
	/**1月追加金额*/
	private BigDecimal bsuperMoney1;
	/**2月追加金额*/
	private BigDecimal bsuperMoney2;
	/**3月追加金额*/
	private BigDecimal bsuperMoney3;
	/**4月追加金额*/
	private BigDecimal bsuperMoney4;
	/**5月追加金额*/
	private BigDecimal bsuperMoney5;
	/**6月追加金额*/
	private BigDecimal bsuperMoney6;
	/**7月追加金额*/
	private BigDecimal bsuperMoney7;
	/**8月追加金额*/
	private BigDecimal bsuperMoney8;
	/**9月追加金额*/
	private BigDecimal bsuperMoney9;
	/**10月追加金额*/
	private BigDecimal bsuperMoney10;
	/**11月追加金额*/
	private BigDecimal bsuperMoney11;
	/**12月追加金额*/
	private BigDecimal bsuperMoney12;
	/**本次追加金额*/
	private BigDecimal btempMoney;
	/**1月本次追加金额*/
	private BigDecimal btempMoney1;
	/**2月本次追加金额*/
	private BigDecimal btempMoney2;
	/**3月本次追加金额*/
	private BigDecimal btempMoney3;
	/**4月本次追加金额*/
	private BigDecimal btempMoney4;
	/**5月本次追加金额*/
	private BigDecimal btempMoney5;
	/**6月本次追加金额*/
	private BigDecimal btempMoney6;
	/**7月本次追加金额*/
	private BigDecimal btempMoney7;
	/**8月本次追加金额*/
	private BigDecimal btempMoney8;
	/**9月本次追加金额*/
	private BigDecimal btempMoney9;
	/**10月本次追加金额*/
	private BigDecimal btempMoney10;
	/**11月本次追加金额*/
	private BigDecimal btempMoney11;
	/**12月本次追加金额*/
	private BigDecimal btempMoney12;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
	HashMap<Integer, BigDecimal> bdetailMoneyMap = new HashMap<Integer, BigDecimal>();
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
	public BigDecimal getBsuperMoney7() {
		return bsuperMoney7;
	}
	public void setBsuperMoney7(BigDecimal bsuperMoney7) {
		this.bsuperMoney7 = bsuperMoney7;
	}
	public BigDecimal getBsuperMoney8() {
		return bsuperMoney8;
	}
	public void setBsuperMoney8(BigDecimal bsuperMoney8) {
		this.bsuperMoney8 = bsuperMoney8;
	}
	public BigDecimal getBsuperMoney9() {
		return bsuperMoney9;
	}
	public void setBsuperMoney9(BigDecimal bsuperMoney9) {
		this.bsuperMoney9 = bsuperMoney9;
	}
	public BigDecimal getBsuperMoney10() {
		return bsuperMoney10;
	}
	public void setBsuperMoney10(BigDecimal bsuperMoney10) {
		this.bsuperMoney10 = bsuperMoney10;
	}
	public BigDecimal getBsuperMoney11() {
		return bsuperMoney11;
	}
	public void setBsuperMoney11(BigDecimal bsuperMoney11) {
		this.bsuperMoney11 = bsuperMoney11;
	}
	public BigDecimal getBsuperMoney12() {
		return bsuperMoney12;
	}
	public void setBsuperMoney12(BigDecimal bsuperMoney12) {
		this.bsuperMoney12 = bsuperMoney12;
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
	public BigDecimal getBtempMoney7() {
		return btempMoney7;
	}
	public void setBtempMoney7(BigDecimal btempMoney7) {
		this.btempMoney7 = btempMoney7;
	}
	public BigDecimal getBtempMoney8() {
		return btempMoney8;
	}
	public void setBtempMoney8(BigDecimal btempMoney8) {
		this.btempMoney8 = btempMoney8;
	}
	public BigDecimal getBtempMoney9() {
		return btempMoney9;
	}
	public void setBtempMoney9(BigDecimal btempMoney9) {
		this.btempMoney9 = btempMoney9;
	}
	public BigDecimal getBtempMoney10() {
		return btempMoney10;
	}
	public void setBtempMoney10(BigDecimal btempMoney10) {
		this.btempMoney10 = btempMoney10;
	}
	public BigDecimal getBtempMoney11() {
		return btempMoney11;
	}
	public void setBtempMoney11(BigDecimal btempMoney11) {
		this.btempMoney11 = btempMoney11;
	}
	public BigDecimal getBtempMoney12() {
		return btempMoney12;
	}
	public void setBtempMoney12(BigDecimal btempMoney12) {
		this.btempMoney12 = btempMoney12;
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
	public HashMap<Integer, BigDecimal> getBdetailMoneyMap() {
		return bdetailMoneyMap;
	}
	public void setBdetailMoneyMap(HashMap<Integer, BigDecimal> bdetailMoneyMap) {
		this.bdetailMoneyMap = bdetailMoneyMap;
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
	public BigDecimal getBMoney7() {
		return bMoney7;
	}
	public void setBMoney7(BigDecimal bMoney7) {
		this.bMoney7 = bMoney7;
	}
	public BigDecimal getBMoney8() {
		return bMoney8;
	}
	public void setBMoney8(BigDecimal bMoney8) {
		this.bMoney8 = bMoney8;
	}
	public BigDecimal getBMoney9() {
		return bMoney9;
	}
	public void setBMoney9(BigDecimal bMoney9) {
		this.bMoney9 = bMoney9;
	}
	public BigDecimal getBMoney10() {
		return bMoney10;
	}
	public void setBMoney10(BigDecimal bMoney10) {
		this.bMoney10 = bMoney10;
	}
	public BigDecimal getBMoney11() {
		return bMoney11;
	}
	public void setBMoney11(BigDecimal bMoney11) {
		this.bMoney11 = bMoney11;
	}
	public BigDecimal getBMoney12() {
		return bMoney12;
	}
	public void setBMoney12(BigDecimal bMoney12) {
		this.bMoney12 = bMoney12;
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
