package com.bytter.system.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
public class LoginLogVO {
    private String id;
    @BtCompositeSearch(fieldName = "username")
    private String username;
    @Dict(dictGroup = "sys.user.loginType", inject = "typeLang")
    @BtCompositeSearch(fieldName = "type")
    private Integer type;
    private String typeLang;
    @BtCompositeSearch(fieldName = "host")
    private String host;
    private String userAgent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName = "loginDate")
    private Date loginDate;

    @BtCompositeSearch(fieldName = "logoutDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date LogoutDate;

    @BtCompositeSearch(fieldName = "REASON")
    private String reason;

    @Dict(dictGroup = "sys.loginLog.status", inject = "statusLang")
    private Integer status;

    private String statusLang;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusLang() {
        return statusLang;
    }
    public void setStatusLang(String statusLang) {
        this.statusLang = statusLang;
    }
    public String getTypeLang() {
        return typeLang;
    }
    public void setTypeLang(String typeLang) {
        this.typeLang = typeLang;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public Date getLoginDate() {
        return loginDate;
    }
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
    public Date getLogoutDate() {
        return LogoutDate;
    }
    public void setLogoutDate(Date logoutDate) {
        LogoutDate = logoutDate;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
}
