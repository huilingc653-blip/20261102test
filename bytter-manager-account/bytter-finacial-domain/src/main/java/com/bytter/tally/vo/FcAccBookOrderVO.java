package com.bytter.tally.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
public class FcAccBookOrderVO implements Serializable {
	/** 所属网点 */
	@BtCompositeSearch(fieldName = "main_info.net_id")
	private String netId;
	/** 流水号 */
	@BtCompositeSearch(fieldName = "main_info.order_no")
	private String orderNo;
	/** 凭证号 */
	@BtCompositeSearch(fieldName = "main_info.voucher_no")
	private String voucherNo;
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
	private String voucherType;
	/** 凭证状态 */
	@BtCompositeSearch(fieldName = "main_info.voucher_state")
	private String voucherState;
	/** 业务来源类型 */
	@BtCompositeSearch(fieldName = "main_info.bus_ori_type")
	private Integer busOriType;
	/** 业务种类 */
	@BtCompositeSearch(fieldName = "main_info.bus_race_id")
	private String busRaceId;
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
	public String getVoucherState() {
		return voucherState;
	}
	public void setVoucherState(String voucherState) {
		this.voucherState = voucherState;
	}
	public Integer getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(Integer busOriType) {
		this.busOriType = busOriType;
	}
	public String getBusRaceId() {
		return busRaceId;
	}
	public void setBusRaceId(String busRaceId) {
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
