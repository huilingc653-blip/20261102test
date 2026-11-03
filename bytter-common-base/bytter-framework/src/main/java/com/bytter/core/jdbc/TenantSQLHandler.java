package com.bytter.core.jdbc;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.digest.MD5;
import com.bytter.core.common.exception.BusinessException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQL拦截器，负责解析SQL并添加租户条件。
 *
 * @author zhaohc
 * @since 2025/12/18 9:30
 */
@Slf4j
public class TenantSQLHandler {

    // Caffeine缓存
    private final Cache<String, ParsedSQL> sqlCache;

    // 租户ID列名
    private String tenantColumnName = "TENANT_ID";
    // 多租户配置
    private TenantProperties tenantProperties;

    public TenantSQLHandler() {
        // 初始化Caffeine缓存
        this.sqlCache = Caffeine.newBuilder()
                .maximumSize(5000) // 最大缓存5000条SQL
                .expireAfterWrite(Duration.ofHours(24)) // 24小时后过期
                .expireAfterAccess(Duration.ofHours(12)) // 12小时无访问过期
                .recordStats() // 记录统计信息
                .removalListener((key, value, cause) ->
                        log.info("SQL缓存移除: key={}, cause={}", key, cause))
                .build();
    }

    /**
     * 拦截并修改SQL
     */
    public ParsedSQL interceptSQL(String originalSql, String tenantId) {
        // 缓存key，需要区分租户ID
        String cacheKey = DigestUtil.md5Hex(tenantId + "|" + originalSql);

        // 检查缓存
        ParsedSQL cached = sqlCache.getIfPresent(cacheKey);
        if (cached != null) {
            log.info("SQL缓存命中: key = 【{}】 \n【原始SQL】{} \n【重写SQL】{}", cacheKey, cached.getOriginalSql(), cached.getSql());
            return cached;
        }

        try {
            // 根据SQL类型进行处理
            ComplexNestedSQLProcessor processor = new ComplexNestedSQLProcessor(tenantColumnName, tenantId, tenantProperties);
            // 一步到位：替换所有换行符并规范化空格
            originalSql = originalSql.replaceAll("[\\r\\n]+", " ").trim();
            log.info("\n【原始SQL】{}", originalSql);
            ParsedSQL parsedSQL = processor.processSQL(originalSql);

            log.info("\n【重写SQL】{}", parsedSQL.getSql());

            // 放入缓存
            sqlCache.put(cacheKey, parsedSQL);
            return parsedSQL;

        } catch (JSQLParserException e) {
            log.error("SQL解析失败: key = 【{}】 \n【原始SQL】{}", cacheKey, originalSql, e);
            // 解析失败，返回原始SQL
            ParsedSQL parsedSQL = new ParsedSQL();
            parsedSQL.setSql(originalSql);
            parsedSQL.setOriginalSql(originalSql);
            parsedSQL.setTenantId(tenantId);
            parsedSQL.setColumnName(tenantColumnName);
            // return parsedSQL;
            throw new BusinessException("SQL解析异常！");
        }
    }

    /**
     * 空租户 SQL 访问校验（fail-fast 兜底）。
     * 租户上下文为空时，仅允许白名单表访问；非白名单表直接阻断，防止空租户直通业务表导致跨租户数据泄露。
     *
     * @param sql 原始 SQL
     */
    public void checkEmptyTenant(String sql) {
        if (tenantProperties == null) {
            log.error("空租户上下文且未配置多租户白名单，阻断执行：{}", sql);
            throw new BusinessException("空租户上下文且未配置多租户白名单，已阻断执行");
        }
        try {
            ComplexNestedSQLProcessor processor = new ComplexNestedSQLProcessor(tenantColumnName, null, tenantProperties);
            processor.checkEmptyTenantAccess(sql);
        } catch (BusinessException e) {
            // 白名单校验阻断（含 DDL 目标表非白名单），直接传播
            throw e;
        } catch (JSQLParserException e) {
            log.error("空租户 SQL 解析失败，阻断执行：{}", sql, e);
            throw new BusinessException("空租户上下文 SQL 解析失败，已阻断执行");
        } catch (UnsupportedOperationException e) {
            // TablesNamesFinder 对部分语句类型（如 ALTER TABLE）不支持，空租户下 fail-closed；
            // 仅收窄捕获该类型，其余 RuntimeException（如 NPE）向上传播，避免误吞编程缺陷
            log.error("空租户 SQL 语句处理失败，阻断执行：{}", sql, e);
            throw new BusinessException("空租户上下文 SQL 语句处理失败，已阻断执行");
        }
    }

    /**
     * 初始化多租户改造配置。
     */
    public void initTenantProperties(TenantProperties tenantProperties) {
        log.info("初始化多租户改造配置：{}", tenantProperties);
        this.tenantProperties = tenantProperties;
        if (ObjectUtil.isNotEmpty(tenantProperties.getColumnName())) {
            tenantColumnName = tenantProperties.getColumnName();
        }
    }


}
