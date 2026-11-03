package com.bytter.poi.util;
import com.bytter.poi.exception.excel.ExcelExportException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
/**
 * Created by zhouj on 2017/6/2.
 */
public final class PoiFunctionUtil {
    private static final String TWO_DECIMAL_STR = "###.00";
    private static final String THREE_DECIMAL_STR = "###.000";
    private static final DecimalFormat TWO_DECIMAL = new DecimalFormat("###.00");
    private static final DecimalFormat THREE_DECIMAL = new DecimalFormat("###.000");
    private static final String DAY_STR = "yyyy-MM-dd";
    private static final String TIME_STR = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME__NO_S_STR = "yyyy-MM-dd HH:mm";
    private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat TIME__NO_S_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private PoiFunctionUtil() {
    }
    public static int length(Object obj) {
        return obj == null ? 0
                : (obj instanceof Map ? ((Map) obj).size() : (obj instanceof Collection ? ((Collection) obj).size()
                        : (obj.getClass().isArray() ? Array.getLength(obj) : String.valueOf(obj).length())));
    }
    public static String formatNumber(Object obj, String format) {
        if (obj != null && obj.toString() != "") {
            double number = Double.valueOf(obj.toString()).doubleValue();
            DecimalFormat decimalFormat = null;
            if (TWO_DECIMAL.equals(format)) {
                decimalFormat = TWO_DECIMAL;
            } else if ("###.000".equals(format)) {
                decimalFormat = THREE_DECIMAL;
            } else {
                decimalFormat = new DecimalFormat(format);
            }
            return decimalFormat.format(number);
        } else {
            return "";
        }
    }
    public static String formatDate(Object obj, String format) {
        if (obj != null && obj.toString() != "") {
            SimpleDateFormat dateFormat = null;
            if ("yyyy-MM-dd".equals(format)) {
                dateFormat = DAY_FORMAT;
            } else if ("yyyy-MM-dd HH:mm:ss".equals(format)) {
                dateFormat = TIME_FORMAT;
            } else if ("yyyy-MM-dd HH:mm".equals(format)) {
                dateFormat = TIME__NO_S_FORMAT;
            } else {
                dateFormat = new SimpleDateFormat(format);
            }
            return dateFormat.format(obj);
        } else {
            return "";
        }
    }
    public static boolean isTrue(Object first, String operator, Object second) {
        if (">".endsWith(operator)) {
            return isGt(first, second);
        } else if ("<".endsWith(operator)) {
            return isGt(second, first);
        } else if ("==".endsWith(operator)) {
            return first != null && second != null ? first.equals(second) : first == second;
        } else if ("!=".endsWith(operator)) {
            return first != null && second != null ? !first.equals(second) : first != second;
        } else {
            throw new ExcelExportException("占不支持改操作符");
        }
    }
    private static boolean isGt(Object first, Object second) {
        if (first != null && first.toString() != "") {
            if (second != null && second.toString() != "") {
                double one = Double.valueOf(first.toString()).doubleValue();
                double two = Double.valueOf(second.toString()).doubleValue();
                return one > two;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
