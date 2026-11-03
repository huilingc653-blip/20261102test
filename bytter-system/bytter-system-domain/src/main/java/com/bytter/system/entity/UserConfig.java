package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * Created by zhoul on 2017/5/23.
 */
@Entity
@Table(name = "SYS_USER_CONFIG")
public class UserConfig extends IdEntity implements Serializable {
    private String userId;
    private String type;
    private String content;
    public UserConfig() {
    }
    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return "UserConfig{" +
                "userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
