package com.bytter.core.dict;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * 字段信息包装类
 */
@Data
public   class FieldInfo {
    private Field field;
    private Field injectField;
    private String dictGroup;
    private String injectFieldName;
    private String orgField;
    public   FieldInfo(Field field, Field injectField, String dictGroup, String injectFieldName,String orgField) {
        this.field = field;
        this.injectField = injectField;
        this.dictGroup = dictGroup;
        this.injectFieldName = injectFieldName;
        this.orgField=orgField;
    }
}
