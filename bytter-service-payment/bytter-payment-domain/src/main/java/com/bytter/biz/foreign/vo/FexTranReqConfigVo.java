package com.bytter.biz.foreign.vo;

import com.bytter.check.vo.IBaseDataInject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FexTranReqConfigVo implements Serializable, IBaseDataInject {
    private static final long serialVersionUID = 1L;
    private String id;
    /** 模板配置版本ID */
    private String templateConfigVersionId;
    /** 版本号 */
    private String versionNo;
    /** 模板ID */
    private String templateId;
    private String templateNo;
    private String templateName;
    /** 是否预设字段（1-预设字段；2-自定义字段） */
    private Integer isSys;
    /** 字段类型（NUMBER,MONEY,DATE,DICT,TEXT.CORP） */
    private String dataType;
    private String dataClass;
    private String fieldArea;
    private String fieldParentCode;
    private Integer fieldSort;
    /** 自定义字段ID */
    private String fieldId;
    /** 字段编码 */
    private String fieldCode;
    /** 字段名称 */
    private String fieldName;
    /** 是否启用 */
    private Integer isUse;
    /** 是否可见 */
    private Integer isVisible;
    /** 是否必填 */
    private Integer isRequired;
    /** 是否控制长度 */
    private Integer isLength;
    /** 是否限制枚举值 */
    private Integer isLimitedDict;
    /** 是否发送银行指令 */
    private Integer isExc;
    /** 长度限制 */
    private Integer fieldLength;
    /** 限制值 */
    private String fieldLimitVal;
    /** 默认值 */
    private String fieldDefVal;
    /**
     * 字典组
     */
    private String fieldDictGroup;
    /** 限制枚举值 */
    private String fieldLimitedDict;
    /** 状态 */
    private String status;

    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;

    /**
     * 子节点列表
     */
    private List<FexTranReqConfigVo> children;

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

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
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

    public Integer getIsLength() {
        return isLength;
    }

    public void setIsLength(Integer isLength) {
        this.isLength = isLength;
    }

    public Integer getIsLimitedDict() {
        return isLimitedDict;
    }

    public void setIsLimitedDict(Integer isLimitedDict) {
        this.isLimitedDict = isLimitedDict;
    }

    public Integer getIsExc() {
        return isExc;
    }

    public void setIsExc(Integer isExc) {
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<FexTranReqConfigVo> getChildren() {
        return children;
    }

    public void setChildren(List<FexTranReqConfigVo> children) {
        this.children = children;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateNo() {
        return templateNo;
    }

    public void setTemplateNo(String templateNo) {
        this.templateNo = templateNo;
    }
}
