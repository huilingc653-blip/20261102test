package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: AB_DRAFT_EXC  
 * @Description: 指令基类
 * @author Jinyong.Cao
 * @date 2018-11-06 19:26:10
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_DRAFT_EXC", schema = "")
@SuppressWarnings("serial")
public class AbDraftExc extends IdEntity implements java.io.Serializable{
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
	/** 流水表ID */
	private String recordId;
	/** 签收表ID */
	private String signId;

	public AbDraftExc(String voucherId, String payerAcc, String bifCode, String origNo, String msgType, Date operDate, int status, String returnCode, String returnMsg, String recordId, String signId, String sbbllRngStrtSn, String sbbllRngEndSn, String billType, String signType) {
		this.voucherId = voucherId;
		this.payerAcc = payerAcc;
		this.bifCode = bifCode;
		this.origNo = origNo;
		this.msgType = msgType;
		this.operDate = operDate;
		this.status = status;
		this.returnCode = returnCode;
		this.returnMsg = returnMsg;
		this.recordId = recordId;
		this.signId = signId;
		this.sbbllRngStrtSn = sbbllRngStrtSn;
		this.sbbllRngEndSn = sbbllRngEndSn;
		this.billType = billType;
		this.signType = signType;
	}

	public AbDraftExc(){
		
	}
	@Override
	public String toString() {
		return "AbDraftExc{" +
				"voucherId='" + voucherId + '\'' +
				", payerAcc='" + payerAcc + '\'' +
				", bifCode='" + bifCode + '\'' +
				", origNo='" + origNo + '\'' +
				", msgType='" + msgType + '\'' +
				", operDate=" + operDate +
				", status=" + status +
				", returnCode='" + returnCode + '\'' +
				", returnMsg='" + returnMsg + '\'' +
				", recordId='" + recordId + '\'' +
				", signId='" + signId + '\'' +
				", sbbllRngStrtSn='" + sbbllRngStrtSn + '\'' +
				", sbbllRngEndSn='" + sbbllRngEndSn + '\'' +
				", billType='" + billType + '\'' +
				", signType='" + signType + '\'' +
				'}';
	}
	/** 子票区间开始序号 */
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	private String sbbllRngEndSn;

	/**
	 * 票据类型
	 */
	private String billType;

	/**
	 * 签收类型
	 */
	private String signType;

	@Column(name ="BILL_TYPE",nullable=true)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	@Column(name ="SIGN_TYPE",nullable=true)
	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}
	@Column(name ="SBBLL_RNG_STRT_SN",nullable=true)
	public String getSbbllRngStrtSn() {
		return sbbllRngStrtSn;
	}
	public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
		this.sbbllRngStrtSn = sbbllRngStrtSn;
	}
	@Column(name ="SBBLL_RNG_END_SN",nullable=true)
	public String getSbbllRngEndSn() {
		return sbbllRngEndSn;
	}
	public void setSbbllRngEndSn(String sbbllRngEndSn) {
		this.sbbllRngEndSn = sbbllRngEndSn;
	}
	@Column(name ="voucher_id",nullable=true)
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}
	@Column(name ="payeracc",nullable=true)
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	@Column(name ="bif_code",nullable=true,length=4)
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	@Column(name ="orig_no",nullable=true,length=40)
	public String getOrigNo() {
		return origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	@Column(name ="MSGTYPE",nullable=true)
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@Column(name ="OPER_DATE")
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	@Column(name ="status")
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name ="return_code",nullable=true,length=10)
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	@Column(name ="return_msg",nullable=true,length=200)
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	@Column(name ="RECORD_ID")
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	@Column(name ="SIGN_ID")
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	
	
	
}