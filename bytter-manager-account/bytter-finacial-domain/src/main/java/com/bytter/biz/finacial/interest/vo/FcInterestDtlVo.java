package com.bytter.biz.finacial.interest.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
public class FcInterestDtlVo implements Serializable{
	
	
	/**帐号**/
	@BtCompositeSearch(fieldName = "dtl.account")
	private String account;
	/**户名**/
	@BtCompositeSearch(fieldName = "dtl.acc_name")
	private String accName;
	
	/**币别代码*/
	private java.lang.String curCode;
	@BtCompositeSearch(fieldName = "dtl.cur_Name")
	private java.lang.String curName;
	
	/**起息日期**/
	@BtCompositeSearch(fieldName="dtl.interest_start_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestStartDate;
	
	/**结息日期**/
	@BtCompositeSearch(fieldName = "dtl.bln_inst_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date blnInstDate;
	
	/**计息日期*/
	@BtCompositeSearch(fieldName = "dtl.interest_time")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestTime;
	
	/**天数*/
	@BtCompositeSearch(fieldName = "dtl.days")
	private java.lang.Integer days;
	
	/**积数*/
	@BtCompositeSearch(fieldName = "dtl.cumulation")
	private java.math.BigDecimal cumulation;
	
	/**利率*/
	@BtCompositeSearch(fieldName = "dtl.interest_rate")
	private java.math.BigDecimal interestRate;
	/**带百分号的利率*/
	private java.lang.String interestRatePercent;
	/**利息*/
	@BtCompositeSearch(fieldName = "dtl.interest")
	private java.math.BigDecimal interest;
	
	/**计息标志*/
	private java.lang.String interestSign;
	private java.lang.String interestSignName;
	/**网点**/
	@BtCompositeSearch(fieldName = "dtl.net_id")
	private String netId;
	
	/**单位**/
	@BtCompositeSearch(fieldName = "dtl.corp_id")
	private String corpId;
	
	/**开户种类**/
	@BtCompositeSearch(fieldName = "dtl.acc_type")
	private String accType;
	
	private java.lang.String id;
	/**预提ID*/
	private java.lang.String predrawDtlId;
	/**利息类型*/
	private java.lang.String interestType;
	private java.lang.String interestName;
	/**个别计息的还款金额*/
	private java.math.BigDecimal repayMoney;
	/**备注*/
	private java.lang.String rmk;
	/**备注*/
	private java.lang.String generateMode;
	/**流水号*/
	private java.lang.String orderNo;
	/**电子回单号*/
	private java.lang.String eReceiptNo;
	/**业务状态*/
	private java.lang.Integer busStatus;
	/**经办人代码*/
	private java.lang.String operatorCode;
	/**记账日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/**复核人代码*/
	private java.lang.String checkerCode;
	/**复核日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date checkDate;
	/**创建时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	public java.lang.Integer getDays() {
		return days;
	}
	public void setDays(java.lang.Integer days) {
		this.days = days;
	}
	public java.math.BigDecimal getCumulation() {
		return cumulation;
	}
	public void setCumulation(java.math.BigDecimal cumulation) {
		this.cumulation = cumulation;
	}
	public java.math.BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(java.math.BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public java.lang.String getInterestRatePercent() {
		return interestRatePercent;
	}
	public void setInterestRatePercent(java.lang.String interestRatePercent) {
		this.interestRatePercent = interestRatePercent;
	}
	public java.math.BigDecimal getInterest() {
		return interest;
	}
	public java.util.Date getInterestTime() {
		return interestTime;
	}
	public void setInterestTime(java.util.Date interestTime) {
		this.interestTime = interestTime;
	}
	public java.util.Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(java.util.Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public void setInterest(java.math.BigDecimal interest) {
		this.interest = interest;
	}
	public java.lang.String getInterestSign() {
		return interestSign;
	}
	public void setInterestSign(java.lang.String interestSign) {
		this.interestSign = interestSign;
	}
	
	public java.lang.String getInterestSignName() {
		return interestSignName;
	}
	public void setInterestSignName(java.lang.String interestSignName) {
		this.interestSignName = interestSignName;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public java.util.Date getBlnInstDate() {
		return blnInstDate;
	}
	public void setBlnInstDate(java.util.Date blnInstDate) {
		this.blnInstDate = blnInstDate;
	}
	public java.lang.String getId() {
		return id;
	}
	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getPredrawDtlId() {
		return predrawDtlId;
	}
	public void setPredrawDtlId(java.lang.String predrawDtlId) {
		this.predrawDtlId = predrawDtlId;
	}
	public java.lang.String getInterestType() {
		return interestType;
	}
	public void setInterestType(java.lang.String interestType) {
		this.interestType = interestType;
	}
	public java.math.BigDecimal getRepayMoney() {
		return repayMoney;
	}
	public void setRepayMoney(java.math.BigDecimal repayMoney) {
		this.repayMoney = repayMoney;
	}
	public java.lang.String getRmk() {
		return rmk;
	}
	public void setRmk(java.lang.String rmk) {
		this.rmk = rmk;
	}
	public java.lang.String getGenerateMode() {
		return generateMode;
	}
	public void setGenerateMode(java.lang.String generateMode) {
		this.generateMode = generateMode;
	}
	public java.lang.String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	public java.lang.String geteReceiptNo() {
		return eReceiptNo;
	}
	public void seteReceiptNo(java.lang.String eReceiptNo) {
		this.eReceiptNo = eReceiptNo;
	}
	public java.lang.Integer getBusStatus() {
		return busStatus;
	}
	public void setBusStatus(java.lang.Integer busStatus) {
		this.busStatus = busStatus;
	}
	public java.lang.String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(java.lang.String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public java.lang.String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(java.lang.String checkerCode) {
		this.checkerCode = checkerCode;
	}
	public java.util.Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(java.util.Date checkDate) {
		this.checkDate = checkDate;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	public java.lang.String getInterestName() {
		return interestName;
	}
	public void setInterestName(java.lang.String interestName) {
		this.interestName = interestName;
	}
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	
}
