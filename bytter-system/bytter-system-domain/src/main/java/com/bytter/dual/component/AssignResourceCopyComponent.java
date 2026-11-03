package com.bytter.dual.component;
import com.bytter.core.annotation.DualManager;
import com.bytter.system.service.UserService;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/**
 * 双管理员 - 特殊权限 - 复制
 * Created by zhoul on 2017/10/9.
 */
@DualManager
public class AssignResourceCopyComponent extends AbstractDualManagerCommand<Map<String, Object>> {
    @Resource
    private UserService userService;
    @Override
    public String getApprovePageUrl() {
        return "/dual/manager/assignResourceCopy";
    }
    @Override
    void executeData(Map<String, Object> variables) {
        String userId = (String) variables.get("userId");
        String sourceCorpId = (String) variables.get("sourceCorpId");
        List<String> corps = (List<String>) variables.get("corps");
        userService.copyCorpUserResource(userId, sourceCorpId, corps);
    }
}
