package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 功能介绍 : 评分等级<br>
 * <b>日期：</b>2024/8/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Entity
@Table(name = "SCORING_GRADE", schema = "")
public class CrmScoringGrade extends IdEntity implements java.io.Serializable {
    /**
     * 计分制
     */
    private Integer scoringSystem;
    /**
     * 评分等级名称
     */
    private String scoringGradeName;
    /**
     * 评分等级编码
     */
    private String scoringGradeCode;
    /**
     * 机构类别
     */
    private String institutionType;
    /**
     * 评分等级说明
     */
    private String scoringGradeDescription;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期时间
     */
    private Date createDate;
    /** 状态*/
    private Integer status;
    /** 是否有效*/
    private String validSign;

    @Column(name = "SCORING_SYSTEM")
    public Integer getScoringSystem() {
        return scoringSystem;
    }

    public void setScoringSystem(Integer scoringSystem) {
        this.scoringSystem = scoringSystem;
    }


    @Column(name = "SCORING_GRADE_DESCRIPTION")
    public String getScoringGradeDescription() {
        return scoringGradeDescription;
    }

    public void setScoringGradeDescription(String scoringGradeDescription) {
        this.scoringGradeDescription = scoringGradeDescription;
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

    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    @Column(name = "SCORING_GRADE_NAME")
    public String getScoringGradeName() {
        return scoringGradeName;
    }

    public void setScoringGradeName(String scoringGradeName) {
        this.scoringGradeName = scoringGradeName;
    }

    @Column(name = "SCORING_GRADE_CODE")
    public String getScoringGradeCode() {
        return scoringGradeCode;
    }

    public void setScoringGradeCode(String scoringGradeCode) {
        this.scoringGradeCode = scoringGradeCode;
    }

    @Column(name = "INSTITUTION_TYPE")
    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
    }
}
