package com.bytter.biz.budget.spi.dto;
import com.bytter.core.utils.DateUtils;
/**
 * @Title: Entity
 * @Description: FBS_BUDGET_PERIOD
 * @author onlineGenerator
 * @date 2017-07-07 16:09:08
 * @version 1.0
 *
 */
public class FbsBudgetPeriodDTO implements java.io.Serializable {
	private String id;
	/**年份*/
	private String year;
	/**月*/
	private Integer month;
	/**周次*/
	private Integer week;
	/**周顺序*/
	private Integer weekNo;
	/**开始日期*/
	private java.util.Date beginDate;
	private String beginDateStr;
	/**结束日期*/
	private java.util.Date endDate;
	private String endDateStr;
	private String showEndDate;
	/**本周天数*/
	private Integer weekdays;
	/**工作日天数*/
	private Integer workdays;
	/**假期天数*/
	private Integer holidays;
	/**备注*/
	private String rmk;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getWeek() {
		return week;
	}
	public void setWeek(Integer week) {
		this.week = week;
	}
	public Integer getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(Integer weekNo) {
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
	public Integer getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(Integer weekdays) {
		this.weekdays = weekdays;
	}
	public Integer getWorkdays() {
		return workdays;
	}
	public void setWorkdays(Integer workdays) {
		this.workdays = workdays;
	}
	public Integer getHolidays() {
		return holidays;
	}
	public void setHolidays(Integer holidays) {
		this.holidays = holidays;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
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
