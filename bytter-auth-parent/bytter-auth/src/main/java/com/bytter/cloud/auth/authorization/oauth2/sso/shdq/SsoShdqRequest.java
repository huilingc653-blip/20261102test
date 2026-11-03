package com.bytter.cloud.auth.authorization.oauth2.sso.shdq;
import com.bytter.cloud.auth.authorization.oauth2.sso.base.SsoReqeust;
import java.io.Serializable;
/**
 * 单点登录--请求信息
 *
 * @author liuwf 2021-08-17
 * @since 1.0.0
 */
public class SsoShdqRequest extends SsoReqeust {
    public SsoShdqRequest(String token) {
        this.token = token;
    }
    private String token;
    private boolean hasCompanyInfo = false;
    private boolean hasAuthInfo = false;
    private LogInfo logInfo = new LogInfo();
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public boolean isHasCompanyInfo() {
        return hasCompanyInfo;
    }
    public void setHasCompanyInfo(boolean hasCompanyInfo) {
        this.hasCompanyInfo = hasCompanyInfo;
    }
    public boolean isHasAuthInfo() {
        return hasAuthInfo;
    }
    public void setHasAuthInfo(boolean hasAuthInfo) {
        this.hasAuthInfo = hasAuthInfo;
    }
    public LogInfo getLogInfo() {
        return logInfo;
    }
    public void setLogInfo(LogInfo logInfo) {
        this.logInfo = logInfo;
    }
    public static class LogInfo implements Serializable {
        public String getIp() {
            return ip;
        }
        public void setIp(String ip) {
            this.ip = ip;
        }
        public String getHost() {
            return host;
        }
        public void setHost(String host) {
            this.host = host;
        }
        private String ip = "127.0.0.1";
        private String host = "bytter";
    }
}
