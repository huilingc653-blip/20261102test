package com.bytter.dual.component;
import com.bytter.core.annotation.DualManager;
import com.bytter.system.entity.Role;
import com.bytter.system.service.RoleService;
import javax.annotation.Resource;
import java.util.Map;
/**
 * 双管理员 - 角色资源授权
 * Created by zhoul on 2017/8/31.
 */
@DualManager
public class RoleAssignComponent extends AbstractDualManagerCommand<Map<String, String>> {
    @Resource
    private RoleService roleService;
    @Override
    public String getApprovePageUrl() {
        return "/dual/manager/roleAssign";
    }
    @Override
    void executeData(Map<String, String> map) {
        Role role = new Role();
        role.setId(map.get("roleId"));
        String menuIds = map.get("menuIds");
        String resourcesIds = map.get("resourcesIds");
        roleService.assign(role, menuIds, resourcesIds);
    }
}
