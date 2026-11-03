package com.bytter.biz.report.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description: 多级资金收支表
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/9/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeExpenditureReportVO implements Serializable {
    private String corpId;
    private String corpCode;
    private String corpName;
    private String parentId;
    private String parentIds;
    private Integer sort;
    private String provinceId;
    private String provinceName;
    /**管理单元Code*/
    private String cellCode;
    /**管理单元Name*/
    private String cellName;
    /**股权属性Code*/
    private String equityCode;
    /**股权属性Name*/
    private String equityName;
    private BigDecimal maxRate;
    private BigDecimal beginBal;
    private BigDecimal income;
    private BigDecimal expenditure;
    private BigDecimal endBal;
    private BigDecimal realEndBal;
    private BigDecimal diffMoney;
    private BigDecimal choMoney;
    private String remark;
    /**报表是否显示字段 1显示，0不显示*/
    private String reportDisplay;
    private String startDate;
    private String endDate;

}
