package com.bytter.core.jdbc;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL参数索引分析器，用于分析SQL语句中特定字段的参数索引变化情况。
 *
 * @author zhaohc
 * @since 2025/12/24 15:48
 */
@Slf4j
public class ParameterIndexAnalyzer {

    /**
     * 通用分析方法（自动判断SQL类型），分析参数索引变化。
     *
     * @param sql          SQL语句。
     * @param targetColumn 目标字段名。
     * @return 分析结果。
     */
    public static AnalysisResult analyze(String sql, String targetColumn) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            if (statement instanceof Insert) {
                return analyzeInsert(sql, targetColumn);
            } else if (statement instanceof Update) {
                return analyzeUpdate(sql, targetColumn);
            } else {
                // log.warn("SQL is not an INSERT or UPDATE statement");
                return initResult();
            }
        } catch (JSQLParserException e) {
            log.warn("SQL parse error:", e);
            return initResult();
        }
    }

    /**
     * 分析INSERT语句的参数索引变化
     *
     * @param sql          SQL语句
     * @param targetColumn 目标字段名（如tenant_id）
     * @return 分析结果
     */
    public static AnalysisResult analyzeInsert(String sql, String targetColumn) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (!(statement instanceof Insert)) {
            log.warn("SQL is not an INSERT statement");
            return initResult();
        }

        Insert insert = (Insert) statement;
        List<Column> columns = insert.getColumns();
        ItemsList itemsList = insert.getItemsList();

        if (columns == null || itemsList == null) {
            // 如果没有列列表，则无法确定目标字段位置
            return initResult();
        }

        // 查找目标字段在列列表中的位置
        int targetIndex = -1;
        for (int i = 0; i < columns.size(); i++) {
            if (targetColumn.equalsIgnoreCase(columns.get(i).getColumnName())) {
                targetIndex = i;
                break;
            }
        }

        // 统计原始参数数量
        List<Expression> expressions = extractExpressions(itemsList);
        int originalCount = expressions.size();

        // 检查目标字段是否已被替换（不是占位符?）
        boolean targetReplaced = false;
        if (targetIndex >= 0 && targetIndex < expressions.size()) {
            Expression expr = expressions.get(targetIndex);
            targetReplaced = !(expr instanceof JdbcParameter);
        }

        // 构建索引映射
        Map<Integer, Integer> indexMapping = new HashMap<>();

        if (targetReplaced) {
            // 目标字段被替换，后续参数索引减1
            int newIndex = 0;
            for (int oldIndex = 0; oldIndex < expressions.size(); oldIndex++) {
                if (oldIndex == targetIndex) {
                    // 目标字段被替换，跳过
                    continue;
                }
                indexMapping.put(oldIndex, newIndex);
                newIndex++;
            }

            return new AnalysisResult(
                    indexMapping,
                    originalCount,
                    originalCount - 1, // 少了一个占位符
                    targetIndex,
                    true
            );
        } else {
            // 目标字段未被替换，索引保持不变
            for (int i = 0; i < expressions.size(); i++) {
                indexMapping.put(i, i);
            }

            return new AnalysisResult(
                    indexMapping,
                    originalCount,
                    originalCount,
                    targetIndex,
                    false
            );
        }
    }

    /**
     * 分析UPDATE语句的参数索引变化
     *
     * @param sql          SQL语句
     * @param targetColumn 目标字段名
     * @return 分析结果
     */
    public static AnalysisResult analyzeUpdate(String sql, String targetColumn) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (!(statement instanceof Update)) {
            log.warn("SQL is not an UPDATE statement");
            return initResult();
        }

        Update update = (Update) statement;
        List<UpdateSet> updateSets = update.getUpdateSets();

        if (updateSets == null || updateSets.isEmpty()) {
            return initResult();
        }

        // 查找目标字段在SET列表中的位置
        int targetIndex = -1;
        int totalParams = 0;
        int currentIndex = 0;

        for (UpdateSet updateSet : updateSets) {
            List<Expression> expressions = updateSet.getExpressions();
            if (expressions != null) {
                for (int i = 0; i < expressions.size(); i++) {
                    // 这里简化处理，实际应该匹配列名
                    if (updateSet.getColumns().get(i).getColumnName().equalsIgnoreCase(targetColumn)) {
                        targetIndex = currentIndex;
                    }
                    currentIndex++;
                }
                totalParams += expressions.size();
            }
        }

        // 检查目标字段是否已被替换
        boolean targetReplaced = false;
        if (targetIndex >= 0) {
            // 需要重新遍历检查目标字段的表达式类型
            currentIndex = 0;
            for (UpdateSet updateSet : updateSets) {
                List<Expression> expressions = updateSet.getExpressions();
                if (expressions != null) {
                    for (int i = 0; i < expressions.size(); i++) {
                        if (updateSet.getColumns().get(i).getColumnName().equalsIgnoreCase(targetColumn)) {
                            targetReplaced = !(expressions.get(i) instanceof JdbcParameter);
                            break;
                        }
                        currentIndex++;
                    }
                }
            }
        }

        // 构建索引映射
        Map<Integer, Integer> indexMapping = new HashMap<>();

        if (targetReplaced) {
            int newIndex = 0;
            for (int oldIndex = 0; oldIndex < totalParams; oldIndex++) {
                if (oldIndex == targetIndex) {
                    continue;
                }
                indexMapping.put(oldIndex, newIndex);
                newIndex++;
            }

            return new AnalysisResult(
                    indexMapping,
                    totalParams,
                    totalParams - 1,
                    targetIndex,
                    true
            );
        } else {
            for (int i = 0; i < totalParams; i++) {
                indexMapping.put(i, i);
            }

            return new AnalysisResult(
                    indexMapping,
                    totalParams,
                    totalParams,
                    targetIndex,
                    false
            );
        }
    }

    /**
     * 初始化分析结果。
     *
     * @return 分析结果。
     */
    private static AnalysisResult initResult() {
        return new AnalysisResult(new HashMap<>(), 0, 0, -1, false);
    }

    /**
     * 从ItemsList中提取表达式列表
     */
    private static List<Expression> extractExpressions(ItemsList itemsList) {
        List<Expression> expressions = new ArrayList<>();

        if (itemsList instanceof ExpressionList) {
            ExpressionList exprList = (ExpressionList) itemsList;
            expressions.addAll(exprList.getExpressions());
        } else if (itemsList instanceof MultiExpressionList) {
            // 多行插入，只分析第一行
            MultiExpressionList multiList = (MultiExpressionList) itemsList;
            List<ExpressionList> expressionLists = multiList.getExpressionLists();
            if (ObjectUtil.isNotEmpty(expressionLists)) {
                expressions.addAll(expressionLists.get(0).getExpressions());
            }
        }

        return expressions;
    }

    /**
     * 调整参数绑定顺序的实用方法
     *
     * @param analysis 分析结果
     * @param params   原始参数列表
     * @return 调整后的参数列表（已移除被跳过的参数）
     */
    public static List<Object> adjustParameters(AnalysisResult analysis, List<Object> params) {
        List<Object> adjustedParams = new ArrayList<>();

        for (int i = 0; i < params.size(); i++) {
            int newIndex = analysis.getNewIndex(i);
            if (newIndex != -1) {
                adjustedParams.add(params.get(i));
            }
        }

        return adjustedParams;
    }

    /**
     * 批量调整参数（用于批量插入/更新）
     */
    public static List<List<Object>> adjustBatchParameters(AnalysisResult analysis, List<List<Object>> batchParams) {
        List<List<Object>> adjustedBatch = new ArrayList<>();

        for (List<Object> params : batchParams) {
            adjustedBatch.add(adjustParameters(analysis, params));
        }

        return adjustedBatch;
    }

    /**
     * 分析结果
     */
    @Getter
    public static class AnalysisResult {
        /**
         * 原索引 -> 新索引映射
         */
        private final Map<Integer, Integer> indexMapping;
        /**
         * 原参数总数
         */
        private final int originalParamCount;
        /**
         * 新参数总数
         */
        private final int newParamCount;
        /**
         * 目标字段在列列表中的索引（从0开始）
         */
        private final int targetColumnIndex;
        /**
         * 目字段是否被替换
         */
        private final boolean targetReplaced;

        /**
         * 构造函数。
         *
         * @param indexMapping       原索引 -> 新索引映射。
         * @param originalParamCount 原参数总数。
         * @param newParamCount      新参数总数。
         * @param targetColumnIndex  目标字段在列列表中的索引（从0开始）。
         * @param targetReplaced     目字段是否被替换。
         */
        public AnalysisResult(Map<Integer, Integer> indexMapping, int originalParamCount,
                              int newParamCount, int targetColumnIndex, boolean targetReplaced) {
            this.indexMapping = indexMapping;
            this.originalParamCount = originalParamCount;
            this.newParamCount = newParamCount;
            this.targetColumnIndex = targetColumnIndex;
            this.targetReplaced = targetReplaced;
        }

        /**
         * 根据原参数索引获取新参数索引
         *
         * @param originalIndex 原参数索引（从0开始）
         * @return 新参数索引，-1表示该参数被跳过
         */
        public int getNewIndex(int originalIndex) {
            return indexMapping.getOrDefault(originalIndex, -1);
        }

        /**
         * 根据原参数索引获取新参数索引（JDBC风格，从1开始）
         *
         * @param originalJdbcIndex 原JDBC参数索引（从1开始）
         * @return 新JDBC参数索引，0表示该参数被跳过
         */
        public int getNewJdbcIndex(int originalJdbcIndex) {
            int newIndex = getNewIndex(originalJdbcIndex - 1);
            return newIndex == -1 ? 0 : newIndex + 1;
        }

        @Override
        public String toString() {
            return String.format(
                    "AnalysisResult{original=%d, new=%d, targetIndex=%d, replaced=%s, mapping=%s}",
                    originalParamCount, newParamCount, targetColumnIndex, targetReplaced, indexMapping
            );
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) throws JSQLParserException {
        // 测试1：租户ID被替换的情况
        String sql1 = "INSERT INTO SYS_USER (ID, NAME, TENANT_ID, EMAIL) VALUES (?, ?, '1000', ?)";
        AnalysisResult result1 = analyze(sql1, "TENANT_ID");
        System.out.println("测试1 - 租户ID被替换：");
        System.out.println("SQL: " + sql1);
        System.out.println("分析结果: " + result1);
        System.out.println("索引映射: " + result1.getIndexMapping());

        // 模拟参数绑定
        List<Object> params1 = ListUtil.of("001", "张三", "tenant_original_value", "zhangsan@example.com");
        List<Object> adjusted1 = adjustParameters(result1, params1);
        System.out.println("原始参数: " + params1);
        System.out.println("调整后参数: " + adjusted1);
        System.out.println();

        // 测试2：租户ID未被替换的情况
        String sql2 = "INSERT INTO SYS_USER (ID, NAME, TENANT_ID, EMAIL) VALUES (?, ?, ?, ?)";
        AnalysisResult result2 = analyze(sql2, "TENANT_ID");
        System.out.println("测试2 - 租户ID未被替换：");
        System.out.println("SQL: " + sql2);
        System.out.println("分析结果: " + result2);
        System.out.println("索引映射: " + result2.getIndexMapping());

        List<Object> params2 = ListUtil.of("002", "李四", "2000", "lisi@example.com");
        List<Object> adjusted2 = adjustParameters(result2, params2);
        System.out.println("原始参数: " + params2);
        System.out.println("调整后参数: " + adjusted2);
        System.out.println();

        // 测试3：UPDATE语句
        String sql3 = "UPDATE SYS_USER SET NAME = ?, TENANT_ID = '3000', EMAIL = ? WHERE ID = ?";
        AnalysisResult result3 = analyze(sql3, "TENANT_ID");
        System.out.println("测试3 - UPDATE语句：");
        System.out.println("SQL: " + sql3);
        System.out.println("分析结果: " + result3);
        System.out.println("索引映射: " + result3.getIndexMapping());

        // 测试4：租户ID不在列中
        String sql4 = "INSERT INTO LOGS (ID, MESSAGE, LEVEL) VALUES (?, ?, ?)";
        AnalysisResult result4 = analyze(sql4, "TENANT_ID");
        System.out.println("测试4 - 租户ID不存在：");
        System.out.println("SQL: " + sql4);
        System.out.println("分析结果: " + result4);
        System.out.println();

        // 测试JDBC索引转换
        System.out.println("测试JDBC索引转换（测试1）：");
        for (int jdbcIndex = 1; jdbcIndex <= params1.size(); jdbcIndex++) {
            int newJdbcIndex = result1.getNewJdbcIndex(jdbcIndex);
            System.out.printf("原JDBC索引%d -> 新JDBC索引%d%n", jdbcIndex, newJdbcIndex);
        }
    }
}
