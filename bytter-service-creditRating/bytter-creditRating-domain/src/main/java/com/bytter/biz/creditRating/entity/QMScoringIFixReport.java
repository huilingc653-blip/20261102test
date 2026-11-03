package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @Title: Entity
 * @Description: 报告中评分指标定量方法固化
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Entity
@Table(name = "Q_M_SCORING_I_FIX_REPORT", schema = "")
public class QMScoringIFixReport extends IdEntity implements java.io.Serializable {
    /** 评估报告主键*/
    private String evaluationReportId;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 评分指标名称*/
    private String scoringIndicatorName;

    /** 评分指标代码*/
    private String scoringIndicatorCode;

    /** 评分指标说明*/
    private String scoringIndicatorDescription;

    /** 指标类别名称*/
    private String categoryName;

    /** 指标类别代码*/
    private String categoryEncoding;

    /** 数值*/
    private BigDecimal value;

    /** 数值名称*/
    private String valueName;

    /** 数值名称代码*/
    private String valueNameCode;

    /** 报告中评分指标固化主键*/
    private String sifrId;

    /** 额外数值*/
    private BigDecimal otherValue;

    /** 版本号*/
    private Integer versionNumber;

    /** 数值对应得分细则符号*/
    private Integer valueDirection;

    /** 额外数值对应得分细则符号*/
    private Integer otherValueDirection;

    /** 得分*/
    private BigDecimal grade;

    @Column(name="OTHERVALUE")
    public BigDecimal getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(BigDecimal otherValue) {
        this.otherValue = otherValue;
    }

    @Column(name ="VERSION_NUMBER")
    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Column(name ="VALUE_DIRECTION")
    public Integer getValueDirection() {
        return valueDirection;
    }

    public void setValueDirection(Integer valueDirection) {
        this.valueDirection = valueDirection;
    }

    @Column(name ="OTHERVALUE_DIRECTION")
    public Integer getOtherValueDirection() {
        return otherValueDirection;
    }

    public void setOtherValueDirection(Integer otherValueDirection) {
        this.otherValueDirection = otherValueDirection;
    }

    @Column(name ="GRADE")
    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    @Column(name ="EVALUATION_REPORT_ID",nullable=true,length=32)
    public String getEvaluationReportId() {
        return evaluationReportId;
    }

    public void setEvaluationReportId(String evaluationReportId) {
        this.evaluationReportId = evaluationReportId;
    }

    @Column(name ="MODEL_BATCH_NUMBER",nullable=true,length=13)
    public String getModelBatchNumber() {
        return modelBatchNumber;
    }

    public void setModelBatchNumber(String modelBatchNumber) {
        this.modelBatchNumber = modelBatchNumber;
    }

    @Column(name ="EVALUATION_BATCH_NUMBER",nullable=true,length=13)
    public String getEvaluationBatchNumber() {
        return evaluationBatchNumber;
    }

    public void setEvaluationBatchNumber(String evaluationBatchNumber) {
        this.evaluationBatchNumber = evaluationBatchNumber;
    }

    @Column(name ="SCORING_INDICATOR_NAME",nullable=true,length=100)
    public String getScoringIndicatorName() {
        return scoringIndicatorName;
    }

    public void setScoringIndicatorName(String scoringIndicatorName) {
        this.scoringIndicatorName = scoringIndicatorName;
    }

    @Column(name ="SCORING_INDICATOR_CODE",nullable=true,length=4)
    public String getScoringIndicatorCode() {
        return scoringIndicatorCode;
    }

    public void setScoringIndicatorCode(String scoringIndicatorCode) {
        this.scoringIndicatorCode = scoringIndicatorCode;
    }

    @Column(name ="SCORING_INDICATOR_DESCRIPTION",nullable=true,length=200)
    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
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

    @Column(name ="VALUE",nullable=true)
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Column(name ="VALUE_NAME",nullable=true,length=100)
    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    @Column(name ="VALUE_NAME_CODE",nullable=true,length=6)
    public String getValueNameCode() {
        return valueNameCode;
    }

    public void setValueNameCode(String valueNameCode) {
        this.valueNameCode = valueNameCode;
    }

    @Column(name ="S_I_F_R_ID",nullable=true,length=32)
    public String getSifrId() {
        return sifrId;
    }

    public void setSifrId(String sifrId) {
        this.sifrId = sifrId;
    }
}
