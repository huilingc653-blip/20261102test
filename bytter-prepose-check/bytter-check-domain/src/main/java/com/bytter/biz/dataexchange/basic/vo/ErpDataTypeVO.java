package com.bytter.biz.dataexchange.basic.vo;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.enums.CommonDictEnum;
import lombok.Data;
import java.io.Serializable;
/**
 * Created by zhangxb on 2017/12/13.
 */
@Data
public class ErpDataTypeVO implements Serializable {
    private String id;
    /**类型代码*/
    @BtCompositeSearch(fieldName = "code")
    private String code;
    /**类型名称*/
    @BtCompositeSearch(fieldName = "name")
    private String name;
    /**表名*/
    @BtCompositeSearch(fieldName = "tableName")
    private String tableName;
    /**CODE字段*/
    @BtCompositeSearch(fieldName = "keyField")
    private String keyField;
    /**NAME字段*/
    @BtCompositeSearch(fieldName = "valueField")
    private String valueField;
    /**条件*/
    private String queryCondition;
    /** 有效标志*/
    @Dict(dictGroup = "sf_yn", inject = "validSignName")
    private String validSign;
    private String validSignName;
    /**备注*/
    private String rmk;
    /**是否系统初始化*/
    private String isSystem;
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
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getIsSystem() {
        return isSystem;
    }
    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }
    public String getValidSignName() {
        if (ObjectUtil.isNotEmpty(validSign)){
            return CommonDictEnum.getDictValue("sf_yn",validSign+"");
        }
        return validSignName;
    }
    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
    @Override
    public String toString() {
        return "ErpDataTypeVO{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", tableName='" + tableName + '\'' +
                ", keyField='" + keyField + '\'' +
                ", valueField='" + valueField + '\'' +
                ", queryCondition='" + queryCondition + '\'' +
                ", validSign='" + validSign + '\'' +
                ", validSignName='" + validSignName + '\'' +
                ", rmk='" + rmk + '\'' +
                ", isSystem='" + isSystem + '\'' +
                '}';
    }
}
