package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * @author luxy
 * @Title: AccountMappingDTO
 * @ProjectName T3.4
 * @Description: 用户账号映射DTO
 * @date 2020/8/26 16:42
 */
public class AccountMappingDTO implements Serializable {
    private String id;
    /**
     * 系统用户id(必填)
     */
    private String sysUserId;
    /**
     * 其他平台用户id(必填)
     */
    private String relationUserId;
    /**
     * 账号映射类型:0-微信 1-钉钉(必填)
     */
    private String clientType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSysUserId() {
        return sysUserId;
    }
    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }
    public String getRelationUserId() {
        return relationUserId;
    }
    public void setRelationUserId(String relationUserId) {
        this.relationUserId = relationUserId;
    }
    public String getClientType() {
        return clientType;
    }
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
