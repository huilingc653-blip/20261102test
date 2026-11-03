package com.bytter.erp.dto;
/**
 * Created by zhouj on 2018/5/21.
 */
public class ErpDataTypeDTO implements java.io.Serializable{
    private String id;
    /**类型代码*/
    private String code;
    /**类型名称*/
    private String name;
    /**表名*/
    private String tableName;
    /**CODE字段*/
    private String keyField;
    /**NAME字段*/
    private String valueField;
    /**条件*/
    private String queryCondition;
    /**是否有效*/
    private String validSign;
    private String validSignName;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getKeyField() {
        return keyField;
    }
    public void setKeyField(String keyField) {
        this.keyField = keyField;
    }
    public String getValueField() {
        return valueField;
    }
    public void setValueField(String valueField) {
        this.valueField = valueField;
    }
    public String getQueryCondition() {
        return queryCondition;
    }
    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getValidSignName() {
        return validSignName;
    }

    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
}
