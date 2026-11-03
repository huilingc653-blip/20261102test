package com.bytter.bill.domain.billbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * <br>
 * <b>功能：</b>指令查询表（支付信用指令，背面信息指令）实体<br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2018-04-27 <br>
 */
@Entity
@Table(name = "AB_DRAFT_EXC",schema = "")
public class EDraftExcBill extends IdEntity implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String voucherId;//票据主表ID
	private String payerAcc;//持票人账号
    private String bifCode; //银行接口代码
    private String origNo; //票据号
    private String msgType; //消息类型（支付信用01，背面信息02）
    private java.util.Date operDate; // 操作日期
    private Integer status; // 状态 16待发送  19已完成   -1失败
    private String returnCode; // 返回代码
    private String returnMsg;  // 返回消息
    /** 流水表ID */
    private String recordId;
	/** 签收表ID */
	private String signId;
	private String sbbllRngStrtSn;
    private String sbbllRngEndSn;

    /**  *
     * 是否确认无此子票信息，1确认无信息，其他为有
     */
    private String isNoValue;

    @Column(name = "is_No_Value")
    public String getIsNoValue() {
        return isNoValue;
    }

    public void setIsNoValue(String isNoValue) {
        this.isNoValue = isNoValue;
    }

    @Column(name = "SBBLL_RNG_STRT_SN")
    public String getSbbllRngStrtSn() {
        return sbbllRngStrtSn;
    }
    public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
        this.sbbllRngStrtSn = sbbllRngStrtSn;
    }
    @Column(name = "SBBLL_RNG_END_SN")
    public String getSbbllRngEndSn() {
        return sbbllRngEndSn;
    }
    public void setSbbllRngEndSn(String sbbllRngEndSn) {
        this.sbbllRngEndSn = sbbllRngEndSn;
    }
    @Column(name = "PAYERACC")
    public String getPayerAcc() {
        return payerAcc;
    }
    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }
    @Column(name = "BIF_CODE")
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    @Column(name = "ORIG_NO")
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    @Column(name = "MSGTYPE")
    public String getMsgType() {
        return msgType;
    }
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
    @Column(name = "OPER_DATE")
    public java.util.Date getOperDate() {
        return operDate;
    }
    public void setOperDate(java.util.Date operDate) {
        this.operDate = operDate;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "RETURN_CODE")
    public String getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    @Column(name = "RETURN_MSG")
    public String getReturnMsg() {
        return returnMsg;
    }
    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
    @Column(name = "VOUCHER_ID")
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
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
