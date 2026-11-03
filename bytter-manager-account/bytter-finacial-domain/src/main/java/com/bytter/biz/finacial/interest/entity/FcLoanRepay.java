package com.bytter.biz.finacial.interest.entity;
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
 * @Description: FC_LOAN_REPAY
 * @author onlineGenerator
 * @date 2017-08-31 10:08:43
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_LOAN_REPAY", schema = "")
@SuppressWarnings("serial")
public class FcLoanRepay extends IdEntity implements java.io.Serializable {
	/**所属网点*/
	private java.lang.String netId;
	/**单位代码*/
	private java.lang.String corpId;
	/**币别代码*/
	private java.lang.String curCode;
	/**贷款帐号*/
	private java.lang.String loanAcc;
	/**归还账号*/
	private java.lang.String detailAcc;
	/**业务类型*/
	private java.lang.String busType;
	/**借方利息账号*/
	private java.lang.String needInterestAcc;
	/**还款账号*/
	private java.lang.String repayAcc;
	/**归还类型*/
	private java.lang.String repayType;
	/**还款金额*/
	private java.math.BigDecimal repayMoney;
	/**利息*/
	private java.math.BigDecimal interest;
	/**录入日期*/
	private java.util.Date enterDate;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**状态*/
	private java.lang.Integer status;
	/**操作人*/
	private java.lang.String operatorCode;
	/**复核人*/
	private java.lang.String checkerCode;
	/**复核日期*/
	private java.util.Date checkTime;
	/**打回原因*/
	private java.lang.String refuseWhy;
	/**归还本金记账流水号*/
	private java.lang.String orderNo;
	/**利息记账流水号*/
	private java.lang.String interestOrderNo;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
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
	 *@return: java.lang.String  单位代码
	 */
	@Column(name ="CORP_ID")
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位代码
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_CODE")
	public java.lang.String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurCode(java.lang.String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷款帐号
	 */
	@Column(name ="LOAN_ACC")
	public java.lang.String getLoanAcc(){
		return this.loanAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷款帐号
	 */
	public void setLoanAcc(java.lang.String loanAcc){
		this.loanAcc = loanAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  归还账号
	 */
	@Column(name ="DETAIL_ACC")
	public java.lang.String getDetailAcc(){
		return this.detailAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  归还账号
	 */
	public void setDetailAcc(java.lang.String detailAcc){
		this.detailAcc = detailAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型
	 */
	@Column(name ="BUS_TYPE")
	public java.lang.String getBusType(){
		return this.busType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型
	 */
	public void setBusType(java.lang.String busType){
		this.busType = busType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  应收利息账号
	 */
	@Column(name ="NEED_INTEREST_ACC")
	public java.lang.String getNeedInterestAcc(){
		return this.needInterestAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借方利息账号
	 */
	public void setNeedInterestAcc(java.lang.String needInterestAcc){
		this.needInterestAcc = needInterestAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  还款账号
	 */
	@Column(name ="REPAY_ACC")
	public java.lang.String getRepayAcc(){
		return this.repayAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  还款账号
	 */
	public void setRepayAcc(java.lang.String repayAcc){
		this.repayAcc = repayAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  归还类型
	 */
	@Column(name ="REPAY_TYPE")
	public java.lang.String getRepayType(){
		return this.repayType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  归还类型
	 */
	public void setRepayType(java.lang.String repayType){
		this.repayType = repayType;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  还款金额
	 */
	@Column(name ="REPAY_MONEY")
	public java.math.BigDecimal getRepayMoney(){
		return this.repayMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  还款金额
	 */
	public void setRepayMoney(java.math.BigDecimal repayMoney){
		this.repayMoney = repayMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利息
	 */
	@Column(name ="INTEREST")
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  录入日期
	 */
	@Column(name ="ENTER_DATE")
	public java.util.Date getEnterDate(){
		return this.enterDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  录入日期
	 */
	public void setEnterDate(java.util.Date enterDate){
		this.enterDate = enterDate;
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS")
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
	 *@return: java.lang.String  操作人
	 */
	@Column(name ="OPERATOR_CODE")
	public java.lang.String getOperatorCode(){
		return this.operatorCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作人
	 */
	public void setOperatorCode(java.lang.String operatorCode){
		this.operatorCode = operatorCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复核人
	 */
	@Column(name ="CHECKER_CODE")
	public java.lang.String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核人
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打回原因
	 */
	@Column(name ="REFUSE_WHY")
	public java.lang.String getRefuseWhy(){
		return this.refuseWhy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打回原因
	 */
	public void setRefuseWhy(java.lang.String refuseWhy){
		this.refuseWhy = refuseWhy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  归还本金记账流水号
	 */
	@Column(name ="ORDER_NO")
	public java.lang.String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  归还本金记账流水号
	 */
	public void setOrderNo(java.lang.String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  利息记账流水号
	 */
	@Column(name ="INTEREST_ORDER_NO")
	public java.lang.String getInterestOrderNo(){
		return this.interestOrderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  利息记账流水号
	 */
	public void setInterestOrderNo(java.lang.String interestOrderNo){
		this.interestOrderNo = interestOrderNo;
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
}
