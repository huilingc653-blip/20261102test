package com.bytter.biz.budget.utils;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 资金计划辅助类
 *
 * @author zuoy 2018-02-06
 */
public class BudgetUtils {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 根据周期类型获取开始日期
	 *
	 * @param seachBeginDate 查询开始日期
	 * @param timeType       周期类型
	 * @return
	 */
	public static Date getBeginDate(Date seachBeginDate, String timeType) {
		if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
			return DateUtils.getFirstYearDate(seachBeginDate);
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
			return DateUtils.getFirstQuarterDate(seachBeginDate);
		} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
			return DateUtils.getFirstMonthDate(seachBeginDate);
		} else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
			Date date = DateUtils.getFirstWeekDate(seachBeginDate);
			return date.before(DateUtils.getFirstMonthDate(seachBeginDate)) ? DateUtils.getFirstMonthDate(seachBeginDate) : date;
		} else {
			return seachBeginDate;
		}
	}
	/**
	 * 根据周期类型获取结束日期
	 *
	 * @param seachEndDate 查询结束日期
	 * @param timeType     周期类型
	 * @return
	 */
	public static Date getEndDate(Date seachEndDate, String timeType) {
		if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
			return DateUtils.getLastYearDate(seachEndDate);
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
			return DateUtils.getLastQuarterDate(seachEndDate);
		} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
			return DateUtils.getLastMonthDate(seachEndDate);
		} else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
			Date date = DateUtils.getLastWeekDate(seachEndDate);
			return date.after(DateUtils.getLastMonthDate(seachEndDate)) ? DateUtils.getLastMonthDate(seachEndDate) : date;
		} else {
			return seachEndDate;
		}
	}
	/**
	 * 根据周期类型获取上期开始日期
	 *
	 * @param beginDate 查询开始日期
	 * @param timeType  周期类型
	 * @return
	 */
	public static Date getLastBeginDate(Date beginDate, String timeType) {
		if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
			return DateUtils.addYear(beginDate, -1);
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
			return DateUtils.addQuarter(beginDate, -1);
		} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
			return DateUtils.addMonth(beginDate, -1);
		} else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
			return DateUtils.addWeek(beginDate, -1);
		} else {
			return DateUtils.addDay(beginDate, -1);
		}
	}
	/**
	 * 根据周期类型获取上期结束日期
	 *
	 * @param endDate  查询开始日期
	 * @param timeType 周期类型
	 * @return
	 */
	public static Date getLastEndDate(Date endDate, String timeType) {
		if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
			return DateUtils.addYear(endDate, -1);
		} else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
			Date date = DateUtils.addQuarter(endDate, -1) ;
			date = DateUtils.getLastQuarterDate(date);
			return date;
		} else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
			Date date =  DateUtils.addMonth(endDate, -1);
			date = DateUtils.getLastMonthDate(date);
			return date;
		} else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
			return DateUtils.addWeek(endDate, -1);
		} else {
			return DateUtils.addDay(endDate, -1);
		}
	}
	/**
	 * 获取年的指定月份的第一天
	 *
	 * @param year  年
	 * @param month 指定月份
	 * @return
	 */
	public static Date getFirstMonthDateOfYear(Date year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(year);
		calendar.add(Calendar.MONTH, month - 1);
		return DateUtils.getFirstMonthDate(calendar.getTime());
	}
	/**
	 * 获取年的指定月份的最后一天
	 *
	 * @param year  年
	 * @param month 指定月份
	 * @return
	 */
	public static Date getLastMonthDateOfYear(Date year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(year);
		calendar.add(Calendar.MONTH, month - 1);
		return DateUtils.getLastMonthDate(calendar.getTime());
	}
	/**
	 * 获取月的指定周的第一天
	 *
	 * @param month 月
	 * @param week  指定周份
	 * @return
	 */
	public static Date getFirstWeekDateOfMonth(Date month, int week) {
		month = DateUtils.getFirstMonthDate(month);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.add(Calendar.WEEK_OF_MONTH, week);
		Date date = DateUtils.getFirstWeekDate(calendar.getTime());
		return date.before(DateUtils.getFirstMonthDate(calendar.getTime())) ? DateUtils.getFirstMonthDate(calendar.getTime()) : date;
	}
	/**
	 * 获取月的指定周的最后一天
	 *
	 * @param month 月
	 * @param week  指定周
	 * @return
	 */
	public static Date getLastWeekDateOfMonth(Date month, int week) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.add(Calendar.WEEK_OF_MONTH, week);
		Date date = DateUtils.getLastWeekDate(calendar.getTime());
		return date.after(DateUtils.getLastMonthDate(calendar.getTime())) ? DateUtils.getLastMonthDate(calendar.getTime()) : date;
	}
	/**
	 * 当前日期不是月第一天，则取月第一天
	 *
	 * @param month 当前月
	 * @param week  周第一天
	 * @return
	 */
	public static Date isFirstWeek(Date month, Date week) {
		return week.before(DateUtils.getFirstMonthDate(month)) ? DateUtils.getFirstMonthDate(month) : week;
	}
	/**
	 * 当前日期不是月最后一天，则取月后一天
	 *
	 * @param month 当前月
	 * @param week  周最后一天
	 * @return
	 */
	public static Date isLastWeekDate(Date month, Date week) {
		return week.after(DateUtils.getLastMonthDate(month)) ? DateUtils.getLastMonthDate(month) : week;
	}
}
