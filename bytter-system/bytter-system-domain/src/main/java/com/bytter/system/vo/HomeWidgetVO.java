package com.bytter.system.vo;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
public class HomeWidgetVO {
    private String id;
    @Dict(dictGroup = "sys.home.widget.type", inject = "typeLang")
    private String type;
    private String typeLang;
    private String name;
    private String content;
    @Dict(dictGroup = "sys.home.widget.category", inject = "categoryLang")
    private String categoryId;
    private String categoryLang;
    private Integer height;
    private Integer minWidth;
    private Integer maxWidth;
    private Integer sort;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String resourceContent;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTypeLang() {
        return typeLang;
    }
    public void setTypeLang(String typeLang) {
        this.typeLang = typeLang;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryLang() {
        return categoryLang;
    }
    public void setCategoryLang(String categoryLang) {
        this.categoryLang = categoryLang;
    }
    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }
    public Integer getMinWidth() {
        return minWidth;
    }
    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }
    public Integer getMaxWidth() {
        return maxWidth;
    }
    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getResourceContent() {
        return resourceContent;
    }
    public void setResourceContent(String resourceContent) {
        this.resourceContent = resourceContent;
    }
    @Override
    public String toString() {
        return "HomeWidgetVO{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", typeLang='" + typeLang + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", categoryLang='" + categoryLang + '\'' +
                ", height=" + height +
                ", minWidth=" + minWidth +
                ", maxWidth=" + maxWidth +
                ", sort=" + sort +
                ", createTime=" + createTime +
                '}';
    }
}
