package com.bytter.tally.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 记账列表查询显示账务数据
 *
 * @author liyh
 *
 */
public class FcAccbookDtlSearchVO implements Serializable {
	/** 所属网点 */
	@BtCompositeSearch(fieldName = "accbook.net_id")
	private String netId;
	/** 流水号 */
	@BtCompositeSearch(fieldName = "accbook.order_no")
	private String orderNo;
	/** 凭证号 */
	@BtCompositeSearch(fieldName = "accbook.voucher_no")
	private String voucherNo;
	/** 记账日期 */
	@BtCompositeSearch(fieldName = "accbook.tally_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/** 起息日期 */
	@BtCompositeSearch(fieldName = "accbook.interest_start_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestStartDate;
	/** 凭证种类代码 */
	@BtCompositeSearch(fieldName = "accbook.voucher_type")
	private String voucherType;
	/** 凭证种类代码 */
	private String voucherName;
	/** 记账员代码 */
	@BtCompositeSearch(fieldName = "accbook.operator_code")
	private String operatorCode;
	/** 复核员代码 */
	private String checkerCode;
	/** 复核日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date checkTime;
	/** 凭证状态 */
	@BtCompositeSearch(fieldName = "accbook.voucher_state")
	private String voucherState;
	/** 产生方式 */
	@BtCompositeSearch(fieldName = "accbook.generate_mode")
	private String generateMode;
	/** 业务来源类型 */
	@BtCompositeSearch(fieldName = "accbook.bus_ori_type")
	private Integer busOriType;
	@BtCompositeSearch(fieldName = "accbook.redblue_sign")
	private String redblueSign;
	/** 业务种类 */
	@BtCompositeSearch(fieldName = "accbook.bus_race_id")
	private String busRaceId;
	/** 系统业务种类 */
	@BtCompositeSearch(fieldName = "race.sys_race_code")
	private String sysRaceCode;
	/** 业务种类名称 */
	private String raceName;
	/** 备注 */
	@BtCompositeSearch(fieldName = "accbook.rmk")
	private String rmk;
	/** 币别代码 */
	@BtCompositeSearch(fieldName = "accbook.cur_code")
	private String curCode;
	/** 记账金额 */
	private BigDecimal money;
	/** 冲账标识 */
	@BtCompositeSearch(fieldName = "accbook.rush_sign")
	private String rushSign;
	/** 帐号 */
	private String account;
	/**账户名称*/
	@BtCompositeSearch(fieldName = "dtl.acc_name")
	private String accName;
	/**原凭证号*/
	@BtCompositeSearch(fieldName = "dtl.orig_voucher_no")
	private String origVoucherNo;
	/** 借方发生额 */
	@BtCompositeSearch(fieldName = "dtl.debit_money + dtl.credit_money")
	private BigDecimal debitMoney;
	/** 贷方发生额 */
	@BtCompositeSearch(fieldName = "dtl.credit_Money")
	private BigDecimal creditMoney;
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
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
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}
	public java.util.Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(java.util.Date checkTime) {
		this.checkTime = checkTime;
	}
	public String getVoucherState() {
		return voucherState;
	}
	public void setVoucherState(String voucherState) {
		this.voucherState = voucherState;
	}
	public String getGenerateMode() {
		return generateMode;
	}
	public void setGenerateMode(String generateMode) {
		this.generateMode = generateMode;
	}
	public Integer getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(Integer busOriType) {
		this.busOriType = busOriType;
	}
	public String getRaceName() {
		return raceName;
	}
	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}
	public String getRedblueSign() {
		return redblueSign;
	}
	public void setRedblueSign(String redblueSign) {
		this.redblueSign = redblueSign;
	}
	public String getBusRaceId() {
		return busRaceId;
	}
	public void setBusRaceId(String busRaceId) {
		this.busRaceId = busRaceId;
	}
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
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
	public String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
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
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Override
	public String toString() {
		return "FcAccbookSearchVO{" +
				"netId='" + netId + '\'' +
				", orderNo='" + orderNo + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", tallyDate=" + tallyDate +
				", interestStartDate=" + interestStartDate +
				", voucherType='" + voucherType + '\'' +
				", voucherName='" + voucherName + '\'' +
				", operatorCode='" + operatorCode + '\'' +
				", checkerCode='" + checkerCode + '\'' +
				", checkTime=" + checkTime +
				", voucherState='" + voucherState + '\'' +
				", generateMode='" + generateMode + '\'' +
				", busOriType=" + busOriType +
				", redblueSign='" + redblueSign + '\'' +
				", busRaceId='" + busRaceId + '\'' +
				", sysRaceCode='" + sysRaceCode + '\'' +
				", raceName='" + raceName + '\'' +
				", rmk='" + rmk + '\'' +
				", curCode='" + curCode + '\'' +
				", money=" + money +
				", rushSign='" + rushSign + '\'' +
				", account='" + account + '\'' +
				", origVoucherNo='" + origVoucherNo + '\'' +
				", accName='" + accName + '\'' +
				", debitMoney=" + debitMoney +
				", creditMoney=" + creditMoney +
				'}';
	}
}
