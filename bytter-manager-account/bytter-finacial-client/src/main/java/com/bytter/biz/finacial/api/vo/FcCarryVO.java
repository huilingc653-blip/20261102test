package com.bytter.biz.finacial.api.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcCarryVO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	/**结转类型*/
	private String carryType;
	/**所属网点*/
	@BtCompositeSearch(fieldName="a.net_Id")
	private String netId;
	
	private String netName;
	/**结转日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName="a.carry_time")
	private java.util.Date carryTime;
	/**操作日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date OpDate;
	/**结转人代码*/
	@BtCompositeSearch(fieldName="a.operator_Code")
	private String operatorCode;
	/**凭证起始日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName="a.voucher_Start_Date")
	private java.util.Date voucherStartDate;
	
	/**凭证终止日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName="a.voucher_End_Date")
	private java.util.Date voucherEndDate;
	/**结转帐号*/
	private String carryAcc;
	/**反结转日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName="a.restore_Carry_Time")
	private java.util.Date restoreCarryTime;
	/**反结转人代码*/
	@BtCompositeSearch(fieldName="a.restore_Carry_Optor")
	private String restoreCarryOptor;
	/**结转状态*/
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public java.util.Date getOpDate() {
		return OpDate;
	}
	public void setOpDate(java.util.Date opDate) {
		OpDate = opDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getCarryType() {
		return carryType;
	}
	public void setCarryType(String carryType) {
		this.carryType = carryType;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public java.util.Date getCarryTime() {
		return carryTime;
	}
	public void setCarryTime(java.util.Date carryTime) {
		this.carryTime = carryTime;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public java.util.Date getVoucherStartDate() {
		return voucherStartDate;
	}
	public void setVoucherStartDate(java.util.Date voucherStartDate) {
		this.voucherStartDate = voucherStartDate;
	}
	public java.util.Date getVoucherEndDate() {
		return voucherEndDate;
	}
	public void setVoucherEndDate(java.util.Date voucherEndDate) {
		this.voucherEndDate = voucherEndDate;
	}
	public String getCarryAcc() {
		return carryAcc;
	}
	public void setCarryAcc(String carryAcc) {
		this.carryAcc = carryAcc;
	}
	public java.util.Date getRestoreCarryTime() {
		return restoreCarryTime;
	}
	public void setRestoreCarryTime(java.util.Date restoreCarryTime) {
		this.restoreCarryTime = restoreCarryTime;
	}
	public String getRestoreCarryOptor() {
		return restoreCarryOptor;
	}
	public void setRestoreCarryOptor(String restoreCarryOptor) {
		this.restoreCarryOptor = restoreCarryOptor;
	}
	
}
