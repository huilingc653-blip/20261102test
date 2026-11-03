package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Title: Entity
 * @Description: 报告中评分指标值固化
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Entity
@Table(name = "SCORING_METRIC_VAL_FIX_REPORT", schema = "")
public class ScoringMetricValFixReport extends IdEntity implements java.io.Serializable {

    /** 评分指标名称*/
    private String scoringIndicatorName;

    /** 评分指标说明*/
    private String scoringIndicatorDescription;

    /** 评分指标代码*/
    private String scoringIndicatorCode;

    /** 指标类别名称*/
    private String categoryName;

    /** 指标类别代码*/
    private String categoryEncoding;

    /** 指标数值*/
    private BigDecimal indicatorValue;

    /** 外键（报告中已评数据主键）*/
    private String evaluatedDataId;

    /** 指标得分*/
    private BigDecimal score;

    @Column(name ="SCORING_INDICATOR_NAME",nullable=true,length=100)
    public String getScoringIndicatorName() {
        return scoringIndicatorName;
    }

    public void setScoringIndicatorName(String scoringIndicatorName) {
        this.scoringIndicatorName = scoringIndicatorName;
    }

    @Column(name ="SCORING_INDICATOR_DESCRIPTION",nullable=true,length=200)
    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
    }

    @Column(name ="SCORING_INDICATOR_CODE",nullable=true,length=4)
    public String getScoringIndicatorCode() {
        return scoringIndicatorCode;
    }

    public void setScoringIndicatorCode(String scoringIndicatorCode) {
        this.scoringIndicatorCode = scoringIndicatorCode;
    }

    @Column(name ="CATEGORY_NAME",nullable=true,length=100)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column(name ="CATEGORY_ENCODING",nullable=true,length=4)
    public String getCategoryEncoding() {
        return categoryEncoding;
    }

    public void setCategoryEncoding(String categoryEncoding) {
        this.categoryEncoding = categoryEncoding;
    }

    @Column(name ="INDICATOR_VALUE",nullable=true,scale=2,length=15)
    public BigDecimal getIndicatorValue() {
        return indicatorValue;
    }

    public void setIndicatorValue(BigDecimal indicatorValue) {
        this.indicatorValue = indicatorValue;
    }

    @Column(name ="EVALUATED_DATA_ID",nullable=true,length=32)
    public String getEvaluatedDataId() {
        return evaluatedDataId;
    }

    public void setEvaluatedDataId(String evaluatedDataId) {
        this.evaluatedDataId = evaluatedDataId;
    }

    @Column(name ="SCORE",nullable=true)
    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

}
