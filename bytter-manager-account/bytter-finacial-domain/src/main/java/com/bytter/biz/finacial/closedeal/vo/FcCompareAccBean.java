package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 中心对账单Bean
 * 
 * @author liyh
 *
 */
public class FcCompareAccBean implements Serializable {
	/** 对账类型 */
	private java.lang.String checkType = FinacialConstant.CHECK_TYPE_CEN;
	/** 对账标志 */
	private java.lang.String compareSign = FinacialConstant.COMPARE_SIGN_NO;
	/** 币别代码 */
	private java.lang.String curId;
	/** 企业账号 */
	private String compAcc;
	/** 流水号 */
	private String orderNo;
	/** 凭证号 */
	private String voucherNo;
	/** 记账日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	/** 起息日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestStartDate;
	private String createBy;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTime;
	
	/** 票据号 */
	private String billNo;
	
	/** 行号 */
	private BigDecimal lineNo;
	
	/** 摘要 */
	private String summaryContent;
	
	/** 红蓝标志 */
	private String redblueSign;
	
	/** 借方发生额 */
	private BigDecimal debitMoney;
	
	/** 贷方发生额 */
	private BigDecimal creditMoney;
	
	/** 余额 */
	private BigDecimal balance;
	/** 输入类型 */
	private java.lang.String importType = FinacialConstant.IMPORT_TYPE_INTERFACE;
	public java.lang.String getCheckType() {
		return checkType;
	}
	public void setCheckType(java.lang.String checkType) {
		this.checkType = checkType;
	}
	public java.lang.String getCompareSign() {
		return compareSign;
	}
	public void setCompareSign(java.lang.String compareSign) {
		this.compareSign = compareSign;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public String getCompAcc() {
		return compAcc;
	}
	public void setCompAcc(String compAcc) {
		this.compAcc = compAcc;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public BigDecimal getLineNo() {
		return lineNo;
	}
	public void setLineNo(BigDecimal lineNo) {
		this.lineNo = lineNo;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public String getRedblueSign() {
		return redblueSign;
	}
	public void setRedblueSign(String redblueSign) {
		this.redblueSign = redblueSign;
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
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public java.lang.String getImportType() {
		return importType;
	}
	public void setImportType(java.lang.String importType) {
		this.importType = importType;
	}
}
