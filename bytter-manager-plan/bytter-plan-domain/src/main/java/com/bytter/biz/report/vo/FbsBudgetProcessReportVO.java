package com.bytter.biz.report.vo;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 计划执行报表VO
 * @date 2018-03-13 15:06:43
 */
public class FbsBudgetProcessReportVO implements Serializable {
	/**
	 * 单位ID
	 */
	@BtCompositeSearch(fieldName = "fbs.corp_id")
	//@Corp(inject = "corpName")
	private String corpId;
	private String corpName;
	/**
	 * *科目Id
	 * 根据 itemId ，查询本域 FBS_ITEM 表，返回 itemName；已在sql关联查询返回，不需要冗余字段
	 */
	//@Dict(tableName="FBS_ITEM",valueFieldName="id",showFiledName="item_name",inject="itemName")
	private String itemId;
	/**
	 * 科目代码
	 */
	@BtCompositeSearch(fieldName = "fbs.item_Code")
	private String itemCode;
	/**
	 * 科目名称
	 */
	@BtCompositeSearch(fieldName = "fbs.item_Name")
	private String itemName;
	/**
	 * 上报金额
	 */
	private BigDecimal bMoney;
	/**
	 * 追加金额
	 */
	private BigDecimal bsuperMoney;
	/**
	 * 追加金额 在途
	 */
	private BigDecimal btempMoney;
	/**
	 * 上报金额合计
	 */
	private BigDecimal bMoneySum;
	/**
	 * 执行金额（内部）
	 */
	private BigDecimal inProcessMoney;
	/**
	 * 执行金额（内部）在途
	 */
	private BigDecimal inTempProcessMoney;
	/**
	 * 执行金额（外部）
	 */
	private BigDecimal outProcessMoney;
	/**
	 * 执行金额（外部）在途
	 */
	private BigDecimal outTempProcessMoney;
	/**
	 * 可用金额
	 */
	private BigDecimal canBMoney;
	/**
	 * 执行比例
	 */
	private BigDecimal exerciseRatio;
	/**
	 * 误差率
	 */
	private BigDecimal errorRate;
	/**
	 * 可用金额占比
	 */
	private BigDecimal canBMoneyRatio;
	/**
	 * 计划开始时间
	 */
	@BtCompositeSearch(fieldName = "fbs.begin_date")
	private java.util.Date beginDate;
	/**
	 * 计划结束时间
	 */
	@BtCompositeSearch(fieldName = "fbs.end_date")
	private java.util.Date endDate;
	/**
	 * 计划周期类型 1-年 2-季 3-月 4-周 5-天
	 */
	@BtCompositeSearch(fieldName = "fbs.time_type")
	private java.lang.String timeType;
	/**
	 * 留存金额
	 */
	private BigDecimal remainBMoney;
	/**
	 * 科目
	 */
	private FbsItemVO item = new FbsItemVO();
	/**收款账号名称*/
	@BtCompositeSearch(fieldName = "t.payee_Acc_Name")
	private String payeeAccName;
	@BtCompositeSearch(fieldName = "fbs.budget_Id")
	private String budgetId;
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
	public BigDecimal getbMoneySum() {
		return bMoneySum;
	}
	public void setbMoneySum(BigDecimal bMoneySum) {
		this.bMoneySum = bMoneySum;
	}
	public BigDecimal getInProcessMoney() {
		return inProcessMoney;
	}
	public void setInProcessMoney(BigDecimal inProcessMoney) {
		this.inProcessMoney = inProcessMoney;
	}
	public BigDecimal getInTempProcessMoney() {
		return inTempProcessMoney;
	}
	public void setInTempProcessMoney(BigDecimal inTempProcessMoney) {
		this.inTempProcessMoney = inTempProcessMoney;
	}
	public BigDecimal getOutProcessMoney() {
		return outProcessMoney;
	}
	public void setOutProcessMoney(BigDecimal outProcessMoney) {
		this.outProcessMoney = outProcessMoney;
	}
	public BigDecimal getOutTempProcessMoney() {
		return outTempProcessMoney;
	}
	public void setOutTempProcessMoney(BigDecimal outTempProcessMoney) {
		this.outTempProcessMoney = outTempProcessMoney;
	}
	public BigDecimal getCanBMoney() {
		return canBMoney;
	}
	public void setCanBMoney(BigDecimal canBMoney) {
		this.canBMoney = canBMoney;
	}
	public BigDecimal getExerciseRatio() {
		return exerciseRatio;
	}
	public void setExerciseRatio(BigDecimal exerciseRatio) {
		this.exerciseRatio = exerciseRatio;
	}
	public BigDecimal getErrorRate() {
		return errorRate;
	}
	public void setErrorRate(BigDecimal errorRate) {
		this.errorRate = errorRate;
	}
	public BigDecimal getCanBMoneyRatio() {
		return canBMoneyRatio;
	}
	public void setCanBMoneyRatio(BigDecimal canBMoneyRatio) {
		this.canBMoneyRatio = canBMoneyRatio;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public BigDecimal getRemainBMoney() {
		return remainBMoney;
	}
	public void setRemainBMoney(BigDecimal remainBMoney) {
		this.remainBMoney = remainBMoney;
	}
	public FbsItemVO getItem() {
		return item;
	}
	public void setItem(FbsItemVO item) {
		this.item = item;
	}
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public String getBudgetId() {
		return budgetId;
	}
	public void setBudgetId(String budgetId) {
		this.budgetId = budgetId;
	}
	@Override
	public String toString() {
		return "FbsBudgetProcessReportVO{" +
				"corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", itemCode='" + itemCode + '\'' +
				", itemName='" + itemName + '\'' +
				", bMoney=" + bMoney +
				", bsuperMoney=" + bsuperMoney +
				", btempMoney=" + btempMoney +
				", bMoneySum=" + bMoneySum +
				", inProcessMoney=" + inProcessMoney +
				", inTempProcessMoney=" + inTempProcessMoney +
				", outProcessMoney=" + outProcessMoney +
				", outTempProcessMoney=" + outTempProcessMoney +
				", canBMoney=" + canBMoney +
				", exerciseRatio=" + exerciseRatio +
				", errorRate=" + errorRate +
				", canBMoneyRatio=" + canBMoneyRatio +
				", beginDate=" + beginDate +
				", endDate=" + endDate +
				", timeType='" + timeType + '\'' +
				'}';
	}
}
