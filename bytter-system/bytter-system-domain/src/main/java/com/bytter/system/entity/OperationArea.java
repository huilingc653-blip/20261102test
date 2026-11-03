package com.bytter.system.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SYS_OPERATION_AREA")
public class OperationArea extends IdEntity implements Serializable {
    private String operationAreaCode;
    private String operationAreaName;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private String STATUS;
    private String provinceId;

    @Column(name = "PROVINCE_ID")
    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    @Column(name = "OPERATION_AREA_CODE", length = 64)
    public String getOperationAreaCode() {
        return operationAreaCode;
    }

    public void setOperationAreaCode(String operationAreaCode) {
        this.operationAreaCode = operationAreaCode;
    }

    @Column(name = "OPERATION_AREA_NAME", length = 128)
    public String getOperationAreaName() {
        return operationAreaName;
    }

    public void setOperationAreaName(String operationAreaName) {
        this.operationAreaName = operationAreaName;
    }

    @Column(name = "CREATE_TIME", updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "CREATE_BY", length = 32, updatable = false)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "UPDATE_BY", length = 32)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "STATUS", length = 2)
    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}
