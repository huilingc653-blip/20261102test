package com.bytter.core.utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
/**
 * 数值工具类
 *
 * @author zhoul
 * @since 2017/6/14.
 */
public class NumberUtil {
    /**
     * 数字转字符串
     *
     * @param numeric
     *            数字
     * @param pattern
     *            格式
     * @return 转换后字符串
     */
    public static String formatNumeric(double numeric, String pattern) {
        if (numeric == -0) {
            numeric = 0;
        }
        DecimalFormat decFormat = new DecimalFormat(pattern);
        return decFormat.format(numeric);
    }
    /**
     * 数字转字符串
     *
     * @param numeric
     *            数字
     * @param pattern
     *            格式
     * @return 转换后字符串
     */
    public static String formatNumeric(BigDecimal numeric, String pattern) {
        if (numeric == null) {
            numeric = BigDecimal.ZERO;
        }
        return formatNumeric(numeric.doubleValue(), pattern);
    }
    /**
     * 数字转逗号分隔字符串
     *
     * @param numeric
     *            数字
     * @return 转换后字符串
     */
    public static String formatNumeric(double numeric) {
        return formatNumeric(numeric, "#,##0.00");
    }
    /**
     * 数字转逗号分隔字符串
     *
     * @param numeric
     *            数字
     * @return 转换后字符串
     */
    public static String formatNumeric(BigDecimal numeric) {
        if (numeric == null) {
            numeric = BigDecimal.ZERO;
        }
        return formatNumeric(numeric.doubleValue());
    }
    /**
     * 数字转逗号分隔字符串,附加小数位数(保留8位小数，那么dec参数为6，即，最少要有2位小数)
     *
     * @param numeric
     *            数字
     * @param dec
     *            小数位数
     * @return 字符串
     */
    public static String formatNumeric(double numeric, int dec) {
        String p = "";
        for (int i = 0; i < dec; i++) {
            p += "#";
        }
        return formatNumeric(numeric, "#,##0.00" + p);
    }
    /**
     * 数字转逗号分隔字符串,附加小数位数(保留8位小数，那么dec参数为6，即，最少要有2位小数)
     *
     * @param numeric
     *            数字
     * @param dec
     *            小数位数
     * @return 字符串
     */
    public static String formatNumeric(BigDecimal numeric, int dec) {
        if (numeric == null) {
            numeric = BigDecimal.ZERO;
        }
        return formatNumeric(numeric.doubleValue(), dec);
    }
    /**
     * 数字转逗号分隔字符串；如果数字为零，则返回空
     *
     * @param numeric
     *            数字
     * @return 字符串
     */
    public static String formatNumericEx(double numeric) {
        String result;
        if (numeric != 0) {
            result = formatNumeric(numeric);
        } else {
            result = "0.00";
        }
        return result;
    }
    /**
     * 数字转逗号分隔字符串；如果数字为零，则返回空
     *
     * @param numeric
     *            数字
     * @return 字符串
     */
    public static String formatNumericEx(BigDecimal numeric) {
        if (numeric == null) {
            numeric = BigDecimal.ZERO;
        }
        return formatNumericEx(numeric.doubleValue());
    }
    /**
     * 将大数字格式化为字符串，避免以科学计数法显示
     *
     * @param numeric
     *            数字
     * @return 字符串
     */
    public static String formatDouble(double numeric) {
        return formatNumeric(numeric, "#0.00");
    }
    /**
     * 将大数字格式化为字符串，避免以科学计数法显示
     *
     * @param numeric
     *            数字
     * @return 字符串
     */
    public static String formatDouble(BigDecimal numeric) {
        if (numeric == null) {
            numeric = BigDecimal.ZERO;
        }
        return formatDouble(numeric.doubleValue());
    }
    /**
     * 将大数字格式化为字符串，避免以科学计数法显示5位
     *
     * @param numeric
     *            数字
     * @return 字符串
     */
    public static String formatDoubles(double numeric) {
        return formatNumeric(numeric, "#0.00000");
    }
    /**
     * 将数字格式化为字符串
     *
     * @param numeric
     *            数字
     * @param dec
     *            小数位
     * @return 字符串
     */
    public static String formatDoubles(BigDecimal numeric, int dec) {
        String p = "";
        if (dec < 2) {
            dec = 2;
        }
        for (int i = 0; i < dec; i++) {
            p += "0";
        }
        return formatNumeric(numeric, "#0." + p);
    }
    /**
     * 数字转字符串
     *
     * @param numeric
     *            数字
     * @return 字符串
     */
    public static String formatDoubles(BigDecimal numeric) {
        if (numeric == null) {
            numeric = BigDecimal.ZERO;
        }
        return formatNumeric(numeric.doubleValue(), "#0.00000");
    }
    /**
     * 将数字格式化为字符串，避免以科学计数法显示6位
     *
     * @param numeric
     *            数字
     * @return 字符串
     */
    public static String formatDoubleWithSix(double numeric) {
        return formatNumeric(numeric, "#0.000000");
    }
    /**
     * 将数字格式化为字符串，避免以科学计数法显示6位
     *
     * @param numeric
     *            数字
     * @return 字符串
     */
    public static String formatDoubleWithSix(BigDecimal numeric) {
        if (numeric == null) {
            numeric = BigDecimal.ZERO;
        }
        return formatNumeric(numeric.doubleValue(), "#0.000000");
    }
    /**
     * 将形如234，567.00的逗号分隔字符串格式化为Double
     *
     * @param str
     *            字符串
     * @return 数字
     */
    public static double formatNumeric(String str) {
        try {
            return (new DecimalFormat("#,##0.00######")).parse(str).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }
    /**
     * 数据非空转换为零
     *
     * @param numeric
     *            数字
     * @return BigDecimal
     */
    public static BigDecimal formatNumericNull(BigDecimal numeric) {
        if (numeric == null) {
            numeric = BigDecimal.ZERO;
        }
        return numeric;
    }
    /**
     * Object转换为BigDecimal
     *
     * @param objNum
     *            对象
     * @return BigDecimal
     */
    public static BigDecimal objToBigDecimal(Object objNum) {
        BigDecimal num = null;
        if (objNum != null) {
            if (objNum instanceof BigDecimal) {
                num = (BigDecimal) objNum;
            } else if (objNum instanceof Integer) {
                num = new BigDecimal((Integer) objNum);
            } else if (objNum instanceof Double) {
                num = new BigDecimal((Double) objNum);
            } else if (objNum instanceof String) {
                num = new BigDecimal((String) objNum);
            } else if (objNum instanceof BigInteger) {
                num = new BigDecimal((BigInteger) objNum);
            }
        }
        return num;
    }
    public static void main(String[] args) {
        System.out.println(NumberUtil.formatDouble(9.435));
    }
    /**
     * 计算环比增长率=（本期数-上期数）/上期数 × 100 %
     *
     * @param curMonth
     *            本期数
     * @param lastMonth
     *            上期数
     * @return
     */
    public static BigDecimal calculateMonthOnMonth(BigDecimal curMonth, BigDecimal lastMonth) {
        // 月环比：环比增长率=（本期数-上期数）/上期数 × 100 %
        if (lastMonth.compareTo(BigDecimal.ZERO) == 0) {
            if (curMonth.compareTo(BigDecimal.ZERO) == 0) {
                // 本期数=0，上期数=0，月环比=0%
                return BigDecimal.ZERO;
            } else {
                // 本期数=0，上期数>0，月环比=100%
                return new BigDecimal(100);
            }
        } else {
            if (curMonth.compareTo(BigDecimal.ZERO) == 0) {
                // 本期数=0，上期数>0，月环比=-100%
                return new BigDecimal(-100);
            } else {
                // 本期数>0，上期数>0，月环比=（本期数-上期数）/上期数 × 100
                BigDecimal difBalance = curMonth.subtract(lastMonth);
                return difBalance.divide(lastMonth, 4, BigDecimal.ROUND_UP).multiply(new BigDecimal(100));
            }
        }
    }
    /**
     * 计算同比增长率=（本期数－同期数）÷ |同期数| × 100 %
     *
     * @param curYear
     *            本期数
     * @param lastYear
     *            上期数
     * @return
     */
    public static BigDecimal calculateYearOnYear(BigDecimal curYear, BigDecimal lastYear) {
        if (curYear.intValue() == 0 && lastYear.intValue() != 0) {
            return new BigDecimal(-100.00);
        }
        if (lastYear.intValue() == 0 && curYear.intValue() != 0) {
            return new BigDecimal(+100.00);
        }
        BigDecimal yearOnYear = curYear.subtract(lastYear);
        if (yearOnYear.intValue() == 0) {
            return yearOnYear;
        }
        yearOnYear = yearOnYear.divide(lastYear.abs(), 4, BigDecimal.ROUND_HALF_UP);
        yearOnYear = yearOnYear.multiply(new BigDecimal(100));
        return yearOnYear;
    }
}
