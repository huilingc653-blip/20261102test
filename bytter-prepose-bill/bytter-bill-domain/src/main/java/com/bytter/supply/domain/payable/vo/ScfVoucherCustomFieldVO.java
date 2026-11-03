package com.bytter.supply.domain.payable.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("应付债权凭证自定义字段VO")
public class ScfVoucherCustomFieldVO implements Serializable {
    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("凭证ID")
    private String voucherId;
    @ApiModelProperty("自定义字段ID")
    private String fieldId;
    @ApiModelProperty("自定义字段编码")
    private String fieldCode;
    @ApiModelProperty("自定义字段名称")
    private String fieldName;
    @ApiModelProperty("自定义字段类型")
    private String fieldType;
    @ApiModelProperty("自定义字段值")
    private String fieldValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
