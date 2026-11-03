package com.bytter.biz.budget.vo;

import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FBS_BUDGET_DETAIL
 * @date 2017-07-19 14:42:18
 */
@ExcelTarget("FbsBudgetDetailVO")
public class FbsBudgetDetailVO extends ExcelModel implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2029677864327828735L;
	private String id;
	/**
	 * 预算id
	 */
	private String budgetId;
	/**
	 * 明细ID
	 */
	private java.lang.String detailId;
	/**
	 * 追加ID
	 */
	private java.lang.String superaddId;
	/**
	 * 科目代码
	 */
	@Excel(name = "*科目")
	private java.lang.String itemCode;
	/**
	 * 科目名称
	 */
	@Excel(name = "科目名称")
	private java.lang.String itemName;
	/**
	 * 计划周期类型 1-年 2-季 3-月 4-周 5-天
	 */
	private java.lang.String timeType;
	/**
	 * 上报金额
	 */
	@Excel(name = "*上报金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal bMoney;
	/**
	 * 追加金额
	 */
	private java.math.BigDecimal bsuperMoney;
	/**本次追加金额*/
	/**
	 * 上报金额
	 */
	@Excel(name = "*追加金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private java.math.BigDecimal btempMoney;
	/**
	 * 执行金额
	 */
	private java.math.BigDecimal processMoney;
	/**
	 * 币别
	 */
	//@Currency(inject = "curName")
	private java.lang.String curId;
	@Excel(name = "*币别")
	private java.lang.String curName;
	/**
	 * 结算方式  0-现款 1-票据
	 * TODO 数据字典国际化问题，待解决
	 */
	//@Dict(dictGroup = "settlType", inject = "settlTypeName")
	@Excel(name = "结算方式")
	private java.lang.String settlType;
	private java.lang.String settlTypeName;
	/**
	 * 部门
	 */
	@Excel(name = "部门")
	private java.lang.String corpName;
	/**
	 * 用途
	 */
	@Excel(name = "用途")
	private java.lang.String purpose;
	/**
	 * 期望支付日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "期望支付日期")
	private java.util.Date wishPayday;
	private String wishPaydayStr;
	/**
	 * 付款账号
	 */
	@Excel(name = "付款账号")
	private java.lang.String payerAcc;
	/**
	 * 收款人
	 */
	@Excel(name = "收款人")
	private java.lang.String payeeAccName;
	/**
	 * 收款账号
	 */
	@Excel(name = "收款账号")
	private java.lang.String payeeAcc;
	/**
	 * 收款开户行
	 */
	@Excel(name = "收款开户行")
	private java.lang.String payeeBank;
	/**
	 * 合同号
	 */
	@Excel(name = "合同号")
	private java.lang.String contract;
	/**
	 * 归属日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "*归属日期")
	private java.util.Date vestingDate;
	private String vestingDateStr;
	/**
	 * 创建人登录名称
	 */
	private java.lang.String createBy;
	/**
	 * 创建日期
	 */
	private java.util.Date createDate;
	/**
	 * 更新人登录名称
	 */
	private java.lang.String updateBy;
	/**
	 * 更新日期
	 */
	private java.util.Date updateDate;
	/**
	 * 汇总时下级单位的明细id
	 */
	private java.lang.String childCorpDtlId;
	/**
	 * 操作标志add 为追加 input 为录入
	 */
	private String flag;
	/**
	 * 可上报金额
	 */
	private java.math.BigDecimal canBMoney;
	/**
	 * 折合人民币
	 */
	private java.math.BigDecimal natualMoney;
	/**
	 * 折合人民币-临时变量
	 */
	private java.math.BigDecimal tempNatualMoney;
	/**已追加金额折合人民币*/
	private java.math.BigDecimal bsuperNatualMoney;
	/**本次追加金额折合人民币*/
	private java.math.BigDecimal btempNatualMoney;
	/**
	 * 是否为审批修改
	 */
	private String isApprove;

	/**汇率*/
	private BigDecimal exchangeRate;
	/**上报计划币种*/
	private String natualCurId;
	/**追加金额是否可以编辑*/
	private String tempMoneyIsEdit;


	/**
	 * 计划科目
	 */
	private FbsItemVO item = new FbsItemVO();
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
	public java.lang.String getDetailId() {
		return detailId;
	}
	public void setDetailId(java.lang.String detailId) {
		this.detailId = detailId;
	}
	public java.lang.String getSuperaddId() {
		return superaddId;
	}
	public void setSuperaddId(java.lang.String superaddId) {
		this.superaddId = superaddId;
	}
	public java.lang.String getItemCode() {
		return itemCode;
	}
	public void setItemCode(java.lang.String itemCode) {
		this.itemCode = itemCode;
	}
	public java.lang.String getItemName() {
		return itemName;
	}
	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}
	public java.lang.String getTimeType() {
		return timeType;
	}
	public void setTimeType(java.lang.String timeType) {
		this.timeType = timeType;
	}
	public java.math.BigDecimal getBMoney() {
		return bMoney;
	}
	public void setBMoney(java.math.BigDecimal bMoney) {
		this.bMoney = bMoney;
	}
	public BigDecimal getbMoney() {
		return bMoney;
	}
	public void setbMoney(BigDecimal bMoney) {
		this.bMoney = bMoney;
	}
	public java.math.BigDecimal getBsuperMoney() {
		return bsuperMoney;
	}
	public void setBsuperMoney(java.math.BigDecimal bsuperMoney) {
		this.bsuperMoney = bsuperMoney;
	}
	public java.math.BigDecimal getBtempMoney() {
		return btempMoney;
	}
	public void setBtempMoney(java.math.BigDecimal btempMoney) {
		this.btempMoney = btempMoney;
	}
	public java.math.BigDecimal getProcessMoney() {
		return processMoney;
	}
	public void setProcessMoney(java.math.BigDecimal processMoney) {
		this.processMoney = processMoney;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public java.lang.String getSettlType() {
		return settlType;
	}
	public void setSettlType(java.lang.String settlType) {
		this.settlType = settlType;
	}
	public String getSettlTypeName() {
		return settlTypeName;
	}
	public void setSettlTypeName(String settlTypeName) {
		this.settlTypeName = settlTypeName;
	}
	public java.lang.String getCorpName() {
		return corpName;
	}
	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}
	public java.lang.String getPurpose() {
		return purpose;
	}
	public void setPurpose(java.lang.String purpose) {
		this.purpose = purpose;
	}
	public java.lang.String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(java.lang.String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public java.lang.String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(java.lang.String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public java.lang.String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(java.lang.String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public java.lang.String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(java.lang.String payeeBank) {
		this.payeeBank = payeeBank;
	}
	public java.lang.String getContract() {
		return contract;
	}
	public void setContract(java.lang.String contract) {
		this.contract = contract;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getChildCorpDtlId() {
		return childCorpDtlId;
	}
	public void setChildCorpDtlId(String childCorpDtlId) {
		this.childCorpDtlId = childCorpDtlId;
	}
	public Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	public String getWishPaydayStr() {
		return wishPaydayStr;
	}
	public void setWishPaydayStr(String wishPaydayStr) {
		this.wishPaydayStr = wishPaydayStr;
	}
	public Date getVestingDate() {
		return vestingDate;
	}
	public void setVestingDate(Date vestingDate) {
		this.vestingDate = vestingDate;
	}
	public String getVestingDateStr() {
		return vestingDateStr;
	}
	public void setVestingDateStr(String vestingDateStr) {
		this.vestingDateStr = vestingDateStr;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public BigDecimal getCanBMoney() {
		return canBMoney;
	}
	public void setCanBMoney(BigDecimal canBMoney) {
		this.canBMoney = canBMoney;
	}
	public BigDecimal getNatualMoney() {
		return natualMoney;
	}
	public void setNatualMoney(BigDecimal natualMoney) {
		this.natualMoney = natualMoney;
	}
	public FbsItemVO getItem() {
		return item;
	}
	public void setItem(FbsItemVO item) {
		this.item = item;
	}
	public BigDecimal getTempNatualMoney() {
		return tempNatualMoney;
	}
	public void setTempNatualMoney(BigDecimal tempNatualMoney) {
		this.tempNatualMoney = tempNatualMoney;
	}
	public BigDecimal getBsuperNatualMoney() {
		return bsuperNatualMoney;
	}
	public void setBsuperNatualMoney(BigDecimal bsuperNatualMoney) {
		this.bsuperNatualMoney = bsuperNatualMoney;
	}
	public BigDecimal getBtempNatualMoney() {
		return btempNatualMoney;
	}
	public void setBtempNatualMoney(BigDecimal btempNatualMoney) {
		this.btempNatualMoney = btempNatualMoney;
	}
	public String getIsApprove() {
		return isApprove;
	}
	public void setIsApprove(String isApprove) {
		this.isApprove = isApprove;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getNatualCurId() {
		return natualCurId;
	}

	public void setNatualCurId(String natualCurId) {
		this.natualCurId = natualCurId;
	}

	public String getTempMoneyIsEdit() {
		return tempMoneyIsEdit;
	}

	public void setTempMoneyIsEdit(String tempMoneyIsEdit) {
		this.tempMoneyIsEdit = tempMoneyIsEdit;
	}

	@Override
	public String getBeanName() {
		return "fbsBudgetDetailService";
	}
}
