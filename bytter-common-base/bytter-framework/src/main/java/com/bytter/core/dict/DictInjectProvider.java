package com.bytter.core.dict;



import java.util.List;
import java.util.Map;

/**
 * 字典注入提供者接口
 * 由各微服务实现，提供具体的字典数据获取方式
 */
public interface DictInjectProvider {
    /**
     * 批量获取字典映射
     *
     * @param dictGroups 字典组编码列表（如：["sex", "status", "type"]）
     * @return 字典组 -> (字典项编码 -> 字典DTO)
     */
    Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups);

    /**
     * 是否支持字典注入
     * 默认返回true，子类可重写控制是否启用
     */
    default boolean isSupported() {
        return true;
    }
}
