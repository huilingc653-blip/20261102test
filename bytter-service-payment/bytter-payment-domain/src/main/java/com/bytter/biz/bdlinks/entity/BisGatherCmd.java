package com.bytter.biz.bdlinks.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 收款指令表
 * @author onlineGenerator
 * @date 2017-07-04 15:57:19
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_GATHER_CMD", schema = "")
@SuppressWarnings("serial")
public class BisGatherCmd extends IdEntity implements java.io.Serializable {
	/**指令编号*/
	private java.lang.String voucherNo;
	/**单位Id*/
	private String corpId;
	/**接口代码*/
	private java.lang.String bifCode;
	/**调拨单ID*/
	private java.lang.String cbsBillId;
	/**调拨单号*/
	private java.lang.String cbsBillNo;
	/**子账号ID*/
	private java.lang.String childAccId;
	/**子账号*/
	private String childAcc;
	/**子账号账户名*/
	private String childAccName;
	/**子账号开户行*/
	private String childBank;
	/**子账号省*/
	private String childProv;
	/**子账号市*/
	private String childCity;
	/**父账号ID*/
	private java.lang.String parentAccId;
	/**父账号*/
	private String parentAcc;
	/**父账号账户名*/
	private String parentAccName;
	/**父账号开户行*/
	private String parentBank;
	/**父账号省*/
	private String parentProv;
	/**父账号市*/
	private String parentCity;
	/**金额*/
	private java.math.BigDecimal gatherAmt;
	/**调拨策略ID*/
	private java.lang.String gatherPolicyId;
	/**币别*/
	private java.lang.String curId;
	/**执行日期*/
	private java.util.Date runTime;
	/**加急标志 1-是 0-否*/
	private java.lang.String isQuick;
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
	/**业务类型*/
	private java.lang.Integer busOriType;
	/**指令类型*/
	private java.lang.String cmdType;
	/**消息提醒标志*/
	private java.lang.String noticeSign;
	/**银行流水ID*/
	private java.lang.String dtlId;
	/**银行流水号*/
	private java.lang.String bankSerialNo;
	/**交易状态
	 * 6=准备发送；0=交易成功
	 * */
	private java.lang.String cmdStat;
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
	/**加密信息*/
	private String verifyCode;
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
	@Column(name ="CORP_ID")
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
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
	 *@return: java.lang.String  调拨单ID
	 */
	@Column(name ="CBS_BILL_ID",nullable=true,length=32)
	public java.lang.String getCbsBillId(){
		return this.cbsBillId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  调拨单ID
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
	 *@return: java.lang.String  子账号
	 */
	@Column(name ="CHILD_ACC_ID",nullable=false,length=30)
	public java.lang.String getChildAccId(){
		return this.childAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  子账号
	 */
	public void setChildAccId(java.lang.String childAccId){
		this.childAccId = childAccId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  父账号
	 */
	@Column(name ="PARENT_ACC_ID",nullable=true,length=40)
	public java.lang.String getParentAccId(){
		return this.parentAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  父账号
	 */
	public void setParentAccId(java.lang.String parentAccId){
		this.parentAccId = parentAccId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="GATHER_AMT",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getGatherAmt(){
		return this.gatherAmt;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setGatherAmt(java.math.BigDecimal gatherAmt){
		this.gatherAmt = gatherAmt;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  调拨策略ID
	 */
	@Column(name ="GATHER_POLICY_ID",nullable=true,length=32)
	public java.lang.String getGatherPolicyId(){
		return this.gatherPolicyId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  调拨策略ID
	 */
	public void setGatherPolicyId(java.lang.String gatherPolicyId){
		this.gatherPolicyId = gatherPolicyId;
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  执行日期
	 */
	@Column(name ="RUN_TIME",nullable=true)
	public java.util.Date getRunTime(){
		return this.runTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  执行日期
	 */
	public void setRunTime(java.util.Date runTime){
		this.runTime = runTime;
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
	 *@return: java.lang.String  指令来源
	 */
	@Column(name ="VOUCHER_FROM",nullable=true,length=2)
	public java.lang.String getVoucherFrom(){
		return this.voucherFrom;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指令来源
	 */
	public void setVoucherFrom(java.lang.String voucherFrom){
		this.voucherFrom = voucherFrom;
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
	@Column(name ="CMD_TYPE",nullable=true,length=1)
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
	 *@return: java.lang.String  银行流水ID
	 */
	@Column(name ="DTL_ID",nullable=true,length=32)
	public java.lang.String getDtlId(){
		return this.dtlId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行流水ID
	 */
	public void setDtlId(java.lang.String dtlId){
		this.dtlId = dtlId;
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
	@Column(name ="VERIFY_CODE",nullable=true,length=3000)
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	@Column(name ="RESEND_SIGN")
	public String getResendSign() {
		return resendSign;
	}
	public void setResendSign(String resendSign) {
		this.resendSign = resendSign;
	}
	@Column(name ="CHILD_ACC")
	public String getChildAcc() {
		return childAcc;
	}
	public void setChildAcc(String childAcc) {
		this.childAcc = childAcc;
	}
	@Column(name ="CHILD_ACC_NAME")
	public String getChildAccName() {
		return childAccName;
	}
	public void setChildAccName(String childAccName) {
		this.childAccName = childAccName;
	}
	@Column(name ="CHILD_BANK")
	public String getChildBank() {
		return childBank;
	}
	public void setChildBank(String childBank) {
		this.childBank = childBank;
	}
	@Column(name ="CHILD_PROV")
	public String getChildProv() {
		return childProv;
	}
	public void setChildProv(String childProv) {
		this.childProv = childProv;
	}
	@Column(name ="CHILD_CITY")
	public String getChildCity() {
		return childCity;
	}
	public void setChildCity(String childCity) {
		this.childCity = childCity;
	}
	@Column(name ="PARENT_ACC")
	public String getParentAcc() {
		return parentAcc;
	}
	public void setParentAcc(String parentAcc) {
		this.parentAcc = parentAcc;
	}
	@Column(name ="PARENT_ACC_NAME")
	public String getParentAccName() {
		return parentAccName;
	}
	public void setParentAccName(String parentAccName) {
		this.parentAccName = parentAccName;
	}
	@Column(name ="PARENT_BANK")
	public String getParentBank() {
		return parentBank;
	}
	public void setParentBank(String parentBank) {
		this.parentBank = parentBank;
	}
	@Column(name ="PARENT_PROV")
	public String getParentProv() {
		return parentProv;
	}
	public void setParentProv(String parentProv) {
		this.parentProv = parentProv;
	}
	@Column(name ="PARENT_CITY")
	public String getParentCity() {
		return parentCity;
	}
	public void setParentCity(String parentCity) {
		this.parentCity = parentCity;
	}
}
