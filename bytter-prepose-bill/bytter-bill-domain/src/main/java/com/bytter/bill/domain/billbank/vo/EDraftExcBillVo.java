package com.bytter.bill.domain.billbank.vo;
import java.io.Serializable;
import java.sql.Timestamp;
/**
 * <br>
 * <b>功能：</b>指令查询表（支付信用指令，背面信息指令）实体<br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2018-04-18 <br>
 */
public class EDraftExcBillVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4847425598252017929L;
	private String id;
	private Integer billType; //票据种类
	
	private String payerAcc;//持票人账号
	
	private String bifCode; //银行接口代码
	
	private String origNo; //票据号
	
	private String msgType; //消息类型（支付信用01，背面信息02）
	
	private Timestamp operDate; // 操作日期
	
	private Integer status; // 状态 16待发送  19已完成   -1失败
	
	private String returnCode; // 返回代码
	
	private String returnMsg;  // 返回消息
	private String voucherId; // 票据主表ID
	
	private String briefId;//业务类型
	
	private String billId;//票据外部业务ID
	private String sbbllRngStrtSn;//		子票区间开始序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	private String sbbllRngEndSn;//				子票区间结束序号。旧票据默认0，新票据 12位长度，不足12位前面补0。
	public String getSbbllRngStrtSn() {
		return sbbllRngStrtSn;
	}
	public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
		this.sbbllRngStrtSn = sbbllRngStrtSn;
	}
	public String getSbbllRngEndSn() {
		return sbbllRngEndSn;
	}
	public void setSbbllRngEndSn(String sbbllRngEndSn) {
		this.sbbllRngEndSn = sbbllRngEndSn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getOrigNo() {
		return origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public Timestamp getOperDate() {
		return operDate;
	}
	public void setOperDate(Timestamp operDate) {
		this.operDate = operDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}
	public String getBriefId() {
		return briefId;
	}
	public void setBriefId(String briefId) {
		this.briefId = briefId;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String toString() {
		return "EDraftExcBillVo [id=" + id + ", billType=" + billType + ", payerAcc=" + payerAcc + ", bifCode="
				+ bifCode + ", origNo=" + origNo + ", msgType=" + msgType + ", operDate=" + operDate + ", status="
				+ status + ", returnCode=" + returnCode + ", returnMsg=" + returnMsg + ", voucherId=" + voucherId
				+ ", briefId=" + briefId + "]";
	}
	
}
