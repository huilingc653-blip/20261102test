package com.bytter.system.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
/**
 * Created by zhoul on 2017/11/20.
 */
public class NotificationResponseVO {
    private String id;
    private String name;
    private String categoryId;
    private String categoryName;
    private String listUrl;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    private String topParentCategoryName;
    private String menuId;
    private String businessId;
    private String type;
    private String tag;
    /**
     * 重点关注数
     */
    private String startTargetCount;
    /**
     * 到期提醒数
     */
    private String expireCount;
    /**
     * 到期日期
     */
    private String expireDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getListUrl() {
        return listUrl;
    }
    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getTopParentCategoryName() {
        return topParentCategoryName;
    }
    public void setTopParentCategoryName(String topParentCategoryName) {
        this.topParentCategoryName = topParentCategoryName;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getStartTargetCount() {
        return startTargetCount;
    }
    public void setStartTargetCount(String startTargetCount) {
        this.startTargetCount = startTargetCount;
    }
    public String getExpireCount() {
        return expireCount;
    }
    public void setExpireCount(String expireCount) {
        this.expireCount = expireCount;
    }
    public String getExpireDate() {
        return expireDate;
    }
    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
