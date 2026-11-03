package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Title: Entity
 * @Description: 报告中评分等级级别固化
 * @author chenqin
 * @date 2024-08-09
 * @version 1.0
 *
 */

@Entity
@Table(name = "SCORING_GRADE_LE_FIX_REPORT", schema = "")
public class ScoringGradeLeFixReport extends IdEntity implements java.io.Serializable {

    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 报告中评分等级固化主键*/
    private String scoringGradeFixReportId;

    /** 计算模型固化主键*/
    private String modelSolidificationId;

    /** 评分级别*/
    private String scoringLevel;

    /** 评分级别上限分*/
    private BigDecimal upperScoreLimitLevel;

    /** 评分级别下线分*/
    private BigDecimal lowerScoreLimitLevel;

    /** 评分级别说明*/
    private String explanationOfScoringLevel;

    /**
     * 上限等级符号
     */
    private String upScoringSymbol;
    /**
     * 下限等级符号
     */
    private String downScoringSymbol;

    @Column(name ="UP_SCORING_SYMBOL")
    public String getUpScoringSymbol() {
        return upScoringSymbol;
    }

    public void setUpScoringSymbol(String upScoringSymbol) {
        this.upScoringSymbol = upScoringSymbol;
    }

    @Column(name ="DOWN_SCORING_SYMBOL")
    public String getDownScoringSymbol() {
        return downScoringSymbol;
    }

    public void setDownScoringSymbol(String downScoringSymbol) {
        this.downScoringSymbol = downScoringSymbol;
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

    @Column(name ="SCORING_GRADE_FIX_REPORT_ID",nullable=true,length=32)
    public String getScoringGradeFixReportId() {
        return scoringGradeFixReportId;
    }

    public void setScoringGradeFixReportId(String scoringGradeFixReportId) {
        this.scoringGradeFixReportId = scoringGradeFixReportId;
    }

    @Column(name ="MODEL_SOLIDIFICATION_ID",nullable=true,length=32)
    public String getModelSolidificationId() {
        return modelSolidificationId;
    }

    public void setModelSolidificationId(String modelSolidificationId) {
        this.modelSolidificationId = modelSolidificationId;
    }

    @Column(name ="SCORING_LEVEL",nullable=true,length=32)
    public String getScoringLevel() {
        return scoringLevel;
    }

    public void setScoringLevel(String scoringLevel) {
        this.scoringLevel = scoringLevel;
    }

    @Column(name ="UPPER_SCORE_LIMIT_LEVEL",nullable=true)
    public BigDecimal getUpperScoreLimitLevel() {
        return upperScoreLimitLevel;
    }

    public void setUpperScoreLimitLevel(BigDecimal upperScoreLimitLevel) {
        this.upperScoreLimitLevel = upperScoreLimitLevel;
    }

    @Column(name ="LOWER_SCORE_LIMIT_LEVEL",nullable=true)
    public BigDecimal getLowerScoreLimitLevel() {
        return lowerScoreLimitLevel;
    }

    public void setLowerScoreLimitLevel(BigDecimal lowerScoreLimitLevel) {
        this.lowerScoreLimitLevel = lowerScoreLimitLevel;
    }

    @Column(name ="EXPLANATION_OF_SCORING_LEVEL",nullable=true,length=300)
    public String getExplanationOfScoringLevel() {
        return explanationOfScoringLevel;
    }

    public void setExplanationOfScoringLevel(String explanationOfScoringLevel) {
        this.explanationOfScoringLevel = explanationOfScoringLevel;
    }
}
