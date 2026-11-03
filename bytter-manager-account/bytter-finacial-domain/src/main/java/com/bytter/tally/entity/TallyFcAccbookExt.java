package com.bytter.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**   
 * @Title: Entity
 * @Description: FC_ACCBOOK_EXT
 * @author onlineGenerator
 * @date 2017-05-31 15:03:10
 * @version 1.0
 * TODO 非本域实体，临时解决
 */
@Entity
@Table(name = "FC_ACCBOOK_EXT", schema = "")
@SuppressWarnings("serial")
public class TallyFcAccbookExt extends IdEntity implements java.io.Serializable {
	/**打回标志*/
	private String backSign;
	/**交易项目*/
	private Integer tradeId;
	/**对方国别*/
	private String coutryType;
	/**参考汇率*/
	private java.math.BigDecimal referRate;
	/**是否外汇市场交易*/
	private Integer fxOrnot;
	/**成交汇率*/
	private java.math.BigDecimal tradeRate;
	/**审批件编号*/
	private String fxAppnum;
	/**外币交易方式*/
	private Integer fxType;
	/**申报号码*/
	private String declareNum;
	/**打回原因*/
	private String backReason;
	/**冲账原因*/
	private String desReason;
	/**作废原因*/
	private String invalidReason;
	/**作废申请人代码*/
	private String invalidOpCode;
	/**作废申请日期*/
	private java.util.Date invalidOpTime;
	/**打印标志*/
	private String printSign;
	/**打印次数*/
	private Integer printCount;
	/**是否已发送银企直连*/
	private String sended;
	/**电子回单是否产生*/
	private String exportSign;
	/**联行号*/
	private String paysysbankcode;
	/**付息凭证*/
	private String interestCharge;
	/**流水ID*/
	private String bookId;
	/**外单位ID*/
	private String externalId;
	/**委托收款的收款单位*/
	private String startCorp;
	/**委托收款的收款账号*/
	private String startCorpAcc;
	/**委托收款的收款银行*/
	private String startBank;
	
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
	/**付款银行账号*/
	private String payerAcc;
	/**外单位类型*/
	private String externalType;
	/**境内境外标识*/
	private String typeSign;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打回标志
	 */
	@Column(name ="BACK_SIGN")
	public String getBackSign(){
		return this.backSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打回标志
	 */
	public void setBackSign(String backSign){
		this.backSign = backSign;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  交易项目
	 */
	@Column(name ="TRADE_ID")
	public Integer getTradeId(){
		return this.tradeId;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  交易项目
	 */
	public void setTradeId(Integer tradeId){
		this.tradeId = tradeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对方国别
	 */
	@Column(name ="COUTRY_TYPE")
	public String getCoutryType(){
		return this.coutryType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对方国别
	 */
	public void setCoutryType(String coutryType){
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
	public Integer getFxOrnot(){
		return this.fxOrnot;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否外汇市场交易
	 */
	public void setFxOrnot(Integer fxOrnot){
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
	public String getFxAppnum(){
		return this.fxAppnum;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审批件编号
	 */
	public void setFxAppnum(String fxAppnum){
		this.fxAppnum = fxAppnum;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  外币交易方式
	 */
	@Column(name ="FX_TYPE")
	public Integer getFxType(){
		return this.fxType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  外币交易方式
	 */
	public void setFxType(Integer fxType){
		this.fxType = fxType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  申报号码
	 */
	@Column(name ="DECLARE_NUM")
	public String getDeclareNum(){
		return this.declareNum;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  申报号码
	 */
	public void setDeclareNum(String declareNum){
		this.declareNum = declareNum;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打回原因
	 */
	@Column(name ="BACK_REASON")
	public String getBackReason(){
		return this.backReason;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打回原因
	 */
	public void setBackReason(String backReason){
		this.backReason = backReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  冲账原因
	 */
	@Column(name ="DES_REASON")
	public String getDesReason(){
		return this.desReason;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  冲账原因
	 */
	public void setDesReason(String desReason){
		this.desReason = desReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  作废原因
	 */
	@Column(name ="INVALID_REASON")
	public String getInvalidReason(){
		return this.invalidReason;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  作废原因
	 */
	public void setInvalidReason(String invalidReason){
		this.invalidReason = invalidReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  作废申请人代码
	 */
	@Column(name ="INVALID_OP_CODE")
	public String getInvalidOpCode(){
		return this.invalidOpCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  作废申请人代码
	 */
	public void setInvalidOpCode(String invalidOpCode){
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
	public String getPrintSign(){
		return this.printSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打印标志
	 */
	public void setPrintSign(String printSign){
		this.printSign = printSign;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  打印次数
	 */
	@Column(name ="PRINT_COUNT")
	public Integer getPrintCount(){
		return this.printCount;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  打印次数
	 */
	public void setPrintCount(Integer printCount){
		this.printCount = printCount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否已发送银企直连
	 */
	@Column(name ="SENDED")
	public String getSended(){
		return this.sended;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否已发送银企直连
	 */
	public void setSended(String sended){
		this.sended = sended;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子回单是否产生
	 */
	@Column(name ="EXPORT_SIGN")
	public String getExportSign(){
		return this.exportSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单是否产生
	 */
	public void setExportSign(String exportSign){
		this.exportSign = exportSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  联行号
	 */
	@Column(name ="PAYSYSBANKCODE")
	public String getPaysysbankcode(){
		return this.paysysbankcode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  联行号
	 */
	public void setPaysysbankcode(String paysysbankcode){
		this.paysysbankcode = paysysbankcode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付息凭证
	 */
	@Column(name ="INTEREST_CHARGE")
	public String getInterestCharge(){
		return this.interestCharge;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付息凭证
	 */
	public void setInterestCharge(String interestCharge){
		this.interestCharge = interestCharge;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水ID
	 */
	@Column(name ="BOOK_ID")
	public String getBookId(){
		return this.bookId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水ID
	 */
	public void setBookId(String bookId){
		this.bookId = bookId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位ID
	 */
	@Column(name ="EXTERNAL_ID")
	public String getExternalId(){
		return this.externalId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位ID
	 */
	public void setExternalId(String externalId){
		this.externalId = externalId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托收款的收款单位
	 */
	@Column(name ="START_CORP")
	public String getStartCorp(){
		return this.startCorp;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托收款的收款单位
	 */
	public void setStartCorp(String startCorp){
		this.startCorp = startCorp;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托收款的收款账号
	 */
	@Column(name ="START_CORP_ACC")
	public String getStartCorpAcc(){
		return this.startCorpAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托收款的收款账号
	 */
	public void setStartCorpAcc(String startCorpAcc){
		this.startCorpAcc = startCorpAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托收款的收款银行
	 */
	@Column(name ="START_BANK")
	public String getStartBank(){
		return this.startBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托收款的收款银行
	 */
	public void setStartBank(String startBank){
		this.startBank = startBank;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位账号
	 */
	@Column(name ="EXTERNAL_ACC")
	public String getExternalAcc(){
		return this.externalAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位账号
	 */
	public void setExternalAcc(String externalAcc){
		this.externalAcc = externalAcc;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位
	 */
	@Column(name ="EXTERNAL_CORP")
	public String getExternalCorp(){
		return this.externalCorp;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位
	 */
	public void setExternalCorp(String externalCorp){
		this.externalCorp = externalCorp;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位开户行
	 */
	@Column(name ="EXTERNAL_BANK")
	public String getExternalBank(){
		return this.externalBank;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位
	 */
	public void setExternalBank(String externalBank){
		this.externalBank = externalBank;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位开户行
	 */
	@Column(name ="EXTERNAL_PROV")
	public String getExternalProv(){
		return this.externalProv;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位
	 */
	public void setExternalProv(String externalProv){
		this.externalProv = externalProv;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  外单位开户行
	 */
	@Column(name ="EXTERNAL_CITY")
	public String getExternalCity(){
		return this.externalCity;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  外单位
	 */
	public void setExternalCity(String externalCity){
		this.externalCity = externalCity;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款银行账号
	 */
	@Column(name ="PAYER_ACC")
	public String getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款银行账号
	 */
	public void setPayerAcc(String payerAcc){
		this.payerAcc = payerAcc;
	}
	/**
     * 跨行标志
     */
    private String difBank;
    
    /**
     * 地域标志
     */
    private String areaSign;
    @Transient
	public String getDifBank() {
		return difBank;
	}
	public void setDifBank(String difBank) {
		this.difBank = difBank;
	}
	@Transient
	public String getAreaSign() {
		return areaSign;
	}
	public void setAreaSign(String areaSign) {
		this.areaSign = areaSign;
	}
	/**
     * 外单位账号
     */
    private String oppAccNo;
    /**
     * 外单位户名
     */
    private String oppAccName;
    /**
     * 外单位开户行
     */
    private String  oppBankName;
    /**
     * 紧急标志
     */
    private String emergencySign;
    /**
     * 摘要
     */
    private String rmk;
    
    /**
     * 省市
     */
    private String prov;
    /**
     * 省市
     */
    private String city;
    
    @Transient
	public String getOppAccNo() {
		return oppAccNo;
	}
	public void setOppAccNo(String oppAccNo) {
		this.oppAccNo = oppAccNo;
	}
	@Transient
	public String getOppAccName() {
		return oppAccName;
	}
	public void setOppAccName(String oppAccName) {
		this.oppAccName = oppAccName;
	}
	@Transient
	public String getOppBankName() {
		return oppBankName;
	}
	public void setOppBankName(String oppBankName) {
		this.oppBankName = oppBankName;
	}
	
	@Transient
	public String getEmergencySign() {
		return emergencySign;
	}
	public void setEmergencySign(String emergencySign) {
		this.emergencySign = emergencySign;
	}
	
	@Transient
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	
	@Transient
	public String getProv() {
		return prov;
	}
	
	public void setProv(String prov) {
		this.prov = prov;
	}
	@Transient
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
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
