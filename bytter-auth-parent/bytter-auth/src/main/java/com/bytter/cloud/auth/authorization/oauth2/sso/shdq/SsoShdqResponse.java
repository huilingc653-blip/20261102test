package com.bytter.cloud.auth.authorization.oauth2.sso.shdq;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoResponse;
/**
 * 单点登录--鉴权http api响应信息
 *
 * @author liuwf 2021-08-17
 * @since 1.0.0
 */
public class SsoShdqResponse extends SsoResponse {
    private boolean isError;
    private String message;
    private String token;
    private ShdqAccount account;
    public boolean isError() {
        return isError;
    }
    public void setError(boolean error) {
        isError = error;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public ShdqAccount getAccount() {
        return account;
    }
    public void setAccount(ShdqAccount account) {
        this.account = account;
    }
}
