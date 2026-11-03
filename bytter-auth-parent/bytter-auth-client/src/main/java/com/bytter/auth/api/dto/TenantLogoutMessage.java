package com.bytter.auth.api.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 内管端通知认证服务注销租户在线令牌的消息。
 */
public class TenantLogoutMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;

    private List<String> clientIds;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public List<String> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<String> clientIds) {
        this.clientIds = clientIds;
    }
}
