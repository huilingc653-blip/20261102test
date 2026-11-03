package com.bytter.system.service.impl;
import cn.hutool.core.collection.ListUtil;
import com.bytter.system.api.IMenuService;
import com.bytter.system.service.MenuService;
import com.bytter.system.service.ResourcesService;
import com.bytter.system.service.RoleService;
import com.bytter.system.vo.ResourcesSimpleVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Created by zhoul on 2017/12/11.
 */
@DubboService
public class MenuServiceImpl implements IMenuService {
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourcesService resourcesService;
    @Override
    public Set<String> getEnableMenuIdSetByUserId(String userId) {
        return menuService.getEnableMenuIdSetByUserId(userId);
    }
    @Override
    public List<String> getEnablePermissionByUserId(String userId) {
        List<ResourcesSimpleVO> resourcesData = resourcesService.listResourceByUserId(userId);
        if (resourcesData != null && resourcesData.size() > 0) {
            return resourcesData.stream().map(ResourcesSimpleVO::getPermission).collect(Collectors.toList());
        }
        return ListUtil.empty();
    }
    @Override
    public Set<String> getEnableRoleIdSetByUserId(String userId) {
        return roleService.getRoleIdsByUserId(userId);
    }
    @Override
    public List<Map<String, String>> getEnableMenuIdsAndUrl() {
        return menuService.listEnableMenuIdsAndUrl();
    }


    @Override
    public Set<String> getTenantMenuIds(String tenantId) {
        return menuService.getTenantMenuIds(tenantId);
    }
}
