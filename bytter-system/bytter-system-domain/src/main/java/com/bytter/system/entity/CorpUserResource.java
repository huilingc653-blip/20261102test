package com.bytter.system.entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by zhoul on 2017/6/15.
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sys_corp_user_resource", schema = "")
public class CorpUserResource {
    private String corpId;
    private String userId;
    private String resourceId;
    private String permission;
    private String menuId;

    private LocalDateTime createTime = LocalDateTime.now();
    public CorpUserResource() {
    }
    public CorpUserResource(String corpId, String userId, String resourceId) {
        this(corpId, userId);
        this.resourceId = resourceId;
    }
    public CorpUserResource(String corpId, String userId, String resourceId, String permission, String menuId) {
        this(corpId, userId);
        this.resourceId = resourceId;
        this.permission = permission;
        this.menuId = menuId;
    }
    public CorpUserResource(String corpId, String userId) {
        this.corpId = corpId;
        this.userId = userId;
    }
    @Column(name = "CORP_ID", length = 32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "USER_ID", length = 32)
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Column(name = "RESOURCE_ID", length = 32)
    public String getResourceId() {
        return resourceId;
    }
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    @Column(name = "PERMISSION", length = 200)
    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
    @Column(name = "MENU_ID", length = 32)
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @Column(name = "CREATE_TIME")
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
