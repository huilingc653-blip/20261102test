package com.bytter.guarantee.dto;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:FcBondAddVO
 * @Description:
 * @date 2018年9月12日 上午11:01:21
 */
@SuppressWarnings("serial")
@ExcelTarget("FcBondAddVO")
public class FcBondAddDTO extends ExcelModel implements Serializable,  IBusinessKey {
	private String id;
	/** 业务单号 */
	@BtCompositeSearch(fieldName = "m.bill_no")
	private String billNo;
	/** 网点id */
	@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName = "b.corp_id")
	private String corpId;
	/** 单位名称 */
	@Excel(name = "*单位名称")
	@BtCompositeSearch(fieldName = "b.corp_id")
	@NotNull(message = "nis.netbank.message.corpname.null")
	private String corpName;
	private String bankAccId;
	/** 存入银行 */
	@Excel(name = "*存入银行")
	@BtCompositeSearch(fieldName = "b.bank_acc_id")
	private String bankName;
	/** 存入账户 */
	@BtCompositeSearch(fieldName = "b.bank_acc_id")
	private String bankAcc;
	/** 账户名称 */
	@BtCompositeSearch(fieldName = "b.bank_acc_id")
	private String accName;

	/** 保证金ID */
	@BtCompositeSearch(fieldName = "B.BOND_ID")
	private String bondId;
	/** 追加日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "B.ADD_DATE")
	private java.util.Date addDate;
	private String addDateStr;
	/** 是否已付款 */
	@Excel(name = "*是否已付款")
	@Dict(dictGroup = "sf_yn", inject = "isPayedName")
	private String isPayed;
	private String isPayedName;
	/** 新追加金额 */
	@BtCompositeSearch(fieldName = "m.add_money")
	private BigDecimal addMoney;
	/** 保证金金额 */
	@BtCompositeSearch(fieldName = "b.bond_money")
	private BigDecimal bondMoney;
	/** 追加备注 */
	private String remark;
	/** 状态 */
	@BtCompositeSearch(fieldName = "m.status")
	private Integer status;

	private String statusName;
	/** 创建人登录名称 */
	private String createBy;
	/**登录人名*/
	private String createByName;
	/** 创建日期 */
	@Excel(name = "*存入日期", format = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "m.create_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createDate;
	/** 更新人登录名称 */
	private String updateBy;
	/** 更新日期 */
	private java.util.Date updateDate;
	/** 新增/修改标志位 2-新增 6-修改 */
	private String addOrUpdateFlag;
	/** 历史追加金额总和 */
	private BigDecimal sumAddMoney;
	/**币别*/
	@BtCompositeSearch(fieldName = "b.cur_Id")
	@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	@Excel(name = "*币别")
	private String curName;
	/**存出账号*/
	private String outBankAcc;
	/**用途*/
	private String purpose;
	/**转出账户余额*/
	private BigDecimal outBankAccMoney;
	/**是否上线*/
	private String isOnline;

	private String order;

	private String saveType;

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
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
	public String getBondId() {
		return bondId;
	}
	public void setBondId(String bondId) {
		this.bondId = bondId;
	}
	public java.util.Date getAddDate() {
		return addDate;
	}
	public void setAddDate(java.util.Date addDate) {
		this.addDate = addDate;
	}
	public String getIsPayed() {
		return isPayed;
	}
	public void setIsPayed(String isPayed) {
		this.isPayed = isPayed;
	}
	public String getIsPayedName() {
		return isPayedName;
	}
	public void setIsPayedName(String isPayedName) {
		this.isPayedName = isPayedName;
	}
	public BigDecimal getBondMoney() {
		return bondMoney;
	}
	public void setBondMoney(BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}
	public BigDecimal getAddMoney() {
		return addMoney;
	}
	public void setAddMoney(BigDecimal addMoney) {
		this.addMoney = addMoney;
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
	public String getAddOrUpdateFlag() {
		return addOrUpdateFlag;
	}
	public void setAddOrUpdateFlag(String addOrUpdateFlag) {
		this.addOrUpdateFlag = addOrUpdateFlag;
	}
	public BigDecimal getSumAddMoney() {
		return sumAddMoney;
	}
	public void setSumAddMoney(BigDecimal sumAddMoney) {
		this.sumAddMoney = sumAddMoney;
	}

	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
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
	public String getOutBankAcc() {
		return outBankAcc;
	}
	public void setOutBankAcc(String outBankAcc) {
		this.outBankAcc = outBankAcc;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public BigDecimal getOutBankAccMoney() {
		return outBankAccMoney;
	}
	public void setOutBankAccMoney(BigDecimal outBankAccMoney) {
		this.outBankAccMoney = outBankAccMoney;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public String getAddDateStr() {
		return addDateStr;
	}

	public void setAddDateStr(String addDateStr) {
		this.addDateStr = addDateStr;
	}
}
