package com.bytter.system.api;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Menu业务
 *
 * @author chenlw 2020-09-23
 * @since V3.4
 */
public interface IMenuService {
    /**
     * 获取 用户拥有的菜单ID集合
     *
     * @param userId 用户ID
     * @return 用户拥有的菜单ID集合
     */
    Set<String> getEnableMenuIdSetByUserId(String userId);
    /**
     * 获取 用户拥有的菜单权限列表集合。
     *
     * @param userId 用户ID。
     * @return 菜单权限集合。
     */
    List<String> getEnablePermissionByUserId(String userId);
    /**
     * 获取 用户拥有的角色ID集合。
     *
     * @param userId 用户ID。
     * @return 用户角色ID集合。
     */
    Set<String> getEnableRoleIdSetByUserId(String userId);
    /**
     * 获取 所有可用的菜单ID和Url集合。
     *
     * @return 菜单ID和Url集合。
     */
    List<Map<String, String>> getEnableMenuIdsAndUrl();

    /**
     * 获取 租户下所有菜单ID集合。
     *
     * @param tenantId 租户ID。
     * @return 菜单ID集合。
     */
    Set<String> getTenantMenuIds(String tenantId);
}
