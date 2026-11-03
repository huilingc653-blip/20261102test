package com.bytter.bill.domain.billbank.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 * <br>
 * <b>功能：</b>支付信用实体<br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2018-04-19 <br>
 * @throws Exception
 */
public class PaymentCreditVo implements Serializable{
	
	private static final long serialVersionUID = -5067710362769050255L;
	/**
	 * 报文ID
	 */
	private String msgId;
	
	/**
	 * 报文返回时间
	 */
	private Timestamp credtTm; 
	
	/**
	 * 机构名称
	 */
	private String nm; 
	
	/**
	 * 被查人组机构代码
	 */
	private String cmonid; 
	
	/**
	 * 承兑票据总笔数
	 */
	private Integer accptdQty; 
	
	/**
	 * 承兑票据总金额
	 */
	private BigDecimal accptdAmt;  
	
	/**
	 *  结清票据总笔数
	 */
	private Integer pydQty;
	
	/**
	 * 结清票据总金额
	 */
	private BigDecimal pydAmt; 
	
	/**
	 * 未结清票据总笔数
	 */
	private Integer pdgQty;
	
	/**
	 * 未结清票据总金额
	 */
	private BigDecimal pdgAmt;
	
	/**
	 * 拒付票据总笔数
	 */
	private Integer dshnrQty; 
	
	/**
	 * 拒付票据总金额
	 */
	private BigDecimal dshnrAmt; 
	
	/**
	 * 今年拒付总笔数
	 */
	private Integer dshnrQtySncthsyr; 
	
	/**
	 * 今年拒付总金额
	 */
	private BigDecimal dshnrAmtSncthsyr; 
	
	/**
	 * 去年拒付总笔数
	 */
	private Integer dshnrQtySnclastyr; 
	
	/**
	 * 去年拒付总金额
	 */
	private BigDecimal dshnrAmtSnclastyr; 
	
	/**
	 * 创建时间
	 */
	private Timestamp enterDate;  
	
	/**
	 * 票据号码
	 */
	private String billCode; 
	
	/**
	 *  账号
	 */
	private String acctId;
	
	/**
	 *  返回代码
	 */
	private String returnCode;
	
	/**
	 *  返回消息
	 */
	private String returnMsg;
	private String sbbllRngStrtSn;//子票区间开始序号
	private String sbbllRngEndSn;//子票区间结束序号
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
	public String getMsgId() {
		return msgId;
	}
	/**
	 * 报文ID
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public Timestamp getCredtTm() {
		return credtTm;
	}
	/**
	 * 报文返回时间
	 */
	public void setCredtTm(Timestamp credtTm) {
		this.credtTm = credtTm;
	}
	public String getNm() {
		return nm;
	}
	/**
	 * 机构名称
	 */
	public void setNm(String nm) {
		this.nm = nm;
	}
	public String getCmonid() {
		return cmonid;
	}
	/**
	 * 被查人组机构代码
	 */
	public void setCmonid(String cmonid) {
		this.cmonid = cmonid;
	}
	public Integer getAccptdQty() {
		return accptdQty;
	}
	/**
	 * 承兑票据总笔数
	 */
	public void setAccptdQty(Integer accptdQty) {
		this.accptdQty = accptdQty;
	}
	public BigDecimal getAccptdAmt() {
		return accptdAmt;
	}
	/**
	 * 承兑票据总金额
	 */
	public void setAccptdAmt(BigDecimal accptdAmt) {
		this.accptdAmt = accptdAmt;
	}
	public Integer getPydQty() {
		return pydQty;
	}
	/**
	 *  结清票据总笔数
	 */
	public void setPydQty(Integer pydQty) {
		this.pydQty = pydQty;
	}
	public BigDecimal getPydAmt() {
		return pydAmt;
	}
	/**
	 * 结清票据总金额
	 */
	public void setPydAmt(BigDecimal pydAmt) {
		this.pydAmt = pydAmt;
	}
	public Integer getPdgQty() {
		return pdgQty;
	}
	/**
	 * 未结清票据总笔数
	 */
	public void setPdgQty(Integer pdgQty) {
		this.pdgQty = pdgQty;
	}
	public BigDecimal getPdgAmt() {
		return pdgAmt;
	}
	/**
	 * 未结清票据总金额
	 */
	public void setPdgAmt(BigDecimal pdgAmt) {
		this.pdgAmt = pdgAmt;
	}
	public Integer getDshnrQty() {
		return dshnrQty;
	}
	/**
	 * 拒付票据总笔数
	 */
	public void setDshnrQty(Integer dshnrQty) {
		this.dshnrQty = dshnrQty;
	}
	public BigDecimal getDshnrAmt() {
		return dshnrAmt;
	}
	/**
	 * 拒付票据总金额
	 */
	public void setDshnrAmt(BigDecimal dshnrAmt) {
		this.dshnrAmt = dshnrAmt;
	}
	public Integer getDshnrQtySncthsyr() {
		return dshnrQtySncthsyr;
	}
	public void setDshnrQtySncthsyr(Integer dshnrQtySncthsyr) {
		this.dshnrQtySncthsyr = dshnrQtySncthsyr;
	}
	public BigDecimal getDshnrAmtSncthsyr() {
		return dshnrAmtSncthsyr;
	}
	/**
	 * 今年拒付总笔数
	 */
	public void setDshnrAmtSncthsyr(BigDecimal dshnrAmtSncthsyr) {
		this.dshnrAmtSncthsyr = dshnrAmtSncthsyr;
	}
	public Integer getDshnrQtySnclastyr() {
		return dshnrQtySnclastyr;
	}
	/**
	 * 去年拒付总笔数
	 */
	public void setDshnrQtySnclastyr(Integer dshnrQtySnclastyr) {
		this.dshnrQtySnclastyr = dshnrQtySnclastyr;
	}
	public BigDecimal getDshnrAmtSnclastyr() {
		return dshnrAmtSnclastyr;
	}
	/**
	 * 去年拒付总金额
	 */
	public void setDshnrAmtSnclastyr(BigDecimal dshnrAmtSnclastyr) {
		this.dshnrAmtSnclastyr = dshnrAmtSnclastyr;
	}
	public Timestamp getEnterDate() {
		return enterDate;
	}
	/**
	 * 创建时间
	 */
	public void setEnterDate(Timestamp enterDate) {
		this.enterDate = enterDate;
	}
	public String getBillCode() {
		return billCode;
	}
	/**
	 * 票据号码
	 */
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getAcctId() {
		return acctId;
	}
	/**
	 * 账号
	 * @param acctId
	 */
	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	public String getReturnCode() {
		return returnCode;
	}
	/**
	 * 返回代码
	 * @param returnCode
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	/**
	 * 返回消息
	 * @param returnMsg
	 */
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String toString() {
		return "PaymentCreditVo [msgId=" + msgId + ", credtTm=" + credtTm + ", nm=" + nm + ", cmonid=" + cmonid
				+ ", accptdQty=" + accptdQty + ", accptdAmt=" + accptdAmt + ", pydQty=" + pydQty + ", pydAmt=" + pydAmt
				+ ", pdgQty=" + pdgQty + ", pdgAmt=" + pdgAmt + ", dshnrQty=" + dshnrQty + ", dshnrAmt=" + dshnrAmt
				+ ", dshnrQtySncthsyr=" + dshnrQtySncthsyr + ", dshnrAmtSncthsyr=" + dshnrAmtSncthsyr
				+ ", dshnrQtySnclastyr=" + dshnrQtySnclastyr + ", dshnrAmtSnclastyr=" + dshnrAmtSnclastyr
				+ ", enterDate=" + enterDate + ", billCode=" + billCode + ", acctId=" + acctId + ", returnCode="
				+ returnCode + ", returnMsg=" + returnMsg + "]";
	} 
	
	
}
