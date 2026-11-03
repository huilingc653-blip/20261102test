package com.bytter.increment.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Description: 理财产品DTO
 * @author qinl
 * @date 2020-07098
 * @version 1.0
 *
 */
public class ChoBankFinanceDTO  implements Serializable {
	private String id;
	/**业务单号*/
	private String billNo;
	/**单位ID*/
	private String corpId;
	/**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知*/
	private Integer busType;
	/**付款账号ID*/
	private String payerAccId;
	/**付款账号*/
	private String payerAcc;
	/**付款账户余额*/
	private BigDecimal balance;
	/**收款账号ID*/
	private String payeeAccId;
	/**收款账号*/
	private String payeeAcc;
	/**币别*/
	private String curId;
	/**金额*/
	private BigDecimal payMoney;
	/**期限（活期转定期）：3个月、6个月、1年、2年、3年、5年*/
	private Integer dayLimit;
	/**日期单位 Y-年、M-月、D-日*/
	private String dateUnit;
	/**会计凭证号*/
	private String bursarBillNo;
	/**利息*/
	private BigDecimal interest;
	/**到期处理方式0-不处理 1-本金续存 2-本息续存*/
	private Integer certificateWay;
	/**期望支付日期*/
	private Date wishPayday;
	/**利率*/
	private BigDecimal interestRate;
	/**用途*/
	private String purpose;
	/**附言*/
	private String addWord;
	/**取款日期*/
	private Date payDate;
	/**取款单据ID（定期转活期，通知转活期）*/
	private String payBillId;
	/**通知日期*/
	private Date notifyTime;
	/**补录利率*/
	private BigDecimal blInterestRate;
	/**是否补录 0-否 1-是*/
	private String isBl;
	/**补录单号*/
	private String bankBillNo;
	/**补录利息*/
	private BigDecimal blInterest;
	/**状态*/
	private Integer status;
	/**起息日期*/
	private Date interestStartDate;
	/**到期日期*/
	private Date deadlineDate;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**银行流水认领组件挂接确认*/
	private String collectionConfirm;
	/**是否上线*/
	private String isOnline;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public Integer getBusType() {
		return busType;
	}
	public void setBusType(Integer busType) {
		this.busType = busType;
	}
	public String getPayerAccId() {
		return payerAccId;
	}
	public void setPayerAccId(String payerAccId) {
		this.payerAccId = payerAccId;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getPayeeAccId() {
		return payeeAccId;
	}
	public void setPayeeAccId(String payeeAccId) {
		this.payeeAccId = payeeAccId;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
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
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public Integer getCertificateWay() {
		return certificateWay;
	}
	public void setCertificateWay(Integer certificateWay) {
		this.certificateWay = certificateWay;
	}
	public Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getAddWord() {
		return addWord;
	}
	public void setAddWord(String addWord) {
		this.addWord = addWord;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public String getPayBillId() {
		return payBillId;
	}
	public void setPayBillId(String payBillId) {
		this.payBillId = payBillId;
	}
	public Date getNotifyTime() {
		return notifyTime;
	}
	public void setNotifyTime(Date notifyTime) {
		this.notifyTime = notifyTime;
	}
	public BigDecimal getBlInterestRate() {
		return blInterestRate;
	}
	public void setBlInterestRate(BigDecimal blInterestRate) {
		this.blInterestRate = blInterestRate;
	}
	public String getIsBl() {
		return isBl;
	}
	public void setIsBl(String isBl) {
		this.isBl = isBl;
	}
	public String getBankBillNo() {
		return bankBillNo;
	}
	public void setBankBillNo(String bankBillNo) {
		this.bankBillNo = bankBillNo;
	}
	public BigDecimal getBlInterest() {
		return blInterest;
	}
	public void setBlInterest(BigDecimal blInterest) {
		this.blInterest = blInterest;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public Date getDeadlineDate() {
		return deadlineDate;
	}
	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
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
	public String getCollectionConfirm() {
		return collectionConfirm;
	}
	public void setCollectionConfirm(String collectionConfirm) {
		this.collectionConfirm = collectionConfirm;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
}
