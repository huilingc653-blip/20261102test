package com.bytter.core.base.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息
 *
 * @author 周亮
 */
public class ShiroUser implements ICurrentUser, Serializable {
    private static final long serialVersionUID = -1373760761780840081L;

    private String id;
    private String loginName;
    private String username;
    private Set<String> resources;
    private Set<String> permission;
    private Set<String> roles;
    private List<String> roleIds;
    private String userCadn;
    private String corpId;
    private String userAgent;
    private String loginType;
    private String host;
    private String sysSkin;// 系统肤色
    private String uiStyle;// 系统风格
    private String tenantId ;

    private boolean admin;
    private Map<String, Object> userDetailMap;

    public Map<String, Object> getUserDetailMap() {
		return userDetailMap;
	}

	public void setUserDetailMap(Map<String, Object> userDetailMap) {
		this.userDetailMap = userDetailMap;
	}

	@Override
    public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public ShiroUser() {
    }

    public ShiroUser(String id, String loginName, String name) {
        this.id = id;
        this.loginName = loginName;
        this.username = name;
    }

    public ShiroUser(String id, String loginName, String username, Set<String> menus, Set<String> resources,
            Set<String> roles) {
        this.id = id;
        this.loginName = loginName;
        this.username = username;
        this.resources = resources;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getCorpId() {
        return corpId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getResources() {
        return resources;
    }

    public void setResources(Set<String> resources) {
        this.resources = resources;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<String> getPermission() {
        return permission;
    }

    public void setPermission(Set<String> permission) {
        this.permission = permission;
    }

    public String getUserCadn() {
        return userCadn;
    }

    public void setUserCadn(String userCadn) {
        this.userCadn = userCadn;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSysSkin() {
        return sysSkin;
    }

    public void setSysSkin(String sysSkin) {
        this.sysSkin = sysSkin;
    }

    public String getUiStyle() {
        return uiStyle;
    }

    public void setUiStyle(String uiStyle) {
        this.uiStyle = uiStyle;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }
}
