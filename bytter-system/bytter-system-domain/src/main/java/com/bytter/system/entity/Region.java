package com.bytter.system.entity;
import com.bytter.core.base.entity.TreeEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * Created by zhoul on 2017/4/17.
 */
@Entity
@Table(name = "SYS_REGION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Region extends TreeEntity<Region> implements Serializable {
    private String name;
    private Integer level;
    private String parentId;
    private Integer bankProv;
    private Integer bankCity;
    private String provName;
    public Region() {
    }
    public Region(String id, String name) {
        setId(id);
        this.name = name;
    }
    public Region(String id, String name, Integer level, String parentId) {
        setId(id);
        this.name = name;
        this.level = level;
        this.parentId = parentId;
    }
    @Column(name = "NAME")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "RE_LEVEL")
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    @Column(name = "BANK_PROV")
    public Integer getBankProv() {
        return bankProv;
    }
    public void setBankProv(Integer bankProv) {
        this.bankProv = bankProv;
    }
    @Column(name = "BANK_INPUT_CITY")
    public Integer getBankCity() {
        return bankCity;
    }
    public void setBankCity(Integer bankCity) {
        this.bankCity = bankCity;
    }
    @Column(name = "PROV_INPUT_NAME")
    public String getProvName() {
        return provName;
    }
    public void setProvName(String provName) {
        this.provName = provName;
    }
}
