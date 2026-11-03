package com.bytter.system.velocity.tags;

import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.system.api.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.tools.config.DefaultKey;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * 前端鉴权标签（待实现，先统一放权）。
 *
 * @author zhaohc
 * @date 2023/5/5 16:33
 */
@Slf4j
@DefaultKey("shiro")
public class Permission {

    /**
     * 验证用户是否具备某权限。
     *
     * @param permission 权限名称
     * @return 用户是否具备某权限
     * @throws UnsupportedEncodingException 
     */
    public boolean hasPermission(String permission) throws UnsupportedEncodingException {
        String userId = UserContextHelper.helper().getUserId();
        IUserService iUserService = ApplicationContextUtil.getContext().getBean("userServiceI", IUserService.class);
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        return iUserService.hasPermissionV1(userId, permission,UserContextHelper.helper().getTenantId());
   }

    /**
     * 返回用户 Principal。
     *
     * @return 用户 Principal 使用shiroUser 返回更多参数
     */
    public ShiroUser getPrincipal() {
    	return (ShiroUser) UserContextHelper.helper().getCurrentUser();
    }
    

}
