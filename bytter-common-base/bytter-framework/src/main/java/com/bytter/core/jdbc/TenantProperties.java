package com.bytter.core.jdbc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 多租户配置。
 *
 * @author zhaohc
 * @since 2025/12/26 15:23
 */
@Data
@Component
@ConfigurationProperties(prefix = "bytter.tenant")
public class TenantProperties implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 租户编号字段名
     */
    private String columnName = "TENANT_ID";
    /**
     * 排除租户改造的表
     */
    private List<String> excludeTables;
    /**
     * 特殊表，添加全局租户条件
     */
    private List<String> specialTables;
    /**
     * 全局租户编号（未配置时默认 "000"，作为全局租户编号的单一权威来源）
     */
    private String tenantId = "000";
    /**
     * 排除表前缀列表
     */
    private List<String> excludeTablePrefix;
    /**
     * 数据库类型：MYSQL / ORACLE，默认 MYSQL
     */
    private String dbType = "MYSQL";
    /**
     * 是否开启多租户
     */
    private Boolean enabled;
    /**
     * 默认租户ID
     */
    private String defaultId;
}
