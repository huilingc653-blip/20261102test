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
@ExcelTarget("OperationAreaVo")
@Data
public class OperationAreaVo extends ExcelModel implements Serializable, IBusinessKey {

    private java.lang.String id;

    @Excel(name = "*营运区名称")
    @NotNull(message = "营运区名称")
    @BtCompositeSearch(fieldName = "operation_Area_Name")
    private String operationAreaName;
    @Excel(name = "*营运区编码")
    @NotNull(message = "营运区编码")
    @BtCompositeSearch(fieldName = "operation_Area_Code")
    private String operationAreaCode;

    private String provinceId;

    @Excel(name = "所属省区编码")
    private String provinceCode;

    @Excel(name = "所属省区名称")
    private String provinceName;

    @BtCompositeSearch(fieldName = "status")
    private String status;

    private String statusName;

    public String getStatusName() {
        return StringUtils.isNotBlank(this.status) && "1".equals(this.status) ? "启用" : "禁用";
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
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

    public String getOperationAreaName() {
        return operationAreaName;
    }

    public void setOperationAreaName(String operationAreaName) {
        this.operationAreaName = operationAreaName;
    }

    public String getOperationAreaCode() {
        return operationAreaCode;
    }

    public void setOperationAreaCode(String operationAreaCode) {
        this.operationAreaCode = operationAreaCode;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }
    @Override
    public String getBeanName() {
        return "operationAreaService";
    }
}
