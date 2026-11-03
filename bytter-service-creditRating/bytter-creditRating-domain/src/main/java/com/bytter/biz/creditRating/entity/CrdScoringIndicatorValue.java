package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 评分指标数值
 */
@Entity
@Table(name = "SCORING_INDICATOR_VALUE", schema = "")
@SuppressWarnings("serial")
public class CrdScoringIndicatorValue extends IdEntity implements Serializable {

    /**
     *评分指标主键:评分指标(Scoring_Indicator)id
     */
    private String scoringIndicatorId;
    /**
     *评分指标名称
     */
    private String scoringIndicatorName;
    /**
     *评分指标代码
     */
    private String scoringIndicatorCode;
    /**
     *评分指标说明
     */
    private String scoringIndicatorDescription;
    /**
     *指标类别名称;冗余。指标类别表(Indicator_Category)属性Category_Name
     */
    private String categoryName;
    /**
     *指标类别代码;冗余。指标类别表(Indicator_Category)属性Category_Encoding
     */
    private String categoryEncoding;
    /**
     *指标数值：待评数据指标的数值
     */
    private BigDecimal indicatorValue;
    /**
     *版本号
     */
    private Integer versionNumber;
    /**
     *外键(原始数据主键)：原始数据(Raw_Data)id
     */
    private String institutionalRawDataId;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期时间:yyyy-MM-dd hh24:mm:ss
     */
    private Date createDate;

    @Column(name = "SCORING_INDICATOR_ID")
    public String getScoringIndicatorId() {
        return scoringIndicatorId;
    }

    public void setScoringIndicatorId(String scoringIndicatorId) {
        this.scoringIndicatorId = scoringIndicatorId;
    }

    @Column(name = "SCORING_INDICATOR_NAME")
    public String getScoringIndicatorName() {
        return scoringIndicatorName;
    }

    public void setScoringIndicatorName(String scoringIndicatorName) {
        this.scoringIndicatorName = scoringIndicatorName;
    }

    @Column(name = "SCORING_INDICATOR_CODE")
    public String getScoringIndicatorCode() {
        return scoringIndicatorCode;
    }

    public void setScoringIndicatorCode(String scoringIndicatorCode) {
        this.scoringIndicatorCode = scoringIndicatorCode;
    }

    @Column(name = "SCORING_INDICATOR_DESCRIPTION")
    public String getScoringIndicatorDescription() {
        return scoringIndicatorDescription;
    }

    public void setScoringIndicatorDescription(String scoringIndicatorDescription) {
        this.scoringIndicatorDescription = scoringIndicatorDescription;
    }

    @Column(name = "CATEGORY_NAME")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Column(name = "CATEGORY_ENCODING")
    public String getCategoryEncoding() {
        return categoryEncoding;
    }

    public void setCategoryEncoding(String categoryEncoding) {
        this.categoryEncoding = categoryEncoding;
    }

    @Column(name = "INDICATOR_VALUE")
    public BigDecimal getIndicatorValue() {
        return indicatorValue;
    }

    public void setIndicatorValue(BigDecimal indicatorValue) {
        this.indicatorValue = indicatorValue;
    }

    @Column(name = "VERSION_NUMBER")
    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Column(name = "INSTITUTIONAL_RAW_DATA_ID")
    public String getInstitutionalRawDataId() {
        return institutionalRawDataId;
    }

    public void setInstitutionalRawDataId(String institutionalRawDataId) {
        this.institutionalRawDataId = institutionalRawDataId;
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

    @Override
    public String toString() {
        return "ScoringIndicatorValue{" +
                "scoringIndicatorId='" + scoringIndicatorId + '\'' +
                ", scoringIndicatorName='" + scoringIndicatorName + '\'' +
                ", scoringIndicatorCode='" + scoringIndicatorCode + '\'' +
                ", scoringIndicatorDescription='" + scoringIndicatorDescription + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryEncoding='" + categoryEncoding + '\'' +
                ", indicatorValue=" + indicatorValue +
                ", versionNumber=" + versionNumber +
                ", institutionalRawDataId='" + institutionalRawDataId + '\'' +
                ", creator='" + creator + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
