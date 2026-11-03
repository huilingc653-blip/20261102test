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
 * @Description: FBS_BUDGET_PERIOD
 * @author onlineGenerator
 * @date 2017-07-07 16:09:08
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_BUDGET_PERIOD", schema = "")
@SuppressWarnings("serial")
public class FbsBudgetPeriod extends IdEntity implements java.io.Serializable {
	/**年份*/
	private java.lang.String year;
	/**月*/
	private java.lang.Integer month;
	/**周次*/
	private java.lang.Integer week;
	/**周顺序*/
	private java.lang.Integer weekNo;
	/**开始日期*/
	private java.util.Date beginDate;
	/**结束日期*/
	private java.util.Date endDate;
	/**本周天数*/
	private java.lang.Integer weekdays;
	/**工作日天数*/
	private java.lang.Integer workdays;
	/**假期天数*/
	private java.lang.Integer holidays;
	/**备注*/
	private java.lang.String rmk;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  年份
	 */
	@Column(name ="YEAR",nullable=false,length=40)
	public java.lang.String getYear(){
		return this.year;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  年份
	 */
	public void setYear(java.lang.String year){
		this.year = year;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  月
	 */
	@Column(name ="MONTH",nullable=false,length=10)
	public java.lang.Integer getMonth(){
		return this.month;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  月
	 */
	public void setMonth(java.lang.Integer month){
		this.month = month;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  周次
	 */
	@Column(name ="WEEK",nullable=false,length=10)
	public java.lang.Integer getWeek(){
		return this.week;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  周次
	 */
	public void setWeek(java.lang.Integer week){
		this.week = week;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  周顺序
	 */
	@Column(name ="WEEK_NO",nullable=true,length=10)
	public java.lang.Integer getWeekNo(){
		return this.weekNo;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  周顺序
	 */
	public void setWeekNo(java.lang.Integer weekNo){
		this.weekNo = weekNo;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  开始日期
	 */
	@Column(name ="BEGIN_DATE",nullable=true)
	public java.util.Date getBeginDate(){
		return this.beginDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  开始日期
	 */
	public void setBeginDate(java.util.Date beginDate){
		this.beginDate = beginDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结束日期
	 */
	@Column(name ="END_DATE",nullable=true)
	public java.util.Date getEndDate(){
		return this.endDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结束日期
	 */
	public void setEndDate(java.util.Date endDate){
		this.endDate = endDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  本周天数
	 */
	@Column(name ="WEEKDAYS",nullable=true,length=10)
	public java.lang.Integer getWeekdays(){
		return this.weekdays;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  本周天数
	 */
	public void setWeekdays(java.lang.Integer weekdays){
		this.weekdays = weekdays;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  工作日天数
	 */
	@Column(name ="WORKDAYS",nullable=true,length=10)
	public java.lang.Integer getWorkdays(){
		return this.workdays;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  工作日天数
	 */
	public void setWorkdays(java.lang.Integer workdays){
		this.workdays = workdays;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  假期天数
	 */
	@Column(name ="HOLIDAYS",nullable=true,length=10)
	public java.lang.Integer getHolidays(){
		return this.holidays;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  假期天数
	 */
	public void setHolidays(java.lang.Integer holidays){
		this.holidays = holidays;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=60)
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
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
}
