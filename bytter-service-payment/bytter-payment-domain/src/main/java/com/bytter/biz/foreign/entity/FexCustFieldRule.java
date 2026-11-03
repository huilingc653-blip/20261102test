package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 自定义字段维护
 * @author zhengxn
 * @date 2020-02-17 14:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_CUST_FIELD_RULE", schema = "")
@SuppressWarnings("serial")
public class FexCustFieldRule extends IdEntity implements java.io.Serializable {
    /**字段编码*/
    private String fieldCode;
    /**字段名称*/
    private String fieldName;
    /**字段描述*/
    private String fieldDesc;
    /**字段类型 1-文本、2-数字、3-日期、4-时间、5-日期-时间*/
    private String fieldType;
    /**应用范围1 0-交易对手、1-交易*/
    private String appRange1;
    /**应用范围2
     应用范围1=1时，1-交易时：包含1-外汇交易（拆分为7-外汇即期交易、8-外汇远期交易、9-外汇掉期交易）、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
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
    /**有效标志 1-是 0-否*/
    private String validSign;
    /**创建时间*/
    private Date createDate;
    /**创建人*/
    private String createBy;
    /**更新时间*/
    private Date updateDate;
    /**更新人*/
    private String updateBy;
    /**字典组代码*/
    private String dictGroup;
    /**
     * 银行标签
     */
    private String labelBank;

    @Column(name ="FIELD_CODE")
    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    @Column(name ="FIELD_NAME")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(name ="FIELD_DESC")
    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    @Column(name ="APP_RANGE_1")
    public String getAppRange1() {
        return appRange1;
    }

    public void setAppRange1(String appRange1) {
        this.appRange1 = appRange1;
    }
    
    @Column(name ="APP_RANGE_2")
    public String getAppRange2() {
        return appRange2;
    }

    public void setAppRange2(String appRange2) {
        this.appRange2 = appRange2;
    }

    @Column(name ="CONTROL_RULE")
    public String getControlRule() {
        return controlRule;
    }

    public void setControlRule(String controlRule) {
        this.controlRule = controlRule;
    }

    @Column(name ="PRESET_VAL")
    public String getPresetVal() {
        return presetVal;
    }

    public void setPresetVal(String presetVal) {
        this.presetVal = presetVal;
    }

    @Column(name ="DECIMAL_DIGIT")
    public BigDecimal getDecimalDigit() {
        return decimalDigit;
    }

    public void setDecimalDigit(BigDecimal decimalDigit) {
        this.decimalDigit = decimalDigit;
    }

    @Column(name ="MIN_VAL")
    public BigDecimal getMinVal() {
        return minVal;
    }

    public void setMinVal(BigDecimal minVal) {
        this.minVal = minVal;
    }

    @Column(name ="MAXT_VAL")
    public BigDecimal getMaxtVal() {
        return maxtVal;
    }

    public void setMaxtVal(BigDecimal maxtVal) {
        this.maxtVal = maxtVal;
    }

    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name="VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="FIELD_TYPE")
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    @Column(name ="DICT_GROUP")
    public String getDictGroup() {
        return dictGroup;
    }
    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }

    @Column(name ="LABEL_BANK")
    public String getLabelBank() {
        return labelBank;
    }

    public void setLabelBank(String labelBank) {
        this.labelBank = labelBank;
    }
}
