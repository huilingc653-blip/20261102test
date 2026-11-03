package com.bytter.biz.finacial.accbook.vo;
import java.io.Serializable;
import com.bytter.core.annotation.Region;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcAccbookExtVO implements Serializable {
	/** 打回标志 */
	private java.lang.String backSign;
	/** 交易项目 */
	private java.lang.Integer tradeId;
	/** 对方国别 */
	private java.lang.String coutryType;
	/** 参考汇率 */
	private java.math.BigDecimal referRate;
	/** 是否外汇市场交易 */
	private java.lang.Integer fxOrnot;
	/** 成交汇率 */
	private java.math.BigDecimal tradeRate;
	/** 审批件编号 */
	private java.lang.String fxAppnum;
	/** 外币交易方式 */
	private java.lang.Integer fxType;
	/** 申报号码 */
	private java.lang.String declareNum;
	/** 打回原因 */
	private java.lang.String backReason;
	/** 冲账原因 */
	private java.lang.String desReason;
	/** 作废原因 */
	private java.lang.String invalidReason;
	/** 作废申请人代码 */
	private java.lang.String invalidOpCode;
	/** 作废申请日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date invalidOpTime;
	/** 打印标志 */
	private java.lang.String printSign;
	/** 打印次数 */
	private java.lang.Integer printCount;
	/** 是否已发送银企直连 */
	private java.lang.String sended;
	/** 电子回单是否产生 */
	private java.lang.String exportSign;
	/** 联行号 */
	private java.lang.String paysysbankcode;
	/** 付息凭证 */
	private java.lang.String interestCharge;
	/** 流水ID */
	private java.lang.String bookId;
	/** 外单位ID */
	private java.lang.String externalId;
	/** 委托收款的收款单位 */
	private java.lang.String startCorp;
	/** 委托收款的收款账号 */
	private java.lang.String startCorpAcc;
	/** 委托收款的收款银行 */
	private java.lang.String startBank;
	
	/*** 摘要*/
    private  String rmk;
	
	/**
	 * 外单位账号
	 */
	private String externalAcc;
	/**
	 * 外单位
	 */
	private String externalCorp;
	/**
	 * 外单位开户行
	 */
	private String externalBank;
	
	/**
	 * 省
	 */
	@Region(inject="prov")
	private String externalProv;
	
	/**
	 * 市
	 */
	@Region(inject="city")
	private String externalCity;
	/**
	 * 省
	 */
	private String prov;
	/**
	 * 市
	 */
	private String city;
	
	private String payerAcc;
    
	public java.lang.String getBackSign() {
		return backSign;
	}
	public void setBackSign(java.lang.String backSign) {
		this.backSign = backSign;
	}
	public java.lang.Integer getTradeId() {
		return tradeId;
	}
	public void setTradeId(java.lang.Integer tradeId) {
		this.tradeId = tradeId;
	}
	public java.lang.String getCoutryType() {
		return coutryType;
	}
	public void setCoutryType(java.lang.String coutryType) {
		this.coutryType = coutryType;
	}
	public java.math.BigDecimal getReferRate() {
		return referRate;
	}
	public void setReferRate(java.math.BigDecimal referRate) {
		this.referRate = referRate;
	}
	public java.lang.Integer getFxOrnot() {
		return fxOrnot;
	}
	public void setFxOrnot(java.lang.Integer fxOrnot) {
		this.fxOrnot = fxOrnot;
	}
	public java.math.BigDecimal getTradeRate() {
		return tradeRate;
	}
	public void setTradeRate(java.math.BigDecimal tradeRate) {
		this.tradeRate = tradeRate;
	}
	public java.lang.String getFxAppnum() {
		return fxAppnum;
	}
	public void setFxAppnum(java.lang.String fxAppnum) {
		this.fxAppnum = fxAppnum;
	}
	public java.lang.Integer getFxType() {
		return fxType;
	}
	public void setFxType(java.lang.Integer fxType) {
		this.fxType = fxType;
	}
	public java.lang.String getDeclareNum() {
		return declareNum;
	}
	public void setDeclareNum(java.lang.String declareNum) {
		this.declareNum = declareNum;
	}
	public java.lang.String getBackReason() {
		return backReason;
	}
	public void setBackReason(java.lang.String backReason) {
		this.backReason = backReason;
	}
	public java.lang.String getDesReason() {
		return desReason;
	}
	public void setDesReason(java.lang.String desReason) {
		this.desReason = desReason;
	}
	public java.lang.String getInvalidReason() {
		return invalidReason;
	}
	public void setInvalidReason(java.lang.String invalidReason) {
		this.invalidReason = invalidReason;
	}
	public java.lang.String getInvalidOpCode() {
		return invalidOpCode;
	}
	public void setInvalidOpCode(java.lang.String invalidOpCode) {
		this.invalidOpCode = invalidOpCode;
	}
	public java.util.Date getInvalidOpTime() {
		return invalidOpTime;
	}
	public void setInvalidOpTime(java.util.Date invalidOpTime) {
		this.invalidOpTime = invalidOpTime;
	}
	public java.lang.String getPrintSign() {
		return printSign;
	}
	public void setPrintSign(java.lang.String printSign) {
		this.printSign = printSign;
	}
	public java.lang.Integer getPrintCount() {
		return printCount;
	}
	public void setPrintCount(java.lang.Integer printCount) {
		this.printCount = printCount;
	}
	public java.lang.String getSended() {
		return sended;
	}
	public void setSended(java.lang.String sended) {
		this.sended = sended;
	}
	public java.lang.String getExportSign() {
		return exportSign;
	}
	public void setExportSign(java.lang.String exportSign) {
		this.exportSign = exportSign;
	}
	public java.lang.String getPaysysbankcode() {
		return paysysbankcode;
	}
	public void setPaysysbankcode(java.lang.String paysysbankcode) {
		this.paysysbankcode = paysysbankcode;
	}
	public java.lang.String getInterestCharge() {
		return interestCharge;
	}
	public void setInterestCharge(java.lang.String interestCharge) {
		this.interestCharge = interestCharge;
	}
	public java.lang.String getBookId() {
		return bookId;
	}
	public void setBookId(java.lang.String bookId) {
		this.bookId = bookId;
	}
	public java.lang.String getExternalId() {
		return externalId;
	}
	public void setExternalId(java.lang.String externalId) {
		this.externalId = externalId;
	}
	public java.lang.String getStartCorp() {
		return startCorp;
	}
	public void setStartCorp(java.lang.String startCorp) {
		this.startCorp = startCorp;
	}
	public java.lang.String getStartCorpAcc() {
		return startCorpAcc;
	}
	public void setStartCorpAcc(java.lang.String startCorpAcc) {
		this.startCorpAcc = startCorpAcc;
	}
	public java.lang.String getStartBank() {
		return startBank;
	}
	public void setStartBank(java.lang.String startBank) {
		this.startBank = startBank;
	}
	public String getExternalAcc() {
		return externalAcc;
	}
	public void setExternalAcc(String externalAcc) {
		this.externalAcc = externalAcc;
	}
	public String getExternalCorp() {
		return externalCorp;
	}
	public void setExternalCorp(String externalCorp) {
		this.externalCorp = externalCorp;
	}
	public String getExternalBank() {
		return externalBank;
	}
	public void setExternalBank(String externalBank) {
		this.externalBank = externalBank;
	}
	public String getExternalProv() {
		return externalProv;
	}
	public void setExternalProv(String externalProv) {
		this.externalProv = externalProv;
	}
	public String getExternalCity() {
		return externalCity;
	}
	public void setExternalCity(String externalCity) {
		this.externalCity = externalCity;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	@Override
	public String toString() {
		return "FcAccbookExtVO{" +
				"backSign='" + backSign + '\'' +
				", tradeId=" + tradeId +
				", coutryType='" + coutryType + '\'' +
				", referRate=" + referRate +
				", fxOrnot=" + fxOrnot +
				", tradeRate=" + tradeRate +
				", fxAppnum='" + fxAppnum + '\'' +
				", fxType=" + fxType +
				", declareNum='" + declareNum + '\'' +
				", backReason='" + backReason + '\'' +
				", desReason='" + desReason + '\'' +
				", invalidReason='" + invalidReason + '\'' +
				", invalidOpCode='" + invalidOpCode + '\'' +
				", invalidOpTime=" + invalidOpTime +
				", printSign='" + printSign + '\'' +
				", printCount=" + printCount +
				", sended='" + sended + '\'' +
				", exportSign='" + exportSign + '\'' +
				", paysysbankcode='" + paysysbankcode + '\'' +
				", interestCharge='" + interestCharge + '\'' +
				", bookId='" + bookId + '\'' +
				", externalId='" + externalId + '\'' +
				", startCorp='" + startCorp + '\'' +
				", startCorpAcc='" + startCorpAcc + '\'' +
				", startBank='" + startBank + '\'' +
				", rmk='" + rmk + '\'' +
				", externalAcc='" + externalAcc + '\'' +
				", externalCorp='" + externalCorp + '\'' +
				", externalBank='" + externalBank + '\'' +
				", externalProv='" + externalProv + '\'' +
				", externalCity='" + externalCity + '\'' +
				", prov='" + prov + '\'' +
				", city='" + city + '\'' +
				", payerAcc='" + payerAcc + '\'' +
				'}';
	}
}
