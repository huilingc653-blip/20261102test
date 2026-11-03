package com.bytter.cloud.auth.authorization.oauth2.sso.dsl;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 单点登录--鉴权http api响应信息
 *
 * @author zhaohc
 * @date 2024/5/16 17:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SsoDslResponse extends SsoResponse {

    private String code;
    private String msg;
    private Object data;

    /**
     * 请求返回是否正常。
     * @return true/false。
     */
    public boolean isSuccess() {
        return ObjectUtil.equal("0",code);
    }
}
