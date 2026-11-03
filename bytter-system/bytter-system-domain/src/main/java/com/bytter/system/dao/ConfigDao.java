package com.bytter.system.dao;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.jdbc.TenantProperties;
import com.bytter.system.entity.Config;
import com.bytter.core.base.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoul on 2017/5/23.
 */
@Repository
public class ConfigDao extends BaseDao<Config, String> {
    public static final String CONFIG_CACHE_NAME = "configCache";
    @Autowired
    private TenantProperties tenantProperties;
    /**
     * 注意：当前 getByTypeAndName/getGlobalConfig 均未启用读缓存（@Cacheable 已注释），
     * 此处 @CacheEvict 为悬空驱逐。缓存 key 不含租户维度；未来若启用读缓存，
     * key 必须补租户维度（type+name+tenantId）且对 tenantId 做 null 安全，否则会跨租户串读缓存。
     */
    @CacheEvict(value = CONFIG_CACHE_NAME, key = "#config.type.concat(#config.name)")
    public void saveEntity(Config config) {
        save(config);
    }
    @CacheEvict(value = CONFIG_CACHE_NAME, key = "#config.type.concat(#config.name)")
    public void updateEntity(Config config) {
        update(config);
    }
    /**
     * 根据配置类型和配置名获取对象
     *
     * @param config
     * @return
     */
    public Config getByTypeAndName(Config config) {
        return getByTypeAndName(config.getType(), config.getName());
    }

    /**
     * 根据业务类型，和参数名获取值
     *
     * @param type
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
//    @Cacheable(value = CONFIG_CACHE_NAME, key = "#type.concat(#name)")
    public Config getByTypeAndName(String type, String name) {
        String hql = "from Config where type=?0 and name=?1";
        return getUnique(hql, type, name);
    }

    /**
     * 获取全局配置（未登录/空租户场景专用）。
     * <p>显式携带全局租户编号（TENANT_ID = 全局租户ID）作为查询条件，
     * 命中空租户 SQL 校验的“显式租户条件放行”分支，避免未登录时查询系统配置被阻断。</p>
     *
     * @param type 配置类型
     * @param name 配置名
     * @return 全局配置对象，未命中返回 null
     */
    @Transactional(readOnly = true)
    public Config getGlobalConfig(String type, String name) {
        // 全局租户编号以 TenantProperties.tenantId 为唯一权威来源（默认 "000"），
        // 显式携带 TENANT_ID = 全局租户ID，命中空租户 SQL 校验的“显式租户条件放行”分支。
        String hql = "from Config where type=?0 and name=?1 and tenantId=?2";
        return getUnique(hql, type, name, tenantProperties.getTenantId());
    }

    public List<Config> listConfig(String type, String value, List<String> names) {
        Map<String, Object> params = new HashMap<>();
        String hql = "from Config where type=:type and value=:value ";
        if (ObjectUtil.isNotEmpty(names)){
            hql += " and name in (:names) ";
            params.put("names", names);
        }
        params.put("type", type);
        params.put("value", value);
        return this.listByHQL(hql, params);
    }


    public List<Config> listConfigByTypes(  List<String> types) {
        if (ObjectUtil.isEmpty(types)){
            return  new ArrayList<>();
        }
        Map<String, Object> params = new HashMap<>();
        String hql = "from Config where type in (:types) ";
        params.put("types", types);
        return this.listByHQL(hql, params);
    }
}
