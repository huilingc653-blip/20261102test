package com.bytter.supply.domain.payable.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel("客户自定义字段配置VO")
public class ScfCustFieldConfigVO implements Serializable {
    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 字段名称
     */
    @BtCompositeSearch(fieldName = "c.FIELD_NAME")
    @ApiModelProperty("字段名称")
    private String fieldName;
    /**
     * 字段描述
     */
    @BtCompositeSearch(fieldName = "c.FIELD_DESC")
    @ApiModelProperty("字段描述")
    private String fieldDesc;
    /**
     * 类型
     */
    @ApiModelProperty("字段类型")
    private String fieldType;
    /**
     * 字典组
     */
    @ApiModelProperty("字典组")
    private String dictGroup;
    /**
     * 字段编码
     */
    @ApiModelProperty("字段编码")
    private String fieldCode;
    /**
     * 预设默认值
     */
    @ApiModelProperty("预设默认值")
    private String defaultVal;
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
     * 是否限制长度
     */
    @ApiModelProperty("是否限制长度")
    private Integer isLimitLength;
    /**
     * 长度限制值
     */
    @ApiModelProperty("长度限制值")
    private String fieldLength;
    /**
     * 有效标志
     */
    @ApiModelProperty("有效标志")
    private String validSign;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createDate;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateDate;
    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDictGroup() {
        return dictGroup;
    }

    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
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

    public String getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(String fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
