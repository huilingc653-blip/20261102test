package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zhous
 * @Description 银承开票占比目标维护VO
 * @date 2025/4/22 11:01
 */
@Data
public class AbEmitBillTargetProportionInfoVO extends ExcelModel implements java.io.Serializable {

    private String id;

    /**
     * 单位ID
     */
    @BtCompositeSearch(fieldName = "corp_id")
    private String corpId;
    /**
     * 单位编码
     */
    private String corpCode;
    /**
     * 单位名称
     */
    @Excel(name = "*单位名称", isImportField = "true")
    @NotNull(message = "单位名称不能为空")
    private String corpName;
    /**
     * 当前月份
     */
    @BtCompositeSearch(fieldName = "current_year")
    @Excel(name = "*年份", isImportField = "true")
    @NotNull(message = "年份不能为空")
    private String currentYear;
    /**
     * 目标占比
     */
    @Excel(name = "*目标(%)", isImportField = "true")
    @NotNull(message = "目标不能为空")
    @BtCompositeSearch(fieldName = "target_proportion")
    private BigDecimal targetProportion;
    /**
     * 仓库编码
     */
    private String unitStoreMaintenanceId;

    @Override
    public String getBeanName() {
        return "abEmitBillTargetProportionInfoService";
    }
}
