package com.bytter.system.api;
import com.bytter.system.api.dto.MobileChartMenuDTO;
import java.util.List;
import java.util.Set;
/**
 * @author chenlw
 * @since 2020-06-22
 */
public interface IMobileChartMenuService {
    /**
     * 获取报表模块KEY
     *
     * @return 获取报表模块KEY
     */
    String getMobileChartModuleKey();
    /**
     * 获取报表模块的名称
     *
     * @return 报表模块的名称
     */
    String getMobileChartModuleName();
    /**
     * 获取报表的二级菜单列表
     *
     * @param userHavePermissionMenuIds 用户有权限的菜单ID集合
     * @return 报表的二级菜单列表
     */
    List<MobileChartMenuDTO> getMobileChartMenuList(Set<String> userHavePermissionMenuIds);
    /**
     * 获取报表tab报表菜单列表
     *
     * @param menuId                    二级菜单ID
     * @param userHavePermissionMenuIds 用户有权限的菜单ID集合
     * @return 报表tab报表菜单列表
     */
    List<MobileChartMenuDTO> getMobileChartTabMenuList(String menuId, Set<String> userHavePermissionMenuIds);
    /**
     * 获取菜单列表个数，用于判断是否显示菜单
     *
     * @param userHavePermissionMenuIds 用户有权限的菜单ID集合
     * @return 菜单列表个数
     */
    int getUserHavePermissionMenuSize(Set<String> userHavePermissionMenuIds);
}
