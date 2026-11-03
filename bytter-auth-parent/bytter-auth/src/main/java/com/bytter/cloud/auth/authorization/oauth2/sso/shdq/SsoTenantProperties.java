package com.bytter.cloud.auth.authorization.oauth2.sso.shdq;

import com.bytter.cloud.auth.authorization.oauth2.sso.shdq.SsoShdqConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * 单点登录-配置类
 *
 * @author liuwf 2021-08-17
 * @since 1.0.0
 */
@Component
@ConfigurationProperties("sso.service.shdq")
public class SsoTenantProperties {
    private List<SsoShdqConfig> tenants;
    public List<SsoShdqConfig> getTenants() {
        return tenants;
    }
    public void setTenants(List<SsoShdqConfig> tenants) {
        this.tenants = tenants;
    }
}
