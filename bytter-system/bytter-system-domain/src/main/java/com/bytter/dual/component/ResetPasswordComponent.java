package com.bytter.dual.component;
import com.bytter.core.annotation.DualManager;
import com.bytter.system.service.UserService;
import javax.annotation.Resource;
import java.util.ArrayList;
/**
 * 双管理员 - 重置密码
 * Created by zhoul on 2017/8/31.
 */
@DualManager
public class ResetPasswordComponent extends AbstractDualManagerCommand<String> {
    @Resource
    private UserService userService;
    @Override
    public String getApprovePageUrl() {
        return "/dual/manager/resetPassWord";
    }
    @Override
    void executeData(String userId) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(userId);
        userService.resetPassword(strings);
    }
}
