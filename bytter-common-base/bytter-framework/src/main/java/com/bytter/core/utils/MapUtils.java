package com.bytter.core.utils;

import java.util.Map;

/**
 * Map 工具类。
 *
 * @author zhaohc
 * @since 2026/7/14 14:46
 */
public class MapUtils {

    /**
     * 从 Map 中按 key 大小写不敏感地取值。
     * <p>
     * 解决 Oracle 数据库通过 Hibernate 的 ALIAS_TO_ENTITY_MAP 查询时，
     * 返回的 Map key 会自动转为大写（如 SQL 写 "as code" 实际返回 "CODE"），
     * 而 MySQL 保持原样，导致调用方用固定大小写 key 取值得到 null 进而 NPE 的问题。
     * </p>
     *
     * @param map 查询结果 Map
     * @param key 目标 key（大小写不敏感）
     * @return 匹配到的值；未匹配返回 null
     */
    public static Object getMapValueIgnoreCase(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        // 优先精确匹配（兼容 MySQL 原样 key）
        if (map.containsKey(key)) {
            return map.get(key);
        }
        // 大小写不敏感匹配（兼容 Oracle 大写 key）
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (key.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
