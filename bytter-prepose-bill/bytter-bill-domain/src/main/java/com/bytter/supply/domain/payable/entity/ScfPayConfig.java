package com.bytter.supply.domain.payable.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName ScfPayConfig
 * @Description 应付债权页面字段配置表
 */
@Entity
@Table(name = "SCF_PAY_CONFIG", schema = "")
@SuppressWarnings("serial")
public class ScfPayConfig extends IdEntity implements Serializable {
    /**
     * 模板配置版本ID
     */
    private String templateVersionId;
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 预设字段ID
     */
    private String fieldId;
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
     * 排序
     */
    private Integer fieldSort;
    /**
     * 长度限制
     */
    private Integer fieldLength;
    /**
     * 限制值
     */
    private String fieldLimitVal;
    /**
     * 默认值
     */
    private String fieldDefVal;
    /**
     * 枚举组
     */
    private String fieldDictGroup;
    /**
     * 限制枚举值
     */
    private String fieldLimitDict;
    /**
     * 字段所属区域
     */
    private String fieldArea;
    /**
     * 字段内容
     */
    private String fieldContent;
    /**
     * 字段类型
     */
    private String dataType;
    /**
     * 是否预设字段（1-预设字段;2-自定义字段）
     */
    private Integer isSys;
    /**
     * 是否可见
     */
    private Integer isVisible;
    /**
     * 是否必填
     */
    private Integer isRequired;
    /**
     * 是否控制长度
     */
    private Integer isLimitLength;
    /**
     * 是否限制枚举值
     */
    private Integer isLimitDict;
    /**
     * 是否删除
     */
    private Integer isDeleted;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;

    @Column(name = "TEMPLATE_VERSION_ID")
    public String getTemplateVersionId() {
        return templateVersionId;
    }

    public void setTemplateVersionId(String templateVersionId) {
        this.templateVersionId = templateVersionId;
    }

    @Column(name = "TEMPLATE_ID")
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Column(name = "FIELD_ID")
    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
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

    @Column(name = "FIELD_SORT")
    public Integer getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(Integer fieldSort) {
        this.fieldSort = fieldSort;
    }

    @Column(name = "FIELD_LENGTH")
    public Integer getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(Integer fieldLength) {
        this.fieldLength = fieldLength;
    }

    @Column(name = "FIELD_LIMIT_VAL")
    public String getFieldLimitVal() {
        return fieldLimitVal;
    }

    public void setFieldLimitVal(String fieldLimitVal) {
        this.fieldLimitVal = fieldLimitVal;
    }

    @Column(name = "FIELD_DEF_VAL")
    public String getFieldDefVal() {
        return fieldDefVal;
    }

    public void setFieldDefVal(String fieldDefVal) {
        this.fieldDefVal = fieldDefVal;
    }

    @Column(name = "FIELD_DICT_GROUP")
    public String getFieldDictGroup() {
        return fieldDictGroup;
    }

    public void setFieldDictGroup(String fieldDictGroup) {
        this.fieldDictGroup = fieldDictGroup;
    }

    @Column(name = "FIELD_LIMIT_DICT")
    public String getFieldLimitDict() {
        return fieldLimitDict;
    }

    public void setFieldLimitDict(String fieldLimitDict) {
        this.fieldLimitDict = fieldLimitDict;
    }

    @Column(name = "FIELD_AREA")
    public String getFieldArea() {
        return fieldArea;
    }

    public void setFieldArea(String fieldArea) {
        this.fieldArea = fieldArea;
    }

    @Column(name = "FIELD_CONTENT")
    public String getFieldContent() {
        return fieldContent;
    }

    public void setFieldContent(String fieldContent) {
        this.fieldContent = fieldContent;
    }

    @Column(name = "DATA_TYPE")
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Column(name = "IS_SYS")
    public Integer getIsSys() {
        return isSys;
    }

    public void setIsSys(Integer isSys) {
        this.isSys = isSys;
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

    @Column(name = "IS_LIMIT_LENGTH")
    public Integer getIsLimitLength() {
        return isLimitLength;
    }

    public void setIsLimitLength(Integer isLimitLength) {
        this.isLimitLength = isLimitLength;
    }

    @Column(name = "IS_LIMIT_DICT")
    public Integer getIsLimitDict() {
        return isLimitDict;
    }

    public void setIsLimitDict(Integer isLimitDict) {
        this.isLimitDict = isLimitDict;
    }

    @Column(name = "IS_DELETED")
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Column(name = "STATUS")
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
