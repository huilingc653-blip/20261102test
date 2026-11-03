package com.bytter.biz.erp.entity;

import com.bytter.core.base.entity.BaseEntity;
import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "bis_server_param", schema = "")
public class BisServerParam extends IdEntity implements Serializable {
    private String paramCode;
    private String paramValue;
    private String description;
    private String paramType;

    @Column(name ="PARAM_CODE")
    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    @Column(name ="PARAM_VALUE")
    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Column(name ="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name ="PARAM_TYPE")
    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }
}
