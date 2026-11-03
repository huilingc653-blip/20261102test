package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.Date;
/**
 * <br>
 * <b>功能：</b>  APP消息推送DTO <br>
 * <b>作者：</b>chenlw<br>
 * <b>日期：</b> 2020-12-21 <br>
 */
public class AppNotificationPushDTO implements Serializable {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 消息ID
     */
    private String notificationId;
    /**
     * 消息分类名称
     */
    private String categoryName;
    /**
     * 消息内容
     */
    private String name;
    /**
     * 消息创建时间
     */
    private Date createDate;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getNotificationId() {
        return notificationId;
    }
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
