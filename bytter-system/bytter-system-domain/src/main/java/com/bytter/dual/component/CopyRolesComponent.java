package com.bytter.dual.component;
import com.bytter.core.annotation.DualManager;
import com.bytter.system.service.UserService;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/**
 * 双管理员 - 复制角色
 * Created by zhoul on 2017/10/9.
 */
@DualManager
public class CopyRolesComponent extends AbstractDualManagerCommand<Map<String, Object>> {
    @Resource
    private UserService userService;
    @Override
    public String getApprovePageUrl() {
        return "/dual/manager/copyRoles";
    }
    @Override
    void executeData(Map<String, Object> variables) {
        String userId = (String) variables.get("userId");
        List<String> ids = (List<String>) variables.get("ids");
        userService.copyRoles(userId, ids);
    }
}
