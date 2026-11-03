package com.bytter.cloud.auth.authorization.oauth2.sso.shdq;

import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoConfig;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoTenant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 单点登录配置类-子类
 * - key: "shdq" #上海电气，-禁止修改
 *   domain: ".shanghai-electric.com"
 *   cookie-name: "Ltpatoken" #需要解析的cookie名称
 *   tenant-code: "001" #分配的租户编号
 *   app-code: "code123" #申请的信息，根据实际情况修改
 *   api-account: "admin" #申请的信息，根据实际情况修改
 *   api-password: "admin" #申请的信息，根据实际情况修改
 *   api-url: "http://webqas02.shanghai-electric.com/NAUTHM/services/api/accountcorpServiceI"
 *
 * @author liuwf 2021-08-17
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SsoShdqConfig extends SsoConfig {

    private String tenantCode;

    private String domain;

    private String cookieName;

    private String appCode;

    private String apiAccount;

    private String apiPassword;

    private String apiUrl;

    @Override
    public String getKey() {
        return SsoTenant.SHDQ.getTenant();
    }

}
