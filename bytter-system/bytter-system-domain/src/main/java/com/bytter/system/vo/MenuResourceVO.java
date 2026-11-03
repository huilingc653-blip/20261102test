package com.bytter.system.vo;
/**
 * Created by zhoul on 2017/6/15.
 */
public class MenuResourceVO {
    private String id;
    private String name;
    private Long childrenSize = 0L;
    private Long resourceSize = 0L;
    private Integer isResource = 1;
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
    public Long getChildrenSize() {
        return childrenSize;
    }
    public void setChildrenSize(Long childrenSize) {
        this.childrenSize = childrenSize;
    }
    public Long getResourceSize() {
        return resourceSize;
    }
    public void setResourceSize(Long resourceSize) {
        this.resourceSize = resourceSize;
    }
    public Integer getIsResource() {
        return isResource;
    }
    public void setIsResource(Integer isResource) {
        this.isResource = isResource;
    }
}
