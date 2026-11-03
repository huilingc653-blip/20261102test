package com.bytter.biz.finacial.interest.entity;

import com.bytter.core.base.entity.IdEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.List;
/**   
 * @Title: Entity
 * @Description: FC_PREDRAW_DTL
 * @author onlineGenerator
 * @date 2017-06-28 13:55:58
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_PREDRAW_DTL", schema = "")
@SuppressWarnings("serial")
public class FcPredrawDtl extends IdEntity implements java.io.Serializable {
	/**计息日期*/
	private java.util.Date interestTime;
	/**流水号*/
	private java.lang.String orderNo;
	/**账号*/
	private java.lang.String account;

	/**
	 * 新增冗余字段
	 */
	private String accName;
	private String accType;
	private String netId;
	private String corpId;
	private String corpName;
	private String curId;
	/**币别代码*/
	private String curCode;
	private String curName;

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
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**预提利息明细集合*/
	private List<FcInterestDtl>  interestDtls;

	@Column(name = "ACC_TYPE")
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	@Column(name = "NET_ID")
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
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
	@Column(name ="ACC_NAME")
	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Column(name ="CORP_ID")
	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name ="CORP_NAME")
	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	@Column(name ="CUR_ID")
	public String getCurId() {
		return curId;
	}

	public void setCurId(String curId) {
		this.curId = curId;
	}
	@Column(name ="CUR_NAME")
	public String getCurName() {
		return curName;
	}

	public void setCurName(String curName) {
		this.curName = curName;
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
	@OneToMany// 指定一对多关系
	@Cascade(value={CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="PREDRAW_DTL_ID")
	public List<FcInterestDtl> getInterestDtls() {
		return interestDtls;
	}
	public void setInterestDtls(List<FcInterestDtl> interestDtls) {
		this.interestDtls = interestDtls;
	}
	
}
