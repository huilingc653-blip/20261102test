package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
/**
 * @Title: Entity
 * @Description: 额度信息表
 * @author zhengg
 * @date 2020-02-24 14:40:41
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_LIMIT", schema = "")
@SuppressWarnings("serial")
public class AbPoolLimit extends IdEntity implements java.io.Serializable {
	/**单位ID*/
	private String corpId;
	/**票据池ID*/
	private String bpId;
	/**票据质押额度*/
	private BigDecimal pledgeMoney;
	/**保证金额度*/
	private BigDecimal bailMoney;
	/**票据池总额度*/
	private BigDecimal poolMoney;
	/**票据池可用额度*/
	private BigDecimal validMoney;
	/**票据池已用额度*/
	private BigDecimal lockMoney;
	/**票据池调剂额度 必回 */
	private BigDecimal adjustMoney;
	/**
	 * 低风险额度
	 */
	private BigDecimal lowRiskMoney;
	/**
	 * 非低风险额度
	 */
	private BigDecimal inLowRiskMoney;
	/**
	 * 已用低风险额度
	 */
	private BigDecimal usedLowRiskMoney;
	/**
	 * 已用非低风险额度
	 */
	private BigDecimal usedInLowRiskMoney;
	/**
	 * 可用低风险额度
	 */
	private BigDecimal validLowRiskMoney;
	/**
	 * 可用非低风险额度
	 */
	private BigDecimal validInLowRiskMoney;
	/**状态：1有效、0无效 */
	private String validSign;
	/**额度日期*/
	private java.util.Date limitDate;
	/**更新日期*/
	private java.util.Date createTime;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=true,length=32)
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位ID
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据池ID
	 */
	@Column(name ="BP_ID",nullable=true,length=32)
	public String getBpId(){
		return this.bpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据池ID
	 */
	public void setBpId(String bpId){
		this.bpId = bpId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  票据质押额度
	 */
	@Column(name ="PLEDGE_MONEY",nullable=true,precision=15,scale=2)
	public BigDecimal getPledgeMoney(){
		return this.pledgeMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  票据质押额度
	 */
	public void setPledgeMoney(BigDecimal pledgeMoney){
		this.pledgeMoney = pledgeMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  保证金额度
	 */
	@Column(name ="BAIL_MONEY",nullable=true,precision=15,scale=2)
	public BigDecimal getBailMoney(){
		return this.bailMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  保证金额度
	 */
	public void setBailMoney(BigDecimal bailMoney){
		this.bailMoney = bailMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  票据池总额度
	 */
	@Column(name ="POOL_MONEY",nullable=true,precision=15,scale=2)
	public BigDecimal getPoolMoney(){
		return this.poolMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  票据池总额度
	 */
	public void setPoolMoney(BigDecimal poolMoney){
		this.poolMoney = poolMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  票据池可用额度
	 */
	@Column(name ="VALID_MONEY",nullable=true,precision=15,scale=2)
	public BigDecimal getValidMoney(){
		return this.validMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  票据池可用额度
	 */
	public void setValidMoney(BigDecimal validMoney){
		this.validMoney = validMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  票据池已用额度
	 */
	@Column(name ="LOCK_MONEY",nullable=true,precision=15,scale=2)
	public BigDecimal getLockMoney(){
		return this.lockMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  票据池已用额度
	 */
	public void setLockMoney(BigDecimal lockMoney){
		this.lockMoney = lockMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态：1有效、0无效
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=2)
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态：1有效、0无效
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="LIMIT_DATE",nullable=true)
	public java.util.Date getLimitDate(){
		return this.limitDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setLimitDate(java.util.Date limitDate){
		this.limitDate = limitDate;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="CREATE_TIME",nullable=true,scale=6)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	@Column(name ="ADJUST_MONEY")
	public BigDecimal getAdjustMoney() {
		return adjustMoney;
	}
	public void setAdjustMoney(BigDecimal adjustMoney) {
		this.adjustMoney = adjustMoney;
	}
	
	@Column(name = "LOWRISK_MONEY")
	public BigDecimal getLowRiskMoney() {
		return lowRiskMoney;
	}
	public void setLowRiskMoney(BigDecimal lowRiskMoney) {
		this.lowRiskMoney = lowRiskMoney;
	}
	@Column(name = "INLOWRISK_MONEY")
	public BigDecimal getInLowRiskMoney() {
		return inLowRiskMoney;
	}
	public void setInLowRiskMoney(BigDecimal inLowRiskMoney) {
		this.inLowRiskMoney = inLowRiskMoney;
	}
	@Column(name = "USED_LOWRISK_MONEY")
	public BigDecimal getUsedLowRiskMoney() {
		return usedLowRiskMoney;
	}
	public void setUsedLowRiskMoney(BigDecimal usedLowRiskMoney) {
		this.usedLowRiskMoney = usedLowRiskMoney;
	}
	@Column(name = "USED_INLOWRISK_MONEY")
	public BigDecimal getUsedInLowRiskMoney() {
		return usedInLowRiskMoney;
	}
	public void setUsedInLowRiskMoney(BigDecimal usedInLowRiskMoney) {
		this.usedInLowRiskMoney = usedInLowRiskMoney;
	}
	@Column(name = "VALID_LOWRISK_MONEY")
	public BigDecimal getValidLowRiskMoney() {
		return validLowRiskMoney;
	}
	public void setValidLowRiskMoney(BigDecimal validLowRiskMoney) {
		this.validLowRiskMoney = validLowRiskMoney;
	}
	@Column(name = "VALID_INLOWRISK_MONEY")
	public BigDecimal getValidInLowRiskMoney() {
		return validInLowRiskMoney;
	}
	public void setValidInLowRiskMoney(BigDecimal validInLowRiskMoney) {
		this.validInLowRiskMoney = validInLowRiskMoney;
	}
	@Override
	public String toString() {
		return "AbPoolLimit [corpId=" + corpId + ", bpId=" + bpId
				+ ", pledgeMoney=" + pledgeMoney + ", bailMoney=" + bailMoney
				+ ", poolMoney=" + poolMoney + ", validMoney=" + validMoney
				+ ", lockMoney=" + lockMoney + ", adjustMoney=" + adjustMoney
				+ ", lowRiskMoney=" + lowRiskMoney + ", inLowRiskMoney="
				+ inLowRiskMoney + ", usedLowRiskMoney=" + usedLowRiskMoney
				+ ", usedInLowRiskMoney=" + usedInLowRiskMoney
				+ ", validLowRiskMoney=" + validLowRiskMoney
				+ ", validInLowRiskMoney=" + validInLowRiskMoney
				+ ", validSign=" + validSign + ", limitDate=" + limitDate
				+ ", createTime=" + createTime + "]";
	}
}
