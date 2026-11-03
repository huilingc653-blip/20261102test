package com.bytter.cloud.auth.authorization.oauth2;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoTenant;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.utils.StringUtil;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义SSO用户加载service，配合spring security
 *
 * @author zhaohc
 * @date 2024/5/17 15:53
 */
@Slf4j
@Service("ssoUserDetailsService")
public class SsoUserDetailsServiceImpl extends BaseUserDetailsService {

    @DubboReference(check = true)
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String uniqueId) throws UsernameNotFoundException {
        String[] split = uniqueId.split("@BT@");
        String tenant = split[0];
        String username = split[1];
        UserDTO user = null;
        if (ObjectUtil.equal(SsoTenant.DSL.getTenant(), tenant)) {
            user = userService.getByEmployeeId(username);
        } else {
            user = userService.getByLoginName(username);
        }
        AssertHelper.isTrueLoginEx(Objects.nonNull(user), "用户不存在");
        AssertHelper.isTrueLoginEx(user.isAccountNonLocked(), "帐号被锁定");
        Set<GrantedAuthority> roles = this.obtainGrantedAuthorities(user);
        AssertHelper.isTrueLoginEx(!CollectionUtils.isEmpty(roles), "用户未分配角色,请联系管理员");
        String salt = user.getSalt();
        if (StringUtil.isBlank(salt)) {
            salt = DigestUtil.sha256Hex("000" + user.getLoginName());
        }
        return getAuthUser(user, salt, roles);
    }

}
