package com.bytter.check.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

@ExcelTarget("FcCompareAccVO")
public class FcCompareAccVO extends ExcelModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	/**对账类型*/
	private String checkType;
	/**流水号*/
	private String orderNo;
	/**记账日期*/
	@Excel(name ="*发生日期",format="yyyy/MM/dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	@BtCompositeSearch(fieldName = "tally_date")
	private java.util.Date tallyDate;
	/**企业账号*/
	@BtCompositeSearch(fieldName="comp_acc")
	@Excel(name="*企业帐号")
	private String compAcc;
	/**币别代码*/
	@BtCompositeSearch(fieldName="cur_id")
	private String curId;
	/**币别*/
	private String curName;
	/**借方发生额*/
	@Excel(name="*借方金额",numFormat = "##########0.00")
	@BtCompositeSearch(fieldName="debit_money")
	private java.math.BigDecimal debitMoney;
	/**贷方发生额*/
	@Excel(name="*贷方金额",numFormat = "##########0.00")
	@BtCompositeSearch(fieldName="credit_money")
	private java.math.BigDecimal creditMoney;
	/**余额*/
	@Excel(name="余额",numFormat = "##########0.00")
	private java.math.BigDecimal balance;
	/**凭证号*/
	@Excel(name="*凭证号")
	@BtCompositeSearch(fieldName="voucher_No")
	private String voucherNo;
	/**红蓝标志*/
	@Excel(name="红蓝标志")
	private String redblueSign;
	/**对账标志*/
	@Excel(name="*对账标志")
	@BtCompositeSearch(fieldName = "compare_sign")
	private String compareSign;
	/**摘要*/
	@Excel(name="摘要")
	private String summaryContent;
	/**行号*/
	private java.math.BigDecimal lineNo;
	/**序号*/
	private String bankId;
	/**票据号*/
	private String billNo;
	/**起息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestStartDate;
	/**调节日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date adjustDate;
	/**输入日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date importDate;
	/**输入类型*/
	private String importType;
	/**数据源*/
	@Excel(name="*企业账来源")
	private String dataSource;
	/**收款账号*/
	private String payeeAcc;
	/**导入用户*/
	private String importUser;
	/**对账时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date checkDate;
	/**对账勾对用户*/
	private String checkUser;
	/**最后更新用户代码*/
	private String updateBy;
	/**最后更新时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date updateTime;
	/**创建用户代码*/
	private String createBy;
	/**创建时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTime;

	@BtCompositeSearch(fieldName = "b.corp_id")
	@Corp(inject = "corpName")
	private String corpId;
	private String corpName;

	/**银行账号**/
	private String bankAcc;
	private String bankName;
	private String erpCode;
	/**凭证状态**/
	private String voucherState;

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

	public String getVoucherState() {
		return voucherState;
	}

	public void setVoucherState(String voucherState) {
		this.voucherState = voucherState;
	}

	@Override
	public String getBeanName() {
		return "erpDataManagerService";
	}
	
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
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
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
	public java.math.BigDecimal getLineNo() {
		return lineNo;
	}
	public void setLineNo(java.math.BigDecimal lineNo) {
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
	public java.util.Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(java.util.Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public java.util.Date getAdjustDate() {
		return adjustDate;
	}
	public void setAdjustDate(java.util.Date adjustDate) {
		this.adjustDate = adjustDate;
	}
	public java.util.Date getImportDate() {
		return importDate;
	}
	public void setImportDate(java.util.Date importDate) {
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
	public java.util.Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(java.util.Date checkDate) {
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
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
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

	public String getCurName() {
		return curName;
	}

	public void setCurName(String curName) {
		this.curName = curName;
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
}
