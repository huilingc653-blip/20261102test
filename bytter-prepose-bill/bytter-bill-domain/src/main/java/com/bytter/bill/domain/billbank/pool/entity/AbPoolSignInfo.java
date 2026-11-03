package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 票据池签约信息
 * @author zhengg
 * @date 2020-02-24 14:40:42
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_SIGN_INFO", schema = "")
@SuppressWarnings("serial")
public class AbPoolSignInfo extends IdEntity implements java.io.Serializable {
	/**池接口代码*/
	private String corpId;
	/**票据池ID*/
	private String bpId;
	/**合同编号*/
	private String conNo;
	/**合同日期*/
	private java.util.Date conDate;
	/**合同日期*/
	private java.util.Date signDate;
	/**保证金账户*/
	private String bankAcc;
	/**保证金金额*/
	private java.math.BigDecimal bailMoney;
	/**签约机构*/
	private String signOrgNo;
	/**签约机构*/
	private String signOrgName;
	/**票据池状态 0-已解约 1-已签约*/
	private String validSign;
	/**创建日期*/
	private java.util.Date createTime;
	/**更新日期*/
	private java.util.Date updateTime;
	/**origCode机构客户号*/
	private String origCode;
	/**origName机构客户名称*/
	private String origName;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  池接口代码
	 */
	@Column(name ="CORP_ID",nullable=true,length=32)
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  池接口代码
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合同编号
	 */
	@Column(name ="CON_NO",nullable=true,length=50)
	public String getConNo(){
		return this.conNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合同编号
	 */
	public void setConNo(String conNo){
		this.conNo = conNo;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  合同日期
	 */
	@Column(name ="CON_DATE",nullable=true)
	public java.util.Date getConDate(){
		return this.conDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  合同日期
	 */
	public void setConDate(java.util.Date conDate){
		this.conDate = conDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  合同日期
	 */
	@Column(name ="SIGN_DATE",nullable=true)
	public java.util.Date getSignDate(){
		return this.signDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  合同日期
	 */
	public void setSignDate(java.util.Date signDate){
		this.signDate = signDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  保证金账户
	 */
	@Column(name ="BANK_ACC",nullable=true,length=50)
	public String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  保证金账户
	 */
	public void setBankAcc(String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  保证金金额
	 */
	@Column(name ="BAIL_MONEY",nullable=true,precision=15,scale=2)
	public java.math.BigDecimal getBailMoney(){
		return this.bailMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  保证金金额
	 */
	public void setBailMoney(java.math.BigDecimal bailMoney){
		this.bailMoney = bailMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  签约机构
	 */
	@Column(name ="SIGN_ORG_NO",nullable=true,length=500)
	public String getSignOrgNo(){
		return this.signOrgNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  签约机构
	 */
	public void setSignOrgNo(String signOrgNo){
		this.signOrgNo = signOrgNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  签约机构
	 */
	@Column(name ="SIGN_ORG_NAME",nullable=true,length=500)
	public String getSignOrgName(){
		return this.signOrgName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  签约机构
	 */
	public void setSignOrgName(String signOrgName){
		this.signOrgName = signOrgName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据池状态 0-已解约 1-已签约
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=2)
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据池状态 0-已解约 1-已签约
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_TIME",nullable=true,scale=6)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_TIME",nullable=true,scale=6)
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  origCode
	 */
	@Column(name ="ORIG_CODE")
	public String getOrigCode(){
		return this.origCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  origCode
	 */
	public void setOrigCode(String origCode){
		this.origCode = origCode;
	}
	@Column(name ="ORIG_NAME")
	public String getOrigName() {
		return origName;
	}
	public void setOrigName(String origName) {
		this.origName = origName;
	}
	@Override
	public String toString() {
		return "AbPoolSignInfo ["+
		"id=" + 
		getId() + ", "+
		"corpId=" + 
		corpId + ", "+
		"bpId=" + 
		bpId + ", "+
		"conNo=" + 
		conNo + ", "+
		"conDate=" + 
		conDate + ", "+
		"signDate=" + 
		signDate + ", "+
		"bankAcc=" + 
		bankAcc + ", "+
		"bailMoney=" + 
		bailMoney + ", "+
		"signOrgNo=" + 
		signOrgNo + ", "+
		"signOrgName=" + 
		signOrgName + ", "+
		"validSign=" + 
		validSign + ", "+
		"createTime=" + 
		createTime + ", "+
		"updateTime=" + 
		updateTime + ", "+
		"origCode=" + 
		origCode + ", "+
		"origName=" + 
		origName + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
