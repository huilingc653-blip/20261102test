package com.bytter.supply.domain.payable.entity;

import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SCF_VOUCHER_CUSTOM_FIELD", schema = "")
@SuppressWarnings("serial")
public class ScfVoucherCustomField extends IdEntity implements Serializable {
    /**
     * 凭证id
     */
    private String voucherId;

    private String fieldId;
    /**
     * 自定义字段编码
     */
    private String fieldCode;
    /**
     * 自定义字段名称
     */
    private String fieldName;
    /**
     * 自定义字段类型
     */
    private String fieldType;
    /**
     * 自定义字段值
     */
    private String fieldValue;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;

    @Column(name = "VOUCHER_ID")
    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    @Column(name = "FIELD_ID")
    public String getFieldId() { return fieldId; }

    public void setFieldId(String fieldId) { this.fieldId = fieldId; }

    @Column(name = "FIELD_CODE")
    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    @Column(name = "FIELD_NAME")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(name = "FIELD_TYPE")
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Column(name = "FIELD_VALUE")
    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
