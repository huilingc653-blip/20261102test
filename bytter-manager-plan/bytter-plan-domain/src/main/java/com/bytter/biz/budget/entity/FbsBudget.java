package com.bytter.biz.budget.entity;
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
 * @Description: 资金计划上报主表
 * @author onlineGenerator
 * @date 2017-07-14 14:51:48
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_BUDGET", schema = "")
@SuppressWarnings("serial")
public class FbsBudget extends IdEntity implements java.io.Serializable {
	/**单位ID*/
	private java.lang.String corpId;
	private java.lang.String corpName;
	private java.lang.String corpCode;
	/**编制时间*/
	private java.util.Date enrolDate;
	/**计划开始时间*/
	private java.util.Date beginDate;
	/**计划结束时间*/
	private java.util.Date endDate;
	/**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
	private java.lang.String timeType;
	/**需审批金额*/
	private java.math.BigDecimal checkMoney;
	/**状态*/
	private java.lang.Integer status;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**附件ID*/
	private java.lang.String attachmentId;
	/**币别*/
	private java.lang.String curId;
	private java.lang.String curName;
	/**汇率*/
	private java.math.BigDecimal exchangeRate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位ID
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	@Column(name ="CORP_NAME",nullable=true)
	public java.lang.String getCorpName(){
		return this.corpName;
	}
	public void setCorpName(java.lang.String corpName){
		this.corpName = corpName;
	}
	@Column(name ="CORP_CODE",nullable=true)
	public java.lang.String getCorpCode(){
		return this.corpCode;
	}
	public void setCorpCode(java.lang.String corpCode){
		this.corpCode = corpCode;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  编制时间
	 */
	@Column(name ="ENROL_DATE",nullable=false)
	public java.util.Date getEnrolDate() {
		return enrolDate;
	}
	
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  编制时间
	 */
	public void setEnrolDate(java.util.Date enrolDate) {
		this.enrolDate = enrolDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  计划开始时间
	 */
	@Column(name ="BEGIN_DATE",nullable=false)
	public java.util.Date getBeginDate(){
		return this.beginDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  计划开始时间
	 */
	public void setBeginDate(java.util.Date beginDate){
		this.beginDate = beginDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  计划结束时间
	 */
	@Column(name ="END_DATE",nullable=false)
	public java.util.Date getEndDate(){
		return this.endDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  计划结束时间
	 */
	public void setEndDate(java.util.Date endDate){
		this.endDate = endDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计划周期类型 1-年 2-季 3-月 4-周 5-天
	 */
	@Column(name ="TIME_TYPE",nullable=false,length=1)
	public java.lang.String getTimeType(){
		return this.timeType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计划周期类型 1-年 2-季 3-月 4-周 5-天
	 */
	public void setTimeType(java.lang.String timeType){
		this.timeType = timeType;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  需审批金额
	 */
	@Column(name ="CHECK_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getCheckMoney(){
		return this.checkMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  需审批金额
	 */
	public void setCheckMoney(java.math.BigDecimal checkMoney){
		this.checkMoney = checkMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附件ID
	 */
	@Column(name ="ATTACHMENT_ID",nullable=true,length=32)
	public java.lang.String getAttachmentId() {
		return attachmentId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附件ID
	 */
	public void setAttachmentId(java.lang.String attachmentId) {
		this.attachmentId = attachmentId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
	public java.lang.String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别
	 */
	public void setCurId(java.lang.String curId){
		this.curId = curId;
	}
	@Column(name ="CUR_NAME",nullable=true)
	public java.lang.String getCurName(){
		return this.curName;
	}
	public void setCurName(java.lang.String curName){
		this.curName = curName;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  计划汇率
	 */
	@Column(name ="EXCHANGE_RATE",nullable=false,scale=4,length=12)
	public java.math.BigDecimal getExchangeRate(){
		return this.exchangeRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  计划汇率
	 */
	public void setExchangeRate(java.math.BigDecimal exchangeRate){
		this.exchangeRate = exchangeRate;
	}
}
