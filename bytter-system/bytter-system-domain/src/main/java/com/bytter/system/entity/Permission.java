package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
/**
 * 角色和菜单的关系维护类
 * Created by zhoul on 2017/11/2.
 */
@Entity
@Table(name = "SYS_PERMISSION")
public class Permission extends IdEntity {
    private Role role;
    private Resources resources;
    public Permission() {
    }
    public Permission(Role role, Resources resources) {
        this.role = role;
        this.resources = resources;
    }
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROLE_ID")
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RESOURCES_ID")
    public Resources getResources() {
        return resources;
    }
    public void setResources(Resources resources) {
        this.resources = resources;
    }
}
