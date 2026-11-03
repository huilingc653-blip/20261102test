package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.BtCompositeSearch;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CrQuaMethodForScoringIndVo implements Serializable {
    /**
     * id
     */
    @BtCompositeSearch(fieldName = "qmf.id")
    private String id;
    /**
     * 指标类别表id
     */
    @BtCompositeSearch(fieldName = "qmf.SCORING_INDICATOR_ID")
    private String scoringIndicatorId;
    /**
     * 评分指标的代码
     */
    @BtCompositeSearch(fieldName = "qmf.SCORING_INDICATOR_CODE")
    private String scoringIndicatorCode;
    /**
     * 评分指标的名称
     */
    @BtCompositeSearch(fieldName = "qmf.SCORING_INDICATOR_NAME")
    private String scoringIndicatorName;
    /**
     * 评分指标作用说明
     */
    @BtCompositeSearch(fieldName = "qmf.SCORING_INDICATOR_DESCRIPTION")
    private String scoringIndicatorDescription;
    /**
     * 指标大类代码
     */
    @BtCompositeSearch(fieldName = "qmf.CATEGORY_ENCODING")
    private String categoryEnCoding;
    /**
     * 指标大类名称
     */
    @BtCompositeSearch(fieldName = "qmf.CATEGORY_NAME")
    private String categoryName;
    /**
     * 计量方法的值
     */
    @BtCompositeSearch(fieldName = "qmf.VALUE")
    private BigDecimal value;
    /**
     * 计量方法的额外的值
     */
    @BtCompositeSearch(fieldName = "qmf.OTHERVALUE")
    private BigDecimal otherValue;
    /**
     * 指标方向名称
     */
    @BtCompositeSearch(fieldName = "qmf.VALUE_NAME")
    private String valueName;
    /**
     * 指标方向名称代码
     */
    @BtCompositeSearch(fieldName = "qmf.VALUE_NAME_CODE")
    private String valueNameCode;
    /**
     * 计量方法值对应的评分细则符号
     */
    private Integer valueDirection;
    /**
     * 计量方法额外的值对应的评分细则符号
     */
    private Integer otherValueDirection;
    /**
     * 指标评分
     */
    private BigDecimal grade;
    /**
     * 版本号
     */
    @BtCompositeSearch(fieldName = "qmf.VERSION_NUMBER")
    private Integer versionNumber;
    /**
     * 创建者
     */
    @BtCompositeSearch(fieldName = "qmf.CREATOR")
    private String creator;
    /**
     * 创建时间
     */
    @BtCompositeSearch(fieldName = "qmf.CREATE_DATE")
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScoringIndicatorId() {
        return scoringIndicatorId;
    }

    public void setScoringIndicatorId(String scoringIndicatorId) {
        this.scoringIndicatorId = scoringIndicatorId;
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


    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
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

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public BigDecimal getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(BigDecimal otherValue) {
        this.otherValue = otherValue;
    }

    public Integer getValueDirection() {
        return valueDirection;
    }

    public void setValueDirection(Integer valueDirection) {
        this.valueDirection = valueDirection;
    }

    public Integer getOtherValueDirection() {
        return otherValueDirection;
    }

    public void setOtherValueDirection(Integer otherValueDirection) {
        this.otherValueDirection = otherValueDirection;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
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
