package com.bytter.core.utils;
import java.util.*;
/**
 * 判断工具类
 */
public class EmptyUtils {
    /**
     * 如果字符串为空或空串返回
     *
     * @param str
     *            字符串
     * @return boolean
     */
    public static boolean isBlank(String str) {
        return StringUtils.isBlank(str);
    }
    /**
     * 字符串不为空返回true
     *
     * @param str
     *            字符串
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }
    /**
     * 如果字符串为空返回
     *
     * @param str
     *            字符串
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }
    /**
     * 判断对象、String、Long、Integer 是否为空
     *
     * @param obj
     *            对象
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        return obj == null;
    }
    public static boolean isEmpty(Integer obj) {
        return obj == null;
    }
    public static boolean isEmpty(Double obj) {
        return obj == null;
    }
    public static boolean isEmpty(Float obj) {
        return obj == null;
    }
    public static boolean isEmpty(Long obj) {
        return obj == null;
    }
    public static boolean isEmpty(Byte obj) {
        return obj == null;
    }
    public static boolean isEmpty(Date obj) {
        return obj == null;
    }
    public static boolean isEmpty(Object[] obj) {
        return null == obj || obj.length == 0;
    }
    /**
     * 如果List为空返回
     *
     * @param result
     *            集合
     * @return 是否为空
     */
    public static boolean isEmptyList(List<?> result) {
        return isEmpty(result);
    }

    /**
     * 判断是否为数值类型
     *
     * @param str
     *            字符串
     * @return 是否为数值
     */
    public static boolean isNumeric(String str) {
        try {
            if (isBlank(str)) {
                return false;
            }
            double a = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * 判断数组是否为空
     *
     * @param obj
     *            数组对象
     * @return 是否为空
     */
    public static boolean isEmptyArray(Object[] obj) {
        return isEmpty(obj);
    }
    /**
     * 判断List、Map是否为空
     *
     * @param collection
     *            List、Map
     * @return 是否为空
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }
    /**
     * 判断List、Map是否不为空
     *
     * @param collection
     *            List、Map
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }
    /**
     * 如果字符串为空返回
     *
     * @param str
     *            字符串
     * @return 是否为空
     */
    public static boolean isEmpty(StringBuilder str) {
        return null == str || isBlank(str.toString());
    }
    /**
     * 删除List中重复元素
     *
     * @param list
     *            集合
     */
    public static void removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
    }
    /**
     * 删除List中重复元素，保持顺序
     *
     * @param list
     *            集合
     */
    public static void removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
    }
}
