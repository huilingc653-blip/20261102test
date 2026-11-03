package com.bytter.bill.domain.billbank.pool.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 保证金存取申请信息
 * 
 */
public class PoolBailVO extends PoolBaseVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 存取类型:1：存入， 2：提取
	 */
	private String type;
	/**
	 * 交易申请日期
	 */
	private Date appDate;
	
	/**
	 * 交易金额
	 */
	private BigDecimal money;
	/**
	 * 保证金账户
	 */
	private String bankAcc;
	
	/**
	 * 保证金账户名称
	 */
	private String accName;
	
	/**
	 * 保证金账户所属行号
	 */
	private String bankNo;
	
	/**
	 * 保证金账户所属行名
	 */
	private String bankName;
	/**
	 * 对手账户
	 */
	private String oppBankAcc;
	
	/**
	 * 对手账户名称
	 */
	private String oppAccName;
	
	/**
	 * 对手账户所属行号
	 */
	private String oppBankNo;
	
	/**
	 * 对手账户所属行名
	 */
	private String oppBankName;
	
	/**
	 * 申请人名称
	 */
	private String opName;
	/**
	 * 申请人ID
	 */
	private String opId;

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getAppDate() {
		return appDate;
	}
	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
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
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getOppBankAcc() {
		return oppBankAcc;
	}
	public void setOppBankAcc(String oppBankAcc) {
		this.oppBankAcc = oppBankAcc;
	}
	public String getOppAccName() {
		return oppAccName;
	}
	public void setOppAccName(String oppAccName) {
		this.oppAccName = oppAccName;
	}
	public String getOppBankNo() {
		return oppBankNo;
	}
	public void setOppBankNo(String oppBankNo) {
		this.oppBankNo = oppBankNo;
	}
	public String getOppBankName() {
		return oppBankName;
	}
	public void setOppBankName(String oppBankName) {
		this.oppBankName = oppBankName;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	@Override
	public String toString() {
		return "PoolBailVO [type=" + type + ", appDate=" + appDate + ", money=" + money + ", bankAcc=" + bankAcc
				+ ", accName=" + accName + ", bankNo=" + bankNo + ", bankName=" + bankName + ", oppBankAcc="
				+ oppBankAcc + ", oppAccName=" + oppAccName + ", oppBankNo=" + oppBankNo + ", oppBankName="
				+ oppBankName + ", opName=" + opName + ", opId=" + opId + ", getBankId()=" + getBankId() + ", getApplyId()=" + getApplyId()
				+ ", getOptStatus()=" + getOptStatus() + ", getReCode()=" + getReCode() + ", getReMsg()=" + getReMsg()
				+ ", getPoolNo()=" + getPoolNo() + ", getBifCode()=" + getBifCode() + ", getBankOrgCode()="
				+ getBankOrgCode() + ", getBusId()=" + getBusId() + ", super.toString()=" + super.toString() + "]";
	}
	
}
