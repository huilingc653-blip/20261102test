package com.bytter.biz.foreign.vo;

import com.bytter.check.vo.IBaseDataInject;
import com.bytter.core.annotation.BtCompositeSearch;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 租户自定义字段规则VO。
 */
public class FexCustFieldRuleTenantVO implements Serializable, IBaseDataInject {

    private String id;
    /**字段编码*/
    @BtCompositeSearch(fieldName = "f.FIELD_CODE")
    private String fieldCode;
    /**字段名称*/
    @BtCompositeSearch(fieldName = "f.FIELD_NAME")
    private String fieldName;
    /**字段描述*/
    @BtCompositeSearch(fieldName = "f.FIELD_DESC")
    private String fieldDesc;
    /**应用范围1 0-交易对手、1-交易*/
    @BtCompositeSearch(fieldName = "f.APP_RANGE_1")
    private String appRange1;
    /**应用范围2
     应用范围1=1时，1-交易时：包含1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    @BtCompositeSearch(fieldName = "f.APP_RANGE_2")
    private String appRange2;
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
    /**币种*/
    private String curId;
    private String curName;
    private String curEnglishCode;
    /**有效标志 1-是 0-否*/
    @BtCompositeSearch(fieldName = "f.VALID_SIGN")
    private String validSign;
    /**创建时间*/
    private Date createDate;
    /**创建人*/
    private String createBy;
    /**更新时间*/
    private Date updateDate;
    /**更新人*/
    private String updateBy;
    /**字段类型 1-文本、2-数字、3-日期、4-时间、5-日期-时间*/
    @BtCompositeSearch(fieldName = "f.FIELD_TYPE")
    private String fieldType;
    /**规则1*/
    private String Rule1;
    /**规则2*/
    private String Rule2;
    /**规则3*/
    private String Rule3;
    /**规则4*/
    private String Rule4;
    /**对手*/
    private String cotpGroup;
    /**字典组代码*/
    private String dictGroup;

    /**
     * 银行标签
     */
    private String labelBank;


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

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getAppRange1() {
        return appRange1;
    }

    public void setAppRange1(String appRange1) {
        this.appRange1 = appRange1;
    }

    public String getAppRange2() {
        return appRange2;
    }

    public void setAppRange2(String appRange2) {
        this.appRange2 = appRange2;
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

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
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

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getRule1() {
        return Rule1;
    }

    public void setRule1(String rule1) {
        Rule1 = rule1;
    }

    public String getRule2() {
        return Rule2;
    }

    public void setRule2(String rule2) {
        Rule2 = rule2;
    }

    public String getRule3() {
        return Rule3;
    }

    public void setRule3(String rule3) {
        Rule3 = rule3;
    }

    public String getRule4() {
        return Rule4;
    }

    public void setRule4(String rule4) {
        Rule4 = rule4;
    }

    public String getCotpGroup() {
        return cotpGroup;
    }

    public void setCotpGroup(String cotpGroup) {
        this.cotpGroup = cotpGroup;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurEnglishCode() {
        return curEnglishCode;
    }

    public void setCurEnglishCode(String curEnglishCode) {
        this.curEnglishCode = curEnglishCode;
    }

    public String getDictGroup() {
        return dictGroup;
    }

    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }

    public String getLabelBank() {
        return labelBank;
    }

    public void setLabelBank(String labelBank) {
        this.labelBank = labelBank;
    }
}
