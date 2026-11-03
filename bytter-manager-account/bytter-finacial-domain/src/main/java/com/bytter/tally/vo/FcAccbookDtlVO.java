package com.bytter.tally.vo;
import com.bytter.core.annotation.Dict;
import com.bytter.system.api.dto.FcAccDTO;
import java.io.Serializable;
public class FcAccbookDtlVO implements Serializable {
	
	private String id;
	
	/** 流水ID */
	private String bookId;
	/** 行号 */
	private Integer lineNo;
	/** 摘要 */
	private String summaryContent;
	/** 账号 */
	private String account;
	@Dict(tableName="FC_SUBJECT",valueFieldName="id",showFiledName="SUBJECT_NAME",inject="subjectName")
	private String subjectId;
	private String subjectName;
	
	private String subjectCode;
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	/**
	 * 币别代码
	 */
	private String curCode;
	/** 账户名称 */
	private String accName;
	/** 单价 */
	private java.math.BigDecimal price;
	/** 数量 */
	private Integer amount;
	/** 借方发生额 */
	private java.math.BigDecimal debitMoney;
	/** 贷方发生额 */
	private java.math.BigDecimal creditMoney;
	/** 余额 */
	private java.math.BigDecimal balance;
	/** 凭证种类代码 */
	private String voucherType;
	/** 原始凭证号 */
	private String origVoucherNo;
	/** 原始凭证号 */
	private String origVoucherId;
	/** 现金流量分类 */
	private String cashFlux;
	/**
	 * 现金流项目名
	 */
	private String cashFluxName;
	
	private VoucherRenderControlVo controlVo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public Integer getLineNo() {
		return lineNo;
	}
	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public java.math.BigDecimal getPrice() {
		return price;
	}
	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
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
	public java.math.BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(java.math.BigDecimal balance) {
		this.balance = balance;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
	}
	public String getOrigVoucherId() {
		return origVoucherId;
	}
	public void setOrigVoucherId(String origVoucherId) {
		this.origVoucherId = origVoucherId;
	}
	public String getCashFlux() {
		return cashFlux;
	}
	public void setCashFlux(String cashFlux) {
		this.cashFlux = cashFlux;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public VoucherRenderControlVo getControlVo() {
		return controlVo;
	}
	public void setControlVo(VoucherRenderControlVo controlVo) {
		this.controlVo = controlVo;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	/**
	 * 账户信息
	 */
	private FcAccDTO accDTO;
	
	public FcAccDTO getAccDTO() {
		return accDTO;
	}
	public void setAccDTO(FcAccDTO accDTO) {
		this.accDTO = accDTO;
	}
	public String getCashFluxName() {
		return cashFluxName;
	}
	public void setCashFluxName(String cashFluxName) {
		this.cashFluxName = cashFluxName;
	}
	@Override
	public String toString() {
		return "FcAccbookDtlVO{" +
				"id='" + id + '\'' +
				", bookId='" + bookId + '\'' +
				", lineNo=" + lineNo +
				", summaryContent='" + summaryContent + '\'' +
				", account='" + account + '\'' +
				", subjectId='" + subjectId + '\'' +
				", subjectName='" + subjectName + '\'' +
				", subjectCode='" + subjectCode + '\'' +
				", curCode='" + curCode + '\'' +
				", accName='" + accName + '\'' +
				", price=" + price +
				", amount=" + amount +
				", debitMoney=" + debitMoney +
				", creditMoney=" + creditMoney +
				", balance=" + balance +
				", voucherType='" + voucherType + '\'' +
				", origVoucherNo='" + origVoucherNo + '\'' +
				", origVoucherId='" + origVoucherId + '\'' +
				", cashFlux='" + cashFlux + '\'' +
				", controlVo=" + controlVo +
				", accDTO=" + accDTO +
				'}';
	}
}
