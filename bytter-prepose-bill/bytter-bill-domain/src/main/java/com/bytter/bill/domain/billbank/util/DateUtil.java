package com.bytter.bill.domain.billbank.util;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class DateUtil {
	private Calendar calendar = null;
	private int day, month, year;
	public DateUtil() {
		calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH);
		year = calendar.get(Calendar.YEAR);
	}
	public DateUtil(java.sql.Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH);
		year = calendar.get(Calendar.YEAR);
	}
	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	public void setCalendar(Date date) {
		this.calendar.setTime(date);
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		// 鐢变簬鍦–alendar涓湀鏄粠0寮�
		return month + 1;
	}
	public void setMonth(int month) {
		// 鐢变簬鍦–alendar涓湀鏄粠0寮�
		this.month = month - 1;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public Date getDate() {
		calendar.clear();
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}
	/**
	 * add 7.30 鎶奡tring鏃ユ湡杞负util鏃ユ湡
	 * 
	 * @param dateStr
	 * @param formatStr
	 * @return
	 * @throws Exception
	 */
	public static Date getDateByString(String dateStr, String formatStr)
			throws Exception {
		if (!StringUtil.isBlank(dateStr) && !StringUtil.isBlank(formatStr)) {
			SimpleDateFormat format = new SimpleDateFormat(formatStr);
			return format.parse(dateStr);
		}
		return null;
	}
	/**
	 * 闈欐�鏂规硶 鏍规嵁涓や釜鏃ユ湡姹傜浉宸ぉ鏁�姝ｆ暟琛ㄧずd1澶т簬d2澶╂暟 璐熸暟琛ㄧずd1灏忎簬d2澶╂暟 add 7.30
	 * 
	 * @author zdl
	 */
	public static int dayDiffs(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return 0;
		}
		long tt = d1.getTime() - d2.getTime();
		int days = (int) (tt / (24 * 60 * 60 * 1000));
		return days;
	}
	
	/**
	 * 闈欐�鏂规硶 鏍规嵁涓や釜鏃ユ湡姹傜浉宸ぉ鏁�+1 = 2涓棩鏈熺浉宸殑澶╂暟
	 * 
	 * @author zdl
	 */
	public static int dayDiffsAddOne(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return 0;
		}
		long tt = d1.getTime() - d2.getTime();
		int days = (int) (tt / (24 * 60 * 60 * 1000) + 1);
		return days;
	}
	/**
	 * 闈欐�鏂规硶 鏍规嵁涓や釜鏃ユ湡姹傜浉宸ぉ鏁�姝ｆ暟琛ㄧずd1澶т簬d2澶╂暟 璐熸暟琛ㄧずd1灏忎簬d2澶╂暟
	 * 
	 * @author zdl
	 */
	public static int dayDiffs(String beginDateStr, String endDateStr) {
		Date beginDate = null;
		Date endDate = null;
		try {
			if (beginDateStr.indexOf("-") != -1) {
				beginDateStr = beginDateStr.replaceAll("-", ".");
			}
			if (endDateStr.indexOf("-") != -1) {
				endDateStr = endDateStr.replaceAll("-", ".");
			}
			beginDate = getDateByString(beginDateStr, "yyyy.MM.dd");
			endDate = getDateByString(endDateStr, "yyyy.MM.dd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DateUtil.dayDiffs(endDate, beginDate);
	}
	public java.sql.Date getSqlDate() {
		return new java.sql.Date(this.getDate().getTime());
	}
	
	public static String getDateStr(java.sql.Date date) {
		return DateUtil.getDateStr(date, "yyyy-MM-dd");
	}
	public static java.sql.Date getToDate(String time) {
		java.sql.Date date = null;
		try {
			date = java.sql.Date.valueOf(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	public static String getDateStr(java.sql.Date date, String Pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(Pattern);
		return formatter.format(date);
	}
	public static Timestamp getTimeNow() {
		return new Timestamp(new Date().getTime());
	}
	// add by lixiuzhong 2004-02-18 ????????????????yymmdd
	public static String getDateStrYYMMDD(java.sql.Date date) {
		return DateUtil.getDateStr(date, "yyMMdd");
	}
	// add by zhoudailong 2006-04-28 use in elecbill
	public static String getDateStrYYYYMMDD(java.sql.Date date) {
		return DateUtil.getDateStr(date, "yyyyMMdd");
	}
	/**
	 * 鑾峰彇浼犲叆鏃堕棿鎵�湪骞寸殑绗竴澶�
	 * 
	 * @param date
	 * @return
	 */
	public static Date getYearBeginDate(Date date) {
		if (date == null) {
			return null;
		}
		Date newDate = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		newDate = new Date(calendar.getTime().getTime());
		return newDate;
	}
	/**
	 * 鏃ユ湡鐨勫鍔犳湀鏁�
	 * 
	 * @param date
	 *            鏃ユ湡
	 * @param addDay
	 *            澧炲姞鐨勬湀鏁�姝ｆ暟琛ㄧず澧炲姞锛岃礋鏁拌〃绀哄噺灏�
	 * @return Date 鏃ユ湡鐨勫鍔犳湀鏁板悗鐨勭粨鏋�
	 */
	public static Date addMonth(Date date, int addMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, addMonth);
		return new Date(calendar.getTime().getTime());
	}
	/**
	 * 鑾峰彇浼犲叆鏃堕棿鎵�湪鏈堜唤绗竴澶�
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthBeginDate(Date date) {
		if (date == null) {
			return null;
		}
		Date newDate = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		newDate = new Date(calendar.getTime().getTime());
		return newDate;
	}
	public static Timestamp getTimestamp(String date) {
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date11 = null;
		try {
			date11 = df1.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String time = df1.format(date11);
		Timestamp ts = Timestamp.valueOf(time);
		return ts;
	}
	public static java.sql.Date formatDate3(String strDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return new java.sql.Date(sdf.parse(strDate).getTime());
		} catch (ParseException e) {
			return new java.sql.Date(new Date().getTime());
		}
	}
	
	public static String formatDate(long time) {
		return formatDate(new Date(time));
	}
	
	public static String formatDate(long time, String pattern) {
		return formatDate(new Date(time), pattern);
	}
	
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}
	
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static Date parseDate(String source) {
		return parseDate(source, "yyyy-MM-dd");
	}
	
	public static Date parseDate(String source, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param str
	 *            yyyy-MM-dd
	 * @return
	 */
	public static Timestamp formatTimestampYYYY_MM_DD(String str) {
		if (str == null || str.length() == 0)
			return null;
		Date p = null;
		try {
			p = new SimpleDateFormat("yyyy-MM-dd").parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Timestamp(p.getTime());
	}
	public static Timestamp formatTimestampYYYYMMDD(String str) {
		
		if (str == null || str.length() == 0)
			return null;
		Date p = null;
		try {
			if (str.indexOf("-") > 0) {
				p = new SimpleDateFormat("yyyy-MM-dd").parse(str);
			}else{
				p = new SimpleDateFormat("yyyyMMdd").parse(str);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Timestamp(p.getTime());
	}
	/**
	 * 
	 * @param str
	 *            yyyy-MM-dd
	 * @return
	 */
	public static java.sql.Date formatDateYYYY_MM_DD(String str) {
		if (str == null || str.length() == 0)
			return null;
		Date p = null;
		try {
			p = new SimpleDateFormat("yyyy-MM-dd").parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new java.sql.Date(p.getTime());
	}
	
	public static java.sql.Date formatDateYYYY_MM_DD_T(String str) {
		if (str == null || str.length() == 0)
			return null;
		Date p = null;
		str = str.replace("T", " ");
		try {
			p = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new java.sql.Date(p.getTime());
	}
	public static java.sql.Date formatDateYYYYMMDD(String str) {
		if (str == null || str.length() == 0)
			return null;
		Date p = null;
		try {
			if (str.indexOf("-") > 0) {
				p = new SimpleDateFormat("yyyy-MM-dd").parse(str);
			}else{
				p = new SimpleDateFormat("yyyyMMdd").parse(str);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new java.sql.Date(p.getTime());
	}
	/**
	 * 鍏堝皢T杞崲鎴愮┖鏍�
	 * 
	 * @param str
	 *            yyyy-MM-ddTHH:mm:ss
	 * @return
	 */
	public static Timestamp formatTimestampT(String str) {
		if (str == null || str.length() == 0)
			return null;
		Date p = null;
		str = str.replace("T", " ");
		try {
			p = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Timestamp(p.getTime());
	}
	/**
	 * 
	 * @param str
	 *            yyyyMMddHHmmss
	 * @return
	 */
	public static Timestamp formatTimestamp(String str) {
		Date p = null;
		try {
			p = new SimpleDateFormat("yyyyMMddHHmmss").parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Timestamp(p.getTime());
	}
	public static Timestamp formatT() {
		Date d = new Date();
		String s = new SimpleDateFormat("yyyyMMddHHmmss").format(d);
		return formatTimestamp(s);
	}
	public static String formatYYYY_MM_DD(Timestamp timestamp) {
		return timestamp.toString().substring(0, 10);
	}
	
	public static String formatYYYYMMDD(Timestamp timestamp) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(timestamp);
	}
	
	public static String formatYYYYMMDD(String date) {
		if(date != null && date.trim().length() > 0) {
			return date.replaceAll("-", "");
		} else {
			return null;
		}
	}
	
	public static Date getOneYearLater(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 12);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
	
	public static Date getBeforeOneYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, -1);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
	
	public static Date getTomorrow(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
	
	public static Date getYesterday(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
}
