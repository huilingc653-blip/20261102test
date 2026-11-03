package com.bytter.core.jdbc;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.common.exception.BusinessException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
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
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.grant.Grant;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
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
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.*;

/**
 * 复杂嵌套SQL条件处理器 - 完整版
 * 支持增删改查所有场景，处理多级嵌套：FROM子查询、EXISTS、函数中的子查询等
 */
@Slf4j
public class ComplexNestedSQLProcessor {

    private final String conditionField;
    private final Object conditionValue;
    private final ConditionMode conditionMode;

    // 使用嵌套Map来存储上下文路径 -> 表键
    private final Map<String, Set<String>> contextProcessedTables = new HashMap<>();
    // 不需要处理的表名
    private Set<String> excludedTables = new HashSet<>();
    // 不需要处理的表名前缀
    private Set<String> excludeTablePrefix = new HashSet<>();
    // 特殊处理表，租户ID追加OR条件，用于全局数据访问
    private Set<String> specialTables = new HashSet<>();
    // 全局租户ID值
    private String tenantIdValue = "";
    // 参数索引映射
    private final Map<Integer, Integer> parameterIndexMapping = new HashMap<>();
    // 当前处理的参数索引位置
    private int currentParamIndex = 1;
    // 是否已经替换了租户ID字段（影响参数索引）
    private boolean hasReplacedTenantId = false;
    // 数据库类型：MYSQL / ORACLE
    private final String dbType;

    public enum ConditionMode {
        ADD,  // 添加条件
        REMOVE, // 移除条件
        REPLACE // 替换条件
    }

    public ComplexNestedSQLProcessor(String conditionField, Object conditionValue, TenantProperties tenantProperties) {
        this(conditionField, conditionValue, ConditionMode.ADD, tenantProperties);
    }

    public ComplexNestedSQLProcessor(String conditionField, Object conditionValue, ConditionMode conditionMode, TenantProperties tenantProperties) {
        this.conditionField = conditionField;
        this.conditionValue = conditionValue;
        this.conditionMode = conditionMode;
        if (ObjectUtil.isNotEmpty(tenantProperties.getExcludeTables())) {
            this.excludedTables = new HashSet<>(tenantProperties.getExcludeTables()) ;
        }
        if (ObjectUtil.isNotEmpty(tenantProperties.getSpecialTables())) {
            this.specialTables = new HashSet<>(tenantProperties.getSpecialTables());
        }
        if (ObjectUtil.isNotEmpty(tenantProperties.getTenantId())) {
            this.tenantIdValue = tenantProperties.getTenantId();
        }
        if (ObjectUtil.isNotEmpty(tenantProperties.getExcludeTablePrefix())) {
            this.excludeTablePrefix = new HashSet<>(tenantProperties.getExcludeTablePrefix());
        }
        if (ObjectUtil.isNotEmpty(tenantProperties.getDbType())) {
            this.dbType = tenantProperties.getDbType().toUpperCase();
        } else {
            this.dbType = "MYSQL";
        }
    }

    /**
     * 处理SQL语句
     */
    public ParsedSQL processSQL(String sql) throws JSQLParserException {
        // 重置状态
        parameterIndexMapping.clear();
        currentParamIndex = 1;
        hasReplacedTenantId = false;

        Statement statement = CCJSqlParserUtil.parse(sql);

        // 使用自定义的StatementVisitor处理所有语句类型
        StatementVisitor processor = new AllStatementProcessor();
        statement.accept(processor);

        // 获取改写后的SQL
        String rewrittenSql = statement.toString();

        // 根据数据库类型，对SQL中的标识符引号做适配（使用独立工具类，基于JSqlParser AST精准处理）
        rewrittenSql = SqlIdentifierQuoteAdapter.adapt(rewrittenSql, dbType);

        // 创建ParsedSQL对象
        ParsedSQL parsedSQL = new ParsedSQL();
        parsedSQL.setOriginalSql(sql);
        parsedSQL.setSql(rewrittenSql);
        parsedSQL.setTenantId(conditionValue.toString());
        parsedSQL.setColumnName(conditionField);
        parsedSQL.setParameterIndexMapping(new HashMap<>(parameterIndexMapping));

        return parsedSQL;
    }

    /**
     * 检查是否是租户ID字段
     */
    private boolean isTenantIdField(String fieldName) {
        return conditionField.equalsIgnoreCase(fieldName);
    }

    /**
     * 处理INSERT语句的参数索引映射
     */
    private void processInsertParameterIndex(Insert insert) {
        if (insert.getTable() != null && isExcludedTable(insert.getTable().getName())) {
            return;
        }
        if (insert.getItemsList() instanceof ExpressionList) {
            ExpressionList exprList = (ExpressionList) insert.getItemsList();
            List<Expression> expressions = exprList.getExpressions();

            // 检查列中是否有租户ID字段
            List<Column> columns = insert.getColumns();
            boolean hasTenantIdColumn = false;
            int tenantIdIndex = -1;

            if (columns != null) {
                for (int i = 0; i < columns.size(); i++) {
                    if (isTenantIdField(columns.get(i).getColumnName())) {
                        hasTenantIdColumn = true;
                        tenantIdIndex = i;
                        break;
                    }
                }
            }

            // 处理每个参数
            if (hasTenantIdColumn) {
                for (int i = 0; i < expressions.size(); i++) {
                    // Expression expr = expressions.get(i);
                    // if (expr instanceof JdbcParameter) {
                    //
                    // }
                    // 这是一个参数占位符
                    int originalIndex = currentParamIndex++;

                    if (i == tenantIdIndex) {
                        // 原始索引映射到0，跳过
                        parameterIndexMapping.put(originalIndex, 0);
                    } else if (i > tenantIdIndex) {
                        // 租户ID已被替换，后续参数索引前移1位
                        parameterIndexMapping.put(originalIndex, originalIndex - 1);
                    } else {
                        parameterIndexMapping.put(originalIndex, originalIndex);
                    }
                }
            }

        }
    }

    /**
     * 处理UPDATE语句的参数索引映射
     */
    private void processUpdateParameterIndex(Update update) {
        // 检查SET子句中是否有租户ID字段
        boolean tenantIdInSet = false;
        if (update.getUpdateSets() != null) {
            for (UpdateSet set : update.getUpdateSets()) {
                if (set.getColumns() != null) {
                    for (Column column : set.getColumns()) {
                        if (isTenantIdField(column.getColumnName())) {
                            tenantIdInSet = true;
                            break;
                        }
                    }
                }
                if (tenantIdInSet) break;
            }
        }

        // 遍历所有表达式收集参数
        List<Expression> allExpressions = new ArrayList<>();

        // SET子句中的表达式
        if (update.getUpdateSets() != null) {
            for (UpdateSet set : update.getUpdateSets()) {
                if (set.getExpressions() != null) {
                    allExpressions.addAll(set.getExpressions());
                }
            }
        }

        // WHERE条件中的表达式
        if (update.getWhere() != null) {
            collectExpressionsFromWhere(update.getWhere(), allExpressions);
        }

        // 处理参数索引
        int paramCount = 0;
        boolean foundTenantIdInSet = false;

        for (Expression expr : allExpressions) {
            if (expr instanceof JdbcParameter) {
                paramCount++;
                int originalIndex = currentParamIndex++;

                if (tenantIdInSet && !foundTenantIdInSet) {
                    // 第一个参数可能是租户ID（如果在SET子句中）
                    foundTenantIdInSet = true;
                    hasReplacedTenantId = true;
                    // 原始索引映射到相同位置（值已替换）
                    parameterIndexMapping.put(originalIndex, paramCount);
                } else {
                    // 其他参数，检查是否需要调整索引
                    if (hasReplacedTenantId) {
                        parameterIndexMapping.put(originalIndex, paramCount - 1);
                    } else {
                        parameterIndexMapping.put(originalIndex, paramCount);
                    }
                }
            }
        }
    }

    /**
     * 从WHERE条件中收集所有表达式
     */
    private void collectExpressionsFromWhere(Expression where, List<Expression> expressions) {
        if (where instanceof JdbcParameter) {
            expressions.add(where);
        } else if (where instanceof BinaryExpression) {
            BinaryExpression binary = (BinaryExpression) where;
            collectExpressionsFromWhere(binary.getLeftExpression(), expressions);
            collectExpressionsFromWhere(binary.getRightExpression(), expressions);
        } else if (where instanceof Parenthesis) {
            Parenthesis paren = (Parenthesis) where;
            collectExpressionsFromWhere(paren.getExpression(), expressions);
        } else if (where instanceof InExpression) {
            InExpression in = (InExpression) where;
            if (in.getLeftExpression() != null) {
                collectExpressionsFromWhere(in.getLeftExpression(), expressions);
            }
            if (in.getRightItemsList() instanceof ExpressionList) {
                ExpressionList exprList = (ExpressionList) in.getRightItemsList();
                expressions.addAll(exprList.getExpressions());
            }
        }
    }

    /**
     * 处理所有语句类型的处理器
     */
    class AllStatementProcessor implements StatementVisitor {

        @Override
        public void visit(Select select) {
            ContextAwareProcessor processor = new ContextAwareProcessor();
            select.getSelectBody().accept(processor);
        }

        @Override
        public void visit(Analyze analyze) {

        }

        @Override
        public void visit(SavepointStatement savepointStatement) {

        }

        @Override
        public void visit(RollbackStatement rollbackStatement) {

        }

        @Override
        public void visit(Comment comment) {

        }

        @Override
        public void visit(Commit commit) {

        }

        @Override
        public void visit(Delete delete) {
            processDeleteStatement(delete);
        }

        @Override
        public void visit(Update update) {
            processUpdateStatement(update);
            // 处理UPDATE的参数索引映射
            // processUpdateParameterIndex(update);
        }

        @Override
        public void visit(Insert insert) {
            processInsertStatement(insert);
            // 处理INSERT的参数索引映射
            processInsertParameterIndex(insert);
        }

        @Override
        public void visit(Upsert upsert) {
            processUpsertStatement(upsert);
        }

        @Override
        public void visit(UseStatement useStatement) {

        }

        @Override
        public void visit(Block block) {

        }

        @Override
        public void visit(Replace replace) {
            processReplaceStatement(replace);
        }

        @Override
        public void visit(Merge merge) {
            processMergeStatement(merge);
        }

        @Override
        public void visit(Truncate truncate) {
            // TRUNCATE语句不支持条件添加
            // 但可以记录日志或抛出异常
            System.out.println("TRUNCATE语句不支持条件处理: " + truncate);
        }

        @Override
        public void visit(CreateTable createTable) {
            // CREATE TABLE语句可能包含SELECT子查询
            processCreateTableStatement(createTable);
        }

        @Override
        public void visit(Alter alter) {
            // ALTER语句通常不包含条件
            System.out.println("ALTER语句不支持条件处理: " + alter);
        }

        @Override
        public void visit(Statements statements) {

        }

        @Override
        public void visit(Drop drop) {
            // DROP语句不支持条件
            System.out.println("DROP语句不支持条件处理: " + drop);
        }

        @Override
        public void visit(Execute execute) {
            // EXECUTE语句可能包含参数，暂不支持
            System.out.println("EXECUTE语句不支持条件处理: " + execute);
        }

        @Override
        public void visit(SetStatement setStatement) {

        }

        @Override
        public void visit(ResetStatement resetStatement) {

        }

        @Override
        public void visit(ShowColumnsStatement showColumnsStatement) {

        }

        @Override
        public void visit(ShowIndexStatement showIndexStatement) {

        }

        @Override
        public void visit(ShowTablesStatement showTables) {
            // SHOW语句不支持条件
            System.out.println("SHOW语句不支持条件处理: " + showTables);
        }

        // 其他StatementVisitor方法
        @Override
        public void visit(CreateIndex createIndex) {
            // CREATE INDEX语句可能包含SELECT子查询
        }

        @Override
        public void visit(CreateSchema createSchema) {

        }

        @Override
        public void visit(CreateView createView) {
            // CREATE VIEW语句可能包含SELECT子查询
        }

        @Override
        public void visit(AlterView alterView) {

        }

        @Override
        public void visit(CreateSynonym createSynonym) {
            // CREATE SYNONYM语句可能包含SELECT子查询
        }
        @Override
        public void visit(AlterSession alterSession) {
            // ALTER SESSION语句可能包含SELECT子查询
        }

        @Override
        public void visit(IfElseStatement ifElseStatement) {

        }

        @Override
        public void visit(RenameTableStatement renameTableStatement) {

        }

        @Override
        public void visit(PurgeStatement purgeStatement) {

        }

        @Override
        public void visit(AlterSystemStatement alterSystemStatement) {

        }

        @Override
        public void visit(UnsupportedStatement unsupportedStatement) {

        }

        @Override
        public void visit(ValuesStatement values) {
            // VALUES语句可能包含SELECT子查询
        }

        @Override
        public void visit(DescribeStatement describeStatement) {

        }

        @Override
        public void visit(ExplainStatement explainStatement) {

        }

        @Override
        public void visit(ShowStatement showStatement) {

        }

        @Override
        public void visit(DeclareStatement declareStatement) {

        }

        @Override
        public void visit(Grant grant) {

        }

        @Override
        public void visit(CreateSequence createSequence) {

        }

        @Override
        public void visit(AlterSequence alterSequence) {

        }

        @Override
        public void visit(CreateFunctionalStatement createFunctionalStatement) {

        }

    }

    /**
     * 处理DELETE语句
     */
    private void processDeleteStatement(Delete delete) {
        // 设置当前上下文
        ContextHolder.setCurrentContext("/delete");

        try {
            // 获取表信息
            Table table = delete.getTable();
            if (table == null) {
                return;
            }

            String tableName = table.getName();
            String alias = table.getAlias() != null ? table.getAlias().getName() : null;

            // 检查表是否被排除
            if (isExcludedTable(tableName)) {
                return;
            }

            // 处理WHERE条件
            if (delete.getWhere() != null) {
                processDeleteWhereCondition(delete, tableName, alias);
            } else {
                // 如果没有WHERE条件，创建新的WHERE条件
                addConditionToDelete(delete, alias);
            }

            // 处理USING子句（某些数据库支持）
            if (delete.getUsingList() != null) {
                for (Table usingTable : delete.getUsingList()) {
                    processUsingTable(usingTable, delete);
                }
            }

            // 处理JOIN（某些数据库支持DELETE JOIN）
            if (delete.getJoins() != null) {
                for (int i = 0; i < delete.getJoins().size(); i++) {
                    Join join = delete.getJoins().get(i);
                    processDeleteJoin(join, delete, "/delete/join_" + i);
                }
            }

        } finally {
            ContextHolder.clear();
        }
    }

    /**
     * 处理DELETE的WHERE条件
     */
    private void processDeleteWhereCondition(Delete delete, String tableName, String alias) {
        Expression where = delete.getWhere();

        // 检查是否已存在相同条件
        ConditionChecker checker = new ConditionChecker(alias, conditionField, conditionValue);
        where.accept(checker);

        boolean conditionExists = checker.isConditionExists();

        switch (conditionMode) {
            case ADD:
                if (!conditionExists) {
                    Expression newCondition = createConditionExpression(alias);
                    Expression combined = combineExpressions(where, newCondition, conditionMode);
                    delete.setWhere(combined);
                }
                break;

            case REMOVE:
                if (conditionExists) {
                    Expression removed = removeConditionFromExpression(where, alias);
                    delete.setWhere(removed);
                }
                break;

            case REPLACE:
                Expression replaced = replaceConditionInExpression(where, alias);
                delete.setWhere(replaced);
                break;
        }

        // 处理WHERE中的子查询
        ContextAwareExpressionProcessor expressionProcessor = new ContextAwareExpressionProcessor();
        expressionProcessor.setCurrentContext("/delete/where");
        where.accept(expressionProcessor);
    }

    /**
     * 向DELETE添加条件
     */
    private void addConditionToDelete(Delete delete, String alias) {
        Expression condition = createConditionExpression(alias);
        delete.setWhere(condition);
    }

    /**
     * 处理DELETE的JOIN
     */
    private void processDeleteJoin(Join join, Delete delete, String contextPath) {
        String oldContext = ContextHolder.getCurrentContext();
        try {
            ContextHolder.setCurrentContext(contextPath);

            // 处理ON条件
            Collection<Expression> onExpressions = join.getOnExpressions();
            if (onExpressions != null) {
                ContextAwareExpressionProcessor processor = new ContextAwareExpressionProcessor();
                processor.setCurrentContext(contextPath + "/on");
                onExpressions.forEach(expression -> expression.accept(processor));
            }

            // 处理JOIN右侧的子查询
            if (join.getRightItem() != null) {
                join.getRightItem().accept(new FromItemVisitorAdapter() {
                    @Override
                    public void visit(SubSelect subSelect) {
                        if (subSelect.getSelectBody() != null) {
                            ContextAwareProcessor selectProcessor = new ContextAwareProcessor();
                            selectProcessor.setCurrentContextPath(contextPath + "/right_subquery");
                            subSelect.getSelectBody().accept(selectProcessor);
                        }
                    }
                });
            }

        } finally {
            ContextHolder.setCurrentContext(oldContext);
        }
    }

    /**
     * 处理USING子句中的表
     */
    private void processUsingTable(Table usingTable, Delete delete) {
        String tableName = usingTable.getName();
        String alias = usingTable.getAlias() != null ? usingTable.getAlias().getName() : tableName;

        // 检查WHERE条件中是否已包含此表的条件
        if (delete.getWhere() != null) {
            ConditionChecker checker = new ConditionChecker(alias, conditionField, conditionValue);
            delete.getWhere().accept(checker);

            if (!checker.isConditionExists()) {
                Expression condition = createConditionExpression(alias);
                Expression newWhere = combineExpressions(delete.getWhere(), condition, conditionMode);
                delete.setWhere(newWhere);
            }
        }
    }

    /**
     * 处理UPDATE语句
     */
    private void processUpdateStatement(Update update) {
        ContextHolder.setCurrentContext("/update");

        try {
            // 获取更新表
            Table table = update.getTable(); // UPDATE通常只有一个表
            String tableName = table.getName();
            String alias = table.getAlias() != null ? table.getAlias().getName() : null;

            // 检查是否排除此表
            if (isExcludedTable(tableName)) {
                return;
            }

            // 处理SET子句中的子查询
            if (update.getUpdateSets() != null) {
                for (int i = 0; i < update.getUpdateSets().size(); i++) {
                    UpdateSet set = update.getUpdateSets().get(i);
                    processUpdateSet(set, "/update/set_" + i);
                }
            }

            // 处理FROM子句（某些数据库支持UPDATE FROM）
            if (update.getFromItem() != null) {
                processUpdateFrom(update.getFromItem(), "/update/from");
            }

            // 处理JOIN（某些数据库支持UPDATE JOIN）
            if (update.getJoins() != null) {
                for (int i = 0; i < update.getJoins().size(); i++) {
                    Join join = update.getJoins().get(i);
                    processUpdateJoin(join, "/update/join_" + i);
                }
            }

            // 处理WHERE条件
            if (update.getWhere() != null) {
                processUpdateWhereCondition(update, alias);
            } else {
                // 如果没有WHERE条件，添加条件
                addConditionToUpdate(update, alias);
            }

        } finally {
            ContextHolder.clear();
        }
    }

    /**
     * 处理UPDATE的SET子句
     */
    private void processUpdateSet(UpdateSet set, String contextPath) {
        String oldContext = ContextHolder.getCurrentContext();
        try {
            ContextHolder.setCurrentContext(contextPath);

            // 处理SET值中的子查询
            List<Expression> expressions = set.getExpressions();
            if (expressions != null) {
                ContextAwareExpressionProcessor processor = new ContextAwareExpressionProcessor();
                processor.setCurrentContext(contextPath + "/expression");
                expressions.forEach(expression -> expression.accept(processor));
            }

        } finally {
            ContextHolder.setCurrentContext(oldContext);
        }
    }

    /**
     * 处理UPDATE的FROM子句
     */
    private void processUpdateFrom(FromItem fromItem, String contextPath) {
        fromItem.accept(new FromItemVisitorAdapter() {
            @Override
            public void visit(SubSelect subSelect) {
                if (subSelect.getSelectBody() != null) {
                    ContextAwareProcessor processor = new ContextAwareProcessor();
                    processor.setCurrentContextPath(contextPath + "/subquery");
                    subSelect.getSelectBody().accept(processor);
                }
            }

            @Override
            public void visit(Table table) {
                // 处理FROM中的表，可能需要添加条件
                String tableName = table.getName();
                String alias = table.getAlias() != null ? table.getAlias().getName() : tableName;

                // 记录此表已在当前上下文中处理
                String contextKey = ContextHolder.getCurrentContext();
                Set<String> processedTables = contextProcessedTables
                        .computeIfAbsent(contextKey, k -> new HashSet<>());

                String tableKey = tableName + "|" + alias;
                // 这里可以添加对FROM表的条件处理逻辑
                processedTables.add(tableKey);
            }
        });
    }

    /**
     * 处理UPDATE的JOIN
     */
    private void processUpdateJoin(Join join, String contextPath) {
        String oldContext = ContextHolder.getCurrentContext();
        try {
            ContextHolder.setCurrentContext(contextPath);

            // 处理ON条件
            Collection<Expression> onExpressions = join.getOnExpressions();
            if (onExpressions != null) {
                ContextAwareExpressionProcessor processor = new ContextAwareExpressionProcessor();
                processor.setCurrentContext(contextPath + "/on");
                onExpressions.forEach(expression -> expression.accept(processor));
            }

            // 处理JOIN右侧
            if (join.getRightItem() != null) {
                join.getRightItem().accept(new FromItemVisitorAdapter() {
                    @Override
                    public void visit(SubSelect subSelect) {
                        if (subSelect.getSelectBody() != null) {
                            ContextAwareProcessor selectProcessor = new ContextAwareProcessor();
                            selectProcessor.setCurrentContextPath(contextPath + "/right_subquery");
                            subSelect.getSelectBody().accept(selectProcessor);
                        }
                    }

                    @Override
                    public void visit(Table table) {
                        // 处理JOIN表
                        String tableName = table.getName();
                        String alias = table.getAlias() != null ? table.getAlias().getName() : tableName;

                        // 记录此表已在当前上下文中处理
                        String contextKey = ContextHolder.getCurrentContext();
                        Set<String> processedTables = contextProcessedTables
                                .computeIfAbsent(contextKey, k -> new HashSet<>());

                        String tableKey = tableName + "|" + alias;
                        processedTables.add(tableKey);
                    }
                });
            }

        } finally {
            ContextHolder.setCurrentContext(oldContext);
        }
    }

    /**
     * 处理UPDATE的WHERE条件
     */
    private void processUpdateWhereCondition(Update update, String alias) {
        Expression where = update.getWhere();

        // 检查是否已存在相同条件
        ConditionChecker checker = new ConditionChecker(alias, conditionField, conditionValue);
        where.accept(checker);

        boolean conditionExists = checker.isConditionExists();

        switch (conditionMode) {
            case ADD:
                if (!conditionExists) {
                    Expression newCondition = createConditionExpression(alias);
                    Expression combined = combineExpressions(where, newCondition, conditionMode);
                    update.setWhere(combined);
                }
                break;

            case REMOVE:
                if (conditionExists) {
                    Expression removed = removeConditionFromExpression(where, alias);
                    update.setWhere(removed);
                }
                break;

            case REPLACE:
                Expression replaced = replaceConditionInExpression(where, alias);
                update.setWhere(replaced);
                break;
        }

        // 处理WHERE中的子查询
        ContextAwareExpressionProcessor expressionProcessor = new ContextAwareExpressionProcessor();
        expressionProcessor.setCurrentContext("/update/where");
        where.accept(expressionProcessor);
    }

    /**
     * 向UPDATE添加条件
     */
    private void addConditionToUpdate(Update update, String alias) {
        Expression condition = createConditionExpression(alias);
        update.setWhere(condition);
    }

    /**
     * 处理INSERT语句
     */
    private void processInsertStatement(Insert insert) {
        ContextHolder.setCurrentContext("/insert");

        try {
            Table table = insert.getTable();
            String tableName = table.getName();

            // 检查是否排除此表
            if (isExcludedTable(tableName)) {
                return;
            }

            // INSERT可能包含SELECT子查询
            if (insert.getSelect() != null) {
                // 处理INSERT ... SELECT ... 形式
                processInsertSelect(insert);
            }

            // 处理INSERT ... VALUES ... 形式
            if (insert.getColumns() != null && insert.getItemsList() != null) {
                processInsertValues(insert);
            }

            // 处理ON DUPLICATE KEY UPDATE子句
            if (insert.getDuplicateUpdateColumns() != null && insert.getDuplicateUpdateExpressionList() != null) {
                processDuplicateUpdate(insert);
            }

            // 处理WITH子句（CTE）
            if (insert.getWithItemsList() != null && !insert.getWithItemsList().isEmpty()) {
                processWithItems(insert.getWithItemsList());
            }

        } finally {
            ContextHolder.clear();
        }
    }

    /**
     * 处理WITH子句（CTE）
     */
    private void processWithItems(List<WithItem> withItemsList) {
        for (WithItem withItem : withItemsList) {
            if (withItem.getSubSelect() != null && withItem.getSubSelect().getSelectBody() != null) {
                ContextAwareProcessor processor = new ContextAwareProcessor();
                processor.setCurrentContextPath("/insert/with/" + withItem.getName());
                withItem.getSubSelect().getSelectBody().accept(processor);
            }
        }
    }

    /**
     * 处理INSERT ... VALUES ... 形式
     */
    private void processInsertValues(Insert insert) {
        List<Column> columns = insert.getColumns();

        // 检查是否包含条件字段
        boolean hasConditionColumn = false;
        int conditionColumnIndex = -1;

        if (columns != null) {
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                if (conditionField.equalsIgnoreCase(column.getColumnName())) {
                    hasConditionColumn = true;
                    conditionColumnIndex = i;
                    break;
                }
            }
        }

        // 根据不同的条件模式处理
        switch (conditionMode) {
            case ADD:
                // ADD模式：如果没有条件字段，则添加
                if (!hasConditionColumn) {
                    addConditionToInsertValues(insert);
                } else {
                    // 如果已有条件字段，确保其值为条件值
                    ensureConditionValueInInsert(insert, conditionColumnIndex);
                }
                break;

            case REMOVE:
                // REMOVE模式：移除条件字段及其值
                if (hasConditionColumn) {
                    removeConditionFromInsertValues(insert, conditionColumnIndex);
                }
                break;

            case REPLACE:
                // REPLACE模式：替换条件字段的值或添加条件字段
                if (hasConditionColumn) {
                    replaceConditionValueInInsert(insert, conditionColumnIndex);
                } else {
                    addConditionToInsertValues(insert);
                }
                break;
        }

        // 处理VALUES中的子查询
        ItemsList itemsList = insert.getItemsList();
        processInsertValuesSubqueries(itemsList);
    }

    /**
     * 向INSERT VALUES添加条件字段和值
     */
    private void addConditionToInsertValues(Insert insert) {
        List<Column> columns = insert.getColumns();
        ItemsList itemsList = insert.getItemsList();

        // 创建条件字段列
        Column conditionColumn = new Column();
        conditionColumn.setColumnName(conditionField);

        // 创建条件值表达式
        Expression conditionValueExpr = convertToExpression(conditionValue);

        // 添加到列列表
        if (columns == null) {
            // 如果没有指定列，表示插入所有列，这种情况下不能添加列
            // 可能需要抛出异常或记录日志
            System.out.println("警告: INSERT语句没有指定列列表，无法添加条件字段");
            return;
        }

        columns.add(conditionColumn);

        // 添加到值列表
        if (itemsList instanceof ExpressionList) {
            // 单行插入
            ExpressionList exprList = (ExpressionList) itemsList;
            exprList.getExpressions().add(conditionValueExpr);
        } else if (itemsList instanceof MultiExpressionList) {
            // 多行插入
            MultiExpressionList multiExprList = (MultiExpressionList) itemsList;
            for (ExpressionList exprList : multiExprList.getExpressionLists()) {
                exprList.getExpressions().add(conditionValueExpr);
            }
        }
    }

    /**
     * 确保INSERT VALUES中的条件字段值为条件值
     */
    private void ensureConditionValueInInsert(Insert insert, int conditionColumnIndex) {
        ItemsList itemsList = insert.getItemsList();

        if (itemsList instanceof ExpressionList) {
            Expression conditionValueExpr = convertToExpression(conditionValue);
            // 单行插入
            ExpressionList exprList = (ExpressionList) itemsList;
            List<Expression> expressions = exprList.getExpressions();

            if (conditionColumnIndex < expressions.size()) {
                // 检查当前值是否为条件值
                Expression currentValue = expressions.get(conditionColumnIndex);
                if (isNotConditionValue(currentValue)) {
                    // 如果不是条件值，替换为条件值
                    expressions.set(conditionColumnIndex, conditionValueExpr);
                }
            } else {
                // 如果索引超出范围，添加条件字段和值
                if (expressions.get(0) instanceof RowConstructor) {
                    for (Expression expression : expressions) {
                        RowConstructor rowConstructor = (RowConstructor) expression;
                        rowConstructor.getExprList().getExpressions().add(conditionColumnIndex, conditionValueExpr);
                    }
                } else {
                    expressions.add(conditionColumnIndex, conditionValueExpr);
                }
            }
        } else if (itemsList instanceof MultiExpressionList) {
            // 多行插入
            MultiExpressionList multiExprList = (MultiExpressionList) itemsList;
            Expression conditionValueExpr = convertToExpression(conditionValue);

            for (ExpressionList exprList : multiExprList.getExpressionLists()) {
                List<Expression> expressions = exprList.getExpressions();

                if (conditionColumnIndex < expressions.size()) {
                    Expression currentValue = expressions.get(conditionColumnIndex);
                    if (isNotConditionValue(currentValue)) {
                        expressions.set(conditionColumnIndex, conditionValueExpr);
                    }
                } else {
                    // 如果索引超出范围，添加条件字段和值
                    expressions.add(conditionColumnIndex, conditionValueExpr);
                }
            }
        }
    }

    /**
     * 从INSERT VALUES中移除条件字段和值
     */
    private void removeConditionFromInsertValues(Insert insert, int conditionColumnIndex) {
        List<Column> columns = insert.getColumns();
        ItemsList itemsList = insert.getItemsList();

        // 从列列表中移除条件字段
        if (columns != null && conditionColumnIndex < columns.size()) {
            columns.remove(conditionColumnIndex);
        }

        // 从值列表中移除对应位置的值
        if (itemsList instanceof ExpressionList) {
            // 单行插入
            ExpressionList exprList = (ExpressionList) itemsList;
            List<Expression> expressions = exprList.getExpressions();

            if (conditionColumnIndex < expressions.size()) {
                expressions.remove(conditionColumnIndex);
            }
        } else if (itemsList instanceof MultiExpressionList) {
            // 多行插入
            MultiExpressionList multiExprList = (MultiExpressionList) itemsList;

            for (ExpressionList exprList : multiExprList.getExpressionLists()) {
                List<Expression> expressions = exprList.getExpressions();

                if (conditionColumnIndex < expressions.size()) {
                    expressions.remove(conditionColumnIndex);
                }
            }
        }
    }

    /**
     * 替换INSERT VALUES中条件字段的值
     */
    private void replaceConditionValueInInsert(Insert insert, int conditionColumnIndex) {
        ItemsList itemsList = insert.getItemsList();
        Expression conditionValueExpr = convertToExpression(conditionValue);

        if (itemsList instanceof ExpressionList) {
            // 单行插入
            ExpressionList exprList = (ExpressionList) itemsList;
            List<Expression> expressions = exprList.getExpressions();

            if (conditionColumnIndex < expressions.size()) {
                expressions.set(conditionColumnIndex, conditionValueExpr);
            }
        } else if (itemsList instanceof MultiExpressionList) {
            // 多行插入
            MultiExpressionList multiExprList = (MultiExpressionList) itemsList;

            for (ExpressionList exprList : multiExprList.getExpressionLists()) {
                List<Expression> expressions = exprList.getExpressions();

                if (conditionColumnIndex < expressions.size()) {
                    expressions.set(conditionColumnIndex, conditionValueExpr);
                }
            }
        }
    }

    /**
     * 检查表达式是否为条件值
     */
    private boolean isNotConditionValue(Expression expression) {
        if (expression == null) {
            return true;
        }

        String exprStr = expression.toString();
        String conditionValueStr = conditionValue.toString();

        if (expression instanceof StringValue) {
            // 字符串值，需要去掉引号
            StringValue stringValue = (StringValue) expression;
            return !stringValue.getValue().replace("'", "").equals(conditionValueStr);
        } else if (expression instanceof LongValue) {
            // 长整型值
            LongValue longValue = (LongValue) expression;
            String value = String.valueOf(longValue.getValue());
            return !value.equals(conditionValueStr);
        } else if (expression instanceof DoubleValue) {
            // 双精度值
            DoubleValue doubleValue = (DoubleValue) expression;
            String value = String.valueOf(doubleValue.getValue());
            return !value.equals(conditionValueStr);
        } else if (expression instanceof Function) {
            // 函数调用，如NOW()
            Function function = (Function) expression;
            return !function.toString().contains(conditionValueStr);
        }

        return !exprStr.equals(conditionValueStr) && !exprStr.contains(conditionValueStr);
    }

    /**
     * 处理INSERT VALUES中的子查询
     */
    private void processInsertValuesSubqueries(ItemsList itemsList) {
        if (itemsList instanceof SubSelect) {
            // 子查询插入
            SubSelect subSelect = (SubSelect) itemsList;
            if (subSelect.getSelectBody() != null) {
                ContextAwareProcessor processor = new ContextAwareProcessor();
                processor.setCurrentContextPath("/insert/values/subquery");
                subSelect.getSelectBody().accept(processor);
            }
        } else if (itemsList instanceof ExpressionList) {
            // 检查表达式列表中是否有子查询
            ExpressionList exprList = (ExpressionList) itemsList;
            for (int i = 0; i < exprList.getExpressions().size(); i++) {
                Expression expr = exprList.getExpressions().get(i);
                processExpressionForSubqueries(expr, "/insert/values/expr_" + i);
            }
        } else if (itemsList instanceof MultiExpressionList) {
            // 多行插入，检查每行的表达式
            MultiExpressionList multiExprList = (MultiExpressionList) itemsList;
            for (int rowIdx = 0; rowIdx < multiExprList.getExpressionLists().size(); rowIdx++) {
                ExpressionList exprList = multiExprList.getExpressionLists().get(rowIdx);
                for (int colIdx = 0; colIdx < exprList.getExpressions().size(); colIdx++) {
                    Expression expr = exprList.getExpressions().get(colIdx);
                    processExpressionForSubqueries(expr, "/insert/values/row_" + rowIdx + "/expr_" + colIdx);
                }
            }
        }
    }

    /**
     * 处理表达式中的子查询
     */
    private void processExpressionForSubqueries(Expression expression, String contextPath) {
        if (expression instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) expression;
            if (subSelect.getSelectBody() != null) {
                ContextAwareProcessor processor = new ContextAwareProcessor();
                processor.setCurrentContextPath(contextPath + "/subquery");
                subSelect.getSelectBody().accept(processor);
            }
        } else if (expression instanceof Function) {
            // 函数中可能包含子查询
            Function function = (Function) expression;
            ExpressionList parameters = function.getParameters();
            if (parameters != null) {
                for (int i = 0; i < parameters.getExpressions().size(); i++) {
                    Expression param = parameters.getExpressions().get(i);
                    processExpressionForSubqueries(param, contextPath + "/func/param_" + i);
                }
            }
        } else if (expression instanceof CaseExpression) {
            // CASE WHEN表达式
            CaseExpression caseExpr = (CaseExpression) expression;

            // 处理WHEN子句
            if (caseExpr.getWhenClauses() != null) {
                for (int i = 0; i < caseExpr.getWhenClauses().size(); i++) {
                    WhenClause whenClause = caseExpr.getWhenClauses().get(i);
                    processExpressionForSubqueries(whenClause.getWhenExpression(),
                            contextPath + "/case/when_" + i + "/condition");
                    processExpressionForSubqueries(whenClause.getThenExpression(),
                            contextPath + "/case/when_" + i + "/result");
                }
            }

            // 处理ELSE表达式
            if (caseExpr.getElseExpression() != null) {
                processExpressionForSubqueries(caseExpr.getElseExpression(),
                        contextPath + "/case/else");
            }
        }
    }

    /**
     * 处理INSERT ... SELECT ...
     */
    private void processInsertSelect(Insert insert) {
        // 检查是否包含租户ID字段
        List<Column> insertColumns = insert.getColumns();
        boolean hasTenantColumnInInsert = false;
        int tenantColumnIndex = -1;

        if (insertColumns != null) {
            for (int i = 0; i < insertColumns.size(); i++) {
                if (isTenantIdField(insertColumns.get(i).getColumnName())) {
                    hasTenantColumnInInsert = true;
                    tenantColumnIndex = i;
                    break;
                }
            }
        }

        // 检查SELECT子查询中是否使用.*
        Select select = insert.getSelect();
        boolean selectHasAllColumns = false;

        if (select != null && select.getSelectBody() instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            selectHasAllColumns = checkSelectHasAllColumns(plainSelect);
        }

        // 根据条件模式处理
        switch (conditionMode) {
            case ADD:
                // ADD模式：如果没有租户ID字段，则添加
                if (!hasTenantColumnInInsert) {
                    // 如果SELECT使用.*且INSERT没有指定列，可以不添加
                    if (!(selectHasAllColumns && insertColumns == null)) {
                        addTenantIdToInsertSelect(insert);
                    }
                } else {
                    // 如果已有租户ID字段，确保SELECT中有对应值
                    ensureTenantIdInInsertSelect(insert, tenantColumnIndex);
                }
                break;

            case REMOVE:
                // REMOVE模式：移除租户ID字段
                if (hasTenantColumnInInsert) {
                    removeConditionFromInsertValues(insert, tenantColumnIndex);
                }
                break;

            case REPLACE:
                // REPLACE模式：替换或添加租户ID
                if (hasTenantColumnInInsert) {
                    replaceConditionValueInInsert(insert, tenantColumnIndex);
                } else {
                    // 如果SELECT使用.*且INSERT没有指定列，可以不添加
                    if (!(selectHasAllColumns && insertColumns == null)) {
                        addTenantIdToInsertSelect(insert);
                    }
                }
                break;
        }

        // 处理SELECT子查询中的条件（递归处理）
        if (select != null && select.getSelectBody() != null) {
            ContextAwareProcessor processor = new ContextAwareProcessor();
            processor.setCurrentContextPath("/insert/select");
            select.getSelectBody().accept(processor);
        }
    }

    /**
     * 检查SELECT语句是否使用.*（AllColumns）
     */
    private boolean checkSelectHasAllColumns(PlainSelect plainSelect) {
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        if (selectItems != null) {
            for (SelectItem item : selectItems) {
                if (item instanceof AllColumns || item instanceof AllTableColumns) {
                    return true;
                }

                // 检查SELECT表达式是否为.*模式
                if (item instanceof SelectExpressionItem) {
                    SelectExpressionItem exprItem = (SelectExpressionItem) item;
                    Expression expression = exprItem.getExpression();

                    // 如果已存在租户ID值，则不再添加
                    if (exprItem.getAlias() != null && exprItem.getAlias().getName().equals(conditionField)) {
                        return true;
                    }

                    // 检查是否是 t.* 格式
                    if (expression instanceof Column) {
                        Column column = (Column) expression;
                        if ("*".equals(column.getColumnName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 向INSERT ... SELECT添加租户ID
     */
    private void addTenantIdToInsertSelect(Insert insert) {
        List<Column> columns = insert.getColumns();
        Select select = insert.getSelect();

        // 添加租户ID列到INSERT列列表
        if (columns == null) {
            // INSERT没有指定列，表示插入所有列
            System.out.println("警告: INSERT语句没有指定列列表，无法添加条件字段");
            return;
        }

        // 添加租户ID列
        Column tenantColumn = new Column(conditionField);
        columns.add(tenantColumn);

        // 在SELECT中添加租户ID值
        if (select != null && select.getSelectBody() instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            List<SelectItem> selectItems = plainSelect.getSelectItems();

            // 创建租户ID常量值表达式项
            SelectExpressionItem tenantValueItem = new SelectExpressionItem(convertToExpression(conditionValue));
            if (selectItems != null) {
                selectItems.add(tenantValueItem);
            }
        } else if (select != null && select.getSelectBody() instanceof SetOperationList) {
            // 处理UNION等集合操作
            SetOperationList setOpList = (SetOperationList) select.getSelectBody();
            for (SelectBody selectBody : setOpList.getSelects()) {
                if (selectBody instanceof PlainSelect) {
                    PlainSelect plainSelect = (PlainSelect) selectBody;
                    List<SelectItem> selectItems = plainSelect.getSelectItems();

                    if (selectItems != null) {
                        SelectExpressionItem tenantValueItem = new SelectExpressionItem(convertToExpression(conditionValue));
                        selectItems.add(tenantValueItem);
                    }
                }
            }
        }
    }

    /**
     * 确保INSERT ... SELECT中的租户ID值正确
     */
    private void ensureTenantIdInInsertSelect(Insert insert, int tenantColumnIndex) {
        Select select = insert.getSelect();

        if (select != null && select.getSelectBody() instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            List<SelectItem> selectItems = plainSelect.getSelectItems();

            if (selectItems != null && tenantColumnIndex < selectItems.size()) {
                // 获取当前的租户ID值项
                SelectItem currentItem = selectItems.get(tenantColumnIndex);

                if (currentItem instanceof SelectExpressionItem) {
                    SelectExpressionItem exprItem = (SelectExpressionItem) currentItem;
                    Expression currentExpr = exprItem.getExpression();

                    // 检查当前值是否为正确的租户ID值
                    if (isNotConditionValue(currentExpr)) {
                        // 替换为正确的租户ID值
                        exprItem.setExpression(convertToExpression(conditionValue));
                    }
                }
            }
        }
    }

    /**
     * 处理ON DUPLICATE KEY UPDATE
     */
    private void processDuplicateUpdate(Insert insert) {
        List<Expression> expressions = insert.getDuplicateUpdateExpressionList();

        for (int i = 0; i < expressions.size(); i++) {
            Expression expression = expressions.get(i);
            if (expression != null) {
                ContextAwareExpressionProcessor processor = new ContextAwareExpressionProcessor();
                processor.setCurrentContext("/insert/duplicate_update/expr_" + i);
                expression.accept(processor);
            }
        }
    }

    /**
     * 处理UPSERT语句
     */
    private void processUpsertStatement(Upsert upsert) {
        // UPSERT是INSERT的一种变体，通常包含WHERE条件
        ContextHolder.setCurrentContext("/upsert");

        try {
            Table table = upsert.getTable();
            // 检查表是否被排除
            if (isExcludedTable(table.getName())){
                return;
            }

            // 处理SELECT部分
            if (upsert.getSelect() != null) {
                processInsertSelect(new Insert() {
                    @Override
                    public void accept(StatementVisitor statementVisitor) {}

                    @Override
                    public Select getSelect() {
                        return upsert.getSelect();
                    }
                });
            }

            // 处理WHERE条件
            List<Expression> updateExpressionList = upsert.getDuplicateUpdateExpressionList();
            if (updateExpressionList != null) {
                ContextAwareExpressionProcessor processor = new ContextAwareExpressionProcessor();
                processor.setCurrentContext("/upsert/where");
                updateExpressionList.forEach(expression -> expression.accept(processor));
            }

        } finally {
            ContextHolder.clear();
        }
    }

    /**
     * 处理REPLACE语句
     */
    private void processReplaceStatement(Replace replace) {
        // REPLACE类似INSERT，但会替换已存在的记录
        ContextHolder.setCurrentContext("/replace");

        try {
            // 处理SELECT部分
            if (replace.getSelect() != null) {
                Select select = replace.getSelect();
                if (select.getSelectBody() != null) {
                    ContextAwareProcessor processor = new ContextAwareProcessor();
                    processor.setCurrentContextPath("/replace/select");
                    select.getSelectBody().accept(processor);
                }
            }

        } finally {
            ContextHolder.clear();
        }
    }

    /**
     * 处理MERGE语句
     */
    private void processMergeStatement(Merge merge) {
        // MERGE语句非常复杂，包含USING、ON、WHEN MATCHED、WHEN NOT MATCHED等子句
        ContextHolder.setCurrentContext("/merge");

        try {
            // 检查表是否被排除
            Table table = merge.getTable();
            if (isExcludedTable(table.getName())){
                return;
            }

            // 处理USING子句
            if (merge.getUsingTable() != null) {
                processMergeUsing(merge.getUsingTable());
            }

            // 处理ON条件
            if (merge.getOnCondition() != null) {
                ContextAwareExpressionProcessor onProcessor = new ContextAwareExpressionProcessor();
                onProcessor.setCurrentContext("/merge/on");
                merge.getOnCondition().accept(onProcessor);
            }

            // 处理WHEN MATCHED子句
            if (merge.getMergeUpdate() != null) {
                processMergeUpdate(merge.getMergeUpdate());
            }

            // 处理WHEN NOT MATCHED子句
            if (merge.getMergeInsert() != null) {
                processMergeInsert(merge.getMergeInsert());
            }

        } finally {
            ContextHolder.clear();
        }
    }

    /**
     * 处理MERGE的USING子句
     */
    private void processMergeUsing(Table usingTable) {
        String tableName = usingTable.getName();
        String alias = usingTable.getAlias() != null ? usingTable.getAlias().getName() : tableName;

        // 记录USING表
        String contextKey = ContextHolder.getCurrentContext();
        Set<String> processedTables = contextProcessedTables
                .computeIfAbsent(contextKey, k -> new HashSet<>());

        String tableKey = tableName + "|" + alias;
        processedTables.add(tableKey);
    }

    /**
     * 处理MERGE的UPDATE子句
     */
    private void processMergeUpdate(MergeUpdate mergeUpdate) {
        // 处理SET子句
        if (mergeUpdate.getColumns() != null && mergeUpdate.getValues() != null) {
            for (int i = 0; i < mergeUpdate.getColumns().size(); i++) {
                Expression expression = mergeUpdate.getValues().get(i);
                if (expression != null) {
                    ContextAwareExpressionProcessor processor = new ContextAwareExpressionProcessor();
                    processor.setCurrentContext("/merge/update/set_" + i);
                    expression.accept(processor);
                }
            }
        }

        // 处理WHERE条件
        if (mergeUpdate.getWhereCondition() != null) {
            ContextAwareExpressionProcessor whereProcessor = new ContextAwareExpressionProcessor();
            whereProcessor.setCurrentContext("/merge/update/where");
            mergeUpdate.getWhereCondition().accept(whereProcessor);
        }
    }

    /**
     * 处理MERGE的INSERT子句
     */
    private void processMergeInsert(net.sf.jsqlparser.statement.merge.MergeInsert mergeInsert) {
        // 处理WHERE条件
        if (mergeInsert.getWhereCondition() != null) {
            ContextAwareExpressionProcessor whereProcessor = new ContextAwareExpressionProcessor();
            whereProcessor.setCurrentContext("/merge/insert/where");
            mergeInsert.getWhereCondition().accept(whereProcessor);
        }
    }

    /**
     * 处理CREATE TABLE语句
     */
    private void processCreateTableStatement(CreateTable createTable) {
        // 检查表是否被排除
        Table table = createTable.getTable();
        if (isExcludedTable(table.getName())){
            return;
        }

        // CREATE TABLE可能包含SELECT子查询（CREATE TABLE ... AS SELECT ...）
        if (createTable.getSelect() != null) {
            Select select = createTable.getSelect();
            if (select.getSelectBody() != null) {
                ContextHolder.setCurrentContext("/create_table/select");
                try {
                    ContextAwareProcessor processor = new ContextAwareProcessor();
                    processor.setCurrentContextPath("/create_table/select");
                    select.getSelectBody().accept(processor);
                } finally {
                    ContextHolder.clear();
                }
            }
        }
    }

    /**
     * 创建条件表达式
     */
    private Expression createConditionExpression(String tableAlias) {
        Column column = new Column();
        column.setColumnName(conditionField);
        column.setTable(new Table(null, tableAlias));

        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(column);
        equalsTo.setRightExpression(convertToExpression(conditionValue));

        return equalsTo;
    }

    /**
     * 组合表达式
     */
    private Expression combineExpressions(Expression original, Expression newCondition, ConditionMode mode) {
        if (mode == ConditionMode.ADD) {
            if (original == null) {
                return newCondition;
            }
            return new AndExpression(original, newCondition);
        }
        return original;
    }

    /**
     * 从表达式中移除条件
     */
    private Expression removeConditionFromExpression(Expression expression, String tableAlias) {
        ConditionRemover remover = new ConditionRemover(tableAlias, conditionField, conditionValue);
        expression.accept(remover);
        return remover.getResultExpression();
    }

    /**
     * 替换表达式中的条件
     */
    private Expression replaceConditionInExpression(Expression expression, String tableAlias) {
        ConditionReplacer replacer = new ConditionReplacer(tableAlias, conditionField, conditionValue);
        expression.accept(replacer);
        return replacer.getResultExpression();
    }

    /**
     * 条件移除器
     */
    static class ConditionRemover extends ExpressionVisitorAdapter {
        private final String tableAlias;
        private final String conditionField;
        private final Object conditionValue;
        @Getter
        private Expression resultExpression;

        public ConditionRemover(String tableAlias, String conditionField, Object conditionValue) {
            this.tableAlias = tableAlias;
            this.conditionField = conditionField;
            this.conditionValue = conditionValue;
            this.resultExpression = null;
        }

        @Override
        public void visit(EqualsTo equalsTo) {
            if (isTargetCondition(equalsTo)) {
                resultExpression = null; // 移除这个条件
            } else {
                resultExpression = equalsTo;
            }
        }

        @Override
        public void visit(AndExpression andExpression) {
            Expression left = processSubExpression(andExpression.getLeftExpression());
            Expression right = processSubExpression(andExpression.getRightExpression());

            if (left == null && right == null) {
                resultExpression = null;
            } else if (left == null) {
                resultExpression = right;
            } else if (right == null) {
                resultExpression = left;
            } else {
                resultExpression = new AndExpression(left, right);
            }
        }

        @Override
        public void visit(OrExpression orExpression) {
            Expression left = processSubExpression(orExpression.getLeftExpression());
            Expression right = processSubExpression(orExpression.getRightExpression());

            if (left == null && right == null) {
                resultExpression = null;
            } else if (left == null) {
                resultExpression = right;
            } else if (right == null) {
                resultExpression = left;
            } else {
                resultExpression = new OrExpression(left, right);
            }
        }

        private Expression processSubExpression(Expression expression) {
            ConditionRemover subRemover = new ConditionRemover(tableAlias, conditionField, conditionValue);
            expression.accept(subRemover);
            return subRemover.getResultExpression();
        }

        private boolean isTargetCondition(EqualsTo equalsTo) {
            if (equalsTo.getLeftExpression() instanceof Column) {
                Column column = (Column) equalsTo.getLeftExpression();

                boolean tableMatches = (column.getTable() != null &&
                        column.getTable().getName() != null &&
                        column.getTable().getName().equals(tableAlias));
                boolean fieldMatches = column.getColumnName().equals(conditionField);

                return tableMatches && fieldMatches;
            }
            return false;
        }
    }

    /**
     * 条件替换器
     */
    class ConditionReplacer extends ExpressionVisitorAdapter {
        private final String tableAlias;
        private final String conditionField;
        private final Object conditionValue;
        @Getter
        private Expression resultExpression;

        public ConditionReplacer(String tableAlias, String conditionField, Object conditionValue) {
            this.tableAlias = tableAlias;
            this.conditionField = conditionField;
            this.conditionValue = conditionValue;
            this.resultExpression = null;
        }

        @Override
        public void visit(EqualsTo equalsTo) {
            if (isTargetCondition(equalsTo)) {
                // 替换为新条件
                resultExpression = createConditionExpression(tableAlias);
            } else {
                resultExpression = equalsTo;
            }
        }

        @Override
        public void visit(AndExpression andExpression) {
            Expression left = processSubExpression(andExpression.getLeftExpression());
            Expression right = processSubExpression(andExpression.getRightExpression());

            resultExpression = new AndExpression(left, right);
        }

        @Override
        public void visit(OrExpression orExpression) {
            Expression left = processSubExpression(orExpression.getLeftExpression());
            Expression right = processSubExpression(orExpression.getRightExpression());

            resultExpression = new OrExpression(left, right);
        }

        private Expression processSubExpression(Expression expression) {
            ConditionReplacer subReplacer = new ConditionReplacer(tableAlias, conditionField, conditionValue);
            expression.accept(subReplacer);
            return subReplacer.getResultExpression();
        }

        private boolean isTargetCondition(EqualsTo equalsTo) {
            if (equalsTo.getLeftExpression() instanceof Column) {
                Column column = (Column) equalsTo.getLeftExpression();

                boolean tableMatches = (column.getTable() != null &&
                        column.getTable().getName() != null &&
                        column.getTable().getName().equals(tableAlias));
                boolean fieldMatches = column.getColumnName().equals(conditionField);

                return tableMatches && fieldMatches;
            }
            return false;
        }
    }

    /**
     * 上下文感知的处理器（原Select处理器）
     */
    @Setter
    class ContextAwareProcessor extends SelectVisitorAdapter {
        // 当前上下文路径
        private String currentContextPath = "";

        @Override
        public void visit(PlainSelect plainSelect) {
            // 保存旧的上下文路径
            String oldContextPath = currentContextPath;

            try {
                // 为当前SELECT生成唯一标识符
                String selectId = "select_" + System.identityHashCode(plainSelect);
                currentContextPath = oldContextPath + "/" + selectId;

                // 处理所有连接表（JOIN），在ON条件中添加租户条件
                processAllJoinsForTenantCondition(plainSelect);

                // 为当前SELECT添加条件
                addConditionToMainTableWhere(plainSelect);

                // 处理SELECT中的子查询
                if (plainSelect.getSelectItems() != null) {
                    processSelectItemWithContext(plainSelect.getSelectItems());
                }

                // 处理FROM中的子查询
                if (plainSelect.getFromItem() != null) {
                    processFromItemWithContext(plainSelect);
                }

                // 处理WHERE条件中的子查询
                if (plainSelect.getWhere() != null) {
                    processWhereSubqueries(plainSelect.getWhere());
                }

            } finally {
                // 恢复上下文路径
                currentContextPath = oldContextPath;
            }
        }

        /**
         * 处理SELECT中的SELECT项
         */
        private void processSelectItemWithContext(List<SelectItem> selectItems) {
            for (SelectItem selectItem: selectItems) {
                if (selectItem instanceof SelectExpressionItem) {
                    SelectExpressionItem expressionItem = (SelectExpressionItem) selectItem;
                    processExpressionWithContext(expressionItem.getExpression(), currentContextPath + "/select");
                }
            }
        }


        /**
         * 处理WHERE中的子查询
         */
        private void processWhereSubqueries(Expression where) {
            ContextAwareExpressionProcessor expressionProcessor = new ContextAwareExpressionProcessor();
            expressionProcessor.setCurrentContext(currentContextPath + "/where");
            where.accept(expressionProcessor);
        }

        /**
         * 创建连接表的租户条件
         */
        private void processAllJoinsForTenantCondition(PlainSelect plainSelect) {
            if (plainSelect.getJoins() == null || plainSelect.getJoins().isEmpty()) {
                return;
            }

            for (int i = 0; i < plainSelect.getJoins().size(); i++) {
                Join join = plainSelect.getJoins().get(i);
                FromItem rightItem = join.getRightItem();
                // 获取连接表的表信息
                TableInfo joinTableInfo = extractTableInfoFromJoinItem(rightItem);

                if (joinTableInfo == null) {
                    continue;
                }

                // 检查是否排除表
                if (isExcludedTable(joinTableInfo.getTableName())) {
                    continue;
                }

                // 如果Join对象是子查询，则不追加租户条件，在子查询中处理
                if (!(rightItem instanceof SubSelect)) {
                    // 如果是Simple Join或CROSS JOIN，则直接将租户条件添加到Where条件中
                    // 注意：CROSS JOIN不允许有ON子句，必须加到WHERE中
                    if (join.isSimple() || join.isCross()) {
                        addConditionToWhere(plainSelect, joinTableInfo);
                    } else {
                        // 判断是否已存在租户条件
                        if (!isAlreadyExists(join, joinTableInfo)) {
                            // 创建连接表的租户条件
                            Expression tenantCondition = createConditionForJoinTable(joinTableInfo);
                            // 将租户条件添加到ON条件中
                            addConditionToJoinOn(join, tenantCondition);
                        }
                    }
                }

                // 处理JOIN中的子查询
                processJoinWithContext(join, currentContextPath + "/join_" + i);
            }
        }

        /**
         * 创建连接表的租户条件
         */
        private void addConditionToAllJoinWhere(PlainSelect plainSelect) {
            // 收集当前SELECT中的所有表
            List<TableInfo> tables = collectTablesFromSelect(plainSelect);

            if (tables.isEmpty()) {
                return;
            }

            // 批量添加Where条件
            for (TableInfo tableInfo: tables) {
                addConditionToWhere(plainSelect, tableInfo);
            }
        }

        /**
         * 检查连接表是否已存在租户条件
         */
        private boolean isAlreadyExists(Join join, TableInfo joinTableInfo) {
            Collection<Expression> onExpressions = join.getOnExpressions();
            if (onExpressions == null || onExpressions.isEmpty()) {
                return false;
            }

            ConditionChecker checker = new ConditionChecker(joinTableInfo.getAlias(), conditionField, conditionValue);
            onExpressions.forEach(expression -> expression.accept(checker));
            return checker.isConditionExists();
        }

        /**
         * 将租户条件添加到JOIN的ON条件中
         */
        private void addConditionToJoinOn(Join join, Expression tenantCondition) {
            // 如果FromItem是子查询，则不追加租户条件，子查询中已添加租户条件
            if (join.getRightItem() != null && join.getRightItem() instanceof SubSelect) {
                return;
            }
            Collection<Expression> onExpressions = join.getOnExpressions();

            if (onExpressions == null || onExpressions.isEmpty()) {
                // 如果没有ON条件，创建新的
                onExpressions = new ArrayList<>();
                onExpressions.add(tenantCondition);
                join.setOnExpressions(onExpressions);
            } else {
                // 添加到现有ON条件中（用AND连接）
                // 将现有ON条件转换为单个表达式（如果多个）
                Expression combinedCondition = combineOnExpressions(onExpressions);
                Expression newCombinedCondition = new AndExpression(combinedCondition, tenantCondition);

                // 清空并添加新的组合条件
                onExpressions.clear();
                onExpressions.add(newCombinedCondition);
            }
        }

        /**
         * 组合多个ON条件表达式为一个表达式
         */
        private Expression combineOnExpressions(Collection<Expression> onExpressions) {
            if (onExpressions == null || onExpressions.isEmpty()) {
                return null;
            }

            Iterator<Expression> iterator = onExpressions.iterator();
            Expression result = iterator.next();

            while (iterator.hasNext()) {
                result = new AndExpression(result, iterator.next());
            }

            return result;
        }

        /**
         * 为连接表创建租户条件
         */
        private Expression createConditionForJoinTable(TableInfo tableInfo) {
            // 如果是特殊表，构建IN条件
            if (isSpecialTable(tableInfo.getTableName())) {
                return buildInTenantCondition(tableInfo.getAlias());
            } else {
                // 普通条件
                return buildNormalCondition(tableInfo.getAlias());
            }
        }

        /**
         * 从JOIN项中提取表信息
         */
        private TableInfo extractTableInfoFromJoinItem(FromItem fromItem) {
            if (fromItem == null) {
                return null;
            }

            final TableInfo[] result = new TableInfo[1];

            fromItem.accept(new FromItemVisitorAdapter() {
                @Override
                public void visit(Table table) {
                    String tableName = table.getName();
                    String alias = table.getAlias() != null ? table.getAlias().getName() : table.getName();
                    result[0] = new TableInfo(tableName, alias);
                }

                @Override
                public void visit(SubSelect subSelect) {
                    // 子查询作为JOIN表，使用别名
                    if (subSelect.getAlias() != null) {
                        result[0] = new TableInfo(
                                subSelect.getAlias().getName(),
                                subSelect.getAlias().getName()
                        );
                    }
                }
            });

            return result[0];
        }

        /**
         * 识别主表（FROM子句中的表）
         */
        private TableInfo identifyMainTable(PlainSelect plainSelect) {
            if (plainSelect.getFromItem() == null) {
                return null;
            }

            final TableInfo[] result = new TableInfo[1];

            plainSelect.getFromItem().accept(new FromItemVisitorAdapter() {
                @Override
                public void visit(Table table) {
                    String tableName = table.getName();
                    String alias = table.getAlias() != null ? table.getAlias().getName() : table.getName();
                    result[0] = new TableInfo(tableName, alias);
                }

                @Override
                public void visit(SubSelect subSelect) {
                    // 子查询作为主表，使用别名
                    if (subSelect.getAlias() != null) {
                        result[0] = new TableInfo(
                                subSelect.getAlias().getName(),
                                subSelect.getAlias().getName()
                        );
                    }
                }
            });

            return result[0];
        }

        @Override
        public void visit(SetOperationList setOpList) {
            String oldContextPath = currentContextPath;
            try {
                for (int i = 0; i < setOpList.getSelects().size(); i++) {
                    currentContextPath = oldContextPath + "/union_" + i;
                    setOpList.getSelects().get(i).accept(this);
                }
            } finally {
                currentContextPath = oldContextPath;
            }
        }

        /**
         * 添加主表租户Where条件
         */
        private void addConditionToMainTableWhere(PlainSelect plainSelect) {
            // 收集当前SELECT中的所有表
            List<TableInfo> tables = collectTablesFromSelect(plainSelect);

            if (tables.isEmpty()) {
                return;
            }

            // 识别主表
            TableInfo mainTableInfo = identifyMainTable(plainSelect);
            if (mainTableInfo == null) {
                return;
            }

            // 如果FromItem是子查询，则不需要追加租户条件
            if (plainSelect.getFromItem() != null && plainSelect.getFromItem() instanceof SubSelect) {
                return;
            }

            addConditionToWhere(plainSelect, mainTableInfo);
        }

        /**
         * 添加WHERE条件
         */
        private void addConditionToWhere(PlainSelect plainSelect, TableInfo tableInfo) {
            // 判断当前表是否需要排除
            if (isExcludedTable(tableInfo.getTableName())) {
                return;
            }

            // 检查WHERE条件中是否已存在相同条件
            if (isConditionAlreadyExists(plainSelect, tableInfo)) {
                return;
            }

            // 创建主表的租户条件
            Expression mainTableCondition;
            if (isSpecialTable(tableInfo.getTableName())) {
                // 特殊表使用IN条件
                mainTableCondition = buildInTenantCondition(tableInfo.getAlias());
            } else {
                // 普通条件
                mainTableCondition = buildNormalCondition(tableInfo.getAlias());
            }

            Expression where = plainSelect.getWhere();
            if (where == null) {
                plainSelect.setWhere(mainTableCondition);
            } else {
                // 递归处理表达式，为 OR 添加括号
                where = processOrConditionWithParentheses(where);
                plainSelect.setWhere(new AndExpression(where, mainTableCondition));
            }
        }

        /**
         * 递归处理表达式，为 OR 条件添加括号
         */
        private Expression processOrConditionWithParentheses(Expression expression) {
            if (expression == null) {
                return null;
            }

            // 如果表达式已经是括号，则直接返回
            if (isParenthesized(expression)) {
                return expression;
            }

            // 如果表达式本身就是 OR 表达式，直接加括号
            if (expression instanceof OrExpression) {
                return new Parenthesis(expression);
            }

            // 如果表达式是 AND 表达式，递归处理左右两边
            if (expression instanceof AndExpression) {
                AndExpression andExpr = (AndExpression) expression;

                // 递归处理左表达式
                Expression left = processOrConditionWithParentheses(andExpr.getLeftExpression());

                // 递归处理右表达式
                Expression right = processOrConditionWithParentheses(andExpr.getRightExpression());

                // 重新构建 AND 表达式
                return new AndExpression(left, right);
            }

            // 其他类型的表达式直接返回
            return expression;
        }

        /**
         * 检查表达式是否已有括号
         */
        private boolean isParenthesized(Expression expression) {
            if (expression instanceof Parenthesis) {
                return true;
            }

            // 检查父节点是否是括号
            // 这个方法需要更复杂的实现，这里简化处理
            return expression.toString().startsWith("(") && expression.toString().endsWith(")");
        }

        /**
         * 构建IN条件（用于特殊表）
         */
        private Expression buildInTenantCondition(String tableAlias) {
            InExpression inExpression = new InExpression();
            inExpression.setLeftExpression(createColumn(conditionField, tableAlias));

            ExpressionList expressionList = new ExpressionList();
            expressionList.addExpressions(
                    convertToExpression(conditionValue),
                    convertToExpression(tenantIdValue)
            );
            inExpression.setRightItemsList(expressionList);

            return inExpression;
        }

        /**
         * 构建普通条件
         */
        private Expression buildNormalCondition(String tableAlias) {
            EqualsTo condition = new EqualsTo();
            condition.setLeftExpression(createColumn(conditionField, tableAlias));
            condition.setRightExpression(convertToExpression(conditionValue));
            return condition;
        }

        /**
         * 创建列对象
         */
        private Column createColumn(String fieldName, String tableAlias) {
            Column column = new Column();
            column.setColumnName(fieldName);
            column.setTable(new Table(null, tableAlias));
            return column;
        }

        /**
         * 检查WHERE条件中是否已存在相同条件
         */
        private boolean isConditionAlreadyExists(PlainSelect plainSelect, TableInfo tableInfo) {
            if (plainSelect.getWhere() == null) {
                return false;
            }

            ConditionChecker checker = new ConditionChecker(tableInfo.getAlias(), conditionField, conditionValue);
            plainSelect.getWhere().accept(checker);
            return checker.isConditionExists();
        }

        /**
         * 处理FROM项（带上下文）
         */
        private void processFromItemWithContext(PlainSelect plainSelect) {
            // 处理FROM项
            FromItem fromItem = plainSelect.getFromItem();
            fromItem.accept(new FromItemVisitorAdapter() {
                @Override
                public void visit(SubSelect subSelect) {
                    if (subSelect.getSelectBody() != null) {
                        String oldContextPath = currentContextPath;
                        try {
                            currentContextPath = oldContextPath + "/from_subquery";
                            subSelect.getSelectBody().accept(ContextAwareProcessor.this);
                        } finally {
                            currentContextPath = oldContextPath;
                        }
                    }
                    // 处理WITH 子查询
                    if (subSelect.getWithItemsList() != null) {
                        // 创建租户ID常量值表达式项
                        SelectExpressionItem tenantValueItem = new SelectExpressionItem(convertToExpression(conditionValue));
                        tenantValueItem.setAlias(new Alias(conditionField));

                        for (WithItem withItem : subSelect.getWithItemsList()) {
                            String oldContextPath = currentContextPath;
                            try {
                                currentContextPath = oldContextPath + "/with_item/";
                                if (withItem.getSubSelect() != null && withItem.getSubSelect().getSelectBody() != null) {
                                    SubSelect select = withItem.getSubSelect();
                                    if (select == null){
                                        return;
                                    }
                                    // 在SELECT中添加租户ID值
                                    if (select.getSelectBody() instanceof PlainSelect) {
                                        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
                                        List<SelectItem> selectItems = plainSelect.getSelectItems();
                                        addSelectTenantId(selectItems, tenantValueItem);

                                    } else if (select.getSelectBody() instanceof SetOperationList) {
                                        // 处理UNION等集合操作
                                        SetOperationList setOpList = (SetOperationList) select.getSelectBody();
                                        for (SelectBody selectBody : setOpList.getSelects()) {
                                            if (selectBody instanceof PlainSelect) {
                                                PlainSelect plainSelect = (PlainSelect) selectBody;
                                                List<SelectItem> selectItems = plainSelect.getSelectItems();
                                                addSelectTenantId(selectItems, tenantValueItem);
                                            }
                                        }
                                    }
                                    ContextAwareProcessor processor = new ContextAwareProcessor();
                                    processor.setCurrentContextPath(currentContextPath);
                                    select.getSelectBody().accept(processor);
                                }
                            } finally {
                                currentContextPath = oldContextPath;
                            }
                        }
                    }
                }
            });
        }

        /**
         * 校验 SELECT中是否已包含租户ID值
         */
        private void addSelectTenantId(List<SelectItem> selectItems, SelectExpressionItem tenantValueItem) {
            boolean selectHasAllColumns = false;
            if (ObjectUtil.isNotEmpty(selectItems)) {
                for (SelectItem selectItem : selectItems) {
                    if (selectItem instanceof AllColumns || selectItem instanceof AllTableColumns) {
                        selectHasAllColumns = true;
                        break;
                    }
                    if (selectItem instanceof SelectExpressionItem) {
                        SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
                        // 如果是COUNT函数，则不再添加
                        Expression expression = selectExpressionItem.getExpression();
                        if (expression instanceof Function) {
                            Function function = (Function) expression;
                            if (function.getName().equalsIgnoreCase("count")) {
                                selectHasAllColumns = true;
                                break;
                            }
                        }
                        // 如果已存在租户ID值，则不再添加
                        if (selectExpressionItem.getAlias() != null && selectExpressionItem.getAlias().getName().equals(conditionField)) {
                            selectHasAllColumns = true;
                            break;
                        }
                    }
                }
                if (!selectHasAllColumns) {
                    selectItems.add(tenantValueItem);
                }
            }
        }

        /**
         * 处理表达式（带上下文）
         */
        private void processExpressionWithContext(Expression expression, String contextPath) {
            if (expression == null) return;

            String oldContextPath = currentContextPath;
            try {
                currentContextPath = contextPath;

                // 使用上下文感知的表达式处理器
                expression.accept(new ContextAwareExpressionProcessor());
            } finally {
                currentContextPath = oldContextPath;
            }
        }

        /**
         * 处理JOIN（带上下文）
         */
        private void processJoinWithContext(Join join, String contextPath) {
            String oldContextPath = currentContextPath;
            try {
                currentContextPath = contextPath;

                // 处理ON条件
                Collection<Expression> onExpressions = join.getOnExpressions();
                if (ObjectUtil.isNotEmpty(onExpressions)) {
                    for (Expression onExpression : onExpressions) {
                        processExpressionWithContext(onExpression, contextPath + "/on");
                    }
                }

                // 处理JOIN右侧
                if (join.getRightItem() != null) {
                    join.getRightItem().accept(new FromItemVisitorAdapter() {
                        @Override
                        public void visit(SubSelect subSelect) {
                            if (subSelect.getSelectBody() != null) {
                                String joinSubContext = contextPath + "/right_subquery";
                                String savePath = currentContextPath;
                                try {
                                    currentContextPath = joinSubContext;
                                    subSelect.getSelectBody().accept(ContextAwareProcessor.this);
                                } finally {
                                    currentContextPath = savePath;
                                }
                            }
                        }
                    });
                }
            } finally {
                currentContextPath = oldContextPath;
            }
        }
    }

    /**
     * 上下文感知的表达式处理器
     */
    class ContextAwareExpressionProcessor extends ExpressionVisitorAdapter {

        private String currentContext = "";

        public void setCurrentContext(String context) {
            this.currentContext = context;
            ContextHolder.setCurrentContext(context);
        }

        @Override
        public void visit(Function function) {
            // 为函数创建唯一上下文
            String funcContext = currentContext + "/func_" + System.identityHashCode(function);

            // 处理函数参数
            ExpressionList parameters = function.getParameters();
            if (parameters != null) {
                List<Expression> expressions = parameters.getExpressions();
                for (int i = 0; i < expressions.size(); i++) {
                    Expression expression = expressions.get(i);
                    String paramContext = funcContext + "/param_" + i;
                    processExpressionWithContext(expression, paramContext);
                }
            }
        }

        @Override
        public void visit(ExistsExpression existsExpression) {
            // EXISTS子查询
            if (existsExpression.getRightExpression() != null) {
                existsExpression.getRightExpression().accept(new ExpressionVisitorAdapter() {
                    @Override
                    public void visit(SubSelect subSelect) {
                        if (subSelect.getSelectBody() != null) {
                            String existsContext = currentContext + "/exists";
                            processSubSelectWithContext(subSelect.getSelectBody(), existsContext);
                        }
                    }
                });
            }
        }

        @Override
        public void visit(SubSelect subSelect) {
            // 普通子查询
            if (subSelect.getSelectBody() != null) {
                String subContext = currentContext + "/subquery";
                processSubSelectWithContext(subSelect.getSelectBody(), subContext);
            }
        }

        /**
         * 处理子查询（带上下文）
         */
        private void processSubSelectWithContext(SelectBody selectBody, String contextPath) {
            ContextAwareProcessor processor = new ContextAwareProcessor();
            String oldPath = processor.currentContextPath;
            try {
                processor.setCurrentContextPath(contextPath);
                selectBody.accept(processor);
            } finally {
                processor.setCurrentContextPath(oldPath);
            }
        }

        /**
         * 处理表达式（带上下文）
         */
        private void processExpressionWithContext(Expression expression, String contextPath) {
            if (expression == null) return;

            String oldPath = currentContext;
            try {
                setCurrentContext(contextPath);
                expression.accept(this);
            } finally {
                setCurrentContext(oldPath);
            }
        }
    }

    /**
     * 条件检查器：检查WHERE中是否已存在相同条件
     */
    static class ConditionChecker extends ExpressionVisitorAdapter {
        private final String tableAlias;
        private final String conditionField;
        private final Object conditionValue;
        @Getter
        private boolean conditionExists = false;

        public ConditionChecker(String tableAlias, String conditionField, Object conditionValue) {
            this.tableAlias = tableAlias;
            this.conditionField = conditionField;
            this.conditionValue = conditionValue;
        }

        @Override
        public void visit(EqualsTo equalsTo) {
            // 检查是否为我们要添加的条件
            if (equalsTo.getLeftExpression() instanceof Column) {
                Column column = (Column) equalsTo.getLeftExpression();

                // 检查表别名和字段名
                boolean tableMatches = column.getTable() == null || (column.getTable() != null &&
                        column.getTable().getName() != null &&
                        column.getTable().getName().equals(tableAlias));
                // 忽略大小写
                boolean fieldMatches = column.getColumnName().equalsIgnoreCase(conditionField);

                // 检查值，不再检查租户值，只要带了租户参数就不再追加
                // boolean valueMatches = false;
                // if (equalsTo.getRightExpression() instanceof LongValue) {
                //     LongValue longValue = (LongValue) equalsTo.getRightExpression();
                //     String value = String.valueOf(longValue.getValue());
                //     valueMatches = value.equals(conditionValue.toString());
                // } else if (equalsTo.getRightExpression() instanceof StringValue) {
                //     StringValue stringValue = (StringValue) equalsTo.getRightExpression();
                //     valueMatches = stringValue.getValue().equals(conditionValue.toString());
                // } else if (equalsTo.getRightExpression() instanceof JdbcParameter) {
                //     // 占位符放行，不重复添加租户条件
                //     valueMatches = true;
                // }

                if (tableMatches && fieldMatches /*&& valueMatches*/) {
                    conditionExists = true;
                }
            }
        }

        @Override
        public void visit(InExpression expr) {
            if (expr.getLeftExpression() instanceof Column) {
                Column column = (Column) expr.getLeftExpression();
                boolean tableMatches = column.getTable() == null || (column.getTable() != null &&
                        column.getTable().getName() != null &&
                        column.getTable().getName().equalsIgnoreCase(tableAlias));
                boolean fieldMatches = column.getColumnName().equalsIgnoreCase(conditionField);
                if (tableMatches && fieldMatches) {
                    conditionExists = true;
                }
            }

        }

        @Override
        public void visit(AndExpression andExpression) {
            // 递归检查AND表达式的两边
            if (andExpression.getLeftExpression() != null) {
                andExpression.getLeftExpression().accept(this);
                if (conditionExists) return;
            }
            if (andExpression.getRightExpression() != null) {
                andExpression.getRightExpression().accept(this);
            }
        }

        @Override
        public void visit(OrExpression orExpression) {
            // 递归检查OR表达式的两边
            if (orExpression.getLeftExpression() != null) {
                orExpression.getLeftExpression().accept(this);
                if (conditionExists) return;
            }
            if (orExpression.getRightExpression() != null) {
                orExpression.getRightExpression().accept(this);
            }
        }
    }

    /**
     * 表信息类
     */
    @Data
    static class TableInfo {
        private String tableName;
        private String alias;

        public TableInfo(String tableName, String alias) {
            this.tableName = tableName;
            this.alias = alias;
        }

    }

    /**
     * 上下文持有者（线程安全的）
     */
    static class ContextHolder {
        private static final ThreadLocal<String> currentContext = new ThreadLocal<>();

        public static String getCurrentContext() {
            return currentContext.get();
        }

        public static void setCurrentContext(String context) {
            currentContext.set(context);
        }

        public static void clear() {
            currentContext.remove();
        }
    }

    /**
     * 从SELECT收集表信息
     */
    private List<TableInfo> collectTablesFromSelect(PlainSelect plainSelect) {
        List<TableInfo> tables = new ArrayList<>();

        // 主表
        if (plainSelect.getFromItem() != null) {
            plainSelect.getFromItem().accept(new FromItemVisitorAdapter() {
                @Override
                public void visit(Table table) {
                    // 获取别名
                    String alias = table.getAlias() != null ? table.getAlias().getName() : table.getName();
                    tables.add(new TableInfo(table.getName(), alias));
                }
            });
        }

        // JOIN表
        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                join.getRightItem().accept(new FromItemVisitorAdapter() {
                    @Override
                    public void visit(Table table) {
                        // 获取别名
                        String alias = table.getAlias() != null ? table.getAlias().getName() : table.getName();
                        tables.add(new TableInfo(table.getName(), alias));
                    }
                });
            }
        }

        // 如果只有一个表，且别名等于表名，则去年别名
        if (tables.size() == 1) {
            TableInfo tableInfo = tables.get(0);
            if (ObjectUtil.equal(tableInfo.getTableName(), tableInfo.getAlias())) {
                tableInfo.setAlias(null);
            }
        }

        return tables;
    }

    private Expression convertToExpression(Object value) {
        if (value instanceof Number) {
            return new LongValue(value.toString());
        } else if (value instanceof String) {
            return new StringValue("'" + value + "'");
        } else if (value instanceof Boolean) {
            return new StringValue(value.toString());
        }
        return new StringValue(value.toString());
    }

    /**
     * 检查表是否在排除列表中
     */
    private boolean isExcludedTable(String tableName) {
        if (tableName == null) {
            return false;
        }

        String name = convertTableName(tableName);
        // 检查是否为排除前缀的表
        for (String prefix : excludeTablePrefix) {
            if (name.startsWith(prefix)) {
                log.info("排除前缀表，统一放行：{}", name);
                return true;
            }
        }

        // 检查表是否在排除列表中
        return excludedTables.contains(name);
    }

    /**
     * 转换表名，用于比较。
     */
    private static String convertTableName(String tableName) {
        String name = tableName.toLowerCase();
        // 移除可能的模式前缀
        if (name.contains(".")) {
            name = name.substring(name.indexOf(".") + 1);
        }
        // 移除可能的引号
        name = name.replace("\"", "").replace("`", "").replace("'", "");
        return name;
    }

    /**
     * 检查表是否为特殊表。
     */
    private boolean isSpecialTable(String tableName) {
        if (tableName == null) {
            return false;
        }

        String name = convertTableName(tableName);
        return specialTables.contains(name);
    }

    /**
     * 空租户访问校验（fail-fast 兜底）。
     * 放行判据：SQL 中每一个业务表（非白名单表）都能被严格证明受租户绑定参数约束；
     * 只要有任一业务表无法证明，或语句结构无法逐表枚举（CTE、集合操作、派生表、子查询等），一律阻断。
     *
     * @param sql 原始 SQL
     * @throws JSQLParserException SQL 解析失败
     */
    public void checkEmptyTenantAccess(String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);

        // 先提取 SQL 涉及的所有业务表（TablesNamesFinder 会递归遍历子查询、CTE、JOIN、UNION 分支），
        // 用于确认是否需要校验，并在放行前与语法结构枚举结果交叉核对，避免解析器版本差异漏掉隐藏表。
        List<String> businessTables = collectBusinessTables(statement);
        // 无业务表：全为白名单/系统表，放行
        if (businessTables.isEmpty()) {
            return;
        }

        // 从语法结构枚举「表出现位置」：CTE、集合操作、派生表、子查询、非表 JOIN 项等无法逐表枚举 → fail-closed
        List<TableOccurrence> occurrences = collectTableOccurrences(statement);
        if (occurrences == null) {
            log.error("空租户上下文访问无法逐表枚举的语句被阻断：{}", businessTables);
            throw new BusinessException("空租户上下文禁止访问非白名单表：" + businessTables.get(0));
        }

        // 交叉校验：语法结构枚举之外若仍存在业务表名，说明存在无法逐表证明的表 → fail-closed
        Set<String> occurrenceNames = new HashSet<>();
        for (TableOccurrence occurrence : occurrences) {
            occurrenceNames.add(occurrence.tableName);
        }
        for (String businessTable : businessTables) {
            if (!occurrenceNames.contains(businessTable)) {
                log.error("空租户上下文访问未被表结构枚举覆盖的业务表被阻断：{}", businessTable);
                throw new BusinessException("空租户上下文禁止访问非白名单表：" + businessTable);
            }
        }

        // 逐表证明：每个业务表出现位置都必须有一条可证明的租户绑定条件，缺一即阻断
        List<Expression> conditionSources = collectProvableConditionSources(statement);
        boolean qualifierRequired = countBusinessOccurrences(occurrences) > 1;
        List<String> unprovenTables = new ArrayList<>();
        for (TableOccurrence occurrence : occurrences) {
            if (isExcludedTable(occurrence.rawName)) {
                continue;
            }
            if (!hasProvableTenantConditionFor(conditionSources, occurrence, qualifierRequired)) {
                unprovenTables.add(occurrence.describe());
            }
        }
        if (!unprovenTables.isEmpty()) {
            log.error("空租户上下文访问未证明租户条件的业务表被阻断：{}", unprovenTables);
            throw new BusinessException("空租户上下文禁止访问非白名单表：" + unprovenTables.get(0));
        }
    }

    /**
     * 提取 SQL 涉及的所有业务表（非白名单表），表名已归一化。
     */
    private List<String> collectBusinessTables(Statement statement) {
        TablesNamesFinder finder = new TablesNamesFinder();
        List<String> tableNames = finder.getTableList(statement);
        List<String> businessTables = new ArrayList<>();
        for (String tableName : tableNames) {
            if (!isExcludedTable(tableName)) {
                businessTables.add(convertTableName(tableName));
            }
        }
        return businessTables;
    }

    /**
     * 枚举语句中可被逐表证明的「表出现位置」。
     * 返回 null 表示该语句结构上无法逐表枚举，调用方按 fail-closed 阻断。
     */
    private List<TableOccurrence> collectTableOccurrences(Statement statement) {
        if (statement instanceof Select) {
            Select select = (Select) statement;
            // 顶层 WITH CTE：无法保证 CTE 内所有表都受限 → fail-closed
            if (select.getWithItemsList() != null && !select.getWithItemsList().isEmpty()) {
                return null;
            }
            return collectSelectOccurrences(select.getSelectBody());
        }
        if (statement instanceof Update) {
            Update update = (Update) statement;
            if (update.getWithItemsList() != null && !update.getWithItemsList().isEmpty()) {
                return null;
            }
            // 多表 UPDATE（JOIN / startJoins / FROM / SELECT 子句）：写入目标表语义随方言变化，
            // 空租户为未登录上下文，不提供逐表证明 → fail-closed
            if ((update.getJoins() != null && !update.getJoins().isEmpty())
                    || (update.getStartJoins() != null && !update.getStartJoins().isEmpty())
                    || update.getFromItem() != null
                    || update.getSelect() != null
                    || update.getTable() == null) {
                return null;
            }
            // SET / WHERE / ORDER BY / LIMIT / RETURNING 中不得出现子查询，
            // 否则可借子查询访问其他业务表绕过隔离
            if (containsSubSelectInExpressions(update.getExpressions())
                    || containsSubSelect(update.getWhere())
                    || containsSubSelectInOrderBy(update.getOrderByElements())
                    || containsSubSelectInLimit(update.getLimit())
                    || containsSubSelectInReturning(update.getReturningExpressionList())) {
                return null;
            }
            return singleOccurrence(update.getTable());
        }
        if (statement instanceof Delete) {
            Delete delete = (Delete) statement;
            if (delete.getWithItemsList() != null && !delete.getWithItemsList().isEmpty()) {
                return null;
            }
            // 多表 DELETE（JOIN / USING / 多表列表）：无法逐表证明 → fail-closed
            if ((delete.getJoins() != null && !delete.getJoins().isEmpty())
                    || (delete.getUsingList() != null && !delete.getUsingList().isEmpty())
                    || (delete.getTables() != null && delete.getTables().size() > 1)
                    || delete.getTable() == null) {
                return null;
            }
            if (containsSubSelect(delete.getWhere())
                    || containsSubSelectInOrderBy(delete.getOrderByElements())
                    || containsSubSelectInLimit(delete.getLimit())) {
                return null;
            }
            return singleOccurrence(delete.getTable());
        }
        return null;
    }

    /**
     * SELECT 查询体仅接受单表 PlainSelect：集合操作（UNION 等）和复杂查询体一律 fail-closed。
     * 枚举 FROM 与 JOIN 中的每一个表出现位置；派生表、子查询、表函数等非表项一律 fail-closed。
     */
    private List<TableOccurrence> collectSelectOccurrences(SelectBody selectBody) {
        if (!(selectBody instanceof PlainSelect)) {
            return null;
        }
        PlainSelect plainSelect = (PlainSelect) selectBody;
        // 显式结构化拒绝：SELECT 列表、WHERE、HAVING、GROUP BY、ORDER BY、LIMIT/OFFSET
        // 任一位置出现子查询一律 fail-closed。
        // 不能只依赖 TablesNamesFinder 提取的表集合唯一性——其部分版本（如 4.2）会漏提取
        // ORDER BY / GROUP BY 中的子查询，使第二张业务表绕过表集合校验（安全评审 F-1/F-2）。
        if (containsSubSelectInSelectItems(plainSelect.getSelectItems())
                || containsSubSelect(plainSelect.getWhere())
                || containsSubSelect(plainSelect.getHaving())
                || containsSubSelectInGroupBy(plainSelect.getGroupBy())
                || containsSubSelectInOrderBy(plainSelect.getOrderByElements())
                || containsSubSelectInLimit(plainSelect.getLimit())
                || containsSubSelect(plainSelect.getOffset())) {
            return null;
        }
        List<TableOccurrence> occurrences = new ArrayList<>();
        FromItem fromItem = plainSelect.getFromItem();
        // 无 FROM（如 SELECT 1）时没有表需要证明
        if (fromItem == null) {
            return occurrences;
        }
        // FROM 必须是单表（Table），子查询等复杂 FROM → fail-closed
        if (!(fromItem instanceof Table)) {
            return null;
        }
        occurrences.add(new TableOccurrence((Table) fromItem));
        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                FromItem rightItem = join.getRightItem();
                // JOIN 右侧必须是真实表：派生表/子查询/表函数内部的业务表无法逐表证明 → fail-closed
                if (!(rightItem instanceof Table)) {
                    return null;
                }
                occurrences.add(new TableOccurrence((Table) rightItem));
            }
        }
        return occurrences;
    }

    private List<TableOccurrence> singleOccurrence(Table table) {
        List<TableOccurrence> occurrences = new ArrayList<>();
        occurrences.add(new TableOccurrence(table));
        return occurrences;
    }

    /**
     * 收集可作为租户条件证明来源的表达式：顶层 WHERE，以及非外连接 JOIN 的 ON 子句。
     */
    private List<Expression> collectProvableConditionSources(Statement statement) {
        List<Expression> sources = new ArrayList<>();
        if (statement instanceof Select) {
            SelectBody selectBody = ((Select) statement).getSelectBody();
            if (!(selectBody instanceof PlainSelect)) {
                return sources;
            }
            PlainSelect plainSelect = (PlainSelect) selectBody;
            if (plainSelect.getWhere() != null) {
                sources.add(plainSelect.getWhere());
            }
            if (plainSelect.getJoins() != null) {
                for (Join join : plainSelect.getJoins()) {
                    if (isInnerJoinOnly(join) && join.getOnExpressions() != null) {
                        sources.addAll(join.getOnExpressions());
                    }
                }
            }
            return sources;
        }
        if (statement instanceof Update && ((Update) statement).getWhere() != null) {
            sources.add(((Update) statement).getWhere());
        }
        if (statement instanceof Delete && ((Delete) statement).getWhere() != null) {
            sources.add(((Delete) statement).getWhere());
        }
        return sources;
    }

    /**
     * 仅 INNER JOIN 与逗号连接（simple）的 ON 条件在语义上等价于 WHERE，可作为证明来源；
     * 外连接（LEFT/RIGHT/FULL）的 ON 条件不约束保留侧表，纳入即形成绕过，
     * CROSS/NATURAL/SEMI/APPLY 语义各不相同，同样一律排除。
     */
    private boolean isInnerJoinOnly(Join join) {
        return !join.isOuter() && !join.isLeft() && !join.isRight() && !join.isFull()
                && !join.isCross() && !join.isNatural() && !join.isSemi() && !join.isApply();
    }

    /**
     * 判断某个表出现位置是否存在可证明的租户绑定条件。
     */
    private boolean hasProvableTenantConditionFor(List<Expression> conditionSources,
                                                  TableOccurrence occurrence,
                                                  boolean qualifierRequired) {
        for (Expression source : conditionSources) {
            if (isProvableConjunction(source, occurrence.tableName, occurrence.aliasName, qualifierRequired)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 统计需要证明的业务表出现位置数量，用于决定租户列是否必须带表限定符。
     * 仅一个业务表时，无表限定的租户列不存在归属歧义，可沿用宽松判定。
     */
    private int countBusinessOccurrences(List<TableOccurrence> occurrences) {
        int count = 0;
        for (TableOccurrence occurrence : occurrences) {
            if (!isExcludedTable(occurrence.rawName)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 表在语句中的一次出现（携带别名）。多表校验按「出现位置」逐个证明，
     * 避免自连接场景下按表名去重后只需一条租户条件即可放行。
     */
    private static final class TableOccurrence {
        private final String rawName;
        private final String tableName;
        private final String aliasName;

        private TableOccurrence(Table table) {
            this.rawName = table.getName();
            this.tableName = convertTableName(table.getName());
            this.aliasName = table.getAlias() != null ? normalizeAlias(table.getAlias().getName()) : null;
        }

        private String describe() {
            return aliasName == null ? tableName : tableName + " " + aliasName;
        }
    }

    /**
     * 判断 PlainSelect 的 OFFSET 子句（OFFSET n ROWS）中是否包含子查询。
     */
    private boolean containsSubSelect(Offset offset) {
        return offset != null && containsSubSelect(offset.getOffset());
    }

    /**
     * 归一化别名：去掉引号并转小写，保证带引号别名（如 AS "u"）能与租户列的限定符匹配。
     */
    private static String normalizeAlias(String alias) {
        if (alias == null) {
            return null;
        }
        return alias.toLowerCase().replace("\"", "").replace("`", "").replace("'", "").trim();
    }

    /**
     * 判断表达式树中是否包含任意子查询（SubSelect）。用于结构化拒绝：
     * 单表可证明路径中一旦出现子查询，即无法保证子查询内的表也受租户约束，一律 fail-closed。
     */
    private boolean containsSubSelect(Expression expression) {
        if (expression == null) {
            return false;
        }
        SubSelectDetector detector = new SubSelectDetector();
        expression.accept(detector);
        return detector.found;
    }

    private boolean containsSubSelectInExpressions(List<Expression> expressions) {
        if (expressions == null) {
            return false;
        }
        for (Expression expression : expressions) {
            if (containsSubSelect(expression)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSubSelectInSelectItems(List<SelectItem> selectItems) {
        if (selectItems == null) {
            return false;
        }
        for (SelectItem item : selectItems) {
            if (item instanceof SelectExpressionItem
                    && containsSubSelect(((SelectExpressionItem) item).getExpression())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSubSelectInGroupBy(GroupByElement groupBy) {
        return groupBy != null && containsSubSelectInExpressions(groupBy.getGroupByExpressions());
    }

    private boolean containsSubSelectInOrderBy(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (containsSubSelect(orderByElement.getExpression())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSubSelectInLimit(Limit limit) {
        if (limit == null) {
            return false;
        }
        return containsSubSelect(limit.getRowCount()) || containsSubSelect(limit.getOffset());
    }

    private boolean containsSubSelectInReturning(List<SelectItem> returningExpressionList) {
        if (returningExpressionList == null) {
            return false;
        }
        for (SelectItem item : returningExpressionList) {
            if (item instanceof SelectExpressionItem
                    && containsSubSelect(((SelectExpressionItem) item).getExpression())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 子查询探测器：沿表达式树递归遍历，命中任意 SubSelect 即标记。
     * SubSelect 同时实现 Expression 与 ItemsList，重写 {@code visit(SubSelect)} 可同时覆盖
     * 表达式位置（WHERE/HAVING/SELECT 列表/ORDER BY/GROUP BY）与 IN(...) 右项位置。
     */
    private static class SubSelectDetector extends ExpressionVisitorAdapter {
        private boolean found = false;

        @Override
        public void visit(SubSelect subSelect) {
            found = true;
        }
    }

    /**
     * 白名单式可证明判断：仅当表达式是顶层 AND 链、且链中存在一条
     * 「租户列(属目标表出现位置) = 绑定参数」或「租户列 IN 受控参数列表」时才可证明。
     * 不使用 ExpressionVisitorAdapter 默认递归——任何非白名单节点（OR/NOT/XOR/CASE/函数/
     * BETWEEN/LIKE/其它比较/Exists/IsNull 等）一律视为不可证明，从而杜绝
     * 「CASE WHEN tenant_id=? THEN FALSE ELSE TRUE END」「IF(tenant_id=?,1,0)」这类
     * 内层命中 tenant=?、但外层语义否定的跨租户绕过。
     *
     * @param qualifierRequired 为 true 时租户列必须带表限定符，用于多业务表场景排除归属歧义
     */
    private boolean isProvableConjunction(Expression where, String tableName, String aliasName,
                                          boolean qualifierRequired) {
        ConjunctionResult result = analyzeConjunction(where, tableName, aliasName, qualifierRequired);
        return !result.unsupported && result.satisfied;
    }

    private ConjunctionResult analyzeConjunction(Expression expr, String tableName, String aliasName,
                                                 boolean qualifierRequired) {
        ConjunctionResult result = new ConjunctionResult();
        if (expr == null) {
            result.unsupported = true;
            return result;
        }
        if (expr instanceof Parenthesis) {
            return analyzeConjunction(((Parenthesis) expr).getExpression(), tableName, aliasName, qualifierRequired);
        }
        if (expr instanceof AndExpression) {
            AndExpression and = (AndExpression) expr;
            ConjunctionResult left = analyzeConjunction(and.getLeftExpression(), tableName, aliasName, qualifierRequired);
            ConjunctionResult right = analyzeConjunction(and.getRightExpression(), tableName, aliasName, qualifierRequired);
            result.satisfied = left.satisfied || right.satisfied;
            result.unsupported = left.unsupported || right.unsupported;
            return result;
        }
        if (expr instanceof EqualsTo) {
            EqualsTo equalsTo = (EqualsTo) expr;
            result.satisfied = (isTenantColumn(equalsTo.getLeftExpression(), tableName, aliasName, qualifierRequired)
                    && isBindParameter(equalsTo.getRightExpression()))
                    || (isTenantColumn(equalsTo.getRightExpression(), tableName, aliasName, qualifierRequired)
                    && isBindParameter(equalsTo.getLeftExpression()));
            return result;
        }
        if (expr instanceof InExpression) {
            InExpression inExpression = (InExpression) expr;
            // NOT IN 语义为「排除」，不构成租户隔离
            if (inExpression.isNot()) {
                result.unsupported = true;
                return result;
            }
            result.satisfied = isTenantColumn(inExpression.getLeftExpression(), tableName, aliasName, qualifierRequired)
                    && isControlledInList(inExpression.getRightItemsList());
            return result;
        }
        // 其余一切节点类型 → 不可证明（fail-closed）
        result.unsupported = true;
        return result;
    }

    private boolean isTenantColumn(Expression expression, String tableName, String aliasName,
                                   boolean qualifierRequired) {
        if (!(expression instanceof Column)) {
            return false;
        }
        Column column = (Column) expression;
        if (column.getColumnName() == null || !column.getColumnName().equalsIgnoreCase(conditionField)) {
            return false;
        }
        // 租户列必须属于目标表出现位置：无表限定（单业务表默认）或限定符匹配表名/别名
        String columnTable = column.getTable() != null ? column.getTable().getName() : null;
        if (columnTable == null) {
            // 多业务表场景下无表限定的租户列无法判定归属，不构成证明
            return !qualifierRequired;
        }
        if (convertTableName(columnTable).equalsIgnoreCase(tableName)) {
            return true;
        }
        return aliasName != null && normalizeAlias(columnTable).equalsIgnoreCase(aliasName);
    }

    private boolean isBindParameter(Expression expression) {
        return expression instanceof JdbcParameter || expression instanceof JdbcNamedParameter;
    }

    private boolean isControlledInList(ItemsList itemsList) {
        if (!(itemsList instanceof ExpressionList)) {
            return false;
        }
        ExpressionList expressionList = (ExpressionList) itemsList;
        if (expressionList.getExpressions() == null || expressionList.getExpressions().isEmpty()) {
            return false;
        }
        for (Expression item : expressionList.getExpressions()) {
            if (!isBindParameter(item)) {
                return false;
            }
        }
        return true;
    }

    private static final class ConjunctionResult {
        private boolean satisfied = false;
        private boolean unsupported = false;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        try {
            TenantProperties tenantProperties = new TenantProperties();
            List<String> excludedTables = new ArrayList<>();
            excludedTables.add("bt_acc_nature");
            tenantProperties.setExcludeTables(excludedTables);
            List<String> specialTables = new ArrayList<>();
            specialTables.add("sys_config");
            tenantProperties.setSpecialTables(specialTables);
            tenantProperties.setTenantId("000");
            // 测试各种SQL语句
            ComplexNestedSQLProcessor processor = new ComplexNestedSQLProcessor("TENANT_ID", "1000", tenantProperties);

            // 测试DELETE
            // String deleteSQL = "DELETE FROM USERS WHERE status = 'INACTIVE'";
            // System.out.println("\n处理前DELETE: " + deleteSQL);
            // System.out.println("处理后DELETE: " + processor.processSQL(deleteSQL).getSql());
            //
            // // 测试UPDATE
            // String updateSQL = "UPDATE users SET name = 'John' WHERE id = 1";
            // System.out.println("\n处理前UPDATE: " + updateSQL);
            // System.out.println("处理后UPDATE: " + processor.processSQL(updateSQL).getSql());
            //
            // // 测试INSERT SELECT
            // String insertSQL = "INSERT INTO backup_users SELECT * FROM users";
            // System.out.println("\n处理前INSERT: " + insertSQL);
            // System.out.println("处理后INSERT: " + processor.processSQL(insertSQL).getSql());
            //
            // String sql_update = "update SYS_REGION set TENANT_ID = '1000' where id = '010EBF14061544D699B6C4974C782137' ";
            // System.out.println("\n处理前update: " + sql_update);
            // System.out.println("处理后update: " + processor.processSQL(sql_update).getSql());
            //
            // String insert = "insert into SYS_REGION (id, name) values ('010EBF14061544D699B6C4974C782137', '中国')";
            // System.out.println("\n处理前insert: " + insert);
            // System.out.println("处理后insert: " + processor.processSQL(insert).getSql());
            //
            // String delete = "delete from SYS_REGION where id = '010EBF14061544D699B6C4974C782137' ";
            // System.out.println("\n处理前delete: " + delete);
            // System.out.println("处理后delete: " + processor.processSQL(delete).getSql());
            //
            // String inSql = "select * from SYS_REGION where TENANT_ID in ('010EBF14061544D699B6C4974C782137', '010EBF14061544D699B6C4974C782138')";
            // System.out.println("\n处理前inSql: " + inSql);
            // System.out.println("处理后inSql: " + processor.processSQL(inSql).getSql());
            // //
            // String inSql2 = "select * from SYS_REGION where TENANT_ID = '010EBF14061544D699B6C4974C782137'";
            // System.out.println("\n处理前inSql: " + inSql2);
            // System.out.println("处理后inSql: " + processor.processSQL(inSql2).getSql());
            // //
            // String sql = "select * from sys_config s where s.type = 'sys' and s.name like '%lin%'";
            // System.out.println("\n处理前sql: " + sql);
            // System.out.println("处理后sql: " + processor.processSQL(sql).getSql());
            //
            // String sql_insert = "insert into SYS_CORP_USER_RESOURCE(USER_ID,CORP_ID,RESOURCE_ID,PERMISSION,MENU_ID)  select ?,?,r.ID,r.PERMISSION,r.MENU_ID  From SYS_RESOURCES r where r.ID in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // System.out.println("\n处理前sql_insert: " + sql_insert);
            // System.out.println("处理后sql_insert: " + processor.processSQL(sql_insert).getSql());
            //
            // sql_update = "update SYS_USER set BEGIN_USE_TIME=?, CODE=?, CORP_ID=?, CREATE_BY=?, CREATE_TIME=?, EMAIL=?, EMPLOYEE_ID=?, IS_ADMIN=?, LOGIN_NAME=?, LOGIN_TYPE=?, PASSWORD=?, PHONE=?, SALT=?, SEX=?, SHOW_REPORT=?, STATUS=?, SYS_SKIN=?, TENANT_ID=?, UPDATE_BY=?, UPDATE_TIME=?, USER_CADN=?, USERNAME=? where ID=?";
            // System.out.println("\n处理前sql_update: " + sql_update);
            // System.out.println("处理后sql_update: " + processor.processSQL(sql_update).getSql());
            //
            // String sql_insert1 = "INSERT INTO table (col1, col2) VALUES (1, 'A')";
            // System.out.println("\n处理前sql_insert: " + sql_insert1);
            // System.out.println("处理后sql_insert: " + processor.processSQL(sql_insert1).getSql());
            //
            // String sql_insert2 = "INSERT INTO table (col1, col2) VALUES (1, 'A'),(1, 'B')";
            // System.out.println("\n处理前sql_insert: " + sql_insert2);
            // System.out.println("处理后sql_insert: " + processor.processSQL(sql_insert2).getSql());
            //
            // String sql1 = "SELECT u.*, o.* FROM users u JOIN orders o ON u.id = o.user_id";
            // System.out.println("测试1 - 简单INNER JOIN查询");
            // System.out.println("处理前: " + sql1);
            // System.out.println("处理后: " + processor.processSQL(sql1).getSql());
            //
            // String sql3 = "SELECT u.*, sub.* FROM users u " +
            //         "JOIN (SELECT * FROM orders WHERE amount > 100) sub ON u.id = sub.user_id";
            // System.out.println("测试3 - 复杂INNER JOIN和LEFT JOIN查询");
            // System.out.println("处理前: " + sql3);
            // System.out.println("处理后: " + processor.processSQL(sql3).getSql());
            //
            // String sql4 = "SELECT * FROM sys_config s WHERE s.type = 'sys'";
            // System.out.println("测试4 - 带WHERE条件的LEFT JOIN查询");
            // System.out.println("处理前: " + sql4);
            // System.out.println("处理后: " + processor.processSQL(sql4).getSql());

            // String sql5 = "SELECT c.id, c.name, c.code, c.parent_id AS parentId, c.type, c.net_id AS netId, (SELECT count(*) FROM sys_corp c1 WHERE c1.parent_id = c.id AND c1.status = 1) childCount FROM sys_corp c WHERE c.status = ? ORDER BY c.sort, c.code";
            // System.out.println("测试5 - 带WHERE条件的LEFT JOIN查询");
            // System.out.println("处理前: " + sql5);
            // System.out.println("处理后: " + processor.processSQL(sql5).getSql());

            // String sql6 = "SELECT count(*) FROM (SELECT acc.id AS id, acc.bank_acc AS bankAcc, acc.acc_name AS accName, acc.corp_id AS corpId, corp.name AS corpName, acccur.cur_id AS curId, c.cur_name curName, acc.acc_type AS accType, att.attribute_name AS attributeName, nat.nature_name AS natureName, banktype.type_name AS bankTypeName, acc.bank_name AS bankName, acc.reg_date, acc.electric_bill AS electricBill FROM bt_bank_acc acc LEFT JOIN BIS_ACC_ATTRIBUTE att ON acc.attribute_id = att.id LEFT JOIN sys_corp corp ON acc.corp_id = corp.id LEFT JOIN bt_acc_nature nat ON acc.nature_id = nat.id LEFT JOIN bt_bank_type banktype ON acc.bank_type_id = banktype.id LEFT JOIN bt_bank_acc_cur acccur ON acc.id = acccur.bank_acc_id LEFT JOIN bt_currency c ON c.id = acccur.cur_id WHERE 1 = 1 AND acc.status = 95 AND acc.VALID_SIGN = 1 AND acc.IS_ONLINE = 0 AND EXISTS (SELECT 1 FROM BT_BANK_ACC_FREEZE WHERE 1 = 1 AND bt_nvl_num((SELECT sum(freeze_money) FROM BT_BANK_ACC_FREEZE WHERE uf_flag = 0 AND status = 95 AND bank_acc_id = acc.id AND cur_id = acccur.cur_id), 0) - bt_nvl_num((SELECT sum(freeze_money) FROM BT_BANK_ACC_FREEZE WHERE uf_flag = 1 AND status = 95 AND bank_acc_id = acc.id AND cur_id = acccur.cur_id), 0) > 0 )) total";
            // System.out.println("测试6 - 带WHERE条件的LEFT JOIN查询");
            // System.out.println("处理前: " + sql6);
            // System.out.println("处理后: " + processor.processSQL(sql6).getSql());
            //
            // String sql7 = "select b1.bank_acc as bankAcc, b1.CUR_ID as curId, b1.bal_date as balDate, b1.bal as bal from bis_acc_bal b1 join (select bank_acc as bankAcc, MAX(bal_date) as max_date from bis_acc_bal group by bank_acc) b2 on b1.bank_acc = b2.bankAcc and b1.BAL_DATE = b2.max_date where (b1.bank_acc in ('6876544', '676878', '676876', '40404040404040', '4040400440404040', '30303030303033030', '202512300011', '20251230001', '2020202020202020', '10101011010010101'))";
            // System.out.println("测试7 - 带WHERE条件的LEFT JOIN查询");
            // System.out.println("处理前: " + sql7);
            // System.out.println("处理后: " + processor.processSQL(sql7).getSql());
            //
            // String sql8 = "SELECT count(*) FROM (SELECT main_info.voucher_state AS voucherState, main_info.voucher_type AS voucherType, main_info.tally_date AS tallyDate, main_info.interest_start_date AS interestStartDate, main_info.voucher_no AS voucherNo, main_info.order_no AS orderNo, main_info.orig_voucher_no AS origVoucherNo, main_info.bus_global_id AS busGlobalId, main_info.id AS id, main_info.checker_code AS checkerCode, main_info.money AS money, main_info.rmk AS rmk, main_info.operator_code AS operatorCode, main_info.bus_race_id AS busRaceId, fc.race_name AS raceName, main_info.bus_Ori_Type AS busOriType, main_info.net_id AS netId FROM (SELECT accbook.voucher_state, accbook.voucher_Type, accbook.tally_date, accbook.interest_start_date, accbook.voucher_no, accbook.order_no, accbook.bus_global_id, accbook.id, accbook.checker_code, accbook.orig_voucher_no, accbook.operator_code, accbook.bus_race_id, accbook.rmk, sum(CASE WHEN accbook.redblue_sign = '1' THEN 0 - dtl.credit_money ELSE dtl.credit_money END) AS money, accbook.bus_ori_type, accbook.net_id FROM fc_accbook accbook, fc_accbook_dtl dtl, (SELECT DISTINCT a.book_id, a.net_id FROM (SELECT dtl.book_id, dtl.net_id, sum(dtl.debit_money) AS money FROM fc_accbook_dtl dtl WHERE dtl.debit_money + dtl.credit_money <> 0 GROUP BY dtl.book_id, dtl.net_id) a, (SELECT dtl.book_id FROM fc_accbook_dtl dtl WHERE dtl.debit_money + dtl.credit_money <> 0 ) b WHERE a.book_id = b.book_id) dtl_info WHERE dtl_info.book_id = accbook.id AND dtl.book_id = dtl_info.book_id AND accbook.operator_code = '872a4fb2e273481d8e1e2f1c5d8556c6' AND (accbook.voucher_state IN ('F', 'T', 'C')) AND accbook.bus_ori_type = 10 AND accbook.redblue_sign = 'B' GROUP BY accbook.voucher_state, accbook.voucher_Type, accbook.tally_date, accbook.operator_code, accbook.checker_code, accbook.bus_race_id, accbook.bus_ori_type, accbook.orig_voucher_no, accbook.rmk, accbook.net_id, accbook.interest_start_date, accbook.voucher_no, accbook.order_no, accbook.bus_global_id, accbook.id) main_info LEFT JOIN fc_bus_race fc ON fc.id = main_info.bus_race_id WHERE 1 = 1 AND (main_info.net_id IN ('4299db17dc1545fb97056b43c616935b', '7600f26b0cad4251b048ee2780904a2b'))) total";
            // System.out.println("测试8 - 带WHERE条件的LEFT JOIN查询");
            // System.out.println("处理前: " + sql8);
            // System.out.println("处理后: " + processor.processSQL(sql8).getSql());
            //
            // String sql10 = "SELECT count(*) FROM (SELECT e.id, e.name AS name, ecba.bank AS bank, ecba.bank_type AS bankType, ecba.external_acc AS externalAcc, ecba.bank_sourceCode AS bankSourceCode, ecba.acc_province AS accProvince, ecba.acc_city AS accCity, ap.prov_input_name AS accProvinceFexName, ac.name AS accCityFexName, ap.prov_input_name AS accProvinceName, ac.name AS accCityName, ecba.is_default_acc AS isDefaultAcc, ecba.remark AS remarks, e.province AS province, e.city AS city, p.prov_input_name AS provinceFexName, c.name AS cityFexName, e.address AS address, s.bank_add AS accAddress, e.bw_type AS bwType, e.soc_code AS socCode FROM (SELECT ec.id, ec.status, ec.name, ec.audit_status, ec.province, ec.city, ec.address, ec.bw_type, ec.soc_code FROM sys_externalcorp_corp ecc LEFT JOIN sys_external_corp ec ON ecc.externalcorp_id = ec.id AND ec.TENANT_ID = '1000' WHERE 1 = 1 AND (ec.is_native = 1 OR ec.is_native IS NULL) AND (ecc.corp_id IN ('0')) AND ecc.TENANT_ID = '1000' UNION SELECT ec2.id, ec2.status, ec2.name, ec2.audit_status, ec2.province, ec2.city, ec2.address, ec2.bw_type, ec2.soc_code FROM sys_external_corp ec2 LEFT JOIN sys_externalcorp_corp ecc2 ON ec2.id = ecc2.externalcorp_id AND ecc2.TENANT_ID = '1000' WHERE (ec2.is_common = 1 OR ec2.is_common IS NULL) AND ecc2.id IS NULL AND (ec2.is_native = 1 OR ec2.is_native IS NULL) AND ec2.TENANT_ID = '1000') e INNER JOIN sys_external_corp_bankacc ecba ON ecba.external_corp_id = e.id AND ecba.TENANT_ID = '1000' LEFT JOIN bt_swift_info s ON ecba.bank_sourcecode = s.swift_code AND s.TENANT_ID = '1000' LEFT JOIN sys_region p ON p.id = e.province AND p.TENANT_ID = '1000' LEFT JOIN sys_region c ON c.id = e.city AND c.TENANT_ID = '1000' LEFT JOIN sys_region ap ON ap.id = ecba.acc_province AND ap.TENANT_ID = '1000' LEFT JOIN sys_region ac ON ac.id = ecba.acc_city AND ac.TENANT_ID = '1000' WHERE e.status = 1 AND e.audit_status = 95) total";
            // System.out.println("测试10 - 带WHERE条件的SELECT查询");
            // System.out.println("处理前: " + sql10);
            // System.out.println("处理后: " + processor.processSQL(sql10).getSql());
            //
            // String sql11 = "SELECT b1.bank_acc AS bankAcc, b1.CUR_ID AS curId, b1.bal_date AS balDate, b1.bal AS bal FROM bis_acc_bal b1 JOIN (SELECT bank_acc AS bankAcc, MAX(bal_date) AS max_date FROM bis_acc_bal GROUP BY bank_acc) b2 ON b1.bank_acc = b2.bankAcc AND b1.BAL_DATE = b2.max_date WHERE (b1.bank_acc IN ('40404040404040', '20251230001', '2020202020202020', '10101010100101011010', '3454645645646456', '6876544', '676876', '50505050050505050505', '676878', '4040400440404040', '10101011010010101', '9090123901312', '606060606006060606', '9090900090909090', '30303030303033030', '202512300011')) ";
            // System.out.println("测试11 - FromItem带子查询的SQL");
            // System.out.println("处理前: " + sql11);
            // System.out.println("处理后: " + processor.processSQL(sql11).getSql());
            //
            // String sql12 = "SELECT count(*) FROM (WITH detail AS (SELECT dm.EXTERNAL_NAME externalName, dm.CORP_ID corpId, dm.CUR_ID curId, ifnull(dm.WRITE_OFF_BAL, dm.RECEIVABLE_MONEY) writeOffBal, datediff(now(), dm.AGING_DATE) agingDay FROM doc_manage dm WHERE 1 = 1 AND dm.status = 95 AND datediff(now(), dm.AGING_DATE) >= 0 AND (dm.WRITE_OFF_BAL <> 0 OR dm.WRITE_OFF_BAL IS NULL)) SELECT * FROM (SELECT s.externalName, s.corpId, s.curId, sum(s.writeOffBal) writeOffBal, sum(if(s.agingDay = 'thirtyDay', s.writeOffBal, 0)) AS 'thirtyDay', sum(if(s.agingDay = 'sixtyDay', s.writeOffBal, 0)) AS 'sixtyDay', sum(if(s.agingDay = 'ninetyDay', s.writeOffBal, 0)) AS 'ninetyDay', sum(if(s.agingDay = 'overNinetyDay', s.writeOffBal, 0)) AS 'overNinetyDay' FROM (SELECT t.externalName, t.corpId, t.curId, t.writeOffBal, CASE WHEN t.agingDay >= 0 AND t.agingDay <= 30 THEN 'thirtyDay' WHEN t.agingDay > 30 AND t.agingDay <= 60 THEN 'sixtyDay' WHEN t.agingDay > 60 AND t.agingDay <= 90 THEN 'ninetyDay' ELSE 'overNinetyDay' END agingDay FROM detail t ) s GROUP BY s.externalName, s.corpId, s.curId) tt) total";
            // System.out.println("测试12 - 带子查询的SQL");
            // System.out.println("处理前: " + sql12);
            // System.out.println("处理后: " + processor.processSQL(sql12).getSql());

            // String sql13 = "SELECT this_.ID AS ID1_104_1_, this_.BEGIN_USE_TIME AS BEGIN_US2_104_1_, this_.CODE AS CODE3_104_1_, this_.CORP_ID AS CORP_ID23_104_1_, this_.CREATE_BY AS CREATE_B4_104_1_, this_.CREATE_TIME AS CREATE_T5_104_1_, this_.EMAIL AS EMAIL6_104_1_, this_.EMPLOYEE_ID AS EMPLOYEE7_104_1_, this_.IS_ADMIN AS IS_ADMIN8_104_1_, this_.LOGIN_NAME AS LOGIN_NA9_104_1_, this_.LOGIN_TYPE AS LOGIN_T10_104_1_, this_.PASSWORD AS PASSWOR11_104_1_, this_.PHONE AS PHONE12_104_1_, this_.SALT AS SALT13_104_1_, this_.SEX AS SEX14_104_1_, this_.SHOW_REPORT AS SHOW_RE15_104_1_, this_.STATUS AS STATUS16_104_1_, this_.SYS_SKIN AS SYS_SKI17_104_1_, this_.TENANT_ID AS TENANT_18_104_1_, this_.UPDATE_BY AS UPDATE_19_104_1_, this_.UPDATE_TIME AS UPDATE_20_104_1_, this_.USER_CADN AS USER_CA21_104_1_, this_.USERNAME AS USERNAM22_104_1_, corp2_.ID AS ID1_60_0_, corp2_.ACCOUNT_LICENCE AS ACCOUNT_2_60_0_, corp2_.ADDRESS AS ADDRESS3_60_0_, corp2_.ADDRESS_EN AS ADDRESS_4_60_0_, corp2_.CERT_NO AS CERT_NO5_60_0_, corp2_.CERT_TYPE AS CERT_TYP6_60_0_, corp2_.CITY AS CITY7_60_0_, corp2_.CODE AS CODE8_60_0_, corp2_.COUNTRY_ID AS COUNTRY_9_60_0_, corp2_.CREATE_BY AS CREATE_10_60_0_, corp2_.CREATE_TIME AS CREATE_11_60_0_, corp2_.CUR_ID AS CUR_ID12_60_0_, corp2_.DAY_LIMIT_QUOTA AS DAY_LIM13_60_0_, corp2_.EQUITYMAINTENANCE_ID AS EQUITYM14_60_0_, corp2_.HEAD_PERSON AS HEAD_PE15_60_0_, corp2_.IS_LIMIT_QUOTA AS IS_LIMI16_60_0_, corp2_.LISTED_COMPANY AS LISTED_17_60_0_, corp2_.MONTH_LIMIT_QUOTA AS MONTH_L18_60_0_, corp2_.NAME AS NAME19_60_0_, corp2_.NAME_EN AS NAME_EN20_60_0_, corp2_.NET_ID AS NET_ID21_60_0_, corp2_.OPERATIONAREA_ID AS OPERATI22_60_0_, corp2_.PARENT_ID AS PARENT_23_60_0_, corp2_.PARENT_IDS AS PARENT_24_60_0_, corp2_.PROVINCE AS PROVINC25_60_0_, corp2_.PROVINCE_ID AS PROVINC26_60_0_, corp2_.RAT_GROUP AS RAT_GRO27_60_0_, corp2_.REPORT_DISPLAY AS REPORT_28_60_0_, corp2_.SOC_CODE AS SOC_COD29_60_0_, corp2_.SORT AS SORT30_60_0_, corp2_.STATUS AS STATUS31_60_0_, corp2_.TYPE AS TYPE32_60_0_, corp2_.UNIT_ATTRIBUTE AS UNIT_AT33_60_0_, corp2_.UNIT_CM AS UNIT_CM34_60_0_, corp2_.unitStoreMaintenance_ID AS unitSto35_60_0_, corp2_.UPDATE_BY AS UPDATE_36_60_0_, corp2_.UPDATE_TIME AS UPDATE_37_60_0_, corp2_.USE_ACCOUNT_CODE AS USE_ACC38_60_0_ FROM SYS_USER this_ LEFT OUTER JOIN SYS_CORP corp2_ ON this_.CORP_ID = corp2_.ID  WHERE this_.LOGIN_NAME = ? AND this_.TENANT_ID = ?";
            // System.out.println("测试13 - 带FROM ITEM的SQL");
            // System.out.println("处理前: " + sql13);
            // System.out.println("处理后: " + processor.processSQL(sql13).getSql());

            // String sql14 = "SELECT * FROM TB_BASE_RATE t WHERE t.is_bank_base_rate = '1' or (code = 'lpr' or BASE_TYPE_NAME = 'LPR') ";
            // System.out.println("测试14 - 带FROM ITEM的SQL");
            // System.out.println("处理前: " + sql14);
            // System.out.println("处理后: " + processor.processSQL(sql14).getSql());

            // String sql15 = "SELECT A.ACCOUNT   FROM  FC_ONLINE_LEDGER A INNER JOIN FC_ACC_BALANCE B ON A.ACCOUNT = B.ACCOUNT   WHERE B.BALANCE_DATE = ?  and ( A.ACCOUNT in ( '6666011020000001','6666011020000000','6666012120000001','6666012122000002','6666012120000000' ))  AND ( A.CHECK_CNT - (SELECT  COUNT(1)   FROM   FC_ACCBOOK, FC_ACCBOOK_DTL   WHERE  FC_ACCBOOK.ID = FC_ACCBOOK_DTL.BOOK_ID   AND FC_ACCBOOK_DTL.ACCOUNT = A.ACCOUNT   AND FC_ACCBOOK.TALLY_DATE > ?   AND FC_ACCBOOK.NET_ID = ?   AND FC_ACCBOOK.CHECKER_CODE IS NOT NULL   AND FC_ACCBOOK.VOUCHER_STATE in ('M','D','W','N') ) <> B.CNT) ";
            // System.out.println("测试14 - 带FROM ITEM的SQL");
            // System.out.println("处理前: " + sql15);
            // System.out.println("处理后: " + processor.processSQL(sql15).getSql());

            // String sql16 = "select count(*) from(  select main_info.voucher_state as voucherState,main_info.voucher_type as voucherType, main_info.tally_date as tallyDate,main_info.interest_start_date as interestStartDate,main_info.voucher_no as voucherNo, main_info.order_no as orderNo,main_info.orig_voucher_no as origVoucherNo, main_info.bus_global_id as busGlobalId,main_info.id as id,main_info.checker_code as checkerCode, main_info.money as money,main_info.rmk as rmk,main_info.operator_code AS operatorCode,main_info.bus_race_id as busRaceId,fc.race_name AS raceName, main_info.bus_Ori_Type as busOriType,main_info.net_id as netId from (select accbook.voucher_state ,accbook.voucher_Type,accbook.tally_date,accbook.interest_start_date,accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id,accbook.checker_code,accbook.orig_voucher_no,accbook.operator_code,accbook.bus_race_id,accbook.rmk,sum(case when accbook.redblue_sign='1' then 0-dtl.credit_money else dtl.credit_money end) as money,  accbook.bus_ori_type, accbook.net_id from fc_accbook accbook, fc_accbook_dtl dtl,(select distinct a.book_id,a.net_id from (select dtl.book_id,dtl.net_id, sum(dtl.debit_money) as money from fc_accbook_dtl dtl where dtl.debit_money + dtl.credit_money<>0  group by dtl.book_id, dtl.net_id ) a,(select dtl.book_id from fc_accbook_dtl dtl where dtl.debit_money + dtl.credit_money<>0 ) b where a.book_id=b.book_id ) dtl_info where dtl_info.book_id=accbook.id  and dtl.book_id = dtl_info.book_id  and accbook.operator_code = ?  and (accbook.voucher_state in ('F','T','C'))  and accbook.bus_ori_type = ?  and accbook.redblue_sign = ?  group by accbook.voucher_state,accbook.voucher_Type,accbook.tally_date,accbook.operator_code,accbook.checker_code,accbook.bus_race_id,accbook.bus_ori_type,accbook.orig_voucher_no,accbook.rmk,accbook.net_id,accbook.interest_start_date,accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id) main_info  left join fc_bus_race fc on  fc.id = main_info.bus_race_id where 1 = 1 and ( main_info.net_id in ( 'e7b83ace93c54bbeb570916686774123','6666','713ba90ab74c4f2dafae1ba9793c3ca1','a78c8bce06824b30b474c2c0906b71f8' )) ) total";
            // System.out.println("测试14 - 带FROM ITEM的SQL");
            // System.out.println("处理前: " + sql16);
            // System.out.println("处理后: " + processor.processSQL(sql16).getSql());

            // String sql17 = "select count(*) from( select fab.voucherState as voucherState,fab.voucherType as voucherType,fab.tallyDate as tallyDate, fab.checkerCode as checkerCode, fab.operatorCode as operatorCode,fab.interestStartDate as interestStartDate,fab.voucherNo as voucherNo,fab.orderNo as orderNo,fab.origVoucherNo as origVoucherNo,fab.busGlobalId as busGlobalId,fab.id as id,fab.creditMoney as creditMoney,fab.rmk as rmk,fab.debitMoney as debitMoney,fab.raceName as raceName, fab.raceId as raceId,fab.busOriType as busOriType,fab.emergencySign as emergencySign,fab.curCode as curCode  from (select voucher_state as voucherState,voucher_type as voucherType, tally_date as tallyDate,interest_start_date as interestStartDate,voucher_no as voucherNo,order_no as orderNo,orig_voucher_no as origVoucherNo,bus_global_id as busGlobalId,main_info.id as id, main_info.checker_code as checkerCode, main_info.operator_code as operatorCode, creditMoney as creditMoney,summary as rmk,debitMoney as debitMoney,race.race_name as raceName,race.id as raceId,bus_ori_type as busOriType,emergency_sign as emergencySign,cur_code as curCode  from (select  accbook.voucher_state,accbook.voucher_type, accbook.net_id , accbook.tally_date,accbook.interest_start_date,accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id,accbook.checker_code,accbook.orig_voucher_no,accbook.operator_code,accbook.bus_race_id,sum(case when accbook.redblue_sign='R' then 0-dtl.credit_money else dtl.credit_money end) as creditMoney,max(dtl.summary_content) as summary, sum(case when accbook.redblue_sign='R' then 0-dtl.debit_money else dtl.debit_money end) as debitMoney,  accbook.bus_ori_type,accbook.emergency_sign,accbook.cur_code  from fc_accbook accbook  left join fc_bus_race race on accbook.bus_race_id = race.id , fc_accbook_dtl dtl,(select distinct a.book_id  from (select dtl.book_id from fc_accbook_dtl dtl where dtl.debit_money + dtl.credit_money<>0 ) a,(select dtl.book_id from fc_accbook_dtl dtl where dtl.debit_money + dtl.credit_money<>0 ) b where a.book_id=b.book_id) dtl_info where  dtl_info.book_id=accbook.id  and dtl.book_id = dtl_info.book_id  group by accbook.voucher_state,accbook.voucher_type,  accbook.tally_date,accbook.operator_code,accbook.checker_code,accbook.bus_race_id,accbook.bus_ori_type,accbook.orig_voucher_no,accbook.interest_start_date,accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id,  accbook.emergency_sign,accbook.cur_code,accbook.net_id) main_info left join fc_bus_race race on  race.id = main_info.bus_race_id  where 1 = 1  and ( main_info.net_id in ( 'e7b83ace93c54bbeb570916686774123','6666','713ba90ab74c4f2dafae1ba9793c3ca1','a78c8bce06824b30b474c2c0906b71f8' )) union select voucher_state as voucherState,voucher_type as voucherType, tally_date as tallyDate,interest_start_date as interestStartDate,voucher_no as voucherNo,order_no as orderNo,orig_voucher_no as origVoucherNo,bus_global_id as busGlobalId,main_info.id as id, main_info.checker_code as checkerCode, main_info.operator_code as operatorCode, creditMoney as creditMoney,summary as rmk,debitMoney as debitMoney,race.race_name as raceName,race.id as raceId,bus_ori_type as busOriType,emergency_sign as emergencySign,cur_code as curCode  from (select  accbook.voucher_state,accbook.voucher_type, accbook.net_id , accbook.tally_date,accbook.interest_start_date,accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id,accbook.checker_code,accbook.orig_voucher_no,accbook.operator_code,accbook.bus_race_id,sum(case when accbook.redblue_sign='R' then 0-dtl.credit_money else dtl.credit_money end) as creditMoney,max(dtl.summary_content) as summary, sum(case when accbook.redblue_sign='R' then 0-dtl.debit_money else dtl.debit_money end) as debitMoney,  accbook.bus_ori_type,accbook.emergency_sign,accbook.cur_code  from fc_accbook_blned accbook  left join fc_bus_race race on accbook.bus_race_id = race.id , fc_accbook_blned_dtl dtl,(select distinct a.book_id  from (select dtl.book_id from fc_accbook_blned_dtl dtl where dtl.debit_money + dtl.credit_money<>0 ) a,(select dtl.book_id from fc_accbook_blned_dtl dtl where dtl.debit_money + dtl.credit_money<>0 ) b where a.book_id=b.book_id) dtl_info where  dtl_info.book_id=accbook.id  and dtl.book_id = dtl_info.book_id  group by accbook.voucher_state,accbook.voucher_type,  accbook.tally_date,accbook.operator_code,accbook.checker_code,accbook.bus_race_id,accbook.bus_ori_type,accbook.orig_voucher_no,accbook.interest_start_date,accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id,  accbook.emergency_sign,accbook.cur_code,accbook.net_id) main_info left join fc_bus_race race on  race.id = main_info.bus_race_id  where 1 = 1  and ( main_info.net_id in ( 'e7b83ace93c54bbeb570916686774123','6666','713ba90ab74c4f2dafae1ba9793c3ca1','a78c8bce06824b30b474c2c0906b71f8' )) ) fab  ) total";
            // System.out.println("测试14 - 带FROM ITEM的SQL");
            // System.out.println("处理前: " + sql17);
            // System.out.println("处理后: " + processor.processSQL(sql17).getSql());

            // String sql18 = "SELECT p.id AS id, p.rate AS rate, p.repay_cycle AS repayCycle, p.pay_way AS payWay, p.pay_money AS payMoney, p.status AS STATUS, p.is_savesign AS isSaveSign, p.create_By AS createBy, p.create_Date AS createDate, p.update_By AS updateBy, p.update_Date AS updateDate, l.id AS leaseId, l.lessor AS lessor, l.lease_type AS leaseType, l.corp_id AS corpId, l.corp_name AS corpName, l.lease_no AS leaseNo, l.lease_code AS leaseCode, l.supplier AS supplier, l.pay_code AS payCode, l.ori_curid AS oriCurid, l.ori_cur_name AS oriCurName, l.ori_money AS oriMoney, l.rmb_money AS rmbMoney, l.begin_date AS beginDate, l.end_date AS endDate, l.status AS leaseStatus, l.biz_status AS bizStatus FROM CMS_LEASE_CAPITAL_PLAN p LEFT JOIN CMS_FINANCING_LEASE_INFO l ON p.lease_id = l.id AND l.tenant_id = '1000' WHERE 1 = 1 AND l.status NOT IN ('0', '11') AND (p.is_savesign = '1' OR p.is_savesign IS NULL) AND p.tenant_id = '1000'";
            // System.out.println("测试14 - 带FROM ITEM的SQL");
            // System.out.println("处理前: " + sql18);
            // System.out.println("处理后: " + processor.processSQL(sql18).getSql());

            String sql19 = "select count(*) from( select case when bpc.status=1 then '1' when bpc.status = 6 then '1' when bpc.status = 3 then '1' when bpc.status = 4 then '1' else '0' end as checkboxHide, bpc.id as Id,bpc.corp_id as corpId ,bpc.orig_no as origNo,bpc.input_date as inputDate,bpc.bill_money as billMoney, bpc.start_Date as startDate,bpc.end_Date as endDate,bpc.cur_Id as curId,bpc.cur_name as curName,bpc.cur_exrate as curExrate, bpc.payee as payee,bpc.payee_bank_name as payeeBankName,bpc.payee_bank_no as payee_bank_no,bpc.payee_acc as payeeAcc,bpc.payer as payer, bpc.payer_bank_name as payerBankName,bpc.payer_bank_no as payerBankNo,bpc.payer_acc as payerAcc,bpc.`use` as uses,bpc.status as status,bpc.corp_name as corpName,bpc.corp_code as corpCode  From Bv_Payee_Cheque bpc where 1=1  and ( bpc.corp_id in (?, ?, ?, ?, ?, ?))  and (bpc.status in ('0','-1','1'))   ) total";
            System.out.println("测试14 - 带FROM ITEM的SQL");
            System.out.println("处理前: " + sql19);
            System.out.println("处理后: " + processor.processSQL(sql19).getSql());

            String sql = "SELECT DISTINCT res.ID as id,res.NAME as name,res.PERMISSION as permission,res.MENU_ID as menuId ,scur.USER_ID as userId,scur.TENANT_ID as tenantId\n" +
                    " from SYS_RESOURCES res left join sys_corp_user_resource scur on res.id = scur.resource_id \n" +
                    " WHERE scur.USER_ID = '3ed799234a64424b9b214dc7988e7adf' AND scur.TENANT_ID = '1000' AND res.TENANT_ID = '1000'";
            System.out.println("测试14 - 带FROM ITEM的SQL");
            System.out.println("处理前: " + sql);
            processor.checkEmptyTenantAccess(sql);

        } catch (Exception e) {
            System.err.println("处理异常：" + e.getMessage());
        }
    }
}
