package com.bytter.guarantee.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author fusb
 * @version 1.0
 * @Title FcBondBackVO
 * @Description 保证金转回VO
 * @date 2018年9月18日
 */
@ExcelTarget("FcBondBackVO")
public class FcBondBackVO extends ExcelModel implements Serializable,  IBusinessKey {
	private static final long serialVersionUID = -6113114604127704368L;
	private String id;
	/** 业务单号 */
	@Excel(name = "*申请单号")
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
	private String bankAccId;
	/** 存入银行 */
	@Excel(name = "*存入银行")
	@BtCompositeSearch(fieldName = "B.BANK_NAME")
	private String bankName;
	/** 存入账户 */
	@Excel(name = "*存入账户")
	@BtCompositeSearch(fieldName = "B.BANK_ACC")
	private String bankAcc;
	/** 账户名称 */
	@Excel(name = "*账户名称")
	@BtCompositeSearch(fieldName = "B.ACC_NAME")
	private String accName;
	/** 保证金账户余额 */
	@Excel(name = "*保证金账户余额")
	@BtCompositeSearch(fieldName = "balMoney")
	private BigDecimal balMoney;
	/** 占用金额 */
	@Excel(name = "*占用金额")
	@BtCompositeSearch(fieldName = "applyMoney")
	private BigDecimal applyMoney;
	/** 可用金额 */
	@Excel(name = "*可用金额")
	@BtCompositeSearch(fieldName = "availMoney")
	private BigDecimal availMoney;
	/** 保证金ID */
	private String bondId;
	/** 转回日期 */
	private Date backDate;
	/** 转回金额 */
	private BigDecimal backMoney;
	/** 转回利息 */
	@Excel(name = "*利息收入")
	@BtCompositeSearch(fieldName = "backInterest")
	private BigDecimal backInterest;
	/** 状态 */
	private Integer status;
	/** 创建人登录名称 */
	private String createBy;
	/**登录人名*/
	private String createByName;
	/** 创建日期 */
	private Date createDate;
	/** 更新人登录名称 */
	private String updateBy;
	/** 更新日期 */
	private Date updateDate;
	/**币别*/
	@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	@Excel(name = "*币别")
	@BtCompositeSearch(fieldName = "B.CUR_ID")
	private String curName;
	/**保证金类型*/
	@BtCompositeSearch(fieldName = "t.bond_type_name")
	private String bondTypeName;
	@BtCompositeSearch(fieldName = "b.bond_Type_Id")
	private String bondTypeId;
	/**存回账号*/
	private String outBankAcc;
	/**用途*/
	private String purpose;
	/**存入日期*/
	private Date startDate;
	/**是否存入定期*/
	private String isRegular;
	/**利率*/
	private BigDecimal interestRate;
	/**结束日期*/
	private Date endDate;
	/**期限*/
	private String dayLimit;
	/**存入金额*/
	private BigDecimal bondMoney;
	private String isPayed;
	private String remark;
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
	public BigDecimal getBalMoney() {
		return balMoney;
	}
	public void setBalMoney(BigDecimal balMoney) {
		this.balMoney = balMoney;
	}
	public BigDecimal getApplyMoney() {
		return applyMoney;
	}
	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}
	public BigDecimal getAvailMoney() {
		return availMoney;
	}
	public void setAvailMoney(BigDecimal availMoney) {
		this.availMoney = availMoney;
	}
	public String getBondId() {
		return bondId;
	}
	public void setBondId(String bondId) {
		this.bondId = bondId;
	}
	public Date getBackDate() {
		return backDate;
	}
	public void setBackDate(Date backDate) {
		this.backDate = backDate;
	}
	public BigDecimal getBackMoney() {
		return backMoney;
	}
	public void setBackMoney(BigDecimal backMoney) {
		this.backMoney = backMoney;
	}
	public BigDecimal getBackInterest() {
		return backInterest;
	}
	public void setBackInterest(BigDecimal backInterest) {
		this.backInterest = backInterest;
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
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
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
	public String getBondTypeName() {
		return bondTypeName;
	}
	public void setBondTypeName(String bondTypeName) {
		this.bondTypeName = bondTypeName;
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
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getIsRegular() {
		return isRegular;
	}
	public void setIsRegular(String isRegular) {
		this.isRegular = isRegular;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(String dayLimit) {
		this.dayLimit = dayLimit;
	}
	public BigDecimal getBondMoney() {
		return bondMoney;
	}
	public void setBondMoney(BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}
	public String getBondTypeId() {
		return bondTypeId;
	}
	public void setBondTypeId(String bondTypeId) {
		this.bondTypeId = bondTypeId;
	}
	public String getIsPayed() {
		return isPayed;
	}
	public void setIsPayed(String isPayed) {
		this.isPayed = isPayed;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
