package com.bytter.cloud.auth.authorization.oauth2.granter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
/**
 * 手机验证码登陆provider
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
public class MobileAuthenticationProvider extends DaoAuthenticationProvider {
    public MobileAuthenticationProvider(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
