package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ExcelTarget("CrScoringIndicatorVo")
public class CrScoringIndicatorVo extends ExcelModel implements Serializable, IBusinessKey {
    /**
     * id
     */
    @BtCompositeSearch(fieldName = "si.ID")
    private String id;
    /**
     * 指标类别表id
     */
    @BtCompositeSearch(fieldName = "si.PARENT_ID")
    private String parentId;
    /**
     * 评分指标的代码
     */
    @BtCompositeSearch(fieldName = "si.SCORING_INDICATOR_CODE")
    private String scoringIndicatorCode;
    /**
     * 评分指标的名称
     */
    @BtCompositeSearch(fieldName = "si.SCORING_INDICATOR_NAME")
    private String scoringIndicatorName;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "si.SCORING_INDICATOR_DESCRIPTION")
    private String scoringIndicatorDescription;
    /**
     * 指标大类代码
     */
    @BtCompositeSearch(fieldName = "si.CATEGORY_ENCODING")
    private String categoryEnCoding;
    /**
     * 指标大类名称
     */
    @BtCompositeSearch(fieldName = "si.CATEGORY_NAME")
    private String categoryName;
    /**
     * 版本号
     */
    @BtCompositeSearch(fieldName = "si.VERSION_NUMBER")
    private Integer versionNumber;
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
     * 删除标志
     */
    @BtCompositeSearch(fieldName = "si.DELETE_FLAG")
    private Integer deleteFlag;
    /**
     * 状态
     */
    @Dict(dictGroup = "valid_sign", inject = "statusName")
    @BtCompositeSearch(fieldName = "si.STATUS")
    private Integer status;
    private String statusName;
    /**
     * 评分级别
     */
    private Integer level_;
    /**
     * 是否为末级指标
     */
    private Integer isLast;
    /**
     * 创建者
     */
    @BtCompositeSearch(fieldName = "si.CREATOR")
    private String creator;
    /**
     * 创建时间
     */
    @BtCompositeSearch(fieldName = "si.CREATE_DATE")
    private Date createDate;

    /**
     *指标方向
     */
    private BigDecimal indicatorDirectionValue;
    /**
     *最大可能值
     */
    private BigDecimal maxPossibleValue;
    /**
     *最小可能值
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
    /**
     * 评分标准
     */
    private List<CrScoringStandardVo> crScoringStandardVoList;
    /**
     * 是否额外加/减分
     */
    private BigDecimal isAddOrSubGrade;
    /**
     * 指标性质
     */
    private BigDecimal indexProperty;
    /**
     * 是否被勾选
     */
    private String isChecked;

    /**
     * 子集
     */
    private List<CrScoringIndicatorVo> childrens;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScoringIndicatorCode() {
        return scoringIndicatorCode;
    }

    public void setScoringIndicatorCode(String scoringIndicatorCode) {
        this.scoringIndicatorCode = scoringIndicatorCode;
    }

    public String getScoringIndicatorName() {
        return scoringIndicatorName;
    }

    public void setScoringIndicatorName(String scoringIndicatorName) {
        this.scoringIndicatorName = scoringIndicatorName;
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

    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
    }

    public String getDataSourceCode() {
        return dataSourceCode;
    }

    public void setDataSourceCode(String dataSourceCode) {
        this.dataSourceCode = dataSourceCode;
    }

    public String getCategoryEnCoding() {
        return categoryEnCoding;
    }

    public void setCategoryEnCoding(String categoryEnCoding) {
        this.categoryEnCoding = categoryEnCoding;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public void setLevel(Integer level_) {
        this.level_ = level_;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
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

    public List<CrScoringStandardVo> getCrScoringStandardVoList() {
        return crScoringStandardVoList;
    }

    public void setCrScoringStandardVoList(List<CrScoringStandardVo> crScoringStandardVoList) {
        this.crScoringStandardVoList = crScoringStandardVoList;
    }

    public BigDecimal getIsAddOrSubGrade() {
        return isAddOrSubGrade;
    }

    public void setIsAddOrSubGrade(BigDecimal isAddOrSubGrade) {
        this.isAddOrSubGrade = isAddOrSubGrade;
    }

    public BigDecimal getIndexProperty() {
        return indexProperty;
    }

    public void setIndexProperty(BigDecimal indexProperty) {
        this.indexProperty = indexProperty;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<CrScoringIndicatorVo> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<CrScoringIndicatorVo> childrens) {
        this.childrens = childrens;
    }

    @Override
    public String getBusinessKey() {
        return "crScoringIndicatorService";
    }
}
