package com.bytter.cloud.auth.authorization.oauth2.sso.dsl;

import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoConfig;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoTenant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 单点登录配置类-大参森
 *
 * @author zhaohc
 * @date 2024/5/16 15:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Configuration
@ConfigurationProperties("sso.service.dsl")
public class SsoDslConfig extends SsoConfig {

    /**
     * 大参林SSO鉴权地址
     */
    private String validateUrl;
    /**
     * 大参林SSO退出地址
     */
    private String ssoLogout;
    /**
     * 大参林SSO查询用户信息地址
     */
    private String queryUrl;
    /**
     * 资金系统登陆跳转地址
     */
    private String loginUrl;
    /**
     * 资金系统退出登陆地址
     */
    private String logoutUrl;

    @Override
    public String getKey() {
        return SsoTenant.DSL.getTenant();
    }
}
