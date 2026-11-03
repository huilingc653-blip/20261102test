package com.bytter.cloud.auth.authorization.oauth2.sso.base;

/**
 * SSO客户枚举。
 *
 * @author zhaohc
 * @date 2024/5/17 10:14
 */
public enum SsoTenant {
    SHDQ("shdq","上海电气"),
    DSL("dsl","大参林"),
    ;

    SsoTenant(String tenant, String name) {
        this.tenant = tenant;
        this.name = name;
    }

    private final String tenant;
    private final String name;

    public String getTenant() {
        return tenant;
    }

    public String getName() {
        return name;
    }
}
