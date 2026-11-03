package com.bytter.biz.finacial.report.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
public class FcAccrualRecordListVO implements Serializable{
	/**id*/
	private String id;
	/** 开始日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date startDate;
	/** 结束日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date endDate;
	/**币别code*/
	private java.lang.String curId;
	/**币别*/
	private java.lang.String curName;
	/**业务流水号*/
	private java.lang.String targetOrderNo;
	/**计提帐号*/
	private java.lang.String account;
	/**经办员*/
	private java.lang.String operatorCode;
	/** 计提时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date accrueTime;
	/** 手续费总数 */
	private java.lang.Long accrualNum;
	public java.util.Date getStartDate() {
		return startDate;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	public java.lang.String getTargetOrderNo() {
		return targetOrderNo;
	}
	public void setTargetOrderNo(java.lang.String targetOrderNo) {
		this.targetOrderNo = targetOrderNo;
	}
	public java.lang.String getAccount() {
		return account;
	}
	public void setAccount(java.lang.String account) {
		this.account = account;
	}
	public java.lang.String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(java.lang.String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public java.util.Date getAccrueTime() {
		return accrueTime;
	}
	public void setAccrueTime(java.util.Date accrueTime) {
		this.accrueTime = accrueTime;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public java.lang.Long getAccrualNum() {
		return accrualNum;
	}
	public void setAccrualNum(java.lang.Long accrualNum) {
		this.accrualNum = accrualNum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
