package com.bytter.biz.ttpay.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 第三方支付实体类
 * @author chenf
 * @date 2022-5-9 15:43:05
 * @version 1.0
 *
 */
@Entity
@Table(name = "TP_PAYMENT", schema = "")
public class TpPayment extends IdEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**付款单号*/
	private java.lang.String billNo;
	/**业务标题*/
	private String busTitle;
	/**付款单位**/
	private String corpId;
	/**付款账号*/
	private java.lang.String payerAcc;
	/**付款账号户名*/
	private java.lang.String payerAccName;
	/**金额*/
	private java.math.BigDecimal payMoney;
	/**收款账号*/
	private java.lang.String payeeAcc;
	/**收款户名*/
	private java.lang.String payeeAccName;
	/**支付方式**/
	private String payType;
	/**期望支付日期*/
	private java.util.Date wishPayday;
	/**是否对私付款*/
	private java.lang.String isForindividual;
	/**业务类型*/
	private java.lang.String busType;
	/**状态*/
	private java.lang.Integer status;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建人用户名*/
	private java.lang.String createUserName;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**附件ID*/
	private java.lang.String attachmentId;
	/**备注说明*/
	private String rmk;
	/**商户订单号**/
	private String trandNo;
	/**币别ID*/
	private String curId;
	/**附言*/
	private java.lang.String addWord;
	/**时间戳*/
	private String timeStamp;
	/**收款方式 apple_id、手机号、邮箱**/
	private String payeeMode;
	/**付款用途**/
	private String purpose;
	/**银行交易订单号**/
	private String bankOrderNo;
	/**银行交易付款流水号**/
	private String bankPayNo;
	/**1待返回结果，2处理失败，0处理成功，3待发送**/
	private String voucherStat;
	/**交易时间**/
	private Date transTime;
	/**交易时间**/
	private Date returnTime;
	/**银行返回消息**/
	private String returnMsg;
	@Column(name ="BILL_NO",nullable=false,length=12)
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	@Column(name ="CORP_ID",nullable=false,length=32)
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name ="PAYER_ACC",nullable=true,length=50)
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	@Column(name ="PAY_MONEY",nullable=false,scale=2,length=15)
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	@Column(name ="PAYEE_ACC",nullable=true,length=50)
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	@Column(name ="PAYEE_ACC_NAME",nullable=true,length=150)
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	@Column(name ="PAY_TYPE",nullable=true,length=32)
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Column(name ="WISH_PAYDAY",nullable=true)
	public Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	@Column(name ="IS_FORINDIVIDUAL",nullable=true,length=1)
	public String getIsForindividual() {
		return isForindividual;
	}
	public void setIsForindividual(String isForindividual) {
		this.isForindividual = isForindividual;
	}
	@Column(name ="BUS_TYPE",nullable=true,length=32)
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	@Column(name ="STATUS",nullable=true,length=10)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人用户名
	 */
	@Column(name ="CREATE_USERNAME",nullable=true,length=50)
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
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
	 *@return: java.lang.String  附件ID
	 */
	@Column(name ="ATTACHMENT_ID",nullable=true,length=32)
	public java.lang.String getAttachmentId() {
		return attachmentId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附件ID
	 */
	public void setAttachmentId(java.lang.String attachmentId) {
		this.attachmentId = attachmentId;
	}
	@Column(name ="RMK",nullable=true,length=150)
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	@Column(name ="BUS_TITLE",nullable=true,length=32)
	public String getBusTitle() {
		return busTitle;
	}
	public void setBusTitle(String busTitle) {
		this.busTitle = busTitle;
	}
	@Column(name ="PAYER_ACC_NAME",nullable=true,length=150)
	public String getPayerAccName() {
		return payerAccName;
	}
	public void setPayerAccName(String payerAccName) {
		this.payerAccName = payerAccName;
	}
	@Column(name ="TRAND_NO",nullable=true,length=32)
	public String getTrandNo() {
		return trandNo;
	}
	public void setTrandNo(String trandNo) {
		this.trandNo = trandNo;
	}
	@Column(name ="CUR_ID",nullable=true,length=32)
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Column(name ="ADD_WORD",nullable=true,length=32)
	public String getAddWord() {
		return addWord;
	}
	public void setAddWord(String addWord) {
		this.addWord = addWord;
	}
	@Column(name ="TIME_STAMP")
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Column(name ="PAYEE_MODE",nullable=true,length=2)
	public String getPayeeMode() {
		return payeeMode;
	}
	public void setPayeeMode(String payeeMode) {
		this.payeeMode = payeeMode;
	}
	@Column(name ="PURPOSE",nullable=true,length=32)
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	@Column(name ="BANK_ORDER_NO",nullable=true,length=100)
	public String getBankOrderNo() {
		return bankOrderNo;
	}
	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}
	@Column(name ="BANK_PAY_NO",nullable=true,length=100)
	public String getBankPayNo() {
		return bankPayNo;
	}
	public void setBankPayNo(String bankPayNo) {
		this.bankPayNo = bankPayNo;
	}
	@Column(name ="VOUCHER_STAT",nullable=true,length=50)
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	@Column(name ="RETURN_MSG",nullable=true,length=50)
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	@Column(name ="TRANS_TIME",nullable=true)
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	@Column(name ="RETURN_TIME",nullable=true)
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
}
