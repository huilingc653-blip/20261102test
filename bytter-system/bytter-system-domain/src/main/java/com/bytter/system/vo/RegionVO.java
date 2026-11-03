package com.bytter.system.vo;
import java.io.Serializable;
public class RegionVO implements Serializable {
    private String id;
    private String name;
    private Integer level;
    private String parentId;
    private Integer bankProv;
    private Integer bankCity;
    private String provName;
    private Boolean isParent;
    public RegionVO() {
        super();
        // TODO Auto-generated constructor stub
    }
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
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public Integer getBankProv() {
        return bankProv;
    }
    public void setBankProv(Integer bankProv) {
        this.bankProv = bankProv;
    }
    public Integer getBankCity() {
        return bankCity;
    }
    public void setBankCity(Integer bankCity) {
        this.bankCity = bankCity;
    }
    public String getProvName() {
        return provName;
    }
    public void setProvName(String provName) {
        this.provName = provName;
    }
    public Boolean getIsParent() {
        return isParent;
    }
    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }
}
