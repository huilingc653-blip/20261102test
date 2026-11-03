package com.bytter.system.entity;
import com.bytter.core.base.entity.TreeEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
/**
 * Created by zhoul on 2017/4/11.
 */
@Entity
@Table(name = "SYS_MENU")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu extends TreeEntity<Menu> implements Serializable {
    private static int ENABLE = 1;
    private String parentId;
    private String parentIds;
    private String name;
    private String description;
    private Integer sort;
    private Integer status;
    private String code;
    private String url;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private String icon;
    private String oldIcon;
    private Integer isModule;
    /**
     * 是否为二开菜单。，1=是；0或者null=否
     */
    private Integer isExtend;
    private boolean isCollect;
    private List<Resources> resources = new ArrayList<Resources>(0);
    private Set<RoleMenu> roleMenus = new HashSet<>(0);
    public Menu() {
    }
    @Column(name = "PARENT_ID", length = 32)
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    @Column(name = "PARENT_IDS", length = 330)
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    @Column(name = "NAME", nullable = false, length = 50)
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
    @Column(name = "SORT", nullable = false)
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    @Column(name = "STATUS", nullable = false)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "URL")
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    @Transient
    public List<Resources> getResources() {
        return resources;
    }
    public void setResources(List<Resources> resources) {
        this.resources = resources;
    }
    @Transient
    public Set<RoleMenu> getRoleMenus() {
        return roleMenus;
    }
    public void setRoleMenus(Set<RoleMenu> roleMenus) {
        this.roleMenus = roleMenus;
    }
    @Column(name = "ICON")
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    @Column(name = "OLD_ICON")
    public String getOldIcon() {
        return oldIcon;
    }
    public void setOldIcon(String oldIcon) {
        this.oldIcon = oldIcon;
    }
    @Transient
    public boolean isEnable() {
        return ENABLE == getStatus();
    }
    @Transient
    public boolean isCollect() {
        return isCollect;
    }
    public void setCollect(boolean collect) {
        isCollect = collect;
    }
    @Column(name = "IS_MODULE")
    public Integer getIsModule() {
        return isModule;
    }
    public void setIsModule(Integer isModule) {
        this.isModule = isModule;
    }
    /**
     * 是否为模块
     *
     * @return
     */
    @Transient
    public boolean isModule() {
        return getIsModule() == 1;
    }
    /**
     * 是否为二开菜单。，1=是；0或者null=否
     */
    @Column(name = "IS_EXTEND")
    public Integer getIsExtend() {
        return isExtend;
    }
    public void setIsExtend(Integer isExtend) {
        this.isExtend = isExtend;
    }
}
