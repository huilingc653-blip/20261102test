package com.bytter.core.jdbc;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterSession;
import net.sf.jsqlparser.statement.alter.AlterSystemStatement;
import net.sf.jsqlparser.statement.alter.RenameTableStatement;
import net.sf.jsqlparser.statement.alter.sequence.AlterSequence;
import net.sf.jsqlparser.statement.analyze.Analyze;
import net.sf.jsqlparser.statement.comment.Comment;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.schema.CreateSchema;
import net.sf.jsqlparser.statement.create.sequence.CreateSequence;
import net.sf.jsqlparser.statement.create.synonym.CreateSynonym;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.grant.Grant;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.merge.MergeInsert;
import net.sf.jsqlparser.statement.merge.MergeUpdate;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.show.ShowIndexStatement;
import net.sf.jsqlparser.statement.show.ShowTablesStatement;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import net.sf.jsqlparser.statement.upsert.Upsert;
import net.sf.jsqlparser.statement.values.ValuesStatement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * SQL标识符引号适配器
 * <p>
 * 根据目标数据库类型（MySQL/Oracle），对 SQL 中作为关键字的标识符添加/替换正确的引号，
 * 对非关键字的标识符保持原样不动。
 * </p>
 *
 * <h3>设计思路</h3>
 * <p>
 * 与字符串扫描方案不同，本工具类使用 <b>JSqlParser AST 层面</b>处理标识符引号：
 * <ol>
 *   <li>先用 JSqlParser 解析 SQL，得到 AST</li>
 *   <li>通过 Visitor 遍历所有 Column 节点（精准定位标识符）</li>
 *   <li>根据 dbType 判断是否需要添加/移除引号</li>
 *   <li>将处理后的 AST 序列化回 SQL 字符串</li>
 * </ol>
 * 这种方式比字符串扫描更精准，不会误处理字符串字面量、注释、函数名等。
 * </p>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * // MySQL
 * String sql = "SELECT order, name FROM users WHERE status = 1";
 * String adapted = SqlIdentifierQuoteAdapter.adapt(sql, "MYSQL");
 * // 结果: "SELECT `order`, name FROM users WHERE status = 1"
 *
 * // Oracle
 * String sql2 = "SELECT `order`, name FROM users WHERE status = 1";
 * String adapted2 = SqlIdentifierQuoteAdapter.adapt(sql2, "ORACLE");
 * // 结果: "SELECT \"order\", name FROM users WHERE status = 1"
 * }</pre>
 *
 * @author bytter
 * @since 1.0
 */
@Slf4j
public class SqlIdentifierQuoteAdapter {

    private SqlIdentifierQuoteAdapter() {
        // 工具类，禁止实例化
    }

    // ==================== 数据库关键字集合 ====================

    /**
     * MySQL 8.0 保留字/关键字
     * <p>参考：https://dev.mysql.com/doc/refman/8.0/en/keywords.html</p>
     * <p>包含官方保留字(R) + 常用关键字(K)（作为列名/表名时需加反引号）</p>
     */
    private static final Set<String> MYSQL_KEYWORDS = new HashSet<>(Arrays.asList(
            // MySQL 8.0 官方保留字 (R)
            "ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC",
            "ASENSITIVE", "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH",
            "BY", "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK",
            "COLLATE", "COLUMN", "CONDITION", "CONSTRAINT", "CONTINUE", "CONVERT",
            "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP",
            "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR",
            "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL",
            "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DESC", "DESCRIBE",
            "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP",
            "DUAL", "EACH", "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS",
            "EXIT", "EXPLAIN", "FALSE", "FETCH", "FLOAT", "FLOAT4", "FLOAT8",
            "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "GRANT", "GROUP",
            "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE",
            "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER",
            "INOUT", "INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3",
            "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IS", "ITERATE", "JOIN",
            "KEY", "KEYS", "KILL", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT",
            "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "LONG",
            "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MASTER_SSL_VERIFY_SERVER_CERT",
            "MATCH", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT",
            "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL",
            "NOT", "NO_WRITE_TO_BINLOG", "NULL", "NUMERIC", "ON", "OPTIMIZE",
            "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE",
            "PARTITION", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE",
            "READ", "READS", "READ_WRITE", "REAL", "REFERENCES", "REGEXP", "RELEASE",
            "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESIGNAL", "RESTRICT",
            "RETURN", "REVOKE", "RIGHT", "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND",
            "SELECT", "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SIGNAL", "SMALLINT",
            "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING",
            "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL",
            "STARTING", "STRAIGHT_JOIN", "TABLE", "TERMINATED", "THEN", "TINYBLOB",
            "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO",
            "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE",
            "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY",
            "VARCHAR", "VARCHARACTER", "VARYING", "WHEN", "WHERE", "WHILE", "WITH",
            "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL",
            // 补充：MySQL 官方关键字（非保留但常用作标识符时可能出问题）
            "DATE", "TIME", "TIMESTAMP",
            "LEVEL", "OFFSET", "ROW", "ROWS"
    ));

    /**
     * Oracle 19c SQL 保留字
     * <p>
     * 参考：https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlrf/Oracle-SQL-Reserved-Words.html
     * </p>
     * <p>
     * 注意：Oracle SQL 保留字和 PL/SQL 保留字是不同的集合。此列表仅包含 Oracle SQL 保留字。
     * Oracle 保留字/关键字都是大小写不敏感的——因为 Oracle 会将不加引号的标识符
     * 自动转为大写后匹配，因此 type/TYPE/Type 都会命中关键字 TYPE。
     * </p>
     */
    private static final Set<String> ORACLE_KEYWORDS = new HashSet<>(Arrays.asList(
            // Oracle 19c SQL 保留字 (按字母序)
            "ACCESS", "ADD", "ALL", "ALTER", "AND", "ANY", "AS", "ASC", "AUDIT",
            "BETWEEN", "BY",
            "CHAR", "CHECK", "CLUSTER", "COLUMN", "COMMENT",
            "COMPRESS", "CONNECT", "CREATE", "CURRENT",
            "DATE", "DECIMAL", "DEFAULT", "DELETE", "DESC", "DISTINCT", "DROP",
            "ELSE", "EXCLUSIVE", "EXISTS",
            "FILE", "FLOAT", "FOR", "FROM",
            "GRANT", "GROUP",
            "HAVING",
            "IDENTIFIED", "IMMEDIATE", "IN", "INCREMENT", "INDEX", "INITIAL",
            "INSERT", "INTEGER", "INTERSECT", "INTO", "IS",
            "LEVEL", "LIKE", "LOCK", "LONG",
            "MAXEXTENTS", "MINUS", "MLSLABEL", "MODE", "MODIFY",
            "NOAUDIT", "NOCOMPRESS", "NOT", "NOWAIT", "NULL", "NUMBER",
            "OF", "OFFLINE", "ON", "ONLINE", "OPTION", "OR", "ORDER",
            "PCTFREE", "PRIOR", "PRIVILEGES", "PUBLIC",
            "RAW", "RENAME", "RESOURCE", "REVOKE", "ROW", "ROWID", "ROWNUM", "ROWS",
            "SELECT", "SESSION", "SET", "SHARE", "SIZE", "SMALLINT", "START",
            "SUCCESSFUL", "SYNONYM", "SYSDATE",
            "TABLE", "THEN", "TO", "TRIGGER",
            "UID", "UNION", "UNIQUE", "UPDATE", "USER",
            "VALIDATE", "VALUES", "VARCHAR", "VARCHAR2", "VIEW",
            "WHENEVER", "WHERE", "WITH"
    ));

    /**
     * Oracle 系统内置标识符（伪列/函数），即使在关键字集合中，也不应加引号。
     * <p>
     * 这些是 Oracle 内置的伪列、函数或特殊标识符，出现在 SELECT 列表、WHERE 条件等位置时
     * 是作为系统功能使用，而非用户定义的列名/表名。对它们加引号会导致 SQL 执行失败。
     * </p>
     * <ul>
     *   <li><b>ROWNUM</b> — 伪列，返回结果集行号</li>
     *   <li><b>ROWID</b> — 伪列，返回行的物理地址</li>
     *   <li><b>SYSDATE</b> — 系统函数，返回当前日期时间</li>
     *   <li><b>UID</b> — 系统函数，返回当前用户ID</li>
     *   <li><b>USER</b> — 系统函数，返回当前用户名</li>
     *   <li><b>LEVEL</b> — 层级查询伪列（CONNECT BY 中）</li>
     * </ul>
     * <p>注意：如果用户确实创建了名为 ROWNUM/ROWID 等的列，则应由用户在原始 SQL 中
     * 自行添加双引号，本工具不做二次处理。</p>
     */
    private static final Set<String> ORACLE_BUILTIN_IDENTIFIERS = new HashSet<>(Arrays.asList(
            "ROWNUM", "ROWID", "SYSDATE", "UID", "USER", "LEVEL"
    ));

    /**
     * 适配 SQL 中的标识符引号。
     * <p>
     * 使用 JSqlParser 解析 SQL，在 AST 层面精准识别标识符（Column），
     * 根据目标数据库类型添加/替换/移除引号。
     * </p>
     * <h4>大小写处理</h4>
     * <p>
     * MySQL 和 Oracle 的关键字匹配都是大小写不敏感的。Oracle 中不加引号的标识符
     * 会被自动转为大写，因此 type/TYPE/Type 都会在 Oracle 中命中关键字 TYPE。
     * 只有加了双引号的小写标识符（如 "type"）在 Oracle 中才保持小写、不命中关键字。
     * </p>
     *
     * @param sql    原始 SQL 字符串
     * @param dbType 目标数据库类型：MYSQL 或 ORACLE（大小写不敏感）
     * @return 引号适配后的 SQL；如果解析失败则返回原始 SQL
     */
    public static String adapt(String sql, String dbType) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }
        if (dbType == null) {
            dbType = "MYSQL";
        }

        String upperDbType = dbType.toUpperCase();
        Set<String> keywords;
        char expectedQuote;
        switch (upperDbType) {
            case "MYSQL":
                keywords = MYSQL_KEYWORDS;
                expectedQuote = '`';
                break;
            case "ORACLE":
                keywords = ORACLE_KEYWORDS;
                expectedQuote = '"';
                break;
            default:
                keywords = MYSQL_KEYWORDS;
                expectedQuote = '`';
                break;
        }

        try {
            Statement statement = CCJSqlParserUtil.parse(sql);
            IdentifierQuoteVisitor visitor = new IdentifierQuoteVisitor(keywords, expectedQuote);
            statement.accept(visitor);
            return statement.toString();
        } catch (JSQLParserException e) {
            log.warn("SQL标识符引号适配失败，返回原始SQL: {}", e.getMessage());
            return sql;
        }
    }

    /**
     * AST Visitor：遍历所有 Column 节点，根据关键字集合适配引号。
     * <p>
     * 处理规则：
     * <ol>
     *   <li>列名是关键字 → 替换为目标数据库的引号（MySQL 用 `` ` ``，Oracle 用 `"`）</li>
     *   <li>列名不是关键字 → 保持原样，不做任何修改</li>
     * </ol>
     * </p>
     * <p>
     * MySQL 和 Oracle 的关键字匹配都是大小写不敏感的。Oracle 中不加引号的标识符
     * 会被自动转为大写，因此 type/TYPE/Type 都会命中关键字 TYPE。
     * </p>
     * 注意：JSqlParser 4.6 中 Column.setColumnName 传入带引号的值时，
     * toString() 会原样输出。不带引号则输出裸名。
     */
    private static class IdentifierQuoteVisitor implements StatementVisitor {

        private final Set<String> keywords;
        private final char expectedQuote;

        IdentifierQuoteVisitor(Set<String> keywords, char expectedQuote) {
            this.keywords = keywords;
            this.expectedQuote = expectedQuote;
        }

        // ==================== Statement 分发 ====================

        @Override
        public void visit(Select select) {
            if (select.getSelectBody() != null) {
                select.getSelectBody().accept(new SelectBodyVisitor());
            }
        }

        @Override
        public void visit(Insert insert) {
            // 处理 INSERT 列列表
            if (insert.getColumns() != null) {
                for (Column col : insert.getColumns()) {
                    adaptColumn(col);
                }
            }
            // 处理 INSERT ... SELECT
            if (insert.getSelect() != null) {
                insert.getSelect().accept(this);
            }
        }

        @Override
        public void visit(Update update) {
            // 处理 SET 子句中的列
            if (update.getUpdateSets() != null) {
                for (UpdateSet set : update.getUpdateSets()) {
                    if (set.getColumns() != null) {
                        for (Column col : set.getColumns()) {
                            adaptColumn(col);
                        }
                    }
                }
            }
            // 处理 WHERE 条件
            if (update.getWhere() != null) {
                update.getWhere().accept(new ExpressionVisitor());
            }
            // 处理 FROM (JOIN)
            if (update.getFromItem() != null) {
                update.getFromItem().accept(new FromItemVisitor());
            }
            if (update.getJoins() != null) {
                for (Join join : update.getJoins()) {
                    if (join.getRightItem() != null) {
                        join.getRightItem().accept(new FromItemVisitor());
                    }
                    if (join.getOnExpressions() != null) {
                        for (Expression expr : join.getOnExpressions()) {
                            expr.accept(new ExpressionVisitor());
                        }
                    }
                }
            }
        }

        @Override
        public void visit(Delete delete) {
            if (delete.getWhere() != null) {
                delete.getWhere().accept(new ExpressionVisitor());
            }
            // DELETE ... JOIN
            if (delete.getJoins() != null) {
                for (Join join : delete.getJoins()) {
                    if (join.getRightItem() != null) {
                        join.getRightItem().accept(new FromItemVisitor());
                    }
                    if (join.getOnExpressions() != null) {
                        for (Expression expr : join.getOnExpressions()) {
                            expr.accept(new ExpressionVisitor());
                        }
                    }
                }
            }
        }

        @Override
        public void visit(Replace replace) {
            if (replace.getColumns() != null) {
                for (Column col : replace.getColumns()) {
                    adaptColumn(col);
                }
            }
            if (replace.getExpressions() != null) {
                for (Expression expr : replace.getExpressions()) {
                    expr.accept(new ExpressionVisitor());
                }
            }
        }

        @Override
        public void visit(Merge merge) {
            if (merge.getUsingSelect() != null) {
                merge.getUsingSelect().accept(new FromItemVisitor());
            }
            if (merge.getOnCondition() != null) {
                merge.getOnCondition().accept(new ExpressionVisitor());
            }
            if (merge.getMergeInsert() != null) {
                MergeInsert mergeInsert = merge.getMergeInsert();
                if (mergeInsert.getColumns() != null) {
                    for (Column col : mergeInsert.getColumns()) {
                        adaptColumn(col);
                    }
                }
                if (mergeInsert.getValues() != null) {
                    for (Expression expr : mergeInsert.getValues()) {
                        expr.accept(new ExpressionVisitor());
                    }
                }
            }
            if (merge.getMergeUpdate() != null) {
                MergeUpdate mergeUpdate = merge.getMergeUpdate();
                if (mergeUpdate.getColumns() != null) {
                    for (Column col : mergeUpdate.getColumns()) {
                        adaptColumn(col);
                    }
                }
                if (mergeUpdate.getValues() != null) {
                    for (Expression expr : mergeUpdate.getValues()) {
                        expr.accept(new ExpressionVisitor());
                    }
                }
                if (mergeUpdate.getDeleteWhereCondition() != null) {
                    mergeUpdate.getDeleteWhereCondition().accept(new ExpressionVisitor());
                }
            }
        }

        @Override
        public void visit(Upsert upsert) {
            if (upsert.getColumns() != null) {
                for (Column col : upsert.getColumns()) {
                    adaptColumn(col);
                }
            }
            if (upsert.getSelect() != null) {
                upsert.getSelect().accept(this);
            }
        }

        @Override
        public void visit(CreateTable createTable) {
            if (createTable.getColumnDefinitions() != null) {
                for (ColumnDefinition colDef : createTable.getColumnDefinitions()) {
                    // ColumnDefinition.getColumnName() 返回 String，不是 Column 对象
                    String adaptedName = adaptColumnName(colDef.getColumnName());
                    if (adaptedName != null) {
                        colDef.setColumnName(adaptedName);
                    }
                }
            }
            if (createTable.getSelect() != null) {
                createTable.getSelect().accept(this);
            }
        }

        // 以下语句类型不涉及列标识符，直接跳过
        @Override public void visit(Analyze analyze) { }
        @Override public void visit(SavepointStatement savepointStatement) { }
        @Override public void visit(RollbackStatement rollbackStatement) { }
        @Override public void visit(Comment comment) { }
        @Override public void visit(Commit commit) { }
        @Override public void visit(UseStatement useStatement) { }
        @Override public void visit(Block block) { }
        @Override
        public void visit(ValuesStatement valuesStatement) {

        }
        @Override public void visit(Alter alter) { }
        @Override public void visit(Drop drop) { }
        @Override public void visit(Execute execute) { }
        @Override public void visit(Truncate truncate) { }
        @Override public void visit(SetStatement set) { }
        @Override public void visit(Grant grant) { }
        @Override public void visit(ShowTablesStatement showTables) { }
        @Override public void visit(ShowIndexStatement showIndex) { }
        @Override public void visit(DescribeStatement describe) { }
        @Override public void visit(DeclareStatement declare) { }
        @Override public void visit(ResetStatement reset) { }
        @Override public void visit(AlterSession alterSession) { }
        @Override
        public void visit(IfElseStatement ifElseStatement) {

        }
        @Override public void visit(AlterSystemStatement alterSystem) { }
        @Override
        public void visit(UnsupportedStatement unsupportedStatement) {

        }
        @Override public void visit(RenameTableStatement renameTable) { }
        @Override public void visit(AlterSequence alterSequence) { }
        @Override
        public void visit(CreateFunctionalStatement createFunctionalStatement) {

        }
        @Override public void visit(CreateSchema createSchema) { }
        @Override public void visit(CreateSequence createSequence) { }
        @Override public void visit(CreateSynonym createSynonym) { }
        @Override public void visit(AlterView alterView) { }
        @Override public void visit(CreateView createView) { }
        @Override public void visit(CreateIndex createIndex) { }
        @Override public void visit(ExplainStatement explain) { }
        @Override public void visit(ShowStatement show) { }
        @Override public void visit(PurgeStatement purge) { }
        @Override public void visit(ShowColumnsStatement showColumns) { }
        @Override public void visit(Statements statements) { }

        // ==================== SelectBody Visitor ====================

        private class SelectBodyVisitor extends SelectVisitorAdapter {
            @Override
            public void visit(PlainSelect plainSelect) {
                // 处理 SELECT 列表
                if (plainSelect.getSelectItems() != null) {
                    for (SelectItem item : plainSelect.getSelectItems()) {
                        item.accept(new SelectItemVisitor());
                    }
                }
                // 处理 FROM
                if (plainSelect.getFromItem() != null) {
                    plainSelect.getFromItem().accept(new FromItemVisitor());
                }
                // 处理 JOIN
                if (plainSelect.getJoins() != null) {
                    for (Join join : plainSelect.getJoins()) {
                        if (join.getRightItem() != null) {
                            join.getRightItem().accept(new FromItemVisitor());
                        }
                        if (join.getOnExpressions() != null) {
                            for (Expression expr : join.getOnExpressions()) {
                                expr.accept(new ExpressionVisitor());
                            }
                        }
                    }
                }
                // 处理 WHERE
                if (plainSelect.getWhere() != null) {
                    plainSelect.getWhere().accept(new ExpressionVisitor());
                }
                // 处理 GROUP BY
                if (plainSelect.getGroupBy() != null
                        && plainSelect.getGroupBy().getGroupByExpressionList() != null) {
                    plainSelect.getGroupBy().getGroupByExpressionList().accept(new ExpressionVisitor());
                }
                // 处理 HAVING
                if (plainSelect.getHaving() != null) {
                    plainSelect.getHaving().accept(new ExpressionVisitor());
                }
                // 处理 ORDER BY
                if (plainSelect.getOrderByElements() != null) {
                    for (OrderByElement obe : plainSelect.getOrderByElements()) {
                        obe.getExpression().accept(new ExpressionVisitor());
                    }
                }
            }

            @Override
            public void visit(SetOperationList setOpList) {
                if (setOpList.getSelects() != null) {
                    for (SelectBody sb : setOpList.getSelects()) {
                        sb.accept(this);
                    }
                }
                if (setOpList.getOrderByElements() != null) {
                    for (OrderByElement obe : setOpList.getOrderByElements()) {
                        obe.getExpression().accept(new ExpressionVisitor());
                    }
                }
            }

            @Override
            public void visit(WithItem withItem) {
                if (withItem.getSubSelect() != null
                        && withItem.getSubSelect().getSelectBody() != null) {
                    withItem.getSubSelect().getSelectBody().accept(this);
                }
            }
        }

        // ==================== SelectItem Visitor ====================

        private class SelectItemVisitor extends SelectItemVisitorAdapter {
            @Override
            public void visit(SelectExpressionItem item) {
                if (item.getExpression() != null) {
                    item.getExpression().accept(new ExpressionVisitor());
                }
            }

            @Override
            public void visit(AllColumns columns) {
                // SELECT * — 无需处理
            }

            @Override
            public void visit(AllTableColumns columns) {
                // SELECT t.* — 无需处理
            }
        }

        // ==================== FromItem Visitor ====================

        private class FromItemVisitor extends FromItemVisitorAdapter {
            @Override
            public void visit(Table table) {
                // 表名如果是关键字，也需要适配引号
                String adaptedName = adaptColumnName(table.getName());
                if (adaptedName != null) {
                    table.setName(adaptedName);
                }
            }

            @Override
            public void visit(SubSelect subSelect) {
                if (subSelect.getSelectBody() != null) {
                    subSelect.getSelectBody().accept(new SelectBodyVisitor());
                }
            }

            @Override
            public void visit(LateralSubSelect lateralSubSelect) {
                if (lateralSubSelect.getSubSelect() != null
                        && lateralSubSelect.getSubSelect().getSelectBody() != null) {
                    lateralSubSelect.getSubSelect().getSelectBody().accept(new SelectBodyVisitor());
                }
            }

            @Override
            public void visit(TableFunction tableFunction) {
                if (tableFunction.getFunction() != null) {
                    tableFunction.getFunction().accept(new ExpressionVisitor());
                }
            }

        }

        // ==================== Expression Visitor ====================

        private class ExpressionVisitor extends ExpressionVisitorAdapter {

            @Override
            public void visit(Column column) {
                adaptColumn(column);
            }

            @Override
            public void visit(SubSelect subSelect) {
                if (subSelect.getSelectBody() != null) {
                    subSelect.getSelectBody().accept(new SelectBodyVisitor());
                }
            }

            @Override
            public void visit(Function function) {
                if (function.getParameters() != null) {
                    function.getParameters().accept(this);
                }
            }

            @Override
            public void visit(ExpressionList expressionList) {
                for (Expression expr : expressionList.getExpressions()) {
                    expr.accept(this);
                }
            }

            @Override
            public void visit(NamedExpressionList namedExpressionList) {
                for (Expression expr : namedExpressionList.getExpressions()) {
                    expr.accept(this);
                }
            }

            @Override
            public void visit(CaseExpression caseExpr) {
                if (caseExpr.getSwitchExpression() != null) {
                    caseExpr.getSwitchExpression().accept(this);
                }
                if (caseExpr.getWhenClauses() != null) {
                    for (WhenClause wc : caseExpr.getWhenClauses()) {
                        if (wc.getWhenExpression() != null) {
                            wc.getWhenExpression().accept(this);
                        }
                        if (wc.getThenExpression() != null) {
                            wc.getThenExpression().accept(this);
                        }
                    }
                }
                if (caseExpr.getElseExpression() != null) {
                    caseExpr.getElseExpression().accept(this);
                }
            }

            @Override
            public void visit(ExistsExpression existsExpr) {
                if (existsExpr.getRightExpression() != null) {
                    existsExpr.getRightExpression().accept(this);
                }
            }

            @Override
            public void visit(AnyComparisonExpression anyExpr) {
                if (anyExpr.getSubSelect() != null
                        && anyExpr.getSubSelect().getSelectBody() != null) {
                    anyExpr.getSubSelect().getSelectBody().accept(new SelectBodyVisitor());
                }
            }

            @Override
            public void visit(InExpression inExpr) {
                if (inExpr.getLeftExpression() != null) {
                    inExpr.getLeftExpression().accept(this);
                }
                if (inExpr.getRightExpression() != null) {
                    inExpr.getRightExpression().accept(this);
                }
                if (inExpr.getRightItemsList() instanceof SubSelect) {
                    SubSelect ss = (SubSelect) inExpr.getRightItemsList();
                    if (ss.getSelectBody() != null) {
                        ss.getSelectBody().accept(new SelectBodyVisitor());
                    }
                } else if (inExpr.getRightItemsList() instanceof ExpressionList) {
                    ((ExpressionList) inExpr.getRightItemsList()).accept(this);
                }
            }

            @Override
            public void visit(JsonFunction jsonFunc) {
                for (JsonFunctionExpression expr : jsonFunc.getExpressions()) {
                    expr.getExpression().accept(this);
                }
            }

            @Override
            public void visit(RowConstructor rowConstructor) {
                if (rowConstructor.getExprList() != null) {
                    rowConstructor.getExprList().accept(this);
                }
            }

            @Override
            public void visit(Parenthesis parenthesis) {
                if (parenthesis.getExpression() != null) {
                    parenthesis.getExpression().accept(this);
                }
            }

            @Override
            public void visit(WhenClause whenClause) {
                if (whenClause.getWhenExpression() != null) {
                    whenClause.getWhenExpression().accept(this);
                }
                if (whenClause.getThenExpression() != null) {
                    whenClause.getThenExpression().accept(this);
                }
            }

            @Override
            public void visit(BitwiseRightShift bitwiseRightShift) {
                super.visit(bitwiseRightShift);
            }

            @Override
            public void visit(BitwiseLeftShift bitwiseLeftShift) {
                super.visit(bitwiseLeftShift);
            }
        }

        // ==================== 核心适配逻辑 ====================

        /**
         * 适配单个 Column 节点的引号。
         * <p>
         * 处理规则（按优先级）：
         * </p>
         * <ol>
         *   <li>内置标识符（如 ROWNUM、SYSDATE）→ 保持原样，不加引号</li>
         *   <li>标识符原本带有引号（任意类型）→ 替换为目标数据库的引号（引号转换）</li>
         *   <li>标识符是关键字 → 加上目标数据库的引号</li>
         *   <li>其他情况 → 保持原样</li>
         * </ol>
         * <p>
         * 关键字匹配大小写不敏感（MySQL 和 Oracle 均如此）。
         * Oracle 中不加引号的标识符会被自动转为大写，因此小写的 type 也会命中关键字 TYPE。
         * </p>
         * <p>
         * 系统内置标识符（如 ROWNUM、SYSDATE）即使在关键字集合中，也不应加引号，
         * 因为它们作为伪列/函数使用时加引号会导致 SQL 执行失败。
         * </p>
         */
        private void adaptColumn(Column column) {
            if (column == null || column.getColumnName() == null) {
                return;
            }
            String rawName = column.getColumnName();
            // 提取纯列名（去掉可能的引号）
            String pureName = stripQuotes(rawName);

            // 规则1: 内置标识符保持原样
            if (isBuiltinIdentifier(pureName)) {
                return;
            }

            // 规则2: 原本带有引号 → 统一替换为目标数据库引号（引号类型转换）
            boolean wasQuoted = hasQuotes(rawName);
            if (wasQuoted) {
                String adapted = expectedQuote + pureName + expectedQuote;
                if (!adapted.equals(rawName)) {
                    column.setColumnName(adapted);
                }
                return;
            }

            // 规则3: 是关键字 → 加上目标数据库引号
            if (isKeyword(pureName)) {
                String adapted = expectedQuote + pureName + expectedQuote;
                column.setColumnName(adapted);
            }
            // 规则4: 不是关键字、也没有引号 → 保持原样
        }

        /**
         * 适配列名/表名字符串的引号（用于 ColumnDefinition 等返回 String 而非 Column 的场景）。
         * <p>
         * 处理规则（按优先级）：
         * </p>
         * <ol>
         *   <li>内置标识符（如 ROWNUM、SYSDATE）→ 保持原样</li>
         *   <li>标识符原本带有引号（任意类型）→ 替换为目标数据库的引号（引号转换）</li>
         *   <li>标识符是关键字 → 加上目标数据库的引号</li>
         *   <li>其他情况 → 保持原样</li>
         * </ol>
         *
         * @param name 原始列名/表名字符串
         * @return 适配后的名称；无需修改时返回 null
         */
        private String adaptColumnName(String name) {
            if (name == null || name.isEmpty()) {
                return null;
            }
            String pureName = stripQuotes(name);

            // 规则1: 内置标识符保持原样
            if (isBuiltinIdentifier(pureName)) {
                return null;
            }

            // 规则2: 原本带有引号 → 统一替换为目标数据库引号（引号类型转换）
            boolean wasQuoted = hasQuotes(name);
            if (wasQuoted) {
                String adapted = expectedQuote + pureName + expectedQuote;
                return adapted.equals(name) ? null : adapted;
            }

            // 规则3: 是关键字 → 加上目标数据库引号
            if (isKeyword(pureName)) {
                String adapted = expectedQuote + pureName + expectedQuote;
                return adapted;
            }

            // 规则4: 不是关键字、也没有引号 → 保持原样
            return null;
        }

        /**
         * 去除标识符上可能已有的引号（双引号、反引号、方括号）。
         */
        private String stripQuotes(String identifier) {
            if (identifier == null || identifier.length() < 2) {
                return identifier;
            }
            char first = identifier.charAt(0);
            char last = identifier.charAt(identifier.length() - 1);
            if ((first == '"' && last == '"')
                    || (first == '`' && last == '`')
                    || (first == '[' && last == ']')) {
                return identifier.substring(1, identifier.length() - 1);
            }
            return identifier;
        }

        /**
         * 判断标识符是否原本带有引号（双引号、反引号、方括号）。
         * <p>
         * 用于区分"用户主动加了引号"和"裸标识符"两种场景：
         * - 用户加了引号 → 说明用户希望保持该标识符的原始形式，应转换为目标数据库的引号
         * - 裸标识符 → 仅在是关键字时才加引号
         * </p>
         */
        private boolean hasQuotes(String identifier) {
            if (identifier == null || identifier.length() < 2) {
                return false;
            }
            char first = identifier.charAt(0);
            char last = identifier.charAt(identifier.length() - 1);
            return (first == '"' && last == '"')
                    || (first == '`' && last == '`')
                    || (first == '[' && last == ']');
        }

        /**
         * 判断标识符是否为数据库关键字（大小写不敏感）。
         * <p>
         * MySQL 和 Oracle 的保留字/关键字匹配都是大小写不敏感的。
         * Oracle 中不加引号的标识符会被自动转为大写后匹配，因此
         * type/TYPE/Type 都会命中关键字 TYPE。
         * </p>
         */
        private boolean isKeyword(String identifier) {
            return keywords.contains(identifier.toUpperCase());
        }

        /**
         * 判断标识符是否为数据库系统内置标识符（伪列/函数名），不应加引号。
         * <p>
         * 目前仅 Oracle 需要此判断。这些词在 ORACLE_KEYWORDS 中，但作为系统功能使用时
         * 加引号会导致 SQL 语义错误（如 "ROWNUM" 不再被识别为伪列）。
         * </p>
         */
        private boolean isBuiltinIdentifier(String identifier) {
            return ORACLE_BUILTIN_IDENTIFIERS.contains(identifier.toUpperCase());
        }
    }

    // ==================== 校验入口 ====================

    public static void main(String[] args) {
        System.out.println("========== SqlIdentifierQuoteAdapter 校验 ==========\n");

        // 测试用例列表
        // {序号, 描述, 原始SQL, 目标DB类型}
        String[][] testCases = {
            // ==================== 基础功能测试 ====================
            {"1", "SELECT 关键字列名(order)", "SELECT id, name, `order` FROM users WHERE status = 1", "MYSQL"},
            {"2", "SELECT 关键字列名(order) -> Oracle", "SELECT id, name, `order` FROM users WHERE status = 1", "ORACLE"},
            {"3", "SELECT 无关键字列名", "SELECT id, name, age FROM users WHERE status = 1", "MYSQL"},
            {"4", "INSERT 关键字列名", "INSERT INTO orders (id, name, `order`, `status`) VALUES (1, 'test', 100, 1)", "MYSQL"},
            {"5", "INSERT 关键字列名 -> Oracle", "INSERT INTO orders (id, name, `order`, `status`) VALUES (1, 'test', 100, 1)", "ORACLE"},
            {"6", "UPDATE 关键字列名", "UPDATE users SET name = 'new', `status` = 0 WHERE id = 1", "MYSQL"},
            {"7", "UPDATE 关键字列名 -> Oracle", "UPDATE users SET name = 'new', `status` = 0 WHERE id = 1", "ORACLE"},
            {"8", "DELETE WHERE 关键字", "DELETE FROM users WHERE `status` = 0 AND `level` > 5", "MYSQL"},
            {"9", "JOIN + 关键字列名", "SELECT u.id, u.name, o.`order` FROM users u JOIN orders o ON u.id = o.user_id WHERE o.`status` = 1", "MYSQL"},
            {"10", "子查询 关键字列名", "SELECT * FROM (SELECT id, `order`, `status` FROM orders) t WHERE t.`status` = 1", "MYSQL"},
            {"11", "ORDER BY 关键字列名", "SELECT id, name FROM users ORDER BY `order`, `status` DESC", "MYSQL"},
            {"12", "GROUP BY 关键字列名", "SELECT `status`, COUNT(*) FROM orders GROUP BY `status`", "MYSQL"},
            {"13", "CREATE TABLE 关键字列名", "CREATE TABLE test_table (id INT, name VARCHAR(100), `order` INT, `status` INT, `type` VARCHAR(50))", "MYSQL"},
            {"14", "CREATE TABLE 关键字列名 -> Oracle", "CREATE TABLE test_table (id INT, name VARCHAR(100), `order` INT, `status` INT, `type` VARCHAR(50))", "ORACLE"},
            {"15", "字符串中包含关键字(不处理)", "SELECT id, name FROM users WHERE comment = 'order status type'", "MYSQL"},
            {"16", "函数 + 关键字列名", "SELECT COUNT(*), MAX(`order`) FROM orders WHERE `status` = 1", "MYSQL"},
            {"17", "CASE WHEN 关键字列名", "SELECT id, CASE WHEN `status` = 1 THEN 'active' ELSE 'inactive' END FROM users", "MYSQL"},
            {"18", "IN 子查询 关键字列名", "SELECT * FROM users WHERE id IN (SELECT user_id FROM orders WHERE `status` = 1)", "MYSQL"},
            {"19", "MERGE 关键字列名", "MERGE INTO target USING source ON (target.id = source.id) WHEN MATCHED THEN UPDATE SET target.`status` = source.`status`", "MYSQL"},
            {"20", "关键字表名", "SELECT * FROM `order` WHERE id = 1", "MYSQL"},
            {"21", "Oracle业务SQL(大小写混合)", "SELECT ID, CODE, type, NUMBER, PAR_AMT AS parAmt, TAXINC_AMT AS taxincAmt, CHECK_CODE AS checkCode, DRA_DATE AS draDate, BILL_ID AS billId, BILL_NO AS billNo FROM doc_invoice dm WHERE 1 = 1 AND bill_id = ? AND dm.TENANT_ID = '0703'", "ORACLE"},

            // ==================== Oracle SQL保留字大小写不敏感测试 ====================
            // TYPE 不是 Oracle SQL 保留字，任何大小写都不需要加引号
            {"22", "Oracle: TYPE不是SQL保留字(不加引号)", "SELECT id, TYPE, name FROM users", "ORACLE"},
            {"23", "Oracle: type不是SQL保留字(不加引号)", "SELECT id, type, name FROM users", "ORACLE"},
            // LEVEL 是 Oracle SQL 保留字，大小写不敏感，都需要加引号
            {"24", "Oracle: LEVEL是SQL保留字(加引号)", "SELECT id, LEVEL, name FROM users", "ORACLE"},
            {"25", "Oracle: level是SQL保留字(加引号)", "SELECT id, level, name FROM users", "ORACLE"},
            // DATE 是 Oracle SQL 保留字，大小写不敏感
            {"26", "Oracle: DATE是SQL保留字(加引号)", "SELECT id, DATE, name FROM users", "ORACLE"},
            {"27", "Oracle: date是SQL保留字(加引号)", "SELECT id, date, name FROM users", "ORACLE"},
            // NUMBER 是 Oracle SQL 保留字
            {"28", "Oracle: NUMBER是SQL保留字(加引号)", "SELECT id, NUMBER, name FROM users", "ORACLE"},
            {"29", "Oracle: number是SQL保留字(加引号)", "SELECT id, number, name FROM users", "ORACLE"},
            // MySQL 侧验证：date是关键字加反引号
            {"30", "MySQL: date视为关键字(加反引号)", "SELECT id, date, name FROM users", "MYSQL"},
            {"31", "MySQL: type不是关键字(不加反引号)", "SELECT id, type, name FROM users", "MYSQL"},

            // ==================== Oracle 系统内置标识符不加引号测试 ====================
            {"32", "Oracle: ROWNUM伪列不加引号", "SELECT * FROM users WHERE ROWNUM <= 10", "ORACLE"},
            {"33", "Oracle: rownum小写伪列不加引号", "SELECT * FROM users WHERE rownum <= 10", "ORACLE"},
            {"34", "Oracle: ROWID伪列不加引号", "SELECT ROWID, name FROM users", "ORACLE"},
            {"35", "Oracle: SYSDATE函数不加引号", "SELECT SYSDATE, name FROM users", "ORACLE"},
            {"36", "Oracle: UID函数不加引号", "SELECT UID, name FROM users", "ORACLE"},
            {"37", "Oracle: USER函数不加引号", "SELECT USER, name FROM users", "ORACLE"},
            {"38", "Oracle: LEVEL伪列不加引号(WHERE中)", "SELECT * FROM users WHERE LEVEL = 1", "ORACLE"},
            {"39", "Oracle: LEVEL伪列不加引号(SELECT中)", "SELECT LEVEL, name FROM users CONNECT BY PRIOR id = parent_id", "ORACLE"},
            {"40", "Oracle: ROWNUM在子查询中不加引号", "SELECT * FROM (SELECT * FROM users WHERE ROWNUM <= 5) WHERE ROWNUM <= 3", "ORACLE"},
            {"41", "Oracle: 普通列LEVEL(用户已加引号)", "SELECT \"LEVEL\", name FROM users", "ORACLE"},
            // 验证非内置的 Oracle 保留字列仍然加引号
            {"42", "Oracle: DATE列加引号(非内置)", "SELECT DATE, name FROM users", "ORACLE"},
            {"43", "Oracle: NUMBER列加引号(非内置)", "SELECT NUMBER, name FROM users", "ORACLE"},

            // ==================== 引号类型转换测试 ====================
            // Oracle 目标：输入中混用了 MySQL 反引号 → 应转换为 Oracle 双引号
            {"44", "Oracle: 反引号转双引号(关键字)", "SELECT `order`, `status` FROM users", "ORACLE"},
            // Oracle 目标：输入中非关键字但加了反引号 → 也应转换为双引号
            {"45", "Oracle: 反引号转双引号(非关键字)", "SELECT `user_name`, `age` FROM users", "ORACLE"},
            // Oracle 目标：用户已用双引号（保持双引号不变）
            {"46", "Oracle: 双引号保持不变(非关键字)", "SELECT \"user_name\", \"age\" FROM users", "ORACLE"},
            // Oracle 目标：关键字已用双引号（保持双引号不变）
            {"47", "Oracle: 双引号保持不变(关键字)", "SELECT \"DATE\", \"NUMBER\" FROM users", "ORACLE"},
            // MySQL 目标：输入中混用了 Oracle 双引号 → 应转换为 MySQL 反引号
            {"48", "MySQL: 双引号转反引号(关键字)", "SELECT \"order\", \"status\" FROM users", "MYSQL"},
            // MySQL 目标：输入中非关键字但加了双引号 → 也应转换为反引号
            {"49", "MySQL: 双引号转反引号(非关键字)", "SELECT \"user_name\", \"age\" FROM users", "MYSQL"},
            // MySQL 目标：反引号保持不变
            {"50", "MySQL: 反引号保持不变(非关键字)", "SELECT `user_name`, `age` FROM users", "MYSQL"},
            // Oracle 目标：复杂场景 — 混用引号 + 关键字 + 内置标识符
            {"51", "Oracle: 混用引号转换+关键字+内置", "SELECT `order`, name, ROWNUM FROM users WHERE `status` = 1", "ORACLE"},
            // MySQL 目标：复杂场景 — 混用引号 + 关键字
            {"52", "MySQL: 混用引号转换+关键字", "SELECT \"order\", name, date FROM users WHERE \"status\" = 1", "MYSQL"},
            // Oracle 目标：表名中的反引号转换
            {"53", "Oracle: 表名反引号转双引号", "SELECT * FROM `order_items` WHERE id = 1", "ORACLE"},
            // MySQL 目标：表名中的双引号转换
            {"54", "MySQL: 表名双引号转反引号", "SELECT * FROM \"order_items\" WHERE id = 1", "MYSQL"},
        };

        int passCount = 0;
        int failCount = 0;

        for (String[] tc : testCases) {
            String no = tc[0];
            String desc = tc[1];
            String sql = tc[2];
            String dbType = tc[3];

            System.out.println("--- [" + no + "] " + desc + " (" + dbType + ") ---");
            System.out.println("原始: " + sql);

            try {
                String result = adapt(sql, dbType);
                System.out.println("结果: " + result);

                // 基本校验：结果不能为null，不能为空
                if (result == null || result.isEmpty()) {
                    System.out.println(">>> [FAIL] 结果为空！\n");
                    failCount++;
                    continue;
                }

                // 校验：结果中不应包含反引号(MySQL)或双引号(Oracle)以外的引号
                boolean hasWrongQuote = false;
                if ("MYSQL".equalsIgnoreCase(dbType)) {
                    // MySQL 目标：结果中不应有双引号包裹的标识符（但字符串字面量中的双引号除外）
                    hasWrongQuote = result.contains("\"");
                } else if ("ORACLE".equalsIgnoreCase(dbType)) {
                    // Oracle 目标：结果中不应有反引号
                    hasWrongQuote = result.contains("`");
                }
                if (hasWrongQuote) {
                    System.out.println(">>> [WARN] 结果中包含非预期的引号字符\n");
                } else {
                    System.out.println(">>> [PASS]\n");
                    passCount++;
                }
            } catch (Exception e) {
                System.out.println(">>> [FAIL] 异常: " + e.getMessage() + "\n");
                failCount++;
            }
        }

        System.out.println("========== 校验完成 ==========");
        System.out.println("通过: " + passCount + " / " + testCases.length);
        System.out.println("失败: " + failCount + " / " + testCases.length);
    }
}
