package com.bytter.biz.finacial.interest.entity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 预提明细临时表
 * @author onlineGenerator
 * @date 2017-07-06 20:17:59
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_PREDRAW_DTL_TEMP", schema = "")
@SuppressWarnings("serial")
public class FcPredrawDtlTemp extends IdEntity implements java.io.Serializable {
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**计息日期*/
	private java.util.Date interestTime;
	/**流水号*/
	private java.lang.String orderNo;
	/**账号*/
	private java.lang.String account;
	/**币别代码*/
	private java.lang.String curCode;
	/**利息*/
	private java.math.BigDecimal interest;
	/**余额*/
	private java.math.BigDecimal balance;
	/**冲账标志*/
	private java.lang.String strikeSign;
	/**冲账日期*/
	private java.util.Date strikeDate;
	/**冲账利息*/
	private java.math.BigDecimal strikeInterest;
	/**结息标志*/
	private java.lang.String sign;
	
	List<FcInterestDtlTemp> interestDtls;
	
	@OneToMany// 指定一对多关系
	@Cascade(value={CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="PREDRAW_DTL_ID")
	public List<FcInterestDtlTemp> getInterestDtls() {
		return interestDtls;
	}
	public void setInterestDtls(List<FcInterestDtlTemp> interestDtls) {
		this.interestDtls = interestDtls;
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  余额
	 */
	@Column(name ="BALANCE")
	public java.math.BigDecimal getBalance(){
		return this.balance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  余额
	 */
	public void setBalance(java.math.BigDecimal balance){
		this.balance = balance;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  冲账标志
	 */
	@Column(name ="STRIKE_SIGN")
	public java.lang.String getStrikeSign(){
		return this.strikeSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  冲账标志
	 */
	public void setStrikeSign(java.lang.String strikeSign){
		this.strikeSign = strikeSign;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  冲账日期
	 */
	@Column(name ="STRIKE_DATE")
	public java.util.Date getStrikeDate(){
		return this.strikeDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  冲账日期
	 */
	public void setStrikeDate(java.util.Date strikeDate){
		this.strikeDate = strikeDate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  冲账利息
	 */
	@Column(name ="STRIKE_INTEREST")
	public java.math.BigDecimal getStrikeInterest(){
		return this.strikeInterest;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  冲账利息
	 */
	public void setStrikeInterest(java.math.BigDecimal strikeInterest){
		this.strikeInterest = strikeInterest;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结息标志
	 */
	@Column(name ="SIGN")
	public java.lang.String getSign(){
		return this.sign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结息标志
	 */
	public void setSign(java.lang.String sign){
		this.sign = sign;
	}
}
