package com.bytter.supply.domain.payable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("应付债权字段预设VO")
public class ScfPayFieldConfigVO implements Serializable {
    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 默认排序
     */
    @BtCompositeSearch(fieldName = "f.FIELD_SORT")
    @ApiModelProperty("默认排序")
    private Integer fieldSort;
    /**
     * 字段编码
     */
    @BtCompositeSearch(fieldName = "f.FIELD_CODE")
    @ApiModelProperty("字段编码")
    private String fieldCode;
    /**
     * 字段名称
     */
    @ApiModelProperty("字段名称")
    private String fieldName;
    /**
     * 字段类型
     */
    @ApiModelProperty("字段类型")
    private String fieldType;
    /**
     * 字段所属区域
     */
    @ApiModelProperty("字段所属区域")
    private String fieldArea;
    /**
     * 字段预设默认值
     */
    @ApiModelProperty("字段预设默认值")
    private String fieldDefVal;
    /**
     * 字段预设长度限制
     */
    @ApiModelProperty("字段预设长度限制")
    private String fieldLength;
    /**
     * 数据类型
     */
    @ApiModelProperty("数据类型")
    private String dataType;
    /**
     * 字典组
     */
    @ApiModelProperty("字典组")
    private String dataGroup;
    /**
     * 数据来源URL
     */
    @ApiModelProperty("数据来源URL")
    private String dataUrl;
    /**
     * 是否可见
     */
    @ApiModelProperty("是否可见")
    private Integer isVisible;
    /**
     * 是否必填
     */
    @ApiModelProperty("是否必填")
    private Integer isRequired;
    /**
     * 有效标志
     */
    @ApiModelProperty("有效标志")
    private String validSign;
    /**
     * 是否系统字段
     */
    @ApiModelProperty("是否系统字段")
    private String isSys;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(Integer fieldSort) {
        this.fieldSort = fieldSort;
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

    public String getFieldArea() {
        return fieldArea;
    }

    public void setFieldArea(String fieldArea) {
        this.fieldArea = fieldArea;
    }

    public String getFieldDefVal() {
        return fieldDefVal;
    }

    public void setFieldDefVal(String fieldDefVal) {
        this.fieldDefVal = fieldDefVal;
    }

    public String getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(String fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataGroup() {
        return dataGroup;
    }

    public void setDataGroup(String dataGroup) {
        this.dataGroup = dataGroup;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public Integer getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Integer isVisible) {
        this.isVisible = isVisible;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getIsSys() {
        return isSys;
    }

    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }
}
