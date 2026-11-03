package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
/**
 * Created by zhoul on 2017/4/11.
 */
@Entity
@Table(name = "SYS_ROLE")
public class Role extends IdEntity implements Serializable {
    private String name;
    private String description;
    private Integer useable;
    private String code;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private Set<RoleMenu> roleMenus = new HashSet<>(0);
    private Set<Permission> permissions = new HashSet<>(0);
    private List<UserRole> userRoles = new ArrayList<>(0);
    public Role() {
    }
    @Column(name = "NAME", nullable = false, length = 20)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "DESCRIPTION", length = 200)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Column(name = "USEABLE", nullable = false, length = 2)
    public Integer getUseable() {
        return useable;
    }
    public void setUseable(Integer useable) {
        this.useable = useable;
    }
    @Column(name = "CODE", length = 32)
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "CREATE_TIME", nullable = false, updatable = false)
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "CREATE_BY", nullable = false, length = 32, updatable = false)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    @Column(name = "UPDATE_BY", length = 32)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    public List<UserRole> getUserRoles() {
        return userRoles;
    }
    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    @OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    public Set<RoleMenu> getRoleMenus() {
        return roleMenus;
    }
    public void setRoleMenus(Set<RoleMenu> roleMenus) {
        this.roleMenus = roleMenus;
    }
    @OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    public Set<Permission> getPermissions() {
        return permissions;
    }
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
