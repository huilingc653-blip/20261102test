package com.bytter.dual.component;
import com.bytter.core.annotation.DualManager;
import com.bytter.system.service.UserService;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/**
 * 双管理员 - 用户授权角色
 * Created by zhoul on 2018/12/28.
 */
@DualManager
public class UserAssignRoleComponent extends AbstractDualManagerCommand<Map<String, Object>> {
    @Resource
    private UserService userService;
    @Override
    public String getApprovePageUrl() {
        return "/dual/manager/userAssignRole";
    }
    @Override
    void executeData(Map<String, Object> map) {
        String userId = (String) map.get("userId");
        List<String> ids = (List<String>) map.get("roleIds");
        userService.assignRoles(userId, ids);
    }
}
