package com.bytter.check.vo;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class CentralityDataVO implements java.io.Serializable {
	
	/**保存列表添加数据*/
	private List<FcCompareAcc> accList = new ArrayList<FcCompareAcc>();
	
	/**接受中心对账数据生成勾选参数*/
	public List<Map<String,String>> paramList = new ArrayList<Map<String,String>>();
	
	/**查询条件*/
	@BtCompositeSearch(fieldName="accbook.tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date queryDate;
	@BtCompositeSearch(fieldName="acc.blnctr_acc")
	private String account;
	
	/**列表属性*/
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
	private String curId;
	private String blnctrAcc;
	private String accName;
	private String subjectName;
	
	
	public CentralityDataVO() {
		super();
	}
	public CentralityDataVO(List<FcCompareAcc> accList) {
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
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getBlnctrAcc() {
		return blnctrAcc;
	}
	public void setBlnctrAcc(String blnctrAcc) {
		this.blnctrAcc = blnctrAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	
}
