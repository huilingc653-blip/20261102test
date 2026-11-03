package com.bytter.increment.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: CHO_BANK_FINANCE
 * @author onlineGenerator
 * @date 2017-08-18 14:03:30
 * @version 1.0
 *
 */
@Entity
@Table(name = "CHO_BANK_FINANCE", schema = "")
@SuppressWarnings("serial")
public class ChoBankFinance extends IdEntity implements java.io.Serializable {
	/**业务单号*/
	private String billNo;
	/**单位ID*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知*/
	private Integer busType;
	/**付款账号ID*/
	private String payerAccId;
	/**付款账号*/
	private String payerAcc;
	/**付款账号名称*/
	private String accName;
	/**付款账号名称*/
	private String bankName;
	/**账号上线*/
	private String bankAccOnline;
	/**付款账户余额*/
	private java.math.BigDecimal balance;
	/**收款账号ID*/
	private String payeeAccId;
	/**收款账号*/
	private String payeeAcc;
	/**币别*/
	private String curId;
	/**币种名称*/
	private String curName;
	/**金额*/
	private java.math.BigDecimal payMoney;
	/**期限（活期转定期）：3个月、6个月、1年、2年、3年、5年*/
	private Integer dayLimit;
	/**日期单位 Y-年、M-月、D-日*/
	private String dateUnit;
	/**会计凭证号*/
	private String bursarBillNo;
	/**利息*/
	private java.math.BigDecimal interest;
	/**到期处理方式0-不处理 1-本金续存 2-本息续存*/
	private Integer certificateWay;
	/**期望支付日期*/
	private Date wishPayday;
	/**利率*/
	private java.math.BigDecimal interestRate;
	/**用途*/
	private String purpose;
	/**附言*/
	private String addWord;
	/**取款日期*/
	private Date payDate;
	/**取款单据ID（定期转活期，通知转活期）*/
	private String payBillId;
	/**通知日期*/
	private Date notifyTime;
	/**补录利率*/
	private java.math.BigDecimal blInterestRate;
	/**是否补录 0-否 1-是*/
	private String isBl;
	/**补录单号*/
	private String bankBillNo;
	/**补录利息*/
	private java.math.BigDecimal blInterest;
	/**状态*/
	private Integer status;
	/**起息日期*/
	private Date interestStartDate;
	/**到期日期*/
	private Date deadlineDate;
	/**创建人登录名称*/
	private String createBy;
	/**创建人登录用户名*/
	private String username;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**银行流水认领组件挂接确认*/
	private String collectionConfirm;
	/**是否上线*/
	private String isOnline;
	/**指令ID*/
	private String excId;
	/**交易状态*/
	private String voucherStat;
	/**定期账号余额*/
	private BigDecimal timeBalance;
	@Column(name = "CORP_NAME", length = 200)
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	@Column(name = "acc_Name", length = 140)
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Column(name = "bank_Name", length = 140)
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	@Column(name = "bank_Acc_Online", length = 1)
	public String getBankAccOnline() {
		return bankAccOnline;
	}
	public void setBankAccOnline(String bankAccOnline) {
		this.bankAccOnline = bankAccOnline;
	}
	@Column(name = "CUR_NAME", length = 30)
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	@Column(name = "USERNAME", length = 30)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name = "exc_Id", length = 32)
	public String getExcId() {
		return excId;
	}
	public void setExcId(String excId) {
		this.excId = excId;
	}
	@Column(name = "voucher_Stat", length = 1)
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	@Column(name = "TIME_BALANCE", scale = 2, length = 15)
	public BigDecimal getTimeBalance() {
		return timeBalance;
	}
	public void setTimeBalance(BigDecimal timeBalance) {
		this.timeBalance = timeBalance;
	}
	@Column(name ="COLLECTION_CONFIRM",nullable=false,length=1)
	public String getCollectionConfirm() {
		return collectionConfirm;
	}
	public void setCollectionConfirm(String collectionConfirm) {
		this.collectionConfirm = collectionConfirm;
	}
	/**
	 *方法: 取得String
	 *@return: String  业务单号
	 */
	@Column(name ="BILL_NO",nullable=false,length=12)
	public String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置String
	 *@param: String  业务单号
	 */
	public void setBillNo(String billNo){
		this.billNo = billNo;
	}
	/**
	 *方法: 取得String
	 *@return: String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置String
	 *@param: String  单位ID
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得Integer
	 *@return: Integer  业务类型
10-活期转定期
11-定期转活期
12-活期转通知
13-通知转活期
14-通知预约
15-取消通知
	 */
	@Column(name ="BUS_TYPE",nullable=false,length=4)
	public Integer getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置Integer
	 *@param: Integer  业务类型
10-活期转定期
11-定期转活期
12-活期转通知
13-通知转活期
14-通知预约
15-取消通知
	 */
	public void setBusType(Integer busType){
		this.busType = busType;
	}
	/**
	 *方法: 取得String
	 *@return: String  付款账号ID
	 */
	@Column(name ="PAYER_ACC_ID",nullable=true,length=32)
	public String getPayerAccId(){
		return this.payerAccId;
	}
	/**
	 *方法: 设置String
	 *@param: String  付款账号ID
	 */
	public void setPayerAccId(String payerAccId){
		this.payerAccId = payerAccId;
	}
	/**
	 *方法: 取得String
	 *@return: String  付款账号
	 */
	@Column(name ="PAYER_ACC",nullable=true,length=30)
	public String getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置String
	 *@param: String  付款账号
	 */
	public void setPayerAcc(String payerAcc){
		this.payerAcc = payerAcc;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  付款账户余额
	 */
	@Column(name ="BALANCE",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBalance(){
		return this.balance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  付款账户余额
	 */
	public void setBalance(java.math.BigDecimal balance){
		this.balance = balance;
	}
	/**
	 *方法: 取得String
	 *@return: String  收款账号ID
	 */
	@Column(name ="PAYEE_ACC_ID",nullable=true,length=32)
	public String getPayeeAccId(){
		return this.payeeAccId;
	}
	/**
	 *方法: 设置String
	 *@param: String  收款账号ID
	 */
	public void setPayeeAccId(String payeeAccId){
		this.payeeAccId = payeeAccId;
	}
	/**
	 *方法: 取得String
	 *@return: String  收款账号
	 */
	@Column(name ="PAYEE_ACC",nullable=true,length=40)
	public String getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置String
	 *@param: String  收款账号
	 */
	public void setPayeeAcc(String payeeAcc){
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得String
	 *@return: String  币别
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
	public String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置String
	 *@param: String  币别
	 */
	public void setCurId(String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="PAY_MONEY",nullable=true,scale=2,length=15)
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
	 *方法: 取得Integer
	 *@return: Integer  期限（活期转定期）：3个月、6个月、1年、2年、3年、5年
	 */
	@Column(name ="DAY_LIMIT",nullable=true,length=4)
	public Integer getDayLimit(){
		return this.dayLimit;
	}
	/**
	 *方法: 设置Integer
	 *@param: Integer  期限（活期转定期）：3个月、6个月、1年、2年、3年、5年
	 */
	public void setDayLimit(Integer dayLimit){
		this.dayLimit = dayLimit;
	}
	/**
	 *方法: 取得String
	 *@return: String  日期单位 Y-年、M-月、D-日
	 */
	@Column(name ="DATE_UNIT",nullable=true,length=2)
	public String getDateUnit(){
		return this.dateUnit;
	}
	/**
	 *方法: 设置String
	 *@param: String  日期单位 Y-年、M-月、D-日
	 */
	public void setDateUnit(String dateUnit){
		this.dateUnit = dateUnit;
	}
	/**
	 *方法: 取得String
	 *@return: String  会计凭证号
	 */
	@Column(name ="BURSAR_BILL_NO",nullable=true,length=40)
	public String getBursarBillNo(){
		return this.bursarBillNo;
	}
	/**
	 *方法: 设置String
	 *@param: String  会计凭证号
	 */
	public void setBursarBillNo(String bursarBillNo){
		this.bursarBillNo = bursarBillNo;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利息
	 */
	@Column(name ="INTEREST",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getInterest(){
		return this.interest;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利息
	 */
	public void setInterest(java.math.BigDecimal interest){
		this.interest = interest;
	}
	/**
	 *方法: 取得Integer
	 *@return: Integer  到期处理方式
0-不处理 1-本金续存 2-本息续存
	 */
	@Column(name ="CERTIFICATE_WAY",nullable=true,length=2)
	public Integer getCertificateWay(){
		return this.certificateWay;
	}
	/**
	 *方法: 设置Integer
	 *@param: Integer  到期处理方式
0-不处理 1-本金续存 2-本息续存
	 */
	public void setCertificateWay(Integer certificateWay){
		this.certificateWay = certificateWay;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  期望支付日期
	 */
	@Column(name ="WISH_PAYDAY",nullable=true)
	public Date getWishPayday(){
		return this.wishPayday;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  期望支付日期
	 */
	public void setWishPayday(Date wishPayday){
		this.wishPayday = wishPayday;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利率
	 */
	@Column(name ="INTEREST_RATE",nullable=true,scale=6,length=8)
	public java.math.BigDecimal getInterestRate(){
		return this.interestRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利率
	 */
	public void setInterestRate(java.math.BigDecimal interestRate){
		this.interestRate = interestRate;
	}
	/**
	 *方法: 取得String
	 *@return: String  用途
	 */
	@Column(name ="PURPOSE",nullable=true,length=60)
	public String getPurpose(){
		return this.purpose;
	}
	/**
	 *方法: 设置String
	 *@param: String  用途
	 */
	public void setPurpose(String purpose){
		this.purpose = purpose;
	}
	/**
	 *方法: 取得String
	 *@return: String  附言
	 */
	@Column(name ="ADD_WORD",nullable=true,length=200)
	public String getAddWord(){
		return this.addWord;
	}
	/**
	 *方法: 设置String
	 *@param: String  附言
	 */
	public void setAddWord(String addWord){
		this.addWord = addWord;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  取款日期
	 */
	@Column(name ="PAY_DATE",nullable=true)
	public Date getPayDate(){
		return this.payDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  取款日期
	 */
	public void setPayDate(Date payDate){
		this.payDate = payDate;
	}
	/**
	 *方法: 取得java.util.String
	 *@return: java.util.String  取款单据ID（定期转活期，通知转活期）
	 */
	@Column(name ="PAY_BILL_ID",nullable=true)
	public String getPayBillId() {
		return payBillId;
	}
	/**
	 *方法: 设置java.util.String
	 *@param: java.util.String  取款单据ID（定期转活期，通知转活期）
	 */
	public void setPayBillId(String payBillId) {
		this.payBillId = payBillId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  通知日期
	 */
	@Column(name ="NOTIFY_TIME",nullable=true)
	public Date getNotifyTime(){
		return this.notifyTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  通知日期
	 */
	public void setNotifyTime(Date notifyTime){
		this.notifyTime = notifyTime;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  补录利率
	 */
	@Column(name ="BL_INTEREST_RATE",nullable=true,scale=6,length=8)
	public java.math.BigDecimal getBlInterestRate(){
		return this.blInterestRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  补录利率
	 */
	public void setBlInterestRate(java.math.BigDecimal blInterestRate){
		this.blInterestRate = blInterestRate;
	}
	/**
	 *方法: 取得String
	 *@return: String  是否补录 0-否 1-是
	 */
	@Column(name ="IS_BL",nullable=true,length=1)
	public String getIsBl(){
		return this.isBl;
	}
	/**
	 *方法: 设置String
	 *@param: String  是否补录 0-否 1-是
	 */
	public void setIsBl(String isBl){
		this.isBl = isBl;
	}
	/**
	 *方法: 取得String
	 *@return: String  补录单号
	 */
	@Column(name ="BANK_BILL_NO",nullable=true,length=20)
	public String getBankBillNo(){
		return this.bankBillNo;
	}
	/**
	 *方法: 设置String
	 *@param: String  补录单号
	 */
	public void setBankBillNo(String bankBillNo){
		this.bankBillNo = bankBillNo;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  补录利息
	 */
	@Column(name ="BL_INTEREST",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBlInterest(){
		return this.blInterest;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  补录利息
	 */
	public void setBlInterest(java.math.BigDecimal blInterest){
		this.blInterest = blInterest;
	}
	/**
	 *方法: 取得Integer
	 *@return: Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置Integer
	 *@param: Integer  状态
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  起息日期
	 */
	@Column(name ="INTEREST_START_DATE",nullable=true)
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  到期日期
	 */
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  到期日期
	 */
	@Column(name ="DEADLINE_DATE",nullable=true)
	public Date getDeadlineDate() {
		return deadlineDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  到期日期
	 */
	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置String
	 *@param: String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置String
	 *@param: String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 * 方法: 取得String
	 *
	 * @return: String  是否上线
	 */
	@Column(name = "IS_ONLINE", nullable = true, length = 1)
	public String getIsOnline() {
		return this.isOnline;
	}
	/**
	 * 方法: 设置String
	 *
	 * @param: String  是否上线
	 */
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
}
