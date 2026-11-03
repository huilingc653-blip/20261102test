package com.bytter.cloud.common.core.util;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.*;
import java.util.*;
/**
 * 日期工具类
 *
 * @author zhoul
 * @since 1.0.0
 */
public class DateUtil {
    private DateUtil() {
    }
    public static final String PATTERN_DATETIME = DatePattern.NORM_DATETIME_PATTERN;
    public static final String PATTERN_DATE = DatePattern.NORM_DATE_PATTERN;
    public static final String PATTERN_TIME = DatePattern.NORM_TIME_PATTERN;
    /**
     * java 8 时间格式化
     */
    public static final DateTimeFormatter DATETIME_FORMATTER = DatePattern.NORM_DATETIME_FORMAT;
    public static final DateTimeFormatter DATE_FORMATTER = DatePattern.NORM_DATE_FORMAT;
    public static final DateTimeFormatter TIME_FORMATTER = DatePattern.NORM_TIME_FORMAT;
    /**
     * 添加年
     *
     * @param date       时间
     * @param yearsToAdd 添加的年数
     * @return 设置后的时间
     */
    public static Date plusYears(Date date, int yearsToAdd) {
        return DateUtil.plus(date, Period.ofYears(yearsToAdd));
    }
    /**
     * 添加月
     *
     * @param date        时间
     * @param monthsToAdd 添加的月数
     * @return 设置后的时间
     */
    public static Date plusMonths(Date date, int monthsToAdd) {
        return DateUtil.plus(date, Period.ofMonths(monthsToAdd));
    }
    /**
     * 添加周
     *
     * @param date       时间
     * @param weeksToAdd 添加的周数
     * @return 设置后的时间
     */
    public static Date plusWeeks(Date date, int weeksToAdd) {
        return DateUtil.plus(date, Period.ofWeeks(weeksToAdd));
    }
    /**
     * 添加天
     *
     * @param date      时间
     * @param daysToAdd 添加的天数
     * @return 设置后的时间
     */
    public static Date plusDays(Date date, long daysToAdd) {
        return DateUtil.plus(date, Duration.ofDays(daysToAdd));
    }
    /**
     * 添加小时
     *
     * @param date       时间
     * @param hoursToAdd 添加的小时数
     * @return 设置后的时间
     */
    public static Date plusHours(Date date, long hoursToAdd) {
        return DateUtil.plus(date, Duration.ofHours(hoursToAdd));
    }
    /**
     * 添加分钟
     *
     * @param date         时间
     * @param minutesToAdd 添加的分钟数
     * @return 设置后的时间
     */
    public static Date plusMinutes(Date date, long minutesToAdd) {
        return DateUtil.plus(date, Duration.ofMinutes(minutesToAdd));
    }
    /**
     * 添加秒
     *
     * @param date         时间
     * @param secondsToAdd 添加的秒数
     * @return 设置后的时间
     */
    public static Date plusSeconds(Date date, long secondsToAdd) {
        return DateUtil.plus(date, Duration.ofSeconds(secondsToAdd));
    }
    /**
     * 添加毫秒
     *
     * @param date        时间
     * @param millisToAdd 添加的毫秒数
     * @return 设置后的时间
     */
    public static Date plusMillis(Date date, long millisToAdd) {
        return DateUtil.plus(date, Duration.ofMillis(millisToAdd));
    }
    /**
     * 日期添加时间量
     *
     * @param date   时间
     * @param amount 时间量
     * @return 设置后的时间
     */
    public static Date plus(Date date, TemporalAmount amount) {
        Objects.requireNonNull(date, "The date must not be null");
        Instant instant = date.toInstant()
                .atZone(ZoneOffset.UTC)
                .plus(amount)
                .toInstant();
        return Date.from(instant);
    }
    /**
     * 减少年
     *
     * @param date  时间
     * @param years 减少的年数
     * @return 设置后的时间
     */
    public static Date minusYears(Date date, int years) {
        return DateUtil.minus(date, Period.ofYears(years));
    }
    /**
     * 减少月
     *
     * @param date   时间
     * @param months 减少的月数
     * @return 设置后的时间
     */
    public static Date minusMonths(Date date, int months) {
        return DateUtil.minus(date, Period.ofMonths(months));
    }
    /**
     * 减少周
     *
     * @param date  时间
     * @param weeks 减少的周数
     * @return 设置后的时间
     */
    public static Date minusWeeks(Date date, int weeks) {
        return DateUtil.minus(date, Period.ofWeeks(weeks));
    }
    /**
     * 减少天
     *
     * @param date 时间
     * @param days 减少的天数
     * @return 设置后的时间
     */
    public static Date minusDays(Date date, long days) {
        return DateUtil.minus(date, Duration.ofDays(days));
    }
    /**
     * 减少小时
     *
     * @param date  时间
     * @param hours 减少的小时数
     * @return 设置后的时间
     */
    public static Date minusHours(Date date, long hours) {
        return DateUtil.minus(date, Duration.ofHours(hours));
    }
    /**
     * 减少分钟
     *
     * @param date    时间
     * @param minutes 减少的分钟数
     * @return 设置后的时间
     */
    public static Date minusMinutes(Date date, long minutes) {
        return DateUtil.minus(date, Duration.ofMinutes(minutes));
    }
    /**
     * 减少秒
     *
     * @param date    时间
     * @param seconds 减少的秒数
     * @return 设置后的时间
     */
    public static Date minusSeconds(Date date, long seconds) {
        return DateUtil.minus(date, Duration.ofSeconds(seconds));
    }
    /**
     * 减少毫秒
     *
     * @param date   时间
     * @param millis 减少的毫秒数
     * @return 设置后的时间
     */
    public static Date minusMillis(Date date, long millis) {
        return DateUtil.minus(date, Duration.ofMillis(millis));
    }
    /**
     * 日期减少时间量
     *
     * @param date   时间
     * @param amount 时间量
     * @return 设置后的时间
     */
    public static Date minus(Date date, TemporalAmount amount) {
        Objects.requireNonNull(date, "The date must not be null");
        Instant instant = date.toInstant()
                .atZone(ZoneOffset.UTC)
                .minus(amount)
                .toInstant();
        return Date.from(instant);
    }
    /**
     * java8 日期时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间
     */
    public static String formatDateTime(TemporalAccessor temporal) {
        return DATETIME_FORMATTER.format(temporal);
    }
    /**
     * 日期时间格式化
     *
     * @return 格式化后的时间
     */
    public static String formatDateTime() {
        return DATETIME_FORMATTER.format(LocalDateTime.now());
    }
    /**
     * 日期时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间
     */
    @Nullable
    public static String formatDateTime(@Nullable Date date) {
        if (date == null) {
            return null;
        }
        return DATETIME_FORMATTER.format(date.toInstant());
    }
    /**
     * 日期时间格式化
     *
     * @return 格式化后的时间
     */
    public static String formatDate() {
        return DATE_FORMATTER.format(LocalDate.now());
    }
    /**
     * 日期格式化
     *
     * @param date 时间
     * @return 格式化后的时间
     */
    @Nullable
    public static String formatDate(@Nullable Date date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date.toInstant());
    }
    /**
     * java8 日期时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间
     */
    public static String formatDate(TemporalAccessor temporal) {
        return DATE_FORMATTER.format(temporal);
    }
    /**
     * 时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间
     */
    @Nullable
    public static String formatTime(@Nullable Date date) {
        if (date == null) {
            return null;
        }
        return TIME_FORMATTER.format(date.toInstant());
    }
    /**
     * java8 时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间
     */
    public static String formatTime(TemporalAccessor temporal) {
        return TIME_FORMATTER.format(temporal);
    }
    /**
     * java8 日期格式化
     *
     * @param temporal 时间
     * @param pattern  表达式
     * @return 格式化后的时间
     */
    public static String format(TemporalAccessor temporal, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(temporal);
    }
    /**
     * 日期格式化
     *
     * @param date    时间
     * @param pattern 表达式
     * @return 格式化后的时间
     */
    @Nullable
    public static String format(@Nullable Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(date.toInstant());
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static Date parse(String dateStr, String pattern) {
        return DateUtil.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param format  DateTimeFormatter
     * @return 时间
     */
    public static Date parse(String dateStr, DateTimeFormatter format) {
        if (format.getZone() == null) {
            format = format.withZone(ZoneId.systemDefault());
        }
        Instant instant = format.parse(dateStr, instantQuery());
        return Date.from(instant);
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @param query   {@link TemporalQuery}
     * @param <T>     泛型
     * @return 时间
     */
    public static <T> T parse(String dateStr, String pattern, TemporalQuery<T> query) {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).parse(dateStr, query);
    }
    /**
     * 将字符串转换为时间
     *
     * @param text          时间字符串
     * @param locale        Locale
     * @param parsePatterns 时间正则数组
     * @param query         {@link TemporalQuery}
     * @param <T>           泛型
     * @return 时间
     */
    public static <T> T parse(@Nullable CharSequence text, @Nullable Locale locale, @Nullable String[] parsePatterns, TemporalQuery<T> query) {
        if (text == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
        final Locale lcl = locale == null ? Locale.getDefault() : locale;
        final ZoneId systemZone = ZoneId.systemDefault();
        DateTimeFormatter formatter;
        for (final String parsePattern : parsePatterns) {
            formatter = DateTimeFormatter.ofPattern(parsePattern, lcl).withZone(systemZone);
            try {
                return formatter.parse(text, query);
            } catch (final DateTimeParseException ignore) {
                // leniency is preventing calendar from being set
            }
        }
        throw new DateTimeParseException("Unable to parse the date: " + text, text, -1);
    }
    /**
     * LocalDateTime 转 Instant
     *
     * @param dateTime 时间
     * @return Instant
     */
    public static Instant toInstant(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
    /**
     * Instant 转 LocalDateTime
     *
     * @param instant Instant
     * @return Instant
     */
    public static LocalDateTime toDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    /**
     * Date 转 LocalDateTime
     *
     * @param date Date
     * @return Instant
     */
    public static LocalDateTime toDateTime(Date date) {
        return DateUtil.toDateTime(date.toInstant());
    }
    /**
     * LocalDateTime 转换成 date
     *
     * @param dateTime LocalDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(DateUtil.toInstant(dateTime));
    }
    /**
     * LocalDate 转换成 date
     *
     * @param localDate LocalDate
     * @return Date
     */
    public static Date toDate(final LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    /**
     * LocalDateTime 转换成 Calendar.
     *
     * @param localDateTime 时间
     * @return {@link Calendar}
     */
    public static Calendar toCalendar(final LocalDateTime localDateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
    }
    /**
     * localDateTime 转换成毫秒数
     *
     * @param localDateTime LocalDateTime
     * @return long
     */
    public static long toMilliseconds(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    /**
     * localDate 转换成毫秒数
     *
     * @param localDate LocalDate
     * @return long
     */
    public static long toMilliseconds(LocalDate localDate) {
        return toMilliseconds(localDate.atStartOfDay());
    }
    /**
     * 转换成java8 时间
     *
     * @param calendar 日历
     * @return LocalDateTime
     */
    public static LocalDateTime fromCalendar(final Calendar calendar) {
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }
    /**
     * 转换成java8 时间
     *
     * @param instant Instant
     * @return LocalDateTime
     */
    public static LocalDateTime fromInstant(final Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    /**
     * 转换成java8 时间
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime fromDate(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    /**
     * 转换成java8 时间
     *
     * @param milliseconds 毫秒数
     * @return LocalDateTime
     */
    public static LocalDateTime fromMilliseconds(final long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }
    /**
     * 比较2个时间差，跨度比较小
     *
     * @param startInclusive 开始时间
     * @param endExclusive   结束时间
     * @return 时间间隔
     */
    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive);
    }
    /**
     * 比较2个时间差，跨度比较大，年月日为单位
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间间隔
     */
    public static Period between(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate);
    }
    /**
     * 比较2个 时间差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间间隔
     */
    public static Duration between(Date startDate, Date endDate) {
        return Duration.between(startDate.toInstant(), endDate.toInstant());
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static LocalDateTime parseDateTime(CharSequence dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return DateUtil.parseDateTime(dateStr, formatter);
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr   时间字符串
     * @param formatter DateTimeFormatter
     * @return 时间
     */
    public static LocalDateTime parseDateTime(CharSequence dateStr, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateStr, formatter);
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @return 时间
     */
    public static LocalDateTime parseDateTime(CharSequence dateStr) {
        return DateUtil.parseDateTime(dateStr, DateUtil.DATETIME_FORMATTER);
    }
    /**
     * 将字符串转换为时间
     *
     * @param text          时间字符串
     * @param parsePatterns 时间正则数组
     * @return 时间
     */
    public static LocalDateTime parseDateTime(@Nullable CharSequence text, @Nullable String[] parsePatterns) {
        return parseDateTime(text, null, parsePatterns);
    }
    /**
     * 将字符串转换为时间
     *
     * @param text          时间字符串
     * @param locale        Locale
     * @param parsePatterns 时间正则数组
     * @return 时间
     */
    public static LocalDateTime parseDateTime(@Nullable CharSequence text, @Nullable Locale locale, @Nullable String[] parsePatterns) {
        return parse(text, locale, parsePatterns, LocalDateTime::from);
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static LocalDate parseDate(CharSequence dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return DateUtil.parseDate(dateStr, formatter);
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr   时间字符串
     * @param formatter DateTimeFormatter
     * @return 时间
     */
    public static LocalDate parseDate(CharSequence dateStr, DateTimeFormatter formatter) {
        return LocalDate.parse(dateStr, formatter);
    }
    /**
     * 将字符串转换为日期
     *
     * @param dateStr 时间字符串
     * @return 时间
     */
    public static LocalDate parseDate(CharSequence dateStr) {
        return DateUtil.parseDate(dateStr, DateUtil.DATE_FORMATTER);
    }
    /**
     * 将字符串转换为日期
     *
     * @param text          时间字符串
     * @param parsePatterns 时间正则数组
     * @return 时间
     */
    public static LocalDate parseDate(@Nullable CharSequence text, @Nullable String[] parsePatterns) {
        return parseDate(text, null, parsePatterns);
    }
    /**
     * 将字符串转换为日期
     *
     * @param text          时间字符串
     * @param locale        Locale
     * @param parsePatterns 时间正则数组
     * @return 时间
     */
    public static LocalDate parseDate(@Nullable CharSequence text, @Nullable Locale locale, @Nullable String[] parsePatterns) {
        return parse(text, locale, parsePatterns, LocalDate::from);
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 时间正则
     * @return 时间
     */
    public static LocalTime parseTime(CharSequence dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return DateUtil.parseTime(dateStr, formatter);
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr   时间字符串
     * @param formatter DateTimeFormatter
     * @return 时间
     */
    public static LocalTime parseTime(CharSequence dateStr, DateTimeFormatter formatter) {
        return LocalTime.parse(dateStr, formatter);
    }
    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @return 时间
     */
    public static LocalTime parseTime(CharSequence dateStr) {
        return DateUtil.parseTime(dateStr, DateUtil.TIME_FORMATTER);
    }
    /**
     * 将字符串转换为时间
     *
     * @param text          时间字符串
     * @param parsePatterns 时间正则数组
     * @return 时间
     */
    public static LocalTime parseTime(@Nullable CharSequence text, @Nullable String[] parsePatterns) {
        return parseTime(text, null, parsePatterns);
    }
    /**
     * 将字符串转换为时间
     *
     * @param text          时间字符串
     * @param locale        Locale
     * @param parsePatterns 时间正则数组
     * @return 时间
     */
    public static LocalTime parseTime(@Nullable CharSequence text, @Nullable Locale locale, @Nullable String[] parsePatterns) {
        return parse(text, locale, parsePatterns, LocalTime::from);
    }
    /**
     * 支持日期、时间、时间日期格式转换成 Instant
     */
    private static final TemporalQuery<Instant> INSTANT_QUERY = new TemporalQuery<Instant>() {
        @Nullable
        @Override
        public Instant queryFrom(TemporalAccessor temporal) {
            if (temporal.isSupported(ChronoField.INSTANT_SECONDS) && temporal.isSupported(ChronoField.NANO_OF_SECOND)) {
                long instantSecs = temporal.getLong(ChronoField.INSTANT_SECONDS);
                int nanoOfSecond = temporal.get(ChronoField.NANO_OF_SECOND);
                return Instant.ofEpochSecond(instantSecs, nanoOfSecond);
            }
            // 获取时区
            ZoneId zoneId = temporal.query(TemporalQueries.zoneId());
            Objects.requireNonNull(zoneId, "Unable to obtain Instant from TemporalAccessor: zoneId is null.");
            if (temporal.isSupported(ChronoField.NANO_OF_DAY)) {
                return LocalTime.ofNanoOfDay(temporal.getLong(ChronoField.NANO_OF_DAY))
                        .atDate(DateUtil.EPOCH)
                        .atZone(zoneId)
                        .toInstant();
            } else if (temporal.isSupported(ChronoField.EPOCH_DAY)) {
                return LocalDate.ofEpochDay(temporal.getLong(ChronoField.EPOCH_DAY))
                        .atStartOfDay()
                        .atZone(zoneId)
                        .toInstant();
            }
            return null;
        }
    };
    /**
     * 获取当前日期
     *
     * @return Date
     */
    public static Date nowDate() {
        return toDate(LocalDate.now());
    }
    /**
     * 获取当前时间
     *
     * @return Date
     */
    public static Date nowDateTime() {
        return toDate(LocalDateTime.now());
    }
    /**
     * 获取当前时间
     *
     * @param pattern 格式化规则{@link DatePattern}
     * @return 当前时间
     */
    public static String nowPatternFormat(String pattern) {
        return format(LocalDateTime.now(), pattern);
    }
    /**
     * Date转LocalDateTime
     *
     * @param date date
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }
    /**
     * Date转LocalDate
     *
     * @param date date
     * @return LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone).toLocalDate();
    }
    /**
     * 获取当前日期的字符串表示：yyyy-MM-dd HH:mm:ss
     *
     * @return 当前日期的字符串表示：yyyy-MM-dd HH:mm:ss
     */
    public static String getNowDatetimeString() {
        return DateUtil.format(DateUtil.nowDateTime(), DateUtil.PATTERN_DATETIME);
    }
    /**
     * 兼容 java 8
     * <p>The epoch year {@code LocalDate}, '1970-01-01'</p>
     */
    public static final LocalDate EPOCH = LocalDate.of(1970, 1, 1);
    public static TemporalQuery<Instant> instantQuery() {
        return INSTANT_QUERY;
    }

    /**
     *获取当前周的某一天
     * @param day
     * @return
     */
    public static String getCurrentWeekDay(int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);//取星期一的日期
        switch (day){
            case 1:break;
            case 2:calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+1);break;
            case 3:calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+2);break;
            case 4:calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+3);break;
            case 5:calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+4);break;
            case 6:calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+5);break;
            case 7:calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+6);break;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentWeekDay = format.format(calendar.getTime());
        return currentWeekDay;
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
     * 计算两个日期间相差的月份 (不考虑日对月影响，6/30 与 7/1 也相差一个月)
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 相差月份
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

    public static Date parseDate(String source, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
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

    /***********2018年03月13日 by左宇新增以下代码***********/
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
}
