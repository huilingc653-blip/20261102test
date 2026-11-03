package com.bytter.biz.foreign.vo;

import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;

public class FexTranReqFieldVo implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = 1L;

    private String id;
    private int fieldSort;
    /** 字段编码 */
    private String fieldCode;
    /** 字段名称 */
    private String fieldName;
    /** 字段类型 */
    private String fieldType;
    /** 数据类型 */
    private String dataType;
    /** 字典组 */
    private String dataGroup;
    /** 数据分类 */
    private String dataClass;
    /** 数据URL */
    private String dataUrl;
    /** 数据回调 */
    private String dataCallback;
    /** 数据区域 */
    private String fieldArea;
    private String fieldParentCode;
    /** 有效标识 */
    private String validSign;
    /** 是否系统预设 */
    private String isSys;
    /** 初始化前事件 */
    private String dataBeforeinit;
    /** 值字段 */
    private String dataValuefield;
    /** 文本字段 */
    private String dataTextfield;

    /**
     * 银行标签
     */
    private String labelBank;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(int fieldSort) {
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

    public String getDataClass() {
        return dataClass;
    }

    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getDataCallback() {
        return dataCallback;
    }

    public void setDataCallback(String dataCallback) {
        this.dataCallback = dataCallback;
    }

    public String getFieldArea() {
        return fieldArea;
    }

    public void setFieldArea(String fieldArea) {
        this.fieldArea = fieldArea;
    }

    public String getFieldParentCode() {
        return fieldParentCode;
    }

    public void setFieldParentCode(String fieldParentCode) {
        this.fieldParentCode = fieldParentCode;
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

    public String getDataBeforeinit() {
        return dataBeforeinit;
    }

    public void setDataBeforeinit(String dataBeforeinit) {
        this.dataBeforeinit = dataBeforeinit;
    }

    public String getDataValuefield() {
        return dataValuefield;
    }

    public void setDataValuefield(String dataValuefield) {
        this.dataValuefield = dataValuefield;
    }

    public String getDataTextfield() {
        return dataTextfield;
    }

    public void setDataTextfield(String dataTextfield) {
        this.dataTextfield = dataTextfield;
    }

    public String getLabelBank() {
        return labelBank;
    }

    public void setLabelBank(String labelBank) {
        this.labelBank = labelBank;
    }
}
