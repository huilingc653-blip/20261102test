package com.bytter.biz.bdlinks.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: BIS_EXC
 * @author onlineGenerator
 * @date 2017-06-28 16:31:56
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_EXC", schema = "")
@SuppressWarnings("serial")
public class BisExc extends IdEntity implements java.io.Serializable {



	/**指令编号*/
	private java.lang.String voucherNo;
	/**付款单ID*/
	private java.lang.String cbsBillId;
	/**付款单号*/
	private java.lang.String cbsBillNo;
	/**单位ID*/
	private java.lang.String corpId;
	/**申请日期*/
	private java.util.Date reqDate;
	/**中心账号*/
	private java.lang.String counterAcc;
	/**付款账号*/
	private java.lang.String payerAcc;
	/**付款账号名称*/
	private java.lang.String payerAccName;
	/**付款开户行名称*/
	private java.lang.String payerBank;
	/**付方市*/
	private java.lang.String payerCity;
	/**付方省*/
	private java.lang.String payerProv;
	/**币别*/
	private java.lang.String curId;
	/**金额*/
	private java.math.BigDecimal payMoney;
	/**付款方式*/
	private java.lang.String voucherTypeId;
	/**收款账号*/
	private java.lang.String payeeAcc;
	/**收款账号名称*/
	private java.lang.String payeeAccName;
	/**收款方开户行行号*/
	private java.lang.String payeeBankCode;
	/**收款开户行名称*/
	private java.lang.String payeeBank;
	/**收方省*/
	private java.lang.String payeeProv;
	/**收方市*/
	private java.lang.String payeeCity;
	/**期望支付日期*/
	private java.util.Date wishPayday;
	/**加急标志 1-是 0-否*/
	private java.lang.String isQuick;
	/**交易模式 3-对公 4-对私*/
	private java.lang.String transMode;
	/**接口代码*/
	private java.lang.String bifCode;
	/**跨行标识 0-同行 1-他行*/
	private java.lang.String difBank;
	/**地域标识 0-同城 1-异地*/
	private java.lang.String areaSign;
	/**用途*/
	private java.lang.String purpose;
	/**附言*/
	private java.lang.String addWord;
	/**指令来源*/
	private java.lang.String voucherFrom;
	/**对私付款指令来源 0-付款办理 1-费用报销*/
	private String voucherFromPrivate;
	/**审批标识*/
	private java.lang.String approveSign;
	/**业务类型*/
	private java.lang.Integer busOriType;
	/**指令类型*/
	private java.lang.String cmdType;
	/**调拨策略ID*/
	private java.lang.String policyId;
	/**记账标志*/
	private java.lang.String tallySign;
	/**记账员代码*/
	private java.lang.String tallyCode;
	/**业务全局ID*/
	private java.lang.String busGlobalId;
	/**消息提醒标志*/
	private java.lang.String noticeSign;
	/**银行流水号*/
	private java.lang.String bankSerialNo;
	/**交易状态*/
	private java.lang.String voucherStat;
	/**交易时间*/
	private java.util.Date transTime;
	/**返回时间*/
	private java.util.Date returnTime;
	/**返回信息（交易前）*/
	private java.lang.String firstReturnMsg;
	/**返回信息（交易后）*/
	private java.lang.String nextReturnMsg;
	/**状态*/
	private java.lang.Integer status;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**失败原因*/
	private String failReason;
	/**加密信息*/
	private String verifyCode;
	/**时间戳*/
	private String timeStamp;
	/**指令重发标志*/
	private String resendSign;
	/**中间付款帐号*/
	private String midPayerAccNo;
	/**中间付款人*/
	private String midPayerAccName;
	/**中间付款开户行*/
	private String midPayerBankName;
	/**跨行转付在下拨指令成功后延迟支付时间*/
	private Integer delayTime;

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  指令编号
	 */
	@Column(name ="VOUCHER_NO",nullable=false,length=12)
	public java.lang.String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指令编号
	 */
	public void setVoucherNo(java.lang.String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款单ID
	 */
	@Column(name ="CBS_BILL_ID",nullable=true,length=100)
	public java.lang.String getCbsBillId(){
		return this.cbsBillId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单ID
	 */
	public void setCbsBillId(java.lang.String cbsBillId){
		this.cbsBillId = cbsBillId;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款单号
	 */
	@Column(name ="CBS_BILL_NO",nullable=true,length=30)
	public java.lang.String getCbsBillNo() {
		return cbsBillNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单号
	 */
	public void setCbsBillNo(java.lang.String cbsBillNo) {
		this.cbsBillNo = cbsBillNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位ID
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	
	/**
	 *方法: 取得java.lang.Date
	 *@return: java.lang.Date  申请日期
	 */
	@Column(name ="REQ_DATE",nullable=true)
	public java.util.Date getReqDate() {
		return reqDate;
	}
	/**
	 *方法: 设置java.lang.Date
	 *@param: java.lang.Date  申请日期
	 */
	public void setReqDate(java.util.Date reqDate) {
		this.reqDate = reqDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  中心账号
	 */
	@Column(name ="COUNTER_ACC",nullable=true)
	public java.lang.String getCounterAcc() {
		return counterAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  中心账号
	 */
	public void setCounterAcc(java.lang.String counterAcc) {
		this.counterAcc = counterAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款账号
	 */
	@Column(name ="PAYER_ACC",nullable=false,length=50)
	public java.lang.String getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款账号
	 */
	public void setPayerAcc(java.lang.String payerAcc){
		this.payerAcc = payerAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款账号名称
	 */
	@Column(name ="PAYER_ACC_NAME",nullable=true,length=150)
	public java.lang.String getPayerAccName(){
		return this.payerAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款账号名称
	 */
	public void setPayerAccName(java.lang.String payerAccName){
		this.payerAccName = payerAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款开户行名称
	 */
	@Column(name ="PAYER_BANK",nullable=true,length=150)
	public java.lang.String getPayerBank(){
		return this.payerBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款开户行名称
	 */
	public void setPayerBank(java.lang.String payerBank){
		this.payerBank = payerBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付方市
	 */
	@Column(name ="PAYER_CITY",nullable=true,length=32)
	public java.lang.String getPayerCity(){
		return this.payerCity;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付方市
	 */
	public void setPayerCity(java.lang.String payerCity){
		this.payerCity = payerCity;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付方省
	 */
	@Column(name ="PAYER_PROV",nullable=true,length=32)
	public java.lang.String getPayerProv(){
		return this.payerProv;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付方省
	 */
	public void setPayerProv(java.lang.String payerProv){
		this.payerProv = payerProv;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_ID",nullable=false,length=32)
	public java.lang.String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别
	 */
	public void setCurId(java.lang.String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="PAY_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getPayMoney(){
		return this.payMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setPayMoney(java.math.BigDecimal payMoney){
		this.payMoney = payMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款方式
	 */
	@Column(name ="VOUCHER_TYPE_ID",nullable=false,length=32)
	public java.lang.String getVoucherTypeId(){
		return this.voucherTypeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款方式
	 */
	public void setVoucherTypeId(java.lang.String voucherTypeId){
		this.voucherTypeId = voucherTypeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款账号
	 */
	@Column(name ="PAYEE_ACC",nullable=true,length=50)
	public java.lang.String getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号
	 */
	public void setPayeeAcc(java.lang.String payeeAcc){
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款账号名称
	 */
	@Column(name ="PAYEE_ACC_NAME",nullable=true,length=150)
	public java.lang.String getPayeeAccName(){
		return this.payeeAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号名称
	 */
	public void setPayeeAccName(java.lang.String payeeAccName){
		this.payeeAccName = payeeAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款方开户行行号
	 */
	@Column(name ="PAYEE_BANK_CODE",nullable=true,length=20)
	public java.lang.String getPayeeBankCode(){
		return this.payeeBankCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款方开户行行号
	 */
	public void setPayeeBankCode(java.lang.String payeeBankCode){
		this.payeeBankCode = payeeBankCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款开户行名称
	 */
	@Column(name ="PAYEE_BANK",nullable=true,length=150)
	public java.lang.String getPayeeBank(){
		return this.payeeBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款开户行名称
	 */
	public void setPayeeBank(java.lang.String payeeBank){
		this.payeeBank = payeeBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收方省
	 */
	@Column(name ="PAYEE_PROV",nullable=true,length=32)
	public java.lang.String getPayeeProv(){
		return this.payeeProv;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收方省
	 */
	public void setPayeeProv(java.lang.String payeeProv){
		this.payeeProv = payeeProv;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收方市
	 */
	@Column(name ="PAYEE_CITY",nullable=true,length=32)
	public java.lang.String getPayeeCity(){
		return this.payeeCity;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收方市
	 */
	public void setPayeeCity(java.lang.String payeeCity){
		this.payeeCity = payeeCity;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  期望支付日期
	 */
	@Column(name ="WISH_PAYDAY",nullable=true)
	public java.util.Date getWishPayday(){
		return this.wishPayday;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  期望支付日期
	 */
	public void setWishPayday(java.util.Date wishPayday){
		this.wishPayday = wishPayday;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  加急标志 1-是 0-否
	 */
	@Column(name ="IS_QUICK",nullable=true,length=1)
	public java.lang.String getIsQuick(){
		return this.isQuick;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加急标志 1-是 0-否
	 */
	public void setIsQuick(java.lang.String isQuick){
		this.isQuick = isQuick;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  交易模式 3-对公 4-对私
	 */
	@Column(name ="TRANS_MODE",nullable=true,length=1)
	public java.lang.String getTransMode(){
		return this.transMode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  交易模式 3-对公 4-对私
	 */
	public void setTransMode(java.lang.String transMode){
		this.transMode = transMode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口代码
	 */
	@Column(name ="BIF_CODE",nullable=true,length=32)
	public java.lang.String getBifCode(){
		return this.bifCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口代码
	 */
	public void setBifCode(java.lang.String bifCode){
		this.bifCode = bifCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  跨行标识 0-同行 1-他行
	 */
	@Column(name ="DIF_BANK",nullable=true,length=1)
	public java.lang.String getDifBank(){
		return this.difBank;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  跨行标识 0-同行 1-他行
	 */
	public void setDifBank(java.lang.String difBank){
		this.difBank = difBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  地域标识 0-同城 1-异地
	 */
	@Column(name ="AREA_SIGN",nullable=true,length=1)
	public java.lang.String getAreaSign(){
		return this.areaSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  地域标识 0-同城 1-异地
	 */
	public void setAreaSign(java.lang.String areaSign){
		this.areaSign = areaSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  用途
	 */
	@Column(name ="PURPOSE",nullable=true,length=60)
	public java.lang.String getPurpose(){
		return this.purpose;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用途
	 */
	public void setPurpose(java.lang.String purpose){
		this.purpose = purpose;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附言
	 */
	@Column(name ="ADD_WORD",nullable=true,length=100)
	public java.lang.String getAddWord(){
		return this.addWord;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附言
	 */
	public void setAddWord(java.lang.String addWord){
		this.addWord = addWord;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  指令来源1——来自下拨2——来自头寸调拨3——来自上划4——来自请款5——来自付款6——来自柜台手工记账
	 */
	@Column(name ="VOUCHER_FROM",nullable=true,length=2)
	public java.lang.String getVoucherFrom(){
		return this.voucherFrom;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指令来源1——来自下拨2——来自头寸调拨3——来自上划4——来自请款5——来自付款6——来自柜台手工记账
	 */
	public void setVoucherFrom(java.lang.String voucherFrom){
		this.voucherFrom = voucherFrom;
	}
	@Column(name ="VOUCHER_FROM_PRIVATE",nullable=true,length=2)
	public String getVoucherFromPrivate() {
		return voucherFromPrivate;
	}
	public void setVoucherFromPrivate(String voucherFromPrivate) {
		this.voucherFromPrivate = voucherFromPrivate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  审批标识
	 */
	@Column(name ="APPROVE_SIGN",nullable=true,length=1)
	public java.lang.String getApproveSign(){
		return this.approveSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审批标识
	 */
	public void setApproveSign(java.lang.String approveSign){
		this.approveSign = approveSign;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  业务类型
	 */
	@Column(name ="BUS_ORI_TYPE",nullable=true,length=2)
	public java.lang.Integer getBusOriType(){
		return this.busOriType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务类型
	 */
	public void setBusOriType(java.lang.Integer busOriType){
		this.busOriType = busOriType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  指令类型
	 */
	@Column(name ="CMD_TYPE",nullable=true)
	public java.lang.String getCmdType() {
		return cmdType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指令类型
	 */
	public void setCmdType(java.lang.String cmdType) {
		this.cmdType = cmdType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行流水号
	 */
	@Column(name ="BANK_SERIAL_NO",nullable=true,length=60)
	public java.lang.String getBankSerialNo(){
		return this.bankSerialNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行流水号
	 */
	public void setBankSerialNo(java.lang.String bankSerialNo){
		this.bankSerialNo = bankSerialNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  调拨策略ID
	 */
	@Column(name ="POLICY_ID",nullable=true,length=32)
	public java.lang.String getPolicyId() {
		return policyId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  调拨策略ID
	 */
	public void setPolicyId(java.lang.String policyId) {
		this.policyId = policyId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账标志
	 */
	@Column(name ="TALLY_SIGN",nullable=true,length=1)
	public java.lang.String getTallySign() {
		return tallySign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账标志
	 */
	public void setTallySign(java.lang.String tallySign) {
		this.tallySign = tallySign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账员代码
	 */
	@Column(name ="TALLY_CODE",nullable=true,length=32)
	public java.lang.String getTallyCode() {
		return tallyCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账员代码
	 */
	public void setTallyCode(java.lang.String tallyCode) {
		this.tallyCode = tallyCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务全局ID
	 */
	@Column(name ="BUS_GLOBAL_ID",nullable=true,length=50)
	public java.lang.String getBusGlobalId() {
		return busGlobalId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务全局ID
	 */
	public void setBusGlobalId(java.lang.String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账标志
	 */
	@Column(name ="NOTICE_SIGN",nullable=true,length=1)
	public java.lang.String getNoticeSign() {
		return noticeSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  消息提醒标志
	 */
	public void setNoticeSign(java.lang.String noticeSign) {
		this.noticeSign = noticeSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  交易状态
	 */
	@Column(name ="VOUCHER_STAT",nullable=true,length=1)
	public java.lang.String getVoucherStat(){
		return this.voucherStat;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  交易状态
	 */
	public void setVoucherStat(java.lang.String voucherStat){
		this.voucherStat = voucherStat;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  交易时间
	 */
	@Column(name ="TRANS_TIME",nullable=true)
	public java.util.Date getTransTime(){
		return this.transTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  交易时间
	 */
	public void setTransTime(java.util.Date transTime){
		this.transTime = transTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  返回时间
	 */
	@Column(name ="RETURN_TIME",nullable=true)
	public java.util.Date getReturnTime(){
		return this.returnTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  返回时间
	 */
	public void setReturnTime(java.util.Date returnTime){
		this.returnTime = returnTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  返回信息（交易前）
	 */
	@Column(name ="FIRST_RETURN_MSG",nullable=true,length=2000)
	public java.lang.String getFirstReturnMsg(){
		return this.firstReturnMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  返回信息（交易前）
	 */
	public void setFirstReturnMsg(java.lang.String firstReturnMsg){
		this.firstReturnMsg = firstReturnMsg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  返回信息（交易后）
	 */
	@Column(name ="NEXT_RETURN_MSG",nullable=true,length=2000)
	public java.lang.String getNextReturnMsg(){
		return this.nextReturnMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  返回信息（交易后）
	 */
	public void setNextReturnMsg(java.lang.String nextReturnMsg){
		this.nextReturnMsg = nextReturnMsg;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  失败原因
	 */
	@Column(name ="FAIL_REASON",nullable=true,length=30)
	public String getFailReason() {
		return failReason;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  失败原因
	 */
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	@Column(name ="VERIFY_CODE",nullable=true,length=3000)
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	@Column(name ="TIME_STAMP")
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Column(name ="RESEND_SIGN")
	public String getResendSign() {
		return resendSign;
	}
	public void setResendSign(String resendSign) {
		this.resendSign = resendSign;
	}
	@Column(name ="MID_PAYER_ACC_NO")
	public String getMidPayerAccNo() {
		return midPayerAccNo;
	}
	public void setMidPayerAccNo(String midPayerAccNo) {
		this.midPayerAccNo = midPayerAccNo;
	}
	@Column(name ="MID_PAYER_ACC_NAME")
	public String getMidPayerAccName() {
		return midPayerAccName;
	}
	public void setMidPayerAccName(String midPayerAccName) {
		this.midPayerAccName = midPayerAccName;
	}
	@Column(name ="MID_PAYER_BANK_NAME")
	public String getMidPayerBankName() {
		return midPayerBankName;
	}
	public void setMidPayerBankName(String midPayerBankName) {
		this.midPayerBankName = midPayerBankName;
	}
	@Column(name ="DELAY_TIME")
	public Integer getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}
}
