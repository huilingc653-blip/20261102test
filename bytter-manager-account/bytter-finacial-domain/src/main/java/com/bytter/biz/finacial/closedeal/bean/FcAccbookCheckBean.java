package com.bytter.biz.finacial.closedeal.bean;
import java.io.Serializable;
import java.math.BigDecimal;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 内部账务检查Bean
 * @author liyh
 *
 */
public class FcAccbookCheckBean implements Serializable {
	private String id;
	/** 所属网点 */
	@BtCompositeSearch(fieldName = "accbook.net_id")
	private java.lang.String netId;
	/** 流水号 */
	@BtCompositeSearch(fieldName = "accbook.order_no")
	private java.lang.String orderNo;
	/** 凭证号 */
	@BtCompositeSearch(fieldName = "accbook.voucher_no")
	private java.lang.String voucherNo;
	/** 记账日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "accbook.tally_date")
	private java.util.Date tallyDate;
	/** 起息日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "accbook.interest_start_date")
	private java.util.Date interestStartDate;
	/** 凭证种类代码 */
	private java.lang.String voucherType;
	/** 凭证种类代码 */
	private java.lang.String voucherName;
	/** 记账员代码 */
	@BtCompositeSearch(fieldName = "accbook.operator_code")
	private java.lang.String operatorCode;
	/** 复核员代码 */
	private java.lang.String checkerCode;
	/** 复核日期 */
	private java.util.Date checkTime;
	/** 凭证状态 */
	@BtCompositeSearch(fieldName = "accbook.voucher_state")
	private java.lang.String voucherState;
	/** 产生方式 */
	private java.lang.String generateMode;
	/** 业务来源类型 */
	@BtCompositeSearch(fieldName = "accbook.bus_ori_type")
	private java.lang.Integer busOriType;
	@BtCompositeSearch(fieldName = "accbook.redblue_sign")
	private java.lang.String redblueSign;
	/** 业务种类 */
	@BtCompositeSearch(fieldName = "accbook.bus_race_id")
	private java.lang.String busRaceId;
	/** 业务种类名称*/
	private String raceName;
	/** 备注 */
	@BtCompositeSearch(fieldName = "accbook.rmk")
	private java.lang.String rmk;
	/** 币别代码 */
	private java.lang.String curCode;
	/** 记账金额 */
	private BigDecimal money;
	/** 全局ID */
	private String busGlobalId;
	/** 冲账标识 */
	private String rushSign;
	
	/**
	 * 记账账号
	 */
	private String account;
	/**
	 * 户名
	 */
	private String accName;
	
	/**
	 * 借方金额
	 */
	private BigDecimal debitMoney;
	/**
	 * 贷方金额
	 */
	private BigDecimal creditMoney;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	public java.lang.String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(java.lang.String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public java.util.Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(java.util.Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public java.lang.String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(java.lang.String voucherType) {
		this.voucherType = voucherType;
	}
	public java.lang.String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(java.lang.String voucherName) {
		this.voucherName = voucherName;
	}
	public java.lang.String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(java.lang.String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public java.lang.String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(java.lang.String checkerCode) {
		this.checkerCode = checkerCode;
	}
	public java.util.Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(java.util.Date checkTime) {
		this.checkTime = checkTime;
	}
	public java.lang.String getVoucherState() {
		return voucherState;
	}
	public void setVoucherState(java.lang.String voucherState) {
		this.voucherState = voucherState;
	}
	public java.lang.String getGenerateMode() {
		return generateMode;
	}
	public void setGenerateMode(java.lang.String generateMode) {
		this.generateMode = generateMode;
	}
	public java.lang.Integer getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(java.lang.Integer busOriType) {
		this.busOriType = busOriType;
	}
	public String getRaceName() {
		return raceName;
	}
	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}
	public java.lang.String getRedblueSign() {
		return redblueSign;
	}
	public void setRedblueSign(java.lang.String redblueSign) {
		this.redblueSign = redblueSign;
	}
	public java.lang.String getBusRaceId() {
		return busRaceId;
	}
	public void setBusRaceId(java.lang.String busRaceId) {
		this.busRaceId = busRaceId;
	}
	public java.lang.String getRmk() {
		return rmk;
	}
	public void setRmk(java.lang.String rmk) {
		this.rmk = rmk;
	}
	public java.lang.String getCurCode() {
		return curCode;
	}
	public void setCurCode(java.lang.String curCode) {
		this.curCode = curCode;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	public String getRushSign() {
		return rushSign;
	}
	public void setRushSign(String rushSign) {
		this.rushSign = rushSign;
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
	public BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
}
