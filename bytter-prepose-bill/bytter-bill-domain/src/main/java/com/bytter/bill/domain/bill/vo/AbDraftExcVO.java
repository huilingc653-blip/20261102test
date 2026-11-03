package com.bytter.bill.domain.bill.vo;
import com.bytter.core.base.entity.IdEntity;
import java.util.Date;
/**
 * @Title: AB_DRAFT_EXC  
 * @Description: 指令基类
 * @author Jinyong.Cao
 * @date 2018-11-06 19:26:10
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class AbDraftExcVO extends IdEntity implements java.io.Serializable{
	/** 票据主表ID */
	private String voucherId;
	/** 持票人账号*/
	private String payerAcc;
	/** 银行接口代码 */
	private String bifCode;
	/** 票据号 */
	private String origNo;
	/** 消息类型（支付信用01\背面信息02\正面信息03） */
	private String msgType;
	/** 操作日期 */
	private Date operDate;
	/** 状态 */
	private int status;
	/** 返回代码 */
	private String returnCode;
	/** 返回消息 */
	private String returnMsg;
	
	public AbDraftExcVO(String voucherId, String payerAcc, String bifCode,
			String origNo, String msgType, Date operDate, int status,
			String returnCode, String returnMsg) {
		super();
		this.voucherId = voucherId;
		this.payerAcc = payerAcc;
		this.bifCode = bifCode;
		this.origNo = origNo;
		this.msgType = msgType;
		this.operDate = operDate;
		this.status = status;
		this.returnCode = returnCode;
		this.returnMsg = returnMsg;
	}
	
	@Override
	public String toString() {
		return "AbDraftExc [voucherId=" + voucherId + ", payerAcc=" + payerAcc
				+ ", bifCode=" + bifCode + ", origNo=" + origNo + ", msgType="
				+ msgType + ", operDate=" + operDate + ", status=" + status
				+ ", returnCode=" + returnCode + ", returnMsg=" + returnMsg
				+ "]";
	}
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
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
	
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
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
	
	
	
}
