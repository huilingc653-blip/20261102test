package com.bytter.biz.foreign.vo;

/**
 * @Title: VO
 * @Description: 交易管理自定义信息表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
public class FexCashFlowCustInfoVO implements java.io.Serializable {

    private String id;
    /**自定义字段表id*/
    private String fieldId;
    /**字段值*/
    private String fieldVal;
    /**字段名称*/
    private String fieldName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldVal() {
        return fieldVal;
    }

    public void setFieldVal(String fieldVal) {
        this.fieldVal = fieldVal;
    }
}
