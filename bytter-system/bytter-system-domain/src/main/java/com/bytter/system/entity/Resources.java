package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;

import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/**
 * Created by zhoul on 2017/4/11.
 */
@Setter
@Entity
@Table(name = "SYS_RESOURCES")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resources extends IdEntity implements Serializable {
    private String name;
    private String description;
    private int status;
    private String permission;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private String url;
    private Menu menu;
    private String menuId;
    private Set<Permission> permissions = new HashSet<>(0);

    private String  tenantId;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MENU_ID")
    @Transient
    public Menu getMenu() {
        return menu;
    }

    @Column(name = "URL", length = 200)
    public String getUrl() {
        return url;
    }

    @Column(name = "NAME", nullable = false, length = 32)
    public String getName() {
        return name;
    }

    @Column(name = "DESCRIPTION", nullable = true, length = 200)
    public String getDescription() {
        return description;
    }

    @Column(name = "STATUS", nullable = false)
    public int getStatus() {
        return status;
    }

    @Column(name = "PERMISSION", nullable = true, length = 200)
    public String getPermission() {
        return permission;
    }

    @Column(name = "CREATE_TIME", nullable = false, updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

    @Column(name = "CREATE_BY", nullable = false, length = 32, updatable = false)
    public String getCreateBy() {
        return createBy;
    }

    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }

    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return updateBy;
    }

    @Transient
    public Set<Permission> getPermissions() {
        return permissions;
    }

    @Column(name = "MENU_ID", nullable = true, length = 32)
    public String getMenuId() {
        return menuId;
    }

    @Column(name = "TENANT_ID", nullable = true, length = 32)
    public String getTenantId() {
        return tenantId;
    }

}
