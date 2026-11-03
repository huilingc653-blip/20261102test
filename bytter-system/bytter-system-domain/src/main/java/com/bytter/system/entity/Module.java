package com.bytter.system.entity;
import com.bytter.core.base.entity.TreeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * Created by zhoul on 2017/12/18.
 */
@Entity
@Table(name = "SYS_MODULE")
public class Module extends TreeEntity {
    private String parentId;
    private String name;
    private Integer sort;
    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    @Column(name = "NAME")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "SORT")
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
