package com.bytter.bill.domain.bill.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
/**   
 * @Description: 票据支付VO
 * @author onlineGenerator
 * @date 2017-06-21 15:36:23
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class AbPaymentCreditVo implements java.io.Serializable {
	private String id;
	/**
	 * 报文ID
	 */
	private String msgId;
	/**
	 * 报文返回时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date credtTm;
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
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date enterDate;
	/**
	 * 票据号码
	 */
	private String billCode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 报文ID
	 */
	public String getMsgId() {
		return msgId;
	}
	/**
	 * 报文ID
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	/**
	 * 报文返回时间
	 */
	public java.util.Date getCredtTm() {
		return credtTm;
	}
	/**
	 * 报文返回时间
	 */
	public void setCredtTm(java.util.Date credtTm) {
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
	public java.util.Date getEnterDate() {
		return enterDate;
	}
	/**
	 * 创建时间
	 */
	public void setEnterDate(java.util.Date enterDate) {
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
}
