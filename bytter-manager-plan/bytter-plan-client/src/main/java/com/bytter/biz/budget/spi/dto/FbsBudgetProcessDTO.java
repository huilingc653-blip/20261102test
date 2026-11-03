package com.bytter.biz.budget.spi.dto;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Title: Entity
 * Description: 预算执行表
 * @author onlineGenerator
 * @since 2017-07-18 10:48:05
 * @version 1.0
 *
 */
public class FbsBudgetProcessDTO implements java.io.Serializable {
	private String id;
	/**状态*/
	private Integer status;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**单位ID*/
	private String corpId;
	/**科目代码*/
	private String itemCode;
	/**科目名称*/
	private String itemName;
	/**执行金额*/
	private BigDecimal processMoney;
	/**执行日期*/
	private Date processDate;
	/**业务单据号*/
	private String billNo;
	/**业务单ID*/
	private String billId;
	/**币别*/
	private String curId;
	/**结算类型  0-现款 1-票据*/
	private String settlType;
	/**部门*/
	private String deptName;
	/**用途*/
	private String purpose;
	/**期望支付日期*/
	private Date wishPayday;
	/**付款账号*/
	private String payerAcc;
	/**收款账号名称*/
	private String payeeAccName;
	/**收款账号*/
	private String payeeAcc;
	/**收款开户行名称*/
	private String payeeBank;
	/**合同号*/
	private String contract;
	/**归属日期*/
	private Date vestingDate;
	/**备注*/
	private String rmk;
	/**执行业务名称*/
	private String businessName;
	/**执行系统名称*/
	private String processSysname;
	/**内转标识 1-内转 0- 非内转*/
	private java.lang.String innerSign;
	/**汇率*/
	private BigDecimal exchangeRate;
	/**执行金额-折合本身币别*/
	private BigDecimal processCurMoney;
	/**
	 * 单位是否受控 0-否 1-是
	 */
	private Integer controlBudget;
	/**
	 * 单位是否上报  0-否 1-是
	 */
	private Integer reportBudget;
	/**是否计划控制科目 1-是 0-否*/
	private String isControl;

	private String tenantId;


	/**计划上报单位*/
	private String budgetCorpId;
	private String budgetCorpName;


	/**执行明细信息*/
	private String processDetailInfo;


	/**现金流项目*/
	private String cashItem;
	private String cashItemName;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
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
	public BigDecimal getProcessMoney() {
		return processMoney;
	}
	public void setProcessMoney(BigDecimal processMoney) {
		this.processMoney = processMoney;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal getProcessCurMoney() {
		return processCurMoney;
	}
	public void setProcessCurMoney(BigDecimal processCurMoney) {
		this.processCurMoney = processCurMoney;
	}
	public Date getProcessDate() {
		return processDate;
	}
	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getSettlType() {
		return settlType;
	}
	public void setSettlType(String settlType) {
		this.settlType = settlType;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
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
	public Date getVestingDate() {
		return vestingDate;
	}
	public void setVestingDate(Date vestingDate) {
		this.vestingDate = vestingDate;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getProcessSysname() {
		return processSysname;
	}
	public void setProcessSysname(String processSysname) {
		this.processSysname = processSysname;
	}
	public String getInnerSign() {
		return innerSign;
	}
	public void setInnerSign(String innerSign) {
		this.innerSign = innerSign;
	}
	public Integer getControlBudget() {
		return controlBudget;
	}
	public void setControlBudget(Integer controlBudget) {
		this.controlBudget = controlBudget;
	}
	public Integer getReportBudget() {
		return reportBudget;
	}
	public void setReportBudget(Integer reportBudget) {
		this.reportBudget = reportBudget;
	}
	public String getIsControl() {
		return isControl;
	}
	public void setIsControl(String isControl) {
		this.isControl = isControl;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getBudgetCorpId() {
		return budgetCorpId;
	}

	public void setBudgetCorpId(String budgetCorpId) {
		this.budgetCorpId = budgetCorpId;
	}

	public String getBudgetCorpName() {
		return budgetCorpName;
	}

	public void setBudgetCorpName(String budgetCorpName) {
		this.budgetCorpName = budgetCorpName;
	}

	public String getProcessDetailInfo() {
		return processDetailInfo;
	}

	public void setProcessDetailInfo(String processDetailInfo) {
		this.processDetailInfo = processDetailInfo;
	}

	public String getCashItem() {
		return cashItem;
	}

	public void setCashItem(String cashItem) {
		this.cashItem = cashItem;
	}

	public String getCashItemName() {
		return cashItemName;
	}

	public void setCashItemName(String cashItemName) {
		this.cashItemName = cashItemName;
	}
}
