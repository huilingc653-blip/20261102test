package com.bytter.core.utils;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.springframework.util.StringUtils;
import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 类描述：时间操作定义类
 *
 * @author zuoy
 * @since 3.0
 */
public class DateUtils extends PropertyEditorSupport {
    // 各种时间格式 以下所有常量会导致多线程并发问题 后续不要使用
    @Deprecated
    public static final SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Deprecated
    public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    @Deprecated
    public static final SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");
    @Deprecated
    public static final SimpleDateFormat time_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Deprecated
    public static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    @Deprecated
    public static final SimpleDateFormat short_time_sdf = new SimpleDateFormat("HH:mm");
    @Deprecated
    public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 以毫秒表示的时间
    private static final long DAY_IN_MILLIS = 24 * 3600 * 1000;
    private static final long HOUR_IN_MILLIS = 3600 * 1000;
    private static final long MINUTE_IN_MILLIS = 60 * 1000;
    private static final long SECOND_IN_MILLIS = 1000;
    // Classical style
    public static final String PATTERN_CLASSICAL = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_CLASSICAL_NORMAL = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_CLASSICAL_SIMPLE = "yyyy-MM-dd";
    public static final String PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String PATTERN_SHORTTIME = "HH:mm";
    public static final String PATTERN_SHORTTIMESS = "HH:mm:ss";

    public static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
    // CH style
    public static final String PATTERN_CH = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String PATTERN_CH_NORMAL = "yyyy年MM月dd日 HH时mm分";
    public static final String PATTERN_CH_SIMPLE = "yyyy年MM月dd日";
    public static final String PATTERN_CH_YYYYMM = "yyyy年MM月";
    public static final String PATTERN_CH_YEAR = "yyyy年";
    // 指定模式的时间格式 yyyy-MM-dd HH:mm:ss
    public static SimpleDateFormat getSDFormat() {
        return datetimeFormat();
    }
    // 指定模式的时间格式yyyyMMdd
    public static SimpleDateFormat yyyyMMdd() {
        return getSDFormat(PATTERN_YYYYMMDD);
    }
    // 指定模式的时间格式yyyyMMdd
    public static SimpleDateFormat yyyymmddhhmmss() {
        return getSDFormat(PATTERN_YYYYMMDDHHMMSS);
    }
    // 指定模式的时间格式HH:mm
    public static SimpleDateFormat short_time_sdf() {
        return getSDFormat(PATTERN_SHORTTIME);
    }
    // 指定模式的时间格式yyyy-MM-dd HH:mm
    public static SimpleDateFormat time_sdf() {
        return getSDFormat(PATTERN_CLASSICAL_NORMAL);
    }
    // 指定模式的时间格式yyyy-MM-dd
    public static SimpleDateFormat date_sdf() {
        return getSDFormat(PATTERN_CLASSICAL_SIMPLE);
    }
    // 指定模式的时间格式 yyyy-MM-dd HH:mm:ss
    public static SimpleDateFormat datetimeFormat() {
        return getSDFormat(PATTERN_CLASSICAL);
    }
    // 指定模式的时间格式 yyyy年MM月dd日
    public static SimpleDateFormat date_sdf_wz() {
        return getSDFormat(PATTERN_CH_SIMPLE);
    }
    // 指定模式的时间格式
    public static SimpleDateFormat getSDFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }
    /**
     * 当前日历，这里用中国时间表示
     *
     * @return 以当地时区表示的系统当前日历
     */
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }
    /**
     * 指定毫秒数表示的日历
     *
     * @param millis 毫秒数
     * @return 指定毫秒数表示的日历
     */
    public static Calendar getCalendar(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(millis));
        return cal;
    }
    // ////////////////////////////////////////////////////////////////////////////
    // getDate
    // 各种方式获取的Date
    // ////////////////////////////////////////////////////////////////////////////
    /**
     * 当前日期 带时分秒
     *
     * @return 系统当前时间
     */
    public static Date getDateTime() {
        return DateUtils.str2Date(getSDFormat().format(new Date()), getSDFormat());
    }
    /**
     * 当前日期
     *
     * @return 系统当前时间
     */
    public static Date getDate() {
        return new Date();
    }
    /**
     * 当前日期 不带时分秒
     *
     * @return 系统当前时间
     */
    public static Date getDateYMD() {
        return DateUtils.str2Date(DateUtils.date2Str(yyyyMMdd()), yyyyMMdd());
    }
    /**
     * 获取日期 不带时分秒
     *
     * @param date 日期
     * @return 系统当前时间
     */
    public static Date getDateYMD(Date date) {
        return DateUtils.str2Date(DateUtils.date2Str(date, yyyyMMdd()), yyyyMMdd());
    }
    /**
     * 指定毫秒数表示的日期
     *
     * @param millis 毫秒数
     * @return 指定毫秒数表示的日期
     */
    public static Date getDate(long millis) {
        return new Date(millis);
    }
    /**
     * 时间戳转换为字符串
     *
     * @param time 时间戳
     * @return 时间戳字符串
     */
    public static String timestampToString(Timestamp time) {
        Date date = null;
        if (null != time) {
            date = new Date(time.getTime());
        }
        return date2Str(date_sdf());
    }
    /**
     * @param str yyyy-MM-dd
     * @return
     */
    public static Timestamp formatTimestampYYYY_MM_DD(String str) {
        if (str == null || str.length() == 0)
            return null;
        Date p = null;
        try {
            p = new SimpleDateFormat(PATTERN_CLASSICAL_SIMPLE).parse(str);
        } catch (ParseException e) {
            return null;
        }
        return new Timestamp(p.getTime());
    }
    /**
     * 字符串转换时间戳
     *
     * @param str 字符串
     * @return 时间戳
     */
    public static Timestamp stringConvertTimestamp(String str) {
        Date date = str2Date(str, date_sdf());
        return new Timestamp(date.getTime());
    }
    /**
     * 字符串转换成日期
     *
     * @param str 字符串
     * @param sdf 转换格式
     * @return 日期
     */
    public static Date str2Date(String str, SimpleDateFormat sdf) {
        if (null == str || "".equals(str)) {
            return null;
        }
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 日期转换为字符串
     *
     * @param date_sdf 日期格式
     * @return 字符串
     */
    public static String date2Str(SimpleDateFormat date_sdf) {
        Date date = getDate();
        if (null == date) {
            return null;
        }
        return date_sdf.format(date);
    }
    /**
     * 格式化时间
     *
     * @param date   日期
     * @param format 日期格式
     * @return 日期字符串
     */
    public static String dateFormat(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date dateTime = null;
        try {
            dateTime = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simpleDateFormat.format(dateTime);
    }
    /**
     * 日期转换为字符串
     *
     * @param date 日期
     * @return 字符串
     */
    public static String date2StrCNY(Date date) {
        if (null == date) {
            return null;
        }
        return date2Str(date, date_sdf_wz());
    }
    /**
     * 日期转换为字符串
     *
     * @param date     日期
     * @param date_sdf 格式
     * @return 字符串
     */
    public static String date2Str(Date date, SimpleDateFormat date_sdf) {
        if (null == date) {
            return null;
        }
        return date_sdf.format(date);
    }
    /**
     * 日期转换为字符串
     *
     * @param format 日期格式
     * @return 字符串
     */
    public static String getDate(String format) {
        Date date = new Date();
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    /**
     * 指定毫秒数的时间戳
     *
     * @param millis 毫秒数
     * @return 指定毫秒数的时间戳
     */
    public static Timestamp getTimestamp(long millis) {
        return new Timestamp(millis);
    }
    /**
     * 以字符形式表示的时间戳
     *
     * @param time 毫秒数
     * @return 以字符形式表示的时间戳
     */
    public static Timestamp getTimestamp(String time) {
        return new Timestamp(Long.parseLong(time));
    }
    /**
     * 系统当前的时间戳
     *
     * @return 系统当前的时间戳
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    /**
     * 指定日期的时间戳
     *
     * @param date 指定日期
     * @return 指定日期的时间戳
     */
    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }
    /**
     * 指定日历的时间戳
     *
     * @param cal 指定日历
     * @return 指定日历的时间戳
     */
    public static Timestamp getCalendarTimestamp(Calendar cal) {
        return new Timestamp(cal.getTime().getTime());
    }
    // ////////////////////////////////////////////////////////////////////////////
    // getMillis
    // 各种方式获取的Millis
    // ////////////////////////////////////////////////////////////////////////////
    /**
     * 系统时间的毫秒数
     *
     * @return 系统时间的毫秒数
     */
    public static long getMillis() {
        return System.currentTimeMillis();
    }
    /**
     * 指定日历的毫秒数
     *
     * @param cal 指定日历
     * @return 指定日历的毫秒数
     */
    public static long getMillis(Calendar cal) {
        return cal.getTime().getTime();
    }
    /**
     * 指定日期的毫秒数
     *
     * @param date 指定日期
     * @return 指定日期的毫秒数
     */
    public static long getMillis(Date date) {
        return date.getTime();
    }
    /**
     * 指定时间戳的毫秒数
     *
     * @param ts 指定时间戳
     * @return 指定时间戳的毫秒数
     */
    public static long getMillis(Timestamp ts) {
        return ts.getTime();
    }
    // ////////////////////////////////////////////////////////////////////////////
    // formatDate
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////
    /**
     * 默认方式表示的系统当前日期，具体格式：年-月-日
     *
     * @return 默认日期按“年-月-日“格式显示
     */
    public static String formatDate() {
        return date_sdf().format(getCalendar().getTime());
    }
    /**
     * 默认方式表示的系统当前日期，具体格式：yyyy-MM-dd HH:mm:ss
     *
     * @return 默认日期按“yyyy-MM-dd HH:mm:ss“格式显示
     */
    public static String formatDateTime() {
        return datetimeFormat().format(getCalendar().getTime());
    }
    /**
     * 默认方式表示的系统当前日期，具体格式：yyyy-MM-dd HH:mm:ss
     *
     * @return 默认日期按“yyyy-MM-dd HH:mm:ss“格式显示
     */
    public static String formatDateTime(Date date) {
        return datetimeFormat().format(date);
    }
    /**
     * 获取时间字符串
     *
     * @param formatstr 格式
     * @return 时间字符串
     */
    public static String getDataString(SimpleDateFormat formatstr) {
        return formatstr.format(getCalendar().getTime());
    }
    /**
     * 指定日期的默认显示，具体格式：年-月-日
     *
     * @param cal 指定的日期
     * @return 指定日期按“年-月-日“格式显示
     */
    public static String formatDate(Calendar cal) {
        return date_sdf().format(cal.getTime());
    }
    /**
     * 指定日期的默认显示，具体格式：年-月-日
     *
     * @param date 指定的日期
     * @return 指定日期按“年-月-日“格式显示
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    /**
     * 指定毫秒数表示日期的默认显示，具体格式：年-月-日
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“年-月-日“格式显示
     */
    public static String formatDate(long millis) {
        return date_sdf().format(new Date(millis));
    }
    /**
     * 默认日期按指定格式显示
     *
     * @param pattern 指定的格式
     * @return 默认日期按指定格式显示
     */
    public static String formatDate(String pattern) {
        return getSDFormat(pattern).format(getCalendar().getTime());
    }
    /**
     * 指定日期按指定格式显示
     *
     * @param cal     指定的日期
     * @param pattern 指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Calendar cal, String pattern) {
        return getSDFormat(pattern).format(cal.getTime());
    }
    /**
     * 指定日期按指定格式显示
     *
     * @param date    指定的日期
     * @param pattern 指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Date date, String pattern) {
        return getSDFormat(pattern).format(date);
    }
    // ////////////////////////////////////////////////////////////////////////////
    // formatTime
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////
    /**
     * 默认方式表示的系统当前日期，具体格式：年-月-日 时：分
     *
     * @return 默认日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime() {
        return time_sdf().format(getCalendar().getTime());
    }
    /**
     * 指定毫秒数表示日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(long millis) {
        return time_sdf().format(new Date(millis));
    }
    /**
     * 指定日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param cal 指定的日期
     * @return 指定日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(Calendar cal) {
        return time_sdf().format(cal.getTime());
    }
    /**
     * 指定日期的默认显示，具体格式：年-月-日 时：分
     *
     * @param date 指定的日期
     * @return 指定日期按“年-月-日 时：分“格式显示
     */
    public static String formatTime(Date date) {
        return time_sdf().format(date);
    }
    // ////////////////////////////////////////////////////////////////////////////
    // formatShortTime
    // 将日期按照一定的格式转化为字符串
    // ////////////////////////////////////////////////////////////////////////////
    /**
     * 默认方式表示的系统当前日期，具体格式：时：分
     *
     * @return 默认日期按“时：分“格式显示
     */
    public static String formatShortTime() {
        return short_time_sdf().format(getCalendar().getTime());
    }
    /**
     * 指定毫秒数表示日期的默认显示，具体格式：时：分
     *
     * @param millis 指定的毫秒数
     * @return 指定毫秒数表示日期按“时：分“格式显示
     */
    public static String formatShortTime(long millis) {
        return short_time_sdf().format(new Date(millis));
    }
    /**
     * 指定日期的默认显示，具体格式：时：分
     *
     * @param cal 指定的日期
     * @return 指定日期按“时：分“格式显示
     */
    public static String formatShortTime(Calendar cal) {
        return short_time_sdf().format(cal.getTime());
    }
    /**
     * 指定日期的默认显示，具体格式：时：分
     *
     * @param date 指定的日期
     * @return 指定日期按“时：分“格式显示
     */
    public static String formatShortTime(Date date) {
        return short_time_sdf().format(date);
    }
    // ////////////////////////////////////////////////////////////////////////////
    // parseDate
    // parseCalendar
    // parseTimestamp
    // 将字符串按照一定的格式转化为日期或时间
    // ////////////////////////////////////////////////////////////////////////////
    /**
     * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     * @throws ParseException 日期转换异常
     */
    public static Date parseDate(String src, String pattern) throws ParseException {
        return getSDFormat(pattern).parse(src);
    }
    /**
     * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     * @throws ParseException 日期转换异常
     */
    public static Calendar parseCalendar(String src, String pattern) throws ParseException {
        Date date = parseDate(src, pattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    /**
     * 根据指定的格式将字符串转换成Date，并增加相应天数
     *
     * @param src     字符串
     * @param pattern 转换格式
     * @param amount  增加天数
     * @return 日期
     * @throws ParseException 日期转换异常
     */
    public static String formatAddDate(String src, String pattern, int amount) throws ParseException {
        Calendar cal;
        cal = parseCalendar(src, pattern);
        cal.add(Calendar.DATE, amount);
        return formatDate(cal);
    }
    /**
     * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
     *
     * @param src     将要转换的原始字符窜
     * @param pattern 转换的匹配格式
     * @return 如果转换成功则返回转换后的时间戳
     * @throws ParseException 日期转换异常
     */
    public static Timestamp parseTimestamp(String src, String pattern) throws ParseException {
        Date date = parseDate(src, pattern);
        return new Timestamp(date.getTime());
    }
    // ////////////////////////////////////////////////////////////////////////////
    // dateDiff
    // 计算两个日期之间的差值
    // ////////////////////////////////////////////////////////////////////////////
    /**
     * 计算两个时间之间的差值，根据标志的不同而不同
     *
     * @param flag   计算标志，表示按照年/月/日/时/分/秒等计算
     * @param calSrc 减数
     * @param calDes 被减数
     * @return 两个日期之间的差值
     */
    public static int dateDiff(char flag, Calendar calSrc, Calendar calDes) {
        long millisDiff = getMillis(calSrc) - getMillis(calDes);
        if (flag == 'y') {
            return (calSrc.get(Calendar.YEAR) - calDes.get(Calendar.YEAR));
        }
        if (flag == 'M') {
            return (calSrc.get(Calendar.MONTH) + ((calSrc.get(Calendar.YEAR) - calDes.get(Calendar.YEAR)) * 12)
                    - calDes.get(Calendar.MONTH));
        }
        if (flag == 'd') {
            return (int) (millisDiff / DAY_IN_MILLIS);
        }
        if (flag == 'h') {
            return (int) (millisDiff / HOUR_IN_MILLIS);
        }
        if (flag == 'm') {
            return (int) (millisDiff / MINUTE_IN_MILLIS);
        }
        if (flag == 's') {
            return (int) (millisDiff / SECOND_IN_MILLIS);
        }
        return 0;
    }

    /**
     * String类型 转换为Date, 如果参数长度为10 转换格式”yyyy-MM-dd“ 如果参数长度为19 转换格式”yyyy-MM-dd HH:mm:ss“
     *
     * @param text String类型的时间值
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try {
                if (text.indexOf(":") == -1 && text.length() == 10) {
                    setValue(date_sdf().parse(text));
                } else if (text.indexOf(":") > 0 && text.length() == 19) {
                    setValue(datetimeFormat().parse(text));
                } else {
                    throw new IllegalArgumentException("Could not parse date, date format is error ");
                }
            } catch (ParseException ex) {
                IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
                iae.initCause(ex);
                throw iae;
            }
        } else {
            setValue(null);
        }
    }
    public static int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        return calendar.get(Calendar.YEAR);
    }
    public static String getAfterMonth(int month, int day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(2, month);
        calendar.add(5, day);
        String date = format.format(calendar.getTime());
        return date;
    }
    public static String getAfterDate(int day) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(5, day);
        String date = format.format(calendar.getTime());
        return date;
    }
    public static String getAfterMinute(int minute) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(12, minute);
        String date = format.format(calendar.getTime());
        return date;
    }
    public static String getAfterSecond(int second) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(13, second);
        String date = format.format(calendar.getTime());
        return date;
    }
    public static String getDateStr(Date date, String Pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(Pattern);
        return formatter.format(date);
    }
    /**
     * 日期的增加分钟
     *
     * @param date      日期
     * @param addSecond 增加的秒钟,正数表示增加，负数表示减少
     * @return Date 日期的增加月数后的结果
     */
    public static Date addSecond(Date date, int addSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, addSecond);
        return new Date(calendar.getTime().getTime());
    }
    /**
     * 日期的增加分钟
     *
     * @param date      日期
     * @param addMinute 增加的分钟,正数表示增加，负数表示减少
     * @return Date 日期的增加月数后的结果
     */
    public static Date addMinute(Date date, int addMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, addMinute);
        return new Date(calendar.getTime().getTime());
    }
    /**
     * 日期的增加小时
     *
     * @param date    日期
     * @param addHour 增加的小时,正数表示增加，负数表示减少
     * @return Date 日期的增加月数后的结果
     */
    public static Date addHour(Date date, int addHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, addHour);
        return new Date(calendar.getTime().getTime());
    }
    /**
     * 日期的增加天数
     *
     * @param date   日期
     * @param addDay 增加的天数,正数表示增加，负数表示减少
     * @return Date 日期的增加月数后的结果
     */
    public static Date addDay(Date date, int addDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, addDay);
        return new Date(calendar.getTime().getTime());
    }
    /**
     * 将datetime转换为date类型
     *
     * @param date 日期
     * @return 日期
     */
    public static Date parseDate(Date date) {
        return parse(DateUtils.date2Str(date, DateUtils.date_sdf()), "yyyy-MM-dd");
    }
    /**
     * 静态方法 根据两个日期求相差天数 正数表示d1大于d2天数 负数表示d1小于d2天数 add 7.30
     *
     * @param d1 日期1
     * @param d2 日期2
     * @return 相差天数
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
     * 验证日期是否是周末
     *
     * @param date 日期
     * @return 是否是周末
     */
    public static boolean checkHoliday(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        // 判断日期是否是周六周日
        if (ca.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || ca.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        }
        return false;
    }
    /**
     * 获取当前月份所在天
     *
     * @param date 日期
     * @return 当前月份所在天
     */
    public static int getDay(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        return ca.get(Calendar.DAY_OF_MONTH);
    }
    /*********** 2017年10月24日 by周亮新增以下代码 ***********/
    /**
     * 根据默认格式将指定字符串解析成日期
     *
     * @param str 指定字符串
     * @return 返回解析后的日期
     */
    public static Date parse(String str) {
        return parse(str, PATTERN_CLASSICAL);
    }
    /**
     * 根据指定格式将指定字符串解析成日期
     *
     * @param str     指定日期
     * @param pattern 指定格式
     * @return 返回解析后的日期
     */
    public static Date parse(String str, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据默认格式将日期转格式化成字符串
     *
     * @param date 指定日期
     * @return 返回格式化后的字符串
     */
    public static String format(Date date) {
        return format(date, PATTERN_CLASSICAL);
    }
    /**
     * 根据指定格式将指定日期格式化成字符串
     *
     * @param date    指定日期
     * @param pattern 指定格式
     * @return 返回格式化后的字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    /**
     * 获取时间date1与date2相差的秒数
     *
     * @param date1 起始时间
     * @param date2 结束时间
     * @return 返回相差的秒数
     */
    public static int getOffsetSeconds(Date date1, Date date2) {
        int seconds = (int) ((date2.getTime() - date1.getTime()) / 1000);
        return seconds;
    }
    /**
     * 获取时间date1与date2相差的分钟数
     *
     * @param date1 起始时间
     * @param date2 结束时间
     * @return 返回相差的分钟数
     */
    public static int getOffsetMinutes(Date date1, Date date2) {
        return getOffsetSeconds(date1, date2) / 60;
    }
    /**
     * 获取时间date1与date2相差的小时数
     *
     * @param date1 起始时间
     * @param date2 结束时间
     * @return 返回相差的小时数
     */
    public static int getOffsetHours(Date date1, Date date2) {
        return getOffsetMinutes(date1, date2) / 60;
    }
    /**
     * 获取时间date1与date2相差的天数数
     *
     * @param date1 起始时间
     * @param date2 结束时间
     * @return 返回相差的天数
     */
    public static int getOffsetDays(Date date1, Date date2) {
        return getOffsetHours(date1, date2) / 24;
    }
    /**
     * 获取时间date1与date2相差的周数
     *
     * @param date1 起始时间
     * @param date2 结束时间
     * @return 返回相差的周数
     */
    public static int getOffsetWeeks(Date date1, Date date2) {
        return getOffsetDays(date1, date2) / 7;
    }
    /**
     * 获取重置指定日期的时分秒后的时间
     *
     * @param date   指定日期
     * @param hour   指定小时
     * @param minute 指定分钟
     * @param second 指定秒
     * @return 返回重置时分秒后的时间
     */
    public static Date getResetTime(Date date, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.SECOND, minute);
        cal.set(Calendar.MINUTE, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    /**
     * 返回指定日期的起始时间
     *
     * @param date 指定日期（例如2014-08-01）
     * @return 返回起始时间（例如2014-08-01 00:00:00）
     */
    public static Date getIntegralStartTime(Date date) {
        return getResetTime(date, 0, 0, 0);
    }
    /**
     * 返回指定日期的结束时间
     *
     * @param date 指定日期（例如2014-08-01）
     * @return 返回结束时间（例如2014-08-01 23:59:59）
     */
    public static Date getIntegralEndTime(Date date) {
        return getResetTime(date, 23, 59, 59);
    }
    /**
     * 获取指定日期累加年月日后的时间
     *
     * @param date  指定日期
     * @param year  指定年数
     * @param month 指定月数
     * @param day   指定天数
     * @return 返回累加年月日后的时间
     */
    public static Date rollDate(Date date, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }
    /**
     * 获取指定日期累加指定月数后的时间
     *
     * @param date  指定日期
     * @param month 指定月数
     * @return 返回累加月数后的时间
     */
    public static Date rollMonth(Date date, int month) {
        return rollDate(date, 0, month, 0);
    }
    /**
     * 获取指定日期累加指定天数后的时间
     *
     * @param date 指定日期
     * @param day  指定天数
     * @return 返回累加天数后的时间
     */
    public static Date rollDay(Date date, int day) {
        return rollDate(date, 0, 0, day);
    }
    /**
     * 计算指定日期所在月份的天数
     *
     * @param date 指定日期
     * @return 返回所在月份的天数
     */
    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        int dayOfMonth = cal.getActualMaximum(Calendar.DATE);
        return dayOfMonth;
    }
    /**
     * 返回日期所在星期几 周一返回1 依次类推
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        Boolean isFirstSunday = (cal.getFirstDayOfWeek() == Calendar.SUNDAY);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return isFirstSunday ? dayOfWeek - 1 < 0 ? 7 : dayOfWeek - 1 : dayOfWeek;
    }
    /**
     * 获取当月第一天的起始时间，例如2014-08-01 00:00:00
     *
     * @return 返回当月第一天的起始时间
     */
    public static Date getMonthStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getIntegralStartTime(cal.getTime());
    }
    /**
     * 获取当月最后一天的结束时间，例如2014-08-31 23:59:59
     *
     * @return 返回当月最后一天的结束时间
     */
    public static Date getMonthEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, getDayOfMonth(cal.getTime()));
        return getIntegralEndTime(cal.getTime());
    }
    /**
     * 获取上个月第一天的起始时间，例如2014-07-01 00:00:00
     *
     * @return 返回上个月第一天的起始时间
     */
    public static Date getLastMonthStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getIntegralStartTime(cal.getTime());
    }
    /**
     * 获取上个月最后一天的结束时间，例如2014-07-31 23:59:59
     *
     * @return 返回上个月最后一天的结束时间
     */
    public static Date getLastMonthEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, getDayOfMonth(cal.getTime()));
        return getIntegralEndTime(cal.getTime());
    }
    /**
     * 获取下个月第一天的起始时间，例如2014-09-01 00:00:00
     *
     * @return 返回下个月第一天的起始时间
     */
    public static Date getNextMonthStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getIntegralStartTime(cal.getTime());
    }
    /**
     * 获取下个月最后一天的结束时间，例如2014-09-30 23:59:59
     *
     * @return 返回下个月最后一天的结束时间
     */
    public static Date getNextMonthEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, getDayOfMonth(cal.getTime()));
        return getIntegralEndTime(cal.getTime());
    }
    /**
     * 获取当前季度第一天的起始时间
     *
     * @return 返回当前季度第一天的起始时间
     */
    public static Date getQuarterStartTime() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        if (month < 3) {
            cal.set(Calendar.MONTH, 0);
        } else if (month < 6) {
            cal.set(Calendar.MONTH, 3);
        } else if (month < 9) {
            cal.set(Calendar.MONTH, 6);
        } else {
            cal.set(Calendar.MONTH, 9);
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getIntegralStartTime(cal.getTime());
    }
    /**
     * 获取当前季度最后一天的结束时间
     *
     * @return 返回当前季度最后一天的结束时间
     */
    public static Date getQuarterEndTime() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        if (month < 3) {
            cal.set(Calendar.MONTH, 2);
        } else if (month < 6) {
            cal.set(Calendar.MONTH, 5);
        } else if (month < 9) {
            cal.set(Calendar.MONTH, 8);
        } else {
            cal.set(Calendar.MONTH, 11);
        }
        cal.set(Calendar.DAY_OF_MONTH, getDayOfMonth(cal.getTime()));
        return getIntegralEndTime(cal.getTime());
    }
    /**
     * 获取前一个工作日
     *
     * @return 返回前一个工作日
     */
    public static Date getPrevWorkday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_MONTH, -2);
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        return getIntegralStartTime(cal.getTime());
    }
    /**
     * 获取下一个工作日
     *
     * @return 返回下个工作日
     */
    public static Date getNextWorkday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            cal.add(Calendar.DAY_OF_MONTH, 2);
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return getIntegralStartTime(cal.getTime());
    }
    /**
     * 获取当周的第一个工作日
     *
     * @return 返回第一个工作日
     */
    public static Date getFirstWorkday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getIntegralStartTime(cal.getTime());
    }
    /**
     * 获取当周的最后一个工作日
     *
     * @return 返回最后一个工作日
     */
    public static Date getLastWorkday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return getIntegralStartTime(cal.getTime());
    }
    /**
     * 判断指定日期是否是工作日
     *
     * @param date 指定日期
     * @return 如果是工作日返回true，否则返回false
     */
    public static boolean isWorkday(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return !(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
    }
    /**
     * 获取指定日期是星期几
     *
     * @param date 指定日期
     * @return 返回星期几的描述
     */
    public static String getWeekdayDesc(Date date) {
        final String[] weeks = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        return weeks[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }
    /*********** 2017年10月25日 by左宇新增以下代码 ***********/
    /**
     * 获取当年的第一天
     *
     * @return 当年的第一天
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
    /**
     * 获取当年的最后一天
     *
     * @return 当年的最后一天
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }
    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }
    /*********** end ***********/
    /**
     * 获取指定日期的月份第一天的日期
     *
     * @param date 日期
     * @return 指定日期的月份第一天的日期
     */
    public static Date getMonthFirstDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getIntegralStartTime(cal.getTime());
    }
    /**
     * 获取指定日期的下个月第一天的日期
     *
     * @param date 日期
     * @return 指定日期的下个月第一天的日期
     */
    public static Date getNextMonthFirstDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return getIntegralStartTime(cal.getTime());
    }
    /*********** 2018年02月06日 by左宇新增以下代码 ***********/
    /**
     * 获取任意时间的年份第一天
     *
     * @param date 日期
     * @return 任意时间的年份第一天
     */
    public static Date getFirstYearDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentYear = calendar.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
    /**
     * 获取任意时间的年份最后一天
     *
     * @param date 日期
     * @return 任意时间的年份最后一天
     */
    public static Date getLastYearDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentYear = calendar.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }
    /**
     * 返回指定日期的季的第一天
     *
     * @param date 日期
     * @return 指定日期的季的第一天
     */
    public static Date getFirstQuarterDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3) {
            c.set(Calendar.MONTH, 0);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            c.set(Calendar.MONTH, 3);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            c.set(Calendar.MONTH, 6);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            c.set(Calendar.MONTH, 9);
        } else {
            c.set(Calendar.DATE, 1);
        }
        c.set(Calendar.DATE, 1);
        return parseDate(c.getTime());
    }
    /**
     * 返回指定日期的季的最后一天
     *
     * @param date 日期
     * @return 指定日期的季的最后一天
     */
    public static Date getLastQuarterDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3) {
            c.set(Calendar.MONTH, 2);
            c.set(Calendar.DATE, 31);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            c.set(Calendar.MONTH, 5);
            c.set(Calendar.DATE, 30);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            c.set(Calendar.MONTH, 8);
            c.set(Calendar.DATE, 30);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
        }
        return parseDate(c.getTime());
    }
    /**
     * 获取任意时间的月第一天
     *
     * @param date 日期
     * @return 任意时间的月第一天
     */
    public static Date getFirstMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return parseDate(calendar.getTime());
    }
    /**
     * 获取任意时间的月的最后一天
     *
     * @param date 日期
     * @return 任意时间的月的最后一天
     */
    public static Date getLastMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return parseDate(calendar.getTime());
    }
    /**
     * 获取任意时间的周一
     *
     * @param date 日期
     * @return 任意时间的周一
     */
    public static Date getFirstWeekDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = calendar.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        return parseDate(calendar.getTime());
    }
    /**
     * 获取任意时间的周末
     *
     * @param date 日期
     * @return 任意时间的周末
     */
    public static Date getLastWeekDate(Date date) {
        Date firstDate = getFirstWeekDate(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDate);
        calendar.add(Calendar.DATE, 6);
        return parseDate(calendar.getTime());
    }
    /**
     * 获取日期属于当年第几季度
     *
     * @param date 日期
     * @return 指定日期属于当年第几季度
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }
    /**
     * 获取日期属于当年第几周
     *
     * @param date 日期
     * @return 指定日期属于当年第几周
     */
    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
    /**
     * 获取指定日期年份
     *
     * @param date 日期
     * @return 指定日期年份
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
    /**
     * 获取指定日期月份
     *
     * @param date 日期
     * @return 指定日期月份
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }
    /**
     * 日期的增加月份
     *
     * @param date     日期
     * @param addMonth 增加的月份,正数表示增加，负数表示减少
     * @return Date 日期的增加月数后的结果
     */
    public static Date addMonth(Date date, int addMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, addMonth);
        return new Date(calendar.getTime().getTime());
    }
    /*********** end ***********/
    /*********** 2018年03月13日 by左宇新增以下代码 ***********/
    /**
     * 日期的增加年份
     *
     * @param date    日期
     * @param addYear 增加的周数,正数表示增加，负数表示减少
     * @return Date 日期的增加年份后的结果
     */
    public static Date addYear(Date date, int addYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, addYear);
        return new Date(calendar.getTime().getTime());
    }
    /**
     * 日期的增加季度
     *
     * @param date       日期
     * @param addQuarter 增加的季度数,正数表示增加，负数表示减少
     * @return Date 日期的增加季度后的结果
     */
    public static Date addQuarter(Date date, int addQuarter) {
        int newAddQuarter = addQuarter * 3;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, newAddQuarter);
        return new Date(calendar.getTime().getTime());
    }
    /**
     * 日期的增加周数
     *
     * @param date    日期
     * @param addWeek 增加的周数,正数表示增加，负数表示减少
     * @return Date 日期的增加周数后的结果
     */
    public static Date addWeek(Date date, int addWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, addWeek);
        return new Date(calendar.getTime().getTime());
    }
    /**
     * 获取日期属于当月第几周
     *
     * @param date 日期
     * @return 指定日期属于当月第几周
     */
    public static int getWeekOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 第几周
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        // 第几天，从周日开始
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return week;
    }
    /**
     * 将字符串转为yyyy-MM-dd日期类型
     *
     * @param dateStr 字符串
     * @return 日期
     */
    public static Date parseDate(String dateStr) {
        return parse(dateStr, "yyyy-MM-dd");
    }
    /*********** end ***********/
    /**
     * 设置指定日期的时间为当前时间的时分秒
     *
     * @param date 指定日期
     * @return
     */
    public static Date setSpecifiedDateOfTime(Date date) {
        String time = format(date, PATTERN_SHORTTIMESS);
        // 没有时分秒
        if ("00:00:00".equals(time)) {
            String format = formatDate(date);
            StringBuilder concat = new StringBuilder(format);
            // 当天
            Date curDate = getDateTime();
            if (dayDiffs(date, curDate) == 0) {
                concat.append(" ").append(format(curDate, PATTERN_SHORTTIMESS));
            } else {
                concat.append(" 23:59:59");
            }
            return parse(concat.toString());
        }
        return date;
    }
    /**
     * 获取所在周的日期
     *
     * @param date
     * @return 周一日期, 周日日期
     */
    public static String getTimeInterval(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，第一天设置为周一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        // 所在周周一的日期
        String imptimeBegin = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        // 所在周周日的日期
        String imptimeEnd = sdf.format(cal.getTime());
        return imptimeBegin + "," + imptimeEnd;
    }
    /**
     * 获取周内所有日期
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }
    /**
     * 获取指定天数后的日期
     *
     * @param day
     * @return
     */
    public static Date getDateAfter(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
        return calendar.getTime();
    }
    /**
     * 比较两个日期大小
     *
     * @param d1
     * @param d2
     * @return d1 > d2 1 , d1 = d2 0 , d1 < d2 -1
     */
    public static int compareDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.compareTo(c2);
    }
    /**
     * 获取给定日期的第一天
     */
    public static Calendar getFirstDayCalender(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
    /**
     * @param date 日期
     * @param day  指定日期
     * @return 返回某个年月的指定日
     */
    public static Date getDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int lastDayNum = DateUtils.getDay(DateUtils.getLastMonthDate(date));
        if (lastDayNum > day) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, lastDayNum);
        }
        return calendar.getTime();
    }

    /**
     * 计算两个日期间相差的月份 (不考虑日对月影响，6/30 与 7/1 也相差一个月)
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getMonthDiff(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(startDate);
        endCal.setTime(endDate);
        int startYear = startCal.get(Calendar.YEAR);
        int endYear = endCal.get(Calendar.YEAR);
        int startMonth = startCal.get(Calendar.MONTH);
        int endMonth = endCal.get(Calendar.MONTH);
        int yearInterval = endYear - startYear;
        int monthInterval = endMonth - startMonth;
        int monthsDiff = yearInterval * 12 + monthInterval;
        return monthsDiff;
    }
}
