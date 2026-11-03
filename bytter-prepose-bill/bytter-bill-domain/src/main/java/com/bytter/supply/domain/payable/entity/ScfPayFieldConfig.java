package com.bytter.supply.domain.payable.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ScfPayFieldConfig
 * @Description 应付债权字段预设表
 */
@Entity
@Table(name = "SCF_PAY_FIELD_CONFIG", schema = "")
@SuppressWarnings("serial")
public class ScfPayFieldConfig extends IdEntity implements Serializable {
    /**
     * 默认排序
     */
    private Integer fieldSort;
    /**
     * 字段编码
     */
    private String fieldCode;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 字段所属区域
     */
    private String fieldArea;
    /**
     * 字段预设默认值
     */
    private String fieldDefVal;
    /**
     * 字段预设长度限制
     */
    private Integer fieldLength;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 字典组
     */
    private String dataGroup;
    /**
     * 数据来源URL
     */
    private String dataUrl;
    /**
     * 是否限制长度
     */
    private Integer isLimitLength;
    /**
     * 是否可见
     */
    private Integer isVisible;
    /**
     * 是否必填
     */
    private Integer isRequired;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 是否系统字段
     */
    private String isSys;


    @Column(name = "IS_LIMIT_LENGTH")
    public Integer getIsLimitLength() {
        return isLimitLength;
    }

    public void setIsLimitLength(Integer isLimitLength) {
        this.isLimitLength = isLimitLength;
    }

    @Column(name = "FIELD_SORT")
    public Integer getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(Integer fieldSort) {
        this.fieldSort = fieldSort;
    }

    @Column(name = "FIELD_CODE")
    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    @Column(name = "FIELD_NAME")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(name = "FIELD_TYPE")
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Column(name = "FIELD_AREA")
    public String getFieldArea() {
        return fieldArea;
    }

    public void setFieldArea(String fieldArea) {
        this.fieldArea = fieldArea;
    }

    @Column(name = "FIELD_DEF_VAL")
    public String getFieldDefVal() {
        return fieldDefVal;
    }

    public void setFieldDefVal(String fieldDefVal) {
        this.fieldDefVal = fieldDefVal;
    }

    @Column(name = "FIELD_LENGTH")
    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    @Column(name = "DATA_TYPE")
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Column(name = "DATA_GROUP")
    public String getDataGroup() {
        return dataGroup;
    }

    public void setDataGroup(String dataGroup) {
        this.dataGroup = dataGroup;
    }

    @Column(name = "DATA_URL")
    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    @Column(name = "IS_VISIBLE")
    public Integer getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Integer isVisible) {
        this.isVisible = isVisible;
    }

    @Column(name = "IS_REQUIRED")
    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    @Column(name = "IS_SYS")
    public String getIsSys() {
        return isSys;
    }

    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }
}
