package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcCorpInterestDtlVO implements Serializable {
	/** 计息时间 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestDate;
	/** 所属网点 */
	private java.lang.String netId;
	/** 单位代码 */
	private java.lang.String corpId;
	/** 币别代码 */
	private java.lang.String curCode;
	/** 单位名称 */
	private java.lang.String corpName;
	/** 系统科目代码 */
	private java.lang.String sysSubjectCode;
	/** 银行类别 */
	private java.lang.String bankType;
	private java.lang.String bankTypeId;
	/** 银行类别名称 */
	private java.lang.String bankTypeName;
	/** 科目代码 */
	private java.lang.String subjectCode;
	/** 借方金额 */
	private BigDecimal debitMoney;
	/** 贷方金额 */
	private BigDecimal creditMoney;
	/** 余额方向 */
	private Integer balanceWay;
	public java.util.Date getInterestDate() {
		return interestDate;
	}
	public void setInterestDate(java.util.Date interestDate) {
		this.interestDate = interestDate;
	}
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getCorpId() {
		return corpId;
	}
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	public java.lang.String getCorpName() {
		return corpName;
	}
	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}
	public java.lang.String getSysSubjectCode() {
		return sysSubjectCode;
	}
	public void setSysSubjectCode(java.lang.String sysSubjectCode) {
		this.sysSubjectCode = sysSubjectCode;
	}
	public java.lang.String getBankType() {
		return bankType;
	}
	public void setBankType(java.lang.String bankType) {
		this.bankType = bankType;
	}
	public java.lang.String getBankTypeName() {
		return bankTypeName;
	}
	public void setBankTypeName(java.lang.String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}
	public java.lang.String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(java.lang.String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
}
