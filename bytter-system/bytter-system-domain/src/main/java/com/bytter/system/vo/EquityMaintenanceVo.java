package com.bytter.system.vo;

import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ExcelTarget("EquityMaintenanceVo")
@SuppressWarnings("serial")
//@ExcelTarget("OperationAreaVo")
@Data
public class EquityMaintenanceVo extends ExcelModel implements Serializable, IBusinessKey  {

    private String id;

    @Excel(name = "*股权编码")
    @NotNull(message = "股权编码")
    private String equityCode;
    @Excel(name = "*股权名称")
    @NotNull(message = "股权名称")
    private String equityName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquityCode() {
        return equityCode;
    }

    public void setEquityCode(String equityCode) {
        this.equityCode = equityCode;
    }

    public String getEquityName() {
        return equityName;
    }

    public void setEquityName(String equityName) {
        this.equityName = equityName;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }

    @Override
    public String getBeanName() {
        return "equityMaintenanceService";
    }
}
