package com.bytter.system.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@SuppressWarnings("serial")
@ExcelTarget("SystemProvinceVo")
@Data
public class SystemProvinceVo extends ExcelModel implements Serializable, IBusinessKey {

    private String id;

    @Excel(name = "*省区名称")
    @NotNull(message = "省区名称")
    @BtCompositeSearch(fieldName = "province_name")
    private String provinceName;
    @Excel(name = "*省区编码")
    @NotNull(message = "省区编码")
    @BtCompositeSearch(fieldName = "province_code")
    private String provinceCode;

    @BtCompositeSearch(fieldName = "status")
    private String status;

    private String statusName;

    public String getStatusName() {
        return StringUtils.isNotBlank(status) && "1".equals(status) ? "启用" : "禁用";
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }
    @Override
    public String getBeanName() {
        return "systemProvinceService";
    }
}
