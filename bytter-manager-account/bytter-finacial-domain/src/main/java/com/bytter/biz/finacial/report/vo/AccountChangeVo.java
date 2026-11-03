package com.bytter.biz.finacial.report.vo;

import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class AccountChangeVo implements java.io.Serializable {
	/**网点id**/
	public String netId;
	/**网点名称**/
	public String netName;
	/**单位**/
	public String corpId;
	public String corpName;
	/**科目**/
	public String subjectId;
	public String subjectName;
	/**币别**/
	public String curId;
	public String curName;
	/**日期**/
	public Date le_balanceDate;
	public Date ge_balanceDate;
	/**账号**/
	@Excel(name = "账号",orderNum = "0",width = 20.0D)
	public String account;
	public String le_account;
	public String ge_account;
	/**户名**/
	@Excel(name = "户名",orderNum = "1",width = 20.0D)
	public String accName;
	/**余额方向**/
	public Integer balanceWay;
	@Excel(name = "方向",orderNum = "2",width = 10.0D)
	public java.lang.String balanceName;
	/**期初余额**/
	//,suffix=(".00")
	@Excel(name = "期初余额",orderNum = "3", numFormat = ",##0.00",width = 15.0D)
	public BigDecimal yesterBalance;
	/**期末余额**/
	//,suffix=(".00")
	@Excel(name = "期末余额",orderNum = "6", numFormat = ",##0.00",width = 15.0D)
	public BigDecimal balance;
	/**借方发生额**/
	//,suffix=(".00")
	@Excel(name = "借方发生额",orderNum = "4", numFormat = ",##0.00",width = 15.0D)
	public BigDecimal debitMoney;
	/**贷方发生额**/
	//,suffix=(".00")
	@Excel(name = "贷方发生额",orderNum = "5", numFormat = ",##0.00",width = 15.0D)
	public BigDecimal creditMoney;
	/****/
	public boolean flag = true;
	/**最大日期**/
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date maxDate;
	/**最小日期**/
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date minDate;
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
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
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public BigDecimal getYesterBalance() {
		if(yesterBalance==null){
			return BigDecimal.ZERO;
		}
		return yesterBalance;
	}
	public void setYesterBalance(BigDecimal yesterBalance) {
		this.yesterBalance = yesterBalance;
	}
	public BigDecimal getBalance() {
		if(balance==null){
			return BigDecimal.ZERO;
		}
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getDebitMoney() {
		if(debitMoney==null){
			return BigDecimal.ZERO;
		}
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public BigDecimal getCreditMoney() {
		if(creditMoney==null){
			return BigDecimal.ZERO;
		}
		return creditMoney;
	}
	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
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
	public Date getLe_balanceDate() {
		return le_balanceDate;
	}
	public void setLe_balanceDate(Date le_balanceDate) {
		this.le_balanceDate = le_balanceDate;
	}
	public Date getGe_balanceDate() {
		return ge_balanceDate;
	}
	public void setGe_balanceDate(Date ge_balanceDate) {
		this.ge_balanceDate = ge_balanceDate;
	}
	public String getLe_account() {
		return le_account;
	}
	public void setLe_account(String le_account) {
		this.le_account = le_account;
	}
	public String getGe_account() {
		return ge_account;
	}
	public void setGe_account(String ge_account) {
		this.ge_account = ge_account;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public Date getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	public Date getMinDate() {
		return minDate;
	}
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}
	public java.lang.String getBalanceName() {
		return balanceName;
	}
	public void setBalanceName(java.lang.String balanceName) {
		this.balanceName = balanceName;
	}
}
