package com.bytter.increment.domain.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: BIS_FINANCE_EXC 理财指令表
 * @author onlineGenerator
 * @date 2017-08-23 11:08:59
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_FINANCE_EXC", schema = "")
@SuppressWarnings("serial")
public class BisFinanceExc extends IdEntity implements java.io.Serializable {
	/**指令编号*/
	private java.lang.String voucherNo;
	/**业务单ID*/
	private java.lang.String cbsBillId;
	/**业务单号*/
	private java.lang.String cbsBillNo;
	/**单位ID*/
	private java.lang.String corpId;
	/**申请日期*/
	private java.util.Date reqDate;
	/**接口代码*/
	private java.lang.String bifCode;
	/**付款账号*/
	private java.lang.String payerAcc;
	/**付款账号名称*/
	private java.lang.String payerAccName;
	/**付款开户行名称*/
	private java.lang.String payerBank;
	/**币别*/
	private java.lang.String curId;
	/**金额*/
	private java.math.BigDecimal payMoney;
	/**用途*/
	private java.lang.String purpose;
	/**期望支付日期*/
	private java.util.Date wishPayday;
	/**期限（活期转定期）：3个月、6个月、1年、2年、3年、5年*/
	private java.lang.Integer dayLimit;
	/**日期单位 Y-年、M-月、D-日*/
	private java.lang.String dateUnit;
	/**会计凭证号*/
	private java.lang.String bursarBillNo;
	/**到期处理方式 0-不处理 1-本金续存 2-本息续存*/
	private java.lang.Integer certificateWay;
	/**业务类型*/
	private java.lang.Integer busType;
	/**银行返回开户日期*/
	private java.util.Date openDate;
	/**银行返回收款账号*/
	private java.lang.String payeeAcc;
	/**银行返回收款账号名称*/
	private java.lang.String payeeAccName;
	/**活期账号余额*/
	private java.math.BigDecimal currBalance;
	/**定期账号余额*/
	private java.math.BigDecimal timeBalance;
	/**银行返回利率*/
	private java.math.BigDecimal interestRate;
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
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**指令重发标志*/
	private String resendSign;
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
	 *@return: java.lang.String  业务单ID
	 */
	@Column(name ="CBS_BILL_ID",nullable=true,length=32)
	public java.lang.String getCbsBillId(){
		return this.cbsBillId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务单ID
	 */
	public void setCbsBillId(java.lang.String cbsBillId){
		this.cbsBillId = cbsBillId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务单号
	 */
	@Column(name ="CBS_BILL_NO",nullable=true,length=20)
	public java.lang.String getCbsBillNo(){
		return this.cbsBillNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务单号
	 */
	public void setCbsBillNo(java.lang.String cbsBillNo){
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  申请日期
	 */
	@Column(name ="REQ_DATE",nullable=true)
	public java.util.Date getReqDate(){
		return this.reqDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  申请日期
	 */
	public void setReqDate(java.util.Date reqDate){
		this.reqDate = reqDate;
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
	 *@return: java.lang.String  付款账号
	 */
	@Column(name ="PAYER_ACC",nullable=false,length=30)
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  期限（活期转定期）：3个月、6个月、1年、2年、3年、5年
	 */
	@Column(name ="DAY_LIMIT",nullable=true,length=4)
	public java.lang.Integer getDayLimit(){
		return this.dayLimit;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  期限（活期转定期）：3个月、6个月、1年、2年、3年、5年
	 */
	public void setDayLimit(java.lang.Integer dayLimit){
		this.dayLimit = dayLimit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  日期单位 Y-年、M-月、D-日
	 */
	@Column(name ="DATE_UNIT",nullable=true,length=2)
	public java.lang.String getDateUnit(){
		return this.dateUnit;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  日期单位 Y-年、M-月、D-日
	 */
	public void setDateUnit(java.lang.String dateUnit){
		this.dateUnit = dateUnit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  会计凭证号
	 */
	@Column(name ="BURSAR_BILL_NO",nullable=true,length=40)
	public java.lang.String getBursarBillNo(){
		return this.bursarBillNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  会计凭证号
	 */
	public void setBursarBillNo(java.lang.String bursarBillNo){
		this.bursarBillNo = bursarBillNo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  到期处理方式 0-不处理 1-本金续存 2-本息续存
	 */
	@Column(name ="CERTIFICATE_WAY",nullable=true,length=2)
	public java.lang.Integer getCertificateWay(){
		return this.certificateWay;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  到期处理方式 0-不处理 1-本金续存 2-本息续存
	 */
	public void setCertificateWay(java.lang.Integer certificateWay){
		this.certificateWay = certificateWay;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  业务类型
	 */
	@Column(name ="BUS_TYPE",nullable=false,length=4)
	public java.lang.Integer getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务类型
	 */
	public void setBusType(java.lang.Integer busType){
		this.busType = busType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  银行返回开户日期
	 */
	@Column(name ="OPEN_DATE",nullable=true)
	public java.util.Date getOpenDate(){
		return this.openDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  银行返回开户日期
	 */
	public void setOpenDate(java.util.Date openDate){
		this.openDate = openDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行返回收款账号
	 */
	@Column(name ="PAYEE_ACC",nullable=true,length=40)
	public java.lang.String getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行返回收款账号
	 */
	public void setPayeeAcc(java.lang.String payeeAcc){
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行返回收款账号名称
	 */
	@Column(name ="PAYEE_ACC_NAME",nullable=true,length=150)
	public java.lang.String getPayeeAccName(){
		return this.payeeAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行返回收款账号名称
	 */
	public void setPayeeAccName(java.lang.String payeeAccName){
		this.payeeAccName = payeeAccName;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  活期账号余额
	 */
	@Column(name ="CURR_BALANCE",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getCurrBalance(){
		return this.currBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  活期账号余额
	 */
	public void setCurrBalance(java.math.BigDecimal currBalance){
		this.currBalance = currBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  定期账号余额
	 */
	@Column(name ="TIME_BALANCE",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getTimeBalance(){
		return this.timeBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  定期账号余额
	 */
	public void setTimeBalance(java.math.BigDecimal timeBalance){
		this.timeBalance = timeBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  银行返回利率
	 */
	@Column(name ="INTEREST_RATE",nullable=true,scale=6,length=8)
	public java.math.BigDecimal getInterestRate(){
		return this.interestRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  银行返回利率
	 */
	public void setInterestRate(java.math.BigDecimal interestRate){
		this.interestRate = interestRate;
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
	@Column(name ="RESEND_SIGN")
	public String getResendSign() {
		return resendSign;
	}
	public void setResendSign(String resendSign) {
		this.resendSign = resendSign;
	}
}
