package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 评分指标
 */
@Entity
@Table(name = "CR_SCORING_INDICATOR", schema = "")
@SuppressWarnings("serial")
public class CrScoringIndicator extends IdEntity implements java.io.Serializable{
    /**
     * 指标类别表id
     */
    private String parentId;
    /**
     * 评分指标的代码
     */
    private String scoringIndicatorCode;
    /**
     * 评分指标的名称
     */
    private String scoringIndicatorName;
    /**
     * 评分指标作用说明
     */
    private String scoringIndicatorDescription;
    /**
     * 指标大类代码
     */
    private String categoryEnCoding;
    /**
     * 指标大类名称
     */
    private String categoryName;
    /**
     * 评分细则
     */
    private String scoringRubric;
    /**
     * 数据源id
     */
    private String dataSourceId;
    /**
     * 数据源id
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
     * 版本号
     */
    private Integer versionNumber;
    /**
     * 删除标志
     */
    private Integer deleteFlag;
    /**
     * 评分级别
     */
    private Integer level_;
    /**
     * 是否为末级指标
     */
    private Integer isLast;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createDate;

    @Column(name ="PARENT_ID",nullable=false,length=32)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    @Column(name ="SCORING_RUBRIC",nullable=false,length=2000)
    public String getScoringRubric() {
        return scoringRubric;
    }

    public void setScoringRubric(String scoringRubric) {
        this.scoringRubric = scoringRubric;
    }

    @Column(name ="DATA_SOURCE_ID",nullable=false,length=32)
    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    @Column(name ="DATA_SOURCE_CODE",nullable=false,length=32)
    public String getDataSourceCode() {
        return dataSourceCode;
    }

    public void setDataSourceCode(String dataSourceCode) {
        this.dataSourceCode = dataSourceCode;
    }

    @Column(name ="DATA_SOURCE_NAME",nullable=false,length=100)
    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
    @Column(name ="DATA_SOURCE_ORIGIN",nullable=false,length=1000)
    public String getDataSourceOrigin() {
        return dataSourceOrigin;
    }

    public void setDataSourceOrigin(String dataSourceOrigin) {
        this.dataSourceOrigin = dataSourceOrigin;
    }

    @Column(name ="SCORING_INDICATOR_CODE",nullable=false,length=12)
    public String getScoringIndicatorCode() {
        return scoringIndicatorCode;
    }

    public void setScoringIndicatorCode(String scoringIndicatorCode) {
        this.scoringIndicatorCode = scoringIndicatorCode;
    }
    @Column(name ="SCORING_INDICATOR_NAME",nullable=false,length=100)
    public String getScoringIndicatorName() {
        return scoringIndicatorName;
    }

    public void setScoringIndicatorName(String scoringIndicatorName) {
        this.scoringIndicatorName = scoringIndicatorName;
    }

    @Column(name ="SCORING_INDICATOR_DESCRIPTION",nullable=false,length=200)
    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
    }

    @Column(name ="CATEGORY_ENCODING",nullable=false,length=12)
    public String getCategoryEnCoding() {
        return categoryEnCoding;
    }

    public void setCategoryEnCoding(String categoryEnCoding) {
        this.categoryEnCoding = categoryEnCoding;
    }
    @Column(name ="CATEGORY_NAME",nullable=false,length=100)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    @Column(name ="DELETE_FLAG",length=4)
    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Column(name ="VERSION_NUMBER",length=4)
    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Column(name ="STATUS",nullable=false,length=4)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="LEVEL_",nullable=false,length=4)
    public Integer getLevel_() {
        return level_;
    }

    public void setLevel_(Integer level_) {
        this.level_ = level_;
    }
    @Column(name ="IS_LAST",nullable=false,length=4)
    public Integer getIsLast() {
        return isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
    }

    @Column(name ="CREATOR",nullable=false,length=100)
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
