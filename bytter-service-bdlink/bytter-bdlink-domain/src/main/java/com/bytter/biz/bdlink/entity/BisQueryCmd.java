package com.bytter.biz.bdlink.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: BIS_QUERY_CMD
 * @author onlineGenerator
 * @date 2017-12-28 16:31:56
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_QUERY_CMD", schema = "")
@SuppressWarnings("serial")
public class BisQueryCmd extends IdEntity implements java.io.Serializable {
	/**指令编号*/
	private java.lang.String voucherNo;
	/**交易类型*/
	private java.lang.String transId;
	/**发送日期*/
	private java.util.Date sendTime;
	/**账号ID*/
	private java.lang.String bankAccId;
	/**账号*/
	private java.lang.String bankAcc;
	/**账户名称*/
	private java.lang.String accName;
	/**开户行*/
	private java.lang.String bankName;
	/**币别*/
	private java.lang.String curId;
	/**开始日期*/
	private java.util.Date startTime;
	/**结束日期*/
	private java.util.Date endTime;
	/**接口代码*/
	private java.lang.String bifCode;
	/**指令类型*/
	private java.lang.String cmdType;
	/**指令状态*/
	private java.lang.String cmdStat;
	/**备注*/
	private java.lang.String rmk;
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

	private String corpName;

	@Column(name ="CORP_NAME",nullable=true)
	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	private java.lang.String corpId;

	@Column(name ="CORP_ID",nullable=true)
	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

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
	 *@return: java.lang.String  交易类型
	 */
	@Column(name ="TRANS_ID",nullable=true,length=6)
	public java.lang.String getTransId() {
		return transId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单ID
	 */
	public void setTransId(java.lang.String transId) {
		this.transId = transId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发送日期
	 */
	@Column(name ="SEND_TIME",nullable=true)
	public java.util.Date getSendTime() {
		return sendTime;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发送日期
	 */
	public void setSendTime(java.util.Date sendTime) {
		this.sendTime = sendTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号ID
	 */
	@Column(name ="BANK_ACC_ID",nullable=true,length=32)
	public java.lang.String getBankAccId() {
		return bankAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号ID
	 */
	public void setBankAccId(java.lang.String bankAccId) {
		this.bankAccId = bankAccId;
	}
	@Column(name ="ACC_NAME")
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Column(name ="BANK_NAME")
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="BANK_ACC",nullable=true,length=30)
	public java.lang.String getBankAcc() {
		return bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号
	 */
	public void setBankAcc(java.lang.String bankAcc) {
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@param: java.lang.String  开始日期
	 */
	@Column(name ="START_TIME",nullable=true)
	public java.util.Date getStartTime() {
		return startTime;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开始日期
	 */
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@param: java.lang.String  结束日期
	 */
	@Column(name ="END_TIME",nullable=true)
	public java.util.Date getEndTime() {
		return endTime;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结束日期
	 */
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
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
	 *@return: java.lang.String  交易状态
	 */
	@Column(name ="CMD_STAT",nullable=true,length=1)
	public java.lang.String getCmdStat(){
		return this.cmdStat;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  交易状态
	 */
	public void setCmdStat(java.lang.String cmdStat){
		this.cmdStat = cmdStat;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=100)
	public java.lang.String getRmk() {
		return rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk) {
		this.rmk = rmk;
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
}
