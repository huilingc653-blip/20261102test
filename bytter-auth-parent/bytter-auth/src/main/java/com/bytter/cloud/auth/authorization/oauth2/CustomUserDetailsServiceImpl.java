package com.bytter.cloud.auth.authorization.oauth2;

import cn.hutool.crypto.digest.DigestUtil;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.TenantConstant;
import com.bytter.system.api.constant.UserConstant;
import com.bytter.system.api.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

/**
 * 自定义用户加载service，配合spring security
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
@Slf4j
@Service("userDetailsService")
public class CustomUserDetailsServiceImpl extends BaseUserDetailsService {

    @DubboReference(check = true)
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String uniqueId) {
        // 解码租户信息
        String[] parts = uniqueId.split("@BT@");
        String username = parts[0];
        UserDTO user;
        if (parts.length == 2) {
            String tenantId = parts[1];
            log.info("########### 登录用户 username:{},tenantId:{} ############", username, tenantId);
            user = userService.getByLoginName(username, tenantId);
        } else {
            user = userService.getByLoginName(username);
        }
        AssertHelper.isTrueLoginEx(Objects.nonNull(user), "用户名或密码错误!");
        log.info("########### 登录用户 ############:{}", user);
        if (TenantConstant.TENANT_STATUS_UN_SIGNED.equals(user.getTenantStatus())){
            AssertHelper.isTrueLoginEx(false, "该租户未签约");
        } else if (TenantConstant.TENANT_STATUS_DISABLED.equals(user.getTenantStatus())) {
            AssertHelper.isTrueLoginEx(false, "该租户已禁用");
        } else if (TenantConstant.TENANT_STATUS_TERMINATED.equals(user.getTenantStatus())) {
            AssertHelper.isTrueLoginEx(false, "该租户已解约");
        } else if(user.getStatus().equals(UserConstant.STATUS_LOCK)) {
        	AssertHelper.isTrueLoginEx(false, "帐号被锁定");
        }else if(user.getStatus().equals(UserConstant.STATUS_DELETE)) {
        	AssertHelper.isTrueLoginEx(false, "帐号被删除");
        }else if(user.getStatus().equals(UserConstant.STATUS_FORBIDDEN)) {
        	AssertHelper.isTrueLoginEx(false, "帐号被禁用");
        }
        // 检验租户时候授权菜单
        if (parts.length == 2) {
            String tenantId = parts[1];
            Set<String> tenantMenus = this.getrantedTenantMenus(tenantId);
            if (EmptyUtils.isEmpty(tenantMenus)){
                AssertHelper.isTrueLoginEx(false, "本租户未进行功能授权，请联系管理员");
            }
        }
        log.info("########### 登录用户 ############:{}", user);
        Set<GrantedAuthority> roles = this.obtainGrantedAuthorities(user);
        AssertHelper.isTrueLoginEx(!CollectionUtils.isEmpty(roles), "用户未分配角色,请联系管理员");
        String salt = user.getSalt();
        if (StringUtil.isBlank(salt)) {
			salt = DigestUtil.sha256Hex("000"+ user.getLoginName());
        }
        return getAuthUser(user, salt, roles);
    }

    // @NotNull
    // private static AuthUser getAuthUser(UserDTO user, String salt, Set<GrantedAuthority> roles) {
    //     org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(user.getLoginName(),
	// 		   user.getPassword() + "|" + salt,
    //            user.isEnabled(),
    //            user.isAccountNonExpired(),
    //            user.isCredentialsNonExpired(),
    //            user.isAccountNonLocked(),
    //             roles);
    //     AuthUser userImpl = new AuthUser(userDetail);
    //     userImpl.setCurrentUserId(user.getId());
    //     userImpl.setCorpId(user.getCorpId());
    //     //用户名
    //     userImpl.setName(user.getUsername());
    //     userImpl.setLoginName(user.getLoginName());
    //     return userImpl;
    // }

    // /**
    //  * 获得登录者所有角色的权限集合.
    //  *
    //  * @param user 用户
    //  * @return 鉴权信息
    //  */
    // protected Set<GrantedAuthority> obtainGrantedAuthorities(UserDTO user) {
    //     Set<String> roles = userService.queryRolesByUserId(user.getId());
    //     return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    // }
}
