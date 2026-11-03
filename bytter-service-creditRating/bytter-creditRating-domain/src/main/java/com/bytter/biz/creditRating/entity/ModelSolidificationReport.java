package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 报告中计算模型固化
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Entity
@Table(name = "MODEL_SOLIDIFICATION_REPORT", schema = "")
public class ModelSolidificationReport extends IdEntity implements java.io.Serializable{
    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 模型公式编号*/
    private String modelFormulaNumber;

    /** 模型公式名称*/
    private String modelFormulaName;

    /** 模型公式说明*/
    private String modelFormulaDescription;

    /** 评分等级*/
    private String ratingLevel;

    /** 计算模型名称*/
    private String modelName;

    /** 计算模型代码*/
    private String modelCode;

    /** 计算模型说明*/
    private String modelDescription;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 创建人*/
    private String creator;

    /** 创建时间*/
    private Date createDate;

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

    @Column(name ="MODEL_FORMULA_NUMBER",nullable=true,length=6)
    public String getModelFormulaNumber() {
        return modelFormulaNumber;
    }

    public void setModelFormulaNumber(String modelFormulaNumber) {
        this.modelFormulaNumber = modelFormulaNumber;
    }

    @Column(name ="MODEL_FORMULA_NAME",nullable=true,length=100)
    public String getModelFormulaName() {
        return modelFormulaName;
    }

    public void setModelFormulaName(String modelFormulaName) {
        this.modelFormulaName = modelFormulaName;
    }

    @Column(name ="MODEL_FORMULA_DESCRIPTION",nullable=true,length=300)
    public String getModelFormulaDescription() {
        return modelFormulaDescription;
    }

    public void setModelFormulaDescription(String modelFormulaDescription) {
        this.modelFormulaDescription = modelFormulaDescription;
    }

    @Column(name ="RATING_LEVEL",nullable=true,length=6)
    public String getRatingLevel() {
        return ratingLevel;
    }

    public void setRatingLevel(String ratingLevel) {
        this.ratingLevel = ratingLevel;
    }

    @Column(name ="MODEL_NAME",nullable=true,length=100)
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Column(name ="MODEL_CODE",nullable=true,length=6)
    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    @Column(name ="MODEL_DESCRIPTION",nullable=true,length=200)
    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    @Column(name ="EVALUATION_BATCH_NUMBER",nullable=true,length=13)
    public String getEvaluationBatchNumber() {
        return evaluationBatchNumber;
    }

    public void setEvaluationBatchNumber(String evaluationBatchNumber) {
        this.evaluationBatchNumber = evaluationBatchNumber;
    }

    @Column(name ="CREATOR",nullable=true,length=100)
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
