package com.bytter.system.api.dto;

import java.io.Serializable;

public class SystemProvinceDTO implements Serializable {
    private String id;
    private String provinceCode;
    private String provinceName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public SystemProvinceDTO() {
    }

    public SystemProvinceDTO(String id, String provinceCode, String provinceName) {
        this.id = id;
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
    }
}
