package com.bytter.check.reconcile.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public class BisBankEnterpriseCheckVo implements java.io.Serializable{
	private String id;
	private String ids;
	private String rids;
	private String opt;
	private String dtlId;
	
	
	 /**单位Id*/
    @BtCompositeSearch(fieldName="bcd.corp_id")
    private String corpId;
    private String corpCodeAndName; 
    @BtCompositeSearch(fieldName="bba.bank_type_id")
    private String bankTypeId;
    private String bankTypeName;
    
    @BtCompositeSearch(fieldName="bcd.BILL_NO")
    private String billNo;
    private String bankName;
    @BtCompositeSearch(fieldName="bcd.BANK_ACC")
    private String bankAcc;
    private String accName;
    private String accNature;
	@Dict(dictGroup = "btBankAcc.accType", inject = "accTypeName")
	private String accType;
	private String accTypeName;
    
    private String bbaId;
    
    private String curId;
    private String curName;
    private Date checkDate;
    private Date returnTime;
	@BtCompositeSearch(fieldName="bcd.TOBILL_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date toBillDate;
    private Date dateStart; 
    private Date dateEnd; 
	@BtCompositeSearch(fieldName="bcd.BAL")
    private BigDecimal bal;
    
    @BtCompositeSearch(fieldName="bcd.VOUCHER_STAT")
    @Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
    private String voucherStat;
    private String voucherStatName;
    
    private String mobileNo;
    
    private String rmk;
    private String busType;
    /** 关键字**/
    @Dict(dictGroup = "BIS_CHECK_REASON_SCOPE", inject = "appScopeName", multi = true)
	private String appScope;
	private String appScopeName;
    @Dict(dictGroup = "bisCheckDtl.mark", inject = "checkResultName")
    private String checkResult;
    private String checkResultName;
    
    @Dict(dictGroup = "bisCheckDtl.status", inject = "statusName")
    @BtCompositeSearch(fieldName="bcd.status")
    private String status;
    private String statusName;
    private String checkStatus;
	/**拓展字段（用于存储银行返回要素）*/
	private String expandFields;
	// 对账账户
	private String checkAccount;
	// 是否保证金
	private String isBail;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpCodeAndName() {
		return corpCodeAndName;
	}
	public void setCorpCodeAndName(String corpCodeAndName) {
		this.corpCodeAndName = corpCodeAndName;
	}
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getBankTypeName() {
		return bankTypeName;
	}
	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccNature() {
		return accNature;
	}
	public void setAccNature(String accNature) {
		this.accNature = accNature;
	}
	public String getBbaId() {
		return bbaId;
	}
	public void setBbaId(String bbaId) {
		this.bbaId = bbaId;
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
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Date getToBillDate() {
		return toBillDate;
	}
	public void setToBillDate(Date toBillDate) {
		this.toBillDate = toBillDate;
	}
	public BigDecimal getBal() {
		return bal;
	}
	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	public String getVoucherStatName() {
		return voucherStatName;
	}
	public void setVoucherStatName(String voucherStatName) {
		this.voucherStatName = voucherStatName;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getAppScope() {
		return appScope;
	}
	public void setAppScope(String appScope) {
		this.appScope = appScope;
	}
	public String getDtlId() {
		return dtlId;
	}
	public void setDtlId(String dtlId) {
		this.dtlId = dtlId;
	}
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public String getCheckResultName() {
		return checkResultName;
	}
	public void setCheckResultName(String checkResultName) {
		this.checkResultName = checkResultName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getRids() {
		return rids;
	}
	public void setRids(String rids) {
		this.rids = rids;
	}
	public String getAppScopeName() {
		return appScopeName;
	}
	public void setAppScopeName(String appScopeName) {
		this.appScopeName = appScopeName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccTypeName() {
		return accTypeName;
	}
	public void setAccTypeName(String accTypeName) {
		this.accTypeName = accTypeName;
	}
	public String getExpandFields() {
		return expandFields;
	}
	public void setExpandFields(String expandFields) {
		this.expandFields = expandFields;
	}

	public String getCheckAccount() {
		return checkAccount;
	}

	public void setCheckAccount(String checkAccount) {
		this.checkAccount = checkAccount;
	}

	public String getIsBail() {
		return isBail;
	}

	public void setIsBail(String isBail) {
		this.isBail = isBail;
	}

	@Override
	public String toString() {
		return "BisBankEnterpriseCheckVo{" +
				"id='" + id + '\'' +
				", ids='" + ids + '\'' +
				", rids='" + rids + '\'' +
				", opt='" + opt + '\'' +
				", dtlId='" + dtlId + '\'' +
				", corpId='" + corpId + '\'' +
				", corpCodeAndName='" + corpCodeAndName + '\'' +
				", bankTypeId='" + bankTypeId + '\'' +
				", bankTypeName='" + bankTypeName + '\'' +
				", billNo='" + billNo + '\'' +
				", bankName='" + bankName + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", accName='" + accName + '\'' +
				", accNature='" + accNature + '\'' +
				", accType='" + accType + '\'' +
				", accTypeName='" + accTypeName + '\'' +
				", bbaId='" + bbaId + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", checkDate=" + checkDate +
				", returnTime=" + returnTime +
				", toBillDate=" + toBillDate +
				", dateStart=" + dateStart +
				", dateEnd=" + dateEnd +
				", bal=" + bal +
				", voucherStat='" + voucherStat + '\'' +
				", voucherStatName='" + voucherStatName + '\'' +
				", mobileNo='" + mobileNo + '\'' +
				", rmk='" + rmk + '\'' +
				", busType='" + busType + '\'' +
				", appScope='" + appScope + '\'' +
				", appScopeName='" + appScopeName + '\'' +
				", checkResult='" + checkResult + '\'' +
				", checkResultName='" + checkResultName + '\'' +
				", status='" + status + '\'' +
				", statusName='" + statusName + '\'' +
				", checkStatus='" + checkStatus + '\'' +
				'}';
	}
}
