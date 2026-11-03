package com.bytter.biz.finacial.api.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class FcAccBalanceQueryVO implements Serializable {
	private String id;
	/**结算中心账号*/
	@BtCompositeSearch(fieldName = "fc.account")
	private String account;
	/**账户名称*/
	@BtCompositeSearch(fieldName = "fa.acc_Name")
	private String accName;
	/**账户类别*/
	private String accType;
	/**科目代码*/
	@BtCompositeSearch(fieldName = "fa.subject_id")
	@Dict(tableName="FC_SUBJECT",valueFieldName="id",showFiledName="SUBJECT_NAME",inject="subjectName")
	private String subjectId;
	@BtCompositeSearch(fieldName = "fs.subject_Name")
	private String subjectName;
	/**单位*/
	@BtCompositeSearch(fieldName = "fa.corp_id")
	private String corpId;
	/**币别*/
	@BtCompositeSearch(fieldName = "fc.cur_code")
	@Currency(inject="curName")
    private String curId;
	@BtCompositeSearch(fieldName = "fc.cur_code")
    private String curName;  
	/**当前账面余额*/
	@BtCompositeSearch(fieldName = "fc.online_balance")
	@NumberFormat(pattern = "#,##0.00")
    private java.math.BigDecimal onlineBalance;
	/**复核余额*/
	@BtCompositeSearch(fieldName = "fc.check_balance")
	@NumberFormat(pattern = "#,##0.00")
	private java.math.BigDecimal checkBalance;
	/**可用余额*/
	@BtCompositeSearch(fieldName = "fc.online_balance-fc.freeze_money")
	@NumberFormat(pattern = "#,##0.00")
    private  BigDecimal availableBalance;
	/**透支金额*/
	@BtCompositeSearch(fieldName = "fa.overdraft_money")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal overdraftMoney;
	/**开户日期*/
	@BtCompositeSearch(fieldName = "fa.acc_open_time")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date accOpenTime;
	/**到期日期*/
	@BtCompositeSearch(fieldName = "fa.deadline_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date deadlineDate;
	/**利率*/
	@BtCompositeSearch(fieldName = "fa.interest_Rate")
	private BigDecimal interestRate;
	/**
	 * 关联活期账号
	 */
	@BtCompositeSearch(fieldName = "fa.relation_acc")
	private String relationAcc;
	/**
	 * 当前字段显示超链接
	 */
	private boolean showLink;
	public boolean getShowLink() {
		return showLink;
	}
	public void setShowLink(boolean showLink) {
		this.showLink = showLink;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
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
	public BigDecimal getOnlineBalance() {
		return onlineBalance;
	}
	public void setOnlineBalance(BigDecimal onlineBalance) {
		this.onlineBalance = onlineBalance;
	}
	public BigDecimal getCheckBalance() {
		return checkBalance;
	}
	public void setCheckBalance(BigDecimal checkBalance) {
		this.checkBalance = checkBalance;
	}
	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}
	public BigDecimal getOverdraftMoney() {
		return overdraftMoney;
	}
	public void setOverdraftMoney(BigDecimal overdraftMoney) {
		this.overdraftMoney = overdraftMoney;
	}
	public Date getAccOpenTime() {
		return accOpenTime;
	}
	public void setAccOpenTime(Date accOpenTime) {
		this.accOpenTime = accOpenTime;
	}
	public Date getDeadlineDate() {
		return deadlineDate;
	}
	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public String getRelationAcc() {
		return relationAcc;
	}
	public void setRelationAcc(String relationAcc) {
		this.relationAcc = relationAcc;
	}
}
