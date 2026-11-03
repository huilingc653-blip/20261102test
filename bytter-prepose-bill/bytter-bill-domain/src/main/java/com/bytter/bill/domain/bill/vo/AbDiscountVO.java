package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.Dict;
@SuppressWarnings("serial")
public class AbDiscountVO extends AbAccrecordsVO{
   
	/**流水ID*/
	private String recordId;
	/**贴现日期*/
	private java.util.Date discountDate;
	/**贴现天数*/
	private Integer daynum;
	/**贴现利息*/
	private java.math.BigDecimal interest;
	/**贴现种类：RM00 买断式、RM01 回购式买断式*/
	@Dict(dictGroup = "AB_DISCOUNT_RPDMK", inject ="rpdmkName")
	private String rpdmk;
	private String rpdmkName;
	/**是否买方付息：10 是、11 否*/
	private String buyerinterestSing;
	/**买方付息比率*/
	private java.math.BigDecimal buyerinterestRatio;
	/**买方付息金额*/
	private java.math.BigDecimal buyerinterestMoney;
	/**实付金额*/
	private java.math.BigDecimal realMoney;
	/**清算方式：SM00 开启线上清算、SM01 关闭线上清算*/
	@Dict(dictGroup = "AB_STTLMMK", inject ="sttlmmkName")
	private String sttlmmk;
	private String sttlmmkName;
	/**划款天数*/
	private Integer paydate;
	/**假期顺延天数*/
	private Integer deferdate;
	/**贴现利率*/
	private java.math.BigDecimal interestRates;
	/**入账账户*/
	private String aoaccninfId;
	/**入账行号*/
	private String aoaccninfAc;
	/**赎回开放日*/
	private java.util.Date rpdopendt;
	/**赎回截止日*/
	private java.util.Date rpdduedt;
	/**交易合同号*/
	private String txlCtrctNb;
	/**发票号码*/
	private String invcNb;
	/**占用比例*/
	private java.math.BigDecimal occupancyRatio;
	/**
	 * 实付金额数组
	 */
	private String realMoneys;
	public String getRealMoneys() {
		return realMoneys;
	}
	public void setRealMoneys(String realMoneys) {
		this.realMoneys = realMoneys;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public java.util.Date getDiscountDate() {
		return discountDate;
	}
	public void setDiscountDate(java.util.Date discountDate) {
		this.discountDate = discountDate;
	}
	public Integer getDaynum() {
		return daynum;
	}
	public void setDaynum(Integer daynum) {
		this.daynum = daynum;
	}
	public java.math.BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(java.math.BigDecimal interest) {
		this.interest = interest;
	}
	public String getRpdmk() {
		return rpdmk;
	}
	public void setRpdmk(String rpdmk) {
		this.rpdmk = rpdmk;
	}
	public String getRpdmkName() {
		return rpdmkName;
	}
	public void setRpdmkName(String rpdmkName) {
		this.rpdmkName = rpdmkName;
	}
	public String getBuyerinterestSing() {
		return buyerinterestSing;
	}
	public void setBuyerinterestSing(String buyerinterestSing) {
		this.buyerinterestSing = buyerinterestSing;
	}
	public java.math.BigDecimal getBuyerinterestRatio() {
		return buyerinterestRatio;
	}
	public void setBuyerinterestRatio(java.math.BigDecimal buyerinterestRatio) {
		this.buyerinterestRatio = buyerinterestRatio;
	}
	public java.math.BigDecimal getBuyerinterestMoney() {
		return buyerinterestMoney;
	}
	public void setBuyerinterestMoney(java.math.BigDecimal buyerinterestMoney) {
		this.buyerinterestMoney = buyerinterestMoney;
	}
	public java.math.BigDecimal getRealMoney() {
		return realMoney;
	}
	public void setRealMoney(java.math.BigDecimal realMoney) {
		this.realMoney = realMoney;
	}
	public String getSttlmmk() {
		return sttlmmk;
	}
	public void setSttlmmk(String sttlmmk) {
		this.sttlmmk = sttlmmk;
	}
	public String getSttlmmkName() {
		return sttlmmkName;
	}
	public void setSttlmmkName(String sttlmmkName) {
		this.sttlmmkName = sttlmmkName;
	}
	public Integer getPaydate() {
		return paydate;
	}
	public void setPaydate(Integer paydate) {
		this.paydate = paydate;
	}
	public Integer getDeferdate() {
		return deferdate;
	}
	public void setDeferdate(Integer deferdate) {
		this.deferdate = deferdate;
	}
	public java.math.BigDecimal getInterestRates() {
		return interestRates;
	}
	public void setInterestRates(java.math.BigDecimal interestRates) {
		this.interestRates = interestRates;
	}
	public String getAoaccninfId() {
		return aoaccninfId;
	}
	public void setAoaccninfId(String aoaccninfId) {
		this.aoaccninfId = aoaccninfId;
	}
	public String getAoaccninfAc() {
		return aoaccninfAc;
	}
	public void setAoaccninfAc(String aoaccninfAc) {
		this.aoaccninfAc = aoaccninfAc;
	}
	public java.util.Date getRpdopendt() {
		return rpdopendt;
	}
	public void setRpdopendt(java.util.Date rpdopendt) {
		this.rpdopendt = rpdopendt;
	}
	public java.util.Date getRpdduedt() {
		return rpdduedt;
	}
	public void setRpdduedt(java.util.Date rpdduedt) {
		this.rpdduedt = rpdduedt;
	}
	public String getTxlCtrctNb() {
		return txlCtrctNb;
	}
	public void setTxlCtrctNb(String txlCtrctNb) {
		this.txlCtrctNb = txlCtrctNb;
	}
	public String getInvcNb() {
		return invcNb;
	}
	public void setInvcNb(String invcNb) {
		this.invcNb = invcNb;
	}
	public java.math.BigDecimal getOccupancyRatio() {
		return occupancyRatio;
	}
	public void setOccupancyRatio(java.math.BigDecimal occupancyRatio) {
		this.occupancyRatio = occupancyRatio;
	}
	
}
