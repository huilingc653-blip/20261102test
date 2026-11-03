package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 功能介绍 : 评分等级<br>
 * <b>日期：</b>2024/8/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class CrmScoringGradeVO implements Serializable {
    private String id;
    /** 计分制*/
    private Integer scoringSystem;
    /**
     * 评分等级名称
     */
    @BtCompositeSearch(fieldName = "sg.SCORING_GRADE_NAME")
    private String scoringGradeName;
    /**
     * 评分等级编码
     */
    @BtCompositeSearch(fieldName = "sg.SCORING_GRADE_CODE")
    private String scoringGradeCode;
    /**
     * 机构类别
     */
    @BtCompositeSearch(fieldName = "sg.INSTITUTION_TYPE")
//    @Dict(dictGroup = "INSTITUTIONA_TYPE", inject = "institutionTypeName")
    private String institutionType;
    private String institutionTypeName;
    /** 评分等级说明*/
    @BtCompositeSearch(fieldName = "sg.SCORING_GRADE_DESCRIPTION")
    private String scoringGradeDescription;
    /** 创建人*/
    private String creator;
    /** 创建日期时间*/
    private Date createDate;
    /** 状态*/
    private Integer status;
    /** 是否有效*/
    @BtCompositeSearch(fieldName = "sg.VALID_SIGN")
    @Dict(dictGroup = "valid_sign", inject = "validSignName")
    private String validSign;
    private String validSignName;

    /** 评分等级明细*/
    private List<CrmScoringGradeLevelVO> gradeLevels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getScoringSystem() {
        return scoringSystem;
    }

    public void setScoringSystem(Integer scoringSystem) {
        this.scoringSystem = scoringSystem;
    }

    public String getScoringGradeDescription() {
        return scoringGradeDescription;
    }

    public void setScoringGradeDescription(String scoringGradeDescription) {
        this.scoringGradeDescription = scoringGradeDescription;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getScoringGradeName() {
        return scoringGradeName;
    }

    public void setScoringGradeName(String scoringGradeName) {
        this.scoringGradeName = scoringGradeName;
    }

    public String getScoringGradeCode() {
        return scoringGradeCode;
    }

    public void setScoringGradeCode(String scoringGradeCode) {
        this.scoringGradeCode = scoringGradeCode;
    }

    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
    }

    public String getInstitutionTypeName() {
        return institutionTypeName;
    }

    public void setInstitutionTypeName(String institutionTypeName) {
        this.institutionTypeName = institutionTypeName;
    }

    public String getValidSignName() {
        return validSignName;
    }

    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }

    public List<CrmScoringGradeLevelVO> getGradeLevels() {
        return gradeLevels;
    }

    public void setGradeLevels(List<CrmScoringGradeLevelVO> gradeLevels) {
        this.gradeLevels = gradeLevels;
    }
}
