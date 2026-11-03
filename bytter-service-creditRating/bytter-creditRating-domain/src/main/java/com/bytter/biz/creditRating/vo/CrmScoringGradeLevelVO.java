package com.bytter.biz.creditRating.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能介绍 : 评分等级级别<br>
 * <b>日期：</b>2024/8/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class CrmScoringGradeLevelVO implements Serializable {
    private String id;
    /** 评分等级id*/
    private String scoringGradeId;
    /** 评分级别*/
    private String scoringLevel;
    /** 评分级别上限分*/
    private BigDecimal upperScoreLimitLevel;
    /** 评分级别下限分*/
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
    /**
     * 显示等级符号
     */
    private String scoringSymbol;
    /** 显示分值*/
    private BigDecimal scoreLimitLevel;
    /** 创建人*/
    private String creator;
    /** 创建日期时间*/
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScoringGradeId() {
        return scoringGradeId;
    }

    public void setScoringGradeId(String scoringGradeId) {
        this.scoringGradeId = scoringGradeId;
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

    public String getScoringSymbol() {
        return scoringSymbol;
    }

    public void setScoringSymbol(String scoringSymbol) {
        this.scoringSymbol = scoringSymbol;
    }

    public BigDecimal getScoreLimitLevel() {
        return scoreLimitLevel;
    }

    public void setScoreLimitLevel(BigDecimal scoreLimitLevel) {
        this.scoreLimitLevel = scoreLimitLevel;
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
