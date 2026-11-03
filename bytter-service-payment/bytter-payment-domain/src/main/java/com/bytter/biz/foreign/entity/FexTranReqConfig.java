package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 交易页面配置
 * @author wuxc
 * @date 2026-05-11
 */
@Entity
@Table(name = "fex_tran_req_config")
public class FexTranReqConfig extends IdEntity implements Serializable {
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
    private int fieldSort;
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

    @Column(name = "template_config_version_id")
    public String getTemplateConfigVersionId() {
        return templateConfigVersionId;
    }

    public void setTemplateConfigVersionId(String templateConfigVersionId) {
        this.templateConfigVersionId = templateConfigVersionId;
    }

    @Column(name = "version_no")
    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    @Column(name = "template_id")
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Column(name = "is_sys")
    public Integer getIsSys() {
        return isSys;
    }

    public void setIsSys(Integer isSys) {
        this.isSys = isSys;
    }

    @Column(name = "data_type")
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Column(name = "DATA_CLASS")
    public String getDataClass() {
        return dataClass;
    }

    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
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

    @Column(name = "field_sort")
    public int getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(int fieldSort) {
        this.fieldSort = fieldSort;
    }

    @Column(name = "field_id")
    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
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

    @Column(name = "is_use")
    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    @Column(name = "is_visible")
    public Integer getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Integer isVisible) {
        this.isVisible = isVisible;
    }

    @Column(name = "is_required")
    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    @Column(name = "is_length")
    public Integer getIsLength() {
        return isLength;
    }

    public void setIsLength(Integer isLength) {
        this.isLength = isLength;
    }

    @Column(name = "is_limited_dict")
    public Integer getIsLimitedDict() {
        return isLimitedDict;
    }

    public void setIsLimitedDict(Integer isLimitedDict) {
        this.isLimitedDict = isLimitedDict;
    }

    @Column(name = "is_exc")
    public Integer getIsExc() {
        return isExc;
    }

    public void setIsExc(Integer isExc) {
        this.isExc = isExc;
    }

    @Column(name = "field_length")
    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    @Column(name = "field_limit_val")
    public String getFieldLimitVal() {
        return fieldLimitVal;
    }

    public void setFieldLimitVal(String fieldLimitVal) {
        this.fieldLimitVal = fieldLimitVal;
    }

    @Column(name = "field_def_val")
    public String getFieldDefVal() {
        return fieldDefVal;
    }

    public void setFieldDefVal(String fieldDefVal) {
        this.fieldDefVal = fieldDefVal;
    }

    @Column(name = "field_dict_group")
    public String getFieldDictGroup() {
        return fieldDictGroup;
    }

    public void setFieldDictGroup(String fieldDictGroup) {
        this.fieldDictGroup = fieldDictGroup;
    }

    @Column(name = "field_limited_dict")
    public String getFieldLimitedDict() {
        return fieldLimitedDict;
    }

    public void setFieldLimitedDict(String fieldLimitedDict) {
        this.fieldLimitedDict = fieldLimitedDict;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
