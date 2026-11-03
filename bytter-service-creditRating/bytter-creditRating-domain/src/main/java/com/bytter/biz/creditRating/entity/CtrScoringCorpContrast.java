package com.bytter.biz.creditRating.entity;


import com.bytter.core.base.entity.IdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 评分机构评分指标对照表
 */
@Entity
@Data
@Table(name = "CRT_SCORING_CORP_CONTRAST", schema = "")
public class CtrScoringCorpContrast extends IdEntity implements Serializable {

    /**
     * 计算模型id
     */

    private String taskNo;

    /**
     * 计算模型id
     */
    private String computationalModelId;

    /**
     * 批次号
     */
    private String batchNumber;

    /**
     * 组织id
     */
    private String corpId;

    /**
     * 评分指标固化id
     */
    private String scoringIndicatorSolidificati;

    /**
     * 父指标固化id
     */
    private String parentId;

    /**
     * 评分指标名称
     */
    private String scoringIndicatorName;

    /**
     * 评分指标代码
     */
    private String scoringIndicatorCode;

    /**
     * 评分指标说明
     */
    private String scoringIndicatorDescription;

    /**
     * 评分指标系数
     */
    private BigDecimal scoringIndicatorCoefficient;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 指标级别
     */
    private Integer level_;

    /**
     * 是否自动评分
     */
    private BigDecimal isAutoScoring;

    /**
     * 评分方法
     */
    private BigDecimal scoringMethod;

    /**
     * 评分细则标题
     */
    private String scoringRubric;

    /**
     * 是否额外加减分项
     */
    private String isAddOrSubGradeName;

    /**
     * 指标性质
     */
    private BigDecimal indexProperty;

    @Column(name = "TASK_NO", length = 32)
    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    @Column(name = "COMPUTATIONAL_MODEL_ID", length = 32)
    public String getComputationalModelId() {
        return computationalModelId;
    }

    public void setComputationalModelId(String computationalModelId) {
        this.computationalModelId = computationalModelId;
    }

    @Column(name = "BATCH_NUMBER", length = 13)
    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    @Column(name = "CORP_ID", length = 32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "SCORING_INDICATOR_SOLIDIFICATI", length = 32)
    public String getScoringIndicatorSolidificati() {
        return scoringIndicatorSolidificati;
    }

    public void setScoringIndicatorSolidificati(String scoringIndicatorSolidificati) {
        this.scoringIndicatorSolidificati = scoringIndicatorSolidificati;
    }

    @Column(name = "PARENT_ID", length = 32)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "SCORING_INDICATOR_NAME", length = 100)
    public String getScoringIndicatorName() {
        return scoringIndicatorName;
    }

    public void setScoringIndicatorName(String scoringIndicatorName) {
        this.scoringIndicatorName = scoringIndicatorName;
    }

    @Column(name = "SCORING_INDICATOR_CODE", length = 100)
    public String getScoringIndicatorCode() {
        return scoringIndicatorCode;
    }

    public void setScoringIndicatorCode(String scoringIndicatorCode) {
        this.scoringIndicatorCode = scoringIndicatorCode;
    }

    @Column(name = "SCORING_INDICATOR_DESCRIPTION", length = 200)
    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
    }

    @Column(name = "SCORING_INDICATOR_COEFFICIENT", length = 8)
    public BigDecimal getScoringIndicatorCoefficient() {
        return scoringIndicatorCoefficient;
    }

    public void setScoringIndicatorCoefficient(BigDecimal scoringIndicatorCoefficient) {
        this.scoringIndicatorCoefficient = scoringIndicatorCoefficient;
    }

    @Column(name = "CREATOR", length = 100)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "level_")
    public Integer getLevel_() {
        return level_;
    }

    public void setLevel_(Integer level_) {
        this.level_ = level_;
    }

    @Column(name = "IS_AUTO_SCORING")
    public BigDecimal getIsAutoScoring() {
        return isAutoScoring;
    }

    public void setIsAutoScoring(BigDecimal isAutoScoring) {
        this.isAutoScoring = isAutoScoring;
    }

    @Column(name = "SCORING_METHOD")
    public BigDecimal getScoringMethod() {
        return scoringMethod;
    }

    public void setScoringMethod(BigDecimal scoringMethod) {
        this.scoringMethod = scoringMethod;
    }

    @Column(name = "SCORING_RUBRIC")
    public String getScoringRubric() {
        return scoringRubric;
    }

    public void setScoringRubric(String scoringrubric) {
        this.scoringRubric = scoringrubric;
    }

    @Column(name = "IS_ADD_OR_SUB_GRADE_NAME")
    public String getIsAddOrSubGradeName() {
        return isAddOrSubGradeName;
    }


    public void setIsAddOrSubGradeName(String isAddOrSubGradeName) {
        this.isAddOrSubGradeName = isAddOrSubGradeName;
    }

    @Column(name = "INDEX_PROPERTY")
    public BigDecimal getIndexProperty() {
        return indexProperty;
    }

    public void setIndexProperty(BigDecimal indexProperty) {
        this.indexProperty = indexProperty;
    }
}
