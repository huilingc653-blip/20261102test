package com.bytter.core.jdbc;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * MySQL SQL 文件转 Oracle SQL 文件工具类
 * 支持：
 * - DROP TABLE IF EXISTS 转 PL/SQL 块
 * - CREATE TABLE 转 Oracle 语法（联合主键、注释、索引）
 * - INSERT 批量转 INSERT INTO SELECT ... FROM DUAL UNION ALL
 * - 函数替换（MD5(UUID()) -> RAWTOHEX(SYS_GUID()) 等）
 * - 自动分批（避免 SQL 过长）
 *
 * @author zhaohc
 * @since 2026/06/08
 */
public class MySqlFileToOracleConverter {

    // 每条 INSERT 最多包含的行数
    private static final int BATCH_SIZE = 500;
    // 列元数据缓存
    private static Map<String, List<ColumnMeta>> tableMetadata = new HashMap<>();

    // ---------- 公开 API ----------
    /**
     * 转换整个 SQL 文件
     * @param inputFile  MySQL 脚本文件
     * @param outputFile Oracle 脚本输出文件
     * @throws JSQLParserException SQL 解析异常
     */
    public static void convertSqlFile(File inputFile, File outputFile) throws JSQLParserException {
        // 1. 读取文件内容
        FileReader reader = new FileReader(inputFile);
        String sqlContent = reader.readString();

        // 预处理：将 \' 转换为 ''
        sqlContent = preprocessMySqlString(sqlContent);
        // 2. 解析所有语句
        List<String> oracleStatements = new ArrayList<>();

        // 当SQL文件超出10MB，直接以分割的方式执行
        System.out.println("SQL文件大小：" + sqlContent.length());

        if (sqlContent.length() > 5 * 1024 * 1024) {
            System.out.println("SQL文件过大，使用手动分割");
            batchStmtExecute(sqlContent, oracleStatements);
        } else {
            try {
                Statements statements = CCJSqlParserUtil.parseStatements(sqlContent);
                for (Statement stmt : statements.getStatements()) {
                    String converted = convertStatement(stmt).replaceAll("`", "");
                    if (StrUtil.isNotBlank(converted)) {
                        oracleStatements.add(converted);
                    }
                }
            } catch (Exception e) {
                System.err.println("整体解析失败，使用手动分割: " + e.getMessage());
                batchStmtExecute(sqlContent, oracleStatements);
            }
        }

        // 3. 写入输出文件（每句后加分号+换行）
        FileWriter writer = new FileWriter(outputFile);
        writer.write(String.join("\n\n", oracleStatements));
        // // 确保末尾有分号（如果最后一句没有自动添加，这里追加一个分号）
        // if (!oracleStatements.isEmpty() && !oracleStatements.get(oracleStatements.size() - 1).endsWith(";")) {
        //     writer.append(";");
        // }
    }

    private static void batchStmtExecute(String sqlContent, List<String> oracleStatements) {
        // 手动分割 SQL 语句
        List<String> rawStatements = splitSqlStatements(sqlContent);
        for (String rawStmt : rawStatements) {
            rawStmt = rawStmt.trim();
            if (rawStmt.isEmpty()) continue;

            // 跳过 MySQL 特有的 LOCK/UNLOCK/ALTER TABLE ... KEYS
            if (rawStmt.startsWith("LOCK TABLES") || rawStmt.startsWith("UNLOCK TABLES")) {
                continue; // 丢弃
            }

            // 如果是存储过程/函数定义，不经过 JSqlParser，直接做文本替换
            if (isRoutineStatement(rawStmt)) {
                String converted = rawStmt.replaceAll("`", "");
                converted = replaceMySqlFunctions(converted);
                // 移除可能残留的 DELIMITER 等
                converted = preprocessMySqlString(converted);
                if (StrUtil.isNotBlank(converted)) {
                    oracleStatements.add(converted);
                }
                continue;
            }

            try {
                Statement stmt = CCJSqlParserUtil.parse(rawStmt);
                String converted = convertStatement(stmt).replaceAll("`", "");
                if (StrUtil.isNotBlank(converted)) {
                    // 移除可能残留的 Mysql语法
                    converted = preprocessMySqlString(converted);
                    oracleStatements.add(converted);
                }
            } catch (JSQLParserException ex) {
                System.err.println("单条语句解析失败，保留原文: " + (rawStmt.length() > 100 ? rawStmt.substring(0, 100) : rawStmt) + "\r\n" + ex.getMessage());
                String fallback = rawStmt.replaceAll("`", "");
                if (StrUtil.isNotBlank(fallback)) {
                    oracleStatements.add(fallback);
                }
            }
        }
    }

    /**
     * 转换单条 SQL 语句
     */
    private static String convertStatement(Statement stmt) {
        String oracleSql = null;
        if (stmt instanceof CreateTable) {
            oracleSql = convertCreateTable((CreateTable) stmt);
        } else if (stmt instanceof Drop) {
            oracleSql = convertDrop((Drop) stmt);
        } else if (stmt instanceof Insert) {
            oracleSql = convertInsert((Insert) stmt);
        } else if(stmt instanceof Delete){
            oracleSql = convertDelete((Delete) stmt);
        } else if(stmt instanceof Update){
            oracleSql = convertUpdate((Update) stmt);
        } else {
            // 其他语句原样返回（注意：原语句中的分号需自己控制，这里只返回 SQL 本体）
            oracleSql = convertGeneralStatement(stmt);
        }
        return replaceMySqlFunctions(oracleSql);
    }

    // ---------- 通用转换 ----------

    /**
     * 判断是否是存储过程/函数相关的语句（不通过 JSqlParser 解析）
     */
    private static boolean isRoutineStatement(String sql) {
        String upper = sql.toUpperCase().trim();
        return upper.startsWith("CREATE PROCEDURE") ||
                upper.startsWith("CREATE FUNCTION") ||
                upper.startsWith("DROP PROCEDURE") ||
                upper.startsWith("DROP FUNCTION") ||
                upper.startsWith("ALTER PROCEDURE") ||
                upper.startsWith("ALTER FUNCTION");
    }
    /**
     * 预处理 MySQL 脚本，移除 JSqlParser 无法解析的语法
     */
    private static String preprocessMySqlString(String sql) {
        // 移除 DELIMITER 语句（行）
        sql = sql.replaceAll("(?m)^\\s*DELIMITER\\s+\\S+\\s*$", "");
        // 移除 SQL SECURITY 子句
        sql = sql.replaceAll("(?i)\\s*SQL SECURITY\\s+(DEFINER|INVOKER)", "");
        // 移除 DEFINER 子句
        sql = sql.replaceAll("(?i)\\s*DEFINER\\s*=\\s*`?\\w+`?@`?\\w+`?", "");
        // 移除 /*! ... */ 形式的 MySQL 条件注释
        sql = sql.replaceAll("/\\*![0-9 ]+.*?\\*/", "");
        // 将 \' 转换为 ''
        sql = sql.replaceAll("\\\\'", "''");
        // 移除以 -- 开头的单行注释（注意忽略字符串内的 --）
        sql = sql.replaceAll("(?m)^\\s*--.*?$", "");
        // 移除反引号（可选，但在转换中会单独处理）
        // sql = sql.replaceAll("`", "");
        return sql;
    }

    /**
     * 按分号分割 SQL 语句，正确处理字符串和注释内的分号
     */
    private static List<String> splitSqlStatements(String sql) {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;
        char prev = 0;

        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);

            // 处理块注释开始
            if (!inString && !inLineComment && !inBlockComment && c == '/' && i+1 < sql.length() && sql.charAt(i+1) == '*') {
                inBlockComment = true;
                i++; // skip '*'
                continue;
            }
            // 处理块注释结束
            if (inBlockComment && c == '*' && i+1 < sql.length() && sql.charAt(i+1) == '/') {
                inBlockComment = false;
                i++; // skip '/'
                continue;
            }
            // 处理行注释 --
            if (!inString && !inBlockComment && !inLineComment && c == '-' && i+1 < sql.length() && sql.charAt(i+1) == '-') {
                inLineComment = true;
                i++; // skip second '-'
                continue;
            }
            if (inLineComment && c == '\n') {
                inLineComment = false;
                continue;
            }
            // 处理字符串开始/结束（简单处理，不考虑转义）
            if (!inBlockComment && !inLineComment && c == '\'' && prev != '\\') {
                inString = !inString;
            }

            if (!inString && !inLineComment && !inBlockComment && c == ';') {
                String stmt = current.toString().trim();
                if (!stmt.isEmpty()) {
                    statements.add(stmt);
                }
                current.setLength(0);
            } else if (!inLineComment && !inBlockComment) {
                current.append(c);
            }
            prev = c;
        }
        String lastStmt = current.toString().trim();
        if (!lastStmt.isEmpty()) {
            statements.add(lastStmt);
        }
        return statements;
    }

    private static String convertGeneralStatement(Statement stmt) {
        String sql = stmt.toString();
        // 跳过 MySQL 特有的 LOCK/UNLOCK/ALTER TABLE ... KEYS
        if (StrUtil.startWithIgnoreCase(sql, "LOCK TABLES")|| StrUtil.startWithIgnoreCase(sql, "UNLOCK TABLES")) {
            return ""; // 丢弃
        }
        // 跳过 MySQL 特有的 SET FOREIGN_KEY_CHECKS 语句
        if (StrUtil.startWithIgnoreCase(sql, "SET FOREIGN_KEY_CHECKS")) {
            return ""; // 丢弃
        }
        return sql.replaceAll("`", "").concat(";");
    }

    // ---------- CREATE TABLE 转换 ----------
    private static String convertCreateTable(CreateTable createTable) {
        String tableName = createTable.getTable().getName().replaceAll("`", "");
        tableName = transformTableName(tableName);
        // if (tableName.length() > 30) {
        //     System.err.println("Create语法跳过，表名长度超过30，请检查：" + tableName);
        //     return "";
        // }
        List<ColumnDefinition> columns = createTable.getColumnDefinitions();
        List<Index> indexes = createTable.getIndexes();

        // 缓存元数据（注意：需要传入截断后的表名）
        cacheTableMetadata(createTable, tableName);

        // 1. 提取主键列（仅支持单列主键）
        String primaryKeyColumn = null;
        Index primaryKeyIndex = null;
        List<Index> foreignKeyIndexes = new ArrayList<>();
        Set<List<String>> primaryKeyColumnSets = new HashSet<>();
        if (indexes != null) {
            for (Index idx : indexes) {
                if ("PRIMARY KEY".equalsIgnoreCase(idx.getType())) {
                    List<String> pkCols = idx.getColumnsNames();
                    primaryKeyColumnSets.add(pkCols);
                    if (pkCols != null && !pkCols.isEmpty()) {
                        primaryKeyColumn = pkCols.get(0).replaceAll("`", "");
                        if (pkCols.size() > 1) {
                            primaryKeyIndex = idx;
                        }
                    }
                    // break;
                } else if ("FOREIGN KEY".equalsIgnoreCase(idx.getType())) {
                    foreignKeyIndexes.add(idx);
                }
            }
        }

        // 2. 生成列定义
        List<String> columnLines = new ArrayList<>();
        for (ColumnDefinition col : columns) {
            String colName = safeColumnName(col.getColumnName());
            String dataType = mapDataType(col.getColDataType());
            StringBuilder line = new StringBuilder("    ").append(colName).append(" ").append(dataType);

            // DEFAULT
            String defaultVal = extractDefaultValue(col);
            if (defaultVal != null && !"NULL".equalsIgnoreCase(defaultVal)) {
                line.append(" DEFAULT ").append(defaultVal);
            }

            // NOT NULL
            boolean isNotNull = false;
            List<String> specs = col.getColumnSpecs();
            if (specs != null) {
                for (int i = 0; i < specs.size() - 1; i++) {
                    if ("NOT".equalsIgnoreCase(specs.get(i)) && "NULL".equalsIgnoreCase(specs.get(i + 1))) {
                        isNotNull = true;
                        break;
                    }
                }
            }
            line.append(isNotNull ? " NOT NULL" : " NULL");

            // PRIMARY KEY (列级)
            if (colName.equals(primaryKeyColumn) && primaryKeyIndex == null) {
                line.append(" PRIMARY KEY");
            }

            columnLines.add(line.toString());
        }

        // 生成表级约束前缀
        String preTable = tableName;
        // 如果表名长度已超出25个字符，则根据驼峰截取各个首字母
        if (preTable.length() >= 25){
            preTable = subTableName(preTable);
        }

        // 多主键约束特殊处理
        if (primaryKeyIndex != null) {
            String pkConstraint = "    CONSTRAINT PK_" + preTable + " PRIMARY KEY (" +
                    String.join(", ", primaryKeyIndex.getColumnsNames()) + ")";
            columnLines.add(pkConstraint);
        }
        // PRIMARY KEY (表级)
        if (!foreignKeyIndexes.isEmpty()) {
            for (Index idx : foreignKeyIndexes) {
                // 忽略 ON DELETE RESTRICT ON UPDATE RESTRICT
                String fkConstraint = "    " + idx.toString().replace("ON DELETE RESTRICT ON UPDATE RESTRICT", "")
                        .replace("ON DELETE CASCADE ON UPDATE CASCADE", "");
                columnLines.add(fkConstraint);
            }
        }

        // 3. 表注释
        String tableComment = extractTableComment(createTable);

        // 4. 拼接 CREATE TABLE
        StringBuilder ddl = new StringBuilder();
        ddl.append("CREATE TABLE ").append(tableName).append(" (\n");
        ddl.append(String.join(",\n", columnLines));
        ddl.append("\n);\r\n");
        if (tableComment != null && !tableComment.isEmpty()) {
            ddl.append("\n");
            ddl.append("COMMENT ON TABLE ").append(tableName).append(" IS '")
                    .append(escapeSingleQuote(tableComment)).append("';");
        }

        // 5. 列注释
        StringBuilder comments = new StringBuilder("\n");
        for (ColumnDefinition col : columns) {
            String colComment = extractColumnComment(col);
            if (colComment != null && !colComment.isEmpty()) {
                comments.append("COMMENT ON COLUMN ").append(tableName).append(".")
                        .append(safeColumnName(col.getColumnName())).append(" IS '")
                        .append(escapeSingleQuote(colComment)).append("';\n");
            }
        }
        ddl.append(comments);

        // 6. 普通索引
        StringBuilder indexesSql = generateIndexSql(indexes, primaryKeyColumnSets, preTable, tableName);
        ddl.append(indexesSql);

        // 生成表结构结束
        ddl.append("\n");
        ddl.append("-- Dump completed for table '").append(tableName).append("'\n");
        return ddl.toString();
    }

    // ---------- DROP TABLE 转换 ----------
    private static String convertDrop(Drop drop) {
        // 只处理 DROP TABLE
        if (!"TABLE".equalsIgnoreCase(drop.getType())) {
            return drop.toString();
        }
        String tableName = drop.getName().getName().replaceAll("`", "");
        tableName = transformTableName(tableName);
        // if (tableName.length() > 30) {
        //     System.err.println("Drop语法跳过，表名长度超过30，请检查：" + tableName);
        //     return "";
        // }
        // 判断是否是 DROP TABLE IF EXISTS（JSqlParser 解析后不会保留 IF EXISTS，但我们可以通过原始 SQL 判断）
        // 为了通用，这里统一生成安全删除块（表存在则删除，不存在不报错）
        String tablePrefix = "--\n" +
                "-- Table structure for table '" + tableName + "'\n" +
                "--\r\n";
        // 判断是否需要添加外键级联删除
        String dropSuffix = "";
        if (tableName.toUpperCase().startsWith("ACT_") || tableName.toUpperCase().startsWith("QRTZ_")) {
            dropSuffix = " CASCADE CONSTRAINTS";
        }
        return tablePrefix +
                "BEGIN\n" +
                "    EXECUTE IMMEDIATE 'DROP TABLE " + tableName + dropSuffix + "';\n" +
                "EXCEPTION WHEN OTHERS THEN NULL;\n" +
                "END;\n"+
                "/";
    }

    // ---------- INSERT INTO 转换 ----------
    private static String convertInsert(Insert insert) {
        // 获取表名（去掉反引号）
        String tableName = insert.getTable().getName().replaceAll("`", "");
        tableName = transformTableName(tableName);
        // if (tableName.length() > 30) {
        //     System.err.println("Insert语法跳过，表名长度超过30，请检查：" + tableName);
        //     return "";
        // }
        // 获取列名
        List<Column> columns = insert.getColumns();
        if (columns == null || columns.isEmpty()) {
            // 没有列名列表，需要从值个数推断？这里简单返回原始 SQL 去掉反引号
            // return returnOriginalSql(insert);
            columns = new ArrayList<>();
        }

        // 获取所有值行
        List<List<Expression>> allRows = getInsertValues(insert);
        if (allRows == null || allRows.isEmpty()) {
            return returnOriginalSql(insert);
        }

        // 分批构建
        StringBuilder result = new StringBuilder();
        int totalRows = allRows.size();
        if (totalRows > 1) {
            result.append("--\n" + "-- Dumping data for table '").append(tableName).append("'\n").append("--\n");
        }
        for (int start = 0; start < totalRows; start += BATCH_SIZE) {
            int end = Math.min(start + BATCH_SIZE, totalRows);
            List<List<Expression>> batch = allRows.subList(start, end);

            String batchSql = buildBatchInsert(tableName, columns, batch);
            result.append(batchSql);
            if (start > 0) {
                result.append("\n");
            }
        }
        // 提交事务
        if (totalRows > 1) {
            // result.append("\n");
            result.append("COMMIT;\n");
        }
        return result.toString();
    }

    private static String convertDelete(Delete stmt) {
        String tableName = stmt.getTable().getName().replaceAll("`", "");
        tableName = transformTableName(tableName);
        // 设置stmt 表名
        stmt.setTable(new Table(tableName));
        // if (tableName.length() > 30) {
        //     System.err.println("Delete语法跳过，表名长度超过30，请检查：" + tableName);
        //     return "";
        // }

        return stmt.toString().replaceAll("`", "").concat(";");
    }

    private static String convertUpdate(Update stmt) {
        String tableName = stmt.getTable().getName().replaceAll("`", "");
        tableName = transformTableName(tableName);
        // 设置表名
        stmt.setTable(new Table(tableName));

        return stmt.toString().replaceAll("`", "").concat(";");
    }


    // ---------- 辅助方法 ----------
    /**
     * 需要转换的表名
     */
    private static final String[] SPECIAL_TABLE_NAMES = new String[]{
            "tenant_pre_bi_remain_term_detail",
            "tenant_pre_notification_category",
            "sys_notification_historic_manager",
            "sys_notification_message_manager",
            "sys_oauth_client_details_manager",
            "sys_organtype_corp_release_manager",
            "sys_user_face_detection_manager",
            "sys_user_permission_log_manager",
            "sys_notification_category_manager"
    };

    /**
     * 转换表名, 如果表名是特殊表，则进行重命名, 如果是工作流表（bt_manager_）则统一命名转换。
     *
     * @param tableName 表名。
     * @return 转换后的表名。
     */
    private static String transformTableName(String tableName) {
        tableName = tableName.toLowerCase();
        String originalTableName = tableName;
        for (String specialTableName : SPECIAL_TABLE_NAMES) {
            if (specialTableName.equalsIgnoreCase(tableName)) {
                if (tableName.startsWith("tenant_pre_")) {
                    tableName = tableName.replace("tenant_pre_", "tenant_p_");
                }
                if (tableName.endsWith("_manager")) {
                    tableName = tableName.replaceAll("_manager", "_m");
                }
                break;
            }
        }
        if (tableName.startsWith("bt_manager_")) {
            tableName = tableName.replaceAll("bt_manager_", "bt_m_");
        }

        if (ObjectUtil.notEqual(originalTableName, tableName)){
            System.err.println(tableName.length() > 30 ? "表名长度超过30，" : "特殊" + "表名重命名：" + originalTableName + " -> " + tableName);
        }
        return tableName.toUpperCase();
    }

    private static String subTableName(String preTable) {
        preTable = Arrays.stream(preTable.split("_")).map(s -> s.substring(0, 1)).collect(Collectors.joining("_"));
        return preTable;
    }

    private static final Set<String> ORACLE_KEYWORDS = new HashSet<>(Arrays.asList(
            "NUMBER", "DATE", "TIMESTAMP", "VARCHAR2", "CHAR", "CLOB", "BLOB",
            "SELECT", "FROM", "WHERE", "GROUP", "ORDER", "BY", "HAVING",
            "PRIMARY", "KEY", "FOREIGN", "REFERENCES", "CONSTRAINT", "INDEX",
            "TRIGGER", "PROCEDURE", "FUNCTION", "PACKAGE", "TYPE", "CURSOR",
            "BEGIN", "END", "DECLARE", "EXCEPTION", "LOOP", "WHILE", "FOR",
            "IF", "THEN", "ELSE", "ELSIF", "CASE", "WHEN", "GOTO", "RETURN",
            "COMMIT", "ROLLBACK", "SAVEPOINT", "LOCK", "TABLE", "CREATE",
            "ALTER", "DROP", "RENAME", "TRUNCATE", "GRANT", "REVOKE", "ANALYZE",
            "AUDIT", "COMMENT", "FLASHBACK", "PURGE", "SYSDATE", "SYSTIMESTAMP",
            "USER", "UID", "CURRENT_DATE", "CURRENT_TIMESTAMP", "ROWNUM", "ROWID"
    ));

    private static String safeColumnName(String colName) {
        colName = colName.replaceAll("`", "");
        if (ORACLE_KEYWORDS.contains(colName.toUpperCase())) {
            // System.err.println("警告：列名 '" + colName + "' 是 Oracle 保留字，已自动添加双引号。");
            return "\"" + colName + "\"";
        }
        return colName;
    }

    private static String returnOriginalSql(Insert insert) {
        rewriteSelectSql(insert);
        String oldSql = insert.toString();
        return oldSql.replaceAll("`", "").concat(";\n");
    }

    private static void rewriteSelectSql(Insert insert) {
        Select select = insert.getSelect();
        if (select!=null && select.getSelectBody() instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            if (selectItems != null && !selectItems.isEmpty()) {
                for (SelectItem item : selectItems) {
                    // 检查SELECT表达式是否为.*模式
                    if (item instanceof SelectExpressionItem) {
                        SelectExpressionItem selectExpressionItem = (SelectExpressionItem) item;
                        String expression = selectExpressionItem.getExpression().toString();
                        // 应用转换逻辑（例如日期包装）
                        String convertedStr = wrapDateIfNeeded(expression);
                        // 如果字符串有变化，则解析为新表达式并设置回去
                        if (!convertedStr.equals(expression)) {
                            try {
                                Expression newExpr = CCJSqlParserUtil.parseExpression(convertedStr);
                                selectExpressionItem.setExpression(newExpr);
                            } catch (JSQLParserException e) {
                                System.err.println("解析新表达式失败: " + convertedStr);
                                // 保留原表达式，不做修改
                            }
                        }
                    }
                }
            }
        }
    }

    private static String wrapDateIfNeeded(String value) {
        if (value == null || !value.startsWith("'") || !value.endsWith("'")) {
            return value;
        }
        String literal = value.substring(1, value.length() - 1);
        String format = null;
        if (literal.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            format = "YYYY-MM-DD HH24:MI:SS";
        } else if (literal.matches("\\d{4}-\\d{2}-\\d{2}")) {
            format = "YYYY-MM-DD";
        } else if (literal.matches("\\d{4}/\\d{2}/\\d{2}")) {
            format = "YYYY/MM/DD";
        } else if (literal.matches("\\d{8}")) {
            format = "YYYYMMDD";
        } else if (literal.matches("\\d{2}:\\d{2}:\\d{2}")) {
            // 仅时间，默认补上当天的日期（或根据业务需要处理）
            format = "HH24:MI:SS";
            literal = "1970-01-01 " + literal; // 补一个虚拟日期
        }
        if (format != null) {
            return "TO_DATE('" + literal + "', '" + format + "')";
        }
        return value;
    }

    private static String replaceMySqlFunctions(String sql) {
        if (sql == null) return null;
        // 注意顺序：先替换长的，避免子串被错误替换
        String result = sql;
        // MD5(UUID()) -> RAWTOHEX(SYS_GUID())
        result = result.replaceAll("(?i)MD5\\s*\\(\\s*UUID\\s*\\(\\s*\\)\\s*\\)", "RAWTOHEX(SYS_GUID())");
        // UUID() -> SYS_GUID() (但如果已经是 RAWTOHEX(SYS_GUID()) 中的则不会匹配)
        result = result.replaceAll("(?i)UUID\\s*\\(\\s*\\)", "SYS_GUID()");
        // NOW() -> SYSDATE
        result = result.replaceAll("(?i)NOW\\s*\\(\\s*\\)", "SYSDATE");
        // CURRENT_TIMESTAMP -> SYSTIMESTAMP
        result = result.replaceAll("(?i)CURRENT_TIMESTAMP", "SYSTIMESTAMP");
        // CURDATE() -> TRUNC(SYSDATE)
        result = result.replaceAll("(?i)CURDATE\\s*\\(\\s*\\)", "TRUNC(SYSDATE)");
        //  LIMIT
        result = result.replaceAll("(?i)\\s+LIMIT\\s+(\\d+)(\\s*,\\s*\\d+)?", "");
        // 正则匹配 MAX(ID) + 1 或 MAX(ID)+1
        result = result.replaceAll("(?i)MAX\\(([^)]+)\\)\\s*\\+\\s*(\\d+)", "NVL(MAX($1), 0) + $2");
        // 其他可扩展...
        return result;
    }


    private static StringBuilder generateIndexSql(List<Index> indexes, Set<List<String>> primaryKeyColumnSets, String preTable, String tableName) {
        StringBuilder indexesSql = new StringBuilder("\n");
        if (indexes != null) {
            for (Index idx : indexes) {
                String type = idx.getType();
                if ("PRIMARY KEY".equalsIgnoreCase(type) || "FOREIGN KEY".equalsIgnoreCase(type)) {
                    continue;
                }
                // 跳过与主键列完全相同的索引
                if (primaryKeyColumnSets.contains(idx.getColumnsNames())) {
                    System.out.println("跳过重复索引: " + idx.getName() + " (与主键列相同)");
                    continue;
                }

                // 唯一索引或普通索引
                boolean isUnique = "UNIQUE KEY".equalsIgnoreCase(type);
                String idxName = idx.getName();

                String prefix = (isUnique ? "UK_" : "IDX_") + preTable + "_";
                if (idxName != null) {
                    idxName = idxName.replaceAll("`", "");
                    if ("IDX_TENANT_ID".equals(idxName)) {
                        idxName = prefix + "TENANT_ID";
                    }
                }
                // 索引名（索引名称不能超过30个字符）
                if (idxName == null || idxName.trim().isEmpty() || idxName.length() > 30) {
                    idxName = prefix + trimKeyName(idx, prefix);
                    if (idxName.length() > 30) {
                        prefix = (isUnique ? "UK_" : "IDX_") + subTableName(preTable) + "_";
                        idxName = prefix + trimKeyName(idx, prefix);
                        System.err.println("索引名已截断：" + idxName);
                    }
                }
                // 创建索引
                indexesSql.append(isUnique ? "CREATE UNIQUE INDEX " : "CREATE INDEX ")
                        .append(idxName).append(" ON ").append(tableName).append(" (")
                        .append(String.join(", ", idx.getColumnsNames())).append(");\n");
            }
        }
        return indexesSql;
    }

    private static String buildBatchInsert(String tableName, List<Column> columns, List<List<Expression>> rows) {
        // 生成列名字符串 (col1, col2, ...)
        String colNames = columns.stream()
                .map(column -> safeColumnName(column.getColumnName()))
                .collect(Collectors.joining(", "));
        // 获取列元数据
        List<ColumnMeta> metas = tableMetadata.get(tableName.toUpperCase());
        if (metas == null) {
            System.err.println("警告：未找到表 " + tableName + " 的元数据，将使用默认列名。");
            metas = ListUtil.empty();
        }
        // 生成 INSERT 语句
        StringBuilder selectBuilder = new StringBuilder();
        for (int i = 0; i < rows.size(); i++) {
            List<Expression> exprList = rows.get(i);
            if (ObjectUtil.isNotEmpty(columns) && exprList.size() != columns.size()) {
                // 值数量与列数不匹配，回退到普通 INSERT 但去掉反引号
                System.err.println("警告：列数量与值数量不匹配，将使用默认列名。");
                return "INSERT INTO " + tableName + (ObjectUtil.isEmpty(colNames) ? "" : " (" + colNames + ")\n" ) + " VALUES (" +
                        exprList.stream().map(Expression::toString).collect(Collectors.joining(", ")) + ");";
            }

            if (rows.size() == 1){
                // 单行插入
                selectBuilder.append(" VALUES (");
                initValues(columns, exprList, selectBuilder, metas);
                selectBuilder.append(" )");
                return "INSERT INTO " + tableName + (ObjectUtil.isEmpty(colNames) ? "" : " (" + colNames + ")" ) + selectBuilder + ";";
            } else {
                // 批量插入
                if (i > 0) {
                    selectBuilder.append(" UNION ALL\n");
                }
                selectBuilder.append("  SELECT ");
                initValues(columns, exprList, selectBuilder, metas);
                selectBuilder.append(" FROM DUAL");
            }
        }

        return "INSERT INTO " + tableName + (ObjectUtil.isEmpty(colNames) ? "" : " (" + colNames + ")\n" ) + selectBuilder + ";";
    }

    private static void initValues(List<Column> columns, List<Expression> exprList, StringBuilder selectBuilder, List<ColumnMeta> metas) {
        for (int j = 0; j < exprList.size(); j++) {
            if (j > 0) selectBuilder.append(", ");
            // 值处理
            String colName;
            String value = exprList.get(j).toString();
            if (ObjectUtil.isNotEmpty(columns)){
                colName = columns.get(j).getColumnName().replaceAll("`", "").toUpperCase();
            } else if (ObjectUtil.isNotEmpty(metas) && metas.size() >= exprList.size()){
                colName = metas.get(j).name.toUpperCase();
            } else {
                colName = "";
            }

            ColumnMeta meta = metas.stream().filter(m -> m.name.equals(colName)).findFirst().orElse(null);
            // 检查是否为日期列（启发式）
            if (isDateColumn(colName, meta) && value != null && !value.equalsIgnoreCase("NULL")) {
                value = wrapDateExpression(value);
            }
            if (meta != null && meta.isClob && value != null && !value.equalsIgnoreCase("NULL")) {
                value = wrapClobExpression(value);
            }

            selectBuilder.append(value);
        }
    }

    private static boolean isDateColumn(String colName, ColumnMeta meta) {
        String upper = colName.toUpperCase();
        return (meta != null && meta.isDate()) || upper.contains("DATE") || upper.contains("TIME") || upper.endsWith("_DATE");
    }

    private static String wrapClobExpression(String value) {
        // 如果值长度超过4000，则使用 || 连接，并使用TO_CLOB，否则直接使用 TO_CLOB
        if (value.length() > 3500) {
            // 使用 TO_CLOB('第一个片段') || '第二个片段' || ...（第一个片段转 CLOB，后续直接用 ||）
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < value.length() - 1; i += 3500) {
                if (i == 1){
                    sb.append("TO_CLOB('").append(value, i, Math.min(i + 3500, value.length() - 1)).append("')");
                } else {
                    sb.append("'").append(value, i, Math.min(i + 3500, value.length() - 1)).append("'");
                }
                if (i + 3500 < value.length() - 1) {
                    sb.append(" || ");
                }
            }
            return sb.toString();
        }
        if (value.toUpperCase().startsWith("TO_CLOB")) {
            return value;
        }
        return "TO_CLOB(" + value + ")";
    }

    private static String wrapDateExpression(String value) {
        // 如果已经是 TO_DATE 或 TO_TIMESTAMP 调用，直接返回
        if (value.toUpperCase().startsWith("TO_DATE") || value.toUpperCase().startsWith("TO_TIMESTAMP")) {
            return value;
        }
        // 如果是字符串字面量（以单引号开头）
        if (value.startsWith("'")) {
            // 移除首尾单引号
            String literal = value.substring(1, value.length() - 1);
            // 尝试匹配常见日期时间格式
            if (literal.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                return "TO_DATE('" + literal + "', 'YYYY-MM-DD HH24:MI:SS')";
            } else if (literal.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return "TO_DATE('" + literal + "', 'YYYY-MM-DD')";
            } else if (literal.matches("\\d{4}/\\d{2}/\\d{2}")) {
                return "TO_DATE('" + literal + "', 'YYYY/MM/DD')";
            } else if (literal.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) {
                return "TO_TIMESTAMP('" + literal + "', 'YYYY-MM-DD\"T\"HH24:MI:SS')";
            } else {
                // 无法识别格式，保留原值（可能会有风险）
                return value;
            }
        } else {
            // 不是字符串字面量，可能是函数调用如 SYSDATE，原样返回
            return value;
        }
    }

    private static List<List<Expression>> getInsertValues(Insert insert) {
        ItemsList itemsList = insert.getItemsList();
        if (itemsList instanceof MultiExpressionList) {
            MultiExpressionList multi = (MultiExpressionList) itemsList;
            List<List<Expression>> allValues = new ArrayList<>();
            for (ExpressionList exprList : multi.getExpressionLists()) {
                allValues.add(exprList.getExpressions());
            }
            return allValues;
        } else if (itemsList instanceof ExpressionList) {
            ExpressionList single = (ExpressionList) itemsList;
            List<List<Expression>> result = new ArrayList<>();
            List<Expression> expressions = single.getExpressions();
            // 批量 VALUES
            if (expressions.get(0) instanceof RowConstructor) {
                for (Expression expression : expressions) {
                    result.add(((RowConstructor) expression).getExprList().getExpressions());
                }
            } else {
                // 单个 VALUES
                result.add(expressions);
            }
            return result;
        }
        return null;
    }

    private static String mapDataType(ColDataType dataType) {
        List<String> argumentsStringList = dataType.getArgumentsStringList();
        String mysqlType = dataType.getDataType()+ (argumentsStringList != null ? PlainSelect.
                getStringList(argumentsStringList, true, true) : "");
        mysqlType = mysqlType.toUpperCase();
        if (mysqlType.startsWith("VARCHAR")) {
            // VARCHAR(M)，则映射为VARCHAR2(M)，如果M>4000，则映射为4000
            if (argumentsStringList != null && !argumentsStringList.isEmpty()) {
                int m = Integer.parseInt(argumentsStringList.get(0));
                if (m > 4000) {
                    m = 4000;
                }
                return "VARCHAR2(" + m + ")";
            }
            return mysqlType.replace("VARCHAR", "VARCHAR2");
        } else if (mysqlType.startsWith("DECIMAL")) {
            // DECIMAL(M,D)，则映射为NUMBER(M,D)，如果M>38，则映射为NUMBER(38,D)
            if (argumentsStringList != null && argumentsStringList.size() >= 2) {
                int m = Integer.parseInt(argumentsStringList.get(0));
                int d = Integer.parseInt(argumentsStringList.get(1));
                if (m > 38) {
                    m = 38;
                }
                mysqlType = "NUMBER(" + m + "," + d + ")";
            }
            return mysqlType.replace("DECIMAL", "NUMBER");
        } else if (mysqlType.equals("DATETIME")) {
            return "DATE";
        } else if (mysqlType.equals("DATETIME(6)") || mysqlType.equals("DATETIME(3)")) {
            return mysqlType.replace("DATETIME", "TIMESTAMP");
        } else if (mysqlType.equals("TIMESTAMP")) {
            return "TIMESTAMP";
        } else if (mysqlType.equals("INT") || mysqlType.equals("INTEGER")) {
            return "NUMBER(10)";
        } else if (mysqlType.equals("BIGINT")) {
            return "NUMBER(19)";
        } else if (mysqlType.equals("DOUBLE") || mysqlType.equals("FLOAT")) {
            return "NUMBER";
        } else if (mysqlType.startsWith("TINYINT")) {
            return mysqlType.replace("TINYINT", "NUMBER");
        } else if (mysqlType.equals("TEXT") || mysqlType.equals("MEDIUMTEXT") || mysqlType.equals("LONGTEXT")) {
            return "CLOB";
        } else if (mysqlType.equals("LONGBLOB")) {
            return "BLOB";
        } else {
            return mysqlType;
        }
    }

    private static String extractDefaultValue(ColumnDefinition col) {
        List<String> specs = col.getColumnSpecs();
        if (specs != null) {
            for (int i = 0; i < specs.size(); i++) {
                if ("DEFAULT".equalsIgnoreCase(specs.get(i))) {
                    if (i + 1 < specs.size()) {
                        return specs.get(i + 1);
                    }
                }
            }
        }
        return null;
    }

    private static String extractTableComment(CreateTable createTable) {
        List<String> options = createTable.getTableOptionsStrings();
        if (options != null) {
            for (int i = 0; i < options.size(); i++) {
                if ("COMMENT".equalsIgnoreCase(options.get(i))) {
                    // 格式: COMMENT = 'xxx' 或者 COMMENT 'xxx'
                    if (i + 1 < options.size() && "=".equals(options.get(i + 1))) {
                        if (i + 2 < options.size()) {
                            String comment = options.get(i + 2);
                            return trimQuotes(comment);
                        }
                    } else if (i + 1 < options.size()) {
                        String comment = options.get(i + 1);
                        return trimQuotes(comment);
                    }
                    break;
                }
            }
        }
        return null;
    }

    private static String extractColumnComment(ColumnDefinition col) {
        List<String> specs = col.getColumnSpecs();
        if (specs != null) {
            for (int i = 0; i < specs.size(); i++) {
                if ("COMMENT".equalsIgnoreCase(specs.get(i))) {
                    if (i + 1 < specs.size()) {
                        String comment = specs.get(i + 1);
                        return trimQuotes(comment);
                    }
                }
            }
        }
        return null;
    }

    private static String trimQuotes(String s) {
        if (s == null) return null;
        if ((s.startsWith("'") && s.endsWith("'")) || (s.startsWith("`") && s.endsWith("`")) || (s.startsWith("\"") && s.endsWith("\""))) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private static String trimKeyName(Index idx, String prefix) {
        List<String> colNames = idx.getColumnsNames().stream().map(MySqlFileToOracleConverter::trimQuotes).collect(Collectors.toList());
        // 如果字段名总长度超出25个字符，则使用字段首字母拼接
        if (colNames.stream().mapToInt(String::length).sum() + prefix.length() > 30) {
            return colNames.stream().map(MySqlFileToOracleConverter::trimQuotes).map(s -> s.substring(0, 1)).collect(Collectors.joining("_"));
        }
        return String.join("_", colNames);

    }

    private static String escapeSingleQuote(String s) {
        return s.replace("'", "''");
    }

    private static void cacheTableMetadata(CreateTable createTable, String actualTableName) {
        List<ColumnMeta> columns = new ArrayList<>();

        // 获取原始列定义
        List<ColumnDefinition> colDefs = createTable.getColumnDefinitions();

        // 提取主键列集合（用于标记 isPrimaryKey）
        Set<String> pkColumns = new HashSet<>();
        List<Index> indexes = createTable.getIndexes();
        if (indexes != null) {
            for (Index idx : indexes) {
                if ("PRIMARY KEY".equalsIgnoreCase(idx.getType())) {
                    for (String col : idx.getColumnsNames()) {
                        pkColumns.add(col.toUpperCase());
                    }
                    break;
                }
            }
        }

        for (ColumnDefinition col : colDefs) {
            // 原始列名（未截断）
            String rawColName = col.getColumnName();
            // 截断列名（与生成 DDL 时保持一致）
            String colName = rawColName.replaceAll("`", "").toUpperCase();

            // 原始数据类型（MySQL风格，如 varchar(50)）
            ColDataType mysqlDataType = col.getColDataType();
            // 转换为 Oracle 完整类型（如 VARCHAR2(50)）
            String oracleDataType = mapDataType(mysqlDataType);

            // 创建元数据对象
            ColumnMeta meta = new ColumnMeta(colName, oracleDataType);

            // 解析长度和精度
            parseLengthAndScale(oracleDataType, meta);

            // 判断 NOT NULL
            List<String> specs = col.getColumnSpecs();
            if (specs != null) {
                for (int i = 0; i < specs.size() - 1; i++) {
                    if ("NOT".equalsIgnoreCase(specs.get(i)) && "NULL".equalsIgnoreCase(specs.get(i+1))) {
                        meta.isNullable = false;
                        break;
                    }
                }
            }

            // 标记主键
            if (pkColumns.contains(colName)) {
                meta.isPrimaryKey = true;
            }

            columns.add(meta);
        }

        tableMetadata.put(actualTableName.toUpperCase(), columns);
        // System.out.println("缓存表元数据: " + actualTableName + " -> " + columns.size() + "列");
    }

    private static void parseLengthAndScale(String oracleDataType, ColumnMeta meta) {
        // 匹配 VARCHAR2(50) 或 VARCHAR2(50 CHAR)
        Pattern varcharPattern = Pattern.compile("VARCHAR2\\((\\d+)(?:\\s+CHAR)?\\)", Pattern.CASE_INSENSITIVE);
        Matcher m = varcharPattern.matcher(oracleDataType);
        if (m.find()) {
            meta.length = Integer.parseInt(m.group(1));
            meta.dataType = "VARCHAR2";
            return;
        }

        // 匹配 NUMBER(10,2) 或 NUMBER(10)
        Pattern numberPattern = Pattern.compile("NUMBER\\((\\d+)(?:,(\\d+))?\\)", Pattern.CASE_INSENSITIVE);
        m = numberPattern.matcher(oracleDataType);
        if (m.find()) {
            meta.length = Integer.parseInt(m.group(1));
            if (m.group(2) != null) {
                meta.scale = Integer.parseInt(m.group(2));
            }
            meta.dataType = "NUMBER";
            return;
        }

        // CLOB / DATE / TIMESTAMP 等
        if (oracleDataType.startsWith("CLOB")) {
            meta.dataType = "CLOB";
            meta.isClob = true;
        } else if (oracleDataType.startsWith("DATE")) {
            meta.dataType = "DATE";
        } else if (oracleDataType.startsWith("TIMESTAMP")) {
            meta.dataType = "TIMESTAMP";
            // 可进一步解析精度，此处略
        }
    }

    public static class ColumnMeta {
        public String name;          // 列名（大写，已截断）
        public String dataType;      // 数据类型（如 VARCHAR2、NUMBER、DATE、CLOB）
        public Integer length;       // 字符长度或数字总精度
        public Integer scale;        // 数字小数位数
        public boolean isClob;       // 是否为 CLOB 类型
        public boolean isNullable;   // 是否允许 NULL
        public boolean isPrimaryKey; // 是否为主键列

        public ColumnMeta(String name, String dataType) {
            this.name = name.toUpperCase();
            this.dataType = dataType.toUpperCase();
            this.isClob = "CLOB".equals(this.dataType);
            this.isNullable = true;
            this.isPrimaryKey = false;
        }

        public boolean isDate() {
            return "DATE".equals(this.dataType) || "TIMESTAMP".equals(this.dataType);
        }

        @Override
        public String toString() {
            return String.format("ColumnMeta{name='%s', type=%s, len=%d, scale=%d, clob=%s, nullable=%s, pk=%s}",
                    name, dataType, length, scale, isClob, isNullable, isPrimaryKey);
        }
    }

    // ---------- 测试入口 ----------
    public static void main(String[] args) {
        // 示例：将当前目录下的 test_mysql.sql 转换为 test_oracle.sql
        String filePath = "D:\\拜特科技\\项目组\\T6微服务-2.0\\数据库脚本\\Mysql\\";
        String outFilePath = "D:\\拜特科技\\项目组\\T6微服务-2.0\\数据库脚本\\Oracle\\";
        File input = new File(filePath + "mysql.sql");
        File output = new File(outFilePath + "oracle.sql");

        // try {
        //     convertSqlFile(input, output);
        //     System.out.println("转换完成，输出文件：" + output.getAbsolutePath());
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // 指定目录路径
        File dir = FileUtil.file(filePath);
        // 递归获取所有 .sql 文件
        List<File> sqlFiles = FileUtil.loopFiles(dir, file -> file.getName().endsWith(".sql"));
        for (File sqlFile : sqlFiles) {
            // 输出文件名，不带后缀
            String outFileName = sqlFile.getName().substring(0, sqlFile.getName().lastIndexOf(".")).concat("-oracle.sql");
            System.out.println(sqlFile.getName() + " --> " + outFileName);
            File outFile = new File(outFilePath + outFileName);
            try {
                convertSqlFile(sqlFile, outFile);
                System.out.println("转换完成，输出文件：" + outFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
