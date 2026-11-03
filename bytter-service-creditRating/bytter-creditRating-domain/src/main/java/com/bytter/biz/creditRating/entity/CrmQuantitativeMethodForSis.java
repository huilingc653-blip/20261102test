package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能介绍 : 评分指标定量方法（固化）<br>
 * <b>日期：</b>2024/8/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Entity
@Table(name = "QUANTITATIVE_METHOD_FOR_SIS", schema = "")
public class CrmQuantitativeMethodForSis extends IdEntity implements java.io.Serializable{
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
    /** 数值名称：指标方向,最小可能值,最大可能值*/
    private String valueName;
    /** 数值名称代码*/
    private String valueNameCode;
    /** 评分指标固化id*/
    private String scoringIndicatorSolId;
    /** 评分指标定量方法主键*/
    private String quantitativeMethodForSiId;
    /** 版本号*/
    private Integer versionNumber;
    /** 创建人*/
    private String creator;
    /** 创建日期时间*/
    private Date createDate;
    /** 额外数值*/
    private BigDecimal otherValue;
    /** 数值对应得分细则符号*/
    private Integer valueDirection;
    /** 额外数值对应得分细则符号*/
    private Integer otherValueDirection;
    /** 得分*/
    private BigDecimal grade;

    @Column(name ="SCORING_INDICATOR_NAME")
    public String getScoringIndicatorName() {
        return scoringIndicatorName;
    }

    public void setScoringIndicatorName(String scoringIndicatorName) {
        this.scoringIndicatorName = scoringIndicatorName;
    }

    @Column(name ="SCORING_INDICATOR_CODE")
    public String getScoringIndicatorCode() {
        return scoringIndicatorCode;
    }

    public void setScoringIndicatorCode(String scoringIndicatorCode) {
        this.scoringIndicatorCode = scoringIndicatorCode;
    }

    @Column(name ="SCORING_INDICATOR_DESCRIPTION")
    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
    }

    @Column(name ="CATEGORY_NAME")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column(name ="CATEGORY_ENCODING")
    public String getCategoryEncoding() {
        return categoryEncoding;
    }

    public void setCategoryEncoding(String categoryEncoding) {
        this.categoryEncoding = categoryEncoding;
    }

    @Column(name ="VALUE")
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }


    @Column(name ="VALUE_NAME")
    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    @Column(name ="VALUE_NAME_CODE")
    public String getValueNameCode() {
        return valueNameCode;
    }

    public void setValueNameCode(String valueNameCode) {
        this.valueNameCode = valueNameCode;
    }

    @Column(name ="SCORING_INDICATOR_SOL_ID")
    public String getScoringIndicatorSolId() {
        return scoringIndicatorSolId;
    }

    public void setScoringIndicatorSolId(String scoringIndicatorSolId) {
        this.scoringIndicatorSolId = scoringIndicatorSolId;
    }

    @Column(name ="QUANTITATIVE_METHOD_FOR_SI_ID")
    public String getQuantitativeMethodForSiId() {
        return quantitativeMethodForSiId;
    }

    public void setQuantitativeMethodForSiId(String quantitativeMethodForSiId) {
        this.quantitativeMethodForSiId = quantitativeMethodForSiId;
    }

    @Column(name ="VERSION_NUMBER")
    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }


    @Column(name ="CREATOR")
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="OTHERVALUE")
    public BigDecimal getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(BigDecimal otherValue) {
        this.otherValue = otherValue;
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
}
