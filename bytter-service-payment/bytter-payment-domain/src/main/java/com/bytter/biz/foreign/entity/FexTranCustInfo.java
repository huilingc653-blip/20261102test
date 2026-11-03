package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Title: Entity
 * @Description: 交易管理自定义信息表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_TRAN_CUST_INFO", schema = "")
@SuppressWarnings("serial")
public class FexTranCustInfo extends IdEntity implements java.io.Serializable {

    /**
     * 主表id
     */
    private String busId;

    /**
     * 自定义字段表id
     */
    private String fieldId;

    /**
     * 字段值
     */
    private String fieldVal;

    @Column(name ="BUS_ID")
    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    @Column(name ="FIELD_ID")
    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    @Column(name ="FIELD_VAL")
    public String getFieldVal() {
        return fieldVal;
    }

    public void setFieldVal(String fieldVal) {
        this.fieldVal = fieldVal;
    }
}
