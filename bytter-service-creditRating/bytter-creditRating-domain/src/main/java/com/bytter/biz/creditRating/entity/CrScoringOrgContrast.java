package com.bytter.biz.creditRating.entity;


import com.bytter.core.base.entity.IdEntity;

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
@Table(name = "CR_SCORING_ORG_CONTRAST", schema = "")
public class CrScoringOrgContrast extends IdEntity implements Serializable {

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
    private String orgId;

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
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;

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

    @Column(name = "ORG_ID", length = 32)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    @Column(name = "UPDATE_BY", length = 100)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
