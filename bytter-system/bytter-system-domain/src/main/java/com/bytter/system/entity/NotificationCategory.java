package com.bytter.system.entity;

import com.bytter.core.base.entity.TreeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 消息通知分类
 * Created by zhoul on 2017/10/25.
 */
@Entity
@Table(name = "SYS_NOTIFICATION_CATEGORY")
public class NotificationCategory extends TreeEntity<NotificationCategory> implements Serializable {
    private String parentId;
    private String name;
    private String tag;
    private String menuId;
    private String menuName;
    private String listUrl;
    private String detailUrl;
    private String resourceContent;
    private Integer roleFilter;
    private Integer sort;
    private String content;
    private Integer type;
    private Integer weixinInd;
    private String notifyObjectType;
    /**
     * 重点关注
     */
    private String starTarget;
    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    @Column(name = "NAME")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "MENU_ID")
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @Column(name = "MENU_NAME")
    public String getMenuName() {
        return menuName;
    }

    @Column(name = "LIST_URL")
    public String getListUrl() {
        return listUrl;
    }
    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }
    @Column(name = "DETAIL_URL")
    public String getDetailUrl() {
        return detailUrl;
    }
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
    @Column(name = "RESOURCE_CONTENT")
    public String getResourceContent() {
        return resourceContent;
    }
    public void setResourceContent(String resourceContent) {
        this.resourceContent = resourceContent;
    }
    @Column(name = "TAG")
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    @Column(name = "ROLE_FILTER")
    public Integer getRoleFilter() {
        return roleFilter;
    }
    public void setRoleFilter(Integer roleFilter) {
        this.roleFilter = roleFilter;
    }
    @Column(name = "SORT")
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    @Column(name = "TYPE")
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    @Column(name = "STAR_TARGET")
    public String getStarTarget() {
        return starTarget;
    }
    public void setStarTarget(String starTarget) {
        this.starTarget = starTarget;
    }

     @Column(name = "WEIXIN_IND")
    public Integer getWeixinInd() {
        return weixinInd;
    }

    public void setWeixinInd(Integer weixinInd) {
        this.weixinInd = weixinInd;
    }

    @Column(name="NOTIFY_OBJECT_TYPE")
    public String getNotifyObjectType() {
        return notifyObjectType;
    }

    public void setNotifyObjectType(String notifyObjectType) {
        this.notifyObjectType = notifyObjectType;
    }
}
