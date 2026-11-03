package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 评分指标定量方法
 */
@Entity
@Table(name = "CR_QUA_METHOD_FOR_SCORING_IND", schema = "")
@SuppressWarnings("serial")
public class CrQuaMethodForScoringInd extends IdEntity implements java.io.Serializable{

    /**
     * 指标类别表id
     */
    private String scoringIndicatorId;
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
     * 计量方法的值
     */
    private BigDecimal value;
    /**
     * 计量方法的值
     */
    private BigDecimal otherValue;
    /**
     * 指标方向名称
     */
    private String valueName;
    /**
     * 指标方向名称代码
     */
    private String valueNameCode;
    /**
     * 版本号
     */
    private Integer versionNumber;
    /**
     * 评分细则符号
     */
    private Integer valueDirection;
    /**
     * 评分细则符号
     */
    private Integer otherValueDirection;
    /**
     * 指标评分
     */
    private BigDecimal grade;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createDate;

    @Column(name ="SCORING_INDICATOR_ID",nullable=false,length=32)
    public String getScoringIndicatorId() {
        return scoringIndicatorId;
    }

    public void setScoringIndicatorId(String scoringIndicatorId) {
        this.scoringIndicatorId = scoringIndicatorId;
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
    @Column(name = "VALUE")
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
    @Column(name ="VALUE_NAME",nullable=false,length=100)
    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
    @Column(name ="VALUE_NAME_CODE",nullable=false,length=100)
    public String getValueNameCode() {
        return valueNameCode;
    }

    public void setValueNameCode(String valueNameCode) {
        this.valueNameCode = valueNameCode;
    }
    @Column(name ="VERSION_NUMBER",length=4)
    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Column(name = "OTHERVALUE")
    public BigDecimal getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(BigDecimal otherValue) {
        this.otherValue = otherValue;
    }
    @Column(name ="VALUE_DIRECTION",length=4)
    public Integer getValueDirection() {
        return valueDirection;
    }

    public void setValueDirection(Integer valueDirection) {
        this.valueDirection = valueDirection;
    }
    @Column(name ="OTHERVALUE_DIRECTION",length=4)
    public Integer getOtherValueDirection() {
        return otherValueDirection;
    }

    public void setOtherValueDirection(Integer otherValueDirection) {
        this.otherValueDirection = otherValueDirection;
    }

    @Column(name = "GRADE")
    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
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
