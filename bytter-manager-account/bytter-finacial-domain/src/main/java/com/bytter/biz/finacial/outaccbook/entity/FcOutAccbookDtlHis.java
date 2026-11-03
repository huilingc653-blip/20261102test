package com.bytter.biz.finacial.outaccbook.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_OUT_ACCBOOK_DTL_HIS
 * @author onlineGenerator
 * @date 2017-07-07 16:10:22
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_OUT_ACCBOOK_DTL_HIS", schema = "")
@SuppressWarnings("serial")
public class FcOutAccbookDtlHis extends IdEntity implements java.io.Serializable {
	/**表外流水ID*/
	private java.lang.String outAccbookId;
	/**行号*/
	private java.lang.Integer lineNo;
	/**账号*/
	private java.lang.String account;
	/**币别代码*/
	private java.lang.String curCode;
	/**借方发生额*/
	private java.math.BigDecimal debitMoney;
	/**贷方发生额*/
	private java.math.BigDecimal creditMoney;
	/**摘要*/
	private java.lang.String summaryContent;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  表外流水ID
	 */
	@Column(name ="OUT_ACCBOOK_ID")
	public java.lang.String getOutAccbookId(){
		return this.outAccbookId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  表外流水ID
	 */
	public void setOutAccbookId(java.lang.String outAccbookId){
		this.outAccbookId = outAccbookId;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  行号
	 */
	@Column(name ="LINE_NO")
	public java.lang.Integer getLineNo(){
		return this.lineNo;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  行号
	 */
	public void setLineNo(java.lang.Integer lineNo){
		this.lineNo = lineNo;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="SUMMARY_CONTENT")
	public java.lang.String getSummaryContent(){
		return this.summaryContent;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要
	 */
	public void setSummaryContent(java.lang.String summaryContent){
		this.summaryContent = summaryContent;
	}
}
