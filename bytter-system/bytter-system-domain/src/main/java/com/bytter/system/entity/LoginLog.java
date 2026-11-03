package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "SYS_USER_LOGIN_LOG")
public class LoginLog extends IdEntity implements Serializable {
    private String loginName;
    /**
     * 登录方式
     */
    private Integer type;
    /**
     * 登录IP
     */
    private String host;
    /**
     * 浏览器
     */
    private String userAgent;
    /**
     * 登录时间
     */
    private Date loginDate;
    /**
     * 退出时间
     */
    private Date logoutDate;
    /**
     * 会话Id
     */
    private String sessionId;
    private Integer status;
    private String reason;
    private String tenantId;
    @Column(name = "LOGIN_NAME", nullable = false)
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    @Column(name = "TYPE")
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    @Column(name = "HOST")
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    @Column(name = "USER_AGENT")
    public String getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    @Column(name = "LOGIN_DATE")
    public Date getLoginDate() {
        return loginDate;
    }
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
    @Column(name = "LOGOUT_DATE")
    public Date getLogoutDate() {
        return logoutDate;
    }
    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }
    @Column(name = "REASON")
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    @Column(name = "SESSION_ID")
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
                "loginName='" + loginName + '\'' +
                ", type=" + type +
                ", host='" + host + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", loginDate=" + loginDate +
                ", logoutDate=" + logoutDate +
                ", sessionId='" + sessionId + '\'' +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
