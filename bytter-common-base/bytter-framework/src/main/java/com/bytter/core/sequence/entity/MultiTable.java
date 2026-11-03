package com.bytter.core.sequence.entity;
/**
 * 多个表查询
 */
public class MultiTable {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 字段名
     */
    private String fieldName;
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public MultiTable(String tableName, String fieldName) {
        super();
        this.tableName = tableName;
        this.fieldName = fieldName;
    }
}
