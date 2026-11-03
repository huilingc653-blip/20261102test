package com.bytter.core.utils;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * 设置字符串占位符自动替换MAP值
 */
public class PlaceholderUtils {
    /**
     * 
     * Prefix for system property placeholders: "${"
     */
    public static final String PLACEHOLDER_PREFIX = "${";
    /**
     * Suffix for system property placeholders: "}"
     */
    public static final String PLACEHOLDER_SUFFIX = "}";
    public static String resolvePlaceholders(String text, Map<String, Object> parameter) {
        if (parameter == null || parameter.isEmpty()) {
            return text;
        }
        StringBuffer buf = new StringBuffer(text);
        int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
        while (startIndex != -1) {
            int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
            if (endIndex != -1) {
                String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                try {
                    Object propVal = parameter.get(placeholder);
                    // if (propVal != null) {
                    String valStr = setValueStr(propVal);
                    buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), valStr);
                    nextIndex = startIndex + valStr.toString().length();
                    // }
                } catch (Exception ex) {
                }
                startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
            } else {
                startIndex = -1;
            }
        }
        return buf.toString();
    }
    private static String setValueStr(Object propVal) {
        if (propVal != null) {
            if (propVal instanceof String || propVal instanceof Integer) {
                return propVal.toString();
            } else if (propVal instanceof Date) {
                return DateUtils.date2Str((Date) propVal, DateUtils.date_sdf());
            } else if (propVal instanceof Timestamp) {
                return DateUtils.date2Str((Timestamp) propVal, DateUtils.date_sdf());
            } else if (propVal instanceof java.sql.Date) {
                return DateUtils.date2Str((java.sql.Date) propVal, DateUtils.date_sdf());
            } else if (propVal instanceof BigDecimal) {
                return NumberUtil.formatDouble((BigDecimal) propVal);
            } else if (propVal instanceof Double) {
                return NumberUtil.formatDouble((Double) propVal);
            } else if (propVal instanceof Float) {
                return NumberUtil.formatDouble((Float) propVal);
            }
        }
        return "";
    }
    public static void main(String[] args) {
        String aa = "我们都是好孩子,${num}你说是吗？ 我认为${people}不是!"
                + " date1:${date1}===date2:${date2}===date3:${date3}====num:${num}==num1:${num1}==num2:${num2}==num3:${num3}";
        Map map = new HashMap();
        // map.put("num","小二");
        map.put("people", "小明");
        map.put("date1", new Date());
        map.put("date2", new java.sql.Date(new Date().getTime()));
        map.put("date3", new Timestamp(new Date().getTime()));
        // map.put("num",123);
        map.put("num1", 11111111.12223D);
        map.put("num2", 12222222.12333F);
        map.put("num3", new BigDecimal(4444444444444.12344D));
        System.out.println(PlaceholderUtils.resolvePlaceholders(aa, map));
    }
}
