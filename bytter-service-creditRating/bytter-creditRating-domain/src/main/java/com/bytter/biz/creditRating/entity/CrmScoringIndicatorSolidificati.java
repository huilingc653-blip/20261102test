package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能介绍 : 评分指标固化<br>
 * <b>日期：</b>2024/8/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Entity
@Table(name = "SCORING_INDICATOR_SOLIDIFICATI", schema = "")
public class CrmScoringIndicatorSolidificati extends IdEntity implements java.io.Serializable {
    /**
     * 计算模型id
     */
    private String computatationalModelId;
    /**
     * 批次号
     */
    private String batchNumber;
    /**
     * 模型批次id
     */
    private String modelBatchId;
    /**
     * 评分指标id
     */
    private String scoringIndicatorId;
    /**
     * 版本号
     */
    private Integer versionNumber;
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
     * 指标类别名称
     */
    private String categoryName;
    /**
     * 指标类别代码
     */
    private String categoryEncoding;
    /**
     * 父指标固化id
     */
    private String parentId;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期时间
     */
    private Date createDate;
    /**
     * 指标级别
     */
    private Integer level_;
    /**
     * 评分细则
     */
    private String scoringRubric;
    /**
     * 数据源id
     */
    private String dataSourceId;
    /**
     * 数据源Code
     */
    private String dataSourceCode;
    /**
     * 数据源名称
     */
    private String dataSourceName;
    /**
     * 数据源取值来源描述
     */
    private String dataSourceOrigin;

    @Column(name = "COMPUTATIONAL_MODEL_ID")
    public String getComputatationalModelId() {
        return computatationalModelId;
    }


    public void setComputatationalModelId(String computatationalModelId) {
        this.computatationalModelId = computatationalModelId;
    }

    @Column(name = "BATCH_NUMBER")
    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    @Column(name = "MODEL_BATCH_ID")
    public String getModelBatchId() {
        return modelBatchId;
    }

    public void setModelBatchId(String modelBatchId) {
        this.modelBatchId = modelBatchId;
    }

    @Column(name = "SCORING_INDICATOR_ID")
    public String getScoringIndicatorId() {
        return scoringIndicatorId;
    }

    public void setScoringIndicatorId(String scoringIndicatorId) {
        this.scoringIndicatorId = scoringIndicatorId;
    }

    @Column(name = "VERSION_NUMBER")
    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Column(name = "SCORING_INDICATOR_NAME")
    public String getScoringIndicatorName() {
        return scoringIndicatorName;
    }

    public void setScoringIndicatorName(String scoringIndicatorName) {
        this.scoringIndicatorName = scoringIndicatorName;
    }

    @Column(name = "SCORING_INDICATOR_CODE")
    public String getScoringIndicatorCode() {
        return scoringIndicatorCode;
    }

    public void setScoringIndicatorCode(String scoringIndicatorCode) {
        this.scoringIndicatorCode = scoringIndicatorCode;
    }

    @Column(name = "SCORING_INDICATOR_DESCRIPTION")
    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
    }

    @Column(name = "SCORING_INDICATOR_COEFFICIENT")
    public BigDecimal getScoringIndicatorCoefficient() {
        return scoringIndicatorCoefficient;
    }

    public void setScoringIndicatorCoefficient(BigDecimal scoringIndicatorCoefficient) {
        this.scoringIndicatorCoefficient = scoringIndicatorCoefficient;
    }


    @Column(name = "CATEGORY_NAME")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column(name = "CATEGORY_ENCODING")
    public String getCategoryEncoding() {
        return categoryEncoding;
    }

    public void setCategoryEncoding(String categoryEncoding) {
        this.categoryEncoding = categoryEncoding;
    }

    @Column(name = "CREATOR")
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

    @Column(name = "PARENT_ID")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "LEVEL_")
    public Integer getLevel_() {
        return level_;
    }

    public void setLevel_(Integer level_) {
        this.level_ = level_;
    }


    @Column(name = "SCORINGRUBRIC")
    public String getScoringRubric() {
        return scoringRubric;
    }

    public void setScoringRubric(String scoringRubric) {
        this.scoringRubric = scoringRubric;
    }

    @Column(name = "DATA_SOURCE_ID")
    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    @Column(name = "DATA_SOURCE_CODE")
    public String getDataSourceCode() {
        return dataSourceCode;
    }

    public void setDataSourceCode(String dataSourceCode) {
        this.dataSourceCode = dataSourceCode;
    }

    @Column(name = "DATA_SOURCE_NAME")
    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    @Column(name = "DATA_SOURCE_ORIGIN")
    public String getDataSourceOrigin() {
        return dataSourceOrigin;
    }

    public void setDataSourceOrigin(String dataSourceOrigin) {
        this.dataSourceOrigin = dataSourceOrigin;
    }
}
