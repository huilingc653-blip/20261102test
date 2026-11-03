package com.bytter.bill.domain.bill.vo;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: 票据账户明细余额VO
 * @author zhengg
 * @date 2017-09-28 19:20:01
 * @version 1.0
 */
public class AbBillAccDtlBalVO implements java.io.Serializable  {

	private static final long serialVersionUID = 268419642335938295L;

	/**
	 * 流水id
	 */
	private String accId;
	/**
	 * 出去流水id
	 */
	private String accIdOut;
	/**
	 * 贴现办理出去流水id
	 */
	private String accIdDiscount;

	/**
	 * 单位ID
	 */
	private String corpId;

	/**
	 * 是否发送指令标志：0否； 1是 ；
	 */
	private java.lang.String sendSign;

	/**
	 * 出去流水 是否发送指令标志：0否； 1是 ；
	 */
	private java.lang.String sendSignOut;

	/**
	 * 贴现办理出去 是否发送指令标志：0否； 1是 ；
	 */
	private java.lang.String sendSignDiscount;

	/**
	 * 银行账户
	 */
	private String bankAcc;
	/**
	 * 银行账户ID
	 */
	private String bankAccId;
	/**
	 * 票面金额
	 */
	private BigDecimal billMoney;
	/**
	 * 票据余额开始日期
	 */
	private Date beginDate;
	/**
	 * 票据余额结束日期
	 */
	private Date endDate;
	/**
	 * 票据收入日期
	 */
	private Date inDate;
	/**
	 *  票据出去日期
	 */
	private Date outDate;
	/**
	 * 票据出去审批状态
	 */
	private Integer outStatus;
	/**
	 * 票据出去指令状态
	 */
	private String outOptStatus;
	/**
	 * 票据贴现办理出去审批状态
	 */
	private Integer outDiscountStatus;
	/**
	 * 票据贴现办理出去指令状态
	 */
	private String outDiscountOptStatus;
	/**
	 * 票据贴现办理出去日期
	 */
	private Date outDiscountDate;

	/**
	 * 票据指令状态
	 */
	private String optStatus;

	/**
	 * 票据类型
	 */
	private Integer billType;

	/**流通介质：10 纸票、11 电票*/
	private Integer billFlag;

	/**
	 * 票据收入类型
	 */
	private String briefIdIn;
	/**
	 * 票据出去类型
	 */
	private String briefIdOut;

	private String bankName;

	private String typeName;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public Integer getOutStatus() {
		return outStatus;
	}
	public void setOutStatus(Integer outStatus) {
		this.outStatus = outStatus;
	}
	public String getOutOptStatus() {
		return outOptStatus;
	}
	public void setOutOptStatus(String outOptStatus) {
		this.outOptStatus = outOptStatus;
	}
	public Integer getOutDiscountStatus() {
		return outDiscountStatus;
	}
	public void setOutDiscountStatus(Integer outDiscountStatus) {
		this.outDiscountStatus = outDiscountStatus;
	}
	public String getOutDiscountOptStatus() {
		return outDiscountOptStatus;
	}
	public void setOutDiscountOptStatus(String outDiscountOptStatus) {
		this.outDiscountOptStatus = outDiscountOptStatus;
	}
	public Date getOutDiscountDate() {
		return outDiscountDate;
	}
	public void setOutDiscountDate(Date outDiscountDate) {
		this.outDiscountDate = outDiscountDate;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getBillFlag() {
		return billFlag;
	}

	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}

	public String getBriefIdIn() {
		return briefIdIn;
	}
	public void setBriefIdIn(String briefIdIn) {
		this.briefIdIn = briefIdIn;
	}
	public String getBriefIdOut() {
		return briefIdOut;
	}
	public void setBriefIdOut(String briefIdOut) {
		this.briefIdOut = briefIdOut;
	}

	public String getBankAccId() {
		return bankAccId;
	}
	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getSendSign() {
		return sendSign;
	}

	public void setSendSign(String sendSign) {
		this.sendSign = sendSign;
	}

	public String getOptStatus() {
		return optStatus;
	}

	public void setOptStatus(String optStatus) {
		this.optStatus = optStatus;
	}

	public String getAccIdOut() {
		return accIdOut;
	}

	public void setAccIdOut(String accIdOut) {
		this.accIdOut = accIdOut;
	}

	public String getAccIdDiscount() {
		return accIdDiscount;
	}

	public void setAccIdDiscount(String accIdDiscount) {
		this.accIdDiscount = accIdDiscount;
	}

	public String getSendSignOut() {
		return sendSignOut;
	}

	public void setSendSignOut(String sendSignOut) {
		this.sendSignOut = sendSignOut;
	}

	public String getSendSignDiscount() {
		return sendSignDiscount;
	}

	public void setSendSignDiscount(String sendSignDiscount) {
		this.sendSignDiscount = sendSignDiscount;
	}

	@Override
	public String toString() {
		return "AbBillAccDtlBalVO{" +
				"accId='" + accId + '\'' +
				", accIdOut='" + accIdOut + '\'' +
				", accIdDiscount='" + accIdDiscount + '\'' +
				", corpId='" + corpId + '\'' +
				", sendSign='" + sendSign + '\'' +
				", sendSignOut='" + sendSignOut + '\'' +
				", sendSignDiscount='" + sendSignDiscount + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", bankAccId='" + bankAccId + '\'' +
				", billMoney=" + billMoney +
				", beginDate=" + beginDate +
				", endDate=" + endDate +
				", inDate=" + inDate +
				", outDate=" + outDate +
				", outStatus=" + outStatus +
				", outOptStatus='" + outOptStatus + '\'' +
				", outDiscountStatus=" + outDiscountStatus +
				", outDiscountOptStatus='" + outDiscountOptStatus + '\'' +
				", outDiscountDate=" + outDiscountDate +
				", optStatus='" + optStatus + '\'' +
				", billType=" + billType +
				", billFlag=" + billFlag +
				", briefIdIn='" + briefIdIn + '\'' +
				", briefIdOut='" + briefIdOut + '\'' +
				'}';
	}

}
