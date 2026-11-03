package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 租户自定义字段规则实体。
 */
@Entity
@Table(name = "FEX_CUST_FIELD_RULE_TENANT", schema = "")
@SuppressWarnings("serial")
public class FexCustFieldRuleTenant extends IdEntity implements java.io.Serializable {

    private String fieldCode;
    private String fieldName;
    private String fieldDesc;
    private String fieldType;
    private String appRange1;
    private String appRange2;
    private String controlRule;
    private String presetVal;
    private BigDecimal decimalDigit;
    private BigDecimal minVal;
    private BigDecimal maxtVal;
    private String curId;
    private String validSign;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
    private String dictGroup;
    private String labelBank;
    private String tenantId;

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

    @Column(name = "FIELD_DESC")
    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    @Column(name = "FIELD_TYPE")
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Column(name = "APP_RANGE_1")
    public String getAppRange1() {
        return appRange1;
    }

    public void setAppRange1(String appRange1) {
        this.appRange1 = appRange1;
    }

    @Column(name = "APP_RANGE_2")
    public String getAppRange2() {
        return appRange2;
    }

    public void setAppRange2(String appRange2) {
        this.appRange2 = appRange2;
    }

    @Column(name = "CONTROL_RULE")
    public String getControlRule() {
        return controlRule;
    }

    public void setControlRule(String controlRule) {
        this.controlRule = controlRule;
    }

    @Column(name = "PRESET_VAL")
    public String getPresetVal() {
        return presetVal;
    }

    public void setPresetVal(String presetVal) {
        this.presetVal = presetVal;
    }

    @Column(name = "DECIMAL_DIGIT")
    public BigDecimal getDecimalDigit() {
        return decimalDigit;
    }

    public void setDecimalDigit(BigDecimal decimalDigit) {
        this.decimalDigit = decimalDigit;
    }

    @Column(name = "MIN_VAL")
    public BigDecimal getMinVal() {
        return minVal;
    }

    public void setMinVal(BigDecimal minVal) {
        this.minVal = minVal;
    }

    @Column(name = "MAXT_VAL")
    public BigDecimal getMaxtVal() {
        return maxtVal;
    }

    public void setMaxtVal(BigDecimal maxtVal) {
        this.maxtVal = maxtVal;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "DICT_GROUP")
    public String getDictGroup() {
        return dictGroup;
    }

    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }

    @Column(name = "LABEL_BANK")
    public String getLabelBank() {
        return labelBank;
    }

    public void setLabelBank(String labelBank) {
        this.labelBank = labelBank;
    }

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
