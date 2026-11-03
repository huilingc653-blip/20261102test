package com.bytter.biz.finacial.closedeal.entity;
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
 * @Description: FC_ACC_MONTH_BALANCE
 * @author onlineGenerator
 * @date 2017-07-17 18:38:13
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_ACC_MONTH_BALANCE", schema = "")
@SuppressWarnings("serial")
public class FcAccMonthBalance extends IdEntity implements java.io.Serializable {
	/**年月*/
	private java.lang.String yearMonth2;
	/**账号*/
	private java.lang.String account;
	/**账户名称*/
	private java.lang.String accName;
	/**内转笔数*/
	private java.lang.Integer trCnt;
	/**冲正笔数*/
	private java.lang.Integer rushCnt;
	/**笔数*/
	private java.lang.Integer cnt;
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
	/**上月余额*/
	private java.math.BigDecimal preBalance;
	/**本月余额*/
	private java.math.BigDecimal balance;
	/**电子回单是否产生*/
	private java.lang.String exportSign;
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
	 *@return: java.lang.String  年月
	 */
	@Column(name ="YEAR_MONTH2")
	public java.lang.String getYearMonth2(){
		return this.yearMonth2;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  年月
	 */
	public void setYearMonth2(java.lang.String yearMonth2){
		this.yearMonth2 = yearMonth2;
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
	 *@return: java.lang.String  账户名称
	 */
	@Column(name ="ACC_NAME")
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
	@Column(name ="TR_CNT")
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
	@Column(name ="RUSH_CNT")
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
	@Column(name ="CNT")
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
	 *@return: java.math.BigDecimal  内转借方发生额
	 */
	@Column(name ="TR_DEBIT_MONEY")
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
	@Column(name ="TR_CREDIT_MONEY")
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
	@Column(name ="RUSH_DEBIT_MONEY")
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
	@Column(name ="RUSH_CREDIT_MONEY")
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
	@Column(name ="DEBIT_MONEY")
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
	@Column(name ="CREDIT_MONEY")
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
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  上月余额
	 */
	@Column(name ="PRE_BALANCE")
	public java.math.BigDecimal getPreBalance(){
		return this.preBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  上月余额
	 */
	public void setPreBalance(java.math.BigDecimal preBalance){
		this.preBalance = preBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  本月余额
	 */
	@Column(name ="BALANCE")
	public java.math.BigDecimal getBalance(){
		return this.balance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  本月余额
	 */
	public void setBalance(java.math.BigDecimal balance){
		this.balance = balance;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子回单是否产生
	 */
	@Column(name ="EXPORT_SIGN")
	public java.lang.String getExportSign(){
		return this.exportSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单是否产生
	 */
	public void setExportSign(java.lang.String exportSign){
		this.exportSign = exportSign;
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
