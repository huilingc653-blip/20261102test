package com.bytter.olap.dataprovider.util;

import com.bytter.olap.dataprovider.config.*;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.bytter.olap.dataprovider.DataProvider.NULL_STRING;
import static com.bytter.olap.dataprovider.DataProvider.separateNull;


/**
 * Created by zyong on 2017/9/15.
 */
public class SqlHelper {

    private String tableName;

    /**
     * 是否是子查询
     */
    private boolean isSubquery;
    private SqlSyntaxHelper sqlSyntaxHelper = new SqlSyntaxHelper();

    public SqlHelper() {
    }

    public SqlHelper(String tableName, boolean isSubquery) {
        this.tableName = tableName;
        this.isSubquery = isSubquery;
    }

//    public String assembleFilterSql(AggConfig config) {
//        String whereStr = null;
//        if (config != null) {
//            Stream<DimensionConfig> c = config.getColumns().stream();
//            Stream<DimensionConfig> r = config.getRows().stream();
//            Stream<ConfigComponent> f = config.getFilters().stream();
//            Stream<ConfigComponent> filters = Stream.concat(Stream.concat(c, r), f);
//            whereStr = filterSql(filters, "WHERE");
//        }
//        return whereStr;
//    }

    /**
     * 组装条件过滤的SQL
     *
     * 当没有设置过滤条件时，则返回""
     *
     * Created by chenlw on 2019/05/13.（去除JAVA8特性）
     *
     * @param config 图表聚合配置对象
     * @return 过滤器的SQL
     */
    public String assembleFilterSql(AggConfig config) {
        String whereStr = null;
        if (config != null) {
            List<DimensionConfig> dimensionConfigList = new ArrayList<>();
            dimensionConfigList.addAll(config.getColumns());
            dimensionConfigList.addAll(config.getRows());
            List<ConfigComponent> filters = new ArrayList<>();
            filters.addAll(config.getFilters());
            for (DimensionConfig dimensionConfig : dimensionConfigList) {
                filters.add(dimensionConfig);
            }
            whereStr = filterSql(filters, "WHERE");
        }
        return whereStr;
    }


    /**
     * Created by chenlw on 2019/05/13.（去除JAVA8特性）
     *
     * @param filters
     * @return
     */
    public String assembleFilterSql(List<ConfigComponent> filters) {
        return filterSql(filters, "WHERE");
    }



    /**
     * 根据图表聚合的配置组装查询聚合数据的SQL
     *
     * （1）通过维度元素（行维、列维）拼接选择的字段字符串，如select XXX,XXX,XXX,XXX。
     * （2）通过维度元素中的条件过滤拼接WHERE子句。
     * （3）通过维度元素拼接GROUP BY子句。
     *
     * 看板，图表查询数据会调用此方法
     * Created by chenlw on 2019/05/13.（去除JAVA8特性）
     *
     * @param config    图表聚合配置对象
     * @return 数据查询的SQL
     * @throws Exception  异常
     */
    public String assembleAggDataSql(AggConfig config) throws Exception {
        List<DimensionConfig> dimensionConfigList = new ArrayList<>();
        dimensionConfigList.addAll(config.getColumns());
        dimensionConfigList.addAll(config.getRows());

        List<ConfigComponent> filters = new ArrayList<>();
        filters.addAll(config.getFilters());
        for (DimensionConfig dimensionConfig : dimensionConfigList) {
            filters.add(dimensionConfig);
        }
        // 维度列字符串（XXX,XXX,XXX）
        String dimColsStr = assembleDimColumns(dimensionConfigList);
        // 指标列字符串（如SUM(XXX),AVG(XXX),COUNT(XXX)）
        String aggColsStr = assembleAggValColumns(config.getValues());

        // 拼接条件过滤的WHERE子句
        String whereStr = filterSql(filters, "WHERE");
        // 拼接GROUP BY子句
        String groupByStr = StringUtils.isBlank(dimColsStr) ? "" : "GROUP BY " + dimColsStr;

        // 拼接选择的列名字符串
        StringBuilder selectColsStr = new StringBuilder();
        if (!StringUtils.isBlank(dimColsStr)) {
            selectColsStr.append(dimColsStr);
        }
        if (!StringUtils.isBlank(aggColsStr)) {
            selectColsStr.append(",").append(aggColsStr);
        }
        String fsql = null;
        if (isSubquery) {
            fsql = "\nSELECT %s \n FROM (\n%s\n) cb_view \n %s \n %s";
        } else {
            fsql = "\nSELECT %s \n FROM %s \n %s \n %s";
        }
        String exec = String.format(fsql, selectColsStr, tableName, whereStr, groupByStr);
        return exec;
    }

    /**
     * 组装查询SQL
     *
     * @param config 图表聚合配置对象
     * @return 数据查询的SQL
     */
    public String assembleCountQuerySql(AggConfig config) {
        List<DimensionConfig> dimensionConfigList = new ArrayList<>();
        dimensionConfigList.addAll(config.getColumns());
        dimensionConfigList.addAll(config.getRows());

        List<ConfigComponent> filters = new ArrayList<>();
        filters.addAll(config.getFilters());
        for (DimensionConfig dimensionConfig : dimensionConfigList) {
            filters.add(dimensionConfig);
        }
        // 拼接条件过滤的WHERE子句
        String whereStr = filterSql(filters, "WHERE");

        String fsql = "%s \n %s";
        String exec = String.format(fsql, tableName, whereStr);
        return exec;
    }


    /**
     * 将行维、列维的过滤条件配置转化成SQL语句，实现条件过滤。
     *
     * Created by chenlw on 2019/05/13.（去除JAVA8特性）
     *
     * @param filterListTemp 条件过滤配置项
     * @param prefix SQL前缀
     * @return 条件过滤SQL
     */
    private String filterSql(List<ConfigComponent> filterListTemp, String prefix) {
        List<ConfigComponent> filterList = new ArrayList<>();
        for (ConfigComponent configComponent : filterListTemp) {
            filterList.add(separateNull(configComponent));
        }
        if (filterList.size() > 0) {
            // 注意：当configComponentToSql(filterList.get(i))为空时，where不要添加prefix
            boolean addPrefixFlag = false;
            StringBuilder where = new StringBuilder();
            String str;
            List<String> whereList = new ArrayList<>();
            for (int i = 0, size = filterList.size(); i < size; i++) {
                if (filterList.get(i) == null) {
                    continue;
                }
                str = configComponentToSql(filterList.get(i));
                if (str != null) {
                    whereList.add(str);
                }
            }

            for (int i = 0, size = whereList.size(); i < size; i++) {
                if (!addPrefixFlag) {
                    where.append(prefix + " ");
                    addPrefixFlag = true;
                }
                if (i == size - 1) {
                    where.append(whereList.get(i));
                    break;
                } else {
                    where.append(whereList.get(i)).append("\nAND ");
                }
            }
            where.append("");
            return where.toString();
        }
        return "";
    }


    /**
     * 配置项转化成SQL
     *
     * Created by chenlw on 2019/05/13.（去除JAVA8特性）
     *
     * @param cc 配置项
     * @return SQL
     */
    private String configComponentToSql(ConfigComponent cc) {
        if (cc instanceof DimensionConfig) {
            String sql = filter2SqlCondition((DimensionConfig) cc);
            return sql;
        } else if (cc instanceof CompositeConfig) {
            CompositeConfig compositeConfig = (CompositeConfig) cc;

            List<ConfigComponent> configComponentListTemp = compositeConfig.getConfigComponents();
            List<ConfigComponent> configComponentList = new ArrayList<>();
            for (ConfigComponent configComponent : configComponentListTemp) {
                configComponentList.add(separateNull(configComponent));
            }
            StringBuilder sql = new StringBuilder();
            for (int i = 0, size = configComponentList.size(); i < size; i++) {
                if (i == size - 1) {
                    sql.append(configComponentToSql(configComponentList.get(i)));
                    break;
                } else {
                    sql.append(configComponentToSql(configComponentList.get(i)))
                            .append(" " + compositeConfig.getType() + " ");
                }
            }
            return "(" + sql.toString() + ")";
        }
        return null;
    }



    /**
     * 将条件过滤配置转化成为sql语句
     * Parser a single filter configuration to sql syntax
     *
     * @param config 维度配置项
     * @return 过滤SQL语句
     */
    private String filter2SqlCondition(DimensionConfig config) {
        if (config.getValues().size() == 0) {
            return null;
        }

        String fieldName = sqlSyntaxHelper.getProjectStr(config);
        String v0 = sqlSyntaxHelper.getDimMemberStr(config, 0);
        String v1 = null;
        if (config.getValues().size() == 2) {
            v1 = sqlSyntaxHelper.getDimMemberStr(config, 1);
        }

        if (NULL_STRING.equals(config.getValues().get(0))) {
            switch (config.getFilterType()) {
                case "=":
                case "≠":
                    return config.getColumnName() + ("=".equals(config.getFilterType()) ? " IS NULL" : " IS NOT NULL");
            }
        }

        switch (config.getFilterType()) {
            case "=":
            case "eq":
                return fieldName + " IN (" + valueList(config) + ")";
            case "≠":
            case "ne":
                return fieldName + " NOT IN (" + valueList(config) + ")";
            case ">":
                return rangeQuery(fieldName, v0, null);
            case "<":
                return rangeQuery(fieldName, null, v0);
            case "≥":
                return rangeQuery(fieldName, v0, null, true, true);
            case "≤":
                return rangeQuery(fieldName, null, v0, true, true);
            case "(a,b]":
                return rangeQuery(fieldName, v0, v1, false, true);
            case "[a,b)":
                return rangeQuery(fieldName, v0, v1, true, false);
            case "(a,b)":
                return rangeQuery(fieldName, v0, v1, false, false);
            case "[a,b]":
                return rangeQuery(fieldName, v0, v1, true, true);
        }
        return null;
    }


//    private String valueList(DimensionConfig config) {
//        String resultList = IntStream.range(0, config.getValues().size())
//                .boxed()
//                .map(i -> sqlSyntaxHelper.getDimMemberStr(config, i))
//                .collect(Collectors
//                        .joining(","));
//        return resultList;
//    }


    /**
     * valueList
     * Created by chenlw on 2019/05/13.（去除JAVA8特性）
     *
     * @param config 维度对象
     * @return
     */
    private String valueList(DimensionConfig config) {
        StringBuilder resultList = new StringBuilder();
        List<String> valueList = config.getValues();
        if (valueList != null && valueList.size() > 0) {
            for (int i = 0, size = valueList.size(); i < size; i++) {
                if (valueList.get(i) == null) {
                    continue;
                }
                if (i == size - 1) {
                    resultList.append(sqlSyntaxHelper.getDimMemberStr(config, i));
                    break;
                } else {
                    resultList.append(sqlSyntaxHelper.getDimMemberStr(config, i)).append(",");
                }
            }
        }

        return resultList.toString();
    }


    private String rangeQuery(String fieldName, Object from, Object to, boolean includeLower, boolean includeUpper) {
        StringBuffer result = new StringBuffer();
        result.append("(");
        final String gt = ">",
                gte = ">=",
                lt = "<",
                lte = "<=";
        if (from != null) {
            String op = includeLower ? gte : gt;
            result.append(fieldName + op + from);
        }
        if (to != null) {
            if (from != null) {
                result.append(" AND ");
            }
            String op = includeUpper ? lte : lt;
            result.append(fieldName + op + to);
        }
        result.append(")");
        return result.toString();
    }

    private String rangeQuery(String fieldName, Object from, Object to) {
        return rangeQuery(fieldName, from, to, false, false);
    }

    public static String surround(String text, String quta) {
        return quta + text + quta;
    }

//    private String assembleAggValColumns(Stream<ValueConfig> selectStream) {
//        StringJoiner columns = new StringJoiner(", ", "", " ");
//        columns.setEmptyValue("");
//        selectStream.map(vc -> sqlSyntaxHelper.getAggStr(vc)).filter(e -> e != null).forEach(columns::add);
//        return columns.toString();
//    }

    /**
     * 将指标列拼接成   XXX(XX),XXX(XX)的字符串表示
     *
     * assembleAggValColumns
     * Created by chenlw on 2019/05/13.（去除JAVA8特性）
     *
     * @param selectList 维度列
     * @return XXX(XX), XXX(XX)的字符串表示
     */
    private String assembleAggValColumns(List<ValueConfig> selectList) {
        StringBuilder columns = new StringBuilder("");
        if (selectList != null && selectList.size() > 0) {
            String appStr;
            for (int i = 0, size = selectList.size(); i < size; i++) {
                if (selectList.get(i) == null) {
                    continue;
                }
                appStr = sqlSyntaxHelper.getAggStr(selectList.get(i));
                if (appStr != null) {
                    if (i == size - 1) {
                        columns.append(appStr);
                        break;
                    } else {
                        columns.append(appStr).append(", ");
                    }
                }
            }
            columns.append(" ");
        }
        return columns.toString();
    }


//    private String assembleDimColumns(Stream<DimensionConfig> columnsStream) {
//        StringJoiner columns = new StringJoiner(", ", "", " ");
//        columns.setEmptyValue("");
//        columnsStream.map(g -> sqlSyntaxHelper.getProjectStr(g)).distinct().filter(e -> e != null).forEach(columns::add);
//        return columns.toString();
//    }

    /**
     * 组装维度列
     *
     * assembleDimColumns
     * Created by chenlw on 2019/05/13.（去除JAVA8特性）
     *
     * @param columnsConfigList
     * @return
     */
    private String assembleDimColumns(List<DimensionConfig> columnsConfigList) {
        StringBuilder columns = new StringBuilder("");
        if (columnsConfigList != null && columnsConfigList.size() > 0) {
            Set<String> projectStrSet = new HashSet<>();
            String projectStr;
            for (int i = 0, size = columnsConfigList.size(); i < size; i++) {
                if (columnsConfigList.get(i) == null) {
                    continue;
                }
                projectStr = sqlSyntaxHelper.getProjectStr(columnsConfigList.get(i));
                if (projectStr != null && !projectStrSet.contains(projectStr)) {
                    projectStrSet.add(projectStr);
                    if (i == size - 1) {
                        columns.append(projectStr);
                        break;
                    } else {
                        columns.append(projectStr).append(", ");
                    }
                }
            }
            columns.append(" ");
        }
        return columns.toString();
    }


    public SqlHelper setSqlSyntaxHelper(SqlSyntaxHelper sqlSyntaxHelper) {
        this.sqlSyntaxHelper = sqlSyntaxHelper;
        return this;
    }

    public SqlSyntaxHelper getSqlSyntaxHelper() {
        return this.sqlSyntaxHelper;
    }
}
