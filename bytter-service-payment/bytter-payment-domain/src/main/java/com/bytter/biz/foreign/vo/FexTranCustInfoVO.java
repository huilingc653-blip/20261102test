package com.bytter.biz.foreign.vo;

import java.math.BigDecimal;

/**
 * @Title: VO
 * @Description: 交易管理自定义信息表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
public class FexTranCustInfoVO implements java.io.Serializable {

    private String id;
    /**自定义字段表id*/
    private String fieldId;
    /**字段值*/
    private String fieldVal;
    /**字段编码*/
    private String fieldCode;
    /**字段名称*/
    private String fieldName;
    /**字段描述*/
    private String fieldDesc;
    /**录入控制规则*/
    private String controlRule;
    /**预设值*/
    private String presetVal;
    /**小数位数*/
    private BigDecimal decimalDigit;
    /**最小值*/
    private BigDecimal minVal;
    /**最大值*/
    private BigDecimal maxtVal;

    /**字段类型 1-文本、2-数字、3-日期、4-时间、5-日期-时间*/
    private String fieldType;

    private String labelBank;

    
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

    public String getControlRule() {
        return controlRule;
    }

    public void setControlRule(String controlRule) {
        this.controlRule = controlRule;
    }

    public String getPresetVal() {
        return presetVal;
    }

    public void setPresetVal(String presetVal) {
        this.presetVal = presetVal;
    }

    public BigDecimal getDecimalDigit() {
        return decimalDigit;
    }

    public void setDecimalDigit(BigDecimal decimalDigit) {
        this.decimalDigit = decimalDigit;
    }

    public BigDecimal getMinVal() {
        return minVal;
    }

    public void setMinVal(BigDecimal minVal) {
        this.minVal = minVal;
    }

    public BigDecimal getMaxtVal() {
        return maxtVal;
    }

    public void setMaxtVal(BigDecimal maxtVal) {
        this.maxtVal = maxtVal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldVal() {
        return fieldVal;
    }

    public void setFieldVal(String fieldVal) {
        this.fieldVal = fieldVal;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
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

    public String getLabelBank() {
        return labelBank;
    }

    public void setLabelBank(String labelBank) {
        this.labelBank = labelBank;
    }
}
