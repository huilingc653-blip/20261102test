package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 指标类别(已弃用)
 */
@Entity
@Table(name = "CR_INDICATOR_CATEGORY", schema = "")
@SuppressWarnings("serial")
public class CrIndicatorCategory extends IdEntity implements java.io.Serializable{

    /**
     * 指标大类代码
     */
    private String categoryEnCoding;
    /**
     * 指标大类名称
     */
    private String categoryName;
    /**
     * 指标大类作用说明
     */
    private String categoryDescription;
    /**
     * 删除标志
     */
    private Integer deleteFlag;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createDate;

    @Column(name ="CATEGORY_ENCODING",nullable=false,length=4)
    public String getCategoryEnCoding() {
        return categoryEnCoding;
    }

    public void setCategoryEnCoding(String categoryEnCoding) {
        this.categoryEnCoding = categoryEnCoding;
    }
    @Column(name ="CATEGORY_NAME",nullable=false,length=100)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    @Column(name ="CATEGORY_DESCRIPTION",nullable=false,length=200)
    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
    @Column(name ="DELETE_FLAG",nullable=false,length=4)
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Column(name ="STATUS",nullable=false,length=4)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="CREATOR",nullable=false,length=32)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
