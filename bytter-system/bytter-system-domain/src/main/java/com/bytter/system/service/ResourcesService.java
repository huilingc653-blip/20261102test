package com.bytter.system.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.hibernate.criterion.RestrictionExtend;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.RedisUtils;
import com.bytter.system.api.dto.RoleResourceDTO;
import com.bytter.system.dao.ResourcesDao;
import com.bytter.system.entity.Resources;
import com.bytter.system.vo.ResourcesSimpleVO;
import org.hibernate.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bytter.core.cache.redis.core.RedisCacheKey.URL_PERMISSION_MAPPING_ALL;

/**
 * Created by zhoul on 2017/4/12.
 */
@Service("resourcesService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ResourcesService extends BaseService<Resources, String> {

    @Autowired
    private ResourcesDao dao;
    @Resource
    private RedisUtils redisUtils;

    public static final String RESOURCES_CACHE_NAME = "resourcesCache";


    @Override
    public BaseDao<Resources, String> getEntityDao() {
        return dao;
    }

    public List<Resources> listByMenuId(String menuId) {
        List<Resources> list = dao.listByMenuId(menuId);
        return list;
    }

    public List<Resources> listByIds(List<String> ids) {
        if (EmptyUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        Criteria criteria = createCriteria();
        criteria.add(RestrictionExtend.in("id", ids));
        return criteria.list();
    }

    public List<ResourcesSimpleVO> list() {
        return dao.listResource();
    }

    @Caching(put = {
            @CachePut(value = "resourcesId", key = "#resources.id"),
            @CachePut(value = "resourcesPermission", key = "#resources.permission"),
    })
    public Resources save(Resources resources, String userId,String tenantId) {
        resources.setCreateBy(userId);
        resources.setCreateTime(new Date());
        saveEntity(resources);
        // 清除缓存
        clearMenuCache(tenantId);
        return resources;
    }

    @Caching(put = {
            @CachePut(value = "resourcesId", key = "#resources.id"),
            @CachePut(value = "resourcesPermission", key = "#resources.permission"),
    })
    public void update(Resources resources, String userId,String tenantId) {
        resources.setUpdateBy(userId);
        resources.setUpdateTime(new Date());
        updateEntity(resources);
        // 清除缓存
        clearMenuCache(tenantId);
    }

    @CacheEvict(value = "resourcesId", key = "#id")
    public void delete(String id,String tenantId) {
        dao.delete(id);
        dao.deletePermissionByResourceId(id);
        // 清除缓存
        clearMenuCache(tenantId);
    }

    public void clearMenuCache(String tenantId){
        String key = RedisCacheKey.makeRedisKey("", URL_PERMISSION_MAPPING_ALL,tenantId);
        redisUtils.remove(key);
    }

    /**
     * 根据用户ID获取资源
     * 周亮 2017-12-21 14:36:48
     *
     * @param userId 用户ID
     * @return
     */

    public List<ResourcesSimpleVO> listResourceByUserId(String userId) {
        return dao.listResourceByUserId(userId);
    }

    public String[] listPermission(String userId) {
        return dao.listResourceByUserIdNew(userId).stream()
                .map(ResourcesSimpleVO::getPermission).toArray(String[]::new);
    }


    /**
     * 根据角色ID所关联使用的资源
     *
     * @param roleId 角色ID
     * @return 资源ID集合
     */
    public List<String> findResourceIdByRoleId(String roleId) {
        return dao.findResourceIdByRoleId(roleId);
    }

    /**
     * 检查该菜单资源是否被角色所关联使用
     *
     * @param resourceId
     * @return
     */
    public String checkUsedByRoles(String resourceId) {
        List<String> roleNameList = this.dao.findUsedRoleNamesByResourceId(resourceId);
        if (EmptyUtils.isEmptyList(roleNameList)) {
            return "";
        }
        return roleNameList.toString();
    }

    public List<RoleResourceDTO> listRoleResourceByRole(String roleIds) {
        return dao.listRoleResourceByRole(roleIds);
    }

}
