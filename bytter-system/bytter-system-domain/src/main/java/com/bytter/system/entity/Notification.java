package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * Created by zhoul on 2017/10/24.
 */
@Entity
@Table(name = "SYS_NOTIFICATION")
public class Notification extends IdEntity implements Serializable {
    private String categoryId;
    private String userId;
    private String corpId;
    private String name;
    private String messageId;
    private Integer readFlag;
    /**
     * type:1,通知；2，待处理。
     */
    private Integer type;
    private String businessId;
    private Date createDate;
    @Column(name = "CATEGORY_ID")
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "NAME")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "READ_FLAG")
    public Integer getReadFlag() {
        return readFlag;
    }
    public void setReadFlag(Integer readFlag) {
        this.readFlag = readFlag;
    }
    @Column(name = "TYPE")
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    @Column(name = "BUSINESS_ID")
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "MSG_ID")
    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
