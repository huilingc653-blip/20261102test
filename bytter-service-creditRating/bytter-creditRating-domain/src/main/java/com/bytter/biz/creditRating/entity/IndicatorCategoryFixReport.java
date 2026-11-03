package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Title: Entity
 * @Description: 报告中指标类别固化
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Entity
@Table(name = "INDICATOR_CATEGORY_FIX_REPORT", schema = "")
public class IndicatorCategoryFixReport extends IdEntity implements java.io.Serializable {
    /** 报告中计算模型固化主键*/
    private String modelSolidificationId;

    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 评估批次号*/
    private String rvaluationBatchNumber;

    /** 类别名称*/
    private String categoryName;

    /** 类别代码*/
    private String categoryRncoding;

    /** 类别说明*/
    private String categoryDescription;

    @Column(name ="MODEL_SOLIDIFICATION_ID",nullable=true,length=32)
    public String getModelSolidificationId() {
        return modelSolidificationId;
    }

    public void setModelSolidificationId(String modelSolidificationId) {
        this.modelSolidificationId = modelSolidificationId;
    }

    @Column(name ="EVALUATION_REPORT_ID",nullable=true,length=32)
    public String getEvaluationReportId() {
        return evaluationReportId;
    }

    public void setEvaluationReportId(String evaluationReportId) {
        this.evaluationReportId = evaluationReportId;
    }

    @Column(name ="MODEL_BATCH_NUMBER",nullable=true,length=13)
    public String getModelBatchNumber() {
        return modelBatchNumber;
    }

    public void setModelBatchNumber(String modelBatchNumber) {
        this.modelBatchNumber = modelBatchNumber;
    }

    @Column(name ="RVALUATION_BATCH_NUMBER",nullable=true,length=13)
    public String getRvaluationBatchNumber() {
        return rvaluationBatchNumber;
    }

    public void setRvaluationBatchNumber(String rvaluationBatchNumber) {
        this.rvaluationBatchNumber = rvaluationBatchNumber;
    }

    @Column(name ="CATEGORY_NAME",nullable=true,length=100)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column(name ="CATEGORY_RNCODING",nullable=true,length=4)
    public String getCategoryRncoding() {
        return categoryRncoding;
    }

    public void setCategoryRncoding(String categoryRncoding) {
        this.categoryRncoding = categoryRncoding;
    }

    @Column(name ="CATEGORY_DESCRIPTION",nullable=true,length=200)
    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
