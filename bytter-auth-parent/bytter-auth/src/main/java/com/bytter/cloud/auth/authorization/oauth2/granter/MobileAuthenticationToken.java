package com.bytter.cloud.auth.authorization.oauth2.granter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
/**
 * 手机验证码登陆Token认证类
 *
 * @author zhoul 2021-1-26
 * @since 1.0.0
 */
public class MobileAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public MobileAuthenticationToken(Authentication authenticationToken) {
        super(authenticationToken.getPrincipal(), authenticationToken.getCredentials());
    }
}
