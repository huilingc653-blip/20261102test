package com.bytter.guarantee.vo;

import com.bytter.core.annotation.BtCompositeSearch;
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
 * 
 * @author luxy
 * @version 1.0
 * @Title:FcBondAccVO
 * @Description:
 * @date 2018年9月12日 下午2:16:51
 */
@SuppressWarnings("serial")
@ExcelTarget("FcBondAccVO")
public class FcBondAccVO extends ExcelModel implements Serializable,  IBusinessKey { 
	/** 保证金ID */
	private String bondId;
	/** 存入账户 */
	@Excel(name = "*存入账户")
	@BtCompositeSearch(fieldName = "a.bank_acc")
	private String bankAcc;
	/** 账户名称 */
	@Excel(name = "*账户名称")
	@BtCompositeSearch(fieldName = "a.acc_name")
	private String accName;
	/** 单位ID */
	@BtCompositeSearch(fieldName = "a.corp_id")

	private String bankAccId;

//	@Corp(inject = "corpName")
	private String corpId;
	/** 单位名称 */
	@Excel(name = "*单位名称")
	@NotNull(message = "nis.netbank.message.corpname.null")
	@BtCompositeSearch(fieldName = "a.corp_id")
	private String corpName;
	/** 账户本金 */
	private BigDecimal bondMoney;
	/** 账户余额 */
	@Excel(name = "*账户余额")
	private BigDecimal accBalance;
	/**币别*/
	@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	@Excel(name = "*币别")
	private String curName;
	/** 存入银行 */
	@Excel(name = "*存入银行")
	private String bankName;
	/** 占用金额 */
	@Excel(name = "*占用金额")
	private BigDecimal applyMoney;
	/** 可用余额 */
	@Excel(name = "*可用余额")
	private BigDecimal availMoney;
	/** 存入日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date bondDate;
	/**联行号*/
	@Excel(name = "*联行号")
	private String bankCode;
	/**保证金比例*/
	private BigDecimal bondPercent;
	/**保证金利率*/
	private BigDecimal bondRate;
	/**存入日期*/
	@BtCompositeSearch(fieldName = "a.start_date")
	private Date startDate;
	/**到期日期*/
	@BtCompositeSearch(fieldName = "a.end_date")
	private Date endDate;
	/**保证金类型*/
	private String bondTypeName;
	/**
	 * 是否定期，1定期保证金，0活期保证金
	 */
	private String bailType;
	public String getBankAccId() {
		return bankAccId;
	}

	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}
	public String getBailType() {
		return bailType;
	}
	public void setBailType(String bailType) {
		this.bailType = bailType;
	}
	public String getBondId() {
		return bondId;
	}
	public void setBondId(String bondId) {
		this.bondId = bondId;
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
	public BigDecimal getBondMoney() {
		return bondMoney;
	}
	public void setBondMoney(BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	public Date getBondDate() {
		return bondDate;
	}
	public void setBondDate(Date bondDate) {
		this.bondDate = bondDate;
	}
	public BigDecimal getAccBalance() {
		return accBalance;
	}
	public void setAccBalance(BigDecimal accBalance) {
		this.accBalance = accBalance;
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
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public BigDecimal getBondPercent() {
		return bondPercent;
	}
	public void setBondPercent(BigDecimal bondPercent) {
		this.bondPercent = bondPercent;
	}
	public BigDecimal getBondRate() {
		return bondRate;
	}
	public void setBondRate(BigDecimal bondRate) {
		this.bondRate = bondRate;
	}
	@Override
	public String getBusinessKey() {
		// TODO Auto-generated method stub
		return null;
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
	public String getBondTypeName() {
		return bondTypeName;
	}
	public void setBondTypeName(String bondTypeName) {
		this.bondTypeName = bondTypeName;
	}
}
