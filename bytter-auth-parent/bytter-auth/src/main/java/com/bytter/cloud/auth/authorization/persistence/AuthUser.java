package com.bytter.cloud.auth.authorization.persistence;
import com.bytter.cloud.common.core.persistence.vo.IUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Map;
/**
 * 当前用户对象
 *
 * @author zhoul 2021-1-21
 * @since 1.0.0
 */
public class AuthUser implements IUser, UserDetails {
    private static final long serialVersionUID = 9527L;
    private org.springframework.security.core.userdetails.User user;
    @Getter
    @Setter
    private String currentUserId;
    @Getter
    @Setter
    private String corpId;
    @Getter
    @Setter
    private String tenantId;

    @Getter
    @Setter
    private String loginName;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String dataScope;

    @Getter
    @Setter
    private String sysSkin;

    @Getter
    @Setter
    private Map<String, Object> userDetailMap;
    public AuthUser() {
    }
    public AuthUser(org.springframework.security.core.userdetails.User user) {
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }
    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    /**
     * userName T6系统不唯一所以取loginName
     * @return
     */
    @Override
    public String getUsername() {
        return this.loginName;
    }

}
