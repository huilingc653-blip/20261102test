package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 功能介绍 : 指标类别固化<br>
 * <b>日期：</b>2024/8/8<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Entity
@Table(name = "INDICATOR_CATEGORY_SOLIDIFICA", schema = "")
public class CrmIndicatorCategorySolidifica extends IdEntity implements java.io.Serializable{
    /** 计算模型id*/
    private String computationalModelId;
    /** 批次号*/
    private String batchNumber;
    /** 模型批次id*/
    private String modelBatchId;
    /** 类别名称*/
    private String categoryName;
    /** 类别代码*/
    private String categoryEncoding;
    /** 类别说明*/
    private String categoryDescription;
    /** 创建人*/
    private String creator;
    /** 创建日期时间*/
    private Date createDate;

    @Column(name ="COMPUTATIONAL_MODEL_ID")
    public String getComputationalModelId() {
        return computationalModelId;
    }

    public void setComputationalModelId(String computationalModelId) {
        this.computationalModelId = computationalModelId;
    }

    @Column(name ="BATCH_NUMBER")
    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    @Column(name ="MODEL_BATCH_ID")
    public String getModelBatchId() {
        return modelBatchId;
    }

    public void setModelBatchId(String modelBatchId) {
        this.modelBatchId = modelBatchId;
    }

    @Column(name ="CATEGORY_NAME")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column(name ="CATEGORY_ENCODING")
    public String getCategoryEncoding() {
        return categoryEncoding;
    }

    public void setCategoryEncoding(String categoryEncoding) {
        this.categoryEncoding = categoryEncoding;
    }

    @Column(name ="CATEGORY_DESCRIPTION")
    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    @Column(name ="CREATOR")
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
