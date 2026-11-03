package com.bytter.olap.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenlw on 2019/05/24  9:14.
 */
public class SqlUtils {


    /**
     * 解析Freemarker语法的SQL语句
     *
     * @param freemarkerSql freemarker语法的SQL语句
     * @param sqlParams     参数映射对象（传参可为null）
     * @return SQL语句
     */
    public static String resolveFreemarkerSql(String freemarkerSql, Map<String, Object> sqlParams) throws TemplateException, IOException {
        String sql = freemarkerSql;

        // 1、加载String模板串
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("freemarkerSql", freemarkerSql);

        // 2、创建模板对象
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setTemplateLoader(stringLoader);

        try {
            // 3、获取模板对象
            Template template = cfg.getTemplate("freemarkerSql", "utf-8");
            StringWriter writer = new StringWriter();
            // 4、解析模板获得字符串
            template.process(sqlParams, writer);
            sql = writer.toString();
        } catch (TemplateException e) {
            throw new TemplateException("解析SQL语句失败，请检查SQL语句是否正确：" + e.getMessage(), e.getEnvironment());
        } catch (IOException e) {
            throw e;
        }
        return sql;
    }

    /**
     * 根据查询语句获取统计语句
     *
     * @param orgSql 查询SQL
     * @return 统计语句
     */
    public static String prepareCountSql(String orgSql) {
        orgSql = orgSql.toUpperCase();

//        int fromKeywordEndIndex = getFromKeywordEndIndex(orgSql);
//        if (fromKeywordEndIndex != -1) {
//            orgSql = orgSql.substring(fromKeywordEndIndex);
//        }
//        orgSql = StringUtils.substringBeforeLast(orgSql, "ORDER BY");
//        orgSql = StringUtils.substringBeforeLast(orgSql, "GROUP BY");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ")
                .append(" ( ")
                .append(orgSql)
                .append(" )");
        return sql.toString();
    }

    private static Pattern formKeywordPattern = Pattern.compile("\\sFROM\\s");

    private static int getFromKeywordEndIndex(String sql) {
        int fromKeywordEndIndex = -1;
        Matcher matcher = formKeywordPattern.matcher(sql);
        if (matcher.find()) {
            fromKeywordEndIndex = matcher.end();
        }
        return fromKeywordEndIndex;
    }

    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 组装数据集查询N条数据的SQL，当数据量过大时只加载设置的N条数据。
     *
     * @param sql        查询SQL
     * @param dbType     数据库类型（oracle,mysql,sqlserver）
     * @param limitCount 限制数目（最大查询数量）
     * @return 查询聚合数据的SQL
     * @throws Exception 异常
     */
    public static String getLimitDatasetCountSql(String sql, String dbType, int limitCount) {
        String resultSql;
        // sqlserver limit sql, selectColsStr, tableName, whereStr, oracle limit sql, mysql limit sql
        String fsql = "SELECT %s * \n FROM (\n%s\n) OLAP_TEMP_VIEW \n %s %s";
        if ("oracle".equals(dbType)) {
            // SELECT * FROM T_EMPLOYEE ROWNUM <= 6 ORDER BY FSALARY DESC
            String oracleLimitCountWhereSql = " WHERE ROWNUM <= " + limitCount + " AND ROWNUM >= 0 ";
            resultSql = String.format(fsql, "", sql, oracleLimitCountWhereSql, "");
        } else if ("mysql".equals(dbType)) {
            // SELECT * FROM T_EMPLOYEE OEDER BY FSALARY DESC LIMIT 2,1000
            String mysqlLimitCountSql = " LIMIT 0," + limitCount;
            resultSql = String.format(fsql, "", sql, "", mysqlLimitCountSql);
        } else if ("sqlserver".equals(dbType)) {
            // SELECT TOP 5 * FROM T_EMPLOYEE ORDER BY FSALARY DESC
            String sqlServerLimitCountSql = " TOP " + limitCount;
            resultSql = String.format(fsql, sqlServerLimitCountSql, sql, "", "");
        } else {
            resultSql = String.format(fsql, "", sql, "", "");
        }
        return resultSql;
    }

}
