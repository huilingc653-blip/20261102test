package com.bytter.biz.budget.vo;
import java.lang.String;
import com.bytter.core.utils.DateUtils;
/**   
 * @Title: Entity
 * @Description: FBS_BUDGET_PERIOD
 * @author onlineGenerator
 * @date 2017-07-07 16:09:08
 * @version 1.0
 *
 */
public class FbsBudgetPeriodVO  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4999274534241862110L;
	
	private String id;
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
	private String beginDateStr;
	/**结束日期*/
	private java.util.Date endDate;
	private String endDateStr;
	private String showEndDate;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getYear() {
		return year;
	}
	public void setYear(java.lang.String year) {
		this.year = year;
	}
	public java.lang.Integer getMonth() {
		return month;
	}
	public void setMonth(java.lang.Integer month) {
		this.month = month;
	}
	public java.lang.Integer getWeek() {
		return week;
	}
	public void setWeek(java.lang.Integer week) {
		this.week = week;
	}
	public java.lang.Integer getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(java.lang.Integer weekNo) {
		this.weekNo = weekNo;
	}
	public java.util.Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(java.util.Date beginDate) {
		this.beginDate = beginDate;
		if(null !=beginDate){
			this.beginDateStr = DateUtils.formatDate(beginDate);
		}
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
		if(null != endDate){
			this.endDateStr = DateUtils.formatDate(endDate);
		}
	}
	public java.lang.Integer getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(java.lang.Integer weekdays) {
		this.weekdays = weekdays;
	}
	public java.lang.Integer getWorkdays() {
		return workdays;
	}
	public void setWorkdays(java.lang.Integer workdays) {
		this.workdays = workdays;
	}
	public java.lang.Integer getHolidays() {
		return holidays;
	}
	public void setHolidays(java.lang.Integer holidays) {
		this.holidays = holidays;
	}
	public java.lang.String getRmk() {
		return rmk;
	}
	public void setRmk(java.lang.String rmk) {
		this.rmk = rmk;
	}
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public java.lang.String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getBeginDateStr() {
		return beginDateStr;
	}
	public void setBeginDateStr(String beginDateStr) {
		this.beginDateStr = beginDateStr;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public String getShowEndDate() {
		return showEndDate;
	}
	public void setShowEndDate(String showEndDate) {
		this.showEndDate = showEndDate;
	}
}
