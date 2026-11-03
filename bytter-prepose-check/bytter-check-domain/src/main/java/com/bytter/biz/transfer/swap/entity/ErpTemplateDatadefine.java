package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
/**
 * Created by zhouj on 2018/5/18.
 */
@Entity
@Table(name = "ERP_TEMPLATE_DATADEFINE", schema = "")
public class ErpTemplateDatadefine extends IdEntity implements java.io.Serializable{
    private String fieldCode;
    private String fieldName;
    private Boolean isTemplateDtl;
    private Integer orderNum;
    @Column(name = "FIELD_CODE", nullable = true, length = 50)
    public String getFieldCode() {
        return fieldCode;
    }
    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }
    @Column(name = "FIELD_NAME", nullable = true, length = 100)
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    @Column(name = "IS_TEMPLATE_DTL", nullable = true, precision = 0)
    public Boolean getTemplateDtl() {
        return isTemplateDtl;
    }
    public void setTemplateDtl(Boolean templateDtl) {
        isTemplateDtl = templateDtl;
    }
    @Column(name = "ORDER_NUM")
    public Integer getOrderNum() {
        return orderNum;
    }
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
