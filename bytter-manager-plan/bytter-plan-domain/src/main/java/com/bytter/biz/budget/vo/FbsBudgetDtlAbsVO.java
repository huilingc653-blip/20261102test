package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 计划上报明细VO
 * @date 2017-07-14 15:06:43
 */
public class FbsBudgetDtlAbsVO implements Serializable {
	private String id;
	/**
	 * 计划ID
	 */
	private String budgetId;
	/***科目Id
	 * FBS_ITEM 为本域表 可以直接连表查询
	 * */
	//@Dict(tableName="FBS_ITEM",valueFieldName="id",showFiledName="item_name",inject="itemName")
	private String itemId;
	/**
	 * 科目代码
	 */
	private String itemCode;
	/**
	 * 科目名称
	 */
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
	 * 本次追加金额
	 */
	private BigDecimal btempMoney;
	/**
	 * 执行金额
	 */
	private BigDecimal processMoney;
	/**
	 * 计划科目
	 */
	private FbsItemVO item = new FbsItemVO();
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
	/**
	 * 明细集合
	 */
	private List<FbsBudgetDetailVO> detailList = new ArrayList<FbsBudgetDetailVO>();
	/**实际金额*/
	private BigDecimal factMoney;
	/**差异原因*/
	private String diffReason;
	/**分析状态*/
	private Integer analyseStatus;
	/**差异金额*/
	private BigDecimal diffMoney;
	/**差异比率*/
	private BigDecimal diffRatio;
	/**是否显示差异原因*/
	private  Boolean showDiffReason;
	/**
	 * 是否存在录入明细
	 */
	private Boolean hasDetail;
	/**
	 * 明细金额
	 */
	private BigDecimal detailMoney;
	/**上级科目*/
	private String superItemCode;
	/**资金流向1， 资金流入-1 ，资金流出*/
	private Integer sign;
	/**是否冲抵科目  1-是 0-否*/
	private String isInternal;
	/**是否计划控制科目 1-是 0-否*/
	private String isControl;
	/**同级科目关系*/
	private String itemRelation;
	/**是否期初科目 1-是 0-否*/
	private String isBeginPeriod;
	/**是否期末科目 1-是 0-否*/
	private String isEndPeriod;
	/**超计划比例*/
	private BigDecimal overPerSpend;
	/**是否超比例*/
	private String isOverPer;
	/**是否允许填写计划 1-是 0-否*/
	private String isWrite;
	/**备注*/
	private String rmk;
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
	public BigDecimal getProcessMoney() {
		return processMoney;
	}
	public void setProcessMoney(BigDecimal processMoney) {
		this.processMoney = processMoney;
	}
	public FbsItemVO getItem() {
		return item;
	}
	public void setItem(FbsItemVO item) {
		this.item = item;
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
		this.bMoneySum = this.bMoney.add(this.bsuperMoney);
		return bMoneySum;
	}
	public void setbMoneySum(BigDecimal bMoneySum) {
		this.bMoneySum = bMoneySum;
	}
	public List<FbsBudgetDetailVO> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<FbsBudgetDetailVO> detailList) {
		this.detailList = detailList;
	}
	public Boolean getHasDetail() {
		return hasDetail;
	}
	public void setHasDetail(Boolean hasDetail) {
		this.hasDetail = hasDetail;
	}
	public BigDecimal getFactMoney() {
		return factMoney;
	}
	public void setFactMoney(BigDecimal factMoney) {
		this.factMoney = factMoney;
	}
	public String getDiffReason() {
		return diffReason;
	}
	public void setDiffReason(String diffReason) {
		this.diffReason = diffReason;
	}
	public Integer getAnalyseStatus() {
		return analyseStatus;
	}
	public void setAnalyseStatus(Integer analyseStatus) {
		this.analyseStatus = analyseStatus;
	}
	public BigDecimal getDiffMoney() {
		return diffMoney;
	}
	public void setDiffMoney(BigDecimal diffMoney) {
		this.diffMoney = diffMoney;
	}
	public BigDecimal getDiffRatio() {
		return diffRatio;
	}
	public void setDiffRatio(BigDecimal diffRatio) {
		this.diffRatio = diffRatio;
	}
	public Boolean getShowDiffReason() {
		return showDiffReason;
	}
	public void setShowDiffReason(Boolean showDiffReason) {
		this.showDiffReason = showDiffReason;
	}
	public BigDecimal getDetailMoney() {
		return detailMoney;
	}
	public void setDetailMoney(BigDecimal detailMoney) {
		this.detailMoney = detailMoney;
	}
	public String getSuperItemCode() {
		return superItemCode;
	}
	public void setSuperItemCode(String superItemCode) {
		this.superItemCode = superItemCode;
	}
	public Integer getSign() {
		return sign;
	}
	public void setSign(Integer sign) {
		this.sign = sign;
	}
	public String getIsInternal() {
		return isInternal;
	}
	public void setIsInternal(String isInternal) {
		this.isInternal = isInternal;
	}
	public String getIsControl() {
		return isControl;
	}
	public void setIsControl(String isControl) {
		this.isControl = isControl;
	}
	public String getItemRelation() {
		return itemRelation;
	}
	public void setItemRelation(String itemRelation) {
		this.itemRelation = itemRelation;
	}
	public String getIsBeginPeriod() {
		return isBeginPeriod;
	}
	public void setIsBeginPeriod(String isBeginPeriod) {
		this.isBeginPeriod = isBeginPeriod;
	}
	public String getIsEndPeriod() {
		return isEndPeriod;
	}
	public void setIsEndPeriod(String isEndPeriod) {
		this.isEndPeriod = isEndPeriod;
	}
	public BigDecimal getOverPerSpend() {
		return overPerSpend;
	}
	public void setOverPerSpend(BigDecimal overPerSpend) {
		this.overPerSpend = overPerSpend;
	}
	public String getIsOverPer() {
		return isOverPer;
	}
	public void setIsOverPer(String isOverPer) {
		this.isOverPer = isOverPer;
	}
	public String getIsWrite() {
		return isWrite;
	}
	public void setIsWrite(String isWrite) {
		this.isWrite = isWrite;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
}
