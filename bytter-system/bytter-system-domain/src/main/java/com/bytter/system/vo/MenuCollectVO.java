package com.bytter.system.vo;
import java.io.Serializable;
import java.util.Date;
public class MenuCollectVO implements Serializable {
    private String id;
    private String menuId;
    private String name;
    private String parentIds;
    private String parentNames;
    private String url;
    private String userId;
    private Integer sort;
    private Date createTime;
    //顶级菜单ID
    private String topParentId;
    //顶级菜单名称
    private String topParentName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    public String getParentNames() {
        return parentNames;
    }
    public void setParentNames(String parentNames) {
        this.parentNames = parentNames;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getTopParentId() {
        return topParentId;
    }
    public void setTopParentId(String topParentId) {
        this.topParentId = topParentId;
    }
    public String getTopParentName() {
        return topParentName;
    }
    public void setTopParentName(String topParentName) {
        this.topParentName = topParentName;
    }
}
