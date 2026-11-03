package com.bytter.biz.finacial.interest.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
public class FcAccuBalancefVo implements Serializable{

	/**id**/
	@BtCompositeSearch(fieldName = "b.id")
	private java.lang.String id;

	/**账户*/
	@BtCompositeSearch(fieldName = "account")
	private java.lang.String account;
	private java.lang.String likes_account;

	/**户名**/
//	@BtCompositeSearch(fieldName = "fab.acc_name")
	private String accname;

	/**科目名称*/
	private String subjectId;
	@BtCompositeSearch(fieldName = "a.SUBJECT_ID")
	private java.lang.String subjectName;
	private java.lang.String subjectPoolName;


	/**积数日期**/
	@BtCompositeSearch(fieldName = "b.balance_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date balanceDate;
	private java.util.Date ged_balanceDate;
	private java.util.Date led_balanceDate;

	/**当天积数*/
	@BtCompositeSearch(fieldName = "b.cumu")
	private java.math.BigDecimal cumu;


	/**当天调整积数*/
	@BtCompositeSearch(fieldName = "b.adjust_cumu")
	private java.math.BigDecimal adjustCumu;

	/**计息标志*/
	@BtCompositeSearch(fieldName = "b.interest_sign")
	private java.lang.String interestSign;
	private java.lang.String interestSignName;
	private java.lang.String eqs_interestSign;

	/**计息日期*/
	@BtCompositeSearch(fieldName = "b.interest_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date interestTime;


	/**预提标志*/
	@BtCompositeSearch(fieldName = "b.pre_sign")
	private java.lang.String preSign;

	/**网点**/
	@BtCompositeSearch(fieldName = "a.net_id")
	private String netId;
	private String eqs_netId;
	@BtCompositeSearch(fieldName = "a.corp_id")
	private String corpId;
	/**开户种类**/
	@BtCompositeSearch(fieldName = "a.acc_type")
	private String accType;
	private String ins_accType;

	/**币别代码*/
	@BtCompositeSearch(fieldName = "fab.cur_code")
	private java.lang.String curCode;
	/**科目*/
	@BtCompositeSearch(fieldName = "fab.subject_sort")
	private java.lang.String subjectSort;

	/**开始日期**/
	/*@BtCompositeSearch(fieldName = "b.create_time")
	private java.util.Date ged_createTime;*/
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	/**结束日期**/
	/*@BtCompositeSearch(fieldName = "b.create_time")
	private java.util.Date led_createTime;*/

	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccname() {
		return accname;
	}
	public void setAccname(String accname) {
		this.accname = accname;
	}
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public java.lang.String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(java.lang.String subjectName) {
		this.subjectName = subjectName;
	}
	public java.math.BigDecimal getCumu() {
		return cumu;
	}
	public void setCumu(java.math.BigDecimal cumu) {
		this.cumu = cumu;
	}
	public java.math.BigDecimal getAdjustCumu() {
		return adjustCumu;
	}
	public void setAdjustCumu(java.math.BigDecimal adjustCumu) {
		this.adjustCumu = adjustCumu;
	}
	public java.lang.String getInterestSign() {
		return interestSign;
	}
	public void setInterestSign(java.lang.String interestSign) {
		this.interestSign = interestSign;
	}
	public java.util.Date getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(java.util.Date interestTime) {
		this.interestTime = interestTime;
	}
	public java.lang.String getPreSign() {
		return preSign;
	}
	public void setPreSign(java.lang.String preSign) {
		this.preSign = preSign;
	}
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	public java.lang.String getSubjectSort() {
		return subjectSort;
	}
	public void setSubjectSort(java.lang.String subjectSort) {
		this.subjectSort = subjectSort;
	}
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.util.Date getBalanceDate() {
		return balanceDate;
	}
	public void setBalanceDate(java.util.Date balanceDate) {
		this.balanceDate = balanceDate;
	}
	public java.lang.String getInterestSignName() {
		return interestSignName;
	}
	public void setInterestSignName(java.lang.String interestSignName) {
		this.interestSignName = interestSignName;
	}
	public java.lang.String getLikes_account() {
		return likes_account;
	}
	public void setLikes_account(java.lang.String likes_account) {
		this.likes_account = likes_account;
	}
	public java.util.Date getGed_balanceDate() {
		return ged_balanceDate;
	}
	public void setGed_balanceDate(java.util.Date ged_balanceDate) {
		this.ged_balanceDate = ged_balanceDate;
	}
	public java.util.Date getLed_balanceDate() {
		return led_balanceDate;
	}
	public void setLed_balanceDate(java.util.Date led_balanceDate) {
		this.led_balanceDate = led_balanceDate;
	}
	public java.lang.String getEqs_interestSign() {
		return eqs_interestSign;
	}
	public void setEqs_interestSign(java.lang.String eqs_interestSign) {
		this.eqs_interestSign = eqs_interestSign;
	}
	public String getEqs_netId() {
		return eqs_netId;
	}
	public void setEqs_netId(String eqs_netId) {
		this.eqs_netId = eqs_netId;
	}
	public String getIns_accType() {
		return ins_accType;
	}
	public void setIns_accType(String ins_accType) {
		this.ins_accType = ins_accType;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectPoolName() {
		return subjectPoolName;
	}
	public void setSubjectPoolName(String subjectPoolName) {
		this.subjectPoolName = subjectPoolName;
	}
}
