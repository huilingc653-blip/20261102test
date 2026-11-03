package com.bytter.guarantee.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fusb
 * @version 1.0
 * @Title FcBondApplyVO
 * @Description
 * @date 2018年9月19日
 */
@ExcelTarget("FcBondApplyVO")
public class FcBondApplyVO extends ExcelModel implements Serializable,  IBusinessKey {
	private static final long serialVersionUID = 5551223901154715791L;
	private String id;
	/** 业务单号 */
	@Excel(name = "*业务单号")
	@BtCompositeSearch(fieldName = "c.bill_no")
	private String billNo;
	/** 单位ID */
	@BtCompositeSearch(fieldName = "C.corp_id")
	@Corp(inject = "corpName")
	private String corpId;
	/** 单位名称 */
	@Excel(name = "*单位名称")
	@BtCompositeSearch(fieldName = "C.corp_id")
	private String corpName;
	private String bankAccId;
	/** 存入银行 */
	@Excel(name = "*存入银行")
	@BtCompositeSearch(fieldName = "A.bank_name")
	private String bankName;
	/** 存入账户 */
	@Excel(name = "*存入账户")
	@BtCompositeSearch(fieldName = "A.bank_acc")
	private String bankAcc;
	/** 账户名称 */
	@Excel(name = "*账户名称")
	@BtCompositeSearch(fieldName = "co.acc_name")
	private String accName;
	/** 占用类型 */
	@BtCompositeSearch(fieldName = "C.apply_type")
	private String applyType;
	/** 项目类型 */
	@BtCompositeSearch(fieldName = "C.item_type")
	private String itemType;
	/** 项目单号 */
	@Excel(name = "*项目单号")
	@BtCompositeSearch(fieldName = "C.item_no")
	private String itemNo;
	/** 银行ID */
	private String bankId;
	/** 项目金额 */
	private BigDecimal itemMoney;
	/** 保证金比例 */
	private BigDecimal bondPercent;
	/** 项目开始日期 */
	@Excel(name = "*项目开始日期", format = "yyyy-MM-dd")
	@NotNull(message = "nis.netbank.message.expenseDate.null")
	@BtCompositeSearch(fieldName = "c.start_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/** 项目结束日期 */
	@Excel(name = "*项目结束日期", format = "yyyy-MM-dd")
	@NotNull(message = "nis.netbank.message.expenseDate.null")
	@BtCompositeSearch(fieldName = "c.end_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/** 项目期限 */
	private Integer itemUnit;
	/** 保证金合同 */
	private String bondContract;
	/** 保证金ID */
	@BtCompositeSearch(fieldName = "c.bond_id")
	private String bondId;
	/** 占用金额 */
	@BtCompositeSearch(fieldName = "C.APPLY_MONEY")
	private BigDecimal applyMoney;
	private BigDecimal beginApplyMoney;
	private BigDecimal endApplyMoney;
	/** 保证金账户金额 */
	@BtCompositeSearch(fieldName = "D.BOND_MONEY+D.ADD_MONEY-D.BACK_MONEY")
	private BigDecimal balMoney;
	/** 备注 */
	private String remark;
	/** 状态 */
	private Integer status;
	/** 创建人登录名称 */
	private String createBy;
	/** 创建日期 */
	@Excel(name = "*录入日期", format = "yyyy-MM-dd")
	@NotNull(message = "nis.netbank.message.expenseDate.null")
	@BtCompositeSearch(fieldName = "c.create_date")
	private Date createDate;
	/** 更新人登录名称 */
	private String updateBy;
	/** 更新日期 */
	private Date updateDate;
	/** 录入日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date inputDate;
	/** 可用金额 */
	private BigDecimal availMoney;
	/** 项目的保证金金额 */
	private BigDecimal bondMoney;
	private BigDecimal beginBondMoney;
	private BigDecimal endBondMoney;
	/** 保证金存入日期 */
	private Date bondDate;
	/**币别*/
	@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	@Excel(name = "*币别")
	@BtCompositeSearch(fieldName = "A.Cur_Id")
	private String curName;
	/**大区：票据单位对应的二级管理单元*/
	private String parentId;
	private String parentName;

	/**营运区:单位信息表中的营运区*/
	private String operationAreaCode;
	private String operationAreaName;

	/**公司类型：单位表中的股权属性*/
	private String equityCode;
	private String equityName;

	/**票据号*/
	private String origNo;
	/**付款单号*/
	private String payBillNo;
	/**共享单号*/
	private String bursarBillNo;

	/** 释放日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date releaseDate;
	/** 自动回转标志*/
	private String autoSign;

	public BigDecimal getBalMoney() {
		return balMoney;
	}
	public void setBalMoney(BigDecimal balMoney) {
		this.balMoney = balMoney;
	}
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
	public BigDecimal getItemMoney() {
		return itemMoney;
	}
	public void setItemMoney(BigDecimal itemMoney) {
		this.itemMoney = itemMoney;
	}
	public BigDecimal getBondPercent() {
		return bondPercent;
	}
	public void setBondPercent(BigDecimal bondPercent) {
		this.bondPercent = bondPercent;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
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
	public BigDecimal getApplyMoney() {
		return applyMoney;
	}
	public void setApplyMoney(BigDecimal applyMoney) {
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
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getBankAccId() {
		return bankAccId;
	}
	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	public Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}
	public BigDecimal getAvailMoney() {
		return availMoney;
	}
	public void setAvailMoney(BigDecimal availMoney) {
		this.availMoney = availMoney;
	}
	public BigDecimal getBondMoney() {
		return bondMoney;
	}
	public void setBondMoney(BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}
	public Date getBondDate() {
		return bondDate;
	}
	public void setBondDate(Date bondDate) {
		this.bondDate = bondDate;
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

	public BigDecimal getBeginApplyMoney() {
		return beginApplyMoney;
	}

	public void setBeginApplyMoney(BigDecimal beginApplyMoney) {
		this.beginApplyMoney = beginApplyMoney;
	}

	public BigDecimal getEndApplyMoney() {
		return endApplyMoney;
	}

	public void setEndApplyMoney(BigDecimal endApplyMoney) {
		this.endApplyMoney = endApplyMoney;
	}

	public BigDecimal getBeginBondMoney() {
		return beginBondMoney;
	}

	public void setBeginBondMoney(BigDecimal beginBondMoney) {
		this.beginBondMoney = beginBondMoney;
	}

	public BigDecimal getEndBondMoney() {
		return endBondMoney;
	}

	public void setEndBondMoney(BigDecimal endBondMoney) {
		this.endBondMoney = endBondMoney;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getOperationAreaCode() {
		return operationAreaCode;
	}

	public void setOperationAreaCode(String operationAreaCode) {
		this.operationAreaCode = operationAreaCode;
	}

	public String getOperationAreaName() {
		return operationAreaName;
	}

	public void setOperationAreaName(String operationAreaName) {
		this.operationAreaName = operationAreaName;
	}

	public String getEquityCode() {
		return equityCode;
	}

	public void setEquityCode(String equityCode) {
		this.equityCode = equityCode;
	}

	public String getEquityName() {
		return equityName;
	}

	public void setEquityName(String equityName) {
		this.equityName = equityName;
	}

	public String getOrigNo() {
		return origNo;
	}

	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}

	public String getPayBillNo() {
		return payBillNo;
	}

	public void setPayBillNo(String payBillNo) {
		this.payBillNo = payBillNo;
	}

	public String getBursarBillNo() {
		return bursarBillNo;
	}

	public void setBursarBillNo(String bursarBillNo) {
		this.bursarBillNo = bursarBillNo;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getAutoSign() {
		return autoSign;
	}

	public void setAutoSign(String autoSign) {
		this.autoSign = autoSign;
	}

	@Override
	public String getBusinessKey() {
		return getId();
	}
}
