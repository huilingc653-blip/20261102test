package com.bytter.system.api.dto;

import java.math.BigDecimal;
import java.util.Date;

/**   
 * Title: Entity
 * Description: 预算明细表
 * @author zuoy
 * @since 2018-03-01
 *
 */

public class FbsBudgetDetailDTO implements java.io.Serializable {
	private String id;
	/**预算id*/
	private String budgetId;
	/**明细ID*/
	private String detailId;
	/**追加ID*/
	private String superaddId;
	/**科目代码*/
	private String itemCode;
	/**科目名称*/
	private String itemName;
	/**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
	private String timeType;
	/**上报金额*/
	private BigDecimal bMoney;
	/**追加金额*/
	private BigDecimal bsuperMoney;
	/**本次追加金额*/
	/**上报金额*/
	private BigDecimal btempMoney;
	/**执行金额*/
	private BigDecimal processMoney;
	/**币别*/
	private String curId;
	/**结算方式  0-现款 1-票据*/
	private String settlType;
	/**部门*/
	private String corpName;
	/**用途*/
	private String purpose;
	/**期望支付日期*/
	private Date wishPayday;
	/**付款账号*/
	private String payerAcc;
	/**收款人*/
	private String payeeAccName;
	/**收款账号*/
	private String payeeAcc;
	/**收款开户行*/
	private String payeeBank;
	/**合同号*/
	private String contract;
	/**归属日期*/
	private Date vestingDate;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**汇总时下级单位的明细id*/
	private String childCorpDtlId;
	/**操作标志add 为追加 input 为录入*/
	private String flag;
	/**顺序号*/
	private Integer orderNo;
	/**所属单位*/
	private String busCorpId;
	private String busCorpName;

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

	public String getSettlType() {
		return settlType;
	}

	public void setSettlType(String settlType) {
		this.settlType = settlType;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getBusCorpId() {
		return busCorpId;
	}

	public void setBusCorpId(String busCorpId) {
		this.busCorpId = busCorpId;
	}

	public String getBusCorpName() {
		return busCorpName;
	}

	public void setBusCorpName(String busCorpName) {
		this.busCorpName = busCorpName;
	}
}
