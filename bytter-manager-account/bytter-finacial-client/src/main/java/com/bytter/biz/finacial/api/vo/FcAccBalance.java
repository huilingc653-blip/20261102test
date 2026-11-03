package com.bytter.biz.finacial.api.vo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_ACC_BALANCE
 * @author onlineGenerator
 * @date 2017-06-07 10:04:02
 * @version 1.0
 *
 */
public class FcAccBalance extends IdEntity implements java.io.Serializable {
	/**余额日期*/
	private java.util.Date balanceDate;
	/**账号*/
	private java.lang.String account;
	/**币别代码*/
	private java.lang.String curId;
	/**账户名称*/
	private java.lang.String accName;
	/**内转笔数*/
	private java.lang.Integer trCnt;
	/**冲正笔数*/
	private java.lang.Integer rushCnt;
	/**笔数*/
	private java.lang.Integer cnt;
	/**昨日余额*/
	private java.math.BigDecimal yesterBalance;
	/**余额*/
	private java.math.BigDecimal balance;
	/**内转借方发生额*/
	private java.math.BigDecimal trDebitMoney;
	/**内转贷方发生额*/
	private java.math.BigDecimal trCreditMoney;
	/**冲正借方发生额*/
	private java.math.BigDecimal rushDebitMoney;
	/**冲正贷方发生额*/
	private java.math.BigDecimal rushCreditMoney;
	/**借方发生额*/
	private java.math.BigDecimal debitMoney;
	/**贷方发生额*/
	private java.math.BigDecimal creditMoney;
	/**是否记账日*/
	private java.lang.String tallyDaySign;
	/**借方笔数*/
	private java.lang.Integer debitCnt;
	/**贷方笔数*/
	private java.lang.Integer creditCnt;
	/**冻结金额*/
	private java.math.BigDecimal freezeMoney;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	//@Column(name ="CUR_ID",nullable=false,length=32)
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  余额日期
	 */
	//@Column(name ="BALANCE_DATE",nullable=false)
	public java.util.Date getBalanceDate(){
		return this.balanceDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  余额日期
	 */
	public void setBalanceDate(java.util.Date balanceDate){
		this.balanceDate = balanceDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	//@Column(name ="ACCOUNT",nullable=false,length=50)
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
	 *@return: java.lang.String  账户名称
	 */
	//@Column(name ="ACC_NAME",nullable=false,length=80)
	public java.lang.String getAccName(){
		return this.accName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账户名称
	 */
	public void setAccName(java.lang.String accName){
		this.accName = accName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  内转笔数
	 */
	//@Column(name ="TR_CNT",nullable=false,length=10)
	public java.lang.Integer getTrCnt(){
		return this.trCnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  内转笔数
	 */
	public void setTrCnt(java.lang.Integer trCnt){
		this.trCnt = trCnt;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  冲正笔数
	 */
	//@Column(name ="RUSH_CNT",nullable=false,length=10)
	public java.lang.Integer getRushCnt(){
		return this.rushCnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  冲正笔数
	 */
	public void setRushCnt(java.lang.Integer rushCnt){
		this.rushCnt = rushCnt;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  笔数
	 */
	//@Column(name ="CNT",nullable=false,length=10)
	public java.lang.Integer getCnt(){
		return this.cnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  笔数
	 */
	public void setCnt(java.lang.Integer cnt){
		this.cnt = cnt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  昨日余额
	 */
	//@Column(name ="YESTER_BALANCE",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getYesterBalance(){
		return this.yesterBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  昨日余额
	 */
	public void setYesterBalance(java.math.BigDecimal yesterBalance){
		this.yesterBalance = yesterBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  余额
	 */
	//@Column(name ="BALANCE",nullable=false,scale=2,length=15)
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  内转借方发生额
	 */
	//@Column(name ="TR_DEBIT_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getTrDebitMoney(){
		return this.trDebitMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  内转借方发生额
	 */
	public void setTrDebitMoney(java.math.BigDecimal trDebitMoney){
		this.trDebitMoney = trDebitMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  内转贷方发生额
	 */
	//@Column(name ="TR_CREDIT_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getTrCreditMoney(){
		return this.trCreditMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  内转贷方发生额
	 */
	public void setTrCreditMoney(java.math.BigDecimal trCreditMoney){
		this.trCreditMoney = trCreditMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  冲正借方发生额
	 */
	//@Column(name ="RUSH_DEBIT_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getRushDebitMoney(){
		return this.rushDebitMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  冲正借方发生额
	 */
	public void setRushDebitMoney(java.math.BigDecimal rushDebitMoney){
		this.rushDebitMoney = rushDebitMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  冲正贷方发生额
	 */
	//@Column(name ="RUSH_CREDIT_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getRushCreditMoney(){
		return this.rushCreditMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  冲正贷方发生额
	 */
	public void setRushCreditMoney(java.math.BigDecimal rushCreditMoney){
		this.rushCreditMoney = rushCreditMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  借方发生额
	 */
	//@Column(name ="DEBIT_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getDebitMoney(){
		return this.debitMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  借方发生额
	 */
	public void setDebitMoney(java.math.BigDecimal debitMoney){
		this.debitMoney = debitMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贷方发生额
	 */
	//@Column(name ="CREDIT_MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getCreditMoney(){
		return this.creditMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贷方发生额
	 */
	public void setCreditMoney(java.math.BigDecimal creditMoney){
		this.creditMoney = creditMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否记账日
	 */
	//@Column(name ="TALLY_DAY_SIGN",nullable=false,length=1)
	public java.lang.String getTallyDaySign(){
		return this.tallyDaySign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否记账日
	 */
	public void setTallyDaySign(java.lang.String tallyDaySign){
		this.tallyDaySign = tallyDaySign;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  借方笔数
	 */
	//@Column(name ="DEBIT_CNT",nullable=true,length=10)
	public java.lang.Integer getDebitCnt(){
		return this.debitCnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  借方笔数
	 */
	public void setDebitCnt(java.lang.Integer debitCnt){
		this.debitCnt = debitCnt;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  贷方笔数
	 */
	//@Column(name ="CREDIT_CNT",nullable=true,length=10)
	public java.lang.Integer getCreditCnt(){
		return this.creditCnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  贷方笔数
	 */
	public void setCreditCnt(java.lang.Integer creditCnt){
		this.creditCnt = creditCnt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  冻结金额
	 */
	//@Column(name ="FREEZE_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getFreezeMoney(){
		return this.freezeMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  冻结金额
	 */
	public void setFreezeMoney(java.math.BigDecimal freezeMoney){
		this.freezeMoney = freezeMoney;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	//@Column(name ="CREATE_TIME",nullable=true)
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
	//@Column(name ="CREATE_BY",nullable=true,length=32)
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
	//@Column(name ="UPDATE_TIME",nullable=true)
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
	//@Column(name ="UPDATE_BY",nullable=true,length=32)
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
