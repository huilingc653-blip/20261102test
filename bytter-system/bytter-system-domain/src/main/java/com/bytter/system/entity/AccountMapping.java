package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * @author luxy
 * @Title: AccountMapping
 * @ProjectName T3.4
 * @Description: 用户账号映射实体类
 * @date 2020/8/26 16:34
 */
@Entity
@Table(name = "SYS_ACCOUNT_MAPPING")
public class AccountMapping extends IdEntity implements Serializable {
    /**
     * 系统用户id
     */
    private String sysUserId;
    /**
     * 其他平台用户id
     */
    private String relationUserId;
    /**
     * 账号映射类型:0-微信 1-钉钉
     */
    private String clientType;
    @Column(name = "SYS_USER_ID")
    public String getSysUserId() {
        return sysUserId;
    }
    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }
    @Column(name = "RELATION_USER_ID")
    public String getRelationUserId() {
        return relationUserId;
    }
    public void setRelationUserId(String relationUserId) {
        this.relationUserId = relationUserId;
    }
    @Column(name = "CLIENT_TYPE")
    public String getClientType() {
        return clientType;
    }
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
