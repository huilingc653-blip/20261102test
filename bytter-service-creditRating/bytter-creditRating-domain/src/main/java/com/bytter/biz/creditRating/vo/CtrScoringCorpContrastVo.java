package com.bytter.biz.creditRating.vo;


import com.bytter.core.base.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 评分机构评分指标对照表
 *
 */
@Data
public class CtrScoringCorpContrastVo extends IdEntity implements Serializable {

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
     * 单位名称
     */
    private String corpName;

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
    private String scoringIndicatorCoefficient;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 子指标
     */
    private List<CtrScoringCorpContrastVo> childrenScoringList;

    /**
     * 指标级别
     */
    private Integer level_;

    /**
     * 此指标所占行数
     */
    private int rowSpan;

    /**
     * 此指标所占列数
     */
    private int colSpan;

    /**
     * 评分细则标题
     */
    private String scoringRubric;

    /**
     * 是否自动评分
     */
    private String isAutoScoring;

    /**
     * 归一法最大值
     */
    private BigDecimal maxValue;

    /**
     * 归一法最小值
     */
    private BigDecimal minValue;

    /**
     * 评分方法
     */
    private BigDecimal scoringMethod;

    /**
     * 机构得分
     */
    private List<OrgScoreVO> orgScoreList;

    /**
     * 评分指标基础信息
     */
    private CrmScoringIndicatorSolidificatiVO indicatorInfo;

    /**
     * 评分指标值固化集合
     */
    private List<ScoringMetricValFixReportVO> scoringMetricValFixReportList;

    /**
     * 额外加减项
     */
    public String isAddOrSubGradeName;

    /**
     * 指标性质
     */
    private BigDecimal indexProperty;

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
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

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getScoringIndicatorSolidificati() {
        return scoringIndicatorSolidificati;
    }

    public void setScoringIndicatorSolidificati(String scoringIndicatorSolidificati) {
        this.scoringIndicatorSolidificati = scoringIndicatorSolidificati;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getScoringIndicatorCoefficient() {
        return scoringIndicatorCoefficient;
    }

    public void setScoringIndicatorCoefficient(String scoringIndicatorCoefficient) {
        this.scoringIndicatorCoefficient = scoringIndicatorCoefficient;
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

    public List<CtrScoringCorpContrastVo> getChildrenScoringList() {
        return childrenScoringList;
    }

    public void setChildrenScoringList(List<CtrScoringCorpContrastVo> childrenScoringList) {
        this.childrenScoringList = childrenScoringList;
    }

    public Integer getLevel_() {
        return level_;
    }

    public void setLevel_(Integer level_) {
        this.level_ = level_;
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

    public String getScoringRubric() {
        return scoringRubric;
    }

    public void setScoringRubric(String scoringRubric) {
        this.scoringRubric = scoringRubric;
    }

    public String getIsAutoScoring() {
        return isAutoScoring;
    }

    public void setIsAutoScoring(String isAutoScoring) {
        this.isAutoScoring = isAutoScoring;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getScoringMethod() {
        return scoringMethod;
    }

    public void setScoringMethod(BigDecimal scoringMethod) {
        this.scoringMethod = scoringMethod;
    }

    public List<OrgScoreVO> getOrgScoreList() {
        return orgScoreList;
    }

    public void setOrgScoreList(List<OrgScoreVO> orgScoreList) {
        this.orgScoreList = orgScoreList;
    }

    public CrmScoringIndicatorSolidificatiVO getIndicatorInfo() {
        return indicatorInfo;
    }

    public void setIndicatorInfo(CrmScoringIndicatorSolidificatiVO indicatorInfo) {
        this.indicatorInfo = indicatorInfo;
    }

    public List<ScoringMetricValFixReportVO> getScoringMetricValFixReportList() {
        return scoringMetricValFixReportList;
    }

    public void setScoringMetricValFixReportList(List<ScoringMetricValFixReportVO> scoringMetricValFixReportList) {
        this.scoringMetricValFixReportList = scoringMetricValFixReportList;
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
}
