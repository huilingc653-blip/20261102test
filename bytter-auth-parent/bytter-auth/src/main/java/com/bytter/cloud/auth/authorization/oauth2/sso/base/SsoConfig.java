package com.bytter.cloud.auth.authorization.oauth2.sso.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 单点登录-配置父类
 *
 * @author zhaohc
 * @date 2024/5/17 9:04
 */
@Data
public class SsoConfig implements Serializable {
    /**
     * 标识第三方SSO
     */
    private String key;
}
