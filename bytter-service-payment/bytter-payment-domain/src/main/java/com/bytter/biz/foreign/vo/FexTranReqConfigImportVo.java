package com.bytter.biz.foreign.vo;

import com.bytter.core.bpm.IBusinessKey;
import com.bytter.check.vo.IBaseDataInject;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;

import java.io.Serializable;

public class FexTranReqConfigImportVo extends ExcelModel implements Serializable, IBaseDataInject, IBusinessKey {
    private static final long serialVersionUID = 1L;
    private String id;
    /** 模板配置版本ID */
    private String templateConfigVersionId;
    /** 版本号 */
    private String versionNo;
    /** 模板ID */
    private String templateId;
    /** 是否预设字段（1-预设字段；2-自定义字段） */
    private Integer isSys;
    /** 字段类型（NUMBER,MONEY,DATE,DICT,TEXT.CORP） */
    private String dataType;
    private String dataClass;
    private String fieldArea;
    private String fieldParentCode;
    @Excel(name = "排序")
    private Integer fieldSort;
    /** 自定义字段ID */
    private String fieldId;
    /** 字段编码 */
    private String fieldCode;
    /** 字段名称 */
    @Excel(name = "字段名称")
    private String fieldName;
    /** 是否启用 */
    private String isUse;
    /** 是否可见 */
    @Excel(name = "是否可见")
    private String isVisible;
    /** 是否必填 */
    @Excel(name = "是否必填")
    private String isRequired;
    /** 是否控制长度 */
    @Excel(name = "是否控制长度")
    private String isLength;
    /** 是否限制枚举值 */
    @Excel(name = "是否限定枚举值")
    private String isLimitedDict;
    /** 是否发送银行指令 */
    private String isExc;
    /** 长度限制 */
    @Excel(name = "长度限制")
    private Integer fieldLength;
    /** 限制值 */
    @Excel(name = "枚举值")
    private String fieldLimitVal;
    /** 默认值 */
    @Excel(name = "默认值")
    private String fieldDefVal;
    /**
     * 字典组
     */
    private String fieldDictGroup;
    /** 限制枚举值 */
    private String fieldLimitedDict;
    /** 状态 */
    private String status;

    public String getTemplateConfigVersionId() {
        return templateConfigVersionId;
    }

    public void setTemplateConfigVersionId(String templateConfigVersionId) {
        this.templateConfigVersionId = templateConfigVersionId;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Integer getIsSys() {
        return isSys;
    }

    public void setIsSys(Integer isSys) {
        this.isSys = isSys;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataClass() {
        return dataClass;
    }

    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
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

    public Integer getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(Integer fieldSort) {
        this.fieldSort = fieldSort;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
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

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(String isVisible) {
        this.isVisible = isVisible;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getIsLength() {
        return isLength;
    }

    public void setIsLength(String isLength) {
        this.isLength = isLength;
    }

    public String getIsLimitedDict() {
        return isLimitedDict;
    }

    public void setIsLimitedDict(String isLimitedDict) {
        this.isLimitedDict = isLimitedDict;
    }

    public String getIsExc() {
        return isExc;
    }

    public void setIsExc(String isExc) {
        this.isExc = isExc;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getFieldLimitVal() {
        return fieldLimitVal;
    }

    public void setFieldLimitVal(String fieldLimitVal) {
        this.fieldLimitVal = fieldLimitVal;
    }

    public String getFieldDefVal() {
        return fieldDefVal;
    }

    public void setFieldDefVal(String fieldDefVal) {
        this.fieldDefVal = fieldDefVal;
    }

    public String getFieldDictGroup() {
        return fieldDictGroup;
    }

    public void setFieldDictGroup(String fieldDictGroup) {
        this.fieldDictGroup = fieldDictGroup;
    }

    public String getFieldLimitedDict() {
        return fieldLimitedDict;
    }

    public void setFieldLimitedDict(String fieldLimitedDict) {
        this.fieldLimitedDict = fieldLimitedDict;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }

    @Override
    public String getBeanName() {
        return "fexTranReqConfigService";
    }
}
