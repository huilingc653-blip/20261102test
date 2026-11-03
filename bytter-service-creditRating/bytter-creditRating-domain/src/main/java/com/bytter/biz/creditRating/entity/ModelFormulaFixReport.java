package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Title: Entity
 * @Description: 报告中模型公式固化
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Entity
@Table(name = "MODEL_FORMULA_FIX_REPORT", schema = "")
public class ModelFormulaFixReport extends IdEntity implements java.io.Serializable {
    /** 报告中计算模型固化主键*/
    private String modelSolidificationId;

    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 评估批次号*/
    private String evaluationNatchNumber;

    /** 模型公式名称*/
    private String modelFormulaName;

    /** 模型公式编号*/
    private String modelFormulaNumber;

    /** 模型公式说明*/
    private String modelMormulaDescription;

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

    @Column(name ="EVALUATION_NATCH_NUMBER",nullable=true,length=13)
    public String getEvaluationNatchNumber() {
        return evaluationNatchNumber;
    }

    public void setEvaluationNatchNumber(String evaluationNatchNumber) {
        this.evaluationNatchNumber = evaluationNatchNumber;
    }

    @Column(name ="MODEL_FORMULA_NAME",nullable=true,length=100)
    public String getModelFormulaName() {
        return modelFormulaName;
    }

    public void setModelFormulaName(String modelFormulaName) {
        this.modelFormulaName = modelFormulaName;
    }

    @Column(name ="MODEL_FORMULA_NUMBER",nullable=true,length=13)
    public String getModelFormulaNumber() {
        return modelFormulaNumber;
    }

    public void setModelFormulaNumber(String modelFormulaNumber) {
        this.modelFormulaNumber = modelFormulaNumber;
    }

    @Column(name ="MODEL_MORMULA_DESCRIPTION",nullable=true,length=300)
    public String getModelMormulaDescription() {
        return modelMormulaDescription;
    }

    public void setModelMormulaDescription(String modelMormulaDescription) {
        this.modelMormulaDescription = modelMormulaDescription;
    }
}
