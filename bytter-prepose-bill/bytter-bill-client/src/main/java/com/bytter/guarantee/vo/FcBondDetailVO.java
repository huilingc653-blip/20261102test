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

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author fusb
 * @version 1.0
 * @Title FcBondDetailVo
 * @Description
 * @date 2018年9月6日
 */
@ExcelTarget("FcBondDetailVO")
public class FcBondDetailVO extends ExcelModel implements Serializable,  IBusinessKey { 
	private static final long serialVersionUID = 8518844566933081253L;
	private String id;
	/** 业务单号 */
	@Excel(name = "*申请单号")
	@BtCompositeSearch(fieldName = "b.bill_no")
	private String billNo;
	/** 单位ID */
	@BtCompositeSearch(fieldName = "b.corp_id")
	@Corp(inject = "corpName")
	private String corpId;
	/** 单位名称 */
	@Excel(name = "*单位名称")
	@NotNull(message = "nis.netbank.message.corpname.null")
	@BtCompositeSearch(fieldName = "b.corp_id")
	private String corpName;
	/** 银行账号ID */
	@BtCompositeSearch(fieldName = "b.bank_acc_id")
	private String bankAccId;
	/** 存入银行 */
	@Excel(name = "*存入银行")
	@BtCompositeSearch(fieldName = "b.bank_Name")
	private String bankName;
	/** 存入账户 */
	@Excel(name = "*存入账户")
	@BtCompositeSearch(fieldName = "b.bank_Acc")
	private String bankAcc;
	/** 账户名称 */
	@Excel(name = "*账户名称")
	@BtCompositeSearch(fieldName = "b.acc_Name")
	private String accName;
	/** 存入日期 */
	@Excel(name = "*存入日期", format = "yyyy-MM-dd")
	@NotNull(message = "nis.netbank.message.expenseDate.null")
	@BtCompositeSearch(fieldName = "b.start_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/** 是否已付款 */
	@Excel(name = "*是否已付款")
	@BtCompositeSearch(fieldName = "b.is_payed")
	@Dict(dictGroup = "sf_yn", inject = "isPayedName")
	private String isPayed;
	private String isPayedName;
	/** 保证金金额 */
	@Excel(name = "*保证金金额")
	@BtCompositeSearch(fieldName = "b.bond_money")
	private BigDecimal bondMoney;
	/**币别*/
	@Currency(inject="curName")
	@NotNull( message = "nis.netbank.message.curname.null")
	private String curId;
	/**币别名称*/
	@Excel(name = "*币别")
	@BtCompositeSearch(fieldName = "b.cur_Id")
	private String curName;
	/** 备注 */
	private String remark;
	/** 状态 */
	@Excel(name = "*状态")
	@Dict(dictGroup = "common.status", inject = "statusName")
	@BtCompositeSearch(fieldName = "b.status")
	private Integer status;

	private String statusName;
	/** 创建人登录名称 */
	private String createBy;
	/** 录入日期 */
	@Excel(name = "*录入日期", format = "yyyy-MM-dd")
	@NotNull(message = "nis.netbank.message.expenseDate.null")
	@BtCompositeSearch(fieldName = "b.create_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	/** 创建日期 */
	private Date createDate;
	/** 更新人登录名称 */
	private String updateBy;
	/** 更新日期 */
	private Date updateDate;
	/** 附件ID */
	private String attachmentId;
	/** 保证金账户余额 */
	private BigDecimal balMoney;
	/** 可用余额 */
	private BigDecimal availMoney;
	/**存出账号*/
	private String outBankAcc;
	/**保证金类型*/
	private String bondTypeId;
	/**利率*/
	private BigDecimal interestRate;
	/**利率*/
	private String interestRateName;
	/**到期日期*/
	private Date endDate;
	/**是否存入定期*/
	private String isRegular;

	private String isRegularName;
	/**期限*/
	private String dayLimit;
	/**用途*/
	private String purpose;
	/**是否上线*/
	private String isOnline;
	/**存出账号余额*/
	private BigDecimal outBankAccMoney;
	/**保证金类型*/
	private String bondTypeName;

	public String getIsRegularName() {
		return isRegularName;
	}

	public void setIsRegularName(String isRegularName) {
		this.isRegularName = isRegularName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getIsPayed() {
		return isPayed;
	}
	public void setIsPayed(String isPayed) {
		this.isPayed = isPayed;
	}
	public BigDecimal getBondMoney() {
		return bondMoney;
	}
	public void setBondMoney(BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
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
	@Override
	public String getBusinessKey() {
		return getId();
	}
	@Override
	public String getBeanName() {
		return "fcBondDetailService";
	}
	public String getIsPayedName() {
		return isPayedName;
	}
	public void setIsPayedName(String isPayedName) {
		this.isPayedName = isPayedName;
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
	public BigDecimal getBalMoney() {
		return balMoney;
	}
	public void setBalMoney(BigDecimal balMoney) {
		this.balMoney = balMoney;
	}
	public BigDecimal getAvailMoney() {
		return availMoney;
	}
	public void setAvailMoney(BigDecimal availMoney) {
		this.availMoney = availMoney;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getOutBankAcc() {
		return outBankAcc;
	}
	public void setOutBankAcc(String outBankAcc) {
		this.outBankAcc = outBankAcc;
	}
	public String getBondTypeId() {
		return bondTypeId;
	}
	public void setBondTypeId(String bondTypeId) {
		this.bondTypeId = bondTypeId;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public String getInterestRateName() {
		return interestRateName;
	}
	public void setInterestRateName(String interestRateName) {
		this.interestRateName = interestRateName;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getIsRegular() {
		return isRegular;
	}
	public void setIsRegular(String isRegular) {
		this.isRegular = isRegular;
	}
	public String getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(String dayLimit) {
		this.dayLimit = dayLimit;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public BigDecimal getOutBankAccMoney() {
		return outBankAccMoney;
	}
	public void setOutBankAccMoney(BigDecimal outBankAccMoney) {
		this.outBankAccMoney = outBankAccMoney;
	}
	public String getBondTypeName() {
		return bondTypeName;
	}
	public void setBondTypeName(String bondTypeName) {
		this.bondTypeName = bondTypeName;
	}
}
