package com.bytter.bill.domain.billbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 票据贴现
 * @author onlineGenerator
 * @date 2017-06-21 15:43:17
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_DISCOUNT", schema = "")
@SuppressWarnings("serial")
public class BillAbDiscount extends IdEntity implements java.io.Serializable {
	/**流水ID*/
	private String recordId;
	/**贴现日期*/
	private java.util.Date discountDate;
	/**贴现天数*/
	private Integer daynum;
	/**贴现利息*/
	private java.math.BigDecimal interest;
	/**贴现种类：RM00 买断式、RM01 回购式买断式*/
	private String rpdmk;
	/**是否买方付息：10 是、11 否*/
	private String buyerinterestSing;
	/**买方付息比率*/
	private java.math.BigDecimal buyerinterestRatio;
	/**买方付息金额*/
	private java.math.BigDecimal buyerinterestMoney;
	/**实付金额*/
	private java.math.BigDecimal realMoney;
	/**清算方式：SM00 开启线上清算、SM01 关闭线上清算*/
	private String sttlmmk;
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
	/**赎回利率*/
	private java.math.BigDecimal  rpdintrstRate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据主表ID
	 */
	@Column(name ="RECORD_ID",nullable=true,length=32)
	public String getRecordId(){
		return this.recordId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据主表ID
	 */
	public void setRecordId(String recordId){
		this.recordId = recordId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  贴现日期
	 */
	@Column(name ="DISCOUNT_DATE",nullable=true,scale=6)
	public java.util.Date getDiscountDate(){
		return this.discountDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  贴现日期
	 */
	public void setDiscountDate(java.util.Date discountDate){
		this.discountDate = discountDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  贴现天数
	 */
	@Column(name ="DAYNUM",nullable=true,length=10)
	public Integer getDaynum(){
		return this.daynum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  贴现天数
	 */
	public void setDaynum(Integer daynum){
		this.daynum = daynum;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贴现利息
	 */
	@Column(name ="INTEREST",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getInterest(){
		return this.interest;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贴现利息
	 */
	public void setInterest(java.math.BigDecimal interest){
		this.interest = interest;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贴现种类：RM00 买断式、RM01 回购式买断式
	 */
	@Column(name ="RPDMK",nullable=true,length=4)
	public String getRpdmk(){
		return this.rpdmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贴现种类：RM00 买断式、RM01 回购式买断式
	 */
	public void setRpdmk(String rpdmk){
		this.rpdmk = rpdmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否买方付息：10 是、11 否
	 */
	@Column(name ="BUYERINTEREST_SING",nullable=true,length=1)
	public String getBuyerinterestSing(){
		return this.buyerinterestSing;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否买方付息：10 是、11 否
	 */
	public void setBuyerinterestSing(String buyerinterestSing){
		this.buyerinterestSing = buyerinterestSing;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  买方付息比率
	 */
	@Column(name ="BUYERINTEREST_RATIO",nullable=true,scale=6,length=8)
	public java.math.BigDecimal getBuyerinterestRatio(){
		return this.buyerinterestRatio;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  买方付息比率
	 */
	public void setBuyerinterestRatio(java.math.BigDecimal buyerinterestRatio){
		this.buyerinterestRatio = buyerinterestRatio;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  买方付息金额
	 */
	@Column(name ="BUYERINTEREST_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBuyerinterestMoney(){
		return this.buyerinterestMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  买方付息金额
	 */
	public void setBuyerinterestMoney(java.math.BigDecimal buyerinterestMoney){
		this.buyerinterestMoney = buyerinterestMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  实付金额
	 */
	@Column(name ="REAL_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getRealMoney(){
		return this.realMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  实付金额
	 */
	public void setRealMoney(java.math.BigDecimal realMoney){
		this.realMoney = realMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  清算方式：SM00 开启线上清算、SM01 关闭线上清算
	 */
	@Column(name ="STTLMMK",nullable=true,length=4)
	public String getSttlmmk(){
		return this.sttlmmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  清算方式：SM00 开启线上清算、SM01 关闭线上清算
	 */
	public void setSttlmmk(String sttlmmk){
		this.sttlmmk = sttlmmk;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  划款天数
	 */
	@Column(name ="PAYDATE",nullable=true,length=10)
	public Integer getPaydate(){
		return this.paydate;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  划款天数
	 */
	public void setPaydate(Integer paydate){
		this.paydate = paydate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  假期顺延天数
	 */
	@Column(name ="DEFERDATE",nullable=true,length=10)
	public Integer getDeferdate(){
		return this.deferdate;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  假期顺延天数
	 */
	public void setDeferdate(Integer deferdate){
		this.deferdate = deferdate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贴现利率
	 */
	@Column(name ="INTEREST_RATES",nullable=true,scale=6,length=8)
	public java.math.BigDecimal getInterestRates(){
		return this.interestRates;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贴现利率
	 */
	public void setInterestRates(java.math.BigDecimal interestRates){
		this.interestRates = interestRates;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  入账账户
	 */
	@Column(name ="AOACCNINF_ID",nullable=true,length=30)
	public String getAoaccninfId(){
		return this.aoaccninfId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  入账账户
	 */
	public void setAoaccninfId(String aoaccninfId){
		this.aoaccninfId = aoaccninfId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  入账行号
	 */
	@Column(name ="AOACCNINF_AC",nullable=true,length=30)
	public String getAoaccninfAc(){
		return this.aoaccninfAc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  入账行号
	 */
	public void setAoaccninfAc(String aoaccninfAc){
		this.aoaccninfAc = aoaccninfAc;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  赎回开放日
	 */
	@Column(name ="RPDOPENDT",nullable=true,scale=6)
	public java.util.Date getRpdopendt(){
		return this.rpdopendt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  赎回开放日
	 */
	public void setRpdopendt(java.util.Date rpdopendt){
		this.rpdopendt = rpdopendt;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  赎回截止日
	 */
	@Column(name ="RPDDUEDT",nullable=true,scale=6)
	public java.util.Date getRpdduedt(){
		return this.rpdduedt;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  赎回截止日
	 */
	public void setRpdduedt(java.util.Date rpdduedt){
		this.rpdduedt = rpdduedt;
	}
	@Column(name ="TXL_CTRCT_NB",nullable=true,length=250)
	public String getTxlCtrctNb() {
		return txlCtrctNb;
	}
	public void setTxlCtrctNb(String txlCtrctNb) {
		this.txlCtrctNb = txlCtrctNb;
	}
	@Column(name ="INVC_NB",nullable=true,length=250)
	public String getInvcNb() {
		return invcNb;
	}
	public void setInvcNb(String invcNb) {
		this.invcNb = invcNb;
	}
	/**
	 * 赎回利率
	 * @return
	 */
	@Column(name ="RPDINTRSTRATE",nullable=true,scale=6,length=8)
	public java.math.BigDecimal getRpdintrstRate() {
		return rpdintrstRate;
	}
	/**
	 * 赎回利率
	 * @param rpdintrstRate
	 */
	public void setRpdintrstRate(java.math.BigDecimal rpdintrstRate) {
		this.rpdintrstRate = rpdintrstRate;
	}



	public String toString() {
		return "AbDiscount [recordId=" + recordId + ", discountDate=" + discountDate + ", daynum=" + daynum
				+ ", interest=" + interest + ", rpdmk=" + rpdmk + ", buyerinterestSing=" + buyerinterestSing
				+ ", buyerinterestRatio=" + buyerinterestRatio + ", buyerinterestMoney=" + buyerinterestMoney
				+ ", realMoney=" + realMoney + ", sttlmmk=" + sttlmmk + ", paydate=" + paydate + ", deferdate="
				+ deferdate + ", interestRates=" + interestRates + ", aoaccninfId=" + aoaccninfId + ", aoaccninfAc="
				+ aoaccninfAc + ", rpdopendt=" + rpdopendt + ", rpdduedt=" + rpdduedt + ", txlCtrctNb=" + txlCtrctNb
				+ ", invcNb=" + invcNb + ", rpdintrstRate=" + rpdintrstRate + "]";
	}
	
}
