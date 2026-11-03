package com.bytter.dual.component;
import com.bytter.core.annotation.DualManager;
import com.bytter.system.service.RoleService;
import javax.annotation.Resource;
import java.util.Map;
/**
 * 双管理员 - 角色分配用户
 * Created by zhoul on 2017/8/31.
 */
@DualManager
public class RoleAssignUserComponent extends AbstractDualManagerCommand<Map<String, Object>> {
    @Resource
    private RoleService roleService;
    @Override
    public String getApprovePageUrl() {
        return "/dual/manager/roleAssignUser";
    }
    @Override
    void executeData(Map<String, Object> data) {
        roleService.assignUser(data);
    }
}
