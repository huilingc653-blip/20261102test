package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * 首页组件实体类
 * Created by zhoul on 2017/10/17.
 */
@Entity
@Table(name = "SYS_HOME_WIDGET")
public class HomeWidget extends IdEntity implements Serializable {
    /**
     * 组件类型
     */
    private String type;
    /**
     * 组件名称
     */
    private String name;
    /**
     * 转向内容
     */
    private String content;
    /**
     * 组件分类
     */
    private String categoryId;
    /**
     * 高度
     */
    private Integer height;
    /**
     * 最小宽度
     */
    private Integer minWidth;
    /**
     * 最大宽度
     */
    private Integer maxWidth;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 资源值
     */
    private String resourceContent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "NAME")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    @Column(name = "CATEGORY_ID")
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    @Column(name = "HEIGHT")
    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }
    @Column(name = "MIN_WIDTH")
    public Integer getMinWidth() {
        return minWidth;
    }
    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }
    @Column(name = "MAX_WIDTH")
    public Integer getMaxWidth() {
        return maxWidth;
    }
    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }
    @Column(name = "SORT", columnDefinition = "INT default 500")
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "RESOURCE_CONTENT")
    public String getResourceContent() {
        return resourceContent;
    }
    public void setResourceContent(String resourceContent) {
        this.resourceContent = resourceContent;
    }
    @Override
    public String toString() {
        return "HomeWidget{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", height=" + height +
                ", minWidth=" + minWidth +
                ", maxWidth=" + maxWidth +
                ", sort=" + sort +
                ", createTime=" + createTime +
                '}';
    }
}
