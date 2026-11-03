package com.bytter.biz.budget.vo;
import java.lang.String;
/**   
 * @Title: Entity
 * @Description: FBS_BUDGET_PERIOD
 * @author onlineGenerator
 * @date 2017-07-07 16:09:08
 * @version 1.0
 *
 */
public class FbsBudgetPeriodTransmitVO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4999274534241862110L;
	
	/**年份*/
	private String year;
	/**月*/
	private String[] month;
	/**周次*/
	private String[] week;
	/**周顺序*/
	private String[] weekNo;
	/**开始日期*/
	private String[] beginDate;
	/**结束日期*/
	private String[] endDate;
	/**本周天数*/
	private String[] weekdays;
	/**工作日天数*/
	private String[] workdays;
	/**假期天数*/
	private String[] holidays;
	/**备注*/
	private String[] rmk;
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String[] getMonth() {
		return month;
	}
	public void setMonth(String[] month) {
		this.month = month;
	}
	public String[] getWeek() {
		return week;
	}
	public void setWeek(String[] week) {
		this.week = week;
	}
	public String[] getWeekNo() {
		return weekNo;
	}
	public void setWeekNo(String[] weekNo) {
		this.weekNo = weekNo;
	}
	public String[] getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String[] beginDate) {
		this.beginDate = beginDate;
	}
	public String[] getEndDate() {
		return endDate;
	}
	public void setEndDate(String[] endDate) {
		this.endDate = endDate;
	}
	public String[] getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(String[] weekdays) {
		this.weekdays = weekdays;
	}
	public String[] getWorkdays() {
		return workdays;
	}
	public void setWorkdays(String[] workdays) {
		this.workdays = workdays;
	}
	public String[] getHolidays() {
		return holidays;
	}
	public void setHolidays(String[] holidays) {
		this.holidays = holidays;
	}
	public String[] getRmk() {
		return rmk;
	}
	public void setRmk(String[] rmk) {
		this.rmk = rmk;
	}
}
