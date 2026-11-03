package com.bytter.supply.domain.payable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel("应付债权页面字段配置VO")
public class ScfPayConfigVO implements Serializable {
    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 模板配置版本ID
     */
    @BtCompositeSearch(fieldName = "c.TEMPLATE_VERSION_ID")
    @ApiModelProperty("模板配置版本ID")
    private String templateVersionId;
    /**
     * 模板ID
     */
    @BtCompositeSearch(fieldName = "c.TEMPLATE_ID")
    @ApiModelProperty("模板ID")
    private String templateId;
    /**
     * 预设字段ID
     */
    @ApiModelProperty("预设字段ID")
    private String fieldId;
    /**
     * 字段编码
     */
    @ApiModelProperty("字段编码")
    private String fieldCode;
    /**
     * 字段名称
     */
    @ApiModelProperty("字段名称")
    private String fieldName;
    @ApiModelProperty("字段类型")
    private String fieldType;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer fieldSort;
    /**
     * 长度限制
     */
    @ApiModelProperty("长度限制")
    private Integer fieldLength;
    /**
     * 限制值
     */
    @ApiModelProperty("限制值")
    private String fieldLimitVal;
    /**
     * 默认值
     */
    @ApiModelProperty("默认值")
    private String fieldDefVal;
    /**
     * 枚举组
     */
    @ApiModelProperty("枚举组")
    private String fieldDictGroup;
    /**
     * 限制枚举值
     */
    @ApiModelProperty("限制枚举值")
    private String fieldLimitDict;
    /**
     * 字段所属区域
     */
    @ApiModelProperty("字段所属区域")
    private String fieldArea;
    /**
     * 字段内容
     */
    @ApiModelProperty("字段内容")
    private String fieldContent;
    /**
     * 字段类型
     */
    @ApiModelProperty("数据类型")
    private String dataType;
    /**
     * 是否预设字段（1-预设字段;2-自定义字段）
     */
    @ApiModelProperty("是否预设字段（1-预设字段;2-自定义字段）")
    private Integer isSys;
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
     * 是否控制长度
     */
    @ApiModelProperty("是否控制长度")
    private Integer isLimitLength;
    /**
     * 是否限制枚举值
     */
    @ApiModelProperty("是否限制枚举值")
    private Integer isLimitDict;
    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Integer isDeleted;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private String status;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createDate;
    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateBy;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateDate;

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateVersionId() {
        return templateVersionId;
    }

    public void setTemplateVersionId(String templateVersionId) {
        this.templateVersionId = templateVersionId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public Integer getFieldSort() {
        return fieldSort;
    }

    public void setFieldSort(Integer fieldSort) {
        this.fieldSort = fieldSort;
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

    public String getFieldLimitDict() {
        return fieldLimitDict;
    }

    public void setFieldLimitDict(String fieldLimitDict) {
        this.fieldLimitDict = fieldLimitDict;
    }

    public String getFieldArea() {
        return fieldArea;
    }

    public void setFieldArea(String fieldArea) {
        this.fieldArea = fieldArea;
    }

    public String getFieldContent() {
        return fieldContent;
    }

    public void setFieldContent(String fieldContent) {
        this.fieldContent = fieldContent;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getIsSys() {
        return isSys;
    }

    public void setIsSys(Integer isSys) {
        this.isSys = isSys;
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

    public Integer getIsLimitLength() {
        return isLimitLength;
    }

    public void setIsLimitLength(Integer isLimitLength) {
        this.isLimitLength = isLimitLength;
    }

    public Integer getIsLimitDict() {
        return isLimitDict;
    }

    public void setIsLimitDict(Integer isLimitDict) {
        this.isLimitDict = isLimitDict;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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
}
