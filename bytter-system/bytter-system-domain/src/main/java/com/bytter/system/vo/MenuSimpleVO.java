package com.bytter.system.vo;
import com.bytter.core.annotation.Menu;
import com.bytter.core.base.persistence.tree.interfaces.TreeList;
/**
 * Created by zhoul on 2017/6/8.
 */
public class MenuSimpleVO implements TreeList {
    @Menu(inject = "name")
    private String id;
    private String parentId;
    private String name;
    private String nameLang;
    private Long childrenSize;
    private Long resourceSize;
    private Integer isModule;
    /**
     * 是否为二开菜单。，1=是；0或者null=否
     */
    private Integer isExtend;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNameLang() {
        return nameLang;
    }
    public void setNameLang(String nameLang) {
        this.nameLang = nameLang;
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
    public Integer getIsModule() {
        return isModule;
    }
    public void setIsModule(Integer isModule) {
        this.isModule = isModule;
    }
    public Integer getIsExtend() {
        return isExtend;
    }
    public void setIsExtend(Integer isExtend) {
        this.isExtend = isExtend;
    }
}
