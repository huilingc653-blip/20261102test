package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 报告中已评数据（原始数据固化）
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */

@Entity
@Table(name = "EVALUATED_DATA_FOR_REPORT", schema = "")
public class EvaluatedDataForReport extends IdEntity implements java.io.Serializable {

    /** 信用分*/
    private Integer creditScore;

    /** 信用等级*/
    private String creditGrade;

    /** 数据名称*/
    private String dataName;

    /** 数据名称*/
    private String dataType;

    /** 数据代码*/
    private String dataCode;

    /** 数据来源业务表主建*/
    private String dataId;

    /** 数据来源类别名称*/
    private String dataSourceCategoryName;

    /** 数据来源类别代码*/
    private String dataSourceCategoryCode;

    /** 评估报告主键*/
    private String evaluationReportId;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 创建人*/
    private String creator;

    /** 创建时间*/
    private Date createDate;

    /** 单位id*/
    private String corpId;

    /** 单位名称*/
    private String corpName;

    @Column(name ="CORP_NAME",nullable=true)
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    @Column(name ="CORP_ID",nullable=true)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="CREDIT_SCORE",nullable=true)
    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    @Column(name ="CREDIT_GRADE",nullable=true,length=10)
    public String getCreditGrade() {
        return creditGrade;
    }

    public void setCreditGrade(String creditGrade) {
        this.creditGrade = creditGrade;
    }

    @Column(name ="DATA_NAME",nullable=true,length=200)
    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    @Column(name ="DATA_CODE",nullable=true,length=32)
    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    @Column(name ="DATA_ID",nullable=true,length=32)
    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    @Column(name ="DATA_SOURCE_CATEGORY_NAME",nullable=true,length=100)
    public String getDataSourceCategoryName() {
        return dataSourceCategoryName;
    }

    public void setDataSourceCategoryName(String dataSourceCategoryName) {
        this.dataSourceCategoryName = dataSourceCategoryName;
    }

    @Column(name ="DATA_SOURCE_CATEGORY_CODE",nullable=true,length=4)
    public String getDataSourceCategoryCode() {
        return dataSourceCategoryCode;
    }

    public void setDataSourceCategoryCode(String dataSourceCategoryCode) {
        this.dataSourceCategoryCode = dataSourceCategoryCode;
    }

    @Column(name ="EVALUATION_REPORT_ID",nullable=true,length=32)
    public String getEvaluationReportId() {
        return evaluationReportId;
    }

    public void setEvaluationReportId(String evaluationReportId) {
        this.evaluationReportId = evaluationReportId;
    }

    @Column(name ="EVALUATION_BATCH_NUMBER",nullable=true,length=13)
    public String getEvaluationBatchNumber() {
        return evaluationBatchNumber;
    }

    public void setEvaluationBatchNumber(String evaluationBatchNumber) {
        this.evaluationBatchNumber = evaluationBatchNumber;
    }

    @Column(name ="CREATOR",nullable=true,length=100)
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

    @Column(name ="DATA_TYPE",nullable=true)
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
