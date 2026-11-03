package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 租户交易自定义信息实体。
 */
@Entity
@Table(name = "FEX_CUST_INFO_TENANT", schema = "")
@SuppressWarnings("serial")
public class FexCustInfoTenant extends IdEntity implements java.io.Serializable {

    /** 业务主表ID。 */
    private String busId;
    /** 自定义字段规则ID。 */
    private String fieldId;
    /** 字段名称（快照）。 */
    private String fieldName;
    /** 字段值。 */
    private String fieldVal;
    /** 租户ID。 */
    private String tenantId;

    @Column(name = "BUS_ID")
    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    @Column(name = "FIELD_ID")
    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    @Column(name = "FIELD_NAME")
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Column(name = "FIELD_VAL")
    public String getFieldVal() {
        return fieldVal;
    }

    public void setFieldVal(String fieldVal) {
        this.fieldVal = fieldVal;
    }

    @Column(name = "TENANT_ID")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
