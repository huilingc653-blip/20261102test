package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@Entity
@Table(name = "SCORING_GRADE_LEVEL", schema = "")
public class CrmScoringGradeLevel extends IdEntity implements java.io.Serializable {
    /**
     * 评分等级id
     */
    private String scoringGradeId;
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

    @Column(name = "SCORING_GRADE_ID")
    public String getScoringGradeId() {
        return scoringGradeId;
    }

    public void setScoringGradeId(String scoringGradeId) {
        this.scoringGradeId = scoringGradeId;
    }

    @Column(name = "SCORING_LEVEL")
    public String getScoringLevel() {
        return scoringLevel;
    }

    public void setScoringLevel(String scoringLevel) {
        this.scoringLevel = scoringLevel;
    }

    @Column(name = "UPPER_SCORE_LIMIT_LEVEL")
    public BigDecimal getUpperScoreLimitLevel() {
        return upperScoreLimitLevel;
    }

    public void setUpperScoreLimitLevel(BigDecimal upperScoreLimitLevel) {
        this.upperScoreLimitLevel = upperScoreLimitLevel;
    }

    @Column(name = "LOWER_SCORE_LIMIT_LEVEL")
    public BigDecimal getLowerScoreLimitLevel() {
        return lowerScoreLimitLevel;
    }

    public void setLowerScoreLimitLevel(BigDecimal lowerScoreLimitLevel) {
        this.lowerScoreLimitLevel = lowerScoreLimitLevel;
    }


    @Column(name = "EXPLANATION_OF_SCORING_LEVEL")
    public String getExplanationOfScoringLevel() {
        return explanationOfScoringLevel;
    }

    public void setExplanationOfScoringLevel(String explanationOfScoringLevel) {
        this.explanationOfScoringLevel = explanationOfScoringLevel;
    }

    @Column(name = "UP_SCORING_SYMBOL")
    public String getUpScoringSymbol() {
        return upScoringSymbol;
    }

    public void setUpScoringSymbol(String upScoringSymbol) {
        this.upScoringSymbol = upScoringSymbol;
    }

    @Column(name = "DOWN_SCORING_SYMBOL")
    public String getDownScoringSymbol() {
        return downScoringSymbol;
    }

    public void setDownScoringSymbol(String downScoringSymbol) {
        this.downScoringSymbol = downScoringSymbol;
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
}
