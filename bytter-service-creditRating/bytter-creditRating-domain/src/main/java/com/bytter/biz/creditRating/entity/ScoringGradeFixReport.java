package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Title: Entity
 * @Description: 报告中评分等级固化
 * @author chenqin
 * @date 2024-08-09
 * @version 1.0
 *
 */

@Entity
@Table(name = "SCORING_GRADE_FIX_REPORT", schema = "")
public class ScoringGradeFixReport extends IdEntity implements java.io.Serializable {
    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 报告中计算模型固化主键*/
    private String modelSolidificationId;

    /** 计分制*/
    private Integer scoringSystem;

    /**
     * 评分等级名称
     */
    private String scoringGradeName;

    /**
     * 评分等级编码
     */
    private String scoringGradeCode;

    /**
     * 机构类别
     */
    private String institutionType;

    /** 评分等级说明*/
    private String scoringGradeDescription;

    @Column(name ="SCORING_GRADE_NAME")
    public String getScoringGradeName() {
        return scoringGradeName;
    }

    public void setScoringGradeName(String scoringGradeName) {
        this.scoringGradeName = scoringGradeName;
    }

    @Column(name ="SCORING_GRADE_CODE")
    public String getScoringGradeCode() {
        return scoringGradeCode;
    }

    public void setScoringGradeCode(String scoringGradeCode) {
        this.scoringGradeCode = scoringGradeCode;
    }

    @Column(name ="INSTITUTION_TYPE")
    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
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

    @Column(name ="EVALUATION_BATCH_NUMBER",nullable=true,length=13)
    public String getEvaluationBatchNumber() {
        return evaluationBatchNumber;
    }

    public void setEvaluationBatchNumber(String evaluationBatchNumber) {
        this.evaluationBatchNumber = evaluationBatchNumber;
    }

    @Column(name ="MODEL_SOLIDIFICATION_ID",nullable=true,length=32)
    public String getModelSolidificationId() {
        return modelSolidificationId;
    }

    public void setModelSolidificationId(String modelSolidificationId) {
        this.modelSolidificationId = modelSolidificationId;
    }

    @Column(name ="SCORING_SYSTEM",nullable=true)
    public Integer getScoringSystem() {
        return scoringSystem;
    }

    public void setScoringSystem(Integer scoringSystem) {
        this.scoringSystem = scoringSystem;
    }

    @Column(name ="SCORING_GRADE_DESCRIPTION",nullable=true,length=200)
    public String getScoringGradeDescription() {
        return scoringGradeDescription;
    }

    public void setScoringGradeDescription(String scoringGradeDescription) {
        this.scoringGradeDescription = scoringGradeDescription;
    }
}
