package com.bytter.system.api.dto;
/**
 * 多个表查询
 */
public class MultiTableDTO {
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
    public MultiTableDTO(String tableName, String fieldName) {
        super();
        this.tableName = tableName;
        this.fieldName = fieldName;
    }
}
