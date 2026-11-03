package com.bytter.biz.creditRating.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能介绍 : 指标类别固化<br>
 * <b>日期：</b>2024/8/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class CrmIndicatorCategorySolidificaVO implements Serializable {
    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComputationalModelId() {
        return computationalModelId;
    }

    public void setComputationalModelId(String computationalModelId) {
        this.computationalModelId = computationalModelId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getModelBatchId() {
        return modelBatchId;
    }

    public void setModelBatchId(String modelBatchId) {
        this.modelBatchId = modelBatchId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryEncoding() {
        return categoryEncoding;
    }

    public void setCategoryEncoding(String categoryEncoding) {
        this.categoryEncoding = categoryEncoding;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
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
