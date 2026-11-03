package com.bytter.biz.budget.vo;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
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
@ExcelTarget("FbsBudgetInputDetailVO")
public class FbsBudgetInputDetailVO extends ExcelModel implements java.io.Serializable {
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
	private String detailId;
	/**
	 * 追加ID
	 */
	private String superaddId;
	/**
	 * 科目代码
	 */
	@Excel(name = "*科目")
	private String itemCode;
	/**
	 * 科目名称
	 */
	@Excel(name = "科目名称")
	private String itemName;
	/**
	 * 计划周期类型 1-年 2-季 3-月 4-周 5-天
	 */
	private String timeType;
	/**
	 * 上报金额
	 */
	@Excel(name = "*上报金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private BigDecimal bMoney;
	/**
	 * 追加金额
	 */
	private BigDecimal bsuperMoney;
	/**本次追加金额*/
	/**
	 * 上报金额
	 */
	@Excel(name = "*追加金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	private BigDecimal btempMoney;
	/**
	 * 执行金额
	 */
	private BigDecimal processMoney;
	/**
	 * 币别
	 * 没有查询到具体使用
	 */
	//@Currency(inject = "curName")
	private String curId;
	@Excel(name = "*币别")
	private String curName;
	/**
	 * 结算方式  0-现款 1-票据
	 * TODO 数据字典国际化问题，待解决
	 */
	@Dict(dictGroup = "settlType", inject = "settlTypeName")
	@Excel(name = "结算方式")
	private String settlType;
	private String settlTypeName;
	/**
	 * 部门
	 */
	@Excel(name = "部门")
	private String corpName;
	/**
	 * 用途
	 */
	@Excel(name = "用途")
	private String purpose;
	/**
	 * 期望支付日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "期望支付日期")
	private Date wishPayday;
	private String wishPaydayStr;
	/**
	 * 付款账号
	 */
	@Excel(name = "付款账号")
	private String payerAcc;
	/**
	 * 收款人
	 */
	@Excel(name = "收款人")
	private String payeeAccName;
	/**
	 * 收款账号
	 */
	@Excel(name = "收款账号")
	private String payeeAcc;
	/**
	 * 收款开户行
	 */
	@Excel(name = "收款开户行")
	private String payeeBank;
	/**
	 * 合同号
	 */
	@Excel(name = "合同号")
	private String contract;
	/**
	 * 归属日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Excel(name = "*归属日期")
	private Date vestingDate;
	private String vestingDateStr;
	/**
	 * 创建人登录名称
	 */
	private String createBy;
	/**
	 * 创建日期
	 */
	private Date createDate;
	/**
	 * 更新人登录名称
	 */
	private String updateBy;
	/**
	 * 更新日期
	 */
	private Date updateDate;
	/**
	 * 汇总时下级单位的明细id
	 */
	private String childCorpDtlId;
	/**
	 * 操作标志add 为追加 input 为录入
	 */
	private String flag;
	/**
	 * 可上报金额
	 */
	private BigDecimal canBMoney;
	/**
	 * 折合人民币
	 */
	private BigDecimal natualMoney;
	/**
	 * 折合人民币-临时变量
	 */
	private BigDecimal tempNatualMoney;
	/**已追加金额折合人民币*/
	private BigDecimal bsuperNatualMoney;
	/**本次追加金额折合人民币*/
	private BigDecimal btempNatualMoney;
	/**
	 * 是否为审批修改
	 */
	private String isApprove;
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
	public String getDetailId() {
		return detailId;
	}
	public void setDetailId(String detailId) {
		this.detailId = detailId;
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
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public BigDecimal getBMoney() {
		return bMoney;
	}
	public void setBMoney(BigDecimal bMoney) {
		this.bMoney = bMoney;
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
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getSettlType() {
		return settlType;
	}
	public void setSettlType(String settlType) {
		this.settlType = settlType;
	}
	public String getSettlTypeName() {
		return settlTypeName;
	}
	public void setSettlTypeName(String settlTypeName) {
		this.settlTypeName = settlTypeName;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
	}
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
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
	@Override
	public String getBeanName() {
		return "fbsBudgetInputDetailService";
	}
}
