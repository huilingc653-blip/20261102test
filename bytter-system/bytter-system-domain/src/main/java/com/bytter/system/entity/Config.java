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
@Table(name = "SYS_CONFIG")
public class Config extends IdEntity implements Serializable {
    private String type;
    private String name;
    private String value;
    private String tenantId;
    public Config() {
    }
    public Config(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
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
    @Column(name = "VALUE", nullable = true, length = 200)
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    @Column(name = "TENANT_ID", nullable = true, length = 50)
    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @Override
    public String toString() {
        return "Config{" +
                "id='" + super.getId() + '\'' +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
