package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.utils.StringUtils;
import javax.persistence.*;
/**
 * Created by zhouj on 2018/5/18.
 */
@Entity
@Table(name = "ERP_TEMPLATE_TYPE_DTL", schema = "")
public class ErpTemplateTypeDtl extends IdEntity implements java.io.Serializable{
    private String typeId;
    private String fieldCode;
    private String fieldName;
    private String showName;
    private Boolean isShow;
    private Boolean isNotNull;
    private String sourceCode;
    private Boolean isTemplateDtl;
    private String inputTypeCode;
    private Integer orderNum;
    @Basic
    @Column(name = "TYPE_ID", nullable = true, length = 32)
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    @Basic
    @Column(name = "FIELD_CODE", nullable = true, length = 32)
    public String getFieldCode() {
        return fieldCode;
    }
    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }
    @Basic
    @Column(name = "FIELD_NAME", nullable = true, length = 100)
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    @Basic
    @Column(name = "SHOW_NAME", nullable = true, length = 100)
    public String getShowName() {
        return showName;
    }
    public void setShowName(String showName) {
        this.showName = showName;
    }
    @Basic
    @Column(name = "IS_SHOW", nullable = true, precision = 0)
    public Boolean getShow() {
        return isShow;
    }
    public void setShow(Boolean show) {
        isShow = show;
    }
    @Basic
    @Column(name = "IS_NOT_NULL", nullable = true, precision = 0)
    public Boolean getNotNull() {
        return isNotNull;
    }
    public void setNotNull(Boolean notNull) {
        isNotNull = notNull;
    }
    @Basic
    @Column(name = "IS_TEMPLATE_DTL", nullable = true, precision = 0)
    public Boolean getTemplateDtl() {
        return isTemplateDtl;
    }
    public void setTemplateDtl(Boolean templateDtl) {
        isTemplateDtl = templateDtl;
    }
    @Basic
    @Column(name = "SOURCE_CODE", nullable = true, length = 32)
    public String getSourceCode() {
        return sourceCode;
    }
    public void setSourceCode(String sourceCode) {
        this.sourceCode = StringUtils.isBlank(sourceCode)?"":sourceCode.trim();
    }
    @Basic
    @Column(name = "INPUT_TYPE_CODE", nullable = true, length = 32)
    public String getInputTypeCode() {
        return inputTypeCode;
    }
    public void setInputTypeCode(String inputTypeCode) {
        this.inputTypeCode = inputTypeCode;
    }
    @Column(name = "ORDER_NUM")
    public Integer getOrderNum() {
        return orderNum;
    }
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
