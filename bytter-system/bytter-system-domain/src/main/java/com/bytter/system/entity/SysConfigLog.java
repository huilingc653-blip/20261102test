package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "SYS_CONFIG_LOG")
public class SysConfigLog extends IdEntity implements Serializable {
    private String type;
    private String name;
    private String beforeValue;
    private String afterValue;
    private Date updateDate;
    private String updateBy;
    public SysConfigLog() {
    }
    public SysConfigLog(String type, String name, String beforeValue, String afterValue, Date updateDate,
                        String updateBy) {
        super();
        this.type = type;
        this.name = name;
        this.beforeValue = beforeValue;
        this.afterValue = afterValue;
        this.updateDate = updateDate;
        this.updateBy = updateBy;
    }
    @Column(name = "TYPE", nullable = true, length = 50)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "NAME", nullable = true, length = 50)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "BEFORE_VALUE", nullable = true, length = 200)
    public String getBeforeValue() {
        return beforeValue;
    }
    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }
    @Column(name = "AFTER_VALUE", nullable = true, length = 200)
    public String getAfterValue() {
        return afterValue;
    }
    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
