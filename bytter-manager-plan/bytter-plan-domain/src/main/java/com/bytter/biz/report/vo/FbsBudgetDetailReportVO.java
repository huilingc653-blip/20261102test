package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FBS_BUDGET_DETAIL
 * @date 2017-07-19 14:42:18
 */
@ExcelTarget("fbsBudgetDetailReportVO")
public class FbsBudgetDetailReportVO extends ExcelModel implements java.io.Serializable {
	private static final long serialVersionUID = 2029677864327828735L;
	/**
	 * 单位id
	 */
	//@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName = "b.corp_id")
	private String corpId;


	/**
	 * 单位名称
	 */
    @BtCompositeSearch(fieldName = " b.corp_name")
    private String corpName;
	/**
	 * *科目Id
	 * 本域表：FBS_ITEM
	 */
	//@Dict(tableName="FBS_ITEM",valueFieldName="id",showFiledName="item_name",inject="itemName")
	private String itemId;
	/**
	 * 科目代码
	 */
	@BtCompositeSearch(fieldName = "d.item_Code")
	private String itemCode;
	/**
	 * 科目名称
	 */
	@BtCompositeSearch(fieldName = "d.item_Name")
	private String itemName;
	/**
	 * 计划周期类型 1-年 2-季 3-月 4-周 5-天
	 */
	private String timeType;
	/**
	 * 上报金额
	 */
	private BigDecimal bMoney;
	/**
	 * 追加金额
	 */
	private BigDecimal bsuperMoney;
	/**
	 * 上报金额
	 */
	private BigDecimal btempMoney;
	/**
	 * 执行金额
	 */
	private BigDecimal processMoney;
	/**
	 * 币别
	 */
	//@Currency(inject = "curName")
	@BtCompositeSearch(fieldName = "b.cur_Id")
	private String curId;
	private String curName;
	/**
	 * 结算方式  0-现款 1-票据
	 * 1.查询 sys_dict_group --> sys_dict --> id --> (sys_internation_base 或 sys_internation) b --> id = b.KEY_ID and LANG_CODE = LocaleContextHolder.getLocale()
	 * sys_dict_group.type_group_code = settlType
	 * sys_dict.type_code = settlType字段的值
	 *
	 * TODO 数据字典国际化问题，待解决
	 */
	@Dict(dictGroup = "settlType", inject = "settlTypeName")
    @BtCompositeSearch(fieldName = "d.settl_type")
	private String settlType;


	private String settlTypeName;
	/**
	 * 部门
	 */
	@BtCompositeSearch(fieldName = "d.corp_Name")
	private String deptName;
	/**
	 * 用途
	 */
    @BtCompositeSearch(fieldName = "d.purpose")
    private String purpose;
	/**
	 * 期望支付日期
	 */

    @BtCompositeSearch(fieldName = "d.wish_payday")
    private Date wishPayday;
	/**
	 * 付款账号
	 */
    @BtCompositeSearch(fieldName = "d.payer_acc")
    private String payerAcc;
	/**
	 * 收款人
	 */
    @BtCompositeSearch(fieldName = "d.payee_acc_name")
    private String payeeAccName;
	/**
	 * 收款账号
	 */
    @BtCompositeSearch(fieldName = "d.payee_acc")
    private String payeeAcc;
	/**
	 * 收款开户行
	 */
    @BtCompositeSearch(fieldName = "d.payee_bank")
    private String payeeBank;
	/**
	 * 合同号
	 */
	private String contract;
	/**
	 * 归属日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "d.vesting_Date")
	private Date vestingDate;
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
}
