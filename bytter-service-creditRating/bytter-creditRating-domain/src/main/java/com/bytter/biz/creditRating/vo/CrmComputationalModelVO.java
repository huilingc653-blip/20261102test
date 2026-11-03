package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 功能介绍 : 计算模型<br>
 * <b>日期：</b>2024/8/9<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class CrmComputationalModelVO implements Serializable {
    private String id;
    /** 模型批次id*/
    private String modelBatchId;
    /** 批次号*/
    private String batchNumber;
    /** 模型公式编号*/
    private String modelFormulaNumber;
    /** 模型公式名称*/
    @BtCompositeSearch(fieldName = "model.MODEL_FORMULA_NAME")
    private String modelFormulaName;
    /** 计算模型名称*/
    @BtCompositeSearch(fieldName = "model.MODEL_NAME")
    private String modelName;
    /** 计算模型代码*/
    @BtCompositeSearch(fieldName = "model.MODEL_CODE")
    private String modelCode;
    /** 计算模型说明*/
    @BtCompositeSearch(fieldName = "model.MODEL_DESCRIPTION")
    private String modelDescription;
    /** 状态*/
    private Integer status;
    /** 是否有效*/
    @BtCompositeSearch(fieldName = "model.VALID_SIGN")
    @Dict(dictGroup = "valid_sign", inject = "validSignName")
    private String validSign;
    private String validSignName;
    /** 创建人*/
    private String creator;
    /** 创建日期时间*/
    @BtCompositeSearch(fieldName = "model.CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 计分制*/
    private Integer scoringSystem;

    /** 原固化表id*/
    private String oldScoringIds;

    /** 指标获取id*/
    private String newScoringIds;

    /** 复制用原模型id*/
    private String copyModelId;

    /** 指标获取id*/
    private Map<String, BigDecimal> scCoefficientMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelBatchId() {
        return modelBatchId;
    }

    public void setModelBatchId(String modelBatchId) {
        this.modelBatchId = modelBatchId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getModelFormulaNumber() {
        return modelFormulaNumber;
    }

    public void setModelFormulaNumber(String modelFormulaNumber) {
        this.modelFormulaNumber = modelFormulaNumber;
    }

    public String getModelFormulaName() {
        return modelFormulaName;
    }

    public void setModelFormulaName(String modelFormulaName) {
        this.modelFormulaName = modelFormulaName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
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

    public String getValidSignName() {
        return validSignName;
    }

    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
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

    public Integer getScoringSystem() {
        return scoringSystem;
    }

    public void setScoringSystem(Integer scoringSystem) {
        this.scoringSystem = scoringSystem;
    }

    public String getOldScoringIds() {
        return oldScoringIds;
    }

    public void setOldScoringIds(String oldScoringIds) {
        this.oldScoringIds = oldScoringIds;
    }

    public String getNewScoringIds() {
        return newScoringIds;
    }

    public void setNewScoringIds(String newScoringIds) {
        this.newScoringIds = newScoringIds;
    }

    public Map<String, BigDecimal> getScCoefficientMap() {
        return scCoefficientMap;
    }

    public void setScCoefficientMap(Map<String, BigDecimal> scCoefficientMap) {
        this.scCoefficientMap = scCoefficientMap;
    }

    public String getCopyModelId() {
        return copyModelId;
    }

    public void setCopyModelId(String copyModelId) {
        this.copyModelId = copyModelId;
    }
}
