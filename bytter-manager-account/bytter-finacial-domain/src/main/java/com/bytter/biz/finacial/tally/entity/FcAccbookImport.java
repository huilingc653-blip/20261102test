package com.bytter.biz.finacial.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 记账凭证导入表
 *
 */
@Entity
@Table(name = "FC_ACCBOOK_IMPORT", schema = "")
public class FcAccbookImport extends IdEntity implements java.io.Serializable {
	/**记账日期*/
	private Date tallyDate;
	/**起息日期*/
	private Date interestStartDate;
	/**摘要*/
	private String summaryContent;
	/**序号*/
	private BigDecimal lineNo;
	/**内部账号**/
	private  String account;
	/**借方发生额*/
	private BigDecimal debitMoney;
	/**贷方发生额*/
	private BigDecimal creditMoney;
	/**凭证类型**/
	private String voucherType;
	/**业务品种**/
	private String busRaceId;
	/**业务全局ID**/
	private String busGlobalId;
	/**操作人代码**/
	private String operatorCode;
	/**创建时间**/
	private Date createDate;
	/**备注*/
	private String rmk;
	/**附件张数*/
	private Integer affixAmount;
	/**对方名称*/
	private String externalCorp;
	/**对方账号*/
	private String externalAcc;
	/**对方账号开户行*/
	private String externalBank;
	@Column(name ="CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name ="TALLY_DATE")
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	@Column(name ="INTEREST_START_DATE")
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	@Column(name ="SUMMARY_CONTENT")
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	@Column(name ="LINE_NO")
	public BigDecimal getLineNo() {
		return lineNo;
	}
	public void setLineNo(BigDecimal lineNo) {
		this.lineNo = lineNo;
	}
	@Column(name ="ACCOUNT")
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	@Column(name ="DEBIT_MONEY")
	public BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	@Column(name ="CREDIT_MONEY")
	public BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	@Column(name ="VOUCHER_TYPE")
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	@Column(name ="BUS_RACE_ID")
	public String getBusRaceId() {
		return busRaceId;
	}
	public void setBusRaceId(String busRaceId) {
		this.busRaceId = busRaceId;
	}
	@Column(name ="BUS_GLOBAL_ID")
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	@Column(name ="OPERATOR_CODE")
	public String getOperatorCode() {
		return operatorCode;
	}
	@Column(name ="RMK")
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@Column(name ="AFFIX_AMOUNT")
	public Integer getAffixAmount() {
		return affixAmount;
	}
	public void setAffixAmount(Integer affixAmount) {
		this.affixAmount = affixAmount;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	@Column(name ="EXTERNAL_CORP")
	public String getExternalCorp() {
		return externalCorp;
	}
	public void setExternalCorp(String externalCorp) {
		this.externalCorp = externalCorp;
	}
	@Column(name ="EXTERNAL_ACC")
	public String getExternalAcc() {
		return externalAcc;
	}
	public void setExternalAcc(String externalAcc) {
		this.externalAcc = externalAcc;
	}
	@Column(name ="EXTERNAL_BANK")
	public String getExternalBank() {
		return externalBank;
	}
	public void setExternalBank(String externalBank) {
		this.externalBank = externalBank;
	}
}
