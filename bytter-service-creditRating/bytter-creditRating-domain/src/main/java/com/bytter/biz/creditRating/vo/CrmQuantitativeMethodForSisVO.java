package com.bytter.biz.creditRating.vo;

import java.io.Serializable;
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
public class CrmQuantitativeMethodForSisVO implements Serializable {
    private String id;
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
    /** 评分指标固化表主键id*/
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryEncoding() {
        return categoryEncoding;
    }

    public void setCategoryEncoding(String categoryEncoding) {
        this.categoryEncoding = categoryEncoding;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValueNameCode() {
        return valueNameCode;
    }

    public void setValueNameCode(String valueNameCode) {
        this.valueNameCode = valueNameCode;
    }

    public String getScoringIndicatorSolId() {
        return scoringIndicatorSolId;
    }

    public void setScoringIndicatorSolId(String scoringIndicatorSolId) {
        this.scoringIndicatorSolId = scoringIndicatorSolId;
    }

    public String getQuantitativeMethodForSiId() {
        return quantitativeMethodForSiId;
    }

    public void setQuantitativeMethodForSiId(String quantitativeMethodForSiId) {
        this.quantitativeMethodForSiId = quantitativeMethodForSiId;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
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

    public BigDecimal getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(BigDecimal otherValue) {
        this.otherValue = otherValue;
    }

    public Integer getOtherValueDirection() {
        return otherValueDirection;
    }

    public void setOtherValueDirection(Integer otherValueDirection) {
        this.otherValueDirection = otherValueDirection;
    }

    public Integer getValueDirection() {
        return valueDirection;
    }

    public void setValueDirection(Integer valueDirection) {
        this.valueDirection = valueDirection;
    }



    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }
}
