package com.bytter.system.api.dto;

import java.io.Serializable;

public class OperationAreaDTO implements Serializable {
    private String id;
    private String operationAreaCode;
    private String operationAreaName;
    private String provinceId;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationAreaCode() {
        return operationAreaCode;
    }

    public void setOperationAreaCode(String operationAreaCode) {
        this.operationAreaCode = operationAreaCode;
    }

    public String getOperationAreaName() {
        return operationAreaName;
    }

    public void setOperationAreaName(String operationAreaName) {
        this.operationAreaName = operationAreaName;
    }

    public OperationAreaDTO() {
    }

    public OperationAreaDTO(String id, String operationAreaCode, String operationAreaName) {
        this.id = id;
        this.operationAreaCode = operationAreaCode;
        this.operationAreaName = operationAreaName;
    }
}
