package com.bytter.tally.vo;
import com.bytter.core.annotation.Region;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
public class FcAccbookExtVO implements Serializable {
	/** 打回标志 */
	private String backSign;
	/** 交易项目 */
	private Integer tradeId;
	/** 对方国别 */
	private String coutryType;
	/** 参考汇率 */
	private java.math.BigDecimal referRate;
	/** 是否外汇市场交易 */
	private Integer fxOrnot;
	/** 成交汇率 */
	private java.math.BigDecimal tradeRate;
	/** 审批件编号 */
	private String fxAppnum;
	/** 外币交易方式 */
	private Integer fxType;
	/** 申报号码 */
	private String declareNum;
	/** 打回原因 */
	private String backReason;
	/** 冲账原因 */
	private String desReason;
	/** 作废原因 */
	private String invalidReason;
	/** 作废申请人代码 */
	private String invalidOpCode;
	/** 作废申请日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date invalidOpTime;
	/** 打印标志 */
	private String printSign;
	/** 打印次数 */
	private Integer printCount;
	/** 是否已发送银企直连 */
	private String sended;
	/** 电子回单是否产生 */
	private String exportSign;
	/** 联行号 */
	private String paysysbankcode;
	/** 付息凭证 */
	private String interestCharge;
	/** 流水ID */
	private String bookId;
	/** 外单位ID */
	private String externalId;
	/** 委托收款的收款单位 */
	private String startCorp;
	/** 委托收款的收款账号 */
	private String startCorpAcc;
	/** 委托收款的收款银行 */
	private String startBank;
	
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
    
	public String getBackSign() {
		return backSign;
	}
	public void setBackSign(String backSign) {
		this.backSign = backSign;
	}
	public Integer getTradeId() {
		return tradeId;
	}
	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}
	public String getCoutryType() {
		return coutryType;
	}
	public void setCoutryType(String coutryType) {
		this.coutryType = coutryType;
	}
	public java.math.BigDecimal getReferRate() {
		return referRate;
	}
	public void setReferRate(java.math.BigDecimal referRate) {
		this.referRate = referRate;
	}
	public Integer getFxOrnot() {
		return fxOrnot;
	}
	public void setFxOrnot(Integer fxOrnot) {
		this.fxOrnot = fxOrnot;
	}
	public java.math.BigDecimal getTradeRate() {
		return tradeRate;
	}
	public void setTradeRate(java.math.BigDecimal tradeRate) {
		this.tradeRate = tradeRate;
	}
	public String getFxAppnum() {
		return fxAppnum;
	}
	public void setFxAppnum(String fxAppnum) {
		this.fxAppnum = fxAppnum;
	}
	public Integer getFxType() {
		return fxType;
	}
	public void setFxType(Integer fxType) {
		this.fxType = fxType;
	}
	public String getDeclareNum() {
		return declareNum;
	}
	public void setDeclareNum(String declareNum) {
		this.declareNum = declareNum;
	}
	public String getBackReason() {
		return backReason;
	}
	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}
	public String getDesReason() {
		return desReason;
	}
	public void setDesReason(String desReason) {
		this.desReason = desReason;
	}
	public String getInvalidReason() {
		return invalidReason;
	}
	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}
	public String getInvalidOpCode() {
		return invalidOpCode;
	}
	public void setInvalidOpCode(String invalidOpCode) {
		this.invalidOpCode = invalidOpCode;
	}
	public java.util.Date getInvalidOpTime() {
		return invalidOpTime;
	}
	public void setInvalidOpTime(java.util.Date invalidOpTime) {
		this.invalidOpTime = invalidOpTime;
	}
	public String getPrintSign() {
		return printSign;
	}
	public void setPrintSign(String printSign) {
		this.printSign = printSign;
	}
	public Integer getPrintCount() {
		return printCount;
	}
	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}
	public String getSended() {
		return sended;
	}
	public void setSended(String sended) {
		this.sended = sended;
	}
	public String getExportSign() {
		return exportSign;
	}
	public void setExportSign(String exportSign) {
		this.exportSign = exportSign;
	}
	public String getPaysysbankcode() {
		return paysysbankcode;
	}
	public void setPaysysbankcode(String paysysbankcode) {
		this.paysysbankcode = paysysbankcode;
	}
	public String getInterestCharge() {
		return interestCharge;
	}
	public void setInterestCharge(String interestCharge) {
		this.interestCharge = interestCharge;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getStartCorp() {
		return startCorp;
	}
	public void setStartCorp(String startCorp) {
		this.startCorp = startCorp;
	}
	public String getStartCorpAcc() {
		return startCorpAcc;
	}
	public void setStartCorpAcc(String startCorpAcc) {
		this.startCorpAcc = startCorpAcc;
	}
	public String getStartBank() {
		return startBank;
	}
	public void setStartBank(String startBank) {
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
