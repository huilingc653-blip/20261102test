package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Title: Entity
 * @Description: 报告中评分指标固化
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Entity
@Table(name = "SCORING_INDICATOR_FIX_REPORT", schema = "")
public class ScoringIndicatorFixReport extends IdEntity implements java.io.Serializable {
    /** 报告中计算模型固化主键*/
    private String modelSolidificationId;

    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 评分指标名称*/
    private String scoringIndicatorName;

    /** 评分指标代码*/
    private String scoringIndicatorCode;

    /** 评分指标说明*/
    private String scoringIndicatorDescription;

    /** 评分指标系数*/
    private BigDecimal scoringIndicatorCoefficient;

    /** 指标类别名称*/
    private String categoryName;

    /** 指标类别代码*/
    private String categoryEncoding;

    /** 报告中指标类别固化主键*/
    private String icfrId;

    /** 报告中指标类别固化主键*/
    private Integer versionNumber;

    /** 报告中指标类别固化主键*/
    private String dataSourceId;

    /** 报告中指标类别固化主键*/
    private String dataSourceCode;

    /** 报告中指标类别固化主键*/
    private String dataSourceName;

    /** 报告中指标类别固化主键*/
    private String dataSourceOrigin;

    /** 报告中指标类别固化主键*/
    private Integer level_;

    /** 报告中指标类别固化主键*/
    private String parentId;

    /** 评分指标id*/
    private String scoringIndicatorId;

    /** 评分指标细则*/
    private String scoringRubric;

    @Column(name ="VERSION_NUMBER")
    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Column(name ="DATA_SOURCE_ID")
    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    @Column(name ="DATA_SOURCE_CODE")
    public String getDataSourceCode() {
        return dataSourceCode;
    }

    public void setDataSourceCode(String dataSourceCode) {
        this.dataSourceCode = dataSourceCode;
    }

    @Column(name ="DATA_SOURCE_NAME")
    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    @Column(name ="DATA_SOURCE_ORIGIN")
    public String getDataSourceOrigin() {
        return dataSourceOrigin;
    }

    public void setDataSourceOrigin(String dataSourceOrigin) {
        this.dataSourceOrigin = dataSourceOrigin;
    }

    @Column(name ="LEVEL_")
    public Integer getLevel_() {
        return level_;
    }

    public void setLevel_(Integer level_) {
        this.level_ = level_;
    }

    @Column(name ="PARENT_ID")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name ="SCORING_INDICATOR_ID")
    public String getScoringIndicatorId() {
        return scoringIndicatorId;
    }

    public void setScoringIndicatorId(String scoringIndicatorId) {
        this.scoringIndicatorId = scoringIndicatorId;
    }

    @Column(name ="SCORING_RUBRIC")
    public String getScoringRubric() {
        return scoringRubric;
    }

    public void setScoringRubric(String scoringRubric) {
        this.scoringRubric = scoringRubric;
    }

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

    @Column(name ="EVALUATION_BATCH_NUMBER",nullable=true,length=13)
    public String getEvaluationBatchNumber() {
        return evaluationBatchNumber;
    }

    public void setEvaluationBatchNumber(String evaluationBatchNumber) {
        this.evaluationBatchNumber = evaluationBatchNumber;
    }

    @Column(name ="SCORING_INDICATOR_NAME",nullable=true,length=100)
    public String getScoringIndicatorName() {
        return scoringIndicatorName;
    }

    public void setScoringIndicatorName(String scoringIndicatorName) {
        this.scoringIndicatorName = scoringIndicatorName;
    }

    @Column(name ="SCORING_INDICATOR_CODE",nullable=true,length=4)
    public String getScoringIndicatorCode() {
        return scoringIndicatorCode;
    }

    public void setScoringIndicatorCode(String scoringIndicatorCode) {
        this.scoringIndicatorCode = scoringIndicatorCode;
    }

    @Column(name ="SCORING_INDICATOR_DESCRIPTION",nullable=true,length=200)
    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
    }

    @Column(name ="SCORING_INDICATOR_COEFFICIENT",nullable=true)
    public BigDecimal getScoringIndicatorCoefficient() {
        return scoringIndicatorCoefficient;
    }

    public void setScoringIndicatorCoefficient(BigDecimal scoringIndicatorCoefficient) {
        this.scoringIndicatorCoefficient = scoringIndicatorCoefficient;
    }

    @Column(name ="CATEGORY_NAME",nullable=true,length=100)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column(name ="CATEGORY_ENCODING",nullable=true,length=4)
    public String getCategoryEncoding() {
        return categoryEncoding;
    }

    public void setCategoryEncoding(String categoryEncoding) {
        this.categoryEncoding = categoryEncoding;
    }

    @Column(name ="I_C_F_R_ID",nullable=true,length=32)
    public String getIcfrId() {
        return icfrId;
    }

    public void setIcfrId(String icfrId) {
        this.icfrId = icfrId;
    }
}
