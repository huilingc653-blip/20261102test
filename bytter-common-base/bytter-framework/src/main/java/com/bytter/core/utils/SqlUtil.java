package com.bytter.core.utils;
import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * SQL脚本工具类
 *
 * @author zuoy
 * @since 1.0
 */
public class SqlUtil {
    /**
     * 拼接占位符式的sql拼接语句
     *
     * @param values
     *            in中的内容
     * @param fieldName
     *            查询的字段名称
     * @param params
     *            sql查询参数集合
     * @return (fieldName in ( placeholder)) 或者 (fieldName in(placeholder) or fieldName in(placeholder))
     */
    public static String getInSqlByParams(List values, String fieldName, Map<String, Object> params) {
        return getInSqlByParams(values, fieldName, null, params);
    }
    /**
     * 拼接占位符式的sql拼接语句
     *
     * @param mapKey
     *            值在params的KEY
     * @param fieldName
     *            查询的字段名称
     * @param params
     *            sql查询参数集合
     * @return (fieldName in ( placeholder)) 或者 (fieldName in(placeholder) or fieldName in(placeholder))
     */
    public static String getInSqlByParams(String mapKey, String fieldName, Map<String, Object> params) {
        if (params.containsKey(mapKey)) {
            Object values = params.get(mapKey);
            if (values == null) {
                return "1<>1";
            } else if (values instanceof Object[]) {
                return getInSqlByParams((Object[]) values, fieldName, mapKey, params);
            } else if (values instanceof List) {
                return getInSqlByParams((List) values, fieldName, mapKey, params);
            } else if (values instanceof String) {
                return getInSqlByParams(String.valueOf(values).split(","), fieldName, mapKey, params);
            } else {
                return "1<>1";
            }
        } else {
            return "1<>1";
        }
    }
    /**
     * 拼接占位符式的sql拼接语句
     *
     * @param values
     *            in中的内容
     * @param fieldName
     *            查询的字段名称
     * @param params
     *            sql查询参数集合
     * @return (fieldName in ( placeholder)) 或者 (fieldName in(placeholder) or fieldName in(placeholder))
     */
    public static String getInSqlByParams(Object[] values, String fieldName, Map<String, Object> params) {
        return getInSqlByParams(values, fieldName, null, params);
    }
    /**
     * 拼接占位符式的sql拼接语句
     *
     * @param values
     *            in中的内容
     * @param fieldName
     *            查询的字段名称
     * @param placeholder
     *            占位符别名
     * @param params
     *            sql查询参数集合
     * @return (fieldName in ( placeholder)) 或者 (fieldName in(placeholder) or fieldName in(placeholder))
     */
    public static String getInSqlByParams(List values, String fieldName, String placeholder,
            Map<String, Object> params) {
        Object[] arrValues = null;
        if (values != null && values.size() > 0) {
            arrValues = values.toArray(new Object[values.size()]);
        }
        return getInSqlByParams(arrValues, fieldName, placeholder, params);
    }
    /**
     * 拼接占位符式的sql拼接语句
     *
     * @param values
     *            in中的内容
     * @param fieldName
     *            查询的字段名称
     * @param placeholder
     *            占位符别名
     * @param params
     *            sql查询参数集合
     * @return (fieldName in ( placeholder)) 或者 (fieldName in(placeholder) or fieldName in(placeholder))
     */
    public static String getInSqlByParams(Object[] values, String fieldName, String placeholder,
            Map<String, Object> params) {
        return getInOrNotInSqlByParams(values, fieldName, placeholder, params, false);
    }
    /**
     * 拼接占位符式的sql拼接语句
     *
     * @param values
     *            in中的内容
     * @param fieldName
     *            查询的字段名称
     * @param params
     *            sql查询参数集合
     * @return (fieldName not in ( placeholder)) 或者 (fieldName not in(placeholder) and fieldName not in(placeholder))
     */
    public static String getNotInSqlByParams(List values, String fieldName, Map<String, Object> params) {
        return getNotInSqlByParams(values, fieldName, null, params);
    }
    /**
     * 拼接占位符式的sql拼接语句
     *
     * @param values
     *            in中的内容
     * @param fieldName
     *            查询的字段名称
     * @param params
     *            sql查询参数集合
     * @return (fieldName not in ( placeholder)) 或者 (fieldName not in(placeholder) and fieldName not in(placeholder))
     */
    public static String getNotInSqlByParams(Object[] values, String fieldName, Map<String, Object> params) {
        return getNotInSqlByParams(values, fieldName, null, params);
    }
    /**
     * 拼接占位符式的sql拼接语句
     *
     * @param values
     *            in中的内容
     * @param fieldName
     *            查询的字段名称
     * @param placeholder
     *            占位符别名
     * @param params
     *            sql查询参数集合
     * @return (fieldName not in ( placeholder)) 或者 (fieldName not in(placeholder) and fieldName not in(placeholder))
     */
    public static String getNotInSqlByParams(List values, String fieldName, String placeholder,
            Map<String, Object> params) {
        Object[] arrValues = null;
        if (values != null && values.size() > 0) {
            arrValues = values.toArray(new Object[values.size()]);
        }
        return getNotInSqlByParams(arrValues, fieldName, placeholder, params);
    }
    /**
     * 拼接占位符式的sql拼接语句
     *
     * @param values
     *            in中的内容
     * @param fieldName
     *            查询的字段名称
     * @param placeholder
     *            占位符别名
     * @param params
     *            sql查询参数集合
     * @return (fieldName not in ( placeholder)) 或者 (fieldName not in(placeholder) and fieldName not in(placeholder))
     */
    public static String getNotInSqlByParams(Object[] values, String fieldName, String placeholder,
            Map<String, Object> params) {
        return getInOrNotInSqlByParams(values, fieldName, placeholder, params, true);
    }
    /**
     * 拼接占位符式的sql拼接语句
     *
     * @param values
     *            in中的内容
     * @param fieldName
     *            查询的字段名称
     * @param placeholder
     *            占位符别名
     * @param params
     *            sql查询参数集合
     * @param isNotin
     *            sql是否notin
     * @return (fieldName in ( placeholder)) 或者 (fieldName in(placeholder) or fieldName in(placeholder))
     */
    public static String getInOrNotInSqlByParams(Object[] values, String fieldName, String placeholder,
            Map<String, Object> params, boolean isNotin) {
        String otherSql = isNotin ? " 1=1 " : " 1<>1 ";
        if (StringUtils.isEmpty(fieldName) || params == null) {
            // throw new
        }
        if (values == null || values.length == 0) {
            return otherSql;
        }
        StringBuilder sql = new StringBuilder();
        String inOrNotIn = isNotin ? " not in " : " in ";
        String andOr = isNotin ? " and " : " or ";
        if (StringUtils.isEmpty(placeholder)) {
            placeholder = "btPlaceholderParams";
        }
        sql.append("( ");
        int numCs = 999;
        if (values.length > numCs) {
            int numSize = values.length / numCs;
            int numQY = values.length % numCs;
            if (numQY > 0) {
                numSize++;
            }
            for (int i = 0; i < numSize; i++) {
                if (i > 0) {
                    sql.append(andOr);
                }
                sql.append(fieldName).append(inOrNotIn).append("(:").append(placeholder).append(i).append(")");
                Object[] valuesNew = new Object[numCs];
                if (numQY > 0 && (i + 1) == numSize) {
                    valuesNew = new Object[numQY];
                }
                System.arraycopy(values, i * numCs, valuesNew, 0, valuesNew.length);
                params.put(placeholder + i, valuesNew);
            }
        } else {
            sql.append(fieldName).append(inOrNotIn).append("(:").append(placeholder).append(")");
            params.put(placeholder, values);
        }
        sql.append(")");
        return sql.toString();
    }
    /**
     * 构造in()查询条件，如果in()中长度超过1000则自动or in()附加 <br>
     * values为null 返回1&lt;&gt;1 valus为{null,null} 返回1&lt;&gt;1
     *
     * @param values
     *            值集合
     * @param fieldName
     *            字段名
     * @param isString
     *            字符数组中是否为字符串
     * @return (fieldName in ( 1, 2, 3, 4)) 或者 (fieldName in(1,2) or fieldName in(3,4))
     * @see #getInSqlByParams(List, String, Map)
     * @since 2016-08-24
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String getInSql(List values, String fieldName, boolean isString) {
        String[] valus = null;
        if (values != null && values.size() > 0) {
            valus = (String[]) values.toArray(new String[values.size()]);
        }
        return getInSql(valus, fieldName, isString);
    }
    /**
     * 构造not in()查询条件，如果not in()中长度超过1000则自动and not in()附加 <br>
     * values为null 返回1=1 values为{null,null} 返回1=1
     *
     * @param values
     *            值集合
     * @param fieldName
     *            字段名
     * @param isString
     *            字符数组中是否为字符串
     * @return (fieldName not in ( 1, 2, 3, 4)) 或者 (fieldName not in(1,2) and fieldName not in(3,4))
     * @see #getNotInSqlByParams(List, String, Map)
     * @since 2016-08-24
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String getNotinSql(List values, String fieldName, boolean isString) {
        String[] valus = null;
        if (values != null && values.size() > 0) {
            valus = (String[]) values.toArray(new String[values.size()]);
        }
        return getNotinSql(valus, fieldName, isString);
    }

    public static String getNotinSql(List values, String fieldName, boolean isString,boolean isRequired) {
        String[] valus = null;
        if (values != null && values.size() > 0) {
            valus = (String[]) values.toArray(new String[values.size()]);
        }
        boolean isNotin = true;
        if (isRequired && ObjectUtil.isEmpty(valus)){
            isNotin = false;
        }
        return getInOrNotinSql(valus, fieldName, isString,isNotin);
    }
    /**
     * 构造in()查询条件，如果in()中长度超过1000则自动or in()附加 <br>
     * valus为null 返回1&lt;&gt;1 valus为{null,null} 返回1&lt;&gt;1
     *
     * @param values
     *            值集合
     * @param fieldName
     *            字段名
     * @param isString
     *            字符数组中是否为字符串
     * @return (fieldName not in ( 1, 2, 3, 4)) 或者 (fieldName not in(1,2) and fieldName not in(3,4))
     * @since 2016-08-24
     */
    public static String getInSql(String[] values, String fieldName, boolean isString) {
        return getInOrNotinSql(values, fieldName, isString, false);
    }
    /**
     * 构造in()查询条件，如果in()中长度超过1000则自动or in()附加 <br>
     * valus为null 返回1=1 valus为{null,null} 返回1=1
     *
     * @param values
     *            值集合
     * @param fieldName
     *            字段名
     * @param isString
     *            字符数组中是否为字符串
     * @return (fieldName not in ( 1, 2, 3, 4)) 或者 (fieldName not in(1,2) and fieldName not in(3,4))
     * @see #getNotInSqlByParams(Object[], String, Map)
     * @since 2016-08-24
     */
    public static String getNotinSql(String[] values, String fieldName, boolean isString) {
        return getInOrNotinSql(values, fieldName, isString, true);
    }
    /**
     * 构造in()查询条件，如果in()中长度超过1000则自动or in()附加 <br>
     * valus为null 返回1&lt;&gt;1 valus为{null,null} 返回1&lt;&gt;1
     *
     * @param values
     *            值集合
     * @param fieldName
     *            字段名
     * @param isString
     *            字符数组中是否为字符串
     * @param isNotin
     *            sql是否notin
     * @return (fieldName in ( 1, 2, 3, 4)) 或者 (fieldName in(1,2) or fieldName in(3,4)) 或者 (fieldName not in(1,2,3,4))
     *         或者 (fieldName not in(1,2) and fieldName not in(3,4))
     * @since 2016-08-24
     */
    public static String getInOrNotinSql(String[] values, String fieldName, boolean isString, boolean isNotin) {
        String otherSql = isNotin ? " 1=1 " : " 1<>1 ";
        if (values == null || values.length == 0) {
            return otherSql;
        }
        StringBuilder sql = new StringBuilder();
        String inOrNotin = isNotin ? " not in " : " in ";
        String andor = isNotin ? " and " : " or ";
        sql.append("( ");
        if (isNotin) {
            sql.append(fieldName).append(" is null or ");
        }
        sql.append(fieldName).append(inOrNotin).append("( ");
        int num = 1;
        boolean flag = false;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                continue;
            }
            if (num == 1000) {
                sql.append(")").append(andor).append(fieldName).append(inOrNotin).append("( ");
                num = 1;
            }
            sql.append(num == 1 ? "" : ",");
            if (isString) {
                sql.append("'").append(escapeSqlLiteral(values[i])).append("'");
            } else {
                sql.append(values[i]);
            }
            num++;
            flag = true;
        }
        sql.append(" ))");
        return flag ? sql.toString() : otherSql;
    }

    private static String escapeSqlLiteral(String value) {
        return value == null ? "" : value.replace("'", "''");
    }

    public static void main(String[] args) {
        Object[] values = { 1, 2, 4, 5, 6, 7, 8, 9, 10 };
        Map<String, Object> params = new HashMap<>();
        String str = SqlUtil.getInSqlByParams(values, "a.test", params);
    }
    /**
     * 根据条件字段和过滤参数自动组装where查询条件,条件字段必须在过滤map的key中存在
     *
     * @param fields
     *            条件字段多个“,”隔开
     * @param params
     *            过滤条件
     * @return and 字段1=:字段1 and 字段2=:字段2
     */
    public static String getWhereByParamsAndField(String fields, Map<String, Object> params) {
        if (StringUtils.isEmpty(fields)) {
            return "";
        }
        String[] strs = StringUtils.split(fields, ",");
        return getWhereByParamsAndField(strs, params);
    }
    /**
     * 根据条件字段和过滤参数自动组装where查询条件,条件字段必须在过滤map的key中存在
     *
     * @param fields
     *            条件字段数组
     * @param params
     *            过滤条件
     * @return and 字段1=:字段1 and 字段2=:字段2
     */
    public static String getWhereByParamsAndField(String[] fields, Map<String, Object> params) {
        if (fields == null) {
            return "";
        }
        StringBuilder sqlW = new StringBuilder("");
        for (String field : fields) {
            String key = field;
            if (key.indexOf(".") != -1) {
                key = key.split("\\.")[1];
            }
            if (params.containsKey(key)) {
                sqlW.append(" and ").append(field).append("=:").append(key).append(" ");
            }
        }
        return sqlW.toString();
    }
}
