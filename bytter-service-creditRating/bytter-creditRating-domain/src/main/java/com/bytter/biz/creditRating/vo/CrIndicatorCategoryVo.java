package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.BtCompositeSearch;

import java.io.Serializable;
import java.util.Date;

/**
 * 指标类别(已弃用)
 */
public class CrIndicatorCategoryVo implements Serializable {
    /**
     * id
     */
    @BtCompositeSearch(fieldName = "ic.id")
    private String id;
    /**
     * 指标大类代码
     */
    @BtCompositeSearch(fieldName = "ic.CATEGORY_ENCODING")
    private String categoryEnCoding;
    /**
     * 指标大类名称
     */
    @BtCompositeSearch(fieldName = "ic.CATEGORY_NAME")
    private String categoryName;
    /**
     * 指标大类作用说明
     */
    @BtCompositeSearch(fieldName = "ic.CATEGORY_DESCRIPTION")
    private String categoryDescription;
    /**
     * 删除标志
     */
    @BtCompositeSearch(fieldName = "ic.DELETE_FLAG")
    private Integer deleteFlag;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "ic.STATUS")
    private Integer status;
    /**
     * 创建者
     */
    @BtCompositeSearch(fieldName = "ic.CREATOR")
    private String creator;
    /**
     * 创建时间
     */
    @BtCompositeSearch(fieldName = "ic.CREATE_DATE")
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryEnCoding() {
        return categoryEnCoding;
    }

    public void setCategoryEnCoding(String categoryEnCoding) {
        this.categoryEnCoding = categoryEnCoding;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
