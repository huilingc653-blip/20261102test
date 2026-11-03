package com.bytter.core.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 数据库方言工具，解决不同数据库 SQL 语法差异。
 * <p>
 * 通过 jdbc.dbType 配置自动适配，避免硬编码特定数据库语法（如 MySQL 的 limit 1 在 Oracle 下不兼容）。
 * </p>
 *
 * <pre>
 * 使用示例：
 * {@code
 * new LambdaQueryChainWrapper<>(FlowModel.class)
 *     .eq(FlowModel::getProDefId, proDefId)
 *     .last(dbDialectHelper.limitOne())
 *     .oneOpt();
 * }
 * </pre>
 *
 * @author zhaohc
 * @date 2026/8/7
 */
@Slf4j
@Component
public class DbDialectHelper {

    @Value("${jdbc.dbType}")
    private String dbType;

    /**
     * 获取数据库对应的 "limit 1" SQL 片段。
     *
     * <pre>
     * MySQL/PostgreSQL/H2/DM  →  " limit 1"
     * Oracle                  →  " AND ROWNUM <= 1"
     * </pre>
     *
     * 注意：Oracle ROWNUM 在 ORDER BY 之前计算，如果查询包含排序请使用
     * {@link #fetchFirstOne()} 或 MyBatis-Plus Page 分页。
     *
     * @return 数据库方言对应的 limit 1 语句
     */
    public String limitOne() {
        return "oracle".equalsIgnoreCase(dbType) ? " AND ROWNUM <= 1" : " limit 1";
    }

    /**
     * 部分 Oracle 新版本(12c+)推荐使用 FETCH FIRST 语法，
     * 该语法在 ORDER BY 之后计算，语义更准确。
     * 适用于需要配合排序的查询。
     *
     * @return 数据库方言对应的 fetch first 1 rows only 语句
     */
    public String fetchFirstOne() {
        return "oracle".equalsIgnoreCase(dbType) ? " FETCH FIRST 1 ROWS ONLY" : " limit 1";
    }
}
