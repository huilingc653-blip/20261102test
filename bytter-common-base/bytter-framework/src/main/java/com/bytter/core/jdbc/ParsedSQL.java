package com.bytter.core.jdbc;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 解析后的SQL信息类
 *
 * @author zhaohc
 * @since 2025/12/22
 */
@Data
public class ParsedSQL {
    /**
     * 原始SQL
     */
    private String originalSql;
    /**
     * 修改后的SQL
     */
    private String sql;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 租户ID字段名
     */
    private String columnName;
    /**
     * 参数索引映射关系
     */
    private Map<Integer, Integer> parameterIndexMapping = new HashMap<>();

    /**
     * 获取参数索引
     *
     * @param originalIndex 原始索引
     * @return 参数索引
     */
    public Integer getParameterIndex(Integer originalIndex) {
        return parameterIndexMapping.getOrDefault(originalIndex, originalIndex);
    }

}