package com.bytter.biz.creditRating.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 功能介绍 : 评分指标固化<br>
 * <b>日期：</b>2024/8/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class CrmScoringIndicatorSolidificatiVO implements Serializable {
    private String id;
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
     * 创建人
     */
    private String creator;
    /**
     * 创建日期时间
     */
    private Date createDate;
    /**
     * 父指标固化id
     */
    private String parentId;
    /**
     * 固化表父id
     */
    private String sisParentId;
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
     * 数据源code
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

    /**
     * 指标方向
     */
    private BigDecimal indicatorDirectionValue;
    /**
     * 最大可能值
     */
    private BigDecimal maxPossibleValue;
    /**
     * 最小可能值
     */
    private BigDecimal minPossibleValue;

    /**
     * 是否自动评分
     */
    private BigDecimal isAutoScoring;
    /**
     * 评分方法
     */
    private BigDecimal scoringMethod;
    private String scoringMethodName;
    /**
     * 是否额外加/减分
     */
    private BigDecimal isAddOrSubGrade;
    private String isAddOrSubGradeName;

    /**
     * 单位id
     */
    private String corpIds;

    /**
     * 指标性质
     */
    private BigDecimal indexProperty;

    /**
     * 自动评分区间
     */
    private List<CrmSectionDataVO> sectionDataVOList;
    /**
     * 子指标
     */
    private List<CrmScoringIndicatorSolidificatiVO> childrenScoringList;

    /**
     * 此指标所占行数
     */
    private int rowSpan;

    /**
     * 此指标所占列数
     */
    private int colSpan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComputatationalModelId() {
        return computatationalModelId;
    }

    public void setComputatationalModelId(String computatationalModelId) {
        this.computatationalModelId = computatationalModelId;
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

    public String getScoringIndicatorId() {
        return scoringIndicatorId;
    }

    public void setScoringIndicatorId(String scoringIndicatorId) {
        this.scoringIndicatorId = scoringIndicatorId;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Integer getLevel_() {
        return level_;
    }

    public void setLevel_(Integer level_) {
        this.level_ = level_;
    }

    public Integer getLevel() {
        return level_;
    }

    public void setLevel(Integer level) {
        this.level_ = level;
    }

    public String getScoringIndicatorName() {
        return scoringIndicatorName;
    }

    public void setScoringIndicatorName(String scoringIndicatorName) {
        this.scoringIndicatorName = scoringIndicatorName;
    }

    public String getScoringIndicatorCode() {
        return scoringIndicatorCode;
    }

    public void setScoringIndicatorCode(String scoringIndicatorCode) {
        this.scoringIndicatorCode = scoringIndicatorCode;
    }

    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
    }

    public BigDecimal getScoringIndicatorCoefficient() {
        return scoringIndicatorCoefficient;
    }

    public void setScoringIndicatorCoefficient(BigDecimal scoringIndicatorCoefficient) {
        this.scoringIndicatorCoefficient = scoringIndicatorCoefficient;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSisParentId() {
        return sisParentId;
    }

    public void setSisParentId(String sisParentId) {
        this.sisParentId = sisParentId;
    }

    public String getScoringRubric() {
        return scoringRubric;
    }

    public void setScoringRubric(String scoringRubric) {
        this.scoringRubric = scoringRubric;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataSourceCode() {
        return dataSourceCode;
    }

    public void setDataSourceCode(String dataSourceCode) {
        this.dataSourceCode = dataSourceCode;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getDataSourceOrigin() {
        return dataSourceOrigin;
    }

    public void setDataSourceOrigin(String dataSourceOrigin) {
        this.dataSourceOrigin = dataSourceOrigin;
    }

    public BigDecimal getIndicatorDirectionValue() {
        return indicatorDirectionValue;
    }

    public void setIndicatorDirectionValue(BigDecimal indicatorDirectionValue) {
        this.indicatorDirectionValue = indicatorDirectionValue;
    }

    public BigDecimal getMaxPossibleValue() {
        return maxPossibleValue;
    }

    public void setMaxPossibleValue(BigDecimal maxPossibleValue) {
        this.maxPossibleValue = maxPossibleValue;
    }

    public BigDecimal getMinPossibleValue() {
        return minPossibleValue;
    }

    public void setMinPossibleValue(BigDecimal minPossibleValue) {
        this.minPossibleValue = minPossibleValue;
    }

    public BigDecimal getIsAutoScoring() {
        return isAutoScoring;
    }

    public void setIsAutoScoring(BigDecimal isAutoScoring) {
        this.isAutoScoring = isAutoScoring;
    }

    public BigDecimal getScoringMethod() {
        return scoringMethod;
    }

    public void setScoringMethod(BigDecimal scoringMethod) {
        this.scoringMethod = scoringMethod;
    }

    public BigDecimal getIsAddOrSubGrade() {
        return isAddOrSubGrade;
    }

    public void setIsAddOrSubGrade(BigDecimal isAddOrSubGrade) {
        this.isAddOrSubGrade = isAddOrSubGrade;
    }

    public String getIsAddOrSubGradeName() {
        return isAddOrSubGradeName;
    }

    public void setIsAddOrSubGradeName(String isAddOrSubGradeName) {
        this.isAddOrSubGradeName = isAddOrSubGradeName;
    }

    public BigDecimal getIndexProperty() {
        return indexProperty;
    }

    public void setIndexProperty(BigDecimal indexProperty) {
        this.indexProperty = indexProperty;
    }

    public String getScoringMethodName() {
        return scoringMethodName;
    }

    public void setScoringMethodName(String scoringMethodName) {
        this.scoringMethodName = scoringMethodName;
    }

    public List<CrmSectionDataVO> getSectionDataVOList() {
        return sectionDataVOList;
    }

    public void setSectionDataVOList(List<CrmSectionDataVO> sectionDataVOList) {
        this.sectionDataVOList = sectionDataVOList;
    }

    public List<CrmScoringIndicatorSolidificatiVO> getChildrenScoringList() {
        return childrenScoringList;
    }

    public void setChildrenScoringList(List<CrmScoringIndicatorSolidificatiVO> childrenScoringList) {
        this.childrenScoringList = childrenScoringList;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public String getCorpIds() {
        return corpIds;
    }

    public void setCorpIds(String corpIds) {
        this.corpIds = corpIds;
    }
}
