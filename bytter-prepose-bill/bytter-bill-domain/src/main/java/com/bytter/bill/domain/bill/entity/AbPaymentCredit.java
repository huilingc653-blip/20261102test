package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
/**
 * @Title: 支付信用实体
 * @Description: 支付信用实体
 * @author zhenghj
 * @date 2021-12-01
 * @version 1.0
 */
@Entity
@Table(name = "AB_CREDITINFO_RESPONSE", schema = "")
@SuppressWarnings("serial")
public class AbPaymentCredit extends IdEntity implements java.io.Serializable {
	/**
	 * 报文ID
	 */
	private String msgId;
	/**
	 * 报文返回时间
	 */
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
	private java.util.Date enterDate;
	/**
	 * 票据号码
	 */
	private String billCode;
	/**
	 * 报文ID
	 */
	@Column(name = "MSGID")
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
	@Column(name = "CREDTTM")
	public java.util.Date getCredtTm() {
		return credtTm;
	}
	/**
	 * 报文返回时间
	 */
	public void setCredtTm(java.util.Date credtTm) {
		this.credtTm = credtTm;
	}
	@Column(name = "NM")
	public String getNm() {
		return nm;
	}
	/**
	 * 机构名称
	 */
	public void setNm(String nm) {
		this.nm = nm;
	}
	@Column(name = "CMONID")
	public String getCmonid() {
		return cmonid;
	}
	/**
	 * 被查人组机构代码
	 */
	public void setCmonid(String cmonid) {
		this.cmonid = cmonid;
	}
	@Column(name = "ACCPTDQTY")
	public Integer getAccptdQty() {
		return accptdQty;
	}
	/**
	 * 承兑票据总笔数
	 */
	public void setAccptdQty(Integer accptdQty) {
		this.accptdQty = accptdQty;
	}
	@Column(name = "ACCPTDAMT")
	public BigDecimal getAccptdAmt() {
		return accptdAmt;
	}
	/**
	 * 承兑票据总金额
	 */
	public void setAccptdAmt(BigDecimal accptdAmt) {
		this.accptdAmt = accptdAmt;
	}
	@Column(name = "PYDQTY")
	public Integer getPydQty() {
		return pydQty;
	}
	/**
	 *  结清票据总笔数
	 */
	public void setPydQty(Integer pydQty) {
		this.pydQty = pydQty;
	}
	@Column(name = "PYDAMT")
	public BigDecimal getPydAmt() {
		return pydAmt;
	}
	/**
	 * 结清票据总金额
	 */
	public void setPydAmt(BigDecimal pydAmt) {
		this.pydAmt = pydAmt;
	}
	@Column(name = "PDGQTY")
	public Integer getPdgQty() {
		return pdgQty;
	}
	/**
	 * 未结清票据总笔数
	 */
	public void setPdgQty(Integer pdgQty) {
		this.pdgQty = pdgQty;
	}
	@Column(name = "PDGAMT")
	public BigDecimal getPdgAmt() {
		return pdgAmt;
	}
	/**
	 * 未结清票据总金额
	 */
	public void setPdgAmt(BigDecimal pdgAmt) {
		this.pdgAmt = pdgAmt;
	}
	@Column(name = "DSHNRQTY")
	public Integer getDshnrQty() {
		return dshnrQty;
	}
	/**
	 * 拒付票据总笔数
	 */
	public void setDshnrQty(Integer dshnrQty) {
		this.dshnrQty = dshnrQty;
	}
	@Column(name = "DSHNRAMT")
	public BigDecimal getDshnrAmt() {
		return dshnrAmt;
	}
	/**
	 * 拒付票据总金额
	 */
	public void setDshnrAmt(BigDecimal dshnrAmt) {
		this.dshnrAmt = dshnrAmt;
	}
	@Column(name = "DSHNRQTYSNCTHSYR")
	public Integer getDshnrQtySncthsyr() {
		return dshnrQtySncthsyr;
	}
	public void setDshnrQtySncthsyr(Integer dshnrQtySncthsyr) {
		this.dshnrQtySncthsyr = dshnrQtySncthsyr;
	}
	@Column(name = "DSHNRAMTSNCTHSYR")
	public BigDecimal getDshnrAmtSncthsyr() {
		return dshnrAmtSncthsyr;
	}
	/**
	 * 今年拒付总笔数
	 */
	public void setDshnrAmtSncthsyr(BigDecimal dshnrAmtSncthsyr) {
		this.dshnrAmtSncthsyr = dshnrAmtSncthsyr;
	}
	@Column(name = "DSHNRQTYSNCLASTYR")
	public Integer getDshnrQtySnclastyr() {
		return dshnrQtySnclastyr;
	}
	/**
	 * 去年拒付总笔数
	 */
	public void setDshnrQtySnclastyr(Integer dshnrQtySnclastyr) {
		this.dshnrQtySnclastyr = dshnrQtySnclastyr;
	}
	@Column(name = "DSHNRAMTSNCLASTYR")
	public BigDecimal getDshnrAmtSnclastyr() {
		return dshnrAmtSnclastyr;
	}
	/**
	 * 去年拒付总金额
	 */
	public void setDshnrAmtSnclastyr(BigDecimal dshnrAmtSnclastyr) {
		this.dshnrAmtSnclastyr = dshnrAmtSnclastyr;
	}
	@Column(name = "ENTERDATE")
	public java.util.Date getEnterDate() {
		return enterDate;
	}
	/**
	 * 创建时间
	 */
	public void setEnterDate(java.util.Date enterDate) {
		this.enterDate = enterDate;
	}
	@Column(name = "BILL_CODE")
	public String getBillCode() {
		return billCode;
	}
	/**
	 * 票据号码
	 */
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	@Override
	public String toString() {
		return "AbPaymentCreit{" +
				"msgId='" + msgId + '\'' +
				", credtTm=" + credtTm +
				", nm='" + nm + '\'' +
				", cmonid='" + cmonid + '\'' +
				", accptdQty=" + accptdQty +
				", accptdAmt=" + accptdAmt +
				", pydQty=" + pydQty +
				", pydAmt=" + pydAmt +
				", pdgQty=" + pdgQty +
				", pdgAmt=" + pdgAmt +
				", dshnrQty=" + dshnrQty +
				", dshnrAmt=" + dshnrAmt +
				", dshnrQtySncthsyr=" + dshnrQtySncthsyr +
				", dshnrAmtSncthsyr=" + dshnrAmtSncthsyr +
				", dshnrQtySnclastyr=" + dshnrQtySnclastyr +
				", dshnrAmtSnclastyr=" + dshnrAmtSnclastyr +
				", enterDate=" + enterDate +
				", billCode='" + billCode + '\'' +
				'}';
	}
}
