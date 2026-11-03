package com.bytter.system.vo;
import com.bytter.system.entity.Menu;
import com.bytter.core.annotation.Lang;
import java.io.Serializable;
import java.util.Date;
/**
 * Created by zhoul on 2017/5/17.
 */
public class MenuVO implements Serializable {
    private String id;
    private String parentId;
    private String parentIds;
    private String name;
    private String nameLang;
    private String description;
    private Integer sort;
    private Integer status;
    private String code;
    @Lang(entity = Menu.class, inject = "url", attrName = "URL")
    private String url;
    private Date createTime;
    private String createBy;
    private String updateBy;
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
    public String getParentIds() {
        return parentIds;
    }
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Integer getIsExtend() {
        return isExtend;
    }
    public void setIsExtend(Integer isExtend) {
        this.isExtend = isExtend;
    }
}
