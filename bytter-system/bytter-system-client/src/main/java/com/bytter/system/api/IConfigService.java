package com.bytter.system.api;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.ConfigEntityDTO;

import java.util.List;
import java.util.Map;
/**
 * Created by huangx on 2017/5/25.
 */
public interface IConfigService {
    Map<String, Object> listByType(String type);
    /**
     * 保存或更新配置
     *
     * @param parasMap
     */
    void saveOrUpdate(Map<String, String[]> parasMap);

    void saveOrUpdate2(Map<String, String> configDTO);

    void deleteByTypeName(String type, String name);
    Map<String,Object> getConfigMap(String type,String name);
    /**
     * 根据模块和配置名获取配置的值
     *
     * @param type
     * @param name
     * @return
     */
    ConfigDTO getConfig(String type, String name);

    /**
     * 获取全局配置（未登录/空租户场景专用）的值。
     * <p>查询时显式携带全局租户编号，命中空租户 SQL 校验的“显式租户条件放行”分支。</p>
     *
     * @param type 配置类型
     * @param name 配置名
     * @return 配置值
     */
    ConfigDTO getGlobalConfig(String type, String name);

    List<ConfigEntityDTO> listConfigEntityDTO(String  type, String value,List<String> names);

    public void goAccountReceivableReset();

    void deleteById(String id);


     Map<String, Map<String,String>> listByTypes(List<String> types);
}
