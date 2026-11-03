package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 评估报告
 * @author chenqin
 * @date 2024-08-08
 * @version 1.0
 *
 */
@Entity
@Table(name = "EVALUATION_REPORT", schema = "")
public class EvaluationReport extends IdEntity implements java.io.Serializable {
    /** 评估批次主键*/
    private String evaluationBatchId;

    /** 评估批次号*/
    private String evaluationBatchNumber;

    /** 模型批次号*/
    private String modelBatchNumber;

    /** 报告中计算模型固化主键*/
    private String modelSolidificationId;

    /** 计算模型名称*/
    private String modelName;

    /** 计算模型代码*/
    private String modelCode;

    /** 计算模型说明*/
    private String modelDescription;

    /** 已评数据主键*/
    private String evaluatedDataId;

    /** 数据名称*/
    private String dataName;

    /** 数据代码*/
    private String dataCode;

    /** 数据来源类别名称*/
    private String dataSourceCategoryName;

    /** 数据来源类别代码*/
    private String dataSourceCategoryCode;

    /** 单位id*/
    //private String corpId;

    /** 状态*/
    //private Integer status;

    /** 创建人*/
    private String creator;

    /** 创建时间*/
    private Date createDate;

    /*@Column(name ="CORP_ID",nullable=true,length=32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="STATUS",nullable=true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }*/

    @Column(name ="EVALUATION_BATCH_ID",nullable=true,length=32)
    public String getEvaluationBatchId() {
        return evaluationBatchId;
    }

    public void setEvaluationBatchId(String evaluationBatchId) {
        this.evaluationBatchId = evaluationBatchId;
    }

    @Column(name ="EVALUATION_BATCH_NUMBER",nullable=true,length=13)
    public String getEvaluationBatchNumber() {
        return evaluationBatchNumber;
    }

    public void setEvaluationBatchNumber(String evaluationBatchNumber) {
        this.evaluationBatchNumber = evaluationBatchNumber;
    }

    @Column(name ="MODEL_BATCH_NUMBER",nullable=true,length=13)
    public String getModelBatchNumber() {
        return modelBatchNumber;
    }

    public void setModelBatchNumber(String modelBatchNumber) {
        this.modelBatchNumber = modelBatchNumber;
    }

    @Column(name ="MODEL_SOLIDIFICATION_ID",nullable=true,length=32)
    public String getModelSolidificationId() {
        return modelSolidificationId;
    }

    public void setModelSolidificationId(String modelSolidificationId) {
        this.modelSolidificationId = modelSolidificationId;
    }

    @Column(name ="MODEL_NAME",nullable=true,length=100)
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Column(name ="MODEL_CODE",nullable=true,length=6)
    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    @Column(name ="MODEL_DESCRIPTION",nullable=true,length=200)
    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    @Column(name ="EVALUATED_DATA_ID",nullable=true,length=32)
    public String getEvaluatedDataId() {
        return evaluatedDataId;
    }

    public void setEvaluatedDataId(String evaluatedDataId) {
        this.evaluatedDataId = evaluatedDataId;
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
}
