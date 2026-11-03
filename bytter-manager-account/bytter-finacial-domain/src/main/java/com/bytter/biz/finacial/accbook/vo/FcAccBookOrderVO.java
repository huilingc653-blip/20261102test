package com.bytter.biz.finacial.accbook.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcAccBookOrderVO implements Serializable {
	/** 所属网点 */
	@BtCompositeSearch(fieldName = "main_info.net_id")
	private java.lang.String netId;
	/** 流水号 */
	@BtCompositeSearch(fieldName = "main_info.order_no")
	private java.lang.String orderNo;
	/** 凭证号 */
	@BtCompositeSearch(fieldName = "main_info.voucher_no")
	private java.lang.String voucherNo;
	/** 记账日期 */
	@BtCompositeSearch(fieldName = "main_info.tally_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/** 起息日期 */
	@BtCompositeSearch(fieldName = "main_info.interest_start_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestStartDate;
	/** 凭证种类代码 */
	@BtCompositeSearch(fieldName = "main_info.voucher_type")
	private java.lang.String voucherType;
	/** 凭证状态 */
	@BtCompositeSearch(fieldName = "main_info.voucher_state")
	private java.lang.String voucherState;
	/** 业务来源类型 */
	@BtCompositeSearch(fieldName = "main_info.bus_ori_type")
	private java.lang.Integer busOriType;
	/** 业务种类 */
	@BtCompositeSearch(fieldName = "main_info.bus_race_id")
	private java.lang.String busRaceId;
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
	public java.lang.String getVoucherState() {
		return voucherState;
	}
	public void setVoucherState(java.lang.String voucherState) {
		this.voucherState = voucherState;
	}
	public java.lang.Integer getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(java.lang.Integer busOriType) {
		this.busOriType = busOriType;
	}
	public java.lang.String getBusRaceId() {
		return busRaceId;
	}
	public void setBusRaceId(java.lang.String busRaceId) {
		this.busRaceId = busRaceId;
	}
	@Override
	public String toString() {
		return "FcAccBookOrderVO{" +
				"netId='" + netId + '\'' +
				", orderNo='" + orderNo + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", tallyDate=" + tallyDate +
				", interestStartDate=" + interestStartDate +
				", voucherType='" + voucherType + '\'' +
				", voucherState='" + voucherState + '\'' +
				", busOriType=" + busOriType +
				", busRaceId='" + busRaceId + '\'' +
				'}';
	}
}
