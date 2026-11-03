package com.bytter.tally.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 记账列表查询显示账务数据
 * 
 * @author liyh
 *
 */
public class FcAccbookListVO implements Serializable {
	private String id;
	/** 所属网点 */
	@BtCompositeSearch(fieldName = "accbook.net_id")
	private String netId;
	private String netName;
	/** 流水号 */
	@BtCompositeSearch(fieldName = "accbook.order_no")
	private String orderNo;
	/** 凭证号 */
	@BtCompositeSearch(fieldName = "accbook.voucher_no")
	private String voucherNo;
	/** 记账日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "accbook.tally_date")
	private java.util.Date tallyDate;
	/** 起息日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "accbook.interest_start_date")
	private java.util.Date interestStartDate;
	/** 凭证种类代码 */
	@Dict(tableName="FC_VOUCHER_TYPE",valueFieldName="id",showFiledName="VOUCHER_NAME",inject="voucherName")
	private String voucherId;
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
	private String generateMode;
	/** 业务来源类型 */
	@BtCompositeSearch(fieldName = "accbook.bus_ori_type")
	private Integer busOriType;
	@BtCompositeSearch(fieldName = "accbook.redblue_sign")
	private String redblueSign;
	/** 业务种类 */
	@BtCompositeSearch(fieldName = "accbook.bus_race_id")
    @Dict(tableName="FC_BUS_RACE",valueFieldName="id",showFiledName="RACE_NAME",inject="raceName")
	private String busRaceId;
	/** 业务种类名称*/
	private String raceName;
	/** 备注 */
	@BtCompositeSearch(fieldName = "accbook.rmk")
	private String rmk;
	/** 币别代码 */
	private String curCode;
	/** 记账金额 */
	private BigDecimal money;
	/** 全局ID */
	private String busGlobalId;
	/** 冲账标识 */
	private String rushSign;
	/** 原凭证号 */
	private String origVoucherNo;
	/** 是否可操作 */
	private String isOperation;
	//添加rushSign
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
	}
	public String getIsOperation() {
		return isOperation;
	}
	public void setIsOperation(String isOperation) {
		this.isOperation = isOperation;
	}
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}
	@Override
	public String toString() {
		return "FcAccbookListVO{" +
				"id='" + id + '\'' +
				", netId='" + netId + '\'' +
				", netName='" + netName + '\'' +
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
				", raceName='" + raceName + '\'' +
				", rmk='" + rmk + '\'' +
				", curCode='" + curCode + '\'' +
				", money=" + money +
				", busGlobalId='" + busGlobalId + '\'' +
				", rushSign='" + rushSign + '\'' +
				", origVoucherNo='" + origVoucherNo + '\'' +
				", isOperation='" + isOperation + '\'' +
				'}';
	}
}
