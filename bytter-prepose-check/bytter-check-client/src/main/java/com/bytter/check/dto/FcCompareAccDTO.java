package com.bytter.check.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class FcCompareAccDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	/**对账类型*/
	private String checkType;
	/**流水号*/
	private String orderNo;
	/**记账日期*/
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date tallyDate;
	/**企业账号*/
	@BtCompositeSearch(fieldName="comp_acc")
	private String compAcc;
	/**币别代码*/
	@BtCompositeSearch(fieldName="cur_id")
	private String curId;
	/**币别*/
	private String curName;
	/**借方发生额*/
	private BigDecimal debitMoney;
	/**贷方发生额*/
	private BigDecimal creditMoney;
	/**余额*/
	private BigDecimal balance;
	/**凭证号*/
	private String voucherNo;
	/**红蓝标志*/
	private String redblueSign;
	/**对账标志*/
	@BtCompositeSearch(fieldName = "compare_sign")
	private String compareSign;
	/**摘要*/
	private String summaryContent;
	/**行号*/
	private BigDecimal lineNo;
	/**序号*/
	private String bankId;
	/**票据号*/
	private String billNo;
	/**起息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestStartDate;
	/**调节日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date adjustDate;
	/**输入日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date importDate;
	/**输入类型*/
	private String importType;
	/**数据源*/
	private String dataSource;
	/**收款账号*/
	private String payeeAcc;
	/**导入用户*/
	private String importUser;
	/**对账时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date checkDate;
	/**对账勾对用户*/
	private String checkUser;
	/**最后更新用户代码*/
	private String updateBy;
	/**最后更新时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updateTime;
	/**创建用户代码*/
	private String createBy;
	/**创建时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createTime;
	
	/**银行账号**/
	private String bankAcc;
	private String bankName;
	private String erpCode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getCompAcc() {
		return compAcc;
	}
	public void setCompAcc(String compAcc) {
		this.compAcc = compAcc;
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
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getRedblueSign() {
		return redblueSign;
	}
	public void setRedblueSign(String redblueSign) {
		this.redblueSign = redblueSign;
	}
	public String getCompareSign() {
		return compareSign;
	}
	public void setCompareSign(String compareSign) {
		this.compareSign = compareSign;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public BigDecimal getLineNo() {
		return lineNo;
	}
	public void setLineNo(BigDecimal lineNo) {
		this.lineNo = lineNo;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public Date getAdjustDate() {
		return adjustDate;
	}
	public void setAdjustDate(Date adjustDate) {
		this.adjustDate = adjustDate;
	}
	public Date getImportDate() {
		return importDate;
	}
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	public String getImportType() {
		return importType;
	}
	public void setImportType(String importType) {
		this.importType = importType;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getImportUser() {
		return importUser;
	}
	public void setImportUser(String importUser) {
		this.importUser = importUser;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public String getCheckUser() {
		return checkUser;
	}
	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getErpCode() {
		return erpCode;
	}
	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}
}
