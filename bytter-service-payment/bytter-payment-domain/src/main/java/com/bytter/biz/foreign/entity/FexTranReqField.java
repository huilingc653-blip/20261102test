package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;




/**
 * @Title: Entity
 * @Description: 交易请求字段配置表
 * @author wuxc
 * @date 2026-05-11
 */
@Entity
@Table(name = "fex_tran_req_field")
public class FexTranReqField extends IdEntity implements Serializable {
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

    @Column(name = "field_sort")
    public int getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(int fieldSort) {
        this.fieldSort = fieldSort;
    }

    @Column(name = "field_code")
    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    @Column(name = "field_name")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(name = "field_type")
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Column(name = "data_type")
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Column(name = "data_group")
    public String getDataGroup() {
        return dataGroup;
    }

    public void setDataGroup(String dataGroup) {
        this.dataGroup = dataGroup;
    }

    @Column(name = "data_class")
    public String getDataClass() {
        return dataClass;
    }

    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
    }

    @Column(name = "data_url")
    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    @Column(name = "data_callback")
    public String getDataCallback() {
        return dataCallback;
    }

    public void setDataCallback(String dataCallback) {
        this.dataCallback = dataCallback;
    }

    @Column(name = "FIELD_AREA")
    public String getFieldArea() {
        return fieldArea;
    }

    public void setFieldArea(String fieldArea) {
        this.fieldArea = fieldArea;
    }

    @Column(name = "FIELD_PARENT_CODE")
    public String getFieldParentCode() {
        return fieldParentCode;
    }

    public void setFieldParentCode(String fieldParentCode) {
        this.fieldParentCode = fieldParentCode;
    }

    @Column(name = "valid_sign")
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    @Column(name = "is_sys")
    public String getIsSys() {
        return isSys;
    }

    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }

    @Column(name = "data_beforeinit")
    public String getDataBeforeinit() {
        return dataBeforeinit;
    }

    public void setDataBeforeinit(String dataBeforeinit) {
        this.dataBeforeinit = dataBeforeinit;
    }

    @Column(name = "data_valuefield")
    public String getDataValuefield() {
        return dataValuefield;
    }

    public void setDataValuefield(String dataValuefield) {
        this.dataValuefield = dataValuefield;
    }

    @Column(name = "data_textfield")
    public String getDataTextfield() {
        return dataTextfield;
    }

    public void setDataTextfield(String dataTextfield) {
        this.dataTextfield = dataTextfield;
    }
}