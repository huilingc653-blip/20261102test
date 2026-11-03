package com.bytter.check.vo;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class ErpDataVO implements java.io.Serializable {
	
	/**保存列表添加数据*/
	private List<FcCompareAcc> accList = new ArrayList<FcCompareAcc>();
	
	/**接受ERP提取数据生成勾选参数*/
	public List<Map<String,String>> paramList = new ArrayList<Map<String,String>>();
	
	/**查询条件*/
	@BtCompositeSearch(fieldName="accbook.tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date queryDate;
	@BtCompositeSearch(fieldName="acc.blnctr_acc")
	private String account;
	
	private String voucherNo;
	private String compAcc;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	private BigDecimal debitMoney;
	private BigDecimal creditMoney;
	private BigDecimal balance;
	private String redblueSign;
	private String summaryContent;
	private String billNo;
	private String orderNo;
	private BigDecimal lineNo;
	private String dataSource;
	private String importType;
	private String checkType;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestStartDate;
	private String bankId;
	private String curCode;
	private String compareSign;
	public ErpDataVO() {
		super();
	}
	public ErpDataVO(List<FcCompareAcc> accList) {
		super();
		this.accList = accList;
	}
	public List<FcCompareAcc> getAccList() {
		return accList;
	}
	public void setAccList(List<FcCompareAcc> accList) {
		this.accList = accList;
	}
	public Date getQueryDate() {
		return queryDate;
	}
	public void setQueryDate(Date queryDate) {
		this.queryDate = queryDate;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getCompAcc() {
		return compAcc;
	}
	public void setCompAcc(String compAcc) {
		this.compAcc = compAcc;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
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
	public String getRedblueSign() {
		return redblueSign;
	}
	public void setRedblueSign(String redblueSign) {
		this.redblueSign = redblueSign;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getLineNo() {
		return lineNo;
	}
	public void setLineNo(BigDecimal lineNo) {
		this.lineNo = lineNo;
	}
	public List<Map<String, String>> getParamList() {
		return paramList;
	}
	public void setParamList(List<Map<String, String>> paramList) {
		this.paramList = paramList;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getImportType() {
		return importType;
	}
	public void setImportType(String importType) {
		this.importType = importType;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getCompareSign() {
		return compareSign;
	}
	public void setCompareSign(String compareSign) {
		this.compareSign = compareSign;
	}
	
	
}
