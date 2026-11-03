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
 * @Description: FC_MONTH_LEDGER
 * @author onlineGenerator
 * @date 2017-07-17 18:36:41
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_MONTH_LEDGER", schema = "")
@SuppressWarnings("serial")
public class FcMonthLedger extends IdEntity implements java.io.Serializable {
	/**年月*/
	private java.lang.String yearMonth2;
	/**网点代码*/
	private java.lang.String netCode;
	/**区域代码*/
	private java.lang.String areaCode;
	/**行业代码*/
	private java.lang.String vocationCode;
	/**科目代码*/
	private java.lang.String subjectCode;
	/**币别代码*/
	private java.lang.String currencyCode;
	/**月初余额*/
	private java.math.BigDecimal initBalance;
	/**借方发生额*/
	private java.math.BigDecimal debitMoney;
	/**贷方发生额*/
	private java.math.BigDecimal creditMoney;
	/**借方笔数*/
	private java.lang.Integer debitCnt;
	/**贷方笔数*/
	private java.lang.Integer creditCnt;
	/**月末余额*/
	private java.math.BigDecimal balance;
	/**累计借方发生额*/
	private java.math.BigDecimal totalDebitMoney;
	/**累计贷方发生额*/
	private java.math.BigDecimal totalCreditMoney;
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
	 *@return: java.lang.String  网点代码
	 */
	@Column(name ="NET_CODE")
	public java.lang.String getNetCode(){
		return this.netCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  网点代码
	 */
	public void setNetCode(java.lang.String netCode){
		this.netCode = netCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  区域代码
	 */
	@Column(name ="AREA_CODE")
	public java.lang.String getAreaCode(){
		return this.areaCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  区域代码
	 */
	public void setAreaCode(java.lang.String areaCode){
		this.areaCode = areaCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  行业代码
	 */
	@Column(name ="VOCATION_CODE")
	public java.lang.String getVocationCode(){
		return this.vocationCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  行业代码
	 */
	public void setVocationCode(java.lang.String vocationCode){
		this.vocationCode = vocationCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目代码
	 */
	@Column(name ="SUBJECT_CODE")
	public java.lang.String getSubjectCode(){
		return this.subjectCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目代码
	 */
	public void setSubjectCode(java.lang.String subjectCode){
		this.subjectCode = subjectCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CURRENCY_CODE")
	public java.lang.String getCurrencyCode(){
		return this.currencyCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurrencyCode(java.lang.String currencyCode){
		this.currencyCode = currencyCode;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  月初余额
	 */
	@Column(name ="INIT_BALANCE")
	public java.math.BigDecimal getInitBalance(){
		return this.initBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  月初余额
	 */
	public void setInitBalance(java.math.BigDecimal initBalance){
		this.initBalance = initBalance;
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
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  借方笔数
	 */
	@Column(name ="DEBIT_CNT")
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
	@Column(name ="CREDIT_CNT")
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
	 *@return: java.math.BigDecimal  月末余额
	 */
	@Column(name ="BALANCE")
	public java.math.BigDecimal getBalance(){
		return this.balance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  月末余额
	 */
	public void setBalance(java.math.BigDecimal balance){
		this.balance = balance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  累计借方发生额
	 */
	@Column(name ="TOTAL_DEBIT_MONEY")
	public java.math.BigDecimal getTotalDebitMoney(){
		return this.totalDebitMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  累计借方发生额
	 */
	public void setTotalDebitMoney(java.math.BigDecimal totalDebitMoney){
		this.totalDebitMoney = totalDebitMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  累计贷方发生额
	 */
	@Column(name ="TOTAL_CREDIT_MONEY")
	public java.math.BigDecimal getTotalCreditMoney(){
		return this.totalCreditMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  累计贷方发生额
	 */
	public void setTotalCreditMoney(java.math.BigDecimal totalCreditMoney){
		this.totalCreditMoney = totalCreditMoney;
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
