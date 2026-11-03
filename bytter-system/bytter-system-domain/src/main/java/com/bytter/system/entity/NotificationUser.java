package com.bytter.system.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by zhoul on 2017/10/31.
 */
@Entity
@Table(name = "SYS_NOTIFICATION_USER")
public class NotificationUser extends IdEntity implements Serializable {

    private String categoryId;
    private String userId;


    @Column(name = "CATEGORY_ID")
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Column(name="USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
