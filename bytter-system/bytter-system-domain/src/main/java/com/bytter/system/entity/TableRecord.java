package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * 前台表格数据存储尸体类
 * Created by zhoul on 2017/11/9.
 */
@Entity
@Table(name = "SYS_TABLE_RECORD")
public class TableRecord extends IdEntity implements Serializable {
    private String userId;
    private String tableKey;
    private String content;
    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Column(name = "TABLE_KEY")
    public String getTableKey() {
        return tableKey;
    }
    public void setTableKey(String tableKey) {
        this.tableKey = tableKey;
    }
    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
