package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
/**
 * Created by zhoul on 2017/10/23.
 */
@Entity
@Table(name = "SYS_MENU_COLLECT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MenuCollect extends IdEntity implements Serializable {
    private String menuId;
    private String name;
    private String parentIds;
    private String parentNames;
    private String url;
    private String userId;
    private Integer sort;
    private Date createTime;
    @Column(name = "MENU_ID")
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    @Column(name = "PARENT_IDS")
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    @Column(name = "URL")
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Column(name = "SORT")
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Transient
    public String getParentNames() {
        return parentNames;
    }
    public void setParentNames(String parentNames) {
        this.parentNames = parentNames;
    }
    @Transient
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
