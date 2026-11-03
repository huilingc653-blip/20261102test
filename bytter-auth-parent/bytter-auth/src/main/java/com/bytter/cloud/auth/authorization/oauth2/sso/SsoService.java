package com.bytter.cloud.auth.authorization.oauth2.sso;

import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoUser;

import javax.servlet.http.Cookie;

/**
 * 单点登录服务验证service
 *
 * @author liuwf 2021-08-11
 * @since 1.0.0
 */
public interface SsoService {
    /**
     * 租户-key，具有唯一性
     *
     * @return 单点登录服务key
     */
    String ssoKey();

    /**
     * 根据token获取登录账户信息
     *
     * @param token   token
     * @param cookies cookies
     * @return 账户信息
     */
    SsoUser parseSsoAccount(String token, Cookie[] cookies);

}
