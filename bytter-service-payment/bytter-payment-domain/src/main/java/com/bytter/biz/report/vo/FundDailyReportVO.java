package com.bytter.biz.report.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 资金日报表
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/9/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundDailyReportVO implements Serializable {
    private Date queryDate;
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
    private BigDecimal minRate;
    private BigDecimal avgRate;
    private BigDecimal beforeBal;
    private BigDecimal currentBal;
    private BigDecimal availBal;
    private BigDecimal frzBal;
    private BigDecimal choMoney;
    /**报表是否显示字段 1显示，0不显示*/
    private String reportDisplay;

}
