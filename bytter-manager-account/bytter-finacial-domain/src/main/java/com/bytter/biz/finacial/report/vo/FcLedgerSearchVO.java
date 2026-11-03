package com.bytter.biz.finacial.report.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcLedgerSearchVO implements Serializable{
	
	/** 所属网点 */
	@BtCompositeSearch(fieldName = "info.netId")
	private java.lang.String netId;
	/** 帐号 */
	@BtCompositeSearch(fieldName = "info.account")
	private java.lang.String account;
	/** 账户类别 */
	@BtCompositeSearch(fieldName = "info.accType")
	private java.lang.String accType;
	/** 凭证号 */
	@BtCompositeSearch(fieldName = "info.voucherNo")
	private java.lang.String voucherNo;
	/**借方发生额*/
	@BtCompositeSearch(fieldName="info.debitMoney")
	private java.math.BigDecimal debitMoney;
	/** 贷方发生额 */
	@BtCompositeSearch(fieldName = "info.creditMoney")
	private java.math.BigDecimal creditMoney;
	/** 币别代码 */
	@BtCompositeSearch(fieldName = "info.curCode")
	private java.lang.String curCode;
	/** 科目 */
	@BtCompositeSearch(fieldName = "info.subjectCode")
	private java.lang.String subjectId;
	/** 记账日期 */
	@BtCompositeSearch(fieldName = "info.tallyDate")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/** 凭证状态 */
	@BtCompositeSearch(fieldName = "info.voucherState")
	private java.lang.String voucherState;
	//记账区间
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date gedTallyDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date ledTallyDate;
	//凭证号
	private java.lang.String gesVoucherNo;
	private java.lang.String lesVoucherNo;
	//贷方金额区间
	private java.math.BigDecimal gesCreditMoney;
	private java.math.BigDecimal lesCreditMoney;
	//借方金额区间
	private java.math.BigDecimal gesDebitMoney;
	private java.math.BigDecimal LesDebitMoney;
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public java.lang.String getAccType() {
		return accType;
	}
	public void setAccType(java.lang.String accType) {
		this.accType = accType;
	}
	public java.lang.String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(java.lang.String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public java.math.BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(java.math.BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public java.math.BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(java.math.BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	public java.lang.String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(java.lang.String subjectId) {
		this.subjectId = subjectId;
	}
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public java.util.Date getGedTallyDate() {
		return gedTallyDate;
	}
	public void setGedTallyDate(java.util.Date gedTallyDate) {
		this.gedTallyDate = gedTallyDate;
	}
	public java.util.Date getLedTallyDate() {
		return ledTallyDate;
	}
	public void setLedTallyDate(java.util.Date ledTallyDate) {
		this.ledTallyDate = ledTallyDate;
	}
	public java.lang.String getGesVoucherNo() {
		return gesVoucherNo;
	}
	public void setGesVoucherNo(java.lang.String gesVoucherNo) {
		this.gesVoucherNo = gesVoucherNo;
	}
	public java.lang.String getLesVoucherNo() {
		return lesVoucherNo;
	}
	public void setLesVoucherNo(java.lang.String lesVoucherNo) {
		this.lesVoucherNo = lesVoucherNo;
	}
	public java.math.BigDecimal getGesCreditMoney() {
		return gesCreditMoney;
	}
	public void setGesCreditMoney(java.math.BigDecimal gesCreditMoney) {
		this.gesCreditMoney = gesCreditMoney;
	}
	public java.math.BigDecimal getLesCreditMoney() {
		return lesCreditMoney;
	}
	public void setLesCreditMoney(java.math.BigDecimal lesCreditMoney) {
		this.lesCreditMoney = lesCreditMoney;
	}
	public java.math.BigDecimal getGesDebitMoney() {
		return gesDebitMoney;
	}
	public void setGesDebitMoney(java.math.BigDecimal gesDebitMoney) {
		this.gesDebitMoney = gesDebitMoney;
	}
	public java.math.BigDecimal getLesDebitMoney() {
		return LesDebitMoney;
	}
	public void setLesDebitMoney(java.math.BigDecimal lesDebitMoney) {
		LesDebitMoney = lesDebitMoney;
	}
	public java.lang.String getVoucherState() {
		return voucherState;
	}
	public void setVoucherState(java.lang.String voucherState) {
		this.voucherState = voucherState;
	}
	
}
