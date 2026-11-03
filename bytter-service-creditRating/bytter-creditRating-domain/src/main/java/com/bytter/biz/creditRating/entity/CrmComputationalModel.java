package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 功能介绍 : 计算模型<br>
 * <b>日期：</b>2024/8/8<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Entity
@Table(name = "COMPUTATIONAL_MODEL", schema = "")
public class CrmComputationalModel extends IdEntity implements java.io.Serializable{
    /** 模型批次id*/
    private String modelBatchId;
    /** 批次号*/
    private String batchNumber;
    /** 模型公式编号*/
    private String modelFormulaNumber;
    /** 模型公式名称*/
    private String modelFormulaName;
    /** 计算模型名称*/
    private String modelName;
    /** 计算模型代码*/
    private String modelCode;
    /** 计算模型说明*/
    private String modelDescription;
    /** 状态*/
    private Integer status;
    /** 是否有效*/
    private String validSign;
    /** 创建人*/
    private String creator;
    /** 创建日期时间*/
    private Date createDate;

    @Column(name ="MODEL_BATCH_ID")
    public String getModelBatchId() {
        return modelBatchId;
    }

    public void setModelBatchId(String modelBatchId) {
        this.modelBatchId = modelBatchId;
    }

    @Column(name ="BATCH_NUMBER")
    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    @Column(name ="MODEL_FORMULA_NUMBER")
    public String getModelFormulaNumber() {
        return modelFormulaNumber;
    }

    public void setModelFormulaNumber(String modelFormulaNumber) {
        this.modelFormulaNumber = modelFormulaNumber;
    }

    @Column(name ="MODEL_FORMULA_NAME")
    public String getModelFormulaName() {
        return modelFormulaName;
    }

    public void setModelFormulaName(String modelFormulaName) {
        this.modelFormulaName = modelFormulaName;
    }

    @Column(name ="MODEL_NAME")
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Column(name ="MODEL_CODE")
    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    @Column(name ="MODEL_DESCRIPTION")
    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Column(name ="VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
}
