package com.bytter.check.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class FcBankErpBalInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
     * 银行账号
     */
    private String bankAcc;
    /**
     * ERPID
     */
    private String erpId;
    /**
     * 中心账号
     */
    private String account;
    /**
     * 中心账面余额
     */
    private BigDecimal accBal;
    /**
     * 银行账面余额
     */
    private BigDecimal bankBal;
    /**
     * erp账面余额
     */
    private BigDecimal erpBal;
    /**
     * 上次结转日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastOverDate;
    /**
     * 结转日期类型
     */
	@JsonFormat(pattern = "yyyy-MM-dd")
    private String checkDateType;
    /**
     * 银企对账类型
     */
    private String paramType;
    /**
     * 对账显示
     */
    private String accDisplayType;
    /**
     * 对账结束日期
     */
    private String transTimeStr;
    /**
     * 对账标识（批量对账）
     */
    private String checkSign;
    /**
     * 中心初始余额（只用记录初始化余额）
     */
    private BigDecimal blnInitBalance;
    /**
     * 银行初始余额（只用记录初始化余额）
     */
    private BigDecimal bankInitBalance;
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getErpId() {
		return erpId;
	}
	public void setErpId(String erpId) {
		this.erpId = erpId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public BigDecimal getAccBal() {
		return accBal;
	}
	public void setAccBal(BigDecimal accBal) {
		this.accBal = accBal;
	}
	public BigDecimal getBankBal() {
		return bankBal;
	}
	public void setBankBal(BigDecimal bankBal) {
		this.bankBal = bankBal;
	}
	public BigDecimal getErpBal() {
		return erpBal;
	}
	public void setErpBal(BigDecimal erpBal) {
		this.erpBal = erpBal;
	}
	public Date getLastOverDate() {
		return lastOverDate;
	}
	public void setLastOverDate(Date lastOverDate) {
		this.lastOverDate = lastOverDate;
	}
	public String getCheckDateType() {
		return checkDateType;
	}
	public void setCheckDateType(String checkDateType) {
		this.checkDateType = checkDateType;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getAccDisplayType() {
		return accDisplayType;
	}
	public void setAccDisplayType(String accDisplayType) {
		this.accDisplayType = accDisplayType;
	}
	public String getTransTimeStr() {
		return transTimeStr;
	}
	public void setTransTimeStr(String transTimeStr) {
		this.transTimeStr = transTimeStr;
	}
	public String getCheckSign() {
		return checkSign;
	}
	public void setCheckSign(String checkSign) {
		this.checkSign = checkSign;
	}
	public BigDecimal getBlnInitBalance() {
		return blnInitBalance;
	}
	public void setBlnInitBalance(BigDecimal blnInitBalance) {
		this.blnInitBalance = blnInitBalance;
	}
	public BigDecimal getBankInitBalance() {
		return bankInitBalance;
	}
	public void setBankInitBalance(BigDecimal bankInitBalance) {
		this.bankInitBalance = bankInitBalance;
	}
    
}
