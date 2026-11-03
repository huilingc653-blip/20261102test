package com.bytter.guarantee.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:FcBondPreserveVO
 * @Description:
 * @date 2018年9月18日 上午11:14:27
 */
@ExcelTarget("FcBondPreserveVO")
public class FcBondPreserveVO extends ExcelModel implements Serializable,  IBusinessKey { 
	private static final long serialVersionUID = 5551223901154715791L;
	private String id;
	/** 业务单号 */
	@BtCompositeSearch(fieldName = "m.bill_no")
	private String billNo;
	/** 占用类型 */
	private String applyType;
	/** 项目类型 */
	private String itemType;
	/** 项目单号 */
	@BtCompositeSearch(fieldName = "M.ITEM_NO")
	private String itemNo;
	/** 单位ID */
	@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName = "m.corp_id")
	private String corpId;
	@BtCompositeSearch(fieldName = "m.corp_id")
	private String corpName;
	/** 银行ID */
	private String bankId;
	/** 银行名称 */
	@BtCompositeSearch(fieldName = "P.BANK_ACC_ID")
	private String bankName;
	/** 项目金额 */
	private java.math.BigDecimal itemMoney;
	/** 保证金比例 */
	private java.math.BigDecimal bondPercent;
	/** 项目开始日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date startDate;
	/** 项目结束日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "m.end_date")
	private java.util.Date endDate;
	/** 项目期限 */
	private Integer itemUnit;
	/** 保证金合同 */
	private String bondContract;
	/** 保证金ID */
	private String bondId;
	/** 占用金额 */
	@BtCompositeSearch(fieldName = "m.apply_money")
	private java.math.BigDecimal applyMoney;
	/** 备注 */
	private String remark;
	/** 状态 */
	@BtCompositeSearch(fieldName = "m.status")
	@Dict(dictGroup = "fcBond.status", inject = "statusName")
	private Integer status;
	private String statusName;
	/** 创建人登录名称 */
	private String createBy;
	/** 创建日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createDate;
	/** 更新人登录名称 */
	private String updateBy;
	/** 更新日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date updateDate;

	private String bankAccId;
	/** 存入账户 */
	@BtCompositeSearch(fieldName = "P.BANK_ACC_ID")
	private String bankAcc;
	/** 账户名称 */
	@BtCompositeSearch(fieldName = "P.BANK_ACC_ID")
	private String accName;
	/** 录入日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "m.create_date")
	private java.util.Date inputDate;
	/** 账户当前可用金额 */
	private java.math.BigDecimal availMoney;
	/** 账户当前余额 */
	@BtCompositeSearch(fieldName = "(q.bond_money+q.add_money-q.back_money)")
	private java.math.BigDecimal accBalance;
	/** 新增/修改标志 2-新增 6-修改 */
	private String addOrUpdateFlag;
	/** 项目的保证金金额 */
	private java.math.BigDecimal bondMoney;
	/** 保证金存入日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date bondDate;
	/**币别*/
	@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	@Excel(name = "*币别")
	@BtCompositeSearch(fieldName = "n.cur_Id")
	private String curName;
	/** 释放ID */
	// private java.lang.String releaseId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getApplyType() {
		return applyType;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public java.math.BigDecimal getItemMoney() {
		return itemMoney;
	}
	public void setItemMoney(java.math.BigDecimal itemMoney) {
		this.itemMoney = itemMoney;
	}
	public java.math.BigDecimal getBondPercent() {
		return bondPercent;
	}
	public void setBondPercent(java.math.BigDecimal bondPercent) {
		this.bondPercent = bondPercent;
	}
	public java.util.Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	public Integer getItemUnit() {
		return itemUnit;
	}
	public void setItemUnit(Integer itemUnit) {
		this.itemUnit = itemUnit;
	}
	public String getBondContract() {
		return bondContract;
	}
	public void setBondContract(String bondContract) {
		this.bondContract = bondContract;
	}
	public String getBondId() {
		return bondId;
	}
	public void setBondId(String bondId) {
		this.bondId = bondId;
	}
	public java.math.BigDecimal getApplyMoney() {
		return applyMoney;
	}
	public void setApplyMoney(java.math.BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccId() {
		return bankAccId;
	}

	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}

	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public java.util.Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(java.util.Date inputDate) {
		this.inputDate = inputDate;
	}
	public java.math.BigDecimal getAvailMoney() {
		return availMoney;
	}
	public void setAvailMoney(java.math.BigDecimal availMoney) {
		this.availMoney = availMoney;
	}
	public java.math.BigDecimal getAccBalance() {
		return accBalance;
	}
	public void setAccBalance(java.math.BigDecimal accBalance) {
		this.accBalance = accBalance;
	}
	public String getAddOrUpdateFlag() {
		return addOrUpdateFlag;
	}
	public void setAddOrUpdateFlag(String addOrUpdateFlag) {
		this.addOrUpdateFlag = addOrUpdateFlag;
	}
	public java.math.BigDecimal getBondMoney() {
		return bondMoney;
	}
	public void setBondMoney(java.math.BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}
	public java.util.Date getBondDate() {
		return bondDate;
	}
	public void setBondDate(java.util.Date bondDate) {
		this.bondDate = bondDate;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
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
	@Override
	public String getBusinessKey() {
		return getId();
	}
}
