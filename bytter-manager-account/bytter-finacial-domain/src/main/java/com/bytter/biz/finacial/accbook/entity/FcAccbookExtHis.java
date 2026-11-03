package com.bytter.biz.finacial.accbook.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: FC_ACCBOOK_EXT_HIS
 * @author onlineGenerator
 * @date 2017-06-15 18:17:53
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_ACCBOOK_EXT_HIS", schema = "")
@SuppressWarnings("serial")
public class FcAccbookExtHis extends IdEntity implements java.io.Serializable {
	/**流水ID*/
	private java.lang.String bookId;
	/**外单位ID*/
	private java.lang.String externalId;
	/**委托收款的收款单位*/
	private java.lang.String startCorp;
	/**委托收款的收款账号*/
	private java.lang.String startCorpAcc;
	/**委托收款的收款银行*/
	private java.lang.String startBank;
	/**打回标志*/
	private java.lang.String backSign;
	/**交易项目*/
	private java.lang.Integer tradeId;
	/**对方国别*/
	private java.lang.String coutryType;
	/**参考汇率*/
	private java.math.BigDecimal referRate;
	/**是否外汇市场交易*/
	private java.lang.Integer fxOrnot;
	/**成交汇率*/
	private java.math.BigDecimal tradeRate;
	/**审批件编号*/
	private java.lang.String fxAppnum;
	/**外币交易方式*/
	private java.lang.Integer fxType;
	/**申报号码*/
	private java.lang.String declareNum;
	/**打回原因*/
	private java.lang.String backReason;
	/**冲账原因*/
	private java.lang.String desReason;
	/**作废原因*/
	private java.lang.String invalidReason;
	/**作废申请人代码*/
	private java.lang.String invalidOpCode;
	/**作废申请日期*/
	private java.util.Date invalidOpTime;
	/**打印标志*/
	private java.lang.String printSign;
	/**打印次数*/
	private java.lang.Integer printCount;
	/**是否已发送银企直连*/
	private java.lang.String sended;
	/**电子回单是否产生*/
	private java.lang.String exportSign;
	/**联行号*/
	private java.lang.String paysysbankcode;
	/**付息凭证*/
	private java.lang.String interestCharge;
	
	/**外单位账号*/
	private String externalAcc;
	/**外单位信息*/
	private String externalCorp;
	/**外单位开户行*/
	private String externalBank;
	/**省*/
	private String externalProv;
	/**市*/
	private String externalCity;
	/**外单位类型*/
	private String externalType;
	/**境内境外标识*/
	private String typeSign;
	
	/**付款银行账号*/
	private String payerAcc;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水ID
	 */
	@Column(name ="BOOK_ID")
	public java.lang.String getBookId(){
		return this.bookId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水ID
	 */
	public void setBookId(java.lang.String bookId){
		this.bookId = bookId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位ID
	 */
	@Column(name ="EXTERNAL_ID")
	public java.lang.String getExternalId(){
		return this.externalId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位ID
	 */
	public void setExternalId(java.lang.String externalId){
		this.externalId = externalId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托收款的收款单位
	 */
	@Column(name ="START_CORP")
	public java.lang.String getStartCorp(){
		return this.startCorp;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托收款的收款单位
	 */
	public void setStartCorp(java.lang.String startCorp){
		this.startCorp = startCorp;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托收款的收款账号
	 */
	@Column(name ="START_CORP_ACC")
	public java.lang.String getStartCorpAcc(){
		return this.startCorpAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托收款的收款账号
	 */
	public void setStartCorpAcc(java.lang.String startCorpAcc){
		this.startCorpAcc = startCorpAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托收款的收款银行
	 */
	@Column(name ="START_BANK")
	public java.lang.String getStartBank(){
		return this.startBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托收款的收款银行
	 */
	public void setStartBank(java.lang.String startBank){
		this.startBank = startBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打回标志
	 */
	@Column(name ="BACK_SIGN")
	public java.lang.String getBackSign(){
		return this.backSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打回标志
	 */
	public void setBackSign(java.lang.String backSign){
		this.backSign = backSign;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  交易项目
	 */
	@Column(name ="TRADE_ID")
	public java.lang.Integer getTradeId(){
		return this.tradeId;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  交易项目
	 */
	public void setTradeId(java.lang.Integer tradeId){
		this.tradeId = tradeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对方国别
	 */
	@Column(name ="COUTRY_TYPE")
	public java.lang.String getCoutryType(){
		return this.coutryType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对方国别
	 */
	public void setCoutryType(java.lang.String coutryType){
		this.coutryType = coutryType;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  参考汇率
	 */
	@Column(name ="REFER_RATE")
	public java.math.BigDecimal getReferRate(){
		return this.referRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  参考汇率
	 */
	public void setReferRate(java.math.BigDecimal referRate){
		this.referRate = referRate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否外汇市场交易
	 */
	@Column(name ="FX_ORNOT")
	public java.lang.Integer getFxOrnot(){
		return this.fxOrnot;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否外汇市场交易
	 */
	public void setFxOrnot(java.lang.Integer fxOrnot){
		this.fxOrnot = fxOrnot;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  成交汇率
	 */
	@Column(name ="TRADE_RATE")
	public java.math.BigDecimal getTradeRate(){
		return this.tradeRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  成交汇率
	 */
	public void setTradeRate(java.math.BigDecimal tradeRate){
		this.tradeRate = tradeRate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  审批件编号
	 */
	@Column(name ="FX_APPNUM")
	public java.lang.String getFxAppnum(){
		return this.fxAppnum;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审批件编号
	 */
	public void setFxAppnum(java.lang.String fxAppnum){
		this.fxAppnum = fxAppnum;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  外币交易方式
	 */
	@Column(name ="FX_TYPE")
	public java.lang.Integer getFxType(){
		return this.fxType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  外币交易方式
	 */
	public void setFxType(java.lang.Integer fxType){
		this.fxType = fxType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  申报号码
	 */
	@Column(name ="DECLARE_NUM")
	public java.lang.String getDeclareNum(){
		return this.declareNum;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  申报号码
	 */
	public void setDeclareNum(java.lang.String declareNum){
		this.declareNum = declareNum;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打回原因
	 */
	@Column(name ="BACK_REASON")
	public java.lang.String getBackReason(){
		return this.backReason;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打回原因
	 */
	public void setBackReason(java.lang.String backReason){
		this.backReason = backReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  冲账原因
	 */
	@Column(name ="DES_REASON")
	public java.lang.String getDesReason(){
		return this.desReason;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  冲账原因
	 */
	public void setDesReason(java.lang.String desReason){
		this.desReason = desReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  作废原因
	 */
	@Column(name ="INVALID_REASON")
	public java.lang.String getInvalidReason(){
		return this.invalidReason;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  作废原因
	 */
	public void setInvalidReason(java.lang.String invalidReason){
		this.invalidReason = invalidReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  作废申请人代码
	 */
	@Column(name ="INVALID_OP_CODE")
	public java.lang.String getInvalidOpCode(){
		return this.invalidOpCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  作废申请人代码
	 */
	public void setInvalidOpCode(java.lang.String invalidOpCode){
		this.invalidOpCode = invalidOpCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  作废申请日期
	 */
	@Column(name ="INVALID_OP_TIME")
	public java.util.Date getInvalidOpTime(){
		return this.invalidOpTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  作废申请日期
	 */
	public void setInvalidOpTime(java.util.Date invalidOpTime){
		this.invalidOpTime = invalidOpTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打印标志
	 */
	@Column(name ="PRINT_SIGN")
	public java.lang.String getPrintSign(){
		return this.printSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打印标志
	 */
	public void setPrintSign(java.lang.String printSign){
		this.printSign = printSign;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  打印次数
	 */
	@Column(name ="PRINT_COUNT")
	public java.lang.Integer getPrintCount(){
		return this.printCount;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  打印次数
	 */
	public void setPrintCount(java.lang.Integer printCount){
		this.printCount = printCount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否已发送银企直连
	 */
	@Column(name ="SENDED")
	public java.lang.String getSended(){
		return this.sended;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否已发送银企直连
	 */
	public void setSended(java.lang.String sended){
		this.sended = sended;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子回单是否产生
	 */
	@Column(name ="EXPORT_SIGN")
	public java.lang.String getExportSign(){
		return this.exportSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单是否产生
	 */
	public void setExportSign(java.lang.String exportSign){
		this.exportSign = exportSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  联行号
	 */
	@Column(name ="PAYSYSBANKCODE")
	public java.lang.String getPaysysbankcode(){
		return this.paysysbankcode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  联行号
	 */
	public void setPaysysbankcode(java.lang.String paysysbankcode){
		this.paysysbankcode = paysysbankcode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付息凭证
	 */
	@Column(name ="INTEREST_CHARGE")
	public java.lang.String getInterestCharge(){
		return this.interestCharge;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付息凭证
	 */
	public void setInterestCharge(java.lang.String interestCharge){
		this.interestCharge = interestCharge;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位账号
	 */
	@Column(name ="EXTERNAL_ACC")
	public java.lang.String getExternalAcc(){
		return this.externalAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位账号
	 */
	public void setExternalAcc(java.lang.String externalAcc){
		this.externalAcc = externalAcc;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位
	 */
	@Column(name ="EXTERNAL_CORP")
	public java.lang.String getExternalCorp(){
		return this.externalCorp;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位
	 */
	public void setExternalCorp(java.lang.String externalCorp){
		this.externalCorp = externalCorp;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位开户行
	 */
	@Column(name ="EXTERNAL_BANK")
	public java.lang.String getExternalBank(){
		return this.externalBank;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位
	 */
	public void setExternalBank(java.lang.String externalBank){
		this.externalBank = externalBank;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位开户行
	 */
	@Column(name ="EXTERNAL_PROV")
	public java.lang.String getExternalProv(){
		return this.externalProv;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位
	 */
	public void setExternalProv(java.lang.String externalProv){
		this.externalProv = externalProv;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位开户行
	 */
	@Column(name ="EXTERNAL_CITY")
	public java.lang.String getExternalCity(){
		return this.externalCity;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位
	 */
	public void setExternalCity(java.lang.String externalCity){
		this.externalCity = externalCity;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款银行账号
	 */
	@Column(name ="PAYER_ACC")
	public java.lang.String getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款银行账号
	 */
	public void setPayerAcc(java.lang.String payerAcc){
		this.payerAcc = payerAcc;
	}
	@Column(name ="EXTERNAL_TYPE")
	public String getExternalType() {
		return externalType;
	}
	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}
	@Column(name ="TYPE_SIGN")
	public String getTypeSign() {
		return typeSign;
	}
	public void setTypeSign(String typeSign) {
		this.typeSign = typeSign;
	}
}
