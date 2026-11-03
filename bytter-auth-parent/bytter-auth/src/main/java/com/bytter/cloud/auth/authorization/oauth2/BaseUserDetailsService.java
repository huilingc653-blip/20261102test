package com.bytter.cloud.auth.authorization.oauth2;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.cloud.auth.authorization.persistence.AuthUser;
import com.bytter.cloud.auth.constants.ErrorConstant;
import com.bytter.cloud.auth.utils.ThreadLocalRequestUtils;
import com.bytter.system.api.IMenuService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.TenantDTO;
import com.bytter.system.api.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * bytter 父类 UserDetailsService
 *
 * @author limm 2022-07-18
 * @since 1.0.0
 */
@Slf4j
public abstract class BaseUserDetailsService implements UserDetailsService {
    private static final String DEFAULT_TENANT_CODE = "";

    private static final String DEFAULT_REQUEST_HEADER = "X-admin-tenant";

    private static final String DEFAULT_HEADER_VALUE = "true";

    @DubboReference(check = true)
    private IUserService userService;

    @DubboReference(check = true)
    private IMenuService menuService;

    /**
     * 管理员租户验证
     *
     * @param tenant 租户
     */
    protected void verifyAdminTenant(TenantDTO tenant) {
        if (DEFAULT_TENANT_CODE.equals(tenant.getCode())) {
            HttpServletRequest request = ThreadLocalRequestUtils.get();
            if (request != null) {
                String identificationHeader = request.getHeader(DEFAULT_REQUEST_HEADER);
                if (!DEFAULT_HEADER_VALUE.equalsIgnoreCase(identificationHeader)) {
                    throw new InvalidGrantException(ErrorConstant.AUTH_TENANT_NOT_EXIST_MSG);
                }
            } else {
                throw new InvalidGrantException(ErrorConstant.AUTH_TENANT_NOT_EXIST_MSG);
            }
        }
    }

    /**
     * 封装登陆用户信息。
     */
    @NotNull
    public static AuthUser getAuthUser(UserDTO user, String salt, Set<GrantedAuthority> roles) {
        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(user.getLoginName(),
                user.getPassword() + "|" + salt,
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                roles);
        AuthUser userImpl = new AuthUser(userDetail);
        userImpl.setCurrentUserId(user.getId());
        userImpl.setCorpId(user.getCorpId());
        //用户名
        userImpl.setName(user.getUsername());
        userImpl.setLoginName(user.getLoginName());
        userImpl.setTenantId(user.getTenantId());
        userImpl.setSysSkin(user.getSysSkin());
        Map<String, Object> userDetailMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(user.getUserCadn())) {
            userDetailMap.put("userCadn", user.getUserCadn());
        }
        userImpl.setUserDetailMap(userDetailMap);
        return userImpl;
    }

    /**
     * 获取登录租户菜单
     * @param tenantId 租用ID
     * @return
     */
    protected Set<String> getrantedTenantMenus(String tenantId) {
        log.info("########### 获取登录租户菜单 ############:{}", tenantId);
        return menuService.getTenantMenuIds(tenantId);
    }

    /**
     * 获得登录者所有角色的权限集合.
     *
     * @param user 用户
     * @return 鉴权信息
     */
    protected Set<GrantedAuthority> obtainGrantedAuthorities(UserDTO user) {
        log.info("########### 登录用户 ############:{}", user.toString());
        Set<String> roles = userService.queryRolesByUserIdAndTenantId(user.getId(), user.getTenantId());
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

}
