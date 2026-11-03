package com.bytter.biz.finacial.outaccbook.entity;
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
 * @Description: 表外流水账主档表
 * @author onlineGenerator
 * @date 2017-07-05 15:40:08
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_OUT_ACCBOOK", schema = "")
@SuppressWarnings("serial")
public class FcOutAccbook extends IdEntity implements java.io.Serializable {
	/**所属网点*/
	private java.lang.String netId;
	/**流水号*/
	private java.lang.String orderNo;
	/**凭证号*/
	private java.lang.String voucherNo;
	/**录入日期*/
	private java.util.Date enrolTime;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**记账员代码*/
	private java.lang.String operatorCode;
	/**结束日期*/
	private java.util.Date endDate;
	/**合同号码*/
	private java.lang.String contractNo;
	/**复核员代码*/
	private java.lang.String checkerCode;
	/**复核日期*/
	private java.util.Date checkTime;
	/**凭证状态*/
	private java.lang.Integer status;
	/**票据号码*/
	private java.lang.String billsNo;
	/**红蓝标志*/
	private java.lang.String redblueSign;
	/**冲账标志*/
	private java.lang.String rushSign;
	/**关联流水号*/
	private java.lang.String relAccbookId;
	/**业务来源类型*/
	private java.lang.Integer busOriType;
	/**打回原因*/
	private java.lang.String backReason;
	/**冲账原因*/
	private java.lang.String rushReason;
	/**作废打回原因*/
	private java.lang.String invalidBackReason;
	/**作废原因*/
	private java.lang.String invalidReason;
	/**作废申请人代码*/
	private java.lang.String invalidOpCode;
	/**作废申请日期*/
	private java.util.Date invalidOpTime;
	/**作废复核人代码*/
	private java.lang.String invalidChecker;
	/**作废复核日期*/
	private java.util.Date invalidCheckDate;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**结转标识*/
	private String overSign;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属网点
	 */
	@Column(name ="NET_ID")
	public java.lang.String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属网点
	 */
	public void setNetId(java.lang.String netId){
		this.netId = netId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流水号
	 */
	@Column(name ="ORDER_NO")
	public java.lang.String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流水号
	 */
	public void setOrderNo(java.lang.String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证号
	 */
	@Column(name ="VOUCHER_NO")
	public java.lang.String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证号
	 */
	public void setVoucherNo(java.lang.String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  录入日期
	 */
	@Column(name ="ENROL_TIME")
	public java.util.Date getEnrolTime(){
		return this.enrolTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  录入日期
	 */
	public void setEnrolTime(java.util.Date enrolTime){
		this.enrolTime = enrolTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记账日期
	 */
	@Column(name ="TALLY_DATE")
	public java.util.Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  记账日期
	 */
	public void setTallyDate(java.util.Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账员代码
	 */
	@Column(name ="OPERATOR_CODE")
	public java.lang.String getOperatorCode(){
		return this.operatorCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账员代码
	 */
	public void setOperatorCode(java.lang.String operatorCode){
		this.operatorCode = operatorCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结束日期
	 */
	@Column(name ="END_DATE")
	public java.util.Date getEndDate(){
		return this.endDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结束日期
	 */
	public void setEndDate(java.util.Date endDate){
		this.endDate = endDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合同号码
	 */
	@Column(name ="CONTRACT_NO")
	public java.lang.String getContractNo(){
		return this.contractNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合同号码
	 */
	public void setContractNo(java.lang.String contractNo){
		this.contractNo = contractNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复核员代码
	 */
	@Column(name ="CHECKER_CODE")
	public java.lang.String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核员代码
	 */
	public void setCheckerCode(java.lang.String checkerCode){
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复核日期
	 */
	@Column(name ="CHECK_TIME")
	public java.util.Date getCheckTime(){
		return this.checkTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  复核日期
	 */
	public void setCheckTime(java.util.Date checkTime){
		this.checkTime = checkTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  凭证状态
	 */
	@Column(name ="STATUS")
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  凭证状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据号码
	 */
	@Column(name ="BILLS_NO")
	public java.lang.String getBillsNo(){
		return this.billsNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据号码
	 */
	public void setBillsNo(java.lang.String billsNo){
		this.billsNo = billsNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  红蓝标志
	 */
	@Column(name ="REDBLUE_SIGN")
	public java.lang.String getRedblueSign(){
		return this.redblueSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  红蓝标志
	 */
	public void setRedblueSign(java.lang.String redblueSign){
		this.redblueSign = redblueSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  冲账标志
	 */
	@Column(name ="RUSH_SIGN")
	public java.lang.String getRushSign(){
		return this.rushSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  冲账标志
	 */
	public void setRushSign(java.lang.String rushSign){
		this.rushSign = rushSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  关联流水号
	 */
	@Column(name ="REL_ACCBOOK_ID")
	public java.lang.String getRelAccbookId(){
		return this.relAccbookId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  关联流水号
	 */
	public void setRelAccbookId(java.lang.String relAccbookId){
		this.relAccbookId = relAccbookId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  业务来源类型
	 */
	@Column(name ="BUS_ORI_TYPE")
	public java.lang.Integer getBusOriType(){
		return this.busOriType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务来源类型
	 */
	public void setBusOriType(java.lang.Integer busOriType){
		this.busOriType = busOriType;
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
	@Column(name ="RUSH_REASON")
	public java.lang.String getRushReason(){
		return this.rushReason;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  冲账原因
	 */
	public void setRushReason(java.lang.String rushReason){
		this.rushReason = rushReason;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  作废打回原因
	 */
	@Column(name ="INVALID_BACK_REASON")
	public java.lang.String getInvalidBackReason(){
		return this.invalidBackReason;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  作废打回原因
	 */
	public void setInvalidBackReason(java.lang.String invalidBackReason){
		this.invalidBackReason = invalidBackReason;
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
	 *@return: java.lang.String  作废复核人代码
	 */
	@Column(name ="INVALID_CHECKER")
	public java.lang.String getInvalidChecker(){
		return this.invalidChecker;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  作废复核人代码
	 */
	public void setInvalidChecker(java.lang.String invalidChecker){
		this.invalidChecker = invalidChecker;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  作废复核日期
	 */
	@Column(name ="INVALID_CHECK_DATE")
	public java.util.Date getInvalidCheckDate(){
		return this.invalidCheckDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  作废复核日期
	 */
	public void setInvalidCheckDate(java.util.Date invalidCheckDate){
		this.invalidCheckDate = invalidCheckDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME")
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户代码
	 */
	@Column(name ="CREATE_BY")
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后更新时间
	 */
	@Column(name ="UPDATE_TIME")
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后更新用户代码
	 */
	@Column(name ="UPDATE_BY")
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	@Column(name ="OVER_SIGN")
	public String getOverSign() {
		return overSign;
	}
	public void setOverSign(String overSign) {
		this.overSign = overSign;
	}
	
	
}
