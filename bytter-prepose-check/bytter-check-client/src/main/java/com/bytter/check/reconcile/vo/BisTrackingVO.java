package com.bytter.check.reconcile.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
@SuppressWarnings("serial")
public class BisTrackingVO implements java.io.Serializable{
	
    private String checkboxHide;
	
	private String id;
	private String ids;
	private String opt;
	private String dtlId;
	/**指令id*/
	private String bisExcId;
	
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
    
    private String bbaId;
    
    private String curId;
    private String curName;
	@BtCompositeSearch(fieldName="bcd.CHECK_DATE")
    private Date checkDate;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName="bcd.RETURN_TIME")
    private Date returnTime;
	@JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName="bcd.TOBILL_DATE")
    private Date toBillDate;
    private Date dateStart; 
    private Date dateEnd; 
    @BtCompositeSearch(fieldName="bcd.bal")
    private BigDecimal bal;
    
    @BtCompositeSearch(fieldName="bisExc.voucher_stat")
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
    private String reasonId;
    private String reasonName;
  
    private String status;
    @BtCompositeSearch(fieldName="bcd.CHECK_STATUS")
    @Dict(dictGroup = "bisCheckDtl.checkStatus", inject = "checkStatusName")
    private String checkStatus;
    private String checkStatusName;
    
    @Dict(dictGroup = "bisCheckDtl.checkStatus", inject = "checkStatName")
    private String checkStat;
    private String checkStatName;
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
	public String getCheckboxHide() {
		if((StringUtils.isNotBlank(this.getCheckStatus()) && this.getCheckStatus().equals("0")) ||
				(StringUtils.isNotBlank(this.getVoucherStat()) &&
						(BdlinkConstant.BANK_FAILED.equals(this.getVoucherStat()) || BdlinkConstant.INTERFACE_FAILED.equals(this.getVoucherStat())))){
			return "0";
		}else{
			return "1";
		}
	}
	public void setCheckboxHide(String checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
	public String getCheckStatusName() {
		return checkStatusName;
	}
	public void setCheckStatusName(String checkStatusName) {
		this.checkStatusName = checkStatusName;
	}
	public String getCheckStat() {
		return checkStat;
	}
	public void setCheckStat(String checkStat) {
		this.checkStat = checkStat;
	}
	public String getCheckStatName() {
		return checkStatName;
	}
	public void setCheckStatName(String checkStatName) {
		this.checkStatName = checkStatName;
	}
	public String getAppScopeName() {
		return appScopeName;
	}
	public void setAppScopeName(String appScopeName) {
		this.appScopeName = appScopeName;
	}
	public String getReasonId() {
		return reasonId;
	}
	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}
	public String getReasonName() {
		return reasonName;
	}
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}
	public String getBisExcId() {
		return bisExcId;
	}
	public void setBisExcId(String bisExcId) {
		this.bisExcId = bisExcId;
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
}