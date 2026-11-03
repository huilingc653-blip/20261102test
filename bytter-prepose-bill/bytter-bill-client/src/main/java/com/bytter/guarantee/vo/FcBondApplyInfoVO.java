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
/**
 * @author fusb
 * @version 1.0
 * @Title FcBondApplyVO
 * @Description
 * @date 2018年9月19日
 */
@ExcelTarget("FcBondApplyVO")
public class FcBondApplyInfoVO extends ExcelModel implements Serializable,  IBusinessKey {
	private static final long serialVersionUID = 5551223901154715791L;
	private String id;
	/** 业务单号 */
	@Excel(name = "*业务单号")
	@BtCompositeSearch(fieldName = "c.bill_no")  
	private String billNo;
	/** 单位ID */
	@BtCompositeSearch(fieldName = "b.corp_id")
	@Corp(inject = "corpName")
	private String corpId;
	/** 单位名称 */
	@Excel(name = "*单位名称")
	@BtCompositeSearch(fieldName = "b.corp_id")
	private String corpName;  
	/**币别*/
	@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	private String curName;
	/** 存入银行 */
	@Excel(name = "*存入银行")
	@BtCompositeSearch(fieldName = "b.bank_name")
	private String bankName;
	/** 存入账户 */
	@Excel(name = "*存入账户")
	@BtCompositeSearch(fieldName = "b.bank_acc")
	private String bankAcc;
	/** 账户名称 */
	@Excel(name = "*账户名称")
	@BtCompositeSearch(fieldName = "b.acc_name")
	private String accName;
	/** 占用类型 */
	@BtCompositeSearch(fieldName = "c.apply_type")
	private String applyType;
	/** 项目类型 */
	@BtCompositeSearch(fieldName = "c.item_type")
	private String itemType;
	/** 项目单号 */
	@Excel(name = "*项目单号")
	@BtCompositeSearch(fieldName = "c.item_no")
	private String itemNo;
	/**项目ID*/
	private String itemId;
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
	private java.util.Date startDate;
	/** 项目结束日期 */
	@Excel(name = "*项目结束日期", format = "yyyy-MM-dd")
	@NotNull(message = "nis.netbank.message.expenseDate.null")
	@BtCompositeSearch(fieldName = "c.end_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endDate;
	/** 项目期限 */
	private Integer itemUnit;
	/** 保证金合同 */
	private String bondContract;
	/** 保证金ID */
	private String bondId;
	/** 占用金额 */
	private BigDecimal applyMoney;
	/** 保证金账户金额 */
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
	private java.util.Date createDate;
	/** 更新人登录名称 */
	private String updateBy;
	/** 更新日期 */
	private java.util.Date updateDate;
	/** 录入日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date inputDate;
	/** 可用金额 */
	private BigDecimal availMoney;
	/** 项目的保证金金额 */
	private BigDecimal bondMoney;
	/** 保证金存入日期 */
	private java.util.Date bondDate;
	/**保证金利率*/
	private BigDecimal bondRate;
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
	public java.util.Date getBondDate() {
		return bondDate;
	}
	public void setBondDate(java.util.Date bondDate) {
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
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	@Override
	public String getBusinessKey() {
		return getId();
	}
	public BigDecimal getBondRate() {
		return bondRate;
	}
	public void setBondRate(BigDecimal bondRate) {
		this.bondRate = bondRate;
	}
}
