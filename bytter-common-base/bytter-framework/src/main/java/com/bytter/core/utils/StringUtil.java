/*
 * Description:	字符串处理工具
 * String:		2003.10.24
 * @author		张彦
 */
package com.bytter.core.utils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sun.org.apache.bcel.internal.generic.SIPUSH;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class StringUtil  implements  java.io.Serializable {
	// SQL参数的单引号处理
	public static String SQLConv(String str) {
		return str.replaceAll("'", "''");
	}
	//获取本月第一天
	public static String getMonthFirstDay() {   
		    Calendar calendar = Calendar.getInstance();   
		    calendar.set(Calendar.DAY_OF_MONTH, calendar   
		            .getActualMinimum(Calendar.DAY_OF_MONTH));   
		    return StringUtil.formatDate(calendar.getTime(),"yyyy-MM-dd");
	}   
	//获取上月最后一天 @author leiyin
	public static String getBeforeMonthfristDay(Date dt) throws Exception {   
	    Calendar calendar = Calendar.getInstance();
	    java.sql.Date date = new java.sql.Date(dt.getTime());
	    String dateStr = StringUtil.formatDate(date,"yyyy-MM-dd");
	    String yy = dateStr.substring(0,4);
	    String mm = dateStr.substring(5,7);
	    String dd = "01";
	    if(mm.equals("01")){
	    	yy=String.valueOf(Integer.parseInt(yy)-1);
	    	mm="12";
	    }else{
	    	int i = Integer.parseInt(mm)-1;
	    	if(String.valueOf(i).length()<2){
	    		mm="0"+String.valueOf(i);
	    	}else {
	    		mm=String.valueOf(i);
			}
	    }
	    dateStr = yy+"-"+mm+"-"+dd; 
	    calendar.setTime(StringUtil.formatDate(dateStr));
	    calendar.set(Calendar.DAY_OF_MONTH, calendar   
	            .getActualMinimum(Calendar.DAY_OF_MONTH));
	    Date d = calendar.getTime();
	    return StringUtil.formatDate(calendar.getTime(),"yyyy-MM-dd");
	}
	//获取本月最后一天
	public static String getMonthLastDay() {   
		    Calendar calendar = Calendar.getInstance();  
		    calendar.set(Calendar.DAY_OF_MONTH, calendar   
		            .getActualMaximum(Calendar.DAY_OF_MONTH));   
		    return StringUtil.formatDate(calendar.getTime(),"yyyy.MM.dd");
	}   
	//获取上月最后一天 @author leiyin
	public static String getBeforeMonthLastDay() throws Exception {   
	    Calendar calendar = Calendar.getInstance();
	    java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
	    String dateStr = StringUtil.formatDate(date,"yyyy-MM-dd");
	    String yy = dateStr.substring(0,4);
	    String mm = dateStr.substring(5,7);
	    String dd = "01";
	    if(mm.equals("01")){
	    	yy=String.valueOf(Integer.parseInt(yy)-1);
	    	mm="12";
	    }else{
	    	int i = Integer.parseInt(mm)-1;
	    	if(String.valueOf(i).length()<2){
	    		mm="0"+String.valueOf(i);
	    	}else {
	    		mm=String.valueOf(i);
			}
	    }
	    dateStr = yy+"-"+mm+"-"+dd; 
	    calendar.setTime(StringUtil.formatDate(dateStr));
	    calendar.set(Calendar.DAY_OF_MONTH, calendar   
	            .getActualMaximum(Calendar.DAY_OF_MONTH));
	    Date d = calendar.getTime();
	    return StringUtil.formatDate(calendar.getTime(),"yyyy.MM.dd");
	}
	//获取上月最后一天 @author leiyin
	public static String getBeforeMonthLastDay(Date dt) throws Exception {   
	    Calendar calendar = Calendar.getInstance();
	    java.sql.Date date = new java.sql.Date(dt.getTime());
	    String dateStr = StringUtil.formatDate(date,"yyyy-MM-dd");
	    String yy = dateStr.substring(0,4);
	    String mm = dateStr.substring(5,7);
	    String dd = "01";
	    if(mm.equals("01")){
	    	yy=String.valueOf(Integer.parseInt(yy)-1);
	    	mm="12";
	    }else{
	    	int i = Integer.parseInt(mm)-1;
	    	if(String.valueOf(i).length()<2){
	    		mm="0"+String.valueOf(i);
	    	}else {
	    		mm=String.valueOf(i);
			}
	    }
	    dateStr = yy+"-"+mm+"-"+dd; 
	    calendar.setTime(StringUtil.formatDate(dateStr));
	    calendar.set(Calendar.DAY_OF_MONTH, calendar   
	            .getActualMaximum(Calendar.DAY_OF_MONTH));
	    Date d = calendar.getTime();
	    return StringUtil.formatDate(calendar.getTime(),"yyyy-MM-dd");
	}
	public static boolean compareTo(double a,double b){
		BigDecimal   data1   =   new   BigDecimal(a).setScale(2,4); 
		BigDecimal   data2   =   new   BigDecimal(b).setScale(2,4); 
		int k=data1.compareTo(data2);
		if(k>0){
			return true;
		}else{
			return false;
		}
	}
	
	// 把带html标签的字符串格式化
	public static String formatHtml(String str) {
		if (str == null)
			return "";
		String temp = str;
		temp = temp.replaceAll("<br>", "\n");
		temp = temp.replaceAll("&nbsp;&nbsp;", "   ");
		temp = temp.replaceAll("&lt;", "<");
		temp = temp.replaceAll("&gt;", ">");
		return temp;
	}
	// 把带html标签的字符串格式化
	public static String formatStrToHtml(String str) {
		if (str == null)
			return "";
		String temp = str;
		temp = temp.replaceAll("\n", "<br>");
		temp = temp.replaceAll("   ", "&nbsp;&nbsp;");
		temp = temp.replaceAll("<", "&lt;");
		temp = temp.replaceAll(">", "&gt;");
		return temp;
	}
	
	/**
	 * 把带去掉字符里面的html代码
	 * @author chenchy
	 */
	public static String formatHtmltoStr(String htmlcontent){
		htmlcontent = htmlcontent.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
		htmlcontent = htmlcontent.replaceAll("\\s*|\t|\r|\n", "");//去除字符串中的空格,回车,换行符,制表符
		return htmlcontent;
	}
	// 把null格式化成""
	public static String formatNull(String str) {
		if (str == null || "null".equals(str))
			return "";
		else
			return str;
	}
	// 判断一个字符串是否null或""
	public static boolean isBlank(String str) {
		if (str == null)
			return true;
		if (str.length() == 0)
			return true;
		return false;
	}
	// 判断一个字符串是否null或""或"null"
	public static boolean isBlank_new(String str) { // add by pengjian 2008-6-11
													// 14:34:57
		if (str == null)
			return true;
		if (str.length() == 0)
			return true;
		if ("null".equals(str))
			return true;
		return false;
	}
	/**
	 * 将日期转化为指定格式字符串
	 */
	public static String formatDate(java.util.Date date, String pattern) {
		if (date == null) {
			return "";
		}
		return (new SimpleDateFormat(pattern)).format(date);
	}
	/**
	 * 功能: 将日期转换为yyyy.MM.dd型字符串<br>
	 */
	public static String formatDate(java.util.Date date) {
		if (date == null)
			return "";
		return (new SimpleDateFormat("yyyy.MM.dd")).format(date);
	}
	/**
	 * 功能: 将日期转换为yyyy-MM-dd型字符串<br>
	 */
	public static String formatDate4(java.util.Date date) {
		if (date == null)
			return "";
		return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
	}
	// 日期转字符串
	/**
	 * 日期转字符串
	 * @param date
	 * @param pattern   yyyy年MM月dd日格式
	 * @return
	 */
	public static String formatDate3(java.util.Date date) {
		return (new SimpleDateFormat("yyyy年MM月dd日")).format(date);
	}
	// 数字转字符串
	public static String formatNumeric(double numeric, String pattern) {
		if (numeric == -0)
			numeric = 0;
		DecimalFormat decFormat = new DecimalFormat(pattern);
		return decFormat.format(numeric);
	}
	// 数字转逗号分隔字符串
	public static String formatNumeric(double numeric) {
		return formatNumeric(numeric, "#,##0.00");
	}
	//汇率
	public static String formatNumeric8(double numeric) {
		return formatNumeric(numeric, "#,##0.00000000");
	}
	// 数字转逗号分隔字符串,附加小数位数(保留8位小数，那么dec参数为6，即，最少要有2位小数)
	public static String formatNumeric(double numeric, int dec) {
		String p = "";
		for (int i = 0; i < dec; i++)
			p += "#";
		return formatNumeric(numeric, "#,##0.00" + p);
	}
	// 数字转逗号分隔字符串；如果数字为零，则返回空
	public static String formatNumericEx(double numeric) {
		String result;
		if (numeric != 0) {
			result = StringUtil.formatNumeric(numeric);
		} else {
			result = "0.00";
		}
		return result;
	}
	/**
	 * 日期字符串格式化为java.sql.Date，日期字符串需包含年月日，例如2011-1-1或者2011.1.1
	 */
	public static java.sql.Date formatDate(String dateStr) {
		java.sql.Date d = null;
		if (dateStr == null || dateStr.equals(""))
			d = null;
		try {
			String[] tmp = null;
			if(dateStr.indexOf("-") != -1){
				tmp = dateStr.split("-");
			} else {
				tmp = dateStr.split("\\u002E");
			}
			if (tmp.length != 3) {
				return null;
			} else {
				if(tmp[1].length() == 1){
					tmp[1] = "0" + tmp[1];
				}
				if(tmp[2].length() == 1){
					tmp[2] = "0" + tmp[2];
				}
				d = java.sql.Date.valueOf(tmp[0] + "-" + tmp[1] + "-" + tmp[2]);
			} 
		} catch (Exception e) {
			d = null;
		}
		return d;
	}
	// 将大数字格式化为字符串，避免以科学计数法显示
	public static String formatDouble(double numeric) {
		return formatNumeric(numeric, "#0.00");
	}
	// 将大数字格式化为字符串，避免以科学计数法显示5位
	public static String formatDoubles(double numeric) {
		return formatNumeric(numeric, "#0.00000");
	}
	public static String formatDoubleWithSix(double numeric) {
		return formatNumeric(numeric, "#0.000000");
	}
	public static String getDateStrYYYYMMDD(java.sql.Date date) {
		return DateUtil.getDateStr(date, "yyyyMMdd");
	}
	public static String getDateStrYYYYMMDD(java.util.Date date) {
		return DateUtil.getDateStr(new java.sql.Date(date.getTime()),
				"yyyyMMdd");
	}
	public static java.sql.Date getNow() {
		return new java.sql.Date(new java.util.Date().getTime());
	}
	// 将大数字格式化为字符串，避免以科学计数法显示
	public static String formatDouble(double numeric, int dec) {
		String p = "";
		for (int i = 0; i < dec; i++)
			p += "#";
		return formatNumeric(numeric, "#0.00" + p);
	}
	// 将形如234，567.00的逗号分隔字符串格式化为Double
	public static double formatNumeric(String str) {
		try {
			return (new DecimalFormat("#,##0.00######")).parse(str)
					.doubleValue();
		} catch (Exception e) {
			return 0;
		}
	}
	//汇率
	public static double formatNumeric8(String str) {
		try {
			return (new DecimalFormat("#,##0.00000000")).parse(str)
					.doubleValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	// 格式化java.sql.Date为句点分隔字符串
	public static String formatDate(java.sql.Date date) {
	    if (date == null){
            return "";
	    }
		return (new SimpleDateFormat("yyyy.MM.dd")).format(date);
	}
	// 指定的格式格式date add by luoyh 2009-319
	public static String fromatDate(java.sql.Date date, String pattern) {
		return (new SimpleDateFormat(pattern)).format(date);
	}
	// add by xy090118
	public static String formatDateYYYYMD(java.sql.Date date) {
		return (new SimpleDateFormat("yyyy.M.d")).format(date);
	}
	/**
	 * 功能: 将日期转换为yyyy-MM-dd型字符串<br>
	 */
	public static String formatDate2(java.sql.Date date) {
		return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
	}
	// 格式化java.sql.Timestamp为句点分隔字符串
	public static String formatLongDate(java.sql.Timestamp date) {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
	}
	public static String formatLongDateHHMMSS(java.sql.Timestamp date) {
		return (new SimpleDateFormat("HH:mm:ss")).format(date);
	}
	//add by luxy date = new Date()
	public static String formatLongDateLocal(java.sql.Date date) {
		return (new SimpleDateFormat("yyyy年MM月dd日HH点")).format(date);
	}
	public static String formatLongDate1(java.sql.Timestamp date) {
		return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
	}
	public static SimpleDateFormat getFormat(String format) {
		String timeArea = "GMT+08:00";// 时区变量
		SimpleDateFormat formatter = new SimpleDateFormat(format);// 设置时间格式
		formatter.setTimeZone(TimeZone.getTimeZone(timeArea));// 设置时区为东八区
		return formatter;
	}
	public static java.sql.Timestamp formatTimestamp(String dateStr) {
		try {
			return new java.sql.Timestamp(getFormat("yyyy-MM-dd HH:mm:ss")
					.parse(dateStr.trim()).getTime());// (dateStr.substring(0,10)
			// + "
			// "
			// + dateStr.substring(11,19));
		} catch (Exception e) {
			return null;
		}
	}
	// 格式化字符 为int
	public static int formatInt(String temp, int defaultNum) {
		if (temp != null && !temp.equals("")) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}
	// 取得当天的日期（sql格式）
	public static java.util.Date getToday() {
		java.util.Date date = new java.util.Date();
		return new java.sql.Date(date.getTime());
	}
	// 取得当天的日期（sql格式）
	public static java.sql.Date getSQLToday() {
		java.util.Date date = new java.util.Date();
		return new java.sql.Date(date.getTime());
	}
	// 把BigDecimal 转化成double
	public static double getDouble(BigDecimal o1) {
		if (o1 == null)
			return 0;
		else
			return o1.doubleValue();
	}
	public static Timestamp getSqlToday() {
		java.util.Date date = new java.util.Date();
		Timestamp date2 = new Timestamp((date).getTime());
		return date2;
	}
	
	public static String ChgNumMoney(String numMoney) {
		String[] chinese = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		int slen = numMoney.length();
		String result = "";
		for (int i = 0; i < slen; i++) {
			int numInt = Integer.parseInt(numMoney.substring(i, i + 1));
			result += chinese[numInt];
		}
		return result;
	}
	public static String dayYMD2chinese(String num) {
		String[] chinese = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		int slen = num.length();
		String result = "";
		if (slen > 2) {
			for (int i = 0; i < slen; i++) {
				int numInt = Integer.parseInt(num.substring(i, i + 1));
				result += chinese[numInt];
			}
		} else {
			int numInt = Integer.parseInt(num);
			if (numInt < 10) {
				result = "零" + chinese[numInt];
			} else {
				for (int i = 0; i < slen; i++) {
					int num1 = Integer.parseInt(num.substring(i, i + 1));
					if (i == 0) {
						result += chinese[num1] + "拾";
					} else if(num1!=0) {
						result += chinese[num1];
					}
				}
			}
		}
		return result;
	}
	// add by lixiuzhong 将小写金额转成大写金额
	// update by liupeng 修改负数时大写金额显示错误
	public static String ChgMoney(double smallmoney) {
		String value = String.valueOf(smallmoney);
		if (null == value || "".equals(value.trim()))
			return "零";
		// String strCheck,strArr,strFen,strDW,strNum,strBig,strNow;
		String strCheck, strFen, strDW, strNum, strBig, strNow;
		strCheck = value + ".";
		int dot = strCheck.indexOf(".");
		if (dot > 12) {
			return "数据" + value + "过大，无法处理！";
		}
		try {
			int i = 0;
			strBig = "";
			strDW = "";
			strNum = "";
			int flag=1;
			if(smallmoney<0)
			{
				smallmoney=smallmoney*-1;
				flag=-1;
			}
			double dFen = smallmoney * 100;
			// strFen = String.valueOf(intFen);
			strFen = formatNumeric(dFen, "###0");
			int lenIntFen = strFen.length();
			while (lenIntFen != 0) {
				i++;
				switch (i) {
				case 1:
					strDW = "分";
					break;
				case 2:
					strDW = "角";
					break;
				case 3:
					strDW = "元";
					break;
				case 4:
					strDW = "拾";
					break;
				case 5:
					strDW = "佰";
					break;
				case 6:
					strDW = "仟";
					break;
				case 7:
					strDW = "万";
					break;
				case 8:
					strDW = "拾";
					break;
				case 9:
					strDW = "佰";
					break;
				case 10:
					strDW = "仟";
					break;
				case 11:
					strDW = "亿";
					break;
				case 12:
					strDW = "拾";
					break;
				case 13:
					strDW = "佰";
					break;
				case 14:
					strDW = "仟";
					break;
				}
				switch (strFen.charAt(lenIntFen - 1)) // 选择数字
				{
				case '1':
					strNum = "壹";
					break;
				case '2':
					strNum = "贰";
					break;
				case '3':
					strNum = "叁";
					break;
				case '4':
					strNum = "肆";
					break;
				case '5':
					strNum = "伍";
					break;
				case '6':
					strNum = "陆";
					break;
				case '7':
					strNum = "柒";
					break;
				case '8':
					strNum = "捌";
					break;
				case '9':
					strNum = "玖";
					break;
				case '0':
					strNum = "零";
					break;
				}
				// 处理特殊情况
				strNow = strBig;
				// 分为零时的情况
				if ((i == 1) && (strFen.charAt(lenIntFen - 1) == '0'))
					strBig = "整";
				// 角为零时的情况
				else if ((i == 2) && (strFen.charAt(lenIntFen - 1) == '0')) { // 角分同时为零时的情况
					if (!strBig.equals("整"))
						strBig = "零" + strBig;
				}
				// 元为零的情况
				else if ((i == 3) && (strFen.charAt(lenIntFen - 1) == '0'))
					strBig = "元" + strBig;
				// 拾－仟中一位为零且其前一位（元以上）不为零的情况时补零
				else if ((i < 7) && (i > 3)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) != '零')
						&& (strNow.charAt(0) != '元'))
					strBig = "零" + strBig;
				// 拾－仟中一位为零且其前一位（元以上）也为零的情况时跨过
				else if ((i < 7) && (i > 3)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '零')) {
				}
				// 拾－仟中一位为零且其前一位是元且为零的情况时跨过
				else if ((i < 7) && (i > 3)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '元')) {
				}
				// 当万为零时必须补上万字
				else if ((i == 7) && (strFen.charAt(lenIntFen - 1) == '0'))
					strBig = "万" + strBig;
				// 拾万－仟万中一位为零且其前一位（万以上）不为零的情况时补零
				else if ((i < 11) && (i > 7)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) != '零')
						&& (strNow.charAt(0) != '万'))
					strBig = "零" + strBig;
				// 拾万－仟万中一位为零且其前一位（万以上）也为零的情况时跨过
				else if ((i < 11) && (i > 7)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '万')) {
				}
				// 拾万－仟万中一位为零且其前一位为万位且为零的情况时跨过
				else if ((i < 11) && (i > 7)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '零')) {
				}
				// 万位为零且存在仟位和十万以上时，在万仟间补零
				else if ((i < 11) && (i > 8)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '万')
						&& (strNow.charAt(2) == '仟'))
					strBig = strNum + strDW + "万零"
							+ strBig.substring(1, strBig.length());
				// 单独处理亿位
				else if (i == 11) {
					// 亿位为零且万全为零存在仟位时，去掉万补为零
					if ((strFen.charAt(lenIntFen - 1) == '0')
							&& (strNow.charAt(0) == '万')
							&& (strNow.charAt(2) == '仟'))
						strBig = "亿" + "零"
								+ strBig.substring(1, strBig.length());
					// 亿位为零且万全为零不存在仟位时，去掉万
					else if ((strFen.charAt(lenIntFen - 1) == '0')
							&& (strNow.charAt(0) == '万')
							&& (strNow.charAt(2) != '仟'))
						strBig = "亿" + strBig.substring(1, strBig.length());
					// 亿位不为零且万全为零存在仟位时，去掉万补为零
					else if ((strNow.charAt(0) == '万')
							&& (strNow.charAt(2) == '仟'))
						strBig = strNum + strDW + "零"
								+ strBig.substring(1, strBig.length());
					// 亿位不为零且万全为零不存在仟位时，去掉万
					else if ((strNow.charAt(0) == '万')
							&& (strNow.charAt(2) != '仟'))
						strBig = strNum + strDW
								+ strBig.substring(1, strBig.length());
					// 其他正常情况
					else
						strBig = strNum + strDW + strBig;
				}
				// 拾亿－仟亿中一位为零且其前一位（亿以上）不为零的情况时补零
				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) != '零')
						&& (strNow.charAt(0) != '亿'))
					strBig = "零" + strBig;
				// 拾亿－仟亿中一位为零且其前一位（亿以上）也为零的情况时跨过
				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '亿')) {
				}
				// 拾亿－仟亿中一位为零且其前一位为亿位且为零的情况时跨过
				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (strNow.charAt(0) == '零')) {
				}
				// 亿位为零且不存在仟万位和十亿以上时去掉上次写入的零
				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) != '0')
						&& (strNow.charAt(0) == '零')
						&& (strNow.charAt(1) == '亿')
						&& (strNow.charAt(3) != '仟'))
					strBig = strNum + strDW
							+ strBig.substring(1, strBig.length());
				// 亿位为零且存在仟万位和十亿以上时，在亿仟万间补零
				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) != '0')
						&& (strNow.charAt(0) == '零')
						&& (strNow.charAt(1) == '亿')
						&& (strNow.charAt(3) == '仟'))
					strBig = strNum + strDW + "亿零"
							+ strBig.substring(2, strBig.length());
				else
					strBig = strNum + strDW + strBig;
				strFen = strFen.substring(0, lenIntFen - 1);
				lenIntFen--;
			}
			// if(flag<0)strBig="负"+strBig;
			return strBig;
		} catch (Exception e) {
			return "";
		}
	}
	// add by 李秀忠 转换编码 解决请求，响应中的乱码问题
	public static String getEncodeStr(String str) {
		if (str == null) {
			return null;
		}
		try {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes("ISO-8859-1");
			String temp = new String(temp_t);
			return temp;
		} catch (Exception e) {
		}
		return null;
	}
	// add by gwb 编码简单判断 解决是否需编码转换
	public static boolean validateEncode(String str1, String str2) {
		boolean bsuccess = false;
		try {
			if (str1.equals(str2)) {
				bsuccess = false;
			} else {
				bsuccess = true;
			}
		} catch (Exception e) {
		}
		return bsuccess;
	}
	// 格式化java.sql.Timestamp为句点分隔字符串
	public static String formatTimestamp(java.sql.Timestamp date) {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(date);
	}
	public static String formatSpace(String str) {
		if (str == null)
			return "";
		return str.replaceAll(" ", "&nbsp;");
	}
	// 数字转逗号分隔字符串；如果 blankZero = true 那么 0 返回空字符串
	public static String formatNumeric(double numeric, boolean blankZero) {
		if (blankZero && numeric == 0)
			return "";
		return formatNumeric(numeric, "#,##0.00");
	}
	public static String formatNumeric(double numeric, boolean blankZero,
			String mask) {
		if (blankZero && numeric == 0)
			return "";
		return formatNumeric(numeric, mask);
	}
	// 删除字符串最后一个字符
	public static String deleteLastChar(String str) {
		if (str == null)
			return null;
		if (str.length() == 0)
			return "";
		return str.substring(0, str.length() - 1);
	}
	/**
	 * 将字符串按分隔符分成字符串数组（去除空字符串）
	 * 
	 * @param str
	 *            字符串
	 * @param format
	 *            分隔符
	 * @return 字符串数组
	 */
	public static String[] split(String str, String format) {
		if (str == null || "".equals(str)) {
			return new String[0];
		}
		String[] strs = str.split(format);
		Collection col = new ArrayList();
		for (int i = 0; i < strs.length; i++) {
			if (strs[i] != null && !"".equals(strs[i])) {
				col.add(strs[i]);
			}
		}
		strs = (String[]) col.toArray(new String[0]);
		return strs;
	}
	/**
	 * 将字符串按分隔符分成Integer数组（去除空字符串）
	 * 
	 * @param str
	 *            字符串
	 * @param format
	 *            分隔符
	 * @return 字符串数组
	 */
	public static Long[] splitLong(String str, String format) {
		if (isBlank_new(str)) {
			return new Long[0];
		}
		String[] strs = str.split(format);
		List<Long> col = new ArrayList<Long>();
		for (int i = 0; i < strs.length; i++) {
			if (!isBlank_new(strs[i])) {
				col.add(Long.parseLong(strs[i]));
			}
		}
		return (Long[]) col.toArray(new Long[0]);
	}
	/**
	 * 将excel传过来的金额（￥333,333）转换成double数字。
	 * 
	 * @param str
	 * @return
	 */
	public static double convertToDouble(String str) {
		str = str.substring(3);
		String[] temp = str.split(",");
		String result = "";
		int size = temp.length;
		for (int i = 0; i < size; i++) {
			result = result + temp[i];
		}
		return Double.parseDouble(result);
	}
	public static void main(String args[]) {
//		double t = StringUtil.convertToDouble("\"$\"33,33,43.00");
//		System.out.print(t);
	   	System.out.println(StringUtil.getZhNumber("3"));
	}
	/**
	 * 用于取得SQL语句中的IN部分 Add by Wang Xinping, 2006-1-11
	 * 
	 * @param vCorp
	 *            一般是操作员所管理的单位列表
	 * @param sField
	 *            字段名 如: " and p.corp_id "
	 * @return
	 */
	public static String getInStr(Vector vCorp, String sField) {
		int size = vCorp.size();
		if (size == 0)
			return "";
		StringBuffer sb = new StringBuffer(sField + " in(");
		for (int i = 0; i < size; i++) {
			sb.append((Integer) vCorp.get(i));
			if (i < size - 1)
				sb.append(",");
		}
		sb.append(") ");
		return sb.toString();
	}
	// add by mbz 转换编码 解决请求，响应中的乱码问题
	public static String getEncodeStr2(String str) {
		if (str == null) {
			return null;
		}
		try {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes("UTF-8");
			String temp = new String(temp_t);
			return temp;
		} catch (Exception e) {
		}
		return null;
	}
	// add by mbz 转换编码 解决请求，响应中的乱码问题
	public static String getEncodeStr(String str,String codeFormart) {
		if (str == null) {
			return null;
		}
		try {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes("ISO-8859-1");
			String temp = new String(temp_t,codeFormart);
			return temp;
		} catch (Exception e) {
		}
		return null;
	}
	// 将date的前一天返回
	public static java.sql.Date formatDate3(java.sql.Date date) {
		DateUtil dateUtil = new DateUtil(date);
		dateUtil.setDay(dateUtil.getDay() - 1);
		date = dateUtil.getSqlDate();
		return date;
	}
	// 将形如2000.01.30的点号分隔的日期字符串格式化为java.sql.Date
	public static java.sql.Date formatDate2(String dateStr) {
		java.util.Date date;
		try {
			date = new java.util.Date(dateStr);
		} catch (Exception e) {
			date = null;
		}
		try {
			if (date == null) {
				date = StringUtil.formatDate(dateStr.replace('.', '-'));
			}
		} catch (Exception e) {
			throw new java.lang.IllegalArgumentException();
		}
		return new java.sql.Date(date.getTime());
	}
	public static String formatDateStr(java.sql.Date date) {
		if (date == null) {
			return "";
		}
		return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
	}
	// 数字转逗号分隔字符串
	public static String formatMoney(double numeric) {
		return formatNumeric(numeric, "#,##0.00");
	}
	public static java.util.Date stringToDate(String date, String pattern) {
		try {
			return (new SimpleDateFormat(pattern)).parse(date);
		} catch (ParseException e) {
			e.printStackTrace(); // To change body of catch statement use
									// File | Settings | File Templates.
			return null;
		}
	}
	public static boolean isNumeric(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	// 将形如234，567.00的逗号分隔字符串格式化为Double add by liut 返回如果是0的时候则返回null
	public static Double formatNumericEX(String str) {
		Double result = null;
		try {
			result = (new DecimalFormat("#,##0.00######")).parse(str)
					.doubleValue();
		} catch (Exception e) {
			return null;
		}
		return result == 0 ? null : result;
	}
	public static String formatDateEx(Date date) {
		if (date == null)
			return "";// add 8.11
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
	}
	/**
	 * 把String类型的转换成为数组，根据;来分割
	 * 
	 * @param str
	 * @return
	 */
	public static String[] StringToArray(String str) {
		if (StringUtil.isBlank_new(str)) {
			return new String[0];
		}
		str = str.trim();
		str = str.replace("；", ";");
		if (str.length() > 0 && !str.endsWith(";")) {
			return str.split(";");
		} else {
			return new String[0];
		}
	}
	public static java.util.Date getCurDate() {
		java.util.Date date = new java.util.Date();
		String strDate = getDateStrYYYYMMDD(date);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		try {
			return sf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 输出指定长度的字符创，长度不够前面补0
	 * 
	 * @param no
	 *            原来的字符创
	 * @param length
	 *            指定的长度
	 * @return
	 */
	public static String formatString(String no, int length) {
		String result = no;
		if (no != null) {
			if (no.length() > length) {
				return "";
			} else {
				for (int i = 0; i < length - no.length(); i++) {
					result = "0" + result;
				}
				return result;
			}
		}
		return null;
	}
	public static String formatDateYYYYMMDDHHMMSS(java.sql.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 解决地址栏传中文参数的问题，先进行转码
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeChinese2(String str) {
		if (str == null || "".equals(str)) {
			return "";
		}
		BASE64Encoder encoder = new BASE64Encoder();
		String encodeStr = encoder.encode(str.getBytes());
		// encodeStr=encodeStr.replaceAll("\\%", "%25");
		// encodeStr=encodeStr.replaceAll("\\#", "%23");
		// encodeStr=encodeStr.replaceAll("\\&", "%26");
		// encodeStr=encodeStr.replaceAll("\\+", "%2B");
		// encodeStr=encodeStr.replaceAll(" ", "%20");
		// encodeStr=encodeStr.replaceAll("/", "%2F");
		// encodeStr=encodeStr.replaceAll("？", "%3F");
		// encodeStr=encodeStr.replaceAll("=", "%3D");
		return encodeStr;
	}
	/**
	 * 解决地址栏传中文参数的问题，解码返回编码前的汉字
	 * 
	 * @param str
	 * @return
	 */
	public static String decodeChinese2(String str) {
		if (str == null || "".equals(str)) {
			return "";
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return new String(decoder.decodeBuffer(str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 解决地址栏传中文参数的问题，先进行转码
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeChinese(String str) {
		if (str == null || "".equals(str)) {
			return "";
		}
		String encodeStr = "";
		try {
			encodeStr = java.net.URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encodeStr;
	}
	/**
	 * 解决地址栏传中文参数的问题，解码返回编码前的汉字
	 * 
	 * @param str
	 * @return
	 */
	public static String decodeChinese(String str) {
		if (str == null || "".equals(str)) {
			return "";
		}
		String decodeStr = "";
		try {
			decodeStr = java.net.URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decodeStr;
	}
	
	public static boolean isNumber(String str)
	{
	Pattern pattern = Pattern.compile("[0-9]*");
	Matcher isNum = pattern.matcher(str);
	if( !isNum.matches() )
	{
	return false;
	}
	return true;
	} 
     // add by liupeng 把阿拉伯数字转换成中文数字
	 public static String getZhNumber(String num)
		{
		
		  String strNum="";
		  String[] A = {"一","二","三","四","五","六","七","八","九"
			};
		  String [] B={"十","百","千","万"};
		  String temp="";
		  int tempInt=0;
		  for(int i=0;i<num.length();i++)
		  {
			  temp=num.substring(i,i+1);
			  tempInt=Integer.parseInt(temp);
			  if(num.length()-i-2>=0&&num.length()>2)
			  strNum=strNum+A[tempInt-1]+B[num.length()-i-2];
			  else if(num.length()-i-2>=0) strNum=strNum+B[num.length()-i-2];
			  else if(tempInt>0) strNum=strNum+A[tempInt-1];
			//  else strNum=strNum+B[num.length()-i-2]+A[tempInt-1];
		  }
		  return strNum;
		}
	 
	 public static String formatDateCN(Date date){
		  GregorianCalendar   ca   =   new   GregorianCalendar(); 
		  System.out.println(ca.get(GregorianCalendar.AM_PM));
		  int i = ca.get(GregorianCalendar.AM_PM);
		  return (new SimpleDateFormat("yyyy年M月dd日"+(i==0?"上午":"下午")+"H点")).format(date);
	 }
	 
	 public static String trim(String str){
		 if(isBlank(str)){
			 return str;
		 }else{
			 return str.trim();
		 }
	 }
   //获取传入月最后一天
	public static String getMonthLastDay(Date date) {   
		    Calendar calendar = Calendar.getInstance();  
		    calendar.setTime(date);
		    calendar.set(Calendar.DAY_OF_MONTH, calendar   
		            .getActualMaximum(Calendar.DAY_OF_MONTH));   
		    return StringUtil.formatDate(calendar.getTime(),"yyyy-MM-dd");
	} 
	//获取传入月第一天
	public static String getMonthFirstDay(Date date) {   
		    Calendar calendar = Calendar.getInstance();   
		    calendar.setTime(date);
		    calendar.set(Calendar.DAY_OF_MONTH, calendar   
		            .getActualMinimum(Calendar.DAY_OF_MONTH));   
		    return StringUtil.formatDate(calendar.getTime(),"yyyy-MM-dd");
	}  
	
    /**
     * 数组按照format拼接成字符串返回
     * @param arr
     * @param format
     * @return
     */
    public static String join(Object[] arr,String format){
        StringBuffer strs=new StringBuffer("");
        if(arr!=null){
            for(Object obj:arr){
                String str=String.valueOf(obj);
                if(strs.length()==0){
                    strs.append(str);
                }else{
                    strs.append(format).append(str);
                }
            }
        }
        return strs.toString();
    }
    /**
     * 作者：黄茜
     * 时间：2014年11月25日
     * 功能：去除空格（回车、水平制表符、空格、换行）
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
        	/*注：\n 回车(\u000a) 
            	\t 水平制表符(\u0009) 
            	\s 空格(\u0008) 
            	\r 换行(\u000d)*/
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    
    /**
     * 解析辅助信息
     * @param auxi
     * @param joinStr
     * @return
     */
    public static String auxiFomt(String auxi,String joinStr){
        String reAuxis="";
        if(!isBlank_new(auxi)){
            String[] auxis=split(auxi, ";");
            for(String auxiInfo:auxis){
                String[] auxiInfos=split(auxiInfo, "#");
                String auxiType=auxiInfos[0];
//                部门档案=depart
//                客商辅助核算=merchants
//                人员档案=person
//                现金流量项目=proCash
//                项目档案=proFile
//                银行账号=bankacc
//                银行档案=bank
                if("depart".equals(auxiType)){
                    auxiType="部门档案";
                }else if("merchants".equals(auxiType)){
                    auxiType="客商辅助核算";
                }else if("person".equals(auxiType)){
                    auxiType="人员档案";
                }else if("proCash".equals(auxiType)){
                    auxiType="现金流量项目";
                }else if("proFile".equals(auxiType)){
                    auxiType="项目档案";
                }else if("bankacc".equals(auxiType)){
                    auxiType="银行账号";
                }else if("bank".equals(auxiType)){
                    auxiType="银行档案";
                }
                String auxiCode=auxiInfos[1];
                String auxiName=auxiInfos[2];
                if(!isBlank_new(reAuxis)){
                    reAuxis=reAuxis+joinStr;
                }
                reAuxis+=auxiType+"：("+auxiCode+")"+auxiName;
            }
        }
        return reAuxis;
    }
    
    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
            .append(Character.toTitleCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
    
    
    /**
     * 判断对象是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
    	boolean flag = false;
    	if (str != null && !str.equals("")) {
    		if (str.toString().trim().length() > 0) {
    			flag = true;
    		}
    	} else {
    		flag = false;
    	}
    	return flag;
    }
    /**
     * 判断对象是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str) {
        return !isNotEmpty(str);
    }
    /**
     * The empty String <code>""</code>.
     * @since 2.0
     */
    public static final String EMPTY = "";
    
    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }
	/**
	 * 获取消息ID
	 * @return
	 */
	public static String getMsgId() {
		return getMsgId(4);
	}
	/**
	 * 获取消息ID
	 * @return
	 */
	public static String getMsgId(int length) {
//		return new java.util.Date().getTime()+String.valueOf((int)((Math.random()*9+1)*1000));
		return new java.util.Date().getTime()+String.valueOf(Math.random()*9).substring(2, length+2);
	}
    
}
