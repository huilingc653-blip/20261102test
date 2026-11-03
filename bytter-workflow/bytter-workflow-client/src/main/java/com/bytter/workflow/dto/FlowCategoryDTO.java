package com.bytter.workflow.dto;
import com.bytter.core.annotation.Menu;
import java.io.Serializable;
/**
 * Created by chenlw on 2020-03-04
 */
public class FlowCategoryDTO implements Serializable {
    private String id;
    private String parentId;
    private String name;
    private String type;
    private String key;
    private String url;
    private Integer sort;
    private String mobileUrl;
    private String notificationId;
    /**
     * 必须挂审批、0不是（默认），1是
     */
    private String mustApprove;
    /**
     * 是否支持批量撤回，1=支持，0=不支持
     * 业务：决定了业务流程列表页是否有撤回功能
     */
    private Integer batchRevokable;
    /**
     * 菜单ID
     */
    @Menu(inject = "menuId")
    private String menuId;
    /**
     * 是否是移动审批：值为0时表示不是；值为1或空置表示是（默认）
     */
    private Integer mobileApprovalFlag;
    /**
     * 资源值
     */
    private String resourceContent;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getMobileUrl() {
        return mobileUrl;
    }
    public void setMobileUrl(String mobileUrl) {
        this.mobileUrl = mobileUrl;
    }
    public String getNotificationId() {
        return notificationId;
    }
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
    public String getMustApprove() {
        return mustApprove;
    }
    public void setMustApprove(String mustApprove) {
        this.mustApprove = mustApprove;
    }
    public Integer getBatchRevokable() {
        return batchRevokable;
    }
    public void setBatchRevokable(Integer batchRevokable) {
        this.batchRevokable = batchRevokable;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public Integer getMobileApprovalFlag() {
        return mobileApprovalFlag;
    }
    public void setMobileApprovalFlag(Integer mobileApprovalFlag) {
        this.mobileApprovalFlag = mobileApprovalFlag;
    }
    public String getResourceContent() {
        return resourceContent;
    }
    public void setResourceContent(String resourceContent) {
        this.resourceContent = resourceContent;
    }
}
