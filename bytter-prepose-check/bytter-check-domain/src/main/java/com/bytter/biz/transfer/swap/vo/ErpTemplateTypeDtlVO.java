package com.bytter.biz.transfer.swap.vo;
import com.bytter.core.annotation.Dict;
/**
 * Created by zhouj on 2018/5/23.
 */
public class ErpTemplateTypeDtlVO {
    private String id;
    private String typeId;
    private String fieldCode;
    private String fieldName;
    private String showName;
    @Dict(dictGroup = "sf_yn", inject = "showStr")
    private Integer isShow;
    private String showStr;
    @Dict(dictGroup = "sf_yn", inject = "notNullStr")
    private Integer isNotNull;
    private String notNullStr;
    private String sourceCode;
    private String sourceName;
    private Integer isTemplateDtl;
    @Dict(dictGroup = "trans.inputType", inject = "inputTypeName")
    private String inputTypeCode;
    private String inputTypeName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
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
    public String getShowName() {
        return showName;
    }
    public void setShowName(String showName) {
        this.showName = showName;
    }
    public Integer getIsShow() {
        return isShow;
    }
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
    public String getShowStr() {
        return showStr;
    }
    public void setShowStr(String showStr) {
        this.showStr = showStr;
    }
    public Integer getIsNotNull() {
        return isNotNull;
    }
    public void setIsNotNull(Integer isNotNull) {
        this.isNotNull = isNotNull;
    }
    public String getNotNullStr() {
        return notNullStr;
    }
    public void setNotNullStr(String notNullStr) {
        this.notNullStr = notNullStr;
    }
    public String getSourceCode() {
        return sourceCode;
    }
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
    public String getSourceName() {
        return sourceName;
    }
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public Integer getIsTemplateDtl() {
        return isTemplateDtl;
    }
    public void setIsTemplateDtl(Integer isTemplateDtl) {
        this.isTemplateDtl = isTemplateDtl;
    }
    public String getInputTypeCode() {
        return inputTypeCode;
    }
    public void setInputTypeCode(String inputTypeCode) {
        this.inputTypeCode = inputTypeCode;
    }
    public String getInputTypeName() {
        return inputTypeName;
    }
    public void setInputTypeName(String inputTypeName) {
        this.inputTypeName = inputTypeName;
    }
}
