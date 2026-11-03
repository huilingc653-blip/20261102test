package com.bytter.cloud.auth.authorization.oauth2.sso.dsl;

import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoReqeust;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 单点登录--请求信息
 *
 * @author zhaohc
 * @date 2024/5/16 17:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SsoDslRequest extends SsoReqeust {
    private String employeeId;
    private String st;
    private String service;

}
