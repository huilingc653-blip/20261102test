package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author luxy
 * @Title: FexSettleFillRuleAdd
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/2/18 11:14
 */
@Entity
@Table(name = "FEX_SETTLE_FILL_RULE_ADD")
public class FexSettleFillRuleAdd extends IdEntity implements java.io.Serializable {
    /**
     *规则id
     */
    private String ruleId;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段名称
     */
    private String fieldVal;

    @Column(name ="RULE_ID")
    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
    @Column(name ="FIELD_NAME")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    @Column(name ="FIELD_VAL")
    public String getFieldVal() {
        return fieldVal;
    }

    public void setFieldVal(String fieldVal) {
        this.fieldVal = fieldVal;
    }
}
