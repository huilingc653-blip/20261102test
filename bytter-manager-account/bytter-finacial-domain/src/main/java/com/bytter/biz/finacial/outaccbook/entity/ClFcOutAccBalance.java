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
 * @Description: FC_OUT_ACC_BALANCE
 * @author onlineGenerator
 * @date 2017-07-19 16:01:24
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_OUT_ACC_BALANCE", schema = "")
@SuppressWarnings("serial")
public class ClFcOutAccBalance extends IdEntity implements java.io.Serializable {
	/**账号*/
	private java.lang.String account;
	/**币别代码*/
	private java.lang.String curCode;
	/**日期*/
	private java.util.Date optDate;
	/**收入数量*/
	private java.lang.Integer debitNum;
	/**付出数量*/
	private java.lang.Integer creditNum;
	/**收入金额*/
	private java.math.BigDecimal debitMoney;
	/**付出金额*/
	private java.math.BigDecimal creditMoney;
	/**今日张数*/
	private java.lang.Integer tPageNum;
	/**昨日余额*/
	private java.math.BigDecimal yBalance;
	/**今天余额*/
	private java.math.BigDecimal tBalance;
	/**昨日笔数*/
	private java.lang.Integer yCount;
	/**今日笔数*/
	private java.lang.Integer tCount;
	/**收方笔数*/
	private java.lang.Integer debitCount;
	/**付方笔数*/
	private java.lang.Integer creditCount;
	/**收方张数*/
	private java.lang.Integer debitPageNum;
	/**付方张数*/
	private java.lang.Integer creditPageNum;
	/**昨日张数*/
	private java.lang.Integer yPageNum;
	/**昨日数量*/
	private java.lang.Integer yNum;
	/**今日数量*/
	private java.lang.Integer tNum;
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  日期
	 */
	@Column(name ="OPT_DATE")
	public java.util.Date getOptDate(){
		return this.optDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  日期
	 */
	public void setOptDate(java.util.Date optDate){
		this.optDate = optDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收入数量
	 */
	@Column(name ="DEBIT_NUM")
	public java.lang.Integer getDebitNum(){
		return this.debitNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  收入数量
	 */
	public void setDebitNum(java.lang.Integer debitNum){
		this.debitNum = debitNum;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付出数量
	 */
	@Column(name ="CREDIT_NUM")
	public java.lang.Integer getCreditNum(){
		return this.creditNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  付出数量
	 */
	public void setCreditNum(java.lang.Integer creditNum){
		this.creditNum = creditNum;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  收入金额
	 */
	@Column(name ="DEBIT_MONEY")
	public java.math.BigDecimal getDebitMoney(){
		return this.debitMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  收入金额
	 */
	public void setDebitMoney(java.math.BigDecimal debitMoney){
		this.debitMoney = debitMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  付出金额
	 */
	@Column(name ="CREDIT_MONEY")
	public java.math.BigDecimal getCreditMoney(){
		return this.creditMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  付出金额
	 */
	public void setCreditMoney(java.math.BigDecimal creditMoney){
		this.creditMoney = creditMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  今日张数
	 */
	@Column(name ="T_PAGE_NUM")
	public java.lang.Integer getTPageNum(){
		return this.tPageNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  今日张数
	 */
	public void setTPageNum(java.lang.Integer tPageNum){
		this.tPageNum = tPageNum;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  昨日余额
	 */
	@Column(name ="Y_BALANCE")
	public java.math.BigDecimal getYBalance(){
		return this.yBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  昨日余额
	 */
	public void setYBalance(java.math.BigDecimal yBalance){
		this.yBalance = yBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  今天余额
	 */
	@Column(name ="T_BALANCE")
	public java.math.BigDecimal getTBalance(){
		return this.tBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  今天余额
	 */
	public void setTBalance(java.math.BigDecimal tBalance){
		this.tBalance = tBalance;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  昨日笔数
	 */
	@Column(name ="Y_COUNT")
	public java.lang.Integer getYCount(){
		return this.yCount;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  昨日笔数
	 */
	public void setYCount(java.lang.Integer yCount){
		this.yCount = yCount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  今日笔数
	 */
	@Column(name ="T_COUNT")
	public java.lang.Integer getTCount(){
		return this.tCount;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  今日笔数
	 */
	public void setTCount(java.lang.Integer tCount){
		this.tCount = tCount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收方笔数
	 */
	@Column(name ="DEBIT_COUNT")
	public java.lang.Integer getDebitCount(){
		return this.debitCount;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  收方笔数
	 */
	public void setDebitCount(java.lang.Integer debitCount){
		this.debitCount = debitCount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付方笔数
	 */
	@Column(name ="CREDIT_COUNT")
	public java.lang.Integer getCreditCount(){
		return this.creditCount;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  付方笔数
	 */
	public void setCreditCount(java.lang.Integer creditCount){
		this.creditCount = creditCount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  收方张数
	 */
	@Column(name ="DEBIT_PAGE_NUM")
	public java.lang.Integer getDebitPageNum(){
		return this.debitPageNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  收方张数
	 */
	public void setDebitPageNum(java.lang.Integer debitPageNum){
		this.debitPageNum = debitPageNum;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  付方张数
	 */
	@Column(name ="CREDIT_PAGE_NUM")
	public java.lang.Integer getCreditPageNum(){
		return this.creditPageNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  付方张数
	 */
	public void setCreditPageNum(java.lang.Integer creditPageNum){
		this.creditPageNum = creditPageNum;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  昨日张数
	 */
	@Column(name ="Y_PAGE_NUM")
	public java.lang.Integer getYPageNum(){
		return this.yPageNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  昨日张数
	 */
	public void setYPageNum(java.lang.Integer yPageNum){
		this.yPageNum = yPageNum;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  昨日数量
	 */
	@Column(name ="Y_NUM")
	public java.lang.Integer getYNum(){
		return this.yNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  昨日数量
	 */
	public void setYNum(java.lang.Integer yNum){
		this.yNum = yNum;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  今日数量
	 */
	@Column(name ="T_NUM")
	public java.lang.Integer getTNum(){
		return this.tNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  今日数量
	 */
	public void setTNum(java.lang.Integer tNum){
		this.tNum = tNum;
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
