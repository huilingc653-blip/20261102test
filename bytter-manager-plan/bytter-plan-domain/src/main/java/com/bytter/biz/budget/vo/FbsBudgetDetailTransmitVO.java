package com.bytter.biz.budget.vo;
import java.lang.String;
/**   
 * @Title: Entity
 * @Description: FBS_BUDGET_DETAIL
 * @author onlineGenerator
 * @date 2017-07-19 14:42:18
 * @version 1.0
 *
 */
public class FbsBudgetDetailTransmitVO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7892981830390379364L;
	
	
	private String[] id;
	/**预算主表id*/
	private String budgetId;
	/**明细id*/
	private String detailId;
	/**上报金额*/
	private String[] bMoney;
	/**本次追加金额*/
	private String[] btempMoney;
	/**币别*/
	private String[] curId;
	/**结算类型  0-现款 1-票据*/
	private String[] settlType;
	/**部门*/
	private String[] corpName;
	/**用途*/
	private String[] purpose;
	/**期望支付日期*/
	private String[] wishPayday;
	/**付款账号*/
	private String[] payerAcc;
	/**收款账号名称*/
	private String[] payeeAccName;
	/**收款账号*/
	private String[] payeeAcc;
	/**收款开户行名称*/
	private String[] payeeBank;
	/**合同号*/
	private String[] contract;
	/**归属日期*/
	private String[] vestingDate;
	/**标志 判断是计划录入 还是计划追加 input为录入 add为追加*/
	private String flag;
	/**折合人民币-临时变量*/
	private String[] tempNatualMoney;
	public String[] getbMoney() {
		return bMoney;
	}
	public void setbMoney(String[] bMoney) {
		this.bMoney = bMoney;
	}
	public String[] getBtempMoney() {
		return btempMoney;
	}
	public void setBtempMoney(String[] btempMoney) {
		this.btempMoney = btempMoney;
	}
	public String[] getCurId() {
		return curId;
	}
	public void setCurId(String[] curId) {
		this.curId = curId;
	}
	public String[] getSettlType() {
		return settlType;
	}
	public void setSettlType(String[] settlType) {
		this.settlType = settlType;
	}
	public String[] getCorpName() {
		return corpName;
	}
	public void setCorpName(String[] corpName) {
		this.corpName = corpName;
	}
	public String[] getPurpose() {
		return purpose;
	}
	public void setPurpose(String[] purpose) {
		this.purpose = purpose;
	}
	public String[] getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(String[] wishPayday) {
		this.wishPayday = wishPayday;
	}
	public String[] getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String[] payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String[] getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String[] payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public String[] getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String[] payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String[] getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String[] payeeBank) {
		this.payeeBank = payeeBank;
	}
	public String[] getContract() {
		return contract;
	}
	public void setContract(String[] contract) {
		this.contract = contract;
	}
	public String[] getVestingDate() {
		return vestingDate;
	}
	public void setVestingDate(String[] vestingDate) {
		this.vestingDate = vestingDate;
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
	public String[] getId() {
		return id;
	}
	public void setId(String[] id) {
		this.id = id;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String[] getTempNatualMoney() {
		return tempNatualMoney;
	}
	public void setTempNatualMoney(String[] tempNatualMoney) {
		this.tempNatualMoney = tempNatualMoney;
	}
}
