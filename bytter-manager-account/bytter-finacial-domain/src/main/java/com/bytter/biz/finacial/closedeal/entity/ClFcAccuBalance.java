package com.bytter.biz.finacial.closedeal.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_ACCU_BALANCE
 * @author onlineGenerator
 * @date 2017-06-07 10:03:48
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_ACCU_BALANCE", schema = "")
@SuppressWarnings("serial")
public class ClFcAccuBalance extends IdEntity implements java.io.Serializable {
	/**积数日期*/
	private java.util.Date balanceDate;
	/**账号*/
	private java.lang.String account;
	/**币别代码*/
	private java.lang.String curCode;
	/**当天积数*/
	private java.math.BigDecimal cumu;
	/**当天调整积数*/
	private java.math.BigDecimal adjustCumu;
	/**协定存款标志*/
	private java.lang.String agreedDepositSign;
	/**计息标志*/
	private java.lang.String interestSign;
	/**预提标志*/
	private java.lang.String preSign;
	/**计息日期*/
	private java.util.Date interestTime;
	/**计息日期*/
	private java.math.BigDecimal aAdjusted;
	/**协定存款登记ID*/
	private java.lang.String agreedDepositId;
	/**积数类型*/
	private java.lang.Integer agreedType;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  积数日期
	 */
	@Column(name ="BALANCE_DATE")
	public java.util.Date getBalanceDate(){
		return this.balanceDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  积数日期
	 */
	public void setBalanceDate(java.util.Date balanceDate){
		this.balanceDate = balanceDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="ACCOUNT")
	public java.lang.String getAccount(){
		return this.account;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号
	 */
	public void setAccount(java.lang.String account){
		this.account = account;
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  当天积数
	 */
	@Column(name ="CUMU")
	public java.math.BigDecimal getCumu(){
		return this.cumu;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  当天积数
	 */
	public void setCumu(java.math.BigDecimal cumu){
		this.cumu = cumu;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  当天调整积数
	 */
	@Column(name ="ADJUST_CUMU")
	public java.math.BigDecimal getAdjustCumu(){
		return this.adjustCumu;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  当天调整积数
	 */
	public void setAdjustCumu(java.math.BigDecimal adjustCumu){
		this.adjustCumu = adjustCumu;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  协定存款标志
	 */
	@Column(name ="AGREED_DEPOSIT_SIGN")
	public java.lang.String getAgreedDepositSign(){
		return this.agreedDepositSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  协定存款标志
	 */
	public void setAgreedDepositSign(java.lang.String agreedDepositSign){
		this.agreedDepositSign = agreedDepositSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计息标志
	 */
	@Column(name ="INTEREST_SIGN")
	public java.lang.String getInterestSign(){
		return this.interestSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计息标志
	 */
	public void setInterestSign(java.lang.String interestSign){
		this.interestSign = interestSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  预提标志
	 */
	@Column(name ="PRE_SIGN")
	public java.lang.String getPreSign(){
		return this.preSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  预提标志
	 */
	public void setPreSign(java.lang.String preSign){
		this.preSign = preSign;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  计息日期
	 */
	@Column(name ="INTEREST_TIME")
	public java.util.Date getInterestTime(){
		return this.interestTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  计息日期
	 */
	public void setInterestTime(java.util.Date interestTime){
		this.interestTime = interestTime;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  计息日期
	 */
	@Column(name ="A_ADJUSTED")
	public java.math.BigDecimal getAAdjusted(){
		return this.aAdjusted;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  计息日期
	 */
	public void setAAdjusted(java.math.BigDecimal aAdjusted){
		this.aAdjusted = aAdjusted;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  协定存款登记ID
	 */
	@Column(name ="AGREED_DEPOSIT_ID")
	public java.lang.String getAgreedDepositId(){
		return this.agreedDepositId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  协定存款登记ID
	 */
	public void setAgreedDepositId(java.lang.String agreedDepositId){
		this.agreedDepositId = agreedDepositId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  积数类型
	 */
	@Column(name ="AGREED_TYPE")
	public java.lang.Integer getAgreedType(){
		return this.agreedType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  积数类型
	 */
	public void setAgreedType(java.lang.Integer agreedType){
		this.agreedType = agreedType;
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
