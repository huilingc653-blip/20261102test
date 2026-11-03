package com.bytter.biz.payment.api.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class FcAccBalanceQueryDTO implements Serializable {
	private String id;
	/**结算中心账号*/
	private String account;
	/**账户名称*/
	private String accName;
	/**账户类别*/
	private String accType;
	/**科目代码*/
	private String subjectId;
	private String subjectName;
	/**单位*/
	private String corpId;
	/**币别*/
    private String curId;
    private String curName;
	/**当前账面余额*/
    private BigDecimal onlineBalance;
	/**复核余额*/
	private BigDecimal checkBalance;
	/**可用余额*/
    private  BigDecimal availableBalance;
	/**透支金额*/
	private BigDecimal overdraftMoney;
	/**开户日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date accOpenTime;
	/**到期日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date deadlineDate;
	/**利率*/
	private BigDecimal interestRate;
	/**
	 * 关联活期账号
	 */
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
