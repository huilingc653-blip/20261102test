package com.bytter.cloud.auth.authorization.oauth2.sso.base;
import lombok.Getter;
import lombok.Setter;
/**
 * 单点登录-鉴权返回用户信息
 *
 * @author liuwf 2021-08-17
 * @since 1.0.0
 */
@Getter
@Setter
public class SsoUser {
    private String tenantCode;
    private String userName;
    private String employeeId;
}
