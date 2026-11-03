package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "ERP_DATATYPE_FIELD", schema = "")
public class ErpDataTypeField extends IdEntity implements java.io.Serializable{
    private String type;
    private String fieldCode;
    private String fieldName;
    private String isShow;
    private String dataType;
    private String validSign;
    private java.lang.Integer  orderNum;
    private String dictGroup;
    @Column(name ="ORDERNUM",nullable=true,length=10)
    public java.lang.Integer getOrderNum() {
        return orderNum;
    }
    public void setOrderNum(java.lang.Integer orderNum) {
        this.orderNum = orderNum;
    }
    @Column(name = "TYPE", nullable = true, length = 50)
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "FIELD_CODE", nullable = true, length = 50)
    public String getFieldCode() {
        return fieldCode;
    }
    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }
    @Column(name = "FIELD_NAME", nullable = true, length = 50)
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    @Column(name = "IS_SHOW", nullable = true, length = 50)
    public String getIsShow() {
        return isShow;
    }
    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
    @Column(name = "DATA_TYPE", nullable = true, length = 50)
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    @Column(name = "VALID_SIGN", nullable = true, length = 50)
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "DICT_GROUP", nullable = true, length = 50)
    public String getDictGroup() {
        return dictGroup;
    }
    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }
}
