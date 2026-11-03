package com.bytter.biz.fundtransfer.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类
 *
 * @author : ZDB
 * @version : v1.0
 * @createTime : 2023/3/15 18:22
 */
public class DateUtil {
	
	public final static long MILLISENCOND_PER_DAY = 24 * 60 * 60 * 1000;

	public final static String yyyyMMdd = "yyyyMMdd";
	public final static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	/**
	 * 格式化指定格式的日期，并返回已格式化的日期字符串。　
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 * @author zhangjf
	 */
	public static String formatDate(Date date, String pattern) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			return format.format(date);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 格式化指定格式的日期字符串，并返回已格式化的日期。　
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 * @author zhangjf
	 */
	public static Date parseDate(String dateStr, String pattern) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			return format.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 格式化指定格式的日期，并返回格式化后的日期对象
	 * @param date
	 * @param pattern
	 * @return
	 * @author zhangjf
	 */
	public static Date getFormateDate(Date date, String pattern) {
		return parseDate(formatDate(date, pattern), pattern);
	}
	
	/**
	 * 增加或减少（负数）指定天数。
	 * 
	 * @param date
	 * @param amount
	 * @return
	 * @author zhangjf
	 */
	public static Date addDay(Date date, int amount) {
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}
	
	/**
	 * 增加或减少（负数）指定类别<code>field</code>的日期。
	 * @param date
	 * @param field the calendar field
	 * @param amount
	 * @return
	 * @author zhangjf
	 */
	public static Date add(Date date, int field, int amount) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(field, amount);
		return instance.getTime();
	}

	/**
	 * 指定日期往前加或减相应的天数，返回指定日期
	 * @param date
	 * @return
	 */
	public static Date calcDate(Date date,int days){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, days);
		return calendar.getTime();
	}
	
	/**
	 * 去掉指定日期时间的时间，返回日期。
	 * 
	 * @param date
	 * @return
	 * @author zhangjf
	 */
	public static Date getDate(Date date) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		instance.set(Calendar.MILLISECOND, 0);
		return instance.getTime();
	}
	
	/**
	 * 获取两时间之间的间隔（天）
	 * @param startDate
 	 * @param endDate
	 * @return long
	 */
	public static long diffDays(Date startDate, Date endDate) {
		startDate = getDate(startDate);
		endDate = getDate(endDate);
		long milliSeconds = endDate.getTime() - startDate.getTime();

		return milliSeconds / MILLISENCOND_PER_DAY;
	}

	/**
	 * 得到当前时间
	 *
	 * @return 返回当前系统时间
	 */
	public static Date getCurrentDate() {
		return new Date();
	}
	
	/**
	 * 获取指定日期的前几天
	 *
	 * @param date 日期
	 * @param day  天数
	 * @return 返回指定日期的前几天日期
	 */
	public static Date getPrevDates(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -day);//第一天DAY_OF_MONTH
		return c.getTime();
	}

	/**
	 * 得到当前系统开始日期 yyyy-MM-dd HH:mm:ss
	 *
	 * @return 返回当前系统开始日期 yyyy-MM-dd 00:00:00
	 */
	public static Date getBeginDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 时间转换工具类
	 * @param date
	 * @param flag 表之
	 * @return java.lang.String
	 */
	public static String getFormattingDateTime(Date date, int flag) {
		if (date == null)
			return null;
		String format = "yyyy-MM-dd HH:mm:ss";
		switch (flag) {
			case 0:
				format = "yyyyMMdd";
				break;
			case 1:
				format = "yyMMdd";
				break;
			case 2:
				format = "HHmmss";
				break;
			case 3:
				format = "HHmmssS";
				break;
			case 4:
				format = "yyyyMMddHHmmssS";
				break;
			case 5:
				format = "yyMMddHHmmss";
				break;
			case 6:
				format = "yyyyMMddHHmmss";
				break;
			case 7:
				format = "yyyy-MM-dd";
				break;
			case 8:
				format = "MMdd";
				break;
			case 9:
				format = "yyyyMM";
				break;
			case 10:
				format = "HH:mm:ss";
				break;
			case 11:
				format = "HH:mm:ss.S";
				break;
			case 12:
				format = "yyyyMMddHHmmssSSS";
				break;
			case 13:
				format = "HHmmssSSS";
				break;
			case 14:
				format = "yyyy/MM/dd";
				break;
			case 15:
				format = "HH:mm:ss";
				break;
			case 16:
				format = "MMhhmmss";
				break;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 获取当天的时间 String
	 * @param flag
	 * @return java.lang.String
	 */
	public static String getFormattingToday(int flag) {
		return getFormattingDateTime(new Date(), flag);
	}

	/**
	 * 在当前时间增加或减少
	 * @param date
	 * @param days
	 * @return java.util.Date
	 */
	public static Date diffFromDate(Date date, long days) {
		return new Date(date.getTime() + days * 24 * 60 * 60 * 1000);
	}

	/**
	 * 判断是否本月最后一个天
	 * @return boolean
	 */
	public static boolean isLastWork(){
		boolean flag = false;
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//获得本月最后一天
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH,lastDay);
		int week = calendar.get(Calendar.DAY_OF_WEEK)-1==0?7:calendar.get(Calendar.DAY_OF_WEEK)-1;
		if(week==7){
			lastDay=lastDay-2;
		}else if(week==6){
			lastDay=lastDay-1;
		}
		if(day==lastDay){
			flag = true;
		}
		return flag;
	}

	public static Calendar getDatumTime() {
		Calendar today = Calendar.getInstance();
		today.set(1970, 0, 1);
		return today;
	}
}
