package com.bytter.biz.creditRating.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能介绍 :评分等级级别固化 <br>
 * <b>日期：</b>2024/9/2<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class CrmScoringGradeLevelSolidVO implements Serializable {
    private String id;
    /**
     * 评分等级id
     */
    private String scoringGradeSolidId;
    /**
     * 批次号
     */
    private String batchNumber;
    /**
     * 模型批次id
     */
    private String modelBatchId;
    /**
     * 评分级别
     */
    private String scoringLevel;
    /**
     * 评分级别上限分
     */
    private BigDecimal upperScoreLimitLevel;
    /**
     * 评分级别下限分
     */
    private BigDecimal lowerScoreLimitLevel;
    /**
     * 评分级别说明
     */
    private String explanationOfScoringLevel;
    /**
     * 上限等级符号
     */
    private String upScoringSymbol;
    /**
     * 下限等级符号
     */
    private String downScoringSymbol;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期时间
     */
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScoringGradeSolidId() {
        return scoringGradeSolidId;
    }

    public void setScoringGradeSolidId(String scoringGradeSolidId) {
        this.scoringGradeSolidId = scoringGradeSolidId;
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

    public String getScoringLevel() {
        return scoringLevel;
    }

    public void setScoringLevel(String scoringLevel) {
        this.scoringLevel = scoringLevel;
    }

    public BigDecimal getUpperScoreLimitLevel() {
        return upperScoreLimitLevel;
    }

    public void setUpperScoreLimitLevel(BigDecimal upperScoreLimitLevel) {
        this.upperScoreLimitLevel = upperScoreLimitLevel;
    }

    public BigDecimal getLowerScoreLimitLevel() {
        return lowerScoreLimitLevel;
    }

    public void setLowerScoreLimitLevel(BigDecimal lowerScoreLimitLevel) {
        this.lowerScoreLimitLevel = lowerScoreLimitLevel;
    }

    public String getExplanationOfScoringLevel() {
        return explanationOfScoringLevel;
    }

    public void setExplanationOfScoringLevel(String explanationOfScoringLevel) {
        this.explanationOfScoringLevel = explanationOfScoringLevel;
    }

    public String getUpScoringSymbol() {
        return upScoringSymbol;
    }

    public void setUpScoringSymbol(String upScoringSymbol) {
        this.upScoringSymbol = upScoringSymbol;
    }

    public String getDownScoringSymbol() {
        return downScoringSymbol;
    }

    public void setDownScoringSymbol(String downScoringSymbol) {
        this.downScoringSymbol = downScoringSymbol;
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
}
