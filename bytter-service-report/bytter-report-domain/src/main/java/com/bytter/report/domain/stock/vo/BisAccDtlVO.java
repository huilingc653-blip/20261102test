package com.bytter.report.domain.stock.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 账户流水明细VO
 * @author zuoy 2017-10-25
 */
public class BisAccDtlVO implements Serializable {
	private String id;
	/**
	 * 交易时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date transTime;
	/**
	 * 单位ID
	 */
	@Corp(inject = "corpName")
	private String corpId;
	/**
	 * 单位名称
	 */
	private String corpName;
	/**
	 * 银行类型ID
	 */
	private String bankTypeId;
	/**
	 * 银行类型
	 */
	private String bankTypeName;
	/**
	 * 币别ID
	 */
	@Currency(inject = "curName")
	private String curId;
	/**
	 * 币别名称
	 */
	private String curName;
	/**
	 * 本方账号
	 */
	private String bankAcc;
	/**
	 * 本方账户名
	 */
	private String accName;
	/**
	 * 本方账户开户行
	 */
	private String bankName;
	/**
	 * 交易金额
	 */
	private BigDecimal amt;
	/**
	 * 余额
	 */
	private BigDecimal bal;
	/**
	 * 借贷标志
	 */
	private String cdSign;
	/**
	 * 红蓝标志 1-红 2-蓝
	 */
	private String rbSign;
	/**
	 * 对方账号
	 */
	private String oppAccNo;
	/**
	 * 对方账户名
	 */
	private String oppAccName;
	/**
	 * 对方账户开户行
	 */
	private String oppAccBank;
	/**
	 * 是否上线
	 */
	private String isOnline;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
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
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getBankTypeName() {
		return bankTypeName;
	}
	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public BigDecimal getBal() {
		return bal;
	}
	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}
	public String getCdSign() {
		return cdSign;
	}
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
	public String getRbSign() {
		return rbSign;
	}
	public void setRbSign(String rbSign) {
		this.rbSign = rbSign;
	}
	public String getOppAccNo() {
		return oppAccNo;
	}
	public void setOppAccNo(String oppAccNo) {
		this.oppAccNo = oppAccNo;
	}
	public String getOppAccName() {
		return oppAccName;
	}
	public void setOppAccName(String oppAccName) {
		this.oppAccName = oppAccName;
	}
	public String getOppAccBank() {
		return oppAccBank;
	}
	public void setOppAccBank(String oppAccBank) {
		this.oppAccBank = oppAccBank;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	@Override
	public String toString() {
		return "BisAccDtlVO{" +
				"id='" + id + '\'' +
				", transTime=" + transTime +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", bankTypeId='" + bankTypeId + '\'' +
				", bankTypeName='" + bankTypeName + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", accName='" + accName + '\'' +
				", bankName='" + bankName + '\'' +
				", amt=" + amt +
				", bal=" + bal +
				", cdSign='" + cdSign + '\'' +
				", rbSign='" + rbSign + '\'' +
				", oppAccNo='" + oppAccNo + '\'' +
				", oppAccName='" + oppAccName + '\'' +
				", oppAccBank='" + oppAccBank + '\'' +
				", isOnline='" + isOnline + '\'' +
				'}';
	}
}
