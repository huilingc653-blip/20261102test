package com.bytter.biz.finacial.interest.vo;
import com.bytter.biz.finacial.api.dto.FcAccDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * <br>
 * <b>类功能：</b>凭证明细<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月10日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public class FcFinacialAccbookDtlVO implements Serializable{
	/**
     * 户名
     */
	private String accName;
	/**
	 * 内部账号
	 */
	private FcAccDTO fcAcc;
	/**
	 * 账号
	 */
	private String account;
	
    private BigDecimal amount;
    /**
     * 余额
     */
    private BigDecimal balance;
    private String budgetItem;
    private String cashFlux;
    private BigDecimal creditMoney;
    private BigDecimal debitMoney;
    private String failRmk;
    private BigDecimal price;
    /**
     * 备注
     */
    private String rmk;
    private String summaryContent;
    /**
     * 凭证类型
     */
    private String voucherType;
    
    private FcVoucherTypeDTO voucherTypeDis;
    /**
     * 原凭证号
     */
    private String origVoucherNo;
    /**
     * 科目
     */
    private String subjectCode;
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public FcAccDTO getFcAcc() {
		return fcAcc;
	}
	public void setFcAcc(FcAccDTO fcAcc) {
		this.fcAcc = fcAcc;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getBudgetItem() {
		return budgetItem;
	}
	public void setBudgetItem(String budgetItem) {
		this.budgetItem = budgetItem;
	}
	public String getCashFlux() {
		return cashFlux;
	}
	public void setCashFlux(String cashFlux) {
		this.cashFlux = cashFlux;
	}
	public BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public String getFailRmk() {
		return failRmk;
	}
	public void setFailRmk(String failRmk) {
		this.failRmk = failRmk;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public FcVoucherTypeDTO getVoucherTypeDis() {
		return voucherTypeDis;
	}
	public void setVoucherTypeDis(FcVoucherTypeDTO voucherTypeDis) {
		this.voucherTypeDis = voucherTypeDis;
	}
	public String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
    /**
     * 操作用户
     */
    private String opUserId;
    public String getOpUserId() {
        return opUserId;
    }
    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }
}
