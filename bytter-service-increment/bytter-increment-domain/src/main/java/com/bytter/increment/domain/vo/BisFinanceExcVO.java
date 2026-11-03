package com.bytter.increment.domain.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @Title: Entity
 * @Description: 理财指令VO
 * @author onlineGenerator
 * @date 2017-08-23 11:30:27
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BisFinanceExcVO implements Serializable {
	/**ID*/
	private String id;
	/**指令编号*/
	@BtCompositeSearch(fieldName = "b.voucher_No")
	private String voucherNo;
	/**业务单ID*/
	private String cbsBillId;
	/**业务单号*/
	@BtCompositeSearch(fieldName = "b.cbs_Bill_No")
	private String cbsBillNo;
	/**单位ID*/
	@BtCompositeSearch(fieldName = "b.corp_Id")
	//@Corp(inject="corpName")
	private String corpId;
	/**单位名称*/
	@BtCompositeSearch(fieldName = "b.corp_Id")
	private String corpName;
	/**申请日期*/
	@BtCompositeSearch(fieldName = "b.req_Date")
	private Date reqDate;
	/**接口代码*/
	@BtCompositeSearch(fieldName = "b.bif_Code")
	//@Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
	private String bifCode;
	/**接口代码名称*/
	@BtCompositeSearch(fieldName = "b.bif_Code")
	private String bifName;
	/**付款账号*/
	@BtCompositeSearch(fieldName = "b.payer_Acc")
	private String payerAcc;
	/**付款账号名称*/
	@BtCompositeSearch(fieldName = "b.payer_Acc_Name")
	private String payerAccName;
	/**付款开户行名称*/
	@BtCompositeSearch(fieldName = "b.payer_Bank")
	private String payerBank;
	/**币别*/
	@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	@BtCompositeSearch(fieldName = "b.cur_Id")
	private String curName;
	/**金额*/
	@BtCompositeSearch(fieldName = "b.pay_Money")
	private BigDecimal payMoney;
	/**用途*/
	@BtCompositeSearch(fieldName = "b.purpose")
	private String purpose;
	/**期望支付日期*/
	@BtCompositeSearch(fieldName = "b.wish_Payday")
	private Date wishPayday;
	/**期限（活期转定期）：3个月、6个月、1年、2年、3年、5年*/
	@BtCompositeSearch(fieldName = "b.day_Limit")
	private Integer dayLimit;
	/**日期单位 Y-年、M-月、D-日*/
	@BtCompositeSearch(fieldName = "b.date_Unit")
	//@Dict(dictGroup = "choBankFinance.dateUnit", inject = "dateUnitName")
	private String dateUnit;
	@BtCompositeSearch(fieldName = "b.date_Unit")
	private String dateUnitName;
	/**会计凭证号*/
	@BtCompositeSearch(fieldName = "b.bursar_Bill_No")
	private String bursarBillNo;
	/**到期处理方式 0-不处理 1-本金续存 2-本息续存*/
	@BtCompositeSearch(fieldName = "b.certificate_Way")
	private Integer certificateWay;
	/**业务类型*/
	@BtCompositeSearch(fieldName = "b.bus_Type")
	//@Dict(dictGroup = "choBankFinance.busType", inject = "busTypeName")
	private Integer busType;
	@BtCompositeSearch(fieldName = "b.bus_Type")
	private String busTypeName;
	/**银行返回开户日期*/
	private Date openDate;
	/**银行返回收款账号*/
	@BtCompositeSearch(fieldName = "b.payee_Acc")
	private String payeeAcc;
	/**银行返回收款账号名称*/
	@BtCompositeSearch(fieldName = "b.payee_Acc_Name")
	private String payeeAccName;
	/**活期账号余额*/
	@BtCompositeSearch(fieldName = "b.curr_Balance")
	private BigDecimal currBalance;
	/**定期账号余额*/
	@BtCompositeSearch(fieldName = "b.time_Balance")
	private BigDecimal timeBalance;
	/**银行返回利率*/
	@BtCompositeSearch(fieldName = "b.interest_Rate")
	private BigDecimal interestRate;
	/**消息提醒标志*/
	private String noticeSign;
	/**银行流水号*/
	@BtCompositeSearch(fieldName = "b.bank_Serial_No")
	private String bankSerialNo;
	/**交易状态*/
	@BtCompositeSearch(fieldName = "b.voucher_Stat")
	@Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
	private String voucherStat;
	@BtCompositeSearch(fieldName = "b.voucher_Stat")
	private String voucherStatName;
	/**交易时间*/
	@BtCompositeSearch(fieldName = "b.trans_Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date transTime;
	/**返回时间*/
	@BtCompositeSearch(fieldName = "b.return_Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date returnTime;
	/**返回信息（交易前）*/
	@BtCompositeSearch(fieldName = "b.first_Return_Msg")
	private String firstReturnMsg;
	/**返回信息（交易后）*/
	@BtCompositeSearch(fieldName = "b.next_Return_Msg")
	private String nextReturnMsg;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**指令重发标志*/
	private String resendSign;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getCbsBillId() {
		return cbsBillId;
	}
	public void setCbsBillId(String cbsBillId) {
		this.cbsBillId = cbsBillId;
	}
	public String getCbsBillNo() {
		return cbsBillNo;
	}
	public void setCbsBillNo(String cbsBillNo) {
		this.cbsBillNo = cbsBillNo;
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
	public Date getReqDate() {
		return reqDate;
	}
	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getBifName() {
		return bifName;
	}
	public void setBifName(String bifName) {
		this.bifName = bifName;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayerAccName() {
		return payerAccName;
	}
	public void setPayerAccName(String payerAccName) {
		this.payerAccName = payerAccName;
	}
	public String getPayerBank() {
		return payerBank;
	}
	public void setPayerBank(String payerBank) {
		this.payerBank = payerBank;
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
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	public Integer getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(Integer dayLimit) {
		this.dayLimit = dayLimit;
	}
	public String getDateUnit() {
		return dateUnit;
	}
	public void setDateUnit(String dateUnit) {
		this.dateUnit = dateUnit;
	}
	public String getBursarBillNo() {
		return bursarBillNo;
	}
	public void setBursarBillNo(String bursarBillNo) {
		this.bursarBillNo = bursarBillNo;
	}
	public Integer getCertificateWay() {
		return certificateWay;
	}
	public void setCertificateWay(Integer certificateWay) {
		this.certificateWay = certificateWay;
	}
	public Integer getBusType() {
		return busType;
	}
	public void setBusType(Integer busType) {
		this.busType = busType;
	}
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public BigDecimal getCurrBalance() {
		return currBalance;
	}
	public void setCurrBalance(BigDecimal currBalance) {
		this.currBalance = currBalance;
	}
	public BigDecimal getTimeBalance() {
		return timeBalance;
	}
	public void setTimeBalance(BigDecimal timeBalance) {
		this.timeBalance = timeBalance;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public String getNoticeSign() {
		return noticeSign;
	}
	public void setNoticeSign(String noticeSign) {
		this.noticeSign = noticeSign;
	}
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	public String getFirstReturnMsg() {
		return firstReturnMsg;
	}
	public void setFirstReturnMsg(String firstReturnMsg) {
		this.firstReturnMsg = firstReturnMsg;
	}
	public String getNextReturnMsg() {
		return nextReturnMsg;
	}
	public void setNextReturnMsg(String nextReturnMsg) {
		this.nextReturnMsg = nextReturnMsg;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getResendSign() {
		return resendSign;
	}
	public void setResendSign(String resendSign) {
		this.resendSign = resendSign;
	}
	public String getDateUnitName() {
		return dateUnitName;
	}
	public void setDateUnitName(String dateUnitName) {
		this.dateUnitName = dateUnitName;
	}
	public String getBusTypeName() {
		return busTypeName;
	}
	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}
	public String getVoucherStatName() {
		return voucherStatName;
	}
	public void setVoucherStatName(String voucherStatName) {
		this.voucherStatName = voucherStatName;
	}
	@Override
	public String toString() {
		return "BisFinanceExcVO{" +
				"id='" + id + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", cbsBillId='" + cbsBillId + '\'' +
				", cbsBillNo='" + cbsBillNo + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", reqDate=" + reqDate +
				", bifCode='" + bifCode + '\'' +
				", bifName='" + bifName + '\'' +
				", payerAcc='" + payerAcc + '\'' +
				", payerAccName='" + payerAccName + '\'' +
				", payerBank='" + payerBank + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", payMoney=" + payMoney +
				", purpose='" + purpose + '\'' +
				", wishPayday=" + wishPayday +
				", dayLimit=" + dayLimit +
				", dateUnit='" + dateUnit + '\'' +
				", bursarBillNo='" + bursarBillNo + '\'' +
				", certificateWay=" + certificateWay +
				", busType=" + busType +
				", openDate=" + openDate +
				", payeeAcc='" + payeeAcc + '\'' +
				", payeeAccName='" + payeeAccName + '\'' +
				", currBalance=" + currBalance +
				", timeBalance=" + timeBalance +
				", interestRate=" + interestRate +
				", noticeSign='" + noticeSign + '\'' +
				", bankSerialNo='" + bankSerialNo + '\'' +
				", voucherStat='" + voucherStat + '\'' +
				", transTime=" + transTime +
				", returnTime=" + returnTime +
				", firstReturnMsg='" + firstReturnMsg + '\'' +
				", nextReturnMsg='" + nextReturnMsg + '\'' +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				'}';
	}
}
